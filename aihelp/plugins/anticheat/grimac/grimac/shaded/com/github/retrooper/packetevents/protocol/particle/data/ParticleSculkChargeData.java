package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ParticleSculkChargeData extends ParticleData {
   private float roll;

   public ParticleSculkChargeData(float roll) {
      this.roll = roll;
   }

   public float getRoll() {
      return this.roll;
   }

   public void setRoll(float roll) {
      this.roll = roll;
   }

   public static ParticleSculkChargeData read(PacketWrapper<?> wrapper) {
      return new ParticleSculkChargeData(wrapper.readFloat());
   }

   public static void write(PacketWrapper<?> wrapper, ParticleSculkChargeData data) {
      wrapper.writeFloat(data.getRoll());
   }

   public static ParticleSculkChargeData decode(NBTCompound compound, ClientVersion version) {
      float roll = compound.getNumberTagOrThrow("roll").getAsFloat();
      return new ParticleSculkChargeData(roll);
   }

   public static void encode(ParticleSculkChargeData data, ClientVersion version, NBTCompound compound) {
      compound.setTag("roll", new NBTFloat(data.roll));
   }

   public boolean isEmpty() {
      return false;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ParticleSculkChargeData that = (ParticleSculkChargeData)obj;
         return Float.compare(that.roll, this.roll) == 0;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.roll);
   }
}
