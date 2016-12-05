package com.denizenscript.denizen2core;

import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandEntry;

import java.io.File;

/**
 * Abstract class representing an implementation of Denizen2.
 */
public abstract class Denizen2Implementation {

    public abstract void preReload();

    public abstract void reload();

    public abstract void outputException(Exception ex); // TODO: Throwable?

    public abstract void outputGood(String text);

    public abstract void outputInfo(String text);

    public abstract void outputInvalid(CommandQueue queue, CommandEntry entry);

    public abstract void outputError(String message);

    public abstract boolean generalDebug();

    public abstract File getScriptsFolder();

    public abstract File getAddonsFolder();

    public abstract String getImplementationName();

    public abstract String getImplementationVersion();

    public abstract boolean enforceLocale();

    public abstract File getScriptDataFolder();

    public abstract boolean isSafePath(String file);
}
