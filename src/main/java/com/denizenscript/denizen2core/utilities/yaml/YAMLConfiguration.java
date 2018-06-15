package com.denizenscript.denizen2core.utilities.yaml;

import com.denizenscript.denizen2core.arguments.TextArgumentBit;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.ListTag;
import com.denizenscript.denizen2core.tags.objects.MapTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

/**
 * Represents a YAML file.
 */
public class YAMLConfiguration {

    public static YAMLConfiguration load(String data) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setAllowUnicode(true);
        Yaml yaml = new Yaml(options);
        Object obj = yaml.load(data);
        YAMLConfiguration config = new YAMLConfiguration();
        if (obj == null) {
            return null;
        }
        else if (obj instanceof String) {
            config.contents = new HashMap<>();
            config.contents.put(null, obj);
        }
        else if (obj instanceof Map) {
            config.contents = (Map<StringHolder, Object>) obj;
        }
        else {
            return null;
        }
        switchKeys(config.contents);
        return config;
    }

    Map<StringHolder, Object> contents = null;

    /**
     * Use StringHolders instead of strings.
     */
    private static void switchKeys(Map<StringHolder, Object> objs) {
        for (Object o : new ArrayList<>(objs.keySet())) {
            Object got = objs.get(o);
            objs.remove(o);
            objs.put(new StringHolder(o == null ? "null" : o.toString()), got);
        }
        for (Map.Entry<StringHolder, Object> str : objs.entrySet()) {
            if (str.getValue() instanceof Map) {
                Map map = (Map<StringHolder, Object>) str.getValue();
                switchKeys(map);
                //objs.remove(map);
                objs.put(str.getKey(), map);
            }
        }
    }

    private static Map<String, Object> reverse(Map<StringHolder, Object> objs) {
        HashMap<String, Object> map = new HashMap<>();
        for (Map.Entry<StringHolder, Object> obj : objs.entrySet()) {
            if (obj.getValue() instanceof Map) {
                map.put(obj.getKey().str, reverse((Map<StringHolder, Object>) obj.getValue()));
            }
            else {
                map.put(obj.getKey().str, obj.getValue());
            }
        }
        return map;
    }

    private static List<String> patchListNonsense(List<Object> objs) {
        List<String> list = new ArrayList<>();
        for (Object o : objs) {
            if (o == null) {
                list.add("null");
            }
            else {
                list.add(o.toString());
            }
        }
        return list;
    }

    public YAMLConfiguration() {
        contents = new HashMap<>();
    }

    public Set<StringHolder> getKeys(boolean deep) {
        if (!deep) {
            return new HashSet<>(contents.keySet());
        }
        else {
            return getKeysDeep(contents, "");
        }
    }

    public Map<StringHolder, Object> getMap() {
        return new HashMap<>(contents);
    }

    private Set<StringHolder> getKeysDeep(Map<StringHolder, Object> objs, String base) {
        Set<StringHolder> strings = new HashSet<>();
        for (Map.Entry<StringHolder, Object> obj : objs.entrySet()) {
            strings.add(new StringHolder(base + obj.getKey()));
            if (obj.getValue() instanceof Map) {
                strings.addAll(getKeysDeep((Map<StringHolder, Object>) obj.getValue(), base + obj.getKey() + "."));
            }
        }
        return strings;
    }

    public String saveToString() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setAllowUnicode(true);
        Yaml yaml = new Yaml(options);
        return yaml.dump(reverse(contents));
    }

    public Object get(String path) {
        List<String> parts = CoreUtilities.split(path, '.');
        Map<StringHolder, Object> portion = contents;
        for (int i = 0; i < parts.size(); i++) {
            Object oPortion = portion.get(new StringHolder(parts.get(i)));
            if (oPortion == null) {
                return null;
            }
            else if (parts.size() == i + 1) {
                return oPortion;
            }
            else if (oPortion instanceof Map) {
                portion = (Map<StringHolder, Object>) oPortion;
            }
            else {
                return null;
            }
        }
        return null;
    }

    public void set(String path, Object o) {
        if (o instanceof YAMLConfiguration) {
            o = new HashMap<>(((YAMLConfiguration) o).contents);
        }
        List<String> parts = CoreUtilities.split(path, '.');
        Map<StringHolder, Object> portion = contents;
        for (int i = 0; i < parts.size(); i++) {
            Object oPortion = portion.get(new StringHolder(parts.get(i)));
            if (parts.size() == i + 1) {
                if (o == null) {
                    portion.remove(new StringHolder(parts.get(i)));
                    emptyEmptyMaps(parts);
                }
                else {
                    portion.put(new StringHolder(parts.get(i)), o);
                }
                return;
            }
            else if (oPortion == null) {
                Map<StringHolder, Object> map = new HashMap<>();
                portion.put(new StringHolder(parts.get(i)), map);
                portion = map;
            }
            else if (oPortion instanceof Map) {
                portion = (Map<StringHolder, Object>) oPortion;
            }
            else {
                Map<StringHolder, Object> map = new HashMap<>();
                portion.put(new StringHolder(parts.get(i)), map);
                portion = map;
            }
        }
    }

    void emptyEmptyMaps(List<String> parts) {
        Map<StringHolder, Object> portion = contents;
        for (int i = 0; i < parts.size(); i++) {
            Object oPortion = portion.get(new StringHolder(parts.get(i)));
            if (oPortion == null) {
                return;
            }
            else if (oPortion instanceof Map) {
                if (((Map<StringHolder, Object>) oPortion).size() == 0) {
                    portion.remove(new StringHolder(parts.get(i)));
                    emptyEmptyMaps(parts);
                    return;
                }
                portion = (Map<StringHolder, Object>) oPortion;
            }
            else {
                return;
            }
        }
    }

    public boolean contains(String path) {
        return get(path) != null;
    }

    public String getString(String path) {
        Object o = get(path);
        if (o == null) {
            return null;
        }
        return o.toString();
    }

    public String getString(String path, String def) {
        Object o = get(path);
        if (o == null) {
            return def;
        }
        return o.toString();
    }

    public boolean isList(String path) {
        Object o = get(path);
        if (o == null) {
            return false;
        }
        if (!(o instanceof List)) {
            return false;
        }
        return true;
    }

    public AbstractTagObject objectFor(Object obj) {
        if (obj instanceof List) {
            return listify((List<Object>) obj);
        }
        if (obj instanceof Map) {
            return mapify((Map<Object, Object>) obj);
        }
        return new TextArgumentBit(obj.toString(), false).value;
    }

    public MapTag mapify(Map<Object, Object> map) {
        MapTag mt = new MapTag();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            mt.getInternal().put(CoreUtilities.toLowerCase(entry.getKey().toString()), objectFor(entry.getValue()));
        }
        return mt;
    }

    public ListTag listify(List<Object> list) {
        ListTag lt = new ListTag();
        for (Object obj : list) {
            lt.getInternal().add(objectFor(obj));
        }
        return lt;
    }

    public ListTag getListTag(String path) {
        return listify(getList(path));
    }

    public List<Object> getList(String path) {
        Object o = get(path);
        if (o == null) {
            return null;
        }
        if (!(o instanceof List)) {
            return null;
        }
        return (List<Object>) o;
    }

    public List<String> getStringList(String path) {
        Object o = get(path);
        if (o == null) {
            return null;
        }
        if (!(o instanceof List)) {
            return null;
        }
        return patchListNonsense((List<Object>) o);
    }

    public YAMLConfiguration getConfigurationSection(String path) {
        if (path.length() == 0) {
            return this;
        }
        List<String> parts = CoreUtilities.split(path, '.');
        Map<StringHolder, Object> portion = contents;
        for (int i = 0; i < parts.size(); i++) {
            Object oPortion = portion.get(new StringHolder(parts.get(i)));
            if (oPortion == null) {
                return null;
            }
            else if (parts.size() == i + 1) {
                YAMLConfiguration configuration = new YAMLConfiguration();
                if (!(oPortion instanceof Map)) {
                    return null;
                }
                configuration.contents = (Map<StringHolder, Object>) oPortion;
                return configuration;
            }
            else if (oPortion instanceof Map) {
                portion = (Map<StringHolder, Object>) oPortion;
            }
            else {
                return null;
            }
        }
        return null;
    }
}
