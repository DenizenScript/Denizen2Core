package com.denizenscript.denizen2core.tags;

import com.denizenscript.denizen2core.tags.objects.NullTag;
import com.denizenscript.denizen2core.utilities.Function2;

import java.util.HashMap;

public abstract class AbstractTagObject {

    public abstract HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers();

    public AbstractTagObject handle(TagData data) {
        if (data.returnsTracked[data.currentIndex() - 1] == null) {
            data.returnsTracked[data.currentIndex() - 1] = this;
        }
        if (data.remaining() == 0) {
            return this;
        }
        String type = data.getNext();
        Function2<TagData, AbstractTagObject, AbstractTagObject> tagAction = getHandlers().get(type);
        if (tagAction != null) {
            try {
                AbstractTagObject curVal = tagAction.apply(data, this);
                return curVal.handle(data.shrink());
            }
            catch (TagData.TagDataEscalateException e) {
                return NullTag.NULL;
            }
        }
        AbstractTagObject ato = handleElseCase(data);
        if (ato != null) {
            return ato.handle(data);
        }
        return NullTag.NULL;
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
