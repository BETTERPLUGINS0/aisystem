package ac.grim.grimac.manager.init.stop;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.utils.anticheat.LogUtil;

public class TerminatePacketEvents implements StoppableInitable {
   public void stop() {
      LogUtil.info("Terminating PacketEvents...");
      PacketEvents.getAPI().terminate();
   }
}
