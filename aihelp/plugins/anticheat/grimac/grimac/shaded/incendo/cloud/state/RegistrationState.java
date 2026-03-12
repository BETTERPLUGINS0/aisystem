package ac.grim.grimac.shaded.incendo.cloud.state;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;

@API(
   status = Status.STABLE
)
public enum RegistrationState implements State {
   BEFORE_REGISTRATION,
   REGISTERING,
   AFTER_REGISTRATION;

   // $FF: synthetic method
   private static RegistrationState[] $values() {
      return new RegistrationState[]{BEFORE_REGISTRATION, REGISTERING, AFTER_REGISTRATION};
   }
}
