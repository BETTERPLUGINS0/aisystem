package me.PM2.infinitevehicles.commands;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

public class CommandReplacements {
   private final CommandManager manager;
   private final Map<String, Entry<Pattern, String>> replacements = new LinkedHashMap();

   CommandReplacements(CommandManager manager) {
      this.manager = var1;
      this.addReplacement0("truthy", "true|false|yes|no|1|0|on|off|t|f");
   }

   public void addReplacements(String... replacements) {
      if (var1.length != 0 && var1.length % 2 == 0) {
         for(int var2 = 0; var2 < var1.length; var2 += 2) {
            this.addReplacement(var1[var2], var1[var2 + 1]);
         }

      } else {
         throw new IllegalArgumentException("Must pass a number of arguments divisible by 2.");
      }
   }

   public String addReplacement(String key, String val) {
      return this.addReplacement0(var1, var2);
   }

   @Nullable
   private String addReplacement0(String key, String val) {
      var1 = ACFPatterns.PERCENTAGE.matcher(var1.toLowerCase(Locale.ENGLISH)).replaceAll("");
      Pattern var3 = Pattern.compile("%\\{" + Pattern.quote(var1) + "}|%" + Pattern.quote(var1) + "\\b", 2);
      SimpleImmutableEntry var4 = new SimpleImmutableEntry(var3, var2);
      Entry var5 = (Entry)this.replacements.put(var1, var4);
      return var5 != null ? (String)var5.getValue() : null;
   }

   public String replace(String text) {
      if (var1 == null) {
         return null;
      } else {
         Entry var3;
         for(Iterator var2 = this.replacements.values().iterator(); var2.hasNext(); var1 = ((Pattern)var3.getKey()).matcher(var1).replaceAll((String)var3.getValue())) {
            var3 = (Entry)var2.next();
         }

         Matcher var4 = ACFPatterns.REPLACEMENT_PATTERN.matcher(var1);

         while(var4.find()) {
            this.manager.log(LogLevel.ERROR, "Found unregistered replacement: " + var4.group());
         }

         return var1;
      }
   }
}
