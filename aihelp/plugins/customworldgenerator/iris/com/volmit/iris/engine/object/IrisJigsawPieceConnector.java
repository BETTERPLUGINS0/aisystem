package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.ArrayType;
import com.volmit.iris.engine.object.annotations.DependsOn;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.RegistryListResource;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import lombok.Generated;

@Snippet("connector")
@Desc("Represents a structure tile")
public class IrisJigsawPieceConnector {
   @Required
   @Desc("The name of this connector, such as entry, or table node. This is a name for organization. Other connectors can specifically use targetName to target a specific connector type. Multiple connectors can use the same name.")
   private String name = "";
   @Required
   @Desc("Target a piece's connector with the specified name. For any piece's connector, define * or don't define it.")
   private String targetName = "*";
   @Desc("Rotates the placed piece on this connector. If rotation is enabled, this connector will effectivley rotate, if this connector is facing the Z direction, then the connected piece would rotate in the X,Y direction in 90 degree segments.")
   private boolean rotateConnector = false;
   @Desc("If set to true, this connector is allowed to place pieces inside of it's own piece. For example if you are adding a light post, or house on top of a path piece, you would set this to true to allow the piece to collide with the path bounding box.")
   private boolean innerConnector = false;
   @RegistryListResource(IrisJigsawPool.class)
   @Desc("Pick piece pools to place onto this connector")
   @ArrayType(
      type = String.class,
      min = 1
   )
   @Required
   private KList<String> pools = new KList();
   @RegistryListResource(IrisEntity.class)
   @Desc("Pick an entity to spawn on this connector")
   private String spawnEntity;
   @Desc("Stop the entity from despawning")
   private boolean keepEntity;
   @MaxNumber(50.0D)
   @MinNumber(1.0D)
   @Desc("The amount of entities to spawn (must be a whole number)")
   private int entityCount = 1;
   @Desc("The relative position this connector is located at for connecting to other pieces")
   @Required
   private IrisPosition position = new IrisPosition(0, 0, 0);
   @Desc("The relative position to this connector to place entities at")
   @DependsOn({"spawnEntity"})
   private IrisPosition entityPosition = null;
   @Desc("The direction this connector is facing. If the direction is set to UP, then pieces will place ABOVE the connector.")
   @Required
   private IrisDirection direction;
   @Desc("Lock the Y position of this connector")
   private boolean lockY;

   public String toString() {
      String var10000 = this.direction.getFace().name();
      return var10000 + "@(" + this.position.getX() + "," + this.position.getY() + "," + this.position.getZ() + ")";
   }

   public IrisJigsawPieceConnector copy() {
      IrisJigsawPieceConnector var1 = new IrisJigsawPieceConnector();
      var1.setInnerConnector(this.isInnerConnector());
      var1.setTargetName(this.getTargetName());
      var1.setPosition(this.getPosition().copy());
      var1.setDirection(this.getDirection());
      var1.setRotateConnector(this.isRotateConnector());
      var1.setName(this.getName());
      var1.setSpawnEntity(this.getSpawnEntity());
      var1.setPools(this.getPools().copy());
      return var1;
   }

   @Generated
   public IrisJigsawPieceConnector() {
      this.direction = IrisDirection.UP_POSITIVE_Y;
      this.lockY = false;
   }

