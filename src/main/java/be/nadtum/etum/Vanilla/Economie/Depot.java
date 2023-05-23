package be.nadtum.etum.Vanilla.Economie;

import be.nadtum.etum.Utility.Modules.FichierGestion;
import be.nadtum.etum.Utility.Modules.PlayerGestion;
import be.nadtum.etum.Utility.Objets.InventoryBuilder;
import be.nadtum.etum.Utility.Objets.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Depot implements Listener {

    private static final String nameMenu = "Menu : Dépôt";
    private static final HashMap<String, InventoryBuilder> jobMenus = new HashMap<>();

    // Énumération des menus spécifiques à chaque métier
    public enum JobMenu {
        MINEUR("Mineur"),
        BÛCHERON("Bûcheron"),
        CHASSEUR("Chasseur"),
        PÊCHEUR("Pêcheur"),
        FERMIER("Fermier");

        private final String jobName;

        JobMenu(String jobName) {
            this.jobName = jobName;
        }

        public String getJobName() {
            return jobName;
        }

        public InventoryBuilder getGuiDepot() {
            return jobMenus.get(jobName);
        }

        public static void createJobMenus() {
            for (JobMenu jobMenu : values()) {
                jobMenus.put(jobMenu.getJobName(), new InventoryBuilder(nameMenu, 54));
                jobMenu.update();
            }
        }

        public void update() {
            createGuiDepot(this, getGuiDepot());
        }

        private void createGuiDepot(@NotNull JobMenu jobMenu, @NotNull InventoryBuilder inventoryBuilder) {
            // Logique pour créer le menu spécifique à chaque métier
            inventoryBuilder.getInventory().clear();
            inventoryBuilder.setupTemplate();

            YamlConfiguration config = FichierGestion.getCfgJobs();
            ConfigurationSection jobSection = config.getConfigurationSection("Jobs." + jobMenu.getJobName());

            if (jobSection != null) {
                int caseCount = 0;
                int inventorySize = inventoryBuilder.getInventory().getSize();

                for (String target : jobSection.getKeys(false)) {
                    String targetPath = "Jobs." + jobMenu.getJobName() + "." + target + ".depot";

                    if (config.contains(targetPath)) {
                        while (caseCount < inventorySize && inventoryBuilder.getInventory().getItem(caseCount) != null) {
                            caseCount++;
                        }

                        if (caseCount >= inventorySize) {
                            break;
                        }

                        Material material = Material.getMaterial(target);
                        if (material == null) {
                            continue;
                        }

                        int amount = config.getInt(targetPath + ".amount");
                        int mainValue = config.getInt(targetPath + ".value");
                        int priceFinal = getPriceFinal(amount, mainValue);

                        ItemBuilder item = new ItemBuilder(material, target.toLowerCase(), 1);
                        item.addLore(" ");
                        item.addLore("§6Valeur §e: §b" + priceFinal);
                        item.addLore("§6Stock §e: §b" + amount);
                        item.addLore(" ");

                        inventoryBuilder.getInventory().setItem(caseCount, item.getItem());
                        caseCount++;
                    }
                }
            }
        }
    }

    // Initialise les menus spécifiques à chaque métier
    public static void initializeJobMenus() {
        JobMenu.createJobMenus();
    }

    // Ouvre le menu de dépôt pour un joueur spécifique
    public static void openDepotMenu(@NotNull Player player) {
        String jobName = PlayerGestion.getPlayerJobName(player.getName());
        JobMenu jobMenu = JobMenu.valueOf(jobName.toUpperCase());
        jobMenu.update();
        InventoryBuilder inventoryBuilder = jobMenu.getGuiDepot();

        player.openInventory(inventoryBuilder.getInventory());
    }

    // Gère les clics sur le menu de dépôt du joueur
    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        // Vérifie si le joueur a cliqué en dehors de l'inventaire ou dans l'inventaire du joueur
        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE) || event.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) {
            return;
        }

        // Vérifie si le menu ouvert est le menu de dépôt
        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            event.setCancelled(true);

            // Vérifie si le joueur a cliqué sur le bouton de retour
            if (event.getCurrentItem().getType().equals(InventoryBuilder.back.getItem().getType())) {
                player.closeInventory();
                return;
            }

            // Vérifie si le joueur a cliqué sur les bords de l'inventaire
            if (event.getCurrentItem().getType().equals(InventoryBuilder.contour.getItem().getType())) {
                return;
            }

            sellItem(event.getCurrentItem(), FichierGestion.getCfgJobs(), player, event);
        }
    }

    // Vend un objet et met à jour les stocks, l'inventaire du joueur et les données du GUI
    public void sellItem(@NotNull ItemStack stack, @NotNull YamlConfiguration config, @NotNull Player player, @NotNull InventoryClickEvent event) {
        String jobName = PlayerGestion.getPlayerJobName(player.getName());
        String targetPath = "Jobs." + jobName + "." + stack.getType() + ".depot";

        // Obtenir la quantité d'objets en stock dans la configuration
        int stockAmount = config.getInt(targetPath + ".amount");
        int mainValue = config.getInt(targetPath + ".value");

        // Calculer le prix final
        int priceFinal = getPriceFinal(stockAmount, mainValue);

        // Vérifier si le joueur clique gauche (une quantité de 1) ou droit (une quantité de 64)
        int quantity = (event.getClick() == ClickType.RIGHT) ? 64 : 1;

        // Récupérer les objets correspondants dans l'inventaire du joueur
        int availableAmount = getPlayerInventoryItems(player, stack);

        // Vérifier si le joueur a suffisamment d'objets à vendre
        if (quantity > availableAmount) {
            player.sendMessage(ChatColor.DARK_RED + "Vous n'avez pas suffisamment d'objets à vendre.");
            return;
        }

        // Retirer la quantité d'objets du joueur
        removePlayerInventoryItems(player, stack, quantity);

        // Mettre à jour la quantité dans la configuration du métier
        config.set(targetPath + ".amount", stockAmount + quantity);

        // Calculer le prix total en fonction de la quantité vendue
        priceFinal = priceFinal * quantity;

        // Donner de l'argent au joueur
        PlayerGestion.addPlayerMoney(player.getName(), (long) priceFinal);

        // Envoyer un message au joueur avec les informations de la transaction
        player.sendMessage(ChatColor.GREEN + "Vous avez vendu " + quantity + " " + stack.getType().toString().toUpperCase() + " pour " + priceFinal + " pièces.");

        // Recharger le GUI avec les nouvelles données
        JobMenu jobMenu = JobMenu.valueOf(jobName.toUpperCase());
        jobMenu.update();

        // Rafraîchir l'inventaire du joueur pour refléter les nouvelles données
        player.updateInventory();
    }

    // Méthode pour obtenir la quantité d'objets correspondants dans l'inventaire du joueur
    private int getPlayerInventoryItems(@NotNull Player player, ItemStack stack) {
        ItemStack[] inventoryContents = player.getInventory().getContents();
        int count = 0;

        for (ItemStack item : inventoryContents) {
            if (item != null && item.getType() == stack.getType()) {
                count += item.getAmount();
            }
        }

        return count;
    }

    // Méthode pour supprimer la quantité d'objets correspondants de l'inventaire du joueur
    private void removePlayerInventoryItems(@NotNull Player player, ItemStack stack, int quantity) {
        ItemStack[] inventoryContents = player.getInventory().getContents();

        for (int i = 0; i < inventoryContents.length; i++) {
            ItemStack item = inventoryContents[i];
            if (item != null && item.getType() == stack.getType()) {
                int amount = item.getAmount();

                if (amount > quantity) {
                    item.setAmount(amount - quantity);
                    player.getInventory().setItem(i, item);
                    break;
                } else if (amount == quantity) {
                    player.getInventory().setItem(i, null);
                    break;
                } else {
                    player.getInventory().setItem(i, null);
                    quantity -= amount;
                }
            }
        }

        player.updateInventory();
    }

    // Calcule le prix final en fonction de la quantité et de la valeur initiale de l'objet
    private static int getPriceFinal(int amount, final int mainValue) {
        final int threshold = 1000;
        final double reductionPercentage = 0.1;

        if (amount >= threshold) {
            final int reductionCount = amount / threshold;
            final int totalReduction = (int) (reductionCount * mainValue * reductionPercentage);
            return mainValue - totalReduction;
        }

        return mainValue;
    }
}

