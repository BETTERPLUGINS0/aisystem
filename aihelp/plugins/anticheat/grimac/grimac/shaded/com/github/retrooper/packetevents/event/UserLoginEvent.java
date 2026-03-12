package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.User;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class UserLoginEvent extends PacketEvent implements CallableEvent, UserEvent, PlayerEvent {
   private final User user;
   private final Object player;

   public UserLoginEvent(User user, Object player) {
      this.user = user;
      this.player = player;
   }

   public User getUser() {
      return this.user;
   }

   public <T> T getPlayer() {
      return this.player;
   }

   public void call(PacketListenerCommon listener) {
      listener.onUserLogin(this);
   }
}
