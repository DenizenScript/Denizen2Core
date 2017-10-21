package com.denizenscript.denizen2core.events.commonevents;

import com.denizenscript.denizen2core.events.ScriptEvent;

public class DeltaTimeEvent extends ScriptEvent {

    // <--[event]
    // @Since 0.3.0
    // @Events
    // delta time secondly
    // delta time minutely
    //
    // @Updated 2017/01/19
    //
    // @Group Common
    //
    // @Triggers every second or minute of game calculation time.
    //
    // @Context
    // second (IntegerTag) returns the exact second of delta time since system startup.
    //
    // @Determinations
    // None.
    // -->

    @Override
    public String getName() {
        return "DeltaTime";
    }

    @Override
    public boolean couldMatch(ScriptEventData data) {
        return data.eventPath.startsWith("delta time minutely") || data.eventPath.startsWith("delta time secondly");
    }

    public long second;

    @Override
    public boolean matches(ScriptEventData data) {
        return data.eventPath.startsWith("delta time secondly") || (((second % 60) == 0) && data.eventPath.startsWith("delta time minutely'"));
    }

    public void call(long sec) {
        DeltaTimeEvent evt = (DeltaTimeEvent) clone();
        evt.second = sec;
        evt.run();
    }
}
