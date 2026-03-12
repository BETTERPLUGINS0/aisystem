package emanondev.itemtag.activity.target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PrimitiveTargetType extends TargetType {
   public PrimitiveTargetType(String id) {
      super(id);
   }

   @NotNull
   protected List<Object> defaultGetTargets(@Nullable String info, @NotNull HashMap<String, TargetType.Target> baseTargets) {
      throw new UnsupportedOperationException();
   }

   public TargetType.Target read(@Nullable String info) {
      return new PrimitiveTargetType.PrimitiveTarget();
   }

   public PrimitiveTargetType.PrimitiveTarget create(@NotNull Object value) {
      return new PrimitiveTargetType.PrimitiveTarget(value);
   }

   public PrimitiveTargetType.PrimitiveTarget create(@NotNull Collection<Object> values) {
      return new PrimitiveTargetType.PrimitiveTarget(values);
   }

   private final class PrimitiveTarget extends TargetType.Target {
      private final List<Object> values;

      private PrimitiveTarget() {
         super((String)null);
         this.values = null;
      }

      private PrimitiveTarget(Object param2) {
         super((String)null);
         this.values = new ArrayList();
         if (value != null) {
            this.values.add(value);
         }

      }

      private PrimitiveTarget(Collection<Object> param2) {
         super((String)null);
         this.values = new ArrayList();
         Iterator var3 = values.iterator();

         while(var3.hasNext()) {
            Object value = var3.next();
            if (value != null) {
               this.values.add(value);
            }
         }

      }

      @NotNull
      public List<Object> getTargets(@NotNull HashMap<String, TargetType.Target> baseTargets) {
         if (this.values == null) {
            if (baseTargets.containsKey(this.getId())) {
               return ((TargetType.Target)baseTargets.get(this.getId())).getTargets(baseTargets);
            } else {
               throw new IllegalArgumentException();
            }
         } else {
            return Collections.unmodifiableList(this.values);
         }
      }

      // $FF: synthetic method
      PrimitiveTarget(Object x1) {
         this();
      }

      // $FF: synthetic method
      PrimitiveTarget(Object x1, Object x2) {
         this((Object)x1);
      }

      // $FF: synthetic method
      PrimitiveTarget(Collection x1, Object x2) {
         this((Collection)x1);
      }
   }
}
