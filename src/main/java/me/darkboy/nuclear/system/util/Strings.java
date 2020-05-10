package me.darkboy.nuclear.system.util;

/**
 * @author zDqrkB0y_
 */
public final class Strings {

    private Strings() {}

    /**
     * Retrieves the last string split in the array of matches of the
     * provided regular expression.
     *
     * @param str   string to split.
     * @param regex delimiting regular expression.
     * @param limit the result threshold.
     * @return last string split.
     *
     * @see String#split(String)
     */
    public static String splitRetrieveLast(String str, String regex, int limit) {
        String[] parts = str.split(regex, limit);

        if (parts.length == 0) {
            return str;
        }

        return parts[parts.length - 1];
    }

    /**
     * Retrieves the last string split in the array of matches of the
     * provided regular expression.
     *
     * @param str   string to split.
     * @param regex delimiting regular expression.
     * @return last string split.
     *
     * @see String#split(String)
     */
    public static String splitRetrieveLast(String str, String regex) {
        return splitRetrieveLast(str, regex, 0);
    }
}