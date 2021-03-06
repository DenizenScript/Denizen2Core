package com.denizenscript.denizen2core.commands.filecommands;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.ListTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class YamlCommand extends AbstractCommand {

    // <--[command]
    // @Since 0.3.0
    // @Name yaml
    // @Arguments <id> 'create'/'load'/'save'/'set'/'setobject'/'remove'/'close' [path] [value]
    // @Short handles and manipulates YAML-formatted data.
    // @Updated 2016/04/02
    // @Group Common
    // @Minimum 2
    // @Maximum 4
    // @Description
    // Handles and manipulates YAML-formatted data.
    // This system can work entirely in RAM, but also interacts within the file system so far as the
    // implementing engine permits.
    // You can create a dataset in memory, or load a dataset from disk, or save to disk, or set a value, or setobject
    // an object-defined value, or remove a key, or close a dataset (preventing it from being accessed further).
    // Note that depending on sub-command, the path argument can refer to either the YAML key or a disk file path.
    // Note that YAML format includes the specification that a key is split along the dot '.' symbol.
    // So for key 'a.b.c', it will output key 'a' contains key 'b' contains key 'c'.
    // An ID must be included to uniquely identify a YAML dataset while its held in memory.
    // IDs are case-insensitive currently, but it is good practice to use consistent casing.
    // This is NOT a guaranteed property: case-sensitivity may be enabled in the future!
    // Note that internal YAML data is manipulated as strings.
    // Also, lists in the set command are automatically split to a YAML list - this only happens if the input is a list-typed object.
    // It is strongly recommended to use the 'setobject' subcommand to more accurately track objects with their types where possible!
    // TODO: Set/Read MAPS support!
    // @Example
    // # This example creates an empty YAML file, and remembers it as file ID 'test'.
    // - yaml 'test' create
    // @Example
    // # This example loads a YAML file 'mydata.yml' in the default data folder, and remembers it as file ID 'test'.
    // - yaml 'test' load 'mydata'
    // @Example
    // # This example edits the YAML data loaded as ID 'test' to now have the key 'a.b' set to 'demo'.
    // - yaml 'test' set 'a.b' 'demo'
    // @Example
    // # This example edits the YAML data loaded as ID 'test' to now have the key 'a.b' set to an integer of 5, as an object!
    // - yaml 'test' setobject 'a.b' '<integer[5]>'
    // @Example
    // # This example edits the YAML data loaded as ID 'test' to no longer have the key 'a.b'.
    // - yaml 'test' remove 'a.b'
    // @Example
    // # This example saves the YAML data loaded as ID 'test' to file 'mydata.yml'.
    // - yaml 'test' save 'mydata'
    // @Example
    // # This example forgets the YAML file loaded as 'test'.
    // - yaml 'test' close
    // -->

    @Override
    public String getName() {
        return "yaml";
    }

    @Override
    public String getArguments() {
        return "<id> 'create'/'load'/'save'/'set'/'setobject'/'remove'/'close' [path] [value]";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public int getMaximumArguments() {
        return 4;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        String id = CoreUtilities.toLowerCase(entry.getArgumentObject(queue, 0).toString());
        String mode = CoreUtilities.toLowerCase(entry.getArgumentObject(queue, 1).toString());
        if (mode.equals("close")) {
            if (queue.procedural) {
                queue.handleError(entry, "Cannot close things from a procedural queue!");
                return;
            }
            if (Denizen2Core.filesLoadedByScripts.remove(id) == null) {
                queue.handleError(entry, "Closed non-existent YAML file!");
                return;
            }
            if (queue.shouldShowGood()) {
                queue.outGood("Closed a YAML file!");
            }
            return;
        }
        boolean hasAlready = Denizen2Core.filesLoadedByScripts.containsKey(id);
        if (mode.equals("load")) {
            if (hasAlready) {
                queue.handleError(entry, "Cannot load to an already-loaded ID ('" + id + "').");
                return;
            }
            String path = "./" + entry.getArgumentObject(queue, 2).toString() + ".yml";
            if (!Denizen2Core.getImplementation().isSafePath(path)) {
                queue.handleError(entry, "Cannot load from that path ('" + path + "'), it's marked un-safe.");
                return;
            }
            try {
                File f = new File(Denizen2Core.getImplementation().getScriptDataFolder(), path);
                FileInputStream fis = new FileInputStream(f);
                String t = CoreUtilities.streamToString(fis);
                fis.close();
                if (t == null) {
                    queue.handleError(entry, "Cannot load from that path ('" + path + "'), it appears to not be valid.");
                    return;
                }
                YAMLConfiguration config = YAMLConfiguration.load(t);
                Denizen2Core.filesLoadedByScripts.put(id, config);
                if (queue.shouldShowGood()) {
                    queue.outGood("Loaded a YAML file!");
                }
                return;
            }
            catch (Exception e) {
                queue.handleError(entry, "Failed to read YAML file: " + e.getClass().getCanonicalName() + ": " + e.getMessage());
                return;
            }
        }
        if (mode.equals("create")) {
            if (hasAlready) {
                queue.handleError(entry, "Cannot create to an already-loaded ID ('" + id + "').");
                return;
            }
            YAMLConfiguration config = new YAMLConfiguration();
            Denizen2Core.filesLoadedByScripts.put(id, config);
            if (queue.shouldShowGood()) {
                queue.outGood("Created a YAML file!");
            }
            return;
        }
        if (!hasAlready) {
            queue.handleError(entry, "Cannot work with an unloaded ID ('" + id + "').");
            return;
        }
        Object o = Denizen2Core.filesLoadedByScripts.get(id);
        YAMLConfiguration yconfig;
        if (o instanceof YAMLConfiguration) {
            yconfig = (YAMLConfiguration) o;
        }
        else {
            queue.handleError(entry, "File specified ID ('" + id + "') is loaded, but is not YAML!");
            return;
        }
        if (mode.equals("save")) {
            String path = "./" + entry.getArgumentObject(queue, 2).toString() + ".yml";
            if (!Denizen2Core.getImplementation().isSafePath(path)) {
                queue.handleError(entry, "Cannot save to that path ('" + path + "'), it's marked un-safe.");
                return;
            }
            try {
                String res = yconfig.saveToString();
                File f = new File(Denizen2Core.getImplementation().getScriptDataFolder(), path);
                f.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(res.getBytes(CoreUtilities.encoding));
                fos.flush();
                fos.close();
                if (queue.shouldShowGood()) {
                    queue.outGood("Saved a YAML file!");
                }
                return;
            }
            catch (Exception e) {
                queue.handleError(entry, "Failed to save to YAML file: " + e.getClass().getCanonicalName() + ": " + e.getMessage());
                return;
            }
        }
        if (mode.equals("set")) {
            String path = entry.getArgumentObject(queue, 2).toString();
            AbstractTagObject val = entry.getArgumentObject(queue, 3);
            if (val instanceof ListTag) {
                List<String> res = new ArrayList<>();
                for (AbstractTagObject str : ((ListTag) val).getInternal()) {
                    res.add(str.toString());
                }
                yconfig.set(path, res);
            }
            else {
                yconfig.set(path, val.toString());
            }
            if (queue.shouldShowGood()) {
                queue.outGood("Set a value!");
            }
            return;
        }
        if (mode.equals("setobject")) {
            String path = entry.getArgumentObject(queue, 2).toString();
            AbstractTagObject val = entry.getArgumentObject(queue, 3);
            if (val instanceof ListTag) {
                List<String> res = new ArrayList<>();
                for (AbstractTagObject str : ((ListTag) val).getInternal()) {
                    res.add(str.savable());
                }
                yconfig.set(path, res);
            }
            else {
                yconfig.set(path, val.savable());
            }
            if (queue.shouldShowGood()) {
                queue.outGood("Set a value!");
            }
            return;
        }
        if (mode.equals("remove")) {
            String path = entry.getArgumentObject(queue, 2).toString();
            yconfig.set(path, null);
            if (queue.shouldShowGood()) {
                queue.outGood("Removed a value!");
            }
            return;
        }
    }
}
