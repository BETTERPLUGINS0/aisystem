package com.volmit.iris.core.service;

import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.ServerConfigurator;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.nms.INMS;
import com.volmit.iris.core.pack.IrisPack;
import com.volmit.iris.core.project.IrisProject;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.data.cache.AtomicCache;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.json.JSONException;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.IrisService;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.J;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.zeroturnaround.zip.ZipUtil;
import org.zeroturnaround.zip.commons.FileUtils;

public class StudioSVC implements IrisService {
   public static final String LISTING = "https://raw.githubusercontent.com/IrisDimensions/_listing/main/listing-v2.json";
   public static final String WORKSPACE_NAME = "packs";
   private static final AtomicCache<Integer> counter = new AtomicCache();
   private final KMap<String, String> cacheListing = null;
   private IrisProject activeProject;

   public void onEnable() {
      J.s(() -> {
         String var1 = IrisSettings.get().getGenerator().getDefaultWorldType();
         File var2 = IrisPack.packsPack(var1);
         if (!var2.exists()) {
            Iris.info("Downloading Default Pack " + var1);
            if (var1.equals("overworld")) {
               String var3 = "https://github.com/IrisDimensions/overworld/releases/download/" + INMS.OVERWORLD_TAG + "/overworld.zip";
               ((StudioSVC)Iris.service(StudioSVC.class)).downloadRelease(Iris.getSender(), var3, false, false);
            } else {
               this.downloadSearch(Iris.getSender(), var1, false);
            }
         }

      });
   }

   public void onDisable() {
      Iris.debug("Studio Mode Active: Closing Projects");
      Iterator var1 = Bukkit.getWorlds().iterator();

      while(var1.hasNext()) {
         World var2 = (World)var1.next();
         if (IrisToolbelt.isIrisWorld(var2) && IrisToolbelt.isStudio(var2)) {
            IrisToolbelt.evacuate(var2);
            IrisToolbelt.access(var2).close();
         }
      }

   }

   public IrisDimension installIntoWorld(VolmitSender sender, String type, File folder) {
      return this.installInto(var1, var2, new File(var3, "iris/pack"));
   }

   public IrisDimension installInto(VolmitSender sender, String type, File folder) {
      var1.sendMessage("Looking for Package: " + var2);
      IrisDimension var4 = IrisData.loadAnyDimension(var2, (IrisData)null);
      File var15;
      if (var4 == null) {
         File[] var5 = this.getWorkspaceFolder().listFiles();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            File var8 = var5[var7];
            if (var8.isFile() && var8.getName().equals(var2 + ".iris")) {
               var1.sendMessage("Found " + var2 + ".iris in packs folder");
               ZipUtil.unpack(var8, var3);
               break;
            }
         }
      } else {
         var1.sendMessage("Found " + var2 + " dimension in packs folder. Repackaging");
         var15 = (new IrisProject(new File(this.getWorkspaceFolder(), var2))).getPath();

         try {
            FileUtils.copyDirectory(var15, var3);
         } catch (IOException var14) {
            Iris.reportError(var14);
         }
      }

      var15 = new File(var3, "dimensions/" + var2 + ".json");
      if (!var15.exists() || !var15.isFile()) {
         this.downloadSearch(var1, var2, false);
         File var16 = this.getWorkspaceFolder(var2);
         File[] var18 = var16.listFiles();
         int var19 = var18.length;

         for(int var9 = 0; var9 < var19; ++var9) {
            File var10 = var18[var9];
            if (var10.isFile()) {
               try {
                  FileUtils.copyFile(var10, new File(var3, var10.getName()));
               } catch (IOException var13) {
                  var13.printStackTrace();
                  Iris.reportError(var13);
               }
            } else {
               try {
                  FileUtils.copyDirectory(var10, new File(var3, var10.getName()));
               } catch (IOException var12) {
                  var12.printStackTrace();
                  Iris.reportError(var12);
               }
            }
         }

         IO.delete(var16);
      }

