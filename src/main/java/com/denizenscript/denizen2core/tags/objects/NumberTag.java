package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.HashMap;

public class NumberTag extends AbstractTagObject implements Denizen2Core.NumberForm {

    // <--[object]
    // @Since 0.3.0
    // @Type NumberTag
    // @SubType TextTag
    // @Group Mathematics
    // @Description Represents a decimal number. Identified as a number with a decimal point.
    // @Note The number is internally stored as a 64-bit signed floating point number (a 'double').
    // -->

    private double internal;

    public NumberTag(double inty) {
        internal = inty;
    }

    public double getInternal() {
        return internal;
    }

    @Override
    public double getNumberForm() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.is_greater_than[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType BooleanTag
        // @Returns whether this number is bigger than the specified number.
        // @Example "1" .is_greater_than[2] returns "false".
        // -->
        handlers.put("is_greater_than", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return BooleanTag.getForBoolean(((NumberTag) obj).internal > two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.is_greater_than_or_equal_to[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType BooleanTag
        // @Returns whether this number is bigger than or equal to the specified number.
        // @Example "1" .is_greater_than_or_equal_to[2] returns "false".
        // -->
        handlers.put("is_greater_than_or_equal_to", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return BooleanTag.getForBoolean(((NumberTag) obj).internal >= two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.is_less_than[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType BooleanTag
        // @Returns whether this number is smaller than the specified number.
        // @Example "1" .is_less_than[2] returns "false".
        // -->
        handlers.put("is_less_than", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return BooleanTag.getForBoolean(((NumberTag) obj).internal < two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.is_less_than_or_equal_to[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType BooleanTag
        // @Returns whether this number is smaller than or equal to the specified number.
        // @Example "1" .is_less_than_or_equal_to[2] returns "false".
        // -->
        handlers.put("is_less_than_or_equal_to", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return BooleanTag.getForBoolean(((NumberTag) obj).internal <= two.internal);
        });
        // Documented in TextTag.
        handlers.put("equals", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return BooleanTag.getForBoolean(((NumberTag) obj).internal == two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.add[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number plus another number.
        // @Note also known as ".+[<NumberTag>]".
        // @Example "1" .add[1] returns "2".
        // -->
        handlers.put("add", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal + two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.+[<NumberTag>]
        // @Updated 2016/12/11
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number plus another number.
        // @Note also known as ".add[<NumberTag>]".
        // @Example "1" .+[1] returns "2".
        // -->
        handlers.put("+", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal + two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.subtract[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number minus another number.
        // @Note also known as ".sub[<NumberTag>]" and ".-[<NumberTag>]".
        // @Example "1" .subtract[1] returns "0".
        // -->
        handlers.put("subtract", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal - two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.sub[<NumberTag>]
        // @Updated 2017/01/29
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number minus another number.
        // @Note also known as ".subtract[<NumberTag>]" and ".-[<NumberTag>]".
        // @Example "1" .sub[1] returns "0".
        // -->
        handlers.put("sub", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal - two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.-[<NumberTag>]
        // @Updated 2016/12/11
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number minus another number.
        // @Note also known as ".subtract[<NumberTag>]" and ".sub[<NumberTag>]".
        // @Example "1" .-[1] returns "0".
        // -->
        handlers.put("-", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal - two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.multiply[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number times another number.
        // @Note also known as ".mul[<NumberTag>]" and ".*[<NumberTag>]".
        // @Example "1" .multiply[1] returns "1".
        // -->
        handlers.put("multiply", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal * two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.mul[<NumberTag>]
        // @Updated 2017/01/29
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number times another number.
        // @Note also known as ".multiply[<NumberTag>]" and ".*[<NumberTag>]".
        // @Example "1" .mul[1] returns "1".
        // -->
        handlers.put("mul", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal * two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.*[<NumberTag>]
        // @Updated 2016/12/11
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number times another number.
        // @Note also known as ".multiply[<NumberTag>]" and ".mul[<NumberTag>]".
        // @Example "1" .*[1] returns "1".
        // -->
        handlers.put("*", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal * two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.divide[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number divided by another number.
        // @Note also known as ".div[<NumberTag>]" and "./[<NumberTag>]".
        // @Example "1" .divide[1] returns "1".
        // -->
        handlers.put("divide", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal / two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.div[<NumberTag>]
        // @Updated 2017/01/29
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number divided by another number.
        // @Note also known as ".divide[<NumberTag>]" and "./[<NumberTag>]".
        // @Example "1" .div[1] returns "1".
        // -->
        handlers.put("div", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal / two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag./[<NumberTag>]
        // @Updated 2016/12/11
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number divided by another number.
        // @Note also known as ".divide[<NumberTag>]" and ".div[<NumberTag>]".
        // @Example "1" ./[1] returns "1".
        // -->
        handlers.put("/", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal / two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.modulo[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number modulo another number.
        // @Note also known as ".mod[<NumberTag>]" and ".%[<NumberTag>]".
        // @Example "1" .modulo[1] returns "0".
        // -->
        handlers.put("modulo", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal % two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.mod[<NumberTag>]
        // @Updated 2017/01/29
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number modulo another number.
        // @Note also known as ".modulo[<NumberTag>]" and ".%[<NumberTag>]".
        // @Example "1" .mod[1] returns "0".
        // -->
        handlers.put("mod", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal % two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.%[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number modulo another number.
        // @Note also known as ".modulo[<NumberTag>]" and ".mod[<NumberTag>]".
        // @Example "1" .%[1] returns "0".
        // -->
        handlers.put("%", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal % two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.maximum[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns whichever is bigger: this number, or the specified number.
        // @Note also known as ".max[<NumberTag>]".
        // @Example "1" .maximum[2] returns "2".
        // -->
        handlers.put("maximum", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(Math.max(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Since 0.5.0
        // @Name NumberTag.max[<NumberTag>]
        // @Updated 2018/06/09
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns whichever is bigger: this number, or the specified number.
        // @Note also known as ".maximum[<NumberTag>]".
        // @Example "1" .max[2] returns "2".
        // -->
        handlers.put("max", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(Math.max(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.minimum[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns whichever is smaller: this number, or the specified number.
        // @Note also known as ".min[<NumberTag>]".
        // @Example "1" .minimum[2] returns "1".
        // -->
        handlers.put("minimum", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(Math.min(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Since 0.5.0
        // @Name NumberTag.min[<NumberTag>]
        // @Updated 2018/06/09
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns whichever is smaller: this number, or the specified number.
        // @Note also known as ".minimum[<NumberTag>]".
        // @Example "1" .min[2] returns "1".
        // -->
        handlers.put("min", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(Math.min(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.log[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the logarithm (base: specified number) of this number.
        // @Example "2" .log[2] returns "1".
        // -->
        handlers.put("log", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(Math.log(((NumberTag) obj).internal) / Math.log(two.internal));
        });
        // <--[tag]
        // @Since 0.5.0
        // @Name NumberTag.natural_log
        // @Updated 2018/06/09
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the natural logarithm (base: e) of this number.
        // @Note also known as ".ln".
        // @Example "2.71828" .natural_log returns "1".
        // -->
        handlers.put("natural_log", (dat, obj) -> new NumberTag(Math.log(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.5.0
        // @Name NumberTag.ln
        // @Updated 2018/06/09
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the natural logarithm (base: e) of this number.
        // @Note also known as ".natural_log".
        // @Example "2.71828" .ln returns "1".
        // -->
        handlers.put("ln", (dat, obj) -> new NumberTag(Math.log(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.power[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number to the power of the specified number.
        // @Note also known as ".^[<NumberTag>]".
        // @Example "2" .power[2] returns "4".
        // -->
        handlers.put("power", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(Math.pow(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.^[<NumberTag>]
        // @Updated 2016/12/11
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number to the power of the specified number.
        // @Note also known as ".power[<NumberTag>]".
        // @Example "2" .^[2] returns "4".
        // -->
        handlers.put("^", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(Math.pow(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.absolute_value
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the absolute value of this number.
        // @Note also known as ".abs".
        // @Example "-1" .absolute_value returns "1".
        // -->
        handlers.put("absolute_value", (dat, obj) -> new NumberTag(Math.abs(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.5.0
        // @Name NumberTag.abs
        // @Updated 2018/06/09
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the absolute value of this number.
        // @Note also known as ".absolute_value".
        // @Example "-1" .abs returns "1".
        // -->
        handlers.put("abs", (dat, obj) -> new NumberTag(Math.abs(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.cosine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the cosine of this number.
        // @Example "3.14159" .cosine returns "-1".
        // -->
        handlers.put("cosine", (dat, obj) -> new NumberTag(Math.cos(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.sine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the sine of this number.
        // @Example "3.14159" .sine returns "0".
        // -->
        handlers.put("sine", (dat, obj) -> new NumberTag(Math.sin(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.arccosine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the arccosine of this number.
        // @Example "1" .arccosine returns "0".
        // -->
        handlers.put("arccosine", (dat, obj) -> new NumberTag(Math.acos(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.arcsine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the arcsine of this number.
        // @Example "0" .arcsine returns "0".
        // -->
        handlers.put("arcsine", (dat, obj) -> new NumberTag(Math.asin(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.arctangent
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the arctangent of this number.
        // @Example "0" .arctangent returns "0".
        // -->
        handlers.put("arctangent", (dat, obj) -> new NumberTag(Math.atan(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.tangent
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the tangent of this number.
        // @Example "3.14159" .tangent returns "0".
        // -->
        handlers.put("tangent", (dat, obj) -> new NumberTag(Math.tan(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.atan2[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the inverse of the tangent that is the number divided by the specified number.
        // @Example "0" .atan2[1] returns "0".
        // -->
        handlers.put("atan2", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.checkedError, dat.getNextModifier());
            return new NumberTag(Math.atan2(((NumberTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.round
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the number rounded to the closest whole number.
        // @Example "0.5" .round returns "1".
        // -->
        handlers.put("round", (dat, obj) -> new IntegerTag(Math.round(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.5.0
        // @Name NumberTag.round_to_places[<IntegerTag>]
        // @Updated 2018/06/09
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number rounded to the specified decimal place.
        // @Example "0.12345" .round_to_places[3] returns "0.123".
        // -->
        handlers.put("round_to_places", (dat, obj) -> {
            IntegerTag places = IntegerTag.getFor(dat.error, dat.getNextModifier());
            double coef = Math.pow(10, places.getInternal());
            return new NumberTag(Math.round(((NumberTag) obj).internal * coef) / coef);
        });
        // <--[tag]
        // @Since 0.5.0
        // @Name NumberTag.round_to[<NumberTag>]
        // @Updated 2018/06/09
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number rounded to the specified precision.
        // @Example "0.12345" .round_to[0.005] returns "0.125".
        // -->
        handlers.put("round_to", (dat, obj) -> {
            double precision = NumberTag.getFor(dat.error, dat.getNextModifier()).internal;
            return new NumberTag(Math.round(((NumberTag) obj).internal / precision) * precision);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.round_up
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number rounded up.
        // @Example "0.5" .round_up returns "1".
        // -->
        handlers.put("round_up", (dat, obj) -> new NumberTag(Math.ceil(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.round_down
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number rounded down.
        // @Example "0.5" .round_down returns "0".
        // -->
        handlers.put("round_down", (dat, obj) -> new NumberTag(Math.floor(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.sign
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the sign of this number, which can be -1, 0, or 1.
        // @Example "-5" .sign returns "-1".
        // -->
        handlers.put("sign", (dat, obj) -> new IntegerTag((int) Math.signum(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.hyberbolic_sine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the hyperbolic sine of this number.
        // @Example "0" .hyberbolic_sine returns "0".
        // -->
        handlers.put("hyberbolic_sine", (dat, obj) -> new NumberTag(Math.sinh(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.hyberbolic_cosine
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the hyperbolic cosine of this number.
        // @Example "0" .hyberbolic_cosine returns "1".
        // -->
        handlers.put("hyberbolic_cosine", (dat, obj) -> new NumberTag(Math.cosh(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.hyberbolic_tangent
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the hyperbolic tangent of this number.
        // @Example "0" .hyberbolic_tangent returns "0".
        // -->
        handlers.put("hyberbolic_tangent", (dat, obj) -> new NumberTag(Math.tanh(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.square_root
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the square root of this number.
        // @Example "4" .square_root returns "2".
        // -->
        handlers.put("square_root", (dat, obj) -> new NumberTag(Math.sqrt(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.truncate
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number, truncated (rounded towards zero).
        // @Example "-2.8" .truncate returns "-2".
        // -->
        handlers.put("truncate", (dat, obj) -> new NumberTag(truncate(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.to_radians
        // @Updated 2017/10/18
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number converted from degrees to radians.
        // -->
        handlers.put("to_radians", (dat, obj) -> new NumberTag(Math.toRadians(((NumberTag) obj).internal)));
        // <--[tag]
        // @Since 0.3.0
        // @Name NumberTag.to_degrees
        // @Updated 2017/10/18
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number converted from radians to degrees.
        // -->
        handlers.put("to_degrees", (dat, obj) -> new NumberTag(Math.toDegrees(((NumberTag) obj).internal)));
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
        if (text instanceof NumberTag) {
            return (NumberTag) text;
        }
        if (text instanceof Denizen2Core.NumberForm) {
            return new NumberTag(((Denizen2Core.NumberForm) text).getNumberForm());
        }
        return getFor(error, text.toString());
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
    public String getTagTypeName() {
        return "NumberTag";
    }

    @Override
    public String toString() {
        return CoreUtilities.doubleToString(internal);
    }
}
