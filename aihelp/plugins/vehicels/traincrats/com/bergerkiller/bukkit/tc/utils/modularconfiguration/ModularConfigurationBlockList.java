package com.bergerkiller.bukkit.tc.utils.modularconfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public abstract class ModularConfigurationBlockList<T> implements ModularConfigurationBlock<T> {
   protected final List<ModularConfigurationBlock<T>> blocks = new ArrayList();

   public List<? extends ModularConfigurationModule<T>> getFiles() {
      if (this.blocks.isEmpty()) {
         return Collections.emptyList();
      } else if (this.blocks.size() == 1) {
         return ((ModularConfigurationBlock)this.blocks.get(0)).getFiles();
      } else {
         ArrayList<ModularConfigurationModule<T>> allFiles = new ArrayList();
         this.blocks.forEach((b) -> {
            allFiles.addAll(b.getFiles());
         });
         return allFiles;
      }
   }

   public void reload() {
      this.blocks.forEach(ModularConfigurationBlock::reload);
   }

   public void saveChanges() {
      this.blocks.forEach(ModularConfigurationBlock::saveChanges);
   }

   public void save() {
      this.blocks.forEach(ModularConfigurationBlock::save);
   }

   public ModularConfigurationDirectory<T> addDirectoryModule(File directory) {
      return this.addDirectoryModule(directory, false);
   }

   public ModularConfigurationDirectory<T> addDirectoryModule(File directory, boolean priority) {
      return (ModularConfigurationDirectory)this.addBlock(new ModularConfigurationDirectory(this.getMain(), directory), priority);
   }

   public ModularConfigurationFile<T> addFileModule(String name, File file, boolean readOnly) {
      return this.addFileModule(name, file, readOnly, false);
   }

   public ModularConfigurationFile<T> addFileModule(String name, File file, boolean readOnly, boolean priority) {
      return (ModularConfigurationFile)this.addBlock(new ModularConfigurationFile(this.getMain(), name, file, readOnly), priority);
   }

   public <B extends ModularConfigurationBlock<T>> B addBlock(B block, boolean priority) {
      if (priority) {
         this.blocks.add(0, block);
      } else {
         this.blocks.add(block);
      }

      List var10000 = block.getFiles();
      ModularConfiguration var10001 = this.getMain();
      Objects.requireNonNull(var10001);
      var10000.forEach(var10001::onModuleAdded);
      return block;
   }

   public void clear() {
      ArrayList<ModularConfigurationBlock<T>> copy = new ArrayList(this.blocks);
      this.blocks.clear();
      Stream var10000 = copy.stream().flatMap((b) -> {
         return b.getFiles().stream();
      });
      ModularConfiguration var10001 = this.getMain();
      Objects.requireNonNull(var10001);
      var10000.forEachOrdered(var10001::onModuleRemoved);
   }
}
