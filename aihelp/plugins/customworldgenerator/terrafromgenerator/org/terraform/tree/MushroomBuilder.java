package org.terraform.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.Vector2f;
import org.terraform.utils.noise.BezierCurve;
import org.terraform.utils.noise.BresenhamLine;
import org.terraform.utils.noise.FastNoise;

public class MushroomBuilder {
   final FractalTypes.Mushroom type;
   final int heightVariation = 0;
   Random rand;
   FastNoise noiseGen;
   @Nullable
   SimpleBlock stemTop;
   FractalTypes.MushroomCap capShape;
   Material stemType;
   Material capType;
   Material spotType;
   int baseHeight;
   float baseThickness;
   float segmentFactor;
   Vector2f curvatureControlPoint1;
   Vector2f curvatureControlPoint2;
   double thicknessIncrement;
   Vector2f thicknessControlPoint1;
   Vector2f thicknessControlPoint2;
   float capRadius;
   int capYOffset;
   double minTilt;
   double maxTilt;
   boolean fourAxisRotationOnly;

   public MushroomBuilder(@NotNull FractalTypes.Mushroom type) {
      this.capShape = FractalTypes.MushroomCap.ROUND;
      this.stemType = Material.MUSHROOM_STEM;
      this.capType = Material.RED_MUSHROOM_BLOCK;
      this.spotType = Material.MUSHROOM_STEM;
      this.baseHeight = 18;
      this.baseThickness = 3.8F;
      this.segmentFactor = 2.0F;
      this.curvatureControlPoint1 = new Vector2f(-2.0F, 0.5F);
      this.curvatureControlPoint2 = new Vector2f(1.6F, 0.4F);
      this.thicknessIncrement = 1.0D;
      this.thicknessControlPoint1 = new Vector2f(0.5F, 0.5F);
      this.thicknessControlPoint2 = new Vector2f(0.5F, 0.5F);
      this.capRadius = 10.0F;
      this.capYOffset = -5;
      this.minTilt = 0.06544984694978735D;
      this.maxTilt = 0.15707963267948966D;
      this.fourAxisRotationOnly = false;
      this.type = type;
      switch(type) {
      case GIANT_BROWN_FUNNEL_MUSHROOM:
         this.setCapType(Material.BROWN_MUSHROOM_BLOCK).setCapRadius(13.0F).setCapYOffset(-2).setCapShape(FractalTypes.MushroomCap.FUNNEL);
         break;
      case GIANT_BROWN_MUSHROOM:
         this.setCapType(Material.BROWN_MUSHROOM_BLOCK);
         break;
      case GIANT_RED_MUSHROOM:
         this.setBaseThickness(6.0F).setThicknessIncrement(1.5D).setCapRadius(15.0F).setCapYOffset(-10);
         break;
      case MEDIUM_BROWN_FUNNEL_MUSHROOM:
         this.setCapType(Material.BROWN_MUSHROOM_BLOCK).setCapRadius(5.0F).setCapYOffset(-1).setBaseHeight(8).setBaseThickness(1.2F).setCapShape(FractalTypes.MushroomCap.FUNNEL);
         break;
      case MEDIUM_BROWN_MUSHROOM:
         this.setCapType(Material.BROWN_MUSHROOM_BLOCK).setCapRadius(5.0F).setCapYOffset(-2).setBaseHeight(8).setBaseThickness(1.2F);
         break;
      case MEDIUM_RED_MUSHROOM:
         this.setBaseThickness(1.3F).setBaseHeight(8).setThicknessIncrement(1.2000000476837158D).setCapRadius(4.5F).setCapYOffset(-3);
         break;
      case SMALL_BROWN_MUSHROOM:
         this.setBaseThickness(0.0F).setThicknessIncrement(0.0D).setBaseHeight(5).setCapType(Material.BROWN_MUSHROOM_BLOCK).setMinTilt(0.0D).setMaxTilt(0.39269908169872414D).setStemCurve(0.8F, 0.2F, 0.8F, 0.4F).setFourAxisRotationOnly(true).setCapShape(FractalTypes.MushroomCap.FLAT).setCapRadius(3.0F).setCapYOffset(0);
         break;
      case SMALL_RED_MUSHROOM:
         this.setBaseThickness(0.0F).setThicknessIncrement(0.5D).setBaseHeight(4).setMaxTilt(0.39269908169872414D).setMinTilt(0.0D).setStemCurve(0.8F, 0.2F, 0.8F, 0.4F).setFourAxisRotationOnly(true).setCapRadius(2.3F).setCapYOffset(-1);
         break;
      case SMALL_POINTY_RED_MUSHROOM:
         this.setBaseThickness(0.0F).setThicknessIncrement(0.0D).setBaseHeight(6).setMaxTilt(0.17453292519943295D).setMinTilt(0.0D).setFourAxisRotationOnly(true).setStemCurve(0.8F, 0.2F, 0.8F, 0.4F).setCapRadius(2.3F).setCapYOffset(-2).setCapShape(FractalTypes.MushroomCap.POINTY);
         break;
      case TINY_RED_MUSHROOM:
         this.setBaseThickness(0.0F).setThicknessIncrement(0.0D).setBaseHeight(4).setMinTilt(0.0D).setMaxTilt(0.0D).setStemCurve(0.5F, 0.5F, 0.5F, 0.5F).setSegmentFactor(1.0F).setCapRadius(1.2F).setCapYOffset(-2).setCapShape(FractalTypes.MushroomCap.POINTY);
      case TINY_BROWN_MUSHROOM:
         this.setBaseThickness(0.0F).setThicknessIncrement(0.0D).setBaseHeight(2).setMinTilt(0.0D).setMaxTilt(0.0D).setStemCurve(0.5F, 0.5F, 0.5F, 0.5F).setSegmentFactor(1.0F).setCapRadius(1.5F).setCapYOffset(1).setCapShape(FractalTypes.MushroomCap.ROUND).setCapType(Material.BROWN_MUSHROOM_BLOCK);
      }

   }

