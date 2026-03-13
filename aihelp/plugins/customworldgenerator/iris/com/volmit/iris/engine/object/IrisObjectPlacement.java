package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.data.B;
import com.volmit.iris.util.data.DataProvider;
import com.volmit.iris.util.data.WeightedRandom;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.TreeType;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;

@Snippet("object-placer")
@Desc("Represents an iris object placer. It places objects.")
public class IrisObjectPlacement {
   private final transient AtomicCache<CNG> surfaceWarp = new AtomicCache();
   @RegistryListResource(IrisObject.class)
   @Required
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("List of objects to place")
   private KList<String> place = new KList();
   @Desc("Rotate this objects placement")
   private IrisObjectRotation rotation = new IrisObjectRotation();
   @Desc("Limit the max height or min height of placement.")
   private IrisObjectLimit clamp = new IrisObjectLimit();
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("The maximum layer level of a snow filter overtop of this placement. Set to 0 to disable. Max of 1.")
   private double snow = 0.0D;
   @Desc("Whether or not this object can be targeted by a dolphin.")
   private boolean isDolphinTarget = false;
   @Desc("The slope at which this object can be placed. Range from 0 to 10 by default. Calculated from a 3-block radius from the center of the object placement.")
   private IrisSlopeClip slopeCondition = new IrisSlopeClip();
   @Desc("Set to true to add the rotation of the direction of the slope of the terrain (wherever the slope is going down) to the y-axis rotation of the object.Rounded to 90 degrees. Adds the *min* rotation of the y axis as well (to still allow you to rotate objects nicely). Discards *max* and *interval* on *yaxis*")
   private boolean rotateTowardsSlope = false;
   @MinNumber(0.0D)
   @MaxNumber(1.0D)
   @Desc("The chance for this to place in a chunk. If you need multiple per chunk, set this to 1 and use density.")
   private double chance = 1.0D;
   @MinNumber(1.0D)
   @Desc("If the chance check passes, place this many in a single chunk")
   private int density = 1;
   @Desc("If the chance check passes, and you specify this, it picks a number in the range based on noise, and 'density' is ignored.")
   private IrisStyledRange densityStyle = null;
   @Desc("When stilting is enabled, this object will define various properties related to it.")
   private IrisStiltSettings stiltSettings;
   @MaxNumber(64.0D)
   @MinNumber(0.0D)
   @Desc("When bore is enabled, expand max-y of the cuboid it removes")
   private int boreExtendMaxY = 0;
   @ArrayType(
      min = 1,
      type = IrisObjectMarker.class
   )
   @Desc("Add markers to blocks in this object")
   private KList<IrisObjectMarker> markers = new KList();
   @MaxNumber(64.0D)
   @MinNumber(-1.0D)
   @Desc("When bore is enabled, lower min-y of the cuboid it removes")
   private int boreExtendMinY = 0;
   @Desc("If set to true, objects will place on the terrain height, ignoring the water surface.")
   private boolean underwater = false;
   @Desc("If set to true, objects will place in carvings (such as underground) or under an overhang.")
   private CarvingMode carvingSupport;
   @Desc("If this is defined, this object wont place on the terrain heightmap, but instead on this virtual heightmap")
   private IrisNoiseGenerator heightmap;
   @Desc("If set to true, Iris will try to fill the insides of 'rooms' and 'pockets' where air should fit based off of raytrace checks. This prevents a village house placing in an area where a tree already exists, and instead replaces the parts of the tree where the interior of the structure is. \n\nThis operation does not affect warmed-up generation speed however it does slow down loading objects.")
   private boolean smartBore;
   @Desc("If set to true, Blocks placed underwater that could be waterlogged are waterlogged.")
   private boolean waterloggable;
   @Desc("If set to true, objects will place on the fluid height level Such as boats.")
   private boolean onwater;
   @Desc("If set to true, this object will only place parts of itself where blocks already exist. Warning: Melding is very performance intensive!")
   private boolean meld;
   @Desc("If set to true, this object will get placed from the bottom of the world up")
   private boolean fromBottom;
   @Desc("If set to true, this object will place from the ground up instead of height checks when not y locked to the surface. This is not compatable with X and Z axis rotations (it may look off)")
   private boolean bottom;
   @Desc("If set to true, air will be placed before the schematic places.")
   private boolean bore;
   @Desc("Use a generator to warp the field of coordinates. Using simplex for example would make a square placement warp like a flag")
   private IrisGeneratorStyle warp;
   @Desc("If the place mode is set to CENTER_HEIGHT_RIGID and you have an X/Z translation, Turning on translate center will also translate the center height check.")
   private boolean translateCenter;
   @Desc("The placement mode")
   private ObjectPlaceMode mode;
   @ArrayType(
      min = 1,
      type = IrisObjectReplace.class
   )
   @Desc("Find and replace blocks")
   private KList<IrisObjectReplace> edit;
   @Desc("Translate this object's placement")
   private IrisObjectTranslate translate;
   @Desc("Scale Objects")
   private IrisObjectScale scale;
   @ArrayType(
      min = 1,
      type = IrisObjectLoot.class
   )
   @Desc("The loot tables to apply to these objects")
   private KList<IrisObjectLoot> loot;
   @ArrayType(
      min = 1,
      type = IrisObjectVanillaLoot.class
   )
   @Desc("The vanilla loot tables to apply to these objects")
   private KList<IrisObjectVanillaLoot> vanillaLoot;
   @Desc("Whether the given loot tables override any and all other loot tables available in the dimension, region or biome.")
   private boolean overrideGlobalLoot;
   @Desc("This object / these objects override the following trees when they grow...")
   @ArrayType(
      min = 1,
      type = IrisTree.class
   )
   private KList<IrisTree> trees;
   @RegistryListResource(IrisObject.class)
   @ArrayType(
      type = String.class
   )
   @Desc("List of objects to this object is allowed to collied with")
   private KList<String> allowedCollisions;
   @RegistryListResource(IrisObject.class)
   @ArrayType(
      type = String.class
   )
   @Desc("List of objects to this object is forbidden to collied with")
   private KList<String> forbiddenCollisions;
   @Desc("Ignore any placement restrictions for this object")
   private boolean forcePlace;
   private transient AtomicCache<IrisObjectPlacement.TableCache> cache;

