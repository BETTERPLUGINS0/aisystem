package advancedplugins.pm2.cv.models.v1_21_R7.network.utils;

import advancedplugins.pm2.cv.models.api.utils.data.NullableHashSet;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import org.jetbrains.annotations.NotNull;

public class PacketBundleProvider extends LinkedHashSet<PacketBundleProvider.PacketFactory> {
   public PacketBundleProvider(int var1, float var2) {
      super(var1, var2);
   }

   public PacketBundleProvider(int var1) {
      super(var1);
   }

   public PacketBundleProvider() {
   }

   public PacketBundleProvider(@NotNull Collection<? extends PacketBundleProvider.PacketFactory> var1) {
      super(var1);
   }

   public boolean add(PacketBundleProvider.PacketFactory var1) {
      return var1 != null && super.add(var1);
   }

   public boolean addStaticPacket(Packet<PacketListenerPlayOut> var1) {
      return this.add((var1x) -> {
         return var1;
      });
   }

   private Collection<Packet<? super PacketListenerPlayOut>> generatePackets(UUID var1) {
      return (Collection)this.stream().map((var1x) -> {
         return var1x.createPacket(var1);
      }).collect(Collectors.toCollection(NullableHashSet::new));
   }

   public void compile(UUID var1, Consumer<Collection<Packet<? super PacketListenerPlayOut>>> var2) {
      Collection var3 = this.generatePackets(var1);
      if (!var3.isEmpty()) {
         var2.accept(var3);
      }

   }

   @FunctionalInterface
   public interface PacketFactory {
      Packet<PacketListenerPlayOut> createPacket(UUID var1);
   }
}
