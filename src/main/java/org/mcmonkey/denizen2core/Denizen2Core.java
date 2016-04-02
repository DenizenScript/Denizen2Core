package org.mcmonkey.denizen2core;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandScriptSection;
import org.mcmonkey.denizen2core.commands.commoncommands.EchoCommand;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;
import org.mcmonkey.denizen2core.utilities.yaml.YAMLConfiguration;

import java.io.InputStream;
import java.util.HashMap;

/**
 * The main entry class for Denizen 2's core engine.
 */
public class Denizen2Core {

    public final static String version;

    static {
        YAMLConfiguration config = null;
        try {
            InputStream is = Denizen2Core.class.getResourceAsStream("/denizen2.yml");
            config = YAMLConfiguration.load(CoreUtilities.streamToString(is));
            is.close();
        }
        catch (Exception ex) {
            Debug.exception(ex);
        }
        if (config == null) {
            version = "UNKNOWN (Error reading version file!)";
        }
        else {
            version = config.getString("VERSION", "UNKNOWN") + " (build " + config.getString("BUILD_NUMBER", "UNKNOWN") + ")";
        }
    }

    private static Denizen2Implementation implementation;

    private static HashMap<String, AbstractCommand> commands = new HashMap<>();

    public static HashMap<String, AbstractCommand> getCommands() {
        return commands;
    }

    public static Denizen2Implementation getImplementation() {
        return implementation;
    }

    public static void register(AbstractCommand command) {
        commands.put(command.getName(), command);
    }

    public static void init(Denizen2Implementation impl) {
        implementation = impl;
        register(new EchoCommand());
    }

    public static void runString(String cmd) {
        CommandScriptSection.forLine(cmd).toQueue().start();
    }
}
