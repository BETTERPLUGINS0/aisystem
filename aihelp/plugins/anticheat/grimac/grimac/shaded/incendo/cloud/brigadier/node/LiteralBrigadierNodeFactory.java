package ac.grim.grimac.shaded.incendo.cloud.brigadier.node;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.BrigadierSetting;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.ArgumentTypeFactory;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMapping;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.permission.BrigadierPermissionChecker;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.permission.BrigadierPermissionPredicate;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.BrigadierSuggestionFactory;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.CloudDelegatingSuggestionProvider;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.SuggestionsType;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.MappedArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.aggregate.AggregateParser;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionFactory;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE,
   since = "2.0.0"
)
public final class LiteralBrigadierNodeFactory<C, S> implements BrigadierNodeFactory<C, S, LiteralCommandNode<S>> {
   private final CloudBrigadierManager<C, S> cloudBrigadierManager;
   private final CommandManager<C> commandManager;
   private final BrigadierSuggestionFactory<C, S> brigadierSuggestionFactory;

   public LiteralBrigadierNodeFactory(@NonNull final CloudBrigadierManager<C, S> cloudBrigadierManager, @NonNull final CommandManager<C> commandManager, @NonNull final SuggestionFactory<C, ? extends TooltipSuggestion> suggestionFactory) {
      this.cloudBrigadierManager = cloudBrigadierManager;
      this.commandManager = commandManager;
      this.brigadierSuggestionFactory = new BrigadierSuggestionFactory(cloudBrigadierManager, commandManager, suggestionFactory);
   }

   @NonNull
   public LiteralCommandNode<S> createNode(@NonNull final String label, @NonNull final CommandNode<C> cloudCommand, @NonNull final Command<S> executor, @NonNull final BrigadierPermissionChecker<C> permissionChecker) {
      LiteralArgumentBuilder<S> literalArgumentBuilder = (LiteralArgumentBuilder)LiteralArgumentBuilder.literal(label).requires(this.requirement(cloudCommand, permissionChecker));
      this.updateExecutes(literalArgumentBuilder, cloudCommand, executor);
      LiteralCommandNode<S> constructedRoot = literalArgumentBuilder.build();
      Iterator var7 = cloudCommand.children().iterator();

      while(var7.hasNext()) {
         CommandNode<C> child = (CommandNode)var7.next();
         constructedRoot.addChild(this.constructCommandNode(child, permissionChecker, executor).build());
      }

      return constructedRoot;
   }

   @NonNull
   private BrigadierPermissionPredicate<C, S> requirement(@NonNull final CommandNode<C> cloudCommand, @NonNull final BrigadierPermissionChecker<C> permissionChecker) {
      return new BrigadierPermissionPredicate(this.cloudBrigadierManager.senderMapper(), permissionChecker, cloudCommand);
   }

   @NonNull
   public LiteralCommandNode<S> createNode(@NonNull final String label, @NonNull final ac.grim.grimac.shaded.incendo.cloud.Command<C> cloudCommand, @NonNull final Command<S> executor, @NonNull final BrigadierPermissionChecker<C> permissionChecker) {
      CommandNode<C> node = this.commandManager.commandTree().getNamedNode(cloudCommand.rootComponent().name());
      Objects.requireNonNull(node, "node");
      return this.createNode(label, node, executor, permissionChecker);
   }

   @NonNull
   public LiteralCommandNode<S> createNode(@NonNull final String label, @NonNull final ac.grim.grimac.shaded.incendo.cloud.Command<C> cloudCommand, @NonNull final Command<S> executor) {
      return this.createNode(label, cloudCommand, executor, (sender, permission) -> {
         return this.commandManager.testPermission(sender, permission).allowed();
      });
   }

   @NonNull
   private ArgumentBuilder<S, ?> constructCommandNode(@NonNull final CommandNode<C> root, @NonNull final BrigadierPermissionChecker<C> permissionChecker, @NonNull final Command<S> executor) {
      if (root.component().parser() instanceof AggregateParser) {
         AggregateParser<C, ?> aggregateParser = (AggregateParser)root.component().parser();
         return this.constructAggregateNode(aggregateParser, root, permissionChecker, executor);
      } else {
         ArgumentBuilder argumentBuilder;
         if (root.component().type() == CommandComponent.ComponentType.LITERAL) {
            argumentBuilder = this.createLiteralArgumentBuilder(root.component(), root, permissionChecker);
         } else {
            argumentBuilder = this.createVariableArgumentBuilder(root.component(), root, permissionChecker);
         }

         this.updateExecutes(argumentBuilder, root, executor);
         Iterator var5 = root.children().iterator();

         while(var5.hasNext()) {
            CommandNode<C> node = (CommandNode)var5.next();
            argumentBuilder.then(this.constructCommandNode(node, permissionChecker, executor));
         }

         return argumentBuilder;
      }
   }

   @NonNull
   private ArgumentBuilder<S, ?> createLiteralArgumentBuilder(@NonNull final CommandComponent<C> component, @NonNull final CommandNode<C> root, @NonNull final BrigadierPermissionChecker<C> permissionChecker) {
      return LiteralArgumentBuilder.literal(component.name()).requires(this.requirement(root, permissionChecker));
   }

