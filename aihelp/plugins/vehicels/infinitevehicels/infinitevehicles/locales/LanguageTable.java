package me.PM2.infinitevehicles.locales;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.jetbrains.annotations.NotNull;

public class LanguageTable {
   private final Locale locale;
   private final Map<MessageKey, String> messages = new HashMap();

   LanguageTable(Locale locale) {
      this.locale = var1;
   }

   public String addMessage(MessageKey key, String message) {
      return (String)this.messages.put(var1, var2);
   }

   public String getMessage(MessageKey key) {
      return (String)this.messages.get(var1);
   }

   public void addMessages(@NotNull Map<MessageKey, String> messages) {
      this.messages.putAll(var1);
   }

   public Locale getLocale() {
      return this.locale;
   }

   public boolean addMessageBundle(String bundleName) {
      return this.addMessageBundle(this.getClass().getClassLoader(), var1);
   }

   public boolean addMessageBundle(ClassLoader classLoader, String bundleName) {
      try {
         return this.addResourceBundle(ResourceBundle.getBundle(var2, this.locale, var1, new UTF8Control()));
      } catch (MissingResourceException var4) {
         return false;
      }
   }

   public boolean addResourceBundle(ResourceBundle bundle) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.addMessage(MessageKey.of(var3), var1.getString(var3));
      }

      return !var1.keySet().isEmpty();
   }
}
