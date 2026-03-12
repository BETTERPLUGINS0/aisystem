package ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;

public class WrapperPlayServerSetCooldown extends PacketWrapper<WrapperPlayServerSetCooldown> {
   private ResourceLocation cooldownGroup;
   private int cooldownTicks;

   public WrapperPlayServerSetCooldown(PacketSendEvent event) {
      super(event);
   }

   @ApiStatus.Obsolete
   public WrapperPlayServerSetCooldown(ItemType item, int cooldownTicks) {
      this(item.getName(), cooldownTicks);
   }

   public WrapperPlayServerSetCooldown(ResourceLocation cooldownGroup, int cooldownTicks) {
      super((PacketTypeCommon)PacketType.Play.Server.SET_COOLDOWN);
      this.cooldownGroup = cooldownGroup;
      this.cooldownTicks = cooldownTicks;
   }

   public void read() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.cooldownGroup = this.readIdentifier();
      } else {
         ItemType item = (ItemType)this.readMappedEntity(ItemTypes.getRegistry());
         this.cooldownGroup = item.getName();
      }

      this.cooldownTicks = this.readVarInt();
   }

   public void write() {
      if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
         this.writeIdentifier(this.cooldownGroup);
      } else {
         this.writeMappedEntity(this.getItem());
      }

      this.writeVarInt(this.cooldownTicks);
   }

   public void copy(WrapperPlayServerSetCooldown wrapper) {
      this.cooldownGroup = wrapper.cooldownGroup;
      this.cooldownTicks = wrapper.cooldownTicks;
   }

   public ResourceLocation getCooldownGroup() {
      return this.cooldownGroup;
   }

   public void setCooldownGroup(ResourceLocation cooldownGroup) {
      this.cooldownGroup = cooldownGroup;
   }

   @ApiStatus.Obsolete
   public ItemType getItem() {
      ItemType item = ItemTypes.getByName(this.cooldownGroup.toString());
      if (item == null) {
         throw new IllegalStateException("Can't get legacy cooldown item for cooldown group " + this.cooldownGroup);
      } else {
         return item;
      }
   }

   @ApiStatus.Obsolete
   public void setItem(ItemType item) {
      this.cooldownGroup = item.getName();
   }

   public int getCooldownTicks() {
      return this.cooldownTicks;
   }

   public void setCooldownTicks(int cooldownTicks) {
      this.cooldownTicks = cooldownTicks;
   }
}
