package ac.grim.grimac.utils.inventory.inventory;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.utils.inventory.Inventory;
import lombok.Generated;

public enum MenuType {
   GENERIC_9x1(0),
   GENERIC_9x2(1),
   GENERIC_9x3(2),
   GENERIC_9x4(3),
   GENERIC_9x5(4),
   GENERIC_9x6(5),
   GENERIC_3x3(6),
   CRAFTER_3x3(7),
   ANVIL(8),
   BEACON(9),
   BLAST_FURNACE(10),
   BREWING_STAND(11),
   CRAFTING(12),
   ENCHANTMENT(13),
   FURNACE(14),
   GRINDSTONE(15),
   HOPPER(16),
   LECTERN(17),
   LOOM(18),
   MERCHANT(19),
   SHULKER_BOX(20),
   SMITHING(21),
   SMOKER(22),
   CARTOGRAPHY_TABLE(23),
   STONECUTTER(24),
   UNKNOWN(-1);

   private static final MenuType[] MENU_BY_ID_ARRAY;
   private final int id;

   public static MenuType getMenuType(int id) {
      if (id < 0) {
         return UNKNOWN;
      } else {
         ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
         if (version.isOlderThan(ServerVersion.V_1_20_3) && id >= 7) {
            ++id;
         }

         return id >= MENU_BY_ID_ARRAY.length ? UNKNOWN : MENU_BY_ID_ARRAY[id];
      }
   }

   public static AbstractContainerMenu getMenuFromID(GrimPlayer player, Inventory playerInventory, MenuType type) {
      Object var10000;
      switch(type.ordinal()) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
         var10000 = new BasicInventoryMenu(player, playerInventory, type.getId() + 1);
         break;
      case 6:
         var10000 = new DispenserMenu(player, playerInventory);
         break;
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 17:
      case 18:
      case 19:
      default:
         var10000 = new NotImplementedMenu(player, playerInventory);
         break;
      case 16:
         var10000 = new HopperMenu(player, playerInventory);
         break;
      case 20:
         var10000 = new BasicInventoryMenu(player, playerInventory, 3);
      }

      return (AbstractContainerMenu)var10000;
   }

   public static AbstractContainerMenu getMenuFromString(GrimPlayer player, Inventory inventory, String legacyType, int slots, int horse) {
      byte var6 = -1;
      switch(legacyType.hashCode()) {
      case -1149092108:
         if (legacyType.equals("minecraft:chest")) {
            var6 = 0;
         }
         break;
      case -1112182111:
         if (legacyType.equals("minecraft:hopper")) {
            var6 = 4;
         }
         break;
      case 712019713:
         if (legacyType.equals("minecraft:dropper")) {
            var6 = 3;
         }
         break;
      case 1374330859:
         if (legacyType.equals("minecraft:shulker_box")) {
            var6 = 5;
         }
         break;
      case 1438413556:
         if (legacyType.equals("minecraft:container")) {
            var6 = 1;
         }
         break;
      case 2090881320:
         if (legacyType.equals("minecraft:dispenser")) {
            var6 = 2;
         }
      }

      Object var10000;
      switch(var6) {
      case 0:
      case 1:
         var10000 = new BasicInventoryMenu(player, inventory, slots / 9);
         break;
      case 2:
      case 3:
         var10000 = new DispenserMenu(player, inventory);
         break;
      case 4:
         var10000 = new HopperMenu(player, inventory);
         break;
      case 5:
         var10000 = new BasicInventoryMenu(player, inventory, 3);
         break;
      default:
         var10000 = new NotImplementedMenu(player, inventory);
      }

      return (AbstractContainerMenu)var10000;
   }

   @Generated
   private MenuType(final int param3) {
      this.id = id;
   }

   @Generated
   public int getId() {
      return this.id;
   }

   // $FF: synthetic method
   private static MenuType[] $values() {
      return new MenuType[]{GENERIC_9x1, GENERIC_9x2, GENERIC_9x3, GENERIC_9x4, GENERIC_9x5, GENERIC_9x6, GENERIC_3x3, CRAFTER_3x3, ANVIL, BEACON, BLAST_FURNACE, BREWING_STAND, CRAFTING, ENCHANTMENT, FURNACE, GRINDSTONE, HOPPER, LECTERN, LOOM, MERCHANT, SHULKER_BOX, SMITHING, SMOKER, CARTOGRAPHY_TABLE, STONECUTTER, UNKNOWN};
   }

   static {
      ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
      MenuType[] menuTypes = values();
      int menuIdLimit;
      if (version.isOlderThan(ServerVersion.V_1_20_3)) {
         menuIdLimit = 23;
      } else {
         menuIdLimit = menuTypes.length - 1;
      }

      MENU_BY_ID_ARRAY = new MenuType[menuIdLimit];
      System.arraycopy(menuTypes, 0, MENU_BY_ID_ARRAY, 0, menuIdLimit);
   }
}
