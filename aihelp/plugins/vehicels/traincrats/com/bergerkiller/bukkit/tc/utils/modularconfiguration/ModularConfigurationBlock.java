package com.bergerkiller.bukkit.tc.utils.modularconfiguration;

import java.util.List;

public interface ModularConfigurationBlock<T> {
   ModularConfiguration<T> getMain();

   List<? extends ModularConfigurationModule<T>> getFiles();

   void reload();

   void saveChanges();

   void save();
}
