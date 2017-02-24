package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.*;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SystemTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base system
    // @Group Utilities
    // @ReturnType SystemTag
    // @Returns a generic utility class full of specific helpful system-related tags.
    // -->

    @Override
    public String getName() {
        return "system";
    }

    public SystemTagBase() {
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name SystemTag.current_time
        // @Updated 2016/08/26
        // @Group Utilities
        // @ReturnType TimeTag
        // @Returns the system's current time.
        // -->
        handlers.put("current_time", (dat, obj) -> new TimeTag(LocalDateTime.now(Clock.systemUTC())));
        // <--[tag]
        // @Name SystemTag.current_time_milliseconds
        // @Updated 2016/12/16
        // @Group Utilities
        // @ReturnType IntegerTag
        // @Returns the system's current time, as a number of milliseconds since the epoch.
        // -->
        handlers.put("current_time_milliseconds", (dat, obj) -> new IntegerTag(System.currentTimeMillis()));
        // <--[tag]
        // @Name SystemTag.core_version
        // @Updated 2016/08/26
        // @Group Denizen2
        // @ReturnType TextTag
        // @Returns the Denizen2 Core version.
        // -->
        handlers.put("core_version", (dat, obj) -> new TextTag(Denizen2Core.version));
        // <--[tag]
        // @Name SystemTag.implementation
        // @Updated 2016/08/26
        // @Group Denizen2
        // @ReturnType TextTag
        // @Returns the Denizen2 implementation's name.
        // -->
        handlers.put("implementation", (dat, obj) -> new TextTag(Denizen2Core.getImplementation().getImplementationName()));
        // <--[tag]
        // @Name SystemTag.implementation_version
        // @Updated 2016/08/26
        // @Group Denizen2
        // @ReturnType TextTag
        // @Returns the Denizen2 implementation's version.
        // -->
        handlers.put("implementation_version", (dat, obj) -> new TextTag(Denizen2Core.getImplementation().getImplementationVersion()));
        // <--[tag]
        // @Name SystemTag.queues
        // @Updated 2016/08/26
        // @Group Denizen2
        // @ReturnType ListTag
        // @Returns a list of all current queues.
        // -->
        handlers.put("queues", (dat, obj) -> {
            ListTag lt = new ListTag();
            for (CommandQueue queue : Denizen2Core.queues) {
                lt.getInternal().add(new QueueTag(queue));
            }
            return lt;
        });
        // <--[tag]
        // @Name SystemTag.yaml_files
        // @Updated 2017/02/24
        // @Group Denizen2
        // @ReturnType ListTag
        // @Returns a list of all currently loaded YAML file IDs.
        // -->
        handlers.put("yaml_files", (dat, obj) -> {
            ListTag lt = new ListTag();
            for (Map.Entry<String, Object> entry : Denizen2Core.filesLoadedByScripts.entrySet()) {
                if (entry.getValue() instanceof  YAMLConfiguration) {
                    lt.getInternal().add(new TextTag(entry.getKey()));
                }
            }
            return lt;
        });
        // <--[tag]
        // @Name SystemTag.has_yaml
        // @Updated 2017/02/24
        // @Group Denizen2
        // @ReturnType BooleanTag
        // @Returns whether the system has the specified YAML file loaded.
        // -->
        handlers.put("has_yaml", (dat, obj) -> {
            String name = dat.getNextModifier().toString();
            return new BooleanTag(Denizen2Core.filesLoadedByScripts.containsKey(CoreUtilities.toLowerCase(name)));
        });
        // <--[tag]
        // @Name SystemTag.has_file
        // @Updated 2017/02/16
        // @Group Denizen2
        // @ReturnType BooleanTag
        // @Returns whether the system has the specified file.
        // -->
        handlers.put("has_file", (dat, obj) -> {
            String path = dat.getNextModifier().toString();
            if (!Denizen2Core.getImplementation().isSafePath(path)) {
                dat.error.run("Cannot load from that path ('" + path + "'), it's marked un-safe.");
                return new NullTag();
            }
            try {
                File f = new File(Denizen2Core.getImplementation().getScriptDataFolder(), path);
                return new BooleanTag(f.exists());
            }
            catch (Exception e) {
                dat.error.run("Failed to read YAML file: " + e.getClass().getCanonicalName() + ": " + e.getMessage());
                return new NullTag();
            }
        });
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return new SystemTag().handle(data.shrink());
    }

    public class SystemTag extends AbstractTagObject {

        @Override
        public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
            return handlers;
        }

        @Override
        public AbstractTagObject handleElseCase(TagData data) {
            return new TextTag(getName());
        }
    }
}
