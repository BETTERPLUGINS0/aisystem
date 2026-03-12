package me.SuperRonanCraft.BetterRTP.references.rtpinfo.worlds;

import java.util.List;
import me.SuperRonanCraft.BetterRTP.BetterRTP;
import me.SuperRonanCraft.BetterRTP.player.rtp.RTP_SHAPE;
import org.bukkit.World;

public interface RTPWorld_Defaulted {
   void setUseWorldBorder(boolean var1);

   void setCenterX(int var1);

   void setCenterZ(int var1);

   void setMaxRadius(int var1);

   void setMinRadius(int var1);

   void setPrice(int var1);

   void setBiomes(List<String> var1);

   void setWorld(World var1);

   void setShape(RTP_SHAPE var1);

   void setMinY(int var1);

   void setMaxY(int var1);

   void setCooldown(long var1);

   void setRTPOnDeath(boolean var1);

   default void setupDefaults() {
      this.setAllFrom(BetterRTP.getInstance().getRTP().getRTPdefaultWorld());
   }

   default void setAllFrom(RTPWorld rtpWorld) {
      this.setMaxRadius(rtpWorld.getMaxRadius());
      this.setMinRadius(rtpWorld.getMinRadius());
      this.setUseWorldBorder(rtpWorld.getUseWorldborder());
      this.setCenterX(rtpWorld.getCenterX());
      this.setCenterZ(rtpWorld.getCenterZ());
      this.setPrice(rtpWorld.getPrice());
      this.setBiomes(rtpWorld.getBiomes());
      this.setShape(rtpWorld.getShape());
      this.setMinY(rtpWorld.getMinY());
      this.setMaxY(rtpWorld.getMaxY());
      this.setCooldown(rtpWorld.getCooldown());
      this.setRTPOnDeath(rtpWorld.getRTPOnDeath());
   }
}
