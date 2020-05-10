package me.darkboy.nuclear.system.resource.yml;

import me.darkboy.nuclear.system.resource.AbstractResource;
import me.darkboy.nuclear.system.resource.ResourceHandler;
import me.darkboy.nuclear.system.resource.ResourceReference;
import me.darkboy.nuclear.system.resource.ResourceSection;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zDqrkB0y_
 */
public class ResourceYaml extends AbstractResource {

    private ConfigurationSection root;

    public ResourceYaml(File file, ResourceReference reference, ResourceHandler handler) {
        super(file, reference, handler);
    }

    public ResourceYaml(ResourceYaml resource, ConfigurationSection root) {
        super(resource.getFile(), resource.getReference(), resource.getHandler());
        this.root = root;
    }

    @Override
    public boolean isRoot() {
        return root.getParent() == null;
    }

    @Override
    public boolean contains(String path) {
        return root.contains(path);
    }

    @Override
    public ResourceSection createSection(String name) {
        if (contains(name)) {
            return getSection(name);
        }

        return new ResourceYaml(this, root.createSection(name));
    }

    @Override
    public String getName() {
        return root.getName();
    }

    @Override
    public String getCurrentPath() {
        return root.getCurrentPath();
    }

    @Override
    public ResourceSection getRoot() {
        return new ResourceYaml(this, root.getRoot());
    }

    @Override
    public ResourceSection getParent() {
        return new ResourceYaml(this, root.getParent());
    }

    @Override
    public ConfigurationSection getConfiguration() {
        return root;
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        return root.getKeys(deep);
    }

    @Override
    public Object get(String path, Class<?> type, Object def) {
        Object ret = root.get(path);

        if (ret == null || ret.getClass().isInstance(type)) {
            return def;
        }

        return ret;
    }

    @Override
    public String getString(String path, String def) {
        return root.getString(path, def);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        return root.getBoolean(path, def);
    }

    @Override
    public byte getByte(String path, byte def) {
        return (byte) root.getInt(path, def);
    }

    @Override
    public char getChar(String path, char def) {
        try {
            return getString(path, Character.toString(def)).charAt(0);
        } catch (StringIndexOutOfBoundsException | NullPointerException e) {
            /* Ignored */
        }

        return def;
    }

    @Override
    public short getShort(String path, short def) {
        return (short) root.getInt(path, def);
    }

    @Override
    public int getInt(String path, int def) {
        return root.getInt(path, def);
    }

    @Override
    public long getLong(String path, long def) {
        return root.getLong(path, def);
    }

    @Override
    public float getFloat(String path, float def) {
        return (float) root.getDouble(path, def);
    }

    @Override
    public double getDouble(String path, double def) {
        return root.getDouble(path, def);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getList(String path, Class<T> clazz) {
        return (List<T>) root.getList(path, new ArrayList<T>());
    }

    @Override
    public ResourceSection getSection(String path) {
        if (!contains(path)) {
            return null;
        }

        return new ResourceYaml(this, root.getConfigurationSection(path));
    }

    @Override
    public void set(String path, Object object) {
        root.set(path, object);
    }

    @Override
    public void setConfiguration(Object configuration) {
        if (configuration instanceof ConfigurationSection) {
            this.root = (ConfigurationSection) configuration;
        }
    }

    public ConfigurationSection createSection(String path, Map<?, ?> keyValues) {
        return root.createSection(path, keyValues);
    }

    public YamlConfiguration getRootConfigurationSection() {
        return (YamlConfiguration) root;
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return root.getConfigurationSection(path);
    }
}