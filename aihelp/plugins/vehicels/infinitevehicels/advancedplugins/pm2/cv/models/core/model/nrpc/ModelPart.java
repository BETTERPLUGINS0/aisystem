package advancedplugins.pm2.cv.models.core.model.nrpc;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.nrpc.AbstractModel;
import advancedplugins.pm2.cv.models.api.model.nrpc.AbstractModelPart;
import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeBlockDisplayEntity;
import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeDisplayEntity;
import advancedplugins.pm2.cv.models.api.model.nrpc.nms.FakeItemDisplayEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.blockbench.BlockbenchModel;
import java.util.Iterator;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class ModelPart extends AbstractModelPart {
   private FakeDisplayEntity fakeDisplayEntity = null;
   private Transformation currentTransformation;
   private boolean transformationDirty = false;
   private int tint = 0;

   public ModelPart(@NotNull UUID var1, @NotNull String var2, @NotNull AbstractModel var3, @NotNull Transformation var4, @NotNull Material var5, @NotNull BlockbenchModel.Element var6, @NotNull Vector3f var7, @Nullable BlockbenchModel.Group var8, @NotNull Vector3f var9, @NotNull Vector3f var10) {
      super(var1, var2, var3, var4, var5, var6, var7, var9, var10, var8);
   }

   public void spawn() {
      if (this.fakeDisplayEntity() != null) {
         this.fakeDisplayEntity.destroy();
         this.fakeDisplayEntity = null;
      }

      this.fakeDisplayEntity = this.findValidFakeDisplayEntity();
      this.fakeDisplayEntity.setTransformation(this.getDefaultTransformation());
      Transformation var1 = this.getDefaultTransformation();
      this.currentTransformation = new Transformation(var1.getTranslation(), var1.getLeftRotation(), var1.getScale(), var1.getRightRotation());
      FakeDisplayEntity var3 = this.fakeDisplayEntity;
      if (var3 instanceof FakeItemDisplayEntity) {
         FakeItemDisplayEntity var2 = (FakeItemDisplayEntity)var3;
         var2.setItemStack(this.getMaterial());
      }

      var3 = this.fakeDisplayEntity;
      if (var3 instanceof FakeBlockDisplayEntity) {
         FakeBlockDisplayEntity var4 = (FakeBlockDisplayEntity)var3;
         var4.setBlock(this.getMaterial());
      }

   }

   public void setCurrentTransformation(Transformation var1) {
      if (this.hasTransformationChanged(var1)) {
         this.currentTransformation = var1;
         this.transformationDirty = true;
         this.fakeDisplayEntity.setTransformation(var1);
         Iterator var2 = this.fakeDisplayEntity.getViewers().iterator();

         while(var2.hasNext()) {
            UUID var3 = (UUID)var2.next();
            Player var4 = Bukkit.getPlayer(var3);
            if (var4 != null && var4.isOnline()) {
               this.fakeDisplayEntity.packAndSend(var4);
            }
         }

         this.transformationDirty = false;
      }
   }

   private boolean hasTransformationChanged(Transformation var1) {
      if (this.currentTransformation == null) {
         return true;
      } else {
         float var2 = 0.001F;
         boolean var3 = !this.currentTransformation.getTranslation().equals(var1.getTranslation(), var2);
         boolean var4 = !this.currentTransformation.getLeftRotation().equals(var1.getLeftRotation(), var2);
         boolean var5 = !this.currentTransformation.getScale().equals(var1.getScale(), var2);
         boolean var6 = !this.currentTransformation.getRightRotation().equals(var1.getRightRotation(), var2);
         return var3 || var4 || var5 || var6;
      }
   }

   private FakeDisplayEntity findValidFakeDisplayEntity() {
      switch(this.findEntityType()) {
      case ITEM_DISPLAY:
         return ModelAPI.getNMSHandler().getFakeDisplayEntityManager().spawn(FakeItemDisplayEntity.class, this.getParent().getLocation());
      case BLOCK_DISPLAY:
         return ModelAPI.getNMSHandler().getFakeDisplayEntityManager().spawn(FakeBlockDisplayEntity.class, this.getParent().getLocation());
      default:
         throw new UnsupportedOperationException("Unsupported fake display entity type: " + String.valueOf(this.findEntityType()));
      }
   }

   public FakeDisplayEntity fakeDisplayEntity() {
      return this.fakeDisplayEntity;
   }

   public Transformation getCurrentTransformation() {
      return this.currentTransformation;
   }

   @Generated
   public FakeDisplayEntity getFakeDisplayEntity() {
      return this.fakeDisplayEntity;
   }

   @Generated
   public boolean isTransformationDirty() {
      return this.transformationDirty;
   }

   @Generated
   public int getTint() {
      return this.tint;
   }

   @Generated
   public void setFakeDisplayEntity(FakeDisplayEntity var1) {
      this.fakeDisplayEntity = var1;
   }

   @Generated
   public void setTransformationDirty(boolean var1) {
      this.transformationDirty = var1;
   }

   @Generated
   public void setTint(int var1) {
      this.tint = var1;
   }
}
