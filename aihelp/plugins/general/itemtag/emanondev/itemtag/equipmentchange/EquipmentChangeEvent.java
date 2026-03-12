package emanondev.itemtag.equipmentchange;

import emanondev.itemedit.utility.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EquipmentChangeEvent extends PlayerEvent {
   private static final HandlerList HANDLERS_LIST = new HandlerList();
   private final ItemStack from;
   private final ItemStack to;
   private final EquipmentSlot slot;
   private final EquipmentChangeEvent.EquipMethod method;

   public EquipmentChangeEvent(@NotNull Player who, @NotNull EquipmentChangeEvent.EquipMethod method, @NotNull EquipmentSlot slot, @Nullable ItemStack from, @Nullable ItemStack to) {
      super(who);
      if (!InventoryUtils.getPlayerEquipmentSlots().contains(slot)) {
         throw new IllegalArgumentException("Slot type not supported");
      } else {
         this.from = from;
         this.to = to;
         this.slot = slot;
         this.method = method;
      }
   }

   public static HandlerList getHandlerList() {
      return HANDLERS_LIST;
   }

   @NotNull
   public HandlerList getHandlers() {
      return HANDLERS_LIST;
   }

   @Nullable
   public ItemStack getFrom() {
      return this.from;
   }

   @Nullable
   public ItemStack getTo() {
      return this.to;
   }

   @NotNull
   public EquipmentSlot getSlotType() {
      return this.slot;
   }

   @NotNull
   public EquipmentChangeEvent.EquipMethod getMethod() {
      return this.method;
   }

   public static enum EquipMethod {
      SHIFT_CLICK,
      INVENTORY_DRAG,
      DISPENSER,
      BROKE,
      DEATH,
      PLUGIN_WORLD_CHANGE,
      COMMAND,
      RIGHT_CLICK,
      SWAP_HANDS_ITEM,
      RESPAWN,
      HOTBAR_HAND_CHANGE,
      PICKUP,
      CONSUME,
      DROP,
      SHEEP_COLOR,
      NAMETAG_APPLY,
      UNKNOWN,
      INVENTORY_DROP,
      INVENTORY_PICKUP,
      INVENTORY_PLACE,
      INVENTORY_HOTBAR_SWAP,
      INVENTORY_MOVE_TO_OTHER_INVENTORY,
      INVENTORY_COLLECT_TO_CURSOR,
      INVENTORY_SWAP_WITH_CURSOR,
      USE,
      ARMOR_STAND_MANIPULATE,
      USE_ON_ENTITY,
      QUIT,
      JOIN;

      // $FF: synthetic method
      private static EquipmentChangeEvent.EquipMethod[] $values() {
         return new EquipmentChangeEvent.EquipMethod[]{SHIFT_CLICK, INVENTORY_DRAG, DISPENSER, BROKE, DEATH, PLUGIN_WORLD_CHANGE, COMMAND, RIGHT_CLICK, SWAP_HANDS_ITEM, RESPAWN, HOTBAR_HAND_CHANGE, PICKUP, CONSUME, DROP, SHEEP_COLOR, NAMETAG_APPLY, UNKNOWN, INVENTORY_DROP, INVENTORY_PICKUP, INVENTORY_PLACE, INVENTORY_HOTBAR_SWAP, INVENTORY_MOVE_TO_OTHER_INVENTORY, INVENTORY_COLLECT_TO_CURSOR, INVENTORY_SWAP_WITH_CURSOR, USE, ARMOR_STAND_MANIPULATE, USE_ON_ENTITY, QUIT, JOIN};
      }
   }
}
