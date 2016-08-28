package org.mcmonkey.denizen2core.tags.objects;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.scripts.CommandScript;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.Function2;

import java.util.HashMap;

public class QueueTag extends AbstractTagObject {

    // <--[object]
    // @Type QueueTag
    // @SubType TextTag
    // @Group Script Systems
    // @Description Represents a running command queue.
    // -->

    private CommandQueue internal;

    public QueueTag(CommandQueue q) {
        internal = q;
    }

    public CommandQueue getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
        // <--[tag]
        // @Name QueueTag.id
        // @Updated 2016/08/26
        // @Group Identification
        // @ReturnType IntegerTag
        // @Returns the integer ID of the queue.
        // @Example "1" .id returns "1".
        // -->
        handlers.put("id", (dat, obj) -> {
            return new IntegerTag(((QueueTag) obj).internal.qID);
        });
        // <--[tag]
        // @Name QueueTag.running
        // @Updated 2016/08/26
        // @Group Information
        // @ReturnType BooleanTag
        // @Returns whether the queue is still running.
        // @Example "1" .running may return "true".
        // -->
        handlers.put("running", (dat, obj) -> {
            return new BooleanTag(((QueueTag) obj).internal.running);
        });
        // <--[tag]
        // @Name QueueTag.determinations
        // @Updated 2016/08/27
        // @Group Information
        // @ReturnType MapTag
        // @Returns a map of all determinations on the queue.
        // @Example "1" .determinations may return "a:b|1:2|".
        // -->
        handlers.put("determinations", (dat, obj) -> {
            return ((QueueTag) obj).internal.determinations;
        });
        // <--[tag]
        // @Name QueueTag.current_script
        // @Updated 2016/08/26
        // @Group Information
        // @ReturnType ScriptTag
        // @Returns the script currently running on the queue. If none is available, returns a NullTag!
        // @Example "1" .current_script may return "MyTask".
        // -->
        handlers.put("current_script", (dat, obj) -> {
            CommandScript cs = ((QueueTag) obj).internal.commandStack.peek().originalScript;
            if (cs == null) {
                return new NullTag();
            }
            return new ScriptTag(cs);
        });
        // <--[tag]
        // @Name QueueTag.base_script
        // @Updated 2016/08/26
        // @Group Information
        // @ReturnType ScriptTag
        // @Returns the script that ran first on the queue. If none is available, returns a NullTag!
        // @Example "1" .base_script may return "MyTask".
        // -->
        handlers.put("base_script", (dat, obj) -> {
            if (((QueueTag) obj).internal.commandStack.size() == 0) {
                return new NullTag();
            }
            CommandScript cs = ((QueueTag) obj).internal.commandStack.firstElement().originalScript;
            if (cs == null) {
                return new NullTag();
            }
            return new ScriptTag(cs);
        });
        // <--[tag]
        // @Name QueueTag.has_definition[<TextTag>]
        // @Updated 2016/08/26
        // @Group Information
        // @ReturnType BooleanTag
        // @Returns whether the queue has the specified definition.
        // @Example "1" .has_definition[value] may return "true".
        // -->
        handlers.put("has_definition", (dat, obj) -> {
            return new BooleanTag(((QueueTag) obj).internal.commandStack.peek().hasDefinition(dat.getNextModifier().toString()));
        });
        // <--[tag]
        // @Name QueueTag.definition[<TextTag>]
        // @Updated 2016/08/26
        // @Group Information
        // @ReturnType Dynamic
        // @Returns the value of the specified definition on the queue.
        // @Example "1" .definition[value] may return "3".
        // -->
        handlers.put("definition", (dat, obj) -> {
            return ((QueueTag) obj).internal.commandStack.peek().getDefinition(dat.getNextModifier().toString());
        });
    }

    public static QueueTag getFor(Action<String> error, String text) {
        try {
            long l = Long.parseLong(text);
            for (CommandQueue queue : Denizen2Core.queues) {
                if (queue.qID == l) {
                    return new QueueTag(queue);
                }
            }
            error.run("Unknown queue specified!");
            return null;
        }
        catch (NumberFormatException ex) {
            error.run("Invalid IntegerTag input!");
            return null;
        }
    }

    public static QueueTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof QueueTag) ? (QueueTag) text : getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new TextTag(toString()).handle(data);
    }

    @Override
    public String toString() {
        return String.valueOf(internal.qID);
    }
}
