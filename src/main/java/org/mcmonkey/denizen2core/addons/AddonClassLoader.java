package org.mcmonkey.denizen2core.addons;

import org.mcmonkey.denizen2core.Denizen2Core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

final class AddonClassLoader extends URLClassLoader {

    private final AddonInfo addonInfo;
    private final File dataFolder;
    private final DenizenAddon addon;
    private boolean initialized;

    public AddonClassLoader(ClassLoader parent, AddonInfo addonInfo, File dataFolder, File file)
            throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        super(new URL[]{file.toURI().toURL()}, parent);
        Class main = Class.forName(addonInfo.getMain(), true, this);
        Class addonClass = main.asSubclass(DenizenAddon.class);
        this.addonInfo = addonInfo;
        this.dataFolder = dataFolder;
        this.addon = (DenizenAddon) addonClass.newInstance();
    }

    public DenizenAddon getAddon() {
        return addon;
    }

    void initialize(DenizenAddon addon) {
        if (addon.getClass().getClassLoader() != this) {
            throw new IllegalStateException("Can't initialize addon outside of its own AddonClassLoader!");
        }
        if (initialized) {
            throw new IllegalArgumentException("Addon is already initialized!");
        }
        initialized = true;
        addon.init(this, Denizen2Core.getImplementation(), addonInfo, dataFolder);
    }
}
