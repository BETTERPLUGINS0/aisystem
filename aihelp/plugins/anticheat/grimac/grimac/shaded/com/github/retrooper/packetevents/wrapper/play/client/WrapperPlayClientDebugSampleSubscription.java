package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDebugSample;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

@ApiStatus.Obsolete
public class WrapperPlayClientDebugSampleSubscription extends PacketWrapper<WrapperPlayClientDebugSampleSubscription> {
   private WrapperPlayServerDebugSample.SampleType sampleType;

   public WrapperPlayClientDebugSampleSubscription(PacketReceiveEvent event) {
      super(event);
   }

   public WrapperPlayClientDebugSampleSubscription(WrapperPlayServerDebugSample.SampleType sampleType) {
      super((PacketTypeCommon)PacketType.Play.Client.DEBUG_SAMPLE_SUBSCRIPTION);
      this.sampleType = sampleType;
   }

   public void read() {
      this.sampleType = WrapperPlayServerDebugSample.SampleType.values()[this.readVarInt()];
   }

   public void write() {
      this.writeVarInt(this.sampleType.ordinal());
   }

   public void copy(WrapperPlayClientDebugSampleSubscription wrapper) {
      this.sampleType = wrapper.sampleType;
   }

   public WrapperPlayServerDebugSample.SampleType getSampleType() {
      return this.sampleType;
   }

   public void setSampleType(WrapperPlayServerDebugSample.SampleType sampleType) {
      this.sampleType = sampleType;
   }
}
