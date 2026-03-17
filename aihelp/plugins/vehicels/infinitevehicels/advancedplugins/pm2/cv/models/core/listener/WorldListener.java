package advancedplugins.pm2.cv.models.core.listener;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.ServerInfo;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.ModelGenerator;
import advancedplugins.pm2.cv.models.api.utils.data.io.SavedData;
import advancedplugins.pm2.cv.models.core.data.DataUpdater;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class WorldListener implements Listener {
   private final ModelGenerator generator = ModelAPI.getModelGenerator();

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onEntityLoad(EntitiesLoadEvent var1) {
      if (this.generator.isInitialized()) {
         this.loadEntities(var1.getEntities());
      } else {
         this.generator.queueTask(ModelGenerator.Phase.POST_IMPORT, () -> {
            this.loadEntities(var1.getEntities());
         });
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void onEntityUnload(EntitiesUnloadEvent var1) {
      if (this.generator.isInitialized()) {
         this.unloadEntities(var1.getEntities());
      } else {
         this.generator.queueTask(ModelGenerator.Phase.POST_IMPORT, () -> {
            this.unloadEntities(var1.getEntities());
         });
      }

   }

   private void loadEntities(List<Entity> var1) {
      var1.stream().filter((var0) -> {
         return !(var0 instanceof Player);
      }).filter((var0) -> {
         return ModelAPI.getModeledEntity(var0.getUniqueId()) == null;
      }).forEach(this::processEntityLoad);
   }

   private void processEntityLoad(Entity var1) {
      String var2 = (String)var1.getPersistentDataContainer().get(SavedData.DATA_KEY, PersistentDataType.STRING);
      if (var2 != null) {
         try {
            SavedData var3 = DataUpdater.convertToSavedData(var1.getLocation(), var2);
            if (DataUpdater.tryUpdate(var3)) {
               IModelContainer var4 = ModelAPI.create(var1);
               var4.load(var3);
            }
         } catch (JsonSyntaxException var5) {
            var5.printStackTrace();
         }

      }
   }

   private void unloadEntities(List<Entity> var1) {
      var1.stream().filter((var0) -> {
         return !(var0 instanceof Player);
      }).filter((var0) -> {
         return !ServerInfo.HAS_CITIZENS || !CitizensAPI.getNPCRegistry().isNPC(var0);
      }).forEach(this::processEntityUnload);
   }

   private void processEntityUnload(Entity var1) {
      PersistentDataContainer var2 = var1.getPersistentDataContainer();
      IModelContainer var3 = ModelAPI.getModeledEntity(var1.getUniqueId());
      if (var3 == null) {
         var2.remove(SavedData.DATA_KEY);
      } else {
         if (var3.shouldBeSaved()) {
            var3.save().ifPresentOrElse((var1x) -> {
               var2.set(SavedData.DATA_KEY, PersistentDataType.STRING, var1x.toString());
            }, () -> {
               var2.remove(SavedData.DATA_KEY);
            });
         } else {
            var2.remove(SavedData.DATA_KEY);
         }

         ModelAPI.getAPI().getModelUpdaters().forceRemoveModeledEntity(var3);
      }
   }
}
