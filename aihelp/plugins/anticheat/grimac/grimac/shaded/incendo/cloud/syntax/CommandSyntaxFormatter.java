package ac.grim.grimac.shaded.incendo.cloud.syntax;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@FunctionalInterface
@API(
   status = Status.STABLE
)
public interface CommandSyntaxFormatter<C> {
   @NonNull
   String apply(@Nullable C sender, @NonNull List<CommandComponent<C>> commandComponents, @Nullable CommandNode<C> node);
}
