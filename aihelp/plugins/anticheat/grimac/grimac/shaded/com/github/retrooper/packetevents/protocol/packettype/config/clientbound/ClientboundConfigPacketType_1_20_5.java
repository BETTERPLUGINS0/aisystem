package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.config.clientbound;

public enum ClientboundConfigPacketType_1_20_5 {
   COOKIE_REQUEST,
   PLUGIN_MESSAGE,
   DISCONNECT,
   CONFIGURATION_END,
   KEEP_ALIVE,
   PING,
   RESET_CHAT,
   REGISTRY_DATA,
   RESOURCE_PACK_REMOVE,
   RESOURCE_PACK_SEND,
   STORE_COOKIE,
   TRANSFER,
   UPDATE_ENABLED_FEATURES,
   UPDATE_TAGS,
   SELECT_KNOWN_PACKS;

   // $FF: synthetic method
   private static ClientboundConfigPacketType_1_20_5[] $values() {
      return new ClientboundConfigPacketType_1_20_5[]{COOKIE_REQUEST, PLUGIN_MESSAGE, DISCONNECT, CONFIGURATION_END, KEEP_ALIVE, PING, RESET_CHAT, REGISTRY_DATA, RESOURCE_PACK_REMOVE, RESOURCE_PACK_SEND, STORE_COOKIE, TRANSFER, UPDATE_ENABLED_FEATURES, UPDATE_TAGS, SELECT_KNOWN_PACKS};
   }
}
