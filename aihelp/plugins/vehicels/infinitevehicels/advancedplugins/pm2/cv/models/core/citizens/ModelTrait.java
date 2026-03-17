package advancedplugins.pm2.cv.models.core.citizens;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BukkitEntity;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.core.data.DataUpdater;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

@TraitName("im_model")
public class ModelTrait extends Trait {
   private String modelData;

   public ModelTrait() {
      super("im_model");
   }

   public void load(DataKey var1) {
      this.modelData = var1.getString("model_data");
   }

   public void save(DataKey var1) {
      IModelContainer var2 = this.getModeledEntity();
      if (var2 != null) {
         var2.save().ifPresent((var1x) -> {
            this.modelData = var1x.toString();
         });
      } else {
         this.modelData = null;
      }

      var1.setString("model_data", this.modelData);
   }

   public void onDespawn() {
      IModelContainer var1 = this.getModeledEntity();
      if (var1 != null) {
         var1.save().ifPresent((var1x) -> {
            this.modelData = var1x.toString();
         });
      } else {
         this.modelData = null;
      }

   }

   public void onSpawn() {
      if (this.modelData != null) {
         Entity var1 = this.getNPC().getEntity();
         Location var2 = var1.getLocation();
         SavedData var3 = DataUpdater.convertToSavedData(var2, this.modelData);
         if (DataUpdater.tryUpdate(var3)) {
            BukkitEntity var4 = new BukkitEntity(var1);
            var4.getBodyRotationController().setYBodyRot(var2.getYaw());
            IModelContainer var5 = ModelAPI.create((BaseEntity)var4);
            var5.setSaved(false);
            var5.load(var3);
         }
      }

   }

   public IModelContainer getModeledEntity() {
      return this.npc.getEntity() != null ? ModelAPI.getModeledEntity(this.npc.getEntity()) : null;
   }

   public IModelContainer getOrCreateModeledEntity() {
      return this.npc.getEntity() != null ? ModelAPI.getOrCreateModeledEntity(this.npc.getEntity()) : null;
   }
}
