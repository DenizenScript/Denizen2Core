package org.mcmonkey.denizen2core.tags;

import org.mcmonkey.denizen2core.utilities.Function2;

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
        return handleElseCase(data);
    }

    public abstract AbstractTagObject handleElseCase(TagData data);
}
