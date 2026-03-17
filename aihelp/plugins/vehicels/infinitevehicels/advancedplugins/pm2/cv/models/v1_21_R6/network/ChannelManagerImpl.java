package advancedplugins.pm2.cv.models.v1_21_R6.network;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.nms.network.NetworkHandler;
import advancedplugins.pm2.cv.models.api.nms.network.PipelineWrapper;
import advancedplugins.pm2.cv.models.api.nms.network.ProtectedPacketUnpacker;
import advancedplugins.pm2.cv.models.api.utils.reflections.ReflectionUtils;
import advancedplugins.pm2.cv.models.v1_21_R6.ReflectionFieldCatalog;
import advancedplugins.pm2.cv.models.v1_21_R6.network.utils.PacketAggregator;
import advancedplugins.pm2.cv.models.v1_21_R6.network.utils.PacketTransmissionUtility;
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
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ChannelManagerImpl implements NetworkHandler {
   public static ChannelManagerImpl instance;
   private final Map<UUID, PipelineWrapper> playerChannels = Maps.newConcurrentMap();
   private final Map<UUID, PacketAggregator> playerAggregators = Maps.newConcurrentMap();
   private volatile boolean batchMode;

   public ChannelManagerImpl() {
      if (instance != null) {
         throw new IllegalStateException("Channel manager already exists");
      } else {
         instance = this;
      }
   }

   public int getProtocolVersion() {
      return SharedConstants.c();
   }

   public Optional<PipelineWrapper> getPipeline(UUID var1) {
      return Optional.ofNullable((PipelineWrapper)this.playerChannels.get(var1));
   }

   public void removePipeline(UUID var1) {
      this.playerChannels.remove(var1);
   }

   public void injectChannel(Player var1) {
      PlayerConnection var2 = ((CraftPlayer)var1).getHandle().g;
      NetworkManager var3 = (NetworkManager)ReflectionUtils.get(var2, ReflectionFieldCatalog.NETWORK_CONNECTION);
      if (var3 != null) {
         ChannelPipeline var4 = var3.n.pipeline();
         UUID var5 = var1.getUniqueId();
         Objects.requireNonNull(var4);
         PipelineWrapper var6 = new PipelineWrapper(var1, var4::writeAndFlush);
         ModelPacketHandler var7 = new ModelPacketHandler(var1, var6);
         this.playerChannels.put(var5, var6);
         this.playerAggregators.put(var5, new PacketAggregator());
         this.insertHandlersIntoPipeline(var4, var7);
      }
   }

   private void insertHandlersIntoPipeline(ChannelPipeline var1, ModelPacketHandler var2) {
      String var3 = ModelAPI.PLUGIN.getName().toLowerCase().replace(" ", "_");
      Iterator var4 = var1.toMap().entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         if (var5.getValue() instanceof NetworkManager) {
            var1.addBefore((String)var5.getKey(), var3 + "_protected_unpacker", new ProtectedPacketUnpacker());
            var1.addBefore((String)var5.getKey(), var3 + "_model_handler", var2);
            break;
         }
      }

   }

   public void ejectChannel(Player var1) {
      PlayerConnection var2 = ((CraftPlayer)var1).getHandle().g;
      NetworkManager var3 = (NetworkManager)ReflectionUtils.get(var2, ReflectionFieldCatalog.NETWORK_CONNECTION);
      if (var3 != null) {
         Channel var4 = var3.n;
         String var5 = ModelAPI.PLUGIN.getName().toLowerCase().replace(" ", "_");
         var4.eventLoop().submit(() -> {
            var4.pipeline().remove(var5 + "_protected_unpacker");
            var4.pipeline().remove(var5 + "_model_handler");
            return null;
         });
         UUID var6 = var1.getUniqueId();
         this.removePipeline(var6);
         this.playerAggregators.remove(var6);
      }
   }

   public void ping(UUID var1) {
      this.getPipeline(var1).ifPresent((var1x) -> {
         int var2 = var1x.getDesyncMonitor().getPingCounter().getAndIncrement();
         PacketTransmissionUtility.transmitToPlayer(var1, new ClientboundPingPacket(var2));
      });
   }

   public void startBatch() {
      this.batchMode = true;
   }

   public void endBatch() {
      this.flushAllAggregators();
      this.batchMode = false;
   }

   private void flushAllAggregators() {
      this.playerAggregators.entrySet().parallelStream().forEach((var1) -> {
         UUID var2 = (UUID)var1.getKey();
         PacketAggregator var3 = (PacketAggregator)var1.getValue();
         PipelineWrapper var4 = (PipelineWrapper)this.playerChannels.get(var2);
         if (var4 != null) {
            Objects.requireNonNull(var4);
            var3.flush(var4::writeAndFlush);
            var3.reset();
         }

      });
   }

   public void appendPacket(UUID var1, Packet<? super PacketListenerPlayOut> var2) {
      PacketAggregator var3 = (PacketAggregator)this.playerAggregators.get(var1);
      if (var3 != null) {
         var3.addPacket(var2);
      }

   }

   public void appendPackets(UUID var1, Collection<Packet<? super PacketListenerPlayOut>> var2) {
      PacketAggregator var3 = (PacketAggregator)this.playerAggregators.get(var1);
      if (var3 != null) {
         var3.addPackets(var2);
      }

   }

   public boolean isBatching() {
      return this.batchMode;
   }
}
