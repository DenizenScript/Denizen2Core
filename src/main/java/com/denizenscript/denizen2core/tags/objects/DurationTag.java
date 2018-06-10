package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.HashMap;

public class DurationTag extends AbstractTagObject implements Denizen2Core.NumberForm {

    // <--[explanation]
    // @Since 0.3.0
    // @Name Duration Tags
    // @Group Tags
    // @Description
    // Duration tags are a representation of a duration of time.
    // They can be specified in terms of seconds, minutes, hours, or days.
    // For example "1s" is one second, "1m" is one minute, "1h" is one hour, and "1d" is one day.
    // -->

    // <--[object]
    // @Since 0.3.0
    // @Type DurationTag
    // @SubType NumberTag
    // @Group Mathematics
    // @Description Represents a duration of time. Identified as a numeric value, with-decimal, of seconds.
    // @Note The time is internally stored as a number of seconds, using the same range as a NumberTag.
    // <@link explanation Duration Tags>What are duration tags?<@/link>
    // -->

    private double internal;

    public DurationTag(double inty) {
        internal = inty;
    }

    public double getInternal() {
        return internal;
    }

    @Override
    public double getNumberForm() {
        return internal;
    }

    public double seconds() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Since 0.3.0
        // @Name DurationTag.add_duration[<DurationTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType DurationTag
        // @Returns the duration plus another duration.
        // @Example "1" .add_duration[1] returns "2".
        // -->
        handlers.put("add_duration", (dat, obj) -> {
            DurationTag two = DurationTag.getFor(dat.checkedError, dat.getNextModifier());
            return new DurationTag(((DurationTag) obj).internal + two.internal);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name DurationTag.subtract_duration[<DurationTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType DurationTag
        // @Returns the duration minus another duration.
        // @Example "1" .subtract_duration[1] returns "0".
        // -->
        handlers.put("subtract_duration", (dat, obj) -> {
            DurationTag two = DurationTag.getFor(dat.checkedError, dat.getNextModifier());
            return new DurationTag(((DurationTag) obj).internal - two.internal);
        });
        // <--[tag]
        // @Since 0.4.0
        // @Name DurationTag.in_seconds
        // @Updated 2018/06/09
        // @Group Conversion
        // @ReturnType NumberTag
        // @Returns the duration converted to seconds.
        // @Example "40" .in_seconds returns "40".
        // -->
        handlers.put("in_seconds", (dat, obj) -> new NumberTag(((DurationTag) obj).internal));
        // <--[tag]
        // @Since 0.4.0
        // @Name DurationTag.in_minutes
        // @Updated 2018/06/09
        // @Group Conversion
        // @ReturnType NumberTag
        // @Returns the duration converted to minutes.
        // @Example "72" .in_minutes returns "1.2".
        // -->
        handlers.put("in_minutes", (dat, obj) -> new NumberTag(((DurationTag) obj).internal * (1.0 / 60.0)));
        // <--[tag]
        // @Since 0.4.0
        // @Name DurationTag.in_hours
        // @Updated 2018/06/09
        // @Group Conversion
        // @ReturnType NumberTag
        // @Returns the duration converted to hours.
        // @Example "1800" .in_hours returns "0.5".
        // -->
        handlers.put("in_hours", (dat, obj) -> new NumberTag(((DurationTag) obj).internal * (1.0 / (60.0 * 60.0))));
        // <--[tag]
        // @Since 0.4.0
        // @Name DurationTag.in_days
        // @Updated 2018/06/09
        // @Group Conversion
        // @ReturnType NumberTag
        // @Returns the duration converted to days.
        // @Example "8640" .in_days returns "0.1".
        // -->
        handlers.put("in_days", (dat, obj) -> new NumberTag(((DurationTag) obj).internal * (1.0 / (60.0 * 60.0 * 24.0))));
        // <--[tag]
        // @Since 0.4.0
        // @Name DurationTag.formatted[<TextTag>]
        // @Updated 2018/06/09
        // @Group Formatting
        // @ReturnType TextTag
        // @Returns the duration, separated in weeks, days, hours, minutes, seconds and milliseconds,
        // and formatted based on input. This tag will replace duration codes with their actual values.
        // Duration codes start with "#", followed by the unit and mode letters. Valid unit letters are:
        // t (thousandth of a second/millisecond), s (second), m (minute), h (hour), d (day) and w (week).
        // Valid mode letters are: p (precise/total double), t (total integer), d (double) and i (integer).
        // @Example "8250.350" .formatted[#hi h, #mi m, #si s and #td ms (#sp seconds in total)] returns "2 h, 17 m, 30 s and 350.0 ms (8250.35 seconds in total)".
        // -->
        handlers.put("formatted", (dat, obj) -> {
            String input = TextTag.getFor(dat.error, dat.getNextModifier()).getInternal();
            int tp = (int) (((DurationTag) obj).internal * 1000);
            if (input.contains("#t")) {
                int td = tp % 1000;
                input = input.replace("#tp", String.valueOf(tp));
                input = input.replace("#tt", String.valueOf(tp));
                input = input.replace("#td", String.valueOf(td));
                input = input.replace("#ti", String.valueOf(td));
            }
            double sp = tp * (1.0 / 1000.0);
            if (input.contains("#s")) {
                double sd = sp % 60;
                input = input.replace("#sp", String.valueOf(sp));
                input = input.replace("#st", String.valueOf((int) sp));
                input = input.replace("#sd", String.valueOf(sd));
                input = input.replace("#si", String.valueOf((int) sd));
            }
            double mp = sp * (1.0 / 60.0);
            if (input.contains("#m")) {
                double md = mp % 60;
                input = input.replace("#mp", String.valueOf(mp));
                input = input.replace("#mt", String.valueOf((int) mp));
                input = input.replace("#md", String.valueOf(md));
                input = input.replace("#mi", String.valueOf((int) md));
            }
            double hp = mp * (1.0 / 60.0);
            if (input.contains("#h")) {
                double hd = hp % 24;
                input = input.replace("#hp", String.valueOf(hp));
                input = input.replace("#ht", String.valueOf((int) hp));
                input = input.replace("#hd", String.valueOf(hd));
                input = input.replace("#hi", String.valueOf((int) hd));
            }
            double dp = hp * (1.0 / 24.0);
            if (input.contains("#d")) {
                double dd = dp % 7;
                input = input.replace("#dp", String.valueOf(dp));
                input = input.replace("#dt", String.valueOf((int) dp));
                input = input.replace("#dd", String.valueOf(dd));
                input = input.replace("#di", String.valueOf((int) dd));
            }
            double wp = dp * (1.0 / 7.0);
            if (input.contains("#w")) {
                input = input.replace("#wp", String.valueOf(wp));
                input = input.replace("#wt", String.valueOf((int) wp));
                input = input.replace("#wd", String.valueOf(wp));
                input = input.replace("#wi", String.valueOf((int) wp));
            }
            return new TextTag(input);
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
        if (text instanceof DurationTag) {
            return (DurationTag) text;
        }
        if (text instanceof Denizen2Core.NumberForm) {
            return new DurationTag(((Denizen2Core.NumberForm) text).getNumberForm());
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
        return "DurationTag";
    }

    @Override
    public String toString() {
        return CoreUtilities.doubleToString(internal);
    }
}
