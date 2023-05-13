package be.nadtum.etum.Vanilla.Player.Commands;


import be.nadtum.etum.Utility.Modules.Chat;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;

public class command_grade implements CommandExecutor {

    enum Grade {
        AKIEN(0, 0, 0),
        BOURGEOIS(1, 10000, 1),
        IRON(2, 50000, 1),
        GOLD(3, 100000, 1),
        DIAMOND(4, 500000, 2),
        EMERALD(5, 1000000, 2),
        NETHERITE(6, 5000000, 3);

        private final int priority;
        private final int reward;
        private final int home;

        Grade(int priority, int money, int home) {
            this.priority = priority;
            this.reward = money;
            this.home = home;
        }

        public int getPriority() {
            return priority;
        }

        public int getMoney() {
            return reward;
        }

        public int getHome() {
            return home;
        }

        // Méthode pour obtenir l'objet Grade à partir du nom du grade
        private static Grade getGrade(String gradeName) {
            try {
                return Grade.valueOf(gradeName);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (!(sender instanceof Player)) {
            // Permettre à la console de gérer les commandes

            // grade buy grade_name player
            if (!Objects.equals(args[0], "buy")) return false;

            Player player = Bukkit.getPlayer(args[2]);
            if (player == null) {
                sender.sendMessage("Ce joueur n'existe pas ou n'est pas en ligne.");
                return false;
            }

            String gradeName = args[1].toUpperCase();
            String currentGradeName = PlayerGestion.getPlayerGrade(player.getName()).toUpperCase();

            Grade grade = Grade.getGrade(gradeName);
            Grade currentGrade = Grade.getGrade(currentGradeName);

            // Vérifier si le grade acheté est plus élevé que le grade actuel du joueur
            if (grade.getPriority() > currentGrade.getPriority()) {
                PlayerGestion.setPlayerGrade(player.getName(), args[1]);
                sender.sendMessage("Grade acheté avec succès !");
            }

            // Récompense en fonction du grade
            int rewardAmount = grade.getMoney();
            if (rewardAmount > 0) {
                // Donner de l'argent au joueur
                PlayerGestion.addPlayerMoney(player.getName(), (long) rewardAmount);
                sender.sendMessage("Vous avez reçu " + rewardAmount + " d'argent en récompense !");
            }

            return false;
        }




        Player player = (Player) sender;

        //vérifier s'il à la permission

        if(!PlayerGestion.hasStaffPermission(player, "admin"))return false;

        /* /grade [action]
            action :
            - create [grade]
            - delete [grade]
            - add [grade] [permission : string]
            - set [grade] [joueur : player]
            - delete [grade] (permission : string)
            - design [grade] [design : string]
         */

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

                    if (!(Bukkit.getPlayer(args[2]) instanceof Player)) {
                        String errorMessage = PrefixMessage.erreur() + "le joueur n'est pas en ligne";
                        player.sendMessage(errorMessage);
                        return false;
                    }

                    if (PlayerGestion.getPlayerGrade(args[2]).equalsIgnoreCase(args[1])) {
                        String errorMessage = PrefixMessage.erreur() + "le joueur possède déjà ce grade";
                        player.sendMessage(errorMessage);
                        return false;
                    }

                    PlayerGestion.setPlayerGrade(args[2], args[1]);
                    player.sendMessage(Component.text(PrefixMessage.admin() + "le joueur §b" + Bukkit.getPlayer(args[2]).getName() + " §ea reçu le grade §b" + args[1]));
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

    // Méthode pour obtenir l'objet Grade à partir du nom du grade



}
