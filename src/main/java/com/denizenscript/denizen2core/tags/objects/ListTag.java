package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.handlers.EscapeTagBase;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.arguments.TextArgumentBit;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListTag extends AbstractTagObject {

    // <--[object]
// @Since 0.3.0
    // @Type ListTag
    // @SubType TextTag
    // @Group Mathematics
    // @Description Represents a list of objects. Identified as a list of escaped entries separated by pipes.
    // -->

    private List<AbstractTagObject> internal;

    public ListTag() {
        internal = new ArrayList<>();
    }

    public ListTag(List<AbstractTagObject> inty) {
        internal = new ArrayList<>(inty);
    }

    public List<AbstractTagObject> getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
// @Since 0.3.0
        // @Name ListTag.get[<IntegerTag>]
        // @Updated 2016/08/26
        // @Group Lists
        // @ReturnType Dynamic
        // @Returns the object at the specified index in the list.
        // @Example "one|two|three|" .get[1] returns "one".
        // -->
        handlers.put("get", (dat, obj) -> {
            IntegerTag ind = IntegerTag.getFor(dat.error, dat.getNextModifier());
            int i = (int) ind.getInternal() - 1;
            if (i < 0 || i >= ((ListTag) obj).internal.size()) {
                if (!dat.hasFallback()) {
                    dat.error.run("Invalid ListTag.GET[...] index!");
                }
                return new NullTag();
            }
            return ((ListTag) obj).internal.get(i);
        });
        // <--[tag]
// @Since 0.3.0
        // @Name ListTag.contains[<TextTag>]
        // @Updated 2017/03/08
        // @Group Lists
        // @ReturnType BooleanTag
        // @Returns whether the list contains the specified text, case insensitive.
        // @Example "one|two|three|" .contains[ONE] returns "true".
        // -->
        handlers.put("contains", (dat, obj) -> {
            String contain_check = CoreUtilities.toLowerCase(dat.getNextModifier().toString());
            ListTag list = (ListTag) obj;
            for (int i = 0; i < list.getInternal().size(); i++) {
                if (CoreUtilities.toLowerCase(list.getInternal().get(i).toString()).equals(contain_check)) {
                    return new BooleanTag(true);
                }
            }
            return new BooleanTag(false);
        });
        // <--[tag]
// @Since 0.3.0
        // @Name ListTag.contains_cased[<TextTag>]
        // @Updated 2017/03/08
        // @Group Lists
        // @ReturnType BooleanTag
        // @Returns whether the list contains the specified text, case sensitive.
        // @Example "one|two|three|" .contains_cased[one] returns "true".
        // -->
        handlers.put("contains_cased", (dat, obj) -> {
            String contain_check = dat.getNextModifier().toString();
            ListTag list = (ListTag) obj;
            for (int i = 0; i < list.getInternal().size(); i++) {
                if (list.getInternal().get(i).toString().equals(contain_check)) {
                    return new BooleanTag(true);
                }
            }
            return new BooleanTag(false);
        });
        // <--[tag]
// @Since 0.3.0
        // @Name ListTag.size
        // @Updated 2016/11/24
        // @Group Lists
        // @ReturnType IntegerTag
        // @Returns the size (number of entries) of the list.
        // @Example "one|two|three|" .size returns "3".
        // -->
        handlers.put("size", (dat, obj) -> new IntegerTag(((ListTag) obj).getInternal().size()));
        // <--[tag]
// @Since 0.3.0
        // @Name ListTag.random
        // @Updated 2016/09/28
        // @Group Randomization
        // @ReturnType Dynamic
        // @Returns the object at a random index in the list.
        // @Example "one|two|three|" .random might return "one", "two", or "three".
        // -->
        handlers.put("random", (dat, obj) -> {
            int size = ((ListTag) obj).internal.size();
            if (size <= 0) {
                if (!dat.hasFallback()) {
                    dat.error.run("Empty list can't be read from for random tag!");
                }
                return new NullTag();
            }
            return ((ListTag) obj).internal.get(CoreUtilities.random.nextInt(size));
        });
    }

    public static ListTag getForSaved(Action<String> error, String text) {
        List<String> strs = CoreUtilities.split(text, '|');
        ListTag lt = new ListTag();
        for (int i = 0; i < strs.size(); i++) {
            if (i == strs.size() - 1 && strs.get(i).length() == 0) {
                break;
            }
            String data = EscapeTagBase.unescape(strs.get(i));
            lt.internal.add(Denizen2Core.loadFromSaved(error, data));
        }
        return lt;
    }

    public static ListTag getFor(Action<String> error, String text) {
        List<String> strs = CoreUtilities.split(text, '|');
        ListTag lt = new ListTag();
        for (int i = 0; i < strs.size(); i++) {
            if (i == strs.size() - 1 && strs.get(i).length() == 0) {
                break;
            }
            String data = EscapeTagBase.unescape(strs.get(i));
            TextArgumentBit tab = new TextArgumentBit(data, false);
            lt.internal.add(tab.value);
        }
        return lt;
    }

    public static ListTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof ListTag) ? (ListTag) text : getFor(error, text.toString());
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
        for (int i = 0; i < internal.size(); i++) {
            sb.append(EscapeTagBase.escape(internal.get(i).toString())).append("|");
        }
        return sb.toString();
    }

    @Override
    public String getTagTypeName() {
        return "ListTag";
    }

    @Override
    public String savable() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTagTypeName()).append(saveMark());
        for (int i = 0; i < internal.size(); i++) {
            sb.append(EscapeTagBase.escape(internal.get(i).savable())).append("|");
        }
        return sb.toString();
    }

    @Override
    public String debug() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < internal.size(); i++) {
            sb.append(internal.get(i).debug()).append(" | ");
        }
        return sb.toString();
    }
}
