package advancedplugins.pm2.cv.models.v1_21_R10.network.utils;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.nms.network.ProtectedPacket;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;

public class PacketAggregator {
   private static int MAX_BUNDLE_CAPACITY;
   private final List<List<Packet<? super ClientGamePacketListener>>> packetGroups = new ArrayList();

   public void addPacket(Packet<? super ClientGamePacketListener> var1) {
      this.ensureGroupExists();
      List var2 = this.getCurrentGroup();
      if (var2.size() >= MAX_BUNDLE_CAPACITY) {
         var2 = this.createNewGroup();
      }

      var2.add(var1);
   }

   public void addPackets(Collection<Packet<? super ClientGamePacketListener>> var1) {
      this.ensureGroupExists();
      List var2 = this.getCurrentGroup();
      if (var2.size() + var1.size() > MAX_BUNDLE_CAPACITY) {
         var2 = this.createNewGroup();
      }

      var2.addAll(var1);
   }

   private void ensureGroupExists() {
      if (this.packetGroups.isEmpty()) {
         this.packetGroups.add(new ArrayList());
      }

   }

   private List<Packet<? super ClientGamePacketListener>> getCurrentGroup() {
      return (List)this.packetGroups.get(this.packetGroups.size() - 1);
   }

   private List<Packet<? super ClientGamePacketListener>> createNewGroup() {
      ArrayList var1 = new ArrayList();
      this.packetGroups.add(var1);
      return var1;
   }

   public void reset() {
      this.packetGroups.clear();
   }

   public void flush(Consumer<Object> var1) {
      Iterator var2 = this.packetGroups.iterator();

      while(var2.hasNext()) {
         List var3 = (List)var2.next();
         ProtectedPacket var4 = new ProtectedPacket(new ClientboundBundlePacket(var3));
         var1.accept(var4);
      }

   }

   static {
      ModelAPI.getAPI().getConfigManager().registerReferenceUpdate(() -> {
         MAX_BUNDLE_CAPACITY = Math.min(ConfigProperty.BUNDLE_SIZE.getInt(), 4096);
      });
   }
}
