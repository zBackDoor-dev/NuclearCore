package me.darkboy.nuclear.system.resource;

import java.util.List;

/**
 * The {@link ResourceHandler} interface is used for loading and saving
 * different types of files.
 *
 * @author zDqrkB0y_
 */
public interface ResourceHandler<T extends Resource> {

    /**
     * @param provider the resource provider.
     * @param reference resource's reference.
     * @return a new or existing resource if {@code fromCache} is true.
     */
    T load(ResourceProvider provider, ResourceReference reference);

    /**
     * @param resource the resource we're saving.
     */
    void save(T resource);

    /**
     * Extensions do not have a period ('{@literal .}') at the start.
     *
     * @return a list containing all possible extensions.
     */
    List<? extends CharSequence> getExtensions();

}