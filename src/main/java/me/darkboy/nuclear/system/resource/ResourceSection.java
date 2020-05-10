package me.darkboy.nuclear.system.resource;

import java.util.List;
import java.util.Set;

/**
 * @author zDqrkB0y_
 */
public interface ResourceSection extends ResourceItem {

    // Logic

    boolean isRoot();

    boolean contains(String path);

    ResourceSection createSection(String name);

    // Getters

    String getName();

    String getCurrentPath();

    ResourceSection getRoot();

    ResourceSection getParent();

    Object get(String path, Class<?> type, Object def);

    Set<String> getKeys(boolean deep);

    String getString(String path, String def);

    boolean getBoolean(String path, boolean def);

    byte getByte(String path, byte def);

    char getChar(String path, char def);

    short getShort(String path, short def);

    int getInt(String path, int def);

    long getLong(String path, long def);

    float getFloat(String path, float def);

    double getDouble(String path, double def);

    <T> List<T> getList(String path, Class<T> clazz);

    ResourceSection getSection(String path);

    // Setters

    void set(String path, Object object);

    // Defaults

    default Object get(String path, Class<?> type) {
        return get(path, type, null);
    }

    default Set<String> getKeys() {
        return getKeys(false);
    }

    default String getString(String path) {
        return getString(path, null);
    }

    default boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    default byte getByte(String path) {
        return getByte(path, (byte) 0);
    }

    default char getChar(String path) {
        return getChar(path, Character.MIN_VALUE);
    }

    default short getShort(String path) {
        return getShort(path, (short) 0);
    }

    default int getInt(String path) {
        return getInt(path, 0);
    }

    default long getLong(String path) {
        return getLong(path, 0L);
    }

    default float getFloat(String path) {
        return getFloat(path, 0.0F);
    }

    default double getDouble(String path) {
        return getDouble(path, 0.0D);
    }

    default List<String> getStringList(String path) {
        return getList(path, String.class);
    }

    default ResourceSection getOrCreateSection(String section) {
        return contains(section) ? getSection(section) : createSection(section);
    }
}