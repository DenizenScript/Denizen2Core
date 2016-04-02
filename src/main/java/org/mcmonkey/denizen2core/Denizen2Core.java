package org.mcmonkey.denizen2core;

import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;
import org.mcmonkey.denizen2core.utilities.yaml.YAMLConfiguration;

import java.io.InputStream;

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
}
