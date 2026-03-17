package advancedplugins.pm2.cv.locator;

import advancedplugins.pm2.cv.nms.NmsImplementations;
import advancedplugins.pm2.cv.service.PacketService;
import advancedplugins.pm2.cv.util.Constants;
import gnu.trove.map.hash.THashMap;
import io.netty.channel.ChannelPipeline;
import java.util.Map;
import java.util.Set;
import me.PM2.infinitevehicles.math.util.FastMath;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.NotNull;

public abstract class ArmorStandLocator {
   public static final int FLAG_FORCE = 2;
   public static final int FLAG_FORCE_TELEPORT = 8;
   @NotNull
   protected final PacketService packetService = (PacketService)NmsImplementations.getSingleInstanceImplementation(PacketService.class);
   @NotNull
   protected final Map<Integer, ArmorStandLocator.Entry> entries = new THashMap();
   protected boolean reducedCalls = true;

   public abstract void setLocation(@NotNull ArmorStand armorStand, double x, double y, double z, float yaw, float pitch, int flags);

   public abstract void setLocation(@NotNull ArmorStand armorStand, double x, double y, double z, int flags);

   @NotNull
   public abstract Set<ChannelPipeline> writeLocation(@NotNull ArmorStand armorStand, double x, double y, double z, float yaw, float pitch, int flags);

   @NotNull
   public abstract Set<ChannelPipeline> writeLocation(@NotNull ArmorStand armorStand, double x, double y, double z, int flags);

   @NotNull
   protected ArmorStandLocator.Entry getEntry(int entityId) {
      return (ArmorStandLocator.Entry)this.entries.computeIfAbsent(var1, (var0) -> {
         return new ArmorStandLocator.Entry();
      });
   }

   @NotNull
   protected ArmorStandLocator.Entry getEntry(@NotNull ArmorStand armorStand) {
      return this.getEntry(var1.getEntityId());
   }

   protected boolean isFlagPresent(int flags, int flag) {
      return (var1 & var2) != 0;
   }

   public boolean isReducedCalls() {
      return this.reducedCalls;
   }

   public void setReducedCalls(final boolean reducedCalls) {
      this.reducedCalls = var1;
   }

   protected static class Entry {
      protected double lastXSet;
      protected double lastYSet;
      protected double lastZSet;
      protected double lastServerX;
      protected double lastServerY;
      protected double lastServerZ;
      protected double lastXSent;
      protected double lastYSent;
      protected double lastZSent;
      protected boolean firstTimeSet = true;
      protected boolean firstServerUpdate = true;
      protected boolean firstTimeSent = true;
      protected Long lastTeleportTimestamp;

      public boolean shouldSet(double newX, double newY, double newZ) {
         if (this.firstTimeSet) {
            return true;
         } else {
            return Double.compare(this.lastXSet, var1) != 0 || Double.compare(this.lastYSet, var3) != 0 || Double.compare(this.lastZSet, var5) != 0;
         }
      }

      public boolean shouldUpdateServer(double newX, double newY, double newZ) {
         if (this.firstServerUpdate) {
            return true;
         } else {
            return FastMath.abs(this.lastServerX - var1) >= 8.0D || FastMath.abs(this.lastServerY - var3) >= 8.0D || FastMath.abs(this.lastServerZ - var5) >= 8.0D;
         }
      }

      public boolean shouldTeleport() {
         if (this.firstTimeSent) {
            return true;
         } else {
            return this.lastTeleportTimestamp != null && System.currentTimeMillis() - this.lastTeleportTimestamp >= Constants.FORCE_TELEPORT_PERIOD;
         }
      }

      public void teleported() {
         this.lastTeleportTimestamp = System.currentTimeMillis();
      }

      public double getLastXSet() {
         return this.lastXSet;
      }

      public void setLastXSet(final double lastXSet) {
         this.lastXSet = var1;
      }

      public double getLastYSet() {
         return this.lastYSet;
      }

      public void setLastYSet(final double lastYSet) {
         this.lastYSet = var1;
      }

      public double getLastZSet() {
         return this.lastZSet;
      }

      public void setLastZSet(final double lastZSet) {
         this.lastZSet = var1;
      }

      public double getLastServerX() {
         return this.lastServerX;
      }

      public void setLastServerX(final double lastServerX) {
         this.lastServerX = var1;
      }

      public double getLastServerY() {
         return this.lastServerY;
      }

      public void setLastServerY(final double lastServerY) {
         this.lastServerY = var1;
      }

      public double getLastServerZ() {
         return this.lastServerZ;
      }

      public void setLastServerZ(final double lastServerZ) {
         this.lastServerZ = var1;
      }

      public double getLastXSent() {
         return this.lastXSent;
      }

      public void setLastXSent(final double lastXSent) {
         this.lastXSent = var1;
      }

      public double getLastYSent() {
         return this.lastYSent;
      }

      public void setLastYSent(final double lastYSent) {
         this.lastYSent = var1;
      }

      public double getLastZSent() {
         return this.lastZSent;
      }

      public void setLastZSent(final double lastZSent) {
         this.lastZSent = var1;
      }

      public boolean isFirstTimeSet() {
         return this.firstTimeSet;
      }

      public void setFirstTimeSet(final boolean firstTimeSet) {
         this.firstTimeSet = var1;
      }

      public boolean isFirstServerUpdate() {
         return this.firstServerUpdate;
      }

      public void setFirstServerUpdate(final boolean firstServerUpdate) {
         this.firstServerUpdate = var1;
      }

      public boolean isFirstTimeSent() {
         return this.firstTimeSent;
      }

      public void setFirstTimeSent(final boolean firstTimeSent) {
         this.firstTimeSent = var1;
      }

      public Long getLastTeleportTimestamp() {
         return this.lastTeleportTimestamp;
      }

      public void setLastTeleportTimestamp(final Long lastTeleportTimestamp) {
         this.lastTeleportTimestamp = var1;
      }
   }
}
