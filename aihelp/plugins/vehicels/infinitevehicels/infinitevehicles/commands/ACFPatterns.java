package me.PM2.infinitevehicles.commands;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import me.PM2.infinitevehicles.commands.lib.expiringmap.ExpirationPolicy;
import me.PM2.infinitevehicles.commands.lib.expiringmap.ExpiringMap;

final class ACFPatterns {
   public static final Pattern COMMA = Pattern.compile(",");
   public static final Pattern PERCENTAGE = Pattern.compile("%", 16);
   public static final Pattern NEWLINE = Pattern.compile("\n");
   public static final Pattern DASH = Pattern.compile("-");
   public static final Pattern UNDERSCORE = Pattern.compile("_");
   public static final Pattern SPACE = Pattern.compile(" ");
   public static final Pattern SEMICOLON = Pattern.compile(";");
   public static final Pattern COLON = Pattern.compile(":");
   public static final Pattern COLONEQUALS = Pattern.compile("([:=])");
   public static final Pattern PIPE = Pattern.compile("\\|");
   public static final Pattern NON_ALPHA_NUMERIC = Pattern.compile("[^a-zA-Z0-9]");
   public static final Pattern INTEGER = Pattern.compile("^[0-9]+$");
   public static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_$]{1,16}$");
   public static final Pattern NON_PRINTABLE_CHARACTERS = Pattern.compile("[^\\x20-\\x7F]");
   public static final Pattern EQUALS = Pattern.compile("=");
   public static final Pattern FORMATTER = Pattern.compile("<c(?<color>\\d+)>(?<msg>.*?)</c\\1>", 2);
   public static final Pattern I18N_STRING = Pattern.compile("\\{@@(?<key>.+?)}", 2);
   public static final Pattern REPLACEMENT_PATTERN = Pattern.compile("%\\{.[^\\s]*}");
   static final Map<String, Pattern> patternCache;

   private ACFPatterns() {
   }

   public static Pattern getPattern(String pattern) {
      return (Pattern)patternCache.computeIfAbsent(var0, (var1) -> {
         return Pattern.compile(var0);
      });
   }

   static {
      patternCache = ExpiringMap.builder().maxSize(200).expiration(1L, TimeUnit.HOURS).expirationPolicy(ExpirationPolicy.ACCESSED).build();
   }
}
