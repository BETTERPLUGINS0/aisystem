package ac.grim.grimac.shaded.incendo.cloud.exception;

import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.STABLE
)
public final class AmbiguousNodeException extends IllegalStateException {
   private final CommandNode<?> parentNode;
   private final CommandNode<?> ambiguousNode;
   private final List<CommandNode<?>> children;

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public AmbiguousNodeException(@Nullable final CommandNode<?> parentNode, @NonNull final CommandNode<?> ambiguousNode, @NonNull final List<CommandNode<?>> children) {
      this.parentNode = parentNode;
      this.ambiguousNode = ambiguousNode;
      this.children = children;
   }

   @Nullable
   public CommandNode<?> parentNode() {
      return this.parentNode;
   }

   @NonNull
   public CommandNode<?> ambiguousNode() {
      return this.ambiguousNode;
   }

   @NonNull
   public List<CommandNode<?>> children() {
      return Collections.unmodifiableList(this.children);
   }

   public String getMessage() {
      StringBuilder stringBuilder = (new StringBuilder("Ambiguous Node: ")).append(this.ambiguousNode.component().name()).append(" cannot be added as a child to ").append(this.parentNode == null ? "<root>" : this.parentNode.component().name()).append(" (All children: ");
      Iterator childIterator = this.children.iterator();

      while(childIterator.hasNext()) {
         stringBuilder.append(((CommandNode)childIterator.next()).component().name());
         if (childIterator.hasNext()) {
            stringBuilder.append(", ");
         }
      }

      return stringBuilder.append(")").toString();
   }
}
