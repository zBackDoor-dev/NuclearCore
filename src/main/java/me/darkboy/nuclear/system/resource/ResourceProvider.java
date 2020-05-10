package me.darkboy.nuclear.system.resource;

import java.io.File;

/**
 * Responsible for handling the loading and saving of resources apart
 * of a specific plugin.
 *
 * @author zDqrkB0y_
 */
public interface ResourceProvider {

    /**
     * @param reference resource's reference containing the file path and extension.
     * @return a loaded resource.
     */
    Resource loadResource(ResourceReference reference);

    /**
     * Used for asynchronously loading resources without the need of surrounding the method
     * in a runnable.
     *
     * @param reference     resource's reference containing the file path and extension.
     * @param resultHandler result handler.
     */
    void loadResource(ResourceReference reference, ResourceLoadResultHandler resultHandler);

    /**
     * Saves a resource using the appropriate handlers.
     *
     * @param resource the resource to be saved.
     */
    void saveResource(Resource resource);

    /**
     * @param handler the resource handler to add.
     */
    void addResourceHandler(ResourceHandler handler);

    /**
     * @param reference the resource's reference.
     * @return the resource.
     */
    Resource getResource(ResourceReference reference);

    /**
     * @return the plugin's base data folder.
     */
    File getDataFolder();

}