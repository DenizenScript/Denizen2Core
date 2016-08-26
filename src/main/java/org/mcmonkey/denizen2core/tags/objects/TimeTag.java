package org.mcmonkey.denizen2core.tags.objects;

import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.Function2;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TimeTag extends AbstractTagObject {

    // <--[object]
    // @Type TimeTag
    // @SubType IntegerTag
    // @Group Mathematics
    // @Description Represents a date-time.
    // @Other Note that the date-time is internally handled as an IntegerTag, representing milliseconds since Jan 1st, 1970, 00:00 UTC.
    // @Other Note that all time values are UTC!
    // -->

    private LocalDateTime internal;

    public TimeTag(LocalDateTime inty) {
        internal = inty;
    }

    public LocalDateTime getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name IntegerTag.year
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the year represented by this date.
        // @Example "0" .year returns "1970".
        // -->
        handlers.put("year", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getYear()));
        // <--[tag]
        // @Name IntegerTag.month
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the month represented by this date.
        // @Example "0" .month returns "1".
        // -->
        handlers.put("month", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getMonthValue()));
        // <--[tag]
        // @Name IntegerTag.month_name
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType TextTag
        // @Returns the name of the month represented by this date.
        // @Example "0" .month returns "JANUARY".
        // -->
        handlers.put("month_name", (dat, obj) -> new TextTag(((TimeTag) obj).internal.getMonth().name()));
        // <--[tag]
        // @Name IntegerTag.day
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the day represented by this date.
        // @Example "0" .day returns "1".
        // -->
        handlers.put("day", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getDayOfMonth()));
        // <--[tag]
        // @Name IntegerTag.day_of_year
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the day of the year represented by this date.
        // @Example "0" .day_of_year returns "1".
        // -->
        handlers.put("day_of_year", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getDayOfYear()));
        // <--[tag]
        // @Name IntegerTag.day_of_week
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the day of the week represented by this date.
        // @Example "0" .day_of_week returns "1".
        // -->
        handlers.put("day_of_week", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getDayOfWeek().getValue()));
        // <--[tag]
        // @Name IntegerTag.day_of_week_name
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the name of the day of the week represented by this date.
        // -->
        handlers.put("day_of_week_name", (dat, obj) -> new TextTag(((TimeTag) obj).internal.getDayOfWeek().name()));
        // <--[tag]
        // @Name IntegerTag.hour
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the hour of the day represented by this date.
        // -->
        handlers.put("hour", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getHour()));
        // <--[tag]
        // @Name IntegerTag.minute
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the minute of the hour represented by this date.
        // -->
        handlers.put("minute", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getMinute()));
        // <--[tag]
        // @Name IntegerTag.second
        // @Updated 2016/08/26
        // @Group General Information
        // @ReturnType IntegerTag
        // @Returns the second of the minute represented by this date.
        // -->
        handlers.put("second", (dat, obj) -> new IntegerTag(((TimeTag) obj).internal.getSecond()));
    }

    public static TimeTag getFor(Action<String> error, String text) {
        try {
            long l = Long.parseLong(text);
            return new TimeTag(LocalDateTime.ofEpochSecond(l, 0, ZoneOffset.UTC));
        }
        catch (NumberFormatException ex) {
            error.run("Invalid TimeTag input!");
            return null;
        }
    }

    public static TimeTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof TimeTag) ? (TimeTag) text : getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new NumberTag(internal.toEpochSecond(ZoneOffset.UTC)).handle(data);
    }

    @Override
    public String toString() {
        return String.valueOf(internal.toEpochSecond(ZoneOffset.UTC));
    }
}
