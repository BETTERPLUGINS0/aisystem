package advancedplugins.pm2.cv.models.core.model.rpc.generator;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.ModelArchive;
import advancedplugins.pm2.cv.models.api.model.rpc.error.ErrorCollector;
import advancedplugins.pm2.cv.models.api.model.rpc.error.IError;
import advancedplugins.pm2.cv.models.api.model.rpc.events.ModelRegistrationEvent;
import advancedplugins.pm2.cv.models.api.model.rpc.events.RegisterParserEvent;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.ModelGenerator;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.BlueprintTexture;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ItemModel;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ItemModelData;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.JavaItemModel;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ModelAssets;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.ModelBlueprint;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.parser.ModelParser;
import advancedplugins.pm2.cv.models.api.utils.FileUtils;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.api.utils.ticker.DualTicker;
import advancedplugins.pm2.cv.models.core.ModelAPIImpl;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.atlas.AtlasManager;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.parser.blockbench.BlockbenchParser;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.Pair;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import lombok.Generated;
import org.bukkit.NamespacedKey;

public class ModelGeneratorImpl implements ModelGenerator {
   private final ModelAPI plugin;
   private final Gson gson;
   private final List<ModelParser> parsers = new ArrayList();
   private final List<ModelAssets> assets = new ArrayList();
   private final AtlasManager atlasManager;
   private final BaseItemManager baseItemManager;
   private final ExecutorService generatorService = Executors.newWorkStealingPool();
   private final File blueprintFolder;
   private final File packFolder;
   private final File assetsFolder;
   private final File zippedResourcePack;
   private final Map<ModelGenerator.Phase, Set<Runnable>> tasks = Maps.newConcurrentMap();
   private final Set<ModelGenerator.Phase> executed = new HashSet();
   private final long[] timer = new long[3];
   private final List<File> foldersToProcess;
   private String namespace;
   private File modelFolder;
   private File itemsFolder;
   private boolean generateMeta;
   private boolean initialized;

   public ModelGeneratorImpl(ModelAPI var1) {
      this.plugin = var1;
      this.gson = var1.getGson();
      this.blueprintFolder = FileUtils.createDirectory(ModelAPI.PLUGIN.getDataFolder(), "blueprints");
      this.packFolder = FileUtils.createDirectory(ModelAPI.PLUGIN.getDataFolder(), "pack");
      this.assetsFolder = FileUtils.createDirectory(this.packFolder, "assets");
      this.zippedResourcePack = FileUtils.createFile(ModelAPI.PLUGIN.getDataFolder(), ModelAPIImpl.PLUGIN.getName() + "Pack.zip");
      this.atlasManager = new AtlasManager(this);
      this.baseItemManager = new BaseItemManager(this);
      this.baseItemManager.refreshCache();
      var1.getConfigManager().registerReferenceUpdate(this::updateConfig);
      this.parsers.add(new BlockbenchParser(this));
      ModelAPI.callEvent(new RegisterParserEvent(this.parsers));
      this.foldersToProcess = new ArrayList();
      this.addFolderToProcess(this.blueprintFolder);
   }

