package advancedplugins.pm2.cv.menu;

import advancedplugins.pm2.cv.api.configuration.LangConfiguration;
import advancedplugins.pm2.cv.api.item.ItemConfiguration;
import advancedplugins.pm2.cv.api.menu.impl.ChestMenu;
import advancedplugins.pm2.cv.api.registry.Registries;
import advancedplugins.pm2.cv.api.util.inventory.ItemStackUtil;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehiclesMenu extends ChestMenu {
   private final int page;

   public VehiclesMenu(int page) {
      this.setTitle(String.format(LangConfiguration.VEHICLES_MENU_TITLE.value(), var1 + 1));
      this.setRows(6);
      this.page = var1;
   }

   public VehiclesMenu() {
      this(0);
   }

   public void onOpen(Player player) {
      ArrayList var2 = new ArrayList(Registries.getRegistry(ItemConfiguration.class).getEntries());
      var2.sort(Comparator.comparing(ItemConfiguration::getDisplayName));
      TreeSet var3 = new TreeSet(Set.of(new Integer[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43}));
      int var4 = var3.size();
      int var5 = (int)Math.ceil((double)var2.size() / (double)var4);
      int var6 = this.page * var4;
      int var7 = Math.min(var6 + var4, var2.size());
      List var8 = var2.subList(var6, var7);
      int var9 = 0;
      Iterator var10 = var3.iterator();

      while(var10.hasNext()) {
         int var11 = (Integer)var10.next();
         if (var9 >= var8.size()) {
            break;
         }

         ItemConfiguration var12 = (ItemConfiguration)var8.get(var9++);
         this.setItem(var11, ItemConfiguration.buildItemStack(var12, List.of("&eClick to receive.")), (var2x) -> {
            HashMap var3 = var1.getInventory().addItem(new ItemStack[]{ItemConfiguration.buildItemStack(var12)});
            if (!var3.isEmpty()) {
               var1.sendMessage(LangConfiguration.ERROR_INVENTORY_FULL.value());
            }
         });
      }

      this.setItem(45, this.createPrevButton(this.page > 0), (var2x) -> {
         (new VehiclesMenu(this.page - 1)).open(var1);
      });
      this.setItem(53, this.createNextButton(this.page < var5 - 1), (var2x) -> {
         (new VehiclesMenu(this.page + 1)).open(var1);
      });

      while(this.getInventory().firstEmpty() != -1) {
         this.setItem(this.getInventory().firstEmpty(), ItemStackUtil.buildCustomItem(Material.BLACK_STAINED_GLASS_PANE, 0, "&r", new ArrayList()));
      }

   }

   private ItemStack createNextButton(boolean required) {
      return !var1 ? null : ItemConfiguration.buildItemStack(ItemConfiguration.builder().id("next-button").material(Material.PLAYER_HEAD).headTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2M2OWQ0MTA3NmE4ZGVhNGYwNmQzZjFhOWFjNDdjYzk5Njk4OGI3NGEwOTEzYWIyYWMxYTc0Y2FmNzA4MTkxOCJ9fX0=").displayName(String.format(LangConfiguration.VEHICLES_MENU_NEXT_ITEM_NAME.value(), this.page + 1)).description(LangConfiguration.VEHICLES_MENU_NEXT_ITEM_LORE.asList(this.page + 1)).build());
   }

   private ItemStack createPrevButton(boolean required) {
      return !var1 ? null : ItemConfiguration.buildItemStack(ItemConfiguration.builder().id("prev-button").material(Material.PLAYER_HEAD).headTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGUxZGZjMTFhODM3MTExZDIyYjAwMWExNDQ2MWY5YTdmYzA5MzUyMmY4OGM1OGZhZWZkNmFkZWZmY2Q0ZTlhYiJ9fX0=").displayName(String.format(LangConfiguration.VEHICLES_MENU_PREV_ITEM_NAME.value(), this.page - 1)).description(LangConfiguration.VEHICLES_MENU_PREV_ITEM_LORE.asList(this.page - 1)).build());
   }

   public void onClose(Player player) {
   }
}
