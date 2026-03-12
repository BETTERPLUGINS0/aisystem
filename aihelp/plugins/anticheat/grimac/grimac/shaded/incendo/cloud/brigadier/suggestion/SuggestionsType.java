package ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(
   status = Status.INTERNAL,
   since = "2.0.0"
)
public enum SuggestionsType {
   BRIGADIER_SUGGESTIONS,
   CLOUD_SUGGESTIONS;

   // $FF: synthetic method
   private static SuggestionsType[] $values() {
      return new SuggestionsType[]{BRIGADIER_SUGGESTIONS, CLOUD_SUGGESTIONS};
   }
}
