package com.bergerkiller.bukkit.tc.utils.modularconfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ModularConfigurationDirectory<T> implements ModularConfigurationBlock<T> {
   private final ModularConfiguration<T> main;
   private List<ModularConfigurationFile<T>> files;
   private List<String> names;
   private final Map<String, ModularConfigurationFile<T>> filesByName;
   private final File directory;

   ModularConfigurationDirectory(ModularConfiguration<T> main, File directory) {
      this.main = main;
      this.files = Collections.emptyList();
      this.names = Collections.emptyList();
      this.filesByName = new HashMap();
      this.directory = directory;
      this.loadFiles();
   }

   public File getDirectory() {
      return this.directory;
   }

   public void clear() {
      if (!this.files.isEmpty()) {
         List<ModularConfigurationFile<T>> filesOrig = this.files;
         this.files = Collections.emptyList();
         this.names = Collections.emptyList();
         this.filesByName.clear();
         ModularConfiguration var10001 = this.main;
         Objects.requireNonNull(var10001);
         filesOrig.forEach(var10001::onModuleRemoved);
      }

   }

   private void loadFiles() {
      if (!this.directory.exists()) {
         this.directory.mkdir();
         this.files = Collections.emptyList();
         this.names = Collections.emptyList();
         this.filesByName.clear();
      } else {
         File[] directoryFiles = this.directory.listFiles();
         if (directoryFiles == null) {
            this.files = Collections.emptyList();
            this.names = Collections.emptyList();
            this.filesByName.clear();
         } else {
            this.files = (List)Arrays.stream(directoryFiles).filter((file) -> {
               String ext = file.getName().toLowerCase(Locale.ENGLISH);
               if (ext.endsWith(".zip")) {
                  this.main.logger.warning("Zip files are not read, please extract '" + file.getAbsolutePath() + "'!");
                  return false;
               } else {
                  return ext.endsWith(".yml") || ext.endsWith(".yaml");
               }
            }).map((file) -> {
               return new ModularConfigurationFile(this.main, file);
            }).filter((m) -> {
               return !m.isEmpty();
            }).sorted().collect(Collectors.toList());
            this.filesByName.clear();
            this.files.forEach((f) -> {
               this.filesByName.put(f.name, f);
            });
            this.regenNames();
         }
      }
   }

   public ModularConfigurationFile<T> getFile(String name) {
      return (ModularConfigurationFile)this.filesByName.get(name.toLowerCase(Locale.ENGLISH));
   }

   public ModularConfigurationFile<T> createFile(String name) {
      File file = new File(this.directory, name + ".yml");
      String fixedName = ModularConfigurationFile.decodeModuleNameFromFile(file);
      ModularConfigurationFile<T> fileModule = (ModularConfigurationFile)this.filesByName.get(fixedName);
      if (fileModule == null) {
         try {
            file.createNewFile();
         } catch (IOException var7) {
            this.main.logger.log(Level.WARNING, "Failed to write to " + file.getAbsolutePath(), var7);
         }

         fileModule = new ModularConfigurationFile(this.main, fixedName, file, false);
         ArrayList<ModularConfigurationFile<T>> newFiles = new ArrayList(this.files);
         int index = Collections.binarySearch(newFiles, fileModule);
         if (index < 0) {
            index = -index - 1;
         }

         newFiles.add(index, fileModule);
         this.files = newFiles;
         this.filesByName.put(fixedName, fileModule);
         this.regenNames();
      }

      return fileModule;
   }

   private void regenNames() {
      List<String> names = new ArrayList(this.files.size());
      Iterator var2 = this.files.iterator();

      while(var2.hasNext()) {
         ModularConfigurationFile<T> file = (ModularConfigurationFile)var2.next();
         names.add(file.getName());
      }

      this.names = Collections.unmodifiableList(names);
   }

   public ModularConfiguration<T> getMain() {
      return this.main;
   }

   public List<ModularConfigurationFile<T>> getFiles() {
      return this.files;
   }

   public List<String> getFileNames() {
      return this.names;
   }

   public void reload() {
      this.saveChanges();
      this.clear();
      this.loadFiles();
      List var10000 = this.files;
      ModularConfiguration var10001 = this.main;
      Objects.requireNonNull(var10001);
      var10000.forEach(var10001::onModuleAdded);
   }

   public void saveChanges() {
      this.files.forEach(ModularConfigurationModule::saveChanges);
   }

   public void save() {
      this.files.forEach(ModularConfigurationModule::save);
   }
}
