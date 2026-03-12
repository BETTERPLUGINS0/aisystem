package me.gypopo.economyshopgui.objects.inventorys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.CalculateAmount;
import me.gypopo.economyshopgui.objects.ShopInventory;
import me.gypopo.economyshopgui.util.ItemBuilder;
import me.gypopo.economyshopgui.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FileUploader extends ShopInventory {
   private final Inventory inv = Bukkit.createInventory(this, 45, "§8§lSelect files to upload");
   private final FileUploader.Listen listener = new FileUploader.Listen();
   private final EconomyShopGUI plugin = EconomyShopGUI.getInstance();
   private final ArrayList<String> shops = new ArrayList();
   private FileUploader.ShopSelect shopSelect = new FileUploader.ShopSelect();
   private final ArrayList<String> files = new ArrayList();
   private final Consumer<ArrayList<String>> onComplete;
   private final Player player;
   private Class<? extends ShopInventory> current = FileUploader.class;

   public FileUploader(Player p, Consumer<ArrayList<String>> onComplete) {
      this.onComplete = onComplete;
      this.player = p;
      this.buildPane();
      Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
      p.openInventory(this.inv);
   }

   private void buildPane() {
      this.inv.setItem(12, (new ItemBuilder(XMaterial.FILLED_MAP.parseItem())).withDisplayName("§a§lshops").hideFlags().withLore(CalculateAmount.splitLongString("§6§lShops selected for upload: §r§c" + this.shops.size() + " \n \n §aLeft click to select/deselect shop files \n §aRight click to select/deselect every shop file")).build());
      this.inv.setItem(14, (new ItemBuilder(XMaterial.PAPER.parseItem())).hideFlags().withDisplayName("§c§lconfig.yml").withLore(CalculateAmount.splitLongString("§6This file won't be uploaded by default, though, if anything related to §ftransaction screens§6, §foption items§6, §fnavigation bars§6, §finventory sizes§6, §fetc... §6is customized in your shop layout, you may opt-in to also include this file in the upload process.")).build());
      this.inv.setItem(32, (new ItemBuilder(XMaterial.GREEN_WOOL.parseItem())).withDisplayName("§a§lComplete upload process").build());
      this.inv.setItem(30, (new ItemBuilder(XMaterial.RED_WOOL.parseItem())).withDisplayName("§c§lCancel upload process").build());
      this.fillEmpty();
   }

   private void fillEmpty() {
      if (this.plugin.createItem.fillItem != null) {
         for(int i = 0; i < this.inv.getSize(); ++i) {
            if (this.inv.getItem(i) == null) {
               this.inv.setItem(i, this.plugin.createItem.fillItem);
            }
         }

      }
   }

   private void updateShops() {
      this.inv.setItem(12, (new ItemBuilder(XMaterial.FILLED_MAP.parseItem())).withDisplayName("§a§lshops").hideFlags().withLore(CalculateAmount.splitLongString("§6§lShops selected for upload: §r§c" + this.shops.size() + " \n \n §aLeft click to select/deselect shop files \n §aRight click to select/deselect every shop file")).build());
   }

   private void quit() {
      InventoryClickEvent.getHandlerList().unregister(this.listener);
   }

   private class ShopSelect extends ShopInventory {
      public final Inventory inv = Bukkit.createInventory(this, 54, "§8§lSelect shops to upload");

      public ShopSelect() {
         FileUploader.this.shops.addAll(ConfigManager.getShops());
      }

      public void open(Player p) {
         if (!this.inv.isEmpty()) {
            this.inv.clear();
         }

         this.update();
         this.buildPane(p);
         p.openInventory(this.inv);
      }

      private void update() {
         ItemBuilder builder;
         for(Iterator var1 = ConfigManager.getShops().iterator(); var1.hasNext(); this.inv.addItem(new ItemStack[]{builder.build()})) {
            String shop = (String)var1.next();
            if (FileUploader.this.shops.contains(shop)) {
               builder = new ItemBuilder(XMaterial.FILLED_MAP.parseMaterial());
               builder.withEnchantGlint();
               builder.hideFlags();
               builder.withDisplayName("§a§l" + shop);
               builder.withLore("§6Click to deselect");
            } else {
               builder = new ItemBuilder(XMaterial.PAPER.parseMaterial());
               builder.hideFlags();
               builder.withDisplayName("§c§l" + shop);
               builder.withLore("§6Click to select");
            }
         }

      }

      private void buildPane(Player p) {
         ItemStack profile = (new ItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())).withSkullOwner(p.getName()).withLore(Lang.NAME.get() + ": " + ChatColor.RED + p.getDisplayName(), Lang.MONEY.get() + ": " + ChatColor.RED + FileUploader.this.plugin.formatPrice(FileUploader.this.plugin.getEcoHandler().getDefaultProvider().getType(), FileUploader.this.plugin.getEcoHandler().getDefaultProvider().getBalance(p)), Lang.LEVEL.get() + ": " + ChatColor.RED + p.getLevel()).build();
         this.inv.setItem(45, profile);
         this.inv.setItem(53, (new ItemBuilder(Material.BARRIER)).withDisplayName(Lang.BACK.get()).build());
      }

      public void toggle(int slot) {
         ItemStack item = this.inv.getItem(slot);
         String shop = ChatColor.stripColor(item.getItemMeta().getDisplayName());
         ItemBuilder builder;
         if (FileUploader.this.shops.contains(shop)) {
            builder = new ItemBuilder(XMaterial.PAPER.parseMaterial());
            builder.hideFlags();
            builder.withDisplayName("§c§l" + shop);
            builder.withLore("§6Click to select");
            this.inv.setItem(slot, builder.build());
            FileUploader.this.shops.remove(shop);
         } else {
            builder = new ItemBuilder(XMaterial.FILLED_MAP.parseMaterial());
            builder.withEnchantGlint();
            builder.withDisplayName("§a§l" + shop);
            builder.withLore("§6Click to deselect");
            this.inv.setItem(slot, builder.build());
            FileUploader.this.shops.add(shop);
         }

      }
   }

   private class Listen implements Listener {
      private Listen() {
      }

      @EventHandler
      public void onInventoryClick(InventoryClickEvent e) {
         if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof ShopInventory && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            e.setCancelled(true);
            int slot = e.getRawSlot();
            if (e.getClickedInventory() == FileUploader.this.inv) {
               if (slot == 14) {
                  ItemBuilder builder;
                  if (!FileUploader.this.files.contains("config.yml")) {
                     builder = new ItemBuilder(XMaterial.FILLED_MAP.parseItem());
                     builder.hideFlags();
                     builder.withDisplayName("§a§lconfig.yml").withEnchantGlint();
                     FileUploader.this.files.add("config.yml");
                  } else {
                     builder = new ItemBuilder(XMaterial.PAPER.parseItem());
                     builder.withDisplayName("§c§lconfig.yml").removeEnchants();
                     FileUploader.this.files.remove("config.yml");
                  }

                  builder.withLore(CalculateAmount.splitLongString("§6This file won't be uploaded by default, though, if anything related to §ftransaction screens§6, §foption items§6, §fnavigation bars§6, §finventory sizes§6, §fetc... §6is customized in your shop layout, you may opt-in to also include this file in the upload process."));
                  FileUploader.this.inv.setItem(14, builder.build());
               } else if (slot == 12) {
                  if (e.getClick() == ClickType.LEFT) {
                     FileUploader.this.current = FileUploader.ShopSelect.class;
                     FileUploader.this.shopSelect.open(FileUploader.this.player);
                  } else if (e.getClick() == ClickType.RIGHT) {
                     if (!FileUploader.this.shops.isEmpty()) {
                        FileUploader.this.shops.clear();
                     } else {
                        FileUploader.this.shops.addAll(ConfigManager.getShops());
                     }

                     FileUploader.this.updateShops();
                  }
               } else if (slot == 30) {
                  FileUploader.this.quit();
                  e.getWhoClicked().closeInventory();
                  FileUploader.this.onComplete.accept((Object)null);
               } else if (slot == 32) {
                  if (FileUploader.this.files.isEmpty() && FileUploader.this.shops.isEmpty()) {
                     return;
                  }

                  FileUploader.this.quit();
                  Iterator var5 = FileUploader.this.shops.iterator();

                  while(var5.hasNext()) {
                     String shop = (String)var5.next();
                     FileUploader.this.files.add("shops/" + shop + ".yml");
                     FileUploader.this.files.add("sections/" + shop + ".yml");
                  }

                  FileUploader.this.onComplete.accept(FileUploader.this.files);
               }
            } else if (e.getClickedInventory().getHolder() instanceof FileUploader.ShopSelect) {
               if (slot == 53) {
                  FileUploader.this.updateShops();
                  FileUploader.this.current = FileUploader.class;
                  FileUploader.this.player.openInventory(FileUploader.this.inv);
               } else {
                  switch(XMaterial.matchXMaterial(e.getCurrentItem())) {
                  case PAPER:
                  case FILLED_MAP:
                     FileUploader.this.shopSelect.toggle(slot);
                  }
               }
            }

         }
      }

      @EventHandler
      public void onInventoryClose(InventoryCloseEvent e) {
         if (e.getInventory() == FileUploader.this.inv && FileUploader.this.current == FileUploader.class) {
            FileUploader.this.quit();
         } else if (e.getInventory().getHolder() instanceof FileUploader.ShopSelect && FileUploader.this.current == FileUploader.ShopSelect.class) {
            FileUploader.this.updateShops();
            FileUploader.this.current = FileUploader.class;
            FileUploader.this.player.openInventory(FileUploader.this.inv);
         }

      }

      // $FF: synthetic method
      Listen(Object x1) {
         this();
      }
   }
}
