package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3f;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ParticleDustData extends ParticleData {
   private float scale;
   private Color color;

   public ParticleDustData(float scale, float red, float green, float blue) {
      this(scale, new Color(red, green, blue));
   }

   public ParticleDustData(float scale, float[] rgb) {
      this(scale, rgb[0], rgb[1], rgb[2]);
   }

   public ParticleDustData(float scale, Vector3f rgb) {
      this(scale, rgb.getX(), rgb.getY(), rgb.getZ());
   }

   public ParticleDustData(float scale, int red, int green, int blue) {
      this(scale, new Color(red, green, blue));
   }

   public ParticleDustData(float scale, Color color) {
      this.scale = scale;
      this.color = color;
   }

   public static ParticleDustData read(PacketWrapper<?> wrapper) {
      Color color;
      float scale;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         color = new Color(wrapper.readInt());
      } else {
         scale = wrapper.readFloat();
         float green = wrapper.readFloat();
         float blue = wrapper.readFloat();
         color = new Color(scale, green, blue);
      }

      scale = wrapper.readFloat();
      return new ParticleDustData(scale, color);
   }

   public static void write(PacketWrapper<?> wrapper, ParticleDustData data) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         wrapper.writeInt(data.color.asRGB());
      } else {
         wrapper.writeFloat(data.getRed());
         wrapper.writeFloat(data.getGreen());
         wrapper.writeFloat(data.getBlue());
      }

      wrapper.writeFloat(data.scale);
   }

   public static ParticleDustData decode(NBTCompound compound, ClientVersion version) {
      Color color = Color.decode(compound.getTagOrThrow("color"), version);
      float scale = compound.getNumberTagOrThrow("scale").getAsFloat();
      return new ParticleDustData(scale, color);
   }

   public static void encode(ParticleDustData data, ClientVersion version, NBTCompound compound) {
      compound.setTag("color", Color.encode(data.color, version));
      compound.setTag("scale", new NBTFloat(data.scale));
   }

   public boolean isEmpty() {
      return false;
   }

   public float getRed() {
      return (float)this.color.red() / 255.0F;
   }

   public void setRed(float red) {
      this.color = new Color(red, this.getGreen(), this.getBlue());
   }

   public float getGreen() {
      return (float)this.color.green() / 255.0F;
   }

   public void setGreen(float green) {
      this.color = new Color(this.getRed(), green, this.getBlue());
   }

   public float getBlue() {
      return (float)this.color.blue() / 255.0F;
   }

   public void setBlue(float blue) {
      this.color = new Color(this.getRed(), this.getGreen(), blue);
   }

   public float getScale() {
      return this.scale;
   }

   public void setScale(float scale) {
      this.scale = scale;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ParticleDustData that = (ParticleDustData)obj;
         return Float.compare(that.scale, this.scale) != 0 ? false : this.color.equals(that.color);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.scale, this.color});
   }
}
