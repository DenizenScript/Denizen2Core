package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.HashMap;

public class ScriptTag extends AbstractTagObject {

    // <--[object]
    // @Type ScriptTag
    // @SubType TextTag
    // @Group Script Systems
    // @Description Represents a specific script. Identified by script title.
    // -->

    private CommandScript internal;

    public ScriptTag(CommandScript cs) {
        internal = cs;
    }

    public CommandScript getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name ScriptTag.title
        // @Updated 2016/08/26
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the title of the script.
        // @Example "MyTask" .title returns "MyTask".
        // -->
        handlers.put("title", (dat, obj) -> {
            return new TextTag(((ScriptTag) obj).internal.title);
        });
        // <--[tag]
        // @Name ScriptTag.yaml_key[<TextTag>]
        // @Updated 2017/02/19
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the contents of a specific yaml key, as text.
        // @Example "MyTask" .yaml_key[type] returns "task".
        // -->
        handlers.put("yaml_key", (dat, obj) -> {
            String val = ((ScriptTag) obj).internal.contents.getString(dat.getNextModifier().toString());
            if (val == null) {
                if (!dat.hasFallback()) {
                    dat.error.run("No contents for the specified YAML key! Does it exist in the script?");
                }
                return new NullTag();
            }
            return new TextTag(val);
        });
        // <--[tag]
        // @Name ScriptTag.time_ran
        // @Updated 2017/01/30
        // @Group Identification
        // @ReturnType DurationTag
        // @Returns the amount of time a script has ran for, in total.
        // -->
        handlers.put("time_ran", (dat, obj) -> {
            return new DurationTag(((ScriptTag) obj).internal.nsUsed / 1000000000.0);
        });
        // <--[tag]
        // @Name ScriptTag.ticks_ran
        // @Updated 2017/01/30
        // @Group Identification
        // @ReturnType IntegerTag
        // @Returns the number of ticks that the script has been run during.
        // -->
        handlers.put("ticks_ran", (dat, obj) -> {
            return new IntegerTag(((ScriptTag) obj).internal.ticksRan);
        });
    }

    public static ScriptTag getFor(Action<String> error, String text) {
        CommandScript cs = Denizen2Core.currentScripts.get(CoreUtilities.toLowerCase(text));
        if (cs == null) {
            error.run("Invalid script name specified!");
            return null;
        }
        return new ScriptTag(cs);
    }

    public static ScriptTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof ScriptTag) ? (ScriptTag) text : getFor(error, text.toString());
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
        return internal.title;
    }
}
