package me.PM2.infinitevehicles.commands;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import me.PM2.infinitevehicles.locales.LocaleManager;
import me.PM2.infinitevehicles.locales.MessageKey;
import me.PM2.infinitevehicles.locales.MessageKeyProvider;
import org.jetbrains.annotations.NotNull;

public class Locales {
   public static final Locale ENGLISH;
   public static final Locale GERMAN;
   public static final Locale FRENCH;
   public static final Locale JAPANESE;
   public static final Locale ITALIAN;
   public static final Locale KOREAN;
   public static final Locale CHINESE;
   public static final Locale SIMPLIFIED_CHINESE;
   public static final Locale TRADITIONAL_CHINESE;
   public static final Locale SPANISH;
   public static final Locale DUTCH;
   public static final Locale DANISH;
   public static final Locale CZECH;
   public static final Locale GREEK;
   public static final Locale LATIN;
   public static final Locale BULGARIAN;
   public static final Locale AFRIKAANS;
   public static final Locale HINDI;
   public static final Locale HEBREW;
   public static final Locale POLISH;
   public static final Locale PORTUGUESE;
   public static final Locale FINNISH;
   public static final Locale SWEDISH;
   public static final Locale RUSSIAN;
   public static final Locale ROMANIAN;
   public static final Locale VIETNAMESE;
   public static final Locale THAI;
   public static final Locale TURKISH;
   public static final Locale UKRANIAN;
   public static final Locale ARABIC;
   public static final Locale WELSH;
   public static final Locale NORWEGIAN_BOKMAAL;
   public static final Locale NORWEGIAN_NYNORSK;
   public static final Locale HUNGARIAN;
   private final CommandManager manager;
   private final LocaleManager<CommandIssuer> localeManager;
   private final Map<ClassLoader, SetMultimap<String, Locale>> loadedBundles = new HashMap();
   private final List<ClassLoader> registeredClassLoaders = new ArrayList();

   public Locales(CommandManager manager) {
      this.manager = var1;
      Objects.requireNonNull(var1);
      this.localeManager = LocaleManager.create(var1::getIssuerLocale);
      this.addBundleClassLoader(this.getClass().getClassLoader());
   }

   public void loadLanguages() {
      this.addMessageBundles("acf-core");
   }

   public Locale getDefaultLocale() {
      return this.localeManager.getDefaultLocale();
   }

   public Locale setDefaultLocale(Locale locale) {
      return this.localeManager.setDefaultLocale(var1);
   }

