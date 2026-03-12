package fr.xephi.authme.settings;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.ch.jalu.configme.SettingsManagerImpl;
import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.migration.MigrationService;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyResource;
import fr.xephi.authme.libs.com.google.common.io.Files;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Settings extends SettingsManagerImpl {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(Settings.class);
   private final File pluginFolder;
   private String passwordEmailMessage;
   private String verificationEmailMessage;
   private String recoveryCodeEmailMessage;
   private String shutdownEmailMessage;
   private String newPasswordEmailMessage;

   public Settings(File pluginFolder, PropertyResource resource, MigrationService migrationService, ConfigurationData configurationData) {
      super(resource, configurationData, migrationService);
      this.pluginFolder = pluginFolder;
      this.loadSettingsFromFiles();
   }

   public String getPasswordEmailMessage() {
      return this.passwordEmailMessage;
   }

   public String getVerificationEmailMessage() {
      return this.verificationEmailMessage;
   }

   public String getRecoveryCodeEmailMessage() {
      return this.recoveryCodeEmailMessage;
   }

   public String getShutdownEmailMessage() {
      return this.shutdownEmailMessage;
   }

   public String getNewPasswordEmailMessage() {
      return this.newPasswordEmailMessage;
   }

   private void loadSettingsFromFiles() {
      this.newPasswordEmailMessage = this.readFile("new_email.html");
      this.passwordEmailMessage = this.readFile("email.html");
      this.verificationEmailMessage = this.readFile("verification_code_email.html");
      this.recoveryCodeEmailMessage = this.readFile("recovery_code_email.html");
      this.shutdownEmailMessage = this.readFile("shutdown.html");
      String country = this.readFile("GeoLite2-Country.mmdb");
   }

   public void reload() {
      super.reload();
      this.loadSettingsFromFiles();
   }

   private String readFile(String filename) {
      File file = new File(this.pluginFolder, filename);
      if (FileUtils.copyFileFromResource(file, filename)) {
         try {
            return Files.asCharSource(file, StandardCharsets.UTF_8).read();
         } catch (IOException var4) {
            this.logger.logException("Failed to read file '" + filename + "':", var4);
         }
      } else {
         this.logger.warning("Failed to copy file '" + filename + "' from JAR");
      }

      return "";
   }
}
