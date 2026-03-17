package advancedplugins.pm2.cv.models.v1_21_R5.network.utils;

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

public class Bundler {
   private static int BUNDLE_SIZE;
   private final List<List<Packet<? super ClientGamePacketListener>>> bundles = new ArrayList();

   public void appendPacket(Packet<? super ClientGamePacketListener> var1) {
      if (this.bundles.isEmpty()) {
         this.bundles.add(new ArrayList());
      }

      Object var2 = (List)this.bundles.getLast();
      if (((List)var2).size() == BUNDLE_SIZE) {
         var2 = new ArrayList();
         this.bundles.add(var2);
      }

      ((List)var2).add(var1);
   }

   public void appendPacket(Collection<Packet<? super ClientGamePacketListener>> var1) {
      if (this.bundles.isEmpty()) {
         this.bundles.add(new ArrayList());
      }

      Object var2 = (List)this.bundles.getLast();
      if (((List)var2).size() + var1.size() > BUNDLE_SIZE) {
         var2 = new ArrayList();
         this.bundles.add(var2);
      }

      ((List)var2).addAll(var1);
   }

   public void clear() {
      this.bundles.clear();
   }

   public void bundle(Consumer<Object> var1) {
      Iterator var2 = this.bundles.iterator();

      while(var2.hasNext()) {
         List var3 = (List)var2.next();
         ProtectedPacket var4 = new ProtectedPacket(new ClientboundBundlePacket(var3));
         var1.accept(var4);
      }

   }

   static {
      ModelAPI.getAPI().getConfigManager().registerReferenceUpdate(() -> {
         BUNDLE_SIZE = Math.min(ConfigProperty.BUNDLE_SIZE.getInt(), 4096);
      });
   }
}
