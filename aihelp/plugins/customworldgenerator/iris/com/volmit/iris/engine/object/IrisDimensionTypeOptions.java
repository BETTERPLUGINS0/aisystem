package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.util.data.Varint;
import com.volmit.iris.util.json.JSONObject;
import java.io.DataInput;
import java.io.DataOutput;
import java.util.Arrays;
import lombok.Generated;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

@Desc("Optional options for the dimension type")
public class IrisDimensionTypeOptions {
   @MinNumber(1.0E-5D)
   @MaxNumber(3.0E7D)
   @Desc("The multiplier applied to coordinates when leaving the dimension. Value between 0.00001 and 30000000.0 (both inclusive).")
   private double coordinateScale = -1.0D;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("How much light the dimension has. When set to 0, it completely follows the light level; when set to 1, there is no ambient lighting.")
   private float ambientLight = -1.0F;
   @MinNumber(0.0D)
   @MaxNumber(9.223372036854776E18D)
   @Desc("If this is set to an int, the time of the day is the specified value. To ensure a normal time cycle, set it to null.")
   @Nullable
   private Long fixedTime = -1L;
   @MinNumber(-2032.0D)
   @MaxNumber(2031.0D)
   @Desc("Optional value between -2032 and 2031. If set, determines the lower edge of the clouds. If set to null, clouds are disabled in the dimension.")
   @Nullable
   private Integer cloudHeight = -1;
   @MinNumber(0.0D)
   @MaxNumber(15.0D)
   @Desc("Value between 0 and 15 (both inclusive). Maximum block light required when the monster spawns.")
   private int monsterSpawnBlockLightLimit = -1;
   @Desc("Whether the dimensions behaves like the nether (water evaporates and sponges dry) or not. Also lets stalactites drip lava and causes lava to spread faster and thinner.")
   @NonNull
   private IrisDimensionTypeOptions.TriState ultrawarm;
   @Desc("When false, compasses spin randomly, and using a bed to set the respawn point or sleep, is disabled. When true, nether portals can spawn zombified piglins, and creaking hearts can spawn creakings.")
   @NonNull
   private IrisDimensionTypeOptions.TriState natural;
   @Desc("When false, Piglins and hoglins shake and transform to zombified entities.")
   @NonNull
   private IrisDimensionTypeOptions.TriState piglinSafe;
   @Desc("When false, the respawn anchor blows up when trying to set spawn point.")
   @NonNull
   private IrisDimensionTypeOptions.TriState respawnAnchorWorks;
   @Desc("When false, the bed blows up when trying to sleep.")
   @NonNull
   private IrisDimensionTypeOptions.TriState bedWorks;
   @Desc("Whether players with the Bad Omen effect can cause a raid.")
   @NonNull
   private IrisDimensionTypeOptions.TriState raids;
   @Desc("Whether the dimension has skylight or not.")
   @NonNull
   private IrisDimensionTypeOptions.TriState skylight;
   @Desc("Whether the dimension has a bedrock ceiling. Note that this is only a logical ceiling. It is unrelated with whether the dimension really has a block ceiling.")
   @NonNull
   private IrisDimensionTypeOptions.TriState ceiling;

