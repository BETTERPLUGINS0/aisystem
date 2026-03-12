package ac.grim.grimac.shaded.incendo.cloud.bukkit.internal;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.CloudBrigadierManager;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.BlockPredicateParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.EnchantmentParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.ItemStackParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.ItemStackPredicateParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.NamespacedKeyParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location.Location2DParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location.LocationParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.MultipleEntitySelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.MultiplePlayerSelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.SingleEntitySelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.SinglePlayerSelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.UUIDParser;
import com.google.common.base.Suppliers;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.lang.reflect.Constructor;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
public final class BukkitBrigadierMapper<C> {
   private final Logger logger;
   private final CloudBrigadierManager<C, ?> brigadierManager;

   public BukkitBrigadierMapper(@NonNull final Logger logger, @NonNull final CloudBrigadierManager<C, ?> brigadierManager) {
      this.logger = logger;
      this.brigadierManager = brigadierManager;
   }

   public void registerBuiltInMappings() {
      this.registerUUID();
      this.mapSimpleNMS(new TypeToken<NamespacedKeyParser<C>>() {
      }, "resource_location", true);
      this.registerEnchantment();
      this.mapSimpleNMS(new TypeToken<ItemStackParser<C>>() {
      }, "item_stack");
      this.mapSimpleNMS(new TypeToken<ItemStackPredicateParser<C>>() {
      }, "item_predicate");
      this.mapSimpleNMS(new TypeToken<BlockPredicateParser<C>>() {
      }, "block_predicate");
      this.mapSelector(new TypeToken<SingleEntitySelectorParser<C>>() {
      }, true, false);
      this.mapSelector(new TypeToken<SinglePlayerSelectorParser<C>>() {
      }, true, true);
      this.mapSelector(new TypeToken<MultipleEntitySelectorParser<C>>() {
      }, false, false);
      this.mapSelector(new TypeToken<MultiplePlayerSelectorParser<C>>() {
      }, false, true);
      this.mapNMS(new TypeToken<LocationParser<C>>() {
      }, "vec3", this::argumentVec3);
      this.mapNMS(new TypeToken<Location2DParser<C>>() {
      }, "vec2", this::argumentVec2);
   }

   private void registerEnchantment() {
      if (Bukkit.getServer() == null) {
         this.mapResourceKey(new TypeToken<EnchantmentParser<C>>() {
         }, "enchantment");
      } else {
         try {
            Class<? extends ArgumentType<?>> ench = MinecraftArgumentTypes.getClassByKey(NamespacedKey.minecraft("item_enchantment"));
            this.mapSimpleNMS(new TypeToken<EnchantmentParser<C>>() {
            }, "item_enchantment");
         } catch (IllegalArgumentException var2) {
            this.mapResourceKey(new TypeToken<EnchantmentParser<C>>() {
            }, "enchantment");
         }

      }
   }

   private void registerUUID() {
      if (Bukkit.getServer() == null) {
         this.mapSimpleNMS(new TypeToken<UUIDParser<C>>() {
         }, "uuid");
      } else {
         try {
            Class<? extends ArgumentType<?>> uuid = MinecraftArgumentTypes.getClassByKey(NamespacedKey.minecraft("uuid"));
            this.mapSimpleNMS(new TypeToken<UUIDParser<C>>() {
            }, "uuid");
         } catch (IllegalArgumentException var2) {
         }

      }
   }

   private <T extends ArgumentParser<C, ?>> void mapResourceKey(@NonNull final TypeToken<T> parserType, @NonNull final String registryName) {
      this.mapNMS(parserType, "resource_key", (type) -> {
         return (ArgumentType)type.getDeclaredConstructors()[0].newInstance(RegistryReflection.registryKey(registryName));
      });
   }

   private <T extends ArgumentParser<C, ?>> void mapSelector(@NonNull final TypeToken<T> parserType, final boolean single, final boolean playersOnly) {
      this.mapNMS(parserType, "entity", (argumentTypeCls) -> {
         Constructor<?> constructor = argumentTypeCls.getDeclaredConstructors()[0];
         constructor.setAccessible(true);
         return (ArgumentType)constructor.newInstance(single, playersOnly);
      });
   }

   @NonNull
   private ArgumentType<?> argumentVec3(final Class<? extends ArgumentType<?>> type) throws ReflectiveOperationException {
      return (ArgumentType)type.getDeclaredConstructor(Boolean.TYPE).newInstance(true);
   }

   @NonNull
   private ArgumentType<?> argumentVec2(final Class<? extends ArgumentType<?>> type) throws ReflectiveOperationException {
      return (ArgumentType)type.getDeclaredConstructor(Boolean.TYPE).newInstance(true);
   }

   public <T extends ArgumentParser<C, ?>> void mapSimpleNMS(@NonNull final TypeToken<T> type, @NonNull final String argumentId) {
      this.mapSimpleNMS(type, argumentId, false);
   }

   public <T extends ArgumentParser<C, ?>> void mapSimpleNMS(@NonNull final TypeToken<T> type, @NonNull final String argumentId, final boolean useCloudSuggestions) {
      this.mapNMS(type, argumentId, (cls) -> {
         Constructor<?> ctr = cls.getDeclaredConstructors()[0];
         Object[] args = ctr.getParameterCount() == 1 ? new Object[]{CommandBuildContextSupplier.commandBuildContext()} : new Object[0];
         return (ArgumentType)ctr.newInstance(args);
      }, useCloudSuggestions);
   }

   public <T extends ArgumentParser<C, ?>> void mapNMS(@NonNull final TypeToken<T> type, @NonNull final String argumentId, @NonNull final BukkitBrigadierMapper.ArgumentTypeFactory factory) {
      this.mapNMS(type, argumentId, factory, false);
   }

   public <T extends ArgumentParser<C, ?>> void mapNMS(@NonNull final TypeToken<T> type, @NonNull final String argumentId, @NonNull final BukkitBrigadierMapper.ArgumentTypeFactory factory, final boolean cloudSuggestions) {
      Supplier<Class<? extends ArgumentType<?>>> argumentTypeClass = Suppliers.memoize(() -> {
         try {
            return MinecraftArgumentTypes.getClassByKey(NamespacedKey.minecraft(argumentId));
         } catch (Exception var2) {
            throw new RuntimeException("Failed to locate class for " + argumentId, var2);
         }
      });
      this.brigadierManager.registerMapping(type, (builder) -> {
         builder.to((argument) -> {
            try {
               return factory.makeInstance((Class)argumentTypeClass.get());
            } catch (Exception var6) {
               this.logger.log(Level.WARNING, "Failed to create instance of " + argumentId + ", falling back to StringArgumentType.word()", var6);
               return StringArgumentType.word();
            }
         });
         if (cloudSuggestions) {
            builder.cloudSuggestions();
         }

      });
   }

   @API(
      status = Status.INTERNAL
   )
   @FunctionalInterface
   public interface ArgumentTypeFactory {
      ArgumentType<?> makeInstance(Class<? extends ArgumentType<?>> argumentTypeClass) throws ReflectiveOperationException;
   }
}
