package libs.com.ryderbelserion.vital.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import libs.com.ryderbelserion.vital.common.VitalAPI;
import libs.com.ryderbelserion.vital.common.api.Provider;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileUtil {
   @NotNull
   private static final VitalAPI api = Provider.getApi();
   @NotNull
   private static final ComponentLogger logger;
   @NotNull
   private static final File dataFolder;

   private FileUtil() {
      throw new AssertionError();
   }

   public static void download(@NotNull String link, @NotNull File directory) {
      CompletableFuture.runAsync(() -> {
         URL url = null;

         try {
            url = URI.create(link).toURL();
         } catch (MalformedURLException var10) {
            var10.printStackTrace();
         }

         if (url != null) {
            try {
               ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());

               try {
                  FileOutputStream outputStream = new FileOutputStream(directory);

                  try {
                     outputStream.getChannel().transferFrom(readableByteChannel, 0L, Long.MAX_VALUE);
                  } catch (Throwable var9) {
                     try {
                        outputStream.close();
                     } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                     }

                     throw var9;
                  }

                  outputStream.close();
               } catch (Throwable var11) {
                  if (readableByteChannel != null) {
                     try {
                        readableByteChannel.close();
                     } catch (Throwable var7) {
                        var11.addSuppressed(var7);
                     }
                  }

                  throw var11;
               }

               if (readableByteChannel != null) {
                  readableByteChannel.close();
               }
            } catch (IOException var12) {
               var12.printStackTrace();
            }

         }
      });
   }

   public static void write(@NotNull File input, @NotNull String format) {
      try {
         FileWriter writer = new FileWriter(input, true);

         try {
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            try {
               bufferedWriter.write(format);
               bufferedWriter.newLine();
               writer.flush();
            } catch (Throwable var8) {
               try {
                  bufferedWriter.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            bufferedWriter.close();
         } catch (Throwable var9) {
            try {
               writer.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }

            throw var9;
         }

         writer.close();
      } catch (Exception var10) {
         logger.warn("Failed to write to: {}", input.getName(), var10);
      }

   }

   public static void zip(@NotNull File input, boolean purge) {
      zip(List.of(input), (File)null, "", purge);
   }

   public static void zip(@NotNull File input, @NotNull String extension, boolean purge) {
      List<File> files = getFileObjects(dataFolder, input.getName(), extension);
      if (!files.isEmpty()) {
         boolean hasNonEmptyFile = false;
         Iterator var5 = files.iterator();

         while(var5.hasNext()) {
            File zip = (File)var5.next();
            if (zip.exists() && zip.length() > 0L) {
               hasNonEmptyFile = true;
               break;
            }
         }

         if (!hasNonEmptyFile) {
            if (api.isVerbose()) {
               logger.warn("All log files are empty. No zip file will be created.");
            }

         } else {
            int count = getFiles(input, ".gz", true).size();
            ++count;
            zip(files, input, "-" + count, purge);
         }
      }
   }

   public static void zip(@NotNull List<File> files, @Nullable File directory, String extra, boolean purge) {
      if (!files.isEmpty()) {
         StringBuilder builder = new StringBuilder();
         builder.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
         if (!builder.isEmpty()) {
            builder.append(extra);
         }

         builder.append(".gz");
         if (directory != null) {
            directory.mkdirs();
         }

         File zipFile = new File(directory == null ? dataFolder : directory, builder.toString());

         try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);

            try {
               ZipOutputStream zipOut = new ZipOutputStream(fileOutputStream);

               try {
                  Iterator var8 = files.iterator();

                  label87:
                  while(true) {
                     while(true) {
                        if (!var8.hasNext()) {
                           break label87;
                        }

                        File file = (File)var8.next();
                        if (file.length() > 0L) {
                           FileInputStream fileInputStream = new FileInputStream(file);

                           try {
                              ZipEntry zipEntry = new ZipEntry(file.getName());
                              zipOut.putNextEntry(zipEntry);
                              byte[] bytes = new byte[1024];

                              int length;
                              while((length = fileInputStream.read(bytes)) >= 0) {
                                 zipOut.write(bytes, 0, length);
                              }
                           } catch (Throwable var17) {
                              try {
                                 fileInputStream.close();
                              } catch (Throwable var16) {
                                 var17.addSuppressed(var16);
                              }

                              throw var17;
                           }

                           fileInputStream.close();
                           if (purge) {
                              file.delete();
                           }
                        } else if (api.isVerbose()) {
                           logger.warn("The file named {}'s size is 0, We are not adding to zip.", file.getName());
                        }
                     }
                  }
               } catch (Throwable var18) {
                  try {
                     zipOut.close();
                  } catch (Throwable var15) {
                     var18.addSuppressed(var15);
                  }

                  throw var18;
               }

               zipOut.close();
            } catch (Throwable var19) {
               try {
                  fileOutputStream.close();
               } catch (Throwable var14) {
                  var19.addSuppressed(var14);
               }

               throw var19;
            }

            fileOutputStream.close();
         } catch (IOException var20) {
            var20.printStackTrace();
         }

      }
   }

   public static void extract(@NotNull String input, boolean overwrite) {
      api.saveResource(input, overwrite);
   }

   public static void visit(@NotNull Consumer<Path> consumer, @NotNull String input) throws IOException {
      URL resource = FileUtil.class.getClassLoader().getResource("config.yml");
      if (resource == null) {
         throw new IllegalStateException("We are lacking awareness of the files in src/main/resources/" + input);
      } else {
         String[] var10000 = resource.toString().split("!");
         URI path = URI.create(var10000[0] + "!/");
         FileSystem fileSystem = FileSystems.newFileSystem(path, Map.of("create", "true"));

         try {
            Path toVisit = fileSystem.getPath(input);
            if (Files.exists(toVisit, new LinkOption[0])) {
               consumer.accept(toVisit);
            }
         } catch (Throwable var8) {
            if (fileSystem != null) {
               try {
                  fileSystem.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (fileSystem != null) {
            fileSystem.close();
         }

      }
   }

   public static void extract(@NotNull String input) {
      extract(input, input, false);
   }

   public static void extract(@NotNull String input, @NotNull String output, boolean replaceExisting) {
      try {
         visit((path) -> {
            Path directory = api.getDirectory().toPath().resolve(output);

            try {
               if (replaceExisting) {
                  directory.toFile().delete();
               }

               if (!Files.exists(directory, new LinkOption[0])) {
                  directory.toFile().mkdirs();
                  Stream files = Files.walk(path);

                  try {
                     files.filter((x$0) -> {
                        return Files.isRegularFile(x$0, new LinkOption[0]);
                     }).forEach((file) -> {
                        try {
                           Path langFile = directory.resolve(file.getFileName().toString());
                           if (!Files.exists(langFile, new LinkOption[0])) {
                              InputStream stream = Files.newInputStream(file);

                              try {
                                 Files.copy(stream, langFile, new CopyOption[0]);
                              } catch (Throwable var7) {
                                 if (stream != null) {
                                    try {
                                       stream.close();
                                    } catch (Throwable var6) {
                                       var7.addSuppressed(var6);
                                    }
                                 }

                                 throw var7;
                              }

                              if (stream != null) {
                                 stream.close();
                              }
                           }
                        } catch (IOException var8) {
                           var8.printStackTrace();
                        }

                     });
                  } catch (Throwable var8) {
                     if (files != null) {
                        try {
                           files.close();
                        } catch (Throwable var7) {
                           var8.addSuppressed(var7);
                        }
                     }

                     throw var8;
                  }

                  if (files != null) {
                     files.close();
                  }
               }
            } catch (IOException var9) {
               var9.printStackTrace();
            }

         }, input);
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   public static void extracts(@Nullable Class<?> object, @NotNull String input, @Nullable Path output, boolean replaceExisting) {
      if (object != null && output != null && !input.isEmpty()) {
         try {
            JarFile jarFile = new JarFile(Path.of(object.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile());

            try {
               String path = input.substring(1);
               Enumeration entries = jarFile.entries();

               label106:
               while(true) {
                  while(true) {
                     JarEntry entry;
                     Path file;
                     boolean exists;
                     do {
                        String name;
                        do {
                           if (!entries.hasMoreElements()) {
                              break label106;
                           }

                           entry = (JarEntry)entries.nextElement();
                           name = entry.getName();
                        } while(!name.startsWith(path));

                        file = output.resolve(name.substring(path.length()));
                        exists = Files.exists(file, new LinkOption[0]);
                     } while(!replaceExisting && exists);

                     if (entry.isDirectory()) {
                        if (!exists) {
                           try {
                              Files.createDirectories(file);
                           } catch (IOException var18) {
                              var18.printStackTrace();
                           }
                        }
                     } else {
                        try {
                           BufferedInputStream inputStream = new BufferedInputStream(jarFile.getInputStream(entry));

                           try {
                              BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file.toFile()));

                              try {
                                 byte[] buffer = new byte[4096];

                                 int readCount;
                                 while((readCount = inputStream.read(buffer)) > 0) {
                                    outputStream.write(buffer, 0, readCount);
                                 }

                                 outputStream.flush();
                              } catch (Throwable var19) {
                                 try {
                                    outputStream.close();
                                 } catch (Throwable var17) {
                                    var19.addSuppressed(var17);
                                 }

                                 throw var19;
                              }

                              outputStream.close();
                           } catch (Throwable var20) {
                              try {
                                 inputStream.close();
                              } catch (Throwable var16) {
                                 var20.addSuppressed(var16);
                              }

                              throw var20;
                           }

                           inputStream.close();
                        } catch (IOException var21) {
                           var21.printStackTrace();
                        }
                     }
                  }
               }
            } catch (Throwable var22) {
               try {
                  jarFile.close();
               } catch (Throwable var15) {
                  var22.addSuppressed(var15);
               }

               throw var22;
            }

            jarFile.close();
         } catch (IOException var23) {
            var23.printStackTrace();
         } catch (URISyntaxException var24) {
            throw new RuntimeException(var24);
         }

      }
   }

   public static List<String> getFiles(@NotNull File directory, @NotNull String extension, boolean keepExtension) {
      List<String> files = new ArrayList();
      String[] list = directory.list();
      if (list == null) {
         return files;
      } else {
         File[] array = directory.listFiles();
         if (array == null) {
            return files;
         } else {
            File[] var6 = array;
            int var7 = array.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               File file = var6[var8];
               if (file.isDirectory()) {
                  String[] folder = file.list();
                  if (folder != null) {
                     String[] var11 = folder;
                     int var12 = folder.length;

                     for(int var13 = 0; var13 < var12; ++var13) {
                        String name = var11[var13];
                        if (name.endsWith(extension)) {
                           files.add(keepExtension ? name : name.replaceAll(extension, ""));
                        }
                     }
                  }
               } else {
                  String name = file.getName();
                  if (name.endsWith(extension)) {
                     files.add(keepExtension ? name : name.replaceAll(extension, ""));
                  }
               }
            }

            return Collections.unmodifiableList(files);
         }
      }
   }

   public static List<String> getFiles(@NotNull File directory, @NotNull String folder, @NotNull String extension) {
      return getFiles(folder.isEmpty() ? directory : new File(directory, folder), extension, false);
   }

   public static List<File> getFileObjects(@NotNull File directory, @NotNull String folder, @NotNull String extension) {
      List<File> files = new ArrayList();
      File root = new File(directory, folder);
      getFiles(folder.isEmpty() ? directory : root, extension, true).forEach((file) -> {
         files.add(new File(root, file));
      });
      return files;
   }

   static {
      logger = api.getComponentLogger();
      dataFolder = api.getDirectory();
   }
}
