package com.volmit.iris.engine.object;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListFunction;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.functions.StructureKeyFunction;
import com.volmit.iris.engine.object.annotations.functions.StructureKeyOrTagFunction;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;
import java.util.Iterator;
import lombok.Generated;

@Desc("Represents a jigsaw structure")
public class IrisJigsawStructure extends IrisRegistrant {
   @RegistryListFunction(StructureKeyFunction.class)
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("The datapack structures. Randomply chooses a structure to place\nIgnores every other setting")
   private KList<String> datapackStructures = new KList();
   @RegistryListResource(IrisJigsawPiece.class)
   @Required
   @ArrayType(
      min = 1,
      type = String.class
   )
   @Desc("The starting pieces. Randomly chooses a starting piece, then connects pieces using the pools define in the starting piece.")
   private KList<String> pieces = new KList();
   @MaxNumber(32.0D)
   @MinNumber(1.0D)
   @Desc("The maximum pieces that can step out from the center piece")
   private int maxDepth = 9;
   @Desc("Jigsaw grows the parallax layer which slows iris down a bit. Since there are so many pieces, Iris takes the avg piece size and calculates the parallax radius from that. Unless your structures are using only the biggest pieces, your structure should fit in the chosen size fine. If you are seeing cut-off parts of your structures or broken terrain, turn this option on. This option will pick the biggest piece dimensions and multiply it by your (maxDepth+1) * 2 as the size to grow the parallax layer by. But typically keep this off.")
   private boolean useMaxPieceSizeForParallaxRadius = false;
   @Desc("If set to true, iris will look for any pieces with only one connector in valid pools for edge connectors and attach them to 'terminate' the paths/piece connectors. Essentially it caps off ends. For example in a village, Iris would add houses to the ends of roads where possible. For terminators to be selected, they can only have one connector or they wont be chosen.")
   private boolean terminate = true;
   @RegistryListResource(IrisJigsawPool.class)
   @Desc("The pool to use when terminating pieces")
   private String terminatePool = null;
   @Desc("Override the y range instead of placing on the height map")
   private IrisStyledRange overrideYRange = null;
   @Desc("Force Y to a specific value")
   private int lockY = -1;
   @Desc("Set to true to prevent rotating the initial structure piece")
   private boolean disableInitialRotation = false;
   @RegistryListFunction(StructureKeyOrTagFunction.class)
   @Desc("The minecraft key to use when creating treasure maps")
   private String structureKey = null;
   @Desc("Force Place the whole structure")
   private boolean forcePlace = false;
   private transient AtomicCache<Integer> maxDimension = new AtomicCache();

