package tntrun.menu;

import java.util.Arrays;

public enum ConfigMenu {
   RED_WOOL(4),
   BLAZE_ROD(10),
   WOODEN_AXE(11),
   BONE(12),
   MAGMA_CREAM(14),
   NETHER_STAR(15),
   ENDER_PEARL(16),
   GLOWSTONE_DUST(19),
   REDSTONE(20),
   FILLED_MAP(21),
   OAK_SIGN(23),
   LADDER(24),
   DIAMOND(25),
   BARRIER(31),
   ARROW(35);

   private int slot;

   private ConfigMenu(int param3) {
      this.slot = slot;
   }

   public int getSlot() {
      return this.slot;
   }

   public static ConfigMenu getName(int slot) {
      return (ConfigMenu)Arrays.stream(values()).filter((value) -> {
         return value.getSlot() == slot;
      }).findFirst().orElse((Object)null);
   }

   // $FF: synthetic method
   private static ConfigMenu[] $values() {
      return new ConfigMenu[]{RED_WOOL, BLAZE_ROD, WOODEN_AXE, BONE, MAGMA_CREAM, NETHER_STAR, ENDER_PEARL, GLOWSTONE_DUST, REDSTONE, FILLED_MAP, OAK_SIGN, LADDER, DIAMOND, BARRIER, ARROW};
   }
}
