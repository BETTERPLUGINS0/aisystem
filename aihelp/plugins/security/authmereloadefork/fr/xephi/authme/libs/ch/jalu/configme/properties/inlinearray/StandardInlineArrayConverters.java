package fr.xephi.authme.libs.ch.jalu.configme.properties.inlinearray;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public class StandardInlineArrayConverters<T> implements InlineArrayConverter<T> {
   public static final InlineArrayConverter<Long> LONG = new StandardInlineArrayConverters(",", (x$0) -> {
      return new Long[x$0];
   }, Long::parseLong);
   public static final InlineArrayConverter<Integer> INTEGER = new StandardInlineArrayConverters(",", (x$0) -> {
      return new Integer[x$0];
   }, Integer::parseInt);
   public static final InlineArrayConverter<Float> FLOAT = new StandardInlineArrayConverters(",", (x$0) -> {
      return new Float[x$0];
   }, Float::parseFloat);
   public static final InlineArrayConverter<Double> DOUBLE = new StandardInlineArrayConverters(",", (x$0) -> {
      return new Double[x$0];
   }, Double::parseDouble);
   public static final InlineArrayConverter<Short> SHORT = new StandardInlineArrayConverters(",", (x$0) -> {
      return new Short[x$0];
   }, Short::parseShort);
   public static final InlineArrayConverter<Byte> BYTE = new StandardInlineArrayConverters(",", (x$0) -> {
      return new Byte[x$0];
   }, Byte::parseByte);
   public static final InlineArrayConverter<Boolean> BOOLEAN = new StandardInlineArrayConverters(",", (x$0) -> {
      return new Boolean[x$0];
   }, (s) -> {
      return s.isEmpty() ? null : Boolean.parseBoolean(s);
   });
   public static final InlineArrayConverter<String> STRING = new StandardInlineArrayConverters("\n", (x$0) -> {
      return new String[x$0];
   }, (s) -> {
      return s;
   }, false);
   private final String separator;
   private final IntFunction<T[]> arrayProducer;
   private final Function<String, T> convertFunction;
   private final boolean useTrimAndSpaces;

   public StandardInlineArrayConverters(String separator, IntFunction<T[]> arrayProducer, Function<String, T> convertFunction) {
      this(separator, arrayProducer, convertFunction, true);
   }

   public StandardInlineArrayConverters(String separator, IntFunction<T[]> arrayProducer, Function<String, T> convertFunction, boolean useTrimAndSpaces) {
      this.separator = separator;
      this.arrayProducer = arrayProducer;
      this.convertFunction = convertFunction;
      this.useTrimAndSpaces = useTrimAndSpaces;
   }

   public T[] fromString(String input) {
      String[] inputArray = input.split(Pattern.quote(this.separator));
      return Arrays.stream(inputArray).map(this::convert).filter(Objects::nonNull).toArray(this.arrayProducer);
   }

   public String toExportValue(T[] value) {
      String delimiter = this.useTrimAndSpaces ? this.separator + " " : this.separator;
      StringJoiner joiner = new StringJoiner(delimiter);
      Object[] var4 = value;
      int var5 = value.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         T entry = var4[var6];
         joiner.add(String.valueOf(entry));
      }

      return joiner.toString();
   }

   @Nullable
   protected T convert(String input) {
      try {
         String argument = this.useTrimAndSpaces ? input.trim() : input;
         return this.convertFunction.apply(argument);
      } catch (Exception var3) {
         return null;
      }
   }
}
