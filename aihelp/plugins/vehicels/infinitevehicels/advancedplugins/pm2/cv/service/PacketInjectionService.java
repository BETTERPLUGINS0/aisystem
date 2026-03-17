package advancedplugins.pm2.cv.service;

import advancedplugins.pm2.cv.api.enums.EnumInjectorPriority;
import advancedplugins.pm2.cv.api.service.Service;
import com.google.common.collect.Sets;
import io.netty.channel.Channel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PacketInjectionService implements Service {
   private final Map<Class<?>, Set<PacketInjectionService.Entry>> entries = new ConcurrentHashMap();

   public void register(@NotNull PacketInjectionService.Injector injector) {
      Class[] var2 = var1.packetTypes();
      Class[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Class var6 = var3[var5];
         ((Set)this.entries.computeIfAbsent(var6, (var0) -> {
            return Sets.newConcurrentHashSet();
         })).add(new PacketInjectionService.Entry(var1.priority(), var1));
      }

   }

   public void unregister(@NotNull PacketInjectionService.Injector injector) {
      Iterator var2 = this.entries.values().iterator();

      while(var2.hasNext()) {
         Set var3 = (Set)var2.next();
         var3.removeIf((var1x) -> {
            return Objects.equals(var1x.injector, var1);
         });
      }

   }

   @Nullable
   public Object processClientPacket(@Nullable Player sender, @NotNull Channel channel, @NotNull Object packet) {
      Object var4 = var3;
      Iterator var5 = this.matchEntries(var3.getClass()).iterator();

      while(var5.hasNext()) {
         PacketInjectionService.Entry var6 = (PacketInjectionService.Entry)var5.next();
         if ((var4 = var6.injector.onPacketReceive(var1, var2, var3)) == null) {
            break;
         }
      }

      return var4;
   }

   @Nullable
   public Object processServerPacket(@Nullable Player receiver, @NotNull Channel channel, @NotNull Object packet) {
      Object var4 = var3;
      Iterator var5 = this.matchEntries(var3.getClass()).iterator();

      while(var5.hasNext()) {
         PacketInjectionService.Entry var6 = (PacketInjectionService.Entry)var5.next();
         if ((var4 = var6.injector.onPacketSend(var1, var2, var3)) == null) {
            break;
         }
      }

      return var4;
   }

   private List<PacketInjectionService.Entry> matchEntries(@NotNull Class<?> packetType) {
      ArrayList var2 = new ArrayList();
      Set var3 = (Set)this.entries.get(var1);
      if (var3 != null && !var3.isEmpty()) {
         var2.addAll(var3);
         var2.sort((var0, var1x) -> {
            int var2 = var0.priority.ordinal();
            int var3 = var1x.priority.ordinal();
            if (var2 != var3) {
               return var2 < var3 ? -1 : 1;
            } else {
               return 0;
            }
         });
         return var2;
      } else {
         return var2;
      }
   }

   public interface Injector {
      @NotNull
      Class<?>[] packetTypes();

      @NotNull
      EnumInjectorPriority priority();

      @Nullable
      Object onPacketReceive(@Nullable Player sender, @NotNull Channel channel, @NotNull Object packet);

      @Nullable
      Object onPacketSend(@Nullable Player receiver, @NotNull Channel channel, @NotNull Object packet);
   }

   private static class Entry {
      @NotNull
      private final EnumInjectorPriority priority;
      @NotNull
      private final PacketInjectionService.Injector injector;

      public Entry(@NotNull EnumInjectorPriority priority, @NotNull PacketInjectionService.Injector injector) {
         this.priority = var1;
         this.injector = var2;
      }
   }
}
