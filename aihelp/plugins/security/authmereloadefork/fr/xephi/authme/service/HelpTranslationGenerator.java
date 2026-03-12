package fr.xephi.authme.service;

import fr.xephi.authme.command.CommandArgumentDescription;
import fr.xephi.authme.command.CommandDescription;
import fr.xephi.authme.command.CommandInitializer;
import fr.xephi.authme.command.help.HelpMessage;
import fr.xephi.authme.command.help.HelpMessagesService;
import fr.xephi.authme.command.help.HelpSection;
import fr.xephi.authme.initialization.DataFolder;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessagePathHelper;
import fr.xephi.authme.permission.DefaultPermission;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

public class HelpTranslationGenerator {
   @Inject
   private CommandInitializer commandInitializer;
   @Inject
   private HelpMessagesService helpMessagesService;
   @Inject
   private Settings settings;
   @DataFolder
   @Inject
   private File dataFolder;

   public File updateHelpFile() throws IOException {
      String languageCode = (String)this.settings.getProperty(PluginSettings.MESSAGES_LANGUAGE);
      File helpFile = new File(this.dataFolder, MessagePathHelper.createHelpMessageFilePath(languageCode));
      Map<String, Object> helpEntries = this.generateHelpMessageEntries();
      String helpEntriesYaml = exportToYaml(helpEntries);
      Files.write(helpFile.toPath(), helpEntriesYaml.getBytes(), new OpenOption[]{StandardOpenOption.TRUNCATE_EXISTING});
      return helpFile;
   }

   private static String exportToYaml(Map<String, Object> helpEntries) {
      DumperOptions options = new DumperOptions();
      options.setDefaultFlowStyle(FlowStyle.BLOCK);
      options.setAllowUnicode(true);
      return (new Yaml(options)).dump(helpEntries);
   }

   private Map<String, Object> generateHelpMessageEntries() {
      Map<String, Object> messageEntries = new LinkedHashMap(HelpMessage.values().length);
      HelpMessage[] var2 = HelpMessage.values();
      int var3 = var2.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         HelpMessage message = var2[var4];
         messageEntries.put(message.getEntryKey(), this.helpMessagesService.getMessage(message));
      }

      Map<String, String> defaultPermissions = new LinkedHashMap();
      DefaultPermission[] var9 = DefaultPermission.values();
      var4 = var9.length;

      int var12;
      for(var12 = 0; var12 < var4; ++var12) {
         DefaultPermission defaultPermission = var9[var12];
         defaultPermissions.put(HelpMessagesService.getDefaultPermissionsSubPath(defaultPermission), this.helpMessagesService.getMessage(defaultPermission));
      }

      messageEntries.put("defaultPermissions", defaultPermissions);
      Map<String, String> sectionEntries = new LinkedHashMap(HelpSection.values().length);
      HelpSection[] var11 = HelpSection.values();
      var12 = var11.length;

      for(int var14 = 0; var14 < var12; ++var14) {
         HelpSection section = var11[var14];
         sectionEntries.put(section.getEntryKey(), this.helpMessagesService.getMessage(section));
      }

      Map<String, Object> commandEntries = new LinkedHashMap();
      Iterator var15 = this.commandInitializer.getCommands().iterator();

      while(var15.hasNext()) {
         CommandDescription command = (CommandDescription)var15.next();
         this.generateCommandEntries(command, commandEntries);
      }

      return ImmutableMap.of("common", messageEntries, "section", sectionEntries, "commands", commandEntries);
   }

   private void generateCommandEntries(CommandDescription command, Map<String, Object> commandEntries) {
      CommandDescription translatedCommand = this.helpMessagesService.buildLocalizedDescription(command);
      Map<String, Object> commandData = new LinkedHashMap();
      commandData.put("description", translatedCommand.getDescription());
      commandData.put("detailedDescription", translatedCommand.getDetailedDescription());
      int i = 1;

      for(Iterator var6 = translatedCommand.getArguments().iterator(); var6.hasNext(); ++i) {
         CommandArgumentDescription argument = (CommandArgumentDescription)var6.next();
         Map<String, String> argumentData = new LinkedHashMap(2);
         argumentData.put("label", argument.getName());
         argumentData.put("description", argument.getDescription());
         commandData.put("arg" + i, argumentData);
      }

      commandEntries.put(HelpMessagesService.getCommandSubPath(translatedCommand), commandData);
      translatedCommand.getChildren().forEach((child) -> {
         this.generateCommandEntries(child, commandEntries);
      });
   }
}
