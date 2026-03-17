package advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JointBehaviorData {
   private final Map<String, Object> data;

   public JointBehaviorData(Map<String, Object> var1) {
      this.data = var1;
   }

   @Nullable
   public <T> T get(String var1) {
      try {
         return this.data.get(var1);
      } catch (ClassCastException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   @NotNull
   public <T> T get(String var1, @NotNull T var2) {
      Object var3 = this.data.get(var1);
      if (var3 == null) {
         return var2;
      } else {
         Class var4 = var2.getClass();
         Class var5 = var3.getClass();
         if (var4.isAssignableFrom(var5)) {
            return var3;
         } else {
            (new ClassCastException(String.format("Could not cast %s to %s. Returning default value.", var5.getSimpleName(), var4.getSimpleName()))).printStackTrace();
            return var2;
         }
      }
   }
}
