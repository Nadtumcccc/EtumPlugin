package be.nadtum.etum.Listeners;


import be.nadtum.etum.Lobby.commands.CommandSpawn;
import be.nadtum.etum.Main;
import be.nadtum.etum.Vanilla.City.commands.command_city;
import be.nadtum.etum.Vanilla.Commands.*;
import be.nadtum.etum.Vanilla.Commands.command_mana;
import be.nadtum.etum.Vanilla.Economie.commands.command_money;
import be.nadtum.etum.Vanilla.Economie.commands.command_pay;
import be.nadtum.etum.Vanilla.MenuGui.commands.command_menu;
import be.nadtum.etum.Region.command_region;
import be.nadtum.etum.Staff.commands.*;


public class Commands {
    public Commands(Main main) {

        //admin
        main.getCommand("rg").setExecutor(new command_region());
        //joueur

        //staff
        main.getCommand("tempban").setExecutor(new command_tempban());
        main.getCommand("tempmute").setExecutor(new command_tempmute());
        main.getCommand("unban").setExecutor(new command_unban());
        main.getCommand("unmute").setExecutor(new command_unmute());

        main.getCommand("gm").setExecutor(new command_gamemode());
        main.getCommand("grade").setExecutor(new command_grade());
        main.getCommand("menu").setExecutor(new command_menu());
        main.getCommand("home").setExecutor(new command_home());
        main.getCommand("sethome").setExecutor(new command_home());
        main.getCommand("delhome").setExecutor(new command_home());
        main.getCommand("ec").setExecutor(new command_ec());
        main.getCommand("craft").setExecutor(new command_craft());
        main.getCommand("fly").setExecutor(new command_fly());
        main.getCommand("money").setExecutor(new command_money());
        main.getCommand("claim").setExecutor(new CommandClaim());
        main.getCommand("unclaim").setExecutor(new CommandClaim());
        main.getCommand("mana").setExecutor(new command_mana());
        main.getCommand("job").setExecutor(new command_job());
        main.getCommand("city").setExecutor(new command_city());
        main.getCommand("pay").setExecutor(new command_pay());
        main.getCommand("spawn").setExecutor(new CommandSpawn());
        main.getCommand("day").setExecutor(new command_world());
        main.getCommand("night").setExecutor(new command_world());
        main.getCommand("tpa").setExecutor(new command_tpa());
        main.getCommand("tpaccept").setExecutor(new command_tpa());
        main.getCommand("tpreject").setExecutor(new command_tpa());
        main.getCommand("tp").setExecutor(new command_tp());
        main.getCommand("back").setExecutor(new command_back());
        main.getCommand("world").setExecutor(new command_world());
        main.getCommand("motd").setExecutor(new command_motd());
    }
}
