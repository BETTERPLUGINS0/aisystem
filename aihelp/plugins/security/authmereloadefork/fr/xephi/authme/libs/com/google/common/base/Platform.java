package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.ServiceConfigurationError;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
final class Platform {
   private static final Logger logger = Logger.getLogger(Platform.class.getName());
   private static final PatternCompiler patternCompiler = loadPatternCompiler();

   private Platform() {
   }

   static long systemNanoTime() {
      return System.nanoTime();
   }

   static CharMatcher precomputeCharMatcher(CharMatcher matcher) {
      return matcher.precomputedInternal();
   }

   static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> enumClass, String value) {
      WeakReference<? extends Enum<?>> ref = (WeakReference)Enums.getEnumConstants(enumClass).get(value);
      return ref == null ? Optional.absent() : Optional.of((Enum)enumClass.cast(ref.get()));
   }

   static String formatCompact4Digits(double value) {
      return String.format(Locale.ROOT, "%.4g", value);
   }

   static boolean stringIsNullOrEmpty(@CheckForNull String string) {
      return string == null || string.isEmpty();
   }

   static String nullToEmpty(@CheckForNull String string) {
      return string == null ? "" : string;
   }

   @CheckForNull
   static String emptyToNull(@CheckForNull String string) {
      return stringIsNullOrEmpty(string) ? null : string;
   }

   static CommonPattern compilePattern(String pattern) {
      Preconditions.checkNotNull(pattern);
      return patternCompiler.compile(pattern);
   }

   static boolean patternCompilerIsPcreLike() {
      return patternCompiler.isPcreLike();
   }

   private static PatternCompiler loadPatternCompiler() {
      return new Platform.JdkPatternCompiler();
   }

   private static void logPatternCompilerError(ServiceConfigurationError e) {
      logger.log(Level.WARNING, "Error loading regex compiler, falling back to next option", e);
   }

   static void checkGwtRpcEnabled() {
      String propertyName = "guava.gwt.emergency_reenable_rpc";
      if (!Boolean.parseBoolean(System.getProperty(propertyName, "false"))) {
         throw new UnsupportedOperationException(Strings.lenientFormat("We are removing GWT-RPC support for Guava types. You can temporarily reenable support by setting the system property %s to true. For more about system properties, see %s. For more about Guava's GWT-RPC support, see %s.", propertyName, "https://stackoverflow.com/q/5189914/28465", "https://groups.google.com/d/msg/guava-announce/zHZTFg7YF3o/rQNnwdHeEwAJ"));
      } else {
         logger.log(Level.WARNING, "Later in 2020, we will remove GWT-RPC support for Guava types. You are seeing this warning because you are sending a Guava type over GWT-RPC, which will break. You can identify which type by looking at the class name in the attached stack trace.", new Throwable());
      }
   }

   private static final class JdkPatternCompiler implements PatternCompiler {
      private JdkPatternCompiler() {
      }

      public CommonPattern compile(String pattern) {
         return new JdkPattern(Pattern.compile(pattern));
      }

      public boolean isPcreLike() {
         return true;
      }

      // $FF: synthetic method
      JdkPatternCompiler(Object x0) {
         this();
      }
   }
}