   private void replaceSphere(float radius, @NotNull SimpleBlock base, @NotNull Material type) {
      if ((double)radius < 0.5D) {
         if (!base.isSolid()) {
            base.setType(type);
         }

      } else {
         this.noiseGen.SetNoiseType(FastNoise.NoiseType.Simplex);
         this.noiseGen.SetFrequency(0.09F);

         for(int x = -Math.round(radius); x <= Math.round(radius); ++x) {
            for(int y = -Math.round(radius); y <= Math.round(radius); ++y) {
               for(int z = -Math.round(radius); z <= Math.round(radius); ++z) {
                  SimpleBlock block = base.getRelative(x, y, z);
                  if (Math.pow((double)x, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)y, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)radius, 2.0D) <= 1.0D + 0.7D * (double)this.noiseGen.GetNoise((float)block.getX(), (float)block.getY(), (float)block.getZ()) && !block.isSolid()) {
                     block.setType(type);
                  }
               }
            }
         }

      }
   }

   private void spawnSphericalCap(int seed, float r, float ry, @NotNull SimpleBlock base, boolean hardReplace, Material... type) {
      Random rand = new Random((long)seed);
      this.noiseGen.SetNoiseType(FastNoise.NoiseType.Simplex);
      this.noiseGen.SetFrequency(1.4F);
      float belowY = -0.5F * ry;
      float lowThreshold = Math.min((float)(0.12D * (double)Math.min(r, ry)), 0.6F);

      for(int x = Math.round(-r); x <= Math.round(r); ++x) {
         for(int y = Math.round(belowY); y <= Math.round(ry); ++y) {
            for(int z = Math.round(-r); z <= Math.round(r); ++z) {
               float factor = (float)y / belowY;
               if (y >= 0 || !((double)(factor + Math.abs(this.noiseGen.GetNoise((float)x / r, (float)z / r))) > 0.6D)) {
                  SimpleBlock rel = base.getRelative(x, y, z);
                  double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)r, 2.0D) + Math.pow((double)y, 2.0D) / Math.pow((double)ry, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)r, 2.0D);
                  if (equationResult <= 1.0D + 0.25D * (double)Math.abs(this.noiseGen.GetNoise((float)x / r, (float)y / ry, (float)z / r)) && equationResult >= (double)lowThreshold && (hardReplace || !rel.isSolid())) {
                     rel.setType((Material)GenUtils.randChoice(rand, type));
                     BlockUtils.correctSurroundingMushroomData(rel);
                  }
               }
            }
         }
      }

   }

   public void build(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (TConfig.areTallMushroomsEnabled()) {
         if (TConfig.c.DEVSTUFF_VANILLA_MUSHROOMS) {
            String schemName;
            if (this.type.toString().contains("RED")) {
               schemName = "redmushroomcap";
            } else {
               schemName = "brownmushroomcap";
            }

            VanillaMushroomBuilder.buildVanillaMushroom(tw, data, x, y, z, schemName);
         } else {
            this.noiseGen = new FastNoise((int)tw.getSeed());
            this.rand = tw.getRand(256L * (long)x + 16L * (long)y + (long)z);
            SimpleBlock base = new SimpleBlock(data, x, y, z);
            if (this.stemTop == null) {
               this.stemTop = base;
            }

            double initialAngle;
            if (this.fourAxisRotationOnly) {
               initialAngle = 1.5707963267948966D * (double)Math.round(Math.random() * 4.0D);
            } else {
               initialAngle = 6.283185307179586D * Math.random();
            }

            int initialHeight = this.baseHeight + GenUtils.randInt(0, 0);
            this.createStem(base, GenUtils.randDouble(this.rand, this.minTilt, this.maxTilt), initialAngle, (double)this.baseThickness, (double)initialHeight);
            switch(this.capShape) {
            case ROUND:
               this.spawnSphericalCap(tw.getHashedRand((long)x, y, z).nextInt(94929297), this.capRadius, this.capRadius, this.stemTop.getRelative(0, this.capYOffset, 0), true, this.capType);
               break;
            case FLAT:
               this.spawnSphericalCap(tw.getHashedRand((long)x, y, z).nextInt(94929297), this.capRadius, 0.6F * this.capRadius, this.stemTop.getRelative(0, this.capYOffset, 0), true, this.capType);
               break;
            case POINTY:
               this.spawnSphericalCap(tw.getHashedRand((long)x, y, z).nextInt(94929297), this.capRadius, this.capRadius * 1.8F, this.stemTop.getRelative(0, this.capYOffset, 0), true, this.capType);
               break;
            case FUNNEL:
               this.spawnFunnelCap(tw.getHashedRand((long)x, y, z).nextInt(94929297), this.capRadius, this.capRadius * 0.7F, this.capRadius * 0.1F, this.stemTop.getRelative(0, this.capYOffset, 0), this.capType);
            }

         }
      }
   }

   private void createStem(@NotNull SimpleBlock base, double tilt, double yaw, double thickness, double length) {
      int totalSegments = (int)(length * (double)this.segmentFactor);
      boolean oneBlockWide = thickness == 0.0D;
      Vector2f stem2d = new Vector2f((float)(length * Math.cos(1.5707963267948966D - tilt)), (float)(length * Math.sin(1.5707963267948966D - tilt)));
      Vector2f controlPoint1 = new Vector2f(this.curvatureControlPoint1.x * stem2d.x, this.curvatureControlPoint1.y * stem2d.y);
      Vector2f controlPoint2 = new Vector2f(this.curvatureControlPoint2.x * stem2d.x, this.curvatureControlPoint2.y * stem2d.y);
      BezierCurve curvature = new BezierCurve(new Vector2f(0.0F, 0.0F), controlPoint1, controlPoint2, stem2d);
      BezierCurve thicknessIncrementCurve = new BezierCurve(this.thicknessControlPoint1, this.thicknessControlPoint2);
      List<Integer> changedYs = new ArrayList();
      SimpleBlock lastSegment = null;

      for(int i = 0; i <= totalSegments; ++i) {
         float progress = (float)i / (float)totalSegments;
         Vector2f nextPos = curvature.calculate(progress);
         Vector stem3d = new Vector((double)nextPos.x * Math.sin(yaw), (double)nextPos.y, (double)nextPos.x * Math.cos(yaw));
         lastSegment = base.getRelative(stem3d);
         if (!changedYs.contains(lastSegment.getY()) || !oneBlockWide) {
            this.replaceSphere((float)(thickness / 2.0D + this.thicknessIncrement * (double)thicknessIncrementCurve.calculate(1.0F - progress).y), lastSegment, this.stemType);
            changedYs.add(lastSegment.getY());
         }
      }

      this.stemTop = lastSegment;
   }

   private void spawnFunnelCap(int seed, float r, float height, float thickness, @NotNull SimpleBlock base, Material... type) {
      Random rand = new Random((long)seed);
      this.noiseGen.SetNoiseType(FastNoise.NoiseType.Simplex);
      this.noiseGen.SetFrequency(1.4F);

      int heightLimit;
      for(int x = Math.round(-r); x <= Math.round(r); ++x) {
         for(int y = 0; y <= Math.round(3.0F * thickness); ++y) {
            for(heightLimit = Math.round(-r); heightLimit <= Math.round(r); ++heightLimit) {
               if (this.stemTop.getRelative(0, y, 0).getType() == this.stemType) {
                  this.stemTop.getRelative(0, y, 0).setType((Material)GenUtils.randChoice(rand, type));
               }

               double distToCenter = Math.sqrt((double)(x * x + heightLimit * heightLimit)) / (double)r;
               double realY = (double)y + (double)height * (Math.pow(distToCenter + 0.02D, 0.5D) - Math.pow(distToCenter - 0.15D, 8.0D));
               realY += (double)(thickness * Math.abs(this.noiseGen.GetNoise((float)x / r, (float)heightLimit / r)));
               SimpleBlock rel = base.getRelative(x, (int)Math.round(realY), heightLimit);
               double equationResult = Math.pow((double)((float)x / r), 2.0D) + Math.pow((double)Math.abs(y) / ((double)thickness / (1.0D - Math.pow(1.0D - (distToCenter + 0.1D), 6.0D))), 4.0D) + Math.pow((double)((float)heightLimit / r), 2.0D);
               if (equationResult <= 1.0D) {
                  rel.setType((Material)GenUtils.randChoice(rand, type));
                  BlockUtils.correctSurroundingMushroomData(rel);
               }
            }
         }
      }

      double angle = Math.random() * 3.141592653589793D * 2.0D;
      heightLimit = base.getY() + Math.round(height);
      int gillAmount = 16;

      label50:
      for(int i = 0; i < gillAmount; ++i) {
         angle += 3.141592653589793D / ((double)gillAmount / 2.0D);
         List<Vector2f> points = (new BresenhamLine(new Vector2f(0.0F, 0.0F), new Vector2f((float)(0.9D * (double)r * Math.cos(angle)), (float)(0.9D * (double)r * Math.sin(angle))))).getPoints();
         Iterator var14 = points.iterator();

         while(true) {
            label45:
            while(true) {
               if (!var14.hasNext()) {
                  continue label50;
               }

               Vector2f point = (Vector2f)var14.next();
               SimpleBlock pointBase = base.getRelative(Math.round(point.x), 0, Math.round(point.y));

               while(!pointBase.isSolid()) {
                  pointBase = pointBase.getUp();
                  if (pointBase.getY() > heightLimit) {
                     continue label45;
                  }
               }

               if (BlockUtils.isAir(pointBase.getDown().getType())) {
                  pointBase.getDown().setType(Material.MUSHROOM_STEM);
               }
            }
         }
      }

   }

   @NotNull
   private MushroomBuilder setBaseThickness(float baseThickness) {
      this.baseThickness = baseThickness;
      return this;
   }

   @NotNull
   private MushroomBuilder setBaseHeight(int h) {
      this.baseHeight = h;
      return this;
   }

   @NotNull
   private MushroomBuilder setStemType(Material stemType) {
      this.stemType = stemType;
      return this;
   }

   @NotNull
   private MushroomBuilder setCapType(Material capType) {
      this.capType = capType;
      return this;
   }

   @NotNull
   private MushroomBuilder setSpotType(Material spotType) {
      this.spotType = spotType;
      return this;
   }

   @NotNull
   private MushroomBuilder setMinTilt(double minTilt) {
      this.minTilt = minTilt;
      return this;
   }

   @NotNull
   private MushroomBuilder setMaxTilt(double maxTilt) {
      this.maxTilt = maxTilt;
      return this;
   }

   @NotNull
   private MushroomBuilder setCapRadius(float capRadius) {
      this.capRadius = capRadius;
      return this;
   }

   @NotNull
   private MushroomBuilder setCapYOffset(int capYOffset) {
      this.capYOffset = capYOffset;
      return this;
   }

   @NotNull
   private MushroomBuilder setSegmentFactor(float segmentFactor) {
      this.segmentFactor = segmentFactor;
      return this;
   }

   @NotNull
   private MushroomBuilder setStemCurve(Vector2f controlPoint1, Vector2f controlPoint2) {
      this.curvatureControlPoint1 = controlPoint1;
      this.curvatureControlPoint2 = controlPoint2;
      return this;
   }

   @NotNull
   private MushroomBuilder setStemCurve(float controlP1x, float controlP1y, float controlP2x, float controlP2y) {
      return this.setStemCurve(new Vector2f(controlP1x, controlP1y), new Vector2f(controlP2x, controlP2y));
   }

   @NotNull
   private MushroomBuilder setThicknessIncrement(double thicknessIncrement) {
      this.thicknessIncrement = thicknessIncrement;
      return this;
   }

   @NotNull
   private MushroomBuilder setThicknessIncrementCurve(Vector2f controlPoint1, Vector2f controlPoint2) {
      this.thicknessControlPoint1 = controlPoint1;
      this.thicknessControlPoint2 = controlPoint2;
      return this;
   }

   @NotNull
   private MushroomBuilder setThicknessIncrementCurve(float controlP1x, float controlP1y, float controlP2x, float controlP2y) {
      return this.setThicknessIncrementCurve(new Vector2f(controlP1x, controlP1y), new Vector2f(controlP2x, controlP2y));
   }

   @NotNull
   private MushroomBuilder setCapShape(FractalTypes.MushroomCap capShape) {
      this.capShape = capShape;
      return this;
   }

   @NotNull
   private MushroomBuilder setFourAxisRotationOnly(boolean fourAxisRotationOnly) {
      this.fourAxisRotationOnly = fourAxisRotationOnly;
      return this;
   }
}
