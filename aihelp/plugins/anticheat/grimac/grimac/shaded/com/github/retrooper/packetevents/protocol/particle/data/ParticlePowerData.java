package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ParticlePowerData extends ParticleData {
   private float power;

   public ParticlePowerData(float power) {
      this.power = power;
   }

   public static ParticlePowerData read(PacketWrapper<?> wrapper) {
      float power = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_9) ? 1.0F : wrapper.readFloat();
      return new ParticlePowerData(power);
   }

   public static void write(PacketWrapper<?> wrapper, ParticlePowerData data) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         wrapper.writeFloat(data.power);
      }

   }

   @ApiStatus.Internal
   public static ParticlePowerData decode(NBTCompound tag, ClientVersion version) {
      float power = tag.getNumberTagValueOrDefault("power", 1.0F).floatValue();
      return new ParticlePowerData(power);
   }

   @ApiStatus.Internal
   public static void encode(ParticlePowerData data, ClientVersion version, NBTCompound tag) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_21_9) && data.power != 1.0F) {
         tag.setTag("power", new NBTFloat(data.power));
      }

   }

   public boolean isEmpty() {
      return false;
   }

   public float getPower() {
      return this.power;
   }

   public void setPower(float power) {
      this.power = power;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ParticlePowerData that = (ParticlePowerData)obj;
         return Float.compare(that.power, this.power) == 0;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.power);
   }
}
