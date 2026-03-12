package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser;

import ac.grim.grimac.shaded.incendo.cloud.brigadier.parser.WrappedBrigadierParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.ProtoItemStack;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CommandBuildContextSupplier;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.MinecraftArgumentTypes;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import com.google.common.base.Suppliers;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ItemStackParser<C> implements ArgumentParser.FutureArgumentParser<C, ProtoItemStack> {
   private final ArgumentParser<C, ProtoItemStack> parser;

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, ProtoItemStack> itemStackParser() {
      return ParserDescriptor.of(new ItemStackParser(), (Class)ProtoItemStack.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, ProtoItemStack> itemStackComponent() {
      return CommandComponent.builder().parser(itemStackParser());
   }

   @Nullable
   private static Class<?> findItemInputClass() {
      Class<?>[] classes = new Class[]{CraftBukkitReflection.findNMSClass("ArgumentPredicateItemStack"), CraftBukkitReflection.findMCClass("commands.arguments.item.ArgumentPredicateItemStack"), CraftBukkitReflection.findMCClass("commands.arguments.item.ItemInput")};
      Class[] var1 = classes;
      int var2 = classes.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Class<?> clazz = var1[var3];
         if (clazz != null) {
            return clazz;
         }
      }

      return null;
   }

   public ItemStackParser() {
      if (findItemInputClass() != null) {
         this.parser = new ItemStackParser.ModernParser();
      } else {
         this.parser = new ItemStackParser.LegacyParser();
      }

   }

   @NonNull
   public final CompletableFuture<ArgumentParseResult<ProtoItemStack>> parseFuture(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return this.parser.parseFuture(commandContext, commandInput);
   }

   @NonNull
   public final SuggestionProvider<C> suggestionProvider() {
      return this.parser.suggestionProvider();
   }

   private static final class ModernParser<C> implements ArgumentParser.FutureArgumentParser<C, ProtoItemStack> {
      private static final Class<?> NMS_ITEM_STACK_CLASS = CraftBukkitReflection.needNMSClassOrElse("ItemStack", "net.minecraft.world.item.ItemStack");
      private static final Class<?> CRAFT_ITEM_STACK_CLASS = CraftBukkitReflection.needOBCClass("inventory.CraftItemStack");
      private static final Supplier<Class<?>> ARGUMENT_ITEM_STACK_CLASS = Suppliers.memoize(() -> {
         return MinecraftArgumentTypes.getClassByKey(NamespacedKey.minecraft("item_stack"));
      });
      private static final Class<?> ITEM_INPUT_CLASS = (Class)Objects.requireNonNull(ItemStackParser.findItemInputClass(), "ItemInput class");
      private static final Class<?> NMS_ITEM_CLASS = CraftBukkitReflection.needNMSClassOrElse("Item", "net.minecraft.world.item.Item");
      private static final Supplier<Method> GET_MATERIAL_METHOD = Suppliers.memoize(() -> {
         return CraftBukkitReflection.needMethod(CraftBukkitReflection.needOBCClass("util.CraftMagicNumbers"), "getMaterial", NMS_ITEM_CLASS);
      });
      private static final Method CREATE_ITEM_STACK_METHOD;
      private static final Method AS_BUKKIT_COPY_METHOD;
      private static final Field ITEM_FIELD;
      private static final Field EXTRA_DATA_FIELD;
      private static final Class<?> HOLDER_CLASS;
      @Nullable
      private static final Method VALUE_METHOD;
      private static final Class<?> NBT_TAG_CLASS;
      private final ArgumentParser<C, ProtoItemStack> parser = this.createParser();

      ModernParser() {
      }

      private ArgumentParser<C, ProtoItemStack> createParser() {
         Supplier<ArgumentType<Object>> inst = () -> {
            Constructor ctr = ((Class)ARGUMENT_ITEM_STACK_CLASS.get()).getDeclaredConstructors()[0];

            try {
               return ctr.getParameterCount() == 0 ? (ArgumentType)ctr.newInstance() : (ArgumentType)ctr.newInstance(CommandBuildContextSupplier.commandBuildContext());
            } catch (ReflectiveOperationException var2) {
               throw new RuntimeException("Failed to initialize modern ItemStack parser.", var2);
            }
         };
         return (new WrappedBrigadierParser(inst)).flatMapSuccess((ctx, itemInput) -> {
            return ArgumentParseResult.successFuture(new ItemStackParser.ModernParser.ModernProtoItemStack(itemInput));
         });
      }

      @NonNull
      public CompletableFuture<ArgumentParseResult<ProtoItemStack>> parseFuture(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
         return this.parser.parseFuture(commandContext, commandInput);
      }

      @NonNull
      public SuggestionProvider<C> suggestionProvider() {
         return this.parser.suggestionProvider();
      }

      static {
         CREATE_ITEM_STACK_METHOD = (Method)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Couldn't find createItemStack method on ItemInput";
         }, CraftBukkitReflection.findMethod(ITEM_INPUT_CLASS, "a", Integer.TYPE, Boolean.TYPE), CraftBukkitReflection.findMethod(ITEM_INPUT_CLASS, "createItemStack", Integer.TYPE, Boolean.TYPE));
         AS_BUKKIT_COPY_METHOD = CraftBukkitReflection.needMethod(CRAFT_ITEM_STACK_CLASS, "asBukkitCopy", NMS_ITEM_STACK_CLASS);
         ITEM_FIELD = (Field)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Couldn't find item field on ItemInput";
         }, CraftBukkitReflection.findField(ITEM_INPUT_CLASS, "b"), CraftBukkitReflection.findField(ITEM_INPUT_CLASS, "item"));
         EXTRA_DATA_FIELD = (Field)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Couldn't find tag field on ItemInput";
         }, CraftBukkitReflection.findField(ITEM_INPUT_CLASS, "c"), CraftBukkitReflection.findField(ITEM_INPUT_CLASS, "tag"), CraftBukkitReflection.findField(ITEM_INPUT_CLASS, "components"));
         HOLDER_CLASS = CraftBukkitReflection.findMCClass("core.Holder");
         VALUE_METHOD = HOLDER_CLASS == null ? null : (Method)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Couldn't find Holder#value";
         }, CraftBukkitReflection.findMethod(HOLDER_CLASS, "value"), CraftBukkitReflection.findMethod(HOLDER_CLASS, "a"));
         NBT_TAG_CLASS = (Class)CraftBukkitReflection.firstNonNullOrThrow(() -> {
            return "Cloud not find net.minecraft.nbt.Tag";
         }, CraftBukkitReflection.findClass("net.minecraft.nbt.Tag"), CraftBukkitReflection.findClass("net.minecraft.nbt.NBTBase"), CraftBukkitReflection.findNMSClass("NBTBase"));
      }

      private static final class ModernProtoItemStack implements ProtoItemStack {
         private final Object itemInput;
         private final Material material;
         private final boolean hasExtraData;

         ModernProtoItemStack(@NonNull final Object itemInput) {
            this.itemInput = itemInput;

            try {
               Object item = ItemStackParser.ModernParser.ITEM_FIELD.get(itemInput);
               if (ItemStackParser.ModernParser.HOLDER_CLASS != null && ItemStackParser.ModernParser.HOLDER_CLASS.isInstance(item)) {
                  item = ItemStackParser.ModernParser.VALUE_METHOD.invoke(item);
               }

               this.material = (Material)((Method)ItemStackParser.ModernParser.GET_MATERIAL_METHOD.get()).invoke((Object)null, item);
               Object extraData = ItemStackParser.ModernParser.EXTRA_DATA_FIELD.get(itemInput);
               if (!ItemStackParser.ModernParser.NBT_TAG_CLASS.isInstance(extraData) && extraData != null) {
                  List<Method> isEmptyMethod = (List)Arrays.stream(extraData.getClass().getMethods()).filter((it) -> {
                     return it.getParameterCount() == 0 && it.getReturnType().equals(Boolean.TYPE);
                  }).collect(Collectors.toList());
                  if (isEmptyMethod.size() != 1) {
                     throw new IllegalStateException("Failed to locate DataComponentMap/Patch#isEmpty; size=" + isEmptyMethod.size());
                  }

                  this.hasExtraData = !(Boolean)((Method)isEmptyMethod.get(0)).invoke(extraData);
               } else {
                  this.hasExtraData = extraData != null;
               }

            } catch (ReflectiveOperationException var5) {
               throw new RuntimeException(var5);
            }
         }

         @NonNull
         public Material material() {
            return this.material;
         }

         public boolean hasExtraData() {
            return this.hasExtraData;
         }

         @NonNull
         public ItemStack createItemStack(final int stackSize, final boolean respectMaximumStackSize) {
            try {
               return (ItemStack)ItemStackParser.ModernParser.AS_BUKKIT_COPY_METHOD.invoke((Object)null, ItemStackParser.ModernParser.CREATE_ITEM_STACK_METHOD.invoke(this.itemInput, stackSize, respectMaximumStackSize));
            } catch (InvocationTargetException var5) {
               Throwable cause = var5.getCause();
               if (cause instanceof CommandSyntaxException) {
                  throw new IllegalArgumentException(cause.getMessage(), cause);
               } else {
                  throw new RuntimeException(var5);
               }
            } catch (ReflectiveOperationException var6) {
               throw new RuntimeException(var6);
            }
         }
      }
   }

   private static final class LegacyParser<C> implements ArgumentParser.FutureArgumentParser<C, ProtoItemStack>, BlockingSuggestionProvider.Strings<C> {
      private final ArgumentParser<C, ProtoItemStack> parser;

      private LegacyParser() {
         this.parser = (new MaterialParser()).mapSuccess((ctx, material) -> {
            return CompletableFuture.completedFuture(new ItemStackParser.LegacyParser.LegacyProtoItemStack(material));
         });
      }

      @NonNull
      public CompletableFuture<ArgumentParseResult<ProtoItemStack>> parseFuture(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
         return this.parser.parseFuture(commandContext, commandInput);
      }

      @NonNull
      public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
         return (Iterable)Arrays.stream(Material.values()).filter(Material::isItem).map((value) -> {
            return value.name().toLowerCase(Locale.ROOT);
         }).collect(Collectors.toList());
      }

      // $FF: synthetic method
      LegacyParser(Object x0) {
         this();
      }

      private static final class LegacyProtoItemStack implements ProtoItemStack {
         private final Material material;

         private LegacyProtoItemStack(@NonNull final Material material) {
            this.material = material;
         }

         @NonNull
         public Material material() {
            return this.material;
         }

         public boolean hasExtraData() {
            return false;
         }

         @NonNull
         public ItemStack createItemStack(final int stackSize, final boolean respectMaximumStackSize) throws IllegalArgumentException {
            if (respectMaximumStackSize && stackSize > this.material.getMaxStackSize()) {
               throw new IllegalArgumentException(String.format("The maximum stack size for %s is %d", this.material, this.material.getMaxStackSize()));
            } else {
               return new ItemStack(this.material, stackSize);
            }
         }

         // $FF: synthetic method
         LegacyProtoItemStack(Material x0, Object x1) {
            this(x0);
         }
      }
   }
}
