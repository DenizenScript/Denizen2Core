package com.denizenscript.denizen2core;

import com.denizenscript.denizen2core.addons.AddonInfo;
import com.denizenscript.denizen2core.arguments.TagBit;
import com.denizenscript.denizen2core.commands.*;
import com.denizenscript.denizen2core.commands.filecommands.YamlCommand;
import com.denizenscript.denizen2core.commands.queuecommands.*;
import com.denizenscript.denizen2core.events.ScriptEvent;
import com.denizenscript.denizen2core.events.commonevents.DeltaTimeEvent;
import com.denizenscript.denizen2core.events.commonevents.ScriptReloadEvent;
import com.denizenscript.denizen2core.events.commonevents.SystemLoadEvent;
import com.denizenscript.denizen2core.scripts.commontypes.TaskScript;
import com.denizenscript.denizen2core.scripts.commontypes.YamlDataScript;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.handlers.*;
import com.denizenscript.denizen2core.tags.objects.*;
import com.denizenscript.denizen2core.utilities.*;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.denizen2core.utilities.debugging.Debug;
import com.denizenscript.denizen2core.addons.AddonLoader;
import com.denizenscript.denizen2core.addons.DenizenAddon;
import com.denizenscript.denizen2core.arguments.Argument;
import com.denizenscript.denizen2core.arguments.TagArgumentBit;
import com.denizenscript.denizen2core.arguments.TextArgumentBit;
import com.denizenscript.denizen2core.commands.commoncommands.EchoCommand;
import com.denizenscript.denizen2core.commands.commoncommands.ReloadCommand;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.scripts.ScriptHelper;
import com.denizenscript.denizen2core.scripts.commontypes.ProcedureScript;
import com.denizenscript.denizen2core.scripts.commontypes.WorldScript;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.utilities.yaml.StringHolder;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * The main entry class for Denizen2's core engine.
 */
public class Denizen2Core {

    public interface IntegerForm {
        long getIntegerForm();
    }

