package com.denizenscript.denizen2core.events.commonevents;

import com.denizenscript.denizen2core.events.ScriptEvent;

public class SystemLoadEvent extends ScriptEvent {

    // <--[event]
    // @Events
    // system loaded
    //
    // @Updated 2016/08/26
    //
    // @Triggers when the Denizen engine loads for the first time.
    //
    // @Context
    // None.
    //
    // @Determinations
    // None.
    // -->

    @Override
    public String getName() {
        return "SystemLoad";
    }

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("system loaded");
    }

    @Override
    public boolean matches(ScriptEventData data) {
        return true;
    }

    public void call() {
        SystemLoadEvent evt = (SystemLoadEvent) clone();
        evt.run();
    }
}
