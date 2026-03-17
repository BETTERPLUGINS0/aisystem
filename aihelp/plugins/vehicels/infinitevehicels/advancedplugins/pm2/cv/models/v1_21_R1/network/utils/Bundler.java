package advancedplugins.pm2.cv.models.v1_21_R1.network.utils;

import advancedplugins.pm2.cv.models.api.nms.network.ProtectedPacket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;

public class Bundler {
   private static final int BUNDLE_SIZE = 4096;
   private final List<List<Packet<? super PacketListenerPlayOut>>> bundles = new ArrayList();

   public void appendPacket(Packet<? super PacketListenerPlayOut> packet) {
      if (this.bundles.isEmpty()) {
         this.bundles.add(new ArrayList());
      }

      Object var2 = (List)this.bundles.getLast();
      if (((List)var2).size() == 4096) {
         var2 = new ArrayList();
         this.bundles.add(var2);
      }

      ((List)var2).add(var1);
   }

   public void appendPacket(Collection<Packet<? super PacketListenerPlayOut>> packets) {
      if (this.bundles.isEmpty()) {
         this.bundles.add(new ArrayList());
      }

      Object var2 = (List)this.bundles.getLast();
      if (((List)var2).size() + var1.size() > 4096) {
         var2 = new ArrayList();
         this.bundles.add(var2);
      }

      ((List)var2).addAll(var1);
   }

   public void clear() {
      this.bundles.clear();
   }

   public void bundle(Consumer<Object> send) {
      Iterator var2 = this.bundles.iterator();

      while(var2.hasNext()) {
         List var3 = (List)var2.next();
         ProtectedPacket var4 = new ProtectedPacket(new ClientboundBundlePacket(var3));
         var1.accept(var4);
      }

   }
}
