package org.mcmonkey.denizen2core.addons;

import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.yaml.YAMLConfiguration;

import java.io.InputStream;

public class AddonInfo {

    private final YAMLConfiguration yamlConfiguration;
    private final String name;
    private final String version;
    private final String main;

    public AddonInfo(InputStream inputStream) throws IllegalStateException {
        this.yamlConfiguration = YAMLConfiguration.load(CoreUtilities.streamToString(inputStream));
        if (yamlConfiguration == null) {
            throw new IllegalStateException("Invalid addon.yml file!");
        }
        this.name = yamlConfiguration.getString("name");
        this.version = yamlConfiguration.getString("version");
        this.main = yamlConfiguration.getString("main");
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getMain() {
        return main;
    }
}
