package com.denizenscript.denizen2core.tags;

import com.denizenscript.denizen2core.tags.objects.NullTag;
import com.denizenscript.denizen2core.utilities.Function2;

import java.util.HashMap;

public abstract class AbstractTagObject {

    public abstract HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers();

    public AbstractTagObject handle(TagData data) {
        if (data.remaining() == 0) {
            return this;
        }
        String type = data.getNext();
        Function2<TagData, AbstractTagObject, AbstractTagObject> handle = getHandlers().get(type);
        if (handle != null) {
            return handle.apply(data, this).handle(data.shrink());
        }
        AbstractTagObject ato = handleElseCase(data);
        if (ato != null) {
            return ato.handle(data);
        }
        return new NullTag();
    }

    public abstract AbstractTagObject handleElseCase(TagData data);

    public abstract String getTagTypeName();

    public String debug() {
        return toString();
    }

    public static String saveMark() {
        return "@";
    }

    /**
     * Only override this if you have sub-tag-objects within your object
     * (For example, if it's a list of other objects!)
     */
    public String savable() {
        return getTagTypeName() + saveMark() + toString();
    }
}
