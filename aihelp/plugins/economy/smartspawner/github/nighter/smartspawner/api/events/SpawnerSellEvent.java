package github.nighter.smartspawner.api.events;

import java.util.List;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SpawnerSellEvent extends Event implements Cancellable {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final Location location;
   private final List<ItemStack> items;
   private double moneyAmount;
   private boolean cancelled = false;

   public SpawnerSellEvent(Player player, Location location, List<ItemStack> items, double moneyAmount) {
      this.player = player;
      this.location = location;
      this.items = items;
      this.moneyAmount = moneyAmount;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   @Generated
   public Player getPlayer() {
      return this.player;
   }

   @Generated
   public Location getLocation() {
      return this.location;
   }

   @Generated
   public List<ItemStack> getItems() {
      return this.items;
   }

   @Generated
   public double getMoneyAmount() {
      return this.moneyAmount;
   }

   @Generated
   public boolean isCancelled() {
      return this.cancelled;
   }

   @Generated
   public void setMoneyAmount(double moneyAmount) {
      this.moneyAmount = moneyAmount;
   }

   @Generated
   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }
}
