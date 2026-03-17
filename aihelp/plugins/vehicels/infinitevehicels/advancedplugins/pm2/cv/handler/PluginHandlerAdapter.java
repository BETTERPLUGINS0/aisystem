package advancedplugins.pm2.cv.handler;

import advancedplugins.pm2.cv.api.enums.EnumInjectorPriority;
import advancedplugins.pm2.cv.api.handler.PluginHandler;
import advancedplugins.pm2.cv.enums.EnumPacketType;
import advancedplugins.pm2.cv.service.PacketInjectionService;
import io.netty.channel.Channel;
import java.util.Objects;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PluginHandlerAdapter implements PluginHandler, Listener, PacketInjectionService.Injector {
   private final Class<?>[] injectorPacketTypes;
   private final EnumInjectorPriority injectorPriority;

   public PluginHandlerAdapter(@Nullable EnumInjectorPriority injectorPriority, @Nullable EnumPacketType... injectorPacketTypes) {
      this.injectorPriority = var1 != null ? var1 : EnumInjectorPriority.NORMAL;
      if (var2 != null && var2.length > 0) {
         this.injectorPacketTypes = new Class[var2.length];

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.injectorPacketTypes[var3] = ((EnumPacketType)Objects.requireNonNull(var2[var3])).getPacketClass();
         }
      } else {
         this.injectorPacketTypes = new Class[0];
      }

   }

   public PluginHandlerAdapter() {
      this((EnumInjectorPriority)null);
   }

   @NotNull
   public Class<?>[] packetTypes() {
      return this.injectorPacketTypes;
   }

   @NotNull
   public EnumInjectorPriority priority() {
      return this.injectorPriority;
   }

   @Nullable
   public Object onPacketReceive(@Nullable Player sender, @NotNull Channel channel, @NotNull Object packet) {
      return var3;
   }

   @Nullable
   public Object onPacketSend(@Nullable Player receiver, @NotNull Channel channel, @NotNull Object packet) {
      return var3;
   }
}