   public void importModels(boolean var1) {
      this.executed.clear();
      this.queueTask(ModelGenerator.Phase.PRE_IMPORT, () -> {
         this.timer[0] = System.nanoTime();
      });
      this.queueTask(ModelGenerator.Phase.POST_IMPORT, () -> {
         this.timer[0] = System.nanoTime() - this.timer[0];
      });
      this.queueTask(ModelGenerator.Phase.PRE_ASSETS, () -> {
         this.timer[1] = System.nanoTime();
      });
      this.queueTask(ModelGenerator.Phase.POST_ASSETS, () -> {
         this.timer[1] = System.nanoTime() - this.timer[1];
      });
      this.queueTask(ModelGenerator.Phase.PRE_ZIPPING, () -> {
         this.timer[2] = System.nanoTime();
      });
      this.queueTask(ModelGenerator.Phase.POST_ZIPPING, () -> {
         this.timer[2] = System.nanoTime() - this.timer[2];
      });
      this.queueTask(ModelGenerator.Phase.FINISHED, () -> {
         String var10000 = String.valueOf(LogUtil.LogColor.PURPLE);
         LogUtil.log(var10000 + "Completed import in " + Math.round((double)this.timer[0] / 1000000.0D) + "ms, " + Math.round((double)this.timer[1] / 1000000.0D) + "ms for assets, " + Math.round((double)this.timer[2] / 1000000.0D) + "ms for zipping.");
      });
      if (var1 && !ConfigProperty.LATE_REGISTER.getBoolean()) {
         this.importModelsInternal(true);
      } else {
         DualTicker.queueIOTask(() -> {
            this.importModelsInternal(var1);
         });
      }

   }

   public void generateAssets(boolean var1) {
      if (!ConfigProperty.LATE_REGISTER.getBoolean() && ConfigProperty.LATE_ASSETS.getBoolean()) {
         DualTicker.queueIOTask(() -> {
            this.generateAssetsInternal(var1);
         });
      } else {
         this.generateAssetsInternal(true);
      }

   }

   public void zipResourcePack(boolean var1) {
      if (!ConfigProperty.ZIP.getBoolean()) {
         this.executeQueuedTask(ModelGenerator.Phase.FINISHED);
      } else if (!ConfigProperty.LATE_ASSETS.getBoolean() && ConfigProperty.LATE_ZIPPING.getBoolean()) {
         DualTicker.queueIOTask(this::zipResourcePackInternal);
      } else {
         this.zipResourcePackInternal();
      }

   }

   public void updateConfig() {
      this.namespace = ConfigProperty.NAMESPACE.getString().toLowerCase(Locale.ENGLISH);
      this.modelFolder = FileUtils.createDirectory(this.assetsFolder, this.namespace, "models");
      this.itemsFolder = FileUtils.createDirectory(this.assetsFolder, this.namespace, "items");
      this.baseItemManager.updateModels();
      this.generateMeta = ConfigProperty.MCMETA.getBoolean();
   }

   public void queueTask(ModelGenerator.Phase var1, Runnable var2) {
      if (!this.executed.contains(ModelGenerator.Phase.FINISHED) && !this.executed.contains(var1)) {
         ((Set)this.tasks.computeIfAbsent(var1, (var0) -> {
            return new LinkedHashSet();
         })).add(var2);
      } else {
         var2.run();
      }

   }

   public void addFolderToProcess(File var1) {
      if (!var1.isDirectory()) {
         throw new IllegalArgumentException(var1.getAbsolutePath() + " is not a directory!");
      } else {
         this.foldersToProcess.add(var1);
      }
   }

   private void executeQueuedTask(ModelGenerator.Phase var1) {
      this.executed.add(var1);
      if (var1 == ModelGenerator.Phase.FINISHED) {
         this.tasks.values().forEach((var0) -> {
            var0.forEach(Runnable::run);
         });
         this.tasks.clear();
      } else {
         this.tasks.computeIfPresent(var1, (var0, var1x) -> {
            var1x.forEach(Runnable::run);
            var1x.clear();
            return var1x;
         });
      }

      ModelAPI.callEvent(new ModelRegistrationEvent(var1));
   }

