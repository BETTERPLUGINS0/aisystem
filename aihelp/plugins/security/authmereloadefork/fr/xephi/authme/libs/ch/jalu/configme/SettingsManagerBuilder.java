package fr.xephi.authme.libs.ch.jalu.configme;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import fr.xephi.authme.libs.ch.jalu.configme.migration.MigrationService;
import fr.xephi.authme.libs.ch.jalu.configme.migration.PlainMigrationService;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyResource;
import fr.xephi.authme.libs.ch.jalu.configme.resource.YamlFileResource;
import fr.xephi.authme.libs.ch.jalu.configme.resource.YamlFileResourceOptions;
import fr.xephi.authme.libs.ch.jalu.configme.utils.Utils;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import javax.annotation.Nullable;

public final class SettingsManagerBuilder {
   private final PropertyResource resource;
   private ConfigurationData configurationData;
   private MigrationService migrationService;

   private SettingsManagerBuilder(PropertyResource resource) {
      this.resource = resource;
   }

   public static SettingsManagerBuilder withYamlFile(Path file) {
      return withYamlFile(file, YamlFileResourceOptions.builder().build());
   }

   public static SettingsManagerBuilder withYamlFile(File file) {
      return withYamlFile(file.toPath());
   }

   public static SettingsManagerBuilder withYamlFile(Path path, YamlFileResourceOptions resourceOptions) {
      Utils.createFileIfNotExists(path);
      return new SettingsManagerBuilder(new YamlFileResource(path, resourceOptions));
   }

   public static SettingsManagerBuilder withYamlFile(File file, YamlFileResourceOptions resourceOptions) {
      return withYamlFile(file.toPath(), resourceOptions);
   }

   public static SettingsManagerBuilder withResource(PropertyResource resource) {
      return new SettingsManagerBuilder(resource);
   }

   @SafeVarargs
   public final SettingsManagerBuilder configurationData(Class<? extends SettingsHolder>... classes) {
      this.configurationData = ConfigurationDataBuilder.createConfiguration(classes);
      return this;
   }

   public SettingsManagerBuilder configurationData(ConfigurationData configurationData) {
      this.configurationData = configurationData;
      return this;
   }

   public SettingsManagerBuilder migrationService(@Nullable MigrationService migrationService) {
      this.migrationService = migrationService;
      return this;
   }

   public SettingsManagerBuilder useDefaultMigrationService() {
      this.migrationService = new PlainMigrationService();
      return this;
   }

   public SettingsManager create() {
      Objects.requireNonNull(this.resource, "resource");
      Objects.requireNonNull(this.configurationData, "configurationData");
      return new SettingsManagerImpl(this.resource, this.configurationData, this.migrationService);
   }
}
