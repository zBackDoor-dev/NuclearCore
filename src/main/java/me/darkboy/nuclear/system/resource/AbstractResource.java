package me.darkboy.nuclear.system.resource;

import java.io.File;

/**
 * @author zDqrkB0y_
 */
public abstract class AbstractResource implements Resource {

    private final File file;
    private final ResourceReference reference;
    private final ResourceHandler handler;

    /**
     * @param file      a <b>valid</b> file location pointing to the resource.
     * @param reference the resource's reference.
     * @param handler   the handler which loaded this resource.
     */
    public AbstractResource(File file, ResourceReference reference, ResourceHandler handler) {
        this.file = file;
        this.reference = reference;
        this.handler = handler;
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public ResourceReference getReference() {
        return reference;
    }

    @Override
    public ResourceHandler getHandler() {
        return handler;
    }

    /**
     * @return the loaded configuration file of the resource, or null if it had not been loaded correctly.
     */
    public abstract Object getConfiguration();

    /**
     * @param configuration the configuration to set.
     */
    public abstract void setConfiguration(Object configuration);
}