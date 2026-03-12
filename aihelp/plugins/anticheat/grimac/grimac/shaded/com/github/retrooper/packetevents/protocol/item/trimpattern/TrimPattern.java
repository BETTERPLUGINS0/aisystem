package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface TrimPattern extends MappedEntity, CopyableEntity<TrimPattern>, DeepComparableEntity {
   ResourceLocation getAssetId();

   @ApiStatus.Obsolete
   ItemType getTemplateItem();

   Component getDescription();

   boolean isDecal();

   static TrimPattern read(PacketWrapper<?> wrapper) {
      return (TrimPattern)wrapper.readMappedEntityOrDirect((IRegistry)TrimPatterns.getRegistry(), TrimPattern::readDirect);
   }

   static TrimPattern readDirect(PacketWrapper<?> wrapper) {
      ResourceLocation assetId = wrapper.readIdentifier();
      ItemType templateItem = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? null : (ItemType)wrapper.readMappedEntity(ItemTypes::getById);
      Component description = wrapper.readComponent();
      boolean decal = wrapper.readBoolean();
      return new StaticTrimPattern(assetId, templateItem, description, decal);
   }

   static void write(PacketWrapper<?> wrapper, TrimPattern pattern) {
      wrapper.writeMappedEntityOrDirect(pattern, TrimPattern::writeDirect);
   }

   static void writeDirect(PacketWrapper<?> wrapper, TrimPattern pattern) {
      wrapper.writeIdentifier(pattern.getAssetId());
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
         wrapper.writeMappedEntity(pattern.getTemplateItem());
      }

      wrapper.writeComponent(pattern.getDescription());
      wrapper.writeBoolean(pattern.isDecal());
   }

   /** @deprecated */
   @Deprecated
   static TrimPattern decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      return decode(nbt, PacketWrapper.createDummyWrapper(version), data);
   }

   static TrimPattern decode(NBT nbt, PacketWrapper<?> wrapper, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
      ItemType templateItem = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) ? null : ItemTypes.getByName(compound.getStringTagValueOrThrow("template_item"));
      Component description = (Component)compound.getOrThrow("description", wrapper.getSerializers(), wrapper);
      boolean decal = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_2) && compound.getBoolean("decal");
      return new StaticTrimPattern(data, assetId, templateItem, description, decal);
   }

   /** @deprecated */
   @Deprecated
   static NBT encode(TrimPattern pattern, ClientVersion version) {
      return encode(PacketWrapper.createDummyWrapper(version), pattern);
   }

   static NBT encode(PacketWrapper<?> wrapper, TrimPattern pattern) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("asset_id", new NBTString(pattern.getAssetId().toString()));
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
         compound.setTag("template_item", new NBTString(pattern.getTemplateItem().getName().toString()));
      }

      compound.set("description", pattern.getDescription(), wrapper.getSerializers(), wrapper);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
         compound.setTag("decal", new NBTByte(pattern.isDecal()));
      }

      return compound;
   }
}
