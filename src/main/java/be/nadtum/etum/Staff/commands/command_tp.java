package be.nadtum.etum.Staff.commands;


import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class command_tp implements CommandExecutor, TabExecutor {



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            System.out.println(PrefixMessage.erreur() + " vous ne pouvez pas utiliser cette commande");
            return false;
        }

        Player player = (Player) sender;

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if (!cfg.contains("Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission.tp")) {
            if (!player.isOp()) {
                player.sendMessage(PrefixMessage.erreur() + " vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        if(args.length == 1){

            if(Bukkit.getWorld(args[0]) == null && Bukkit.getPlayer(args[0]) == null){
                player.sendMessage(PrefixMessage.erreur() + "/tp [world or player]");
                return false;
            }

            if(Bukkit.getPlayer(args[0]) == null && !(Bukkit.getWorld(args[0]) == null)){
                player.teleport(Objects.requireNonNull(Bukkit.getWorld(args[0])).getSpawnLocation());
                player.sendMessage(PrefixMessage.admin() + "vous vous êtes téléporté dans le monde §b" + Objects.requireNonNull(Bukkit.getWorld(args[0])).getName());
                return false;
            }


            Player target = Bukkit.getPlayer(args[0]);

            try {
                player.teleport(target != null ? target.getLocation() : null);
                player.sendMessage(PrefixMessage.admin() + "vous vous êtes téléporté au joueur " + args[0]);
            }catch (Exception e){
                player.sendMessage(PrefixMessage.erreur() + "le joueur ciblé n'est pas présent");
            }
            return false;
        }

        if(args.length == 3){

            player.teleport(new Location(player.getWorld(),Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2])));
            player.sendMessage(PrefixMessage.admin() + "vous vous êtes téléporté");
            return false;
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(args.length == 1){
            List<String> list = new ArrayList<>();

            for (World world : Bukkit.getServer().getWorlds()){
                list.add(world.getName());
            }

            for (Player player : Bukkit.getOnlinePlayers()){
                list.add(player.getName());
            }
            return list;
        }

        return null;
    }
}
