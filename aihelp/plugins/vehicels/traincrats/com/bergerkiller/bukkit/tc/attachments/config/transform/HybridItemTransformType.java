package com.bergerkiller.bukkit.tc.attachments.config.transform;

import com.bergerkiller.bukkit.common.math.Matrix4x4;
import com.bergerkiller.bukkit.common.wrappers.ItemDisplayMode;
import org.bukkit.util.Vector;

public enum HybridItemTransformType {
   ARMORSTAND_HEAD("head Ⓐ", 0.625D, ArmorStandItemTransformType.HEAD, ItemDisplayMode.HEAD) {
      public Matrix4x4 transformDisplay(Matrix4x4 tmp, Matrix4x4 transform) {
         tmp.set(transform);
         tmp.translate(0.0D, 0.25D, 0.0D);
         return tmp;
      }
   },
   ARMORSTAND_HEAD_SMALL("head Ⓐ⒮", 0.4375D, ArmorStandItemTransformType.SMALL_HEAD, ItemDisplayMode.HEAD) {
      public Matrix4x4 transformDisplay(Matrix4x4 tmp, Matrix4x4 transform) {
         tmp.set(transform);
         tmp.translate(0.0D, 0.175D, 0.0D);
         return tmp;
      }
   },
   ARMORSTAND_RIGHT_HAND("right hand Ⓐ", 1.0D, ArmorStandItemTransformType.RIGHT_HAND, ItemDisplayMode.THIRD_PERSON_RIGHT_HAND) {
      public Matrix4x4 transformDisplay(Matrix4x4 tmp, Matrix4x4 transform) {
         tmp.set(transform);
         tmp.translate(-0.0625D, 0.12575D, 0.625D);
         return tmp;
      }
   },
   ARMORSTAND_RIGHT_HAND_SMALL("right hand Ⓐ⒮", 0.5D, ArmorStandItemTransformType.SMALL_RIGHT_HAND, ItemDisplayMode.THIRD_PERSON_RIGHT_HAND) {
      public Matrix4x4 transformDisplay(Matrix4x4 tmp, Matrix4x4 transform) {
         tmp.set(transform);
         tmp.translate(-0.0315D, 0.06275D, 0.31225D);
         tmp.worldTranslate(-0.03625D, 0.19625D, 0.0D);
         return tmp;
      }
   },
   DISPLAY_HEAD("head Ⓓ", 0.625D, ArmorStandItemTransformType.HEAD, ItemDisplayMode.HEAD) {
      public Matrix4x4 transformArmorStand(Matrix4x4 tmp, Matrix4x4 transform) {
         tmp.set(transform);
         tmp.translate(0.0D, -0.25D, 0.0D);
         return tmp;
      }
   },
   DISPLAY_HEAD_SMALL("head Ⓓ⒮", 0.4375D, ArmorStandItemTransformType.SMALL_HEAD, ItemDisplayMode.HEAD) {
      public Matrix4x4 transformArmorStand(Matrix4x4 tmp, Matrix4x4 transform) {
         tmp.set(transform);
         tmp.translate(0.0D, -0.175D, 0.0D);
         return tmp;
      }
   },
   DISPLAY_RIGHT_HAND("right hand Ⓓ", 1.0D, ArmorStandItemTransformType.RIGHT_HAND, ItemDisplayMode.THIRD_PERSON_RIGHT_HAND) {
      public Matrix4x4 transformArmorStand(Matrix4x4 tmp, Matrix4x4 transform) {
         tmp.set(transform);
         tmp.translate(-0.0625D, -0.12575D, 0.625D);
         return tmp;
      }
   },
   DISPLAY_RIGHT_HAND_SMALL("right hand Ⓓ⒮", 0.5D, ArmorStandItemTransformType.SMALL_RIGHT_HAND, ItemDisplayMode.THIRD_PERSON_RIGHT_HAND) {
      public Matrix4x4 transformArmorStand(Matrix4x4 tmp, Matrix4x4 transform) {
         tmp.set(transform);
         tmp.worldTranslate(0.03625D, -0.19625D, 0.0D);
         tmp.translate(-0.0315D, -0.06275D, 0.31225D);
         return tmp;
      }
   };

   private final String name;
   private final Vector displayScale;
   private final ArmorStandItemTransformType armorStandTransform;
   private final ItemDisplayMode displayMode;

   private HybridItemTransformType(String name, double displayScale, ArmorStandItemTransformType armorStandTransform, ItemDisplayMode displayMode) {
      this.name = name;
      this.displayScale = new Vector(displayScale, displayScale, displayScale);
      this.armorStandTransform = armorStandTransform;
      this.displayMode = displayMode;
   }

   public Vector displayScale() {
      return this.displayScale;
   }

   public ItemDisplayMode displayMode() {
      return this.displayMode;
   }

   public ArmorStandItemTransformType armorStandTransform() {
      return this.armorStandTransform;
   }

   public Matrix4x4 transformArmorStand(Matrix4x4 tmp, Matrix4x4 transform) {
      return transform;
   }

   public Matrix4x4 transformDisplay(Matrix4x4 tmp, Matrix4x4 transform) {
      return transform;
   }

   public String toString() {
      return this.name;
   }

   // $FF: synthetic method
   private static HybridItemTransformType[] $values() {
      return new HybridItemTransformType[]{ARMORSTAND_HEAD, ARMORSTAND_HEAD_SMALL, ARMORSTAND_RIGHT_HAND, ARMORSTAND_RIGHT_HAND_SMALL, DISPLAY_HEAD, DISPLAY_HEAD_SMALL, DISPLAY_RIGHT_HAND, DISPLAY_RIGHT_HAND_SMALL};
   }

   // $FF: synthetic method
   HybridItemTransformType(String x2, double x3, ArmorStandItemTransformType x4, ItemDisplayMode x5, Object x6) {
      this(x2, x3, x4, x5);
   }
}
