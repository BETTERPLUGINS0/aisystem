package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerDebugSample extends PacketWrapper<WrapperPlayServerDebugSample> {
   private long[] sample;
   private WrapperPlayServerDebugSample.SampleType sampleType;

   public WrapperPlayServerDebugSample(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerDebugSample(long[] sample, WrapperPlayServerDebugSample.SampleType sampleType) {
      super((PacketTypeCommon)PacketType.Play.Server.DEBUG_SAMPLE);
      this.sample = sample;
      this.sampleType = sampleType;
   }

   public void read() {
      this.sample = this.readLongArray();
      this.sampleType = WrapperPlayServerDebugSample.SampleType.values()[this.readVarInt()];
   }

   public void write() {
      this.writeLongArray(this.sample);
      this.writeVarInt(this.sampleType.ordinal());
   }

   public void copy(WrapperPlayServerDebugSample wrapper) {
      this.sample = wrapper.sample;
      this.sampleType = wrapper.sampleType;
   }

   public long[] getSample() {
      return this.sample;
   }

   public void setSample(long[] sample) {
      this.sample = sample;
   }

   public WrapperPlayServerDebugSample.SampleType getSampleType() {
      return this.sampleType;
   }

   public void setSampleType(WrapperPlayServerDebugSample.SampleType sampleType) {
      this.sampleType = sampleType;
   }

   public static enum SampleType {
      TICK_DURATION;

      // $FF: synthetic method
      private static WrapperPlayServerDebugSample.SampleType[] $values() {
         return new WrapperPlayServerDebugSample.SampleType[]{TICK_DURATION};
      }
   }
}
