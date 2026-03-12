package ac.grim.grimac.shaded.incendo.cloud.setting;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(
   status = Status.STABLE
)
public enum ManagerSetting implements Setting {
   FORCE_SUGGESTION,
   @API(
      status = Status.STABLE
   )
   ALLOW_UNSAFE_REGISTRATION,
   @API(
      status = Status.STABLE
   )
   OVERRIDE_EXISTING_COMMANDS,
   @API(
      status = Status.EXPERIMENTAL
   )
   LIBERAL_FLAG_PARSING;

   // $FF: synthetic method
   private static ManagerSetting[] $values() {
      return new ManagerSetting[]{FORCE_SUGGESTION, ALLOW_UNSAFE_REGISTRATION, OVERRIDE_EXISTING_COMMANDS, LIBERAL_FLAG_PARSING};
   }
}
