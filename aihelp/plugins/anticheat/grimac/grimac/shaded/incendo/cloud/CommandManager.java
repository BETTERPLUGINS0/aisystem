package ac.grim.grimac.shaded.incendo.cloud;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.caption.Caption;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionFormatter;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionRegistry;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionVariable;
import ac.grim.grimac.shaded.incendo.cloud.caption.StandardCaptionsProvider;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContextFactory;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.context.StandardCommandContextFactory;
import ac.grim.grimac.shaded.incendo.cloud.exception.handling.ExceptionController;
import ac.grim.grimac.shaded.incendo.cloud.execution.CommandExecutor;
import ac.grim.grimac.shaded.incendo.cloud.execution.ExecutionCoordinator;
import ac.grim.grimac.shaded.incendo.cloud.execution.postprocessor.AcceptingCommandPostprocessor;
import ac.grim.grimac.shaded.incendo.cloud.execution.postprocessor.CommandPostprocessingContext;
import ac.grim.grimac.shaded.incendo.cloud.execution.postprocessor.CommandPostprocessor;
import ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor.AcceptingCommandPreprocessor;
import ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor.CommandPreprocessor;
import ac.grim.grimac.shaded.incendo.cloud.help.CommandPredicate;
import ac.grim.grimac.shaded.incendo.cloud.help.HelpHandler;
import ac.grim.grimac.shaded.incendo.cloud.help.HelpHandlerFactory;
import ac.grim.grimac.shaded.incendo.cloud.injection.ParameterInjectorRegistry;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandNode;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandRegistrationHandler;
import ac.grim.grimac.shaded.incendo.cloud.meta.CommandMeta;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserRegistry;
import ac.grim.grimac.shaded.incendo.cloud.parser.StandardParserRegistry;
import ac.grim.grimac.shaded.incendo.cloud.parser.flag.CommandFlag;
import ac.grim.grimac.shaded.incendo.cloud.permission.AndPermission;
import ac.grim.grimac.shaded.incendo.cloud.permission.OrPermission;
import ac.grim.grimac.shaded.incendo.cloud.permission.Permission;
import ac.grim.grimac.shaded.incendo.cloud.permission.PermissionResult;
import ac.grim.grimac.shaded.incendo.cloud.permission.PredicatePermission;
import ac.grim.grimac.shaded.incendo.cloud.services.ServicePipeline;
import ac.grim.grimac.shaded.incendo.cloud.services.State;
import ac.grim.grimac.shaded.incendo.cloud.setting.Configurable;
import ac.grim.grimac.shaded.incendo.cloud.setting.ManagerSetting;
import ac.grim.grimac.shaded.incendo.cloud.state.RegistrationState;
import ac.grim.grimac.shaded.incendo.cloud.state.Stateful;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.DelegatingSuggestionFactory;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.FilteringSuggestionProcessor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionFactory;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionMapper;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProcessor;
import ac.grim.grimac.shaded.incendo.cloud.syntax.CommandSyntaxFormatter;
import ac.grim.grimac.shaded.incendo.cloud.syntax.StandardCommandSyntaxFormatter;
import ac.grim.grimac.shaded.incendo.cloud.type.tuple.Pair;
import ac.grim.grimac.shaded.incendo.cloud.type.tuple.Triplet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;

@API(
   status = Status.STABLE
)
public abstract class CommandManager<C> implements Stateful<RegistrationState>, CommandBuilderSource<C> {
   private final Configurable<ManagerSetting> settings = Configurable.enumConfigurable(ManagerSetting.class);
   private final ServicePipeline servicePipeline = ServicePipeline.builder().build();
   private final ParserRegistry<C> parserRegistry = new StandardParserRegistry();
   private final Collection<Command<C>> commands = new LinkedList();
   private final ParameterInjectorRegistry<C> parameterInjectorRegistry = new ParameterInjectorRegistry();
   private final CommandTree<C> commandTree;
   private final SuggestionFactory<C, ? extends Suggestion> suggestionFactory;
   private final Set<CloudCapability> capabilities = new HashSet();
   private final ExceptionController<C> exceptionController = new ExceptionController();
   private final CommandExecutor<C> commandExecutor;
   private CaptionFormatter<C, String> captionVariableReplacementHandler = CaptionFormatter.placeholderReplacing();
   private CommandSyntaxFormatter<C> commandSyntaxFormatter = new StandardCommandSyntaxFormatter(this);
   private SuggestionProcessor<C> suggestionProcessor = new FilteringSuggestionProcessor();
   private CommandRegistrationHandler<C> commandRegistrationHandler;
   private CaptionRegistry<C> captionRegistry;
   private HelpHandlerFactory<C> helpHandlerFactory = HelpHandlerFactory.standard(this);
   private SuggestionMapper<? extends Suggestion> mapper = SuggestionMapper.identity();
   private final AtomicReference<RegistrationState> state;

