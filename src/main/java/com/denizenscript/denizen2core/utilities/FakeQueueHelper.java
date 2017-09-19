package com.denizenscript.denizen2core.utilities;

import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandStackEntry;
import com.denizenscript.denizen2core.scripts.commontypes.TaskScript;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

import java.util.HashMap;

public class FakeQueueHelper {

    public static CommandQueue genFakeQueueFor(HashMap<String, AbstractTagObject> defs, Action<String> error) {
        CommandQueue q = new CommandQueue();
        CommandStackEntry cse = new CommandStackEntry(new CommandEntry[]{}, "__Debug_Queue__", new TaskScript("__Debug_Script__", new YAMLConfiguration()));
        cse.definitions = defs;
        q.commandStack.push(cse);
        q.error = error;
        return q;
    }
}
