package be.nadtum.etum.Moderation.commands;


import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
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

public class command_world implements CommandExecutor, TabExecutor {



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!(sender instanceof Player player)){
            System.out.println(PrefixMessage.erreur() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }

        YamlConfiguration cfg = FichierGestion.getCfgPermission();

        if(!cfg.contains("Grade." + PlayerGestion.getPlayerStaffGrade(player.getName()) + ".permission.admin") ){
            if(!player.isOp()){
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        if(cmd.getName().equalsIgnoreCase("world")){
            if(args[0].equalsIgnoreCase("create")){
                //vérifier si le nom du monde est bien présent
                if(args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty()){
                    player.sendMessage("/world create nom_du_monde [NORMAL/NETHER/END]");
                }

                if(args[2].equalsIgnoreCase("NORMAL")){
                    WorldCreator wc = new WorldCreator(args[1]);
                    wc.environment(World.Environment.NORMAL);
                    wc.type(WorldType.NORMAL);
                    wc.createWorld();
                }

                if(args[2].equalsIgnoreCase("NETHER")){
                    WorldCreator wc = new WorldCreator(args[1]);
                    wc.environment(World.Environment.NETHER);
                    wc.type(WorldType.NORMAL);
                    wc.createWorld();
                }

                if(args[2].equalsIgnoreCase("END")){
                    WorldCreator wc = new WorldCreator(args[1]);
                    wc.environment(World.Environment.THE_END);
                    wc.type(WorldType.NORMAL);
                    wc.createWorld();
                }

                player.sendMessage(PrefixMessage.admin() + "le monde " + args[1] + " a été créé");
                return false;
            }

            if(args[0].equalsIgnoreCase("delete")){
                if(args[1].isEmpty()){
                    player.sendMessage("/world delete nom_du_monde");
                }

            }
            return false;
        }

        if(cmd.getName().equalsIgnoreCase("day")){
            player.getWorld().setTime(1000);
            player.sendMessage(PrefixMessage.admin() + "vous avez mis le §bjour");
            return false;
        }

        if(cmd.getName().equalsIgnoreCase("night")){
            player.getWorld().setTime(13000);
            player.sendMessage(PrefixMessage.admin() + "vous avez mis la §bnuit");
            return false;
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {


        Player player = (Player) sender;
        List<String> list = new ArrayList<>();

        if(args.length == 1){
            list.add("create");
        }

        if(args.length == 3){
            list.add("NORMAL");
            list.add("NETHER");
            list.add("END");
        }


        return null;
    }
}
