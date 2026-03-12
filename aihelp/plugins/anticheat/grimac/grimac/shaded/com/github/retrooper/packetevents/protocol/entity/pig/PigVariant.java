package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.pig;

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
public interface PigVariant extends MappedEntity, CopyableEntity<PigVariant>, DeepComparableEntity {
   PigVariant.ModelType getModelType();

   ResourceLocation getAssetId();

   static PigVariant read(PacketWrapper<?> wrapper) {
      return (PigVariant)wrapper.readMappedEntity((IRegistry)PigVariants.getRegistry());
   }

   static void write(PacketWrapper<?> wrapper, PigVariant variant) {
      wrapper.writeMappedEntity(variant);
   }

   static PigVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      String modelTypeString = compound.getStringTagValueOrNull("model");
      PigVariant.ModelType modelType = modelTypeString != null ? PigVariant.ModelType.getByName(modelTypeString) : PigVariant.ModelType.NORMAL;
      ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
      return new StaticPigVariant(data, modelType, assetId);
   }

   static NBT encode(PigVariant variant, ClientVersion version) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("model", new NBTString(variant.getModelType().getName()));
      compound.setTag("asset_id", new NBTString(variant.getAssetId().toString()));
      return compound;
   }

   public static enum ModelType {
      NORMAL("normal"),
      COLD("cold");

      private static final Index<String, PigVariant.ModelType> NAME_INDEX = Index.create(PigVariant.ModelType.class, PigVariant.ModelType::getName);
      private final String name;

      private ModelType(String name) {
         this.name = name;
      }

      public static PigVariant.ModelType getByName(String name) {
         return (PigVariant.ModelType)AdventureIndexUtil.indexValueOrThrow(NAME_INDEX, name);
      }

      public String getName() {
         return this.name;
      }

      // $FF: synthetic method
      private static PigVariant.ModelType[] $values() {
         return new PigVariant.ModelType[]{NORMAL, COLD};
      }
   }
}
