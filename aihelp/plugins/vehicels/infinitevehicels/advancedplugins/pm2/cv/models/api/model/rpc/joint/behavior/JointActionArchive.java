package advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;

public class JointActionArchive {
   private final Map<String, JointActionType<?>> idRegistry = new ConcurrentHashMap();
   private Gson gson;

   public void register(JointActionType<?> var1) {
      this.idRegistry.put(var1.getId(), var1);
   }

   @Nullable
   public JointActionType<?> getById(String var1) {
      return (JointActionType)this.idRegistry.get(var1);
   }

   public Set<String> getIds() {
      return ImmutableSet.copyOf(this.idRegistry.keySet());
   }

   public Gson getGson() {
      if (this.gson == null) {
         GsonBuilder var1 = new GsonBuilder();
         this.idRegistry.forEach((var1x, var2) -> {
            Map var3 = var2.getDataDeserializer();
            Objects.requireNonNull(var1);
            Objects.requireNonNull(var1);
            var3.forEach(var1::registerTypeAdapter);
         });
         this.gson = var1.create();
      }

      return this.gson;
   }
}
