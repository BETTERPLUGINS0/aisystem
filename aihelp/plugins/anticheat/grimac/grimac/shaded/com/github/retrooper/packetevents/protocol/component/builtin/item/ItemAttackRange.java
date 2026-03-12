package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ItemAttackRange {
   private float minRange;
   private float maxRange;
   private float minCreativeRange;
   private float maxCreativeRange;
   private float hitboxMargin;
   private float mobFactor;

   public ItemAttackRange(float minRange, float maxRange, float minCreativeRange, float maxCreativeRange, float hitboxMargin, float mobFactor) {
      this.minRange = minRange;
      this.maxRange = maxRange;
      this.minCreativeRange = minCreativeRange;
      this.maxCreativeRange = maxCreativeRange;
      this.hitboxMargin = hitboxMargin;
      this.mobFactor = mobFactor;
   }

   public static ItemAttackRange read(PacketWrapper<?> wrapper) {
      float minRange = wrapper.readFloat();
      float maxRange = wrapper.readFloat();
      float minCreativeRange = wrapper.readFloat();
      float maxCreativeRange = wrapper.readFloat();
      float hitboxMargin = wrapper.readFloat();
      float mobFactor = wrapper.readFloat();
      return new ItemAttackRange(minRange, maxRange, minCreativeRange, maxCreativeRange, hitboxMargin, mobFactor);
   }

   public static void write(PacketWrapper<?> wrapper, ItemAttackRange component) {
      wrapper.writeFloat(component.minRange);
      wrapper.writeFloat(component.maxRange);
      wrapper.writeFloat(component.minCreativeRange);
      wrapper.writeFloat(component.maxCreativeRange);
      wrapper.writeFloat(component.hitboxMargin);
      wrapper.writeFloat(component.mobFactor);
   }

   public float getMinRange() {
      return this.minRange;
   }

   public void setMinRange(float minRange) {
      this.minRange = minRange;
   }

   public float getMaxRange() {
      return this.maxRange;
   }

   public void setMaxRange(float maxRange) {
      this.maxRange = maxRange;
   }

   public float getMinCreativeRange() {
      return this.minCreativeRange;
   }

   public void setMinCreativeRange(float minCreativeRange) {
      this.minCreativeRange = minCreativeRange;
   }

   public float getMaxCreativeRange() {
      return this.maxCreativeRange;
   }

   public void setMaxCreativeRange(float maxCreativeRange) {
      this.maxCreativeRange = maxCreativeRange;
   }

   public float getHitboxMargin() {
      return this.hitboxMargin;
   }

   public void setHitboxMargin(float hitboxMargin) {
      this.hitboxMargin = hitboxMargin;
   }

   public float getMobFactor() {
      return this.mobFactor;
   }

   public void setMobFactor(float mobFactor) {
      this.mobFactor = mobFactor;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ItemAttackRange that = (ItemAttackRange)obj;
         if (Float.compare(that.minRange, this.minRange) != 0) {
            return false;
         } else if (Float.compare(that.maxRange, this.maxRange) != 0) {
            return false;
         } else if (Float.compare(that.minCreativeRange, this.minCreativeRange) != 0) {
            return false;
         } else if (Float.compare(that.maxCreativeRange, this.maxCreativeRange) != 0) {
            return false;
         } else if (Float.compare(that.hitboxMargin, this.hitboxMargin) != 0) {
            return false;
         } else {
            return Float.compare(that.mobFactor, this.mobFactor) == 0;
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.minRange, this.maxRange, this.minCreativeRange, this.maxCreativeRange, this.hitboxMargin, this.mobFactor});
   }
}
