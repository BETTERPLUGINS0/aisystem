package tntrun;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.function.Consumer;
import org.bukkit.Bukkit;

public class VersionChecker {
   private TNTRun plugin;
   private final int resourceId;

   public VersionChecker(TNTRun plugin, int resourceId) {
      this.plugin = plugin;
      this.resourceId = resourceId;
   }

   public void getVersion(Consumer<String> consumer) {
      Bukkit.getScheduler().runTaskLaterAsynchronously(this.plugin, () -> {
         try {
            InputStream is = (new URI("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/~")).toURL().openStream();

            try {
               Scanner scann = new Scanner(is);

               try {
                  if (scann.hasNext()) {
                     consumer.accept(scann.next());
                  }
               } catch (Throwable var8) {
                  try {
                     scann.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }

                  throw var8;
               }

               scann.close();
            } catch (Throwable var9) {
               if (is != null) {
                  try {
                     is.close();
                  } catch (Throwable var6) {
                     var9.addSuppressed(var6);
                  }
               }

               throw var9;
            }

            if (is != null) {
               is.close();
            }
         } catch (URISyntaxException | IOException var10) {
            this.plugin.getLogger().info("Unable to check for update: " + var10.getMessage());
         }

      }, 30L);
   }
}
