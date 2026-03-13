package com.volmit.iris.util.decree;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.plugin.VolmitSender;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface DecreeExecutor {
   default VolmitSender sender() {
      return DecreeContext.get();
   }

   default Player player() {
      return this.sender().player();
   }

   default IrisData data() {
      PlatformChunkGenerator access = this.access();
      return access != null ? access.getData() : null;
   }

   default Engine engine() {
      if (this.sender().isPlayer() && IrisToolbelt.access(this.sender().player().getWorld()) != null) {
         PlatformChunkGenerator gen = IrisToolbelt.access(this.sender().player().getWorld());
         if (gen != null) {
            return gen.getEngine();
         }
      }

      return null;
   }

   default PlatformChunkGenerator access() {
      return this.sender().isPlayer() ? IrisToolbelt.access(this.world()) : null;
   }

   default World world() {
      return this.sender().isPlayer() ? this.sender().player().getWorld() : null;
   }

   default <T> T get(T v, T ifUndefined) {
      return v == null ? ifUndefined : v;
   }
}
