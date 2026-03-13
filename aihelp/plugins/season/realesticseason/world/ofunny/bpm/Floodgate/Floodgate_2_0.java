package world.ofunny.bpm.Floodgate;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

class Floodgate_2_0 implements Floodgate {
   private FloodgateApi floodgateApi = FloodgateApi.getInstance();

   public Floodgate_2_0() {
   }

   public boolean isBedrockPlayer(Player var1) {
      return this.floodgateApi == null ? false : this.floodgateApi.isFloodgatePlayer(var1.getUniqueId());
   }
}