   protected CommandManager(@NonNull final ExecutionCoordinator<C> executionCoordinator, @NonNull final CommandRegistrationHandler<C> commandRegistrationHandler) {
      this.state = new AtomicReference(RegistrationState.BEFORE_REGISTRATION);
      CommandContextFactory<C> commandContextFactory = new StandardCommandContextFactory(this);
      this.commandTree = CommandTree.newTree(this);
      this.commandRegistrationHandler = commandRegistrationHandler;
      this.suggestionFactory = new DelegatingSuggestionFactory(this, this.commandTree, commandContextFactory, executionCoordinator, (suggestion) -> {
         return this.mapper.map(suggestion);
      });
      this.commandExecutor = new StandardCommandExecutor(this, executionCoordinator, commandContextFactory);
      this.servicePipeline.registerServiceType(new TypeToken<CommandPreprocessor<C>>() {
      }, new AcceptingCommandPreprocessor());
      this.servicePipeline.registerServiceType(new TypeToken<CommandPostprocessor<C>>() {
      }, new AcceptingCommandPostprocessor());
      this.captionRegistry = CaptionRegistry.captionRegistry();
      this.captionRegistry.registerProvider(new StandardCaptionsProvider());
      this.parameterInjectorRegistry().registerInjector(CommandContext.class, (context, annotationAccessor) -> {
         return context;
      });
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public CommandExecutor<C> commandExecutor() {
      return this.commandExecutor;
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public SuggestionFactory<C, ? extends Suggestion> suggestionFactory() {
      return this.suggestionFactory;
   }

   @NonNull
   public SuggestionMapper<? extends Suggestion> suggestionMapper() {
      return this.mapper;
   }

   public void appendSuggestionMapper(@NonNull final SuggestionMapper<? extends Suggestion> mapper) {
      this.suggestionMapper(this.suggestionMapper().then(mapper));
   }

   public void suggestionMapper(@NonNull final SuggestionMapper<? extends Suggestion> mapper) {
      this.mapper = (SuggestionMapper)Objects.requireNonNull(mapper, "mapper");
   }

   @This
   @NonNull
   public CommandManager<C> command(@NonNull final Command<? extends C> command) {
      if (!this.transitionIfPossible(RegistrationState.BEFORE_REGISTRATION, RegistrationState.REGISTERING) && !this.isCommandRegistrationAllowed()) {
         throw new IllegalStateException("Unable to register commands because the manager is no longer in a registration state. Your platform may allow unsafe registrations by enabling the appropriate manager setting.");
      } else {
         this.commandTree.insertCommand(command);
         this.commands.add(command);
         return this;
      }
   }

   @API(
      status = Status.STABLE
   )
   @This
   @NonNull
   public CommandManager<C> command(@NonNull final CommandFactory<C> commandFactory) {
      commandFactory.createCommands(this).forEach(this::command);
      return this;
   }

   @NonNull
   public CommandManager<C> command(@NonNull final Command.Builder<? extends C> command) {
      return this.command(command.manager(this).build());
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public CaptionFormatter<C, String> captionFormatter() {
      return this.captionVariableReplacementHandler;
   }

   @API(
      status = Status.STABLE
   )
   public void captionFormatter(@NonNull final CaptionFormatter<C, String> captionFormatter) {
      this.captionVariableReplacementHandler = captionFormatter;
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public CommandSyntaxFormatter<C> commandSyntaxFormatter() {
      return this.commandSyntaxFormatter;
   }

   @API(
      status = Status.STABLE
   )
   public void commandSyntaxFormatter(@NonNull final CommandSyntaxFormatter<C> commandSyntaxFormatter) {
      this.commandSyntaxFormatter = commandSyntaxFormatter;
   }

   @NonNull
   public CommandRegistrationHandler<C> commandRegistrationHandler() {
      return this.commandRegistrationHandler;
   }

   @API(
      status = Status.STABLE
   )
   protected final void commandRegistrationHandler(@NonNull final CommandRegistrationHandler<C> commandRegistrationHandler) {
      this.requireState(RegistrationState.BEFORE_REGISTRATION);
      this.commandRegistrationHandler = commandRegistrationHandler;
   }

   @API(
      status = Status.STABLE
   )
   protected final void registerCapability(@NonNull final CloudCapability capability) {
      this.capabilities.add(capability);
   }

   @API(
      status = Status.STABLE
   )
   public boolean hasCapability(@NonNull final CloudCapability capability) {
      return this.capabilities.contains(capability);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public Collection<CloudCapability> capabilities() {
      return Collections.unmodifiableSet(new HashSet(this.capabilities));
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public PermissionResult testPermission(@NonNull final C sender, @NonNull final Permission permission) {
      if (permission instanceof PredicatePermission) {
         return ((PredicatePermission)permission).testPermission(sender);
      } else {
         Iterator var3;
         Permission innerPermission;
         PermissionResult result;
         if (permission instanceof OrPermission) {
            var3 = permission.permissions().iterator();

            do {
               if (!var3.hasNext()) {
                  return PermissionResult.denied(permission);
               }

               innerPermission = (Permission)var3.next();
               result = this.testPermission(sender, innerPermission);
            } while(!result.allowed());

            return result;
         } else if (permission instanceof AndPermission) {
            var3 = permission.permissions().iterator();

            do {
               if (!var3.hasNext()) {
                  return PermissionResult.allowed(permission);
               }

               innerPermission = (Permission)var3.next();
               result = this.testPermission(sender, innerPermission);
            } while(result.allowed());

            return result;
         } else {
            return PermissionResult.of(permission.isEmpty() || this.hasPermission(sender, permission.permissionString()), permission);
         }
      }
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public final CaptionRegistry<C> captionRegistry() {
      return this.captionRegistry;
   }

   @API(
      status = Status.STABLE
   )
   public final void captionRegistry(@NonNull final CaptionRegistry<C> captionRegistry) {
      this.captionRegistry = captionRegistry;
   }

   public abstract boolean hasPermission(@NonNull C sender, @NonNull String permission);

   @API(
      status = Status.EXPERIMENTAL
   )
   public void deleteRootCommand(@NonNull final String rootCommand) throws CloudCapability.CloudCapabilityMissingException {
      if (!this.hasCapability(CloudCapability.StandardCapabilities.ROOT_COMMAND_DELETION)) {
         throw new CloudCapability.CloudCapabilityMissingException(CloudCapability.StandardCapabilities.ROOT_COMMAND_DELETION);
      } else {
         CommandNode<C> node = this.commandTree.getNamedNode(rootCommand);
         if (node != null && node.component() != null) {
            this.commandRegistrationHandler.unregisterRootCommand(node.component());
            CommandTree var10000 = this.commandTree;
            Collection var10003 = this.commands;
            Objects.requireNonNull(var10003);
            var10000.deleteRecursively(node, true, var10003::remove);
         }
      }
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public Collection<String> rootCommands() {
      return (Collection)this.commandTree.rootNodes().stream().map(CommandNode::component).filter(Objects::nonNull).filter((component) -> {
         return component.type() == CommandComponent.ComponentType.LITERAL;
      }).map(CommandComponent::name).collect(Collectors.toList());
   }

   @NonNull
   public final Command.Builder<C> decorateBuilder(@NonNull final Command.Builder<C> builder) {
      return builder.manager(this);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public <T> CommandComponent.Builder<C, T> componentBuilder(@NonNull final Class<T> type, @NonNull final String name) {
      return CommandComponent.ofType(type, name).commandManager(this);
   }

   @NonNull
   public CommandFlag.Builder<C, Void> flagBuilder(@NonNull final String name) {
      return CommandFlag.builder(name);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public CommandTree<C> commandTree() {
      return this.commandTree;
   }

   @NonNull
   public CommandMeta createDefaultCommandMeta() {
      return CommandMeta.empty();
   }

   public void registerCommandPreProcessor(@NonNull final CommandPreprocessor<C> processor) {
      this.servicePipeline.registerServiceImplementation((TypeToken)(new TypeToken<CommandPreprocessor<C>>() {
      }), processor, Collections.emptyList());
   }

   public void registerCommandPostProcessor(@NonNull final CommandPostprocessor<C> processor) {
      this.servicePipeline.registerServiceImplementation((TypeToken)(new TypeToken<CommandPostprocessor<C>>() {
      }), processor, Collections.emptyList());
   }

   @API(
      status = Status.STABLE
   )
   public State preprocessContext(@NonNull final CommandContext<C> context, @NonNull final CommandInput commandInput) {
      this.servicePipeline.pump(CommandPreprocessingContext.of(context, commandInput)).through(new TypeToken<CommandPreprocessor<C>>() {
      }).complete();
      return ((String)context.optional("__COMMAND_PRE_PROCESSED__").orElse("")).isEmpty() ? State.REJECTED : State.ACCEPTED;
   }

   public State postprocessContext(@NonNull final CommandContext<C> context, @NonNull final Command<C> command) {
      this.servicePipeline.pump(CommandPostprocessingContext.of(context, command)).through(new TypeToken<CommandPostprocessor<C>>() {
      }).complete();
      return ((String)context.optional("__COMMAND_POST_PROCESSED__").orElse("")).isEmpty() ? State.REJECTED : State.ACCEPTED;
   }

   @NonNull
   public SuggestionProcessor<C> suggestionProcessor() {
      return this.suggestionProcessor;
   }

   public void suggestionProcessor(@NonNull final SuggestionProcessor<C> suggestionProcessor) {
      this.suggestionProcessor = suggestionProcessor;
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public ParserRegistry<C> parserRegistry() {
      return this.parserRegistry;
   }

   @NonNull
   public final ParameterInjectorRegistry<C> parameterInjectorRegistry() {
      return this.parameterInjectorRegistry;
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public final ExceptionController<C> exceptionController() {
      return this.exceptionController;
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public final Collection<Command<C>> commands() {
      return Collections.unmodifiableCollection(this.commands);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public final HelpHandler<C> createHelpHandler() {
      return this.helpHandlerFactory.createHelpHandler((cmd) -> {
         return true;
      });
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public final HelpHandler<C> createHelpHandler(@NonNull final CommandPredicate<C> filter) {
      return this.helpHandlerFactory.createHelpHandler(filter);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public final HelpHandlerFactory<C> helpHandlerFactory() {
      return this.helpHandlerFactory;
   }

   @API(
      status = Status.STABLE
   )
   public final void helpHandlerFactory(@NonNull final HelpHandlerFactory<C> helpHandlerFactory) {
      this.helpHandlerFactory = helpHandlerFactory;
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public Configurable<ManagerSetting> settings() {
      return this.settings;
   }

   @NonNull
   public final RegistrationState state() {
      return (RegistrationState)this.state.get();
   }

   public final boolean transitionIfPossible(@NonNull final RegistrationState in, @NonNull final RegistrationState out) {
      return this.state.compareAndSet(in, out) || this.state.get() == out;
   }

   @API(
      status = Status.STABLE
   )
   protected final void lockRegistration() {
      if (this.state() == RegistrationState.BEFORE_REGISTRATION) {
         this.transitionOrThrow(RegistrationState.BEFORE_REGISTRATION, RegistrationState.AFTER_REGISTRATION);
      } else {
         this.transitionOrThrow(RegistrationState.REGISTERING, RegistrationState.AFTER_REGISTRATION);
      }
   }

   @API(
      status = Status.STABLE
   )
   public boolean isCommandRegistrationAllowed() {
      return this.settings().get(ManagerSetting.ALLOW_UNSAFE_REGISTRATION) || this.state.get() != RegistrationState.AFTER_REGISTRATION;
   }

   protected void registerDefaultExceptionHandlers(@NonNull final Consumer<Triplet<CommandContext<C>, Caption, List<CaptionVariable>>> messageSender, @NonNull final Consumer<Pair<String, Throwable>> logger) {
      DefaultExceptionHandlers<C> defaultExceptionHandlers = new DefaultExceptionHandlers(messageSender, logger, this.exceptionController);
      defaultExceptionHandlers.register();
   }
}
