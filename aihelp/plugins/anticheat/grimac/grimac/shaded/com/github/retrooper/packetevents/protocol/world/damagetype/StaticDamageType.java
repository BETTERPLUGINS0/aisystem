package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class StaticDamageType extends AbstractMappedEntity implements DamageType {
   private final String messageId;
   private final DamageScaling scaling;
   private final float exhaustion;
   private final DamageEffects effects;
   private final DeathMessageType deathMessageType;

   @ApiStatus.Internal
   public StaticDamageType(@Nullable TypesBuilderData data, String messageId, DamageScaling scaling, float exhaustion, DamageEffects effects, DeathMessageType deathMessageType) {
      super(data);
      this.messageId = messageId;
      this.scaling = scaling;
      this.exhaustion = exhaustion;
      this.effects = effects;
      this.deathMessageType = deathMessageType;
   }

   public DamageType copy(@Nullable TypesBuilderData newData) {
      return new StaticDamageType(newData, this.messageId, this.scaling, this.exhaustion, this.effects, this.deathMessageType);
   }

   public String getMessageId() {
      return this.messageId;
   }

   public DamageScaling getScaling() {
      return this.scaling;
   }

   public float getExhaustion() {
      return this.exhaustion;
   }

   public DamageEffects getEffects() {
      return this.effects;
   }

   public DeathMessageType getDeathMessageType() {
      return this.deathMessageType;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof StaticDamageType)) {
         return false;
      } else {
         StaticDamageType that = (StaticDamageType)obj;
         if (Float.compare(that.exhaustion, this.exhaustion) != 0) {
            return false;
         } else if (!this.messageId.equals(that.messageId)) {
            return false;
         } else if (this.scaling != that.scaling) {
            return false;
         } else if (this.effects != that.effects) {
            return false;
         } else {
            return this.deathMessageType == that.deathMessageType;
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.messageId, this.scaling, this.exhaustion, this.effects, this.deathMessageType});
   }

   public String toString() {
      return "StaticDamageType{messageId='" + this.messageId + '\'' + ", scaling=" + this.scaling + ", exhaustion=" + this.exhaustion + ", effects=" + this.effects + ", deathMessageType=" + this.deathMessageType + '}';
   }
}
