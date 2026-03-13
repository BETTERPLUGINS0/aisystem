package com.volmit.iris.engine.jigsaw;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.object.IObjectPlacer;
import com.volmit.iris.engine.object.IrisJigsawPiece;
import com.volmit.iris.engine.object.IrisJigsawPieceConnector;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisObjectPlacement;
import com.volmit.iris.engine.object.IrisObjectRotation;
import com.volmit.iris.engine.object.IrisObjectTranslate;
import com.volmit.iris.engine.object.IrisPosition;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.math.AxisAlignedBB;
import com.volmit.iris.util.math.Vector3i;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;

public class PlannedPiece {
   private IrisPosition position;
   private IrisObject object;
   private IrisObject ogObject;
   private IrisJigsawPiece piece;
   private IrisObjectRotation rotation;
   private IrisData data;
   private KList<IrisJigsawPieceConnector> connected;
   private boolean dead;
   private AxisAlignedBB box;
   private PlannedStructure structure;
   private PlannedPiece.ParentConnection parent;
   private KMap<IrisJigsawPieceConnector, IrisPosition> realPositions;

   public PlannedPiece(PlannedStructure structure, IrisPosition position, IrisJigsawPiece piece) {
      this(var1, var2, var3, 0, 0, 0);
   }

   public PlannedPiece(PlannedStructure structure, IrisPosition position, IrisJigsawPiece piece, int rx, int ry, int rz) {
      this(var1, var2, var3, IrisObjectRotation.of((double)var4 * 90.0D, (double)var5 * 90.0D, (double)var6 * 90.0D));
   }

   public PlannedPiece(PlannedStructure structure, IrisPosition position, IrisJigsawPiece piece, IrisObjectRotation rot) {
      this.dead = false;
      this.parent = null;
      this.structure = var1;
      this.position = var2;
      this.data = var3.getLoader();
      this.setRotation(var4);
      this.ogObject = (IrisObject)this.data.getObjectLoader().load(var3.getObject());
      this.object = var1.rotated(var3, this.rotation);
      this.piece = this.rotation.rotateCopy(var3, new IrisPosition(this.object.getShrinkOffset()));
      this.piece.setLoadKey(var3.getLoadKey());
      this.object.setLoadKey(var3.getObject());
      this.ogObject.setLoadKey(var3.getObject());
      this.connected = new KList();
      this.realPositions = new KMap();
   }

   public void setPosition(IrisPosition p) {
      this.position = var1;
      this.box = null;
   }

   public String toString() {
      String var10000 = this.piece.getLoadKey();
      return var10000 + "@(" + this.position.getX() + "," + this.position.getY() + "," + this.position.getZ() + ")[rot:" + this.rotation.toString() + "]";
   }

   public AxisAlignedBB getBox() {
      if (this.box != null) {
         return this.box;
      } else {
         Vector3i var1 = this.getObject().getCenter();
         IrisPosition var2 = new IrisPosition();
         IrisObjectPlacement var3 = this.piece.getPlacementOptions();
         if (var3 != null && var3.getTranslate() != null) {
            IrisObjectTranslate var4 = var3.getTranslate();
            var2.setX(var4.getX());
            var2.setY(var4.getY());
            var2.setZ(var4.getZ());
         }

         this.box = this.object.getAABB().shifted(this.position.add(new IrisPosition(this.object.getCenter())).add(var2));
         return this.box;
      }
   }

   public boolean contains(IrisPosition p) {
      return this.getBox().contains(var1);
   }

   public boolean collidesWith(PlannedPiece p) {
      return this.getBox().intersects(var1.getBox());
   }

   public KList<IrisJigsawPieceConnector> getAvailableConnectors() {
      if (this.connected.isEmpty()) {
         return this.piece.getConnectors().copy();
      } else if (this.connected.size() == this.piece.getConnectors().size()) {
         return new KList();
      } else {
         KList var1 = new KList();
         Iterator var2 = this.piece.getConnectors().iterator();

         while(var2.hasNext()) {
            IrisJigsawPieceConnector var3 = (IrisJigsawPieceConnector)var2.next();
            if (!this.connected.contains(var3)) {
               var1.add((Object)var3);
            }
         }

         return var1;
      }
   }

