package advancedplugins.pm2.cv.models.v1_21_R4.network.utils;

import advancedplugins.pm2.cv.models.api.utils.data.NullableHashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import org.jetbrains.annotations.NotNull;

public class Packets extends LinkedHashSet<Packets.PacketSupplier> {
   public Packets(int initialCapacity, float loadFactor) {
      super(var1, var2);
   }

   public Packets(int initialCapacity) {
      super(var1);
   }

   public Packets() {
   }

   public Packets(@NotNull Collection<? extends Packets.PacketSupplier> c) {
      super(var1);
   }

   public boolean add(Packets.PacketSupplier supplier) {
      return var1 == null ? false : super.add(var1);
   }

   public boolean add(Packet<PacketListenerPlayOut> packet) {
      return this.add((var1x) -> {
         return var1;
      });
   }

   private Collection<Packet<? super PacketListenerPlayOut>> compile(UUID player) {
      NullableHashSet var2 = new NullableHashSet();
      Iterator var3 = this.iterator();

      while(var3.hasNext()) {
         Packets.PacketSupplier var4 = (Packets.PacketSupplier)var3.next();
         var2.add(var4.supply(var1));
      }

      return var2;
   }

   public void compile(UUID player, Consumer<Collection<Packet<? super PacketListenerPlayOut>>> ifNotEmpty) {
      Collection var3 = this.compile(var1);
      if (!var3.isEmpty()) {
         var2.accept(var3);
      }

   }

   @FunctionalInterface
   public interface PacketSupplier {
      Packet<PacketListenerPlayOut> supply(UUID var1);
   }
}
