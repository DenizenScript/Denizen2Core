package com.denizenscript.denizen2core.utilities;

import java.util.*;

public class MathHelper {

    public final static int OP_COUNT = 8;

    public static class MathContext {
        public Stack<Double> stk;
        public HashMap<String, Tuple<Integer, Action<MathContext>>> functions;
    }

    public static int[] priority = new int[128];
    public static MathOp[] operations = new MathOp[128];
    public static Action[] operators = new Action[OP_COUNT]; // Java is stupid. Pretend we have <MathContext> on this...

    public static HashMap<String, Tuple<Integer, Action<MathContext>>> baseFunctions = new HashMap<>();

    public static void FUNCTION_SIN(MathContext m) {
        double a = m.stk.pop();
        m.stk.push(Math.sin(a));
    }

    public static void OPERATOR_ADD(MathContext m) {
        double b = m.stk.pop();
        double a = m.stk.pop();
        m.stk.push(a + b);
    }

    public static void OPERATOR_SUB(MathContext m) {
        double b = m.stk.pop();
        double a = m.stk.pop();
        m.stk.push(a - b);
    }

    public static void OPERATOR_MUL(MathContext m) {
        double b = m.stk.pop();
        double a = m.stk.pop();
        m.stk.push(a * b);
    }

    public static void OPERATOR_DIV(MathContext m) {
        double b = m.stk.pop();
        double a = m.stk.pop();
        m.stk.push(a / b);
    }

    public static void OPERATOR_MOD(MathContext m) {
        double b = m.stk.pop();
        double a = m.stk.pop();
        m.stk.push(a % b);
    }

    public static void OPERATOR_EXP(MathContext m) {
        double b = m.stk.pop();
        double a = m.stk.pop();
        m.stk.push(Math.pow(a, b));
    }

    static {
        priority['('] = 10;
        priority['+'] = 50;
        priority['-'] = 50;
        priority['*'] = 60;
        priority['/'] = 60;
        priority['%'] = 60;
        priority['^'] = 70;
        priority['F'] = 100;
        operations['+'] = MathOp.ADD;
        operations['-'] = MathOp.SUB;
        operations['*'] = MathOp.MUL;
        operations['/'] = MathOp.DIV;
        operations['%'] = MathOp.MOD;
        operations['^'] = MathOp.EXP;
        operators[MathOp.ADD.Value] = (Action<MathContext>) MathHelper::OPERATOR_ADD;
        operators[MathOp.SUB.Value] = (Action<MathContext>) MathHelper::OPERATOR_SUB;
        operators[MathOp.MUL.Value] = (Action<MathContext>) MathHelper::OPERATOR_MUL;
        operators[MathOp.DIV.Value] = (Action<MathContext>) MathHelper::OPERATOR_DIV;
        operators[MathOp.MOD.Value] = (Action<MathContext>) MathHelper::OPERATOR_MOD;
        operators[MathOp.EXP.Value] = (Action<MathContext>) MathHelper::OPERATOR_EXP;
        baseFunctions.put("sin", new Tuple<>(1, MathHelper::FUNCTION_SIN));
    }

    public static boolean isNumberSymbol(char inp) {
        return (inp >= '0' && inp <= '9') || inp == '.';
    }

    public static boolean isOperator(char inp) {
        return inp == '+' || inp == '-' || inp == '*' || inp == '/' || inp == '%' || inp == '^';
    }

    public static boolean isLetter(char inp) {
        return (inp >= 'a' && inp <= 'z') || inp == '_';
    }

    static MathContext calcInternal(List<MathOperation> mops,
                                    HashMap<String, Tuple<Integer, Action<MathContext>>> functions) {
        MathContext math = new MathContext();
        math.stk = new Stack<>();
        math.functions = functions;
        for (int i = 0; i < mops.size(); i++) {
            if (mops.get(i).opValue == MathOp.LDN) {
                math.stk.push(mops.get(i).numericValue);
            }
            else if (mops.get(i).opValue == MathOp.FNC) {
                functions.get(mops.get(i).functionValue).two.run(math);
            }
            else {
                ((Action<MathContext>) operators[mops.get(i).opValue.Value]).run(math);
            }
        }
        return math;
    }

    public static double calculate(List<MathOperation> mops,
                                   HashMap<String, Tuple<Integer, Action<MathContext>>> functions) {
        return calcInternal(mops, functions).stk.pop();
    }

    public static String verify(List<MathOperation> mops,
                                HashMap<String, Tuple<Integer, Action<MathContext>>> functions) {
        for (int i = 0; i < mops.size(); i++) {
            if (mops.get(i).opValue == MathOp.BAD) {
                return "BAD op call!";
            }
        }
        try {
            HashMap<String, Tuple<Integer, Action<MathContext>>> tfunc = new HashMap<>();
            for (Map.Entry<String, Tuple<Integer, Action<MathContext>>> func : functions.entrySet()) {
                tfunc.put(func.getKey(), new Tuple<>(func.getValue().one, (m) -> {
                for (int i = 0; i < func.getValue().one; i++) {
                    m.stk.pop();
                }
                m.stk.push(Double.valueOf(0)); // Ain't this one fun. IntelliJ warning here is wrong.
                }));
            }
            if (calcInternal(mops, tfunc).stk.size() != 1) {
                return "End stack sized incorrectly!";
            }
            return null;
        }
        catch (EmptyStackException e) {
            return "Unreasonable stack!";
        }
        catch (Exception e) {
            return "Error: " + e.getClass().getCanonicalName() + ": " + e.getMessage();
        }
    }

