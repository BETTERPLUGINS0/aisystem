package ac.grim.grimac.shaded.incendo.cloud.syntax;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import ac.grim.grimac.shaded.incendo.cloud.parser.aggregate.AggregateParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.flag.CommandFlag;
import ac.grim.grimac.shaded.incendo.cloud.parser.flag.CommandFlagParser;
import ac.grim.grimac.shaded.incendo.cloud.permission.Permission;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public class StandardCommandSyntaxFormatter<C> implements CommandSyntaxFormatter<C> {
   private final CommandManager<C> manager;

   public StandardCommandSyntaxFormatter(@NonNull final CommandManager<C> manager) {
      this.manager = manager;
   }

   @NonNull
   public final String apply(@Nullable final C sender, @NonNull final List<CommandComponent<C>> commandComponents, @Nullable final CommandNode<C> node) {
      return this.apply(commandComponents, node, (n) -> {
         if (sender == null) {
            return true;
         } else {
            Map<Type, Permission> accessMap = (Map)n.nodeMeta().getOrDefault(CommandNode.META_KEY_ACCESS, Collections.emptyMap());
            Iterator var4 = accessMap.entrySet().iterator();

            Entry entry;
            do {
               if (!var4.hasNext()) {
                  return false;
               }

               entry = (Entry)var4.next();
            } while(!GenericTypeReflector.isSuperType((Type)entry.getKey(), sender.getClass()) || !this.manager.testPermission(sender, (Permission)entry.getValue()).allowed());

            return true;
         }
      });
   }

   @NonNull
   private String apply(@NonNull final List<CommandComponent<C>> commandComponents, @Nullable final CommandNode<C> node, @NonNull final Predicate<CommandNode<C>> filter) {
      StandardCommandSyntaxFormatter.FormattingInstance formattingInstance = this.createInstance();
      Iterator iterator = commandComponents.iterator();

      while(iterator.hasNext()) {
         CommandComponent<C> commandComponent = (CommandComponent)iterator.next();
         if (commandComponent.type() == CommandComponent.ComponentType.LITERAL) {
            formattingInstance.appendLiteral(commandComponent);
         } else if (commandComponent.parser() instanceof AggregateParser) {
            AggregateParser<?, ?> aggregateParser = (AggregateParser)commandComponent.parser();
            formattingInstance.appendAggregate(commandComponent, aggregateParser);
         } else if (commandComponent.type() == CommandComponent.ComponentType.FLAG) {
            formattingInstance.appendFlag((CommandFlagParser)commandComponent.parser());
         } else if (commandComponent.required()) {
            formattingInstance.appendRequired(commandComponent);
         } else {
            formattingInstance.appendOptional(commandComponent);
         }

         if (iterator.hasNext()) {
            formattingInstance.appendBlankSpace();
         }
      }

      for(CommandNode tail = node; tail != null && !tail.isLeaf() && filter.test(tail); tail = (CommandNode)tail.children().get(0)) {
         if (tail.children().size() > 1) {
            formattingInstance.appendBlankSpace();
            Iterator childIterator = tail.children().stream().filter(filter).iterator();

            while(childIterator.hasNext()) {
               CommandNode<C> child = (CommandNode)childIterator.next();
               if (child.component() != null) {
                  switch(child.component().type()) {
                  case LITERAL:
                     formattingInstance.appendName(child.component().name());
                     break;
                  case REQUIRED_VARIABLE:
                     formattingInstance.appendRequired(child.component());
                     break;
                  case OPTIONAL_VARIABLE:
                     formattingInstance.appendOptional(child.component());
                  }

                  if (childIterator.hasNext()) {
                     formattingInstance.appendPipe();
                  }
               }
            }

            return formattingInstance.toString();
         }

         if (!filter.test((CommandNode)tail.children().get(0))) {
            break;
         }

         CommandComponent<C> component = ((CommandNode)tail.children().get(0)).component();
         if (component.parser() instanceof AggregateParser) {
            AggregateParser<?, ?> aggregateParser = (AggregateParser)component.parser();
            formattingInstance.appendBlankSpace();
            formattingInstance.appendAggregate(component, aggregateParser);
         } else if (component.type() == CommandComponent.ComponentType.FLAG) {
            formattingInstance.appendBlankSpace();
            formattingInstance.appendFlag((CommandFlagParser)component.parser());
         } else if (component.type() == CommandComponent.ComponentType.LITERAL) {
            formattingInstance.appendBlankSpace();
            formattingInstance.appendLiteral(component);
         } else {
            formattingInstance.appendBlankSpace();
            if (component.required()) {
               formattingInstance.appendRequired(component);
            } else {
               formattingInstance.appendOptional(component);
            }
         }
      }

      return formattingInstance.toString();
   }

   @NonNull
   protected StandardCommandSyntaxFormatter.FormattingInstance createInstance() {
      return new StandardCommandSyntaxFormatter.FormattingInstance();
   }

   @API(
      status = Status.STABLE
   )
   public static class FormattingInstance {
      private final StringBuilder builder = new StringBuilder();

      protected FormattingInstance() {
      }

      @NonNull
      public final String toString() {
         return this.builder.toString();
      }

      public void appendLiteral(@NonNull final CommandComponent<?> literal) {
         this.appendName(literal.name());
      }

      @API(
         status = Status.STABLE
      )
      public void appendAggregate(@NonNull final CommandComponent<?> component, @NonNull final AggregateParser<?, ?> parser) {
         String prefix = component.required() ? this.requiredPrefix() : this.optionalPrefix();
         String suffix = component.required() ? this.requiredSuffix() : this.optionalSuffix();
         this.builder.append(prefix);
         Iterator innerComponents = parser.components().iterator();

         while(innerComponents.hasNext()) {
            CommandComponent<?> innerComponent = (CommandComponent)innerComponents.next();
            this.builder.append(prefix);
            this.appendName(innerComponent.name());
            this.builder.append(suffix);
            if (innerComponents.hasNext()) {
               this.builder.append(' ');
            }
         }

         this.builder.append(suffix);
      }

      public void appendFlag(@NonNull final CommandFlagParser<?> flagParser) {
         this.builder.append(this.optionalPrefix());
         Iterator flagIterator = flagParser.flags().iterator();

         while(flagIterator.hasNext()) {
            CommandFlag<?> flag = (CommandFlag)flagIterator.next();
            this.appendName(String.format("--%s", flag.name()));
            if (flag.commandComponent() != null) {
               this.builder.append(' ');
               this.builder.append(this.optionalPrefix());
               this.appendName(flag.commandComponent().name());
               this.builder.append(this.optionalSuffix());
            }

            if (flagIterator.hasNext()) {
               this.appendBlankSpace();
               this.appendPipe();
               this.appendBlankSpace();
            }
         }

         this.builder.append(this.optionalSuffix());
      }

      public void appendRequired(@NonNull final CommandComponent<?> argument) {
         this.builder.append(this.requiredPrefix());
         this.appendName(argument.name());
         this.builder.append(this.requiredSuffix());
      }

      public void appendOptional(@NonNull final CommandComponent<?> argument) {
         this.builder.append(this.optionalPrefix());
         this.appendName(argument.name());
         this.builder.append(this.optionalSuffix());
      }

      public void appendPipe() {
         this.builder.append("|");
      }

      public void appendName(@NonNull final String name) {
         this.builder.append(name);
      }

      @NonNull
      public String requiredPrefix() {
         return "<";
      }

      @NonNull
      public String requiredSuffix() {
         return ">";
      }

      @NonNull
      public String optionalPrefix() {
         return "[";
      }

      @NonNull
      public String optionalSuffix() {
         return "]";
      }

      public void appendBlankSpace() {
         this.builder.append(' ');
      }
   }
}
