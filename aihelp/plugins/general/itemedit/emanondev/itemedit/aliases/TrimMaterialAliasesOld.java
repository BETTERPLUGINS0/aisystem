package emanondev.itemedit.aliases;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.bukkit.inventory.meta.trim.TrimMaterial;

public class TrimMaterialAliasesOld extends AliasSet<TrimMaterial> implements TrimMaterialAliases {
   private final HashSet<TrimMaterial> values = new HashSet();

   public TrimMaterialAliasesOld() {
      super("trim_material");
      this.registerValue(TrimMaterial.AMETHYST);
      this.registerValue(TrimMaterial.COPPER);
      this.registerValue(TrimMaterial.DIAMOND);
      this.registerValue(TrimMaterial.EMERALD);
      this.registerValue(TrimMaterial.GOLD);
      this.registerValue(TrimMaterial.IRON);
      this.registerValue(TrimMaterial.LAPIS);
      this.registerValue(TrimMaterial.NETHERITE);
      this.registerValue(TrimMaterial.QUARTZ);
      this.registerValue(TrimMaterial.REDSTONE);
   }

   public String getName(TrimMaterial value) {
      return value.getKey().toString();
   }

   public void registerValue(TrimMaterial pattern) {
      this.values.add(pattern);
   }

   public Collection<TrimMaterial> getValues() {
      return Collections.unmodifiableCollection(this.values);
   }
}
