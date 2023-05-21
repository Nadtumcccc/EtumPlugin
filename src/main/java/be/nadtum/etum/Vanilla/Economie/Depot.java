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

    public static void initializeJobMenus() {
        JobMenu.createJobMenus();
    }


    public static void menu(@NotNull Player player) {
        String jobName = PlayerGestion.getPlayerJobName(player.getName());
        JobMenu jobMenu = JobMenu.valueOf(jobName.toUpperCase());
        jobMenu.update();
        InventoryBuilder inventoryBuilder = jobMenu.getGuiDepot();

        player.openInventory(inventoryBuilder.getInventory());
    }


    @EventHandler
    public void PlayerMenu(@NotNull InventoryClickEvent event) {

        if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) return;
        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))return;
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;

        if (event.getView().getTitle().equalsIgnoreCase(nameMenu)) {
            event.setCancelled(true);

            if (event.getCurrentItem().getType().equals(InventoryBuilder.back.getItem().getType())) {
                player.closeInventory();
                return;
            }

            if (event.getCurrentItem().getType().equals(InventoryBuilder.contour.getItem().getType())) {
                return;
            }

            sellItem(event.getCurrentItem(), FichierGestion.getCfgJobs(), player, event);
        }
    }

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


        player.sendMessage(availableAmount + " " +  quantity);
        // Vérifier si le joueur a suffisamment d'objets à vendre
        if (quantity > availableAmount) {
            player.sendMessage(ChatColor.RED + "Vous n'avez pas suffisamment d'objets à vendre.");
            return;
        }

        // Retirer la quantité d'objets du joueur
        removePlayerInventoryItems(player, stack, quantity);

        // Mettre à jour la quantité dans la configuration du métier
        config.set(targetPath + ".amount", stockAmount + quantity);

        priceFinal = priceFinal * quantity;

        // Donner de l'argent au joueur
        PlayerGestion.addPlayerMoney(player.getName(), (long) priceFinal);
        // Ajoutez ici votre logique pour donner de l'argent au joueur, en utilisant par exemple une économie virtuelle ou une autre méthode.

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