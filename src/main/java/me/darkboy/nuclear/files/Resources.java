package me.darkboy.nuclear.files;

import me.darkboy.nuclear.system.NuclearPlugin;
import me.darkboy.nuclear.system.register.Registrable;
import me.darkboy.nuclear.system.resource.Resource;
import me.darkboy.nuclear.system.resource.ResourceReference;

import java.util.stream.Stream;

public class Resources implements Registrable {

    private static final Resources instance = new Resources();

    public static Resources get() {
        return instance;
    }

    @Override
    public void register() {
        Stream.of(ResourceType.values()).forEach(type -> NuclearPlugin.getResourceProvider().loadResource(type.getReference()));
    }

    private Resource getResource(ResourceReference reference) {
        return NuclearPlugin.getResourceProvider().getResource(reference);
    }

    public Resource getResource(ResourceType type) {
        return getResource(type.getReference());
    }
}