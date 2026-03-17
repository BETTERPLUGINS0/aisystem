package com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.implementation;

import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.Feature;
import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.NetworkInterface;
import java.nio.ByteBuffer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/** @deprecated */
@Deprecated
public class ImplV5 extends ImplV6 {
   protected static final String CHANNEL_ENTITY_ROTATION = "smoothcoasters:erot";
   protected static final String CHANNEL_ENTITY_PROPERTIES = "smoothcoasters:eprop";

   public ImplV5(Plugin plugin) {
      super(plugin);
      plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "smoothcoasters:erot");
      plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "smoothcoasters:eprop");
      this.features.add(Feature.ENTITY_ROTATION);
      this.features.add(Feature.ENTITY_PROPERTIES);
   }

   public byte getVersion() {
      return 5;
   }

   public void sendEntityRotation(NetworkInterface network, Player player, int entity, float x, float y, float z, float w, byte ticks) {
      ByteBuffer buffer = ByteBuffer.allocate(21);
      buffer.putInt(entity);
      buffer.putFloat(x);
      buffer.putFloat(y);
      buffer.putFloat(z);
      buffer.putFloat(w);
      buffer.put(ticks);
      buffer.rewind();
      byte[] bytes = new byte[buffer.remaining()];
      buffer.get(bytes);
      network.sendMessage(player, "smoothcoasters:erot", bytes);
   }

   public void sendEntityProperties(NetworkInterface network, Player player, int entity, byte ticks) {
      ByteBuffer buffer = ByteBuffer.allocate(5);
      buffer.putInt(entity);
      buffer.put(ticks);
      buffer.rewind();
      byte[] bytes = new byte[buffer.remaining()];
      buffer.get(bytes);
      network.sendMessage(player, "smoothcoasters:eprop", bytes);
   }
}
