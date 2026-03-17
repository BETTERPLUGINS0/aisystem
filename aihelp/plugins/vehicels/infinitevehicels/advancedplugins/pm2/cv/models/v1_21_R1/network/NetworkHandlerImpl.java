package advancedplugins.pm2.cv.models.v1_21_R1.network;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.nms.network.NetworkHandler;
import advancedplugins.pm2.cv.models.api.nms.network.PipelineWrapper;
import advancedplugins.pm2.cv.models.api.nms.network.ProtectedPacketUnpacker;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R1.NMSFields;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.Bundler;
import advancedplugins.pm2.cv.models.v1_21_R1.network.utils.NetworkUtils;
import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.SharedConstants;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundPingPacket;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NetworkHandlerImpl implements NetworkHandler {
   public static NetworkHandlerImpl instance;
   private final Map<UUID, PipelineWrapper> pipelines = Maps.newConcurrentMap();
   private final Map<UUID, Bundler> bundles = Maps.newConcurrentMap();
   private boolean isBatching;

   public NetworkHandlerImpl() {
      if (instance != null) {
         throw new IllegalStateException("Network handler already initialized");
      } else {
         instance = this;
      }
   }

   public int getProtocolVersion() {
      return SharedConstants.c();
   }

   public Optional<PipelineWrapper> getPipeline(UUID uuid) {
      return Optional.ofNullable((PipelineWrapper)this.pipelines.get(var1));
   }

   public void removePipeline(UUID uuid) {
      this.pipelines.remove(var1);
   }

   public void injectChannel(Player player) {
      PlayerConnection var2 = ((CraftPlayer)var1).getHandle().c;
      NetworkManager var3 = (NetworkManager)ReflectionUtils.get(var2, NMSFields.SERVER_COMMON_PACKET_LISTENER_IMPL_connection);
      ChannelPipeline var4 = var3.n.pipeline();
      Objects.requireNonNull(var4);
      Objects.requireNonNull(var4);
      PipelineWrapper var5 = new PipelineWrapper(var1, var4::writeAndFlush);
      InfiniteModelChannelHandler var6 = new InfiniteModelChannelHandler(var1, var5);
      this.pipelines.put(var1.getUniqueId(), var5);
      this.bundles.put(var1.getUniqueId(), new Bundler());
      Iterator var7 = var4.toMap().keySet().iterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         if (var4.get(var8) instanceof NetworkManager) {
            var4.addBefore(var8, ModelAPI.PLUGIN.getName().toLowerCase().replace(" ", "_") + "_protected_packet_unpacker", new ProtectedPacketUnpacker());
            var4.addBefore(var8, ModelAPI.PLUGIN.getName().toLowerCase().replace(" ", "_") + "_packet_handler", var6);
            break;
         }
      }

   }

   public void ejectChannel(Player player) {
      PlayerConnection var2 = ((CraftPlayer)var1).getHandle().c;
      NetworkManager var3 = (NetworkManager)ReflectionUtils.get(var2, NMSFields.SERVER_COMMON_PACKET_LISTENER_IMPL_connection);
      Channel var4 = var3.n;
      var4.eventLoop().submit(() -> {
         ChannelPipeline var10000 = var4.pipeline();
         String var10001 = ModelAPI.PLUGIN.getName().toLowerCase();
         var10000.remove(var10001.replace(" ", "_") + "_protected_packet_unpacker");
         var10000 = var4.pipeline();
         var10001 = ModelAPI.PLUGIN.getName().toLowerCase();
         var10000.remove(var10001.replace(" ", "_") + "_packet_handler");
         return null;
      });
      this.removePipeline(var1.getUniqueId());
      this.bundles.remove(var1.getUniqueId());
   }

   public void ping(UUID uuid) {
      this.getPipeline(var1).ifPresent((var1x) -> {
         int var2 = var1x.getDesyncMonitor().getPingCounter().getAndIncrement();
         NetworkUtils.send((UUID)var1, new ClientboundPingPacket(var2));
      });
   }

   public void startBatch() {
      this.isBatching = true;
   }

   public void endBatch() {
      Iterator var1 = this.bundles.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         PipelineWrapper var3 = (PipelineWrapper)this.pipelines.get(var2.getKey());
         if (var3 != null) {
            Bundler var4 = (Bundler)var2.getValue();
            Objects.requireNonNull(var3);
            Objects.requireNonNull(var3);
            var4.bundle(var3::writeAndFlush);
            var4.clear();
         }
      }

      this.isBatching = false;
   }

   public void appendPacket(UUID uuid, Packet<? super PacketListenerPlayOut> packet) {
      Bundler var3 = (Bundler)this.bundles.get(var1);
      if (var3 != null) {
         var3.appendPacket(var2);
      }

   }

   public void appendPackets(UUID uuid, Collection<Packet<? super PacketListenerPlayOut>> collection) {
      Bundler var3 = (Bundler)this.bundles.get(var1);
      if (var3 != null) {
         var3.appendPacket(var2);
      }

   }

   public boolean isBatching() {
      return this.isBatching;
   }
}
