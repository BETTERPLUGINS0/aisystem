package me.casperge.interfaces;

import com.comphenix.protocol.events.PacketContainer;
import me.casperge.realisticseasons.biome.HeightAccessor;

public interface ProtocolLibUtils {
   void readPacket(PacketContainer var1, int var2, boolean var3, int var4, int var5, HeightAccessor var6, boolean var7, int var8, boolean var9);
}
