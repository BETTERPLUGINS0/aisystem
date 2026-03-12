package emanondev.itemedit.aliases;

import java.util.HashMap;
import java.util.Locale;
import org.bukkit.enchantments.Enchantment;

public class EnchAliasesOld extends EnchAliases {
   private final HashMap<String, String> enchNick = new HashMap();

   public EnchAliasesOld() {
      this.enchNick.put("PROTECTION_FIRE", "fire_protection");
      this.enchNick.put("DAMAGE_ALL", "sharpness");
      this.enchNick.put("ARROW_FIRE", "flame");
      this.enchNick.put("WATER_WORKER", "aqua_affinity");
      this.enchNick.put("ARROW_KNOCKBACK", "punch");
      this.enchNick.put("DEPTH_STRIDER", "depth_strider");
      this.enchNick.put("VANISHING_CURSE", "vanishing_curse");
      this.enchNick.put("DURABILITY", "unbreaking");
      this.enchNick.put("KNOCKBACK", "knockback");
      this.enchNick.put("LUCK", "luck_of_the_sea");
      this.enchNick.put("BINDING_CURSE", "binding_curse");
      this.enchNick.put("LOOT_BONUS_BLOCKS", "fortune");
      this.enchNick.put("PROTECTION_ENVIRONMENTAL", "protection");
      this.enchNick.put("DIG_SPEED", "efficiency");
      this.enchNick.put("MENDING", "mending");
      this.enchNick.put("FROST_WALKER", "frost_walker");
      this.enchNick.put("LURE", "lure");
      this.enchNick.put("LOOT_BONUS_MOBS", "looting");
      this.enchNick.put("PROTECTION_EXPLOSIONS", "blast_protection");
      this.enchNick.put("DAMAGE_UNDEAD", "smite");
      this.enchNick.put("FIRE_ASPECT", "fire_aspect");
      this.enchNick.put("SWEEPING_EDGE", "sweeping");
      this.enchNick.put("THORNS", "thorns");
      this.enchNick.put("DAMAGE_ARTHROPODS", "bane_of_arthropods");
      this.enchNick.put("OXYGEN", "respiration");
      this.enchNick.put("SILK_TOUCH", "silk_touch");
      this.enchNick.put("PROTECTION_PROJECTILE", "projectile_protection");
      this.enchNick.put("PROTECTION_FALL", "feather_falling");
      this.enchNick.put("ARROW_DAMAGE", "power");
      this.enchNick.put("ARROW_INFINITE", "infinity");
   }

   public String getName(Enchantment ench) {
      if (this.enchNick.containsKey(ench.getName())) {
         return (String)this.enchNick.get(ench.getName());
      } else {
         try {
            return ench.getName().toLowerCase(Locale.ENGLISH);
         } catch (Exception var3) {
            return ench.toString().toLowerCase(Locale.ENGLISH).replace(" ", "_");
         }
      }
   }
}
