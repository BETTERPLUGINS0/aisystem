package ac.grim.grimac.shaded.incendo.cloud.brigadier.node;

import ac.grim.grimac.shaded.incendo.cloud.brigadier.permission.BrigadierPermissionChecker;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.CommandNode;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.brigadier.*"}
)
public interface BrigadierNodeFactory<C, S, N extends CommandNode<S>> {
   @NonNull
   N createNode(@NonNull String label, @NonNull ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode<C> cloudCommand, @NonNull Command<S> executor, @NonNull BrigadierPermissionChecker<C> permissionChecker);

   @NonNull
   N createNode(@NonNull String label, @NonNull ac.grim.grimac.shaded.incendo.cloud.Command<C> cloudCommand, @NonNull Command<S> executor, @NonNull BrigadierPermissionChecker<C> permissionChecker);

   @NonNull
   N createNode(@NonNull String label, @NonNull ac.grim.grimac.shaded.incendo.cloud.Command<C> cloudCommand, @NonNull Command<S> executor);
}
