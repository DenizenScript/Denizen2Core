package org.mcmonkey.denizen2core.events;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.scripts.commontypes.WorldScript;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.objects.BooleanTag;
import org.mcmonkey.denizen2core.tags.objects.IntegerTag;
import org.mcmonkey.denizen2core.tags.objects.MapTag;
import org.mcmonkey.denizen2core.tags.objects.TextTag;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.Tuple;
import org.mcmonkey.denizen2core.utilities.debugging.ColorSet;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;
import org.mcmonkey.denizen2core.utilities.yaml.StringHolder;

import java.util.*;

public abstract class ScriptEvent implements Cloneable {

    @Override
    public ScriptEvent clone() {
        try {
            return (ScriptEvent) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            Debug.exception(ex);
            return null;
        }
    }

    public final static List<WorldScript> currentWorldScripts = new ArrayList<>();

    public static class ScriptEventData {

        public WorldScript script;

        public String eventPath;

        public int priority;
    }

    // <--[explanation]
    // @Name Script Event Priority
    // @Group Events
    // @Description
    // Any ScriptEvent can take a "priority:#" argument.
    // For example: "on object does something priority:3:"
    // The priority indicates which order the events will fire in.
    // Lower numbers fire earlier. EG, -1 fires before 0 fires before 1.
    // Any integer number, within reason, is valid. (IE, -1 is fine, 100000 is fine,
    // but 200000000000 is not, and 1.5 is not as well)
    // The default priority is 0.
    // -->

    public void sort() {
        for (ScriptEventData path : usages) {
            String gotten = getSwitch(path.eventPath, "priority");
            path.priority = gotten == null ? 0 : (int) IntegerTag.getFor((e) -> {
                throw new RuntimeException("Invalid integer specified: " + e);
            }, gotten).getInternal();
        }
        Collections.sort(usages, (t1, t2) -> {
            int rel = t1.priority - t2.priority;
            return rel < 0 ? -1 : (rel > 0 ? 1 : 0);
        });
    }

    public static String getSwitch(String event, String switcher) {
        for (String possible : CoreUtilities.split(event, ' ')) {
            List<String> split = CoreUtilities.split(possible, ':', 2);
            if (split.size() > 1 && CoreUtilities.toLowerCase(split.get(0)).equals(switcher)) {
                return split.get(1);
            }
        }
        return null;
    }

    // TODO: Determinations

    public List<ScriptEventData> usages = new ArrayList<>();

    public void init() {
        boolean generalDebug = Denizen2Core.getImplementation().generalDebug();
        usages.clear();
        for (WorldScript script : currentWorldScripts) {
            Set<StringHolder> evts = script.contents.getConfigurationSection("events").getKeys(false);
            for (StringHolder evt : evts) {
                if (evt.str.length() < "on ".length()) {
                    continue;
                }
                ScriptEventData data = new ScriptEventData();
                data.script = script;
                data.eventPath = evt.str.substring("on ".length());
                if (couldMatch(data)) {
                    usages.add(data);
                    if (generalDebug) {
                        Debug.good("Script event match: " + ColorSet.emphasis + getName()
                        + ColorSet.good + " matched for: " + ColorSet.emphasis + script.title + "." + evt.str
                        + ColorSet.good + "!");
                    }
                }
            }
        }
        sort();
    }

    public HashMap<String, AbstractTagObject> getDefinitions(ScriptEventData data) {
        HashMap<String, AbstractTagObject> defs = new HashMap<>();
        defs.put("priority", new IntegerTag(data.priority));
        defs.put("cancelled", new BooleanTag(cancelled));
        return defs;
    }

    // <--[explanation]
    // @Name Script Event Cancellation
    // @Group Events
    // @Description
    // Any ScriptEvent can take an "ignorecancelled:true" argument.
    // For example: "on object does something ignorecancelled:true:"
    // If you set 'ignorecancelled:true', the event will fire regardless of whether it was cancelled.
    // By default, only non-cancelled events will fire.
    //
    // Any script event can take the determinations "cancelled" and "cancelled:false".
    // These determinations will set whether the script event is 'cancelled' in the eyes of following script events,
    // and, in some cases, can be used to stop the event itself from continuing.
    // A script event can at any time check the cancellation state of an event by accessing "<[context].[cancelled]>".
    // -->

    public boolean cancelled = false;

    public void subRun(ScriptEventData data) {
        HashMap<String, AbstractTagObject> defs = getDefinitions(data);
        if (Denizen2Core.getImplementation().generalDebug()) {
            Debug.good("Running script event: " + ColorSet.emphasis + data.script.title
                    + ColorSet.good + ", event: " + "on " + ColorSet.emphasis + data.eventPath);
            for (Map.Entry<String, AbstractTagObject> def : defs.entrySet()) {
                Debug.good("Definition: " + ColorSet.emphasis + def.getKey() + ColorSet.good
                        + " is " + ColorSet.emphasis + def.getValue().toString());
            }
        }
        CommandQueue queue = data.script.getSection("events.on " + data.eventPath).toQueue();
        queue.commandStack.peek().setDefinition("context", new MapTag(defs));
        queue.start();
    }

    public void run() {
        for (ScriptEventData data : usages) {
            if (matches(data)) {
                if (cancelled) {
                    String ic = getSwitch(data.eventPath, "ignorecancelled");
                    if (ic == null || !CoreUtilities.toLowerCase(ic).equals("true")) {
                        continue;
                    }
                }
                subRun(data);
            }
        }
    }

    public abstract String getName();

    public abstract boolean couldMatch(ScriptEventData data);

    public abstract boolean matches(ScriptEventData data);
}
