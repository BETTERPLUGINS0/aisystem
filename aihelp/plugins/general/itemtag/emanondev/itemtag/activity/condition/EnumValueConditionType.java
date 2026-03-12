package emanondev.itemtag.activity.condition;

import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EnumValueConditionType<E extends Enum<E>> extends StringValueConditionType {
   public EnumValueConditionType(@NotNull String id, @Nullable Class<? extends Event> clazz, @NotNull Class<E> enumClass) {
      super(id, clazz, ";", true, (test) -> {
         try {
            Enum.valueOf(enumClass, test.toUpperCase(Locale.ENGLISH));
            return true;
         } catch (Exception var3) {
            return false;
         }
      });
   }

   protected abstract E getCurrentEnumValue(@NotNull Player var1, @NotNull ItemStack var2, Event var3);

   protected String getCurrentValue(@NotNull Player player, @NotNull ItemStack item, Event event) {
      return this.getCurrentEnumValue(player, item, event).toString();
   }
}