   private void importModelsInternal(boolean var1) {
      String var2 = String.valueOf(LogUtil.LogColor.BOLD);
      LogUtil.log(var2 + String.valueOf(LogUtil.LogColor.PURPLE) + "Initiating model import...");
      this.initialized = false;
      this.executeQueuedTask(ModelGenerator.Phase.PRE_IMPORT);
      this.baseItemManager.refreshCache();
      ModelArchive var3 = this.plugin.getModelArchive();
      var3.clear();
      if (!this.blueprintFolder.isDirectory()) {
         this.initialized = true;
         this.executeQueuedTask(ModelGenerator.Phase.FINISHED);
      } else {
         Object var4 = new ArrayList();
         this.getFoldersToProcess().forEach((var1x) -> {
            File[] var2 = var1x.listFiles();
            if (var2 != null) {
               var4.addAll(Arrays.asList(var2));
            }

         });
         if (!((List)var4).isEmpty()) {
            LinkedList var6 = new LinkedList();
            ((List)var4).sort(Ordering.natural());
            ArrayList var7 = new ArrayList();

            while(true) {
               Iterator var8 = ((List)var4).iterator();

               while(var8.hasNext()) {
                  File var9 = (File)var8.next();
                  if (!var9.isFile()) {
                     if (var9.isDirectory()) {
                        var6.add(var9);
                     }
                  } else {
                     var7.add(CompletableFuture.runAsync(() -> {
                        boolean var3x = false;
                        ErrorCollector var4 = new ErrorCollector(var9.getName());
                        Iterator var5 = this.parsers.iterator();

                        String var10000;
                        while(var5.hasNext()) {
                           ModelParser var6 = (ModelParser)var5.next();
                           if (var6.validateFile(var9)) {
                              try {
                                 Pair var7 = var6.generate(var9, var4);
                                 if (var7 != null) {
                                    ModelBlueprint var8 = (ModelBlueprint)var7.left();
                                    ModelAssets var9x = (ModelAssets)var7.right();
                                    var8.finalizeModel(var4);
                                    Iterator var10 = var8.getFlatMap().entrySet().iterator();

                                    while(var10.hasNext()) {
                                       Entry var11 = (Entry)var10.next();
                                       BlueprintJoint var12 = (BlueprintJoint)var11.getValue();
                                       if (var12.isRenderer()) {
                                          synchronized(this.baseItemManager) {
                                             this.baseItemManager.requestId(var12, var12.getModelData().getMultiModels().getKeys());
                                          }

                                          ItemModelData var23 = var12.getModelData();
                                          String var10005 = this.namespace;
                                          String var10006 = var8.getName();
                                          var23.setSingleComposite(new ItemModelData.SingleComposite(new NamespacedKey(var10005, var10006 + "/" + var12.getName())));
                                       }
                                    }

                                    synchronized(var3) {
                                       var3.registerBlueprint(var8);
                                    }

                                    synchronized(this.assets) {
                                       this.assets.add(var9x);
                                    }

                                    var3x = true;
                                    break;
                                 }
                              } catch (Exception var22) {
                                 var10000 = String.valueOf(LogUtil.LogColor.DARK_RED);
                                 LogUtil.error(var10000 + "Failure loading: '" + var9.getName() + "' [code: " + var22.getMessage() + "]");
                                 var22.printStackTrace();
                                 break;
                              }
                           }
                        }

                        if (!var3x && !var9.getName().endsWith("zip") && !var9.getName().endsWith("yml")) {
                           var10000 = String.valueOf(LogUtil.LogColor.DARK_RED);
                           LogUtil.warn(var10000 + "File rejected: incompatible signature detected for '" + var9.getName() + "'.");
                           IError.UNKNOWN_FORMAT.log(var4);
                        } else {
                           synchronized(this.timer) {
                              var4.logAll();
                           }
                        }
                     }, this.generatorService));
                  }
               }

               File[] var10;
               do {
                  if (var6.isEmpty()) {
                     CompletableFuture.allOf((CompletableFuture[])var7.toArray((var0) -> {
                        return new CompletableFuture[var0];
                     })).join();
                     this.initialized = true;
                     this.baseItemManager.endSession();
                     this.executeQueuedTask(ModelGenerator.Phase.POST_IMPORT);
                     this.generateAssets(var1);
                     return;
                  }

                  var10 = ((File)var6.poll()).listFiles();
               } while(var10 == null || var10.length == 0);

               var4 = Arrays.asList(var10);
            }
         }

         this.initialized = true;
         this.executeQueuedTask(ModelGenerator.Phase.FINISHED);
      }

   }

