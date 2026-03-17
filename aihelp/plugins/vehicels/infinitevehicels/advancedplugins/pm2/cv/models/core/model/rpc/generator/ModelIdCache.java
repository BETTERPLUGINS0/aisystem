package advancedplugins.pm2.cv.models.core.model.rpc.generator;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.BaseItemEnum;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ItemModelData;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import lombok.Generated;

public class ModelIdCache {
   protected final Map<String, Integer> cachedId = new ConcurrentHashMap();
   protected final Queue<Integer> unused = new LinkedList();
   private final transient Map<Integer, String> dataToModel = new ConcurrentHashMap();
   private final transient Set<String> pendingRemove = new HashSet();
   protected int nextId = 1;
   private transient int maxId;

   public void gatherExistingIds(BaseItemEnum var1, Map<String, BlueprintJoint> var2) {
      this.maxId = 0;
      this.dataToModel.clear();
      Iterator var3 = this.cachedId.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         String var5 = (String)var4.getKey();
         Integer var6 = (Integer)var4.getValue();
         if (var2.containsKey(var5)) {
            BlueprintJoint var7 = (BlueprintJoint)var2.remove(var5);
            var7.setDataId(var6);
            var7.setBaseItem(var1);
            ItemModelData.SubModel var8 = var7.getModelData().getMultiModels().getSubModel(var5);
            var8.setData(var6);
            var8.setItem(var1);
            this.dataToModel.put(var6, var5);
            this.maxId = Math.max(var6, this.maxId);
         } else {
            this.pendingRemove.add(var5);
            this.unused.add((Integer)this.cachedId.get(var5));
         }
      }

      Map var9 = this.cachedId;
      Objects.requireNonNull(var9);
      Set var10000 = this.pendingRemove;
      Objects.requireNonNull(var9);
      var10000.forEach(var9::remove);
      this.pendingRemove.clear();
   }

   public void generateNewIds(BaseItemEnum var1, String var2, BlueprintJoint var3) {
      int var4;
      if (this.unused.isEmpty()) {
         int var5 = this.nextId;
         var4 = var5;
         this.nextId = var5 + 1;
      } else {
         var4 = (Integer)this.unused.poll();
      }

      var3.setDataId(var4);
      var3.setBaseItem(var1);
      ItemModelData.SubModel var6 = var3.getModelData().getMultiModels().getSubModel(var2);
      var6.setData(var4);
      var6.setItem(var1);
      this.maxId = Math.max(var4, this.maxId);
      this.dataToModel.put(var4, var2);
      this.cachedId.put(var2, var4);
   }

   public void endSession() {
      int var1;
      for(Iterator var2 = this.unused.iterator(); var2.hasNext(); this.maxId = Math.max(this.maxId, var1)) {
         var1 = (Integer)var2.next();
      }

      this.nextId = this.maxId + 1;
   }

   public void sortedIterate(BiConsumer<String, Integer> var1) {
      for(int var2 = 1; var2 < this.nextId; ++var2) {
         String var3 = (String)this.dataToModel.get(var2);
         if (var3 != null) {
            var1.accept(var3.replace(":", "/"), var2);
         }
      }

   }

   public void cleanUp() {
      this.dataToModel.clear();
      this.pendingRemove.clear();
   }

   public int getCacheLoad() {
      return this.cachedId.size();
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.cachedId);
      return "ModelIdCache(cachedId=" + var10000 + ", unused=" + String.valueOf(this.unused) + ", dataToModel=" + String.valueOf(this.dataToModel) + ", pendingRemove=" + String.valueOf(this.pendingRemove) + ", nextId=" + this.nextId + ", maxId=" + this.maxId + ")";
   }
}
