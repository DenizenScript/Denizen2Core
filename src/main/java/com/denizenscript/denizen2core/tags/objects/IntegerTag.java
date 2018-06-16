package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.utilities.Action;

import java.util.HashMap;

public class IntegerTag extends AbstractTagObject implements Denizen2Core.IntegerForm, Denizen2Core.NumberForm {

    // <--[object]
    // @Since 0.3.0
    // @Type IntegerTag
    // @SubType NumberTag
    // @Group Mathematics
    // @Description Represents an integer. Identified as a simple integer number.
    // @Note The number is internally stored as a 64-bit signed integer (a 'long').
    // -->

    private long internal;

    public IntegerTag(long inty) {
        internal = inty;
    }

    public long getInternal() {
        return internal;
    }

    @Override
    public long getIntegerForm() {
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
        // @Name IntegerTag.add_integer[<IntegerTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer plus another integer.
        // @Note also known as ".add_int[<IntegerTag>]".
        // @Example "1" .add_integer[1] returns "2".
        // -->
        handlers.put("add_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.checkedError, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal + two.internal);
        });
        // <--[tag]
        // @Since 0.5.5
        // @Name IntegerTag.add_int[<IntegerTag>]
        // @Updated 2018/06/16
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer plus another integer.
        // @Note also known as ".add_integer[<IntegerTag>]".
        // @Example "1" .add_int[1] returns "2".
        // -->
        handlers.put("add_int", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal + two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name IntegerTag.subtract_integer[<IntegerTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer minus another integer.
        // @Note also known as ".sub_int[<IntegerTag>]".
        // @Example "1" .subtract_integer[1] returns "0".
        // -->
        handlers.put("subtract_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.checkedError, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal - two.internal);
        });
        // <--[tag]
        // @Since 0.5.5
        // @Name IntegerTag.sub_int[<IntegerTag>]
        // @Updated 2018/06/16
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer minus another integer.
        // @Note also known as ".subtract_integer[<IntegerTag>]".
        // @Example "1" .sub_int[1] returns "0".
        // -->
        handlers.put("sub_int", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal - two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name IntegerTag.multiply_integer[<IntegerTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer times another integer.
        // @Note also known as ".mul_int[<IntegerTag>]".
        // @Example "1" .multiply_integer[1] returns "1".
        // -->
        handlers.put("multiply_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.checkedError, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal * two.internal);
        });
        // <--[tag]
        // @Since 0.5.5
        // @Name IntegerTag.mul_int[<IntegerTag>]
        // @Updated 2018/06/16
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer times another integer.
        // @Note also known as ".multiply_integer[<IntegerTag>]".
        // @Example "1" .mul_int[1] returns "1".
        // -->
        handlers.put("mul_int", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal * two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name IntegerTag.divide_integer[<IntegerTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer divided by another integer.
        // @Note also known as ".div_int[<IntegerTag>]".
        // @Example "1" .divide_integer[1] returns "1".
        // -->
        handlers.put("divide_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.checkedError, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal / two.internal);
        });
        // <--[tag]
        // @Since 0.5.5
        // @Name IntegerTag.div_int[<IntegerTag>]
        // @Updated 2018/06/16
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer divided by another integer.
        // @Note also known as ".divide_integer[<IntegerTag>]".
        // @Example "1" .div_int[1] returns "1".
        // -->
        handlers.put("div_int", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal / two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name IntegerTag.modulo_integer[<IntegerTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer modulo another integer.
        // @Note also known as ".mod_int[<IntegerTag>]".
        // @Example "1" .modulo_integer[1] returns "0".
        // -->
        handlers.put("modulo_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.checkedError, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal % two.internal);
        });
        // <--[tag]
        // @Since 0.5.5
        // @Name IntegerTag.mod_int[<IntegerTag>]
        // @Updated 2018/06/16
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer modulo another integer.
        // @Note also known as ".modulo_integer[<IntegerTag>]".
        // @Example "1" .mod_int[1] returns "0".
        // -->
        handlers.put("mod_int", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal % two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name IntegerTag.maximum_integer[<IntegerTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns whichever is bigger: this integer, or the specified integer.
        // @Note also known as ".max_int[<IntegerTag>]".
        // @Example "1" .maximum_integer[2] returns "2".
        // -->
        handlers.put("maximum_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.checkedError, dat.getNextModifier());
            return new IntegerTag(Math.max(((IntegerTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Since 0.5.5
        // @Name IntegerTag.max_int[<IntegerTag>]
        // @Updated 2018/06/16
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns whichever is bigger: this integer, or the specified integer.
        // @Note also known as ".maximum_integer[<IntegerTag>]".
        // @Example "1" .max_int[2] returns "2".
        // -->
        handlers.put("max_int", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(Math.max(((IntegerTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name IntegerTag.minimum_integer[<IntegerTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns whichever is smaller: this integer, or the specified integer.
        // @Note also known as ".min_int[<IntegerTag>]".
        // @Example "1" .minimum_integer[2] returns "1".
        // -->
        handlers.put("minimum_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.checkedError, dat.getNextModifier());
            return new IntegerTag(Math.min(((IntegerTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Since 0.5.5
        // @Name IntegerTag.min_int[<IntegerTag>]
        // @Updated 2018/06/16
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns whichever is smaller: this integer, or the specified integer.
        // @Note also known as ".minimum_integer[<IntegerTag>]".
        // @Example "1" .min_int[2] returns "1".
        // -->
        handlers.put("min_int", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(Math.min(((IntegerTag) obj).internal, two.internal));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name IntegerTag.absolute_value_integer
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the absolute value of this integer.
        // @Note also known as ".abs_int[<IntegerTag>]".
        // @Example "-1" .absolute_value_integer returns "1".
        // -->
        handlers.put("absolute_value_integer", (dat, obj) -> new IntegerTag(Math.abs(((IntegerTag) obj).internal)));
        // <--[tag]
        // @Since 0.5.5
        // @Name IntegerTag.abs_int
        // @Updated 2018/06/16
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the absolute value of this integer.
        // @Note also known as ".absolute_value_integer[<IntegerTag>]".
        // @Example "-1" .abs_int returns "1".
        // -->
        handlers.put("abs_int", (dat, obj) -> new IntegerTag(Math.abs(((IntegerTag) obj).internal)));
    }

    public static IntegerTag getFor(Action<String> error, String text) {
        try {
            long l = Long.parseLong(text);
            return new IntegerTag(l);
        }
        catch (NumberFormatException ex) {
            error.run("Invalid IntegerTag input!");
            return null;
        }
    }

    public static IntegerTag getFor(Action<String> error, AbstractTagObject text) {
        if (text instanceof IntegerTag) {
            return (IntegerTag) text;
        }
        if (text instanceof Denizen2Core.IntegerForm) {
            return new IntegerTag(((Denizen2Core.IntegerForm) text).getIntegerForm());
        }
        return getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new NumberTag(internal);
    }

    @Override
    public String getTagTypeName() {
        return "IntegerTag";
    }

    @Override
    public String toString() {
        return String.valueOf(internal);
    }
}
