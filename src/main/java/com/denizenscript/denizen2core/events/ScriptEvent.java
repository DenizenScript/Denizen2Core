package com.denizenscript.denizen2core.events;

import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.BooleanTag;
import com.denizenscript.denizen2core.tags.objects.MapTag;
import com.denizenscript.denizen2core.utilities.FakeQueueHelper;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.arguments.Argument;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandScriptSection;
import com.denizenscript.denizen2core.commands.CommandStackEntry;
import com.denizenscript.denizen2core.scripts.commontypes.WorldScript;
import com.denizenscript.denizen2core.tags.objects.IntegerTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.ErrorInducedException;
import com.denizenscript.denizen2core.utilities.debugging.Debug;
import com.denizenscript.denizen2core.utilities.yaml.StringHolder;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

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

        public WorldScript script = null;

        public String rawPath = null;

        public String eventPath = null;

        public boolean ignoreCancelled = false;

        public int priority = 0;

        public List<Argument> requirements = new ArrayList<>();

        public HashMap<String, String> switches = new HashMap<>();

        public CommandScriptSection scriptSection = null;
    }

    // <--[explanation]
    // @Since 0.3.0
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
        usages.sort(Comparator.comparingInt((t) -> t.priority));
    }

    // TODO: Determinations

    public List<ScriptEventData> usages = new ArrayList<>();

    private boolean loaded = false;

    public void enable() {
    }

    public void disable() {
    }

    public void init() {
        boolean generalDebug = Denizen2Core.getImplementation().generalDebug();
        usages.clear();
        for (WorldScript script : currentWorldScripts) {
            if (!script.contents.contains("events")) {
                Debug.error("Invalid world script: " + ColorSet.emphasis + script.title + ColorSet.warning + ": missing events section!");
                continue;
            }
            YAMLConfiguration eventsBlock = script.contents.getConfigurationSection("events");
            if (eventsBlock == null) {
                Debug.error("Invalid world script: " + ColorSet.emphasis + script.title + ColorSet.warning + ": malformed events section!");
                continue;
            }
            Set<StringHolder> evts = eventsBlock.getKeys(false);
            for (StringHolder evt : evts) {
                if (evt.str.length() < "on ".length()) {
                    continue;
                }
                ScriptEventData data = new ScriptEventData();
                try {
                    data.script = script;
                    data.rawPath = evt.str.substring("on ".length());
                    StringBuilder res = new StringBuilder();
                    for (String possible : CoreUtilities.split(data.rawPath, ' ')) {
                        List<String> split = CoreUtilities.split(possible, ':', 2);
                        String low = CoreUtilities.toLowerCase(split.get(0));
                        if (split.size() > 1) {
                            String val = split.get(1);
                            if (low.equals("require")) {
                                data.requirements.add(Denizen2Core.splitToArgument(val.replace("&dot", ".")
                                        .replace("&amp", "&"), false, false, this::error));
                            }
                            else if (low.equals("ignorecancelled")) {
                                data.ignoreCancelled = CoreUtilities.toLowerCase(val).equals("true");
                            }
                            else if (low.equals("priority")) {
                                data.priority = (int) IntegerTag.getFor(this::error, val).getInternal();
                            }
                            else {
                                data.switches.put(low, val);
                            }
                        }
                        else {
                            res.append(possible).append(" ");
                        }
                    }
                    data.eventPath = res.toString().trim();
                }
                catch (ErrorInducedException ex) {
                    Debug.error("While managing script event " + ColorSet.emphasis + getName() + ColorSet.warning
                            + ", tried to process " + ColorSet.emphasis + script.title + "." + evt.str
                            + ColorSet.warning + ", but got error: " + ex.getMessage());
                }
                try {
                    if (couldMatch(data)) {
                        usages.add(data);
                        script.eventsConfirmed.add(evt.str);
                        if (generalDebug) {
                            Debug.good("Script event match: " + ColorSet.emphasis + getName()
                                    + ColorSet.good + " matched for: " + ColorSet.emphasis + script.title + "." + evt.str
                                    + ColorSet.good + "!");
                        }
                    }
                }
                catch (ErrorInducedException ex) {
                    Debug.error("While managing script event " + ColorSet.emphasis + getName() + ColorSet.warning
                            + ", tried to match " + ColorSet.emphasis + script.title + "." + evt.str
                            + ColorSet.warning + ", but got error: " + ex.getMessage());
                }
            }
        }
        if (usages.size() > 0) {
            if (!loaded) {
                loaded = true;
                enable();
            }
            sort();
        }
        else if (loaded) {
            loaded = false;
            disable();
        }
    }

    public HashMap<String, AbstractTagObject> getDefinitions(ScriptEventData data) {
        HashMap<String, AbstractTagObject> defs = new HashMap<>();
        defs.put("priority", new IntegerTag(data.priority));
        defs.put("cancelled", BooleanTag.getForBoolean(cancelled));
        return defs;
    }

    public void error(String message) {
        throw new ErrorInducedException(message);
    }

    public void applyDetermination(boolean errors, String determination, AbstractTagObject value) {
        if (determination.equals("cancelled")) {
            cancelled = BooleanTag.getFor(this::error, value).getInternal();
        }
        else if (errors) {
            Debug.error("Invalid determination: " + determination);
        }
    }

    // <--[explanation]
    // @Since 0.3.0
    // @Name Script Event Cancellation
    // @Group Events
    // @Description
    // Any ScriptEvent can take an "ignorecancelled:true" argument.
    // For example: "on object does something ignorecancelled:true:"
    // If you set 'ignorecancelled:true', the event will fire regardless of whether it was cancelled.
    // By default, only non-cancelled events will fire.
    //
    // Any script event can take the determinations "cancelled" and "cancelled false".
    // These determinations will set whether the script event is 'cancelled' in the eyes of following script events,
    // and, in some cases, can be used to stop the event itself from continuing.
    // A script event can at any time check the cancellation state of an event by accessing "<[context].[cancelled]>".
    // -->

    public boolean cancelled = false;

    // <--[explanation]
    // @Since 0.3.0
    // @Name Script Event Requirements
    // @Group Events
    // @Description
    // Any ScriptEvent can take requirement arguments.
    // Those are switches that contain tags that return boolean values.
    // The script event will not run if any requirement returns false.
    //
    // An example of the syntax is: "on my object does a thing require:<[context].[object].is_type[BIG]>"
    // -->

    // <--[explanation]
    // @Since 0.3.0
    // @Name Script Event Switches
    // @Group Events
    // @Description
    // Any ScriptEvent can take certain switches, such as the "ignorecancelled" switch, the "require" switch, or custom switches unique to the event.
    // Switches must all pass for an event script to run. If even one fails (EG a require switch returns false,
    // or a custom switch has been given incorrect or irrelevant input for that specific firing of the event)
    // then the event script will not run for that firing.
    //
    // In many cases, a switch is similar to being a shorthand for an if statement - one that runs very quickly on the internals,
    // and also that fits in line with the event declaration itself.
    //
    // An example of the syntax is: "on my object does a thing require:<[context].[object].is_type[BIG]>"
    // In the example above, "require" is a switch, and the long tag is the switch argument.
    // -->

    public ScriptEventData curRun = null;

    public void subRun(ScriptEventData data) {
        curRun = data;
        HashMap<String, AbstractTagObject> defs = getDefinitions(data);
        MapTag defmap = new MapTag(defs);
        HashMap<String, AbstractTagObject> contextHelper = new HashMap<>();
        contextHelper.put("context", defmap);
        if (data.scriptSection == null) {
            data.scriptSection = data.script.getSection("events.on " + data.rawPath);
            if (data.scriptSection == null) {
                if (Denizen2Core.getImplementation().generalDebug()) {
                    Debug.info("Script Event path: " + data.rawPath + " failed to load...");
                }
                return; // Something went wrong. Perhaps the script didn't load, or had an error?
            }
        }
        CommandScriptSection css = data.scriptSection;
        DebugMode dbm = css.created.getDebugMode();
        for (Argument req : data.requirements) {
            if (dbm.showFull) {
                Debug.info("Checking requirement: " + req.toString());
            }
            CommandQueue q = FakeQueueHelper.genFakeQueueFor(contextHelper, this::error);
            BooleanTag bt = BooleanTag.getFor(this::error, req.parse(q, contextHelper, dbm, this::error));
            if (dbm.showFull) {
                Debug.info("Requirement result: " + bt.getInternal());
            }
            if (!bt.getInternal()) {
                return;
            }
        }
        if (data.script.getDebugMode().showFull) {
            Debug.good("Running script event: " + ColorSet.emphasis + data.script.title
                    + ColorSet.good + ", event: on " + ColorSet.emphasis + data.rawPath);
            for (Map.Entry<String, AbstractTagObject> def : defs.entrySet()) {
                Debug.good("Context Definition: " + ColorSet.emphasis + def.getKey() + ColorSet.good
                        + " is " + ColorSet.emphasis + def.getValue().toString());
            }
        }
        CommandQueue queue = css.toQueue();
        CommandStackEntry cse = queue.commandStack.peek();
        cse.setDefinition("context", defmap);
        cse.sendDeterminesTo = this;
        queue.start();
        for (Map.Entry<String, AbstractTagObject> entry : queue.determinations.getInternal().entrySet()) {
            applyDetermination(cse.getDebugMode().showMinimal, entry.getKey(), entry.getValue());
        }
    }

    public void run() {
        for (ScriptEventData data : usages) {
            try {
                if (matches(data)) {
                    if (cancelled && !data.ignoreCancelled) {
                        continue;
                    }
                    subRun(data);
                }
            }
            catch (Exception ex) {
                if (ex instanceof ErrorInducedException) {
                    Debug.error(ex.getMessage());
                }
                else {
                    Debug.error("Problem parsing script event '" + getName() + "':");
                    Debug.exception(ex);
                }
            }
        }
    }

    public abstract String getName();

    public abstract boolean couldMatch(ScriptEventData data);

    public abstract boolean matches(ScriptEventData data);
}
