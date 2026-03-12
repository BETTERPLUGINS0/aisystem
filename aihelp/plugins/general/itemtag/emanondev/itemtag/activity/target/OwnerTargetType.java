package emanondev.itemtag.activity.target;

import emanondev.itemtag.ItemTag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OwnerTargetType extends TargetType {
   public OwnerTargetType() {
      super("owner");
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
         targets = this.getFirstAvailable(baseTargets, Arrays.asList("entity", "projectile"));
      }

      if (targets == null) {
         throw new IllegalArgumentException();
      } else {
         Iterator var5 = targets.getTargets(baseTargets).iterator();

         while(true) {
            AnimalTamer owner;
            do {
               label48:
               do {
                  while(var5.hasNext()) {
                     Object target = var5.next();
                     if (target instanceof Tameable) {
                        owner = ((Tameable)target).getOwner();
                        continue label48;
                     }

                     if (target instanceof Projectile) {
                        ProjectileSource shooter = ((Projectile)target).getShooter();
                        if (shooter instanceof LivingEntity) {
                           if (shooter instanceof Player && !((Player)shooter).isOnline()) {
                              continue;
                           }

                           values.add(shooter);
                        }

                        if (shooter instanceof BlockProjectileSource) {
                           values.add(((BlockProjectileSource)shooter).getBlock());
                        }
                     }
                  }

                  return values;
               } while(!(owner instanceof Entity));
            } while(owner instanceof Player && !((Player)owner).isOnline());

            values.add(owner);
         }
      }
   }
}
