package ch.jalu.configme.configurationdata;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.exception.ConfigMeException;
import ch.jalu.configme.properties.Property;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigurationDataBuilder {
   @NotNull
   protected PropertyListBuilder propertyListBuilder = new PropertyListBuilder();
   @NotNull
   protected CommentsConfiguration commentsConfiguration = new CommentsConfiguration();

   protected ConfigurationDataBuilder() {
   }

   @SafeVarargs
   @NotNull
   public static ConfigurationData createConfiguration(@NotNull Class<? extends SettingsHolder>... classes) {
      return createConfiguration((Iterable)Arrays.asList(classes));
   }

   @NotNull
   public static ConfigurationData createConfiguration(@NotNull Iterable<Class<? extends SettingsHolder>> classes) {
      ConfigurationDataBuilder builder = new ConfigurationDataBuilder();
      return builder.collectData(classes);
   }

   @NotNull
   public static ConfigurationData createConfiguration(@NotNull List<? extends Property<?>> properties) {
      return new ConfigurationDataImpl(properties, Collections.emptyMap());
   }

   @NotNull
   public static ConfigurationData createConfiguration(@NotNull List<? extends Property<?>> properties, @NotNull CommentsConfiguration commentsConfiguration) {
      return new ConfigurationDataImpl(properties, commentsConfiguration.getAllComments());
   }

   @NotNull
   protected ConfigurationData collectData(@NotNull Iterable<Class<? extends SettingsHolder>> classes) {
      Iterator var2 = classes.iterator();

      while(var2.hasNext()) {
         Class<? extends SettingsHolder> clazz = (Class)var2.next();
         this.collectProperties(clazz);
         this.collectSectionComments(clazz);
      }

      return new ConfigurationDataImpl(this.propertyListBuilder.create(), this.commentsConfiguration.getAllComments());
   }

   protected void collectProperties(@NotNull Class<?> clazz) {
      this.findFieldsToProcess(clazz).forEach((field) -> {
         Property<?> property = this.getPropertyField(field);
         if (property != null) {
            this.propertyListBuilder.add(property);
            this.setCommentForPropertyField(field, property.getPath());
         }

      });
   }

   protected void setCommentForPropertyField(@NotNull Field field, @NotNull String path) {
      Comment commentAnnotation = (Comment)field.getAnnotation(Comment.class);
      if (commentAnnotation != null) {
         this.commentsConfiguration.setComment(path, commentAnnotation.value());
      }

   }

   @Nullable
   protected Property<?> getPropertyField(@NotNull Field field) {
      if (Property.class.isAssignableFrom(field.getType()) && Modifier.isStatic(field.getModifiers())) {
         try {
            this.setFieldAccessibleIfNeeded(field);
            return (Property)field.get((Object)null);
         } catch (IllegalAccessException var3) {
            throw new ConfigMeException("Could not fetch field '" + field.getName() + "' from class '" + field.getDeclaringClass().getSimpleName() + "'. Is it maybe not public?", var3);
         }
      } else {
         return null;
      }
   }

   protected void setFieldAccessibleIfNeeded(@NotNull Field field) {
      try {
         if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
         }

      } catch (Exception var3) {
         throw new ConfigMeException("Failed to modify access for field '" + field.getName() + "' from class '" + field.getDeclaringClass().getSimpleName() + "'", var3);
      }
   }

   protected void collectSectionComments(@NotNull Class<? extends SettingsHolder> clazz) {
      SettingsHolder settingsHolder = this.createSettingsHolderInstance(clazz);
      settingsHolder.registerComments(this.commentsConfiguration);
   }

   @NotNull
   protected <T extends SettingsHolder> T createSettingsHolderInstance(@NotNull Class<T> clazz) {
      try {
         Constructor<T> constructor = clazz.getDeclaredConstructor();
         constructor.setAccessible(true);
         return (SettingsHolder)constructor.newInstance();
      } catch (NoSuchMethodException var3) {
         throw new ConfigMeException("Expected no-args constructor to be available for " + clazz, var3);
      } catch (InstantiationException | InvocationTargetException | IllegalAccessException var4) {
         throw new ConfigMeException("Could not create instance of " + clazz, var4);
      }
   }

   @NotNull
   protected Stream<Field> findFieldsToProcess(@NotNull Class<?> clazz) {
      if (Object.class.equals(clazz.getSuperclass())) {
         return Arrays.stream(clazz.getDeclaredFields());
      } else {
         List<Class<?>> classes = new ArrayList();

         for(Class currentClass = clazz; currentClass != null && !currentClass.equals(Object.class); currentClass = currentClass.getSuperclass()) {
            classes.add(currentClass);
         }

         Collections.reverse(classes);
         return classes.stream().map(Class::getDeclaredFields).flatMap(Arrays::stream);
      }
   }
}
