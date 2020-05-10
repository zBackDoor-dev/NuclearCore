package me.darkboy.nuclear.system.resource.yml;

import me.darkboy.nuclear.system.resource.Extension;
import me.darkboy.nuclear.system.resource.ResourceHandler;
import me.darkboy.nuclear.system.resource.ResourceProvider;
import me.darkboy.nuclear.system.resource.ResourceReference;
import me.darkboy.nuclear.system.util.logging.StaticLog;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author zDqrkB0y_
 */
public class YamlResourceHandler implements ResourceHandler<ResourceYaml> {

    @Override
    public ResourceYaml load(ResourceProvider provider, ResourceReference reference) {
        ResourceYaml resource = new ResourceYaml(new File(provider.getDataFolder() + reference.getSeparatorPathStart(), reference.getChild()), reference, this);
        resource.setConfiguration(YamlConfiguration.loadConfiguration(resource.getFile()));

        return resource;
    }

    @Override
    public void save(ResourceYaml resource) {
        try {
            resource.getRootConfigurationSection().save(resource.getFile());
        } catch (IOException e) {
            StaticLog.error(String.format("An IOException occurred when trying to save &c%s&r:", resource.getReference()));
            StaticLog.exception(e);
        }
    }

    @Override
    public List<? extends CharSequence> getExtensions() {
        return Extension.YML.getExtensions();
    }
}