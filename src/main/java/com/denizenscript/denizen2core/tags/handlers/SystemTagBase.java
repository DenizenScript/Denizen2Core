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
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SystemTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Since 0.3.0
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
        // @Since 0.3.0
        // @Name SystemTag.pi
        // @Updated 2017/10/18
        // @Group Utilities
        // @ReturnType NumberTag
        // @Returns the approximate value of mathematical constant pi,
        // which is the ratio of a circle's circumference to its diameter.
        // This returns the constant value "3.14159265358979323846" as a NumberTag.
        // -->
        handlers.put("pi", (dat, obj) -> new NumberTag(Math.PI));
        // <--[tag]
        // @Since 0.5.0
        // @Name SystemTag.e
        // @Updated 2018/06/09
        // @Group Utilities
        // @ReturnType NumberTag
        // @Returns the approximate value of mathematical constant e,
        // which is the base number for natural logarithms. This returns
        // the constant value "2.71828182845904523536" as a NumberTag.
        // -->
        handlers.put("e", (dat, obj) -> new NumberTag(Math.E));
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.current_time
        // @Updated 2016/08/26
        // @Group Utilities
        // @ReturnType TimeTag
        // @Returns the system's current time.
        // -->
        handlers.put("current_time", (dat, obj) -> new TimeTag(LocalDateTime.now(Clock.systemUTC())));
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.current_time_milliseconds
        // @Updated 2016/12/16
        // @Group Utilities
        // @ReturnType IntegerTag
        // @Returns the system's current time, as a number of milliseconds since the epoch.
        // -->
        handlers.put("current_time_milliseconds", (dat, obj) -> new IntegerTag(System.currentTimeMillis()));
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.core_version
        // @Updated 2016/08/26
        // @Group Denizen2
        // @ReturnType TextTag
        // @Returns the Denizen2 Core version.
        // -->
        handlers.put("core_version", (dat, obj) -> new TextTag(Denizen2Core.version));
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.implementation
        // @Updated 2016/08/26
        // @Group Denizen2
        // @ReturnType TextTag
        // @Returns the Denizen2 implementation's name.
        // -->
        handlers.put("implementation", (dat, obj) -> new TextTag(Denizen2Core.getImplementation().getImplementationName()));
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.implementation_version
        // @Updated 2016/08/26
        // @Group Denizen2
        // @ReturnType TextTag
        // @Returns the Denizen2 implementation's version.
        // -->
        handlers.put("implementation_version", (dat, obj) -> new TextTag(Denizen2Core.getImplementation().getImplementationVersion()));
        // <--[tag]
        // @Since 0.3.0
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
        // @Since 0.3.0
        // @Name SystemTag.yaml_files
        // @Updated 2017/02/24
        // @Group Denizen2
        // @ReturnType ListTag
        // @Returns a list of all currently loaded YAML file IDs.
        // -->
        handlers.put("yaml_files", (dat, obj) -> {
            ListTag lt = new ListTag();
            for (Map.Entry<String, Object> entry : Denizen2Core.filesLoadedByScripts.entrySet()) {
                if (entry.getValue() instanceof YAMLConfiguration) {
                    lt.getInternal().add(new TextTag(entry.getKey()));
                }
            }
            return lt;
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.has_yaml[<TextTag>]
        // @Updated 2017/02/24
        // @Group Denizen2
        // @ReturnType BooleanTag
        // @Returns whether the system has the specified YAML file loaded.
        // -->
        handlers.put("has_yaml", (dat, obj) -> {
            String name = dat.getNextModifier().toString();
            return BooleanTag.getForBoolean(Denizen2Core.filesLoadedByScripts.containsKey(CoreUtilities.toLowerCase(name)));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.has_file[<TextTag>]
        // @Updated 2017/02/16
        // @Group Denizen2
        // @ReturnType BooleanTag
        // @Returns whether the system has the specified file.
        // -->
        handlers.put("has_file", (dat, obj) -> {
            String path = dat.getNextModifier().toString();
            if (!Denizen2Core.getImplementation().isSafePath(path)) {
                dat.error.run("Cannot load from that path ('" + path + "'), it's marked un-safe.");
                return NullTag.NULL;
            }
            try {
                File f = new File(Denizen2Core.getImplementation().getScriptDataFolder(), path);
                return BooleanTag.getForBoolean(f.exists());
            }
            catch (Exception e) {
                dat.error.run("Failed to read YAML file: " + e.getClass().getCanonicalName() + ": " + e.getMessage());
                return NullTag.NULL;
            }
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.random_decimal_in_range[<ListTag>]
        // @Updated 2017/05/05
        // @Group Random
        // @ReturnType NumberTag
        // @Returns a random decimal number larger than the first number and smaller than the second number input.
        // -->
        handlers.put("random_decimal_in_range", (dat, obj) -> {
            ListTag inp = ListTag.getFor(dat.checkedError, dat.getNextModifier());
            if (inp.getInternal().size() != 2) {
                dat.error.run("Invalid input! Not a list of size 2!");
                return NullTag.NULL;
            }
            double a = NumberTag.getFor(dat.checkedError, inp.getInternal().get(0)).getInternal();
            double b = NumberTag.getFor(dat.checkedError, inp.getInternal().get(1)).getInternal();
            if (b < a) {
                dat.error.run("Invalid input! Second number less than first!");
                return NullTag.NULL;
            }
            return new NumberTag((CoreUtilities.random.nextDouble() * (b - a)) + a);
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.random_integer_in_range[<ListTag>]
        // @Updated 2017/05/05
        // @Group Random
        // @ReturnType IntegerTag
        // @Returns a random integer number larger than the first number and smaller than the second number input.
        // -->
        handlers.put("random_integer_in_range", (dat, obj) -> {
            ListTag inp = ListTag.getFor(dat.checkedError, dat.getNextModifier());
            if (inp.getInternal().size() != 2) {
                dat.error.run("Invalid input! Not a list of size 2!");
                return NullTag.NULL;
            }
            long a = IntegerTag.getFor(dat.checkedError, inp.getInternal().get(0)).getInternal();
            long b = IntegerTag.getFor(dat.checkedError, inp.getInternal().get(1)).getInternal();
            if (b < a) {
                dat.error.run("Invalid input! Second number less than first!");
                return NullTag.NULL;
            }
            return new IntegerTag((CoreUtilities.random.nextInt((int) (b - a)) + a));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.random_integer_massive
        // @Updated 2017/05/05
        // @Group Random
        // @ReturnType IntegerTag
        // @Returns a random integer number, from a range of all possible 64 bit integers! (Including negatives!)
        // -->
        handlers.put("random_integer_massive", (dat, obj) -> {
            return new IntegerTag((CoreUtilities.random.nextLong()));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.random_decimal_gaussian
        // @Updated 2017/05/05
        // @Group Random
        // @ReturnType NumberTag
        // @Returns a random gaussian-distributed decimal number. (IE random negative or positive, tends to be in range -1 to 1, but sometimes goes outside.)
        // -->
        handlers.put("random_decimal_gaussian", (dat, obj) -> {
            return new NumberTag((CoreUtilities.random.nextGaussian()));
        });
        // <--[tag]
        // @Since 0.3.0
        // @Name SystemTag.random_boolean
        // @Updated 2017/05/05
        // @Group Random
        // @ReturnType BooleanTag
        // @Returns a randomly selected true or false value.
        // -->
        handlers.put("random_boolean", (dat, obj) -> {
            return BooleanTag.getForBoolean((CoreUtilities.random.nextBoolean()));
        });
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return new SystemTag().handle(data.shrink());
    }

    public static class SystemTag extends AbstractTagObject {

        @Override
        public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
            return handlers;
        }

        @Override
        public AbstractTagObject handleElseCase(TagData data) {
            return new TextTag("system");
        }

        @Override
        public String getTagTypeName() {
            return "SystemTag";
        }
    }
}
