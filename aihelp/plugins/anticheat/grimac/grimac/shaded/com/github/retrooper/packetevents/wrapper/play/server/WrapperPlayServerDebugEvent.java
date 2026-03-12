package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.DebugSubscription;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WrapperPlayServerDebugEvent extends PacketWrapper<WrapperPlayServerDebugEvent> {
   private DebugSubscription.Event<?> event;

   public WrapperPlayServerDebugEvent(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDebugEvent(DebugSubscription.Event<?> event) {
      super((PacketTypeCommon)PacketType.Play.Server.DEBUG_EVENT);
      this.event = event;
   }

   public void read() {
      this.event = DebugSubscription.Event.read(this);
   }

   public void write() {
      DebugSubscription.Event.write(this, this.event);
   }

   public void copy(WrapperPlayServerDebugEvent wrapper) {
      this.event = wrapper.event;
   }

   public DebugSubscription.Event<?> getEvent() {
      return this.event;
   }

   public void setEvent(DebugSubscription.Event<?> event) {
      this.event = event;
   }
}
