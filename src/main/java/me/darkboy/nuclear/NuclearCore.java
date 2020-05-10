package me.darkboy.nuclear;

import me.darkboy.nuclear.commands.FlyCMD;
import me.darkboy.nuclear.commands.NuclearCoreCMD;
import me.darkboy.nuclear.files.Resources;
import me.darkboy.nuclear.system.NuclearPlugin;

public final class NuclearCore extends NuclearPlugin {

    private static NuclearCore plugin;

    @Override
    public void enable() {
        // Instance
        plugin = this;

        // Register plugin classes
        this.register(Resources.class,
                NuclearCoreCMD.class, FlyCMD.class);

        console.info("&ePlugin Enabled!");
    }

    @Override
    public void disable() {
        // Plugin shutdown logic
        console.info("&ePlugin Disabled!");
    }

    public static NuclearCore getPlugin() {
        return plugin;
    }
}
