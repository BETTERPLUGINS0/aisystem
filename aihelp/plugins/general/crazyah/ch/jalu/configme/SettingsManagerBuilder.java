package ch.jalu.configme;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.migration.PlainMigrationService;
import ch.jalu.configme.resource.PropertyResource;
import ch.jalu.configme.resource.YamlFileResource;
import ch.jalu.configme.resource.YamlFileResourceOptions;
import ch.jalu.configme.utils.Utils;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SettingsManagerBuilder {
   private final PropertyResource resource;
   private ConfigurationData configurationData;
   @Nullable
   private MigrationService migrationService;

   private SettingsManagerBuilder(@NotNull PropertyResource resource) {
      this.resource = resource;
   }

   @NotNull
   public static SettingsManagerBuilder withYamlFile(@NotNull Path file) {
      return withYamlFile(file, YamlFileResourceOptions.builder().build());
   }

   @NotNull
   public static SettingsManagerBuilder withYamlFile(@NotNull File file) {
      return withYamlFile(file.toPath());
   }

   @NotNull
   public static SettingsManagerBuilder withYamlFile(@NotNull Path path, @NotNull YamlFileResourceOptions resourceOptions) {
      Utils.createFileIfNotExists(path);
      return new SettingsManagerBuilder(new YamlFileResource(path, resourceOptions));
   }

   @NotNull
   public static SettingsManagerBuilder withYamlFile(@NotNull File file, @NotNull YamlFileResourceOptions resourceOptions) {
      return withYamlFile(file.toPath(), resourceOptions);
   }

   @NotNull
   public static SettingsManagerBuilder withResource(@NotNull PropertyResource resource) {
      return new SettingsManagerBuilder(resource);
   }

   @SafeVarargs
   @NotNull
   public final SettingsManagerBuilder configurationData(@NotNull Class<? extends SettingsHolder>... classes) {
      this.configurationData = ConfigurationDataBuilder.createConfiguration(classes);
      return this;
   }

   @NotNull
   public SettingsManagerBuilder configurationData(@NotNull ConfigurationData configurationData) {
      this.configurationData = configurationData;
      return this;
   }

   @NotNull
   public SettingsManagerBuilder migrationService(@Nullable MigrationService migrationService) {
      this.migrationService = migrationService;
      return this;
   }

   @NotNull
   public SettingsManagerBuilder useDefaultMigrationService() {
      this.migrationService = new PlainMigrationService();
      return this;
   }

   @NotNull
   public SettingsManager create() {
      Objects.requireNonNull(this.resource, "resource");
      Objects.requireNonNull(this.configurationData, "configurationData");
      return new SettingsManagerImpl(this.resource, this.configurationData, this.migrationService);
   }
}
