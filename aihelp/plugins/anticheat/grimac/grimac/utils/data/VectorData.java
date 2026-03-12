package ac.grim.grimac.utils.data;

import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.utils.math.Vector3dm;
import java.util.Objects;
import lombok.Generated;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

public class VectorData {
   public final VectorData.VectorType vectorType;
   public VectorData lastVector;
   public VectorData preUncertainty;
   public Vector3dm vector;
   @MonotonicNonNull
   public Vector3dm input;
   private boolean isKnockback;
   private boolean firstBreadKb;
   private boolean isExplosion;
   private boolean firstBreadExplosion;
   private boolean isTrident;
   private boolean isZeroPointZeroThree;
   private boolean isSwimHop;
   private boolean isFlipSneaking;
   private boolean isFlipItem;
   private boolean isJump;
   private boolean isAttackSlow = false;

   public VectorData(Vector3dm vector, VectorData lastVector, VectorData.VectorType vectorType) {
      this.vector = vector;
      this.lastVector = lastVector;
      this.vectorType = vectorType;
      if (lastVector != null) {
         this.isKnockback = lastVector.isKnockback;
         this.firstBreadKb = lastVector.firstBreadKb;
         this.isExplosion = lastVector.isExplosion;
         this.firstBreadExplosion = lastVector.firstBreadExplosion;
         this.isTrident = lastVector.isTrident;
         this.isZeroPointZeroThree = lastVector.isZeroPointZeroThree;
         this.isSwimHop = lastVector.isSwimHop;
         this.isFlipSneaking = lastVector.isFlipSneaking;
         this.isFlipItem = lastVector.isFlipItem;
         this.isJump = lastVector.isJump;
         this.preUncertainty = lastVector.preUncertainty;
         this.isAttackSlow = lastVector.isAttackSlow;
         this.input = lastVector.input;
      }

      this.addVectorType(vectorType);
   }

   public VectorData(Vector3dm vector, VectorData.VectorType vectorType) {
      this.vector = vector;
      this.vectorType = vectorType;
      this.addVectorType(vectorType);
   }

   public VectorData returnNewModified(VectorData.VectorType type) {
      return new VectorData(this.vector, this, type);
   }

   public VectorData returnNewModified(Vector3dm newVec, VectorData.VectorType type) {
      return new VectorData(newVec, this, type);
   }

   public boolean isSetbackKb(GrimPlayer player) {
      if (!this.isKnockback) {
         return false;
      } else {
         VelocityData bread = this.firstBreadKb ? player.firstBreadKB : player.likelyKB;
         return bread != null && bread.isSetback;
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         VectorData that = (VectorData)o;
         return this.isKnockback == that.isKnockback && this.firstBreadKb == that.firstBreadKb && this.isExplosion == that.isExplosion && this.firstBreadExplosion == that.firstBreadExplosion && this.isTrident == that.isTrident && this.isZeroPointZeroThree == that.isZeroPointZeroThree && this.isSwimHop == that.isSwimHop && this.isFlipSneaking == that.isFlipSneaking && this.isFlipItem == that.isFlipItem && this.isJump == that.isJump && this.isAttackSlow == that.isAttackSlow && this.vectorType == that.vectorType && Objects.equals(this.lastVector, that.lastVector) && Objects.equals(this.preUncertainty, that.preUncertainty) && Objects.equals(this.vector, that.vector);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.vectorType, this.lastVector, this.preUncertainty, this.vector, this.isKnockback, this.firstBreadKb, this.isExplosion, this.firstBreadExplosion, this.isTrident, this.isZeroPointZeroThree, this.isSwimHop, this.isFlipSneaking, this.isFlipItem, this.isJump, this.isAttackSlow});
   }

   public void addVectorType(VectorData.VectorType type) {
      switch(type.ordinal()) {
      case 1:
         this.isSwimHop = true;
      case 2:
      case 5:
      case 6:
      case 7:
      case 10:
      case 11:
      case 12:
      case 13:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 22:
      default:
         break;
      case 3:
         this.isKnockback = true;
         break;
      case 4:
         this.firstBreadKb = true;
         break;
      case 8:
         this.isExplosion = true;
         break;
      case 9:
         this.firstBreadExplosion = true;
         break;
      case 14:
         this.isJump = true;
         break;
      case 21:
         this.isTrident = true;
         break;
      case 23:
         this.isZeroPointZeroThree = true;
         break;
      case 24:
         this.isAttackSlow = true;
         break;
      case 25:
         this.isFlipSneaking = true;
         break;
      case 26:
         this.isFlipItem = true;
      }

   }

   public String toString() {
      boolean var10000 = this.isZeroPointZeroThree;
      return "VectorData{pointThree=" + var10000 + ", vector=" + String.valueOf(this.vector) + "}";
   }

   @Generated
   public boolean isKnockback() {
      return this.isKnockback;
   }

   @Generated
   public boolean isFirstBreadKb() {
      return this.firstBreadKb;
   }

   @Generated
   public boolean isExplosion() {
      return this.isExplosion;
   }

   @Generated
   public boolean isFirstBreadExplosion() {
      return this.firstBreadExplosion;
   }

   @Generated
   public boolean isTrident() {
      return this.isTrident;
   }

   @Generated
   public boolean isZeroPointZeroThree() {
      return this.isZeroPointZeroThree;
   }

   @Generated
   public boolean isSwimHop() {
      return this.isSwimHop;
   }

   @Generated
   public boolean isFlipSneaking() {
      return this.isFlipSneaking;
   }

   @Generated
   public boolean isFlipItem() {
      return this.isFlipItem;
   }

   @Generated
   public boolean isJump() {
      return this.isJump;
   }

   @Generated
   public boolean isAttackSlow() {
      return this.isAttackSlow;
   }

   public static enum VectorType {
      Normal,
      Swimhop,
      Climbable,
      Knockback,
      FirstBreadKnockback,
      HackyClimbable,
      Teleport,
      SkippedTicks,
      Explosion,
      FirstBreadExplosion,
      InputResult,
      StuckMultiplier,
      Spectator,
      Dead,
      Jump,
      SurfaceSwimming,
      SwimmingSpace,
      BestVelPicked,
      Firework,
      Lenience,
      TridentJump,
      Trident,
      SlimePistonBounce,
      ZeroPointZeroThree,
      AttackSlow,
      Flip_Sneaking,
      Flip_Use_Item,
      EntityPushing;

      // $FF: synthetic method
      private static VectorData.VectorType[] $values() {
         return new VectorData.VectorType[]{Normal, Swimhop, Climbable, Knockback, FirstBreadKnockback, HackyClimbable, Teleport, SkippedTicks, Explosion, FirstBreadExplosion, InputResult, StuckMultiplier, Spectator, Dead, Jump, SurfaceSwimming, SwimmingSpace, BestVelPicked, Firework, Lenience, TridentJump, Trident, SlimePistonBounce, ZeroPointZeroThree, AttackSlow, Flip_Sneaking, Flip_Use_Item, EntityPushing};
      }
   }
}
