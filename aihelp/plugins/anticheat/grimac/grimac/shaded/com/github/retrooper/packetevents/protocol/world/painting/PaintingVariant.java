package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.painting;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface PaintingVariant extends MappedEntity, CopyableEntity<PaintingVariant>, DeepComparableEntity {
   int getWidth();

   int getHeight();

   ResourceLocation getAssetId();

   static PaintingVariant read(PacketWrapper<?> wrapper) {
      return wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21) ? (PaintingVariant)wrapper.readMappedEntityOrDirect((IRegistry)PaintingVariants.getRegistry(), PaintingVariant::readDirect) : (PaintingVariant)wrapper.readMappedEntity((IRegistry)PaintingVariants.getRegistry());
   }

   static void write(PacketWrapper<?> wrapper, PaintingVariant variant) {
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21)) {
         wrapper.writeMappedEntityOrDirect(variant, PaintingVariant::writeDirect);
      } else {
         wrapper.writeMappedEntity(variant);
      }

   }

   static PaintingVariant readDirect(PacketWrapper<?> wrapper) {
      int width = wrapper.readVarInt();
      int height = wrapper.readVarInt();
      ResourceLocation assetId = wrapper.readIdentifier();
      return new StaticPaintingVariant(width, height, assetId);
   }

   static void writeDirect(PacketWrapper<?> wrapper, PaintingVariant variant) {
      wrapper.writeVarInt(variant.getWidth());
      wrapper.writeVarInt(variant.getHeight());
      wrapper.writeIdentifier(variant.getAssetId());
   }

   static PaintingVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      int width = compound.getNumberTagOrThrow("width").getAsInt();
      int height = compound.getNumberTagOrThrow("height").getAsInt();
      ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
      return new StaticPaintingVariant(data, width, height, assetId);
   }

   static NBT encode(PaintingVariant variant, ClientVersion version) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("width", new NBTInt(variant.getWidth()));
      compound.setTag("height", new NBTInt(variant.getHeight()));
      compound.setTag("asset_id", new NBTString(variant.getAssetId().toString()));
      return compound;
   }
}
