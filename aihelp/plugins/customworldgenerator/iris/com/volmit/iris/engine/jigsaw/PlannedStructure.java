package com.volmit.iris.engine.jigsaw;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap.Builder;
import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.engine.framework.placer.WorldObjectPlacer;
import com.volmit.iris.engine.object.CarveResult;
import com.volmit.iris.engine.object.IObjectPlacer;
import com.volmit.iris.engine.object.IrisDirection;
import com.volmit.iris.engine.object.IrisGeneratorStyle;
import com.volmit.iris.engine.object.IrisJigsawPiece;
import com.volmit.iris.engine.object.IrisJigsawPieceConnector;
import com.volmit.iris.engine.object.IrisJigsawPool;
import com.volmit.iris.engine.object.IrisJigsawStructure;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisObjectPlacement;
import com.volmit.iris.engine.object.IrisObjectRotation;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.engine.object.NoiseStyle;
import com.volmit.iris.engine.object.ObjectPlaceMode;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.mantle.Mantle;
import com.volmit.iris.util.math.Position2;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.matter.slices.container.JigsawPieceContainer;
import com.volmit.iris.util.matter.slices.container.JigsawStructureContainer;
import com.volmit.iris.util.matter.slices.container.JigsawStructuresContainer;
import com.volmit.iris.util.scheduling.J;
import java.util.Iterator;
import java.util.function.Consumer;
import lombok.Generated;
import org.bukkit.Axis;

public class PlannedStructure {
   private static ConcurrentLinkedHashMap<String, IrisObject> objectRotationCache = (new Builder()).initialCapacity(64).maximumWeightedCapacity(1024L).concurrencyLevel(32).build();
   private KList<PlannedPiece> pieces = new KList();
   private IrisJigsawStructure structure;
   private IrisPosition position;
   private IrisData data;
   private RNG rng;
   private boolean forcePlace;
   private boolean verbose = true;
   private boolean terminating = false;

   public PlannedStructure(IrisJigsawStructure structure, IrisPosition position, RNG rng, boolean forcePlace) {
      this.structure = var1;
      this.position = var2;
      this.rng = var3;
      this.forcePlace = var4 || var1.isForcePlace();
      this.data = var1.getLoader();
      this.generateStartPiece();

      for(int var5 = 0; var5 < var1.getMaxDepth(); ++var5) {
         this.generateOutwards();
      }

      this.generateTerminators();
      Iris.debug("JPlace: ROOT @ relative " + var2.toString());
      Iterator var7 = this.pieces.iterator();

      while(var7.hasNext()) {
         PlannedPiece var6 = (PlannedPiece)var7.next();
         String var10000 = var6.getObject().getLoadKey();
         Iris.debug("Place: " + var10000 + " at @ relative " + var6.getPosition().toString());
      }

   }

   public boolean place(IObjectPlacer placer, Mantle e, Engine eng) {
      IrisObjectPlacement var4 = new IrisObjectPlacement();
      var4.setRotation(IrisObjectRotation.of(0.0D, 0.0D, 0.0D));
      int var5 = ((PlannedPiece)this.pieces.get(0)).getPosition().getY();
      boolean var6 = false;
      Iterator var7 = this.pieces.iterator();

      while(var7.hasNext()) {
         PlannedPiece var8 = (PlannedPiece)var7.next();
         if (this.place(var8, var5, var4, var1, var2, var3)) {
            var6 = true;
         }
      }

      if (var6) {
         Position2 var10 = new Position2(this.position.getX() >> 4, this.position.getZ() >> 4);
         Position2 var11 = new Position2(var10.getX() >> 5, var10.getZ() >> 5);
         JigsawStructuresContainer var9 = (JigsawStructuresContainer)var2.get(var11.getX(), 0, var11.getZ(), JigsawStructuresContainer.class);
         if (var9 == null) {
            var9 = new JigsawStructuresContainer();
         }

         var9.add(this.structure, var10);
         var2.set(var11.getX(), 0, var11.getZ(), (Object)var9);
      }

      return var6;
   }

