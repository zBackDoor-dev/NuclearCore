/*
    Project: NuclearCore
    @author NotStxnks_
    @since 20/04/2020 - 13:15
*/

package me.darkboy.nuclear.commands;

import me.darkboy.nuclear.files.yml.Language;
import me.darkboy.nuclear.system.register.command.NuclearCommand;
import me.darkboy.nuclear.system.util.Players;
import me.darkboy.nuclear.utils.Permissions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCMD extends NuclearCommand {

    public FlyCMD() {
        this.setAliases("fly", "flight");
        this.setPermission(Permissions.FLY_PERM.getPermission());
        this.setPermissionDenyMessage(Language.NO_PERM.getMessage());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);
            Players.sendMessage(player, Language.FLY_DISABLED.getMessage());
        } else {
            player.setAllowFlight(true);
            Players.sendMessage(player, Language.FLY_ENABLED.getMessage());
        }
    }
}
