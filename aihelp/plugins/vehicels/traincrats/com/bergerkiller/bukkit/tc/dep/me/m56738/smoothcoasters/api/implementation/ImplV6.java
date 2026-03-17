package com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.implementation;

import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.Feature;
import com.bergerkiller.bukkit.tc.dep.me.m56738.smoothcoasters.api.NetworkInterface;
import java.nio.ByteBuffer;
import java.util.EnumSet;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ImplV6 implements Implementation {
   protected static final String CHANNEL_ROTATION = "smoothcoasters:rot";
   protected static final String CHANNEL_ROTATION_LIMIT = "smoothcoasters:limit";
   protected final EnumSet<Feature> features = EnumSet.noneOf(Feature.class);

   public ImplV6(Plugin plugin) {
      plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "smoothcoasters:rot");
      plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "smoothcoasters:limit");
      this.features.add(Feature.ROTATION);
      this.features.add(Feature.ROTATION_LIMIT);
   }

   public EnumSet<Feature> getFeatures() {
      return this.features;
   }

   public byte getVersion() {
      return 6;
   }

   public void sendRotation(NetworkInterface network, Player player, float x, float y, float z, float w, byte ticks) {
      ByteBuffer buffer = ByteBuffer.allocate(17);
      buffer.putFloat(x);
      buffer.putFloat(y);
      buffer.putFloat(z);
      buffer.putFloat(w);
      buffer.put(ticks);
      buffer.rewind();
      byte[] bytes = new byte[buffer.remaining()];
      buffer.get(bytes);
      network.sendMessage(player, "smoothcoasters:rot", bytes);
   }

   public void sendRotationLimit(NetworkInterface network, Player player, float minYaw, float maxYaw, float minPitch, float maxPitch) {
      ByteBuffer buffer = ByteBuffer.allocate(16);
      buffer.putFloat(minYaw);
      buffer.putFloat(maxYaw);
      buffer.putFloat(minPitch);
      buffer.putFloat(maxPitch);
      buffer.rewind();
      byte[] bytes = new byte[buffer.remaining()];
      buffer.get(bytes);
      network.sendMessage(player, "smoothcoasters:limit", bytes);
   }
}
