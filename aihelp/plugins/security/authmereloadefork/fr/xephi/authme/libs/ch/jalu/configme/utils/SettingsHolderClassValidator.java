package fr.xephi.authme.libs.ch.jalu.configme.utils;

import fr.xephi.authme.libs.ch.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import fr.xephi.authme.libs.ch.jalu.configme.migration.MigrationService;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyResource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class SettingsHolderClassValidator {
   @SafeVarargs
   public final void validate(Class<? extends SettingsHolder>... settingHolders) {
      this.validate((Iterable)Arrays.asList(settingHolders));
   }

   public void validate(Iterable<Class<? extends SettingsHolder>> settingHolders) {
      this.validateAllPropertiesAreConstants(settingHolders);
      this.validateSettingsHolderClassesFinal(settingHolders);
      this.validateClassesHaveHiddenNoArgConstructor(settingHolders);
      ConfigurationData configurationData = this.createConfigurationData(settingHolders);
      this.validateHasCommentOnEveryProperty(configurationData, (Predicate)null);
      this.validateCommentLengthsAreWithinBounds(configurationData, (Integer)null, 90);
      this.validateHasAllEnumEntriesInComment(configurationData, (Predicate)null);
   }

   public void validateConfigurationDataValidForMigrationService(ConfigurationData configurationData, PropertyResource resource, MigrationService migrationService) {
      resource.exportProperties(configurationData);
      PropertyReader reader = resource.createReader();
      if (migrationService.checkAndMigrate(reader, configurationData)) {
         throw new IllegalStateException("Migration service unexpectedly returned that a migration is required");
      }
   }

   public void validateAllPropertiesAreConstants(Iterable<Class<? extends SettingsHolder>> settingHolders) {
      List<String> invalidFields = new ArrayList();
      Iterator var3 = settingHolders.iterator();

      while(var3.hasNext()) {
         Class<? extends SettingsHolder> clazz = (Class)var3.next();
         List<String> invalidFieldsForClazz = (List)this.getAllFields(clazz).filter((field) -> {
            return Property.class.isAssignableFrom(field.getType());
         }).filter((field) -> {
            return !this.isValidConstantField(field);
         }).map((field) -> {
            return field.getDeclaringClass().getSimpleName() + "#" + field.getName();
         }).collect(Collectors.toList());
         invalidFields.addAll(invalidFieldsForClazz);
      }

      if (!invalidFields.isEmpty()) {
         throw new IllegalStateException("The following fields were found not to be public static final:\n- " + String.join("\n- ", invalidFields));
      }
   }

   public void validateSettingsHolderClassesFinal(Iterable<Class<? extends SettingsHolder>> settingHolders) {
      List<String> invalidClasses = new ArrayList();
      Iterator var3 = settingHolders.iterator();

      while(var3.hasNext()) {
         Class<? extends SettingsHolder> clazz = (Class)var3.next();
         if (!Modifier.isFinal(clazz.getModifiers())) {
            invalidClasses.add(clazz.getCanonicalName());
         }
      }

      if (!invalidClasses.isEmpty()) {
         throw new IllegalStateException("The following classes are not final:\n- " + String.join("\n- ", invalidClasses));
      }
   }

   public void validateClassesHaveHiddenNoArgConstructor(Iterable<Class<? extends SettingsHolder>> settingHolders) {
      List<String> invalidClasses = new ArrayList();
      Iterator var3 = settingHolders.iterator();

      while(var3.hasNext()) {
         Class<? extends SettingsHolder> clazz = (Class)var3.next();
         if (!this.hasValidConstructorSetup(clazz)) {
            invalidClasses.add(clazz.getCanonicalName());
         }
      }

      if (!invalidClasses.isEmpty()) {
         throw new IllegalStateException("The following classes do not have a single no-args private constructor:\n- " + String.join("\n- ", invalidClasses));
      }
   }

   public void validateHasCommentOnEveryProperty(ConfigurationData configurationData, @Nullable Predicate<Property<?>> propertyFilter) {
      Predicate<Property<?>> filter = propertyFilter == null ? (p) -> {
         return true;
      } : propertyFilter;
      List<String> invalidProperties = new ArrayList();
      Map<String, List<String>> comments = configurationData.getAllComments();
      Iterator var6 = configurationData.getProperties().iterator();

      while(var6.hasNext()) {
         Property<?> property = (Property)var6.next();
         if (filter.test(property)) {
            List<String> commentEntry = (List)comments.get(property.getPath());
            if (!this.hasNonEmptyComment(commentEntry)) {
               invalidProperties.add(property.toString());
            }
         }
      }

      if (!invalidProperties.isEmpty()) {
         throw new IllegalStateException("The following properties do not have a comment:\n- " + String.join("\n- ", invalidProperties));
      }
   }

   public void validateCommentLengthsAreWithinBounds(ConfigurationData configurationData, @Nullable Integer minLength, @Nullable Integer maxLength) {
      Predicate<String> hasInvalidLengthPredicate = this.createValidLengthPredicate(minLength, maxLength).negate();
      List<String> invalidPaths = new ArrayList();
      Iterator var6 = configurationData.getAllComments().entrySet().iterator();

      while(var6.hasNext()) {
         Entry<String, List<String>> entry = (Entry)var6.next();
         boolean hasInvalidLength = ((List)entry.getValue()).stream().anyMatch(hasInvalidLengthPredicate);
         if (hasInvalidLength) {
            invalidPaths.add("Path '" + (String)entry.getKey() + "'");
         }
      }

      if (!invalidPaths.isEmpty()) {
         String bound = minLength == null ? "" : "min length of " + minLength;
         if (maxLength != null) {
            bound = bound + (bound.isEmpty() ? "" : ", ") + "max length of " + maxLength;
         }

         throw new IllegalStateException("The comments for the following paths are not within the bounds: " + bound + " characters:\n- " + String.join("\n- ", invalidPaths));
      }
   }

   public void validateHasAllEnumEntriesInComment(ConfigurationData configurationData, @Nullable Predicate<Property<?>> propertyFilter) {
      List<String> commentErrors = new ArrayList();
      Iterator var4 = configurationData.getProperties().iterator();

      while(true) {
         Property property;
         do {
            if (!var4.hasNext()) {
               if (!commentErrors.isEmpty()) {
                  throw new IllegalStateException("The following enum properties do not list all enum values:\n- " + String.join("\n- ", commentErrors));
               }

               return;
            }

            property = (Property)var4.next();
         } while(propertyFilter != null && !propertyFilter.test(property));

         Class<? extends Enum<?>> enumType = this.getEnumTypeOfProperty(property);
         if (enumType != null) {
            List<String> expectedEnums = this.gatherExpectedEnumNames(enumType);
            String comments = String.join("\n", configurationData.getCommentsForSection(property.getPath()));
            List<String> missingEnumEntries = (List)expectedEnums.stream().filter((e) -> {
               return !comments.contains(e);
            }).collect(Collectors.toList());
            if (!missingEnumEntries.isEmpty()) {
               commentErrors.add("For " + property + ": missing " + String.join(", ", missingEnumEntries));
            }
         }
      }
   }

   protected boolean isValidConstantField(Field field) {
      int modifiers = field.getModifiers();
      return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
   }

   protected ConfigurationData createConfigurationData(Iterable<Class<? extends SettingsHolder>> classes) {
      return ConfigurationDataBuilder.createConfiguration(classes);
   }

   protected boolean hasNonEmptyComment(@Nullable List<String> comments) {
      return comments != null && comments.stream().anyMatch((line) -> {
         return !line.trim().isEmpty();
      });
   }

   protected Predicate<String> createValidLengthPredicate(@Nullable Integer minLength, @Nullable Integer maxLength) {
      if (minLength == null && maxLength == null) {
         throw new IllegalArgumentException("min length or max length must be not null");
      } else {
         return (string) -> {
            return (minLength == null || minLength <= string.length()) && (maxLength == null || maxLength >= string.length());
         };
      }
   }

   @Nullable
   protected Class<? extends Enum<?>> getEnumTypeOfProperty(Property<?> property) {
      Class<?> defaultValueType = property.getDefaultValue().getClass();
      if (defaultValueType.isAnonymousClass()) {
         defaultValueType = defaultValueType.getEnclosingClass();
      }

      return defaultValueType.isEnum() ? defaultValueType : null;
   }

   protected List<String> gatherExpectedEnumNames(Class<? extends Enum<?>> enumClass) {
      return (List)Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).collect(Collectors.toList());
   }

   protected boolean hasValidConstructorSetup(Class<? extends SettingsHolder> clazz) {
      Constructor<?>[] constructors = clazz.getDeclaredConstructors();
      return constructors.length == 1 && constructors[0].getParameterCount() == 0 && Modifier.isPrivate(constructors[0].getModifiers());
   }

   protected Stream<Field> getAllFields(Class<?> clazz) {
      if (Object.class.equals(clazz.getSuperclass())) {
         return Arrays.stream(clazz.getDeclaredFields());
      } else {
         Class<?> currentClass = clazz;

         ArrayList classes;
         for(classes = new ArrayList(); currentClass != null && !currentClass.equals(Object.class); currentClass = currentClass.getSuperclass()) {
            classes.add(currentClass);
         }

         return classes.stream().flatMap((clz) -> {
            return Arrays.stream(clz.getDeclaredFields());
         });
      }
   }
}
