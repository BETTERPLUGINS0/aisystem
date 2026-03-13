/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.entity.Player
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.Nullable
 */
package net.advancedplugins.seasons.menus;

import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.items.ConfigItemCreator;
import net.advancedplugins.as.impl.utils.menus.AdvancedMenu;
import net.advancedplugins.as.impl.utils.menus.item.AdvancedMenuItem;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.enums.Season;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.utils.ShopMenuConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SeasonShopMenu
extends AdvancedMenu {
    private final SeasonType type;
    private final ShopMenuConfig config;

    public SeasonShopMenu(Player player, ShopMenuConfig shopMenuConfig, SeasonType seasonType) {
        super(player, (ConfigurationSection)shopMenuConfig.getConfig(), null);
        this.type = seasonType;
        this.config = shopMenuConfig;
    }

    public SeasonShopMenu(Player player, ShopMenuConfig shopMenuConfig) {
        this(player, shopMenuConfig, SeasonType.SPRING);
    }

    @Override
    public void openInventory() {
        ConfigurationSection configurationSection;
        for (String string : this.config.getKeys("defaultItems." + this.type.name())) {
            configurationSection = this.config.getConfig().getConfigurationSection("defaultItems." + this.type.name());
            this.processItem(string, configurationSection);
        }
        for (String string : this.config.getKeys("shopItems." + this.type.name())) {
            Object object;
            configurationSection = ConfigItemCreator.fromConfigSection(this.config.getConfig(), "shopItems." + this.type.name() + "." + string, null, null);
            String string2 = this.config.getConfig().getString("shopItems." + this.type.name() + "." + string + ".price");
            int n = ASManager.parseInt(this.config.getConfig().getString("shopItems." + this.type.name() + "." + string + ".price").split(":")[1]);
            int[] nArray = ASManager.getSlots(string);
            if (string2 != null) {
                object = configurationSection.clone();
                configurationSection = ConfigItemCreator.fromConfigSection(this.config.getConfig(), (ItemStack)configurationSection, "shopItem", ASManager.stringToMap("%price%;" + n, "%item name%;" + object.getItemMeta().getDisplayName()), null);
                this.addAction((arg_0, arg_1, arg_2, arg_3, arg_4) -> SeasonShopMenu.lambda$openInventory$0(string2, (ItemStack)object, n, arg_0, arg_1, arg_2, arg_3, arg_4), nArray);
            }
            object = new AdvancedMenuItem((ItemStack)configurationSection);
            ((AdvancedMenuItem)object).setSlots(nArray);
            this.addItem((AdvancedMenuItem)object, nArray);
        }
        super.openInventory();
    }

    public static void refreshSeasonMenus() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            SeasonType seasonType;
            Inventory inventory = player.getOpenInventory().getTopInventory();
            if (!(inventory.getHolder() instanceof SeasonShopMenu) || (seasonType = (SeasonType)Core.getSeasonHandler().optionalSeason(player.getWorld().getName()).map(Season::getType).orElse(null)) == null) continue;
            SeasonShopMenu.open(player, null);
        }
    }

    @Nullable
    public static SeasonShopMenu open(Player player, @Nullable String string) {
        String string2 = player.getWorld().getName();
        ShopMenuConfig shopMenuConfig = Core.getShopMenuConfig();
        if (!shopMenuConfig.isAllowedOnAllWorlds() && !Core.getWorldHandler().isWorldEnabled(string2)) {
            if (string != null) {
                player.sendMessage(string.replace("%world%", string2));
            }
            return null;
        }
        SeasonType seasonType = Core.getSeasonHandler().getSeason(string2).getType();
        SeasonShopMenu seasonShopMenu = new SeasonShopMenu(player, shopMenuConfig, seasonType);
        seasonShopMenu.openInventory();
        return seasonShopMenu;
    }

    private static /* synthetic */ void lambda$openInventory$0(String string, ItemStack itemStack, int n, Player player, AdvancedMenu advancedMenu, AdvancedMenuItem advancedMenuItem, int n2, ClickType clickType) {
        if (!Core.getEconomyHandler().charge(player, string)) {
            player.sendMessage(Core.getLocaleHandler().getString("messages.notEnoughMoney"));
            return;
        }
        ASManager.giveItem(player, itemStack);
        player.sendMessage(Core.getLocaleHandler().getString("messages.purchasedItem").replace("%item name%", itemStack.getItemMeta().getDisplayName()).replace("%price%", "" + n));
    }
}