      if (var15.exists() && var15.isFile()) {
         IrisData var17 = IrisData.get(var3);
         var17.hotloaded();
         var4 = (IrisDimension)var17.getDimensionLoader().load(var2);
         if (var4 == null) {
            var1.sendMessage("Can't load the dimension! Failed!");
            return null;
         } else {
            var1.sendMessage(var3.getName() + " type installed. ");
            return var4;
         }
      } else {
         var1.sendMessage("Can't find the " + var15.getName() + " in the dimensions folder of this pack! Failed!");
         return null;
      }
   }

   public void downloadSearch(VolmitSender sender, String key, boolean trim) {
      this.downloadSearch(var1, var2, var3, false);
   }

   public void downloadSearch(VolmitSender sender, String key, boolean trim, boolean forceOverwrite) {
      String var5 = "?";

      try {
         var5 = (String)this.getListing(false).get(var2);
         if (var5 == null) {
            Iris.warn("ITS ULL for " + var2);
         }

         var5 = var5 == null ? var2 : var5;
         Iris.info("Assuming URL " + var5);
         String var6 = "master";
         String[] var7 = var5.split("\\Q/\\E");
         String var8 = var7.length == 1 ? "IrisDimensions/" + var7[0] : var7[0] + "/" + var7[1];
         var6 = var7.length > 2 ? var7[2] : var6;
         this.download(var1, var8, var6, var3, var4, false);
      } catch (Throwable var9) {
         Iris.reportError(var9);
         var9.printStackTrace();
         var1.sendMessage("Failed to download '" + var2 + "' from " + var5 + ".");
      }

   }

   public void downloadRelease(VolmitSender sender, String url, boolean trim, boolean forceOverwrite) {
      try {
         this.download(var1, "IrisDimensions", var2, var3, var4, true);
      } catch (Throwable var6) {
         Iris.reportError(var6);
         var6.printStackTrace();
         var1.sendMessage("Failed to download 'IrisDimensions/overworld' from " + var2 + ".");
      }

   }

   public void download(VolmitSender sender, String repo, String branch, boolean trim) {
      this.download(var1, var2, var3, var4, false, false);
   }

   public void download(VolmitSender sender, String repo, String branch, boolean trim, boolean forceOverwrite, boolean directUrl) {
      String var7 = var6 ? var3 : "https://codeload.github.com/" + var2 + "/zip/refs/heads/" + var3;
      var1.sendMessage("Downloading " + var7 + " ");
      File var8 = Iris.getNonCachedFile("pack-" + var4 + "-" + var2, var7);
      File var9 = Iris.getTemp();
      File var10 = new File(var9, "dl-" + String.valueOf(UUID.randomUUID()));
      File var11 = this.getWorkspaceFolder();
      if (var8 != null && var8.exists()) {
         var1.sendMessage("Unpacking " + var2);

         try {
            ZipUtil.unpack(var8, var10);
         } catch (Throwable var21) {
            Iris.reportError(var21);
            var21.printStackTrace();
            var1.sendMessage("Issue when unpacking. Please check/do the following:\n1. Do you have a functioning internet connection?\n2. Did the download corrupt?\n3. Try deleting the */plugins/iris/packs folder and re-download.\n4. Download the pack from the GitHub repo: https://github.com/IrisDimensions/overworld\n5. Contact support (if all other options do not help)");
         }

         File var12 = null;
         File[] var13 = var10.listFiles();
         if (var13 == null) {
            var1.sendMessage("No files were extracted from the zip file.");
         } else {
            try {
               var12 = var13.length > 1 ? var10 : (var13[0].isDirectory() ? var13[0] : null);
            } catch (NullPointerException var20) {
               Iris.reportError(var20);
               var1.sendMessage("Error when finding home directory. Are there any non-text characters in the file name?");
               return;
            }

            if (var12 == null) {
               var1.sendMessage("Invalid Format. Missing root folder or too many folders!");
            } else {
               IrisData var14 = IrisData.get(var12);
               String[] var15 = var14.getDimensionLoader().getPossibleKeys();
               if (var15 != null && var15.length != 0) {
                  if (var15.length != 1) {
                     var1.sendMessage("Dimensions folder must have 1 file in it");
                     return;
                  }
               } else {
                  var1.sendMessage("No dimension file found in the extracted zip file.");
                  var1.sendMessage("Check it is there on GitHub and report this to staff!");
               }

               IrisDimension var16 = (IrisDimension)var14.getDimensionLoader().load(var15[0]);
               var14.close();
               if (var16 == null) {
                  var1.sendMessage("Invalid dimension (folder) in dimensions folder");
               } else {
                  String var17 = var16.getLoadKey();
                  String var10001 = var16.getName();
                  var1.sendMessage("Importing " + var10001 + " (" + var17 + ")");
                  File var18 = new File(var11, var17);
                  if (var5) {
                     IO.delete(var18);
                  }

                  if (IrisData.loadAnyDimension(var17, (IrisData)null) != null) {
                     var1.sendMessage("Another dimension in the packs folder is already using the key " + var17 + " IMPORT FAILED!");
                  } else if (var18.exists() && var18.listFiles().length > 0) {
                     var1.sendMessage("Another pack is using the key " + var17 + ". IMPORT FAILED!");
                  } else {
                     FileUtils.copyDirectory(var12, var18);
                     if (var4) {
                        var1.sendMessage("Trimming " + var17);
                        File var19 = this.compilePackage(var1, var17, false, false);
                        IO.delete(var18);
                        var18.mkdirs();
                        ZipUtil.unpack(var19, var18);
                     }

                     IrisData.getLoaded(var18).ifPresent(IrisData::hotloaded);
                     var1.sendMessage("Successfully Aquired " + var16.getName());
                     ServerConfigurator.installDataPacks(true);
                  }
               }
            }
         }
      } else {
         var1.sendMessage("Failed to find pack at " + var7);
         var1.sendMessage("Make sure you specified the correct repo and branch!");
         var1.sendMessage("For example: /iris download IrisDimensions/overworld branch=master");
      }
   }

   public KMap<String, String> getListing(boolean cached) {
      JSONObject var2;
      if (var1) {
         var2 = new JSONObject(Iris.getCached("cachedlisting", "https://raw.githubusercontent.com/IrisDimensions/_listing/main/listing-v2.json"));
      } else {
         var2 = new JSONObject(Iris.getNonCached("truelisting", "https://raw.githubusercontent.com/IrisDimensions/_listing/main/listing-v2.json"));
      }

      KMap var3 = new KMap();
      Iterator var4 = var2.keySet().iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (var2.get(var5) instanceof String) {
            var3.put(var5, var2.getString(var5));
         }
      }

      var3.put("IrisDimensions/overworld/master", "IrisDimensions/overworld/stable");
      var3.put("overworld", "IrisDimensions/overworld/stable");
      return var3;
   }

   public boolean isProjectOpen() {
      return this.activeProject != null && this.activeProject.isOpen();
   }

   public void open(VolmitSender sender, String dimm) {
      this.open(var1, 1337L, var2);
   }

   public void open(VolmitSender sender, long seed, String dimm) {
      try {
         this.open(var1, var2, var4, (var0) -> {
         });
      } catch (Exception var6) {
         Iris.reportError(var6);
         var1.sendMessage("Error when creating studio world:");
         var6.printStackTrace();
      }

   }

   public void open(VolmitSender sender, long seed, String dimm, Consumer<World> onDone) {
      if (this.isProjectOpen()) {
         this.close();
      }

      IrisProject var6 = new IrisProject(new File(this.getWorkspaceFolder(), var4));
      this.activeProject = var6;
      var6.open(var1, var2, var5);
   }

   public void openVSCode(VolmitSender sender, String dim) {
      (new IrisProject(new File(this.getWorkspaceFolder(), var2))).openVSCode(var1);
   }

   public File getWorkspaceFolder(String... sub) {
      return Iris.instance.getDataFolderList("packs", var1);
   }

   public File getWorkspaceFile(String... sub) {
      return Iris.instance.getDataFileList("packs", var1);
   }

   public void close() {
      if (this.isProjectOpen()) {
         Iris.debug("Closing Active Project");
         this.activeProject.close();
         this.activeProject = null;
      }

   }

   public File compilePackage(VolmitSender sender, String d, boolean obfuscate, boolean minify) {
      return (new IrisProject(new File(this.getWorkspaceFolder(), var2))).compilePackage(var1, var3, var4);
   }

   public void createFrom(String existingPack, String newName) {
      File var3 = this.getWorkspaceFolder(var1);
      File var4 = this.getWorkspaceFolder(var2);
      if (var3.listFiles().length == 0) {
         Iris.warn("Couldn't find the pack to create a new dimension from.");
      } else {
         try {
            FileUtils.copyDirectory(var3, var4, (var0) -> {
               return !var0.getAbsolutePath().contains(".git");
            }, false);
         } catch (IOException var12) {
            Iris.reportError(var12);
            var12.printStackTrace();
         }

         (new File(var3, var1 + ".code-workspace")).delete();
         File var5 = new File(var3, "dimensions/" + var1 + ".json");
         File var6 = new File(var4, "dimensions/" + var2 + ".json");

         try {
            FileUtils.copyFile(var5, var6);
         } catch (IOException var11) {
            Iris.reportError(var11);
            var11.printStackTrace();
         }

         (new File(var4, "dimensions/" + var1 + ".json")).delete();

         try {
            JSONObject var7 = new JSONObject(IO.readAll(var6));
            if (var7.has("name")) {
               var7.put("name", (Object)Form.capitalizeWords(var2.replaceAll("\\Q-\\E", " ")));
               IO.writeAll(var6, (Object)var7.toString(4));
            }
         } catch (IOException | JSONException var10) {
            Iris.reportError(var10);
            var10.printStackTrace();
         }

         try {
            IrisProject var13 = new IrisProject(this.getWorkspaceFolder(var2));
            JSONObject var8 = var13.createCodeWorkspaceConfig();
            IO.writeAll(this.getWorkspaceFile(var2, var2 + ".code-workspace"), (Object)var8.toString(0));
         } catch (IOException | JSONException var9) {
            Iris.reportError(var9);
            var9.printStackTrace();
         }

      }
   }

   public void create(VolmitSender sender, String s, String downloadable) {
      boolean var4 = false;
      File var5 = this.getWorkspaceFolder(var3);
      if (var5.listFiles().length == 0) {
         this.downloadSearch(var1, var3, false);
         if (var5.listFiles().length > 0) {
            var4 = true;
         }
      }

      if (var5.listFiles().length == 0) {
         var1.sendMessage("Couldn't find the pack to create a new dimension from.");
      } else {
         File var6 = new File(var5, "dimensions/" + var3 + ".json");
         if (!var6.exists()) {
            var1.sendMessage("Missing Imported Dimension File");
         } else {
            var1.sendMessage("Importing " + var3 + " into new Project " + var2);
            this.createFrom(var3, var2);
            if (var4) {
               var5.delete();
            }

            this.open(var1, var2);
         }
      }
   }

   public void create(VolmitSender sender, String s) {
      this.create(var1, var2, "example");
   }

   public IrisProject getActiveProject() {
      return this.activeProject;
   }

   public void updateWorkspace() {
      if (this.isProjectOpen()) {
         this.activeProject.updateWorkspace();
      }

   }
}
