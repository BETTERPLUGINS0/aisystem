package advancedplugins.pm2.cv.models.v1_21_R4.network.utils;

import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

public class PacketInterceptor<P extends PacketListener> {
   private final Map<Class<? extends Packet<? super P>>, PacketInterceptor<P>.Modifier<? extends Packet<? super P>>> registry = Maps.newConcurrentMap();
   private final Map<Class<? extends Packet<? super P>>, PacketInterceptor<P>.Listener<? extends Packet<? super P>>> postRegistry = Maps.newConcurrentMap();

   public <T extends Packet<? super P>> PacketInterceptor<P> register(Class<T> clazz, Function<T, Packet<? super P>> function) {
      this.registry.put(var1, new PacketInterceptor.Modifier(this, this, var1, var2));
      return this;
   }

   public <T extends Packet<? super P>> PacketInterceptor<P> registerPost(Class<T> clazz, Function<T, Collection<Packet<? super P>>> consumer) {
      this.postRegistry.put(var1, new PacketInterceptor.Listener(this, this, var1, var2));
      return this;
   }

   public Packet<? super P> accept(Packet<? super P> original) {
      if (var1 == null) {
         return null;
      } else {
         PacketInterceptor.Modifier var2 = (PacketInterceptor.Modifier)this.registry.get(var1.getClass());
         return var2 == null ? var1 : var2.modify(var1);
      }
   }

   public Collection<Packet<? super P>> acceptPost(Packet<? super P> original) {
      if (var1 == null) {
         return List.of();
      } else {
         PacketInterceptor.Listener var2 = (PacketInterceptor.Listener)this.postRegistry.get(var1.getClass());
         return (Collection)(var2 != null ? var2.listen(var1) : List.of());
      }
   }

   class Modifier<T extends Packet<? super P>> {
      private final Class<T> clazz;
      private final Function<T, Packet<? super P>> function;

      public Modifier(final PacketInterceptor this$0, final PacketInterceptor param1, final Class clazz, final Function function) {
         this.clazz = var3;
         this.function = var4;
      }

      public Packet<? super P> modify(Packet<? super P> original) {
         try {
            return (Packet)this.function.apply((Packet)this.clazz.cast(var1));
         } catch (Throwable var3) {
            LogUtil.error("An error had occurred while modifying the packet " + this.clazz.getSimpleName());
            var3.printStackTrace();
            return var1;
         }
      }
   }

   class Listener<T extends Packet<? super P>> {
      private final Class<T> clazz;
      private final Function<T, Collection<Packet<? super P>>> function;

      public Listener(final PacketInterceptor this$0, final PacketInterceptor param1, final Class clazz, final Function function) {
         this.clazz = var3;
         this.function = var4;
      }

      public Collection<Packet<? super P>> listen(Packet<?> original) {
         try {
            Collection var2 = (Collection)this.function.apply((Packet)this.clazz.cast(var1));
            return (Collection)(var2 == null ? List.of() : var2);
         } catch (Throwable var3) {
            LogUtil.error("An error had occurred while intercepting the packet " + this.clazz.getSimpleName());
            var3.printStackTrace();
            return List.of();
         }
      }
   }
}
