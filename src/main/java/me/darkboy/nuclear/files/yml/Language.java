package me.darkboy.nuclear.files.yml;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.darkboy.nuclear.files.ResourceType;
import me.darkboy.nuclear.files.Resources;
import me.darkboy.nuclear.system.NuclearPlugin;
import me.darkboy.nuclear.system.resource.yml.ResourceYaml;
import me.darkboy.nuclear.system.util.Messages;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Language {

    PREFIX("prefix", "&3Nuclear&bCore &7➜"),

    USAGE("messages.generic.usage", "{prefix} &7Incorrect usage please use &b{usage} {args}"),

    RELOAD("messages.generic.reload", "{prefix} &7Plugin successfully reloaded!"),

    NO_PERM("messages.generic.no-perm", "{prefix} &7You don't have the permission!"),

    FLY_ENABLED("messages.commands.fly.enabled", "{prefix} &7Fly has been enabled!"),

    FLY_DISABLED("messages.commands.fly.disabled", "{prefix} &7Fly has been disabled!")

    ;

    public static ResourceYaml getLocale() {
        return (ResourceYaml) Resources.get().getResource(ResourceType.LANGUAGE);
    }

    private final String path;
    private final List<String> def;

    /**
     * @param path the path pointing to the message.
     * @param def  default value in case message is missing or null.
     */
    Language(String path, String... def) {
        this.path = path;
        this.def = Messages.colour(def);
    }

    /**
     * @return yaml path pointing to the message.
     */
    public String getPath() {
        return path;
    }

    public static void saveFile() {
        NuclearPlugin.getResourceProvider().saveResource(Language.getLocale());
    }

    /**
     * @return default value in case message is missing or {@code null}.
     */
    public List<String> getDefault() {
        return def;
    }

    public static String getPrefix() {
        return Language.PREFIX.getMessage().get(0);
    }

    /**
     * @return list of messages or {@link Language#def} if missing.
     */
    public List<String> getMessage(Object... rep) {
        List<String> replacements = Stream.of(rep)
                .map(String::valueOf)
                .collect(Collectors.toList());

        List<String> unreplaced;

        if (getLocale().getConfiguration().isString(path)) {
            unreplaced = Lists.newArrayList(getLocale().getString(path, def.get(0)));
        } else {
            unreplaced = getLocale().getConfiguration().getStringList(path);
        }

        // Use the default if the returned is empty.
        if (unreplaced.isEmpty()) {
            unreplaced = def;
        }

        // Not replacing anything if the message is the prefix.
        if (this == PREFIX) {
            return unreplaced;
        }

        Map<String, String> replace = Maps.newHashMap();
        List<String> replaced = Lists.newArrayList();

        // Replacements list acting as a key-value map:
        // key1, value1, key2, value2, key3, value3, etc..
        List<String> temp = Lists.newArrayList(replacements);
        temp.add("{prefix}");
        temp.add(Language.PREFIX.getMessage().get(0)); // No need to check length.

        // Convert key-value list to map.
        for (int i = 0; i < temp.size() - 1; i += 2) {
            replace.put(temp.get(i), temp.get(i + 1));
        }

        // Finally, replace.
        for (String s : unreplaced) {
            for (Map.Entry<String, String> r : replace.entrySet()) {
                s = s.replace(r.getKey(), r.getValue());
            }

            replaced.add(s);
        }

        return Messages.colour(replaced);
    }
}