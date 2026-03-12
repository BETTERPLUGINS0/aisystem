package fr.xephi.authme.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ProtectInventoryEvent extends CustomEvent implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final ItemStack[] storedInventory;
   private final ItemStack[] storedArmor;
   private final Player player;
   private boolean isCancelled;

   public ProtectInventoryEvent(Player player, boolean isAsync) {
      super(isAsync);
      this.player = player;
      this.storedInventory = player.getInventory().getContents();
      this.storedArmor = player.getInventory().getArmorContents();
   }

   public ItemStack[] getStoredInventory() {
      return this.storedInventory;
   }

   public ItemStack[] getStoredArmor() {
      return this.storedArmor;
   }

   public Player getPlayer() {
      return this.player;
   }

   public void setCancelled(boolean isCancelled) {
      this.isCancelled = isCancelled;
   }

   public boolean isCancelled() {
      return this.isCancelled;
   }

   public static HandlerList getHandlerList() {
      return handlers;
   }

   public HandlerList getHandlers() {
      return handlers;
   }
}
