package ac.grim.grimac.shaded.incendo.cloud.exception;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.permission.Permission;
import ac.grim.grimac.shaded.incendo.cloud.permission.PermissionResult;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public class NoPermissionException extends CommandParseException {
   private final PermissionResult result;

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public NoPermissionException(@NonNull final PermissionResult permissionResult, @NonNull final Object commandSender, @NonNull final List<CommandComponent<?>> currentChain) {
      super(commandSender, currentChain);
      if (permissionResult.allowed()) {
         throw new IllegalArgumentException("Provided permission result was one that succeeded instead of failed");
      } else {
         this.result = permissionResult;
      }
   }

   public final String getMessage() {
      return String.format("Missing permission '%s'", this.missingPermission());
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public Permission missingPermission() {
      return this.result.permission();
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public PermissionResult permissionResult() {
      return this.result;
   }

   public final synchronized Throwable fillInStackTrace() {
      return this;
   }

   public final synchronized Throwable initCause(final Throwable cause) {
      return this;
   }
}
