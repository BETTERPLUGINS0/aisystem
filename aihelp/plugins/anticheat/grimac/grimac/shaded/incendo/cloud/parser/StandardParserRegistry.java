package ac.grim.grimac.shaded.incendo.cloud.parser;

import ac.grim.grimac.shaded.geantyref.AnnotatedTypeMap;
import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.annotation.specifier.FlagYielding;
import ac.grim.grimac.shaded.incendo.cloud.annotation.specifier.Greedy;
import ac.grim.grimac.shaded.incendo.cloud.annotation.specifier.Liberal;
import ac.grim.grimac.shaded.incendo.cloud.annotation.specifier.Quoted;
import ac.grim.grimac.shaded.incendo.cloud.annotation.specifier.Range;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.BooleanParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.ByteParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.CharacterParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.DoubleParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.DurationParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.EnumParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.FloatParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.IntegerParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.LongParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.ShortParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.StringArrayParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.StringParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.UUIDParser;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Function;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;

@API(
   status = Status.STABLE
)
public final class StandardParserRegistry<C> implements ParserRegistry<C> {
   private static final Map<Class<?>, Class<?>> PRIMITIVE_MAPPINGS = new HashMap<Class<?>, Class<?>>() {
      {
         this.put(Character.TYPE, Character.class);
         this.put(Integer.TYPE, Integer.class);
         this.put(Short.TYPE, Short.class);
         this.put(Byte.TYPE, Byte.class);
         this.put(Float.TYPE, Float.class);
         this.put(Double.TYPE, Double.class);
         this.put(Long.TYPE, Long.class);
         this.put(Boolean.TYPE, Boolean.class);
      }
   };
   private final Map<String, Function<ParserParameters, ArgumentParser<C, ?>>> namedParsers = new HashMap();
   private final Map<AnnotatedType, Function<ParserParameters, ArgumentParser<C, ?>>> parserSuppliers = new AnnotatedTypeMap();
   private final Map<Class<? extends Annotation>, ParserRegistry.AnnotationMapper<?>> annotationMappers = new HashMap();
   private final Map<String, SuggestionProvider<C>> namedSuggestionProviders = new HashMap();

   public StandardParserRegistry() {
      this.registerAnnotationMapper(Range.class, new StandardParserRegistry.RangeMapper());
      this.registerAnnotationMapper(Greedy.class, new StandardParserRegistry.GreedyMapper());
      this.registerAnnotationMapper(Quoted.class, (quoted, typeToken) -> {
         return ParserParameters.single(StandardParameters.QUOTED, true);
      });
      this.registerAnnotationMapper(Liberal.class, (liberal, typeToken) -> {
         return ParserParameters.single(StandardParameters.LIBERAL, true);
      });
      this.registerAnnotationMapper(FlagYielding.class, (flagYielding, typeToken) -> {
         return ParserParameters.single(StandardParameters.FLAG_YIELDING, true);
      });
      this.registerParserSupplier(TypeToken.get(Byte.class), (options) -> {
         return new ByteParser((Byte)((Number)options.get(StandardParameters.RANGE_MIN, -128)), (Byte)((Number)options.get(StandardParameters.RANGE_MAX, (byte)127)));
      });
      this.registerParserSupplier(TypeToken.get(Short.class), (options) -> {
         return new ShortParser((Short)((Number)options.get(StandardParameters.RANGE_MIN, -32768)), (Short)((Number)options.get(StandardParameters.RANGE_MAX, (short)32767)));
      });
      this.registerParserSupplier(TypeToken.get(Integer.class), (options) -> {
         return new IntegerParser((Integer)((Number)options.get(StandardParameters.RANGE_MIN, Integer.MIN_VALUE)), (Integer)((Number)options.get(StandardParameters.RANGE_MAX, Integer.MAX_VALUE)));
      });
      this.registerParserSupplier(TypeToken.get(Long.class), (options) -> {
         return new LongParser((Long)((Number)options.get(StandardParameters.RANGE_MIN, Long.MIN_VALUE)), (Long)((Number)options.get(StandardParameters.RANGE_MAX, Long.MAX_VALUE)));
      });
      this.registerParserSupplier(TypeToken.get(Float.class), (options) -> {
         return new FloatParser((Float)((Number)options.get(StandardParameters.RANGE_MIN, Float.NEGATIVE_INFINITY)), (Float)((Number)options.get(StandardParameters.RANGE_MAX, Float.POSITIVE_INFINITY)));
      });
      this.registerParserSupplier(TypeToken.get(Double.class), (options) -> {
         return new DoubleParser((Double)((Number)options.get(StandardParameters.RANGE_MIN, Double.NEGATIVE_INFINITY)), (Double)((Number)options.get(StandardParameters.RANGE_MAX, Double.POSITIVE_INFINITY)));
      });
      this.registerParserSupplier(TypeToken.get(Character.class), (options) -> {
         return new CharacterParser();
      });
      this.registerParserSupplier(TypeToken.get(String[].class), (options) -> {
         return new StringArrayParser((Boolean)options.get(StandardParameters.FLAG_YIELDING, false));
      });
      this.registerParserSupplier(TypeToken.get(String.class), (options) -> {
         boolean greedy = (Boolean)options.get(StandardParameters.GREEDY, false);
         boolean greedyFlagAware = (Boolean)options.get(StandardParameters.FLAG_YIELDING, false);
         boolean quoted = (Boolean)options.get(StandardParameters.QUOTED, false);
         if (greedyFlagAware && quoted) {
            throw new IllegalArgumentException("Don't know whether to create GREEDY_FLAG_YIELDING or QUOTED StringArgument.StringParser, both specified.");
         } else if (greedy && quoted) {
            throw new IllegalArgumentException("Don't know whether to create GREEDY or QUOTED StringArgument.StringParser, both specified.");
         } else {
            StringParser.StringMode stringMode;
            if (greedyFlagAware) {
               stringMode = StringParser.StringMode.GREEDY_FLAG_YIELDING;
            } else if (greedy) {
               stringMode = StringParser.StringMode.GREEDY;
            } else if (quoted) {
               stringMode = StringParser.StringMode.QUOTED;
            } else {
               stringMode = StringParser.StringMode.SINGLE;
            }

            return new StringParser(stringMode);
         }
      });
      this.registerParserSupplier(TypeToken.get(Boolean.class), (options) -> {
         boolean liberal = (Boolean)options.get(StandardParameters.LIBERAL, false);
         return new BooleanParser(liberal);
      });
      this.registerParser(UUIDParser.uuidParser());
      this.registerParser(DurationParser.durationParser());
      ServiceLoader<ParserContributor> loader = ServiceLoader.load(ParserContributor.class, ParserContributor.class.getClassLoader());
      loader.iterator().forEachRemaining((contributor) -> {
         contributor.contribute(this);
      });
   }

