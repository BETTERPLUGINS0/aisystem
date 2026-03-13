/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package com.andrei1058.bedwars.shop;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import com.andrei1058.bedwars.shop.listeners.InventoryListener;
import com.andrei1058.bedwars.shop.listeners.PlayerDropListener;
import com.andrei1058.bedwars.shop.listeners.QuickBuyListener;
import com.andrei1058.bedwars.shop.listeners.ShopCacheListener;
import com.andrei1058.bedwars.shop.listeners.ShopOpenListener;
import com.andrei1058.bedwars.shop.listeners.SpecialsListener;
import com.andrei1058.bedwars.shop.main.QuickBuyButton;
import com.andrei1058.bedwars.shop.main.ShopCategory;
import com.andrei1058.bedwars.shop.main.ShopIndex;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ShopManager
extends ConfigManager {
    public static ShopIndex shop;

    public ShopManager() {
        super((Plugin)BedWars.plugin, "shop", BedWars.plugin.getDataFolder().getPath());
        this.saveDefaults();
        this.loadShop();
        this.registerListeners();
    }

    private void saveDefaults() {
        String material;
        this.getYml().options().header("Shop with quick buy and tiers");
        this.getYml().addDefault("shop-settings.quick-buy-category.material", (Object)BedWars.getForCurrentVersion("NETHER_STAR", "NETHER_STAR", "NETHER_STAR"));
        this.getYml().addDefault("shop-settings.quick-buy-category.amount", (Object)1);
        this.getYml().addDefault("shop-settings.quick-buy-category.data", (Object)0);
        this.getYml().addDefault("shop-settings.quick-buy-category.enchanted", (Object)false);
        this.getYml().addDefault("shop-settings.quick-buy-empty-item.material", (Object)BedWars.getForCurrentVersion("STAINED_GLASS_PANE", "STAINED_GLASS_PANE", "RED_STAINED_GLASS_PANE"));
        this.getYml().addDefault("shop-settings.quick-buy-empty-item.amount", (Object)1);
        this.getYml().addDefault("shop-settings.quick-buy-empty-item.data", (Object)4);
        this.getYml().addDefault("shop-settings.quick-buy-empty-item.enchanted", (Object)false);
        this.getYml().addDefault("shop-settings.regular-separator-item.material", (Object)BedWars.getForCurrentVersion("STAINED_GLASS_PANE", "STAINED_GLASS_PANE", "GRAY_STAINED_GLASS_PANE"));
        this.getYml().addDefault("shop-settings.regular-separator-item.amount", (Object)1);
        this.getYml().addDefault("shop-settings.regular-separator-item.data", (Object)7);
        this.getYml().addDefault("shop-settings.regular-separator-item.enchanted", (Object)false);
        this.getYml().addDefault("shop-settings.selected-separator-item.material", (Object)BedWars.getForCurrentVersion("STAINED_GLASS_PANE", "STAINED_GLASS_PANE", "GREEN_STAINED_GLASS_PANE"));
        this.getYml().addDefault("shop-settings.selected-separator-item.amount", (Object)1);
        this.getYml().addDefault("shop-settings.selected-separator-item.data", (Object)13);
        this.getYml().addDefault("shop-settings.selected-separator-item.enchanted", (Object)false);
        this.getYml().addDefault("shop-specials.silverfish.enable", (Object)true);
        this.getYml().addDefault("shop-specials.silverfish.material", (Object)BedWars.getForCurrentVersion("SNOW_BALL", "SNOW_BALL", "SNOWBALL"));
        this.getYml().addDefault("shop-specials.silverfish.data", (Object)0);
        this.getYml().addDefault("shop-specials.silverfish.health", (Object)8.0);
        this.getYml().addDefault("shop-specials.silverfish.damage", (Object)4.0);
        this.getYml().addDefault("shop-specials.silverfish.speed", (Object)0.25);
        this.getYml().addDefault("shop-specials.silverfish.despawn", (Object)15);
        this.getYml().addDefault("shop-specials.iron-golem.enable", (Object)true);
        this.getYml().addDefault("shop-specials.iron-golem.material", (Object)BedWars.getForCurrentVersion("MONSTER_EGG", "MONSTER_EGG", "HORSE_SPAWN_EGG"));
        this.getYml().addDefault("shop-specials.iron-golem.data", (Object)0);
        this.getYml().addDefault("shop-specials.iron-golem.health", (Object)100.0);
        this.getYml().addDefault("shop-specials.iron-golem.despawn", (Object)240);
        this.getYml().addDefault("shop-specials.iron-golem.speed", (Object)0.25);
        this.getYml().addDefault("shop-specials.tower.enable", (Object)true);
        this.getYml().addDefault("shop-specials.tower.material", (Object)BedWars.getForCurrentVersion("CHEST", "CHEST", "CHEST"));
        if (this.isFirstTime()) {
            this.getYml().addDefault("quick-buy-defaults.element1.path", (Object)"blocks-category.category-content.wool");
            this.getYml().addDefault("quick-buy-defaults.element1.slot", (Object)19);
            this.getYml().addDefault("quick-buy-defaults.element2.path", (Object)"melee-category.category-content.stone-sword");
            this.getYml().addDefault("quick-buy-defaults.element2.slot", (Object)20);
            this.getYml().addDefault("quick-buy-defaults.element3.path", (Object)"armor-category.category-content.chainmail");
            this.getYml().addDefault("quick-buy-defaults.element3.slot", (Object)21);
            this.getYml().addDefault("quick-buy-defaults.element4.path", (Object)"ranged-category.category-content.bow1");
            this.getYml().addDefault("quick-buy-defaults.element4.slot", (Object)23);
            this.getYml().addDefault("quick-buy-defaults.element5.path", (Object)"potions-category.category-content.speed-potion");
            this.getYml().addDefault("quick-buy-defaults.element5.slot", (Object)24);
            this.getYml().addDefault("quick-buy-defaults.element6.path", (Object)"utility-category.category-content.tnt");
            this.getYml().addDefault("quick-buy-defaults.element6.slot", (Object)25);
            this.getYml().addDefault("quick-buy-defaults.element7.path", (Object)"blocks-category.category-content.wood");
            this.getYml().addDefault("quick-buy-defaults.element7.slot", (Object)28);
            this.getYml().addDefault("quick-buy-defaults.element8.path", (Object)"melee-category.category-content.iron-sword");
            this.getYml().addDefault("quick-buy-defaults.element8.slot", (Object)29);
            this.getYml().addDefault("quick-buy-defaults.element9.path", (Object)"armor-category.category-content.iron-armor");
            this.getYml().addDefault("quick-buy-defaults.element9.slot", (Object)30);
            this.getYml().addDefault("quick-buy-defaults.element10.path", (Object)"tools-category.category-content.shears");
            this.getYml().addDefault("quick-buy-defaults.element10.slot", (Object)31);
            this.getYml().addDefault("quick-buy-defaults.element11.path", (Object)"ranged-category.category-content.arrow");
            this.getYml().addDefault("quick-buy-defaults.element11.slot", (Object)32);
            this.getYml().addDefault("quick-buy-defaults.element12.path", (Object)"potions-category.category-content.jump-potion");
            this.getYml().addDefault("quick-buy-defaults.element12.slot", (Object)33);
            this.getYml().addDefault("quick-buy-defaults.element13.path", (Object)"utility-category.category-content.water-bucket");
            this.getYml().addDefault("quick-buy-defaults.element13.slot", (Object)34);
        }
        if (this.isFirstTime()) {
            this.addDefaultShopCategory("blocks-category", 1, BedWars.getForCurrentVersion("STAINED_CLAY", "STAINED_CLAY", "ORANGE_TERRACOTTA"), 1, 1, false);
            this.adCategoryContentTier("blocks-category", "wool", 19, "tier1", BedWars.getForCurrentVersion("WOOL", "WOOL", "WHITE_WOOL"), 0, 16, false, 4, "iron", false, false);
            this.addBuyItem("blocks-category", "wool", "tier1", "wool", BedWars.getForCurrentVersion("WOOL", "WOOL", "WHITE_WOOL"), 0, 16, "", "", "", false);
            this.adCategoryContentTier("blocks-category", "clay", 20, "tier1", BedWars.getForCurrentVersion("STAINED_CLAY", "STAINED_CLAY", "ORANGE_TERRACOTTA"), 1, 16, false, 12, "iron", false, false);
            this.addBuyItem("blocks-category", "clay", "tier1", "clay", BedWars.getForCurrentVersion("STAINED_CLAY", "STAINED_CLAY", "ORANGE_TERRACOTTA"), 1, 16, "", "", "", false);
            this.adCategoryContentTier("blocks-category", "glass", 21, "tier1", BedWars.getForCurrentVersion("GLASS", "GLASS", "GLASS"), 0, 4, false, 12, "iron", false, false);
            this.addBuyItem("blocks-category", "glass", "tier1", "glass", BedWars.getForCurrentVersion("GLASS", "GLASS", "GLASS"), 0, 4, "", "", "", false);
            this.adCategoryContentTier("blocks-category", "stone", 22, "tier1", BedWars.getForCurrentVersion("ENDER_STONE", "ENDER_STONE", "END_STONE"), 0, 16, false, 24, "iron", false, false);
            this.addBuyItem("blocks-category", "stone", "tier1", "stone", BedWars.getForCurrentVersion("ENDER_STONE", "ENDER_STONE", "END_STONE"), 0, 16, "", "", "", false);
            this.adCategoryContentTier("blocks-category", "ladder", 23, "tier1", BedWars.getForCurrentVersion("LADDER", "LADDER", "LADDER"), 0, 16, false, 4, "iron", false, false);
            this.addBuyItem("blocks-category", "ladder", "tier1", "ladder", BedWars.getForCurrentVersion("LADDER", "LADDER", "LADDER"), 0, 16, "", "", "", false);
            this.adCategoryContentTier("blocks-category", "wood", 24, "tier1", BedWars.getForCurrentVersion("WOOD", "WOOD", "OAK_WOOD"), 0, 16, false, 4, "gold", false, false);
            this.addBuyItem("blocks-category", "wood", "tier1", "wood", BedWars.getForCurrentVersion("WOOD", "WOOD", "OAK_WOOD"), 0, 16, "", "", "", false);
            this.adCategoryContentTier("blocks-category", "obsidian", 25, "tier1", BedWars.getForCurrentVersion("OBSIDIAN", "OBSIDIAN", "OBSIDIAN"), 0, 4, false, 4, "emerald", false, false);
            this.addBuyItem("blocks-category", "obsidian", "tier1", "obsidian", BedWars.getForCurrentVersion("OBSIDIAN", "OBSIDIAN", "OBSIDIAN"), 0, 4, "", "", "", false);
            this.addDefaultShopCategory("melee-category", 2, BedWars.getForCurrentVersion("GOLD_SWORD", "GOLD_SWORD", "GOLDEN_SWORD"), 0, 1, false);
            this.adCategoryContentTier("melee-category", "stone-sword", 19, "tier1", BedWars.getForCurrentVersion("STONE_SWORD", "STONE_SWORD", "STONE_SWORD"), 0, 1, false, 10, "iron", false, false, true);
            this.addBuyItem("melee-category", "stone-sword", "tier1", "sword", BedWars.getForCurrentVersion("STONE_SWORD", "STONE_SWORD", "STONE_SWORD"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("melee-category", "iron-sword", 20, "tier1", BedWars.getForCurrentVersion("IRON_SWORD", "IRON_SWORD", "IRON_SWORD"), 0, 1, false, 7, "gold", false, false, true);
            this.addBuyItem("melee-category", "iron-sword", "tier1", "sword", BedWars.getForCurrentVersion("IRON_SWORD", "IRON_SWORD", "IRON_SWORD"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("melee-category", "diamond-sword", 21, "tier1", BedWars.getForCurrentVersion("DIAMOND_SWORD", "DIAMOND_SWORD", "DIAMOND_SWORD"), 0, 1, false, 4, "emerald", false, false, true);
            this.addBuyItem("melee-category", "diamond-sword", "tier1", "sword", BedWars.getForCurrentVersion("DIAMOND_SWORD", "DIAMOND_SWORD", "DIAMOND_SWORD"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("melee-category", "stick", 22, "tier1", BedWars.getForCurrentVersion("STICK", "STICK", "STICK"), 0, 1, true, 10, "gold", false, false, true);
            this.addBuyItem("melee-category", "stick", "tier1", "stick", BedWars.getForCurrentVersion("STICK", "STICK", "STICK"), 0, 1, "KNOCKBACK 1", "", "", false);
            this.addDefaultShopCategory("armor-category", 3, BedWars.getForCurrentVersion("CHAINMAIL_BOOTS", "CHAINMAIL_BOOTS", "CHAINMAIL_BOOTS"), 0, 1, false);
            this.adCategoryContentTier("armor-category", "chainmail", 19, "tier1", BedWars.getForCurrentVersion("CHAINMAIL_BOOTS", "CHAINMAIL_BOOTS", "CHAINMAIL_BOOTS"), 0, 1, false, 40, "iron", true, false);
            this.addBuyItem("armor-category", "chainmail", "tier1", "boots", BedWars.getForCurrentVersion("CHAINMAIL_BOOTS", "CHAINMAIL_BOOTS", "CHAINMAIL_BOOTS"), 0, 1, "", "", "", true);
            this.addBuyItem("armor-category", "chainmail", "tier1", "leggings", BedWars.getForCurrentVersion("CHAINMAIL_LEGGINGS", "CHAINMAIL_LEGGINGS", "CHAINMAIL_LEGGINGS"), 0, 1, "", "", "", true);
            this.adCategoryContentTier("armor-category", "iron-armor", 20, "tier1", BedWars.getForCurrentVersion("IRON_BOOTS", "IRON_BOOTS", "IRON_BOOTS"), 0, 1, false, 12, "gold", true, false);
            this.addBuyItem("armor-category", "iron-armor", "tier1", "boots", BedWars.getForCurrentVersion("IRON_BOOTS", "IRON_BOOTS", "IRON_BOOTS"), 0, 1, "", "", "", true);
            this.addBuyItem("armor-category", "iron-armor", "tier1", "leggings", BedWars.getForCurrentVersion("IRON_LEGGINGS", "IRON_LEGGINGS", "IRON_LEGGINGS"), 0, 1, "", "", "", true);
            this.adCategoryContentTier("armor-category", "diamond-armor", 21, "tier1", BedWars.getForCurrentVersion("DIAMOND_BOOTS", "DIAMOND_BOOTS", "DIAMOND_BOOTS"), 0, 1, false, 6, "emerald", true, false);
            this.addBuyItem("armor-category", "diamond-armor", "tier1", "boots", BedWars.getForCurrentVersion("DIAMOND_BOOTS", "DIAMOND_BOOTS", "DIAMOND_BOOTS"), 0, 1, "", "", "", true);
            this.addBuyItem("armor-category", "diamond-armor", "tier1", "leggings", BedWars.getForCurrentVersion("DIAMOND_LEGGINGS", "DIAMOND_LEGGINGS", "DIAMOND_LEGGINGS"), 0, 1, "", "", "", true);
            this.addDefaultShopCategory("tools-category", 4, BedWars.getForCurrentVersion("STONE_PICKAXE", "STONE_PICKAXE", "STONE_PICKAXE"), 0, 1, false);
            this.adCategoryContentTier("tools-category", "shears", 19, "tier1", BedWars.getForCurrentVersion("SHEARS", "SHEARS", "SHEARS"), 0, 1, false, 20, "iron", true, false);
            this.addBuyItem("tools-category", "shears", "tier1", "shears", BedWars.getForCurrentVersion("SHEARS", "SHEARS", "SHEARS"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("tools-category", "pickaxe", 20, "tier1", BedWars.getForCurrentVersion("WOOD_PICKAXE", "WOOD_PICKAXE", "WOODEN_PICKAXE"), 0, 1, false, 10, "iron", true, true);
            this.addBuyItem("tools-category", "pickaxe", "tier1", "wooden-pickaxe", BedWars.getForCurrentVersion("WOOD_PICKAXE", "WOOD_PICKAXE", "WOODEN_PICKAXE"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("tools-category", "pickaxe", 20, "tier2", BedWars.getForCurrentVersion("IRON_PICKAXE", "IRON_PICKAXE", "IRON_PICKAXE"), 0, 1, true, 10, "iron", true, true);
            this.addBuyItem("tools-category", "pickaxe", "tier2", "iron-pickaxe", BedWars.getForCurrentVersion("IRON_PICKAXE", "IRON_PICKAXE", "IRON_PICKAXE"), 0, 1, "DIG_SPEED 2", "", "", false);
            this.adCategoryContentTier("tools-category", "pickaxe", 20, "tier3", BedWars.getForCurrentVersion("GOLD_PICKAXE", "GOLD_PICKAXE", "GOLDEN_PICKAXE"), 0, 1, true, 3, "gold", true, true);
            this.addBuyItem("tools-category", "pickaxe", "tier3", "gold-pickaxe", BedWars.getForCurrentVersion("GOLD_PICKAXE", "GOLD_PICKAXE", "GOLDEN_PICKAXE"), 0, 1, "DIG_SPEED 3,DAMAGE_ALL 2", "", "", false);
            this.adCategoryContentTier("tools-category", "pickaxe", 20, "tier4", BedWars.getForCurrentVersion("DIAMOND_PICKAXE", "DIAMOND_PICKAXE", "DIAMOND_PICKAXE"), 0, 1, true, 6, "gold", true, true);
            this.addBuyItem("tools-category", "pickaxe", "tier4", "diamond-pickaxe", BedWars.getForCurrentVersion("DIAMOND_PICKAXE", "DIAMOND_PICKAXE", "DIAMOND_PICKAXE"), 0, 1, "DIG_SPEED 3", "", "", false);
            this.adCategoryContentTier("tools-category", "axe", 21, "tier1", BedWars.getForCurrentVersion("WOOD_AXE", "WOOD_AXE", "WOODEN_AXE"), 0, 1, false, 10, "iron", true, true);
            this.addBuyItem("tools-category", "axe", "tier1", "wooden-axe", BedWars.getForCurrentVersion("WOOD_AXE", "WOOD_AXE", "WOODEN_AXE"), 0, 1, "DIG_SPEED 1", "", "", false);
            this.adCategoryContentTier("tools-category", "axe", 21, "tier2", BedWars.getForCurrentVersion("IRON_AXE", "IRON_AXE", "IRON_AXE"), 0, 1, true, 10, "iron", true, true);
            this.addBuyItem("tools-category", "axe", "tier2", "iron-axe", BedWars.getForCurrentVersion("IRON_AXE", "IRON_AXE", "IRON_AXE"), 0, 1, "DIG_SPEED 1", "", "", false);
            this.adCategoryContentTier("tools-category", "axe", 21, "tier3", BedWars.getForCurrentVersion("GOLD_AXE", "GOLD_AXE", "GOLDEN_AXE"), 0, 1, true, 3, "gold", true, true);
            this.addBuyItem("tools-category", "axe", "tier3", "gold-axe", BedWars.getForCurrentVersion("GOLD_AXE", "GOLD_AXE", "GOLDEN_AXE"), 0, 1, "DIG_SPEED 2", "", "", false);
            this.adCategoryContentTier("tools-category", "axe", 21, "tier4", BedWars.getForCurrentVersion("DIAMOND_AXE", "DIAMOND_AXE", "DIAMOND_AXE"), 0, 1, true, 6, "gold", true, true);
            this.addBuyItem("tools-category", "axe", "tier4", "diamond-axe", BedWars.getForCurrentVersion("DIAMOND_AXE", "DIAMOND_AXE", "DIAMOND_AXE"), 0, 1, "DIG_SPEED 3", "", "", false);
            this.addDefaultShopCategory("ranged-category", 5, BedWars.getForCurrentVersion("BOW", "BOW", "BOW"), 0, 1, false);
            this.adCategoryContentTier("ranged-category", "arrow", 19, "tier1", BedWars.getForCurrentVersion("ARROW", "ARROW", "ARROW"), 0, 8, false, 2, "gold", false, false);
            this.addBuyItem("ranged-category", "arrow", "tier1", "arrows", BedWars.getForCurrentVersion("ARROW", "ARROW", "ARROW"), 0, 8, "", "", "", false);
            this.adCategoryContentTier("ranged-category", "bow1", 20, "tier1", BedWars.getForCurrentVersion("BOW", "BOW", "BOW"), 0, 1, false, 12, "gold", false, false);
            this.addBuyItem("ranged-category", "bow1", "tier1", "bow", BedWars.getForCurrentVersion("BOW", "BOW", "BOW"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("ranged-category", "bow2", 21, "tier1", BedWars.getForCurrentVersion("BOW", "BOW", "BOW"), 0, 1, true, 24, "gold", false, false);
            this.addBuyItem("ranged-category", "bow2", "tier1", "bow", BedWars.getForCurrentVersion("BOW", "BOW", "BOW"), 0, 1, "ARROW_DAMAGE 1", "", "", false);
            this.adCategoryContentTier("ranged-category", "bow3", 22, "tier1", BedWars.getForCurrentVersion("BOW", "BOW", "BOW"), 0, 1, true, 6, "emerald", false, false);
            this.addBuyItem("ranged-category", "bow3", "tier1", "bow", BedWars.getForCurrentVersion("BOW", "BOW", "BOW"), 0, 1, "ARROW_DAMAGE 1,ARROW_KNOCKBACK 1", "", "", false);
            this.addDefaultShopCategory("potions-category", 6, BedWars.getForCurrentVersion("BREWING_STAND_ITEM", "BREWING_STAND_ITEM", "BREWING_STAND"), 0, 1, false);
            this.adCategoryContentTier("potions-category", "jump-potion", 20, "tier1", BedWars.getForCurrentVersion("POTION", "POTION", "POTION"), 0, 1, false, 1, "emerald", false, false);
            this.addBuyPotion("potions-category", "jump-potion", "tier1", "jump", BedWars.getForCurrentVersion("POTION", "POTION", "POTION"), 0, 1, "", "JUMP 45 5", "Jump Potion");
            this.adCategoryContentTier("potions-category", "speed-potion", 19, "tier1", BedWars.getForCurrentVersion("POTION", "POTION", "POTION"), 0, 1, false, 1, "emerald", false, false);
            this.addBuyPotion("potions-category", "speed-potion", "tier1", "speed", BedWars.getForCurrentVersion("POTION", "POTION", "POTION"), 0, 1, "", "SPEED 45 2", "Speed Potion");
            this.adCategoryContentTier("potions-category", "invisibility", 21, "tier1", BedWars.getForCurrentVersion("POTION", "POTION", "POTION"), 0, 1, false, 2, "emerald", false, false);
            this.addBuyPotion("potions-category", "invisibility", "tier1", "invisibility", BedWars.getForCurrentVersion("POTION", "POTION", "POTION"), 0, 1, "", "INVISIBILITY 30 1", "Invisibility Potion");
            this.addDefaultShopCategory("utility-category", 7, BedWars.getForCurrentVersion("TNT", "TNT", "TNT"), 0, 1, false);
            this.adCategoryContentTier("utility-category", "golden-apple", 19, "tier1", BedWars.getForCurrentVersion("GOLDEN_APPLE", "GOLDEN_APPLE", "GOLDEN_APPLE"), 0, 1, false, 3, "gold", false, false);
            this.addBuyItem("utility-category", "golden-apple", "tier1", "apple", BedWars.getForCurrentVersion("GOLDEN_APPLE", "GOLDEN_APPLE", "GOLDEN_APPLE"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("utility-category", "bedbug", 20, "tier1", BedWars.getForCurrentVersion("SNOW_BALL", "SNOW_BALL", "SNOWBALL"), 0, 1, false, 40, "iron", false, false);
            this.addBuyItem("utility-category", "bedbug", "tier1", "bedbug", BedWars.getForCurrentVersion("SNOW_BALL", "SNOW_BALL", "SNOWBALL"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("utility-category", "dream-defender", 21, "tier1", BedWars.getForCurrentVersion("MONSTER_EGG", "MONSTER_EGG", "HORSE_SPAWN_EGG"), 0, 1, false, 120, "iron", false, false);
            this.addBuyItem("utility-category", "dream-defender", "tier1", "defender", BedWars.getForCurrentVersion("MONSTER_EGG", "MONSTER_EGG", "HORSE_SPAWN_EGG"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("utility-category", "fireball", 22, "tier1", BedWars.getForCurrentVersion("FIREBALL", "FIREBALL", "FIRE_CHARGE"), 0, 1, false, 40, "iron", false, false);
            this.addBuyItem("utility-category", "fireball", "tier1", "fireball", BedWars.getForCurrentVersion("FIREBALL", "FIREBALL", "FIRE_CHARGE"), 0, 1, "", "", "Fireball", false);
            this.adCategoryContentTier("utility-category", "tnt", 23, "tier1", BedWars.getForCurrentVersion("TNT", "TNT", "TNT"), 0, 1, false, 4, "gold", false, false);
            this.addBuyItem("utility-category", "tnt", "tier1", "tnt", BedWars.getForCurrentVersion("TNT", "TNT", "TNT"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("utility-category", "ender-pearl", 24, "tier1", BedWars.getForCurrentVersion("ENDER_PEARL", "ENDER_PEARL", "ENDER_PEARL"), 0, 1, false, 4, "emerald", false, false);
            this.addBuyItem("utility-category", "ender-pearl", "tier1", "ender-pearl", BedWars.getForCurrentVersion("ENDER_PEARL", "ENDER_PEARL", "ENDER_PEARL"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("utility-category", "water-bucket", 25, "tier1", BedWars.getForCurrentVersion("WATER_BUCKET", "WATER_BUCKET", "WATER_BUCKET"), 0, 1, false, 4, "gold", false, false);
            this.addBuyItem("utility-category", "water-bucket", "tier1", "water-bucket", BedWars.getForCurrentVersion("WATER_BUCKET", "WATER_BUCKET", "WATER_BUCKET"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("utility-category", "bridge-egg", 28, "tier1", BedWars.getForCurrentVersion("EGG", "EGG", "EGG"), 0, 1, false, 3, "emerald", false, false);
            this.addBuyItem("utility-category", "bridge-egg", "tier1", "egg", BedWars.getForCurrentVersion("EGG", "EGG", "EGG"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("utility-category", "magic-milk", 29, "tier1", BedWars.getForCurrentVersion("MILK_BUCKET", "MILK_BUCKET", "MILK_BUCKET"), 0, 1, false, 4, "gold", false, false);
            this.addBuyItem("utility-category", "magic-milk", "tier1", "milk", BedWars.getForCurrentVersion("MILK_BUCKET", "MILK_BUCKET", "MILK_BUCKET"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("utility-category", "sponge", 30, "tier1", BedWars.getForCurrentVersion("SPONGE", "SPONGE", "SPONGE"), 0, 1, false, 3, "gold", false, false);
            this.addBuyItem("utility-category", "sponge", "tier1", "sponge", BedWars.getForCurrentVersion("SPONGE", "SPONGE", "SPONGE"), 0, 1, "", "", "", false);
            this.adCategoryContentTier("utility-category", "tower", 31, "tier1", BedWars.getForCurrentVersion("CHEST", "CHEST", "CHEST"), 0, 1, false, 24, "iron", false, false);
            this.addBuyItem("utility-category", "tower", "tier1", "tower", BedWars.getForCurrentVersion("CHEST", "CHEST", "CHEST"), 0, 1, "", "", "", false);
        }
        if (this.getYml().get("armor-category.category-content.diamond-armor") != null) {
            this.getYml().addDefault("armor-category.category-content.diamond-armor.content-settings.weight", (Object)2);
        }
        if (this.getYml().get("armor-category.category-content.iron-armor") != null) {
            this.getYml().addDefault("armor-category.category-content.iron-armor.content-settings.weight", (Object)1);
        }
        try {
            material = this.getYml().getString("shop-specials.iron-golem.material");
            BedWars.debug("shop-specials.iron-golem.material is set to: " + material);
            Material.valueOf((String)material);
        } catch (Exception ex) {
            BedWars.plugin.getLogger().severe("Invalid material at shop-specials.iron-golem.material");
        }
        try {
            material = this.getYml().getString("shop-specials.silverfish.material");
            BedWars.debug("shop-specials.silverfish.material is set to: " + material);
            Material.valueOf((String)material);
        } catch (Exception ex) {
            BedWars.plugin.getLogger().severe("Invalid material at shop-specials.silverfish.material");
        }
        this.getYml().options().copyDefaults(true);
        this.save();
    }

    private void loadShop() {
        ItemStack button = BedWars.nms.createItemStack(this.getYml().getString("shop-settings.quick-buy-category.material"), this.getYml().getInt("shop-settings.quick-buy-category.amount"), (short)this.getYml().getInt("shop-settings.quick-buy-category.data"));
        if (this.getYml().getBoolean("shop-settings.quick-buy-category.enchanted")) {
            button = ShopManager.enchantItem(button);
        }
        QuickBuyButton qbb = new QuickBuyButton(0, button, "shop-items-messages.quick-buy-item-name", "shop-items-messages.quick-buy-item-lore");
        ItemStack separatorStandard = BedWars.nms.createItemStack(this.getYml().getString("shop-settings.regular-separator-item.material"), this.getYml().getInt("shop-settings.regular-separator-item.amount"), (short)this.getYml().getInt("shop-settings.regular-separator-item.data"));
        if (this.getYml().getBoolean("shop-settings.regular-separator-item.enchanted")) {
            separatorStandard = ShopManager.enchantItem(separatorStandard);
        }
        ItemStack separatorSelected = BedWars.nms.createItemStack(this.getYml().getString("shop-settings.selected-separator-item.material"), this.getYml().getInt("shop-settings.selected-separator-item.amount"), (short)this.getYml().getInt("shop-settings.selected-separator-item.data"));
        if (this.getYml().getBoolean("shop-settings.selected-separator-item.enchanted")) {
            separatorSelected = ShopManager.enchantItem(separatorSelected);
        }
        shop = new ShopIndex("shop-items-messages.inventory-name", qbb, "shop-items-messages.separator-item-name", "shop-items-messages.separator-item-lore", separatorSelected, separatorStandard);
        for (String s : this.getYml().getConfigurationSection("").getKeys(false)) {
            ShopCategory sc;
            if (s.equalsIgnoreCase("shop-settings") || s.equals("quick-buy-defaults") || s.equalsIgnoreCase("shop-specials") || !(sc = new ShopCategory(s, this.getYml())).isLoaded()) continue;
            shop.addShopCategory(sc);
        }
    }

    public static ItemMeta hideItemStuff(ItemMeta im) {
        if (im != null) {
            im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON});
        }
        return im;
    }

    public static ItemStack enchantItem(ItemStack itemStack) {
        ItemStack i = new ItemStack(itemStack);
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
            i.setItemMeta(ShopManager.hideItemStuff(im));
        }
        return i;
    }

    private void addDefaultShopCategory(String path, int slot, String material, int data, int amount, boolean enchant) {
        this.getYml().addDefault(path + ".category-slot", (Object)slot);
        this.getYml().addDefault(path + ".category-item.material", (Object)material);
        this.getYml().addDefault(path + ".category-item.data", (Object)data);
        this.getYml().addDefault(path + ".category-item.amount", (Object)amount);
        this.getYml().addDefault(path + ".category-item.enchanted", (Object)enchant);
    }

    public void adCategoryContentTier(String path, String contentName, int contentSlot, String tierName, String tierMaterial, int tierData, int amount, boolean enchant, int tierCost, String tierCurrency, boolean permanent, boolean downgradable) {
        path = (String)path + ".category-content." + contentName + ".";
        this.getYml().addDefault((String)path + "content-settings.content-slot", (Object)contentSlot);
        this.getYml().addDefault((String)path + "content-settings.is-permanent", (Object)permanent);
        this.getYml().addDefault((String)path + "content-settings.is-downgradable", (Object)downgradable);
        path = (String)path + "content-tiers." + tierName;
        this.getYml().addDefault((String)path + ".tier-item.material", (Object)tierMaterial);
        this.getYml().addDefault((String)path + ".tier-item.data", (Object)tierData);
        this.getYml().addDefault((String)path + ".tier-item.amount", (Object)amount);
        this.getYml().addDefault((String)path + ".tier-item.enchanted", (Object)enchant);
        this.getYml().addDefault((String)path + ".tier-settings.cost", (Object)tierCost);
        this.getYml().addDefault((String)path + ".tier-settings.currency", (Object)tierCurrency);
    }

    public void adCategoryContentTier(String path, String contentName, int contentSlot, String tierName, String tierMaterial, int tierData, int amount, boolean enchant, int tierCost, String tierCurrency, boolean permanent, boolean downgradable, boolean unbreakable) {
        path = (String)path + ".category-content." + contentName + ".";
        this.getYml().addDefault((String)path + "content-settings.content-slot", (Object)contentSlot);
        this.getYml().addDefault((String)path + "content-settings.is-permanent", (Object)permanent);
        this.getYml().addDefault((String)path + "content-settings.is-downgradable", (Object)downgradable);
        this.getYml().addDefault((String)path + "content-settings.is-unbreakable", (Object)unbreakable);
        path = (String)path + "content-tiers." + tierName;
        this.getYml().addDefault((String)path + ".tier-item.material", (Object)tierMaterial);
        this.getYml().addDefault((String)path + ".tier-item.data", (Object)tierData);
        this.getYml().addDefault((String)path + ".tier-item.amount", (Object)amount);
        this.getYml().addDefault((String)path + ".tier-item.enchanted", (Object)enchant);
        this.getYml().addDefault((String)path + ".tier-settings.cost", (Object)tierCost);
        this.getYml().addDefault((String)path + ".tier-settings.currency", (Object)tierCurrency);
    }

    public void addBuyItem(String path, String contentName, String tierName, String item, String material, int data, int amount, String enchant, String potion, String itemName, boolean autoEquip) {
        path = (String)path + ".category-content." + contentName + ".content-tiers." + tierName + ".buy-items." + item + ".";
        this.getYml().addDefault((String)path + "material", (Object)material);
        this.getYml().addDefault((String)path + "data", (Object)data);
        this.getYml().addDefault((String)path + "amount", (Object)amount);
        if (!enchant.isEmpty()) {
            this.getYml().addDefault((String)path + "enchants", (Object)enchant);
        }
        if (!potion.isEmpty()) {
            this.getYml().addDefault((String)path + "potion", (Object)potion);
        }
        if (autoEquip) {
            this.getYml().addDefault((String)path + "auto-equip", (Object)true);
        }
        if (!itemName.isEmpty()) {
            this.getYml().addDefault((String)path + "name", (Object)itemName);
        }
    }

    public void addBuyPotion(String path, String contentName, String tierName, String item, String material, int data, int amount, String enchant, String potion, String itemName) {
        path = (String)path + ".category-content." + contentName + ".content-tiers." + tierName + ".buy-items." + item + ".";
        this.getYml().addDefault((String)path + "material", (Object)material);
        this.getYml().addDefault((String)path + "data", (Object)data);
        this.getYml().addDefault((String)path + "amount", (Object)amount);
        if (!enchant.isEmpty()) {
            this.getYml().addDefault((String)path + "enchants", (Object)enchant);
        }
        if (!potion.isEmpty()) {
            this.getYml().addDefault((String)path + "potion", (Object)potion);
        }
        this.getYml().addDefault((String)path + "potion-color", (Object)"");
        if (!itemName.isEmpty()) {
            this.getYml().addDefault((String)path + "name", (Object)itemName);
        }
    }

    public static ShopIndex getShop() {
        return shop;
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener)new InventoryListener(), (Plugin)BedWars.plugin);
        pm.registerEvents((Listener)new ShopCacheListener(), (Plugin)BedWars.plugin);
        pm.registerEvents((Listener)new QuickBuyListener(), (Plugin)BedWars.plugin);
        pm.registerEvents((Listener)new ShopOpenListener(), (Plugin)BedWars.plugin);
        pm.registerEvents((Listener)new PlayerDropListener(), (Plugin)BedWars.plugin);
        pm.registerEvents((Listener)new SpecialsListener(), (Plugin)BedWars.plugin);
    }
}

