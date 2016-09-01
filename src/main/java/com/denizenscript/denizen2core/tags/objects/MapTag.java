package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.handlers.EscapeTagBase;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.arguments.TextArgumentBit;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTag extends AbstractTagObject {

    // <--[object]
    // @Type MapTag
    // @SubType TextTag
    // @Group Mathematics
    // @Description Represents a map of names to objects. Identifed as a list of escaped key:value pairs, separated by pipes.
    // -->

    private HashMap<String, AbstractTagObject> internal;

    public MapTag() {
        internal = new HashMap<>();
    }

    public MapTag(HashMap<String, AbstractTagObject> objs) {
        internal = new HashMap<>(objs);
    }

    public HashMap<String, AbstractTagObject> getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name MapTag.get[<IntegerTag>]
        // @Updated 2016/08/26
        // @Group Mathematics
        // @ReturnType Dynamic
        // @Returns the object with the specified name in the map.
        // @Example "one:a|two:b|three:c|" .get[one] returns "a".
        // -->
        handlers.put("get", (dat, obj) -> {
            TextTag ind = TextTag.getFor(dat.error, dat.getNextModifier());
            String key = ind.getInternal();
            AbstractTagObject ato = ((MapTag) obj).internal.get(CoreUtilities.toLowerCase(key));
            if (ato == null) {
                return new NullTag();
            }
            return ato;
        });
    }

    public static MapTag getFor(Action<String> error, String text) {
        List<String> strs = CoreUtilities.split(text, '|');
        MapTag lt = new MapTag();
        for (int i = 0; i < strs.size(); i++) {
            if (i == strs.size() - 1 && strs.get(i).length() == 0) {
                break;
            }
            List<String> datums = CoreUtilities.split(strs.get(i), ':', 2);
            String key = EscapeTagBase.unescape(datums.get(0));
            if (datums.size() < 2) {
                error.run("Invalid map tag input!");
            }
            String data = EscapeTagBase.unescape(datums.get(1));
            TextArgumentBit tab = new TextArgumentBit(data, false);
            lt.internal.put(key, tab.value);
        }
        return lt;
    }

    public static MapTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof MapTag) ? (MapTag) text : getFor(error, text.toString());
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
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, AbstractTagObject> obj : internal.entrySet()) {
            sb.append(EscapeTagBase.escape(obj.getKey())).append(":")
                    .append(EscapeTagBase.escape(obj.getValue().toString())).append("|");
        }
        return sb.toString();
    }
}
