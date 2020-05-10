package me.darkboy.nuclear.system.util;

import org.bukkit.ChatColor;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * The {@link Messages} class contains many utility methods which
 * aid with both Bukkit and regular message altering.
 *
 * @author NotStxnks_
 */
public final class Messages {

    // Constants
    private static final char BUKKIT_COLOUR_CODE = ChatColor.COLOR_CHAR;
    private static final char ALT_COLOUR_CODE = '&';

    // Suppresses default constructor.
    private Messages() {

    }

    /**
     * Returns a coloured version of a string.
     *
     * @param s the string to be coloured.
     * @return coloured string.
     */
    public static String colour(String s) {
        return s != null ? ChatColor.translateAlternateColorCodes(ALT_COLOUR_CODE, s) : null;
    }

    /**
     * Returns a coloured version of a list.
     *
     * @param iterable list to be coloured.
     * @return coloured list.
     */
    public static List<String> colour(Iterable<? extends String> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(Objects::nonNull)
                .map(Messages::colour)
                .collect(Collectors.toList());
    }

    public static List<String> colour(String... args) {
        return colour(Arrays.asList(args));
    }

    /**
     * Replaces all of the {@link Messages#BUKKIT_COLOUR_CODE}'s with
     * {@link Messages#ALT_COLOUR_CODE}. This method is usually invoked
     * when wanting to output a user-friendly string to a configuration file.
     *
     * @param s string to be uncoloured.
     * @return uncoloured string.
     */
    public static String uncolour(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(s.length());
        CharacterIterator it = new StringCharacterIterator(s);

        for (char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
            switch (c) {
                case BUKKIT_COLOUR_CODE:
                    builder.append(ALT_COLOUR_CODE);
                    break;
                default:
                    builder.append(c);
                    break;
            }
        }

        return builder.toString();
    }

    /**
     * Calls {@link Messages#uncolour(String)} on each element
     * of the list.
     *
     * @param iterable iterable to be uncoloured.
     * @return uncoloured list.
     */
    public static List<String> uncolour(Iterable<? extends String> iterable) {
        return iterable == null ? null : StreamSupport.stream(iterable.spliterator(), false)
                .filter(Objects::nonNull)
                .map(Messages::uncolour)
                .collect(Collectors.toList());
    }

    public static List<String> uncolour(String... args) {
        return uncolour(Arrays.asList(args));
    }
}