   public void loadMissingBundles() {
      Set var1 = this.manager.getSupportedLanguages();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Locale var3 = (Locale)var2.next();
         Iterator var4 = this.loadedBundles.values().iterator();

         while(var4.hasNext()) {
            SetMultimap var5 = (SetMultimap)var4.next();
            Iterator var6 = (new HashSet(var5.keys())).iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               this.addMessageBundle(var7, var3);
            }
         }
      }

   }

   public void addMessageBundles(String... bundleNames) {
      String[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         Set var6 = this.manager.getSupportedLanguages();
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            Locale var8 = (Locale)var7.next();
            this.addMessageBundle(var5, var8);
         }
      }

   }

   public boolean addMessageBundle(String bundleName, Locale locale) {
      boolean var3 = false;
      Iterator var4 = this.registeredClassLoaders.iterator();

      while(var4.hasNext()) {
         ClassLoader var5 = (ClassLoader)var4.next();
         if (this.addMessageBundle(var5, var1, var2)) {
            var3 = true;
         }
      }

      return var3;
   }

   public boolean addMessageBundle(ClassLoader classLoader, String bundleName, Locale locale) {
      SetMultimap var4 = (SetMultimap)this.loadedBundles.getOrDefault(var1, HashMultimap.create());
      if (!var4.containsEntry(var2, var3) && this.localeManager.addMessageBundle(var1, var2, var3)) {
         var4.put(var2, var3);
         this.loadedBundles.put(var1, var4);
         return true;
      } else {
         return false;
      }
   }

   public void addMessageStrings(Locale locale, @NotNull Map<String, String> messages) {
      HashMap var3 = new HashMap(var2.size());
      var2.forEach((var1x, var2x) -> {
         var3.put(MessageKey.of(var1x), var2x);
      });
      this.localeManager.addMessages(var1, var3);
   }

   public void addMessages(Locale locale, @NotNull Map<? extends MessageKeyProvider, String> messages) {
      LinkedHashMap var3 = new LinkedHashMap();
      Iterator var4 = var2.entrySet().iterator();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         var3.put(((MessageKeyProvider)var5.getKey()).getMessageKey(), (String)var5.getValue());
      }

      this.localeManager.addMessages(var1, var3);
   }

   public String addMessage(Locale locale, MessageKeyProvider key, String message) {
      return this.localeManager.addMessage(var1, var2.getMessageKey(), var3);
   }

   public String getMessage(CommandIssuer issuer, MessageKeyProvider key) {
      MessageKey var3 = var2.getMessageKey();
      String var4 = this.localeManager.getMessage(var1, var3);
      if (var4 == null) {
         this.manager.log(LogLevel.ERROR, "Missing Language Key: " + var3.getKey());
         var4 = "<MISSING_LANGUAGE_KEY:" + var3.getKey() + ">";
      }

      return var4;
   }

   public String getOptionalMessage(CommandIssuer issuer, MessageKey key) {
      return var1 == null ? this.localeManager.getTable(this.getDefaultLocale()).getMessage(var2) : this.localeManager.getMessage(var1, var2);
   }

   public String replaceI18NStrings(String message) {
      if (var1 == null) {
         return null;
      } else {
         Matcher var2 = ACFPatterns.I18N_STRING.matcher(var1);
         if (!var2.find()) {
            return var1;
         } else {
            CommandIssuer var3 = CommandManager.getCurrentCommandIssuer();
            var2.reset();
            StringBuffer var4 = new StringBuffer(var1.length());

            while(var2.find()) {
               MessageKey var5 = MessageKey.of(var2.group("key"));
               var2.appendReplacement(var4, Matcher.quoteReplacement(this.getMessage(var3, var5)));
            }

            var2.appendTail(var4);
            return var4.toString();
         }
      }
   }

   public boolean addBundleClassLoader(ClassLoader classLoader) {
      return !this.registeredClassLoaders.contains(var1) && this.registeredClassLoaders.add(var1);
   }

   static {
      ENGLISH = Locale.ENGLISH;
      GERMAN = Locale.GERMAN;
      FRENCH = Locale.FRENCH;
      JAPANESE = Locale.JAPANESE;
      ITALIAN = Locale.ITALIAN;
      KOREAN = Locale.KOREAN;
      CHINESE = Locale.CHINESE;
      SIMPLIFIED_CHINESE = Locale.SIMPLIFIED_CHINESE;
      TRADITIONAL_CHINESE = Locale.TRADITIONAL_CHINESE;
      SPANISH = new Locale("es");
      DUTCH = new Locale("nl");
      DANISH = new Locale("da");
      CZECH = new Locale("cs");
      GREEK = new Locale("el");
      LATIN = new Locale("la");
      BULGARIAN = new Locale("bg");
      AFRIKAANS = new Locale("af");
      HINDI = new Locale("hi");
      HEBREW = new Locale("he");
      POLISH = new Locale("pl");
      PORTUGUESE = new Locale("pt");
      FINNISH = new Locale("fi");
      SWEDISH = new Locale("sv");
      RUSSIAN = new Locale("ru");
      ROMANIAN = new Locale("ro");
      VIETNAMESE = new Locale("vi");
      THAI = new Locale("th");
      TURKISH = new Locale("tr");
      UKRANIAN = new Locale("uk");
      ARABIC = new Locale("ar");
      WELSH = new Locale("cy");
      NORWEGIAN_BOKMAAL = new Locale("nb");
      NORWEGIAN_NYNORSK = new Locale("nn");
      HUNGARIAN = new Locale("hu");
   }
}
