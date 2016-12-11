package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.HashMap;

public class NumberTag extends AbstractTagObject {

    // <--[object]
    // @Type NumberTag
    // @SubType TextTag
    // @Group Mathematics
    // @Description Represents a decimal number. Identified as a number with a decimal point.
    // @Other Note that the number is internally stored as a 64-bit signed floating point number (a 'double').
    // -->

    private double internal;

    public NumberTag(double inty) {
        internal = inty;
    }

    public double getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name NumberTag.is_greater_than[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType BooleanTag
        // @Returns whether this number is bigger than the specified number.
        // @Example "1" .is_greater_than[2] returns "false".
        // -->
        handlers.put("is_greater_than", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new BooleanTag(((NumberTag) obj).internal > two.internal);
        });
        // <--[tag]
        // @Name NumberTag.is_greater_than_or_equal_to[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType BooleanTag
        // @Returns whether this number is bigger than or equal to the specified number.
        // @Example "1" .is_greater_than_or_equal_to[2] returns "false".
        // -->
        handlers.put("is_greater_than_or_equal_to", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new BooleanTag(((NumberTag) obj).internal >= two.internal);
        });
        // <--[tag]
        // @Name NumberTag.is_less_than[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType BooleanTag
        // @Returns whether this number is smaller than the specified number.
        // @Example "1" .is_less_than[2] returns "false".
        // -->
        handlers.put("is_less_than", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new BooleanTag(((NumberTag) obj).internal < two.internal);
        });
        // <--[tag]
        // @Name NumberTag.is_less_than_or_equal_to[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType BooleanTag
        // @Returns whether this number is smaller than or equal to the specified number.
        // @Example "1" .is_less_than_or_equal_to[2] returns "false".
        // -->
        handlers.put("is_less_than_or_equal_to", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new BooleanTag(((NumberTag) obj).internal <= two.internal);
        });
        // Documented in TextTag.
        handlers.put("equals", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new BooleanTag(((NumberTag) obj).internal == two.internal);
        });
        // <--[tag]
        // @Name NumberTag.add[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number plus another number.
        // @Example "1" .add[1] returns "2".
        // -->
        handlers.put("add", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal + two.internal);
        });
        // <--[tag]
        // @Name NumberTag.+[<NumberTag>]
        // @Updated 2016/12/11
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number plus another number.
        // @Example "1" .+[1] returns "2".
        // -->
        handlers.put("+", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal + two.internal);
        });
        // <--[tag]
        // @Name NumberTag.subtract[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number minus another number.
        // @Example "1" .subtract[1] returns "0".
        // -->
        handlers.put("subtract", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal - two.internal);
        });
        // <--[tag]
        // @Name NumberTag.-[<NumberTag>]
        // @Updated 2016/12/11
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number minus another number.
        // @Example "1" .-[1] returns "0".
        // -->
        handlers.put("-", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal - two.internal);
        });
        // <--[tag]
        // @Name NumberTag.multiply[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number times another number.
        // @Example "1" .multiply[1] returns "1".
        // -->
        handlers.put("multiply", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal * two.internal);
        });
        // <--[tag]
        // @Name NumberTag.*[<NumberTag>]
        // @Updated 2016/12/11
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number times another number.
        // @Example "1" .*[1] returns "1".
        // -->
        handlers.put("*", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal * two.internal);
        });
        // <--[tag]
        // @Name NumberTag.divide[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number divided by another number.
        // @Example "1" .divide[1] returns "1".
        // -->
        handlers.put("divide", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal / two.internal);
        });
        // <--[tag]
        // @Name NumberTag./[<NumberTag>]
        // @Updated 2016/12/11
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number divided by another number.
        // @Example "1" ./[1] returns "1".
        // -->
        handlers.put("/", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal / two.internal);
        });
        // <--[tag]
        // @Name NumberTag.modulo[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number modulo another number.
        // @Example "1" .modulo[1] returns "0".
        // -->
        handlers.put("modulo", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal % two.internal);
        });
        // <--[tag]
        // @Name NumberTag.%[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number modulo another number.
        // @Example "1" .%[1] returns "0".
        // -->
        handlers.put("%", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal % two.internal);
        });
        // <--[tag]
        // @Name NumberTag.maximum[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns whichever is bigger: this number, or the specified number.
        // @Example "1" .maximum[2] returns "2".
        // -->
        handlers.put("maximum", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(Math.max(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Name NumberTag.minimum[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns whichever is smaller: this number, or the specified number.
        // @Example "1" .maximum[2] returns "1".
        // -->
        handlers.put("minimum", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(Math.min(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Name NumberTag.log[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the logarithm (base: specified number) of this number.
        // @Example "2" .log[2] returns "1".
        // -->
        handlers.put("log", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(Math.log(((NumberTag) obj).internal) / Math.log(two.internal));
        });
        // <--[tag]
        // @Name NumberTag.power[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number to the power of the specified number.
        // @Example "2" .power[2] returns "4".
        // -->
        handlers.put("power", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(Math.pow(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Name NumberTag.^[<NumberTag>]
        // @Updated 2016/12/11
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number to the power of the specified number.
        // @Example "2" .^[2] returns "4".
        // -->
        handlers.put("^", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(Math.pow(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Name NumberTag.absolute_value
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the absolute value of this number.
        // @Example "-1" .absolute_value returns "1".
        // -->
        handlers.put("absolute_value", (dat, obj) -> {
            return new NumberTag(Math.abs(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.cosine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the cosine of this number.
        // @Example "3.14159" .cosine returns "-1".
        // -->
        handlers.put("cosine", (dat, obj) -> {
            return new NumberTag(Math.cos(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.sine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the sine of this number.
        // @Example "3.14159" .sine returns "0".
        // -->
        handlers.put("sine", (dat, obj) -> {
            return new NumberTag(Math.sin(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.arccosine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the arccosine of this number.
        // @Example "1" .arccosine returns "0".
        // -->
        handlers.put("arccosine", (dat, obj) -> {
            return new NumberTag(Math.acos(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.arcsine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the arcsine of this number.
        // @Example "0" .arcsine returns "0".
        // -->
        handlers.put("arcsine", (dat, obj) -> {
            return new NumberTag(Math.asin(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.arctangent
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the arctangent of this number.
        // @Example "0" .arctangent returns "0".
        // -->
        handlers.put("arctangent", (dat, obj) -> {
            return new NumberTag(Math.atan(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.tangent
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the tangent of this number.
        // @Example "3.14159" .tangent returns "0".
        // -->
        handlers.put("tangent", (dat, obj) -> {
            return new NumberTag(Math.tan(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.atan2[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the inverse of the tangent that is the number divided by the specified number.
        // @Example "0" .atan2[1] returns "0".
        // -->
        handlers.put("atan2", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(Math.atan2(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Name NumberTag.round
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number rounded to the closest whole number.
        // @Example "0.5" .round returns "1".
        // -->
        handlers.put("round", (dat, obj) -> {
            return new NumberTag(Math.round(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.round_up
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number rounded up.
        // @Example "0.5" .round_up returns "1".
        // -->
        handlers.put("round_up", (dat, obj) -> {
            return new NumberTag(Math.ceil(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.round_down
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number rounded down.
        // @Example "0.5" .round_down returns "0".
        // -->
        handlers.put("round_down", (dat, obj) -> {
            return new NumberTag(Math.floor(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.sign
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the sign of this number, which can be -1, 0, or 1.
        // @Example "-5" .sign returns "-1".
        // -->
        handlers.put("sign", (dat, obj) -> {
            return new IntegerTag((int)Math.signum(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.hyberbolic_sine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the hyperbolic sine of this number.
        // @Example "0" .hyberbolic_sine returns "0".
        // -->
        handlers.put("hyberbolic_sine", (dat, obj) -> {
            return new NumberTag(Math.sinh(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.hyberbolic_cosine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the hyperbolic cosine of this number.
        // @Example "0" .hyberbolic_cosine returns "1".
        // -->
        handlers.put("hyberbolic_cosine", (dat, obj) -> {
            return new NumberTag(Math.cosh(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.hyberbolic_tangent
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the hyperbolic tangent of this number.
        // @Example "0" .hyberbolic_tangent returns "0".
        // -->
        handlers.put("hyberbolic_tangent", (dat, obj) -> {
            return new NumberTag(Math.tanh(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.square_root
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the square root of this number.
        // @Example "4" .square_root returns "2".
        // -->
        handlers.put("square_root", (dat, obj) -> {
            return new NumberTag(Math.sqrt(((NumberTag) obj).internal));
        });
        // <--[tag]
        // @Name NumberTag.truncate
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number, truncated (rounded towards zero).
        // @Example "-2.8" .truncate returns "-2".
        // -->
        handlers.put("truncate", (dat, obj) -> {
            return new NumberTag(truncate(((NumberTag) obj).internal));
        });
    }

    private static double truncate(double val) {
        if (val < 0) {
            return Math.ceil(val);
        }
        return Math.floor(val);
    }

    public static NumberTag getFor(Action<String> error, String text) {
        try {
            double d = Double.parseDouble(text);
            return new NumberTag(d);
        }
        catch (NumberFormatException ex) {
            error.run("Invalid NumberTag input!");
            return null;
        }
    }

    public static NumberTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof NumberTag) ? (NumberTag) text : getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new TextTag(toString());
    }

    @Override
    public String toString() {
        return CoreUtilities.doubleToString(internal);
    }
}
