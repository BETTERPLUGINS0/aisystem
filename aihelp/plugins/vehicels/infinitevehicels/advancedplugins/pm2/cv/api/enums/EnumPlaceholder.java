package advancedplugins.pm2.cv.api.enums;

import org.jetbrains.annotations.NotNull;

public enum EnumPlaceholder {
   NAME("name"),
   FUEL_LEVEL("fuel_level"),
   FUEL_LEVEL_PERCENTAGE("fuel_level_percentage"),
   FUEL_CAPACITY("fuel_capacity"),
   SEAT_INDEX("seat_index"),
   PLAYER_NAME("player_name"),
   PLAYER_KILLS("player_kills"),
   PAGE("page"),
   NEXT_PAGE("next_page"),
   PREVIOUS_PAGE("previous_page"),
   HEALTH("health"),
   MAX_HEALTH("max_health"),
   NEED_REPAIR("need_repair");

   private final String key;

   private EnumPlaceholder(String param3) {
      this.key = "{" + var3 + "}";
   }

   public String getKey() {
      return this.key;
   }

   @NotNull
   public String format(@NotNull String var1, @NotNull String var2) {
      return var1.replace(this.key, var2);
   }

   public String toString() {
      return this.key;
   }

   // $FF: synthetic method
   private static EnumPlaceholder[] $values() {
      return new EnumPlaceholder[]{NAME, FUEL_LEVEL, FUEL_LEVEL_PERCENTAGE, FUEL_CAPACITY, SEAT_INDEX, PLAYER_NAME, PLAYER_KILLS, PAGE, NEXT_PAGE, PREVIOUS_PAGE, HEALTH, MAX_HEALTH, NEED_REPAIR};
   }
}
