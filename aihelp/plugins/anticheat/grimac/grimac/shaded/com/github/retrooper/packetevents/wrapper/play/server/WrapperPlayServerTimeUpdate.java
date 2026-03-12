package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerTimeUpdate extends PacketWrapper<WrapperPlayServerTimeUpdate> {
   private long worldAge;
   private long timeOfDay;
   private boolean tickTime;

   public WrapperPlayServerTimeUpdate(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerTimeUpdate(long worldAge, long timeOfDay) {
      this(worldAge, timeOfDay, timeOfDay >= 0L);
   }

   public WrapperPlayServerTimeUpdate(long worldAge, long timeOfDay, boolean tickTime) {
      super((PacketTypeCommon)PacketType.Play.Server.TIME_UPDATE);
      this.worldAge = worldAge;
      this.timeOfDay = timeOfDay;
      this.tickTime = tickTime;
   }

   public void read() {
      this.worldAge = this.readLong();
      this.timeOfDay = this.readLong();
      this.tickTime = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2) ? this.readBoolean() : this.timeOfDay >= 0L;
   }

   public void write() {
      this.writeLong(this.worldAge);
      this.writeLong(this.timeOfDay);
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.writeBoolean(this.tickTime);
      }

   }

   public void copy(WrapperPlayServerTimeUpdate wrapper) {
      this.worldAge = wrapper.worldAge;
      this.timeOfDay = wrapper.timeOfDay;
      this.tickTime = wrapper.tickTime;
   }

   public long getWorldAge() {
      return this.worldAge;
   }

   public void setWorldAge(long worldAge) {
      this.worldAge = worldAge;
   }

   public long getTimeOfDay() {
      return this.timeOfDay;
   }

   public void setTimeOfDay(long timeOfDay) {
      this.timeOfDay = timeOfDay;
   }

   public boolean isTickTime() {
      return this.tickTime;
   }

   public void setTickTime(boolean tickTime) {
      this.tickTime = tickTime;
   }
}
