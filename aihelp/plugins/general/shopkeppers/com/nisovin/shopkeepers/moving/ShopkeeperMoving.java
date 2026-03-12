package com.nisovin.shopkeepers.moving;

import com.nisovin.shopkeepers.api.events.ShopkeeperEditedEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopCreationData;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.input.InputRequest;
import com.nisovin.shopkeepers.input.interaction.InteractionInput;
import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.shopcreation.ShopkeeperPlacement;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopType;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopobjects.AbstractShopObjectType;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ShopkeeperMoving {
   private final InteractionInput interactionInput;
   private final ShopkeeperPlacement shopkeeperPlacement;

   public ShopkeeperMoving(InteractionInput interactionInput, ShopkeeperPlacement shopkeeperPlacement) {
      Validate.notNull(interactionInput, (String)"interactionInput is null");
      Validate.notNull(shopkeeperPlacement, (String)"shopkeeperPlacement is null");
      this.interactionInput = interactionInput;
      this.shopkeeperPlacement = shopkeeperPlacement;
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public void startMoving(Player player, Shopkeeper shopkeeper) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      this.interactionInput.request(player, new ShopkeeperMoving.ShopkeeperLocationRequest(this.shopkeeperPlacement, player, shopkeeper));
   }

   public void abortMoving(Player player) {
      Validate.notNull(player, (String)"player is null");
      InputRequest<Event> request = this.interactionInput.getRequest(player);
      if (request instanceof ShopkeeperMoving.ShopkeeperLocationRequest) {
         this.interactionInput.abortRequest(player, request);
      }

   }

   public boolean requestMove(Player player, Shopkeeper shopkeeper, Location newLocation, BlockFace blockFace) {
      Validate.notNull(player, (String)"player is null");
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Validate.notNull(newLocation, (String)"newLocation is null");
      if (!shopkeeper.isValid()) {
         return false;
      } else {
         boolean isSpawnLocationValid = this.shopkeeperPlacement.validateSpawnLocation(player, (AbstractShopType)shopkeeper.getType(), (AbstractShopObjectType)shopkeeper.getShopObject().getType(), newLocation, blockFace, (ShopCreationData)null, (AbstractShopkeeper)shopkeeper);
         if (!isSpawnLocationValid) {
            TextUtils.sendMessage(player, (Text)Messages.shopkeeperMoveAborted);
            return false;
         } else {
            ((AbstractShopkeeper)shopkeeper).teleport(newLocation, blockFace);
            TextUtils.sendMessage(player, (Text)Messages.shopkeeperMoved);
            Bukkit.getPluginManager().callEvent(new ShopkeeperEditedEvent(shopkeeper, player));
            shopkeeper.save();
            return true;
         }
      }
   }

   private class ShopkeeperLocationRequest implements InteractionInput.Request {
      private final ShopkeeperPlacement shopkeeperPlacement;
      private final Player player;
      private final Shopkeeper shopkeeper;

      ShopkeeperLocationRequest(ShopkeeperPlacement param2, Player param3, Shopkeeper param4) {
         assert shopkeeperPlacement != null;

         assert player != null;

         assert shopkeeper != null;

         this.shopkeeperPlacement = shopkeeperPlacement;
         this.player = player;
         this.shopkeeper = shopkeeper;
      }

      private boolean isAbortAction(Action action) {
         return action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
      }

      public boolean accepts(PlayerInteractEvent event) {
         Action action = event.getAction();
         if (this.isAbortAction(action)) {
            return true;
         } else if (action != Action.RIGHT_CLICK_BLOCK) {
            return false;
         } else {
            Block clickedBlock = (Block)Unsafe.assertNonNull(event.getClickedBlock());
            if (this.isInteractionIgnored(clickedBlock.getType())) {
               Log.debug(() -> {
                  return "Shopkeeper moving: Ignoring interaction with block of type " + String.valueOf(clickedBlock.getType());
               });
               return false;
            } else {
               return true;
            }
         }
      }

      private boolean isInteractionIgnored(Material clickedBlockType) {
         if (ItemUtils.isClickableDoor(clickedBlockType)) {
            return true;
         } else {
            return ItemUtils.isClickableSwitch(clickedBlockType);
         }
      }

      public void onInteract(PlayerInteractEvent event) {
         event.setCancelled(true);
         if (this.isAbortAction(event.getAction())) {
            this.onAborted();
         } else {
            Player player = event.getPlayer();
            Block clickedBlock = (Block)Unsafe.assertNonNull(event.getClickedBlock());
            BlockFace clickedBlockFace = event.getBlockFace();
            Location spawnLocation = this.shopkeeperPlacement.determineSpawnLocation(player, clickedBlock, clickedBlockFace);
            ShopkeeperMoving.this.requestMove(player, this.shopkeeper, spawnLocation, clickedBlockFace);
         }
      }

      public void onAborted() {
         TextUtils.sendMessage(this.player, (Text)Messages.shopkeeperMoveAborted);
      }
   }
}
