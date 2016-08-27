package org.mcmonkey.denizen2core.addons;

import org.mcmonkey.denizen2core.Denizen2Implementation;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public abstract class DenizenAddon {

    private AddonClassLoader addonClassLoader = null;
    private Denizen2Implementation implementation = null;
    private AddonInfo addonInfo = null;
    private File dataFolder;

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

    public void saveDefaultConfig() {
        saveResource("config.yml", false);
    }

    public void saveResource(String resource, boolean overwrite) {
        try (InputStream inputStream = addonClassLoader.getResourceAsStream(resource)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource '" + resource + "' could not be found!");
            }
            File output = new File(getDataFolder(), resource);
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
    }
}
