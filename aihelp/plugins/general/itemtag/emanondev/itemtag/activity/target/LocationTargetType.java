package emanondev.itemtag.activity.target;

import emanondev.itemtag.ItemTag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationTargetType extends TargetType {
   public LocationTargetType() {
      super("location");
   }

   public TargetType.Target read(@Nullable String info) {
      return new TargetType.Target(info);
   }

   @NotNull
   protected List<Object> defaultGetTargets(@Nullable String info, @NotNull HashMap<String, TargetType.Target> baseTargets) {
      ArrayList<Object> values = new ArrayList();
      TargetType.Target targets;
      if (info != null) {
         targets = ItemTag.get().getTargetManager().read(info, baseTargets);
      } else {
         targets = this.getFirstAvailable(baseTargets, Arrays.asList(this.getId(), "entity", "projectile", "block", "player"));
      }

      if (targets == null) {
         throw new IllegalArgumentException();
      } else {
         Iterator var5 = targets.getTargets(baseTargets).iterator();

         while(var5.hasNext()) {
            Object target = var5.next();
            if (target instanceof Location) {
               values.add(target);
            } else if (target instanceof Entity) {
               values.add(((Entity)target).getLocation());
            } else if (target instanceof Block) {
               values.add(((Block)target).getLocation().add(0.5D, 0.0D, 0.5D));
            }
         }

         return values;
      }
   }
}
