package emanondev.itemtag.activity.target;

import emanondev.itemtag.ItemTag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TargetTargetType extends TargetType {
   public TargetTargetType() {
      super("target");
   }

   @NotNull
   public TargetType.Target read(@Nullable String info) {
      return new TargetType.Target(info);
   }

   @NotNull
   protected List<Object> defaultGetTargets(@Nullable String info, @NotNull HashMap<String, TargetType.Target> baseTargets) {
      ArrayList<Object> values = new ArrayList();
      double radius = 32.0D;
      EnumSet<EntityType> valid = EnumSet.allOf(EntityType.class);
      TargetType.Target targets = null;
      if (info != null) {
         String target = TargetManager.extractTarget(info);
         if (target != null) {
            info = info.replace(target, "");
            if (info.startsWith(" ")) {
               info = info.substring(1);
            }

            targets = ItemTag.get().getTargetManager().read(target, baseTargets);
         }
      }

      if (targets == null) {
         targets = this.getFirstAvailable(baseTargets, Arrays.asList("entity", "player"));
      }

      if (targets == null) {
         throw new IllegalArgumentException();
      } else {
         if (info != null && !info.isEmpty()) {
            String[] arguments = info.split(" ");
            if (arguments.length > 0) {
               radius = Double.parseDouble(arguments[0]);
            }

            if (arguments.length > 1) {
               valid = EnumSet.complementOf(valid);
            }
         }

         Iterator var12 = targets.getTargets(baseTargets).iterator();

         while(var12.hasNext()) {
            Object target = var12.next();
            if (target instanceof Player) {
               ((Player)target).getWorld().rayTraceEntities(((Player)target).getEyeLocation(), ((Player)target).getEyeLocation().getDirection(), radius, (e) -> {
                  return e != target && valid.contains(e.getType());
               });
            } else if (target instanceof Mob) {
               values.add(((Mob)target).getTarget());
            }
         }

         return values;
      }
   }
}
