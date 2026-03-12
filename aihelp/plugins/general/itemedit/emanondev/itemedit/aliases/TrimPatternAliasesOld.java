package emanondev.itemedit.aliases;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import org.bukkit.inventory.meta.trim.TrimPattern;

public class TrimPatternAliasesOld extends AliasSet<TrimPattern> implements TrimPatternAliases {
   private final HashSet<TrimPattern> values = new HashSet();

   public TrimPatternAliasesOld() {
      super("trim_pattern");
      this.registerValue(TrimPattern.COAST);
      this.registerValue(TrimPattern.DUNE);
      this.registerValue(TrimPattern.EYE);
      this.registerValue(TrimPattern.RIB);
      this.registerValue(TrimPattern.HOST);
      this.registerValue(TrimPattern.RAISER);
      this.registerValue(TrimPattern.SENTRY);
      this.registerValue(TrimPattern.SHAPER);
      this.registerValue(TrimPattern.SILENCE);
      this.registerValue(TrimPattern.SPIRE);
      this.registerValue(TrimPattern.SNOUT);
      this.registerValue(TrimPattern.TIDE);
      this.registerValue(TrimPattern.VEX);
      this.registerValue(TrimPattern.WARD);
      this.registerValue(TrimPattern.WILD);
      this.registerValue(TrimPattern.WAYFINDER);
   }

   public String getName(TrimPattern value) {
      return value.getKey().toString();
   }

   public void registerValue(TrimPattern pattern) {
      this.values.add(pattern);
   }

   public Collection<TrimPattern> getValues() {
      return Collections.unmodifiableCollection(this.values);
   }
}
