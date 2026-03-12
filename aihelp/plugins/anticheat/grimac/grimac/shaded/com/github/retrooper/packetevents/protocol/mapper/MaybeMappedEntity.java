package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.Objects;

@ApiStatus.Experimental
public final class MaybeMappedEntity<T extends MappedEntity> {
   @Nullable
   private final T entity;
   @Nullable
   private final ResourceLocation name;
   @Nullable
   private final IRegistry<T> registry;

   public MaybeMappedEntity(T entity) {
      this(entity, (ResourceLocation)null, (IRegistry)null);
   }

   public MaybeMappedEntity(ResourceLocation name) {
      this((ResourceLocation)name, (IRegistry)null);
   }

   public MaybeMappedEntity(ResourceLocation name, @Nullable IRegistry<T> registry) {
      this((MappedEntity)null, name, registry);
   }

   public MaybeMappedEntity(@Nullable T entity, @Nullable ResourceLocation name) {
      this(entity, name, (IRegistry)null);
   }

   public MaybeMappedEntity(@Nullable T entity, @Nullable ResourceLocation name, @Nullable IRegistry<T> registry) {
      if (entity == null && name == null) {
         throw new IllegalArgumentException("Only one of entity and name is allowed to be null");
      } else {
         this.entity = entity;
         this.name = name;
         this.registry = registry;
      }
   }

   public static <T extends MappedEntity> MaybeMappedEntity<T> read(PacketWrapper<?> wrapper, IRegistry<T> registry, PacketWrapper.Reader<T> reader) {
      if (wrapper.readBoolean()) {
         return new MaybeMappedEntity((MappedEntity)reader.apply(wrapper));
      } else {
         ClientVersion version = wrapper.getServerVersion().toClientVersion();
         IRegistry<T> replacedRegistry = wrapper.getRegistryHolder().getRegistryOr(registry, version);
         return new MaybeMappedEntity(wrapper.readIdentifier(), replacedRegistry);
      }
   }

   public static <T extends MappedEntity> void write(PacketWrapper<?> wrapper, MaybeMappedEntity<T> entity, PacketWrapper.Writer<T> writer) {
      if (entity.entity != null) {
         wrapper.writeBoolean(true);
         writer.accept(wrapper, entity.entity);
      } else {
         wrapper.writeBoolean(false);
         wrapper.writeIdentifier(entity.name);
      }

   }

   public T getValueOrThrow() {
      T value = this.getValue();
      if (value == null) {
         throw new IllegalStateException("Can't resolve entity by name " + this.name);
      } else {
         return value;
      }
   }

   @Nullable
   public T getValue() {
      if (this.entity != null) {
         return this.entity;
      } else {
         return this.registry != null && this.name != null ? this.registry.getByName(this.name) : null;
      }
   }

   public ResourceLocation getName() {
      if (this.name != null) {
         return this.name;
      } else if (this.entity != null) {
         return this.entity.getName();
      } else {
         throw new AssertionError();
      }
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof MaybeMappedEntity)) {
         return false;
      } else {
         MaybeMappedEntity<?> that = (MaybeMappedEntity)obj;
         return !Objects.equals(this.entity, that.entity) ? false : Objects.equals(this.name, that.name);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.entity, this.name});
   }
}
