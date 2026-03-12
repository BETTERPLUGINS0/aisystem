package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.Biome;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface WolfVariant extends MappedEntity, CopyableEntity<WolfVariant>, DeepComparableEntity {
   ResourceLocation getWildTexture();

   ResourceLocation getTameTexture();

   ResourceLocation getAngryTexture();

   @ApiStatus.Obsolete
   MappedEntitySet<Biome> getBiomes();

   static WolfVariant read(PacketWrapper<?> wrapper) {
      return !wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) && !wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21) ? (WolfVariant)wrapper.readMappedEntityOrDirect((IRegistry)WolfVariants.getRegistry(), WolfVariant::readDirect) : (WolfVariant)wrapper.readMappedEntity((IRegistry)WolfVariants.getRegistry());
   }

   static void write(PacketWrapper<?> wrapper, WolfVariant variant) {
      if (!wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) && !wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21)) {
         wrapper.writeMappedEntityOrDirect(variant, WolfVariant::writeDirect);
      } else {
         wrapper.writeMappedEntity(variant);
      }

   }

   @ApiStatus.Obsolete
   static WolfVariant readDirect(PacketWrapper<?> wrapper) {
      ResourceLocation wildTexture = wrapper.readIdentifier();
      ResourceLocation tameTexture = wrapper.readIdentifier();
      ResourceLocation angryTexture = wrapper.readIdentifier();
      MappedEntitySet<Biome> biomes = MappedEntitySet.read(wrapper, Biomes.getRegistry());
      return new StaticWolfVariant(wildTexture, tameTexture, angryTexture, biomes);
   }

   @ApiStatus.Obsolete
   static void writeDirect(PacketWrapper<?> wrapper, WolfVariant variant) {
      wrapper.writeIdentifier(variant.getWildTexture());
      wrapper.writeIdentifier(variant.getTameTexture());
      wrapper.writeIdentifier(variant.getAngryTexture());
      MappedEntitySet.write(wrapper, variant.getBiomes());
   }

   /** @deprecated */
   @Deprecated
   static WolfVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      return decode(nbt, PacketWrapper.createDummyWrapper(version), data);
   }

   static WolfVariant decode(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      ResourceLocation wildTexture;
      ResourceLocation tameTexture;
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
         ResourceLocation wildTexture = new ResourceLocation(compound.getStringTagValueOrThrow("wild_texture"));
         wildTexture = new ResourceLocation(compound.getStringTagValueOrThrow("tame_texture"));
         tameTexture = new ResourceLocation(compound.getStringTagValueOrThrow("angry_texture"));
         MappedEntitySet<Biome> biomes = (MappedEntitySet)compound.getOrThrow("biomes", (tag, ew) -> {
            return MappedEntitySet.decode(tag, (PacketWrapper)ew, Biomes.getRegistry());
         }, wrapper);
         return new StaticWolfVariant(data, wildTexture, wildTexture, tameTexture, biomes);
      } else {
         NBTCompound assets = compound.getCompoundTagOrThrow("assets");
         wildTexture = new ResourceLocation(assets.getStringTagValueOrThrow("wild"));
         tameTexture = new ResourceLocation(assets.getStringTagValueOrThrow("tame"));
         ResourceLocation angryTexture = new ResourceLocation(assets.getStringTagValueOrThrow("angry"));
         return new StaticWolfVariant(data, wildTexture, tameTexture, angryTexture, MappedEntitySet.createEmpty());
      }
   }

   /** @deprecated */
   @Deprecated
   static NBT encode(WolfVariant variant, ClientVersion version) {
      return encode(PacketWrapper.createDummyWrapper(version), variant);
   }

   static NBT encode(PacketWrapper<?> wrapper, WolfVariant variant) {
      NBTCompound compound = new NBTCompound();
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
         compound.setTag("wild_texture", new NBTString(variant.getWildTexture().toString()));
         compound.setTag("tame_texture", new NBTString(variant.getTameTexture().toString()));
         compound.setTag("angry_texture", new NBTString(variant.getAngryTexture().toString()));
         compound.set("biomes", variant.getBiomes(), MappedEntitySet::encode, wrapper);
      } else {
         NBTCompound assets = new NBTCompound();
         assets.setTag("wild", new NBTString(variant.getWildTexture().toString()));
         assets.setTag("tame", new NBTString(variant.getWildTexture().toString()));
         assets.setTag("angry", new NBTString(variant.getWildTexture().toString()));
         compound.setTag("assets", assets);
      }

      return compound;
   }
}
