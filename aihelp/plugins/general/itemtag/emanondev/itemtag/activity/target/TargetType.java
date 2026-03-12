package emanondev.itemtag.activity.target;

import emanondev.itemtag.activity.Factory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class TargetType extends Factory {
   public TargetType(String id) {
      super(id);
   }

   @NotNull
   public abstract TargetType.Target read(@Nullable String var1);

   @NotNull
   protected abstract List<Object> defaultGetTargets(@Nullable String var1, @NotNull HashMap<String, TargetType.Target> var2);

   @Nullable
   protected TargetType.Target getFirstAvailable(@NotNull HashMap<String, TargetType.Target> baseTargets, @NotNull List<String> values) {
      Iterator var3 = values.iterator();

      String key;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         key = (String)var3.next();
      } while(!baseTargets.containsKey(key));

      return (TargetType.Target)baseTargets.get(key);
   }

   public class Target extends Factory.Element {
      public Target(@Nullable String param2) {
         super(info);
      }

      @NotNull
      public List<Object> getTargets(@NotNull HashMap<String, TargetType.Target> baseTargets) {
         return TargetType.this.defaultGetTargets(this.getInfo(), baseTargets);
      }

      @NotNull
      public String toString() {
         return "@" + this.getId() + (this.getInfo() == null ? "" : "(" + this.getInfo() + ")");
      }
   }
}