   private static boolean isPrimitive(@NonNull final TypeToken<?> type) {
      return GenericTypeReflector.erase(type.getType()).isPrimitive();
   }

   @This
   public <T> StandardParserRegistry<C> registerParserSupplier(@NonNull final TypeToken<T> type, @NonNull final Function<ParserParameters, ArgumentParser<C, ?>> supplier) {
      this.parserSuppliers.put(type.getAnnotatedType(), supplier);
      return this;
   }

   @This
   public StandardParserRegistry<C> registerNamedParserSupplier(@NonNull final String name, @NonNull final Function<ParserParameters, ArgumentParser<C, ?>> supplier) {
      this.namedParsers.put(name, supplier);
      return this;
   }

   @This
   public <A extends Annotation> StandardParserRegistry<C> registerAnnotationMapper(@NonNull final Class<A> annotation, @NonNull final ParserRegistry.AnnotationMapper<A> mapper) {
      this.annotationMappers.put(annotation, mapper);
      return this;
   }

   @NonNull
   public ParserParameters parseAnnotations(@NonNull final TypeToken<?> parsingType, @NonNull final Collection<? extends Annotation> annotations) {
      ParserParameters parserParameters = new ParserParameters();
      annotations.forEach((annotation) -> {
         ParserRegistry.AnnotationMapper mapper = (ParserRegistry.AnnotationMapper)this.annotationMappers.get(annotation.annotationType());
         if (mapper != null) {
            ParserParameters parserParametersCasted = mapper.mapAnnotation(annotation, parsingType);
            parserParameters.merge(parserParametersCasted);
         }
      });
      return parserParameters;
   }

   @NonNull
   public <T> Optional<ArgumentParser<C, T>> createParser(@NonNull final TypeToken<T> type, @NonNull final ParserParameters parserParameters) {
      TypeToken actualType;
      if (GenericTypeReflector.erase(type.getType()).isPrimitive()) {
         actualType = TypeToken.get((Class)PRIMITIVE_MAPPINGS.get(GenericTypeReflector.erase(type.getType())));
      } else {
         actualType = type;
      }

      Function<ParserParameters, ArgumentParser<C, ?>> producer = (Function)this.parserSuppliers.get(actualType.getAnnotatedType());
      if (producer == null) {
         if (GenericTypeReflector.isSuperType(Enum.class, actualType.getType())) {
            EnumParser enumArgument = new EnumParser(GenericTypeReflector.erase(actualType.getType()));
            return Optional.of(enumArgument);
         } else {
            return Optional.empty();
         }
      } else {
         ArgumentParser<C, T> parser = (ArgumentParser)producer.apply(parserParameters);
         return Optional.of(parser);
      }
   }

