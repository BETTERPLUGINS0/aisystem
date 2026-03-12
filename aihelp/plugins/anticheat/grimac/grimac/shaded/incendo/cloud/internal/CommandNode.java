package ac.grim.grimac.shaded.incendo.cloud.internal;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import ac.grim.grimac.shaded.incendo.cloud.key.SimpleMutableCloudKeyContainer;
import ac.grim.grimac.shaded.incendo.cloud.permission.Permission;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class CommandNode<C> {
   public static final CloudKey<Set<Type>> META_KEY_SENDER_TYPES = CloudKey.cloudKey("senderTypes", new TypeToken<Set<Type>>() {
   });
   public static final CloudKey<Map<Type, Permission>> META_KEY_ACCESS = CloudKey.cloudKey("access", new TypeToken<Map<Type, Permission>>() {
   });
   private final SimpleMutableCloudKeyContainer nodeMeta = new SimpleMutableCloudKeyContainer(new HashMap());
   private final List<CommandNode<C>> children = new LinkedList();
   private final CommandComponent<C> component;
   private CommandNode<C> parent;
   private Command<C> command;

   public CommandNode(@Nullable final CommandComponent<C> component) {
      this.component = component;
   }

   @NonNull
   public List<CommandNode<C>> children() {
      return Collections.unmodifiableList(this.children);
   }

   @NonNull
   public CommandNode<C> addChild(@NonNull final CommandComponent<C> component) {
      CommandNode<C> node = new CommandNode(component);
      this.children.add(node);
      return node;
   }

   @Nullable
   public CommandNode<C> getChild(@NonNull final CommandComponent<C> component) {
      Iterator var2 = this.children.iterator();

      CommandNode child;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         child = (CommandNode)var2.next();
      } while(!component.equals(child.component()));

      return child;
   }

   public boolean removeChild(@NonNull final CommandNode<C> child) {
      return this.children.remove(child);
   }

   public boolean isLeaf() {
      return this.children.isEmpty();
   }

   @NonNull
   public SimpleMutableCloudKeyContainer nodeMeta() {
      return this.nodeMeta;
   }

   @MonotonicNonNull
   public CommandComponent<C> component() {
      return this.component;
   }

   @MonotonicNonNull
   public Command<C> command() {
      return this.command;
   }

   public void command(@NonNull final Command<C> command) {
      if (this.command != null) {
         throw new IllegalStateException("Cannot replace owning command");
      } else {
         this.command = command;
      }
   }

   @Nullable
   public CommandNode<C> parent() {
      return this.parent;
   }

   public void parent(@Nullable final CommandNode<C> parent) {
      this.parent = parent;
   }

   public void sortChildren() {
      this.children.sort(Comparator.comparing(CommandNode::component));
   }

   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         CommandNode<?> node = (CommandNode)o;
         return Objects.equals(this.component(), node.component());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.component()});
   }

   public String toString() {
      return "Node{value=" + this.component + '}';
   }
}
