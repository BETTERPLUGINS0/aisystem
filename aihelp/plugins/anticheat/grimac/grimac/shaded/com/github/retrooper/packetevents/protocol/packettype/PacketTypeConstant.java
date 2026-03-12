package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;

public interface PacketTypeConstant extends PacketTypeCommon {
   default int getId(ClientVersion version) {
      return this.getId();
   }

   int getId();
}
