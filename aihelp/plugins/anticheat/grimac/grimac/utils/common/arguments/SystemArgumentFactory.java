package ac.grim.grimac.utils.common.arguments;

import ac.grim.grimac.platform.api.Platform;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;

public record SystemArgumentFactory(Map<String, String> arguments, Map<Class<?>, Function<String, ?>> parsers, Consumer<SystemArgument<?>> creationListener, Consumer<ArgumentOptions.Builder<?>> optionModifier) {
   public SystemArgumentFactory(Map<String, String> arguments, Map<Class<?>, Function<String, ?>> parsers, Consumer<SystemArgument<?>> creationListener, Consumer<ArgumentOptions.Builder<?>> optionModifier) {
      this.arguments = arguments;
      this.parsers = parsers;
      this.creationListener = creationListener;
      this.optionModifier = optionModifier;
   }

   private <T> SystemArgument<T> createDefaultSupplier(ArgumentOptions<T> options) {
      T value = options.getModifier().apply(options.getDefaultSupplier().get());
      if (value == null && !options.isNullable()) {
         throw new IllegalArgumentException("Default value cannot be null for startup argument \"" + options.getKey() + "\"");
      } else if (value != null && !options.getVerifier().test(value)) {
         throw new IllegalArgumentException("Invalid default value for startup argument \"" + options.getKey() + "\"");
      } else {
         SystemArgument<T> argument = new SystemArgument(options.getKey(), options.getClazz(), value, false, options.getVisibility());
         this.creationListener.accept(argument);
         return argument;
      }
   }

   public <T> SystemArgument<T> create(ArgumentOptions.Builder<T> builder) {
      if (this.optionModifier != null) {
         this.optionModifier.accept(builder);
      }

      ArgumentOptions<T> options = builder.build();
      String value = (String)this.arguments.get(options.getKey().toLowerCase());
      if (value == null) {
         return this.createDefaultSupplier(options);
      } else {
         try {
            Function<String, T> parser = (Function)this.parsers.get(options.getClazz());
            if (parser == null) {
               return this.createDefaultSupplier(options);
            } else {
               T parsed = options.getModifier().apply(parser.apply(value));
               if (parsed != null && options.getVerifier().test(parsed)) {
                  SystemArgument<T> newArgument = new SystemArgument(options.getKey(), options.getClazz(), parsed, true, options.getVisibility());
                  this.creationListener.accept(newArgument);
                  return newArgument;
               } else {
                  return this.createDefaultSupplier(options);
               }
            }
         } catch (Exception var7) {
            exception("Failed to parse value for startup argument \"" + options.getKey() + "\"", var7);
            return this.createDefaultSupplier(options);
         }
      }
   }

   public Map<String, String> getFoundArguments() {
      return this.arguments;
   }

   private static void exception(String message, Exception e) {
   }

   private static void warn(String message) {
   }

   public Map<String, String> arguments() {
      return this.arguments;
   }

   public Map<Class<?>, Function<String, ?>> parsers() {
      return this.parsers;
   }

   public Consumer<SystemArgument<?>> creationListener() {
      return this.creationListener;
   }

   public Consumer<ArgumentOptions.Builder<?>> optionModifier() {
      return this.optionModifier;
   }

   public static class Builder {
      private final String prefix;
      private boolean envSupport = false;
      private Consumer<SystemArgument<?>> registerListener = (argument) -> {
      };
      private final Map<Class<?>, Function<String, ?>> parseBuilder;
      private Consumer<ArgumentOptions.Builder<?>> optionModifier = null;

      public static SystemArgumentFactory.Builder of(String prefix) {
         return new SystemArgumentFactory.Builder(prefix);
      }

      public SystemArgumentFactory.Builder onRegister(Consumer<SystemArgument<?>> listener) {
         this.registerListener = listener;
         return this;
      }

      public SystemArgumentFactory.Builder optionModifier(Consumer<ArgumentOptions.Builder<?>> modifier) {
         this.optionModifier = modifier;
         return this;
      }

      public SystemArgumentFactory.Builder supportEnv() {
         this.envSupport = true;
         return this;
      }

      private Builder(String prefix) {
         this.prefix = prefix;
         this.parseBuilder = new HashMap();
         this.registerDefaultParsers();
      }

      protected void registerDefaultParsers() {
         this.registerParser(Boolean.class, Boolean::parseBoolean).registerParser(Byte.class, Byte::parseByte).registerParser(Short.class, Short::parseShort).registerParser(Integer.class, Integer::parseInt).registerParser(Float.class, Float::parseFloat).registerParser(Double.class, Double::parseDouble).registerParser(Long.class, Long::parseLong).registerParser(Character.class, (s) -> {
            return !s.isEmpty() ? s.charAt(0) : '\u0000';
         }).registerParser(char[].class, String::toCharArray).registerParser(String.class, String::valueOf).registerParser(Charset.class, Charset::forName).registerParser(Platform.class, Platform::getByName);
      }

      public <T> SystemArgumentFactory.Builder registerParser(Class<T> type, Function<String, T> parser) {
         this.parseBuilder.put(type, parser);
         return this;
      }

      private void updateFromEnv(Map<String, String> builder) {
         Iterator var2 = System.getenv().entrySet().iterator();

         while(var2.hasNext()) {
            Entry<String, String> entry = (Entry)var2.next();
            if (((String)entry.getKey()).startsWith(this.prefix.toUpperCase()) && builder.put(((String)entry.getKey()).toLowerCase(), (String)entry.getValue()) != null) {
               SystemArgumentFactory.warn("Env variable overwriting system variable: " + (String)entry.getKey());
            }
         }

      }

      public SystemArgumentFactory build() {
         String findPrefix = "-d" + this.prefix.toLowerCase();
         Map<String, String> builder = this.getSystemPropertiesMap(findPrefix);
         if (this.envSupport) {
            try {
               this.updateFromEnv(builder);
            } catch (Exception var4) {
               SystemArgumentFactory.exception("Failed to read environment variables", var4);
            }
         }

         return new SystemArgumentFactory(Map.copyOf(builder), Map.copyOf(this.parseBuilder), this.registerListener, this.optionModifier);
      }

      @NotNull
      protected Map<String, String> getSystemPropertiesMap(String findPrefix) {
         Map<String, String> builder = new HashMap();
         Iterator var3 = ManagementFactory.getRuntimeMXBean().getInputArguments().iterator();

         while(true) {
            while(true) {
               String line;
               do {
                  if (!var3.hasNext()) {
                     return builder;
                  }

                  line = (String)var3.next();
               } while(!line.toLowerCase().startsWith(findPrefix));

               int index = line.indexOf(61);
               if (index > 0 && index < line.length() - 1) {
                  String key = line.substring(2, index);
                  String value = line.substring(index + 1);
                  builder.put(key.toLowerCase(), value);
               } else {
                  SystemArgumentFactory.warn("Invalid startup argument: " + line);
               }
            }
         }
      }
   }
}
