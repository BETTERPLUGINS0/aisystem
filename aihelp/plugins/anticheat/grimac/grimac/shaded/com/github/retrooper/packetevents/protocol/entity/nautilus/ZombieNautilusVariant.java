package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.nautilus;

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
public interface ZombieNautilusVariant extends MappedEntity, CopyableEntity<ZombieNautilusVariant>, DeepComparableEntity {
   ZombieNautilusVariant.ModelType getModelType();

   ResourceLocation getAssetId();

   static ZombieNautilusVariant read(PacketWrapper<?> wrapper) {
      return (ZombieNautilusVariant)wrapper.readMappedEntity((IRegistry)ZombieNautilusVariants.getRegistry());
   }

   static void write(PacketWrapper<?> wrapper, ZombieNautilusVariant variant) {
      wrapper.writeMappedEntity(variant);
   }

   static ZombieNautilusVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      String modelTypeString = compound.getStringTagValueOrNull("model");
      ZombieNautilusVariant.ModelType modelType = modelTypeString != null ? ZombieNautilusVariant.ModelType.getByName(modelTypeString) : ZombieNautilusVariant.ModelType.NORMAL;
      ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
      return new StaticZombieNautilusVariant(data, modelType, assetId);
   }

   static NBT encode(ZombieNautilusVariant variant, ClientVersion version) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("model", new NBTString(variant.getModelType().getName()));
      compound.setTag("asset_id", new NBTString(variant.getAssetId().toString()));
      return compound;
   }

   public static enum ModelType {
      NORMAL("normal"),
      WARM("warm");

      private static final Index<String, ZombieNautilusVariant.ModelType> NAME_INDEX = Index.create(ZombieNautilusVariant.ModelType.class, ZombieNautilusVariant.ModelType::getName);
      private final String name;

      private ModelType(String name) {
         this.name = name;
      }

      public static ZombieNautilusVariant.ModelType getByName(String name) {
         return (ZombieNautilusVariant.ModelType)AdventureIndexUtil.indexValueOrThrow(NAME_INDEX, name);
      }

      public String getName() {
         return this.name;
      }

      // $FF: synthetic method
      private static ZombieNautilusVariant.ModelType[] $values() {
         return new ZombieNautilusVariant.ModelType[]{NORMAL, WARM};
      }
   }
}
