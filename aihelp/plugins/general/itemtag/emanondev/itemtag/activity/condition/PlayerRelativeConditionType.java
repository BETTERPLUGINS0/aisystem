package emanondev.itemtag.activity.condition;

import emanondev.itemtag.activity.ConditionType;
import java.util.Locale;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerRelativeConditionType extends BooleanValueConditionType {
   public PlayerRelativeConditionType(@NotNull String id, @Nullable Class<? extends Event> clazz) {
      super(id, (Class)null);
   }

   protected final boolean getCurrentValue(@NotNull Player player, @NotNull ItemStack item, @Nullable Event event) {
      throw new UnsupportedOperationException();
   }

   protected abstract boolean getCurrentValue(@NotNull Player var1, @NotNull ItemStack var2, @Nullable Event var3, boolean var4);

   @NotNull
   public PlayerRelativeConditionType.Condition read(@NotNull String info, boolean reversed) {
      return new PlayerRelativeConditionType.Condition(info, reversed);
   }

   private class Condition extends ConditionType.Condition {
      private final boolean playerRelative;

      public Condition(String param2, boolean param3) {
         super(info, reversed);
         String[] args = info.split(" ");
         if (args.length > 1) {
            String var5 = args[1].toLowerCase(Locale.ENGLISH);
            byte var6 = -1;
            switch(var5.hashCode()) {
            case 3569038:
               if (var5.equals("true")) {
                  var6 = 0;
               }
               break;
            case 97196323:
               if (var5.equals("false")) {
                  var6 = 1;
               }
            }

            switch(var6) {
            case 0:
               this.playerRelative = true;
               break;
            case 1:
               this.playerRelative = false;
               break;
            default:
               throw new IllegalArgumentException();
            }
         } else {
            this.playerRelative = false;
         }

      }

      protected boolean evaluateImpl(@NotNull Player player, @NotNull ItemStack item, Event event) {
         return PlayerRelativeConditionType.this.getCurrentValue(player, item, event, this.playerRelative);
      }
   }
}
