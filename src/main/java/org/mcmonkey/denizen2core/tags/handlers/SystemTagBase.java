package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.*;
import org.mcmonkey.denizen2core.utilities.Function2;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;

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
        // @Group Utilities
        // @ReturnType TimeTag
        // @Returns the system's current time.
        // -->
        handlers.put("current_time", (dat, obj) -> new TimeTag(LocalDateTime.now(Clock.systemUTC())));
        // <--[tag]
        // @Name SystemTag.core_version
        // @Group Denizen2
        // @ReturnType TextTag
        // @Returns the Denizen2 Core version.
        // -->
        handlers.put("core_version", (dat, obj) -> new TextTag(Denizen2Core.version));
        // <--[tag]
        // @Name SystemTag.implementation
        // @Group Denizen2
        // @ReturnType TextTag
        // @Returns the Denizen2 implementation's name.
        // -->
        handlers.put("implementation", (dat, obj) -> new TextTag(Denizen2Core.getImplementation().getImplementationName()));
        // <--[tag]
        // @Name SystemTag.implementation_version
        // @Group Denizen2
        // @ReturnType TextTag
        // @Returns the Denizen2 implementation's version.
        // -->
        handlers.put("implementation_version", (dat, obj) -> new TextTag(Denizen2Core.getImplementation().getImplementationVersion()));
        // <--[tag]
        // @Name SystemTag.queues
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
            return new TextTag(getName()).handle(data);
        }
    }
}
