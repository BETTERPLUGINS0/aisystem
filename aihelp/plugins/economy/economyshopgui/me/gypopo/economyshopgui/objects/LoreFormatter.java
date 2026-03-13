package me.gypopo.economyshopgui.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.CalculateAmount;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.providers.priceModifiers.seasons.SeasonProvider;
import me.gypopo.economyshopgui.providers.requirements.ItemRequirement;
import me.gypopo.economyshopgui.util.ChatUtil;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.Replacement;
import me.gypopo.economyshopgui.util.meta.PaperMeta;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class LoreFormatter {
   private final EconomyShopGUI plugin;
   private final boolean components;
   private final List<LoreFormatter.DisplayLore> displayLore;
   private final LoreFormatter.ActionAdminLore actionAdminLore;
   private int item;
   private final List<LoreFormatter.StaticLore> itemLore;

   public LoreFormatter(EconomyShopGUI plugin) {
      this.plugin = plugin;
      this.components = plugin.paperMeta;
      List<String> arrangement = ConfigManager.getConfig().getStringList("lore-arrangement");
      int index = arrangement.indexOf("item-lore");
      if (index == -1) {
         SendMessage.warnMessage("Failed to load lore arrangement, cannot find 'item-lore' which is required!");
         arrangement = ConfigManager.getConfig().getDef().getStringList("lore-arrangement");
         index = arrangement.indexOf("item-lore");
      }

      String path = "display-lore-layout.";
      List<LoreFormatter.DisplayLore> displayLore = new ArrayList();
      Iterator var6 = arrangement.iterator();

      ConfigurationSection config;
      byte var11;
      while(var6.hasNext()) {
         String section = (String)var6.next();
         int i = arrangement.indexOf(section);
         config = null;
         if (!section.equals("item-lore")) {
            config = ConfigManager.getConfig().getConfigurationSection(path + section);
            if (config == null) {
               SendMessage.warnMessage("Cannot find lore format for %part% inside config.yml, using default...".replace("%part%", section));
               config = ConfigManager.getConfig().getDef().getConfigurationSection(path + section);
            }
         }

         var11 = -1;
         switch(section.hashCode()) {
         case -1310968950:
            if (section.equals("special-lore")) {
               var11 = 0;
            }
            break;
         case -1247636699:
            if (section.equals("sell-prices")) {
               var11 = 3;
            }
            break;
         case 522657267:
            if (section.equals("decoration-lore")) {
               var11 = 4;
            }
            break;
         case 641703604:
            if (section.equals("admin-lore")) {
               var11 = 6;
            }
            break;
         case 1927356785:
            if (section.equals("buy-prices")) {
               var11 = 2;
            }
            break;
         case 2000629002:
            if (section.equals("item-requirements")) {
               var11 = 1;
            }
            break;
         case 2107400560:
            if (section.equals("item-lore")) {
               var11 = 5;
            }
         }

         switch(var11) {
         case 0:
            displayLore.add(new LoreFormatter.SpecialLore(config, i > index));
            break;
         case 1:
            displayLore.add(new LoreFormatter.RequirementLore());
            break;
         case 2:
            displayLore.add(new LoreFormatter.BuyLore(config, i > index));
            break;
         case 3:
            displayLore.add(new LoreFormatter.SellLore(config, i > index));
            break;
         case 4:
            displayLore.add(new LoreFormatter.DecorationLore(config));
            break;
         case 5:
            this.item = i;
            displayLore.add(new LoreFormatter.ItemLore());
            break;
         case 6:
            displayLore.add(new LoreFormatter.AdminLore(config, i > index));
         }
      }

      this.displayLore = displayLore;
      List<LoreFormatter.StaticLore> itemLore = new ArrayList();
      Iterator var18 = arrangement.iterator();

      while(true) {
         label157:
         while(var18.hasNext()) {
            String section = (String)var18.next();
            config = ConfigManager.getConfig().getConfigurationSection(path + section);
            if (config == null && !section.equals("item-lore")) {
               SendMessage.warnMessage("Cannot find lore format for %part% inside config.yml, using default...".replace("%part%", section));
               config = ConfigManager.getConfig().getDef().getConfigurationSection(path + section);
            }

            var11 = -1;
            switch(section.hashCode()) {
            case -1247636699:
               if (section.equals("sell-prices")) {
                  var11 = 1;
               }
               break;
            case 522657267:
               if (section.equals("decoration-lore")) {
                  var11 = 2;
               }
               break;
            case 1927356785:
               if (section.equals("buy-prices")) {
                  var11 = 0;
               }
               break;
            case 2107400560:
               if (section.equals("item-lore")) {
                  var11 = 3;
               }
            }

            switch(var11) {
            case 0:
               itemLore.add(new LoreFormatter.StaticLore(LoreFormatter.LoreType.BUY_LORE, config.getStringList("normal")));
               break;
            case 1:
               itemLore.add(new LoreFormatter.StaticLore(LoreFormatter.LoreType.SELL_LORE, config.getStringList("normal")));
               if (this.plugin.MMB && !Lang.SHIFT_RIGHT_CLICK_SELL_ALL.get().getLegacy().isEmpty()) {
                  itemLore.add(new LoreFormatter.StaticLore(LoreFormatter.LoreType.SELL_ALL_LORE, config.getStringList("sell-all")));
               }
               break;
            case 2:
               Iterator var12 = ConfigManager.getConfig().getConfigurationSection(path + section).getKeys(false).iterator();

               while(true) {
                  if (!var12.hasNext()) {
                     continue label157;
                  }

                  String s = (String)var12.next();
                  List<String> lore = ConfigManager.getConfig().getStringList(path + section + "." + s);
                  if (!lore.isEmpty()) {
                     byte var16 = -1;
                     switch(s.hashCode()) {
                     case -1903967040:
                        if (s.equals("extra-sell-all-lore")) {
                           var16 = 2;
                        }
                        break;
                     case -767525100:
                        if (s.equals("extra-sell-lore")) {
                           var16 = 3;
                        }
                        break;
                     case 380702458:
                        if (s.equals("extra-buy-lore")) {
                           var16 = 1;
                        }
                        break;
                     case 701456019:
                        if (s.equals("extra-lore")) {
                           var16 = 0;
                        }
                     }

                     switch(var16) {
                     case 0:
                        itemLore.add(new LoreFormatter.StaticLore(LoreFormatter.LoreType.DECORATION_LORE, lore));
                        break;
                     case 1:
                        itemLore.add(new LoreFormatter.StaticLore(LoreFormatter.LoreType.DECORATION_LORE_BUY, lore));
                        break;
                     case 2:
                        if (this.plugin.MMB && !Lang.SHIFT_RIGHT_CLICK_SELL_ALL.get().getLegacy().isEmpty()) {
                           itemLore.add(new LoreFormatter.StaticLore(LoreFormatter.LoreType.DECORATION_LORE_SELL_ALL, lore));
                        }
                        break;
                     case 3:
                        itemLore.add(new LoreFormatter.StaticLore(LoreFormatter.LoreType.DECORATION_LORE_SELL, lore));
                     }
                  }
               }
            case 3:
               itemLore.add(new LoreFormatter.StaticLore(LoreFormatter.LoreType.ITEM_LORE, (List)null));
            }
         }

         this.itemLore = itemLore;
         this.actionAdminLore = new LoreFormatter.ActionAdminLore(ConfigManager.getConfig().getConfigurationSection(path + ".admin-lore"));
         return;
      }
   }

   public ItemStack format(ActionItem actionItem, ItemStack item, Player p, EcoType type, String section, boolean fast, boolean pr, boolean itemIndex, int page, int pages) {
      LoreFormatter.Lore lore = this.components ? (fast ? new LoreFormatter.ComponentLore(item) : new LoreFormatter.RawComponentLore(item)) : (fast && !pr ? new LoreFormatter.FastLore(item) : new LoreFormatter.ShopLore(item));
      if (actionItem.getRequirements() != null) {
         Iterator var12 = actionItem.getRequirements().iterator();

         while(var12.hasNext()) {
            ItemRequirement requirement = (ItemRequirement)var12.next();
            if (!requirement.isMet(p)) {
               ((LoreFormatter.Lore)lore).addAll(requirement.getLore(p, fast, pr));
            }
         }
      }

      ((LoreFormatter.Lore)lore).addAll(this.parseLocal(actionItem, p, type, fast, pr, page, pages));
      this.actionAdminLore.apply(actionItem, p, (LoreFormatter.Lore)lore, section, fast, pr, itemIndex);
      return ((LoreFormatter.Lore)lore).form();
   }

   public ItemStack format(ShopItem shopItem, ItemStack item, Player p, String section, boolean fast, boolean pr, boolean discounts, boolean multipliers, boolean itemIndex, boolean seasonal, int page, int pages) {
      LoreFormatter.Lore lore = this.components ? (fast ? new LoreFormatter.ComponentLore(item) : new LoreFormatter.RawComponentLore(item)) : (fast && !pr ? new LoreFormatter.FastLore(item) : new LoreFormatter.ShopLore(item));
      Iterator var14 = (shopItem.isPriorLore() ? this.priorLore() : this.displayLore).iterator();

      while(true) {
         while(var14.hasNext()) {
            LoreFormatter.DisplayLore e = (LoreFormatter.DisplayLore)var14.next();
            if (e instanceof LoreFormatter.ItemLore) {
               ((LoreFormatter.Lore)lore).addAll(this.parseLocal(shopItem, p, discounts, multipliers, seasonal, fast, pr, page, pages));
            } else if (!(e instanceof LoreFormatter.BuyLore) && !(e instanceof LoreFormatter.SellLore)) {
               if (e instanceof LoreFormatter.AdminLore) {
                  e.apply(shopItem, p, (LoreFormatter.Lore)lore, section, fast, pr, itemIndex);
               } else if (e instanceof LoreFormatter.SpecialLore) {
                  e.apply(shopItem, p, (LoreFormatter.Lore)lore, section, fast, pr, seasonal);
               } else if (e instanceof LoreFormatter.DecorationLore) {
                  e.apply(shopItem, p, (LoreFormatter.Lore)lore, section, fast, pr);
               } else if (e instanceof LoreFormatter.RequirementLore && shopItem.getRequirements() != null) {
                  Iterator var16 = shopItem.getRequirements().iterator();

                  while(var16.hasNext()) {
                     ItemRequirement requirement = (ItemRequirement)var16.next();
                     if (!requirement.isMet(p)) {
                        ((LoreFormatter.Lore)lore).addAll(requirement.getLore(p, fast, pr));
                     }
                  }
               }
            } else if (!shopItem.isHidePricingLore() && !shopItem.hasItemError() && !shopItem.isDisplayItem()) {
               e.apply(shopItem, p, (LoreFormatter.Lore)lore, section, fast, pr, discounts, multipliers, seasonal);
            }
         }

         return ((LoreFormatter.Lore)lore).form();
      }
   }

   private List<LoreFormatter.DisplayLore> priorLore() {
      List l = new ArrayList(this.displayLore);
      l.add(0, l.remove(this.item));
      return l;
   }

   public List<LoreFormatter.StaticLore> getItemLore() {
      return this.itemLore;
   }

   private LoreFormatter.DynamicLoreList getDynamicLoreList(List<String> list, String def, String... ph) {
      return (LoreFormatter.DynamicLoreList)(this.components ? new LoreFormatter.DynamicCompLoreList(list, def, ph) : new LoreFormatter.DynamicBukkitLoreList(list, def, ph));
   }

   private List<String> getStringList(ConfigurationSection config, String key) {
      return (List)(config.get(key) instanceof List ? config.getStringList(key) : new ArrayList(Collections.singletonList(config.getString(key))));
   }

   private boolean isEmpty(List<String> list) {
      return list.stream().allMatch(String::isEmpty);
   }

   private ItemStack parseDisplayname(ItemStack item, Player player, ShopItem shopItem, boolean discounts, boolean multipliers, boolean seasonal, boolean fast, boolean pr, int page, int pages) {
      if (!this.components && fast && !pr) {
         return item;
      } else {
         ItemMeta meta = item.getItemMeta();
         String translated = this.plugin.getMetaUtils().getRawDisplayName(meta);
         this.plugin.getMetaUtils().setRawDisplayName(meta, translated);
         item.setItemMeta(meta);
         return item;
      }
   }

   private Replacement[] pricePlaceholders(ShopItem shopItem, Player p, boolean discounts, boolean multipliers, boolean seasonal, int page, int pages) {
      Replacement[] var10000 = new Replacement[]{new Replacement("%buyPrice%", () -> {
         return this.plugin.formatPrice(shopItem.getEcoType(), (!discounts && !seasonal ? shopItem.getBuyPrice() : shopItem.getBuyPrice(p)) * (double)shopItem.getStackSize());
      }), new Replacement("%sellPrice%", () -> {
         return this.plugin.formatPrice(shopItem.getEcoType(), (!multipliers && !seasonal ? shopItem.getSellPrice() : shopItem.getSellPrice(p)) * (double)shopItem.getStackSize());
      }), new Replacement("%buyPriceRaw%", () -> {
         return String.valueOf((!discounts && !seasonal ? shopItem.getBuyPrice() : shopItem.getBuyPrice(p)) * (double)shopItem.getStackSize());
      }), new Replacement("%sellPriceRaw%", () -> {
         return String.valueOf((!multipliers && !seasonal ? shopItem.getSellPrice() : shopItem.getSellPrice(p)) * (double)shopItem.getStackSize());
      }), null, null, null, null, null, null};
      Objects.requireNonNull(p);
      var10000[4] = new Replacement("%player_name%", p::getName);
      var10000[5] = new Replacement("%player_level%", () -> {
         return String.valueOf(p.getLevel());
      });
      var10000[6] = new Replacement("%player_displayname%", () -> {
         return this.plugin.getDisplayName(p);
      });
      var10000[7] = new Replacement("%player_balance%", () -> {
         return this.plugin.formatPrice(shopItem.getEcoType(), this.plugin.getEcoHandler().getEcon(shopItem.getEcoType()).getBalance(p));
      });
      var10000[8] = new Replacement("%page%", () -> {
         return String.valueOf(page);
      });
      var10000[9] = new Replacement("%pages%", () -> {
         return String.valueOf(pages);
      });
      return var10000;
   }

   private String[] parseLocal(ShopItem shopItem, Player player, boolean discounts, boolean multipliers, boolean seasonal, boolean fast, boolean pr, int page, int pages) {
      String[] lore = shopItem.getLore().get(fast, pr);
      Iterator var11 = shopItem.getPlaceholders().iterator();

      while(var11.hasNext()) {
         Integer i = (Integer)var11.next();
         if (i != -1) {
            lore[i] = this.replaceLocal(lore[i], this.pricePlaceholders(shopItem, player, discounts, multipliers, seasonal, page, pages));
         }
      }

      return lore;
   }

   private Replacement[] actionPlaceholders(ActionItem actionItem, Player p, EcoType type, int page, int pages) {
      Replacement[] var10000 = new Replacement[6];
      Objects.requireNonNull(p);
      var10000[0] = new Replacement("%player_name%", p::getName);
      var10000[1] = new Replacement("%player_level%", () -> {
         return String.valueOf(p.getLevel());
      });
      var10000[2] = new Replacement("%player_displayname%", () -> {
         return this.plugin.getDisplayName(p);
      });
      var10000[3] = new Replacement("%player_balance%", () -> {
         return this.plugin.formatPrice(type, this.plugin.getEcoHandler().getEcon(type).getBalance(p));
      });
      var10000[4] = new Replacement("%page%", () -> {
         return String.valueOf(page);
      });
      var10000[5] = new Replacement("%pages%", () -> {
         return String.valueOf(pages);
      });
      return var10000;
   }

   private String[] parseLocal(ActionItem actionItem, Player player, EcoType type, boolean fast, boolean pr, int page, int pages) {
      String[] lore = actionItem.getLore().get(fast, pr);
      Iterator var9 = actionItem.getPlaceholders().iterator();

      while(var9.hasNext()) {
         Integer i = (Integer)var9.next();
         if (i != -1) {
            lore[i] = this.replaceLocal(lore[i], this.actionPlaceholders(actionItem, player, type, page, pages));
         }
      }

      return lore;
   }

   private String replaceLocal(String s, Replacement[] replacements) {
      Replacement[] var3 = replacements;
      int var4 = replacements.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Replacement replacement = var3[var5];
         if (s.contains(replacement.key)) {
            s = s.replace(replacement.key, (CharSequence)replacement.value.get());
         }
      }

      return s;
   }

   private String translate(String s) {
      if (s == null) {
         return "";
      } else {
         if (s.contains("%translations-")) {
            try {
               Lang placeholder = Lang.valueOf(s.split("%translations-")[1].split("%")[0].toUpperCase(Locale.ENGLISH).replace("-", "_"));
               s = s.replace("%translations-" + placeholder.getKey() + "%", placeholder.getRaw());
            } catch (IllegalArgumentException var3) {
            }
         }

         return this.components ? ChatUtil.getAdventureUtils().formatLegacyToMini(s) : ChatUtil.formatColors(s);
      }
   }

   private List<String> translate(List<String> list) {
      if (list.isEmpty()) {
         return list;
      } else {
         List<String> lore = new ArrayList();
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            String s = (String)var3.next();
            if (s.contains("%translations-")) {
               try {
                  Lang placeholder = Lang.valueOf(s.split("%translations-")[1].split("%")[0].toUpperCase(Locale.ENGLISH).replace("-", "_"));
                  s = s.replace("%translations-" + placeholder.getKey() + "%", placeholder.getRaw().replace("\\n", "\n"));
               } catch (IllegalArgumentException var9) {
               }
            }

            String[] var10 = s.split("\n");
            int var6 = var10.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String e = var10[var7];
               lore.add(this.components ? ChatUtil.getAdventureUtils().formatLegacyToMini(e) : ChatUtil.formatColors(e));
            }
         }

         return lore;
      }
   }

   private interface DynamicLoreList {
      String[] get(boolean var1, boolean var2, Player var3, String... var4);
   }

   private final class SpecialLore implements LoreFormatter.DisplayLore {
      private final LoreFormatter.DynamicLoreList seasonalPrice;
      private final boolean after;

      public SpecialLore(ConfigurationSection param2, boolean param3) {
         this.after = after;
         this.seasonalPrice = LoreFormatter.this.getDynamicLoreList(LoreFormatter.this.getStringList(section, "seasonal-pricing"), Lang.ITEM_SEASONAL_PRICE.get().toString(), "%season-icon%", "%season-name%");
      }

      public void apply(ShopItem shopItem, Player p, LoreFormatter.Lore lore, String section, boolean fast, boolean pr, boolean... data) {
         if (data[0]) {
            lore.addAll(this.seasonalPrice.get(fast, pr, p, "%season-icon%", SeasonProvider.getSeason(p.getWorld().getName()).getIcon(), "%season-name%", SeasonProvider.getSeason(p.getWorld().getName()).getName()));
         }

      }
   }

   private final class RequirementLore implements LoreFormatter.DisplayLore {
      public RequirementLore() {
      }

      public void apply(ShopItem shopItem, Player p, LoreFormatter.Lore lore, String section, boolean fast, boolean pr, boolean... data) {
      }
   }

   private final class BuyLore implements LoreFormatter.DisplayLore {
      private final LoreFormatter.DynamicLoreList disabled;
      private final LoreFormatter.DynamicLoreList undefined;
      private final LoreFormatter.DynamicLoreList buyPrices;
      private final LoreFormatter.DynamicLoreList seasonalBuyPrices;
      private final LoreFormatter.DynamicLoreList discountedBuyPrices;
      private final boolean after;

      public BuyLore(ConfigurationSection param2, boolean param3) {
         this.after = after;
         this.disabled = LoreFormatter.this.getDynamicLoreList(section.getStringList("disabled"), Lang.ITEM_CANNOT_BE_BOUGHT.get().toString());
         this.undefined = LoreFormatter.this.getDynamicLoreList(section.getStringList("undefined"), "");
         this.buyPrices = LoreFormatter.this.getDynamicLoreList(section.getStringList("normal"), Lang.LEFT_CLICK_BUY.get().toString(), "%buyPrice%", "%buyPriceRaw%");
         this.seasonalBuyPrices = LoreFormatter.this.getDynamicLoreList(section.getStringList("seasonal"), Lang.SEASONAL_BUY_PRICE.get().toString(), "%buyPrice%", "%buyPriceRaw%", "%seasonalBuyPrice%");
         this.discountedBuyPrices = LoreFormatter.this.getDynamicLoreList(section.getStringList("discounted"), Lang.DISCOUNTED_BUY_PRICE.get().toString(), "%buyPrice%", "%buyPriceRaw%", "%discountedPrice%");
      }

      public void apply(ShopItem shopItem, Player p, LoreFormatter.Lore lore, String section, boolean fast, boolean pr, boolean... data) {
         if (shopItem.isBuyLore()) {
            if (shopItem.getBuyPrice() >= 0.0D) {
               if (data[2]) {
                  lore.addAll(this.seasonalBuyPrices.get(fast, pr, p, "%buyPrice%", ChatColor.stripColor(LoreFormatter.this.plugin.formatPrice(shopItem.getEcoType(), shopItem.getBuyPrice() * (double)shopItem.getStackSize())), "%buyPriceRaw%", String.valueOf(shopItem.getBuyPrice() * (double)shopItem.getStackSize()), "%seasonalBuyPrice%", LoreFormatter.this.plugin.formatPrice(shopItem.getEcoType(), shopItem.getBuyPrice(p) * (double)shopItem.getStackSize())));
               } else if (data[0] && CalculateAmount.calculateDiscount(p, section, shopItem.getBuyPrice()) != shopItem.getBuyPrice()) {
                  lore.addAll(this.discountedBuyPrices.get(fast, pr, p, "%buyPrice%", ChatColor.stripColor(LoreFormatter.this.plugin.formatPrice(shopItem.getEcoType(), shopItem.getBuyPrice() * (double)shopItem.getStackSize())), "%buyPriceRaw%", String.valueOf(shopItem.getBuyPrice() * (double)shopItem.getStackSize()), "%discountedPrice%", LoreFormatter.this.plugin.formatPrice(shopItem.getEcoType(), shopItem.getBuyPrice(p) * (double)shopItem.getStackSize())));
               } else {
                  lore.addAll(this.buyPrices.get(fast, pr, p, "%buyPrice%", LoreFormatter.this.plugin.formatPrice(shopItem.getEcoType(), shopItem.getBuyPrice() * (double)shopItem.getStackSize()), "%buyPriceRaw%", String.valueOf(shopItem.getBuyPrice() * (double)shopItem.getStackSize())));
               }
            } else {
               lore.addAll(this.disabled.get(fast, pr, p));
            }
         } else {
            lore.addAll(this.undefined.get(fast, pr, p));
         }

      }
   }

   private final class SellLore implements LoreFormatter.DisplayLore {
      private final LoreFormatter.DynamicLoreList disabled;
      private final LoreFormatter.DynamicLoreList undefined;
      private final LoreFormatter.DynamicLoreList sellPrices;
      private final LoreFormatter.DynamicLoreList seasonalSellPrices;
      private final LoreFormatter.DynamicLoreList multipliedSellPrices;
      private final LoreFormatter.DynamicLoreList sellAllLore;
      private final boolean after;

      public SellLore(ConfigurationSection param2, boolean param3) {
         this.after = after;
         this.disabled = LoreFormatter.this.getDynamicLoreList(section.getStringList("disabled"), Lang.ITEM_CANNOT_BE_SOLD.get().toString());
         this.undefined = LoreFormatter.this.getDynamicLoreList(section.getStringList("undefined"), "");
         this.sellPrices = LoreFormatter.this.getDynamicLoreList(section.getStringList("normal"), Lang.RIGHT_CLICK_SELL.get().toString(), "%sellPrice%", "%sellPriceRaw%");
         this.seasonalSellPrices = LoreFormatter.this.getDynamicLoreList(section.getStringList("seasonal"), Lang.SEASONAL_SELL_PRICE.get().toString(), "%sellPrice%", "%sellPriceRaw%", "%seasonalSellPrice%");
         this.multipliedSellPrices = LoreFormatter.this.getDynamicLoreList(section.getStringList("multiplied"), Lang.MULTIPLIED_SELL_PRICE.get().toString(), "%sellPrice%", "%sellPriceRaw%", "%multipliedPrice%");
         this.sellAllLore = LoreFormatter.this.getDynamicLoreList(section.getStringList("sell-all"), Lang.SHIFT_RIGHT_CLICK_SELL_ALL.get().toString());
      }

      public void apply(ShopItem shopItem, Player p, LoreFormatter.Lore lore, String section, boolean fast, boolean pr, boolean... data) {
         if (shopItem.isSellLore()) {
            if (shopItem.getSellPrice() >= 0.0D) {
               if (data[2]) {
                  lore.addAll(this.seasonalSellPrices.get(fast, pr, p, "%sellPrice%", ChatColor.stripColor(LoreFormatter.this.plugin.formatPrice(shopItem.getEcoType(), shopItem.getSellPrice() * (double)shopItem.getStackSize())), "%sellPriceRaw%", String.valueOf(shopItem.getSellPrice(p) * (double)shopItem.getStackSize()), "%seasonalSellPrice%", LoreFormatter.this.plugin.formatPrice(shopItem.getEcoType(), shopItem.getSellPrice(p) * (double)shopItem.getStackSize())));
               } else if (data[1] && CalculateAmount.calculateMultiplier(p, section, shopItem.getSellPrice()) != shopItem.getSellPrice()) {
                  lore.addAll(this.multipliedSellPrices.get(fast, pr, p, "%sellPrice%", ChatColor.stripColor(LoreFormatter.this.plugin.formatPrice(shopItem.getEcoType(), shopItem.getSellPrice() * (double)shopItem.getStackSize())), "%sellPriceRaw%", String.valueOf(shopItem.getSellPrice(p) * (double)shopItem.getStackSize()), "%multipliedPrice%", LoreFormatter.this.plugin.formatPrice(shopItem.getEcoType(), shopItem.getSellPrice(p) * (double)shopItem.getStackSize())));
               } else {
                  lore.addAll(this.sellPrices.get(fast, pr, p, "%sellPrice%", LoreFormatter.this.plugin.formatPrice(shopItem.getEcoType(), shopItem.getSellPrice() * (double)shopItem.getStackSize()), "%sellPriceRaw%", String.valueOf(shopItem.getSellPrice() * (double)shopItem.getStackSize())));
               }

               if (LoreFormatter.this.plugin.MMB) {
                  lore.addAll(this.sellAllLore.get(fast, pr, p));
               }
            } else {
               lore.addAll(this.disabled.get(fast, pr, p));
            }
         } else {
            lore.addAll(this.undefined.get(fast, pr, p));
         }

      }
   }

   private final class DecorationLore implements LoreFormatter.DisplayLore {
      private final LoreFormatter.DynamicLoreList lore;
      private final LoreFormatter.DynamicLoreList buyLore;
      private final LoreFormatter.DynamicLoreList sellLore;
      private final LoreFormatter.DynamicLoreList sellAllLore;

      public DecorationLore(ConfigurationSection param2) {
         this.lore = LoreFormatter.this.getDynamicLoreList(section.getStringList("extra-lore"), "");
         this.buyLore = LoreFormatter.this.getDynamicLoreList(section.getStringList("extra-buy-lore"), "");
         this.sellLore = LoreFormatter.this.getDynamicLoreList(section.getStringList("extra-sell-lore"), "");
         this.sellAllLore = LoreFormatter.this.getDynamicLoreList(section.getStringList("extra-sell-all-lore"), "");
      }

      public void apply(ShopItem shopItem, Player p, LoreFormatter.Lore lore, String section, boolean fast, boolean pr, boolean... data) {
         if (shopItem.isDecorationLore()) {
            lore.addAll(this.lore.get(fast, pr, p));
            if (shopItem.getBuyPrice() >= 0.0D && shopItem.isBuyLore()) {
               lore.addAll(this.buyLore.get(fast, pr, p));
            }

            if (shopItem.getSellPrice() >= 0.0D && shopItem.isSellLore()) {
               if (LoreFormatter.this.plugin.MMB) {
                  lore.addAll(this.sellAllLore.get(fast, pr, p));
               }

               lore.addAll(this.sellLore.get(fast, pr, p));
            }
         }

      }
   }

   private final class ItemLore implements LoreFormatter.DisplayLore {
      public ItemLore() {
      }

      public void apply(ShopItem shopItem, Player p, LoreFormatter.Lore lore, String section, boolean fast, boolean pr, boolean... data) {
      }
   }

   private final class AdminLore implements LoreFormatter.DisplayLore {
      private final LoreFormatter.DynamicLoreList itemIndex;
      private final boolean after;

      public AdminLore(ConfigurationSection param2, boolean param3) {
         this.after = after;
         this.itemIndex = LoreFormatter.this.getDynamicLoreList(LoreFormatter.this.getStringList(section, "item-index"), Lang.ITEM_LOCATION_IN_SHOP.get().toString(), "%shopsection%", "%itemLoc%");
      }

      public void apply(ShopItem shopItem, Player p, LoreFormatter.Lore lore, String section, boolean fast, boolean pr, boolean... data) {
         if (data[0]) {
            lore.addAll(this.itemIndex.get(fast, pr, p, "%shopsection%", section, "%itemLoc%", shopItem.itemLoc));
         }

      }
   }

   public final class StaticLore {
      public final LoreFormatter.LoreType type;
      public final List<String> lore;

      public StaticLore(LoreFormatter.LoreType param2, String[] param3) {
         this.type = type;
         this.lore = lore == null && type == LoreFormatter.LoreType.ITEM_LORE ? null : LoreFormatter.this.translate(type != LoreFormatter.LoreType.BUY_LORE && type != LoreFormatter.LoreType.SELL_LORE ? Arrays.asList(lore) : (List)Arrays.stream(lore).map((s) -> {
            return s.replace("%dpp%", "");
         }).collect(Collectors.toList()));
      }

      public StaticLore(LoreFormatter.LoreType param2, List<String> param3) {
         this.type = type;
         this.lore = lore == null && type == LoreFormatter.LoreType.ITEM_LORE ? null : (type != LoreFormatter.LoreType.BUY_LORE && type != LoreFormatter.LoreType.SELL_LORE ? LoreFormatter.this.translate(lore) : (List)LoreFormatter.this.translate(lore).stream().map((s) -> {
            return s.replace("%dpp%", "");
         }).collect(Collectors.toList()));
      }
   }

   public static enum LoreType {
      BUY_LORE,
      SELL_LORE,
      SELL_ALL_LORE,
      DECORATION_LORE,
      DECORATION_LORE_BUY,
      DECORATION_LORE_SELL,
      DECORATION_LORE_SELL_ALL,
      ITEM_LORE;

      // $FF: synthetic method
      private static LoreFormatter.LoreType[] $values() {
         return new LoreFormatter.LoreType[]{BUY_LORE, SELL_LORE, SELL_ALL_LORE, DECORATION_LORE, DECORATION_LORE_BUY, DECORATION_LORE_SELL, DECORATION_LORE_SELL_ALL, ITEM_LORE};
      }
   }

   private final class ActionAdminLore {
      private final LoreFormatter.DynamicLoreList itemIndex;

      public ActionAdminLore(ConfigurationSection param2) {
         this.itemIndex = LoreFormatter.this.getDynamicLoreList(LoreFormatter.this.getStringList(section, "item-index"), Lang.ITEM_LOCATION_IN_SHOP.get().toString(), "%shopsection%", "%itemLoc%");
      }

      public void apply(ActionItem shopItem, Player p, LoreFormatter.Lore lore, String section, boolean fast, boolean pr, boolean... data) {
         if (data[0]) {
            lore.addAll(this.itemIndex.get(fast, pr, p, "%shopsection%", section, "%itemLoc%", shopItem.itemLoc));
         }

      }
   }

   private final class ComponentLore implements LoreFormatter.Lore {
      private final ArrayList<String> lore = new ArrayList();
      private final ItemStack item;

      public ComponentLore(ItemStack param2) {
         this.item = item;
      }

      public void addAll(String[] list) {
         this.lore.addAll(Arrays.asList(list));
      }

      public void add(String o) {
         this.lore.add(o);
      }

      public ItemStack form() {
         try {
            return PaperMeta.setLore(this.item, this.lore);
         } catch (Exception var2) {
            SendMessage.warnMessage("Failed to display component lore on shop item");
            var2.printStackTrace();
            return null;
         }
      }
   }

   private final class RawComponentLore implements LoreFormatter.Lore {
      private final ArrayList<String> lore = new ArrayList();
      private final ItemStack item;

      public RawComponentLore(ItemStack param2) {
         this.item = item;
      }

      public void addAll(String[] list) {
         this.lore.addAll(Arrays.asList(list));
      }

      public void add(String o) {
         this.lore.add(o);
      }

      public ItemStack form() {
         try {
            return PaperMeta.setRawLore(this.item, this.lore);
         } catch (Exception var2) {
            SendMessage.warnMessage("Failed to display component lore on shop item");
            var2.printStackTrace();
            return null;
         }
      }
   }

   private final class FastLore implements LoreFormatter.Lore {
      private final Object lore;
      private final ItemStack stack;

      public FastLore(ItemStack param2) {
         this.lore = LoreFormatter.this.plugin.versionHandler.emptyList();
         this.stack = item;
      }

      public void add(String s) {
         LoreFormatter.this.plugin.versionHandler.addItemLore(this.lore, s);
      }

      public void add(String s, int i) {
         LoreFormatter.this.plugin.versionHandler.addItemLore(this.lore, s, i);
      }

      public void addAll(String[] list) {
         String[] var2 = list;
         int var3 = list.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            this.add(s);
         }

      }

      public ItemStack form() {
         return LoreFormatter.this.plugin.versionHandler.applyNMSLore(this.stack, this.lore);
      }
   }

   private final class ShopLore implements LoreFormatter.Lore {
      private final List<String> lore = new ArrayList();
      private final ItemStack stack;

      public ShopLore(ItemStack param2) {
         this.stack = item;
      }

      public void add(String s) {
         this.lore.add(s);
      }

      public void add(String s, int i) {
         this.lore.add(i, s);
      }

      public void addAll(String[] list) {
         String[] var2 = list;
         int var3 = list.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String s = var2[var4];
            this.add(s);
         }

      }

      public ItemStack form() {
         ItemMeta meta = this.stack.getItemMeta();
         meta.setLore(this.lore);
         this.stack.setItemMeta(meta);
         return this.stack;
      }
   }

   private interface Lore {
      void addAll(String[] var1);

      void add(String var1);

      ItemStack form();
   }

   private interface DisplayLore {
      void apply(ShopItem var1, Player var2, LoreFormatter.Lore var3, String var4, boolean var5, boolean var6, boolean... var7);
   }

   private final class DynamicCompLoreList implements LoreFormatter.DynamicLoreList {
      private final Map<Integer, String> placeholders = new HashMap();
      private final String[] lore;
      private final String[] compLore;

      public DynamicCompLoreList(List<String> param2, String param3, String... param4) {
         this.lore = list != null ? (!LoreFormatter.this.isEmpty(list) ? (String[])LoreFormatter.this.translate(list).toArray(new String[list.size()]) : new String[0]) : LoreFormatter.this.translate(def).split("\n");
         this.compLore = (String[])Arrays.stream(this.lore).map(ChatUtil::getGsonComponent).toArray((x$0) -> {
            return new String[x$0];
         });
         int i = 0;
         String[] var6 = this.lore;
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String s = var6[var8];
            Stream var10000 = Stream.of(ph);
            Objects.requireNonNull(s);
            if (var10000.anyMatch(s::contains)) {
               this.placeholders.put(i, s);
            }

            ++i;
         }

      }

      public String[] get(boolean fast, boolean pr, Player p, String... replacements) {
         String[] clone = (String[])(fast ? this.compLore : this.lore).clone();
         Iterator var6 = this.placeholders.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<Integer, String> ph = (Entry)var6.next();

            for(int i = 0; i < replacements.length; i += 2) {
               if (((String)ph.getValue()).contains(replacements[i])) {
                  clone[(Integer)ph.getKey()] = clone[(Integer)ph.getKey()].replace(replacements[i], replacements[i + 1]);
               }
            }
         }

         return clone;
      }
   }

   private final class DynamicBukkitLoreList implements LoreFormatter.DynamicLoreList {
      private final Map<Integer, String> placeholders = new HashMap();
      private final String[] lore;
      private final String[] fastLore;

      public DynamicBukkitLoreList(List<String> param2, String param3, String... param4) {
         this.lore = list != null ? (!LoreFormatter.this.isEmpty(list) ? (String[])LoreFormatter.this.translate(list).toArray(new String[list.size()]) : new String[0]) : LoreFormatter.this.translate(def).split("\n");
         this.fastLore = (String[])Arrays.stream(this.lore).map((sx) -> {
            return LoreFormatter.this.plugin.versionHandler.toNBT(sx);
         }).toArray((x$0) -> {
            return new String[x$0];
         });
         int i = 0;
         String[] var6 = this.lore;
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String s = var6[var8];
            Stream var10000 = Stream.of(ph);
            Objects.requireNonNull(s);
            if (var10000.anyMatch(s::contains)) {
               this.placeholders.put(i, s);
            }

            ++i;
         }

      }

      public String[] get(boolean fast, boolean pr, Player p, String... replacements) {
         String[] clone = (String[])(fast && !pr ? this.fastLore : this.lore).clone();
         Iterator var6 = this.placeholders.entrySet().iterator();

         while(var6.hasNext()) {
            Entry<Integer, String> ph = (Entry)var6.next();

            for(int i = 0; i < replacements.length; i += 2) {
               if (clone[(Integer)ph.getKey()].contains(replacements[i])) {
                  clone[(Integer)ph.getKey()] = clone[(Integer)ph.getKey()].replace(replacements[i], replacements[i + 1]);
               }
            }
         }

         return clone;
      }
   }
}
