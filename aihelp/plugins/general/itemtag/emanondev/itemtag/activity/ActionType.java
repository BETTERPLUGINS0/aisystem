package emanondev.itemtag.activity;

import java.util.regex.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ActionType {
   private final String id;
   private final Class<? extends Event> clazz;

   public ActionType(@NotNull String id) {
      this(id, (Class)null);
   }

   public ActionType(@NotNull String id, @Nullable Class<? extends Event> clazz) {
      if (!Pattern.compile("[a-z][_a-z0-9]*").matcher(id).matches()) {
         throw new IllegalArgumentException();
      } else {
         this.id = id;
         this.clazz = clazz;
      }
   }

   @NotNull
   public abstract ActionType.Action read(@NotNull String var1);

   @NotNull
   public final String getId() {
      return this.id;
   }

   public abstract class Action {
      private final String info;

      public Action(@NotNull String param2) {
         this.info = info;
      }

      @NotNull
      public final String getInfo() {
         return this.info;
      }

      @NotNull
      public final String getId() {
         return ActionType.this.getId();
      }

      @NotNull
      public String toString() {
         return this.getId() + (this.getInfo().isEmpty() ? "" : " " + this.getInfo());
      }

      public abstract boolean execute(@NotNull Player var1, @NotNull ItemStack var2, @Nullable Event var3);

      public boolean isAssignable(@Nullable Event event) {
         if (ActionType.this.clazz == null) {
            return true;
         } else {
            return event != null && ActionType.this.clazz.isAssignableFrom(event.getClass());
         }
      }
   }
}
