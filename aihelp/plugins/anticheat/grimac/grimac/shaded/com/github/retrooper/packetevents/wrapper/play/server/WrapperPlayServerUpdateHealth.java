package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class WrapperPlayServerUpdateHealth extends PacketWrapper<WrapperPlayServerUpdateHealth> {
   private float health;
   private int food;
   private float foodSaturation;

   public WrapperPlayServerUpdateHealth(PacketSendEvent event) {
      super(event);
   }

   public WrapperPlayServerUpdateHealth(float health, int food, float foodSaturation) {
      super((PacketTypeCommon)PacketType.Play.Server.UPDATE_HEALTH);
      this.health = health;
      this.food = food;
      this.foodSaturation = foodSaturation;
   }

   public void read() {
      this.health = this.readFloat();
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.food = this.readShort();
      } else {
         this.food = this.readVarInt();
      }

      this.foodSaturation = this.readFloat();
   }

   public void write() {
      this.writeFloat(this.health);
      if (this.serverVersion.isOlderThanOrEquals(ServerVersion.V_1_7_10)) {
         this.writeShort(this.food);
      } else {
         this.writeVarInt(this.food);
      }

      this.writeFloat(this.foodSaturation);
   }

   public void copy(WrapperPlayServerUpdateHealth wrapper) {
      this.health = wrapper.health;
      this.food = wrapper.food;
      this.foodSaturation = wrapper.foodSaturation;
   }

   public float getHealth() {
      return this.health;
   }

   public void setHealth(float health) {
      this.health = health;
   }

   public int getFood() {
      return this.food;
   }

   public void setFood(int food) {
      this.food = food;
   }

   public float getFoodSaturation() {
      return this.foodSaturation;
   }

   public void setFoodSaturation(float foodSaturation) {
      this.foodSaturation = foodSaturation;
   }
}
