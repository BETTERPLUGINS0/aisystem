package me.gypopo.economyshopgui.methodes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.Lang;

public class UpdateChecker {
   public final UpdateChecker.Version CURRENT_VERSION = new UpdateChecker.Version(EconomyShopGUI.getInstance().getDescription().getVersion());
   public UpdateChecker.Version LATEST_VERSION;
   public boolean updateAvailable;

   public UpdateChecker() {
      EconomyShopGUI.getInstance().runKillableTask(() -> {
         try {
            InputStream inputStream = (new URL(this.CURRENT_VERSION.isDev() ? "https://api.gpplugins.com:2096/val/latestBeta?plugin=esgui" : "https://api.spigotmc.org/legacy/update.php?resource=69927")).openStream();

            try {
               this.LATEST_VERSION = new UpdateChecker.Version((new BufferedReader(new InputStreamReader(inputStream))).readLine());
               this.updateAvailable = this.isUpdateAvailable();
               this.checkForUpdates();
            } catch (Throwable var5) {
               if (inputStream != null) {
                  try {
                     inputStream.close();
                  } catch (Throwable var4) {
                     var5.addSuppressed(var4);
                  }
               }

               throw var5;
            }

            if (inputStream != null) {
               inputStream.close();
            }
         } catch (Exception var6) {
            SendMessage.infoMessage(Lang.COULD_NOT_CHECK_FOR_UPDATES.get());
            this.updateAvailable = false;
         }

      });
   }

   private boolean isUpdateAvailable() {
      if (this.CURRENT_VERSION.isSmaller(this.LATEST_VERSION)) {
         return this.CURRENT_VERSION.isDev() || !this.LATEST_VERSION.isDev();
      } else {
         return false;
      }
   }

   public void checkForUpdates() {
      if (this.updateAvailable) {
         SendMessage.infoMessage(Lang.UPDATE_AVAILABLE.get().replace("%plugin_version%", this.CURRENT_VERSION.getVer()).replace("%latest_version%", this.LATEST_VERSION.getVer()));
         SendMessage.infoMessage(this.LATEST_VERSION.isDev() ? "Download at: https://open-beta.gpplugins.com/economyshopgui/" : "Download at: https://www.spigotmc.org/resources/economyshopgui.69927/");
      }

   }

   public static final class Version {
      private final String ver;
      private final boolean dev;
      private int major = 0;
      private int minor = 0;
      private int patch = 0;
      private int beta = 0;

      public Version(String version) {
         this.ver = version;
         String[] parts;
         if (version.matches(".*[a-zA-Z].*")) {
            parts = version.replace("b", "").replace("a", "").replace("r", "").replace("-", "").replace("v", "").split("\\.");
            this.dev = true;
         } else {
            parts = version.split("\\.");
            this.dev = false;
         }

         try {
            this.major = parts.length >= 1 ? Integer.parseInt(parts[0]) : 0;
            this.minor = parts.length >= 2 ? Integer.parseInt(parts[1]) : 0;
            this.patch = parts.length >= 3 ? Integer.parseInt(parts[2]) : 0;
            this.beta = parts.length >= 4 ? Integer.parseInt(parts[3]) : 0;
         } catch (NumberFormatException var4) {
            EconomyShopGUI.getInstance().getServer().getLogger().warning("Invalid version numbering for '" + version + "'");
         }

      }

      public boolean isGreater(UpdateChecker.Version ver) {
         return this.major > ver.major || this.major == ver.major && this.minor > ver.minor || this.major == ver.major && this.minor == ver.minor && this.patch > ver.patch || this.major == ver.major && this.minor == ver.minor && this.patch == ver.patch && this.beta > ver.beta;
      }

      public boolean isSmaller(UpdateChecker.Version ver) {
         return this.major < ver.major || this.major == ver.major && this.minor < ver.minor || this.major == ver.major && this.minor == ver.minor && this.patch < ver.patch || this.major == ver.major && this.minor == ver.minor && this.patch == ver.patch && this.beta < ver.beta;
      }

      public boolean isSame(UpdateChecker.Version ver) {
         return this.major == ver.major && this.minor == ver.minor && this.patch == ver.patch && this.beta == ver.beta;
      }

      public boolean isDev() {
         return this.dev;
      }

      public String getVer() {
         return this.ver;
      }

      public String toString() {
         return this.major + "." + this.minor + "." + this.patch;
      }
   }
}