   public IrisDimensionTypeOptions(@NonNull IrisDimensionTypeOptions.TriState ultrawarm, @NonNull IrisDimensionTypeOptions.TriState natural, @NonNull IrisDimensionTypeOptions.TriState piglinSafe, @NonNull IrisDimensionTypeOptions.TriState respawnAnchorWorks, @NonNull IrisDimensionTypeOptions.TriState bedWorks, @NonNull IrisDimensionTypeOptions.TriState raids, @NonNull IrisDimensionTypeOptions.TriState skylight, @NonNull IrisDimensionTypeOptions.TriState ceiling, double coordinateScale, float ambientLight, @Nullable Long fixedTime, @Nullable Integer cloudHeight, int monsterSpawnBlockLightLimit) {
      this.ultrawarm = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.natural = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.piglinSafe = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.respawnAnchorWorks = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.bedWorks = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.raids = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.skylight = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.ceiling = IrisDimensionTypeOptions.TriState.DEFAULT;
      if (var1 == null) {
         throw new NullPointerException("ultrawarm is marked non-null but is null");
      } else if (var2 == null) {
         throw new NullPointerException("natural is marked non-null but is null");
      } else if (var3 == null) {
         throw new NullPointerException("piglinSafe is marked non-null but is null");
      } else if (var4 == null) {
         throw new NullPointerException("respawnAnchorWorks is marked non-null but is null");
      } else if (var5 == null) {
         throw new NullPointerException("bedWorks is marked non-null but is null");
      } else if (var6 == null) {
         throw new NullPointerException("raids is marked non-null but is null");
      } else if (var7 == null) {
         throw new NullPointerException("skylight is marked non-null but is null");
      } else if (var8 == null) {
         throw new NullPointerException("ceiling is marked non-null but is null");
      } else if (var9 != -1.0D && (var9 < 1.0E-5D || var9 > 3.0E7D)) {
         throw new IllegalArgumentException("Coordinate scale must be between 0.00001 and 30000000");
      } else if (var11 == -1.0F || !(var11 < 0.0F) && !(var11 > 1.0F)) {
         if (var13 != null && var13 != -1 && (var13 < -2032 || var13 > 2031)) {
            throw new IllegalArgumentException("Cloud height must be between -2032 and 2031");
         } else if (var14 == -1 || var14 >= 0 && var14 <= 15) {
            this.ultrawarm = var1;
            this.natural = var2;
            this.piglinSafe = var3;
            this.respawnAnchorWorks = var4;
            this.bedWorks = var5;
            this.raids = var6;
            this.skylight = var7;
            this.ceiling = var8;
            this.coordinateScale = var9;
            this.ambientLight = var11;
            this.fixedTime = var12;
            this.cloudHeight = var13;
            this.monsterSpawnBlockLightLimit = var14;
         } else {
            throw new IllegalArgumentException("Monster spawn block light limit must be between 0 and 15");
         }
      } else {
         throw new IllegalArgumentException("Ambient light must be between 0 and 1");
      }
   }

   public IrisDimensionTypeOptions coordinateScale(double coordinateScale) {
      if (var1 == -1.0D || !(var1 < 1.0E-5D) && !(var1 > 3.0E7D)) {
         this.coordinateScale = var1;
         return this;
      } else {
         throw new IllegalArgumentException("Coordinate scale must be between 0.00001 and 30000000");
      }
   }

   public IrisDimensionTypeOptions ambientLight(float ambientLight) {
      if (var1 == -1.0F || !(var1 < 0.0F) && !(var1 > 1.0F)) {
         this.ambientLight = var1;
         return this;
      } else {
         throw new IllegalArgumentException("Ambient light must be between 0 and 1");
      }
   }

   public IrisDimensionTypeOptions cloudHeight(@Nullable Integer cloudHeight) {
      if (var1 == null || var1 == -1 || var1 >= -2032 && var1 <= 2031) {
         this.cloudHeight = var1;
         return this;
      } else {
         throw new IllegalArgumentException("Cloud height must be between -2032 and 2031");
      }
   }

   public IrisDimensionTypeOptions monsterSpawnBlockLightLimit(int monsterSpawnBlockLightLimit) {
      if (var1 == -1 || var1 >= 0 && var1 <= 15) {
         this.monsterSpawnBlockLightLimit = var1;
         return this;
      } else {
         throw new IllegalArgumentException("Monster spawn block light limit must be between 0 and 15");
      }
   }

   public void write(DataOutput dos) {
      int var2 = 0;
      int var3 = 0;
      IrisDimensionTypeOptions.TriState[] var4 = new IrisDimensionTypeOptions.TriState[]{this.ultrawarm, this.natural, this.skylight, this.ceiling, this.piglinSafe, this.bedWorks, this.respawnAnchorWorks, this.raids};
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         IrisDimensionTypeOptions.TriState var7 = var4[var6];
         if (var7 == IrisDimensionTypeOptions.TriState.DEFAULT) {
            ++var3;
         } else {
            var2 |= (short)(1 << var3++);
            if (var7 == IrisDimensionTypeOptions.TriState.TRUE) {
               var2 |= (short)(1 << var3++);
            }
         }
      }

      if (this.coordinateScale != -1.0D) {
         var2 |= 1 << var3++;
      }

      if (this.ambientLight != -1.0F) {
         var2 |= 1 << var3++;
      }

      if (this.monsterSpawnBlockLightLimit != -1) {
         var2 |= 1 << var3++;
      }

      if (this.fixedTime != null) {
         var2 |= 1 << var3++;
         if (this.fixedTime != -1L) {
            var2 |= 1 << var3++;
         }
      }

      if (this.cloudHeight != null) {
         var2 |= 1 << var3++;
         if (this.cloudHeight != -1) {
            var2 |= 1 << var3;
         }
      }

