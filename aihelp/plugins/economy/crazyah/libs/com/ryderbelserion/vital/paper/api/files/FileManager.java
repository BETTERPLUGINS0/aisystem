package libs.com.ryderbelserion.vital.paper.api.files;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import libs.com.ryderbelserion.vital.common.VitalAPI;
import libs.com.ryderbelserion.vital.common.api.Provider;
import libs.com.ryderbelserion.vital.common.util.FileUtil;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class FileManager {
   private final VitalAPI api = Provider.getApi();
   private final ComponentLogger logger;
   private final File dataFolder;
   private final boolean isVerbose;
   private final Map<String, CustomFile> files;
   private final Map<String, CustomFile> customFiles;
   private final Set<String> folders;

   public FileManager() {
      this.logger = this.api.getComponentLogger();
      this.dataFolder = this.api.getDirectory();
      this.isVerbose = this.api.isVerbose();
      this.files = new HashMap();
      this.customFiles = new HashMap();
      this.folders = new HashSet();
   }

   public final FileManager init() {
      this.dataFolder.mkdirs();
      Iterator var1 = this.folders.iterator();

      while(var1.hasNext()) {
         String key = (String)var1.next();
         File folder = new File(this.dataFolder, key);
         if (!folder.exists()) {
            folder.mkdir();
            FileUtil.extracts(FileManager.class, String.format("/%s/", folder.getName()), folder.toPath(), true);
         }

         File[] contents = folder.listFiles();
         if (contents == null) {
            return this;
         }

         File[] var5 = contents;
         int var6 = contents.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            File file = var5[var7];
            if (file.isDirectory()) {
               String[] files = file.list();
               if (files != null) {
                  String[] var10 = files;
                  int var11 = files.length;

                  for(int var12 = 0; var12 < var11; ++var12) {
                     String fileName = var10[var12];
                     if (fileName.endsWith(".yml")) {
                        this.addFile(true, new File(file, fileName));
                     }
                  }
               }
            } else if (file.getName().endsWith(".yml")) {
               this.addFile(true, file);
            }
         }
      }

      return this;
   }

   public final FileManager reloadFiles() {
      this.customFiles.forEach((key, file) -> {
         file.load();
      });
      this.files.forEach((key, file) -> {
         file.load();
      });
      return this;
   }

   public final FileManager purge() {
      this.files.clear();
      this.customFiles.clear();
      this.folders.clear();
      return this;
   }

   public final FileManager run(Consumer<FileManager> consumer) {
      consumer.accept(this);
      return this;
   }

   public final FileManager addFile(boolean isDynamic, String fileName, String filePath) {
      if (fileName != null && !fileName.isEmpty()) {
         File file = filePath.isEmpty() ? new File(this.dataFolder, fileName) : new File(isDynamic ? new File(filePath.replace(fileName, "")) : new File(this.dataFolder, filePath.replace(fileName, "")), fileName);
         String resourcePath = filePath.isEmpty() ? fileName : filePath + File.separator + fileName;
         if (!fileName.endsWith(".yml")) {
            if (!file.exists()) {
               if (this.isVerbose) {
                  this.logger.warn("Successfully extracted file {} to {}", fileName, file.getPath());
               }

               this.api.saveResource(resourcePath, false);
            }

            return this;
         } else {
            String cleanName = this.strip(fileName, "yml");
            if (isDynamic) {
               if (this.customFiles.containsKey(cleanName)) {
                  if (this.isVerbose) {
                     this.logger.warn("Cannot add custom file {}, because it already exists. We are reloading the config!", fileName);
                  }

                  ((CustomFile)this.customFiles.get(cleanName)).load();
                  return this;
               } else {
                  if (this.isVerbose) {
                     this.logger.warn("Successfully loaded custom file {} in {}", fileName, file.getPath());
                  }

                  this.customFiles.put(cleanName, (new CustomFile(fileName, file)).load());
                  return this;
               }
            } else {
               if (!file.exists()) {
                  if (this.isVerbose) {
                     this.logger.warn("Successfully extracted static file {} to {}", fileName, file.getPath());
                  }

                  this.api.saveResource(resourcePath, false);
               }

               if (this.files.containsKey(cleanName)) {
                  if (this.isVerbose) {
                     this.logger.warn("Cannot add static file {}, because it already exists. We are reloading the config!", fileName);
                  }

                  ((CustomFile)this.files.get(cleanName)).load();
                  return this;
               } else {
                  this.files.put(cleanName, (new CustomFile(fileName, file)).load());
                  if (this.isVerbose) {
                     this.logger.warn("Successfully loaded static file {} in {}", fileName, file.getPath());
                  }

                  return this;
               }
            }
         }
      } else {
         if (this.isVerbose) {
            this.logger.warn("Cannot add the file as the file is null or empty.");
         }

         return this;
      }
   }

   public final FileManager addFile(boolean isDynamic, CustomFile file) {
      String cleanName = file.getCleanName();
      if (isDynamic) {
         this.customFiles.put(cleanName, file.load());
      } else {
         this.files.put(cleanName, file.load());
      }

      return this;
   }

   public final FileManager addFile(String fileName, String filePath) {
      return this.addFile(false, fileName, filePath);
   }

   public final FileManager addFile(boolean isDynamic, File file) {
      return this.addFile(isDynamic, file.getName(), file.getPath());
   }

   public final FileManager addFile(CustomFile file) {
      return this.addFile(false, file);
   }

   public final FileManager addFile(String fileName) {
      return this.addFile(false, fileName, "");
   }

   public final FileManager addFile(File file) {
      return this.addFile(false, file);
   }

   public final FileManager removeFile(boolean isDynamic, String fileName, boolean purge) {
      if (fileName.isEmpty()) {
         if (this.isVerbose) {
            this.logger.warn("Cannot remove the file as the name is empty.");
         }

         return this;
      } else {
         String cleanName = this.strip(fileName, "yml");
         CustomFile customFile = isDynamic ? (CustomFile)this.customFiles.get(cleanName) : (CustomFile)this.files.get(cleanName);
         if (customFile == null) {
            return this;
         } else {
            if (isDynamic) {
               this.customFiles.remove(cleanName);
            } else {
               this.files.remove(cleanName);
            }

            if (purge) {
               File file = customFile.getFile();
               if (file == null) {
                  return this;
               }

               file.delete();
               if (this.isVerbose) {
                  this.logger.warn("Successfully deleted {}", fileName);
               }
            } else {
               customFile.save();
            }

            return this;
         }
      }
   }

   public final FileManager removeFile(String fileName, boolean purge) {
      return this.removeFile(false, fileName, purge);
   }

   public final FileManager removeFile(boolean isDynamic, String fileName) {
      return this.removeFile(isDynamic, fileName, false);
   }

   public final FileManager removeFile(String fileName) {
      return this.removeFile(fileName, false);
   }

   public final FileManager saveFile(boolean isDynamic, String fileName) {
      String cleanName = this.strip(fileName, "yml");
      if (isDynamic) {
         ((CustomFile)this.customFiles.get(cleanName)).save();
      } else {
         ((CustomFile)this.files.get(cleanName)).save();
      }

      return this;
   }

   public final FileManager saveFile(String fileName) {
      return this.saveFile(false, fileName);
   }

   public final FileManager addFolder(String folder) {
      if (!folder.isEmpty() && !this.folders.contains(folder)) {
         this.folders.add(folder);
         return this;
      } else {
         return this;
      }
   }

   public final FileManager removeFolder(String folder) {
      if (folder.isEmpty()) {
         return this;
      } else {
         this.folders.remove(folder);
         return this;
      }
   }

   public final CustomFile getFile(String fileName, boolean isCustom) {
      String cleanName = this.strip(fileName, "yml");
      return isCustom ? (CustomFile)this.customFiles.get(cleanName) : (CustomFile)this.files.get(cleanName);
   }

   public final CustomFile getFile(String fileName) {
      return this.getFile(fileName, false);
   }

   public final Set<String> getFolders() {
      return Collections.unmodifiableSet(this.folders);
   }

   public final Map<String, CustomFile> getCustomFiles() {
      return Collections.unmodifiableMap(this.customFiles);
   }

   public final Map<String, CustomFile> getFiles() {
      return Collections.unmodifiableMap(this.files);
   }

   public final String strip(String fileName, String prefix) {
      return fileName.replace("." + prefix, "");
   }
}
