package advancedplugins.pm2.cv.models.core.listener;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.utils.FileUtils;
import advancedplugins.pm2.cv.models.api.utils.ZipUtils;
import advancedplugins.pm2.cv.models.core.ModelAPIImpl;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ItemsAdderListener implements Listener {
   @EventHandler
   public void onItemsAdderPackZip(ItemsAdderLoadDataEvent var1) {
      if (ModelAPI.getModelGenerator().getZippedResourcePack().exists() && !ModelAPI.getModelGenerator().getZippedResourcePack().isDirectory()) {
         File var2 = ModelAPI.getModelGenerator().getZippedResourcePack();
         String var3 = "ItemsAdder/contents/" + ModelAPIImpl.PLUGIN.getName().toLowerCase() + "model/resourcepack";
         File var4 = new File(Bukkit.getPluginsFolder(), var3);
         if (!var4.exists()) {
            var4.getParentFile().mkdirs();
            var4.mkdir();
         }

         try {
            FileUtils.cleanDirectory(var4.toPath());
         } catch (IOException var7) {
            Logger var10000 = ModelAPIImpl.PLUGIN.getLogger();
            String var10001 = var4.getAbsolutePath();
            var10000.warning("Failed to clean directory ( " + var10001 + " ) for ItemsAdder: " + var7.getMessage());
            var7.printStackTrace();
         }

         try {
            ZipUtils.unzip(var2, var4);
         } catch (IOException var6) {
            ModelAPIImpl.PLUGIN.getLogger().severe("Failed to unzip resource pack contents for ItemsAdder: " + var6.getMessage());
            var6.printStackTrace();
         }

      }
   }
}