   public KList<IrisJigsawPieceConnector> getChildConnectors() {
      PlannedPiece.ParentConnection var1 = this.getParent();
      KList var2 = this.getConnected().copy();
      if (var1 != null) {
         var2.removeIf((var1x) -> {
            return var1x.equals(var1.connector);
         });
      }

      return var2;
   }

   public boolean connect(IrisJigsawPieceConnector c, PlannedPiece p, IrisJigsawPieceConnector pc) {
      if (this.piece.getConnectors().contains(var1) && var2.getPiece().getConnectors().contains(var3)) {
         if (!this.connected.contains(var1) && !var2.connected.contains(var3)) {
            this.connected.add((Object)var1);
            var2.connected.add((Object)var3);
            var2.parent = new PlannedPiece.ParentConnection(this, var1, var2, var3);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public IrisPosition getWorldPosition(IrisJigsawPieceConnector c) {
      return this.getWorldPosition(var1.getPosition());
   }

   public List<IrisPosition> getConnectorWorldPositions() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.piece.getConnectors().iterator();

      while(var2.hasNext()) {
         IrisJigsawPieceConnector var3 = (IrisJigsawPieceConnector)var2.next();
         IrisPosition var4 = this.getWorldPosition(var3.getPosition());
         var1.add(var4);
      }

      return var1;
   }

   public IrisPosition getWorldPosition(IrisPosition position) {
      return this.position.add(var1).add(new IrisPosition(this.object.getCenter()));
   }

   public void debugPrintConnectorPositions() {
      Iris.debug("Connector World Positions for PlannedPiece at " + String.valueOf(this.position) + ":");
      List var1 = this.getConnectorWorldPositions();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         IrisPosition var3 = (IrisPosition)var2.next();
         Iris.debug(" - Connector at: " + String.valueOf(var3));
      }

   }

   public boolean isFull() {
      return this.connected.size() >= this.piece.getConnectors().size();
   }

   public void setRealPositions(int x, int y, int z, IObjectPlacer placer) {
      boolean var5 = this.piece.getPlacementOptions().isUnderwater();

      IrisJigsawPieceConnector var7;
      IrisPosition var8;
      for(Iterator var6 = this.piece.getConnectors().iterator(); var6.hasNext(); this.realPositions.put(var7, var8)) {
         var7 = (IrisJigsawPieceConnector)var6.next();
         var8 = var7.getPosition().add(new IrisPosition(var1, 0, var3));
         if (var2 < 0) {
            var8.setY(var8.getY() + var4.getHighest(var8.getX(), var8.getZ(), this.getData(), var5) + this.object.getH() / 2);
         } else {
            var8.setY(var8.getY() + var2);
         }
      }

   }

   @Generated
   public IrisPosition getPosition() {
      return this.position;
   }

   @Generated
   public IrisObject getObject() {
      return this.object;
   }

   @Generated
   public IrisObject getOgObject() {
      return this.ogObject;
   }

   @Generated
   public IrisJigsawPiece getPiece() {
      return this.piece;
   }

   @Generated
   public IrisObjectRotation getRotation() {
      return this.rotation;
   }

   @Generated
   public IrisData getData() {
      return this.data;
   }

   @Generated
   public KList<IrisJigsawPieceConnector> getConnected() {
      return this.connected;
   }

   @Generated
   public boolean isDead() {
      return this.dead;
   }

   @Generated
   public PlannedStructure getStructure() {
      return this.structure;
   }

   @Generated
   public PlannedPiece.ParentConnection getParent() {
      return this.parent;
   }

   @Generated
   public KMap<IrisJigsawPieceConnector, IrisPosition> getRealPositions() {
      return this.realPositions;
   }

   @Generated
   public void setObject(final IrisObject object) {
      this.object = var1;
   }

   @Generated
   public void setOgObject(final IrisObject ogObject) {
      this.ogObject = var1;
   }

   @Generated
   public void setPiece(final IrisJigsawPiece piece) {
      this.piece = var1;
   }

   @Generated
   public void setRotation(final IrisObjectRotation rotation) {
      this.rotation = var1;
   }

   @Generated
   public void setData(final IrisData data) {
      this.data = var1;
   }

   @Generated
   public void setConnected(final KList<IrisJigsawPieceConnector> connected) {
      this.connected = var1;
   }

   @Generated
   public void setDead(final boolean dead) {
      this.dead = var1;
   }

   @Generated
   public void setBox(final AxisAlignedBB box) {
      this.box = var1;
   }

   @Generated
   public void setStructure(final PlannedStructure structure) {
      this.structure = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof PlannedPiece)) {
         return false;
      } else {
         PlannedPiece var2 = (PlannedPiece)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isDead() != var2.isDead()) {
            return false;
         } else {
            label97: {
               IrisPosition var3 = this.getPosition();
               IrisPosition var4 = var2.getPosition();
               if (var3 == null) {
                  if (var4 == null) {
                     break label97;
                  }
               } else if (var3.equals(var4)) {
                  break label97;
               }

               return false;
            }

            IrisObject var5 = this.getObject();
            IrisObject var6 = var2.getObject();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisObject var7 = this.getOgObject();
            IrisObject var8 = var2.getOgObject();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            label76: {
               IrisJigsawPiece var9 = this.getPiece();
               IrisJigsawPiece var10 = var2.getPiece();
               if (var9 == null) {
                  if (var10 == null) {
                     break label76;
                  }
               } else if (var9.equals(var10)) {
                  break label76;
               }

               return false;
            }

            IrisObjectRotation var11 = this.getRotation();
            IrisObjectRotation var12 = var2.getRotation();
            if (var11 == null) {
               if (var12 != null) {
                  return false;
               }
            } else if (!var11.equals(var12)) {
               return false;
            }

            KList var13 = this.getConnected();
            KList var14 = var2.getConnected();
            if (var13 == null) {
               if (var14 != null) {
                  return false;
               }
            } else if (!var13.equals(var14)) {
               return false;
            }

            AxisAlignedBB var15 = this.getBox();
            AxisAlignedBB var16 = var2.getBox();
            if (var15 == null) {
               if (var16 != null) {
                  return false;
               }
            } else if (!var15.equals(var16)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof PlannedPiece;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var10 = var2 * 59 + (this.isDead() ? 79 : 97);
      IrisPosition var3 = this.getPosition();
      var10 = var10 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisObject var4 = this.getObject();
      var10 = var10 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisObject var5 = this.getOgObject();
      var10 = var10 * 59 + (var5 == null ? 43 : var5.hashCode());
      IrisJigsawPiece var6 = this.getPiece();
      var10 = var10 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisObjectRotation var7 = this.getRotation();
      var10 = var10 * 59 + (var7 == null ? 43 : var7.hashCode());
      KList var8 = this.getConnected();
      var10 = var10 * 59 + (var8 == null ? 43 : var8.hashCode());
      AxisAlignedBB var9 = this.getBox();
      var10 = var10 * 59 + (var9 == null ? 43 : var9.hashCode());
      return var10;
   }

   public static record ParentConnection(PlannedPiece parent, IrisJigsawPieceConnector parentConnector, PlannedPiece self, IrisJigsawPieceConnector connector) {
      public ParentConnection(PlannedPiece parent, IrisJigsawPieceConnector parentConnector, PlannedPiece self, IrisJigsawPieceConnector connector) {
         this.parent = var1;
         this.parentConnector = var2;
         this.self = var3;
         this.connector = var4;
      }

      public IrisPosition getTargetPosition() {
         IrisPosition var1 = (IrisPosition)this.parent.realPositions.get(this.parentConnector);
         return var1 == null ? null : var1.add(new IrisPosition(this.parentConnector.getDirection().toVector())).sub(this.connector.getPosition()).sub(new IrisPosition(this.self.object.getCenter()));
      }

      public PlannedPiece parent() {
         return this.parent;
      }

      public IrisJigsawPieceConnector parentConnector() {
         return this.parentConnector;
      }

      public PlannedPiece self() {
         return this.self;
      }

      public IrisJigsawPieceConnector connector() {
         return this.connector;
      }
   }
}
