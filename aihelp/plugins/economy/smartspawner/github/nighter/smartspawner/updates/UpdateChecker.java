package github.nighter.smartspawner.updates;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import github.nighter.smartspawner.Scheduler;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateChecker implements Listener {
   private final JavaPlugin plugin;
   private final String projectId = "9tQwxSFr";
   private boolean updateAvailable = false;
   private final String currentVersion;
   private String latestVersion = "";
   private String downloadUrl = "";
   private String directLink = "";
   private boolean serverVersionSupported = true;
   private JsonArray latestSupportedVersions = null;
   private static final String CONSOLE_RESET = "\u001b[0m";
   private static final String CONSOLE_BRIGHT_GREEN = "\u001b[92m";
   private static final String CONSOLE_YELLOW = "\u001b[33m";
   private static final String CONSOLE_INDIGO = "\u001b[38;5;93m";
   private static final String CONSOLE_LAVENDER = "\u001b[38;5;183m";
   private static final String CONSOLE_BRIGHT_PURPLE = "\u001b[95m";
   private static final String CONSOLE_RED = "\u001b[91m";
   private final Map<UUID, LocalDate> notifiedPlayers = new HashMap();

   public UpdateChecker(JavaPlugin plugin) {
      this.plugin = plugin;
      this.currentVersion = plugin.getDescription().getVersion();
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.checkForUpdates().thenAccept((hasUpdate) -> {
         if (hasUpdate && this.serverVersionSupported) {
            this.displayConsoleUpdateMessage();
         } else if (!this.serverVersionSupported) {
            this.displayUnsupportedVersionMessage();
         }

      }).exceptionally((ex) -> {
         plugin.getLogger().warning("Failed to check for updates: " + ex.getMessage());
         return null;
      });
   }

   private void displayConsoleUpdateMessage() {
      String modrinthLink = "https://modrinth.com/plugin/9tQwxSFr/version/" + this.latestVersion;
      String frameColor = "\u001b[38;5;93m";
      this.plugin.getLogger().info(frameColor + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\u001b[0m");
      this.plugin.getLogger().info(frameColor + "\u001b[92m         \ud83d\udd2e ꜱᴍᴀʀᴛꜱᴘᴀᴡɴᴇʀ ᴜᴘᴅᴀᴛᴇ ᴀᴠᴀɪʟᴀʙʟᴇ \ud83d\udd2e\u001b[0m");
      this.plugin.getLogger().info(frameColor + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\u001b[0m");
      this.plugin.getLogger().info("");
      this.plugin.getLogger().info(frameColor + "\u001b[0m\ud83d\udce6 ᴄᴜʀʀᴇɴᴛ ᴠᴇʀꜱɪᴏɴ: \u001b[33m" + this.formatConsoleText(this.currentVersion, 31) + "\u001b[0m");
      this.plugin.getLogger().info(frameColor + "\u001b[0m✅ ʟᴀᴛᴇꜱᴛ ᴠᴇʀꜱɪᴏɴ: \u001b[92m" + this.formatConsoleText(this.latestVersion, 32) + "\u001b[0m");
      this.plugin.getLogger().info("");
      this.plugin.getLogger().info(frameColor + "\u001b[0m\ud83d\udce5 ᴅᴏᴡɴʟᴏᴀᴅ ᴛʜᴇ ʟᴀᴛᴇꜱᴛ ᴠᴇʀꜱɪᴏɴ ᴀᴛ:\u001b[0m");
      this.plugin.getLogger().info(frameColor + " \u001b[38;5;183m" + this.formatConsoleText(modrinthLink, 51) + "\u001b[0m");
      this.plugin.getLogger().info("");
      this.plugin.getLogger().info(frameColor + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\u001b[0m");
   }

   private void displayUnsupportedVersionMessage() {
      String frameColor = "\u001b[91m";
      String serverVersion = Bukkit.getVersion();
      this.plugin.getLogger().warning(frameColor + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\u001b[0m");
      this.plugin.getLogger().warning(frameColor + "\u001b[33m      ⚠️  ꜱᴇʀᴠᴇʀ ᴠᴇʀꜱɪᴏɴ ɴᴏ ʟᴏɴɢᴇʀ ꜱᴜᴘᴘᴏʀᴛᴇᴅ  ⚠️\u001b[0m");
      this.plugin.getLogger().warning(frameColor + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\u001b[0m");
      this.plugin.getLogger().warning("");
      this.plugin.getLogger().warning(frameColor + "\u001b[0m\ud83d\udda5️ ʏᴏᴜʀ ꜱᴇʀᴠᴇʀ ᴠᴇʀꜱɪᴏɴ: \u001b[33m" + serverVersion + "\u001b[0m");
      this.plugin.getLogger().warning(frameColor + "\u001b[0m\ud83d\udce6 ʟᴀᴛᴇꜱᴛ ᴘʟᴜɢɪɴ ᴠᴇʀꜱɪᴏɴ: \u001b[92m" + this.latestVersion + "\u001b[0m");
      this.plugin.getLogger().warning(frameColor + "\u001b[0m\ud83c\udfaf ꜱᴜᴘᴘᴏʀᴛᴇᴅ ꜱᴇʀᴠᴇʀ ᴠᴇʀꜱɪᴏɴꜱ: \u001b[38;5;183m" + this.getSupportedVersionsString() + "\u001b[0m");
      this.plugin.getLogger().warning("");
      this.plugin.getLogger().warning(frameColor + "\u001b[0m⚠️  ᴛʜɪꜱ ꜱᴇʀᴠᴇʀ ᴠᴇʀꜱɪᴏɴ ɪꜱ ɴᴏ ʟᴏɴɢᴇʀ ꜱᴜᴘᴘᴏʀᴛᴇᴅ\u001b[0m");
      this.plugin.getLogger().warning(frameColor + "\u001b[0m\ud83d\udccb ᴜᴘᴅᴀᴛᴇ ɴᴏᴛɪꜰɪᴄᴀᴛɪᴏɴꜱ ᴅɪꜱᴀʙʟᴇᴅ\u001b[0m");
      this.plugin.getLogger().warning("");
      this.plugin.getLogger().warning(frameColor + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\u001b[0m");
   }

   private String getSupportedVersionsString() {
      return this.latestSupportedVersions != null && !this.latestSupportedVersions.isEmpty() ? (String)this.latestSupportedVersions.asList().stream().map(JsonElement::getAsString).collect(Collectors.joining(", ")) : "N/A";
   }

   private String formatConsoleText(String text, int maxLength) {
      if (text.length() > maxLength) {
         String var10000 = text.substring(0, maxLength - 3);
         return var10000 + "...";
      } else {
         return text + " ".repeat(maxLength - text.length());
      }
   }

   private boolean isServerVersionSupported(JsonObject latestVersionObj) {
      try {
         String serverVersion = Bukkit.getVersion();
         JsonArray gameVersions = latestVersionObj.getAsJsonArray("game_versions");
         if (gameVersions != null && !gameVersions.isEmpty()) {
            String cleanServerVersion = this.extractMinecraftVersion(serverVersion);
            Iterator var5 = gameVersions.iterator();

            String supportedVersion;
            do {
               if (!var5.hasNext()) {
                  return false;
               }

               JsonElement versionElement = (JsonElement)var5.next();
               supportedVersion = versionElement.getAsString();
            } while(!this.isVersionCompatible(cleanServerVersion, supportedVersion));

            return true;
         } else {
            return true;
         }
      } catch (Exception var8) {
         this.plugin.getLogger().warning("Error checking server version compatibility: " + var8.getMessage());
         return true;
      }
   }

   private String extractMinecraftVersion(String serverVersion) {
      if (serverVersion.contains("MC: ")) {
         String mcPart = serverVersion.substring(serverVersion.indexOf("MC: ") + 4);
         if (mcPart.contains(")")) {
            mcPart = mcPart.substring(0, mcPart.indexOf(")"));
         }

         return mcPart.trim();
      } else {
         if (serverVersion.matches(".*\\d+\\.\\d+(\\.\\d+)?.*")) {
            String[] parts = serverVersion.split("\\s+");
            String[] var3 = parts;
            int var4 = parts.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String part = var3[var5];
               if (part.matches("\\d+\\.\\d+(\\.\\d+)?")) {
                  return part;
               }
            }
         }

         return serverVersion;
      }
   }

   private boolean isVersionCompatible(String serverVersion, String supportedVersion) {
      try {
         if (serverVersion.equals(supportedVersion)) {
            return true;
         } else {
            String[] serverParts = serverVersion.split("\\.");
            String[] supportedParts = supportedVersion.split("\\.");
            if (serverParts.length >= 2 && supportedParts.length >= 2) {
               int serverMajor = Integer.parseInt(serverParts[0]);
               int serverMinor = Integer.parseInt(serverParts[1]);
               int supportedMajor = Integer.parseInt(supportedParts[0]);
               int supportedMinor = Integer.parseInt(supportedParts[1]);
               return serverMajor == supportedMajor && serverMinor == supportedMinor;
            } else {
               return false;
            }
         }
      } catch (NumberFormatException var9) {
         return serverVersion.equals(supportedVersion);
      }
   }

   public CompletableFuture<Boolean> checkForUpdates() {
      return CompletableFuture.supplyAsync(() -> {
         try {
            URL url = new URL("https://api.modrinth.com/v2/project/9tQwxSFr/version");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "SmartSpawner-UpdateChecker/1.0");
            if (connection.getResponseCode() != 200) {
               this.plugin.getLogger().warning("Failed to check for updates. HTTP Error: " + connection.getResponseCode());
               return false;
            } else {
               BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
               String response = (String)reader.lines().collect(Collectors.joining("\n"));
               reader.close();
               JsonArray versions = JsonParser.parseString(response).getAsJsonArray();
               if (versions.isEmpty()) {
                  return false;
               } else {
                  JsonObject latestVersionObj = null;
                  Iterator var7 = versions.iterator();

                  JsonObject version;
                  while(var7.hasNext()) {
                     JsonElement element = (JsonElement)var7.next();
                     version = element.getAsJsonObject();
                     String versionType = version.get("version_type").getAsString();
                     if (versionType.equals("release")) {
                        if (latestVersionObj == null) {
                           latestVersionObj = version;
                        } else {
                           String currentDate = latestVersionObj.get("date_published").getAsString();
                           String newDate = version.get("date_published").getAsString();
                           if (newDate.compareTo(currentDate) > 0) {
                              latestVersionObj = version;
                           }
                        }
                     }
                  }

                  if (latestVersionObj == null) {
                     return false;
                  } else {
                     this.latestVersion = latestVersionObj.get("version_number").getAsString();
                     String versionId = latestVersionObj.get("id").getAsString();
                     this.downloadUrl = "https://modrinth.com/plugin/9tQwxSFr/version/" + this.latestVersion;
                     JsonArray files = latestVersionObj.getAsJsonArray("files");
                     if (!files.isEmpty()) {
                        version = files.get(0).getAsJsonObject();
                        this.directLink = version.get("url").getAsString();
                     }

                     this.serverVersionSupported = this.isServerVersionSupported(latestVersionObj);
                     this.latestSupportedVersions = latestVersionObj.getAsJsonArray("game_versions");
                     Version latest = new Version(this.latestVersion);
                     Version current = new Version(this.currentVersion);
                     this.updateAvailable = latest.compareTo(current) > 0;
                     return this.updateAvailable;
                  }
               }
            }
         } catch (Exception var13) {
            this.plugin.getLogger().warning("Error checking for updates: " + var13.getMessage());
            var13.printStackTrace();
            return false;
         }
      });
   }

   private void sendUpdateNotification(Player player) {
      if (this.updateAvailable && this.serverVersionSupported && player.hasPermission("smartspawner.admin")) {
         TextColor primaryPurple = TextColor.fromHexString("#ab7afd");
         TextColor deepPurple = TextColor.fromHexString("#7b68ee");
         TextColor indigo = TextColor.fromHexString("#5B2C6F");
         TextColor brightGreen = TextColor.fromHexString("#37eb9a");
         TextColor yellow = TextColor.fromHexString("#f0c857");
         TextColor white = TextColor.fromHexString("#e6e6fa");
         Component borderTop = Component.text("━━━━━━━━ ꜱᴍᴀʀᴛꜱᴘᴀᴡɴᴇʀ ᴜᴘᴅᴀᴛᴇ ━━━━━━━━").color(deepPurple);
         Component borderBottom = Component.text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━").color(deepPurple);
         Component updateMsg = Component.text("➤ ɴᴇᴡ ᴜᴘᴅᴀᴛᴇ ᴀᴠᴀɪʟᴀʙʟᴇ!").color(brightGreen);
         Component versionsComponent = ((TextComponent)((TextComponent)((TextComponent)Component.text("✦ ᴄᴜʀʀᴇɴᴛ: ").color(white)).append(Component.text(this.currentVersion).color(yellow))).append(Component.text("  ✦ ʟᴀᴛᴇꜱᴛ: ").color(white))).append(Component.text(this.latestVersion).color(brightGreen));
         Component downloadButton = ((TextComponent)((TextComponent)Component.text("▶ [ᴄʟɪᴄᴋ ᴛᴏ ᴅᴏᴡɴʟᴏᴀᴅ ʟᴀᴛᴇꜱᴛ ᴠᴇʀꜱɪᴏɴ]").color(primaryPurple)).clickEvent(ClickEvent.openUrl(this.downloadUrl))).hoverEvent(HoverEvent.showText(((TextComponent)Component.text("ᴅᴏᴡɴʟᴏᴀᴅ ᴠᴇʀꜱɪᴏɴ ").color(white)).append(Component.text(this.latestVersion).color(brightGreen))));
         player.sendMessage(" ");
         player.sendMessage(borderTop);
         player.sendMessage(" ");
         player.sendMessage(updateMsg);
         player.sendMessage(versionsComponent);
         player.sendMessage(downloadButton);
         player.sendMessage(" ");
         player.sendMessage(borderBottom);
         player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8F, 1.2F);
      }
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      if (player.isOp()) {
         UUID playerId = player.getUniqueId();
         LocalDate today = LocalDate.now();
         this.notifiedPlayers.entrySet().removeIf((entry) -> {
            return ((LocalDate)entry.getValue()).isBefore(today);
         });
         if (this.notifiedPlayers.containsKey(playerId) && ((LocalDate)this.notifiedPlayers.get(playerId)).isEqual(today)) {
            return;
         }

         if (this.updateAvailable && this.serverVersionSupported) {
            Scheduler.runTaskLater(() -> {
               this.sendUpdateNotification(player);
               this.notifiedPlayers.put(playerId, today);
            }, 40L);
         } else {
            if (!this.serverVersionSupported) {
               return;
            }

            this.checkForUpdates().thenAccept((hasUpdate) -> {
               if (hasUpdate && this.serverVersionSupported) {
                  Scheduler.runTask(() -> {
                     this.sendUpdateNotification(player);
                     this.notifiedPlayers.put(playerId, today);
                  });
               }

            });
         }
      }

   }
}
