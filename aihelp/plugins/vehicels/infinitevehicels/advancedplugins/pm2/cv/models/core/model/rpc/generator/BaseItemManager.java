package advancedplugins.pm2.cv.models.core.model.rpc.generator;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.BaseItemEnum;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.utils.FileUtils;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.logger.LogUtil;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.java.BaseItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;

public class BaseItemManager {
   private final Gson gson;
   private final Map<BaseItemEnum, BaseItemManager.BaseItemCache> baseItems = new ConcurrentHashMap();
   private final Map<String, BlueprintJoint> requested = new LinkedHashMap();
   private final File baseItemFolder;
   private final File cachedIDJson;

   public BaseItemManager(ModelGeneratorImpl var1) {
      this.gson = var1.getGson();
      this.baseItemFolder = FileUtils.createDirectory(var1.getAssetsFolder(), "minecraft", "models", "item");
      this.cachedIDJson = FileUtils.createFileOrEmpty(ModelAPI.PLUGIN.getDataFolder(), ".temp", "storage.json");
   }

   public void updateModels() {
      Set var1 = ConfigProperty.ITEM_MODELS.getBaseItems();
      if (var1.isEmpty()) {
         var1.add(ConfigProperty.ITEM_MODEL.getBaseItem());
      }

      ConcurrentHashMap var2 = new ConcurrentHashMap(this.baseItems);
      this.baseItems.clear();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         BaseItemEnum var4 = (BaseItemEnum)var3.next();
         String var5 = var4.name().toLowerCase(Locale.ENGLISH);
         InputStream var6 = ModelAPI.PLUGIN.getResource("pack/colorable/" + var5 + ".json");
         if (var6 == null) {
            LogUtil.warn("Unknown colorable item: " + var5 + ".");
         } else {
            InputStreamReader var7 = new InputStreamReader(var6, StandardCharsets.UTF_8);
            BaseItem var8 = (BaseItem)this.gson.fromJson(var7, BaseItem.class);
            var8.setName(var5);
            this.baseItems.computeIfAbsent(var4, (var2x) -> {
               BaseItemManager.BaseItemCache var3 = (BaseItemManager.BaseItemCache)var2.get(var2x);
               return var3 == null ? new BaseItemManager.BaseItemCache(var2x, var8, new ModelIdCache()) : var3.setBaseItem(var8);
            });
         }
      }

   }

   public void requestId(BlueprintJoint var1, Set<String> var2) {
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         this.requested.put(var4, var1);
      }

   }

   public void endSession() {
      ModelCacheGroup var1 = new ModelCacheGroup();
      Iterator var2 = this.baseItems.entrySet().iterator();

      Entry var3;
      while(var2.hasNext()) {
         var3 = (Entry)var2.next();
         ModelIdCache var4 = ((BaseItemManager.BaseItemCache)var3.getValue()).getCache();
         var4.gatherExistingIds((BaseItemEnum)var3.getKey(), this.requested);
         var1.cache.put(((BaseItemEnum)var3.getKey()).name(), var4);
      }

      var2 = this.requested.entrySet().iterator();

      while(var2.hasNext()) {
         var3 = (Entry)var2.next();
         BaseItemManager.BaseItemCache var9 = this.pollOptimalCache();
         var9.getCache().generateNewIds(var9.getBaseItemEnum(), (String)var3.getKey(), (BlueprintJoint)var3.getValue());
      }

      var2 = this.baseItems.entrySet().iterator();

      while(var2.hasNext()) {
         var3 = (Entry)var2.next();
         ((BaseItemManager.BaseItemCache)var3.getValue()).getCache().endSession();
      }

      try {
         FileWriter var8 = new FileWriter(this.cachedIDJson);

         try {
            var8.write(this.gson.toJson(var1));
         } catch (Throwable var6) {
            try {
               var8.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         var8.close();
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   public void clearOverrides() {
      this.baseItems.values().forEach(BaseItemManager.BaseItemCache::clearOverrides);
   }

   public void registerModels(String var1) {
      Iterator var2 = this.baseItems.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         BaseItem var4 = ((BaseItemManager.BaseItemCache)var3.getValue()).getBaseItem();
         var4.addModels(var1, ((BaseItemManager.BaseItemCache)var3.getValue()).getCache());
      }

   }

   public void createModelFiles() {
      this.baseItems.values().forEach((var1) -> {
         BaseItem var2 = var1.getBaseItem();
         File var3 = FileUtils.createFile(this.baseItemFolder, var2.getName() + ".json");

         try {
            FileWriter var4 = new FileWriter(var3);

            try {
               var4.write(this.gson.toJson(var2));
            } catch (Throwable var8) {
               try {
                  var4.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            var4.close();
         } catch (IOException var9) {
            var9.printStackTrace();
         }

      });
   }

   public void refreshCache() {
      FileUtils.recreateFile(this.cachedIDJson);

      try {
         FileReader var1 = new FileReader(this.cachedIDJson);

         try {
            ModelCacheGroup var2 = null;
            JsonObject var3 = (JsonObject)this.gson.fromJson(var1, JsonObject.class);
            BaseItemEnum var4;
            if (var3 != null && var3.get("cache") != null && var3.get("cache").getAsJsonObject().get(ConfigProperty.ITEM_MODEL.getBaseItem().name()) != null) {
               String var10000 = String.valueOf(LogUtil.LogColor.BRIGHT_GREEN);
               LogUtil.log(var10000 + "Loading existing model ID cache from " + this.cachedIDJson.getName());
               var4 = ConfigProperty.ITEM_MODEL.getBaseItem();
               ModelIdCache var5 = (ModelIdCache)this.gson.fromJson(var3.get("cache").getAsJsonObject().get(var4.name()), ModelIdCache.class);
               var2 = new ModelCacheGroup();
               var2.cache.put(var4.name(), var5);
            }

            if (var2 == null) {
               var2 = new ModelCacheGroup();
               Iterator var11 = ConfigProperty.ITEM_MODELS.getBaseItems().iterator();

               while(var11.hasNext()) {
                  BaseItemEnum var6 = (BaseItemEnum)var11.next();
                  var2.cache.put(var6.name(), new ModelIdCache());
               }
            }

            Set var12 = ConfigProperty.ITEM_MODELS.getBaseItems();
            if (var12.isEmpty()) {
               var12.add(ConfigProperty.ITEM_MODEL.getBaseItem());
            }

            Iterator var13 = var2.cache.entrySet().iterator();

            while(var13.hasNext()) {
               Entry var7 = (Entry)var13.next();
               var4 = BaseItemEnum.get((String)var7.getKey());
               if (var4 != null && var12.contains(var4)) {
                  this.baseItems.compute(var4, (var1x, var2x) -> {
                     return var2x == null ? new BaseItemManager.BaseItemCache(var1x, new BaseItem(), (ModelIdCache)var7.getValue()) : var2x.setCache((ModelIdCache)var7.getValue());
                  });
               }
            }
         } catch (Throwable var9) {
            try {
               var1.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }

            throw var9;
         }

         var1.close();
      } catch (IOException var10) {
         var10.printStackTrace();
      }

   }

   public void cleanUp() {
      this.baseItems.values().forEach((var0) -> {
         var0.getCache().cleanUp();
      });
   }

   private BaseItemManager.BaseItemCache pollOptimalCache() {
      int var1 = Integer.MAX_VALUE;
      BaseItemManager.BaseItemCache var2 = null;
      Iterator var3 = this.baseItems.values().iterator();

      while(var3.hasNext()) {
         BaseItemManager.BaseItemCache var4 = (BaseItemManager.BaseItemCache)var3.next();
         int var5 = var4.getCache().getCacheLoad();
         if (var5 < var1) {
            var1 = var5;
            var2 = var4;
         }
      }

      if (var2 == null) {
         throw new RuntimeException("No cache is available!");
      } else {
         return var2;
      }
   }

   private static class BaseItemCache {
      private final BaseItemEnum baseItemEnum;
      private BaseItem baseItem;
      private ModelIdCache cache;

      public BaseItemCache(BaseItemEnum var1, BaseItem var2, ModelIdCache var3) {
         this.baseItemEnum = var1;
         this.baseItem = var2;
         this.cache = var3;
      }

      public BaseItemManager.BaseItemCache setBaseItem(BaseItem var1) {
         this.baseItem = var1;
         return this;
      }

      public BaseItemManager.BaseItemCache setCache(ModelIdCache var1) {
         this.cache = var1;
         return this;
      }

      public void clearOverrides() {
         if (this.baseItem != null) {
            this.baseItem.clearOverrides();
         }

      }

      @Generated
      public BaseItemEnum getBaseItemEnum() {
         return this.baseItemEnum;
      }

      @Generated
      public BaseItem getBaseItem() {
         return this.baseItem;
      }

      @Generated
      public ModelIdCache getCache() {
         return this.cache;
      }
   }
}