    public static List<MathOperation> parse(String input, StringBuilder err)
    {
        try {
            char[] inp = input.toLowerCase(Locale.US).replace(" ", "").toCharArray();
            Stack<MathWaiting> waiting = new Stack<>();
            List<MathOperation> result = new ArrayList<>();
            for (int i = 0; i < inp.length; i++) {
                if (inp[i] == '(') {
                    MathWaiting mw = new MathWaiting();
                    mw.type = '(';
                    waiting.push(mw);
                }
                else if ((inp[i] == '-' && (i - 1 < 0
                        || (isOperator(inp[i - 1])
                        || inp[i - 1] == '(' || inp[i - 1] == ')')))
                        || isNumberSymbol(inp[i])) {
                    StringBuilder snum = new StringBuilder(6);
                    snum.append(inp[i]);
                    while (i + 1 < inp.length && isNumberSymbol(inp[i + 1])) {
                        snum.append(inp[++i]);
                    }
                    MathOperation mo = new MathOperation();
                    mo.numericValue = Double.parseDouble(snum.toString());
                    mo.opValue = MathOp.LDN;
                    result.add(mo);
                }
                else if (isLetter(inp[i])) {
                    StringBuilder sfnc = new StringBuilder(6);
                    sfnc.append(inp[i]);
                    while (inp[i + 1] != '(') {
                        sfnc.append(inp[++i]);
                    }
                    int p = priority['F'];
                    while (waiting.size() != 0) {
                        MathWaiting waitnow = waiting.peek();
                        if (priority[waitnow.type] < p) {
                            break;
                        }
                        waiting.pop();
                        MathOperation mo = new MathOperation();
                        if (waitnow.type == 'F') {
                            mo.opValue = MathOp.FNC;
                            mo.functionValue = waitnow.functionName;
                            result.add(mo);
                        }
                        else {
                            mo.opValue = operations[waitnow.type];
                            result.add(mo);
                        }
                    }
                    MathWaiting mw = new MathWaiting();
                    mw.type = 'F';
                    mw.functionName = sfnc.toString();
                    waiting.push(mw);
                }
                else if (isOperator(inp[i])) {
                    int p = priority[inp[i]];
                    while (waiting.size() != 0) {
                        MathWaiting waitnow = waiting.peek();
                        if (priority[waitnow.type] < p) {
                            break;
                        }
                        waiting.pop();
                        MathOperation mo = new MathOperation();
                        if (waitnow.type == 'F') {
                            mo.opValue = MathOp.FNC;
                            mo.functionValue = waitnow.functionName;
                            result.add(mo);
                        }
                        else {
                            mo.opValue = operations[waitnow.type];
                            result.add(mo);
                        }
                    }
                    MathWaiting mw = new MathWaiting();
                    mw.type = inp[i];
                    waiting.push(mw);
                }
                else if (inp[i] == ')') {
                    while (true) {
                        MathWaiting waitnow = waiting.pop();
                        if (waitnow.type == '(') {
                            break;
                        }
                        MathOperation mo = new MathOperation();
                        if (waitnow.type == 'F') {
                            mo.opValue = MathOp.FNC;
                            mo.functionValue = waitnow.functionName;
                            result.add(mo);
                        }
                        else {
                            mo.opValue = operations[waitnow.type];
                            result.add(mo);
                        }
                    }
                }
            }
            while (waiting.size() != 0) {
                MathWaiting waitnow = waiting.pop();
                if (waitnow.type == '(')
                {
                    err.append("Inconsistent parenthesis!");
                    return null;
                }
                MathOperation mo = new MathOperation();
                if (waitnow.type == 'F') {
                    mo.opValue = MathOp.FNC;
                    mo.functionValue = waitnow.functionName;
                    result.add(mo);
                }
                else {
                    mo.opValue = operations[waitnow.type];
                    result.add(mo);
                }
            }
            return result;
        }
        catch (Exception e)
        {
            err.append(e.getClass().getCanonicalName() + ": " + e.getMessage());
            return null;
        }
    }

    public static class MathWaiting {
        public char type;
        public String functionName;
    }

    public static class MathOperation {
        public double numericValue;
        public String functionValue;
        public MathOp opValue;
    }

    public enum MathOp {
        BAD(0),
        ADD(1),
        SUB(2),
        MUL(3),
        DIV(4),
        MOD(5),
        EXP(6),
        LDN(7),
        FNC(8);

        public final int Value;

        MathOp(int c) {
            Value = c;
        }
    }
}
