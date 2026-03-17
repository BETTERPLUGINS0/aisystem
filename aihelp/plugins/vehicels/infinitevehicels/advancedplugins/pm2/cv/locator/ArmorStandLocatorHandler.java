package advancedplugins.pm2.cv.locator;

import advancedplugins.pm2.cv.api.enums.EnumInjectorPriority;
import advancedplugins.pm2.cv.enums.EnumPacketType;
import advancedplugins.pm2.cv.handler.PluginHandlerAdapter;
import advancedplugins.pm2.cv.handler.PluginHandlerOptions;
import advancedplugins.pm2.cv.nms.NmsImplementations;
import advancedplugins.pm2.cv.packet.PacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.TeleportEntityPacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.UpdateEntityPositionPacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.UpdateEntityPositionRotationPacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.UpdateEntityRotationPacketWrapper;
import gnu.trove.set.hash.THashSet;
import io.netty.channel.Channel;
import java.util.Objects;
import java.util.Set;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@PluginHandlerOptions(
   packetInjector = true
)
public class ArmorStandLocatorHandler extends PluginHandlerAdapter {
   @NotNull
   private final ArmorStandLocator locator = (ArmorStandLocator)Objects.requireNonNull((ArmorStandLocator)NmsImplementations.getSingleInstanceImplementation(ArmorStandLocator.class));
   private final Set<Integer> handling = new THashSet();

   public ArmorStandLocatorHandler() {
      super(EnumInjectorPriority.HIGHEST, EnumPacketType.OUTGOING_TELEPORT_ENTITY, EnumPacketType.OUTGOING_UPDATE_ENTITY_POSITION, EnumPacketType.OUTGOING_UPDATE_ENTITY_ROTATION, EnumPacketType.OUTGOING_UPDATE_ENTITY_POSITION_ROTATION);
   }

   @NotNull
   public ArmorStandLocator getLocator() {
      return this.locator;
   }

   public void handle(@NotNull ArmorStand armorStand) {
      this.handling.add(var1.getEntityId());
   }

   public void stopHandling(@NotNull ArmorStand armorStand) {
      this.handling.remove(var1.getEntityId());
   }

   public boolean isHandling(@NotNull ArmorStand armorStand) {
      return this.handling.contains(var1.getEntityId());
   }

   @Nullable
   public Object onPacketSend(@Nullable Player receiver, @NotNull Channel channel, @NotNull Object packet) {
      PacketWrapper var4 = PacketWrapper.of(var3);
      int var5;
      if (var4 instanceof TeleportEntityPacketWrapper) {
         var5 = ((TeleportEntityPacketWrapper)var4).entityId;
      } else if (var4 instanceof UpdateEntityPositionPacketWrapper) {
         var5 = ((UpdateEntityPositionPacketWrapper)var4).entityId;
      } else if (var4 instanceof UpdateEntityRotationPacketWrapper) {
         var5 = ((UpdateEntityRotationPacketWrapper)var4).entityId;
      } else {
         if (!(var4 instanceof UpdateEntityPositionRotationPacketWrapper)) {
            return var3;
         }

         var5 = ((UpdateEntityPositionRotationPacketWrapper)var4).entityId;
      }

      return this.handling.contains(var5) ? null : var3;
   }
}
