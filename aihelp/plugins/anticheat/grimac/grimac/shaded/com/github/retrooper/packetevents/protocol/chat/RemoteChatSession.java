package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.PublicProfileKey;
import java.util.UUID;

public class RemoteChatSession {
   private final UUID sessionId;
   private final PublicProfileKey publicProfileKey;

   public RemoteChatSession(UUID sessionId, PublicProfileKey publicProfileKey) {
      this.sessionId = sessionId;
      this.publicProfileKey = publicProfileKey;
   }

   public UUID getSessionId() {
      return this.sessionId;
   }

   public PublicProfileKey getPublicProfileKey() {
      return this.publicProfileKey;
   }
}
