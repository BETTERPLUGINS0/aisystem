package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser;

import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.parser.WrappedBrigadierParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.BlockPredicate;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CommandBuildContextSupplier;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.MinecraftArgumentTypes;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.RegistryReflection;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import com.google.common.base.Suppliers;
import com.mojang.brigadier.arguments.ArgumentType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class BlockPredicateParser<C> implements ArgumentParser.FutureArgumentParser<C, BlockPredicate> {
   private final ArgumentParser<C, BlockPredicate> parser = this.createParser();

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, BlockPredicate> blockPredicateParser() {
      return ParserDescriptor.of(new BlockPredicateParser(), (Class)BlockPredicate.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, BlockPredicate> blockPredicateComponent() {
      return CommandComponent.builder().parser(blockPredicateParser());
   }

   private ArgumentParser<C, BlockPredicate> createParser() {
      Supplier<ArgumentType<Object>> inst = () -> {
         Constructor ctr = ((Class)BlockPredicateParser.Reflection.ARGUMENT_BLOCK_PREDICATE_CLASS.get()).getDeclaredConstructors()[0];

         try {
            return ctr.getParameterCount() == 0 ? (ArgumentType)ctr.newInstance() : (ArgumentType)ctr.newInstance(CommandBuildContextSupplier.commandBuildContext());
         } catch (ReflectiveOperationException var2) {
            throw new RuntimeException("Failed to initialize BlockPredicate parser.", var2);
         }
      };
      return (new WrappedBrigadierParser(inst)).flatMapSuccess((ctx, result) -> {
         if (result instanceof Predicate) {
            return ArgumentParseResult.successFuture(new BlockPredicateParser.BlockPredicateImpl((Predicate)result));
         } else {
            Object commandSourceStack = ctx.get("_cloud_brigadier_native_sender");

            try {
               Object server = BlockPredicateParser.Reflection.GET_SERVER_METHOD.invoke(commandSourceStack);
               Object obj;
               if (BlockPredicateParser.Reflection.GET_TAG_REGISTRY_METHOD != null) {
                  obj = BlockPredicateParser.Reflection.GET_TAG_REGISTRY_METHOD.invoke(server);
               } else {
                  obj = RegistryReflection.builtInRegistryByName("block");
               }

               Objects.requireNonNull(BlockPredicateParser.Reflection.CREATE_PREDICATE_METHOD, "create on BlockPredicateArgument$Result");
               Predicate<Object> predicate = (Predicate)BlockPredicateParser.Reflection.CREATE_PREDICATE_METHOD.invoke(result, obj);
               return ArgumentParseResult.successFuture(new BlockPredicateParser.BlockPredicateImpl(predicate));
            } catch (ReflectiveOperationException var6) {
               throw new RuntimeException(var6);
            }
         }
      });
   }

   @NonNull
   public CompletableFuture<ArgumentParseResult<BlockPredicate>> parseFuture(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return this.parser.parseFuture(commandContext, commandInput);
   }

   @NonNull
   public SuggestionProvider<C> suggestionProvider() {
      return this.parser.suggestionProvider();
   }

   private static <C> void registerParserSupplier(@NonNull final CommandManager<C> commandManager) {
      commandManager.parserRegistry().registerParser(blockPredicateParser());
   }

   private static final class BlockPredicateImpl implements BlockPredicate {
      private final Predicate<Object> predicate;

      BlockPredicateImpl(@NonNull final Predicate<Object> predicate) {
         this.predicate = predicate;
      }

      private boolean testImpl(@NonNull final Block block, final boolean loadChunks) {
         try {
            Object blockInWorld = BlockPredicateParser.Reflection.SHAPE_DETECTOR_BLOCK_CTR.newInstance(BlockPredicateParser.Reflection.GET_HANDLE_METHOD.invoke(block.getWorld()), BlockPredicateParser.Reflection.BLOCK_POSITION_CTR.newInstance(block.getX(), block.getY(), block.getZ()), loadChunks);
            return this.predicate.test(blockInWorld);
         } catch (ReflectiveOperationException var4) {
            throw new RuntimeException(var4);
         }
      }

      public boolean test(@NonNull final Block block) {
         return this.testImpl(block, false);
      }

      @NonNull
      public BlockPredicate loadChunks() {
         return new BlockPredicate() {
            @NonNull
            public BlockPredicate loadChunks() {
               return this;
            }

            public boolean test(final Block block) {
               return BlockPredicateImpl.this.testImpl(block, true);
            }
         };
      }
   }

   private static final class Reflection {
      private static final Class<?> TAG_CONTAINER_CLASS;
      private static final Class<?> CRAFT_WORLD_CLASS;
      private static final Class<?> MINECRAFT_SERVER_CLASS;
      private static final Class<?> COMMAND_LISTENER_WRAPPER_CLASS;
      private static final Supplier<Class<?>> ARGUMENT_BLOCK_PREDICATE_CLASS;
      private static final Class<?> ARGUMENT_BLOCK_PREDICATE_RESULT_CLASS;
      private static final Class<?> SHAPE_DETECTOR_BLOCK_CLASS;
      private static final Class<?> LEVEL_READER_CLASS;
      private static final Class<?> BLOCK_POSITION_CLASS;
      private static final Constructor<?> BLOCK_POSITION_CTR;
      private static final Constructor<?> SHAPE_DETECTOR_BLOCK_CTR;
      private static final Method GET_HANDLE_METHOD;
      @Nullable
      private static final Method CREATE_PREDICATE_METHOD;
      private static final Method GET_SERVER_METHOD;
      @Nullable
      private static final Method GET_TAG_REGISTRY_METHOD;

      static {
         Class tagContainerClass;
         if (CraftBukkitReflection.MAJOR_REVISION > 12 && CraftBukkitReflection.MAJOR_REVISION < 16) {
            tagContainerClass = CraftBukkitReflection.needNMSClass("TagRegistry");
         } else {
            tagContainerClass = (Class)CraftBukkitReflection.firstNonNullOrThrow(() -> {
               return "tagContainerClass";
            }, CraftBukkitReflection.findNMSClass("ITagRegistry"), CraftBukkitReflection.findMCClass("tags.ITagRegistry"), CraftBukkitReflection.findMCClass("tags.TagContainer"), CraftBukkitReflection.findMCClass("core.IRegistry"), CraftBukkitReflection.findMCClass("core.Registry"));
         }

         TAG_CONTAINER_CLASS = tagContainerClass;
         CRAFT_WORLD_CLASS = CraftBukkitReflection.needOBCClass("CraftWorld");
         MINECRAFT_SERVER_CLASS = CraftBukkitReflection.needNMSClassOrElse("MinecraftServer", "net.minecraft.server.MinecraftServer");
         COMMAND_LISTENER_WRAPPER_CLASS = (Class)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Couldn't find CommandSourceStack class";
         }, CraftBukkitReflection.findNMSClass("CommandListenerWrapper"), CraftBukkitReflection.findMCClass("commands.CommandListenerWrapper"), CraftBukkitReflection.findMCClass("commands.CommandSourceStack"));
         ARGUMENT_BLOCK_PREDICATE_CLASS = Suppliers.memoize(() -> {
            return MinecraftArgumentTypes.getClassByKey(NamespacedKey.minecraft("block_predicate"));
         });
         ARGUMENT_BLOCK_PREDICATE_RESULT_CLASS = (Class)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Couldn't find BlockPredicateArgument$Result class";
         }, CraftBukkitReflection.findNMSClass("ArgumentBlockPredicate$b"), CraftBukkitReflection.findMCClass("commands.arguments.blocks.ArgumentBlockPredicate$b"), CraftBukkitReflection.findMCClass("commands.arguments.blocks.BlockPredicateArgument$Result"));
         SHAPE_DETECTOR_BLOCK_CLASS = (Class)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Couldn't find BlockInWorld class";
         }, CraftBukkitReflection.findNMSClass("ShapeDetectorBlock"), CraftBukkitReflection.findMCClass("world.level.block.state.pattern.ShapeDetectorBlock"), CraftBukkitReflection.findMCClass("world.level.block.state.pattern.BlockInWorld"));
         LEVEL_READER_CLASS = (Class)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Couldn't find LevelReader class";
         }, CraftBukkitReflection.findNMSClass("IWorldReader"), CraftBukkitReflection.findMCClass("world.level.IWorldReader"), CraftBukkitReflection.findMCClass("world.level.LevelReader"));
         BLOCK_POSITION_CLASS = (Class)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Couldn't find BlockPos class";
         }, CraftBukkitReflection.findNMSClass("BlockPosition"), CraftBukkitReflection.findMCClass("core.BlockPosition"), CraftBukkitReflection.findMCClass("core.BlockPos"));
         BLOCK_POSITION_CTR = CraftBukkitReflection.needConstructor(BLOCK_POSITION_CLASS, Integer.TYPE, Integer.TYPE, Integer.TYPE);
         SHAPE_DETECTOR_BLOCK_CTR = CraftBukkitReflection.needConstructor(SHAPE_DETECTOR_BLOCK_CLASS, LEVEL_READER_CLASS, BLOCK_POSITION_CLASS, Boolean.TYPE);
         GET_HANDLE_METHOD = CraftBukkitReflection.needMethod(CRAFT_WORLD_CLASS, "getHandle");
         CREATE_PREDICATE_METHOD = (Method)CraftBukkitReflection.firstNonNullOrNull(CraftBukkitReflection.findMethod(ARGUMENT_BLOCK_PREDICATE_RESULT_CLASS, "create", TAG_CONTAINER_CLASS), CraftBukkitReflection.findMethod(ARGUMENT_BLOCK_PREDICATE_RESULT_CLASS, "a", TAG_CONTAINER_CLASS));
         GET_SERVER_METHOD = (Method)CraftBukkitReflection.streamMethods(COMMAND_LISTENER_WRAPPER_CLASS).filter((it) -> {
            return it.getReturnType().equals(MINECRAFT_SERVER_CLASS) && it.getParameterCount() == 0;
         }).findFirst().orElseThrow(() -> {
            return new IllegalStateException("Could not find CommandSourceStack#getServer.");
         });
         GET_TAG_REGISTRY_METHOD = (Method)CraftBukkitReflection.firstNonNullOrNull(CraftBukkitReflection.findMethod(MINECRAFT_SERVER_CLASS, "getTagRegistry"), CraftBukkitReflection.findMethod(MINECRAFT_SERVER_CLASS, "getTags"), (Method)CraftBukkitReflection.streamMethods(MINECRAFT_SERVER_CLASS).filter((it) -> {
            return it.getReturnType().equals(TAG_CONTAINER_CLASS) && it.getParameterCount() == 0;
         }).findFirst().orElse((Object)null));
      }
   }
}
