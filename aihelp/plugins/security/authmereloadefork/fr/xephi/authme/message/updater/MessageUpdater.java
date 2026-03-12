package fr.xephi.authme.message.updater;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.PropertyListBuilder;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.properties.StringProperty;
import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyResource;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.common.io.Files;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageUpdater {
   private ConsoleLogger logger = ConsoleLoggerFactory.get(MessageUpdater.class);

   public boolean migrateAndSave(File userFile, String localJarPath, String defaultJarPath) {
      JarMessageSource jarMessageSource = new JarMessageSource(localJarPath, defaultJarPath);
      return this.migrateAndSave(userFile, jarMessageSource);
   }

   private boolean migrateAndSave(File userFile, JarMessageSource jarMessageSource) {
      MessageKeyConfigurationData configurationData = createConfigurationData();
      PropertyResource userResource = new MigraterYamlFileResource(userFile);
      PropertyReader reader = userResource.createReader();
      configurationData.initializeValues(reader);
      boolean movedOldKeys = this.migrateOldKeys(reader, configurationData);
      boolean movedNewerKeys = this.migrateKeys(reader, configurationData);
      boolean addedMissingKeys = this.addMissingKeys(jarMessageSource, configurationData);
      if (!movedOldKeys && !movedNewerKeys && !addedMissingKeys) {
         return false;
      } else {
         backupMessagesFile(userFile);
         userResource.exportProperties(configurationData);
         this.logger.debug("Successfully saved {0}", (Object)userFile);
         return true;
      }
   }

   private boolean migrateKeys(PropertyReader propertyReader, MessageKeyConfigurationData configurationData) {
      return moveIfApplicable(propertyReader, configurationData, "misc.two_factor_create", MessageKey.TWO_FACTOR_CREATE);
   }

   private static boolean moveIfApplicable(PropertyReader reader, MessageKeyConfigurationData configurationData, String oldPath, MessageKey messageKey) {
      if (configurationData.getMessage(messageKey) == null && reader.getString(oldPath) != null) {
         configurationData.setMessage(messageKey, reader.getString(oldPath));
         return true;
      } else {
         return false;
      }
   }

   private boolean migrateOldKeys(PropertyReader propertyReader, MessageKeyConfigurationData configurationData) {
      boolean hasChange = OldMessageKeysMigrater.migrateOldPaths(propertyReader, configurationData);
      if (hasChange) {
         this.logger.info("Old keys have been moved to the new ones in your messages_xx.yml file");
      }

      return hasChange;
   }

   private boolean addMissingKeys(JarMessageSource jarMessageSource, MessageKeyConfigurationData configurationData) {
      List<String> addedKeys = new ArrayList();
      Iterator var4 = configurationData.getAllMessageProperties().iterator();

      while(var4.hasNext()) {
         Property<String> property = (Property)var4.next();
         String key = property.getPath();
         if (configurationData.getValue(property) == null) {
            configurationData.setValue(property, jarMessageSource.getMessageFromJar(property));
            addedKeys.add(key);
         }
      }

      if (!addedKeys.isEmpty()) {
         this.logger.info("Added " + addedKeys.size() + " missing keys to your messages_xx.yml file: " + addedKeys);
         return true;
      } else {
         return false;
      }
   }

   private static void backupMessagesFile(File messagesFile) {
      String backupName = FileUtils.createBackupFilePath(messagesFile);
      File backupFile = new File(backupName);

      try {
         Files.copy(messagesFile, backupFile);
      } catch (IOException var4) {
         throw new IllegalStateException("Could not back up '" + messagesFile + "' to '" + backupFile + "'", var4);
      }
   }

   public static MessageKeyConfigurationData createConfigurationData() {
      Map<String, String> comments = ImmutableMap.builder().put("registration", "Registration").put("password", "Password errors on registration").put("login", "Login").put("error", "Errors").put("antibot", "AntiBot").put("unregister", "Unregister").put("misc", "Other messages").put("session", "Session messages").put("on_join_validation", "Error messages when joining").put("email", "Email").put("recovery", "Password recovery by email").put("captcha", "Captcha").put("verification", "Verification code").put("time", "Time units").put("two_factor", "Two-factor authentication").put("bedrock_auto_login", "3rd party features: Bedrock Auto Login").put("login_location_fix", "3rd party features: Login Location Fix").put("double_login_fix", "3rd party features: Double Login Fix").build();
      Set<String> addedKeys = new HashSet();
      MessageUpdater.MessageKeyPropertyListBuilder builder = new MessageUpdater.MessageKeyPropertyListBuilder();
      Iterator var3 = comments.keySet().iterator();

      while(var3.hasNext()) {
         String path = (String)var3.next();
         MessageKey key = (MessageKey)Arrays.stream(MessageKey.values()).filter((p) -> {
            return p.getKey().startsWith(path + ".");
         }).findFirst().orElseThrow(() -> {
            return new IllegalStateException(path);
         });
         builder.addMessageKey(key);
         addedKeys.add(key.getKey());
      }

      Stream var10000 = Arrays.stream(MessageKey.values()).filter((keyx) -> {
         return !addedKeys.contains(keyx.getKey());
      });
      Objects.requireNonNull(builder);
      var10000.forEach(builder::addMessageKey);
      Map<String, List<String>> commentsMap = (Map)comments.entrySet().stream().collect(Collectors.toMap(Entry::getKey, (e) -> {
         return Collections.singletonList((String)e.getValue());
      }));
      return new MessageKeyConfigurationData(builder, commentsMap);
   }

   static final class MessageKeyPropertyListBuilder {
      private PropertyListBuilder propertyListBuilder = new PropertyListBuilder();

      void addMessageKey(MessageKey key) {
         this.propertyListBuilder.add(new MessageUpdater.MessageKeyProperty(key));
      }

      List<MessageUpdater.MessageKeyProperty> getAllProperties() {
         return this.propertyListBuilder.create();
      }
   }

   static final class MessageKeyProperty extends StringProperty {
      MessageKeyProperty(MessageKey messageKey) {
         super(messageKey.getKey(), "");
      }

      protected String getFromReader(PropertyReader reader, ConvertErrorRecorder errorRecorder) {
         return reader.getString(this.getPath());
      }
   }
}
