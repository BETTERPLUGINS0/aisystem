package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterials;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTFloat;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface TrimMaterial extends MappedEntity, CopyableEntity<TrimMaterial>, DeepComparableEntity {
   float FALLBACK_ITEM_MODEL_INDEX = 0.0F;

   String getAssetName();

   @ApiStatus.Obsolete
   ItemType getIngredient();

   @ApiStatus.Obsolete
   float getItemModelIndex();

   @Nullable
   default String getArmorMaterialOverride(ArmorMaterial armorMaterial) {
      return (String)this.getOverrideArmorMaterials().get(armorMaterial);
   }

   Map<ArmorMaterial, String> getOverrideArmorMaterials();

   Component getDescription();

   static TrimMaterial read(PacketWrapper<?> wrapper) {
      return (TrimMaterial)wrapper.readMappedEntityOrDirect((IRegistry)TrimMaterials.getRegistry(), TrimMaterial::readDirect);
   }

   static TrimMaterial readDirect(PacketWrapper<?> wrapper) {
      String assetName = wrapper.readString();
      ItemType ingredient = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? null : (ItemType)wrapper.readMappedEntity(ItemTypes::getById);
      float itemModelIndex = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4) ? 0.0F : wrapper.readFloat();
      Map<ArmorMaterial, String> overrideArmorMaterials = wrapper.readMap((ew) -> {
         return (ArmorMaterial)ew.readMappedEntity(ArmorMaterials::getById);
      }, PacketWrapper::readString);
      Component description = wrapper.readComponent();
      return new StaticTrimMaterial(assetName, ingredient, itemModelIndex, overrideArmorMaterials, description);
   }

   static void write(PacketWrapper<?> wrapper, TrimMaterial material) {
      wrapper.writeMappedEntityOrDirect(material, TrimMaterial::writeDirect);
   }

   static void writeDirect(PacketWrapper<?> wrapper, TrimMaterial material) {
      wrapper.writeString(material.getAssetName());
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
         wrapper.writeMappedEntity(material.getIngredient());
      }

      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_4)) {
         wrapper.writeFloat(material.getItemModelIndex());
      }

      wrapper.writeMap(material.getOverrideArmorMaterials(), PacketWrapper::writeMappedEntity, PacketWrapper::writeString);
      wrapper.writeComponent(material.getDescription());
   }

   /** @deprecated */
   @Deprecated
   static TrimMaterial decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      return decode(nbt, PacketWrapper.createDummyWrapper(version), data);
   }

   static TrimMaterial decode(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      String assetName = compound.getStringTagValueOrThrow("asset_name");
      ItemType ingredient = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? null : ItemTypes.getByName(compound.getStringTagValueOrThrow("ingredient"));
      float itemModelIndex = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_4) ? 0.0F : compound.getNumberTagOrThrow("item_model_index").getAsFloat();
      NBTCompound overrideArmorMaterialsTag = compound.getCompoundTagOrNull("override_armor_materials");
      Object overrideArmorMaterials;
      if (overrideArmorMaterialsTag != null) {
         overrideArmorMaterials = new HashMap();
         Iterator var9 = overrideArmorMaterialsTag.getTags().entrySet().iterator();

         while(var9.hasNext()) {
            Entry<String, NBT> entry = (Entry)var9.next();
            ArmorMaterial material = ArmorMaterials.getByName((String)entry.getKey());
            String override = ((NBTString)entry.getValue()).getValue();
            ((Map)overrideArmorMaterials).put(material, override);
         }
      } else {
         overrideArmorMaterials = Collections.emptyMap();
      }

      Component description = (Component)((NBTCompound)nbt).getOrThrow("description", wrapper.getSerializers(), wrapper);
      return new StaticTrimMaterial(data, assetName, ingredient, itemModelIndex, (Map)overrideArmorMaterials, description);
   }

   /** @deprecated */
   @Deprecated
   static NBT encode(TrimMaterial material, ClientVersion version) {
      return encode(PacketWrapper.createDummyWrapper(version), material);
   }

   static NBT encode(PacketWrapper<?> wrapper, TrimMaterial material) {
      NBTCompound overrideArmorMaterialsTag;
      if (!material.getOverrideArmorMaterials().isEmpty()) {
         overrideArmorMaterialsTag = new NBTCompound();
         Iterator var3 = material.getOverrideArmorMaterials().entrySet().iterator();

         while(var3.hasNext()) {
            Entry<ArmorMaterial, String> entry = (Entry)var3.next();
            String materialName = ((ArmorMaterial)entry.getKey()).getName().toString();
            NBTString overrideTag = new NBTString((String)entry.getValue());
            overrideArmorMaterialsTag.setTag(materialName, overrideTag);
         }
      } else {
         overrideArmorMaterialsTag = null;
      }

      NBTCompound compound = new NBTCompound();
      compound.setTag("asset_name", new NBTString(material.getAssetName()));
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
         compound.setTag("ingredient", new NBTString(material.getIngredient().getName().toString()));
      }

      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_4)) {
         compound.setTag("item_model_index", new NBTFloat(material.getItemModelIndex()));
      }

      if (overrideArmorMaterialsTag != null) {
         compound.setTag("override_armor_materials", overrideArmorMaterialsTag);
      }

      compound.set("description", material.getDescription(), wrapper.getSerializers(), wrapper);
      return compound;
   }
}
