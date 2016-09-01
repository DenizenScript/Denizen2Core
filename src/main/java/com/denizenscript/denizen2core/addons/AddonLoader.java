package com.denizenscript.denizen2core.addons;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.utilities.debugging.Debug;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AddonLoader {

    public static List<DenizenAddon> loadAddons(File addonsFolder) {
        List<DenizenAddon> addons = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(addonsFolder.toPath(), path -> path.toString().endsWith(".jar"))) {
            for (Path jarFile : directoryStream) {
                DenizenAddon addon = loadAddon(jarFile.toFile());
                if (addon != null) {
                    AddonInfo addonInfo = addon.getAddonInfo();
                    try {
                        Debug.info("Attempting to enable addon " + addonInfo.getName() + " " + addonInfo.getVersion() + "...");
                        addon.enable();
                        Debug.good("Successfully loaded " + addonInfo.getName() + " " + addonInfo.getVersion() + "!");
                    }
                    catch (Exception e) {
                        Debug.error("Failed to enable addon '" + addonInfo.getName() + " " + addonInfo.getVersion());
                        Debug.exception(e);
                        continue;
                    }
                    addons.add(addon);
                }
            }
        }
        catch (IOException e) {
            Debug.error("Failed to load addons in " + addonsFolder.toString());
            Debug.exception(e);
        }
        return addons;
    }

    private static DenizenAddon loadAddon(File jarFile) {
        try (JarFile jar = new JarFile(jarFile)) {
            JarEntry addonInfoEntry = jar.getJarEntry("addon.yml");
            try (InputStream is = jar.getInputStream(addonInfoEntry)) {
                AddonInfo addonInfo = new AddonInfo(is);
                File dataFolder = new File(Denizen2Core.getImplementation().getAddonsFolder(), addonInfo.getName());
                AddonClassLoader classLoader = new AddonClassLoader(AddonLoader.class.getClassLoader(), addonInfo, dataFolder, jarFile);
                return classLoader.getAddon();
            }
        }
        catch (Exception e) {
            Debug.error("Failed to load addon from " + jarFile.getName());
            Debug.exception(e);
            return null;
        }
    }
}
