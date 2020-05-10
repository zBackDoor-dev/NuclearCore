package me.darkboy.nuclear.system.resource;

import java.io.File;

/**
 * A loaded resource.
 *
 * @author zDqrkB0y_
 * @version 1.2
 */
public interface Resource extends ResourceSection {

    /**
     * @return the resource's file.
     */
    File getFile();

    /**
     * @return the resource's reference.
     *
     * @see ResourceReference
     */
    ResourceReference getReference();

    /**
     * @return handler responsible for the resource.
     */
    ResourceHandler getHandler();

}