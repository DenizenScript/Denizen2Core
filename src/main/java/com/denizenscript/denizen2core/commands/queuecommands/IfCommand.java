package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.BooleanTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.arguments.Argument;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandStackEntry;
import com.denizenscript.denizen2core.tags.objects.NumberTag;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.debugging.Debug;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IfCommand extends AbstractCommand {

    public static class IfCommandData {
        public int result = 0;
    }

    // <--[command]
    // @Name if
    // @Arguments <if comparisons>
    // @Short runs a block of code if-and-only-if the comparisons return true.
    // @Updated 2016/04/18
    // @Group Queue
    // @Procedural true
    // @Minimum 1
    // @Maximum -1
    // @Description
    // Runs a block of code if-and-only-if the comparisons return true.
    // Can be followed by an <@link command else>else<@/link> command.
    // TODO: Explain more!
    // @Example
    // # This example always echoes "hi".
    // - if true:
    //   - echo "hi"
    // # This example never does anything.
    // - if false:
    //   - echo "This won't show"
    // -->

    @Override
    public String getName() {
        return "if";
    }

    @Override
    public String getArguments() {
        return "<if comparisons>";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return -1;
    }

    @Override
    public boolean isProcedural() {
        return true;
    }

    public static boolean getBool(Action<String> error, AbstractTagObject obj) {
        boolean negate = false;
        if (obj instanceof TextTag && obj.toString().startsWith("!")) {
            negate = true;
            obj = new TextTag(obj.toString().substring(1));
        }
        boolean b = BooleanTag.getFor(error, obj).getInternal();
        if (negate) {
            b = !b;
        }
        return b;
    }

    public static class ObjectHolder {
        public AbstractTagObject object = null;
    }

    public static class TryIfHelper {
        public CommandQueue queue;

        public CommandEntry entry;

        public List<ObjectHolder> objectsRead = new ArrayList<>();

        public List<Argument> arguments;

        public void ensure() {
            if (objectsRead.size() == 0) {
                for (int i = 0; i < arguments.size(); i++) {
                    objectsRead.add(new ObjectHolder());
                }
            }
        }

        public AbstractTagObject getObj(int arg) {
            ObjectHolder oh = objectsRead.get(arg);
            if (oh.object != null) {
                return oh.object;
            }
            AbstractTagObject temp = arguments.get(arg).parse(queue, new HashMap<>(), DebugMode.FULL, queue.error);
            oh.object = temp;
            return temp;
        }
    }

    public static boolean tryIf(TryIfHelper helper) {
        helper.ensure();
        if (DEBUG) {
            Debug.info("[IF:DEBUG] Args: " + helper.arguments.size());
        }
        if (helper.arguments.size() == 0) {
            return false;
        }
        if (helper.arguments.size() == 1) {
            return getBool(helper.queue.error, helper.getObj(0));
        }
        for (int i = 0; i < helper.arguments.size(); i++) {
            String iarg = helper.arguments.get(i).toString();
            if (iarg.equals("(")) {
                int c = 1;
                for (int x = i + 1; x < helper.arguments.size(); x++) {
                    String xarg = helper.arguments.get(x).toString();
                    if (xarg.equals("(")) {
                        c++;
                    }
                    if (xarg.equals(")")) {
                        c--;
                        if (c == 0) {
                            TryIfHelper tif = new TryIfHelper();
                            tif.queue = helper.queue;
                            tif.entry = helper.entry;
                            tif.objectsRead = helper.objectsRead.subList(i + 1, x);
                            tif.arguments = helper.arguments.subList(i + 1, x);
                            boolean thisPiece = tryIf(tif);
                            List<Argument> args = new ArrayList<>();
                            List<ObjectHolder> objs = new ArrayList<>();
                            for (int j = 0; j < i; j++) {
                                args.add(helper.arguments.get(j));
                                objs.add(helper.objectsRead.get(j));
                            }
                            args.add(new Argument());
                            ObjectHolder oh = new ObjectHolder();
                            oh.object = new TextTag(thisPiece ? "true" : "false");
                            objs.add(oh);
                            for (int j = x + 1; j < helper.arguments.size(); j++) {
                                args.add(helper.arguments.get(j));
                                objs.add(helper.objectsRead.get(j));
                            }
                            helper.arguments = args;
                            helper.objectsRead = objs;
                            return tryIf(helper);
                        }
                    }
                }
                return false;
            }
        }
        for (int i = 0; i < helper.arguments.size(); i++) {
            String iarg = helper.arguments.get(i).toString();
            if (iarg.equals("&&")) {
                TryIfHelper tif = new TryIfHelper();
                tif.queue = helper.queue;
                tif.entry = helper.entry;
                tif.objectsRead = helper.objectsRead.subList(0, i);
                tif.arguments = helper.arguments.subList(0, i);
                TryIfHelper tif2 = new TryIfHelper();
                tif2.queue = helper.queue;
                tif2.entry = helper.entry;
                tif2.objectsRead = helper.objectsRead.subList(i + 1, helper.arguments.size());
                tif2.arguments = helper.arguments.subList(i + 1, helper.arguments.size());
                if (!tryIf(tif)) {
                    return false;
                }
                return tryIf(tif2);
            }
            if (iarg.equals("||")) {
                TryIfHelper tif = new TryIfHelper();
                tif.queue = helper.queue;
                tif.entry = helper.entry;
                tif.objectsRead = helper.objectsRead.subList(0, i);
                tif.arguments = helper.arguments.subList(0, i);
                TryIfHelper tif2 = new TryIfHelper();
                tif2.queue = helper.queue;
                tif2.entry = helper.entry;
                tif2.objectsRead = helper.objectsRead.subList(i + 1, helper.arguments.size());
                tif2.arguments = helper.arguments.subList(i + 1, helper.arguments.size());
                if (tryIf(tif)) {
                    return true;
                }
                return tryIf(tif2);
            }
        }
        if (DEBUG) {
            Debug.info("[IF:DEBUG] Args [Pos2]: " + helper.arguments.size());
        }
        if (helper.arguments.size() == 1) {
            return getBool(helper.queue.error, helper.getObj(0));
        }
        if (helper.arguments.size() == 3) {
            AbstractTagObject obj1 = helper.getObj(0);
            AbstractTagObject obj2 = helper.getObj(2);
            String comp = helper.arguments.get(1).toString();
            if (DEBUG) {
                Debug.info("[IF:DEBUG] Comparison operator: " + comp);
            }
            if (comp.equals("==")) {
                return obj1.toString().equalsIgnoreCase(obj2.toString());
            }
            else if (comp.equals("!=")) {
                return !obj1.toString().equalsIgnoreCase(obj2.toString());
            }
            NumberTag n1 = NumberTag.getFor(helper.queue.error, obj1);
            NumberTag n2 = NumberTag.getFor(helper.queue.error, obj2);
            if (DEBUG) {
                Debug.info("[IF:DEBUG] Numeric operands: " + n1.getInternal() + ", " + n2.getInternal());
            }
            if (comp.equals(">=")) {
                return n1.getInternal() >= n2.getInternal();
            }
            if (comp.equals(">")) {
                return n1.getInternal() > n2.getInternal();
            }
            if (comp.equals("<")) {
                return n1.getInternal() < n2.getInternal();
            }
            if (comp.equals("<=")) {
                return n1.getInternal() <= n2.getInternal();
            }
        }
        return false;
    }

    private static final boolean DEBUG = false;

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        if (entry.arguments.get(0).toString().equals("\0CALLBACK")) {
            CommandStackEntry cse = queue.commandStack.peek();
            CommandEntry ifentry = cse.entries[entry.blockStart - 1];
            entry.setData(queue, ifentry.getData(queue));
            if (cse.getIndex() < cse.entries.length) {
                CommandEntry elseentry = cse.entries[cse.getIndex()];
                if (elseentry.command instanceof ElseCommand) {
                    elseentry.setData(queue, ifentry.getData(queue));
                }
            }
            return;
        }
        IfCommandData dat = new IfCommandData();
        dat.result = 0;
        entry.setData(queue, dat);
        TryIfHelper helper = new TryIfHelper();
        helper.queue = queue;
        helper.entry = entry;
        helper.arguments = entry.arguments;
        boolean success = tryIf(helper);
        if (success) {
            if (queue.shouldShowGood()) {
                queue.outGood("If is true, executing...");
            }
            ((IfCommandData) entry.getData(queue)).result = 1;
        }
        else {
            if (queue.shouldShowGood()) {
                queue.outGood("If is false, doing nothing!");
            }
            queue.commandStack.peek().goTo(entry.blockEnd + 1);
        }
    }
}
