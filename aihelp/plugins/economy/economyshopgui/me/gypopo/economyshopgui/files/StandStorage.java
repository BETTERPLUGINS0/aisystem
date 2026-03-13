package me.gypopo.economyshopgui.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.stands.Stand;
import me.gypopo.economyshopgui.objects.stands.StandLoc;
import me.gypopo.economyshopgui.objects.stands.StandSettings;
import me.gypopo.economyshopgui.objects.stands.StandType;
import me.gypopo.economyshopgui.util.JsonUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StandStorage {
   private final File file;

   public StandStorage(EconomyShopGUI plugin) {
      this.file = new File(plugin.getDataFolder() + File.separator + "stands.json");

      try {
         if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
         }

         if (!this.file.exists()) {
            this.file.createNewFile();
         }

         if (this.file.length() == 0L) {
            OutputStream outputStream = new FileOutputStream(this.file);
            outputStream.write("[]".getBytes());
            outputStream.close();
         }
      } catch (IOException var3) {
         SendMessage.warnMessage(Lang.ERROR_CREATING_STANDS_STORAGE.get());
         var3.printStackTrace();
      }

   }

   public Set<Stand> getStoredStands() {
      HashSet stands = new HashSet();

      try {
         FileReader reader = new FileReader(this.file);

         try {
            Iterator var3 = JsonUtil.parseArray((InputStreamReader)reader).iterator();

            while(var3.hasNext()) {
               Object obj = var3.next();

               try {
                  JSONObject stand = (JSONObject)obj;
                  int id = JsonUtil.getLong(stand, "stand_id").intValue();
                  StandLoc loc = new StandLoc(JsonUtil.getString(stand, "location"), id);
                  StandType type = StandType.fromName(JsonUtil.getString(stand, "type"));
                  String item = JsonUtil.getString(stand, "item");
                  StandSettings settings = new StandSettings(JsonUtil.getString(stand, "settings"));
                  stands.add(new Stand(id, loc, item, type, settings));
               } catch (Exception var12) {
                  SendMessage.errorMessage(Lang.ERROR_LOADING_SHOP_STAND.get().replace("%stand%", obj.toString()).replace("%reason%", var12.getMessage()));
               }
            }
         } catch (Throwable var13) {
            try {
               reader.close();
            } catch (Throwable var11) {
               var13.addSuppressed(var11);
            }

            throw var13;
         }

         reader.close();
      } catch (Exception var14) {
         SendMessage.warnMessage(Lang.ERROR_LOADING_SHOP_STANDS.get());
         var14.printStackTrace();
      }

      return stands;
   }

   public void save(Set<Stand> stands) {
      this.saveToFile(stands);
   }

   private void saveToFile(Set<Stand> stands) {
      JSONArray array = this.toJSON(stands);
      if (array != null) {
         SendMessage.logDebugMessage(Lang.SAVING_SHOP_STANDS.get().replace("%count%", String.valueOf(stands.size())));

         try {
            FileWriter file = new FileWriter(this.file);

            try {
               file.write(array.toJSONString());
               file.flush();
            } catch (Throwable var7) {
               try {
                  file.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }

               throw var7;
            }

            file.close();
         } catch (IOException var8) {
            SendMessage.warnMessage(Lang.ERROR_SAVING_SHOP_STANDS.get());
            var8.printStackTrace();
         }

      }
   }

   private JSONArray toJSON(Set<Stand> stands) {
      if (stands.isEmpty()) {
         return null;
      } else {
         JSONArray array = new JSONArray();
         Iterator var3 = stands.iterator();

         while(var3.hasNext()) {
            Stand stand = (Stand)var3.next();
            array.add(stand.serialize());
         }

         return array;
      }
   }
}
