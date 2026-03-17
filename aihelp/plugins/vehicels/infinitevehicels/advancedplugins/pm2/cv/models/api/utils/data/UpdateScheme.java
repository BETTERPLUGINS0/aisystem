package advancedplugins.pm2.cv.models.api.utils.data;

import java.util.HashSet;
import java.util.Set;

public class UpdateScheme<T> {
   private final Set<T> added = new HashSet();
   private final Set<T> updated = new HashSet();
   private final Set<T> removed = new HashSet();

   public void addUpdated(T var1) {
      this.updated.add(var1);
   }

   public void addAdded(T var1) {
      this.added.add(var1);
      this.removed.remove(var1);
      this.updated.remove(var1);
   }

   public void addRemove(T var1) {
      this.removed.add(var1);
      this.added.remove(var1);
      this.updated.remove(var1);
   }

   public boolean hasUpdate() {
      return !this.added.isEmpty() || !this.updated.isEmpty() || !this.removed.isEmpty();
   }

   public UpdateScheme.Mode getUpdateMode(T var1) {
      if (this.added.contains(var1)) {
         return UpdateScheme.Mode.ADD;
      } else if (this.updated.contains(var1)) {
         return UpdateScheme.Mode.UPDATE;
      } else {
         return this.removed.contains(var1) ? UpdateScheme.Mode.REMOVE : UpdateScheme.Mode.NONE;
      }
   }

   public void reset() {
      this.added.clear();
      this.updated.clear();
      this.removed.clear();
   }

   public Set<T> getAdded() {
      return this.added;
   }

   public Set<T> getUpdated() {
      return this.updated;
   }

   public Set<T> getRemoved() {
      return this.removed;
   }

   public static enum Mode {
      NONE,
      UPDATE,
      ADD,
      REMOVE;

      private static UpdateScheme.Mode[] $values() {
         return new UpdateScheme.Mode[]{NONE, UPDATE, ADD, REMOVE};
      }

      // $FF: synthetic method
      private static UpdateScheme.Mode[] $values$() {
         return new UpdateScheme.Mode[]{NONE, UPDATE, ADD, REMOVE};
      }
   }
}
