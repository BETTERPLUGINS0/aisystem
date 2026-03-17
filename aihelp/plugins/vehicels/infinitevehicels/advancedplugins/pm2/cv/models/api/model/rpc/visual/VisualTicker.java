package advancedplugins.pm2.cv.models.api.model.rpc.visual;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

public class VisualTicker {
   private final Map<Integer, UUID> entityIdLookup = Maps.newConcurrentMap();
   private final Map<UUID, Visual> uuidLookup = Maps.newConcurrentMap();

   public void updateVisuals() {
      Iterator var1 = this.uuidLookup.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         Visual var3 = (Visual)var2.getValue();
         if (var3.isReady()) {
            if (!var3.tick()) {
               this.uuidLookup.remove(var2.getKey());
               this.entityIdLookup.remove(var3.getOriginal().getEntityId());
               var3.getRenderer().dispose();
               var3.getOriginal().getData().cleanup();
               var3.getOriginal().setForcedAlive(false);
               var3.dispose();
            } else {
               var3.getRenderer().dispatch();
               var3.getOriginal().getData().cleanup();
            }
         }
      }

   }

   public void registerVisual(BaseEntity<?> var1, Visual var2) {
      this.entityIdLookup.put(var1.getEntityId(), var1.getUUID());
      this.uuidLookup.put(var1.getUUID(), var2);
   }

   public Visual getVisual(int var1) {
      return this.getVisual((UUID)this.entityIdLookup.get(var1));
   }

   public Visual getVisual(UUID var1) {
      return var1 == null ? null : (Visual)this.uuidLookup.get(var1);
   }

   public Visual removeVisual(int var1) {
      return this.removeVisual((UUID)this.entityIdLookup.get(var1));
   }

   public Visual removeVisual(UUID var1) {
      if (var1 == null) {
         return null;
      } else {
         Visual var2 = (Visual)this.uuidLookup.get(var1);
         if (var2 != null) {
            var2.setRemoved();
         }

         return var2;
      }
   }
}
