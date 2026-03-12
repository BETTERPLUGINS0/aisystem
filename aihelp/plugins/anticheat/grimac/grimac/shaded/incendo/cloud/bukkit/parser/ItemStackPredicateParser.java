package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser;

import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.parser.WrappedBrigadierParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.ItemStackPredicate;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CommandBuildContextSupplier;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.MinecraftArgumentTypes;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import com.google.common.base.Suppliers;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.tree.CommandNode;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ItemStackPredicateParser<C> implements ArgumentParser.FutureArgumentParser<C, ItemStackPredicate> {
   private static final Class<?> CRAFT_ITEM_STACK_CLASS = CraftBukkitReflection.needOBCClass("inventory.CraftItemStack");
   private static final Supplier<Class<?>> ARGUMENT_ITEM_PREDICATE_CLASS = Suppliers.memoize(() -> {
      return MinecraftArgumentTypes.getClassByKey(NamespacedKey.minecraft("item_predicate"));
   });
   private static final Class<?> ARGUMENT_ITEM_PREDICATE_RESULT_CLASS = (Class)CraftBukkitReflection.firstNonNullOrNull(CraftBukkitReflection.findNMSClass("ArgumentItemPredicate$b"), CraftBukkitReflection.findMCClass("commands.arguments.item.ArgumentItemPredicate$b"), CraftBukkitReflection.findMCClass("commands.arguments.item.ItemPredicateArgument$Result"));
   @Nullable
   private static final Method CREATE_PREDICATE_METHOD;
   private static final Method AS_NMS_COPY_METHOD;
   private final ArgumentParser<C, ItemStackPredicate> parser = this.createParser();

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, ItemStackPredicate> itemStackPredicateParser() {
      return ParserDescriptor.of(new ItemStackPredicateParser(), (Class)ItemStackPredicate.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, ItemStackPredicate> itemStackPredicateComponent() {
      return CommandComponent.builder().parser(itemStackPredicateParser());
   }

   private ArgumentParser<C, ItemStackPredicate> createParser() {
      Supplier<ArgumentType<Object>> inst = () -> {
         Constructor ctr = ((Class)ARGUMENT_ITEM_PREDICATE_CLASS.get()).getDeclaredConstructors()[0];

         try {
            return ctr.getParameterCount() == 0 ? (ArgumentType)ctr.newInstance() : (ArgumentType)ctr.newInstance(CommandBuildContextSupplier.commandBuildContext());
         } catch (ReflectiveOperationException var2) {
            throw new RuntimeException("Failed to initialize ItemPredicate parser.", var2);
         }
      };
      return (new WrappedBrigadierParser(inst)).flatMapSuccess((ctx, result) -> {
         if (result instanceof Predicate) {
            return ArgumentParseResult.successFuture(new ItemStackPredicateParser.ItemStackPredicateImpl((Predicate)result));
         } else {
            Object commandSourceStack = ctx.get("_cloud_brigadier_native_sender");
            CommandContext<Object> dummy = createDummyContext(ctx, commandSourceStack);
            Objects.requireNonNull(CREATE_PREDICATE_METHOD, "ItemPredicateArgument$Result#create");

            try {
               Predicate<Object> predicate = (Predicate)CREATE_PREDICATE_METHOD.invoke(result, dummy);
               return ArgumentParseResult.successFuture(new ItemStackPredicateParser.ItemStackPredicateImpl(predicate));
            } catch (ReflectiveOperationException var5) {
               throw new RuntimeException(var5);
            }
         }
      });
   }

   @NonNull
   private static <C> CommandContext<Object> createDummyContext(@NonNull final ac.grim.grimac.shaded.incendo.cloud.context.CommandContext<C> ctx, @NonNull final Object commandSourceStack) {
      return new CommandContext(commandSourceStack, ctx.rawInput().input(), Collections.emptyMap(), (Command)null, (CommandNode)null, Collections.emptyList(), StringRange.at(0), (CommandContext)null, (RedirectModifier)null, false);
   }

   private static <C> void registerParserSupplier(@NonNull final CommandManager<C> commandManager) {
      commandManager.parserRegistry().registerParser(itemStackPredicateParser());
   }

   @NonNull
   public CompletableFuture<ArgumentParseResult<ItemStackPredicate>> parseFuture(@NonNull final ac.grim.grimac.shaded.incendo.cloud.context.CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return this.parser.parseFuture(commandContext, commandInput);
   }

   @NonNull
   public SuggestionProvider<C> suggestionProvider() {
      return this.parser.suggestionProvider();
   }

   static {
      CREATE_PREDICATE_METHOD = ARGUMENT_ITEM_PREDICATE_RESULT_CLASS == null ? null : (Method)CraftBukkitReflection.firstNonNullOrNull(CraftBukkitReflection.findMethod(ARGUMENT_ITEM_PREDICATE_RESULT_CLASS, "create", CommandContext.class), CraftBukkitReflection.findMethod(ARGUMENT_ITEM_PREDICATE_RESULT_CLASS, "a", CommandContext.class));
      AS_NMS_COPY_METHOD = CraftBukkitReflection.needMethod(CRAFT_ITEM_STACK_CLASS, "asNMSCopy", ItemStack.class);
   }

   private static final class ItemStackPredicateImpl implements ItemStackPredicate {
      private final Predicate<Object> predicate;

      ItemStackPredicateImpl(@NonNull final Predicate<Object> predicate) {
         this.predicate = predicate;
      }

      public boolean test(@NonNull final ItemStack itemStack) {
         try {
            return this.predicate.test(ItemStackPredicateParser.AS_NMS_COPY_METHOD.invoke((Object)null, itemStack));
         } catch (ReflectiveOperationException var3) {
            throw new RuntimeException(var3);
         }
      }
   }
}
