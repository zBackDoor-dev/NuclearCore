package me.darkboy.nuclear.files.yml;

import com.google.common.collect.Lists;
import me.darkboy.nuclear.files.ResourceType;
import me.darkboy.nuclear.files.Resources;
import me.darkboy.nuclear.system.NuclearPlugin;
import me.darkboy.nuclear.system.resource.yml.ResourceYaml;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.stream.Collectors;

public enum Config {

    CONFIG_VERSION("config-version", 1),

    ;

    public static ResourceYaml getConfig() {
        return (ResourceYaml) Resources.get().getResource(ResourceType.CONFIG);
    }

    private final String path;
    private final Object def;

    /**
     * @param path the path to the setting.
     * @param def  default value of the setting.
     */
    Config(String path, Object def) {
        this.path = path;
        this.def = def;
    }

    /**
     * @return path of the setting in Config.yml.
     */
    public String getPath() {
        return path;
    }

    public static void saveFile() {
        NuclearPlugin.getResourceProvider().saveResource(Config.getConfig());
    }

    /**
     * @return value of the path as a string, also replaces the {prefix} placeholder.
     */
    public String getAsString() {
        return getConfig().getString(path, String.valueOf(def));
    }

    /**
     * @return value of the path as a string list.
     */
    public List<String> getAsStringList() {
        List<String> list = Lists.newArrayList();

        if (getConfig().getConfiguration().isString(path)) {
            list.add(getAsString());
        } else {
            list.addAll(getConfig().getConfiguration().getStringList(path));
        }

        return list.stream()
                .map(s -> s.replace("{prefix}", Language.PREFIX.getMessage().get(0)))
                .collect(Collectors.toList());
    }

    /**
     * @return value of the path as an int.
     */
    public int getAsInt() {
        return getConfig().getInt(path, Integer.parseInt(getAsString()));
    }

    /**
     * @return default value as an integer.
     */
    public int getAsIntDefault() {
        return Integer.parseInt(String.valueOf(def));
    }

    /**
     * @return value of the path as a short.
     */
    public short getAsShort() {
        return getConfig().getShort(path, Short.parseShort(getAsString()));
    }

    /**
     * @return value of the path as a double.
     */
    public double getAsDouble() {
        return getConfig().getDouble(path, Double.parseDouble(getAsString()));
    }

    /**
     * @return value of the path as a boolean.
     */
    public boolean getAsBoolean() {
        return getConfig().getBoolean(path, Boolean.parseBoolean(getAsString()));
    }

    public void setValue(Object value) {
        getConfig().set(path, value);
    }

    /**
     * @return value of the path as a configuration section.
     */
    public ConfigurationSection getAsConfigurationSection() {
        return getConfig().getConfigurationSection(path);
    }
}