package emanondev.itemtag.activity.condition;

import emanondev.itemtag.activity.ConditionType;
import java.util.HashSet;
import java.util.Locale;
import java.util.function.Predicate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class StringValueConditionType extends ConditionType {
   private final String separator;
   private final boolean caseInsensitive;
   private final Predicate<String> validator;

   public StringValueConditionType(@NotNull String id, @Nullable Class<? extends Event> clazz, boolean caseInsensitive) {
      this(id, clazz, ";", caseInsensitive, (Predicate)null);
   }

   public StringValueConditionType(@NotNull String id, @Nullable Class<? extends Event> clazz, @Nullable String separator, boolean caseInsensitive, Predicate<String> validator) {
      super(id, clazz);
      this.separator = separator;
      this.caseInsensitive = caseInsensitive;
      this.validator = validator;
   }

   @NotNull
   public ConditionType.Condition read(@NotNull String info, boolean reversed) {
      return new StringValueConditionType.StringCondition(info, reversed);
   }

   protected abstract String getCurrentValue(@NotNull Player var1, @NotNull ItemStack var2, Event var3);

   private class StringCondition extends ConditionType.Condition {
      private final HashSet<String> values = new HashSet();

      public StringCondition(@NotNull String param2, boolean param3) {
         super(info, reversed);
         String valuesRaw = info.split(" ")[0];
         if (StringValueConditionType.this.separator == null) {
            if (StringValueConditionType.this.validator != null && !StringValueConditionType.this.validator.test(valuesRaw)) {
               throw new IllegalArgumentException();
            }

            if (StringValueConditionType.this.caseInsensitive) {
               this.values.add(valuesRaw.toLowerCase(Locale.ENGLISH));
            } else {
               this.values.add(valuesRaw);
            }
         } else {
            String[] var5 = valuesRaw.split(StringValueConditionType.this.separator);
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String value = var5[var7];
               if (StringValueConditionType.this.validator != null && !StringValueConditionType.this.validator.test(valuesRaw)) {
                  throw new IllegalArgumentException();
               }

               if (StringValueConditionType.this.caseInsensitive) {
                  this.values.add(value.toLowerCase(Locale.ENGLISH));
               } else {
                  this.values.add(value);
               }
            }
         }

      }

      protected boolean evaluateImpl(@NotNull Player player, @NotNull ItemStack item, Event event) {
         return this.values.contains(StringValueConditionType.this.caseInsensitive ? StringValueConditionType.this.getCurrentValue(player, item, event).toLowerCase(Locale.ENGLISH) : StringValueConditionType.this.getCurrentValue(player, item, event));
      }
   }
}
