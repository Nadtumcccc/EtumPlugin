package be.nadtum.etum;

import be.nadtum.etum.Listeners.Commands;
import be.nadtum.etum.Listeners.Events;
import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Vanilla.Player.Economy.Depot;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        // Setup permissions and chat
        setupPermissions();
        setupChat();

        // Create necessary files
        FichierGestion.CreateFiles();

        // Register event listeners
        registerListeners();

        // Initialize job menus
        Depot.initializeJobMenus();

        log.info(String.format("[%s] Enabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        // Save all files before disabling the plugin
        FichierGestion.saveAllFiles();
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    private void registerListeners() {
        // Register command and event listeners
        new Commands(this);
        new Events(this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            // Vault not found, disable the plugin
            return false;
        }

        // Try to get the Economy provider from Vault
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            // Economy provider not found, disable the plugin
            return false;
        }

        // Economy provider found, set it
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        // Try to get the Chat provider from Vault
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        // Try to get the Permissions provider from Vault
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }
}
