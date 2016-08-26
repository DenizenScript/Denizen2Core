package org.mcmonkey.denizen2core.addons;

import org.mcmonkey.denizen2core.Denizen2Implementation;

import java.io.File;

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
}