   public IrisObjectPlacement toPlacement(String... place) {
      IrisObjectPlacement var2 = new IrisObjectPlacement();
      var2.setPlace(new KList(var1));
      var2.setTranslateCenter(this.translateCenter);
      var2.setMode(this.mode);
      var2.setEdit(this.edit);
      var2.setTranslate(this.translate);
      var2.setWarp(this.warp);
      var2.setBore(this.bore);
      var2.setMeld(this.meld);
      var2.setWaterloggable(this.waterloggable);
      var2.setOnwater(this.onwater);
      var2.setSmartBore(this.smartBore);
      var2.setCarvingSupport(this.carvingSupport);
      var2.setUnderwater(this.underwater);
      var2.setBoreExtendMaxY(this.boreExtendMaxY);
      var2.setBoreExtendMinY(this.boreExtendMinY);
      var2.setStiltSettings(this.stiltSettings);
      var2.setDensity(this.density);
      var2.setChance(this.chance);
      var2.setSnow(this.snow);
      var2.setClamp(this.clamp);
      var2.setRotation(this.rotation);
      var2.setLoot(this.loot);
      return var2;
   }

   public CNG getSurfaceWarp(RNG rng, IrisData data) {
      return (CNG)this.surfaceWarp.aquire(() -> {
         return this.getWarp().create(var1, var2);
      });
   }

   public double warp(RNG rng, double x, double y, double z, IrisData data) {
      return this.getSurfaceWarp(var1, var8).fitDouble(-(this.getWarp().getMultiplier() / 2.0D), this.getWarp().getMultiplier() / 2.0D, var2, var4, var6);
   }

   public IrisObject getObject(DataProvider g, RNG random) {
      return this.place.isEmpty() ? null : (IrisObject)var1.getData().getObjectLoader().load((String)this.place.get(var2.nextInt(this.place.size())));
   }

   public boolean matches(IrisTreeSize size, TreeType type) {
      Iterator var3 = this.getTrees().iterator();

      IrisTree var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (IrisTree)var3.next();
      } while(!var4.matches(var1, var2));

