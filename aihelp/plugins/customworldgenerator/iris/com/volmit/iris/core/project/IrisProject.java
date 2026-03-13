package com.volmit.iris.core.project;

import com.google.gson.Gson;
import com.volmit.iris.Iris;
import com.volmit.iris.core.IrisSettings;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.IrisRegistrant;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.core.tools.IrisToolbelt;
import com.volmit.iris.engine.object.IrisBiome;
import com.volmit.iris.engine.object.IrisBlockData;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisEntity;
import com.volmit.iris.engine.object.IrisGenerator;
import com.volmit.iris.engine.object.IrisLootTable;
import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisObjectPlacement;
import com.volmit.iris.engine.object.IrisRegion;
import com.volmit.iris.engine.object.IrisSpawner;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.engine.platform.PlatformChunkGenerator;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.collection.KSet;
import com.volmit.iris.util.dom4j.Document;
import com.volmit.iris.util.dom4j.Element;
import com.volmit.iris.util.exceptions.IrisException;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.ChronoLatch;
import com.volmit.iris.util.scheduling.J;
import com.volmit.iris.util.scheduling.O;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import com.volmit.iris.util.scheduling.jobs.JobCollection;
import com.volmit.iris.util.scheduling.jobs.ParallelQueueJob;
import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.zeroturnaround.zip.ZipUtil;

public class IrisProject {
   private File path;
   private String name;
   private PlatformChunkGenerator activeProvider;

   public IrisProject(File path) {
      this.path = var1;
      this.name = var1.getName();
   }

   public static int clean(VolmitSender s, File clean) {
      int var2 = 0;
      if (var1.isDirectory()) {
         File[] var3 = var1.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            var2 += clean(var0, var6);
         }
      } else if (var1.getName().endsWith(".json")) {
         try {
            clean(var1);
         } catch (Throwable var7) {
            Iris.reportError(var7);
            Iris.error("Failed to beautify " + var1.getAbsolutePath() + " You may have errors in your json!");
         }

         ++var2;
      }

