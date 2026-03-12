package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.PlayerModelType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecException;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtCodecs;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapCodec;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.object.PlayerHeadObjectContents;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class ItemProfile {
   @Nullable
   private String name;
   @Nullable
   private UUID id;
   private List<ItemProfile.Property> properties;
   private ItemProfile.SkinPatch skinPatch;

   public ItemProfile(@Nullable String name, @Nullable UUID id, List<ItemProfile.Property> properties) {
      this(name, id, properties, ItemProfile.SkinPatch.EMPTY);
   }

   public ItemProfile(@Nullable String name, @Nullable UUID id, List<ItemProfile.Property> properties, ItemProfile.SkinPatch skinPatch) {
      this.name = name;
      this.id = id;
      this.properties = properties;
      this.skinPatch = skinPatch;
   }

   public static ItemProfile read(PacketWrapper<?> wrapper) {
      boolean partial = wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_9) || !wrapper.readBoolean();
      String name;
      UUID id;
      if (!partial) {
         id = wrapper.readUUID();
         name = wrapper.readString(16);
      } else {
         name = (String)wrapper.readOptional((ew) -> {
            return ew.readString(16);
         });
         id = (UUID)wrapper.readOptional(PacketWrapper::readUUID);
      }

      List<ItemProfile.Property> properties = wrapper.readList(ItemProfile.Property::read);
      ItemProfile.SkinPatch skinPatch = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9) ? ItemProfile.SkinPatch.read(wrapper) : ItemProfile.SkinPatch.EMPTY;
      return new ItemProfile(name, id, properties, skinPatch);
   }

   public static void write(PacketWrapper<?> wrapper, ItemProfile profile) {
      boolean partial;
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         partial = profile.name == null || profile.id == null;
         wrapper.writeBoolean(!partial);
      } else {
         partial = true;
      }

      if (!partial) {
         wrapper.writeUUID(profile.id);
         wrapper.writeString(profile.name, 16);
      } else {
         wrapper.writeOptional(profile.name, (ew, name) -> {
            ew.writeString(name, 16);
         });
         wrapper.writeOptional(profile.id, PacketWrapper::writeUUID);
      }

      wrapper.writeList(profile.properties, ItemProfile.Property::write);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_9)) {
         ItemProfile.SkinPatch.write(wrapper, profile.skinPatch);
      }

   }

   public static ItemProfile decode(NBT nbt, PacketWrapper<?> wrapper) {
      if (nbt instanceof NBTString) {
         String name = ((NBTString)nbt).getValue();
         return new ItemProfile(name, (UUID)null, new ArrayList());
      } else {
         NBTCompound compound = (NBTCompound)nbt;
         UUID id = (UUID)compound.getOrNull("id", NbtCodecs.UUID, wrapper);
         String name = compound.getStringTagValueOrNull("name");
         List<ItemProfile.Property> properties = (List)compound.getOrSupply("properties", ItemProfile.Property.PROPERTY_MAP, ArrayList::new, wrapper);
         ItemProfile.SkinPatch patch = ItemProfile.SkinPatch.decode(compound, wrapper);
         return new ItemProfile(name, id, properties, patch);
      }
   }

   public static NBT encode(PacketWrapper<?> wrapper, ItemProfile profile) {
      NBTCompound compound = new NBTCompound();
      if (profile.id != null) {
         compound.set("id", profile.id, NbtCodecs.UUID, wrapper);
      }

      if (profile.name != null) {
         compound.setTag("name", new NBTString(profile.name));
      }

      if (!profile.properties.isEmpty()) {
         compound.set("properties", profile.properties, ItemProfile.Property.PROPERTY_MAP, wrapper);
      }

      ItemProfile.SkinPatch.encode(compound, wrapper, profile.skinPatch);
      return compound;
   }

   public static ItemProfile fromAdventure(PlayerHeadObjectContents headContents) {
      List<PlayerHeadObjectContents.ProfileProperty> advProps = headContents.profileProperties();
      List<ItemProfile.Property> properties = new ArrayList(advProps.size());
      Iterator var3 = advProps.iterator();

      while(var3.hasNext()) {
         PlayerHeadObjectContents.ProfileProperty property = (PlayerHeadObjectContents.ProfileProperty)var3.next();
         properties.add(ItemProfile.Property.fromAdventure(property));
      }

      return new ItemProfile(headContents.name(), headContents.id(), properties);
   }

   public List<PlayerHeadObjectContents.ProfileProperty> getAdventureProperties() {
      if (this.properties.isEmpty()) {
         return Collections.emptyList();
      } else {
         List<PlayerHeadObjectContents.ProfileProperty> properties = new ArrayList(this.properties.size());
         Iterator var2 = this.properties.iterator();

         while(var2.hasNext()) {
            ItemProfile.Property property = (ItemProfile.Property)var2.next();
            properties.add(property.asAdventure());
         }

         return Collections.unmodifiableList(properties);
      }
   }

   @Nullable
   public String getName() {
      return this.name;
   }

   public void setName(@Nullable String name) {
      this.name = name;
   }

   @Nullable
   public UUID getId() {
      return this.id;
   }

   public void setId(@Nullable UUID id) {
      this.id = id;
   }

   public void addProperty(ItemProfile.Property property) {
      this.properties.add(property);
   }

   public List<ItemProfile.Property> getProperties() {
      return this.properties;
   }

   public void setProperties(List<ItemProfile.Property> properties) {
      this.properties = properties;
   }

   public ItemProfile.SkinPatch getSkinPatch() {
      return this.skinPatch;
   }

   public void setSkinPatch(ItemProfile.SkinPatch skinPatch) {
      this.skinPatch = skinPatch;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemProfile)) {
         return false;
      } else {
         ItemProfile that = (ItemProfile)obj;
         if (!Objects.equals(this.name, that.name)) {
            return false;
         } else if (!Objects.equals(this.id, that.id)) {
            return false;
         } else {
            return !this.properties.equals(that.properties) ? false : this.skinPatch.equals(that.skinPatch);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.name, this.id, this.properties, this.skinPatch});
   }

   public static class SkinPatch {
      public static final ItemProfile.SkinPatch EMPTY = new ItemProfile.SkinPatch((ResourceLocation)null, (ResourceLocation)null, (ResourceLocation)null, (PlayerModelType)null);
      @Nullable
      private final ResourceLocation body;
      @Nullable
      private final ResourceLocation cape;
      @Nullable
      private final ResourceLocation elytra;
      @Nullable
      private final PlayerModelType model;

      public SkinPatch(@Nullable ResourceLocation body, @Nullable ResourceLocation cape, @Nullable ResourceLocation elytra, @Nullable PlayerModelType model) {
         this.body = body;
         this.cape = cape;
         this.elytra = elytra;
         this.model = model;
      }

      public static ItemProfile.SkinPatch read(PacketWrapper<?> wrapper) {
         ResourceLocation body = (ResourceLocation)wrapper.readOptional(ResourceLocation::read);
         ResourceLocation cape = (ResourceLocation)wrapper.readOptional(ResourceLocation::read);
         ResourceLocation elytra = (ResourceLocation)wrapper.readOptional(ResourceLocation::read);
         PlayerModelType model = (PlayerModelType)wrapper.readOptional(PlayerModelType::read);
         return new ItemProfile.SkinPatch(body, cape, elytra, model);
      }

      public static void write(PacketWrapper<?> wrapper, ItemProfile.SkinPatch patch) {
         wrapper.writeOptional(patch.body, ResourceLocation::write);
         wrapper.writeOptional(patch.cape, ResourceLocation::write);
         wrapper.writeOptional(patch.elytra, ResourceLocation::write);
         wrapper.writeOptional(patch.model, PlayerModelType::write);
      }

      public static ItemProfile.SkinPatch decode(NBTCompound nbt, PacketWrapper<?> wrapper) {
         ResourceLocation body = (ResourceLocation)nbt.getOrNull("texture", ResourceLocation.CODEC, wrapper);
         ResourceLocation cape = (ResourceLocation)nbt.getOrNull("cape", ResourceLocation.CODEC, wrapper);
         ResourceLocation elytra = (ResourceLocation)nbt.getOrNull("elytra", ResourceLocation.CODEC, wrapper);
         PlayerModelType model = (PlayerModelType)nbt.getOrNull("model", PlayerModelType.CODEC, wrapper);
         return new ItemProfile.SkinPatch(body, cape, elytra, model);
      }

      public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ItemProfile.SkinPatch patch) {
         if (patch.body != null) {
            compound.set("texture", patch.body, ResourceLocation.CODEC, wrapper);
         }

         if (patch.cape != null) {
            compound.set("cape", patch.cape, ResourceLocation.CODEC, wrapper);
         }

         if (patch.elytra != null) {
            compound.set("elytra", patch.elytra, ResourceLocation.CODEC, wrapper);
         }

         if (patch.model != null) {
            compound.set("model", patch.model, PlayerModelType.CODEC, wrapper);
         }

      }

      @Nullable
      public ResourceLocation getBody() {
         return this.body;
      }

      @Nullable
      public ResourceLocation getCape() {
         return this.cape;
      }

      @Nullable
      public ResourceLocation getElytra() {
         return this.elytra;
      }

      @Nullable
      public PlayerModelType getModel() {
         return this.model;
      }

      public boolean equals(Object obj) {
         if (obj != null && this.getClass() == obj.getClass()) {
            ItemProfile.SkinPatch skinPatch = (ItemProfile.SkinPatch)obj;
            if (!Objects.equals(this.body, skinPatch.body)) {
               return false;
            } else if (!Objects.equals(this.cape, skinPatch.cape)) {
               return false;
            } else if (!Objects.equals(this.elytra, skinPatch.elytra)) {
               return false;
            } else {
               return this.model == skinPatch.model;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.body, this.cape, this.elytra, this.model});
      }
   }

   public static class Property {
      public static final NbtCodec<ItemProfile.Property> CODEC = (new NbtMapCodec<ItemProfile.Property>() {
         public ItemProfile.Property decode(NBTCompound compound, PacketWrapper<?> wrapper) throws NbtCodecException {
            String name = compound.getStringTagValueOrThrow("name");
            String value = compound.getStringTagValueOrThrow("value");
            String signature = compound.getStringTagValueOrNull("signature");
            return new ItemProfile.Property(name, value, signature);
         }

         public void encode(NBTCompound compound, PacketWrapper<?> wrapper, ItemProfile.Property value) throws NbtCodecException {
            compound.setTag("name", new NBTString(value.getName()));
            compound.setTag("value", new NBTString(value.getValue()));
            if (value.getSignature() != null) {
               compound.setTag("signature", new NBTString(value.getSignature()));
            }

         }
      }).codec();
      public static final NbtCodec<List<ItemProfile.Property>> PROPERTY_MAP = new NbtCodec<List<ItemProfile.Property>>() {
         private final NbtCodec<List<ItemProfile.Property>> propertyList;

         {
            this.propertyList = ItemProfile.Property.CODEC.applyList();
         }

         public List<ItemProfile.Property> decode(NBT nbt, PacketWrapper<?> wrapper) {
            if (!(nbt instanceof NBTCompound)) {
               return (List)this.propertyList.decode(nbt, wrapper);
            } else {
               Map<String, NBT> tags = ((NBTCompound)nbt).getTags();
               List<ItemProfile.Property> properties = new ArrayList(tags.size());
               Iterator var5 = tags.entrySet().iterator();

               while(var5.hasNext()) {
                  Entry<String, NBT> entry = (Entry)var5.next();
                  Iterator var7 = ((List)NbtCodecs.STRING_LIST.decode((NBT)entry.getValue(), wrapper)).iterator();

                  while(var7.hasNext()) {
                     String value = (String)var7.next();
                     properties.add(new ItemProfile.Property((String)entry.getKey(), value, (String)null));
                  }
               }

               return properties;
            }
         }

         public NBT encode(PacketWrapper<?> wrapper, List<ItemProfile.Property> value) {
            return this.propertyList.encode(wrapper, value);
         }
      };
      private String name;
      private String value;
      @Nullable
      private String signature;

      public Property(String name, String value, @Nullable String signature) {
         this.name = name;
         this.value = value;
         this.signature = signature;
      }

      public static ItemProfile.Property read(PacketWrapper<?> wrapper) {
         String name = wrapper.readString(64);
         String value = wrapper.readString(32767);
         String signature = (String)wrapper.readOptional((ew) -> {
            return ew.readString(1024);
         });
         return new ItemProfile.Property(name, value, signature);
      }

      public static void write(PacketWrapper<?> wrapper, ItemProfile.Property property) {
         wrapper.writeString(property.name, 64);
         wrapper.writeString(property.value, 32767);
         wrapper.writeOptional(property.signature, (ew, signature) -> {
            ew.writeString(signature, 1024);
         });
      }

      public static ItemProfile.Property fromAdventure(PlayerHeadObjectContents.ProfileProperty property) {
         return new ItemProfile.Property(property.name(), property.value(), property.signature());
      }

      public PlayerHeadObjectContents.ProfileProperty asAdventure() {
         return PlayerHeadObjectContents.property(this.name, this.value, this.signature);
      }

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getValue() {
         return this.value;
      }

      public void setValue(String value) {
         this.value = value;
      }

      @Nullable
      public String getSignature() {
         return this.signature;
      }

      public void setSignature(@Nullable String signature) {
         this.signature = signature;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof ItemProfile.Property)) {
            return false;
         } else {
            ItemProfile.Property property = (ItemProfile.Property)obj;
            if (!this.name.equals(property.name)) {
               return false;
            } else {
               return !this.value.equals(property.value) ? false : Objects.equals(this.signature, property.signature);
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.name, this.value, this.signature});
      }
   }
}
