package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Particle<T extends ParticleData> {
   public static final NbtCodec<Particle<?>> CODEC = (new NbtMapCodec<Particle<?>>() {
      public Particle<?> decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
         ClientVersion version = wrapper.getServerVersion().toClientVersion();
         ParticleType<?> type = (ParticleType)compound.getOrThrow("type", ParticleTypes.CODEC, wrapper);
         ParticleData data = type.decodeData(compound, version);
         return new Particle(type, data);
      }

      public void encode(NBTCompound compound, PacketWrapper<?> wrapper, Particle<?> value) throws NbtCodecException {
         ParticleType<? super ParticleData> type = value.type;
         compound.setTag("type", new NBTString(type.getName().toString()));
         type.encodeData(value.getData(), wrapper.getServerVersion().toClientVersion(), compound);
      }
   }).codec();
   private ParticleType<T> type;
   private T data;

   public Particle(ParticleType<T> type, T data) {
      this.type = type;
      this.data = data;
   }

   public Particle(ParticleType<T> type) {
      this(type, ParticleData.emptyData());
   }

   public static Particle<?> read(PacketWrapper<?> wrapper) {
      ParticleType<?> type = (ParticleType)wrapper.readMappedEntity(ParticleTypes::getById);
      return new Particle(type, type.readData(wrapper));
   }

   public static <T extends ParticleData> void write(PacketWrapper<?> wrapper, Particle<T> particle) {
      wrapper.writeMappedEntity(particle.type);
      particle.getType().writeData(wrapper, particle.data);
   }

   /** @deprecated */
   @Deprecated
   public static Particle<?> decode(NBT nbt, ClientVersion version) {
      NBTCompound compound = (NBTCompound)nbt;
      NBT typeTag = compound.getTagOrThrow("type");
      ParticleType<?> type = typeTag instanceof NBTNumber ? ParticleTypes.getById(version, ((NBTNumber)typeTag).getAsInt()) : ParticleTypes.getByName(((NBTString)typeTag).getValue());
      ParticleData data = type.decodeData(compound, version);
      return new Particle(type, data);
   }

   /** @deprecated */
   @Deprecated
   public static <T extends ParticleData> NBT encode(Particle<T> particle, ClientVersion version) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("type", new NBTString(particle.type.getName().toString()));
      particle.type.encodeData(particle.getData(), version, compound);
      return compound;
   }

   public ParticleType<T> getType() {
      return this.type;
   }

   public void setType(ParticleType<T> type) {
      this.type = type;
   }

   public T getData() {
      return this.data;
   }

   public void setData(T data) {
      this.data = data;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof Particle)) {
         return false;
      } else {
         Particle<?> particle = (Particle)obj;
         return !this.type.equals(particle.type) ? false : this.data.equals(particle.data);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.type, this.data});
   }

   public String toString() {
      return "Particle[" + this.type.getName() + ", " + this.data + ']';
   }
}
