package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.DebugSubscription;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.debug.DebugSubscriptions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.IRegistry;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Set;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class WrapperPlayClientDebugSubscriptionRequest extends PacketWrapper<WrapperPlayClientDebugSubscriptionRequest> {
   private Set<DebugSubscription<?>> subscriptions;

   public WrapperPlayClientDebugSubscriptionRequest(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientDebugSubscriptionRequest(Set<DebugSubscription<?>> subscriptions) {
      super((PacketTypeCommon)PacketType.Play.Client.DEBUG_SUBSCRIPTION_REQUEST);
      this.subscriptions = subscriptions;
   }

   public void read() {
      this.subscriptions = this.readSet((ew) -> {
         return (DebugSubscription)ew.readMappedEntity((IRegistry)DebugSubscriptions.getRegistry());
      });
   }

   public void write() {
      this.writeSet(this.subscriptions, PacketWrapper::writeMappedEntity);
   }

   public void copy(WrapperPlayClientDebugSubscriptionRequest wrapper) {
      this.subscriptions = wrapper.subscriptions;
   }

   public Set<DebugSubscription<?>> getSubscriptions() {
      return this.subscriptions;
   }

   public void setSubscriptions(Set<DebugSubscription<?>> subscriptions) {
      this.subscriptions = subscriptions;
   }
}
