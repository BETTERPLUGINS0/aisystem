package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.parser.WrappedBrigadierParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCommandContextKeys;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.MinecraftArgumentTypes;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import com.google.common.base.Suppliers;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class SelectorUtils {
   private SelectorUtils() {
   }

   @Nullable
   private static <C, T> ArgumentParser<C, T> createModernParser(final boolean single, final boolean playersOnly, final SelectorUtils.SelectorMapper<T> mapper) {
      if (CraftBukkitReflection.MAJOR_REVISION < 13) {
         return null;
      } else {
         WrappedBrigadierParser<C, Object> wrappedBrigParser = new WrappedBrigadierParser(() -> {
            return createEntityArgument(single, playersOnly);
         }, SelectorUtils.EntityArgumentParseFunction.INSTANCE);
         return new SelectorUtils.ModernSelectorParser(wrappedBrigParser, mapper);
      }
   }

   private static ArgumentType<Object> createEntityArgument(final boolean single, final boolean playersOnly) {
      Constructor<?> constructor = MinecraftArgumentTypes.getClassByKey(NamespacedKey.minecraft("entity")).getDeclaredConstructors()[0];
      constructor.setAccessible(true);

      try {
         return (ArgumentType)constructor.newInstance(single, playersOnly);
      } catch (ReflectiveOperationException var4) {
         throw new RuntimeException(var4);
      }
   }

   private static <X extends Throwable> RuntimeException rethrow(final Throwable t) throws X {
      throw t;
   }

   @FunctionalInterface
   interface SelectorMapper<T> {
      T mapResult(String input, SelectorUtils.EntitySelectorWrapper wrapper) throws Exception;
   }

   private static final class EntityArgumentParseFunction implements WrappedBrigadierParser.ParseFunction<Object> {
      static final SelectorUtils.EntityArgumentParseFunction INSTANCE = new SelectorUtils.EntityArgumentParseFunction();

      public Object apply(final ArgumentType<Object> type, final StringReader reader) throws CommandSyntaxException {
         Method specialParse = CraftBukkitReflection.findMethod(type.getClass(), "parse", StringReader.class, Boolean.TYPE);
         if (specialParse == null) {
            return type.parse(reader);
         } else {
            try {
               return specialParse.invoke(type, reader, true);
            } catch (InvocationTargetException var6) {
               Throwable cause = var6.getCause();
               if (cause instanceof CommandSyntaxException) {
                  throw (CommandSyntaxException)cause;
               } else {
                  throw new RuntimeException(var6);
               }
            } catch (ReflectiveOperationException var7) {
               throw new RuntimeException(var7);
            }
         }
      }
   }

   private static class ModernSelectorParser<C, T> implements ArgumentParser.FutureArgumentParser<C, T>, SuggestionProvider<C> {
      private final WrappedBrigadierParser<C, Object> wrappedBrigadierParser;
      private final SelectorUtils.SelectorMapper<T> mapper;

      ModernSelectorParser(final WrappedBrigadierParser<C, Object> wrapperBrigParser, final SelectorUtils.SelectorMapper<T> mapper) {
         this.wrappedBrigadierParser = wrapperBrigParser;
         this.mapper = mapper;
      }

      public CompletableFuture<ArgumentParseResult<T>> parseFuture(final CommandContext<C> commandContext, final CommandInput commandInput) {
         return CompletableFuture.supplyAsync(() -> {
            CommandInput originalCommandInput = commandInput.copy();
            ArgumentParseResult<Object> result = this.wrappedBrigadierParser.parse(commandContext, commandInput);
            if (result.failure().isPresent()) {
               return result;
            } else {
               String input = originalCommandInput.difference(commandInput);

               try {
                  return ArgumentParseResult.success(this.mapper.mapResult(input, new SelectorUtils.EntitySelectorWrapper(commandContext, result.parsedValue().get())));
               } catch (CommandSyntaxException var7) {
                  return ArgumentParseResult.failure(var7);
               } catch (Exception var8) {
                  throw SelectorUtils.rethrow(var8);
               }
            }
         }, (Executor)commandContext.get(BukkitCommandContextKeys.SENDER_SCHEDULER_EXECUTOR));
      }

      public CompletableFuture<? extends Iterable<? extends Suggestion>> suggestionsFuture(final CommandContext<C> commandContext, final CommandInput input) {
         Object commandSourceStack = commandContext.get("_cloud_brigadier_native_sender");
         Field bypassField = CraftBukkitReflection.findField(commandSourceStack.getClass(), "bypassSelectorPermissions");

         try {
            boolean prev = false;

            CompletableFuture var6;
            try {
               if (bypassField != null) {
                  prev = bypassField.getBoolean(commandSourceStack);
                  bypassField.setBoolean(commandSourceStack, true);
               }

               var6 = CompletableFuture.completedFuture((Iterable)this.wrappedBrigadierParser.suggestionProvider().suggestionsFuture(commandContext, input).join());
            } finally {
               if (bypassField != null) {
                  bypassField.setBoolean(commandSourceStack, prev);
               }

            }

            return var6;
         } catch (ReflectiveOperationException var11) {
            throw new RuntimeException(var11);
         }
      }
   }

   static final class EntitySelectorWrapper {
      @MonotonicNonNull
      private static volatile SelectorUtils.EntitySelectorWrapper.Methods methods;
      private final CommandContext<?> commandContext;
      private final Object selector;

      EntitySelectorWrapper(final CommandContext<?> commandContext, final Object selector) {
         this.commandContext = commandContext;
         this.selector = selector;
      }

      private static SelectorUtils.EntitySelectorWrapper.Methods methods(final CommandContext<?> commandContext, final Object selector) {
         if (methods == null) {
            Class var2 = SelectorUtils.EntitySelectorWrapper.Methods.class;
            synchronized(SelectorUtils.EntitySelectorWrapper.Methods.class) {
               if (methods == null) {
                  methods = new SelectorUtils.EntitySelectorWrapper.Methods(commandContext, selector);
               }
            }
         }

         return methods;
      }

      private SelectorUtils.EntitySelectorWrapper.Methods methods() {
         return methods(this.commandContext, this.selector);
      }

      Entity singleEntity() {
         return (Entity)reflectiveOperation(() -> {
            return (Entity)this.methods().getBukkitEntity.invoke(this.methods().entity.invoke(this.selector, this.commandContext.get("_cloud_brigadier_native_sender")));
         });
      }

      Player singlePlayer() {
         return (Player)reflectiveOperation(() -> {
            return (Player)this.methods().getBukkitEntity.invoke(this.methods().player.invoke(this.selector, this.commandContext.get("_cloud_brigadier_native_sender")));
         });
      }

      List<Entity> entities() {
         List<Object> internalEntities = (List)reflectiveOperation(() -> {
            return (List)this.methods().entities.invoke(this.selector, this.commandContext.get("_cloud_brigadier_native_sender"));
         });
         return (List)internalEntities.stream().map((o) -> {
            return (Entity)reflectiveOperation(() -> {
               return (Entity)this.methods().getBukkitEntity.invoke(o);
            });
         }).collect(Collectors.toList());
      }

      List<Player> players() {
         List<Object> serverPlayers = (List)reflectiveOperation(() -> {
            return (List)this.methods().players.invoke(this.selector, this.commandContext.get("_cloud_brigadier_native_sender"));
         });
         return (List)serverPlayers.stream().map((o) -> {
            return (Player)reflectiveOperation(() -> {
               return (Player)this.methods().getBukkitEntity.invoke(o);
            });
         }).collect(Collectors.toList());
      }

      private static <T> T reflectiveOperation(final SelectorUtils.EntitySelectorWrapper.ReflectiveOperation<T> op) {
         try {
            return op.run();
         } catch (InvocationTargetException var2) {
            if (var2.getCause() instanceof CommandSyntaxException) {
               throw SelectorUtils.rethrow(var2.getCause());
            } else {
               throw new RuntimeException(var2);
            }
         } catch (ReflectiveOperationException var3) {
            throw new RuntimeException(var3);
         }
      }

      private static final class Methods {
         @MonotonicNonNull
         private Method getBukkitEntity;
         @MonotonicNonNull
         private Method entity;
         @MonotonicNonNull
         private Method player;
         @MonotonicNonNull
         private Method entities;
         @MonotonicNonNull
         private Method players;

         Methods(final CommandContext<?> commandContext, final Object selector) {
            Object nativeSender = commandContext.get("_cloud_brigadier_native_sender");
            Class<?> nativeSenderClass = nativeSender.getClass();
            Method[] var5 = selector.getClass().getDeclaredMethods();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Method method = var5[var7];
               if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(nativeSenderClass) && Modifier.isPublic(method.getModifiers())) {
                  Class<?> returnType = method.getReturnType();
                  if (List.class.isAssignableFrom(returnType)) {
                     ParameterizedType stringListType = (ParameterizedType)method.getGenericReturnType();

                     Type listType;
                     for(listType = stringListType.getActualTypeArguments()[0]; listType instanceof WildcardType; listType = ((WildcardType)listType).getUpperBounds()[0]) {
                     }

                     Class<?> clazz = listType instanceof Class ? (Class)listType : GenericTypeReflector.erase(listType);
                     Method getBukkitEntity = findGetBukkitEntityMethod(clazz);
                     if (getBukkitEntity != null) {
                        Class<?> bukkitType = getBukkitEntity.getReturnType();
                        if (Player.class.isAssignableFrom(bukkitType)) {
                           if (this.players != null) {
                              throw new IllegalStateException();
                           }

                           this.players = method;
                        } else {
                           if (this.entities != null) {
                              throw new IllegalStateException();
                           }

                           this.entities = method;
                        }
                     }
                  } else if (returnType != Void.TYPE) {
                     Method getBukkitEntity = findGetBukkitEntityMethod(returnType);
                     if (getBukkitEntity != null) {
                        Class<?> bukkitType = getBukkitEntity.getReturnType();
                        if (Player.class.isAssignableFrom(bukkitType)) {
                           if (this.player != null) {
                              throw new IllegalStateException();
                           }

                           this.player = method;
                        } else {
                           if (this.entity != null || this.getBukkitEntity != null) {
                              throw new IllegalStateException();
                           }

                           this.entity = method;
                           this.getBukkitEntity = getBukkitEntity;
                        }
                     }
                  }
               }
            }

            Objects.requireNonNull(this.getBukkitEntity, "Failed to locate getBukkitEntity method");
            Objects.requireNonNull(this.player, "Failed to locate findPlayer method");
            Objects.requireNonNull(this.entity, "Failed to locate findEntity method");
            Objects.requireNonNull(this.players, "Failed to locate findPlayers method");
            Objects.requireNonNull(this.entities, "Failed to locate findEntities method");
         }

         private static Method findGetBukkitEntityMethod(final Class<?> returnType) {
            Method getBukkitEntity;
            try {
               getBukkitEntity = returnType.getDeclaredMethod("getBukkitEntity");
            } catch (ReflectiveOperationException var5) {
               try {
                  getBukkitEntity = returnType.getMethod("getBukkitEntity");
               } catch (ReflectiveOperationException var4) {
                  getBukkitEntity = null;
               }
            }

            return getBukkitEntity;
         }
      }

      @FunctionalInterface
      interface ReflectiveOperation<T> {
         T run() throws ReflectiveOperationException;
      }
   }

   abstract static class PlayerSelectorParser<C, T> extends SelectorUtils.SelectorParser<C, T> {
      protected PlayerSelectorParser(final boolean single) {
         super(single, true);
      }

      @NonNull
      protected Iterable<Suggestion> legacySuggestions(final CommandContext<C> commandContext, final CommandInput input) {
         List<Suggestion> suggestions = new ArrayList();
         Iterator var4 = Bukkit.getOnlinePlayers().iterator();

         while(true) {
            Player player;
            CommandSender bukkit;
            do {
               if (!var4.hasNext()) {
                  return suggestions;
               }

               player = (Player)var4.next();
               bukkit = (CommandSender)commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
            } while(bukkit instanceof Player && !((Player)bukkit).canSee(player));

            suggestions.add(Suggestion.suggestion(player.getName()));
         }
      }
   }

   abstract static class EntitySelectorParser<C, T> extends SelectorUtils.SelectorParser<C, T> {
      protected EntitySelectorParser(final boolean single) {
         super(single, false);
      }
   }

   private abstract static class SelectorParser<C, T> implements ArgumentParser.FutureArgumentParser<C, T>, SelectorUtils.SelectorMapper<T>, SuggestionProvider<C> {
      protected static final Supplier<Object> NO_PLAYERS_EXCEPTION_TYPE = Suppliers.memoize(() -> {
         return findExceptionType("argument.entity.notfound.player");
      });
      protected static final Supplier<Object> NO_ENTITIES_EXCEPTION_TYPE = Suppliers.memoize(() -> {
         return findExceptionType("argument.entity.notfound.entity");
      });
      @Nullable
      private final ArgumentParser<C, T> modernParser;

      protected SelectorParser(final boolean single, final boolean playersOnly) {
         this.modernParser = SelectorUtils.createModernParser(single, playersOnly, this);
      }

      protected CompletableFuture<ArgumentParseResult<T>> legacyParse(final CommandContext<C> commandContext, final CommandInput commandInput) {
         return ArgumentParseResult.failureFuture(new SelectorUnsupportedException(commandContext, this.getClass()));
      }

      @NonNull
      protected Iterable<Suggestion> legacySuggestions(final CommandContext<C> commandContext, final CommandInput input) {
         return Collections.emptyList();
      }

      public CompletableFuture<ArgumentParseResult<T>> parseFuture(final CommandContext<C> commandContext, final CommandInput commandInput) {
         return this.modernParser != null ? this.modernParser.parseFuture(commandContext, commandInput) : this.legacyParse(commandContext, commandInput);
      }

      public CompletableFuture<? extends Iterable<? extends Suggestion>> suggestionsFuture(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
         return this.modernParser != null ? this.modernParser.suggestionProvider().suggestionsFuture(commandContext, input) : CompletableFuture.completedFuture(this.legacySuggestions(commandContext, input));
      }

      private static Object findExceptionType(final String type) {
         Field[] fields = MinecraftArgumentTypes.getClassByKey(NamespacedKey.minecraft("entity")).getDeclaredFields();
         return Arrays.stream(fields).filter((field) -> {
            return Modifier.isStatic(field.getModifiers()) && field.getType() == SimpleCommandExceptionType.class;
         }).map((field) -> {
            try {
               Object fieldValue = field.get((Object)null);
               if (fieldValue == null) {
                  return null;
               } else {
                  Field messageField = SimpleCommandExceptionType.class.getDeclaredField("message");
                  messageField.setAccessible(true);
                  return messageField.get(fieldValue).toString().contains(type) ? fieldValue : null;
               }
            } catch (ReflectiveOperationException var4) {
               throw new RuntimeException(var4);
            }
         }).filter(Objects::nonNull).findFirst().orElseThrow(() -> {
            return new IllegalArgumentException("Could not find exception type '" + type + "'");
         });
      }

      protected static final class Thrower {
         private final Object type;

         Thrower(final Object simpleCommandExceptionType) {
            this.type = simpleCommandExceptionType;
         }

         void throwIt() {
            throw SelectorUtils.rethrow(((SimpleCommandExceptionType)this.type).create());
         }
      }
   }
}
