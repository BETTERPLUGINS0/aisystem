package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Collection;

public final class ChatTypes {
   private static final VersionedRegistry<ChatType> REGISTRY = new VersionedRegistry("chat_type");
   public static final ChatType CHAT = define("chat");
   public static final ChatType SAY_COMMAND = define("say_command", ChatTypeDecoration.withSender("chat.type.announcement"));
   public static final ChatType MSG_COMMAND_INCOMING = define("msg_command_incoming", ChatTypeDecoration.incomingDirectMessage("commands.message.display.incoming"));
   public static final ChatType MSG_COMMAND_OUTGOING = define("msg_command_outgoing", ChatTypeDecoration.outgoingDirectMessage("commands.message.display.outgoing"));
   public static final ChatType TEAM_MSG_COMMAND_INCOMING = define("team_msg_command_incoming", ChatTypeDecoration.teamMessage("chat.type.team.text"));
   public static final ChatType TEAM_MSG_COMMAND_OUTGOING = define("team_msg_command_outgoing", ChatTypeDecoration.teamMessage("chat.type.team.sent"));
   public static final ChatType EMOTE_COMMAND = define("emote_command", ChatTypeDecoration.withSender("chat.type.emote"), ChatTypeDecoration.withSender("chat.type.emote"));
   public static final ChatType RAW = define("raw");
   /** @deprecated */
   @Deprecated
   public static final ChatType SYSTEM = define("system");
   /** @deprecated */
   @Deprecated
   public static final ChatType GAME_INFO = define("game_info");
   /** @deprecated */
   @Deprecated
   public static final ChatType MSG_COMMAND = define("msg_command");
   /** @deprecated */
   @Deprecated
   public static final ChatType TEAM_MSG_COMMAND = define("team_msg_command");

   private ChatTypes() {
   }

   @ApiStatus.Internal
   public static ChatType define(String key) {
      return define(key, ChatTypeDecoration.withSender("chat.type.text"));
   }

   @ApiStatus.Internal
   public static ChatType define(String key, ChatTypeDecoration chatDeco) {
      return define(key, chatDeco, ChatTypeDecoration.withSender("chat.type.text.narrate"));
   }

   @ApiStatus.Internal
   public static ChatType define(String key, ChatTypeDecoration chatDeco, ChatTypeDecoration narrationDeco) {
      return (ChatType)REGISTRY.define(key, (data) -> {
         return new StaticChatType(data, chatDeco, narrationDeco);
      });
   }

   public static VersionedRegistry<ChatType> getRegistry() {
      return REGISTRY;
   }

   public static ChatType getByName(String name) {
      return (ChatType)REGISTRY.getByName(name);
   }

   public static ChatType getById(ClientVersion version, int id) {
      return (ChatType)REGISTRY.getById(version, id);
   }

   public static Collection<ChatType> values() {
      return REGISTRY.getEntries();
   }

   static {
      REGISTRY.unloadMappings();
   }
}
