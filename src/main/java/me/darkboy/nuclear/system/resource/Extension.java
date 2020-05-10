package me.darkboy.nuclear.system.resource;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author zDqrkB0y_
 */
public enum Extension {

    YML("yml", "yaml"),
    JSON("json"),

    ;

    /**
     * Retrieves the {@link Extension} containing the provided
     * alias.
     *
     * @param alias the alias to match.
     * @return extension with an alias matching the provided alias.
     */
    public static Extension from(String alias) {
        return Stream.of(values())
                .filter(extension -> extension.getExtensions().contains(alias.toLowerCase()))
                .findFirst()
                .orElse(null);
    }

    private final List<String> extensions;


    /**
     * @param extensions array of extensions for the file type.
     */
    Extension(String... extensions) {
        this.extensions = Lists.newArrayList(extensions);
    }

    /**
     * @return all of the extensions.
     */
    public List<String> getExtensions() {
        return extensions;
    }

    /**
     * @return the first extension.
     */
    public String getExtension() {
        return extensions.iterator().next();
    }
}