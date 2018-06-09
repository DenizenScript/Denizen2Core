package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.Function2;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

public class TimeTag extends AbstractTagObject implements Denizen2Core.IntegerForm, Denizen2Core.NumberForm {

    // <--[object]
    // @Since 0.3.0
    // @Type TimeTag
    // @SubType IntegerTag
    // @Group Mathematics
    // @Description Represents a date-time. Identified as an integer number, representing milliseconds since Jan 1st, 1970, 00:00 UTC.
    // @Note All time values are UTC, except where explicitly localized!
    // -->

    private LocalDateTime internal;

    public TimeTag(LocalDateTime inty) {
        internal = inty;
    }

    public LocalDateTime getInternal() {
        return internal;
    }

    @Override
    public long getIntegerForm() {
        return getInternalInteger();
    }

    @Override
    public double getNumberForm() {
        return getInternalInteger();
    }

    public long getInternalInteger() {
        return internal.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Since 0.3.0
        // @Name TimeTag.year
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the year represented by this date.
        // @Example "0" .year returns "1970".
        // -->
        handlers.put("year", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getYear()));
        // <--[tag]
        // @Since 0.3.0
        // @Name TimeTag.month
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the month represented by this date.
        // @Example "0" .month returns "1".
        // -->
        handlers.put("month", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getMonthValue()));
        // <--[tag]
        // @Since 0.3.0
        // @Name TimeTag.month_name
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType TextTag
        // @Returns the name of the month represented by this date.
        // @Example "0" .month returns "JANUARY".
        // -->
        handlers.put("month_name", (dat, obj) -> new TextTag(((TimeTag) obj).internal.getMonth().name()));
        // <--[tag]
        // @Since 0.3.0
        // @Name TimeTag.day
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the day represented by this date.
        // @Example "0" .day returns "1".
        // -->
        handlers.put("day", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getDayOfMonth()));
        // <--[tag]
        // @Since 0.3.0
        // @Name TimeTag.day_of_year
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the day of the year represented by this date.
        // @Example "0" .day_of_year returns "1".
        // -->
        handlers.put("day_of_year", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getDayOfYear()));
        // <--[tag]
        // @Since 0.3.0
        // @Name TimeTag.day_of_week
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the day of the week represented by this date.
        // @Example "0" .day_of_week returns "1".
        // -->
        handlers.put("day_of_week", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getDayOfWeek().getValue()));
        // <--[tag]
        // @Since 0.3.0
        // @Name TimeTag.day_of_week_name
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the name of the day of the week represented by this date.
        // -->
        handlers.put("day_of_week_name", (dat, obj) -> new TextTag(((TimeTag) obj).internal.getDayOfWeek().name()));
        // <--[tag]
        // @Since 0.3.0
        // @Name TimeTag.hour
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the hour of the day represented by this date.
        // -->
        handlers.put("hour", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getHour()));
        // <--[tag]
        // @Since 0.3.0
        // @Name TimeTag.minute
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the minute of the hour represented by this date.
        // -->
        handlers.put("minute", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getMinute()));
        // <--[tag]
        // @Since 0.3.0
        // @Name TimeTag.second
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the second of the minute represented by this date.
        // -->
        handlers.put("second", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getSecond()));
        // <--[tag]
        // @Since 0.3.0
        // @Name TimeTag.total_milliseconds
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the total number of milliseconds since the epoch.
        // -->
        handlers.put("total_milliseconds", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.toInstant(ZoneOffset.UTC).toEpochMilli()));
    }

    public static TimeTag getForInteger(Action<String> error, long val) {
        try {
            return new TimeTag(LocalDateTime.ofInstant(Instant.ofEpochMilli(val), ZoneOffset.UTC));
        }
        catch (DateTimeException ex) {
            error.run("Invalid TimeTag input (invalid datetime result)!");
            return null;
        }
    }

    public static TimeTag getFor(Action<String> error, String text) {
        try {
            long l = Long.parseLong(text);
            return getForInteger(error, l);
        }
        catch (NumberFormatException ex) {
            error.run("Invalid TimeTag input (not an integer)!");
            return null;
        }
    }

    public static TimeTag getFor(Action<String> error, AbstractTagObject text) {
        if (text instanceof TimeTag) {
            return (TimeTag) text;
        }
        if (text instanceof Denizen2Core.IntegerForm) {
            return getForInteger(error, ((Denizen2Core.IntegerForm) text).getIntegerForm());
        }
        return getFor(error, text.toString());
    }

    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss[z]", Locale.ENGLISH);

    @Override
    public String debug() {
        return internal.format(dateFormatter);
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new IntegerTag(getInternalInteger());
    }

    @Override
    public String getTagTypeName() {
        return "TimeTag";
    }

    @Override
    public String toString() {
        return String.valueOf(getInternalInteger());
    }
}
