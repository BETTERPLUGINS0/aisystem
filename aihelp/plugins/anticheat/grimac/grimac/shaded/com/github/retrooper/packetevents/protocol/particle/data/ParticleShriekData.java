package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ParticleShriekData extends ParticleData {
   private int delay;

   public ParticleShriekData(int delay) {
      this.delay = delay;
   }

   public int getDelay() {
      return this.delay;
   }

   public void setDelay(int delay) {
      this.delay = delay;
   }

   public static ParticleShriekData read(PacketWrapper<?> wrapper) {
      return new ParticleShriekData(wrapper.readVarInt());
   }

   public static void write(PacketWrapper<?> wrapper, ParticleShriekData data) {
      wrapper.writeVarInt(data.getDelay());
   }

   public static ParticleShriekData decode(NBTCompound compound, ClientVersion version) {
      int delay = compound.getNumberTagOrThrow("delay").getAsInt();
      return new ParticleShriekData(delay);
   }

   public static void encode(ParticleShriekData data, ClientVersion version, NBTCompound compound) {
      compound.setTag("delay", new NBTInt(data.delay));
   }

   public boolean isEmpty() {
      return false;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ParticleShriekData that = (ParticleShriekData)obj;
         return this.delay == that.delay;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.delay);
   }
}
