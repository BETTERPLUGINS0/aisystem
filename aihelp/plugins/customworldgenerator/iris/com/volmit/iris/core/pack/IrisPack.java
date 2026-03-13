package com.volmit.iris.core.pack;

import com.volmit.iris.Iris;
import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.core.loader.ResourceLoader;
import com.volmit.iris.engine.object.IrisDimension;
import com.volmit.iris.engine.object.IrisWorld;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.exceptions.IrisException;
import com.volmit.iris.util.format.Form;
import com.volmit.iris.util.io.IO;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;
import com.volmit.iris.util.plugin.VolmitSender;
import com.volmit.iris.util.scheduling.PrecisionStopwatch;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import lombok.Generated;
import org.bukkit.World;
import org.zeroturnaround.zip.commons.FileUtils;

public class IrisPack {
   private final File folder;
   private final IrisData data;

   public IrisPack(String name) {
      this(packsPack(var1));
   }

   public IrisPack(File folder) {
      this.folder = var1;
      if (!var1.exists()) {
         throw new RuntimeException("Cannot open Pack " + var1.getPath() + " (directory doesnt exist)");
      } else if (!var1.isDirectory()) {
         throw new RuntimeException("Cannot open Pack " + var1.getPath() + " (not a directory)");
      } else {
         this.data = IrisData.get(var1);
      }
   }

   public static Future<IrisPack> from(VolmitSender sender, String url) {
      IrisPackRepository var2 = IrisPackRepository.from(var1);
      if (var2 == null) {
         throw new IrisException("Null Repo");
      } else {
         try {
            return from(var0, var2);
         } catch (MalformedURLException var4) {
            throw new IrisException("Malformed URL " + var4.getMessage());
         }
      }
   }

   public static Future<IrisPack> from(VolmitSender sender, IrisPackRepository repo) {
      CompletableFuture var2 = new CompletableFuture();
      var1.install(var0, () -> {
         var2.complete(new IrisPack(var1.getRepo()));
      });
      return var2;
   }

   public static IrisPack blank(String name) {
      File var1 = packsPack(var0);
      if (var1.exists()) {
         throw new IrisException("Already exists");
      } else {
         File var2 = new File(var1, "dimensions/" + var0 + ".json");
         var2.getParentFile().mkdirs();

         try {
            IO.writeAll(var2, (Object)("{\n    \"name\": \"" + Form.capitalize(var0) + "\",\n    \"version\": 1\n}\n"));
         } catch (IOException var4) {
            throw new IrisException(var4.getMessage(), var4);
         }

         IrisPack var3 = new IrisPack(var1);
         var3.updateWorkspace();
         return var3;
      }
   }

   public static File packsPack(String name) {
      return Iris.instance.getDataFolderNoCreate(new String[]{"packs", var0});
   }

   private static KList<File> collectFiles(File f, String fileExtension) {
      KList var2 = new KList();
      if (var0.isDirectory()) {
         File[] var3 = var0.listFiles();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            File var6 = var3[var5];
            var2.addAll(collectFiles(var6, var1));
         }
      } else if (var0.getName().endsWith("." + var1)) {
         var2.add((Object)var0);
      }

      return var2;
   }

   public void delete() {
      IO.delete(this.folder);
      this.folder.delete();
   }

   public String getName() {
      return this.folder.getName();
   }

   public File getWorkspaceFile() {
      return new File(this.getFolder(), this.getName() + ".code-workspace");
   }

   public boolean updateWorkspace() {
      this.getFolder().mkdirs();
      File var1 = this.getWorkspaceFile();

      try {
         PrecisionStopwatch var2 = PrecisionStopwatch.start();
         Iris.debug("Building Workspace: " + var1.getPath());
         JSONObject var3 = this.generateWorkspaceConfig();
         IO.writeAll(var1, (Object)var3.toString(4));
         var2.end();
         String var10000 = var1.getPath();
         Iris.debug("Building Workspace: " + var10000 + " took " + Form.duration(var2.getMilliseconds(), 2));
         return true;
      } catch (Throwable var5) {
         Iris.reportError(var5);
         Iris.warn("Pack invalid: " + var1.getAbsolutePath() + " Re-creating. You may loose some vs-code workspace settings! But not your actual project!");
         var1.delete();

         try {
            IO.writeAll(var1, (Object)this.generateWorkspaceConfig());
         } catch (IOException var4) {
            Iris.reportError(var4);
            var4.printStackTrace();
         }

         return false;
      }
   }

   public IrisPack install(World world) {
      return this.install(new File(var1.getWorldFolder(), "iris/pack"));
   }

   public IrisPack install(IrisWorld world) {
      return this.install(new File(var1.worldFolder(), "iris/pack"));
   }

   public IrisPack install(File folder) {
      if (var1.exists()) {
         throw new IrisException("Cannot install new pack because the folder " + var1.getName() + " already exists!");
      } else {
         var1.mkdirs();

         try {
            FileUtils.copyDirectory(this.getFolder(), var1);
         } catch (IOException var3) {
            Iris.reportError(var3);
         }

         return new IrisPack(var1);
      }
   }

   public IrisPack install(String newName) {
      File var2 = packsPack(var1);
      if (var2.exists()) {
         throw new IrisException("Cannot install new pack because the folder " + var1 + " already exists!");
      } else {
         try {
            FileUtils.copyDirectory(this.getFolder(), var2);
         } catch (IOException var9) {
            Iris.reportError(var9);
         }

         IrisData var3 = IrisData.get(var2);
         IrisDimension var4 = (IrisDimension)var3.getDimensionLoader().load(this.getDimensionKey());
         var3.dump();
         File var5 = var4.getLoadFile();
         File var6 = new File(var5.getParentFile(), var1 + ".json");

         try {
            FileUtils.moveFile(var5, var6);
            (new File(var2, this.getWorkspaceFile().getName())).delete();
         } catch (Throwable var8) {
            throw new IrisException(var8);
         }

         IrisPack var7 = new IrisPack(var2);
         var7.updateWorkspace();
         return var7;
      }
   }

   public String getDimensionKey() {
      return this.getName();
   }

   public IrisDimension getDimension() {
      return (IrisDimension)this.getData().getDimensionLoader().load(this.getDimensionKey());
   }

   public KList<File> collectFiles(String fileExtension) {
      return collectFiles(this.getFolder(), var1);
   }

   private JSONObject generateWorkspaceConfig() {
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
      IrisData var8 = IrisData.get(this.getFolder());
      Iterator var9 = var8.getLoaders().v().iterator();

      while(var9.hasNext()) {
         ResourceLoader var10 = (ResourceLoader)var9.next();
         if (var10.supportsSchemas()) {
            var7.put((Object)var10.buildSchema());
         }
      }

      var4.put("json.schemas", (Object)var7);
      var1.put("settings", (Object)var4);
      return var1;
   }

   @Generated
   public File getFolder() {
      return this.folder;
   }

   @Generated
   public IrisData getData() {
      return this.data;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisPack)) {
         return false;
      } else {
         IrisPack var2 = (IrisPack)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            File var3 = this.getFolder();
            File var4 = var2.getFolder();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            IrisData var5 = this.getData();
            IrisData var6 = var2.getData();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisPack;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      File var3 = this.getFolder();
      int var5 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisData var4 = this.getData();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getFolder());
      return "IrisPack(folder=" + var10000 + ", data=" + String.valueOf(this.getData()) + ")";
   }
}
