package ac.grim.grimac.shaded.incendo.cloud.brigadier;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapper;
import ac.grim.grimac.shaded.incendo.cloud.SenderMapperHolder;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.ArgumentTypeFactory;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMapping;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMappingBuilder;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMappingContributor;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.argument.BrigadierMappings;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.node.LiteralBrigadierNodeFactory;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.parser.WrappedBrigadierParser;
import ac.grim.grimac.shaded.incendo.cloud.brigadier.suggestion.TooltipSuggestion;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.flag.CommandFlagParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.BooleanParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.ByteParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.DoubleParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.FloatParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.IntegerParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.LongParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.ShortParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.StringArrayParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.StringParser;
import ac.grim.grimac.shaded.incendo.cloud.setting.Configurable;
import ac.grim.grimac.shaded.incendo.cloud.type.range.ByteRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.DoubleRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.FloatRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.IntRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.LongRange;
import ac.grim.grimac.shaded.incendo.cloud.type.range.ShortRange;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class CloudBrigadierManager<C, S> implements SenderMapperHolder<S, C> {
   private final BrigadierMappings<C, S> brigadierMappings = BrigadierMappings.create();
   private final LiteralBrigadierNodeFactory<C, S> literalBrigadierNodeFactory;
   private final Map<Class<?>, ArgumentTypeFactory<?>> defaultArgumentTypeSuppliers;
   private final Configurable<BrigadierSetting> settings = Configurable.enumConfigurable(BrigadierSetting.class);
   private final SenderMapper<S, C> brigadierSourceMapper;

   public CloudBrigadierManager(@NonNull final CommandManager<C> commandManager, @NonNull final SenderMapper<S, C> brigadierSourceMapper) {
      this.brigadierSourceMapper = (SenderMapper)Objects.requireNonNull(brigadierSourceMapper, "brigadierSourceMapper");
      this.defaultArgumentTypeSuppliers = new HashMap();
      this.literalBrigadierNodeFactory = new LiteralBrigadierNodeFactory(this, commandManager, commandManager.suggestionFactory().mapped(TooltipSuggestion::tooltipSuggestion));
      this.registerInternalMappings();
      ServiceLoader<BrigadierMappingContributor> loader = ServiceLoader.load(BrigadierMappingContributor.class, BrigadierMappingContributor.class.getClassLoader());
      loader.iterator().forEachRemaining((contributor) -> {
         contributor.contribute(commandManager, this);
      });
      commandManager.registerCommandPreProcessor((ctx) -> {
         if (!ctx.commandContext().contains("_cloud_brigadier_native_sender")) {
            ctx.commandContext().store("_cloud_brigadier_native_sender", this.brigadierSourceMapper.reverse(ctx.commandContext().sender()));
         }

      });
   }

   private void registerInternalMappings() {
      this.registerMapping(new TypeToken<ByteParser<C>>() {
      }, (builder) -> {
         builder.to((argument) -> {
            return IntegerArgumentType.integer(((ByteRange)argument.range()).minByte(), ((ByteRange)argument.range()).maxByte());
         }).cloudSuggestions();
      });
      this.registerMapping(new TypeToken<ShortParser<C>>() {
      }, (builder) -> {
         builder.to((argument) -> {
            return IntegerArgumentType.integer(((ShortRange)argument.range()).minShort(), ((ShortRange)argument.range()).maxShort());
         }).cloudSuggestions();
      });
      this.registerMapping(new TypeToken<IntegerParser<C>>() {
      }, (builder) -> {
         builder.to((argument) -> {
            if (!argument.hasMin() && !argument.hasMax()) {
               return IntegerArgumentType.integer();
            } else if (argument.hasMin() && !argument.hasMax()) {
               return IntegerArgumentType.integer(((IntRange)argument.range()).minInt());
            } else {
               return !argument.hasMin() ? IntegerArgumentType.integer(Integer.MIN_VALUE, ((IntRange)argument.range()).maxInt()) : IntegerArgumentType.integer(((IntRange)argument.range()).minInt(), ((IntRange)argument.range()).maxInt());
            }
         }).cloudSuggestions();
      });
      this.registerMapping(new TypeToken<FloatParser<C>>() {
      }, (builder) -> {
         builder.to((argument) -> {
            if (!argument.hasMin() && !argument.hasMax()) {
               return FloatArgumentType.floatArg();
            } else if (argument.hasMin() && !argument.hasMax()) {
               return FloatArgumentType.floatArg(((FloatRange)argument.range()).minFloat());
            } else {
               return !argument.hasMin() ? FloatArgumentType.floatArg(-3.4028235E38F, ((FloatRange)argument.range()).maxFloat()) : FloatArgumentType.floatArg(((FloatRange)argument.range()).minFloat(), ((FloatRange)argument.range()).maxFloat());
            }
         }).cloudSuggestions();
      });
      this.registerMapping(new TypeToken<DoubleParser<C>>() {
      }, (builder) -> {
         builder.to((argument) -> {
            if (!argument.hasMin() && !argument.hasMax()) {
               return DoubleArgumentType.doubleArg();
            } else if (argument.hasMin() && !argument.hasMax()) {
               return DoubleArgumentType.doubleArg(((DoubleRange)argument.range()).minDouble());
            } else {
               return !argument.hasMin() ? DoubleArgumentType.doubleArg(-1.7976931348623157E308D, ((DoubleRange)argument.range()).maxDouble()) : DoubleArgumentType.doubleArg(((DoubleRange)argument.range()).minDouble(), ((DoubleRange)argument.range()).maxDouble());
            }
         }).cloudSuggestions();
      });
      this.registerMapping(new TypeToken<LongParser<C>>() {
      }, (builder) -> {
         builder.to((longParser) -> {
            if (!longParser.hasMin() && !longParser.hasMax()) {
               return LongArgumentType.longArg();
            } else if (longParser.hasMin() && !longParser.hasMax()) {
               return LongArgumentType.longArg(((LongRange)longParser.range()).minLong());
            } else {
               return !longParser.hasMin() ? LongArgumentType.longArg(Long.MIN_VALUE, ((LongRange)longParser.range()).maxLong()) : LongArgumentType.longArg(((LongRange)longParser.range()).minLong(), ((LongRange)longParser.range()).maxLong());
            }
         }).cloudSuggestions();
      });
      this.registerMapping(new TypeToken<BooleanParser<C>>() {
      }, (builder) -> {
         builder.toConstant(BoolArgumentType.bool());
      });
      this.registerMapping(new TypeToken<StringParser<C>>() {
      }, (builder) -> {
         builder.cloudSuggestions().to((argument) -> {
            switch(argument.stringMode()) {
            case QUOTED:
               return StringArgumentType.string();
            case GREEDY:
            case GREEDY_FLAG_YIELDING:
               return StringArgumentType.greedyString();
            default:
               return StringArgumentType.word();
            }
         });
      });
      this.registerMapping(new TypeToken<CommandFlagParser<C>>() {
      }, (builder) -> {
         builder.cloudSuggestions().toConstant(StringArgumentType.greedyString());
      });
      this.registerMapping(new TypeToken<StringArrayParser<C>>() {
      }, (builder) -> {
         builder.cloudSuggestions().toConstant(StringArgumentType.greedyString());
      });
      this.registerMapping(new TypeToken<WrappedBrigadierParser<C, ?>>() {
      }, (builder) -> {
         builder.to(WrappedBrigadierParser::nativeArgumentType);
      });
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public Configurable<BrigadierSetting> settings() {
      return this.settings;
   }

   @NonNull
   public SenderMapper<S, C> senderMapper() {
      return this.brigadierSourceMapper;
   }

   @API(
      status = Status.STABLE,
      since = "1.2.0"
   )
   public void setNativeNumberSuggestions(final boolean nativeNumberSuggestions) {
      this.setNativeSuggestions(new TypeToken<ByteParser<C>>() {
      }, nativeNumberSuggestions);
      this.setNativeSuggestions(new TypeToken<ShortParser<C>>() {
      }, nativeNumberSuggestions);
      this.setNativeSuggestions(new TypeToken<IntegerParser<C>>() {
      }, nativeNumberSuggestions);
      this.setNativeSuggestions(new TypeToken<FloatParser<C>>() {
      }, nativeNumberSuggestions);
      this.setNativeSuggestions(new TypeToken<DoubleParser<C>>() {
      }, nativeNumberSuggestions);
      this.setNativeSuggestions(new TypeToken<LongParser<C>>() {
      }, nativeNumberSuggestions);
   }

   @API(
      status = Status.STABLE,
      since = "1.2.0"
   )
   public <T, K extends ArgumentParser<C, T>> void setNativeSuggestions(@NonNull final TypeToken<K> argumentType, final boolean nativeSuggestions) throws IllegalArgumentException {
      Class<K> parserClass = GenericTypeReflector.erase(argumentType.getType());
      BrigadierMapping<C, K, S> mapping = this.brigadierMappings.mapping(parserClass);
      if (mapping == null) {
         throw new IllegalArgumentException("No mapper registered for type: " + GenericTypeReflector.erase(argumentType.getType()).toGenericString());
      } else {
         this.brigadierMappings.registerMapping(parserClass, mapping.withNativeSuggestions(nativeSuggestions));
      }
   }

   @API(
      status = Status.STABLE,
      since = "1.5.0"
   )
   public <K extends ArgumentParser<C, ?>> void registerMapping(@NonNull final TypeToken<K> parserType, final Consumer<BrigadierMappingBuilder<K, S>> configurer) {
      BrigadierMappingBuilder<K, S> builder = BrigadierMapping.builder();
      configurer.accept(builder);
      this.mappings().registerMappingUnsafe(GenericTypeReflector.erase(parserType.getType()), builder.build());
   }

   @API(
      status = Status.INTERNAL,
      since = "2.0.0"
   )
   @NonNull
   public BrigadierMappings<C, S> mappings() {
      return this.brigadierMappings;
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public LiteralBrigadierNodeFactory<C, S> literalBrigadierNodeFactory() {
      return this.literalBrigadierNodeFactory;
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   public <T> void registerDefaultArgumentTypeSupplier(@NonNull final Class<T> clazz, @NonNull final ArgumentTypeFactory<T> factory) {
      this.defaultArgumentTypeSuppliers.put(clazz, factory);
   }

   @API(
      status = Status.INTERNAL,
      since = "2.0.0"
   )
   @NonNull
   public Map<Class<?>, ArgumentTypeFactory<?>> defaultArgumentTypeFactories() {
      return Collections.unmodifiableMap(this.defaultArgumentTypeSuppliers);
   }
}
