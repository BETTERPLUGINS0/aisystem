package emanondev.itemtag.activity;

import java.util.regex.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ConditionType {
   private final String id;
   private final Class<? extends Event> clazz;

   public ConditionType(@NotNull String id) {
      this(id, (Class)null);
   }

   public ConditionType(@NotNull String id, @Nullable Class<? extends Event> clazz) {
      if (!Pattern.compile("[a-z][_a-z0-9]*").matcher(id).matches()) {
         throw new IllegalArgumentException();
      } else {
         this.id = id;
         this.clazz = clazz;
      }
   }

   @NotNull
   public final ConditionType.Condition read(@NotNull String info) {
      return this.read(info, false);
   }

   @NotNull
   public abstract ConditionType.Condition read(@NotNull String var1, boolean var2);

   @NotNull
   public final String getId() {
      return this.id;
   }

   public abstract class Condition {
      private final String info;
      private final boolean reversed;

      public Condition(@NotNull String param2, boolean param3) {
         this.info = info;
         this.reversed = reversed;
      }

      @NotNull
      public final String getInfo() {
         return this.info;
      }

      @NotNull
      public final String getId() {
         return ConditionType.this.getId();
      }

      @NotNull
      public String toString() {
         return (this.reversed ? "" : "!") + this.getId() + (this.getInfo().isEmpty() ? "" : " " + this.getInfo());
      }

      public boolean evaluate(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event) {
         return this.reversed != this.evaluateImpl(player, item, event);
      }

      protected abstract boolean evaluateImpl(@NotNull Player var1, @NotNull ItemStack var2, @Nullable Event var3);

      public boolean isCompatible(@Nullable Event event) {
         if (ConditionType.this.clazz == null) {
            return true;
         } else {
            return event != null && ConditionType.this.clazz.isAssignableFrom(event.getClass());
         }
      }
   }
}
