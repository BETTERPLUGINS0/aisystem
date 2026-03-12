package ac.grim.grimac.checks.impl.velocity;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.LpVector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import io.netty.buffer.Unpooled;
import lombok.Generated;

public final class VectorPrecisionConverter {
   private static final ServerVersion SERVER_VERSION = PacketEvents.getAPI().getServerManager().getVersion();
   private static final double PRECISION_LOSS_FIX = 1.0E-11D;

   public static Vector3d convert(ClientVersion version, Vector3d vector) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_21_9) && SERVER_VERSION.isOlderThanOrEquals(ServerVersion.V_1_21_8)) {
         return legacyToLp(vector);
      } else {
         return version.isOlderThanOrEquals(ClientVersion.V_1_21_7) && SERVER_VERSION.isNewerThanOrEquals(ServerVersion.V_1_21_9) ? lpToLegacy(vector) : vector;
      }
   }

   public static Vector3d legacyToLp(Vector3d legacy) {
      PacketWrapper<?> wrapper = PacketWrapper.createUniversalPacketWrapper(Unpooled.buffer());
      LpVector3d.write(wrapper, legacy);
      return LpVector3d.read(wrapper);
   }

   public static Vector3d lpToLegacy(Vector3d lp) {
      int xi = (int)(lp.x * 8000.0D + Math.copySign(1.0E-11D, lp.x));
      int yi = (int)(lp.y * 8000.0D + Math.copySign(1.0E-11D, lp.y));
      int zi = (int)(lp.z * 8000.0D + Math.copySign(1.0E-11D, lp.z));
      short x = (short)xi;
      short y = (short)yi;
      short z = (short)zi;
      return new Vector3d((double)x / 8000.0D, (double)y / 8000.0D, (double)z / 8000.0D);
   }

   @Generated
   private VectorPrecisionConverter() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
