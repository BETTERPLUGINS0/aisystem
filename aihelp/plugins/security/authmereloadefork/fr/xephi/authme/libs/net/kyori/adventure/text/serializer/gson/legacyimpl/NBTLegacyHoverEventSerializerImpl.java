package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson.legacyimpl;

import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.nbt.BinaryTag;
import fr.xephi.authme.libs.net.kyori.adventure.nbt.CompoundBinaryTag;
import fr.xephi.authme.libs.net.kyori.adventure.nbt.TagStringIO;
import fr.xephi.authme.libs.net.kyori.adventure.nbt.api.BinaryTagHolder;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.TextComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.HoverEvent;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
import fr.xephi.authme.libs.net.kyori.adventure.util.Codec;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

final class NBTLegacyHoverEventSerializerImpl implements LegacyHoverEventSerializer {
   static final NBTLegacyHoverEventSerializerImpl INSTANCE = new NBTLegacyHoverEventSerializerImpl();
   private static final TagStringIO SNBT_IO = TagStringIO.get();
   private static final Codec<CompoundBinaryTag, String, IOException, IOException> SNBT_CODEC;
   static final String ITEM_TYPE = "id";
   static final String ITEM_COUNT = "Count";
   static final String ITEM_TAG = "tag";
   static final String ENTITY_NAME = "name";
   static final String ENTITY_TYPE = "type";
   static final String ENTITY_ID = "id";

   private NBTLegacyHoverEventSerializerImpl() {
   }

   @NotNull
   public HoverEvent.ShowItem deserializeShowItem(@NotNull final Component input) throws IOException {
      assertTextComponent(input);
      CompoundBinaryTag contents = (CompoundBinaryTag)SNBT_CODEC.decode(((TextComponent)input).content());
      CompoundBinaryTag tag = contents.getCompound("tag");
      return HoverEvent.ShowItem.of((Key)Key.key(contents.getString("id")), contents.getByte("Count", (byte)1), tag == CompoundBinaryTag.empty() ? null : BinaryTagHolder.encode(tag, SNBT_CODEC));
   }

   @NotNull
   public HoverEvent.ShowEntity deserializeShowEntity(@NotNull final Component input, final Codec.Decoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
      assertTextComponent(input);
      CompoundBinaryTag contents = (CompoundBinaryTag)SNBT_CODEC.decode(((TextComponent)input).content());
      return HoverEvent.ShowEntity.of(Key.key(contents.getString("type")), UUID.fromString(contents.getString("id")), (Component)componentCodec.decode(contents.getString("name")));
   }

   private static void assertTextComponent(final Component component) {
      if (!(component instanceof TextComponent) || !component.children().isEmpty()) {
         throw new IllegalArgumentException("Legacy events must be single Component instances");
      }
   }

   @NotNull
   public Component serializeShowItem(final HoverEvent.ShowItem input) throws IOException {
      CompoundBinaryTag.Builder builder = (CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)CompoundBinaryTag.builder().putString("id", input.item().asString())).putByte("Count", (byte)input.count());
      BinaryTagHolder nbt = input.nbt();
      if (nbt != null) {
         builder.put("tag", (BinaryTag)nbt.get(SNBT_CODEC));
      }

      return Component.text((String)SNBT_CODEC.encode(builder.build()));
   }

   @NotNull
   public Component serializeShowEntity(final HoverEvent.ShowEntity input, final Codec.Encoder<Component, String, ? extends RuntimeException> componentCodec) throws IOException {
      CompoundBinaryTag.Builder builder = (CompoundBinaryTag.Builder)((CompoundBinaryTag.Builder)CompoundBinaryTag.builder().putString("id", input.id().toString())).putString("type", input.type().asString());
      Component name = input.name();
      if (name != null) {
         builder.putString("name", (String)componentCodec.encode(name));
      }

      return Component.text((String)SNBT_CODEC.encode(builder.build()));
   }

   static {
      TagStringIO var10000 = SNBT_IO;
      Objects.requireNonNull(var10000);
      Codec.Decoder var0 = var10000::asCompound;
      TagStringIO var10001 = SNBT_IO;
      Objects.requireNonNull(var10001);
      SNBT_CODEC = Codec.codec(var0, var10001::asString);
   }
}
