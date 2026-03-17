package me.PM2.infinitevehicles.locales;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public class LocaleManager<T> {
   private final Function<T, Locale> localeMapper;
   private Locale defaultLocale;
   private final Map<Locale, LanguageTable> tables = new HashMap();

   LocaleManager(Function<T, Locale> localeMapper, Locale defaultLocale) {
      this.localeMapper = var1;
      this.defaultLocale = var2;
   }

   public static <T> LocaleManager<T> create(@NotNull Function<T, Locale> localeMapper) {
      return new LocaleManager(var0, Locale.ENGLISH);
   }

   public static <T> LocaleManager<T> create(@NotNull Function<T, Locale> localeMapper, Locale defaultLocale) {
      return new LocaleManager(var0, var1);
   }

   public Locale getDefaultLocale() {
      return this.defaultLocale;
   }

   public Locale setDefaultLocale(Locale defaultLocale) {
      Locale var2 = this.defaultLocale;
      this.defaultLocale = var1;
      return var2;
   }

   public boolean addMessageBundle(@NotNull String bundleName, @NotNull Locale... locales) {
      return this.addMessageBundle(this.getClass().getClassLoader(), var1, var2);
   }

   public boolean addMessageBundle(@NotNull ClassLoader classLoader, @NotNull String bundleName, @NotNull Locale... locales) {
      if (var3.length == 0) {
         var3 = new Locale[]{this.defaultLocale};
      }

      boolean var4 = false;
      Locale[] var5 = var3;
      int var6 = var3.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Locale var8 = var5[var7];
         if (this.getTable(var8).addMessageBundle(var1, var2)) {
            var4 = true;
         }
      }

      return var4;
   }

   public void addMessages(@NotNull Locale locale, @NotNull Map<MessageKey, String> messages) {
      this.getTable(var1).addMessages(var2);
   }

   public String addMessage(@NotNull Locale locale, @NotNull MessageKey key, @NotNull String message) {
      return this.getTable(var1).addMessage(var2, var3);
   }

   public String getMessage(T context, @NotNull MessageKey key) {
      Locale var3 = (Locale)this.localeMapper.apply(var1);
      String var4 = this.getTable(var3).getMessage(var2);
      if (var4 == null && !var3.getCountry().isEmpty()) {
         var4 = this.getTable(new Locale(var3.getLanguage())).getMessage(var2);
      }

      if (var4 == null && !Objects.equals(var3, this.defaultLocale)) {
         var4 = this.getTable(this.defaultLocale).getMessage(var2);
      }

      return var4;
   }

   @NotNull
   public LanguageTable getTable(@NotNull Locale locale) {
      return (LanguageTable)this.tables.computeIfAbsent(var1, LanguageTable::new);
   }

   public boolean addResourceBundle(ResourceBundle bundle, Locale locale) {
      return this.getTable(var2).addResourceBundle(var1);
   }
}
