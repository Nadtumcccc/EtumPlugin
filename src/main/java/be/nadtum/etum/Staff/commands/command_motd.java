package be.nadtum.etum.Staff.commands;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class command_motd implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        YamlConfiguration cfg = FichierGestion.getCfgSettings();

        if (args.length >= 2) {
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            if (args[0].equalsIgnoreCase("ligne_1")) {
                cfg.set("Settings.Module.MOTD.ligne_1", message);
            } else if (args[0].equalsIgnoreCase("ligne_2")) {
                cfg.set("Settings.Module.MOTD.ligne_2", message);
            }
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1) {
            return Arrays.asList("ligne_1", "ligne_2");
        }

        return null;
    }
}
