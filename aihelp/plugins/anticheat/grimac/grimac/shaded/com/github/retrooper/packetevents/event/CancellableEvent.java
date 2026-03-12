package ac.grim.grimac.shaded.com.github.retrooper.packetevents.event;

public interface CancellableEvent {
   boolean isCancelled();

   void setCancelled(boolean val);
}
