package fr.xephi.authme.libs.ch.jalu.configme;

import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;

public interface SettingsManager {
   <T> T getProperty(Property<T> var1);

   <T> void setProperty(Property<T> var1, T var2);

   void reload();

   void save();
}