      return var2;
   }

   public static void clean(File clean) {
      JSONObject var1 = new JSONObject(IO.readAll(var0));
      fixBlocks(var1, var0);
      IO.writeAll(var0, (Object)var1.toString(4));
   }

   public static void fixBlocks(JSONObject obj, File f) {
      Iterator var2 = var0.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Object var4 = var0.get(var3);
         if (var3.equals("block") && var4 instanceof String && !var4.toString().trim().isEmpty() && !var4.toString().contains(":")) {
            var0.put(var3, (Object)("minecraft:" + String.valueOf(var4)));
            String var10000 = String.valueOf(var4);
            Iris.debug("Updated Block Key: " + var10000 + " to " + var0.getString(var3) + " in " + var1.getPath());
         }

         if (var4 instanceof JSONObject) {
            fixBlocks((JSONObject)var4, var1);
         } else if (var4 instanceof JSONArray) {
            fixBlocks((JSONArray)var4, var1);
         }
      }

   }

   public static void fixBlocks(JSONArray obj, File f) {
      for(int var2 = 0; var2 < var0.length(); ++var2) {
         Object var3 = var0.get(var2);
         if (var3 instanceof JSONObject) {
            fixBlocks((JSONObject)var3, var1);
         } else if (var3 instanceof JSONArray) {
            fixBlocks((JSONArray)var3, var1);
         }
      }

   }

   public boolean isOpen() {
      return this.activeProvider != null;
   }

   public KList<File> collectFiles(File f, String fileExtension) {
      KList var3 = new KList();
      if (var1.isDirectory()) {
         File[] var4 = var1.listFiles();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File var7 = var4[var6];
            var3.addAll(this.collectFiles(var7, var2));
         }
      } else if (var1.getName().endsWith("." + var2)) {
         var3.add((Object)var1);
      }

      return var3;
   }

   public KList<File> collectFiles(String json) {
      return this.collectFiles(this.path, var1);
   }

   public void open(VolmitSender sender) {
      this.open(var1, 1337L, (var0) -> {
      });
   }

   public void openVSCode(VolmitSender sender) {
      IrisDimension var2 = IrisData.loadAnyDimension(this.getName(), (IrisData)null);
      J.attemptAsync(() -> {
         try {
            if (var2.getLoader() == null) {
               var1.sendMessage("Could not get dimension loader");
               return;
            }

            File var3 = var2.getLoader().getDataFolder();
            if (!this.doOpenVSCode(var3)) {
               File var4 = new File(var2.getLoader().getDataFolder(), var2.getLoadKey() + ".code-workspace");
               Iris.warn("Project missing code-workspace: " + var4.getAbsolutePath() + " Re-creating code workspace.");

               try {
                  IO.writeAll(var4, (Object)this.createCodeWorkspaceConfig());
               } catch (IOException var6) {
                  Iris.reportError(var6);
                  var6.printStackTrace();
               }

               this.updateWorkspace();
               if (!this.doOpenVSCode(var3)) {
                  Iris.warn("Tried creating code workspace but failed a second time. Your project is likely corrupt.");
               }
            }
         } catch (Throwable var7) {
            Iris.reportError(var7);
            var7.printStackTrace();
         }

      });
   }

   private boolean doOpenVSCode(File f) {
      boolean var2 = false;
      File[] var3 = (File[])Objects.requireNonNull(var1.listFiles());
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         File var6 = var3[var5];
         if (var6.getName().endsWith(".code-workspace")) {
            var2 = true;
            J.a(() -> {
               this.updateWorkspace();
            });
            if (IrisSettings.get().getStudio().isOpenVSCode() && !GraphicsEnvironment.isHeadless()) {
               Iris.msg("Opening VSCode. You may see the output from VSCode.");
               Iris.msg("VSCode output always starts with: '(node:#####) electron'");
               Desktop.getDesktop().open(var6);
            }
            break;
         }
      }

      return var2;
   }

   public void open(VolmitSender sender, long seed, Consumer<World> onDone) {
      if (this.isOpen()) {
         this.close();
      }

      J.a(() -> {
         IrisDimension var5 = IrisData.loadAnyDimension(this.getName(), (IrisData)null);
         if (var5 == null) {
            var1.sendMessage("Can't find dimension: " + this.getName());
         } else {
            if (var1.isPlayer()) {
               J.s(() -> {
                  var1.player().setGameMode(GameMode.SPECTATOR);
               });
            }

            try {
               this.activeProvider = (PlatformChunkGenerator)IrisToolbelt.createWorld().seed(var2).sender(var1).studio(true).name("iris/" + String.valueOf(UUID.randomUUID())).dimension(var5.getLoadKey()).create().getGenerator();
               var4.accept(this.activeProvider.getTarget().getWorld().realWorld());
            } catch (IrisException var7) {
               var7.printStackTrace();
            }

            this.openVSCode(var1);
         }
      });
   }

   public void close() {
      Iris.debug("Closing Active Provider");
      IrisToolbelt.evacuate(this.activeProvider.getTarget().getWorld().realWorld());
      this.activeProvider.close();
      File var1 = this.activeProvider.getTarget().getWorld().worldFolder();
      Iris.linkMultiverseCore.removeFromConfig(this.activeProvider.getTarget().getWorld().name());
      Bukkit.unloadWorld(this.activeProvider.getTarget().getWorld().name(), false);
      J.attemptAsync(() -> {
         IO.delete(var1);
      });
      Iris.debug("Closed Active Provider " + this.activeProvider.getTarget().getWorld().name());
      this.activeProvider = null;
   }

   public File getCodeWorkspaceFile() {
      return new File(this.path, this.getName() + ".code-workspace");
   }

   public boolean updateWorkspace() {
      this.getPath().mkdirs();
      File var1 = this.getCodeWorkspaceFile();

      try {
         PrecisionStopwatch var2 = PrecisionStopwatch.start();
         JSONObject var3 = this.createCodeWorkspaceConfig();
         IO.writeAll(var1, (Object)var3.toString(4));
         var2.end();
         return true;
      } catch (Throwable var5) {
         Iris.reportError(var5);
         Iris.warn("Project invalid: " + var1.getAbsolutePath() + " Re-creating. You may loose some vs-code workspace settings! But not your actual project!");
         var1.delete();

         try {
            IO.writeAll(var1, (Object)this.createCodeWorkspaceConfig());
         } catch (IOException var4) {
            Iris.reportError(var4);
            var4.printStackTrace();
         }

         return false;
      }
   }

   public JSONObject createCodeWorkspaceConfig() {
      JSONObject var1 = new JSONObject();
      JSONArray var2 = new JSONArray();
      JSONObject var3 = new JSONObject();
      var3.put("path", (Object)".");
      var2.put((Object)var3);
      var1.put("folders", (Object)var2);
      JSONObject var4 = new JSONObject();
      var4.put("workbench.colorTheme", (Object)"Monokai");
      var4.put("workbench.preferredDarkColorTheme", (Object)"Solarized Dark");
      var4.put("workbench.tips.enabled", false);
      var4.put("workbench.tree.indent", 24);
      var4.put("files.autoSave", (Object)"onFocusChange");
      JSONObject var5 = new JSONObject();
      var5.put("editor.autoIndent", (Object)"brackets");
      var5.put("editor.acceptSuggestionOnEnter", (Object)"smart");
      var5.put("editor.cursorSmoothCaretAnimation", true);
      var5.put("editor.dragAndDrop", false);
      var5.put("files.trimTrailingWhitespace", true);
      var5.put("diffEditor.ignoreTrimWhitespace", true);
      var5.put("files.trimFinalNewlines", true);
      var5.put("editor.suggest.showKeywords", false);
      var5.put("editor.suggest.showSnippets", false);
      var5.put("editor.suggest.showWords", false);
      JSONObject var6 = new JSONObject();
      var6.put("strings", true);
      var5.put("editor.quickSuggestions", (Object)var6);
      var5.put("editor.suggest.insertMode", (Object)"replace");
      var4.put("[json]", (Object)var5);
      var4.put("json.maxItemsComputed", 30000);
      JSONArray var7 = new JSONArray();
      IrisData var8 = IrisData.get(this.getPath());
      Iterator var9 = var8.getLoaders().v().iterator();

      while(var9.hasNext()) {
         ResourceLoader var10 = (ResourceLoader)var9.next();
         if (var10.supportsSchemas()) {
            var7.put((Object)var10.buildSchema());
         }
      }

      var9 = var8.resolveSnippets().iterator();

      while(var9.hasNext()) {
         Class var18 = (Class)var9.next();

         try {
            String var11 = ((Snippet)var18.getDeclaredAnnotation(Snippet.class)).value();
            JSONObject var12 = new JSONObject();
            KList var13 = new KList();

            for(int var14 = 1; var14 < 8; ++var14) {
               var13.add((Object)("/snippet/" + var11 + Form.repeat("/*", var14) + ".json"));
            }

            var12.put("fileMatch", (Object)(new JSONArray(var13.toArray())));
            var12.put("url", (Object)("./.iris/schema/snippet/" + var11 + "-schema.json"));
            var7.put((Object)var12);
            File var23 = new File(var8.getDataFolder(), ".iris/schema/snippet/" + var11 + "-schema.json");
            J.attemptAsync(() -> {
               try {
                  IO.writeAll(var23, (Object)(new SchemaBuilder(var18, var8)).construct().toString(4));
               } catch (Throwable var4) {
                  var4.printStackTrace();
               }

            });
         } catch (Throwable var16) {
            var16.printStackTrace();
         }
      }

      var4.put("json.schemas", (Object)var7);
      var1.put("settings", (Object)var4);
      var8.getEnvironment().configureProject();
      File var17 = new File(this.path, ".idea" + File.separator + "jsonSchemas.xml");
      Document var19 = IO.read(var17);
      Element var20 = (Element)var19.selectSingleNode("//component[@name='JsonSchemaMappingsProjectConfiguration']");
      if (var20 == null) {
         var20 = var19.getRootElement().addElement("component").addAttribute("name", "JsonSchemaMappingsProjectConfiguration");
      }

      Element var21 = (Element)var20.selectSingleNode("state");
      if (var21 == null) {
         var21 = var20.addElement("state");
      }

      Element var22 = (Element)var21.selectSingleNode("map");
      if (var22 == null) {
         var22 = var21.addElement("map");
      }

      KMap var24 = new KMap();
      var7.forEach((var1x) -> {
         if (var1x instanceof JSONObject) {
            JSONObject var2 = (JSONObject)var1x;
            String var3 = var2.getString("url");
            String var4 = var2.getJSONArray("fileMatch").getString(0);
            var24.put(var3, var4.substring(1, var4.indexOf("/*")));
         }
      });
      Stream var10000 = var22.selectNodes("entry/value/SchemaInfo/option[@name='relativePathToSchema']").stream().map((var0) -> {
         return var0.valueOf("@value");
      });
      Objects.requireNonNull(var24);
      var10000.forEach(var24::remove);
      var24.forEach((var1x, var2x) -> {
         String var3 = UUID.randomUUID().toString();
         Element var4 = var22.addElement("entry").addAttribute("key", var3).addElement("value").addElement("SchemaInfo");
         var4.addElement("option").addAttribute("name", "generatedName").addAttribute("value", var3);
         var4.addElement("option").addAttribute("name", "name").addAttribute("value", var2x);
         var4.addElement("option").addAttribute("name", "relativePathToSchema").addAttribute("value", var1x);
         Element var5 = var4.addElement("option").addAttribute("name", "patterns").addElement("list").addElement("Item");
         var5.addElement("option").addAttribute("name", "directory").addAttribute("value", "true");
         var5.addElement("option").addAttribute("name", "path").addAttribute("value", var2x);
         var5.addElement("option").addAttribute("name", "mappingKind").addAttribute("value", "Directory");
      });
      if (!var24.isEmpty()) {
         IO.write(var17, var19);
      }

      Gradle.wrapper(this.path);
      return var1;
   }

   public File compilePackage(VolmitSender sender, boolean obfuscate, boolean minify) {
      String var4 = this.getName();
      IrisData var5 = IrisData.get(this.path);
      IrisDimension var6 = (IrisDimension)var5.getDimensionLoader().load(var4);
      File var10002 = Iris.instance.getDataFolder();
      String var10003 = var6.getLoadKey();
      File var7 = new File(var10002, "exports/" + var10003);
      var7.mkdirs();
      Iris.info("Packaging Dimension " + var6.getName() + " " + (var2 ? "(Obfuscated)" : ""));
      KSet var8 = new KSet(new IrisRegion[0]);
      KSet var9 = new KSet(new IrisBiome[0]);
      KSet var10 = new KSet(new IrisEntity[0]);
      KSet var11 = new KSet(new IrisSpawner[0]);
      KSet var12 = new KSet(new IrisGenerator[0]);
      KSet var13 = new KSet(new IrisLootTable[0]);
      KSet var14 = new KSet(new IrisBlockData[0]);
      String[] var15 = var5.getBlockLoader().getPossibleKeys();
      int var16 = var15.length;

      for(int var17 = 0; var17 < var16; ++var17) {
         String var18 = var15[var17];
         var14.add((IrisBlockData)var5.getBlockLoader().load(var18));
      }

      var6.getRegions().forEach((var2x) -> {
         var8.add((IrisRegion)var5.getRegionLoader().load(var2x));
      });
      var6.getLoot().getTables().forEach((var2x) -> {
         var13.add((IrisLootTable)var5.getLootLoader().load(var2x));
      });
      var8.forEach((var2x) -> {
         var9.addAll(var2x.getAllBiomes(() -> {
            return var5;
         }));
      });
      var8.forEach((var2x) -> {
         var2x.getLoot().getTables().forEach((var2) -> {
            var13.add((IrisLootTable)var5.getLootLoader().load(var2));
         });
      });
      var8.forEach((var2x) -> {
         var2x.getEntitySpawners().forEach((var2) -> {
            var11.add((IrisSpawner)var5.getSpawnerLoader().load(var2));
         });
      });
      var6.getEntitySpawners().forEach((var2x) -> {
         var11.add((IrisSpawner)var5.getSpawnerLoader().load(var2x));
      });
      var9.forEach((var2x) -> {
         var2x.getGenerators().forEach((var2) -> {
            var12.add(var2.getCachedGenerator(() -> {
               return var5;
            }));
         });
      });
      var9.forEach((var2x) -> {
         var2x.getLoot().getTables().forEach((var2) -> {
            var13.add((IrisLootTable)var5.getLootLoader().load(var2));
         });
      });
      var9.forEach((var2x) -> {
         var2x.getEntitySpawners().forEach((var2) -> {
            var11.add((IrisSpawner)var5.getSpawnerLoader().load(var2));
         });
      });
      var11.forEach((var2x) -> {
         var2x.getSpawns().forEach((var2) -> {
            var10.add((IrisEntity)var5.getEntityLoader().load(var2.getEntity()));
         });
      });
      KMap var28 = new KMap();
      StringBuilder var30 = new StringBuilder();
      StringBuilder var31 = new StringBuilder();
      var1.sendMessage("Serializing Objects");
      Iterator var19 = var9.iterator();

      while(var19.hasNext()) {
         IrisBiome var20 = (IrisBiome)var19.next();
         Iterator var21 = var20.getObjects().iterator();

         while(var21.hasNext()) {
            IrisObjectPlacement var22 = (IrisObjectPlacement)var21.next();
            var30.append(var22.hashCode());
            KList var23 = new KList();
            Iterator var24 = var22.getPlace().iterator();

            while(var24.hasNext()) {
               String var25 = (String)var24.next();
               if (var28.containsKey(var25)) {
                  var23.add((Object)((String)var28.get(var25)));
               } else {
                  String var26 = !var2 ? var25 : UUID.randomUUID().toString().replaceAll("-", "");
                  var30.append(var26);
                  var23.add((Object)var26);
                  var28.put(var25, var26);
               }
            }

            var22.setPlace(var23);
         }
      }

      KMap var32 = var28.flip();
      StringBuilder var33 = new StringBuilder();
      ChronoLatch var34 = new ChronoLatch(1000L);
      O var35 = new O();
      var35.set(0);
      var9.forEach((var7x) -> {
         var7x.getObjects().forEach((var7xx) -> {
            var7xx.getPlace().forEach((var7x) -> {
               try {
                  File var8 = var5.getObjectLoader().findFile((String)((KList)var32.get(var7x)).get(0));
                  IO.copyFile(var8, new File(var7, "objects/" + var7x + ".iob"));
                  var33.append(IO.hash(var8));
                  var35.set((Integer)var35.get() + 1);
                  if (var34.flip()) {
                     int var9 = (Integer)var35.get();
                     var35.set(0);
                     var1.sendMessage("Wrote another " + var9 + " Objects");
                  }
               } catch (Throwable var10) {
                  Iris.reportError(var10);
               }

            });
         });
      });
      var30.append(IO.hash(var33.toString()));
      var31.append(IO.hash(var30.toString()));
      var30 = new StringBuilder();
      Iris.info("Writing Dimensional Scaffold");

      try {
         String var29 = (new JSONObject((new Gson()).toJson(var6))).toString(var3 ? 0 : 4);
         IO.writeAll(new File(var7, "dimensions/" + var6.getLoadKey() + ".json"), (Object)var29);
         var30.append(IO.hash(var29));
         Iterator var36 = var12.iterator();

         while(var36.hasNext()) {
            IrisGenerator var37 = (IrisGenerator)var36.next();
            var29 = (new JSONObject((new Gson()).toJson(var37))).toString(var3 ? 0 : 4);
            IO.writeAll(new File(var7, "generators/" + var37.getLoadKey() + ".json"), (Object)var29);
            var30.append(IO.hash(var29));
         }

         var31.append(IO.hash(var30.toString()));
         var30 = new StringBuilder();
         var36 = var8.iterator();

         while(var36.hasNext()) {
            IrisRegion var38 = (IrisRegion)var36.next();
            var29 = (new JSONObject((new Gson()).toJson(var38))).toString(var3 ? 0 : 4);
            IO.writeAll(new File(var7, "regions/" + var38.getLoadKey() + ".json"), (Object)var29);
            var30.append(IO.hash(var29));
         }

         var36 = var14.iterator();

         while(var36.hasNext()) {
            IrisBlockData var39 = (IrisBlockData)var36.next();
            var29 = (new JSONObject((new Gson()).toJson(var39))).toString(var3 ? 0 : 4);
            IO.writeAll(new File(var7, "blocks/" + var39.getLoadKey() + ".json"), (Object)var29);
            var30.append(IO.hash(var29));
         }

         var36 = var9.iterator();

         while(var36.hasNext()) {
            IrisBiome var40 = (IrisBiome)var36.next();
            var29 = (new JSONObject((new Gson()).toJson(var40))).toString(var3 ? 0 : 4);
            IO.writeAll(new File(var7, "biomes/" + var40.getLoadKey() + ".json"), (Object)var29);
            var30.append(IO.hash(var29));
         }

         var36 = var10.iterator();

         while(var36.hasNext()) {
            IrisEntity var43 = (IrisEntity)var36.next();
            var29 = (new JSONObject((new Gson()).toJson(var43))).toString(var3 ? 0 : 4);
            IO.writeAll(new File(var7, "entities/" + var43.getLoadKey() + ".json"), (Object)var29);
            var30.append(IO.hash(var29));
         }

         var36 = var13.iterator();

         while(var36.hasNext()) {
            IrisLootTable var44 = (IrisLootTable)var36.next();
            var29 = (new JSONObject((new Gson()).toJson(var44))).toString(var3 ? 0 : 4);
            IO.writeAll(new File(var7, "loot/" + var44.getLoadKey() + ".json"), (Object)var29);
            var30.append(IO.hash(var29));
         }

         var31.append(IO.hash(var30.toString()));
         String var41 = IO.hash(var31.toString());
         JSONObject var45 = new JSONObject();
         var45.put("hash", (Object)var41);
         var45.put("time", M.ms());
         var45.put("version", var6.getVersion());
         IO.writeAll(new File(var7, "package.json"), (Object)var45.toString(var3 ? 0 : 4));
         File var42 = new File(Iris.instance.getDataFolder(), "exports/" + var6.getLoadKey() + ".iris");
         Iris.info("Compressing Package");
         ZipUtil.pack(var7, var42, 9);
         IO.delete(var7);
         var1.sendMessage("Package Compiled!");
         return var42;
      } catch (Throwable var27) {
         Iris.reportError(var27);
         var27.printStackTrace();
         var1.sendMessage("Failed!");
         return null;
      }
   }

   public void compile(VolmitSender sender) {
      final IrisData var2 = IrisData.get(this.getPath());
      KList var3 = new KList();
      KList var4 = new KList();
      KList var5 = new KList();
      this.files(this.getPath(), var4);
      this.filesObjects(this.getPath(), var5);
      var3.add((Object)(new ParallelQueueJob<File>(this) {
         public void execute(File f) {
            try {
               IrisObject var2 = new IrisObject(0, 0, 0);
               var2.read(var1x);
               VolmitSender var10000;
               String var10001;
               if (var2.getBlocks().isEmpty()) {
                  var10000 = var1;
                  var10001 = var1x.getPath();
                  var10000.sendMessageRaw("<hover:show_text:'Error:\n<yellow>" + var10001 + "'><red>- IOB " + var1x.getName() + " has 0 blocks!");
               }

               if (var2.getW() == 0 || var2.getH() == 0 || var2.getD() == 0) {
                  var10000 = var1;
                  var10001 = var1x.getPath();
                  var10000.sendMessageRaw("<hover:show_text:'Error:\n<yellow>" + var10001 + "\n<red>The width height or depth has a zero in it (bad format)'><red>- IOB " + var1x.getName() + " is not 3D!");
               }
            } catch (IOException var3) {
               var3.printStackTrace();
            }

         }

         public String getName() {
            return "IOB";
         }
      }).queue(var5));
      var3.add((Object)(new ParallelQueueJob<File>() {
         public void execute(File f) {
            try {
               JSONObject var2x = new JSONObject(IO.readAll(var1x));
               IrisProject.this.fixBlocks(var2x);
               IrisProject.this.scanForErrors(var2, var1x, var2x, var1);
               IO.writeAll(var1x, (Object)var2x.toString(4));
            } catch (Throwable var3) {
               VolmitSender var10000 = var1;
               String var10001 = var1x.getPath();
               var10000.sendMessageRaw("<hover:show_text:'Error:\n<yellow>" + var10001 + "\n<red>" + var3.getMessage() + "'><red>- JSON Error " + var1x.getName());
            }

         }

         public String getName() {
            return "JSON";
         }
      }).queue(var4));
      (new JobCollection("Compile", var3)).execute(var1);
   }

   private void scanForErrors(IrisData data, File f, JSONObject p, VolmitSender sender) {
      String var5 = var1.toLoadKey(var2);
      ResourceLoader var6 = var1.getTypedLoaderFor(var2);
      if (var6 == null) {
         var4.sendMessageBasic("Can't find loader for " + var2.getPath());
      } else {
         IrisRegistrant var7 = var6.load(var5);
         this.compare(var7.getClass(), var3, var4, new KList());
         var7.scanForErrors(var3, var4);
      }
   }

   public void compare(Class<?> c, JSONObject j, VolmitSender sender, KList<String> path) {
      try {
         Object var5 = var1.getClass().getConstructor().newInstance();
      } catch (Throwable var6) {
      }

   }

   public void files(File clean, KList<File> files) {
      if (var1.isDirectory()) {
         File[] var3 = var1.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            this.files(var6, var2);
         }
      } else if (var1.getName().endsWith(".json")) {
         try {
            var2.add((Object)var1);
         } catch (Throwable var7) {
            Iris.reportError(var7);
         }
      }

   }

   public void filesObjects(File clean, KList<File> files) {
      if (var1.isDirectory()) {
         File[] var3 = var1.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            this.filesObjects(var6, var2);
         }
      } else if (var1.getName().endsWith(".iob")) {
         try {
            var2.add((Object)var1);
         } catch (Throwable var7) {
            Iris.reportError(var7);
         }
      }

   }

   private void fixBlocks(JSONObject obj) {
      Iterator var2 = var1.keySet().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         Object var4 = var1.get(var3);
         if (var3.equals("block") && var4 instanceof String && !var4.toString().trim().isEmpty() && !var4.toString().contains(":")) {
            var1.put(var3, (Object)("minecraft:" + String.valueOf(var4)));
         }

         if (var4 instanceof JSONObject) {
            this.fixBlocks((JSONObject)var4);
         } else if (var4 instanceof JSONArray) {
            this.fixBlocks((JSONArray)var4);
         }
      }

   }

   private void fixBlocks(JSONArray obj) {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         Object var3 = var1.get(var2);
         if (var3 instanceof JSONObject) {
            this.fixBlocks((JSONObject)var3);
         } else if (var3 instanceof JSONArray) {
            this.fixBlocks((JSONArray)var3);
         }
      }

   }

   @Generated
   public File getPath() {
      return this.path;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public PlatformChunkGenerator getActiveProvider() {
      return this.activeProvider;
   }

   @Generated
   public void setPath(final File path) {
      this.path = var1;
   }

   @Generated
   public void setName(final String name) {
      this.name = var1;
   }

   @Generated
   public void setActiveProvider(final PlatformChunkGenerator activeProvider) {
      this.activeProvider = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisProject)) {
         return false;
      } else {
         IrisProject var2 = (IrisProject)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            label47: {
               File var3 = this.getPath();
               File var4 = var2.getPath();
               if (var3 == null) {
                  if (var4 == null) {
                     break label47;
                  }
               } else if (var3.equals(var4)) {
                  break label47;
               }

               return false;
            }

            String var5 = this.getName();
            String var6 = var2.getName();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            PlatformChunkGenerator var7 = this.getActiveProvider();
            PlatformChunkGenerator var8 = var2.getActiveProvider();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisProject;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      File var3 = this.getPath();
      int var6 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      String var4 = this.getName();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      PlatformChunkGenerator var5 = this.getActiveProvider();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getPath());
      return "IrisProject(path=" + var10000 + ", name=" + this.getName() + ", activeProvider=" + String.valueOf(this.getActiveProvider()) + ")";
   }
}