      Varint.writeSignedVarInt(var2, var1);
      if (this.coordinateScale != -1.0D) {
         Varint.writeUnsignedVarLong(Double.doubleToLongBits(this.coordinateScale), var1);
      }

      if (this.ambientLight != -1.0F) {
         Varint.writeUnsignedVarInt(Float.floatToIntBits(this.ambientLight), var1);
      }

      if (this.monsterSpawnBlockLightLimit != -1) {
         Varint.writeSignedVarInt(this.monsterSpawnBlockLightLimit, var1);
      }

      if (this.fixedTime != null && this.fixedTime != -1L) {
         Varint.writeSignedVarLong(this.fixedTime, var1);
      }

      if (this.cloudHeight != null && this.cloudHeight != -1) {
         Varint.writeSignedVarInt(this.cloudHeight, var1);
      }

   }

   public IrisDimensionTypeOptions read(DataInput dis) {
      IrisDimensionTypeOptions.TriState[] var2 = new IrisDimensionTypeOptions.TriState[8];
      Arrays.fill(var2, IrisDimensionTypeOptions.TriState.DEFAULT);
      int var3 = Varint.readSignedVarInt(var1);
      int var4 = 0;

      for(int var5 = 0; var5 < 8; ++var5) {
         if ((var3 & 1 << var4++) != 0) {
            var2[var5] = (var3 & 1 << var4++) == 0 ? IrisDimensionTypeOptions.TriState.FALSE : IrisDimensionTypeOptions.TriState.TRUE;
         }
      }

      this.ultrawarm = var2[0];
      this.natural = var2[1];
      this.skylight = var2[2];
      this.ceiling = var2[3];
      this.piglinSafe = var2[4];
      this.bedWorks = var2[5];
      this.respawnAnchorWorks = var2[6];
      this.raids = var2[7];
      this.coordinateScale = (var3 & 1 << var4++) != 0 ? Double.longBitsToDouble(Varint.readUnsignedVarLong(var1)) : -1.0D;
      this.ambientLight = (var3 & 1 << var4++) != 0 ? Float.intBitsToFloat(Varint.readUnsignedVarInt(var1)) : -1.0F;
      this.monsterSpawnBlockLightLimit = (var3 & 1 << var4++) != 0 ? Varint.readSignedVarInt(var1) : -1;
      this.fixedTime = (var3 & 1 << var4++) != 0 ? (var3 & 1 << var4) != 0 ? Varint.readSignedVarLong(var1) : -1L : null;
      this.cloudHeight = (var3 & 1 << var4++) != 0 ? (var3 & 1 << var4) != 0 ? Varint.readSignedVarInt(var1) : -1 : null;
      return this;
   }

   public IrisDimensionTypeOptions resolve(IrisDimensionTypeOptions other) {
      if (this.ultrawarm == IrisDimensionTypeOptions.TriState.DEFAULT) {
         this.ultrawarm = var1.ultrawarm;
      }

      if (this.natural == IrisDimensionTypeOptions.TriState.DEFAULT) {
         this.natural = var1.natural;
      }

      if (this.piglinSafe == IrisDimensionTypeOptions.TriState.DEFAULT) {
         this.piglinSafe = var1.piglinSafe;
      }

      if (this.respawnAnchorWorks == IrisDimensionTypeOptions.TriState.DEFAULT) {
         this.respawnAnchorWorks = var1.respawnAnchorWorks;
      }

      if (this.bedWorks == IrisDimensionTypeOptions.TriState.DEFAULT) {
         this.bedWorks = var1.bedWorks;
      }

      if (this.raids == IrisDimensionTypeOptions.TriState.DEFAULT) {
         this.raids = var1.raids;
      }

      if (this.skylight == IrisDimensionTypeOptions.TriState.DEFAULT) {
         this.skylight = var1.skylight;
      }

      if (this.ceiling == IrisDimensionTypeOptions.TriState.DEFAULT) {
         this.ceiling = var1.ceiling;
      }

      if (this.coordinateScale == -1.0D) {
         this.coordinateScale = var1.coordinateScale;
      }

      if (this.ambientLight == -1.0F) {
         this.ambientLight = var1.ambientLight;
      }

      if (this.fixedTime != null && this.fixedTime == -1L) {
         this.fixedTime = var1.fixedTime;
      }

      if (this.cloudHeight != null && this.cloudHeight == -1) {
         this.cloudHeight = var1.cloudHeight;
      }

      if (this.monsterSpawnBlockLightLimit == -1) {
         this.monsterSpawnBlockLightLimit = var1.monsterSpawnBlockLightLimit;
      }

      return this;
   }

   public JSONObject toJson() {
      if (!this.isComplete()) {
         throw new IllegalStateException("Cannot serialize incomplete options");
      } else {
         JSONObject var1 = new JSONObject();
         var1.put("ultrawarm", this.ultrawarm.bool());
         var1.put("natural", this.natural.bool());
         var1.put("piglin_safe", this.piglinSafe.bool());
         var1.put("respawn_anchor_works", this.respawnAnchorWorks.bool());
         var1.put("bed_works", this.bedWorks.bool());
         var1.put("has_raids", this.raids.bool());
         var1.put("has_skylight", this.skylight.bool());
         var1.put("has_ceiling", this.ceiling.bool());
         var1.put("coordinate_scale", this.coordinateScale);
         var1.put("ambient_light", (double)this.ambientLight);
         var1.put("monster_spawn_block_light_limit", this.monsterSpawnBlockLightLimit);
         if (this.fixedTime != null) {
            var1.put("fixed_time", (Object)this.fixedTime);
         }

         if (this.cloudHeight != null) {
            var1.put("cloud_height", (Object)this.cloudHeight);
         }

         return var1;
      }
   }

   public IrisDimensionTypeOptions copy() {
      return new IrisDimensionTypeOptions(this.ultrawarm, this.natural, this.piglinSafe, this.respawnAnchorWorks, this.bedWorks, this.raids, this.skylight, this.ceiling, this.coordinateScale, this.ambientLight, this.fixedTime, this.cloudHeight, this.monsterSpawnBlockLightLimit);
   }

   public boolean isComplete() {
      return this.ultrawarm != IrisDimensionTypeOptions.TriState.DEFAULT && this.natural != IrisDimensionTypeOptions.TriState.DEFAULT && this.piglinSafe != IrisDimensionTypeOptions.TriState.DEFAULT && this.respawnAnchorWorks != IrisDimensionTypeOptions.TriState.DEFAULT && this.bedWorks != IrisDimensionTypeOptions.TriState.DEFAULT && this.raids != IrisDimensionTypeOptions.TriState.DEFAULT && this.skylight != IrisDimensionTypeOptions.TriState.DEFAULT && this.ceiling != IrisDimensionTypeOptions.TriState.DEFAULT && this.coordinateScale != -1.0D && this.ambientLight != -1.0F && this.monsterSpawnBlockLightLimit != -1 && (this.fixedTime == null || this.fixedTime != -1L) && (this.cloudHeight == null || this.cloudHeight != -1);
   }

   @Generated
   public double coordinateScale() {
      return this.coordinateScale;
   }

   @Generated
   public float ambientLight() {
      return this.ambientLight;
   }

   @Nullable
   @Generated
   public Long fixedTime() {
      return this.fixedTime;
   }

   @Nullable
   @Generated
   public Integer cloudHeight() {
      return this.cloudHeight;
   }

   @Generated
   public int monsterSpawnBlockLightLimit() {
      return this.monsterSpawnBlockLightLimit;
   }

   @NonNull
   @Generated
   public IrisDimensionTypeOptions.TriState ultrawarm() {
      return this.ultrawarm;
   }

   @NonNull
   @Generated
   public IrisDimensionTypeOptions.TriState natural() {
      return this.natural;
   }

   @NonNull
   @Generated
   public IrisDimensionTypeOptions.TriState piglinSafe() {
      return this.piglinSafe;
   }

   @NonNull
   @Generated
   public IrisDimensionTypeOptions.TriState respawnAnchorWorks() {
      return this.respawnAnchorWorks;
   }

   @NonNull
   @Generated
   public IrisDimensionTypeOptions.TriState bedWorks() {
      return this.bedWorks;
   }

   @NonNull
   @Generated
   public IrisDimensionTypeOptions.TriState raids() {
      return this.raids;
   }

   @NonNull
   @Generated
   public IrisDimensionTypeOptions.TriState skylight() {
      return this.skylight;
   }

   @NonNull
   @Generated
   public IrisDimensionTypeOptions.TriState ceiling() {
      return this.ceiling;
   }

   @Generated
   public IrisDimensionTypeOptions fixedTime(@Nullable final Long fixedTime) {
      this.fixedTime = var1;
      return this;
   }

   @Generated
   public IrisDimensionTypeOptions ultrawarm(@NonNull final IrisDimensionTypeOptions.TriState ultrawarm) {
      if (var1 == null) {
         throw new NullPointerException("ultrawarm is marked non-null but is null");
      } else {
         this.ultrawarm = var1;
         return this;
      }
   }

   @Generated
   public IrisDimensionTypeOptions natural(@NonNull final IrisDimensionTypeOptions.TriState natural) {
      if (var1 == null) {
         throw new NullPointerException("natural is marked non-null but is null");
      } else {
         this.natural = var1;
         return this;
      }
   }

   @Generated
   public IrisDimensionTypeOptions piglinSafe(@NonNull final IrisDimensionTypeOptions.TriState piglinSafe) {
      if (var1 == null) {
         throw new NullPointerException("piglinSafe is marked non-null but is null");
      } else {
         this.piglinSafe = var1;
         return this;
      }
   }

   @Generated
   public IrisDimensionTypeOptions respawnAnchorWorks(@NonNull final IrisDimensionTypeOptions.TriState respawnAnchorWorks) {
      if (var1 == null) {
         throw new NullPointerException("respawnAnchorWorks is marked non-null but is null");
      } else {
         this.respawnAnchorWorks = var1;
         return this;
      }
   }

   @Generated
   public IrisDimensionTypeOptions bedWorks(@NonNull final IrisDimensionTypeOptions.TriState bedWorks) {
      if (var1 == null) {
         throw new NullPointerException("bedWorks is marked non-null but is null");
      } else {
         this.bedWorks = var1;
         return this;
      }
   }

   @Generated
   public IrisDimensionTypeOptions raids(@NonNull final IrisDimensionTypeOptions.TriState raids) {
      if (var1 == null) {
         throw new NullPointerException("raids is marked non-null but is null");
      } else {
         this.raids = var1;
         return this;
      }
   }

   @Generated
   public IrisDimensionTypeOptions skylight(@NonNull final IrisDimensionTypeOptions.TriState skylight) {
      if (var1 == null) {
         throw new NullPointerException("skylight is marked non-null but is null");
      } else {
         this.skylight = var1;
         return this;
      }
   }

   @Generated
   public IrisDimensionTypeOptions ceiling(@NonNull final IrisDimensionTypeOptions.TriState ceiling) {
      if (var1 == null) {
         throw new NullPointerException("ceiling is marked non-null but is null");
      } else {
         this.ceiling = var1;
         return this;
      }
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisDimensionTypeOptions)) {
         return false;
      } else {
         IrisDimensionTypeOptions var2 = (IrisDimensionTypeOptions)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.coordinateScale(), var2.coordinateScale()) != 0) {
            return false;
         } else if (Float.compare(this.ambientLight(), var2.ambientLight()) != 0) {
            return false;
         } else if (this.monsterSpawnBlockLightLimit() != var2.monsterSpawnBlockLightLimit()) {
            return false;
         } else {
            Long var3 = this.fixedTime();
            Long var4 = var2.fixedTime();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            Integer var5 = this.cloudHeight();
            Integer var6 = var2.cloudHeight();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label124: {
               IrisDimensionTypeOptions.TriState var7 = this.ultrawarm();
               IrisDimensionTypeOptions.TriState var8 = var2.ultrawarm();
               if (var7 == null) {
                  if (var8 == null) {
                     break label124;
                  }
               } else if (var7.equals(var8)) {
                  break label124;
               }

               return false;
            }

            IrisDimensionTypeOptions.TriState var9 = this.natural();
            IrisDimensionTypeOptions.TriState var10 = var2.natural();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            IrisDimensionTypeOptions.TriState var11 = this.piglinSafe();
            IrisDimensionTypeOptions.TriState var12 = var2.piglinSafe();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            label103: {
               IrisDimensionTypeOptions.TriState var13 = this.respawnAnchorWorks();
               IrisDimensionTypeOptions.TriState var14 = var2.respawnAnchorWorks();
               if (var13 == null) {
                  if (var14 == null) {
                     break label103;
                  }
               } else if (var13.equals(var14)) {
                  break label103;
               }

               return false;
            }

            label96: {
               IrisDimensionTypeOptions.TriState var15 = this.bedWorks();
               IrisDimensionTypeOptions.TriState var16 = var2.bedWorks();
               if (var15 == null) {
                  if (var16 == null) {
                     break label96;
                  }
               } else if (var15.equals(var16)) {
                  break label96;
               }

               return false;
            }

            label89: {
               IrisDimensionTypeOptions.TriState var17 = this.raids();
               IrisDimensionTypeOptions.TriState var18 = var2.raids();
               if (var17 == null) {
                  if (var18 == null) {
                     break label89;
                  }
               } else if (var17.equals(var18)) {
                  break label89;
               }

               return false;
            }

            IrisDimensionTypeOptions.TriState var19 = this.skylight();
            IrisDimensionTypeOptions.TriState var20 = var2.skylight();
            if (var19 == null) {
               if (var20 != null) {
                  return false;
               }
            } else if (!var19.equals(var20)) {
               return false;
            }

            IrisDimensionTypeOptions.TriState var21 = this.ceiling();
            IrisDimensionTypeOptions.TriState var22 = var2.ceiling();
            if (var21 == null) {
               if (var22 != null) {
                  return false;
               }
            } else if (!var21.equals(var22)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisDimensionTypeOptions;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.coordinateScale());
      int var15 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var15 = var15 * 59 + Float.floatToIntBits(this.ambientLight());
      var15 = var15 * 59 + this.monsterSpawnBlockLightLimit();
      Long var5 = this.fixedTime();
      var15 = var15 * 59 + (var5 == null ? 43 : var5.hashCode());
      Integer var6 = this.cloudHeight();
      var15 = var15 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisDimensionTypeOptions.TriState var7 = this.ultrawarm();
      var15 = var15 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisDimensionTypeOptions.TriState var8 = this.natural();
      var15 = var15 * 59 + (var8 == null ? 43 : var8.hashCode());
      IrisDimensionTypeOptions.TriState var9 = this.piglinSafe();
      var15 = var15 * 59 + (var9 == null ? 43 : var9.hashCode());
      IrisDimensionTypeOptions.TriState var10 = this.respawnAnchorWorks();
      var15 = var15 * 59 + (var10 == null ? 43 : var10.hashCode());
      IrisDimensionTypeOptions.TriState var11 = this.bedWorks();
      var15 = var15 * 59 + (var11 == null ? 43 : var11.hashCode());
      IrisDimensionTypeOptions.TriState var12 = this.raids();
      var15 = var15 * 59 + (var12 == null ? 43 : var12.hashCode());
      IrisDimensionTypeOptions.TriState var13 = this.skylight();
      var15 = var15 * 59 + (var13 == null ? 43 : var13.hashCode());
      IrisDimensionTypeOptions.TriState var14 = this.ceiling();
      var15 = var15 * 59 + (var14 == null ? 43 : var14.hashCode());
      return var15;
   }

   @Generated
   public String toString() {
      double var10000 = this.coordinateScale();
      return "IrisDimensionTypeOptions(coordinateScale=" + var10000 + ", ambientLight=" + this.ambientLight() + ", fixedTime=" + this.fixedTime() + ", cloudHeight=" + this.cloudHeight() + ", monsterSpawnBlockLightLimit=" + this.monsterSpawnBlockLightLimit() + ", ultrawarm=" + String.valueOf(this.ultrawarm()) + ", natural=" + String.valueOf(this.natural()) + ", piglinSafe=" + String.valueOf(this.piglinSafe()) + ", respawnAnchorWorks=" + String.valueOf(this.respawnAnchorWorks()) + ", bedWorks=" + String.valueOf(this.bedWorks()) + ", raids=" + String.valueOf(this.raids()) + ", skylight=" + String.valueOf(this.skylight()) + ", ceiling=" + String.valueOf(this.ceiling()) + ")";
   }

   @Generated
   public IrisDimensionTypeOptions() {
      this.ultrawarm = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.natural = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.piglinSafe = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.respawnAnchorWorks = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.bedWorks = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.raids = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.skylight = IrisDimensionTypeOptions.TriState.DEFAULT;
      this.ceiling = IrisDimensionTypeOptions.TriState.DEFAULT;
   }

   @Desc("Allows reusing the behavior of the base dimension")
   public static enum TriState {
      @Desc("Follow the behavior of the base dimension")
      DEFAULT,
      @Desc("True")
      TRUE,
      @Desc("False")
      FALSE;

      public boolean bool() {
         return this == TRUE;
      }

      // $FF: synthetic method
      private static IrisDimensionTypeOptions.TriState[] $values() {
         return new IrisDimensionTypeOptions.TriState[]{DEFAULT, TRUE, FALSE};
      }
   }
}