   private void generateAssetsInternal(boolean var1) {
      this.executeQueuedTask(ModelGenerator.Phase.PRE_ASSETS);
      this.baseItemManager.clearOverrides();
      this.atlasManager.reset();
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.assets.iterator();

      while(var3.hasNext()) {
         ModelAssets var4 = (ModelAssets)var3.next();
         var2.add(CompletableFuture.runAsync(() -> {
            Iterator var2 = var4.getTextures().iterator();

            File var7;
            FileWriter var8;
            while(var2.hasNext()) {
               BlueprintTexture var3 = (BlueprintTexture)var2.next();
               if (!"minecraft".equals(var3.getPath().getNamespace())) {
                  this.atlasManager.addSingle(var3.getPath());
                  File var4x = FileUtils.createFile(this.assetsFolder, "textures", var3.getPath(), "png");
                  BufferedImage var5 = FileUtils.toImage(var3.getSource());

                  try {
                     ImageIO.write(var5, "png", var4x);
                  } catch (IOException var28) {
                     var28.printStackTrace();
                  }

                  if (var3.getMcMeta() != null && this.shouldGenerateMCMeta(var5, var3)) {
                     ConcurrentHashMap var6 = new ConcurrentHashMap();
                     var7 = FileUtils.createFile(this.assetsFolder, "textures", var3.getPath(), "png.mcmeta");

                     try {
                        var8 = new FileWriter(var7);

                        try {
                           var6.put("animation", var3.getMcMeta());
                           var8.write(this.gson.toJson(var6));
                        } catch (Throwable var26) {
                           try {
                              var8.close();
                           } catch (Throwable var25) {
                              var26.addSuppressed(var25);
                           }

                           throw var26;
                        }

                        var8.close();
                     } catch (IOException var27) {
                        var27.printStackTrace();
                     }
                  }
               }
            }

            var2 = var4.getModels().values().iterator();

            while(true) {
               while(var2.hasNext()) {
                  Collection var29 = (Collection)var2.next();
                  String var10005;
                  if (var29.size() == 1) {
                     JavaItemModel var31 = (JavaItemModel)var29.iterator().next();
                     File var33 = FileUtils.createFile(this.modelFolder, var4.getName(), var31.getName() + ".json");

                     try {
                        FileWriter var35 = new FileWriter(var33);

                        try {
                           var35.write(this.gson.toJson(var31));
                        } catch (Throwable var14) {
                           try {
                              var35.close();
                           } catch (Throwable var13) {
                              var14.addSuppressed(var13);
                           }

                           throw var14;
                        }

                        var35.close();
                     } catch (IOException var15) {
                        var15.printStackTrace();
                     }

                     File var36 = FileUtils.createFile(this.itemsFolder, var4.getName(), var31.getName() + ".json");

                     try {
                        FileWriter var38 = new FileWriter(var36);

                        try {
                           String var10004 = this.namespace;
                           var10005 = var4.getName();
                           ItemModel.Model var40 = new ItemModel.Model(new NamespacedKey(var10004, var10005 + "/" + var31.getName()));
                           var38.write(this.gson.toJson(this.wrapModel(var40)));
                        } catch (Throwable var18) {
                           try {
                              var38.close();
                           } catch (Throwable var17) {
                              var18.addSuppressed(var17);
                           }

                           throw var18;
                        }

                        var38.close();
                     } catch (IOException var19) {
                        var19.printStackTrace();
                     }
                  } else {
                     int var30 = 0;
                     String var32 = "";
                     ItemModel.Composite var34 = new ItemModel.Composite();
                     Iterator var37 = var29.iterator();

                     while(var37.hasNext()) {
                        JavaItemModel var39 = (JavaItemModel)var37.next();
                        var39.finalizeModel();
                        File var9;
                        List var10000;
                        String var10006;
                        if (var30 == 0) {
                           var32 = var39.getName();
                           var9 = FileUtils.createFile(this.modelFolder, var4.getName(), var39.getName() + ".json");
                           var10000 = var34.getModels();
                           var10005 = this.namespace;
                           var10006 = var4.getName();
                           var10000.add(new ItemModel.Model(new NamespacedKey(var10005, var10006 + "/" + var39.getName())));
                        } else {
                           var9 = FileUtils.createFile(this.modelFolder, var4.getName(), var39.getName(), var30 + ".json");
                           var10000 = var34.getModels();
                           var10005 = this.namespace;
                           var10006 = var4.getName();
                           var10000.add(new ItemModel.Model(new NamespacedKey(var10005, var10006 + "/" + var39.getName() + "/" + var30)));
                        }

                        ++var30;

                        try {
                           FileWriter var10 = new FileWriter(var9);

                           try {
                              var10.write(this.gson.toJson(var39));
                           } catch (Throwable var23) {
                              try {
                                 var10.close();
                              } catch (Throwable var22) {
                                 var23.addSuppressed(var22);
                              }

                              throw var23;
                           }

                           var10.close();
                        } catch (IOException var24) {
                           var24.printStackTrace();
                        }
                     }

                     var7 = FileUtils.createFile(this.itemsFolder, var4.getName(), var32 + ".json");

                     try {
                        var8 = new FileWriter(var7);

                        try {
                           var8.write(this.gson.toJson(this.wrapModel(var34)));
                        } catch (Throwable var20) {
                           try {
                              var8.close();
                           } catch (Throwable var16) {
                              var20.addSuppressed(var16);
                           }

                           throw var20;
                        }

                        var8.close();
                     } catch (IOException var21) {
                        var21.printStackTrace();
                     }
                  }
               }

               return;
            }
         }, this.generatorService));
      }

      CompletableFuture.allOf((CompletableFuture[])var2.toArray((var0) -> {
         return new CompletableFuture[var0];
      })).join();
      this.assets.clear();
      this.baseItemManager.registerModels(this.namespace);
      this.baseItemManager.createModelFiles();
      this.baseItemManager.cleanUp();
      if (ConfigProperty.ATLAS.getBoolean()) {
         this.atlasManager.generateFile();
      }

      FileUtils.copyResource(ModelAPI.PLUGIN, this.packFolder, "pack", "pack.png");
      FileUtils.copyResource(ModelAPI.PLUGIN, this.packFolder, "pack", "pack.mcmeta");
      FileUtils.copyResource(ModelAPI.PLUGIN, this.packFolder, "pack", "assets/minecraft/models/custom/entities/player/left_arm.json");
      FileUtils.copyResource(ModelAPI.PLUGIN, this.packFolder, "pack", "assets/minecraft/models/custom/entities/player/left_leg.json");
      FileUtils.copyResource(ModelAPI.PLUGIN, this.packFolder, "pack", "assets/minecraft/models/custom/entities/player/right_arm.json");
      FileUtils.copyResource(ModelAPI.PLUGIN, this.packFolder, "pack", "assets/minecraft/models/custom/entities/player/right_leg.json");
      FileUtils.copyResource(ModelAPI.PLUGIN, this.packFolder, "pack", "assets/minecraft/models/custom/entities/player/slim_left.json");
      FileUtils.copyResource(ModelAPI.PLUGIN, this.packFolder, "pack", "assets/minecraft/models/custom/entities/player/slim_right.json");
      FileUtils.copyResource(ModelAPI.PLUGIN, this.packFolder, "pack", "assets/minecraft/models/custom/entities/player/torso.json");
      this.executeQueuedTask(ModelGenerator.Phase.POST_ASSETS);
      this.zipResourcePack(var1);
   }

