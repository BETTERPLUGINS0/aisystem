package world.ofunny.bpm.Floodgate;

import org.bukkit.Bukkit;

public class FloodgateAPI {
   private static FloodgateAPI INSTANCE = null;
   private final Floodgate floodgateAPI;

   public static Floodgate get() {
      if (INSTANCE == null) {
         Class var0 = FloodgateAPI.class;
         synchronized(FloodgateAPI.class) {
            INSTANCE = new FloodgateAPI();
         }
      }

      return INSTANCE.floodgateAPI;
   }

   FloodgateAPI() {
      if (this.isClass("org.geysermc.floodgate.api.FloodgateApi")) {
         Bukkit.getLogger().info("Floodgate 2.x has been detected, enabling floodgate mode of RealisticSeasons");
         this.floodgateAPI = new Floodgate_2_0();
      } else {
         this.floodgateAPI = new Floodgate_Dummy();
      }

   }

   public boolean isClass(String var1) {
      try {
         Class.forName(var1);
         return true;
      } catch (ClassNotFoundException var3) {
         return false;
      }
   }
}