   @Generated
   public IrisJigsawPieceConnector(final String name, final String targetName, final boolean rotateConnector, final boolean innerConnector, final KList<String> pools, final String spawnEntity, final boolean keepEntity, final int entityCount, final IrisPosition position, final IrisPosition entityPosition, final IrisDirection direction, final boolean lockY) {
      this.direction = IrisDirection.UP_POSITIVE_Y;
      this.lockY = false;
      this.name = var1;
      this.targetName = var2;
      this.rotateConnector = var3;
      this.innerConnector = var4;
      this.pools = var5;
      this.spawnEntity = var6;
      this.keepEntity = var7;
      this.entityCount = var8;
      this.position = var9;
      this.entityPosition = var10;
      this.direction = var11;
      this.lockY = var12;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public String getTargetName() {
      return this.targetName;
   }

   @Generated
   public boolean isRotateConnector() {
      return this.rotateConnector;
   }

   @Generated
   public boolean isInnerConnector() {
      return this.innerConnector;
   }

   @Generated
   public KList<String> getPools() {
      return this.pools;
   }

   @Generated
   public String getSpawnEntity() {
      return this.spawnEntity;
   }

   @Generated
   public boolean isKeepEntity() {
      return this.keepEntity;
   }

   @Generated
   public int getEntityCount() {
      return this.entityCount;
   }

   @Generated
   public IrisPosition getPosition() {
      return this.position;
   }

   @Generated
   public IrisPosition getEntityPosition() {
      return this.entityPosition;
   }

   @Generated
   public IrisDirection getDirection() {
      return this.direction;
   }

   @Generated
   public boolean isLockY() {
      return this.lockY;
   }

   @Generated
   public IrisJigsawPieceConnector setName(final String name) {
      this.name = var1;
      return this;
   }

   @Generated
   public IrisJigsawPieceConnector setTargetName(final String targetName) {
      this.targetName = var1;
      return this;
   }

   @Generated
   public IrisJigsawPieceConnector setRotateConnector(final boolean rotateConnector) {
      this.rotateConnector = var1;
      return this;
   }

   @Generated
   public IrisJigsawPieceConnector setInnerConnector(final boolean innerConnector) {
      this.innerConnector = var1;
      return this;
   }

   @Generated
   public IrisJigsawPieceConnector setPools(final KList<String> pools) {
      this.pools = var1;
      return this;
   }

   @Generated
   public IrisJigsawPieceConnector setSpawnEntity(final String spawnEntity) {
      this.spawnEntity = var1;
      return this;
   }

   @Generated
   public IrisJigsawPieceConnector setKeepEntity(final boolean keepEntity) {
      this.keepEntity = var1;
      return this;
   }

   @Generated
   public IrisJigsawPieceConnector setEntityCount(final int entityCount) {
      this.entityCount = var1;
      return this;
   }

   @Generated
   public IrisJigsawPieceConnector setPosition(final IrisPosition position) {
      this.position = var1;
      return this;
   }

   @Generated
   public IrisJigsawPieceConnector setEntityPosition(final IrisPosition entityPosition) {
      this.entityPosition = var1;
      return this;
   }

   @Generated
   public IrisJigsawPieceConnector setDirection(final IrisDirection direction) {
      this.direction = var1;
      return this;
   }

   @Generated
   public IrisJigsawPieceConnector setLockY(final boolean lockY) {
      this.lockY = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisJigsawPieceConnector)) {
         return false;
      } else {
         IrisJigsawPieceConnector var2 = (IrisJigsawPieceConnector)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isRotateConnector() != var2.isRotateConnector()) {
            return false;
         } else if (this.isInnerConnector() != var2.isInnerConnector()) {
            return false;
         } else if (this.isKeepEntity() != var2.isKeepEntity()) {
            return false;
         } else if (this.getEntityCount() != var2.getEntityCount()) {
            return false;
         } else if (this.isLockY() != var2.isLockY()) {
            return false;
         } else {
            label107: {
               String var3 = this.getName();
               String var4 = var2.getName();
               if (var3 == null) {
                  if (var4 == null) {
                     break label107;
                  }
               } else if (var3.equals(var4)) {
                  break label107;
               }

               return false;
            }

            String var5 = this.getTargetName();
            String var6 = var2.getTargetName();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            label93: {
               KList var7 = this.getPools();
               KList var8 = var2.getPools();
               if (var7 == null) {
                  if (var8 == null) {
                     break label93;
                  }
               } else if (var7.equals(var8)) {
                  break label93;
               }

               return false;
            }

            label86: {
               String var9 = this.getSpawnEntity();
               String var10 = var2.getSpawnEntity();
               if (var9 == null) {
                  if (var10 == null) {
                     break label86;
                  }
               } else if (var9.equals(var10)) {
                  break label86;
               }

               return false;
            }

            label79: {
               IrisPosition var11 = this.getPosition();
               IrisPosition var12 = var2.getPosition();
               if (var11 == null) {
                  if (var12 == null) {
                     break label79;
                  }
               } else if (var11.equals(var12)) {
                  break label79;
               }

               return false;
            }

            label72: {
               IrisPosition var13 = this.getEntityPosition();
               IrisPosition var14 = var2.getEntityPosition();
               if (var13 == null) {
                  if (var14 == null) {
                     break label72;
                  }
               } else if (var13.equals(var14)) {
                  break label72;
               }

               return false;
            }

            IrisDirection var15 = this.getDirection();
            IrisDirection var16 = var2.getDirection();
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
      return var1 instanceof IrisJigsawPieceConnector;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var10 = var2 * 59 + (this.isRotateConnector() ? 79 : 97);
      var10 = var10 * 59 + (this.isInnerConnector() ? 79 : 97);
      var10 = var10 * 59 + (this.isKeepEntity() ? 79 : 97);
      var10 = var10 * 59 + this.getEntityCount();
      var10 = var10 * 59 + (this.isLockY() ? 79 : 97);
      String var3 = this.getName();
      var10 = var10 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getTargetName();
      var10 = var10 * 59 + (var4 == null ? 43 : var4.hashCode());
      KList var5 = this.getPools();
      var10 = var10 * 59 + (var5 == null ? 43 : var5.hashCode());
      String var6 = this.getSpawnEntity();
      var10 = var10 * 59 + (var6 == null ? 43 : var6.hashCode());
      IrisPosition var7 = this.getPosition();
      var10 = var10 * 59 + (var7 == null ? 43 : var7.hashCode());
      IrisPosition var8 = this.getEntityPosition();
      var10 = var10 * 59 + (var8 == null ? 43 : var8.hashCode());
      IrisDirection var9 = this.getDirection();
      var10 = var10 * 59 + (var9 == null ? 43 : var9.hashCode());
      return var10;
   }
}
