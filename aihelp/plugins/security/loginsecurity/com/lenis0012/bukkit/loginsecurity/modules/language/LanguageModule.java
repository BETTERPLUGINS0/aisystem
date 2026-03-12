package com.lenis0012.bukkit.loginsecurity.modules.language;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules.Module;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class LanguageModule extends Module<LoginSecurity> {
   private Translation translation;

   public LanguageModule(LoginSecurity plugin) {
      super(plugin);
   }

   public void enable() {
      this.logger().log(Level.INFO, "Loading base translations from \"en_us\"");
      Translation base = this.byResource("en_us", (Translation)null);
      String languageCode = LoginSecurity.getConfiguration().getLanguage();
      this.logger().log(Level.INFO, "Loading specified translations from \"" + languageCode + "\"");
      File file = new File(((LoginSecurity)this.plugin).getDataFolder(), languageCode + ".json");
      if (!languageCode.equalsIgnoreCase("en_us") && !languageCode.equalsIgnoreCase("default")) {
         if (file.exists()) {
            this.translation = this.byFile(file, base);
         } else {
            try {
               this.translation = this.byResource(languageCode, base);
            } catch (Exception var5) {
               this.logger().log(Level.WARNING, "Can't find translation for " + languageCode + ". Are you upt to date?");
               this.translation = base;
            }
         }
      } else {
         this.translation = base;
      }

   }

   public void disable() {
   }

   private Translation byFile(File file, Translation fallback) {
      String name = file.getName().split("\\.")[0];

      try {
         InputStream input = new FileInputStream(file);
         return new Translation(fallback, new InputStreamReader(input, StandardCharsets.UTF_8), name);
      } catch (IOException var5) {
         throw new RuntimeException("Couldn't read internal language file", var5);
      }
   }

   private Translation byResource(String name, Translation fallback) {
      try {
         InputStream input = ((LoginSecurity)this.plugin).getResource("lang/" + name + ".json");
         return new Translation(fallback, new InputStreamReader(input, StandardCharsets.UTF_8), name);
      } catch (IOException var4) {
         throw new RuntimeException("Couldn't read internal language file", var4);
      }
   }

   public Translation getTranslation() {
      return this.translation;
   }

   public TranslatedMessage translate(LanguageKeys key) {
      return this.translate(key.toString());
   }

   public TranslatedMessage translate(String rawKey) {
      return this.translation.translate(rawKey);
   }
}
