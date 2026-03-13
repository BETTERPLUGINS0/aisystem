package tntrun.messages;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import tntrun.TNTRun;

public class Language {
   private TNTRun plugin;
   public static final String PATH = "lang/";
   public static final String MSGFILE = "/messages.yml";

   public Language(TNTRun plugin) {
      this.plugin = plugin;
   }

   public void updateLangFile(File messageconfig) {
      if (!messageconfig.exists()) {
         if (this.plugin.getResource("lang/" + this.getLang() + "/messages.yml") == null) {
            this.plugin.getLogger().info("Requested resource is not present: " + this.getLang());
            return;
         }

         if (!Files.isDirectory(this.plugin.getDataFolder().toPath(), new LinkOption[0])) {
            return;
         }

         try {
            Files.copy(this.plugin.getResource("lang/" + this.getLang() + "/messages.yml"), (new File(this.plugin.getDataFolder(), "/messages.yml")).toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
         } catch (IOException var3) {
            this.plugin.getLogger().info("Error copying file " + String.valueOf(messageconfig));
            var3.printStackTrace();
         }
      }

   }

   public String getLang() {
      return this.plugin.getConfig().getString("language", "en-GB");
   }

   public void setLang(String langDesc) {
      this.plugin.getConfig().set("language", this.getLangCode(langDesc));
      this.plugin.saveConfig();
   }

   public List<String> getTranslatedLanguages() {
      return (List)Stream.of(EnumLang.values()).filter(EnumLang::isSupported).map(EnumLang::getDesc).collect(Collectors.toList());
   }

   private String getLangCode(String langDesc) {
      return (String)Stream.of(EnumLang.values()).filter((e) -> {
         return e.getDesc().equals(langDesc);
      }).map(EnumLang::getCode).findFirst().orElse("en-GB");
   }
}
