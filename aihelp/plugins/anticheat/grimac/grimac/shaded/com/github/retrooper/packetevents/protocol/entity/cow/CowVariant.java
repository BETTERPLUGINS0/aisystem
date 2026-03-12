package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.cow;

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
public interface CowVariant extends MappedEntity, CopyableEntity<CowVariant>, DeepComparableEntity {
   CowVariant.ModelType getModelType();

   ResourceLocation getAssetId();

   static CowVariant read(PacketWrapper<?> wrapper) {
      return (CowVariant)wrapper.readMappedEntity((IRegistry)CowVariants.getRegistry());
   }

   static void write(PacketWrapper<?> wrapper, CowVariant variant) {
      wrapper.writeMappedEntity(variant);
   }

   static CowVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      String modelTypeString = compound.getStringTagValueOrNull("model");
      CowVariant.ModelType modelType = modelTypeString != null ? CowVariant.ModelType.getByName(modelTypeString) : CowVariant.ModelType.NORMAL;
      ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
      return new StaticCowVariant(data, modelType, assetId);
   }

   static NBT encode(CowVariant variant, ClientVersion version) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("model", new NBTString(variant.getModelType().getName()));
      compound.setTag("asset_id", new NBTString(variant.getAssetId().toString()));
      return compound;
   }

   public static enum ModelType {
      NORMAL("normal"),
      COLD("cold"),
      WARM("warm");

      private static final Index<String, CowVariant.ModelType> NAME_INDEX = Index.create(CowVariant.ModelType.class, CowVariant.ModelType::getName);
      private final String name;

      private ModelType(String name) {
         this.name = name;
      }

      public static CowVariant.ModelType getByName(String name) {
         return (CowVariant.ModelType)AdventureIndexUtil.indexValueOrThrow(NAME_INDEX, name);
      }

      public String getName() {
         return this.name;
      }

      // $FF: synthetic method
      private static CowVariant.ModelType[] $values() {
         return new CowVariant.ModelType[]{NORMAL, COLD, WARM};
      }
   }
}
