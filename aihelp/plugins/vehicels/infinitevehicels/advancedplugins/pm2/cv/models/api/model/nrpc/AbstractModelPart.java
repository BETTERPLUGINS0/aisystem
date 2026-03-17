package advancedplugins.pm2.cv.models.api.model.nrpc;

import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeDisplayEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public abstract class AbstractModelPart {
   @NotNull
   private final UUID uniqueID;
   @NotNull
   private final String name;
   @NotNull
   private final AbstractModel parent;
   @NotNull
   private final Transformation defaultTransformation;
   @NotNull
   private final Material material;
   @NotNull
   private final BlockbenchModel.Element coresspondingElement;
   @NotNull
   private final Vector3f positionOffset;
   @NotNull
   private final Vector3f rawOrigin;
   @NotNull
   private final Vector3f rawOffset;
   @Nullable
   private final BlockbenchModel.Group parentBoneGroup;

   public abstract void spawn();

   protected EntityType findEntityType() {
      return this.material.isBlock() ? EntityType.BLOCK_DISPLAY : EntityType.ITEM_DISPLAY;
   }

   public abstract FakeDisplayEntity fakeDisplayEntity();

   public String getParentBoneName() {
      return this.parentBoneGroup != null ? this.parentBoneGroup.getName() : null;
   }

   @NotNull
   @Generated
   public UUID getUniqueID() {
      return this.uniqueID;
   }

   @NotNull
   @Generated
   public String getName() {
      return this.name;
   }

   @NotNull
   @Generated
   public AbstractModel getParent() {
      return this.parent;
   }

   @NotNull
   @Generated
   public Transformation getDefaultTransformation() {
      return this.defaultTransformation;
   }

   @NotNull
   @Generated
   public Material getMaterial() {
      return this.material;
   }

   @NotNull
   @Generated
   public BlockbenchModel.Element getCoresspondingElement() {
      return this.coresspondingElement;
   }

   @NotNull
   @Generated
   public Vector3f getPositionOffset() {
      return this.positionOffset;
   }

   @NotNull
   @Generated
   public Vector3f getRawOrigin() {
      return this.rawOrigin;
   }

   @NotNull
   @Generated
   public Vector3f getRawOffset() {
      return this.rawOffset;
   }

   @Nullable
   @Generated
   public BlockbenchModel.Group getParentBoneGroup() {
      return this.parentBoneGroup;
   }

   @Generated
   public AbstractModelPart(@NotNull UUID var1, @NotNull String var2, @NotNull AbstractModel var3, @NotNull Transformation var4, @NotNull Material var5, @NotNull BlockbenchModel.Element var6, @NotNull Vector3f var7, @NotNull Vector3f var8, @NotNull Vector3f var9, @Nullable BlockbenchModel.Group var10) {
      this.uniqueID = var1;
      this.name = var2;
      this.parent = var3;
      this.defaultTransformation = var4;
      this.material = var5;
      this.coresspondingElement = var6;
      this.positionOffset = var7;
      this.rawOrigin = var8;
      this.rawOffset = var9;
      this.parentBoneGroup = var10;
   }
}
