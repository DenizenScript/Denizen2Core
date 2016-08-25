package org.mcmonkey.denizen2core.tags.objects;

import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.Function2;

import java.util.HashMap;

public class NumberTag extends AbstractTagObject {

    // <--[object]
    // @Type NumberTag
    // @SubType TextTag
    // @Group Mathematics
    // @Description Represents a decimal number.
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
        // @Name NumberTag.add[<NumberTag>]
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
        // @Name NumberTag.subtract[<NumberTag>]
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
        // @Name NumberTag.multiply[<NumberTag>]
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
        // @Name NumberTag.divide[<NumberTag>]
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
        // @Name NumberTag.modulo[<NumberTag>]
        // @Group Mathematics
        // @ReturnType NumberTag
        // @Returns the number modulo another number.
        // @Example "1" .modulo[1] returns "0".
        // -->
        handlers.put("modulo", (dat, obj) -> {
            NumberTag two = NumberTag.getFor(dat.error, dat.getNextModifier());
            return new NumberTag(((NumberTag) obj).internal % two.internal);
        });
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
        return new TextTag(toString()).handle(data);
    }

    @Override
    public String toString() {
        return CoreUtilities.doubleToString(internal);
    }
}
