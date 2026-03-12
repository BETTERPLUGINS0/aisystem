package emanondev.itemedit.utility;

import emanondev.itemedit.aliases.IAliasSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CompleteUtility {
   private static final int MAX_COMPLETES = 200;

   private CompleteUtility() {
      throw new UnsupportedOperationException();
   }

   @NotNull
   public static <T extends Enum<T>> List<String> complete(@NotNull String prefix, @NotNull Class<T> enumClass) {
      prefix = prefix.toUpperCase();
      ArrayList<String> results = new ArrayList();
      int c = 0;
      Enum[] var4 = (Enum[])enumClass.getEnumConstants();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         T el = var4[var6];
         if (el.toString().startsWith(prefix)) {
            results.add(el.toString().toLowerCase(Locale.ENGLISH));
            ++c;
            if (c > 200) {
               return results;
            }
         }
      }

      return results;
   }

   @NotNull
   public static <T extends Enum<T>> List<String> complete(@NotNull String prefix, @NotNull Class<T> type, @NotNull Predicate<T> predicate) {
      prefix = prefix.toUpperCase();
      ArrayList<String> results = new ArrayList();
      int c = 0;
      Enum[] var5 = (Enum[])type.getEnumConstants();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         T el = var5[var7];
         if (predicate.test(el) && el.toString().startsWith(prefix)) {
            results.add(el.toString().toLowerCase(Locale.ENGLISH));
            ++c;
            if (c > 200) {
               return results;
            }
         }
      }

      return results;
   }

   @NotNull
   public static List<String> complete(@NotNull String prefix, String... list) {
      prefix = prefix.toLowerCase(Locale.ENGLISH);
      ArrayList<String> results = new ArrayList();
      int c = 0;
      String[] var4 = list;
      int var5 = list.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String value = var4[var6];
         if (value.toLowerCase(Locale.ENGLISH).startsWith(prefix)) {
            results.add(value);
            ++c;
            if (c > 200) {
               return results;
            }
         }
      }

      return results;
   }

   @NotNull
   public static List<String> complete(@NotNull String prefix, @NotNull Collection<String> list) {
      prefix = prefix.toLowerCase(Locale.ENGLISH);
      ArrayList<String> results = new ArrayList();
      int c = 0;
      Iterator var4 = list.iterator();

      while(var4.hasNext()) {
         String value = (String)var4.next();
         if (value.toLowerCase(Locale.ENGLISH).startsWith(prefix)) {
            results.add(value);
            ++c;
            if (c > 200) {
               return results;
            }
         }
      }

      return results;
   }

   @NotNull
   public static <T> List<String> complete(@NotNull String prefix, @NotNull Iterable<T> list, @NotNull Function<T, String> converter) {
      prefix = prefix.toLowerCase(Locale.ENGLISH);
      ArrayList<String> results = new ArrayList();
      int c = 0;
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         T value = var5.next();
         String textValue = (String)converter.apply(value);
         if (textValue != null && textValue.toLowerCase(Locale.ENGLISH).startsWith(prefix)) {
            results.add(textValue);
            ++c;
            if (c > 200) {
               return results;
            }
         }
      }

      return results;
   }

   @NotNull
   public static List<String> completePlayers(@NotNull String prefix) {
      ArrayList<String> names = new ArrayList();
      String text = prefix.toLowerCase(Locale.ENGLISH);
      Bukkit.getOnlinePlayers().forEach((p) -> {
         if (p.getName().toLowerCase(Locale.ENGLISH).startsWith(text)) {
            names.add(p.getName());
         }

      });
      return names;
   }

   @NotNull
   public static List<String> complete(@NotNull String prefix, @Nullable IAliasSet<?> aliases) {
      return complete(prefix, (IAliasSet)aliases, (Predicate)null);
   }

   @NotNull
   public static <T> List<String> complete(@NotNull String prefix, @Nullable IAliasSet<T> aliases, @Nullable Predicate<T> filter) {
      if (aliases == null) {
         return Collections.emptyList();
      } else {
         ArrayList<String> results = new ArrayList();
         prefix = prefix.toLowerCase(Locale.ENGLISH);
         int c = 0;
         Iterator var5 = aliases.getAliases().iterator();

         while(true) {
            String alias;
            do {
               if (!var5.hasNext()) {
                  return results;
               }

               alias = (String)var5.next();
            } while(filter != null && !filter.test(aliases.convertAlias(alias)));

            if (alias.startsWith(prefix)) {
               results.add(alias);
               ++c;
               if (c > 200) {
                  return results;
               }
            }
         }
      }
   }
}
