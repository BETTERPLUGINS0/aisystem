package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ParticleSpellData extends ParticleData {
   private Color color;
   private float power;

   public ParticleSpellData(Color color, float power) {
      this.color = color;
      this.power = power;
   }

   public static ParticleSpellData read(PacketWrapper<?> wrapper) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         Color color = Color.read(wrapper);
         float power = wrapper.readFloat();
         return new ParticleSpellData(color, power);
      } else {
         return new ParticleSpellData(Color.WHITE, 1.0F);
      }
   }

   public static void write(PacketWrapper<?> wrapper, ParticleSpellData data) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         Color.write(wrapper, data.color);
         wrapper.writeFloat(data.power);
      }

   }

   @ApiStatus.Internal
   public static ParticleSpellData decode(NBTCompound compound, ClientVersion version) {
      Color color = (Color)compound.getOr("color", Color::decode, Color.WHITE, (PacketWrapper)null);
      float power = compound.getNumberTagValueOrDefault("power", 1.0F).floatValue();
      return new ParticleSpellData(color, power);
   }

   @ApiStatus.Internal
   public static void encode(ParticleSpellData data, ClientVersion version, NBTCompound compound) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_21_9)) {
         if (!Color.WHITE.equals(data.color)) {
            compound.setTag("color", Color.encode(data.color, version));
         }

         if (data.power != 1.0F) {
            compound.setTag("power", new NBTFloat(data.power));
         }
      }

   }

   public boolean isEmpty() {
      return false;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   public float getPower() {
      return this.power;
   }

   public void setPower(float power) {
      this.power = power;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ParticleSpellData that = (ParticleSpellData)obj;
         return Float.compare(that.power, this.power) != 0 ? false : this.color.equals(that.color);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.color, this.power});
   }
}
