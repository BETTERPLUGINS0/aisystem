package tntrun.menu;

import java.util.Arrays;

public enum ConfigMenu2 {
   RED_WOOL(4),
   CLOCK(10),
   YELLOW_CARPET(11),
   BLUE_CARPET(12),
   GLOW_INK_SAC(14),
   LIGHT_BLUE_CARPET(15),
   CYAN_CARPET(16),
   IRON_SWORD(19),
   LIGHT_GRAY_CARPET(20),
   WHITE_CARPET(21),
   SPYGLASS(23),
   BROWN_CARPET(24),
   ORANGE_CARPET(25),
   ARROW(27),
   BARRIER(31);

   private int slot;

   private ConfigMenu2(int param3) {
      this.slot = slot;
   }

   public int getSlot() {
      return this.slot;
   }

   public static ConfigMenu2 getName(int slot) {
      return (ConfigMenu2)Arrays.stream(values()).filter((value) -> {
         return value.getSlot() == slot;
      }).findFirst().orElse((Object)null);
   }

   // $FF: synthetic method
   private static ConfigMenu2[] $values() {
      return new ConfigMenu2[]{RED_WOOL, CLOCK, YELLOW_CARPET, BLUE_CARPET, GLOW_INK_SAC, LIGHT_BLUE_CARPET, CYAN_CARPET, IRON_SWORD, LIGHT_GRAY_CARPET, WHITE_CARPET, SPYGLASS, BROWN_CARPET, ORANGE_CARPET, ARROW, BARRIER};
   }
}
