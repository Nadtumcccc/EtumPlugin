package be.nadtum.etum.Vanilla.Player.Events;

import be.nadtum.etum.Utility.Modules.Chat;
import be.nadtum.etum.Utility.Modules.HashMapGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.jetbrains.annotations.NotNull;

public class Death implements Listener {

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer != null) {
            handlePlayerKilledByPlayer(player, killer, event);
        } else {
            handlePlayerDeath(player, event);
        }
    }

    private void handlePlayerKilledByPlayer(Player victim, Player killer, @NotNull PlayerDeathEvent event) {
        String victimName = getPlayerDisplayName(victim);
        String killerName = getPlayerDisplayName(killer);

        String deathMessage = victimName + " a été tué par " + killerName;
        event.deathMessage(Component.text(deathMessage));
    }

    private void handlePlayerDeath(Player player, @NotNull PlayerDeathEvent event) {
        String playerName = getPlayerDisplayName(player);
        String deathMessage = playerName + " §avient de mourir";
        event.deathMessage(Component.text(deathMessage));

        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 4, 1);

        if (PlayerGestion.hasPermission(player, "back")) {
            HashMapGestion.back.put(player, player.getLocation());
        }
    }

    private @NotNull String getPlayerDisplayName(@NotNull Player player) {
        String grade = PlayerGestion.getPlayerStaffGrade(player.getName());
        if (grade.equalsIgnoreCase("NoStaff")) {
            grade = PlayerGestion.getPlayerGrade(player.getName());
        }

        String prefix = PlayerGestion.getGradeDesign(grade);
        String name = player.getName();

        return Chat.colorString(prefix + " " + name);
    }
}
