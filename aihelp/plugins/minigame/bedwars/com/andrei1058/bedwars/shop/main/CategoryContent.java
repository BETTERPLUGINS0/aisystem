/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.andrei1058.bedwars.shop.main;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.shop.IBuyItem;
import com.andrei1058.bedwars.api.arena.shop.ICategoryContent;
import com.andrei1058.bedwars.api.arena.shop.IContentTier;
import com.andrei1058.bedwars.api.events.shop.ShopBuyEvent;
import com.andrei1058.bedwars.api.language.Language;
import com.andrei1058.bedwars.api.language.Messages;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.configuration.Sounds;
import com.andrei1058.bedwars.shop.ShopCache;
import com.andrei1058.bedwars.shop.main.ContentTier;
import com.andrei1058.bedwars.shop.main.ShopCategory;
import com.andrei1058.bedwars.shop.main.ShopIndex;
import com.andrei1058.bedwars.shop.quickbuy.PlayerQuickBuyCache;
import com.andrei1058.bedwars.shop.quickbuy.QuickBuyElement;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CategoryContent
implements ICategoryContent {
    private int slot;
    private boolean loaded = false;
    private List<IContentTier> contentTiers = new ArrayList<IContentTier>();
    private String contentName;
    private String itemNamePath;
    private String itemLorePath;
    private String identifier;
    private boolean permanent = false;
    private boolean downgradable = false;
    private boolean unbreakable = false;
    private byte weight = 0;
    private ShopCategory father;

    public CategoryContent(String path, String name, String categoryName, YamlConfiguration yml, ShopCategory father) {
        BedWars.debug("Loading CategoryContent " + path);
        this.contentName = name;
        this.father = father;
        if (path == null || name == null || categoryName == null || yml == null) {
            return;
        }
        if (yml.get(path + ".content-settings.content-slot") == null) {
            BedWars.plugin.getLogger().severe("Content slot not set at " + path);
            return;
        }
        if (yml.get(path + ".content-tiers") == null) {
            BedWars.plugin.getLogger().severe("No tiers set for " + path);
            return;
        }
        if (yml.getConfigurationSection(path + ".content-tiers").getKeys(false).isEmpty()) {
            BedWars.plugin.getLogger().severe("No tiers set for " + path);
            return;
        }
        if (yml.get(path + ".content-tiers.tier1") == null) {
            BedWars.plugin.getLogger().severe("tier1 not found for " + path);
            return;
        }
        if (yml.get(path + ".content-settings.is-permanent") != null) {
            this.permanent = yml.getBoolean(path + ".content-settings.is-permanent");
        }
        if (yml.get(path + ".content-settings.is-downgradable") != null) {
            this.downgradable = yml.getBoolean(path + ".content-settings.is-downgradable");
        }
        if (yml.get(path + ".content-settings.is-unbreakable") != null) {
            this.unbreakable = yml.getBoolean(path + ".content-settings.is-unbreakable");
        }
        if (yml.get(path + ".content-settings.weight") != null) {
            this.weight = (byte)yml.getInt(path + ".content-settings.weight");
        }
        this.slot = yml.getInt(path + ".content-settings.content-slot");
        for (String s : yml.getConfigurationSection(path + ".content-tiers").getKeys(false)) {
            ContentTier ctt = new ContentTier(path + ".content-tiers." + s, s, path, yml);
            this.contentTiers.add(ctt);
        }
        this.itemNamePath = "shop-items-messages.%category%.content-item-%content%-name".replace("%category%", categoryName).replace("%content%", this.contentName);
        for (Language lang : Language.getLanguages()) {
            if (lang.exists(this.itemNamePath)) continue;
            lang.set(this.itemNamePath, "&cName not set");
        }
        this.itemLorePath = "shop-items-messages.%category%.content-item-%content%-lore".replace("%category%", categoryName).replace("%content%", this.contentName);
        for (Language lang : Language.getLanguages()) {
            if (lang.exists(this.itemLorePath)) continue;
            lang.set(this.itemLorePath, "&cLore not set");
        }
        this.identifier = path;
        this.loaded = true;
    }

    public void execute(Player player, ShopCache shopCache, int slot) {
        IContentTier ct;
        if (shopCache.getCategoryWeight(this.father) > this.weight) {
            return;
        }
        if (shopCache.getContentTier(this.getIdentifier()) > this.contentTiers.size()) {
            Bukkit.getLogger().severe("Wrong tier order at: " + this.getIdentifier());
            return;
        }
        if (shopCache.getContentTier(this.getIdentifier()) == this.contentTiers.size()) {
            if (this.isPermanent() && shopCache.hasCachedItem(this)) {
                player.sendMessage(Language.getMsg(player, Messages.SHOP_ALREADY_BOUGHT));
                Sounds.playSound("shop-insufficient-money", player);
                return;
            }
            ct = this.contentTiers.get(shopCache.getContentTier(this.getIdentifier()) - 1);
        } else {
            ct = !shopCache.hasCachedItem(this) ? this.contentTiers.get(0) : this.contentTiers.get(shopCache.getContentTier(this.getIdentifier()));
        }
        int money = CategoryContent.calculateMoney(player, ct.getCurrency());
        if (money < ct.getPrice()) {
            player.sendMessage(Language.getMsg(player, Messages.SHOP_INSUFFICIENT_MONEY).replace("{currency}", Language.getMsg(player, CategoryContent.getCurrencyMsgPath(ct))).replace("{amount}", String.valueOf(ct.getPrice() - money)));
            Sounds.playSound("shop-insufficient-money", player);
            return;
        }
        ShopBuyEvent event = new ShopBuyEvent(player, Arena.getArenaByPlayer(player), this);
        Bukkit.getPluginManager().callEvent((Event)event);
        if (event.isCancelled()) {
            return;
        }
        CategoryContent.takeMoney(player, ct.getCurrency(), ct.getPrice());
        shopCache.upgradeCachedItem(this, slot);
        this.giveItems(player, shopCache, Arena.getArenaByPlayer(player));
        Sounds.playSound("shop-bought", player);
        if (this.itemNamePath == null || Language.getPlayerLanguage(player).getYml().get(this.itemNamePath) == null) {
            ItemStack displayItem = ct.getItemStack();
            if (displayItem.getItemMeta() != null && displayItem.getItemMeta().hasDisplayName()) {
                player.sendMessage(Language.getMsg(player, Messages.SHOP_NEW_PURCHASE).replace("{item}", displayItem.getItemMeta().getDisplayName()));
            }
        } else {
            player.sendMessage(Language.getMsg(player, Messages.SHOP_NEW_PURCHASE).replace("{item}", ChatColor.stripColor((String)Language.getMsg(player, this.itemNamePath))).replace("{color}", "").replace("{tier}", ""));
        }
        shopCache.setCategoryWeight(this.father, this.weight);
    }

    public void giveItems(Player player, ShopCache shopCache, IArena arena) {
        for (IBuyItem bi : this.contentTiers.get(shopCache.getContentTier(this.getIdentifier()) - 1).getBuyItemsList()) {
            bi.give(player, arena);
        }
    }

    @Override
    public int getSlot() {
        return this.slot;
    }

    @Override
    public ItemStack getItemStack(Player player) {
        ShopCache sc = ShopCache.getShopCache(player.getUniqueId());
        return sc == null ? null : this.getItemStack(player, sc);
    }

    @Override
    public boolean hasQuick(Player player) {
        PlayerQuickBuyCache pqbc = PlayerQuickBuyCache.getQuickBuyCache(player.getUniqueId());
        return pqbc != null && this.hasQuick(pqbc);
    }

    public ItemStack getItemStack(Player player, ShopCache shopCache) {
        IContentTier ct = shopCache.getContentTier(this.identifier) == this.contentTiers.size() ? this.contentTiers.get(this.contentTiers.size() - 1) : (shopCache.hasCachedItem(this) ? this.contentTiers.get(shopCache.getContentTier(this.identifier)) : this.contentTiers.get(shopCache.getContentTier(this.identifier) - 1));
        ItemStack i = ct.getItemStack();
        ItemMeta im = i.getItemMeta();
        if (im != null) {
            im = i.getItemMeta().clone();
            boolean canAfford = CategoryContent.calculateMoney(player, ct.getCurrency()) >= ct.getPrice();
            PlayerQuickBuyCache qbc = PlayerQuickBuyCache.getQuickBuyCache(player.getUniqueId());
            boolean hasQuick = qbc != null && this.hasQuick(qbc);
            String color = Language.getMsg(player, canAfford ? "shop-items-messages.can-buy-color" : "shop-items-messages.cant-buy-color");
            String translatedCurrency = Language.getMsg(player, CategoryContent.getCurrencyMsgPath(ct));
            ChatColor cColor = CategoryContent.getCurrencyColor(ct.getCurrency());
            int tierI = ct.getValue();
            String tier = CategoryContent.getRomanNumber(tierI);
            String buyStatus = this.isPermanent() && shopCache.hasCachedItem(this) && shopCache.getCachedItem(this).getTier() == this.getContentTiers().size() ? (!BedWars.nms.isArmor(i) ? Language.getMsg(player, "shop-lore-status-tier-maxed") : Language.getMsg(player, "shop-lore-status-armor")) : (!canAfford ? Language.getMsg(player, "shop-lore-status-cant-afford").replace("{currency}", translatedCurrency) : Language.getMsg(player, "shop-lore-status-can-buy"));
            im.setDisplayName(Language.getMsg(player, this.itemNamePath).replace("{color}", color).replace("{tier}", tier));
            ArrayList<String> lore = new ArrayList<String>();
            for (String s : Language.getList(player, this.itemLorePath)) {
                if (s.contains("{quick_buy}")) {
                    if (hasQuick) {
                        if (!ShopIndex.getIndexViewers().contains(player.getUniqueId())) continue;
                        s = Language.getMsg(player, "shop-lore-quick-remove");
                    } else {
                        s = Language.getMsg(player, "shop-lore-quick-add");
                    }
                }
                s = s.replace("{tier}", tier).replace("{color}", color).replace("{cost}", String.valueOf(cColor) + String.valueOf(ct.getPrice())).replace("{currency}", String.valueOf(cColor) + translatedCurrency).replace("{buy_status}", buyStatus);
                lore.add(s);
            }
            im.setLore(lore);
            i.setItemMeta(im);
        }
        return i;
    }

    public boolean hasQuick(PlayerQuickBuyCache c) {
        for (QuickBuyElement q : c.getElements()) {
            if (q.getCategoryContent() != this) continue;
            return true;
        }
        return false;
    }

    public static int calculateMoney(Player player, Material currency) {
        if (currency == Material.AIR) {
            return (int)BedWars.getEconomy().getMoney(player);
        }
        int amount = 0;
        for (ItemStack is : player.getInventory().getContents()) {
            if (is == null || is.getType() != currency) continue;
            amount += is.getAmount();
        }
        return amount;
    }

    public static Material getCurrency(String currency) {
        Material material;
        switch (currency) {
            default: {
                material = Material.IRON_INGOT;
                break;
            }
            case "gold": {
                material = Material.GOLD_INGOT;
                break;
            }
            case "diamond": {
                material = Material.DIAMOND;
                break;
            }
            case "emerald": {
                material = Material.EMERALD;
                break;
            }
            case "vault": {
                material = Material.AIR;
            }
        }
        return material;
    }

    public static ChatColor getCurrencyColor(Material currency) {
        ChatColor c = ChatColor.DARK_GREEN;
        if (currency.toString().toLowerCase().contains("diamond")) {
            c = ChatColor.AQUA;
        } else if (currency.toString().toLowerCase().contains("gold")) {
            c = ChatColor.GOLD;
        } else if (currency.toString().toLowerCase().contains("iron")) {
            c = ChatColor.WHITE;
        }
        return c;
    }

    public static String getCurrencyMsgPath(IContentTier contentTier) {
        String c = contentTier.getCurrency().toString().toLowerCase().contains("iron") ? (contentTier.getPrice() == 1 ? Messages.MEANING_IRON_SINGULAR : Messages.MEANING_IRON_PLURAL) : (contentTier.getCurrency().toString().toLowerCase().contains("gold") ? (contentTier.getPrice() == 1 ? Messages.MEANING_GOLD_SINGULAR : Messages.MEANING_GOLD_PLURAL) : (contentTier.getCurrency().toString().toLowerCase().contains("emerald") ? (contentTier.getPrice() == 1 ? Messages.MEANING_EMERALD_SINGULAR : Messages.MEANING_EMERALD_PLURAL) : (contentTier.getCurrency().toString().toLowerCase().contains("diamond") ? (contentTier.getPrice() == 1 ? Messages.MEANING_DIAMOND_SINGULAR : Messages.MEANING_DIAMOND_PLURAL) : (contentTier.getPrice() == 1 ? Messages.MEANING_VAULT_SINGULAR : Messages.MEANING_VAULT_PLURAL))));
        return c;
    }

    public static String getRomanNumber(int n) {
        String s;
        switch (n) {
            default: {
                s = String.valueOf(n);
                break;
            }
            case 1: {
                s = "I";
                break;
            }
            case 2: {
                s = "II";
                break;
            }
            case 3: {
                s = "III";
                break;
            }
            case 4: {
                s = "IV";
                break;
            }
            case 5: {
                s = "V";
                break;
            }
            case 6: {
                s = "VI";
                break;
            }
            case 7: {
                s = "VII";
                break;
            }
            case 8: {
                s = "VIII";
                break;
            }
            case 9: {
                s = "IX";
                break;
            }
            case 10: {
                s = "X";
            }
        }
        return s;
    }

    public static void takeMoney(Player player, Material currency, int amount) {
        if (currency == Material.AIR) {
            if (!BedWars.getEconomy().isEconomy()) {
                player.sendMessage("\u00a74\u00a7lERROR: This requires Vault Support! Please install Vault plugin!");
                return;
            }
            BedWars.getEconomy().buyAction(player, amount);
            return;
        }
        int cost = amount;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i == null || i.getType() != currency) continue;
            if (i.getAmount() < cost) {
                cost -= i.getAmount();
                BedWars.nms.minusAmount(player, i, i.getAmount());
                player.updateInventory();
                continue;
            }
            BedWars.nms.minusAmount(player, i, cost);
            player.updateInventory();
            break;
        }
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public boolean isPermanent() {
        return this.permanent;
    }

    @Override
    public boolean isDowngradable() {
        return this.downgradable;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public List<IContentTier> getContentTiers() {
        return this.contentTiers;
    }
}

