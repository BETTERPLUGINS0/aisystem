package emanondev.itemedit.aliases;

import emanondev.itemedit.utility.ItemUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.bukkit.block.banner.PatternType;

public class BannerPatternAliasesOld extends AliasSet<PatternType> implements BannerPatternAliases {
   private final HashSet<PatternType> values = new HashSet();

   public BannerPatternAliasesOld() {
      super("banner_pattern");
      PatternType[] var1 = ItemUtils.getPatternTypesFiltered();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PatternType type = var1[var3];
         this.registerValue(type);
      }

   }

   public String getName(PatternType value) {
      return "" + value;
   }

   public void registerValue(PatternType pattern) {
      this.values.add(pattern);
   }

   public Collection<PatternType> getValues() {
      return Collections.unmodifiableCollection(this.values);
   }
}