   @NonNull
   private ArgumentBuilder<S, ?> createVariableArgumentBuilder(@NonNull final CommandComponent<C> component, @NonNull final CommandNode<C> root, @NonNull final BrigadierPermissionChecker<C> permissionChecker) {
      ArgumentMapping<S> argumentMapping = this.getArgument(component.valueType(), component.parser());
      Object provider;
      if (argumentMapping.suggestionsType() == SuggestionsType.CLOUD_SUGGESTIONS) {
         provider = new CloudDelegatingSuggestionProvider(this.brigadierSuggestionFactory, root);
      } else {
         provider = argumentMapping.suggestionProvider();
      }

      return RequiredArgumentBuilder.argument(component.name(), argumentMapping.argumentType()).suggests((SuggestionProvider)provider).requires(this.requirement(root, permissionChecker));
   }

   @NonNull
   private ArgumentBuilder<S, ?> constructAggregateNode(@NonNull final AggregateParser<C, ?> aggregateParser, @NonNull final CommandNode<C> root, @NonNull final BrigadierPermissionChecker<C> permissionChecker, @NonNull final Command<S> executor) {
      Iterator<CommandComponent<C>> components = aggregateParser.components().iterator();

      ArrayList argumentBuilders;
      ArgumentBuilder fragmentBuilder;
      for(argumentBuilders = new ArrayList(); components.hasNext(); argumentBuilders.add(fragmentBuilder)) {
         CommandComponent<C> component = (CommandComponent)components.next();
         fragmentBuilder = this.createVariableArgumentBuilder(component, root, permissionChecker);
         if (this.cloudBrigadierManager.settings().get(BrigadierSetting.FORCE_EXECUTABLE)) {
            fragmentBuilder.executes(executor);
         }
      }

      ArgumentBuilder<S, ?> tail = (ArgumentBuilder)argumentBuilders.get(argumentBuilders.size() - 1);
      Iterator var11 = root.children().iterator();

      while(var11.hasNext()) {
         CommandNode<C> node = (CommandNode)var11.next();
         tail.then(this.constructCommandNode(node, permissionChecker, executor));
      }

      this.updateExecutes(tail, root, executor);

      for(int i = argumentBuilders.size() - 1; i > 0; --i) {
         ((ArgumentBuilder)argumentBuilders.get(i - 1)).then((ArgumentBuilder)argumentBuilders.get(i));
      }

      return (ArgumentBuilder)argumentBuilders.get(0);
   }

   @NonNull
   private <K extends ArgumentParser<C, ?>> ArgumentMapping<S> getArgument(@NonNull final TypeToken<?> valueType, @NonNull final K argumentParser) {
      if (argumentParser instanceof MappedArgumentParser) {
         return this.getArgument(valueType, ((MappedArgumentParser)argumentParser).baseParser());
      } else {
         BrigadierMapping<C, K, S> mapping = this.cloudBrigadierManager.mappings().mapping(argumentParser.getClass());
         if (mapping != null && mapping.mapper() != null) {
            SuggestionProvider<S> suggestionProvider = mapping.makeSuggestionProvider(argumentParser);
            return suggestionProvider == BrigadierMapping.delegateSuggestions() ? ImmutableArgumentMapping.builder().argumentType((ArgumentType)mapping.mapper().apply(argumentParser)).suggestionsType(SuggestionsType.CLOUD_SUGGESTIONS).build() : ImmutableArgumentMapping.builder().argumentType((ArgumentType)mapping.mapper().apply(argumentParser)).suggestionProvider(suggestionProvider).build();
         } else {
            return this.getDefaultMapping(valueType);
         }
      }
   }

   @NonNull
   private ArgumentMapping<S> getDefaultMapping(@NonNull final TypeToken<?> type) {
      ArgumentTypeFactory<?> argumentTypeSupplier = (ArgumentTypeFactory)this.cloudBrigadierManager.defaultArgumentTypeFactories().get(GenericTypeReflector.erase(type.getType()));
      if (argumentTypeSupplier != null) {
         ArgumentType<?> argumentType = argumentTypeSupplier.create();
         if (argumentType != null) {
            return ImmutableArgumentMapping.builder().argumentType(argumentType).build();
         }
      }

      return ImmutableArgumentMapping.builder().argumentType(StringArgumentType.word()).suggestionsType(SuggestionsType.CLOUD_SUGGESTIONS).build();
   }

   private void updateExecutes(@NonNull final ArgumentBuilder<S, ?> builder, @NonNull final CommandNode<C> node, @NonNull final Command<S> executor) {
      if (this.cloudBrigadierManager.settings().get(BrigadierSetting.FORCE_EXECUTABLE) || node.isLeaf() || node.component().optional() || node.command() != null || node.children().stream().map(CommandNode::component).filter(Objects::nonNull).anyMatch(CommandComponent::optional)) {
         builder.executes(executor);
      }

   }
}
