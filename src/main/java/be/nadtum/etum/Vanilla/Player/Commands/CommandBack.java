package be.nadtum.etum.Vanilla.Player.Commands;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandBack implements CommandExecutor {

    // Enumération des grades avec leurs prix
    private enum Grade {
        DEFAULT(10000L),
        GRADE_1(8000L),
        GRADE_2(6000L),
        GRADE_3(4000L),
        GRADE_4(2000L);

        private final Long price;

        Grade(Long price) {
            this.price = price;
        }

        public Long getPrice() {
            return price;
        }
    }

    // HashMap pour stocker la dernière localisation de chaque joueur
    private static final Map<Player, Location> lastLocations = new HashMap<>();
    private final Map<Player, Long> cooldowns = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Vous ne pouvez pas utiliser cette commande depuis la console.");
            return false;
        }

        // Vérifier si le joueur a une localisation enregistrée dans la HashMap
        if (!lastLocations.containsKey(player)) {
            player.sendMessage(PrefixMessage.erreur() + "Aucune localisation enregistrée.");
            return false;
        }

        // Récupérer la dernière localisation du joueur
        Location lastLocation = lastLocations.get(player);

        // Récupérer le temps actuel en millisecondes
        long currentTime = System.currentTimeMillis();

        // Vérifier si le joueur a déjà utilisé la commande dans les 15 dernières minutes
        long cooldownDuration = 15 * 60 * 1000L; // 15 minutes en millisecondes

        if (cooldowns.containsKey(player)) {
            long lastUsageTime = cooldowns.get(player);
            if (currentTime - lastUsageTime < cooldownDuration) {
                long remainingCooldown = (lastUsageTime + cooldownDuration - currentTime) / 1000;
                long minutes = remainingCooldown / 60;
                long seconds = remainingCooldown % 60;

                // Générer le message avec les couleurs
                String cooldownMessage = String.format("%sVous devez attendre encore §4%d minute%s §cet §4%d seconde%s §cavant de pouvoir utiliser /back à nouveau.",
                        PrefixMessage.erreur(), minutes, (minutes > 1 ? "s" : ""), seconds, (seconds > 1 ? "s" : ""));

                player.sendMessage(cooldownMessage);
                return false;
            }
        }

        // Récupérer le grade du joueur
        String playerFirstGrade = getPlayerFirstGrade(player);


        // Vérifier si le joueur a suffisamment d'argent pour utiliser /back
        Long price = getGradePrice(playerFirstGrade);
        if (!hasEnoughMoney(player, price)) {
            player.sendMessage(PrefixMessage.erreur() + "Vous n'avez pas assez d'argent pour utiliser /back. Prix : " + price);
            return false;
        }

        // Effectuer la téléportation du joueur à sa dernière localisation
        player.teleport(lastLocation);

        // Retirer le prix de l'argent du joueur
        subtractMoney(player, price);

        // Mettre à jour le temps de la dernière utilisation de /back
        cooldowns.put(player, currentTime);

        player.sendMessage(PrefixMessage.serveur() + "Vous vous êtes téléporté au lieu de votre mort.");
        return true;
    }

    // Méthode pour obtenir le prix du grade à partir de son nom
    private Long getGradePrice(String gradeName) {
        try {
            Grade grade = Grade.valueOf(gradeName);
            return grade.getPrice();
        } catch (IllegalArgumentException e) {
            return Grade.DEFAULT.getPrice(); // Si le grade n'est pas trouvé, on retourne le prix du grade de base
        }
    }

    // Méthode pour récupérer le premier grade du joueur
    private String getPlayerFirstGrade(Player player) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user != null) {
            return user.getPrimaryGroup();
        }
        return "default"; // Si le joueur n'a pas de grade, on retourne le grade de base
    }

    // Méthode pour vérifier si le joueur a suffisamment d'argent
    // Vous devez implémenter cette méthode en fonction de votre économie du jeu
    private boolean hasEnoughMoney(Player player, Long amount) {
        // Votre code pour vérifier si le joueur a assez d'argent ici
        return PlayerBuilder.getPlayerMoney(player.getName()) >= amount;
    }

    // Méthode pour retirer l'argent du joueur
    // Vous devez implémenter cette méthode en fonction de votre économie du jeu
    private void subtractMoney(Player player, Long amount) {
        // Votre code pour retirer l'argent du joueur ici
        PlayerBuilder.removePlayerMoney(player.getName(), amount);
        FichierGestion.getCfgJobs().set("Server.bank.balance", FichierGestion.getCfgJobs().getInt("Server.bank.balance") + amount);
    }

    // Méthode pour ajouter la dernière localisation d'un joueur à la HashMap
    public static void setLastLocation(Player player, Location location) {
        lastLocations.put(player, location);
    }

    // Méthode pour récupérer la dernière localisation d'un joueur depuis la HashMap
    // Si le joueur n'a pas de localisation enregistrée, cette méthode renverra null
    public Location getLastLocation(Player player) {
        return lastLocations.get(player);
    }
}
