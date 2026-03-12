package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.lang.ref.WeakReference;
import java.util.function.Supplier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface MappedEntityRef<T extends MappedEntity> extends Supplier<T> {
   T get();

   static <T extends MappedEntity> MappedEntityRef<T> decode(NBT tag, IRegistry<T> registry, NbtDecoder<T> decoder, PacketWrapper<?> wrapper) {
      if (tag instanceof NBTString) {
         ResourceLocation name = new ResourceLocation(((NBTString)tag).getValue());
         return new MappedEntityRef.Named(wrapper, registry, name);
      } else {
         return new MappedEntityRef.Static((MappedEntity)decoder.decode(tag, wrapper));
      }
   }

   static <T extends MappedEntity> NBT encode(PacketWrapper<?> wrapper, NbtEncoder<T> encoder, MappedEntityRef<T> ref) {
      if (ref instanceof MappedEntityRef.Named) {
         return new NBTString(((MappedEntityRef.Named)ref).name.toString());
      } else if (ref instanceof MappedEntityRef.Static) {
         return encoder.encode(wrapper, ((MappedEntityRef.Static)ref).entity);
      } else {
         throw new UnsupportedOperationException("Unsupported MappedEntityRef implementation: " + ref);
      }
   }

   public static final class Named<T extends MappedEntity> implements MappedEntityRef<T> {
      private final WeakReference<IRegistryHolder> registryHolder;
      private final ClientVersion version;
      private final IRegistry<T> registry;
      private final ResourceLocation name;
      @Nullable
      private volatile T entity;

      public Named(PacketWrapper<?> wrapper, IRegistry<T> registry, ResourceLocation name) {
         this(wrapper.getRegistryHolder(), wrapper.getServerVersion().toClientVersion(), registry, name);
      }

      public Named(IRegistryHolder registryHolder, ClientVersion version, IRegistry<T> registry, ResourceLocation name) {
         this.registryHolder = new WeakReference(registryHolder);
         this.version = version;
         this.registry = registry;
         this.name = name;
      }

      public T get() {
         T entity = this.entity;
         if (entity == null) {
            synchronized(this) {
               entity = this.entity;
               if (entity == null) {
                  IRegistryHolder registryHolder = (IRegistryHolder)this.registryHolder.get();
                  if (registryHolder == null) {
                     throw new IllegalStateException("Registry holder for " + this.registry + "/" + this.version + "/" + this.name + "has disappeared");
                  }

                  IRegistry<T> registry = registryHolder.getRegistryOr(this.registry, this.version);
                  entity = registry.getByNameOrThrow(this.version, this.name);
                  this.entity = entity;
               }
            }
         }

         return entity;
      }
   }

   public static final class Static<T extends MappedEntity> implements MappedEntityRef<T> {
      private final T entity;

      public Static(T entity) {
         this.entity = entity;
      }

      public T get() {
         return this.entity;
      }
   }
}
