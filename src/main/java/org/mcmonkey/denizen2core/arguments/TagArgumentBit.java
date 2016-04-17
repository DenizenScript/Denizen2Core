package org.mcmonkey.denizen2core.arguments;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.NullTag;
import org.mcmonkey.denizen2core.utilities.Action;

import java.util.HashMap;

public class TagArgumentBit extends ArgumentBit {

    public final TagBit[] bits;

    private AbstractTagBase start;

    private Argument fallback;

    public void setStart(AbstractTagBase base) {
        start = base;
    }

    public void setFallback(Argument arg) {
        fallback = arg;
    }

    public TagArgumentBit(TagBit[] b) {
        bits = b;
    }

    @Override
    public String getString() {
        return null;
    }

    @Override
    public AbstractTagObject parse(CommandQueue queue, HashMap<String, AbstractTagObject> vars, DebugMode mode, Action<String> error) {
        if (start == null && bits.length > 0) {
            start = Denizen2Core.tagBases.get(bits[0].key);
        }
        if (start == null) {
            error.run("Invalid tag bits -> empty tag, or invalid base.");
            return new NullTag();
        }
        TagData data = new TagData(error, bits, fallback, vars, mode, queue);
        return start.handle(data);
    }
}
