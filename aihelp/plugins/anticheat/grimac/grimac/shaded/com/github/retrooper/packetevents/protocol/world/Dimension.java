package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypeRef;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;

/** @deprecated */
@Deprecated
public class Dimension {
   private int id;
   private NBTCompound attributes;

   /** @deprecated */
   @Deprecated
   public Dimension(DimensionType type) {
      this.id = type.getId();
      this.attributes = new NBTCompound();
   }

   public Dimension(int id) {
      this.id = id;
      this.attributes = new NBTCompound();
   }

   public Dimension(NBTCompound attributes) {
      this.attributes = attributes;
   }

   /** @deprecated */
   @Deprecated
   public Dimension(int id, NBTCompound attributes) {
      this.id = id;
      this.attributes = attributes;
   }

   public static Dimension fromDimensionTypeRef(DimensionTypeRef ref) {
      if (ref instanceof DimensionTypeRef.NameRef) {
         Dimension dimension = new Dimension(0);
         dimension.setDimensionName(ref.getName().toString());
         return dimension;
      } else if (ref instanceof DimensionTypeRef.IdRef) {
         return new Dimension(ref.getId());
      } else if (ref instanceof DimensionTypeRef.DataRef) {
         return new Dimension((NBTCompound)ref.getData());
      } else if (ref instanceof DimensionTypeRef.DirectRef) {
         return fromDimensionType(((DimensionTypeRef.DirectRef)ref).getDimensionType(), (User)null, ((DimensionTypeRef.DirectRef)ref).getVersion());
      } else {
         throw new UnsupportedOperationException("Unsupported DimensionTypeRef implementation: " + ref);
      }
   }

   public static Dimension fromDimensionType(ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType dimensionType, @Nullable User user, @Nullable ClientVersion version) {
      if (version == null) {
         version = user != null && PacketEvents.getAPI().getInjector().isProxy() ? user.getClientVersion() : PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
      }

      NBTCompound encodedType = (NBTCompound)ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType.encode(dimensionType, version);
      return new Dimension(dimensionType.getId(version), encodedType);
   }

   public DimensionTypeRef asDimensionTypeRef() {
      if (this.attributes == null) {
         return new DimensionTypeRef.IdRef(this.id);
      } else if (this.attributes.size() > 1) {
         return new DimensionTypeRef.DataRef(this.attributes);
      } else {
         ResourceLocation dimensionName = new ResourceLocation(this.getDimensionName());
         return new DimensionTypeRef.NameRef(dimensionName);
      }
   }

   public ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType asDimensionType(@Nullable User user, @Nullable ClientVersion version) {
      if (version == null) {
         version = user != null && PacketEvents.getAPI().getInjector().isProxy() ? user.getClientVersion() : PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
      }

      IRegistry<ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType> registry = user != null ? user.getRegistryOr(DimensionTypes.getRegistry(), version) : DimensionTypes.getRegistry();
      String dimName = this.getDimensionName();
      return !dimName.isEmpty() ? (ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType)((IRegistry)registry).getByName(new ResourceLocation(dimName)) : (ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.dimension.DimensionType)((IRegistry)registry).getById(version, this.id);
   }

   public String getDimensionName() {
      return this.getAttributes().getStringTagValueOrDefault("effects", "");
   }

   public void setDimensionName(String name) {
      NBTCompound compound = this.getAttributes();
      compound.setTag("effects", new NBTString(name));
      this.setAttributes(compound);
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   /** @deprecated */
   @Deprecated
   public DimensionType getType() {
      return DimensionType.getById(this.id);
   }

   /** @deprecated */
   @Deprecated
   public void setType(DimensionType type) {
      this.id = type.getId();
   }

   public NBTCompound getAttributes() {
      return this.attributes;
   }

   public void setAttributes(NBTCompound attributes) {
      this.attributes = attributes;
   }
}
