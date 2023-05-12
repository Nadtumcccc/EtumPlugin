package be.nadtum.etum.Vanilla.Player.Commands;


import be.nadtum.etum.Utility.Modules.Chat;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class command_grade implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

         /* /grade [action]
            action :
            - create [grade]
            - delete [grade]
            - add [grade] [permission : string]
            - set [grade] [joueur : player]
            - delete [grade] (permission : string)
            - design [grade] [design : string]
         */
        if (!(sender instanceof Player)) {
            System.out.println(PrefixMessage.erreur() + "vous ne pouvez pas utiliser cette commande");
            return false;
        }
        Player player = (Player) sender;

        //vérifier s'il à la permission

        if (!FichierGestion.getCfgPermission().contains("Grade." + PlayerGestion.getPlayerGrade(player.getName()) + ".permission.admin")) {
            if (!player.isOp()) {
                player.sendMessage(PrefixMessage.erreur() + "vous n'avez pas la permission d'utiliser cette commande");
                return false;
            }
        }

        if(args.length == 3) {
            if(!GradeExiste(args[1], FichierGestion.getCfgPermission())){
                player.sendMessage(PrefixMessage.erreur() + "le grade §4" + args[1] + " §cn'éxiste pas !");
                return false;
            }
            switch (args[0]){
                case "add":

                    if(PermissionExiste(args[1],args[2],FichierGestion.getCfgPermission())){
                        player.sendMessage(PrefixMessage.erreur() + "le grade §4" + args[1] + " §ca déjà cette permission");
                        return false;
                    }

                    FichierGestion.getCfgPermission().set("Grade." + args[1] + ".permission." + args[2], true);
                    player.sendMessage(PrefixMessage.admin() + "la permission §b" + args[2] + " §ea bien été ajouter au grade §b" + args[1]);

                    break;

                case "delete":
                    if(!PermissionExiste(args[1],args[2],FichierGestion.getCfgPermission())){
                        player.sendMessage(PrefixMessage.erreur() + "le grade §4" + args[1] + " §cn'a pas cette permission");
                        return false;
                    }

                    FichierGestion.getCfgPermission().set("Grade." + args[1] + ".permission." + args[2], null);
                    player.sendMessage(PrefixMessage.admin() + "la permission §b" + args[2] + " §ea bien été retiré au grade §b" + args[1]);
                    break;

                case "set":

                    if(!(Bukkit.getPlayer(args[2]) instanceof Player)){
                        player.sendMessage(PrefixMessage.erreur() + "le joueur n'est pas en ligne");
                        return false;
                    }

                    if( PlayerGestion.getPlayerGrade(args[2]).equalsIgnoreCase(args[1])){
                        player.sendMessage(PrefixMessage.erreur() + "le joueur possède déjà ce grade");
                        return false;
                    }

                     PlayerGestion.setPlayerGrade(args[2], args[1]);
                    player.sendMessage(
                            PrefixMessage.admin() + "le joueur §b" + Bukkit.getPlayer(args[2]).getName() + " §ea reçu le grade §b" + args[1]);
                    break;
                case "setstaff":
                    if(!(Bukkit.getPlayer(args[2]) instanceof Player)){
                        player.sendMessage(PrefixMessage.erreur()+ "le joueur n'est pas en ligne");
                        return false;
                    }

                    if( PlayerGestion.getPlayerStaffGrade(args[2]).equalsIgnoreCase(args[1])){
                        player.sendMessage(PrefixMessage.erreur() + "le joueur possède déjà ce grade");
                        return false;
                    }

                    PlayerGestion.setPlayerStaffGrade(args[2], args[1]);
                    player.sendMessage(
                            PrefixMessage.admin() + "le joueur §b" + Bukkit.getPlayer(args[2]).getName() + " §ea reçu le grade §b" + args[1]);
                    break;
                case "design":
                    if(!GradeExiste(args[1], FichierGestion.getCfgPermission())){
                        player.sendMessage(PrefixMessage.erreur() + "le grade §4" + args[1] + " §cn'éxiste pas !");
                        return false;
                    }

                    FichierGestion.getCfgPermission().set("Grade." + args[1] + ".design", args[2]);
                    player.sendMessage(PrefixMessage.admin() + "le design " + Chat.colorString(args[2]) + " §ea été ajouté pour le grade §b" + args[1]);
                    break;
                default:
                    player.sendMessage(PrefixMessage.erreur() + "/grade [add/delete/set/design]");
                    return false;
            }
        }

        if(args.length == 2) {
            switch (args[0]){
                case "create":
                    if(GradeExiste(args[1], FichierGestion.getCfgPermission())){
                        player.sendMessage(PrefixMessage.erreur() + "le grade §4" + args[1] + " §céxiste déjà !");
                        return false;
                    }

                    FichierGestion.getCfgPermission().set("Grade." + args[1] + ".design", args[1]);
                    FichierGestion.getCfgPermission().set("Grade." + args[1] + ".permission.default", true);
                    player.sendMessage(PrefixMessage.admin() + "le grade §b" + args[1] + " §ea bien été ajouter");
                    break;

                case "delete":
                    if(!GradeExiste(args[1], FichierGestion.getCfgPermission())){
                        player.sendMessage(PrefixMessage.erreur() + "le grade §4" + args[1] + " §cn'éxiste pas !");
                        return false;
                    }

                    FichierGestion.getCfgPermission().set("Grade." + args[1], null);
                    player.sendMessage(PrefixMessage.admin() + "le grade §b" + args[1] + " §ea bien été retiré");
                    break;
                default:
                    player.sendMessage(PrefixMessage.erreur() + "/grade [create/delete]");
                    return false;
            }
        }

        return false;
    }

    private Boolean GradeExiste(String grade, YamlConfiguration cfg){
        return cfg.contains("Grade." + grade);
    }

    private Boolean PermissionExiste(String grade ,String permission, YamlConfiguration cfg){
        return cfg.contains("Grade." + grade + ".permission." + permission);
    }
}
