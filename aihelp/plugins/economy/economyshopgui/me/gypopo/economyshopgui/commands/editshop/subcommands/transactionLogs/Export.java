package me.gypopo.economyshopgui.commands.editshop.subcommands.transactionLogs;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.methodes.MarketplaceIntegration;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Export extends SubCommad {
   private final EconomyShopGUI plugin;

   public Export(EconomyShopGUI plugin) {
      this.plugin = plugin;
   }

   public String getName() {
      return "export";
   }

   public String getDescription() {
      return "Create a web based Transaction Log view of the currently logged transaction";
   }

   public String getSyntax() {
      return "/editshop logs export";
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop.logs." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      SendMessage.infoMessage(logger, ChatColor.GREEN + "Exporting transaction data, this can take some time depending on the size of the transactions database...");
      this.plugin.runTaskAsync(() -> {
         this.retrieveAndCreate(logger);
      });
   }

   public void retrieveAndCreate(Object logger) {
      long start = System.currentTimeMillis();
      String exported = this.plugin.getTransactionLog().exportLogs();

      try {
         (new Gson()).fromJson(exported, JsonObject.class);
      } catch (JsonSyntaxException var11) {
         SendMessage.errorMessage(logger, "A unknown error occurred while exporting logs data");
         var11.printStackTrace();
         return;
      }

      try {
         String rsp = this.export(exported);
         JsonObject obj = (JsonObject)(new Gson()).fromJson(rsp, JsonObject.class);
         String response = obj.get("response").getAsString();
         if (response.equals("error")) {
            SendMessage.errorMessage(logger, "Failed to export logs at this time with reason: " + response);
            return;
         }

         String id = obj.get("id").getAsString();
         this.plugin.runTask(() -> {
            SendMessage.infoMessage(logger, "Took " + (System.currentTimeMillis() - start) + "ms to create logs view");
            SendMessage.infoMessage(logger, ChatColor.GREEN + "View it here: " + ChatColor.WHITE + "https://logs.gpplugins.com/?id=" + id);
         });
      } catch (NullPointerException var9) {
         SendMessage.errorMessage(logger, "Failed to export logs at this point, please try again soon.");
         var9.printStackTrace();
      } catch (Exception var10) {
         SendMessage.errorMessage(logger, "Unable to reach API server at this point, see console for more details.");
         var10.printStackTrace();
      }

   }

   private String export(String logs) throws IOException {
      URL url = new URL(MarketplaceIntegration.s + "/exportLogs");
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setConnectTimeout(5000);
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("Content-Type", "application/json");
      OutputStream os = connection.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
      osw.write(logs);
      osw.flush();
      osw.close();
      os.close();
      connection.connect();
      return (new BufferedReader(new InputStreamReader(connection.getInputStream()))).readLine();
   }

   private File saveToFile(String data) {
      File out = new File(this.plugin.getDataFolder(), "logs.json");

      try {
         if (!out.exists()) {
            out.createNewFile();
         }
      } catch (IOException var9) {
         System.out.println("Failed to create logs.json");
      }

      try {
         FileWriter file = new FileWriter(out);

         try {
            file.write(data);
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
         System.err.println("Failed to save logs to 'logs.json'");
         var8.printStackTrace();
      }

      return out;
   }

   public List<String> getTabCompletion(String[] args) {
      return null;
   }
}
