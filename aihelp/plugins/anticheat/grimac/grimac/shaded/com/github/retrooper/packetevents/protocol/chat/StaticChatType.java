package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.UnknownNullability;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticChatType extends AbstractMappedEntity implements ChatType {
   @UnknownNullability("only nullable for 1.19")
   private final ChatTypeDecoration chatDecoration;
   @Nullable
   private final ChatTypeDecoration overlayDecoration;
   @UnknownNullability("only nullable for 1.19")
   private final ChatTypeDecoration narrationDecoration;
   @Nullable
   private final ChatType.NarrationPriority narrationPriority;

   public StaticChatType(ChatTypeDecoration chatDecoration, ChatTypeDecoration narrationDecoration) {
      this((TypesBuilderData)null, chatDecoration, (ChatTypeDecoration)null, narrationDecoration, (ChatType.NarrationPriority)null);
   }

   @ApiStatus.Internal
   public StaticChatType(@Nullable TypesBuilderData data, ChatTypeDecoration chatDecoration, ChatTypeDecoration narrationDecoration) {
      this(data, chatDecoration, (ChatTypeDecoration)null, narrationDecoration, (ChatType.NarrationPriority)null);
   }

   public StaticChatType(@UnknownNullability("only nullable for 1.19") ChatTypeDecoration chatDecoration, @Nullable ChatTypeDecoration overlayDecoration, @UnknownNullability("only nullable for 1.19") ChatTypeDecoration narrationDecoration, @Nullable ChatType.NarrationPriority narrationPriority) {
      this((TypesBuilderData)null, chatDecoration, overlayDecoration, narrationDecoration, narrationPriority);
   }

   @ApiStatus.Internal
   public StaticChatType(@Nullable TypesBuilderData data, @Nullable ChatTypeDecoration chatDecoration, @Nullable ChatTypeDecoration overlayDecoration, @Nullable ChatTypeDecoration narrationDecoration, @Nullable ChatType.NarrationPriority narrationPriority) {
      super(data);
      this.chatDecoration = chatDecoration;
      this.overlayDecoration = overlayDecoration;
      this.narrationDecoration = narrationDecoration;
      this.narrationPriority = narrationPriority;
   }

   public ChatType copy(@Nullable TypesBuilderData newData) {
      return new StaticChatType(newData, this.chatDecoration, this.overlayDecoration, this.narrationDecoration, this.narrationPriority);
   }

   @UnknownNullability("only nullable for 1.19")
   public ChatTypeDecoration getChatDecoration() {
      return this.chatDecoration;
   }

   @Nullable
   public ChatTypeDecoration getOverlayDecoration() {
      return this.overlayDecoration;
   }

   @UnknownNullability("only nullable for 1.19")
   public ChatTypeDecoration getNarrationDecoration() {
      return this.narrationDecoration;
   }

   @Nullable
   public ChatType.NarrationPriority getNarrationPriority() {
      return this.narrationPriority;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StaticChatType)) {
         return false;
      } else {
         StaticChatType that = (StaticChatType)obj;
         if (!Objects.equals(this.chatDecoration, that.chatDecoration)) {
            return false;
         } else if (!Objects.equals(this.overlayDecoration, that.overlayDecoration)) {
            return false;
         } else if (!Objects.equals(this.narrationDecoration, that.narrationDecoration)) {
            return false;
         } else {
            return this.narrationPriority == that.narrationPriority;
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.chatDecoration, this.overlayDecoration, this.narrationDecoration, this.narrationPriority});
   }

   public String toString() {
      return "StaticChatType{chatDecoration=" + this.chatDecoration + ", overlayDecoration=" + this.overlayDecoration + ", narrationDecoration=" + this.narrationDecoration + ", narrationPriority=" + this.narrationPriority + '}';
   }
}