    public interface NumberForm {
        double getNumberForm();
    }

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
            version = "UNKNOWN (Error reading version file! Possibly a corrupt jar?)";
        }
        else {
            version = config.getString("VERSION", "UNKNOWN") + " (build " + config.getString("BUILD_NUMBER", "UNKNOWN") + ")";
        }
    }

    private static Denizen2Implementation implementation;

    public final static HashMap<String, CommandScript> currentScripts = new HashMap<>();

    public final static HashMap<String, AbstractCommand> commands = new HashMap<>();

    public final static HashMap<String, AbstractTagBase> tagBases = new HashMap<>();

    public final static List<ScriptEvent> events = new ArrayList<>();

    public final static HashMap<String, Function2<String, YAMLConfiguration, CommandScript>> scriptTypeGetters = new HashMap<>();

    public final static HashMap<String, Object> filesLoadedByScripts = new HashMap<>();

    public static Denizen2Implementation getImplementation() {
        return implementation;
    }

    public static void register(ScriptEvent evt) {
        events.add(evt);
    }

    public static void register(AbstractCommand command) {
        commands.put(command.getName(), command);
    }

    public static void register(AbstractTagBase tagbase) {
        tagBases.put(tagbase.getName(), tagbase);
    }

    public static void register(String type, Function2<String, YAMLConfiguration, CommandScript> func) {
        scriptTypeGetters.put(type, func);
    }

    public static List<CommandQueue> queues = new ArrayList<>();

    public static long cqID = 0;

    public static double totalTime;

    static double pTotal;

    public static long currentTick = 0;

    public static void tick(double delta) {
        currentTick++;
        for (int i = 0; i < queues.size(); i++) {
            CommandQueue q = queues.get(i);
            if (q.run(delta)) {
                queues.remove(i);
                i--;
            }
        }
        totalTime += delta;
        while (pTotal + 1.0 < totalTime) {
            deltaTime.call((long) Math.floor(totalTime));
            pTotal += 1.0;
        }
    }

    private static List<DenizenAddon> addons = new ArrayList<>();

    public static void init(Denizen2Implementation impl) {
        // Track this implementation value
        implementation = impl;
        // Enforce a reasonable locale on the machine - to prevent format errors
        // Note that US is chosen primarily as it is:
        // - the most likely to be used by a user of Denizen2 anyway (All Denizen2 info is EN-US)
        // - the least likely to screw with commonly accepted (international) formatting
        // This also helps ensure related text will be in English, to ensure all Denizen2 helpers can read output from foreign servers.
        if (implementation.enforceLocale()) {
            Locale.setDefault(Locale.US);
        }
        // Clear any old data
        commands.clear();
        tagBases.clear();
        currentScripts.clear();
        scriptTypeGetters.clear();
        // Common Commands
        register(new EchoCommand());
        register(new ReloadCommand());
        // File Commands
        register(new YamlCommand());
        // Queue Commands
        register(new AddtoCommand());
        register(new AssertCommand());
        register(new ChooseCommand());
        register(new DefineCommand());
        register(new DetermineCommand());
        register(new ElseCommand());
        register(new ForeachCommand());
        register(new GotoCommand());
        register(new IfCommand());
        register(new InjectCommand());
        register(new MarkCommand());
        register(new PauseCommand());
        register(new RandomCommand());
        register(new RepeatCommand());
        register(new RequireCommand());
        register(new ResumeCommand());
        register(new RunCommand());
        register(new StopCommand());
        register(new UndefineCommand());
        register(new WaitCommand());
        register(new WhileCommand());
        // Common Tag Handlers
        register(BooleanTag.getForBooleanBase());
        register(new DefExistsTagBase());
        register(new DefTagBase());
        register(new DurationTagBase());
        register(new EscapeTagBase());
        register(new FromSavedTagBase());
        register(new IntegerTagBase());
        register(new ListTagBase());
        register(new MapTagBase());
        register(new MathTagBase());
        register(new NullTagBase());
        register(new NumberTagBase());
        register(new ProcedureTagBase());
        register(new QueueTagBase());
        register(new SaveTagBase());
        register(new ScriptTagBase());
        register(new SystemTagBase());
        register(new TaskTagBase());
        register(new TextTagBase());
        register(new TimeTagBase());
        register(new UnescapeTagBase());
        register(new YamlTagBase());
        // Common script types
        register("procedure", ProcedureScript::new);
        register("task", TaskScript::new);
        register("world", WorldScript::new);
        register("yaml data", YamlDataScript::new);
        // Common script events
        register(deltaTime = new DeltaTimeEvent());
        register(scriptReload = new ScriptReloadEvent());
        register(systemLoad = new SystemLoadEvent());
        // Type Loaders
        customSaveLoaders.put("BooleanTag", BooleanTag::getFor);
        customSaveLoaders.put("DurationTag", DurationTag::getFor);
        customSaveLoaders.put("IntegerTag", IntegerTag::getFor);
        customSaveLoaders.put("ListTag", ListTag::getForSaved);
        customSaveLoaders.put("MapTag", MapTag::getForSaved);
        customSaveLoaders.put("NullTag", NullTag::getFor);
        customSaveLoaders.put("NumberTag", NumberTag::getFor);
        customSaveLoaders.put("QueueTag", QueueTag::getFor);
        customSaveLoaders.put("ScriptTag", ScriptTag::getFor);
        customSaveLoaders.put("TextTag", TextTag::getFor);
        customSaveLoaders.put("TimeTag", TimeTag::getFor);
        customSaveLoaders.put("YamlTag", YamlTag::getFor);
        customSaveLoaders.put("SystemTag", (e, s) -> new SystemTagBase.SystemTag());
    }

    public static final HashMap<String, Function2<Action<String>, String, AbstractTagObject>> customSaveLoaders = new HashMap<>();

    public static AbstractTagObject loadFromSaved(Action<String> error, String str) {
        List<String> dat = CoreUtilities.split(str, '@', 2);
        String typed = dat.get(0);
        if (!customSaveLoaders.containsKey(typed)) {
            error.run("No save loader for the specified type: " + ColorSet.emphasis + typed + ColorSet.warning + "! "
                    + "May be invalid input to a saves loader?");
            return NullTag.NULL;
        }
        return customSaveLoaders.get(typed).apply(error, dat.get(1));
    }

    private static ScriptReloadEvent scriptReload = null;

    private static SystemLoadEvent systemLoad = null;

    private static DeltaTimeEvent deltaTime = null;

    public static void reload() {
        implementation.preReload();
        currentScripts.clear();
        ScriptEvent.currentWorldScripts.clear();
        implementation.midLoad();
        load();
        implementation.reload();
        reloadAddons();
        scriptReload.call();
    }

    private static void reloadAddons() {
        for (DenizenAddon addon : addons) {
            addon.reload();
        }
    }

    private static void loadSection(String scriptName, YAMLConfiguration section) {
        String type = CoreUtilities.toLowerCase(section.getString("type", "_not set_"));
        Function2<String, YAMLConfiguration, CommandScript> getter = scriptTypeGetters.get(type);
        if (getter == null) {
            Debug.error("Unknown type '" + ColorSet.emphasis + type + ColorSet.warning + "' for script '" + ColorSet.emphasis + scriptName
                    + ColorSet.warning + "' ... see documentation regarding valid script types!");
            return;
        }
        CommandScript script = getter.apply(scriptName, section);
        if (script.init()) {
            getImplementation().outputGood("Loaded script '" + ColorSet.emphasis + scriptName + ColorSet.good + "'");
            currentScripts.put(scriptName, script);
        }
        else {
            Debug.error("Failed to load script '" + ColorSet.emphasis + scriptName + ColorSet.warning + "'! See documentation regarding "
                    + "scripts of type " + ColorSet.emphasis + type);
        }
    }

    public static void loadFile(String fileName, String contents) {
        try {
            YAMLConfiguration config = YAMLConfiguration.load(ScriptHelper.clearComments(contents));
            if (config == null) {
                Debug.error("Invalid YAML for script '" + ColorSet.emphasis + fileName + ColorSet.warning
                        + "'... the script file may be empty, or unable to load entirely. If it's intentionally empty, "
                        + "change its extension to '.disable'!");
                return;
            }
            Set<StringHolder> strs = config.getKeys(false);
            for (StringHolder strh : strs) {
                loadSection(strh.low, config.getConfigurationSection(strh.str));
            }
        }
        catch (Exception ex) {
            Debug.error("Failed to load script: " + ColorSet.emphasis + fileName);
            Debug.exception(ex);
        }
    }

    public static void start() {
        File addonsFolder = getImplementation().getAddonsFolder();
        if (!addonsFolder.exists()) {
            Debug.error("Addons folder non-existent! Something may have gone wrong during engine setup. Check file permissions!");
        }
        else {
            addons.addAll(AddonLoader.loadAddons(addonsFolder));
        }
        load();
        systemLoad.call();
    }

    private static void load() {
        File folder = getImplementation().getScriptsFolder();
        try {
            if (!folder.exists()) {
                Debug.error("Scripts folder non-existent! Something may have gone wrong during engine setup. Check file permissions!");
                return;
            }
            Stream<Path> paths = Files.walk(folder.toPath(), FileVisitOption.FOLLOW_LINKS);
            Iterator<Path> pathi = paths.iterator();
            while (pathi.hasNext()) {
                Path p = pathi.next();
                if (Files.isDirectory(p)) {
                    continue;
                }
                if (!CoreUtilities.toLowerCase(p.toString()).endsWith(".dsc")) {
                    if (!CoreUtilities.toLowerCase(p.toString()).endsWith(".disable")) {
                        Debug.error("File with path in scripts folder '" + ColorSet.emphasis + p.toString() + ColorSet.warning + "' is invalid. "
                                + "Script files must end in .dsc, ignored files must end in .disable");
                    }
                    continue;
                }
                File f = p.toFile();
                if (f.exists() && !f.isDirectory()) {
                    FileInputStream fis = new FileInputStream(f);
                    loadFile(f.getName(), CoreUtilities.streamToString(fis));
                    fis.close();
                }
            }
        }
        catch (IOException ex) {
            Debug.exception(ex);
        }
        for (ScriptEvent event : events) {
            event.init();
        }
        for (WorldScript script : ScriptEvent.currentWorldScripts) {
            Set<StringHolder> evts = script.contents.getConfigurationSection("events").getKeys(false);
            for (StringHolder evt : evts) {
                if (!script.eventsConfirmed.contains(evt)) {
                    Debug.error("Script event went unmatched: " + ColorSet.emphasis + script.title + ".events." + evt.str
                            + ColorSet.warning + "!");
                }
            }
        }
    }

    public static void unload() {
        // TODO: unload other things???
        disableAddons();
    }

    public static void disableAddons() {
        for (DenizenAddon addon : addons) {
            AddonInfo addonInfo = addon.getAddonInfo();
            try {
                Debug.info("Disabling addon " + ColorSet.emphasis + addonInfo.getName() + " " + addonInfo.getVersion());
                addon.disable();
                Debug.good("Successfully disabled " + ColorSet.emphasis + addonInfo.getName() + " " + addonInfo.getVersion());
            }
            catch (Exception e) {
                Debug.error("Failed to disable addon " + ColorSet.emphasis + addonInfo.getName() + " " + addonInfo.getVersion());
            }
        }
        addons.clear();
    }

    public static void runString(String cmd, MapTag defs, AbstractSender sender) {
        Tuple<CommandScriptSection, String> sec = CommandScriptSection.forLine(cmd);
        if (sec.one != null) {
            CommandQueue q = sec.one.toQueue();
            q.sender = sender;
            q.commandStack.peek().definitions.putAll(defs.getInternal());
            q.start();
        }
        else if (sec.two != null && sender != null) {
            sender.sendColoredMessage(ColorSet.warning + "[Denizen2/Error] " + sec.two);
        }
    }

    public static String escape(String input) {
        return input.replace("<", "\0TAGSTART").replace(">", "\0TAGEND");
    }

    public static String unescape(String input) {
        return input.replace("\0TAGSTART", "<").replace("\0TAGEND", ">");
    }

    public static Argument splitToArgument(String input, boolean wasQuoted, boolean quoteMode, Action<String> error) {
        if (input.length() == 0) {
            return new Argument();
        }
        int i1 = input.indexOf('<');
        int i2 = input.lastIndexOf('>');
        if (i1 < 0 || i1 > i2) {
            Argument arg = new Argument();
            arg.addBit(new TextArgumentBit(input, wasQuoted));
            return arg;
        }
        Argument arg = new Argument();
        arg.setQuoted(wasQuoted);
        arg.setQuoteMode(quoteMode);
        int len = input.length();
        int blocks = 0;
        int brackets = 0;
        StringBuilder blockbuilder = new StringBuilder();
        StringBuilder tbuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = input.charAt(i);
            if (c == '<') {
                // Awkward backup cheat here...
                if (input.lastIndexOf('>') > i) {
                    blocks++;
                    if (blocks == 1) {
                        continue;
                    }
                }
                else {
                    tbuilder.append(c);
                    continue;
                }
            }
            else if (c == '>') {
                if (blocks == 0) {
                    tbuilder.append(c);
                    continue;
                }
                blocks--;
                if (blocks == 0) {
                    if (tbuilder.length() > 0) {
                        arg.addBit(new TextArgumentBit(tbuilder.toString(), wasQuoted));
                        tbuilder = new StringBuilder();
                    }
                    String value = blockbuilder.toString();
                    String fallback = null;
                    int brack = 0;
                    for (int fb = 0; fb < value.length(); fb++) {
                        if (value.charAt(fb) == '[') {
                            brack++;
                        }
                        else if (value.charAt(fb) == ']') {
                            brack--;
                        }
                        else if (brack == 0 && value.charAt(fb) == '|' && value.charAt(fb - 1) == '|') {
                            fallback = value.substring(fb + 1);
                            value = value.substring(0, fb - 1);
                            break;
                        }
                    }
                    List<String> split = CoreUtilities.split(value, '.');
                    for (int s = 0; s < split.size(); s++) {
                        split.set(s, split.get(s).replace("&dot", ".").replace("&amp", "&"));
                    }
                    List<TagBit> bits = new ArrayList<>();
                    for (int x = 0; x < split.size(); x++) {
                        String key;
                        Argument variable;
                        if (split.get(x).length() > 1 && split.get(x).contains("[") && split.get(x).charAt(split.get(x).length() - 1) == ']') {
                            int index = split.get(x).indexOf('[');
                            variable = splitToArgument(split.get(x).substring(index + 1, split.get(x).length() - 1), wasQuoted, quoteMode, error);
                            split.set(x, CoreUtilities.toLowerCase(split.get(x).substring(0, index)));
                            if (split.get(x).length() == 0) {
                                if (x == 0) {
                                    split.set(x, "def");
                                }
                                else {
                                    split.set(x, "get");
                                }
                            }
                        }
                        else {
                            split.set(x, CoreUtilities.toLowerCase(split.get(x)));
                            variable = new Argument();
                        }
                        key = split.get(x);
                        bits.add(new TagBit(key, variable));
                    }
                    TagBit[] tbits = new TagBit[bits.size()];
                    TagArgumentBit tab = new TagArgumentBit(bits.toArray(tbits));
                    if (tab.bits.length > 0) {
                        AbstractTagBase start = tagBases.get(CoreUtilities.toLowerCase(tab.bits[0].key));
                        if (start == null) {
                            error.run("Invalid tag start: " + ColorSet.emphasis + tab.bits[0].key + ColorSet.warning + "!");
                        }
                        tab.setStart(start);
                    }
                    tab.setFallback(fallback == null ? null : splitToArgument(fallback.replace("&dot", ".").replace("&amp", "&"), false, false, error));
                    arg.addBit(tab);
                    blockbuilder = new StringBuilder();
                    continue;
                }
            }
            else if (blocks == 1 && c == '[') {
                brackets++;
            }
            else if (blocks == 1 && c == ']') {
                brackets--;
            }
            if (blocks > 0) {
                switch (c) {
                    case '.':
                        if (blocks > 1 || brackets > 0) {
                            blockbuilder.append("&dot");
                        }
                        else {
                            blockbuilder.append(c);
                        }
                        break;
                    case '&':
                        if (blocks > 1 || brackets > 0) {
                            blockbuilder.append("&amp");
                        }
                        else {
                            blockbuilder.append(c);
                        }
                        break;
                    default:
                        blockbuilder.append(c);
                        break;
                }
            }
            else {
                tbuilder.append(c);
            }
        }
        if (tbuilder.length() > 0) {
            arg.addBit(new TextArgumentBit(tbuilder.toString(), wasQuoted));
        }
        return arg;
    }

    public static void dumpDebug() {
        for (Map.Entry<String, CommandScript> script : currentScripts.entrySet()) {
            Debug.info("Script: " + script.getKey() + " / " + script.getValue().title);
            Debug.info("Debug mode: " + script.getValue().getDebugMode());
            Debug.info("YAML Contents: " + script.getValue().contents.saveToString());
            for (Map.Entry<String, CommandScriptSection> section : script.getValue().sections.entrySet()) {
                CommandStackEntry cse = section.getValue().toCSE();
                Debug.info(" --> Section: " + section.getKey() + " / " + cse.scriptTitle);
                Debug.info(" --> Debug mode: " + cse.getDebugMode());
                for (CommandEntry entry : cse.entries) {
                    Debug.info(" --> - " + entry.originalLine);
                    Debug.info(" --> --> " + entry.ownIndex + " / " + entry.blockStart + " / " + entry.blockEnd);
                    Debug.info(" --> --> " + entry.arguments.toString() + ", " + entry.namedArgs.toString());
                    Debug.info(" --> --> " + entry.cmdName + " / " + entry.command.getName()
                            + " / Waited?: " + entry.waitFor + " / Has-Block?: " + (entry.innerCommandBlock != null));
                }
            }
        }
    }
}
