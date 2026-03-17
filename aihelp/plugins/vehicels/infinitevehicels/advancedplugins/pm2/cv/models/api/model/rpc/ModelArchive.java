package advancedplugins.pm2.cv.models.api.model.rpc;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.utils.archive.AbstractArchive;
import com.google.common.collect.Ordering;
import java.util.Collection;
import java.util.PriorityQueue;

public class ModelArchive extends AbstractArchive<ModelBlueprint> {
   private final PriorityQueue<String> orderedId = new PriorityQueue(Ordering.natural());

   public void registerBlueprint(ModelBlueprint var1) {
      this.orderedId.add(var1.getName());
      this.register(var1.getName(), var1);
   }

   public void clear() {
      this.orderedId.clear();
      this.registry.clear();
   }

   public Collection<String> getOrderedId() {
      return this.orderedId;
   }
}