   private void loadPool(String p, KList<String> pools, KList<String> pieces) {
      if (!var1.isEmpty()) {
         IrisJigsawPool var4 = (IrisJigsawPool)this.getLoader().getJigsawPoolLoader().load(var1);
         if (var4 == null) {
            Iris.warn("Can't find jigsaw pool: " + var1);
         } else {
            Iterator var5 = var4.getPieces().iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               if (var3.addIfMissing(var6)) {
                  this.loadPiece(var6, var2, var3);
               }
            }

         }
      }
   }

   private void loadPiece(String p, KList<String> pools, KList<String> pieces) {
      IrisJigsawPiece var4 = (IrisJigsawPiece)this.getLoader().getJigsawPieceLoader().load(var1);
      if (var4 == null) {
         Iris.warn("Can't find jigsaw piece: " + var1);
      } else {
         Iterator var5 = var4.getConnectors().iterator();

         while(var5.hasNext()) {
            IrisJigsawPieceConnector var6 = (IrisJigsawPieceConnector)var5.next();
            Iterator var7 = var6.getPools().iterator();

            while(var7.hasNext()) {
               String var8 = (String)var7.next();
               if (var2.addIfMissing(var8)) {
                  this.loadPool(var8, var2, var3);
               }
            }
         }

      }
   }

   public int getMaxDimension() {
      return (Integer)this.maxDimension.aquire(() -> {
         if (this.datapackStructures.isNotEmpty()) {
            return 0;
         } else {
            KList var2;
            String var5;
            Iterator var8;
            if (this.useMaxPieceSizeForParallaxRadius) {
               int var6 = 0;
               var2 = new KList();
               KList var9 = new KList();
               var8 = this.getPieces().iterator();

               while(var8.hasNext()) {
                  var5 = (String)var8.next();
                  this.loadPiece(var5, var2, var9);
               }

               for(var8 = var9.iterator(); var8.hasNext(); var6 = Math.max(var6, ((IrisJigsawPiece)this.getLoader().getJigsawPieceLoader().load(var5)).getMax3dDimension())) {
                  var5 = (String)var8.next();
               }

               return var6 * ((this.getMaxDepth() + 1) * 2 + 1);
            } else {
               KList var1 = new KList();
               var2 = new KList();
               Iterator var3 = this.getPieces().iterator();

               while(var3.hasNext()) {
                  String var4 = (String)var3.next();
                  this.loadPiece(var4, var1, var2);
               }

               int var7;
               if (var2.isEmpty()) {
                  var7 = 0;

                  for(var8 = this.getPieces().iterator(); var8.hasNext(); var7 = Math.max(var7, ((IrisJigsawPiece)this.getLoader().getJigsawPieceLoader().load(var5)).getMax2dDimension())) {
                     var5 = (String)var8.next();
                  }

                  return var7;
               } else {
                  var7 = 0;

                  for(var8 = var2.iterator(); var8.hasNext(); var7 += ((IrisJigsawPiece)this.getLoader().getJigsawPieceLoader().load(var5)).getMax2dDimension()) {
                     var5 = (String)var8.next();
                  }

                  return var7 / (!var2.isEmpty() ? var2.size() : 1) * ((this.getMaxDepth() + 1) * 2 + 1);
               }
            }
         }
      });
   }

   public String getFolderName() {
      return "jigsaw-structures";
   }

   public String getTypeName() {
      return "Jigsaw Structure";
   }

   public void scanForErrors(JSONObject p, VolmitSender sender) {
   }

   @Generated
   public IrisJigsawStructure() {
   }

   @Generated
   public IrisJigsawStructure(final KList<String> datapackStructures, final KList<String> pieces, final int maxDepth, final boolean useMaxPieceSizeForParallaxRadius, final boolean terminate, final String terminatePool, final IrisStyledRange overrideYRange, final int lockY, final boolean disableInitialRotation, final String structureKey, final boolean forcePlace, final AtomicCache<Integer> maxDimension) {
      this.datapackStructures = var1;
      this.pieces = var2;
      this.maxDepth = var3;
      this.useMaxPieceSizeForParallaxRadius = var4;
      this.terminate = var5;
      this.terminatePool = var6;
      this.overrideYRange = var7;
      this.lockY = var8;
      this.disableInitialRotation = var9;
      this.structureKey = var10;
      this.forcePlace = var11;
      this.maxDimension = var12;
   }

   @Generated
   public KList<String> getDatapackStructures() {
      return this.datapackStructures;
   }

   @Generated
   public KList<String> getPieces() {
      return this.pieces;
   }

   @Generated
   public int getMaxDepth() {
      return this.maxDepth;
   }

   @Generated
   public boolean isUseMaxPieceSizeForParallaxRadius() {
      return this.useMaxPieceSizeForParallaxRadius;
   }

   @Generated
   public boolean isTerminate() {
      return this.terminate;
   }

   @Generated
   public String getTerminatePool() {
      return this.terminatePool;
   }

   @Generated
   public IrisStyledRange getOverrideYRange() {
      return this.overrideYRange;
   }

   @Generated
   public int getLockY() {
      return this.lockY;
   }

   @Generated
   public boolean isDisableInitialRotation() {
      return this.disableInitialRotation;
   }

   @Generated
   public String getStructureKey() {
      return this.structureKey;
   }

   @Generated
   public boolean isForcePlace() {
      return this.forcePlace;
   }

   @Generated
   public IrisJigsawStructure setDatapackStructures(final KList<String> datapackStructures) {
      this.datapackStructures = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructure setPieces(final KList<String> pieces) {
      this.pieces = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructure setMaxDepth(final int maxDepth) {
      this.maxDepth = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructure setUseMaxPieceSizeForParallaxRadius(final boolean useMaxPieceSizeForParallaxRadius) {
      this.useMaxPieceSizeForParallaxRadius = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructure setTerminate(final boolean terminate) {
      this.terminate = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructure setTerminatePool(final String terminatePool) {
      this.terminatePool = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructure setOverrideYRange(final IrisStyledRange overrideYRange) {
      this.overrideYRange = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructure setLockY(final int lockY) {
      this.lockY = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructure setDisableInitialRotation(final boolean disableInitialRotation) {
      this.disableInitialRotation = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructure setStructureKey(final String structureKey) {
      this.structureKey = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructure setForcePlace(final boolean forcePlace) {
      this.forcePlace = var1;
      return this;
   }

   @Generated
   public IrisJigsawStructure setMaxDimension(final AtomicCache<Integer> maxDimension) {
      this.maxDimension = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getDatapackStructures());
      return "IrisJigsawStructure(datapackStructures=" + var10000 + ", pieces=" + String.valueOf(this.getPieces()) + ", maxDepth=" + this.getMaxDepth() + ", useMaxPieceSizeForParallaxRadius=" + this.isUseMaxPieceSizeForParallaxRadius() + ", terminate=" + this.isTerminate() + ", terminatePool=" + this.getTerminatePool() + ", overrideYRange=" + String.valueOf(this.getOverrideYRange()) + ", lockY=" + this.getLockY() + ", disableInitialRotation=" + this.isDisableInitialRotation() + ", structureKey=" + this.getStructureKey() + ", forcePlace=" + this.isForcePlace() + ", maxDimension=" + this.getMaxDimension() + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisJigsawStructure)) {
         return false;
      } else {
         IrisJigsawStructure var2 = (IrisJigsawStructure)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMaxDepth() != var2.getMaxDepth()) {
            return false;
         } else if (this.isUseMaxPieceSizeForParallaxRadius() != var2.isUseMaxPieceSizeForParallaxRadius()) {
            return false;
         } else if (this.isTerminate() != var2.isTerminate()) {
            return false;
         } else if (this.getLockY() != var2.getLockY()) {
            return false;
         } else if (this.isDisableInitialRotation() != var2.isDisableInitialRotation()) {
            return false;
         } else if (this.isForcePlace() != var2.isForcePlace()) {
            return false;
         } else {
            label86: {
               KList var3 = this.getDatapackStructures();
               KList var4 = var2.getDatapackStructures();
               if (var3 == null) {
                  if (var4 == null) {
                     break label86;
                  }
               } else if (var3.equals(var4)) {
                  break label86;
               }

               return false;
            }

            label79: {
               KList var5 = this.getPieces();
               KList var6 = var2.getPieces();
               if (var5 == null) {
                  if (var6 == null) {
                     break label79;
                  }
               } else if (var5.equals(var6)) {
                  break label79;
               }

               return false;
            }

            String var7 = this.getTerminatePool();
            String var8 = var2.getTerminatePool();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            IrisStyledRange var9 = this.getOverrideYRange();
            IrisStyledRange var10 = var2.getOverrideYRange();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            String var11 = this.getStructureKey();
            String var12 = var2.getStructureKey();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisJigsawStructure;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var8 = var2 * 59 + this.getMaxDepth();
      var8 = var8 * 59 + (this.isUseMaxPieceSizeForParallaxRadius() ? 79 : 97);
      var8 = var8 * 59 + (this.isTerminate() ? 79 : 97);
      var8 = var8 * 59 + this.getLockY();
      var8 = var8 * 59 + (this.isDisableInitialRotation() ? 79 : 97);
      var8 = var8 * 59 + (this.isForcePlace() ? 79 : 97);
      KList var3 = this.getDatapackStructures();
      var8 = var8 * 59 + (var3 == null ? 43 : var3.hashCode());
      KList var4 = this.getPieces();
      var8 = var8 * 59 + (var4 == null ? 43 : var4.hashCode());
      String var5 = this.getTerminatePool();
      var8 = var8 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisStyledRange var6 = this.getOverrideYRange();
      var8 = var8 * 59 + (var6 == null ? 43 : var6.hashCode());
      String var7 = this.getStructureKey();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }
}
