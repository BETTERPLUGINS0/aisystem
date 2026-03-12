package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CallableEvent {
   default void call(PacketListenerCommon listener) {
      listener.onPacketEventExternal((PacketEvent)this);
   }
}
