package emanondev.itemedit.aliases;

import java.util.Collection;
import java.util.stream.Collectors;
import org.bukkit.MusicInstrument;
import org.bukkit.Registry;

public class GoatHornSoundAliases extends AliasSet<MusicInstrument> {
   public GoatHornSoundAliases() {
      super("goat_horn_sound");

      try {
         Registry.INSTRUMENT.stream().collect(Collectors.toList());
      } catch (Throwable var2) {
         MusicInstrument.values();
      }

   }

   public String getName(MusicInstrument type) {
      return type.getKey().getNamespace().equals("minecraft") ? type.getKey().getKey() : type.getKey().toString();
   }

   public Collection<MusicInstrument> getValues() {
      try {
         return (Collection)Registry.INSTRUMENT.stream().collect(Collectors.toList());
      } catch (Throwable var2) {
         return MusicInstrument.values();
      }
   }
}
