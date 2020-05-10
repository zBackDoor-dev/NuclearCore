/*
    Project: NuclearCore
    @author NotStxnks_
    @since 20/04/2020 - 14:00
*/

package me.darkboy.nuclear.commands;

import me.darkboy.nuclear.files.yml.Language;
import me.darkboy.nuclear.system.register.command.NuclearCommand;
import me.darkboy.nuclear.system.util.Players;
import me.darkboy.nuclear.utils.Permissions;
import me.darkboy.nuclear.utils.PluginData;
import org.bukkit.command.CommandSender;

public class NuclearCoreCMD extends NuclearCommand {

    public NuclearCoreCMD() {
        this.setAliases("nuclearcore", "nc");
        this.setPermission(Permissions.ADMIN.getPermission());
        this.setAllowConsole(true);
        this.setPermissionDenyMessage(Language.NO_PERM.getMessage());
        this.setChildren(new ReloadCMD());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Players.sendMessage(sender, "&3Nuclear&bCore §7by §e" + PluginData.author);
    }
}
