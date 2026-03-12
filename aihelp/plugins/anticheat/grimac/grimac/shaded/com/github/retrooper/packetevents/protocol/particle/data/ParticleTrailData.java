package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.color.Color;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3d;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ParticleTrailData extends ParticleData {
   private static final int FALLBACK_DURATION = 25;
   private Vector3d target;
   private Color color;
   private int duration;

   public ParticleTrailData(Vector3d target, Color color) {
      this(target, color, 25);
   }

   public ParticleTrailData(Vector3d target, Color color, int duration) {
      this.target = target;
      this.color = color;
      this.duration = duration;
   }

   public static ParticleTrailData read(PacketWrapper<?> wrapper) {
      Vector3d target = Vector3d.read(wrapper);
      Color color = new Color(wrapper.readInt());
      int duration = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4) ? wrapper.readVarInt() : 25;
      return new ParticleTrailData(target, color, duration);
   }

   public static void write(PacketWrapper<?> wrapper, ParticleTrailData data) {
      Vector3d.write(wrapper, data.target);
      wrapper.writeInt(data.color.asRGB());
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
         wrapper.writeVarInt(data.duration);
      }

   }

   public static ParticleTrailData decode(NBTCompound compound, ClientVersion version) {
      Vector3d target = Vector3d.decode(compound.getTagOrThrow("target"), version);
      Color color = Color.decode(compound.getTagOrThrow("color"), version);
      int duration = 25;
      if (version.isNewerThanOrEquals(ClientVersion.V_1_21_4)) {
         duration = compound.getNumberTagOrThrow("duration").getAsInt();
      }

      return new ParticleTrailData(target, color, duration);
   }

   public static void encode(ParticleTrailData data, ClientVersion version, NBTCompound compound) {
      compound.setTag("target", Vector3d.encode(data.target, version));
      compound.setTag("color", Color.encode(data.color, version));
      if (version.isNewerThanOrEquals(ClientVersion.V_1_21_4)) {
         compound.setTag("duration", new NBTInt(data.duration));
      }

   }

   public boolean isEmpty() {
      return false;
   }

   public Vector3d getTarget() {
      return this.target;
   }

   public void setTarget(Vector3d target) {
      this.target = target;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color color) {
      this.color = color;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ParticleTrailData that = (ParticleTrailData)obj;
         if (this.duration != that.duration) {
            return false;
         } else {
            return !this.target.equals(that.target) ? false : this.color.equals(that.color);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.target, this.color, this.duration});
   }
}
