package ac.grim.grimac.utils.common.arguments;

import ac.grim.grimac.platform.api.Platform;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class ArgumentUtils {
   private static final Pattern WEBSITE_URL_PATTERN = Pattern.compile("^(https?://)?(localhost:[0-9]{1,4}|(([a-zA-Z0-9_-]+\\.)?[a-zA-Z0-9_-]+\\.[a-zA-Z]{2,3}))(/[a-zA-Z0-9_/-?=]*)?$");

   public static <T extends Number> Predicate<T> validRange(T min, T max) {
      return (number) -> {
         return number.doubleValue() >= min.doubleValue() && number.doubleValue() <= max.doubleValue();
      };
   }

   public static Predicate<String> validURL() {
      return (string) -> {
         return WEBSITE_URL_PATTERN.matcher(string).matches();
      };
   }

   public static Function<String, String> modifyURL(Supplier<String> defaultValue) {
      return (string) -> {
         if (!string.endsWith("/")) {
            string = string + "/";
         }

         return WEBSITE_URL_PATTERN.matcher(string).matches() ? string : (String)defaultValue.get();
      };
   }

   public static ArgumentOptions.Builder<Long> range(String key, long defaultValue, long min, long max) {
      return ArgumentOptions.from(Long.class, key, (Object)defaultValue).verifier(validRange(min, max)).modifier((aLong) -> {
         return Math.min(Math.max(aLong, min), max);
      });
   }

   public static ArgumentOptions.Builder<Integer> range(String key, int defaultValue, int min, int max) {
      return ArgumentOptions.from(Integer.class, key, (Object)defaultValue).verifier(validRange(min, max)).modifier((integer) -> {
         return Math.min(Math.max(integer, min), max);
      });
   }

   public static ArgumentOptions.Builder<String> url(String key, String defaultUrl) {
      return ArgumentOptions.from(String.class, key, (Object)defaultUrl).verifier(validURL()).modifier(modifyURL(() -> {
         return defaultUrl;
      }));
   }

   public static ArgumentOptions.Builder<String> string(String key, String defaultValue) {
      return ArgumentOptions.from(String.class, key, (Object)defaultValue);
   }

   public static ArgumentOptions.Builder<Boolean> string(String key, boolean defaultValue) {
      return ArgumentOptions.from(Boolean.class, key, (Object)defaultValue);
   }

   public static ArgumentOptions.Builder<Platform> platform(String key) {
      return ArgumentOptions.from(Platform.class, key);
   }
}
