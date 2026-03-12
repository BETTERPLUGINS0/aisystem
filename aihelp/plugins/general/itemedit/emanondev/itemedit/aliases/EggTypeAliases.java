package emanondev.itemedit.aliases;

import java.util.Collection;
import java.util.EnumSet;
import org.bukkit.entity.EntityType;

public class EggTypeAliases extends EnumAliasSet<EntityType> {
   public EggTypeAliases() {
      super("mob_type", EntityType.class);
   }

   public Collection<EntityType> getValues() {
      EnumSet<EntityType> set = EnumSet.noneOf(EntityType.class);
      EntityType[] var2 = EntityType.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EntityType type = var2[var4];
         if (type.isAlive() && type.isSpawnable()) {
            set.add(type);
         }
      }

      return set;
   }
}
