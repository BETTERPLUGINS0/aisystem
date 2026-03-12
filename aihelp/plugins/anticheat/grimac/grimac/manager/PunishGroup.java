package ac.grim.grimac.manager;

import ac.grim.grimac.api.AbstractCheck;
import ac.grim.grimac.checks.Check;
import ac.grim.grimac.shaded.fastutil.longs.Long2ObjectMap;
import ac.grim.grimac.shaded.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.List;
import lombok.Generated;

class PunishGroup {
   public final List<AbstractCheck> checks;
   public final List<ParsedCommand> commands;
   public final Long2ObjectMap<Check> violations = new Long2ObjectOpenHashMap();
   public final int removeViolationsAfter;

   @Generated
   public PunishGroup(List<AbstractCheck> checks, List<ParsedCommand> commands, int removeViolationsAfter) {
      this.checks = checks;
      this.commands = commands;
      this.removeViolationsAfter = removeViolationsAfter;
   }
}
