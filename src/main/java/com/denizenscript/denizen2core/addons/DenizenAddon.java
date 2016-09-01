package com.denizenscript.denizen2core.addons;

import com.denizenscript.denizen2core.Denizen2Implementation;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.debugging.Debug;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;

public abstract class DenizenAddon {

    private AddonClassLoader addonClassLoader = null;
    private Denizen2Implementation implementation = null;
    private AddonInfo addonInfo = null;
    private File dataFolder;
    private File configFile;
    private YAMLConfiguration config;

    public DenizenAddon() {
        ClassLoader classLoader = getClass().getClassLoader();
        if (!(classLoader instanceof AddonClassLoader)) {
            throw new IllegalStateException("DenizenAddon must be loaded by AddonClassLoader!");
        }
        ((AddonClassLoader) classLoader).initialize(this);
    }

    public AddonClassLoader getAddonClassLoader() {
        return addonClassLoader;
    }

    public Denizen2Implementation getImplementation() {
        return implementation;
    }

    public AddonInfo getAddonInfo() {
        return addonInfo;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public void reloadConfig() {
        try {
            if (!configFile.exists()) {
                if (!hasResource("config.yml")) {
                    return;
                }
                saveResource("config.yml", false);
            }
            config = YAMLConfiguration.load(CoreUtilities.streamToString(new FileInputStream(configFile)));
        }
        catch (FileNotFoundException e) {
            Debug.exception(e);
        }
    }

    public YAMLConfiguration getConfig() {
        return config;
    }

    public void saveResource(String resource, boolean overwrite) {
        try (InputStream inputStream = addonClassLoader.getResourceAsStream(resource)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource '" + resource + "' could not be found!");
            }
            File output = new File(dataFolder, resource);
            File parent = output.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            if (output.exists()) {
                if (!overwrite) {
                    Debug.error("Resource '" + resource + "' already exists!");
                    return;
                }
                output.delete();
            }
            Files.copy(inputStream, output.toPath());
        }
        catch (Exception e) {
            Debug.error("Error while saving resource '" + resource + "'.");
            Debug.exception(e);
        }
    }

    public boolean hasResource(String resource) {
        return addonClassLoader.getResource(resource) != null;
    }

    public void enable() {
    }

    public void reload() {
    }

    public void disable() {
    }

    final void init(AddonClassLoader classLoader, Denizen2Implementation impl, AddonInfo addonInfo, File dataFolder) {
        this.addonClassLoader = classLoader;
        this.implementation = impl;
        this.addonInfo = addonInfo;
        this.dataFolder = dataFolder;
        this.configFile = new File(dataFolder, "config.yml");
        reloadConfig();
    }
}
