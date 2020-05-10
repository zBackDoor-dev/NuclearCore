/*
    Project: NuclearCore
    @author NotStxnks_
    @since 20/04/2020 - 13:56
*/

package me.darkboy.nuclear.commands;

import me.darkboy.nuclear.files.Resources;
import me.darkboy.nuclear.files.yml.Language;
import me.darkboy.nuclear.system.register.command.NuclearCommand;
import me.darkboy.nuclear.system.util.Players;
import me.darkboy.nuclear.utils.Permissions;
import org.bukkit.command.CommandSender;

public class ReloadCMD extends NuclearCommand {

    public ReloadCMD() {
        this.setAliases("reload", "rl");
        this.setPermission(Permissions.ADMIN.getPermission());
        this.setAllowConsole(true);
        this.setPermissionDenyMessage(Language.NO_PERM.getMessage());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Resources.get().register();
        Players.sendMessage(sender, Language.RELOAD.getMessage());
    }
}