   private void zipResourcePackInternal() {
      this.executeQueuedTask(ModelGenerator.Phase.PRE_ZIPPING);

      try {
         FileOutputStream var1 = new FileOutputStream(this.zippedResourcePack);
         ZipOutputStream var2 = new ZipOutputStream(var1);
         File[] var3 = this.packFolder.listFiles();
         if (var3 == null) {
            this.executeQueuedTask(ModelGenerator.Phase.FINISHED);
            return;
         }

         File[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            FileUtils.zipFile(var7, var7.getName(), var2);
         }

         var2.close();
         var1.close();
      } catch (IOException var8) {
         var8.printStackTrace();
      }

      this.executeQueuedTask(ModelGenerator.Phase.POST_ZIPPING);
      this.executeQueuedTask(ModelGenerator.Phase.FINISHED);
   }

   private boolean shouldGenerateMCMeta(BufferedImage var1, BlueprintTexture var2) {
      if (var2.getMcMeta().isMustGenerate()) {
         return true;
      } else if (!this.generateMeta) {
         return false;
      } else {
         float var3 = (float)var1.getHeight() / (float)var1.getWidth();
         float var4 = (float)var2.getFrameHeight() / (float)var2.getFrameWidth();
         if (MathUtils.isSimilar(var3, var4)) {
            return false;
         } else {
            return var3 / var4 > 1.0F;
         }
      }
   }

