package me.darkboy.nuclear.system.resource;

import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import me.darkboy.nuclear.system.NuclearPlugin;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author zDqrkB0y_
 */
public class DefaultResourceProvider implements ResourceProvider {

    private final NuclearPlugin plugin;
    private final File dataFolder;

    private Map<ResourceReference, Resource> cachedResources = Maps.newHashMap();
    private Map<String, ResourceHandler> resourceHandlers = Maps.newHashMap();

    public DefaultResourceProvider(NuclearPlugin plugin) {
        this.plugin = plugin;
        dataFolder = plugin.getDataFolder();
    }

    @Override
    public Resource loadResource(ResourceReference reference) {
        File file = new File(dataFolder + reference.getSeparatorPathStart(), reference.getChild());

        try {
            long start = System.currentTimeMillis();
            Files.createParentDirs(file);

            if (!file.exists()) {
                // Safe loading of defaults (impl in v0.1.8):
                //
                // ByteStreams#copy will throw a NullPointerException if the file is not
                // present in the plugin's /resources/ folder. Catching this exception
                // allows for loading dynamic resources without defaults.

                try (InputStream in = plugin.getResource(reference.getSeparatorPathEnd() + reference.getChild());
                     OutputStream out = new FileOutputStream(file)) {
                    ByteStreams.copy(in, out);

                    plugin.getConsole().info(String.format("Loaded resource defaults for &e%s&r (time: &a%s&rms).",
                            reference.getSeparatorPathEnd() + reference.getChild(), System.currentTimeMillis() - start));
                }
            }
        } catch (IOException e) {
            plugin.getConsole().error(String.format("An IOException occurred when generating resource defaults for [&c%s&r]:", reference.getPath()));
            plugin.getConsole().exception(e);
        } catch (NullPointerException e) {
            // Ignored: no defaults to load
        }

        Resource resource = getResourceHandler(reference.getExtension())
                .map(handler -> handler.load(this, reference))
                .orElse(null);

        cachedResources.put(reference, resource);
        return resource;
    }

    @Override
    public void loadResource(ResourceReference reference, ResourceLoadResultHandler resultHandler) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                resultHandler.onComplete(loadResource(reference));
            } catch (Exception e) {
                resultHandler.onFailure(e);
            }
        });
    }

    @Override
    public void saveResource(Resource resource) {
        getResourceHandler(resource.getReference().getExtension())
                .ifPresent(handler -> handler.save(resource));
    }

    @Override
    public void addResourceHandler(ResourceHandler loader) {
        loader.getExtensions()
                .stream()
                .filter(Objects::nonNull)
                .forEach(extension -> resourceHandlers.put(extension.toString(), loader));
    }

    @Override
    public Resource getResource(ResourceReference reference) {
        return cachedResources.containsKey(reference) ? cachedResources.get(reference) : loadResource(reference);
    }

    @Override
    public File getDataFolder() {
        return dataFolder;
    }

    private Optional<ResourceHandler> getResourceHandler(String extension) {
        return resourceHandlers.values()
                .stream()
                .filter(handler -> handler.getExtensions().contains(extension))
                .findFirst();
    }
}