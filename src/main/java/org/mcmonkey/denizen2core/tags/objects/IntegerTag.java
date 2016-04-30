package org.mcmonkey.denizen2core.tags.objects;

import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.Function2;

import java.util.HashMap;

public class IntegerTag extends AbstractTagObject {

    // <--[object]
    // @Type IntegerTag
    // @SubType NumberTag
    // @Group Mathematics
    // @Description Represents an integer.
    // @Other Note that the number is internally stored as a 64-bit signed integer (a 'long').
    // -->

    private long internal;

    public IntegerTag(long inty) {
        internal = inty;
    }

    public long getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name IntegerTag.add_integer[<IntegerTag>]
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer plus another integer.
        // @Example "1" .add_integer[1] returns "2".
        // -->
        handlers.put("add_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal + two.internal);
        });
        // <--[tag]
        // @Name IntegerTag.subtract_integer[<IntegerTag>]
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer minus another integer.
        // @Example "1" .subtract_integer[1] returns "0".
        // -->
        handlers.put("subtract_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal - two.internal);
        });
        // <--[tag]
        // @Name IntegerTag.multiply_integer[<IntegerTag>]
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer times another integer.
        // @Example "1" .multiply_integer[1] returns "1".
        // -->
        handlers.put("multiply_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal * two.internal);
        });
        // <--[tag]
        // @Name IntegerTag.divide_integer[<IntegerTag>]
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer divided by another integer.
        // @Example "1" .divide_integer[1] returns "1".
        // -->
        handlers.put("divide_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal / two.internal);
        });
        // <--[tag]
        // @Name IntegerTag.modulo_integer[<IntegerTag>]
        // @Group Mathematics
        // @ReturnType IntegerTag
        // @Returns the integer modulo another integer.
        // @Example "1" .modulo_integer[1] returns "0".
        // -->
        handlers.put("modulo_integer", (dat, obj) -> {
            IntegerTag two = IntegerTag.getFor(dat.error, dat.getNextModifier());
            return new IntegerTag(((IntegerTag) obj).internal % two.internal);
        });
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
        return (text instanceof IntegerTag) ? (IntegerTag) text : getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new NumberTag(internal).handle(data);
    }

    @Override
    public String toString() {
        return String.valueOf(internal);
    }
}