   public boolean place(PlannedPiece i, int startHeight, IrisObjectPlacement o, IObjectPlacer placer, Mantle e, Engine eng) {
      IrisObjectPlacement var7 = var3;
      if (var1.getPiece().getPlacementOptions() != null) {
         var7 = var1.getPiece().getPlacementOptions();
         var7.getRotation().setEnabled(false);
         var7.setRotateTowardsSlope(false);
         var7.setWarp(new IrisGeneratorStyle(NoiseStyle.FLAT));
      } else {
         var3.setMode(var1.getPiece().getPlaceMode());
      }

      if (this.forcePlace) {
         var7.setForcePlace(true);
      }

      IrisObject var8 = var1.getObject();
      int var9 = var8.getW() / 2;
      int var10 = var8.getD() / 2;
      int var11 = var1.getPosition().getX() + var9;
      int var12 = var1.getPosition().getZ() + var10;
      int var13 = var1.getPosition().getY() - var2;
      int var14;
      if (var1.getStructure().getStructure().getLockY() == -1) {
         if (var1.getStructure().getStructure().getOverrideYRange() != null) {
            var14 = (int)var1.getStructure().getStructure().getOverrideYRange().get(this.rng, (double)var11, (double)var12, this.getData());
         } else {
            var14 = var4.getHighest(var11, var12, this.getData(), var7.isUnderwater());
         }
      } else {
         var14 = var1.getStructure().getStructure().getLockY();
      }

      PlannedPiece.ParentConnection var15 = var1.getParent();
      if (var15 != null && var15.connector().isLockY()) {
         IrisPosition var16 = var15.getTargetPosition();
         if (var16 != null) {
            var14 = var16.getY();
            var13 = 0;
         } else {
            Iris.warn("Failed to get target position for " + var8.getLoadKey());
         }
      }

      var14 += var13 + var8.getH() / 2;
      if (var7.getMode().equals(ObjectPlaceMode.PAINT)) {
         var14 = -1;
      }

      int var19 = this.rng.i(0, Integer.MAX_VALUE);
      JigsawPieceContainer var17 = JigsawPieceContainer.toContainer(var1.getPiece());
      JigsawStructureContainer var18 = JigsawStructureContainer.toContainer(this.getStructure());
      var1.setRealPositions(var11, var14, var12, var4);
      return var8.place(var11, var14, var12, var4, var7, this.rng, (var5x, var6x) -> {
         int var10001 = var5x.getX();
         int var10002 = var5x.getY();
         int var10003 = var5x.getZ();
         String var10004 = var8.getLoadKey();
         var4.setData(var10001, var10002, var10003, var10004 + "@" + var19);
         var4.setData(var5x.getX(), var5x.getY(), var5x.getZ(), var18);
         var4.setData(var5x.getX(), var5x.getY(), var5x.getZ(), var17);
      }, (CarveResult)null, this.getData().getEngine() != null ? this.getData() : var6.getData()) != -1;
   }

   public void place(WorldObjectPlacer placer, Consumer<Boolean> consumer) {
      J.s(() -> {
         var2.accept(this.place(var1, var1.getMantle().getMantle(), var1.getEngine()));
      });
   }

   private void generateOutwards() {
      Iterator var1 = this.getPiecesWithAvailableConnectors().shuffle(this.rng).iterator();

      while(var1.hasNext()) {
         PlannedPiece var2 = (PlannedPiece)var1.next();
         if (!this.generatePieceOutwards(var2)) {
            var2.setDead(true);
         }
      }

   }

   private boolean generatePieceOutwards(PlannedPiece piece) {
      boolean var2 = false;
      Iterator var3 = var1.getAvailableConnectors().shuffleCopy(this.rng).iterator();

      while(var3.hasNext()) {
         IrisJigsawPieceConnector var4 = (IrisJigsawPieceConnector)var3.next();
         if (this.generateConnectorOutwards(var1, var4)) {
            var2 = true;
            var1.debugPrintConnectorPositions();
         }
      }

      return var2;
   }

   private boolean generateConnectorOutwards(PlannedPiece piece, IrisJigsawPieceConnector pieceConnector) {
      Iterator var3 = this.getShuffledPiecesFor(var2).iterator();

      IrisJigsawPiece var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (IrisJigsawPiece)var3.next();
      } while(!this.generateRotatedPiece(var1, var2, var4));