   private JsonElement wrapModel(ItemModel var1) {
      JsonObject var2 = new JsonObject();
      var2.add("model", this.gson.toJsonTree(var1));
      return var2;
   }

   @Generated
   public ModelAPI getPlugin() {
      return this.plugin;
   }

   @Generated
   public Gson getGson() {
      return this.gson;
   }

   @Generated
   public List<ModelParser> getParsers() {
      return this.parsers;
   }

   @Generated
   public List<ModelAssets> getAssets() {
      return this.assets;
   }

   @Generated
   public AtlasManager getAtlasManager() {
      return this.atlasManager;
   }

   @Generated
   public BaseItemManager getBaseItemManager() {
      return this.baseItemManager;
   }

   @Generated
   public ExecutorService getGeneratorService() {
      return this.generatorService;
   }

   @Generated
   public File getBlueprintFolder() {
      return this.blueprintFolder;
   }

   @Generated
   public File getPackFolder() {
      return this.packFolder;
   }

   @Generated
   public File getAssetsFolder() {
      return this.assetsFolder;
   }

   @Generated
   public File getZippedResourcePack() {
      return this.zippedResourcePack;
   }

   @Generated
   public Map<ModelGenerator.Phase, Set<Runnable>> getTasks() {
      return this.tasks;
   }

   @Generated
   public Set<ModelGenerator.Phase> getExecuted() {
      return this.executed;
   }

   @Generated
   public long[] getTimer() {
      return this.timer;
   }

   @Generated
   public List<File> getFoldersToProcess() {
      return this.foldersToProcess;
   }

   @Generated
   public String getNamespace() {
      return this.namespace;
   }

   @Generated
   public File getModelFolder() {
      return this.modelFolder;
   }

   @Generated
   public File getItemsFolder() {
      return this.itemsFolder;
   }

   @Generated
   public boolean isGenerateMeta() {
      return this.generateMeta;
   }

   @Generated
   public boolean isInitialized() {
      return this.initialized;
   }

   @Generated
   public void setNamespace(String var1) {
      this.namespace = var1;
   }

   @Generated
   public void setModelFolder(File var1) {
      this.modelFolder = var1;
   }

   @Generated
   public void setItemsFolder(File var1) {
      this.itemsFolder = var1;
   }

   @Generated
   public void setGenerateMeta(boolean var1) {
      this.generateMeta = var1;
   }

   @Generated
   public void setInitialized(boolean var1) {
      this.initialized = var1;
   }
}
