package me.gypopo.economyshopgui.objects;

import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.CalculateAmount;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.priceModifiers.ModifierType;
import me.gypopo.economyshopgui.providers.priceModifiers.Modifiers;
import me.gypopo.economyshopgui.providers.priceModifiers.PriceModifier;
import me.gypopo.economyshopgui.providers.priceModifiers.seasons.SeasonModifier;
import me.gypopo.economyshopgui.providers.requirements.ItemRequirement;
import me.gypopo.economyshopgui.providers.requirements.RequirementType;
import me.gypopo.economyshopgui.providers.requirements.Requirements;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.SkullUtil;
import me.gypopo.economyshopgui.util.XMaterial;
import me.gypopo.economyshopgui.util.exceptions.ItemLoadException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ShopItem implements DisplayItem {
   private static final String[] pricePlaceholders = new String[]{"%buyPrice%", "%buyPriceRaw%", "%sellPrice%", "%sellPriceRaw%", "%player_displayname%", "%player_balance%", "%page%", "%pages%"};
   private ItemStack shopItem;
   private ItemStack itemToGive;
   private boolean error = false;
   private final EcoType economy;
   private boolean decorationLore;
   private boolean displayItem;
   private boolean priorLore;
   private boolean disablePricingLore;
   private boolean buyLore = true;
   private boolean sellLore = true;
   private final boolean hidden;
   private final boolean closeMenu;
   private final boolean matchMeta;
   public boolean rgb;
   private final Modifiers modifiers = new Modifiers();
   private final Requirements requirements;
   private int stackSize;
   private double buyPrice = -1.0D;
   private double sellPrice = -1.0D;
   private int loreLine = 0;
   private DisplayItem.DynamicLore lore;
   private final List<Integer> placeholders = new ArrayList();
   private String displayname;
   private String name;
   private final int maxBuy;
   private final int maxSell;
   private final int minBuy;
   private final int minSell;
   private final String subSection;
   private final String itemPath;
   public final String section;
   public final String itemLoc;

   public ShopItem(String section, String itemLoc) {
      this.section = section;
      this.itemLoc = itemLoc;
      this.itemPath = this.section + "." + this.itemLoc;
      this.hidden = ConfigManager.getShop(this.section).getBoolean("pages." + this.itemLoc + ".hidden");
      this.subSection = ConfigManager.getShop(this.section).getString("pages." + this.itemLoc + ".section");
      if (this.subSection != null) {
         this.displayItem = true;
      }

      this.priorLore = ConfigManager.getShop(this.section).getBoolean("pages." + this.itemLoc + ".prior-lore", EconomyShopGUI.getInstance().prioritizeItemLore);
      this.economy = EconomyShopGUI.getInstance().getEcoHandler().getEcon(this.section).getType();
      this.closeMenu = ConfigManager.getShop(this.section).getBoolean(this.itemPath + ".close-menu");
      this.requirements = EconomyShopGUI.getInstance().getRequirementManager().load(this.section, "pages." + this.itemLoc);
      this.matchMeta = ConfigManager.getShop(section).getBoolean("pages." + this.itemLoc + ".matchMeta", true) && EconomyShopGUI.getInstance().matchMeta;
      this.maxBuy = ConfigManager.getShop(this.section).getInt("pages." + this.itemLoc + ".max-buy");
      this.maxSell = ConfigManager.getShop(this.section).getInt("pages." + this.itemLoc + ".max-sell");
      this.minBuy = ConfigManager.getShop(this.section).getInt("pages." + this.itemLoc + ".min-buy");
      this.minSell = ConfigManager.getShop(this.section).getInt("pages." + this.itemLoc + ".min-sell");
      boolean defaultStackSize = false;
      if (!ConfigManager.getShop(this.section).contains("pages." + this.itemLoc + ".stack-size")) {
         if (this.minBuy > 0 && this.minBuy > this.stackSize) {
            this.stackSize = this.minBuy;
            defaultStackSize = true;
         } else if (this.minSell > 0 && this.minSell > this.stackSize) {
            this.stackSize = this.minSell;
            defaultStackSize = true;
         }
      }

      try {
         if (ChatUtil.hasRGB(EconomyShopGUI.getInstance().getEcoHandler().getEcon(this.economy).getPlural()) || ChatUtil.hasRGB(EconomyShopGUI.getInstance().getEcoHandler().getEcon(this.economy).getSingular())) {
            this.rgb = true;
         }

         this.shopItem = EconomyShopGUI.getInstance().createItem.loadShopSectionItem(this);
         if (this.shopItem == null) {
            this.shopItem = EconomyShopGUI.getInstance().createItem.getInvalidShopItem(this.section, this.itemLoc);
            this.loreLine = 0;
            this.error = true;
         } else if (this.shopItem.getType() != Material.AIR) {
            this.itemToGive = EconomyShopGUI.getInstance().createItem.loadItemToGive(this);
            this.stackSize = this.itemToGive.getAmount();
            Double buyPrice = EconomyShopGUI.getInstance().createItem.getBaseBuyPrice(this.itemPath);
            Double sellPrice = EconomyShopGUI.getInstance().createItem.getBaseSellPrice(this.itemPath);
            if (buyPrice == null && sellPrice == null) {
               this.displayItem = true;
            } else {
               this.buyPrice = buyPrice != null ? (defaultStackSize ? buyPrice : buyPrice / (double)this.stackSize) : -1.0D;
               this.sellPrice = sellPrice != null ? (defaultStackSize ? sellPrice : sellPrice / (double)this.stackSize) : -1.0D;
            }

            this.name = EconomyShopGUI.getInstance().useItemName ? (this.itemToGive.getItemMeta().hasDisplayName() ? EconomyShopGUI.getInstance().getMetaUtils().getRawDisplayName(this.itemToGive.getItemMeta()) : EconomyShopGUI.getInstance().getMaterialName(this.itemToGive.getType().name())) : EconomyShopGUI.getInstance().getMaterialName(this.itemToGive.getType().name());
            this.displayname = EconomyShopGUI.getInstance().useItemName ? (this.shopItem.getItemMeta().hasDisplayName() ? EconomyShopGUI.getInstance().getMetaUtils().getRawDisplayName(this.shopItem.getItemMeta()) : EconomyShopGUI.getInstance().getMaterialName(this.shopItem.getType().name())) : EconomyShopGUI.getInstance().getMaterialName(this.shopItem.getType().name());
            List<String> list = this.shopItem.hasItemMeta() ? EconomyShopGUI.getInstance().getMetaUtils().getRawLore(this.shopItem.getItemMeta()) : null;
            this.lore = this.getDynamicLore(EconomyShopGUI.getInstance(), list);
            this.loreLine = 2 + (EconomyShopGUI.getInstance().MMB && !Lang.MIDDLE_CLICK_SELL_ALL.get().getLegacy().isEmpty() && sellPrice != null && !(this.sellPrice < 0.0D) ? 1 : 0);
            if (ConfigManager.getShop(this.section).getBoolean("pages." + this.itemLoc + ".hidePricingLore", false)) {
               this.disablePricingLore = true;
               this.loreLine -= 2;
            } else {
               if (sellPrice == null || this.sellPrice >= 0.0D && Lang.RIGHT_CLICK_SELL.get().getLegacy().isEmpty() || this.sellPrice < 0.0D && Lang.ITEM_CANNOT_BE_SOLD.get().getLegacy().isEmpty()) {
                  this.sellLore = false;
                  --this.loreLine;
               }

               if (buyPrice == null || this.buyPrice >= 0.0D && Lang.LEFT_CLICK_BUY.get().getLegacy().isEmpty() || this.buyPrice < 0.0D && Lang.ITEM_CANNOT_BE_BOUGHT.get().getLegacy().isEmpty()) {
                  this.buyLore = false;
                  --this.loreLine;
               }
            }

            if (!this.isLinked() && (this.buyPrice >= 0.0D || this.sellPrice >= 0.0D)) {
               this.decorationLore = true;
            }
         } else {
            this.displayItem = true;
         }
      } catch (ItemLoadException var7) {
         this.shopItem = EconomyShopGUI.getInstance().createItem.getInvalidShopItem(this.section, this.itemLoc, var7);
         this.loreLine = CalculateAmount.splitLongString(var7.getMessage()).size() - 1;
         this.error = true;
         SendMessage.warnMessage(var7.getMessage());
         SendMessage.errorShops(section, itemLoc);
      } catch (Exception var8) {
         this.shopItem = EconomyShopGUI.getInstance().createItem.getInvalidShopItem(this.section, this.itemLoc);
         this.loreLine = 0;
         this.error = true;
         SendMessage.warnMessage("A error occurred while loading item '" + itemLoc + "' from section " + section);
         var8.printStackTrace();
      }

   }

   public ShopItem(ShopItem shopItem) {
      this.itemPath = shopItem.itemPath;
      this.subSection = shopItem.subSection;
      this.section = shopItem.section;
      this.itemLoc = shopItem.itemLoc;
      this.hidden = shopItem.hidden;
      this.economy = shopItem.economy;
      this.closeMenu = shopItem.closeMenu;
      this.matchMeta = shopItem.matchMeta;
      this.maxBuy = shopItem.maxBuy;
      this.maxSell = shopItem.maxSell;
      this.minBuy = shopItem.minBuy;
      this.minSell = shopItem.minSell;
      this.shopItem = shopItem.shopItem;
      this.itemToGive = shopItem.itemToGive;
      this.buyPrice = shopItem.buyPrice;
      this.sellPrice = shopItem.sellPrice;
      this.requirements = shopItem.requirements;
      this.name = shopItem.name;
      this.displayname = shopItem.displayname;
      this.loreLine = shopItem.loreLine;
      this.buyLore = shopItem.buyLore;
      this.sellLore = shopItem.sellLore;
      this.priorLore = shopItem.priorLore;
      this.disablePricingLore = shopItem.disablePricingLore;
      this.error = shopItem.error;
      this.stackSize = shopItem.stackSize;
      this.displayItem = shopItem.displayItem;
   }

   public DisplayItem.DynamicLore getLore() {
      return this.lore;
   }

   public String section() {
      return this.section;
   }

   public String itemLoc() {
      return this.itemLoc;
   }

   public String getItemPath() {
      return this.itemPath;
   }

   public ItemStack getShopItem() {
      return this.shopItem;
   }

   public ItemStack getItemToGive() {
      return this.itemToGive;
   }

   public boolean hasPlaceholders() {
      return !this.placeholders.isEmpty();
   }

   public List<Integer> getPlaceholders() {
      return this.placeholders;
   }

   public boolean meetsRequirements(Player p, boolean silent) {
      if (this.requirements == null) {
         return true;
      } else {
         Iterator var3 = this.requirements.iterator();

         ItemRequirement requirement;
         do {
            if (!var3.hasNext()) {
               return true;
            }

            requirement = (ItemRequirement)var3.next();
         } while(requirement.isMet(p));

         if (!silent) {
            requirement.sendNotMetMessage(p);
         }

         return false;
      }
   }

   public void addItemRequirement(ItemRequirement requirement) {
      this.requirements.add(requirement);
   }

   public void addItemRequirements(Collection<? extends ItemRequirement> requirements) {
      this.requirements.addAll(requirements);
   }

   public Requirements getRequirements() {
      return this.requirements;
   }

   public boolean hasItemRequirement(RequirementType type) {
      return this.requirements.hasType(type);
   }

   public void removeItemRequirement(ItemRequirement requirement) {
      this.requirements.remove(requirement);
   }

   public void removeItemRequirements(Collection<? extends ItemRequirement> requirements) {
      this.requirements.removeAll(requirements);
   }

   public boolean isLinked() {
      return this.subSection != null;
   }

   public String getSubSection() {
      return this.subSection;
   }

   public int getStackSize() {
      return this.stackSize;
   }

   public double getBuyPrice() {
      return this.buyPrice;
   }

   public double getBuyPrice(Player player) {
      return this.getPlayerBuyPrice(player, this.buyPrice);
   }

   private double getPlayerBuyPrice(Player player, double price) {
      PriceModifier modifier;
      for(Iterator var4 = this.modifiers.iterator(); var4.hasNext(); price = modifier.modify(player, price)) {
         modifier = (PriceModifier)var4.next();
      }

      if (EconomyShopGUI.getInstance().discountsActive && EconomyShopGUI.getInstance().hasDiscount(this.section)) {
         price = CalculateAmount.calculateDiscount(player, this.section, this.buyPrice);
      }

      return price;
   }

   public double getSellPrice() {
      return this.sellPrice;
   }

   public double getSellPrice(ItemStack itemToSell) {
      return itemToSell.getType().equals(XMaterial.SPAWNER.parseMaterial()) ? EconomyShopGUI.getInstance().getSpawnerManager().getProvider().getSpawnerSellPrice(itemToSell, this.sellPrice) * (double)itemToSell.getAmount() : this.sellPrice * (double)itemToSell.getAmount();
   }

   public double getSellPrice(Player player) {
      return this.getPlayerSellPrice(player, this.sellPrice);
   }

   public double getSellPrice(Player player, ItemStack itemToSell) {
      double base = itemToSell.getType().equals(XMaterial.SPAWNER.parseMaterial()) ? EconomyShopGUI.getInstance().getSpawnerManager().getProvider().getSpawnerSellPrice(itemToSell, this.sellPrice) * (double)itemToSell.getAmount() : this.sellPrice * (double)itemToSell.getAmount();
      return this.getPlayerSellPrice(player, base);
   }

   public double getSellPrice(Player player, ItemStack itemToSell, int amount) {
      double base = itemToSell.getType().equals(XMaterial.SPAWNER.parseMaterial()) ? EconomyShopGUI.getInstance().getSpawnerManager().getProvider().getSpawnerSellPrice(itemToSell, this.sellPrice) * (double)amount : this.sellPrice * (double)amount;
      return this.getPlayerSellPrice(player, base);
   }

   private double getPlayerSellPrice(Player player, double price) {
      PriceModifier modifier;
      for(Iterator var4 = this.modifiers.iterator(); var4.hasNext(); price = modifier.modify(player, price)) {
         modifier = (PriceModifier)var4.next();
      }

      if (EconomyShopGUI.getInstance().multipliers && EconomyShopGUI.getInstance().hasMultiplier(this.section)) {
         price = CalculateAmount.calculateMultiplier(player, this.section, price);
      }

      return price;
   }

   public String getDisplayname() {
      return this.displayname;
   }

   public String getName() {
      return this.name;
   }

   public boolean isDisplayItem() {
      return this.displayItem;
   }

   public boolean isDynamicPricing() {
      return false;
   }

   public int getLevelRequired() {
      return 0;
   }

   public int getLimitedStockMode() {
      return 0;
   }

   public boolean isRefillStock() {
      return false;
   }

   public int getLimitedSellMode() {
      return 0;
   }

   public boolean hasItemError() {
      return this.error;
   }

   public int getMaxBuy() {
      return this.maxBuy;
   }

   public boolean isMaxBuy(int amount) {
      return this.maxBuy > 0 && this.maxBuy < amount;
   }

   public int getMaxSell() {
      return this.maxSell;
   }

   public boolean isMaxSell(int amount) {
      return this.maxSell > 0 && this.maxSell < amount;
   }

   public int getMinBuy() {
      return this.minBuy;
   }

   public boolean isMinBuy(int amount) {
      return this.minBuy > 0 && this.minBuy > amount;
   }

   public int getMinSell() {
      return this.minSell;
   }

   public boolean isMinSell(int amount) {
      return this.minSell > 0 && this.minSell > amount;
   }

   public int getLoreLine() {
      return this.loreLine;
   }

   public boolean isBuyLore() {
      return this.buyLore;
   }

   public boolean isSellLore() {
      return this.sellLore;
   }

   public boolean isDecorationLore() {
      return this.decorationLore;
   }

   public boolean isPriorLore() {
      return this.priorLore;
   }

   public boolean isHidden() {
      return this.hidden;
   }

   public boolean isCloseMenu() {
      return this.closeMenu;
   }

   public boolean isHidePricingLore() {
      return this.disablePricingLore;
   }

   public EcoType getEcoType() {
      return this.economy;
   }

   public void addPriceModifier(PriceModifier modifier) {
      this.modifiers.add(modifier);
   }

   public void addPriceModifiers(Collection<? extends PriceModifier> modifiers) {
      this.modifiers.addAll(modifiers);
   }

   public boolean hasPriceModifier(ModifierType type) {
      return this.modifiers.hasType(type);
   }

   public boolean hasSeasonModifier(String world) {
      return this.modifiers.hasSeasonModifier(world);
   }

   public void removePriceModifier(SeasonModifier modifier) {
      this.modifiers.remove(modifier);
   }

   public void removePriceModifiers(Collection<? extends PriceModifier> modifiers) {
      this.modifiers.removeAll(modifiers);
   }

   public void updateSkullTexture(GameProfile profile, boolean itg) {
      SkullMeta meta = (SkullMeta)(itg ? this.itemToGive : this.shopItem).getItemMeta();
      SkullUtil.applySkullProfile(profile, meta);
      (itg ? this.itemToGive : this.shopItem).setItemMeta(meta);
   }

   public boolean matchMeta(ItemStack item) {
      if (!this.matchMeta) {
         return true;
      } else {
         return item.getType().equals(XMaterial.SPAWNER.parseMaterial()) ? EconomyShopGUI.getInstance().getSpawnerManager().getProvider().isShopSpawner(item, this.itemToGive) : EconomyShopGUI.getInstance().isSimilar(item, this.itemToGive);
      }
   }

   public boolean match(ItemStack item) {
      return !item.getType().equals(this.itemToGive.getType()) ? false : this.matchMeta(item);
   }

   private DisplayItem.DynamicLore getDynamicLore(EconomyShopGUI plugin, List<String> lore) {
      return (DisplayItem.DynamicLore)(plugin.paperMeta ? new ShopItem.DynamicCompLore(lore) : new ShopItem.DynamicBukkitLore(plugin, lore));
   }

   public final class DynamicCompLore implements DisplayItem.DynamicLore {
      private final String[] lore;
      private final String[] compLore;

      public DynamicCompLore(List<String> param2) {
         this.lore = list != null && !list.isEmpty() ? (String[])list.toArray(new String[0]) : new String[0];
         this.compLore = (String[])Arrays.stream(this.lore).map(ChatUtil::getGsonComponent).toArray((x$0) -> {
            return new String[x$0];
         });
         int i = 0;
         String[] var4 = this.compLore;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String s = var4[var6];
            Stream var10000 = Arrays.stream(ShopItem.pricePlaceholders);
            Objects.requireNonNull(s);
            if (var10000.anyMatch(s::contains)) {
               ShopItem.this.placeholders.add(i);
            }

            ++i;
         }

      }

      public String[] get(boolean fast, boolean pr) {
         return (String[])(fast ? this.compLore : this.lore).clone();
      }
   }

   public final class DynamicBukkitLore implements DisplayItem.DynamicLore {
      private final String[] lore;
      private final String[] fastLore;

      public DynamicBukkitLore(EconomyShopGUI param2, List<String> param3) {
         this.lore = list != null && !list.isEmpty() ? (String[])list.stream().map(ChatUtil::formatColors).toArray((x$0) -> {
            return new String[x$0];
         }) : new String[0];
         this.fastLore = (String[])Arrays.stream(this.lore).map((sx) -> {
            return plugin.versionHandler.toNBT(sx);
         }).toArray((x$0) -> {
            return new String[x$0];
         });
         int i = 0;
         String[] var5 = this.lore;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            String s = var5[var7];
            Stream var10000 = Arrays.stream(ShopItem.pricePlaceholders);
            Objects.requireNonNull(s);
            if (var10000.anyMatch(s::contains)) {
               ShopItem.this.placeholders.add(i);
            }

            ++i;
         }

      }

      public String[] get(boolean fast, boolean pr) {
         return (String[])(fast && !pr ? this.fastLore : this.lore).clone();
      }
   }

   public static class Shulker extends ShopItem {
      private final Map<ShopItem, Integer> contents;
      private final ItemStack box;

      public Shulker(ShopItem shopItem, ItemStack box, Map<ShopItem, Integer> contents) {
         super(shopItem);
         this.box = box;
         this.contents = contents;
      }

      public Map<ShopItem, Integer> getContents() {
         return this.contents;
      }

      public ItemStack getBox() {
         return this.box;
      }
   }
}
