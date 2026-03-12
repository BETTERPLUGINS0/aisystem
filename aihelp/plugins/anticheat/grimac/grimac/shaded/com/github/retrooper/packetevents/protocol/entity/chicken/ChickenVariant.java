package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.chicken;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ChickenVariant extends MappedEntity, CopyableEntity<ChickenVariant>, DeepComparableEntity {
   ChickenVariant.ModelType getModelType();

   ResourceLocation getAssetId();

   static ChickenVariant read(PacketWrapper<?> wrapper) {
      return (ChickenVariant)wrapper.readMappedEntity((IRegistry)ChickenVariants.getRegistry());
   }

   static void write(PacketWrapper<?> wrapper, ChickenVariant variant) {
      wrapper.writeMappedEntity(variant);
   }

   static ChickenVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      String modelTypeString = compound.getStringTagValueOrNull("model");
      ChickenVariant.ModelType modelType = modelTypeString != null ? ChickenVariant.ModelType.getByName(modelTypeString) : ChickenVariant.ModelType.NORMAL;
      ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
      return new StaticChickenVariant(data, modelType, assetId);
   }

   static NBT encode(ChickenVariant variant, ClientVersion version) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("model", new NBTString(variant.getModelType().getName()));
      compound.setTag("asset_id", new NBTString(variant.getAssetId().toString()));
      return compound;
   }

   public static enum ModelType {
      NORMAL("normal"),
      COLD("cold");

      private static final Index<String, ChickenVariant.ModelType> NAME_INDEX = Index.create(ChickenVariant.ModelType.class, ChickenVariant.ModelType::getName);
      private final String name;

      private ModelType(String name) {
         this.name = name;
      }

      public static ChickenVariant.ModelType getByName(String name) {
         return (ChickenVariant.ModelType)AdventureIndexUtil.indexValueOrThrow(NAME_INDEX, name);
      }

      public String getName() {
         return this.name;
      }

      // $FF: synthetic method
      private static ChickenVariant.ModelType[] $values() {
         return new ChickenVariant.ModelType[]{NORMAL, COLD};
      }
   }
}
