package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemWeapon {
   private int itemDamagePerAttack;
   private float disableBlockingForSeconds;

   public ItemWeapon(int itemDamagePerAttack, float disableBlockingForSeconds) {
      this.itemDamagePerAttack = itemDamagePerAttack;
      this.disableBlockingForSeconds = disableBlockingForSeconds;
   }

   public static ItemWeapon read(PacketWrapper<?> wrapper) {
      int itemDamagePerAttack = wrapper.readVarInt();
      float disableBlockingForSeconds = wrapper.readFloat();
      return new ItemWeapon(itemDamagePerAttack, disableBlockingForSeconds);
   }

   public static void write(PacketWrapper<?> wrapper, ItemWeapon weapon) {
      wrapper.writeVarInt(weapon.itemDamagePerAttack);
      wrapper.writeFloat(weapon.disableBlockingForSeconds);
   }

   public int getItemDamagePerAttack() {
      return this.itemDamagePerAttack;
   }

   public void setItemDamagePerAttack(int itemDamagePerAttack) {
      this.itemDamagePerAttack = itemDamagePerAttack;
   }

   public float getDisableBlockingForSeconds() {
      return this.disableBlockingForSeconds;
   }

   public void setDisableBlockingForSeconds(float disableBlockingForSeconds) {
      this.disableBlockingForSeconds = disableBlockingForSeconds;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ItemWeapon)) {
         return false;
      } else {
         ItemWeapon that = (ItemWeapon)obj;
         if (this.itemDamagePerAttack != that.itemDamagePerAttack) {
            return false;
         } else {
            return Float.compare(that.disableBlockingForSeconds, this.disableBlockingForSeconds) == 0;
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.itemDamagePerAttack, this.disableBlockingForSeconds});
   }
}
