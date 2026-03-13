package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;
import lombok.Generated;

@Desc("Represents a dimension")
public class IrisMod extends IrisRegistrant {
   @MinNumber(2.0D)
   @Required
   @Desc("The human readable name of this dimension")
   private String name = "A Dimension Mod";
   @Desc("If this mod only works with a specific dimension, define it's load key here. Such as overworld, or flat. Otherwise iris will assume this mod works with anything.")
   private String forDimension = "";
   @MinNumber(-1.0D)
   @MaxNumber(512.0D)
   @Desc("Override the fluid height. Otherwise set it to -1")
   private int overrideFluidHeight = -1;
   @Desc("A list of biomes to remove")
   @RegistryListResource(IrisBiome.class)
   @ArrayType(
      type = String.class,
      min = 1
   )
   private KList<String> removeBiomes = new KList();
   @Desc("A list of objects to remove")
   @RegistryListResource(IrisObject.class)
   @ArrayType(
      type = String.class,
      min = 1
   )
   private KList<String> removeObjects = new KList();
   @Desc("A list of regions to remove")
   @RegistryListResource(IrisRegion.class)
   @ArrayType(
      type = String.class,
      min = 1
   )
   private KList<String> removeRegions = new KList();
   @Desc("A list of regions to inject")
   @RegistryListResource(IrisRegion.class)
   @ArrayType(
      type = String.class,
      min = 1
   )
   private KList<String> injectRegions = new KList();
   @ArrayType(
      min = 1,
      type = IrisModBiomeInjector.class
   )
   @Desc("Inject biomes into existing regions")
   private KList<IrisModBiomeInjector> biomeInjectors = new KList();
   @ArrayType(
      min = 1,
      type = IrisModBiomeReplacer.class
   )
   @Desc("Replace biomes with other biomes")
   private KList<IrisModBiomeReplacer> biomeReplacers = new KList();
   @ArrayType(
      min = 1,
      type = IrisModObjectReplacer.class
   )
   @Desc("Replace objects with other objects")
   private KList<IrisModObjectReplacer> objectReplacers = new KList();
   @ArrayType(
      min = 1,
      type = IrisModObjectPlacementBiomeInjector.class
   )
   @Desc("Inject placers into existing biomes")
   private KList<IrisModObjectPlacementBiomeInjector> biomeObjectPlacementInjectors = new KList();
   @ArrayType(
      min = 1,
      type = IrisModObjectPlacementRegionInjector.class
   )
   @Desc("Inject placers into existing regions")
   private KList<IrisModObjectPlacementRegionInjector> regionObjectPlacementInjectors = new KList();
   @ArrayType(
      min = 1,
      type = IrisModRegionReplacer.class
   )
   @Desc("Replace biomes with other biomes")
   private KList<IrisModRegionReplacer> regionReplacers = new KList();
   @ArrayType(
      min = 1,
      type = IrisObjectReplace.class
   )
   @Desc("Replace blocks with other blocks")
   private KList<IrisObjectReplace> blockReplacers = new KList();
   @ArrayType(
      min = 1,
      type = IrisModNoiseStyleReplacer.class
   )
   @Desc("Replace noise styles with other styles")
   private KList<IrisModNoiseStyleReplacer> styleReplacers = new KList();

   public String getFolderName() {
      return "mods";
   }

