package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class UserDisconnectEvent extends PacketEvent implements UserEvent {
   private final User user;

   public UserDisconnectEvent(User user) {
      this.user = user;
   }

   public User getUser() {
      return this.user;
   }

   public void call(PacketListenerCommon listener) {
      listener.onUserDisconnect(this);
   }
}