      return true;
   }

   public int getDensity() {
      return this.densityStyle == null ? this.density : this.densityStyle.getMid();
   }

   public int getDensity(RNG rng, double x, double z, IrisData data) {
      return this.densityStyle == null ? this.density : (int)Math.round(this.densityStyle.get(var1, var2, var4, var6));
   }

   private IrisObjectPlacement.TableCache getCache(IrisData manager) {
      return (IrisObjectPlacement.TableCache)this.cache.aquire(() -> {
         IrisObjectPlacement.TableCache var2 = new IrisObjectPlacement.TableCache();
         var2.merge(this.getCache(var1, this.getVanillaLoot(), IrisObjectPlacement::getVanillaTable));
         KList var10003 = this.getLoot();
         ResourceLoader var10004 = var1.getLootLoader();
         Objects.requireNonNull(var10004);
         var2.merge(this.getCache(var1, var10003, var10004::load));
         return var2;
      });
   }

   private IrisObjectPlacement.TableCache getCache(IrisData manager, KList<? extends IObjectLoot> list, Function<String, IrisLootTable> loader) {
      IrisObjectPlacement.TableCache var4 = new IrisObjectPlacement.TableCache();
      Iterator var5 = var2.iterator();

      while(true) {
         while(true) {
            IObjectLoot var6;
            do {
               if (!var5.hasNext()) {
                  return var4;
               }

               var6 = (IObjectLoot)var5.next();
            } while(var6 == null);

            IrisLootTable var7 = (IrisLootTable)var3.apply(var6.getName());
            if (var7 == null) {
               Iris.warn("Couldn't find loot table " + var6.getName());
            } else if (var6.getFilter().isEmpty()) {
               var4.global.put(var7, var6.getWeight());
            } else {
               Iterator var8;
               BlockData var9;
               if (!var6.isExact()) {
                  for(var8 = var6.getFilter(var1).iterator(); var8.hasNext(); ((WeightedRandom)var4.basic.get(var9.getMaterial())).put(var7, var6.getWeight())) {
                     var9 = (BlockData)var8.next();
                     if (!var4.basic.containsKey(var9.getMaterial())) {
                        var4.basic.put(var9.getMaterial(), new WeightedRandom());
                     }
                  }
               } else {
                  for(var8 = var6.getFilter(var1).iterator(); var8.hasNext(); ((WeightedRandom)((KMap)var4.exact.get(var9.getMaterial())).get(var9)).put(var7, var6.getWeight())) {
                     var9 = (BlockData)var8.next();
                     if (!var4.exact.containsKey(var9.getMaterial())) {
                        var4.exact.put(var9.getMaterial(), new KMap());
                     }

                     if (!((KMap)var4.exact.get(var9.getMaterial())).containsKey(var9)) {
                        ((KMap)var4.exact.get(var9.getMaterial())).put(var9, new WeightedRandom());
                     }
                  }
               }
            }
         }
      }
   }

   @Nullable
   private static IrisVanillaLootTable getVanillaTable(String name) {
      return (IrisVanillaLootTable)Optional.ofNullable(NamespacedKey.fromString(var0)).map(Bukkit::getLootTable).map(IrisVanillaLootTable::new).orElse((Object)null);
   }

   public IrisLootTable getTable(BlockData data, IrisData dataManager) {
      IrisObjectPlacement.TableCache var3 = this.getCache(var2);
      if (!B.isStorageChest(var1)) {
         return null;
      } else {
         IrisLootTable var4 = null;
         if (var3.exact.containsKey(var1.getMaterial()) && ((KMap)var3.exact.get(var1.getMaterial())).containsKey(var1)) {
            var4 = (IrisLootTable)((WeightedRandom)((KMap)var3.exact.get(var1.getMaterial())).get(var1)).pullRandom();
         } else if (var3.basic.containsKey(var1.getMaterial())) {
            var4 = (IrisLootTable)((WeightedRandom)var3.basic.get(var1.getMaterial())).pullRandom();
         } else if (var3.global.getSize() > 0) {
            var4 = (IrisLootTable)var3.global.pullRandom();
         }

         return var4;
      }
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisObjectPlacement)) {
         return false;
      } else {
         IrisObjectPlacement var2 = (IrisObjectPlacement)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getSnow(), var2.getSnow()) != 0) {
            return false;
         } else if (this.isDolphinTarget() != var2.isDolphinTarget()) {
            return false;
         } else if (this.isRotateTowardsSlope() != var2.isRotateTowardsSlope()) {
            return false;
         } else if (Double.compare(this.getChance(), var2.getChance()) != 0) {
            return false;
         } else if (this.getDensity() != var2.getDensity()) {
            return false;
         } else if (this.getBoreExtendMaxY() != var2.getBoreExtendMaxY()) {
            return false;
         } else if (this.getBoreExtendMinY() != var2.getBoreExtendMinY()) {
            return false;
         } else if (this.isUnderwater() != var2.isUnderwater()) {
            return false;
         } else if (this.isSmartBore() != var2.isSmartBore()) {
            return false;
         } else if (this.isWaterloggable() != var2.isWaterloggable()) {
            return false;
         } else if (this.isOnwater() != var2.isOnwater()) {
            return false;
         } else if (this.isMeld() != var2.isMeld()) {
            return false;
         } else if (this.isFromBottom() != var2.isFromBottom()) {
            return false;
         } else if (this.isBottom() != var2.isBottom()) {
            return false;
         } else if (this.isBore() != var2.isBore()) {
            return false;
         } else if (this.isTranslateCenter() != var2.isTranslateCenter()) {
            return false;
         } else if (this.isOverrideGlobalLoot() != var2.isOverrideGlobalLoot()) {
            return false;
         } else if (this.isForcePlace() != var2.isForcePlace()) {
            return false;
         } else {
            label284: {
               KList var3 = this.getPlace();
               KList var4 = var2.getPlace();
               if (var3 == null) {
                  if (var4 == null) {
                     break label284;
                  }
               } else if (var3.equals(var4)) {
                  break label284;
               }

               return false;
            }

            IrisObjectRotation var5 = this.getRotation();
            IrisObjectRotation var6 = var2.getRotation();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label270: {
               IrisObjectLimit var7 = this.getClamp();
               IrisObjectLimit var8 = var2.getClamp();
               if (var7 == null) {
                  if (var8 == null) {
                     break label270;
                  }
               } else if (var7.equals(var8)) {
                  break label270;
               }

               return false;
            }

            label263: {
               IrisSlopeClip var9 = this.getSlopeCondition();
               IrisSlopeClip var10 = var2.getSlopeCondition();
               if (var9 == null) {
                  if (var10 == null) {
                     break label263;
                  }
               } else if (var9.equals(var10)) {
                  break label263;
               }

               return false;
            }

            IrisStyledRange var11 = this.getDensityStyle();
            IrisStyledRange var12 = var2.getDensityStyle();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            label249: {
               IrisStiltSettings var13 = this.getStiltSettings();
               IrisStiltSettings var14 = var2.getStiltSettings();
               if (var13 == null) {
                  if (var14 == null) {
                     break label249;
                  }
               } else if (var13.equals(var14)) {
                  break label249;
               }

               return false;
            }

            label242: {
               KList var15 = this.getMarkers();
               KList var16 = var2.getMarkers();
               if (var15 == null) {
                  if (var16 == null) {
                     break label242;
                  }
               } else if (var15.equals(var16)) {
                  break label242;
               }

               return false;
            }

            CarvingMode var17 = this.getCarvingSupport();
            CarvingMode var18 = var2.getCarvingSupport();
            if (var17 == null) {
               if (var18 != null) {
                  return false;
               }
            } else if (!var17.equals(var18)) {
               return false;
            }

            IrisNoiseGenerator var19 = this.getHeightmap();
            IrisNoiseGenerator var20 = var2.getHeightmap();
            if (var19 == null) {
               if (var20 != null) {
                  return false;
               }
            } else if (!var19.equals(var20)) {
               return false;
            }

            label221: {
               IrisGeneratorStyle var21 = this.getWarp();
               IrisGeneratorStyle var22 = var2.getWarp();
               if (var21 == null) {
                  if (var22 == null) {
                     break label221;
                  }
               } else if (var21.equals(var22)) {
                  break label221;
               }

               return false;
            }

            label214: {
               ObjectPlaceMode var23 = this.getMode();
               ObjectPlaceMode var24 = var2.getMode();
               if (var23 == null) {
                  if (var24 == null) {
                     break label214;
                  }
               } else if (var23.equals(var24)) {
                  break label214;
               }

               return false;
            }

            KList var25 = this.getEdit();
            KList var26 = var2.getEdit();
            if (var25 == null) {
               if (var26 != null) {
                  return false;
               }
            } else if (!var25.equals(var26)) {
               return false;
            }

            label200: {
               IrisObjectTranslate var27 = this.getTranslate();
               IrisObjectTranslate var28 = var2.getTranslate();
               if (var27 == null) {
                  if (var28 == null) {
                     break label200;
                  }
               } else if (var27.equals(var28)) {
                  break label200;
               }

               return false;
            }

            IrisObjectScale var29 = this.getScale();
            IrisObjectScale var30 = var2.getScale();
            if (var29 == null) {
               if (var30 != null) {
                  return false;
               }
            } else if (!var29.equals(var30)) {
               return false;
            }

            label186: {
               KList var31 = this.getLoot();
               KList var32 = var2.getLoot();
               if (var31 == null) {
                  if (var32 == null) {
                     break label186;
                  }
               } else if (var31.equals(var32)) {
                  break label186;
               }

               return false;
            }

            KList var33 = this.getVanillaLoot();
            KList var34 = var2.getVanillaLoot();
            if (var33 == null) {
               if (var34 != null) {
                  return false;
               }
            } else if (!var33.equals(var34)) {
               return false;
            }

            label172: {
               KList var35 = this.getTrees();
               KList var36 = var2.getTrees();
               if (var35 == null) {
                  if (var36 == null) {
                     break label172;
                  }
               } else if (var35.equals(var36)) {
                  break label172;
               }

               return false;
            }

            KList var37 = this.getAllowedCollisions();
            KList var38 = var2.getAllowedCollisions();
            if (var37 == null) {
               if (var38 != null) {
                  return false;
               }
            } else if (!var37.equals(var38)) {
               return false;
            }

            KList var39 = this.getForbiddenCollisions();
            KList var40 = var2.getForbiddenCollisions();
            if (var39 == null) {
               if (var40 != null) {
                  return false;
               }
            } else if (!var39.equals(var40)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisObjectPlacement;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getSnow());
      int var26 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      var26 = var26 * 59 + (this.isDolphinTarget() ? 79 : 97);
      var26 = var26 * 59 + (this.isRotateTowardsSlope() ? 79 : 97);
      long var5 = Double.doubleToLongBits(this.getChance());
      var26 = var26 * 59 + (int)(var5 >>> 32 ^ var5);
      var26 = var26 * 59 + this.getDensity();
      var26 = var26 * 59 + this.getBoreExtendMaxY();
      var26 = var26 * 59 + this.getBoreExtendMinY();
      var26 = var26 * 59 + (this.isUnderwater() ? 79 : 97);
      var26 = var26 * 59 + (this.isSmartBore() ? 79 : 97);
      var26 = var26 * 59 + (this.isWaterloggable() ? 79 : 97);
      var26 = var26 * 59 + (this.isOnwater() ? 79 : 97);
      var26 = var26 * 59 + (this.isMeld() ? 79 : 97);
      var26 = var26 * 59 + (this.isFromBottom() ? 79 : 97);
      var26 = var26 * 59 + (this.isBottom() ? 79 : 97);
      var26 = var26 * 59 + (this.isBore() ? 79 : 97);
      var26 = var26 * 59 + (this.isTranslateCenter() ? 79 : 97);
      var26 = var26 * 59 + (this.isOverrideGlobalLoot() ? 79 : 97);
      var26 = var26 * 59 + (this.isForcePlace() ? 79 : 97);
      KList var7 = this.getPlace();
      var26 = var26 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisObjectRotation var8 = this.getRotation();
      var26 = var26 * 59 + (var8 == null ? 43 : var8.hashCode());
      IrisObjectLimit var9 = this.getClamp();
      var26 = var26 * 59 + (var9 == null ? 43 : var9.hashCode());
      IrisSlopeClip var10 = this.getSlopeCondition();
      var26 = var26 * 59 + (var10 == null ? 43 : var10.hashCode());
      IrisStyledRange var11 = this.getDensityStyle();
      var26 = var26 * 59 + (var11 == null ? 43 : var11.hashCode());
      IrisStiltSettings var12 = this.getStiltSettings();
      var26 = var26 * 59 + (var12 == null ? 43 : var12.hashCode());
      KList var13 = this.getMarkers();
      var26 = var26 * 59 + (var13 == null ? 43 : var13.hashCode());
      CarvingMode var14 = this.getCarvingSupport();
      var26 = var26 * 59 + (var14 == null ? 43 : var14.hashCode());
      IrisNoiseGenerator var15 = this.getHeightmap();
      var26 = var26 * 59 + (var15 == null ? 43 : var15.hashCode());
      IrisGeneratorStyle var16 = this.getWarp();
      var26 = var26 * 59 + (var16 == null ? 43 : var16.hashCode());
      ObjectPlaceMode var17 = this.getMode();
      var26 = var26 * 59 + (var17 == null ? 43 : var17.hashCode());
      KList var18 = this.getEdit();
      var26 = var26 * 59 + (var18 == null ? 43 : var18.hashCode());
      IrisObjectTranslate var19 = this.getTranslate();
      var26 = var26 * 59 + (var19 == null ? 43 : var19.hashCode());
      IrisObjectScale var20 = this.getScale();
      var26 = var26 * 59 + (var20 == null ? 43 : var20.hashCode());
      KList var21 = this.getLoot();
      var26 = var26 * 59 + (var21 == null ? 43 : var21.hashCode());
      KList var22 = this.getVanillaLoot();
      var26 = var26 * 59 + (var22 == null ? 43 : var22.hashCode());
      KList var23 = this.getTrees();
      var26 = var26 * 59 + (var23 == null ? 43 : var23.hashCode());
      KList var24 = this.getAllowedCollisions();
      var26 = var26 * 59 + (var24 == null ? 43 : var24.hashCode());
      KList var25 = this.getForbiddenCollisions();
      var26 = var26 * 59 + (var25 == null ? 43 : var25.hashCode());
      return var26;
   }

   @Generated
   public IrisObjectPlacement() {
      this.carvingSupport = CarvingMode.SURFACE_ONLY;
      this.smartBore = false;
      this.waterloggable = false;
      this.onwater = false;
      this.meld = false;
      this.bottom = false;
      this.bore = false;
      this.warp = new IrisGeneratorStyle(NoiseStyle.FLAT);
      this.translateCenter = false;
      this.mode = ObjectPlaceMode.CENTER_HEIGHT;
      this.edit = new KList();
      this.translate = new IrisObjectTranslate();
      this.scale = new IrisObjectScale();
      this.loot = new KList();
      this.vanillaLoot = new KList();
      this.overrideGlobalLoot = false;
      this.trees = new KList();
      this.allowedCollisions = new KList();
      this.forbiddenCollisions = new KList();
      this.forcePlace = false;
      this.cache = new AtomicCache();
   }

   @Generated
   public IrisObjectPlacement(final KList<String> place, final IrisObjectRotation rotation, final IrisObjectLimit clamp, final double snow, final boolean isDolphinTarget, final IrisSlopeClip slopeCondition, final boolean rotateTowardsSlope, final double chance, final int density, final IrisStyledRange densityStyle, final IrisStiltSettings stiltSettings, final int boreExtendMaxY, final KList<IrisObjectMarker> markers, final int boreExtendMinY, final boolean underwater, final CarvingMode carvingSupport, final IrisNoiseGenerator heightmap, final boolean smartBore, final boolean waterloggable, final boolean onwater, final boolean meld, final boolean fromBottom, final boolean bottom, final boolean bore, final IrisGeneratorStyle warp, final boolean translateCenter, final ObjectPlaceMode mode, final KList<IrisObjectReplace> edit, final IrisObjectTranslate translate, final IrisObjectScale scale, final KList<IrisObjectLoot> loot, final KList<IrisObjectVanillaLoot> vanillaLoot, final boolean overrideGlobalLoot, final KList<IrisTree> trees, final KList<String> allowedCollisions, final KList<String> forbiddenCollisions, final boolean forcePlace, final AtomicCache<IrisObjectPlacement.TableCache> cache) {
      this.carvingSupport = CarvingMode.SURFACE_ONLY;
      this.smartBore = false;
      this.waterloggable = false;
      this.onwater = false;
      this.meld = false;
      this.bottom = false;
      this.bore = false;
      this.warp = new IrisGeneratorStyle(NoiseStyle.FLAT);
      this.translateCenter = false;
      this.mode = ObjectPlaceMode.CENTER_HEIGHT;
      this.edit = new KList();
      this.translate = new IrisObjectTranslate();
      this.scale = new IrisObjectScale();
      this.loot = new KList();
      this.vanillaLoot = new KList();
      this.overrideGlobalLoot = false;
      this.trees = new KList();
      this.allowedCollisions = new KList();
      this.forbiddenCollisions = new KList();
      this.forcePlace = false;
      this.cache = new AtomicCache();
      this.place = var1;
      this.rotation = var2;
      this.clamp = var3;
      this.snow = var4;
      this.isDolphinTarget = var6;
      this.slopeCondition = var7;
      this.rotateTowardsSlope = var8;
      this.chance = var9;
      this.density = var11;
      this.densityStyle = var12;
      this.stiltSettings = var13;
      this.boreExtendMaxY = var14;
      this.markers = var15;
      this.boreExtendMinY = var16;
      this.underwater = var17;
      this.carvingSupport = var18;
      this.heightmap = var19;
      this.smartBore = var20;
      this.waterloggable = var21;
      this.onwater = var22;
      this.meld = var23;
      this.fromBottom = var24;
      this.bottom = var25;
      this.bore = var26;
      this.warp = var27;
      this.translateCenter = var28;
      this.mode = var29;
      this.edit = var30;
      this.translate = var31;
      this.scale = var32;
      this.loot = var33;
      this.vanillaLoot = var34;
      this.overrideGlobalLoot = var35;
      this.trees = var36;
      this.allowedCollisions = var37;
      this.forbiddenCollisions = var38;
      this.forcePlace = var39;
      this.cache = var40;
   }

   @Generated
   public AtomicCache<CNG> getSurfaceWarp() {
      return this.surfaceWarp;
   }

   @Generated
   public KList<String> getPlace() {
      return this.place;
   }

   @Generated
   public IrisObjectRotation getRotation() {
      return this.rotation;
   }

   @Generated
   public IrisObjectLimit getClamp() {
      return this.clamp;
   }

   @Generated
   public double getSnow() {
      return this.snow;
   }

   @Generated
   public boolean isDolphinTarget() {
      return this.isDolphinTarget;
   }

   @Generated
   public IrisSlopeClip getSlopeCondition() {
      return this.slopeCondition;
   }

   @Generated
   public boolean isRotateTowardsSlope() {
      return this.rotateTowardsSlope;
   }

   @Generated
   public double getChance() {
      return this.chance;
   }

   @Generated
   public IrisStyledRange getDensityStyle() {
      return this.densityStyle;
   }

   @Generated
   public IrisStiltSettings getStiltSettings() {
      return this.stiltSettings;
   }

   @Generated
   public int getBoreExtendMaxY() {
      return this.boreExtendMaxY;
   }

   @Generated
   public KList<IrisObjectMarker> getMarkers() {
      return this.markers;
   }

   @Generated
   public int getBoreExtendMinY() {
      return this.boreExtendMinY;
   }

   @Generated
   public boolean isUnderwater() {
      return this.underwater;
   }

   @Generated
   public CarvingMode getCarvingSupport() {
      return this.carvingSupport;
   }

   @Generated
   public IrisNoiseGenerator getHeightmap() {
      return this.heightmap;
   }

   @Generated
   public boolean isSmartBore() {
      return this.smartBore;
   }

   @Generated
   public boolean isWaterloggable() {
      return this.waterloggable;
   }

   @Generated
   public boolean isOnwater() {
      return this.onwater;
   }

   @Generated
   public boolean isMeld() {
      return this.meld;
   }

   @Generated
   public boolean isFromBottom() {
      return this.fromBottom;
   }

   @Generated
   public boolean isBottom() {
      return this.bottom;
   }

   @Generated
   public boolean isBore() {
      return this.bore;
   }

   @Generated
   public IrisGeneratorStyle getWarp() {
      return this.warp;
   }

   @Generated
   public boolean isTranslateCenter() {
      return this.translateCenter;
   }

   @Generated
   public ObjectPlaceMode getMode() {
      return this.mode;
   }

   @Generated
   public KList<IrisObjectReplace> getEdit() {
      return this.edit;
   }

   @Generated
   public IrisObjectTranslate getTranslate() {
      return this.translate;
   }

   @Generated
   public IrisObjectScale getScale() {
      return this.scale;
   }

   @Generated
   public KList<IrisObjectLoot> getLoot() {
      return this.loot;
   }

   @Generated
   public KList<IrisObjectVanillaLoot> getVanillaLoot() {
      return this.vanillaLoot;
   }

   @Generated
   public boolean isOverrideGlobalLoot() {
      return this.overrideGlobalLoot;
   }

   @Generated
   public KList<IrisTree> getTrees() {
      return this.trees;
   }

   @Generated
   public KList<String> getAllowedCollisions() {
      return this.allowedCollisions;
   }

   @Generated
   public KList<String> getForbiddenCollisions() {
      return this.forbiddenCollisions;
   }

   @Generated
   public boolean isForcePlace() {
      return this.forcePlace;
   }

   @Generated
   public AtomicCache<IrisObjectPlacement.TableCache> getCache() {
      return this.cache;
   }

   @Generated
   public IrisObjectPlacement setPlace(final KList<String> place) {
      this.place = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setRotation(final IrisObjectRotation rotation) {
      this.rotation = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setClamp(final IrisObjectLimit clamp) {
      this.clamp = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setSnow(final double snow) {
      this.snow = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setDolphinTarget(final boolean isDolphinTarget) {
      this.isDolphinTarget = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setSlopeCondition(final IrisSlopeClip slopeCondition) {
      this.slopeCondition = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setRotateTowardsSlope(final boolean rotateTowardsSlope) {
      this.rotateTowardsSlope = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setChance(final double chance) {
      this.chance = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setDensity(final int density) {
      this.density = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setDensityStyle(final IrisStyledRange densityStyle) {
      this.densityStyle = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setStiltSettings(final IrisStiltSettings stiltSettings) {
      this.stiltSettings = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setBoreExtendMaxY(final int boreExtendMaxY) {
      this.boreExtendMaxY = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setMarkers(final KList<IrisObjectMarker> markers) {
      this.markers = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setBoreExtendMinY(final int boreExtendMinY) {
      this.boreExtendMinY = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setUnderwater(final boolean underwater) {
      this.underwater = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setCarvingSupport(final CarvingMode carvingSupport) {
      this.carvingSupport = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setHeightmap(final IrisNoiseGenerator heightmap) {
      this.heightmap = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setSmartBore(final boolean smartBore) {
      this.smartBore = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setWaterloggable(final boolean waterloggable) {
      this.waterloggable = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setOnwater(final boolean onwater) {
      this.onwater = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setMeld(final boolean meld) {
      this.meld = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setFromBottom(final boolean fromBottom) {
      this.fromBottom = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setBottom(final boolean bottom) {
      this.bottom = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setBore(final boolean bore) {
      this.bore = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setWarp(final IrisGeneratorStyle warp) {
      this.warp = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setTranslateCenter(final boolean translateCenter) {
      this.translateCenter = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setMode(final ObjectPlaceMode mode) {
      this.mode = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setEdit(final KList<IrisObjectReplace> edit) {
      this.edit = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setTranslate(final IrisObjectTranslate translate) {
      this.translate = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setScale(final IrisObjectScale scale) {
      this.scale = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setLoot(final KList<IrisObjectLoot> loot) {
      this.loot = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setVanillaLoot(final KList<IrisObjectVanillaLoot> vanillaLoot) {
      this.vanillaLoot = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setOverrideGlobalLoot(final boolean overrideGlobalLoot) {
      this.overrideGlobalLoot = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setTrees(final KList<IrisTree> trees) {
      this.trees = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setAllowedCollisions(final KList<String> allowedCollisions) {
      this.allowedCollisions = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setForbiddenCollisions(final KList<String> forbiddenCollisions) {
      this.forbiddenCollisions = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setForcePlace(final boolean forcePlace) {
      this.forcePlace = var1;
      return this;
   }

   @Generated
   public IrisObjectPlacement setCache(final AtomicCache<IrisObjectPlacement.TableCache> cache) {
      this.cache = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getSurfaceWarp());
      return "IrisObjectPlacement(surfaceWarp=" + var10000 + ", place=" + String.valueOf(this.getPlace()) + ", rotation=" + String.valueOf(this.getRotation()) + ", clamp=" + String.valueOf(this.getClamp()) + ", snow=" + this.getSnow() + ", isDolphinTarget=" + this.isDolphinTarget() + ", slopeCondition=" + String.valueOf(this.getSlopeCondition()) + ", rotateTowardsSlope=" + this.isRotateTowardsSlope() + ", chance=" + this.getChance() + ", density=" + this.getDensity() + ", densityStyle=" + String.valueOf(this.getDensityStyle()) + ", stiltSettings=" + String.valueOf(this.getStiltSettings()) + ", boreExtendMaxY=" + this.getBoreExtendMaxY() + ", markers=" + String.valueOf(this.getMarkers()) + ", boreExtendMinY=" + this.getBoreExtendMinY() + ", underwater=" + this.isUnderwater() + ", carvingSupport=" + String.valueOf(this.getCarvingSupport()) + ", heightmap=" + String.valueOf(this.getHeightmap()) + ", smartBore=" + this.isSmartBore() + ", waterloggable=" + this.isWaterloggable() + ", onwater=" + this.isOnwater() + ", meld=" + this.isMeld() + ", fromBottom=" + this.isFromBottom() + ", bottom=" + this.isBottom() + ", bore=" + this.isBore() + ", warp=" + String.valueOf(this.getWarp()) + ", translateCenter=" + this.isTranslateCenter() + ", mode=" + String.valueOf(this.getMode()) + ", edit=" + String.valueOf(this.getEdit()) + ", translate=" + String.valueOf(this.getTranslate()) + ", scale=" + String.valueOf(this.getScale()) + ", loot=" + String.valueOf(this.getLoot()) + ", vanillaLoot=" + String.valueOf(this.getVanillaLoot()) + ", overrideGlobalLoot=" + this.isOverrideGlobalLoot() + ", trees=" + String.valueOf(this.getTrees()) + ", allowedCollisions=" + String.valueOf(this.getAllowedCollisions()) + ", forbiddenCollisions=" + String.valueOf(this.getForbiddenCollisions()) + ", forcePlace=" + this.isForcePlace() + ", cache=" + String.valueOf(this.getCache()) + ")";
   }

   private static class TableCache {
      final transient WeightedRandom<IrisLootTable> global = new WeightedRandom();
      final transient KMap<Material, WeightedRandom<IrisLootTable>> basic = new KMap();
      final transient KMap<Material, KMap<BlockData, WeightedRandom<IrisLootTable>>> exact = new KMap();

      private void merge(IrisObjectPlacement.TableCache other) {
         this.global.merge(var1.global);
         this.basic.merge(var1.basic, WeightedRandom::merge);
         this.exact.merge(var1.exact, (var0, var1x) -> {
            return var0.merge(var1x, WeightedRandom::merge);
         });
      }
   }
}
