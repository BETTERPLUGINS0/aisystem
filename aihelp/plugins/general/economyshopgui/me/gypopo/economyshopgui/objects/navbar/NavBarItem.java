package me.gypopo.economyshopgui.objects.navbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.CreateItemMethodes;
import me.gypopo.economyshopgui.providers.UserManager;
import me.gypopo.economyshopgui.util.EcoType;
import me.gypopo.economyshopgui.util.Replacement;
import me.gypopo.economyshopgui.util.SkullUtil;
import me.gypopo.economyshopgui.util.XMaterial;
import me.gypopo.economyshopgui.util.exceptions.ItemLoadException;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class NavBarItem implements NavItem {
   private ItemStack navBarItem;
   private String action = null;
   private boolean staticItem = true;
   private boolean onlyGeyser;
   private boolean disabled;
   private NavBarItem.Paginated paginatedItem;
   private List<Integer> placeholders;

   public String getAction() {
      return this.action;
   }

   public String getAction(int page, int allPages) {
      return this.paginatedItem == null || allPages != 1 && (!this.action.equals("PAGE_BACK") || page != 1) && (!this.action.equals("PAGE_NEXT") || page != allPages) ? this.action : this.paginatedItem.getA();
   }

   public boolean isDisabled() {
      return this.disabled;
   }

   public NavBarItem(ItemStack item, String action) {
      this.navBarItem = item;
      this.action = action;
   }

   public NavBarItem(ConfigurationSection config, ItemStack fillItem) throws ItemLoadException {
      ItemStack item = CreateItemMethodes.createItemMaterialFromString(config.getString("material"));
      if (item.getType().equals(Material.AIR)) {
         this.disabled = true;
      } else {
         ItemMeta meta = item.getItemMeta();
         String name;
         if (item.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())) {
            name = config.getString("skullowner");
            if (name != null) {
               SkullUtil.setSkullTexture(item, (SkullMeta)meta, name, true);
            }
         }

         name = config.getString("name");
         if (name != null) {
            Translatable displayname = this.replaceTranslations(name);
            EconomyShopGUI.getInstance().getMetaUtils().setDisplayName(meta, displayname);
         }

         List<String> l = config.getStringList("lore");
         if (!l.isEmpty()) {
            List<Translatable> lore = (List)l.stream().map(this::replaceTranslations).collect(Collectors.toList());
            EconomyShopGUI.getInstance().getMetaUtils().setLore(meta, lore);
         }

         item.setItemMeta(meta);
         this.placeholders = this.getPlaceholders(item);
         if (!this.placeholders.isEmpty()) {
            this.staticItem = false;
         }

         if (config.getBoolean("paginated.enabled")) {
            ConfigurationSection section = config.getConfigurationSection("paginated.orElse");
            if (section != null && !section.getKeys(false).isEmpty()) {
               this.paginatedItem = new NavBarItem.Paginated(section);
            } else {
               this.paginatedItem = new NavBarItem.Paginated(fillItem);
            }

            this.staticItem = false;
         }

         this.action = this.getAction(config, fillItem);
         this.navBarItem = item;
      }
   }

   private Translatable replaceTranslations(String s) {
      return Lang.fromConfig(s.replace("%translations-previous-page%", Lang.PREVIOUS_PAGE.get().toString()).replace("%translations-current-page%", Lang.CURRENT_PAGE.get().getLegacy().contains("%page%") ? Lang.CURRENT_PAGE.get().toString() : Lang.CURRENT_PAGE.get().builder().append(" %page%/%pages%").build().toString()).replace("%translations-next-page%", Lang.NEXT_PAGE.get().toString()).replace("%translations-money%", Lang.MONEY.get().toString()).replace("%translations-cancel%", Lang.CANCEL.get().toString()).replace("%translations-back%", Lang.BACK.get().toString()));
   }

   private String getAction(ConfigurationSection section, ItemStack fillItem) {
      if (!section.contains("action")) {
         return "NONE";
      } else {
         String var3 = section.getString("action").toUpperCase(Locale.ENGLISH);
         byte var4 = -1;
         switch(var3.hashCode()) {
         case -595485545:
            if (var3.equals("PAGE_BACK")) {
               var4 = 0;
            }
            break;
         case -595123549:
            if (var3.equals("PAGE_NEXT")) {
               var4 = 1;
            }
            break;
         case 2030823:
            if (var3.equals("BACK")) {
               var4 = 4;
            }
            break;
         case 64218584:
            if (var3.equals("CLOSE")) {
               var4 = 3;
            }
            break;
         case 1536798638:
            if (var3.equals("TOGGLE_MODE")) {
               var4 = 2;
            }
         }

         switch(var4) {
         case 0:
         case 1:
            if (this.paginatedItem == null && section.getBoolean("paginated.enabled", true)) {
               this.paginatedItem = new NavBarItem.Paginated(fillItem);
               this.staticItem = false;
            }
         case 2:
            this.onlyGeyser = section.getBoolean("onlyEnableForGeyserPlayers", true);
         case 3:
         case 4:
            return section.getString("action").toUpperCase(Locale.ENGLISH);
         default:
            return "NONE";
         }
      }
   }

   public ItemStack getItem(Player p, EcoType type, int page, int allpages) {
      if (this.staticItem) {
         return this.navBarItem;
      } else if (this.paginatedItem != null && (allpages == 1 || this.action.equals("PAGE_BACK") && page == 1 || this.action.equals("PAGE_NEXT") && page == allpages)) {
         return this.paginatedItem.getItem(p, type, page, allpages);
      } else {
         ItemStack item = new ItemStack(this.navBarItem);
         if (!item.hasItemMeta()) {
            return item;
         } else {
            ItemMeta meta = item.getItemMeta();
            if (this.placeholders.contains(-1)) {
               EconomyShopGUI.getInstance().getMetaUtils().setRawDisplayName(meta, this.replacePlaceHolders(p, EconomyShopGUI.getInstance().getMetaUtils().getRawDisplayName(meta), type, page, allpages));
            }

            if (meta.hasLore()) {
               List<String> lore = EconomyShopGUI.getInstance().getMetaUtils().getRawLore(meta);
               Iterator var8 = this.placeholders.iterator();

               while(var8.hasNext()) {
                  Integer i = (Integer)var8.next();
                  if (i != -1) {
                     lore.set(i, this.replacePlaceHolders(p, (String)lore.get(i), type, page, allpages));
                  }
               }

               EconomyShopGUI.getInstance().getMetaUtils().setRawLore(meta, lore);
            }

            item.setItemMeta(meta);
            return item;
         }
      }
   }

   public ItemStack getItem(Player p, EcoType type) {
      if (this.onlyGeyser && !UserManager.getUser(p).isBedrock()) {
         return new ItemStack(Material.AIR);
      } else if (this.staticItem) {
         return this.navBarItem;
      } else {
         ItemStack item = new ItemStack(this.navBarItem);
         if (!item.hasItemMeta()) {
            return item;
         } else {
            ItemMeta meta = item.getItemMeta();
            if (this.placeholders.contains(-1)) {
               EconomyShopGUI.getInstance().getMetaUtils().setRawDisplayName(meta, this.replacePlaceHolders(p, EconomyShopGUI.getInstance().getMetaUtils().getRawDisplayName(meta), type, 0, 0));
            }

            if (meta.hasLore()) {
               List<String> lore = EconomyShopGUI.getInstance().getMetaUtils().getRawLore(meta);
               Iterator var6 = this.placeholders.iterator();

               while(var6.hasNext()) {
                  Integer i = (Integer)var6.next();
                  if (i != -1) {
                     lore.set(i, this.replacePlaceHolders(p, (String)lore.get(i), type, 0, 0));
                  }
               }

               EconomyShopGUI.getInstance().getMetaUtils().setRawLore(meta, lore);
            }

            item.setItemMeta(meta);
            return item;
         }
      }
   }

   private boolean hasPlaceholder(String s) {
      return s.contains("%player_name%") || s.contains("player_displayname") || s.contains("%player_level%") || s.contains("%player_balance%") || s.contains("%page%") || s.contains("%pages%");
   }

   private List<Integer> getPlaceholders(ItemStack item) {
      if (item.hasItemMeta() && (item.getItemMeta().hasDisplayName() || item.getItemMeta().hasLore())) {
         ItemMeta meta = item.getItemMeta();
         List<Integer> placeholders = new ArrayList();
         if (meta.hasDisplayName()) {
            String displayName = EconomyShopGUI.getInstance().getMetaUtils().getRawDisplayName(meta);
            if (this.hasPlaceholder(displayName)) {
               placeholders.add(-1);
            }
         }

         if (meta.hasLore()) {
            int i = 0;

            for(Iterator var5 = EconomyShopGUI.getInstance().getMetaUtils().getRawLore(meta).iterator(); var5.hasNext(); ++i) {
               String s = (String)var5.next();
               if (this.hasPlaceholder(s)) {
                  placeholders.add(i);
               }
            }
         }

         return placeholders;
      } else {
         return Collections.EMPTY_LIST;
      }
   }

   private Replacement[] getReplacements(Player p, EcoType type, int page, int pages) {
      Replacement[] var10000 = new Replacement[6];
      Objects.requireNonNull(p);
      var10000[0] = new Replacement("%player_name%", p::getName);
      var10000[1] = new Replacement("%player_displayname%", () -> {
         return EconomyShopGUI.getInstance().getDisplayName(p);
      });
      var10000[2] = new Replacement("%player_level%", () -> {
         return String.valueOf(p.getLevel());
      });
      var10000[3] = new Replacement("%player_balance%", () -> {
         return EconomyShopGUI.getInstance().formatPrice(type, EconomyShopGUI.getInstance().getEcoHandler().getEcon(type).getBalance(p));
      });
      var10000[4] = new Replacement("%page%", () -> {
         return String.valueOf(page);
      });
      var10000[5] = new Replacement("%pages%", () -> {
         return String.valueOf(pages);
      });
      return var10000;
   }

   private String replacePlaceHolders(Player p, String s, EcoType type, int page, int pages) {
      Replacement[] var6 = this.getReplacements(p, type, page, pages);
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Replacement replacement = var6[var8];
         if (s.contains(replacement.key)) {
            s = s.replace(replacement.key, (CharSequence)replacement.value.get());
         }
      }

      return s;
   }

   public class Paginated {
      private ItemStack orElse;
      private String action = null;
      private String skullOwner;
      private boolean staticItem = true;
      private boolean disabled = false;
      private List<Integer> placeholders;

      public Paginated(ItemStack param2) {
         if (fillItem == null) {
            this.disabled = true;
            this.orElse = new ItemStack(Material.AIR);
         } else {
            this.orElse = fillItem;
         }

      }

      public Paginated(ConfigurationSection param2) throws ItemLoadException {
         ItemStack item = CreateItemMethodes.createItemMaterialFromString(config.getString("material"));
         if (item.getType().equals(Material.AIR)) {
            this.disabled = true;
         } else {
            if (item.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())) {
               String owner = config.getString("skullowner");
               if (owner != null && !owner.equals("%player_name%")) {
                  this.skullOwner = owner;
                  this.staticItem = false;
               }
            }

            ItemMeta meta = item.getItemMeta();
            String name = config.getString("name");
            if (name != null) {
               Translatable displayname = NavBarItem.this.replaceTranslations(name);
               EconomyShopGUI.getInstance().getMetaUtils().setDisplayName(meta, displayname);
            }

            List<String> l = config.getStringList("lore");
            if (!l.isEmpty()) {
               List<Translatable> lore = (List)l.stream().map((x$0) -> {
                  return NavBarItem.this.replaceTranslations(x$0);
               }).collect(Collectors.toList());
               EconomyShopGUI.getInstance().getMetaUtils().setLore(meta, lore);
            }

            item.setItemMeta(meta);
            this.placeholders = NavBarItem.this.getPlaceholders(item);
            if (!this.placeholders.isEmpty()) {
               this.staticItem = false;
            }

            this.action = NavBarItem.this.getAction(config, (ItemStack)null);
            this.orElse = item;
         }
      }

      public boolean isDisabled() {
         return this.disabled;
      }

      public String getA() {
         return this.action;
      }

      public ItemStack getItem(Player p, EcoType type, int page, int allpages) {
         if (this.staticItem) {
            return this.orElse;
         } else {
            ItemStack item = new ItemStack(this.orElse);
            if (!item.hasItemMeta()) {
               return item;
            } else {
               if (item.getType().equals(XMaterial.PLAYER_HEAD.parseMaterial())) {
                  SkullMeta skullMeta = (SkullMeta)item.getItemMeta();
                  skullMeta.setOwner(this.skullOwner == null ? p.getName() : this.skullOwner);
                  item.setItemMeta(skullMeta);
               }

               ItemMeta meta = item.getItemMeta();
               if (this.placeholders.contains(-1)) {
                  EconomyShopGUI.getInstance().getMetaUtils().setRawDisplayName(meta, NavBarItem.this.replacePlaceHolders(p, EconomyShopGUI.getInstance().getMetaUtils().getRawDisplayName(meta), type, page, allpages));
               }

               if (meta.hasLore()) {
                  List<String> lore = EconomyShopGUI.getInstance().getMetaUtils().getRawLore(meta);
                  Iterator var8 = this.placeholders.iterator();

                  while(var8.hasNext()) {
                     Integer i = (Integer)var8.next();
                     if (i != -1) {
                        lore.set(i, NavBarItem.this.replacePlaceHolders(p, (String)lore.get(i), type, page, allpages));
                     }
                  }

                  EconomyShopGUI.getInstance().getMetaUtils().setRawLore(meta, lore);
               }

               item.setItemMeta(meta);
               return item;
            }
         }
      }
   }
}
