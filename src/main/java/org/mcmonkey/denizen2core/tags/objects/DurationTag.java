package org.mcmonkey.denizen2core.tags.objects;

import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.Function2;

import java.util.HashMap;

public class DurationTag extends AbstractTagObject {

    // <--[explanation]
    // @Name Duration Tags
    // @Group Tags
    // @Description
    // Duration tags are a representation of a duration of time.
    // They can be specified in terms of seconds, minutes, hours, or days.
    // For example "1s" is one second, "1m" is one minute, "1h" is one hour, and "1d" is one day.
    // -->

    // <--[object]
    // @Type DurationTag
    // @SubType NumberTag
    // @Group Mathematics
    // @Description Represents a duration of time.
    // @Other Note that the time is internally stored as a number of seconds, using the same range as a NumberTag.
    // <@link explanation Duration Tags>What are duration tags?<@/link>
    // -->

    private double internal;

    public DurationTag(double inty) {
        internal = inty;
    }

    public double getInternal() {
        return internal;
    }

    public double seconds() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name DurationTag.add_duration[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType DurationTag
        // @Returns the duration plus another duration.
        // @Example "1" .add_duration[1] returns "2".
        // -->
        handlers.put("add_duration", (dat, obj) -> {
            DurationTag two = DurationTag.getFor(dat.error, dat.getNextModifier());
            return new DurationTag(((DurationTag) obj).internal + two.internal);
        });
        // <--[tag]
        // @Name DurationTag.subtract_duration[<NumberTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType DurationTag
        // @Returns the duration minus another duration.
        // @Example "1" .subtract_duration[1] returns "0".
        // -->
        handlers.put("subtract_duration", (dat, obj) -> {
            DurationTag two = DurationTag.getFor(dat.error, dat.getNextModifier());
            return new DurationTag(((DurationTag) obj).internal - two.internal);
        });
    }

    public static DurationTag getFor(Action<String> error, String text) {
        try {
            if (text.endsWith("s")) {
                double d = Double.parseDouble(text.substring(0, text.length() - 1));
                return new DurationTag(d);
            }
            else if (text.endsWith("m")) {
                double d = Double.parseDouble(text.substring(0, text.length() - 1));
                return new DurationTag(d * 60.0);
            }
            else if (text.endsWith("h")) {
                double d = Double.parseDouble(text.substring(0, text.length() - 1));
                return new DurationTag(d * 60.0 * 60.0);
            }
            else if (text.endsWith("d")) {
                double d = Double.parseDouble(text.substring(0, text.length() - 1));
                return new DurationTag(d * 60.0 * 60.0 * 24.0);
            }
            else {
                double d = Double.parseDouble(text);
                return new DurationTag(d);
            }
        }
        catch (NumberFormatException ex) {
            error.run("Invalid DurationTag input!");
            return null;
        }
    }

    public static DurationTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof DurationTag) ? (DurationTag) text : getFor(error, text.toString());
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
