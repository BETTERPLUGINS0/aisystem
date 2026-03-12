package me.SuperRonanCraft.BetterRTP.references.messages.placeholder;

public enum Placeholders {
   COMMAND("command"),
   PLAYER_NAME("player"),
   COOLDOWN("cooldown"),
   LOCATION_X("x"),
   LOCATION_Y("y"),
   LOCATION_Z("z"),
   WORLD("world"),
   PERMISSION("permission"),
   ATTEMPTS("attempts"),
   PRICE("price"),
   DELAY("delay"),
   TIME("time"),
   BIOME("biome");

   public final String name;

   private Placeholders(String name) {
      this.name = "%" + name + "%";
   }

   // $FF: synthetic method
   private static Placeholders[] $values() {
      return new Placeholders[]{COMMAND, PLAYER_NAME, COOLDOWN, LOCATION_X, LOCATION_Y, LOCATION_Z, WORLD, PERMISSION, ATTEMPTS, PRICE, DELAY, TIME, BIOME};
   }
}
