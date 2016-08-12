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
        // @Group Information
        // @ReturnType BooleanTag
        // @Returns whether the queue is still running.
        // @Example "1" .running may return "true".
        // -->
        handlers.put("running", (dat, obj) -> {
            return new BooleanTag(((QueueTag) obj).internal.running);
        });
        // <--[tag]
        // @Name QueueTag.current_script
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
        // @Name QueueTag.has_definition[<TextTag>]
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
