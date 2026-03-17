package advancedplugins.pm2.cv.models.api.model.rpc.events;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseEntityInteractEvent extends AbstractEvent {
   private static final HandlerList handlers = new HandlerList();
   private final Player player;
   private final BaseEntity<?> baseEntity;
   private final IVisualModel model;
   private final BaseEntityInteractEvent.Action action;
   private final EquipmentSlot slot;
   private final boolean isSecondary;
   private final ItemStack item;
   @Nullable
   private final Vector clickedPosition;

   public BaseEntityInteractEvent(Player var1, BaseEntity<?> var2, IVisualModel var3, BaseEntityInteractEvent.Action var4, EquipmentSlot var5, boolean var6, ItemStack var7, @Nullable Vector var8) {
      this.player = var1;
      this.baseEntity = var2;
      this.model = var3;
      this.action = var4;
      this.slot = var5;
      this.isSecondary = var6;
      this.item = var7;
      this.clickedPosition = var8;
   }

   @NotNull
   public static HandlerList getHandlerList() {
      return handlers;
   }

   @NotNull
   public HandlerList getHandlers() {
      return handlers;
   }

   public String toString() {
      String var1 = String.valueOf(this.getPlayer());
      return "BaseEntityInteractEvent(player=" + var1 + ", baseEntity=" + String.valueOf(this.getBaseEntity()) + ", model=" + String.valueOf(this.getModel()) + ", action=" + String.valueOf(this.getAction()) + ", slot=" + String.valueOf(this.getSlot()) + ", isSecondary=" + this.isSecondary() + ", item=" + String.valueOf(this.getItem()) + ", clickedPosition=" + String.valueOf(this.getClickedPosition()) + ")";
   }

   public Player getPlayer() {
      return this.player;
   }

   public BaseEntity<?> getBaseEntity() {
      return this.baseEntity;
   }

   public IVisualModel getModel() {
      return this.model;
   }

   public BaseEntityInteractEvent.Action getAction() {
      return this.action;
   }

   public EquipmentSlot getSlot() {
      return this.slot;
   }

   public boolean isSecondary() {
      return this.isSecondary;
   }

   public ItemStack getItem() {
      return this.item;
   }

   @Nullable
   public Vector getClickedPosition() {
      return this.clickedPosition;
   }

   public static enum Action {
      ATTACK,
      INTERACT,
      INTERACT_ON;

      private static BaseEntityInteractEvent.Action[] $values() {
         return new BaseEntityInteractEvent.Action[]{ATTACK, INTERACT, INTERACT_ON};
      }

      // $FF: synthetic method
      private static BaseEntityInteractEvent.Action[] $values$() {
         return new BaseEntityInteractEvent.Action[]{ATTACK, INTERACT, INTERACT_ON};
      }
   }
}
