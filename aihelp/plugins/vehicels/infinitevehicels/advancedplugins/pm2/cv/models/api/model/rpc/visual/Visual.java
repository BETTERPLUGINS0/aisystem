package advancedplugins.pm2.cv.models.api.model.rpc.visual;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IPosition;
import advancedplugins.pm2.cv.models.api.model.rpc.Tickable;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRenderer;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import java.util.Collection;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;

public abstract class Visual implements Tickable {
   public abstract BaseEntity<?> getOriginal();

   public abstract VisualRenderer getRenderer();

   public abstract IPosition getPosition();

   public abstract void dispose();

   public abstract boolean isReady();

   public abstract boolean isDisposed();

   public abstract void setRemoved();

   public abstract boolean isOriginalVisible();

   public abstract void setOriginalVisible(boolean var1);

   public abstract void setModelScale(int var1);

   public abstract ItemStack getModel();

   public abstract void setModel(ItemStack var1);

   public abstract DataTracker<ItemStack> getModelTracker();

   public abstract Color getColor();

   public abstract void setColor(Color var1);

   public abstract boolean isEnchanted();

   public abstract void setEnchanted(boolean var1);

   public abstract boolean isVisible();

   public abstract void setVisible(boolean var1);

   public void useModel(String var1, String var2) {
      ModelBlueprint var3 = ModelAPI.getBlueprint(var1);
      if (var3 != null) {
         BlueprintJoint var4 = (BlueprintJoint)var3.getFlatMap().get(var2);
         if (var4 != null && var4.isRenderer()) {
            Collection var5 = var4.getModelData().createItemStack();
            if (!var5.isEmpty()) {
               this.setModel((ItemStack)var5.iterator().next());
            }
         }
      }

   }

   public void registerSelf() {
      ModelAPI.getAPI().getVFXUpdater().registerVisual(this.getOriginal(), this);
      this.getOriginal().registerData();
   }
}
