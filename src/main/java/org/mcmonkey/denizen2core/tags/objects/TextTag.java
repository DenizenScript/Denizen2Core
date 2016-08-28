package org.mcmonkey.denizen2core.tags.objects;

import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.Function2;

import java.util.HashMap;

public class TextTag extends AbstractTagObject {

    // <--[object]
    // @Type TextTag
    // @SubType NONE
    // @Group Mathematics
    // @Description Represents any text. Identified as exactly the input text.
    // @Other <@link explanation Text Tags>What are text tags?<@/link>
    // -->

    private String internal;

    public TextTag(String text) {
        internal = text;
    }

    public String getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name TextTag.to_integer
        // @Updated 2016/08/26
        // @Group Text Modification
        // @ReturnType IntegerTag
        // @Returns the text parsed as an integer.
        // @Example "1" .to_integer returns "1".
        // -->
        handlers.put("to_integer", (dat, obj) -> IntegerTag.getFor(dat.error, ((TextTag) obj).internal));
        // <--[tag]
        // @Name TextTag.to_number
        // @Updated 2016/08/26
        // @Group Text Modification
        // @ReturnType NumberTag
        // @Returns the text parsed as a number.
        // @Example "1" .to_number returns "1".
        // -->
        handlers.put("to_number", (dat, obj) -> NumberTag.getFor(dat.error, ((TextTag) obj).internal));
        // <--[tag]
        // @Name TextTag.to_boolean
        // @Updated 2016/08/26
        // @Group Text Modification
        // @ReturnType NumberTag
        // @Returns the text parsed as a boolean.
        // @Example "true" .to_boolean returns "true".
        // -->
        handlers.put("to_boolean", (dat, obj) -> BooleanTag.getFor(dat.error, ((TextTag) obj).internal));
    }

    public static TextTag getFor(Action<String> error, String text) {
        return new TextTag(text);
    }

    public static TextTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof TextTag) ? (TextTag) text : getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        data.error.run("Unknown tag part '" + data.getNext() + "'!");
        return new NullTag();
    }

    @Override
    public String toString() {
        return internal;
    }
}
