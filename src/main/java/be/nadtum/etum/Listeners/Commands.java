package be.nadtum.etum.Listeners;

import be.nadtum.etum.Main;
import be.nadtum.etum.Moderation.commands.*;
import be.nadtum.etum.Vanilla.City.commands.CommandCity;
import be.nadtum.etum.Vanilla.MenuGui.commands.command_menu;
import be.nadtum.etum.Vanilla.Player.Commands.*;
import be.nadtum.etum.Vanilla.Region.CommandRegion;

public class Commands {
    public Commands(Main main) {
        // Admin Commands
        registerAdminCommands(main);

        // Staff Commands
        registerStaffCommands(main);

        // Menu Commands
        registerMenuCommands(main);

        // Home Commands
        registerHomeCommands(main);

        // Economy Commands
        registerEconomyCommands(main);

        // Claim Commands
        registerClaimCommands(main);

        // Mana Command
        registerManaCommand(main);

        // Job Command
        registerJobCommand(main);

        // City Command
        registerCityCommand(main);

        // Spawn Command
        registerSpawnCommand(main);

        // World Commands
        registerWorldCommands(main);

        // Miscellaneous Commands
        registerMiscCommands(main);
    }

    private void registerAdminCommands(Main main) {
        main.getCommand("rg").setExecutor(new CommandRegion());
    }

    private void registerStaffCommands(Main main) {
        main.getCommand("tempban").setExecutor(new command_tempban());
        main.getCommand("tempmute").setExecutor(new command_tempmute());
        main.getCommand("unban").setExecutor(new command_unban());
        main.getCommand("unmute").setExecutor(new command_unmute());
        main.getCommand("gm").setExecutor(new CommandGamemode());
    }

    private void registerMenuCommands(Main main) {
        main.getCommand("menu").setExecutor(new command_menu());
    }

    private void registerHomeCommands(Main main) {
        main.getCommand("home").setExecutor(new CommandHome());
        main.getCommand("sethome").setExecutor(new CommandHome());
        main.getCommand("delhome").setExecutor(new CommandHome());
    }

    private void registerEconomyCommands(Main main) {
        main.getCommand("ec").setExecutor(new command_ec());
        main.getCommand("money").setExecutor(new CommandMoney());
        main.getCommand("pay").setExecutor(new CommandPay());
    }

    private void registerClaimCommands(Main main) {
        main.getCommand("claim").setExecutor(new CommandClaim());
        main.getCommand("unclaim").setExecutor(new CommandClaim());
    }

    private void registerManaCommand(Main main) {
        main.getCommand("mana").setExecutor(new CommandMana());
    }

    private void registerJobCommand(Main main) {
        main.getCommand("job").setExecutor(new command_job());
    }

    private void registerCityCommand(Main main) {
        main.getCommand("city").setExecutor(new CommandCity());
    }

    private void registerSpawnCommand(Main main) {
        main.getCommand("spawn").setExecutor(new CommandWarp());
    }

    private void registerWorldCommands(Main main) {
        main.getCommand("day").setExecutor(new command_world());
        main.getCommand("night").setExecutor(new command_world());
        main.getCommand("tp").setExecutor(new command_tp());
        main.getCommand("back").setExecutor(new CommandBack());
        main.getCommand("world").setExecutor(new command_world());
    }

    private void registerMiscCommands(Main main) {
        main.getCommand("craft").setExecutor(new command_craft());
        main.getCommand("fly").setExecutor(new command_fly());
        main.getCommand("tpa").setExecutor(new command_tpa());
        main.getCommand("tpaccept").setExecutor(new command_tpa());
        main.getCommand("tpreject").setExecutor(new command_tpa());
        main.getCommand("motd").setExecutor(new command_motd());
    }
}
