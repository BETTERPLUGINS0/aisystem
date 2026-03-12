package ac.grim.grimac.shaded.incendo.cloud.context;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.caption.Caption;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionFormatter;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionRegistry;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionVariable;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import ac.grim.grimac.shaded.incendo.cloud.key.MutableCloudKeyContainer;
import ac.grim.grimac.shaded.incendo.cloud.parser.flag.FlagContext;
import ac.grim.grimac.shaded.incendo.cloud.permission.Permission;
import ac.grim.grimac.shaded.incendo.cloud.util.annotation.AnnotationAccessor;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public class CommandContext<C> implements MutableCloudKeyContainer {
   private final List<ParsingContext<C>> parsingContexts;
   private final FlagContext flagContext;
   private final Map<CloudKey<?>, Object> internalStorage;
   private final C commandSender;
   private final boolean suggestions;
   private final CaptionRegistry<C> captionRegistry;
   private final CommandManager<C> commandManager;
   @MonotonicNonNull
   private volatile Command<C> currentCommand;

   @API(
      status = Status.STABLE
   )
   public CommandContext(@NonNull final C commandSender, @NonNull final CommandManager<C> commandManager) {
      this(false, commandSender, commandManager);
   }

   @API(
      status = Status.STABLE
   )
   public CommandContext(final boolean suggestions, @NonNull final C commandSender, @NonNull final CommandManager<C> commandManager) {
      this.parsingContexts = new LinkedList();
      this.flagContext = FlagContext.create();
      this.internalStorage = new HashMap();
      this.currentCommand = null;
      this.commandSender = commandSender;
      this.suggestions = suggestions;
      this.commandManager = commandManager;
      this.captionRegistry = commandManager.captionRegistry();
   }

   @NonNull
   public String formatCaption(@NonNull final Caption caption, @NonNull final CaptionVariable... variables) {
      return (String)this.formatCaption(this.commandManager.captionFormatter(), caption, variables);
   }

   @NonNull
   public String formatCaption(@NonNull final Caption caption, @NonNull final List<CaptionVariable> variables) {
      return (String)this.formatCaption(this.commandManager.captionFormatter(), caption, variables);
   }

   @NonNull
   public <T> T formatCaption(@NonNull final CaptionFormatter<C, T> formatter, @NonNull final Caption caption, @NonNull final CaptionVariable... variables) {
      return formatter.formatCaption(caption, this.commandSender, this.captionRegistry.caption(caption, this.commandSender), variables);
   }

   @NonNull
   public <T> T formatCaption(@NonNull final CaptionFormatter<C, T> formatter, @NonNull final Caption caption, @NonNull final List<CaptionVariable> variables) {
      return formatter.formatCaption(caption, this.commandSender, this.captionRegistry.caption(caption, this.commandSender), variables);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public C sender() {
      return this.commandSender;
   }

   @API(
      status = Status.STABLE
   )
   public boolean hasPermission(@NonNull final Permission permission) {
      return this.commandManager.testPermission(this.commandSender, permission).allowed();
   }

   @API(
      status = Status.STABLE
   )
   public boolean hasPermission(@NonNull final String permission) {
      return this.commandManager.hasPermission(this.commandSender, permission);
   }

   public boolean isSuggestions() {
      return this.suggestions;
   }

   public <T> void store(@NonNull final String key, final T value) {
      this.internalStorage.put(CloudKey.of(key), value);
   }

   public <T> void store(@NonNull final CloudKey<T> key, final T value) {
      this.internalStorage.put(key, value);
   }

   public boolean contains(@NonNull final CloudKey<?> key) {
      return this.internalStorage.containsKey(key);
   }

   @NonNull
   public <T> Optional<T> optional(@NonNull final CloudKey<T> key) {
      Object value = this.internalStorage.get(key);
      return value != null ? Optional.of(value) : Optional.empty();
   }

   @NonNull
   public <T> Optional<T> optional(@NonNull final String key) {
      Object value = this.internalStorage.get(CloudKey.of(key));
      return value != null ? Optional.of(value) : Optional.empty();
   }

   public void remove(@NonNull final CloudKey<?> key) {
      this.internalStorage.remove(key);
   }

   public <T> T computeIfAbsent(@NonNull final CloudKey<T> key, @NonNull final Function<CloudKey<T>, T> defaultFunction) {
      T castedValue = this.internalStorage.computeIfAbsent(key, (k) -> {
         return defaultFunction.apply(k);
      });
      return castedValue;
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public CommandInput rawInput() {
      return ((CommandInput)this.getOrDefault("__raw_input__", CommandInput.empty())).copy();
   }

   @API(
      status = Status.MAINTAINED
   )
   @NonNull
   public ParsingContext<C> createParsingContext(@NonNull final CommandComponent<C> component) {
      ParsingContext<C> parsingContext = new ParsingContext(component);
      this.parsingContexts.add(parsingContext);
      return parsingContext;
   }

   @API(
      status = Status.MAINTAINED
   )
   @NonNull
   public ParsingContext<C> parsingContext(@NonNull final CommandComponent<C> component) {
      return (ParsingContext)this.parsingContexts.stream().filter((context) -> {
         return context.component().equals(component);
      }).findFirst().orElseThrow(NoSuchElementException::new);
   }

   @API(
      status = Status.MAINTAINED
   )
   @NonNull
   public ParsingContext<C> parsingContext(final int position) {
      return (ParsingContext)this.parsingContexts.get(position);
   }

   @API(
      status = Status.MAINTAINED
   )
   @NonNull
   public ParsingContext<C> parsingContext(final String name) {
      return (ParsingContext)this.parsingContexts.stream().filter((context) -> {
         return context.component().name().equals(name);
      }).findFirst().orElseThrow(NoSuchElementException::new);
   }

   @API(
      status = Status.MAINTAINED
   )
   @NonNull
   public List<ParsingContext<C>> parsingContexts() {
      return Collections.unmodifiableList(this.parsingContexts);
   }

   @NonNull
   public FlagContext flags() {
      return this.flagContext;
   }

   @NonNull
   public Command<C> command() {
      if (this.currentCommand == null) {
         throw new IllegalStateException("The current command is only available once a command has been parsed. Mainly from execution handlers and post processors.");
      } else {
         return this.currentCommand;
      }
   }

   @API(
      status = Status.INTERNAL
   )
   public void command(@NonNull final Command<C> command) {
      this.currentCommand = (Command)Objects.requireNonNull(command, "command");
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public <T> Optional<T> inject(@NonNull final Class<T> clazz) {
      if (this.commandManager == null) {
         throw new UnsupportedOperationException("Cannot retrieve injectable values from a command context that is not associated with a command manager");
      } else {
         return this.commandManager.parameterInjectorRegistry().getInjectable(clazz, this, AnnotationAccessor.empty());
      }
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public <T> Optional<T> inject(@NonNull final TypeToken<T> type) {
      if (this.commandManager == null) {
         throw new UnsupportedOperationException("Cannot retrieve injectable values from a command context that is not associated with a command manager");
      } else {
         return this.commandManager.parameterInjectorRegistry().getInjectable(type, this, AnnotationAccessor.empty());
      }
   }

   @NonNull
   public final Map<CloudKey<?>, ? extends Object> all() {
      return Collections.unmodifiableMap(this.internalStorage);
   }
}
