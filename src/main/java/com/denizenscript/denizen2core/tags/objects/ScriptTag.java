package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.yaml.StringHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class ScriptTag extends AbstractTagObject {

    // <--[object]
    // @Since 0.3.0
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
        // @Since 0.3.0
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
        // @Since 0.3.0
        // @Name ScriptTag.has_yaml_key[<TextTag>]
        // @Updated 2017/04/27
        // @Group Identification
        // @ReturnType BooleanTag
        // @Returns whether the YAML has the specified key.
        // -->
        handlers.put("has_yaml_key", (dat, obj) -> {
            return new BooleanTag(((ScriptTag) obj).internal.contents.contains(dat.getNextModifier().toString()));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name ScriptTag.is_yaml_list[<TextTag>]
        // @Updated 2017/04/27
        // @Group Identification
        // @ReturnType BooleanTag
        // @Returns whether the YAML has the specified key and it is a list typed YAML key.
        // -->
        handlers.put("is_yaml_list", (dat, obj) -> {
            return new BooleanTag(((ScriptTag) obj).internal.contents.isList(dat.getNextModifier().toString()));
        });
        // <--[tag]
        // @Since 0.3.0
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
        // <--ScriptTag
        // @Name YamlTag.read_list[<TextTag>]
        // @Updated 2017/04/27
        // @Group Identification
        // @ReturnType ListTag
        // @Returns the contents of the YAML key, as a list of text.
        // -->
        handlers.put("yaml_list", (dat, obj) -> {
            List<String> val = ((ScriptTag) obj).internal.contents.getStringList(dat.getNextModifier().toString());
            if (val == null) {
                if (!dat.hasFallback()) {
                    dat.error.run("No valid list at the specified YAML key! Does it exist?");
                }
                return new NullTag();
            }
            ListTag list = new ListTag();
            for (String str : val) {
                list.getInternal().add(new TextTag(str));
            }
            return list;
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name ScriptTag.list_keys[<TextTag>]
        // @Updated 2017/02/19
        // @Group Identification
        // @ReturnType TextTag
        // @Returns the contents of a specific yaml key, as a list of keys.
        // @Example "MyTask" .yaml_key[type] returns "task".
        // -->
        handlers.put("list_keys", (dat, obj) -> {
            Set<StringHolder> val = ((ScriptTag) obj).internal.contents.getConfigurationSection(dat.getNextModifier().toString()).getKeys(false);
            if (val == null) {
                if (!dat.hasFallback()) {
                    dat.error.run("No valid keys at the specified YAML key! Does it exist?");
                }
                return new NullTag();
            }
            ListTag list = new ListTag();
            for (StringHolder str : val) {
                list.getInternal().add(new TextTag(str.str));
            }
            return list;
        });
        // <--[tag]
        // @Since 0.3.0
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
        // @Since 0.3.0
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
    public String getTagTypeName() {
        return "ScriptTag";
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
