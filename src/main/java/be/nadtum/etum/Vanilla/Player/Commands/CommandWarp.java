package be.nadtum.etum.Vanilla.Player.Commands;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class CommandWarp implements CommandExecutor {

    private final Map<String, Location> warps = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Vous ne pouvez pas utiliser cette commande en tant que console.");
            return false;
        }

        if (args.length == 0) {
            if (cmd.getName().equalsIgnoreCase("spawn")) {
                TeleportToWarp(player, "Spawn");
            }
        }

        if (args.length == 2) {
            if(!player.hasPermission("admin")){
                player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas la permission d'utiliser cette commande.");
                return false;
            }


            if (args[0].equalsIgnoreCase("set")) {
                if (warps.containsKey(args[1])) {
                    player.sendMessage(PrefixMessage.erreur() + "Le warp existe déjà.");
                    return false;
                }

                Location loc = player.getLocation();
                warps.put(args[1], loc);

                player.sendMessage(PrefixMessage.admin() + "Le warp §b" + args[1] + " §ea été créé.");
                return false;
            }

            if (args[0].equalsIgnoreCase("delete")) {
                if (!warps.containsKey(args[1])) {
                    player.sendMessage(PrefixMessage.erreur() + "Le warp n'existe pas.");
                    return false;
                }

                warps.remove(args[1]);
                player.sendMessage(PrefixMessage.admin() + "Le warp §b" + args[1] + " §ea été supprimé.");
                return false;
            }
        }

        return false;
    }

    @EventHandler
    public void openDepot(NPCRightClickEvent event) {
        if (!(event.getNPC().getId() == 3)) {
            return;
        }
        TeleportToWarp(event.getClicker(), "etum");
        // Your NPCRightClickEvent handling code here.
    }

    public static void TeleportToWarp(Player player, String spawn) {

        Location warpLocation = FichierGestion.getCfgSettings().getLocation("Warp." + spawn);
        player.teleport(warpLocation);
        player.sendMessage(PrefixMessage.serveur() + "Vous avez été téléporté au warp §b" + spawn + ".");
    }
}
