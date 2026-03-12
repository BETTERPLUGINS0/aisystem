package ac.grim.grimac.shaded.incendo.cloud.meta;

import ac.grim.grimac.shaded.incendo.cloud.key.CloudKeyContainer;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public abstract class CommandMeta implements CloudKeyContainer {
   @NonNull
   public static CommandMetaBuilder builder() {
      return new CommandMetaBuilder();
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static CommandMeta empty() {
      return builder().build();
   }

   @NonNull
   public final String toString() {
      return "";
   }
}
