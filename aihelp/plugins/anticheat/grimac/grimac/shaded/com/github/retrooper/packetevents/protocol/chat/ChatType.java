package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19_1;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureIndexUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ChatType extends MappedEntity, CopyableEntity<ChatType>, DeepComparableEntity {
   @UnknownNullability("only nullable for 1.19")
   ChatTypeDecoration getChatDecoration();

   @ApiStatus.Obsolete(
      since = "1.19.1"
   )
   @Nullable
   ChatTypeDecoration getOverlayDecoration();

   @UnknownNullability("only nullable for 1.19")
   ChatTypeDecoration getNarrationDecoration();

   @ApiStatus.Obsolete(
      since = "1.19.1"
   )
   @Nullable
   ChatType.NarrationPriority getNarrationPriority();

   static ChatType readDirect(PacketWrapper<?> wrapper) {
      ChatTypeDecoration chatDecoration = ChatTypeDecoration.read(wrapper);
      ChatTypeDecoration narrationDecoration = ChatTypeDecoration.read(wrapper);
      return new StaticChatType(chatDecoration, narrationDecoration);
   }

   static void writeDirect(PacketWrapper<?> wrapper, ChatType chatType) {
      ChatTypeDecoration.write(wrapper, chatType.getChatDecoration());
      ChatTypeDecoration.write(wrapper, chatType.getNarrationDecoration());
   }

   static ChatType decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
      NBTCompound compound = (NBTCompound)nbt;
      NBTCompound chatTag = compound.getCompoundTagOrNull("chat");
      NBTCompound narrationTag = compound.getCompoundTagOrNull("narration");
      ChatTypeDecoration overlay = null;
      ChatType.NarrationPriority narrationPriority = null;
      if (version.isOlderThan(ClientVersion.V_1_19_1)) {
         NBTCompound overlayTag = compound.getCompoundTagOrNull("overlay");
         if (overlayTag != null) {
            overlayTag = overlayTag.getCompoundTagOrNull("description");
            if (overlayTag != null) {
               overlay = ChatTypeDecoration.decode(overlayTag, (ClientVersion)version);
            }
         }

         if (chatTag != null) {
            chatTag = chatTag.getCompoundTagOrNull("description");
         }

         if (narrationTag != null) {
            narrationPriority = (ChatType.NarrationPriority)AdventureIndexUtil.indexValueOrThrow(ChatType.NarrationPriority.ID_INDEX, narrationTag.getStringTagValueOrThrow("priority"));
            narrationTag = narrationTag.getCompoundTagOrNull("description");
         }
      } else {
         Objects.requireNonNull(chatTag, "NBT chat does not exist");
         Objects.requireNonNull(narrationTag, "NBT narration does not exist");
      }

      ChatTypeDecoration chat = chatTag == null ? null : ChatTypeDecoration.decode(chatTag, (ClientVersion)version);
      ChatTypeDecoration narration = narrationTag == null ? null : ChatTypeDecoration.decode(narrationTag, (ClientVersion)version);
      return new StaticChatType(data, chat, overlay, narration, narrationPriority);
   }

   static NBT encode(ChatType chatType, ClientVersion version) {
      NBTCompound compound = new NBTCompound();
      NBT chatTag = chatType.getChatDecoration() == null ? null : ChatTypeDecoration.encode(chatType.getChatDecoration(), version);
      NBT narrationTag = chatType.getNarrationDecoration() == null ? null : ChatTypeDecoration.encode(chatType.getNarrationDecoration(), version);
      if (version.isOlderThan(ClientVersion.V_1_19_1)) {
         ChatTypeDecoration overlayDeco = chatType.getOverlayDecoration();
         NBTCompound chatCompound;
         if (overlayDeco != null) {
            chatCompound = new NBTCompound();
            chatCompound.setTag("description", ChatTypeDecoration.encode(overlayDeco, version));
            compound.setTag("overlay", chatCompound);
         }

         if (narrationTag != null) {
            chatCompound = new NBTCompound();
            chatCompound.setTag("description", (NBT)narrationTag);
            if (chatType.getNarrationPriority() != null) {
               chatCompound.setTag("priority", new NBTString(chatType.getNarrationPriority().getId()));
            }

            narrationTag = chatCompound;
         }

         if (chatTag != null) {
            chatCompound = new NBTCompound();
            chatCompound.setTag("description", (NBT)chatTag);
            chatTag = chatCompound;
         }
      }

      if (chatTag != null) {
         compound.setTag("chat", (NBT)chatTag);
      }

      if (narrationTag != null) {
         compound.setTag("narration", (NBT)narrationTag);
      }

      return compound;
   }

   @ApiStatus.Obsolete(
      since = "1.19.1"
   )
   public static enum NarrationPriority {
      CHAT("chat"),
      SYSTEM("system");

      public static final Index<String, ChatType.NarrationPriority> ID_INDEX = Index.create(ChatType.NarrationPriority.class, ChatType.NarrationPriority::getId);
      private final String id;

      private NarrationPriority(String id) {
         this.id = id;
      }

      public String getId() {
         return this.id;
      }

      // $FF: synthetic method
      private static ChatType.NarrationPriority[] $values() {
         return new ChatType.NarrationPriority[]{CHAT, SYSTEM};
      }
   }

   public static class Bound extends ChatMessage_v1_19_1.ChatTypeBoundNetwork {
      public Bound(ChatType type, Component name, @Nullable Component targetName) {
         super(type, name, targetName);
      }
   }
}
