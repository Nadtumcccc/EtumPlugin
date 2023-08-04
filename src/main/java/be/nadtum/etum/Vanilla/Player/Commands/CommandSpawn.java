package be.nadtum.etum.Vanilla.Player.Commands;

import be.nadtum.etum.Utility.Modules.*;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class CommandSpawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Vous ne pouvez pas utiliser cette commande en tant que console.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            if (cmd.getName().equalsIgnoreCase("spawn")) {
                Teleportation.PlayerTpToSpawn(player, "Spawn");
            }
        }

        if (args.length == 2) {
            YamlConfiguration cfg = FichierGestion.getCfgPermission();
            if (!cfg.contains("Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission.admin")) {
                if (!player.isOp()) {
                    player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas la permission d'utiliser cette commande.");
                    return false;
                }
            }

            YamlConfiguration cfgSpawn = FichierGestion.getCfgSettings();

            if (args[0].equalsIgnoreCase("set")) {
                if (cfgSpawn.contains("Spawn." + args[1])) {
                    player.sendMessage(PrefixMessage.erreur() + "Le spawn existe déjà.");
                    return false;
                }

                Location loc = player.getLocation();

                cfgSpawn.set("Spawn." + args[1] + ".x", loc.getX());
                cfgSpawn.set("Spawn." + args[1] + ".z", loc.getZ());
                cfgSpawn.set("Spawn." + args[1] + ".y", loc.getY());
                cfgSpawn.set("Spawn." + args[1] + ".yaw", loc.getYaw());
                cfgSpawn.set("Spawn." + args[1] + ".pitch", loc.getPitch());
                cfgSpawn.set("Spawn." + args[1] + ".world", loc.getWorld().getName());
                FichierGestion.saveFile(cfgSpawn, FichierGestion.getSettingsFile());
                player.sendMessage(PrefixMessage.admin() + "Le spawn §b" + args[1] + " §ea été créé.");
                return false;
            }

            if (args[0].equalsIgnoreCase("delete")) {
                if (!cfgSpawn.contains("Spawn." + args[1])) {
                    player.sendMessage(PrefixMessage.erreur() + "Le spawn n'existe pas.");
                    return false;
                }

                cfgSpawn.set("Spawn." + args[1], null);
                FichierGestion.saveFile(cfgSpawn, FichierGestion.getSettingsFile());
                player.sendMessage(PrefixMessage.admin() + "Le spawn §b" + args[1] + " §ea été supprimé.");
                return false;
            }
        }

        return false;
    }

    @EventHandler
    public void openDepot(NPCRightClickEvent event) {
        if (!(event.getNPC().getId() == 1)) {
            return;
        }

    }
}