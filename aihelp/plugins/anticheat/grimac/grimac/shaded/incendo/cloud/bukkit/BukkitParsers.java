package ac.grim.grimac.shaded.incendo.cloud.bukkit;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.annotation.specifier.AllowEmptySelection;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.annotation.specifier.DefaultNamespace;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.annotation.specifier.RequireExplicitNamespace;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.MultipleEntitySelector;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.data.MultiplePlayerSelector;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.internal.CraftBukkitReflection;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.BlockPredicateParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.EnchantmentParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.ItemStackParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.ItemStackPredicateParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.MaterialParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.NamespacedKeyParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.OfflinePlayerParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.PlayerParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.WorldParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location.Location2DParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location.LocationParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.MultipleEntitySelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.MultiplePlayerSelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.SingleEntitySelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.selector.SinglePlayerSelectorParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserParameters;
import java.lang.reflect.Method;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
public final class BukkitParsers {
   private BukkitParsers() {
   }

   public static <C> void register(final CommandManager<C> manager) {
      manager.parserRegistry().registerParser(WorldParser.worldParser()).registerParser(MaterialParser.materialParser()).registerParser(PlayerParser.playerParser()).registerParser(OfflinePlayerParser.offlinePlayerParser()).registerParser(EnchantmentParser.enchantmentParser()).registerParser(LocationParser.locationParser()).registerParser(Location2DParser.location2DParser()).registerParser(ItemStackParser.itemStackParser()).registerParser(SingleEntitySelectorParser.singleEntitySelectorParser()).registerParser(SinglePlayerSelectorParser.singlePlayerSelectorParser());
      manager.parserRegistry().registerAnnotationMapper(AllowEmptySelection.class, (annotation, type) -> {
         return ParserParameters.single(BukkitParserParameters.ALLOW_EMPTY_SELECTOR_RESULT, annotation.value());
      });
      manager.parserRegistry().registerParserSupplier(TypeToken.get(MultipleEntitySelector.class), (parserParameters) -> {
         return new MultipleEntitySelectorParser((Boolean)parserParameters.get(BukkitParserParameters.ALLOW_EMPTY_SELECTOR_RESULT, true));
      });
      manager.parserRegistry().registerParserSupplier(TypeToken.get(MultiplePlayerSelector.class), (parserParameters) -> {
         return new MultiplePlayerSelectorParser((Boolean)parserParameters.get(BukkitParserParameters.ALLOW_EMPTY_SELECTOR_RESULT, true));
      });
      if (CraftBukkitReflection.classExists("org.bukkit.NamespacedKey")) {
         registerParserSupplierFor(manager, NamespacedKeyParser.class);
         manager.parserRegistry().registerAnnotationMapper(RequireExplicitNamespace.class, (annotation, type) -> {
            return ParserParameters.single(BukkitParserParameters.REQUIRE_EXPLICIT_NAMESPACE, true);
         });
         manager.parserRegistry().registerAnnotationMapper(DefaultNamespace.class, (annotation, type) -> {
            return ParserParameters.single(BukkitParserParameters.DEFAULT_NAMESPACE, annotation.value());
         });
      }

      if (manager.hasCapability(CloudBukkitCapabilities.BRIGADIER)) {
         registerParserSupplierFor(manager, ItemStackPredicateParser.class);
         registerParserSupplierFor(manager, BlockPredicateParser.class);
      }

   }

   private static void registerParserSupplierFor(final CommandManager<?> manager, @NonNull final Class<?> argumentClass) {
      try {
         Method registerParserSuppliers = argumentClass.getDeclaredMethod("registerParserSupplier", CommandManager.class);
         registerParserSuppliers.setAccessible(true);
         registerParserSuppliers.invoke((Object)null, manager);
      } catch (ReflectiveOperationException var3) {
         throw new RuntimeException(var3);
      }
   }
}
