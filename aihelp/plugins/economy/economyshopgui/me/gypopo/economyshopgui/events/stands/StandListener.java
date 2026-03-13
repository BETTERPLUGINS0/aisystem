package me.gypopo.economyshopgui.events.stands;

import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.ShopItem;
import me.gypopo.economyshopgui.objects.TransactionMenu;
import me.gypopo.economyshopgui.objects.inventorys.StandManagement;
import me.gypopo.economyshopgui.objects.stands.ChunkLoc;
import me.gypopo.economyshopgui.objects.stands.Stand;
import me.gypopo.economyshopgui.objects.stands.StandLoc;
import me.gypopo.economyshopgui.objects.stands.StandType;
import me.gypopo.economyshopgui.providers.StandProvider;
import me.gypopo.economyshopgui.util.PermissionsCache;
import me.gypopo.economyshopgui.util.Transaction;
import me.gypopo.economyshopgui.util.exceptions.StandLoadException;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class StandListener implements Listener {
   private final EconomyShopGUI plugin;
   private final StandProvider provider;

   public StandListener(EconomyShopGUI plugin, StandProvider provider) {
      this.plugin = plugin;
      this.provider = provider;
   }

   @EventHandler
   public void onChunkLoad(ChunkLoadEvent e) {
      this.provider.loadStands(new ChunkLoc(e.getChunk()));
   }

   @EventHandler
   public void onChunkUnload(ChunkUnloadEvent e) {
      this.provider.checkAndUnload(new ChunkLoc(e.getChunk()));
   }

   @EventHandler
   public void onBlockBreak(BlockBreakEvent event) {
      if (StandType.isValidType(event.getBlock())) {
         Stand stand = this.provider.getStand(new StandLoc(event.getBlock()));
         if (stand != null) {
            Player player = event.getPlayer();
            if (!PermissionsCache.hasPermission(player, "EconomyShopGUI.eshop.shopstands.edit")) {
               SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
            } else {
               new StandManagement(this.plugin, player, stand);
               event.setCancelled(true);
            }
         }
      }
   }

   @EventHandler
   public void onBlockPlace(BlockPlaceEvent event) {
      StandType type = StandType.fromType(event.getBlock());
      if (type != null) {
         String item = this.plugin.versionHandler.getNBTString(event.getItemInHand(), "shopstand_item");
         if (item != null) {
            Player player = event.getPlayer();
            if (!PermissionsCache.hasPermission(player, "EconomyShopGUI.eshop.shopstands.create")) {
               SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
            } else {
               try {
                  this.provider.createStand(event.getBlock(), item, type);
                  SendMessage.chatToPlayer(player, Lang.CREATED_NEW_SHOP_STAND.get().replace("%item%", item));
               } catch (StandLoadException var6) {
                  SendMessage.chatToPlayer(player, Lang.ERROR_CREATING_NEW_SHOP_STAND.get().replace("%reason%", var6.getMessage()));
               }

            }
         }
      }
   }

   @EventHandler
   public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
      if (e.getEntity() instanceof ArmorStand && e.getDamager() instanceof Player) {
         ArmorStand clicked = (ArmorStand)e.getEntity();
         Stand stand = this.provider.getStand(clicked.getUniqueId());
         if (stand != null) {
            ShopItem shopItem = this.plugin.getShopItem(stand.getItem());
            if (shopItem == null) {
               SendMessage.warnMessage(Lang.CANNOT_OPEN_SHOP_STAND.get().replace("%player%", e.getEntity().getName()).replace("%item%", stand.getItem()));
            } else {
               Player player = (Player)e.getDamager();
               if (player.isSneaking() && PermissionsCache.hasPermission(player, "EconomyShopGUI.eshop.shopstands.edit")) {
                  new StandManagement(this.plugin, player, stand);
               } else if (shopItem.getBuyPrice() >= 0.0D) {
                  if (!this.isAllowedGamemode(player)) {
                     return;
                  }

                  if (!PermissionsCache.hasPermission(player, "EconomyShopGUI.shop." + stand.getItem().split("\\.")[0])) {
                     SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
                  }

                  if (!shopItem.meetsRequirements(player, false)) {
                     return;
                  }

                  (new TransactionMenu(player, shopItem, (String)null, true, Transaction.Mode.BUY, Transaction.Type.SHOPSTAND_BUY_SCREEN, 1)).open();
               } else {
                  SendMessage.chatToPlayer(player, Lang.CANNOT_PURCHASE_ITEM.get());
               }

            }
         }
      }
   }

   @EventHandler
   public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
      if (e.getRightClicked() instanceof ArmorStand) {
         ArmorStand clicked = (ArmorStand)e.getRightClicked();
         Stand stand = this.provider.getStand(clicked.getUniqueId());
         if (stand != null) {
            ShopItem shopItem = this.plugin.getShopItem(stand.getItem());
            if (shopItem == null) {
               SendMessage.warnMessage(Lang.CANNOT_OPEN_SHOP_STAND.get().replace("%player%", e.getPlayer().getName()).replace("%item%", stand.getItem()));
            } else {
               Player player = e.getPlayer();
               if (this.isAllowedGamemode(player)) {
                  if (!PermissionsCache.hasPermission(player, "EconomyShopGUI.shop." + stand.getItem().split("\\.")[0])) {
                     SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
                  }

                  if (shopItem.meetsRequirements(player, false)) {
                     if (!(shopItem.getSellPrice() >= 0.0D)) {
                        SendMessage.chatToPlayer(player, Lang.CANNOT_SELL_ITEM.get());
                     } else {
                        (new TransactionMenu(player, shopItem, (String)null, true, Transaction.Mode.SELL, Transaction.Type.SHOPSTAND_SELL_SCREEN, 1)).open();
                     }
                  }
               }
            }
         }
      }
   }

   private boolean isAllowedGamemode(Player player) {
      if (this.plugin.bannedGamemodes.contains(player.getGameMode()) && !PermissionsCache.hasPermission(player, "EconomyShopGUI.bypassgamemode")) {
         SendMessage.chatToPlayer(player, Lang.CANNOT_ENTER_SHOP_BANNED_GAMEMODE.get().replace("%gamemode%", player.getGameMode().name().toLowerCase()));
         return false;
      } else {
         return true;
      }
   }
}
