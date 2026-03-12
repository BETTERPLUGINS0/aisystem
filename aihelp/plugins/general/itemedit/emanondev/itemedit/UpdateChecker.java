package emanondev.itemedit;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import emanondev.itemedit.utility.SchedulerUtils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import org.jetbrains.annotations.NotNull;

public class UpdateChecker {
   private final APlugin plugin;
   private String newVersion;
   private Boolean isUpdated = null;

   public UpdateChecker(@NotNull APlugin plugin) {
      this.plugin = plugin;
      this.newVersion = plugin.getDescription().getVersion();
   }

   @NotNull
   public String getResourceDownloadUrl() {
      return this.plugin.getPluginAdditionalInfo().getModrinthProjectName() != null ? "https://modrinth.com/plugin/" + this.plugin.getPluginAdditionalInfo().getModrinthProjectName() + "/version/latest" : "https://spigotmc.org/resources/" + this.plugin.getPluginAdditionalInfo().getSpigotResourceId();
   }

   public void logUpdates() {
      SchedulerUtils.runAsync(this.plugin, () -> {
         if (this.loadLatestVersion()) {
            this.isUpdated = this.newVersion.equals(this.plugin.getDescription().getVersion());
         }

         if (this.isUpdated != null && !this.isUpdated) {
            this.plugin.log("&bNEW UPDATE&f (&6" + this.plugin.getDescription().getVersion() + "&f -> &a" + this.newVersion + "&f) available at &b" + this.getResourceDownloadUrl());
         }

      });
   }

   private boolean loadLatestVersion() {
      return this.attemptUpdateCheck("Modrinth", this::loadLatestVersionModrinth) || this.attemptUpdateCheck("Spigot", this::loadLatestVersionSpigot);
   }

   private boolean attemptUpdateCheck(String sourceName, Callable<Boolean> checkMethod) {
      try {
         return (Boolean)checkMethod.call();
      } catch (MalformedURLException var4) {
         this.plugin.log("&cInvalid URL while checking for updates on " + sourceName + ".");
      } catch (FileNotFoundException var5) {
         this.plugin.log("&cUpdate file not found on " + sourceName + ".");
      } catch (UnknownHostException var6) {
         this.plugin.log("&cCannot reach " + sourceName + " server. Check your network.");
      } catch (IOException var7) {
         this.plugin.log("&cI/O error while checking " + sourceName + ": " + var7.getMessage());
      } catch (Exception var8) {
         this.plugin.log("&cUnexpected error on " + sourceName + ".");
         var8.printStackTrace();
      }

      return false;
   }

   private Boolean loadLatestVersionSpigot() throws Exception {
      if (this.plugin.getPluginAdditionalInfo().getSpigotResourceId() == null) {
         return false;
      } else {
         URL checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.plugin.getPluginAdditionalInfo().getSpigotResourceId());
         URLConnection con = checkURL.openConnection();
         this.newVersion = (new BufferedReader(new InputStreamReader(con.getInputStream()))).readLine();
         return true;
      }
   }

   private Boolean loadLatestVersionModrinth() throws Exception {
      if (this.plugin.getPluginAdditionalInfo().getModrinthProjectId() == null) {
         return false;
      } else {
         URL checkURL = new URL("https://api.modrinth.com/v2/project/" + this.plugin.getPluginAdditionalInfo().getModrinthProjectId() + "/version");
         URLConnection con = checkURL.openConnection();
         BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

         try {
            String json = reader.readLine();

            try {
               this.newVersion = JsonParser.parseString(json).getAsJsonArray().get(0).getAsJsonObject().get("version_number").getAsString();
            } catch (NoSuchMethodError var8) {
               JsonElement element = (new JsonParser()).parse(json);
               this.newVersion = element.getAsJsonArray().get(0).getAsJsonObject().get("version_number").getAsString();
            }
         } catch (Throwable var9) {
            try {
               reader.close();
            } catch (Throwable var7) {
               var9.addSuppressed(var7);
            }

            throw var9;
         }

         reader.close();
         return true;
      }
   }
}