   @NonNull
   public <T> Optional<ArgumentParser<C, T>> createParser(@NonNull final String name, @NonNull final ParserParameters parserParameters) {
      Function<ParserParameters, ArgumentParser<C, ?>> producer = (Function)this.namedParsers.get(name);
      if (producer == null) {
         return Optional.empty();
      } else {
         ArgumentParser<C, T> parser = (ArgumentParser)producer.apply(parserParameters);
         return Optional.of(parser);
      }
   }

   public void registerSuggestionProvider(@NonNull final String name, @NonNull final SuggestionProvider<C> suggestionProvider) {
      this.namedSuggestionProviders.put(name.toLowerCase(Locale.ENGLISH), suggestionProvider);
   }

   @NonNull
   public Optional<SuggestionProvider<C>> getSuggestionProvider(@NonNull final String name) {
      SuggestionProvider<C> suggestionProvider = (SuggestionProvider)this.namedSuggestionProviders.get(name.toLowerCase(Locale.ENGLISH));
      return Optional.ofNullable(suggestionProvider);
   }

   private static final class RangeMapper implements ParserRegistry.AnnotationMapper<Range> {
      private RangeMapper() {
      }

      @NonNull
      public ParserParameters mapAnnotation(@NonNull final Range range, @NonNull final TypeToken<?> type) {
         Class clazz;
         if (StandardParserRegistry.isPrimitive(type)) {
            clazz = (Class)StandardParserRegistry.PRIMITIVE_MAPPINGS.get(GenericTypeReflector.erase(type.getType()));
         } else {
            clazz = GenericTypeReflector.erase(type.getType());
         }

         if (!Number.class.isAssignableFrom(clazz)) {
            return ParserParameters.empty();
         } else {
            Number min = null;
            Number max = null;
            if (clazz.equals(Byte.class)) {
               if (!range.min().isEmpty()) {
                  min = Byte.parseByte(range.min());
               }

               if (!range.max().isEmpty()) {
                  max = Byte.parseByte(range.max());
               }
            } else if (clazz.equals(Short.class)) {
               if (!range.min().isEmpty()) {
                  min = Short.parseShort(range.min());
               }

               if (!range.max().isEmpty()) {
                  max = Short.parseShort(range.max());
               }
            } else if (clazz.equals(Integer.class)) {
               if (!range.min().isEmpty()) {
                  min = Integer.parseInt(range.min());
               }

               if (!range.max().isEmpty()) {
                  max = Integer.parseInt(range.max());
               }
            } else if (clazz.equals(Long.class)) {
               if (!range.min().isEmpty()) {
                  min = Long.parseLong(range.min());
               }

               if (!range.max().isEmpty()) {
                  max = Long.parseLong(range.max());
               }
            } else if (clazz.equals(Float.class)) {
               if (!range.min().isEmpty()) {
                  min = Float.parseFloat(range.min());
               }

               if (!range.max().isEmpty()) {
                  max = Float.parseFloat(range.max());
               }
            } else if (clazz.equals(Double.class)) {
               if (!range.min().isEmpty()) {
                  min = Double.parseDouble(range.min());
               }

               if (!range.max().isEmpty()) {
                  max = Double.parseDouble(range.max());
               }
            }

            ParserParameters parserParameters = new ParserParameters();
            if (min != null) {
               parserParameters.store(StandardParameters.RANGE_MIN, min);
            }

            if (max != null) {
               parserParameters.store(StandardParameters.RANGE_MAX, max);
            }

            return parserParameters;
         }
      }

      // $FF: synthetic method
      RangeMapper(Object x0) {
         this();
      }
   }

   private static final class GreedyMapper implements ParserRegistry.AnnotationMapper<Greedy> {
      private GreedyMapper() {
      }

      @NonNull
      public ParserParameters mapAnnotation(@NonNull final Greedy greedy, @NonNull final TypeToken<?> typeToken) {
         return ParserParameters.single(StandardParameters.GREEDY, true);
      }

      // $FF: synthetic method
      GreedyMapper(Object x0) {
         this();
      }
   }
}