      return true;
   }

   private boolean generateRotatedPiece(PlannedPiece piece, IrisJigsawPieceConnector pieceConnector, IrisJigsawPiece idea) {
      if (!var1.getPiece().getPlacementOptions().getRotation().isEnabled()) {
         return this.generateRotatedPiece(var1, var2, var3, 0, 0, 0);
      } else {
         KList var4 = (new KList()).qadd(0).qadd(1).qadd(2).qadd(3).shuffle(this.rng);
         KList var5 = (new KList()).qadd(0).qadd(1).qadd(2).qadd(3).shuffle(this.rng);
         Iterator var6 = var4.iterator();

         Integer var7;
         do {
            if (!var6.hasNext()) {
               return false;
            }

            var7 = (Integer)var6.next();
            if (var2.isRotateConnector()) {
               assert var2.getDirection().getAxis() != null;

               if (!var2.getDirection().getAxis().equals(Axis.Y)) {
                  Iterator var8 = var5.iterator();

                  while(var8.hasNext()) {
                     Integer var9 = (Integer)var8.next();
                     if (var2.getDirection().getAxis().equals(Axis.X) && this.generateRotatedPiece(var1, var2, var3, var9, var7, 0)) {
                        return true;
                     }

                     if (var2.getDirection().getAxis().equals(Axis.Z) && this.generateRotatedPiece(var1, var2, var3, 0, var7, var9)) {
                        return true;
                     }
                  }
               }
            }
         } while(!this.generateRotatedPiece(var1, var2, var3, 0, var7, 0));

         return true;
      }
   }

   private boolean generateRotatedPiece(PlannedPiece piece, IrisJigsawPieceConnector pieceConnector, IrisJigsawPiece idea, IrisObjectRotation rotation) {
      if (!var3.getPlacementOptions().getRotation().isEnabled()) {
         var4 = var1.getRotation();
      }

      PlannedPiece var5 = new PlannedPiece(this, var1.getPosition(), var3, var4);
      Iterator var6 = var5.getPiece().getConnectors().shuffleCopy(this.rng).iterator();

      IrisJigsawPieceConnector var7;
      do {
         if (!var6.hasNext()) {
            return false;
         }

         var7 = (IrisJigsawPieceConnector)var6.next();
      } while(!this.generatePositionedPiece(var1, var2, var5, var7));

      return true;
   }

   private boolean generateRotatedPiece(PlannedPiece piece, IrisJigsawPieceConnector pieceConnector, IrisJigsawPiece idea, int x, int y, int z) {
      return this.generateRotatedPiece(var1, var2, var3, IrisObjectRotation.of((double)var4 * 90.0D, (double)var5 * 90.0D, (double)var6 * 90.0D));
   }

   private boolean generatePositionedPiece(PlannedPiece piece, IrisJigsawPieceConnector pieceConnector, PlannedPiece test, IrisJigsawPieceConnector testConnector) {
      var3.setPosition(new IrisPosition(0, 0, 0));
      IrisPosition var5 = var1.getWorldPosition(var2);
      IrisDirection var6 = var2.getDirection().reverse();
      IrisPosition var7 = var5.sub(new IrisPosition(var6.toVector()));
      if (!var2.getTargetName().equals("*") && !var2.getTargetName().equals(var4.getName())) {
         return false;
      } else if (!var4.getDirection().equals(var6)) {
         return false;
      } else {
         IrisPosition var8 = var3.getWorldPosition(var4);
         var3.setPosition(var7.sub(var8));
         if (this.collidesWith(var3, var1)) {
            return false;
         } else {
            var1.connect(var2, var3, var4);
            this.pieces.add((Object)var3);
            return true;
         }
      }
   }

   private KList<IrisJigsawPiece> getShuffledPiecesFor(IrisJigsawPieceConnector c) {
      KList var2 = new KList();
      KList var3 = this.terminating && this.getStructure().getTerminatePool() != null ? new KList(new String[]{this.getStructure().getTerminatePool()}) : var1.getPools().shuffleCopy(this.rng);
      Iterator var4 = var3.iterator();

      label36:
      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         Iterator var6 = ((IrisJigsawPool)this.getData().getJigsawPoolLoader().load(var5)).getPieces().shuffleCopy(this.rng).iterator();

         while(true) {
            IrisJigsawPiece var8;
            do {
               do {
                  if (!var6.hasNext()) {
                     continue label36;
                  }

                  String var7 = (String)var6.next();
                  var8 = (IrisJigsawPiece)this.getData().getJigsawPieceLoader().load(var7);
               } while(var8 == null);
            } while(this.terminating && !var8.isTerminal());

            var2.addIfMissing(var8);
         }
      }

      return var2.shuffle(this.rng);
   }

   private void generateStartPiece() {
      this.pieces.add((Object)(new PlannedPiece(this, this.position, (IrisJigsawPiece)this.getData().getJigsawPieceLoader().load((String)this.rng.pick(this.getStructure().getPieces())), 0, this.getStructure().isDisableInitialRotation() ? 0 : this.rng.nextInt(4), 0)));
   }

   private void generateTerminators() {
      if (this.getStructure().isTerminate()) {
         this.terminating = true;
         this.generateOutwards();
      }

   }

   public KList<PlannedPiece> getPiecesWithAvailableConnectors() {
      KList var1 = this.pieces.copy().removeWhere(PlannedPiece::isFull);
      if (!this.terminating) {
         var1.removeIf(PlannedPiece::isDead);
      }

      return var1;
   }

   public int getVolume() {
      int var1 = 0;

      PlannedPiece var3;
      for(Iterator var2 = this.pieces.iterator(); var2.hasNext(); var1 += var3.getObject().getH() * var3.getObject().getW() * var3.getObject().getD()) {
         var3 = (PlannedPiece)var2.next();
      }

      return var1;
   }

   public int getMass() {
      int var1 = 0;

      PlannedPiece var3;
      for(Iterator var2 = this.pieces.iterator(); var2.hasNext(); var1 += var3.getObject().getBlocks().size()) {
         var3 = (PlannedPiece)var2.next();
      }

      return var1;
   }

   public boolean collidesWith(PlannedPiece piece, PlannedPiece ignore) {
      Iterator var3 = this.pieces.iterator();

      PlannedPiece var4;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         var4 = (PlannedPiece)var3.next();
      } while(var4.equals(var2) || !var4.collidesWith(var1));

      return true;
   }

   public boolean contains(IrisPosition p) {
      Iterator var2 = this.pieces.iterator();

      PlannedPiece var3;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         var3 = (PlannedPiece)var2.next();
      } while(!var3.contains(var1));

      return true;
   }

   public IrisObject rotated(IrisJigsawPiece piece, IrisObjectRotation rotation) {
      String var10000 = var1.getObject();
      String var3 = var10000 + "-" + var2.hashCode();
      return (IrisObject)objectRotationCache.computeIfAbsent(var3, (var3x) -> {
         return var2.rotateCopy((IrisObject)this.data.getObjectLoader().load(var1.getObject()));
      });
   }

   @Generated
   public KList<PlannedPiece> getPieces() {
      return this.pieces;
   }

   @Generated
   public IrisJigsawStructure getStructure() {
      return this.structure;
   }

   @Generated
   public IrisPosition getPosition() {
      return this.position;
   }

   @Generated
   public IrisData getData() {
      return this.data;
   }

   @Generated
   public RNG getRng() {
      return this.rng;
   }

   @Generated
   public boolean isForcePlace() {
      return this.forcePlace;
   }

   @Generated
   public boolean isVerbose() {
      return this.verbose;
   }

   @Generated
   public boolean isTerminating() {
      return this.terminating;
   }

   @Generated
   public void setPieces(final KList<PlannedPiece> pieces) {
      this.pieces = var1;
   }

   @Generated
   public void setStructure(final IrisJigsawStructure structure) {
      this.structure = var1;
   }

   @Generated
   public void setPosition(final IrisPosition position) {
      this.position = var1;
   }

   @Generated
   public void setData(final IrisData data) {
      this.data = var1;
   }

   @Generated
   public void setRng(final RNG rng) {
      this.rng = var1;
   }

   @Generated
   public void setForcePlace(final boolean forcePlace) {
      this.forcePlace = var1;
   }

   @Generated
   public void setVerbose(final boolean verbose) {
      this.verbose = var1;
   }

   @Generated
   public void setTerminating(final boolean terminating) {
      this.terminating = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof PlannedStructure)) {
         return false;
      } else {
         PlannedStructure var2 = (PlannedStructure)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isForcePlace() != var2.isForcePlace()) {
            return false;
         } else if (this.isVerbose() != var2.isVerbose()) {
            return false;
         } else if (this.isTerminating() != var2.isTerminating()) {
            return false;
         } else {
            KList var3 = this.getPieces();
            KList var4 = var2.getPieces();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            label71: {
               IrisJigsawStructure var5 = this.getStructure();
               IrisJigsawStructure var6 = var2.getStructure();
               if (var5 == null) {
                  if (var6 == null) {
                     break label71;
                  }
               } else if (var5.equals(var6)) {
                  break label71;
               }

               return false;
            }

            label64: {
               IrisPosition var7 = this.getPosition();
               IrisPosition var8 = var2.getPosition();
               if (var7 == null) {
                  if (var8 == null) {
                     break label64;
                  }
               } else if (var7.equals(var8)) {
                  break label64;
               }

               return false;
            }

            IrisData var9 = this.getData();
            IrisData var10 = var2.getData();
            if (var9 == null) {
               if (var10 != null) {
                  return false;
               }
            } else if (!var9.equals(var10)) {
               return false;
            }

            RNG var11 = this.getRng();
            RNG var12 = var2.getRng();
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
      return var1 instanceof PlannedStructure;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var8 = var2 * 59 + (this.isForcePlace() ? 79 : 97);
      var8 = var8 * 59 + (this.isVerbose() ? 79 : 97);
      var8 = var8 * 59 + (this.isTerminating() ? 79 : 97);
      KList var3 = this.getPieces();
      var8 = var8 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisJigsawStructure var4 = this.getStructure();
      var8 = var8 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisPosition var5 = this.getPosition();
      var8 = var8 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisData var6 = this.getData();
      var8 = var8 * 59 + (var6 == null ? 43 : var6.hashCode());
      RNG var7 = this.getRng();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getPieces());
      return "PlannedStructure(pieces=" + var10000 + ", structure=" + String.valueOf(this.getStructure()) + ", position=" + String.valueOf(this.getPosition()) + ", data=" + String.valueOf(this.getData()) + ", rng=" + String.valueOf(this.getRng()) + ", forcePlace=" + this.isForcePlace() + ", verbose=" + this.isVerbose() + ", terminating=" + this.isTerminating() + ")";
   }
}
