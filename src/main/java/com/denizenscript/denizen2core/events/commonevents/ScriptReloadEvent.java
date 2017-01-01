package com.denizenscript.denizen2core.events.commonevents;

import com.denizenscript.denizen2core.events.ScriptEvent;

public class ScriptReloadEvent extends ScriptEvent {

    // <--[event]
    // @Events
    // scripts reloaded
    //
    // @Updated 2016/08/26
    //
    // @Group Common
    //
    // @Triggers when the Denizen engine reloads all scripts.
    // See <@link command reload>the reload command<@/link>.
    //
    // @Context
    // None.
    //
    // @Determinations
    // None.
    // -->

    @Override
    public String getName() {
        return "ScriptReload";
    }

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("scripts reloaded");
    }

    @Override
    public boolean matches(ScriptEventData data) {
        return true;
    }

    public void call() {
        ScriptReloadEvent evt = (ScriptReloadEvent) clone();
        evt.run();
    }
}
