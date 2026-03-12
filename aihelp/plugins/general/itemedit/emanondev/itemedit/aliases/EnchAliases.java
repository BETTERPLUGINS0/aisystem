package emanondev.itemedit.aliases;

import java.util.Collection;
import java.util.HashSet;
import org.bukkit.enchantments.Enchantment;

public class EnchAliases extends AliasSet<Enchantment> {
   public EnchAliases() {
      super("enchant");
   }

   public String getName(Enchantment ench) {
      return ench.getKey().getKey();
   }

   public Collection<Enchantment> getValues() {
      HashSet<Enchantment> set = new HashSet();
      Enchantment[] var2 = Enchantment.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Enchantment ench = var2[var4];
         if (ench != null) {
            set.add(ench);
         }
      }

      return set;
   }
}
