package me.darkboy.nuclear.system.register.command;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author NotStxnks_
 */
public interface NuclearCommandTabCompleter {

    /**
     * @param sender tab sender.
     * @param alias  alias of the command being tabbed.
     * @param args   arguments passed.
     * @return list of tab complete suggestions.
     */
    List<String> tabComplete(CommandSender sender, String alias, String[] args);

}