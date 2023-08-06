package be.nadtum.etum.Vanilla.Player.Economy.Jobs;

import be.nadtum.etum.Main;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.HashMapGestion;
import be.nadtum.etum.Utility.Modules.PlayerBuilder;
import be.nadtum.etum.Utility.Modules.PrefixMessage;
import be.nadtum.etum.Vanilla.City.Claim.Claim;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ActionManager implements Listener {

    private final HashMap<UUID, BossBar> bossBarHashMap = new HashMap<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!Claim.canBuild(player, block.getX(), block.getZ())) {
            return;
        }

        if (isMatureCrop(block)) {
            selector(block.getType().name(), player, "MINE");
            return;
        }

        if (HashMapGestion.blockPlaced.containsKey(block)) {
            HashMapGestion.blockPlaced.remove(block);
            return;
        }

        if (Claim.cityCoinShow.containsKey(PlayerBuilder.getPlayerCityName(player.getName()))) {
            player.sendMessage(Component.text(PrefixMessage.serveur() + "le claim est en train d'être édité"));
            event.setCancelled(true);
            return;
        }

        selector(block.getType().name(), player, "MINE");
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        YamlConfiguration cfg = FichierGestion.getCfgJobs();

        if (!cfg.contains("Jobs." + PlayerBuilder.getPlayerJobName(event.getPlayer().getName()) + ".MINE." + event.getBlock().getType())) {
            return;
        }
        HashMapGestion.blockPlaced.put(event.getBlockPlaced(), event.getBlockPlaced());
    }

    @EventHandler
    public void onEntityKill(@NotNull EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        selector(event.getEntity().getType().name(), Objects.requireNonNull(event.getEntity().getKiller()), "KILL");
    }

    @EventHandler
    public void onFish(@NotNull PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            selector(event.getCaught().getName().toUpperCase(), event.getPlayer(), "FISH");
        }
    }

    private boolean isMatureCrop(Block block) {
        YamlConfiguration cfg = FichierGestion.getCfgJobs();
        return cfg.contains("Jobs.Fermier." + block.getType() + ".age") && block.getData() == 7;
    }

    public void selector(String entity, @NotNull Player player, String action) {
        String playerName = player.getName();
        String playerJob = PlayerBuilder.getPlayerJobName(playerName);
        YamlConfiguration cfg = FichierGestion.getCfgJobs();
        int playerLevel = PlayerBuilder.getPlayerJobNiveau(playerName);

        // Check if the player gains xp from this entity type
        if (cfg.contains("Jobs." + playerJob + "." + action + "." + entity + ".gain_xp")) {
            int gain_xp = cfg.getInt("Jobs." + playerJob + "." + action + "." + entity + ".gain_xp");
            int xp_for_next_level = PlayerBuilder.getPlayerJobNiveau(playerName) * 500;

            // Check if the player should level up
            if (PlayerBuilder.getPlayerJobXp(playerName) + gain_xp >= xp_for_next_level) {
                PlayerBuilder.setPlayerJobXp(playerName, 0);
                PlayerBuilder.addPlayerJobNiveau(playerName, 1);
                player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1, 1);
            }

            // Add the xp gain to the player's total
            PlayerBuilder.addPlayerJobXp(playerName, gain_xp);

            // Update the player's boss bar
            BossBar bossBar = bossBarHashMap.getOrDefault(player.getUniqueId(), Bukkit.createBossBar(
                    PlayerBuilder.getPlayerJobName(player.getName()) + " §e[Niveau " + playerLevel + "] §e: §e[§6" + PlayerBuilder.getPlayerJobXp(player.getName()) + " §b/ §6" + xp_for_next_level + "§e]",
                    BarColor.BLUE,
                    BarStyle.SOLID
            ));
            bossBar.setTitle(PlayerBuilder.getPlayerJobName(player.getName()) + " §e[Niveau " + playerLevel + "] §e: §e[§6" + PlayerBuilder.getPlayerJobXp(player.getName()) + " §b/ §6" + xp_for_next_level + "§e]");
            bossBar.setProgress((double) PlayerBuilder.getPlayerJobXp(player.getName()) / xp_for_next_level);
            bossBar.addPlayer(player);
            bossBarHashMap.put(player.getUniqueId(), bossBar);

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), bossBar::removeAll, 100L);

        }

        if (cfg.contains("Jobs." + playerJob + "." + action + "." + entity + ".money")) {
            int gain_money = cfg.getInt("Jobs." + playerJob + "." + action + "." + entity + ".money");
            PlayerBuilder.addPlayerMoney(player.getName(), (long) gain_money);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(("§7vous avez gagné §f" + gain_money + " §7de money")));
        }

    }
}
