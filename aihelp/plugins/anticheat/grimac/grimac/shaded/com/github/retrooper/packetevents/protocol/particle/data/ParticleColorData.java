package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.AlphaColor;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ParticleColorData extends ParticleData {
   private AlphaColor color;

   public ParticleColorData(int color) {
      this(new AlphaColor(color));
   }

   public ParticleColorData(AlphaColor color) {
      this.color = color;
   }

   public static ParticleColorData read(PacketWrapper<?> wrapper) {
      int color = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5) ? wrapper.readInt() : 0;
      return new ParticleColorData(color);
   }

   public static void write(PacketWrapper<?> wrapper, ParticleColorData data) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
         wrapper.writeInt(data.color.asRGB());
      }

   }

   public static ParticleColorData decode(NBTCompound compound, ClientVersion version) {
      AlphaColor argb;
      if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         NBT colorTag = compound.getTagOrThrow("color");
         argb = AlphaColor.decode(colorTag, version);
      } else {
         argb = AlphaColor.WHITE;
      }

      return new ParticleColorData(argb);
   }

   public static void encode(ParticleColorData data, ClientVersion version, NBTCompound compound) {
      if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
         compound.setTag("color", AlphaColor.encode(data.color, version));
      }

   }

   public int getColor() {
      return this.color.asRGB();
   }

   public void setColor(int color) {
      this.color = new AlphaColor(color);
   }

   public void setAlphaColor(AlphaColor color) {
      this.color = color;
   }

   public boolean isEmpty() {
      return false;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ParticleColorData that = (ParticleColorData)obj;
         return this.color.equals(that.color);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.color);
   }
}