   public String getTypeName() {
      return "Mod";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisMod(final String name, final String forDimension, final int overrideFluidHeight, final KList<String> removeBiomes, final KList<String> removeObjects, final KList<String> removeRegions, final KList<String> injectRegions, final KList<IrisModBiomeInjector> biomeInjectors, final KList<IrisModBiomeReplacer> biomeReplacers, final KList<IrisModObjectReplacer> objectReplacers, final KList<IrisModObjectPlacementBiomeInjector> biomeObjectPlacementInjectors, final KList<IrisModObjectPlacementRegionInjector> regionObjectPlacementInjectors, final KList<IrisModRegionReplacer> regionReplacers, final KList<IrisObjectReplace> blockReplacers, final KList<IrisModNoiseStyleReplacer> styleReplacers) {
      this.name = var1;
      this.forDimension = var2;
      this.overrideFluidHeight = var3;
      this.removeBiomes = var4;
      this.removeObjects = var5;
      this.removeRegions = var6;
      this.injectRegions = var7;
      this.biomeInjectors = var8;
      this.biomeReplacers = var9;
      this.objectReplacers = var10;
      this.biomeObjectPlacementInjectors = var11;
      this.regionObjectPlacementInjectors = var12;
      this.regionReplacers = var13;
      this.blockReplacers = var14;
      this.styleReplacers = var15;
   }

   @Generated
   public IrisMod() {
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public String getForDimension() {
      return this.forDimension;
   }

   @Generated
   public int getOverrideFluidHeight() {
      return this.overrideFluidHeight;
   }

   @Generated
   public KList<String> getRemoveBiomes() {
      return this.removeBiomes;
   }

   @Generated
   public KList<String> getRemoveObjects() {
      return this.removeObjects;
   }

   @Generated
   public KList<String> getRemoveRegions() {
      return this.removeRegions;
   }

   @Generated
   public KList<String> getInjectRegions() {
      return this.injectRegions;
   }

   @Generated
   public KList<IrisModBiomeInjector> getBiomeInjectors() {
      return this.biomeInjectors;
   }

   @Generated
   public KList<IrisModBiomeReplacer> getBiomeReplacers() {
      return this.biomeReplacers;
   }

   @Generated
   public KList<IrisModObjectReplacer> getObjectReplacers() {
      return this.objectReplacers;
   }

   @Generated
   public KList<IrisModObjectPlacementBiomeInjector> getBiomeObjectPlacementInjectors() {
      return this.biomeObjectPlacementInjectors;
   }

   @Generated
   public KList<IrisModObjectPlacementRegionInjector> getRegionObjectPlacementInjectors() {
      return this.regionObjectPlacementInjectors;
   }

   @Generated
   public KList<IrisModRegionReplacer> getRegionReplacers() {
      return this.regionReplacers;
   }

   @Generated
   public KList<IrisObjectReplace> getBlockReplacers() {
      return this.blockReplacers;
   }

   @Generated
   public KList<IrisModNoiseStyleReplacer> getStyleReplacers() {
      return this.styleReplacers;
   }

   @Generated
   public IrisMod setName(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisMod setForDimension(final String forDimension) {
      this.forDimension = var1;
      return this;
   }

   @Generated
   public IrisMod setOverrideFluidHeight(final int overrideFluidHeight) {
      this.overrideFluidHeight = var1;
      return this;
   }

   @Generated
   public IrisMod setRemoveBiomes(final KList<String> removeBiomes) {
      this.removeBiomes = var1;
      return this;
   }

   @Generated
   public IrisMod setRemoveObjects(final KList<String> removeObjects) {
      this.removeObjects = var1;
      return this;
   }

   @Generated
   public IrisMod setRemoveRegions(final KList<String> removeRegions) {
      this.removeRegions = var1;
      return this;
   }

   @Generated
   public IrisMod setInjectRegions(final KList<String> injectRegions) {
      this.injectRegions = var1;
      return this;
   }

   @Generated
   public IrisMod setBiomeInjectors(final KList<IrisModBiomeInjector> biomeInjectors) {
      this.biomeInjectors = var1;
      return this;
   }

   @Generated
   public IrisMod setBiomeReplacers(final KList<IrisModBiomeReplacer> biomeReplacers) {
      this.biomeReplacers = var1;
      return this;
   }

   @Generated
   public IrisMod setObjectReplacers(final KList<IrisModObjectReplacer> objectReplacers) {
      this.objectReplacers = var1;
      return this;
   }

   @Generated
   public IrisMod setBiomeObjectPlacementInjectors(final KList<IrisModObjectPlacementBiomeInjector> biomeObjectPlacementInjectors) {
      this.biomeObjectPlacementInjectors = var1;
      return this;
   }

   @Generated
   public IrisMod setRegionObjectPlacementInjectors(final KList<IrisModObjectPlacementRegionInjector> regionObjectPlacementInjectors) {
      this.regionObjectPlacementInjectors = var1;
      return this;
   }

   @Generated
   public IrisMod setRegionReplacers(final KList<IrisModRegionReplacer> regionReplacers) {
      this.regionReplacers = var1;
      return this;
   }

   @Generated
   public IrisMod setBlockReplacers(final KList<IrisObjectReplace> blockReplacers) {
      this.blockReplacers = var1;
      return this;
   }

   @Generated
   public IrisMod setStyleReplacers(final KList<IrisModNoiseStyleReplacer> styleReplacers) {
      this.styleReplacers = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = this.getName();
      return "IrisMod(name=" + var10000 + ", forDimension=" + this.getForDimension() + ", overrideFluidHeight=" + this.getOverrideFluidHeight() + ", removeBiomes=" + String.valueOf(this.getRemoveBiomes()) + ", removeObjects=" + String.valueOf(this.getRemoveObjects()) + ", removeRegions=" + String.valueOf(this.getRemoveRegions()) + ", injectRegions=" + String.valueOf(this.getInjectRegions()) + ", biomeInjectors=" + String.valueOf(this.getBiomeInjectors()) + ", biomeReplacers=" + String.valueOf(this.getBiomeReplacers()) + ", objectReplacers=" + String.valueOf(this.getObjectReplacers()) + ", biomeObjectPlacementInjectors=" + String.valueOf(this.getBiomeObjectPlacementInjectors()) + ", regionObjectPlacementInjectors=" + String.valueOf(this.getRegionObjectPlacementInjectors()) + ", regionReplacers=" + String.valueOf(this.getRegionReplacers()) + ", blockReplacers=" + String.valueOf(this.getBlockReplacers()) + ", styleReplacers=" + String.valueOf(this.getStyleReplacers()) + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisMod)) {
         return false;
      } else {
         IrisMod var2 = (IrisMod)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getOverrideFluidHeight() != var2.getOverrideFluidHeight()) {
            return false;
         } else {
            String var3 = this.getName();
            String var4 = var2.getName();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            String var5 = this.getForDimension();
            String var6 = var2.getForDimension();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label167: {
               KList var7 = this.getRemoveBiomes();
               KList var8 = var2.getRemoveBiomes();
               if (var7 == null) {
                  if (var8 == null) {
                     break label167;
                  }
               } else if (var7.equals(var8)) {
                  break label167;
               }

               return false;
            }

            label160: {
               KList var9 = this.getRemoveObjects();
               KList var10 = var2.getRemoveObjects();
               if (var9 == null) {
                  if (var10 == null) {
                     break label160;
                  }
               } else if (var9.equals(var10)) {
                  break label160;
               }

               return false;
            }

            KList var11 = this.getRemoveRegions();
            KList var12 = var2.getRemoveRegions();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            KList var13 = this.getInjectRegions();
            KList var14 = var2.getInjectRegions();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            label139: {
               KList var15 = this.getBiomeInjectors();
               KList var16 = var2.getBiomeInjectors();
               if (var15 == null) {
                  if (var16 == null) {
                     break label139;
                  }
               } else if (var15.equals(var16)) {
                  break label139;
               }

               return false;
            }

            KList var17 = this.getBiomeReplacers();
            KList var18 = var2.getBiomeReplacers();
            if (var17 == null) {
               if (var18 != null) {
                  return false;
               }
            } else if (!var17.equals(var18)) {
               return false;
            }

            KList var19 = this.getObjectReplacers();
            KList var20 = var2.getObjectReplacers();
            if (var19 == null) {
               if (var20 != null) {
                  return false;
               }
            } else if (!var19.equals(var20)) {
               return false;
            }

            label118: {
               KList var21 = this.getBiomeObjectPlacementInjectors();
               KList var22 = var2.getBiomeObjectPlacementInjectors();
               if (var21 == null) {
                  if (var22 == null) {
                     break label118;
                  }
               } else if (var21.equals(var22)) {
                  break label118;
               }

               return false;
            }

            label111: {
               KList var23 = this.getRegionObjectPlacementInjectors();
               KList var24 = var2.getRegionObjectPlacementInjectors();
               if (var23 == null) {
                  if (var24 == null) {
                     break label111;
                  }
               } else if (var23.equals(var24)) {
                  break label111;
               }

               return false;
            }

            label104: {
               KList var25 = this.getRegionReplacers();
               KList var26 = var2.getRegionReplacers();
               if (var25 == null) {
                  if (var26 == null) {
                     break label104;
                  }
               } else if (var25.equals(var26)) {
                  break label104;
               }

               return false;
            }

            KList var27 = this.getBlockReplacers();
            KList var28 = var2.getBlockReplacers();
            if (var27 == null) {
               if (var28 != null) {
                  return false;
               }
            } else if (!var27.equals(var28)) {
               return false;
            }

            KList var29 = this.getStyleReplacers();
            KList var30 = var2.getStyleReplacers();
            if (var29 == null) {
               if (var30 != null) {
                  return false;
               }
            } else if (!var29.equals(var30)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisMod;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var17 = var2 * 59 + this.getOverrideFluidHeight();
      String var3 = this.getName();
      var17 = var17 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getForDimension();
      var17 = var17 * 59 + (var4 == null ? 43 : var4.hashCode());
      KList var5 = this.getRemoveBiomes();
      var17 = var17 * 59 + (var5 == null ? 43 : var5.hashCode());
      KList var6 = this.getRemoveObjects();
      var17 = var17 * 59 + (var6 == null ? 43 : var6.hashCode());
      KList var7 = this.getRemoveRegions();
      var17 = var17 * 59 + (var7 == null ? 43 : var7.hashCode());
      KList var8 = this.getInjectRegions();
      var17 = var17 * 59 + (var8 == null ? 43 : var8.hashCode());
      KList var9 = this.getBiomeInjectors();
      var17 = var17 * 59 + (var9 == null ? 43 : var9.hashCode());
      KList var10 = this.getBiomeReplacers();
      var17 = var17 * 59 + (var10 == null ? 43 : var10.hashCode());
      KList var11 = this.getObjectReplacers();
      var17 = var17 * 59 + (var11 == null ? 43 : var11.hashCode());
      KList var12 = this.getBiomeObjectPlacementInjectors();
      var17 = var17 * 59 + (var12 == null ? 43 : var12.hashCode());
      KList var13 = this.getRegionObjectPlacementInjectors();
      var17 = var17 * 59 + (var13 == null ? 43 : var13.hashCode());
      KList var14 = this.getRegionReplacers();
      var17 = var17 * 59 + (var14 == null ? 43 : var14.hashCode());
      KList var15 = this.getBlockReplacers();
      var17 = var17 * 59 + (var15 == null ? 43 : var15.hashCode());
      KList var16 = this.getStyleReplacers();
      var17 = var17 * 59 + (var16 == null ? 43 : var16.hashCode());
      return var17;
   }
}
