package me.gypopo.economyshopgui.methodes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.config.Config;
import me.gypopo.economyshopgui.objects.inventorys.AuthRequest;
import me.gypopo.economyshopgui.objects.inventorys.CustomizeLayout;
import me.gypopo.economyshopgui.objects.inventorys.FileUploader;
import me.gypopo.economyshopgui.objects.inventorys.UpdateMessage;
import me.gypopo.economyshopgui.objects.layouts.SimpleCard;
import me.gypopo.economyshopgui.objects.layouts.SimpleLayout;
import me.gypopo.economyshopgui.util.ConfigUtil;
import me.gypopo.economyshopgui.util.ConfirmRequest;
import me.gypopo.economyshopgui.util.SimplePair;
import me.gypopo.economyshopgui.util.exceptions.ConfigLoadException;
import me.gypopo.economyshopgui.util.exceptions.ConfigSaveException;
import me.gypopo.economyshopgui.util.exceptions.LayoutNotFoundException;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MarketplaceIntegration {
   private final EconomyShopGUI plugin = EconomyShopGUI.getInstance();
   private static long token = (new SecureRandom()).nextLong();
   private final File dir;
   public static String s;
   public static String i;
   public static HashSet<String> bannedOptions = new HashSet(Arrays.asList("update-checking", "debug", "use-nms", "transaction-log", "enable-disabled-worlds", "disabled-worlds-per-command", "economy-provider", "spawner-provider", "enable-discordsrv-hook", "discordsrv-transactions", "log-player-transactions", "drop-remaining-items-on-ground", "sell-shulker-boxes", "banned-gamemodes", "override-sell-command", "old-sellall-command", "commands", "enable-spawnerbreak", "enable-spawnerplace", "allow-spawner-type-change", "player-placed-spawners-drop-exp", "only-mine-plugin-spawners", "spawner-break-tools"));
   public static HashSet<String> listOptions = new HashSet(Arrays.asList("shop-commands", "sellall-commands", "sellgui-commands"));
   public static String data;

   public MarketplaceIntegration() {
      this.dir = new File(this.plugin.getDataFolder() + "/layouts");
      if (!this.dir.exists()) {
         this.dir.mkdirs();
      }

   }

   public void startUpload(Player p) {
      new CustomizeLayout(p, (card) -> {
         card.setToken(token);
         new FileUploader(p, (files) -> {
            if (files == null) {
               p.sendMessage(ChatColor.RED + "Cancelled upload process");
            } else {
               new AuthRequest(p, (method) -> {
                  card.setAuthMethod(method);
                  this.requestCard(p, card, (s) -> {
                     this.plugin.runTask(() -> {
                        Runnable onComplete = () -> {
                           if (card.getAuthMethod() == AuthRequest.AuthMethod.DISCORD) {
                              SendMessage.chatToPlayer(p, ChatColor.GREEN + "Validation success, started upload process, zipping files...");
                           } else {
                              SendMessage.chatToPlayer(p, ChatColor.GREEN + "Started upload process, zipping files...");
                           }

                           this.startUpload(p, files, s, (String)null, (String)null, MarketplaceIntegration.UploadReason.CREATE);
                        };
                        switch(method) {
                        case TEMP:
                           onComplete.run();
                           break;
                        case DISCORD:
                           p.closeInventory();
                           p.sendTitle("§1§lLogin required", "§6§lClick the link in chat to authorize", 1, 80, 1);
                           TextComponent message = new TextComponent(TextComponent.fromLegacyText("§cValidation is required to continue, click here to validate via discord."));
                           message.setClickEvent(new ClickEvent(Action.OPEN_URL, this.getAuthorizeURL(s)));
                           SendMessage.chatToPlayer(p, message);
                           this.checkVerification(p, s, 6000L, onComplete);
                           break;
                        default:
                           SendMessage.chatToPlayer(p, ChatColor.RED + "Cancelled upload process");
                        }

                     });
                  });
               });
            }

         });
      });
   }

   private void requestCard(Player p, SimpleCard card, Consumer<String> onComplete) {
      this.plugin.runKillableTask(() -> {
         String s;
         try {
            s = this.sendRequest(MarketplaceIntegration.s + "/createLayout?token=" + token, card.toString());
         } catch (Exception var7) {
            SendMessage.chatToPlayer(p, ChatColor.RED + "Unable to reach API server at this point, see console for more details.");
            var7.printStackTrace();
            return;
         }

         try {
            UUID.fromString(s);
         } catch (IllegalArgumentException var6) {
            SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to authorize with server, see console for details.");
            var6.printStackTrace();
            return;
         }

         onComplete.accept(s);
      });
   }

   private void startUpload(Player p, ArrayList<String> files, String id, String code, String msg, MarketplaceIntegration.UploadReason reason) {
      this.plugin.runKillableTask(() -> {
         Path cDir;
         try {
            cDir = this.createZipFile();
            FileOutputStream outputStream = new FileOutputStream(cDir.toString());
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
            this.zipLayout(files, zipOutputStream);
            zipOutputStream.close();
            outputStream.close();
         } catch (IOException var11) {
            SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to create a copy of the current shop layout, see console for details.");
            var11.printStackTrace();
            return;
         }

         SendMessage.chatToPlayer(p, ChatColor.GREEN + "Successfully compressed layout, uploading to server...");
         SendMessage.chatToPlayer(p, ChatColor.GREEN + "This may take some time, you can continue other things while this process runs in the background...");

         try {
            int status = reason == MarketplaceIntegration.UploadReason.CREATE ? this.upload(cDir, id) : this.update(cDir, id, code, msg);
            switch(status) {
            case 202:
               this.checkStatus(p, id, 6000L, (response) -> {
                  if (response.success) {
                     SendMessage.chatToPlayer(p, ChatColor.GREEN + "Your shop layout was uploaded successfully, you may visit your layout on our website here: " + ChatColor.WHITE + "https://layouts.gpplugins.com/?layout=" + id);
                  } else if (response.reason.equals("deniedBadQuality")) {
                     SendMessage.chatToPlayer(p, ChatColor.RED + "Your shop layout with id " + id + " was denied for low quality.\n" + ChatColor.RED + "Please see our resource guidelines which you can find here for reasons of denial: " + ChatColor.WHITE + "https://layouts.gpplugins.com/guidelines");
                     SendMessage.warnMessage("Shop layout " + id + " was denied for low quality");
                  } else {
                     SendMessage.chatToPlayer(p, ChatColor.RED + "A error occurred while processing your shop layout, please try again soon or if this keeps happening, contact the developers.");
                     SendMessage.warnMessage("A error occurred while uploading shop layout " + id + " with reason: " + response.reason);
                  }

                  token = (new SecureRandom()).nextLong();
               });
               break;
            case 406:
            case 415:
               SendMessage.chatToPlayer(p, ChatColor.RED + "The shop layout was denied by the server, please make sure the files contain no extra content as needed.");
               SendMessage.errorMessage("Server responded with status code '" + status + "'");
               token = (new SecureRandom()).nextLong();
               break;
            default:
               SendMessage.chatToPlayer(p, ChatColor.RED + "The server failed to process your request at this time, see console for details.");
               SendMessage.errorMessage("Server responded with status code '" + status + "'");
               token = (new SecureRandom()).nextLong();
            }
         } catch (IOException var10) {
            SendMessage.chatToPlayer(p, ChatColor.RED + "Error occurred while uploading layout, see console for details.");
            token = (new SecureRandom()).nextLong();
            var10.printStackTrace();
         }

      });
   }

   public void startUpdate(Player p, String id) {
      this.plugin.runKillableTask(() -> {
         SimplePair layout;
         try {
            layout = this.getLayout(id, "/createUpdate");
         } catch (LayoutNotFoundException var5) {
            SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to find a layout with ID " + id + ", make sure the ID is spelled correct and the layout still exists. Keep in mind that temporary layouts are removed after 14 days.");
            return;
         } catch (NullPointerException var6) {
            SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to fetch layout from server at this time, please try again soon or see console for more details.");
            SendMessage.warnMessage(var6.getMessage());
            var6.printStackTrace();
            return;
         } catch (Exception var7) {
            SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to fetch layout from server, see console for more details.");
            var7.printStackTrace();
            return;
         }

         if (!((SimpleLayout)layout.key).authMethod.equals("DISCORD")) {
            SendMessage.chatToPlayer(p, ChatColor.RED + "Given layout is not uploaded using discord validation, it is not possible to update this layout!");
         } else {
            p.sendTitle("§c§lValidation required", "§6§lClick the link in chat to authorize", 1, 80, 1);
            TextComponent message = new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "Validation is required to continue, click here to validate via discord."));
            message.setClickEvent(new ClickEvent(Action.OPEN_URL, this.getAuthorizeURL(id)));
            SendMessage.chatToPlayer(p, message);
            this.checkVerification(p, id, 6000L, () -> {
               SendMessage.chatToPlayer(p, ChatColor.GREEN + "Verification success!");
               new UpdateMessage(p, (msg) -> {
                  new FileUploader(p, (files) -> {
                     p.closeInventory();
                     if (files == null) {
                        p.sendMessage(ChatColor.RED + "Cancelled update process");
                     } else {
                        SendMessage.chatToPlayer(p, ChatColor.translateAlternateColorCodes('&', "&aYou are about to update layout &c%layoutID% &awith data: \n&6Title: &e%title% \n&6Description: &e%description% \n&6Tags: \n&8- &e%tags% \n&6Compatible MC version(s): &e%mcVer% \n&6Created for plugin version: &e%plVer% \n&6Created at: &e%creationDate% \n&6Downloads: &e%downloads% \n&6Shop files: \n&8- &e%files%".replace("%layoutID%", id).replace("%title%", ((SimpleLayout)layout.key).title).replace("%description%", ((SimpleLayout)layout.key).description).replace("%tags%", String.join("\n&8- &e", ((SimpleLayout)layout.key).tags)).replace("%files%", String.join("\n&8- &e", ((SimpleLayout)layout.key).files)).replace("%creationDate%", (new SimpleDateFormat("E, dd MMM yyyy")).format(((SimpleLayout)layout.key).creation)).replace("%downloads%", ((SimpleLayout)layout.key).downloads + "").replace("%mcVer%", ((SimpleLayout)layout.key).mcVer + "").replace("%plVer%", ((SimpleLayout)layout.key).plVer + "")));
                        new ConfirmRequest(this.plugin, ChatColor.RED + "Are you sure you want to continue? This will override the layout and update it with the current installed shop layout.\n" + ChatColor.GREEN + "Enter 'yes' or 'no' in chat.", p, (input) -> {
                           this.plugin.runKillableTask(() -> {
                              if (input) {
                                 this.startUpload(p, files, id, (String)layout.value, msg, MarketplaceIntegration.UploadReason.UPDATE);
                              } else {
                                 SendMessage.chatToPlayer(p, ChatColor.RED + "Cancelled update process");
                              }

                           });
                        });
                     }

                  });
               });
            });
         }
      });
   }

   public void downloadLayout(Player p, String id, boolean unsafe) {
      SendMessage.chatToPlayer(p, ChatColor.GREEN + "Started install process, retrieving layout from server...");
      this.plugin.runKillableTask(() -> {
         SimplePair layout;
         try {
            layout = this.getLayout(id, "/getLayout");
         } catch (LayoutNotFoundException var6) {
            SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to find a layout with ID " + id + ", make sure the ID is spelled correct and the layout still exists. Keep in mind that temporary layouts are removed after 14 days.");
            return;
         } catch (NullPointerException var7) {
            SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to fetch layout from server at this time, please try again soon or see console for more details.");
            SendMessage.warnMessage(var7.getMessage());
            var7.printStackTrace();
            return;
         } catch (Exception var8) {
            SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to fetch layout from server, see console for more details.");
            var8.printStackTrace();
            return;
         }

         SendMessage.chatToPlayer(p, ChatColor.translateAlternateColorCodes('&', "&aYou are about to install layout &c%layoutID% &awith data: \n&6Title: &e%title% \n&6Description: &e%description% \n&6Tags: \n&8- &e%tags% \n&6Compatible MC version(s): &e%mcVer% \n&6Created for plugin version: &e%plVer% \n&6Created at: &e%creationDate% \n&6Downloads: &e%downloads% \n&6Shop files: \n&8- &e%files%".replace("%layoutID%", id).replace("%title%", ((SimpleLayout)layout.key).title).replace("%description%", ((SimpleLayout)layout.key).description).replace("%tags%", String.join("\n&8- &e", ((SimpleLayout)layout.key).tags)).replace("%files%", String.join("\n&8- &e", ((SimpleLayout)layout.key).files)).replace("%creationDate%", (new SimpleDateFormat("E, dd MMM yyyy")).format(((SimpleLayout)layout.key).creation)).replace("%downloads%", ((SimpleLayout)layout.key).downloads + "").replace("%mcVer%", ((SimpleLayout)layout.key).mcVer + "").replace("%plVer%", ((SimpleLayout)layout.key).plVer + "")));
         new ConfirmRequest(this.plugin, ChatColor.RED + "Are you sure you want to continue? This will override the current shop layout.\n" + ChatColor.GREEN + "Enter 'yes' or 'no' in chat to continue.", p, (input) -> {
            this.plugin.runKillableTask(() -> {
               if (input) {
                  File file = this.createLayout(id);
                  Runnable onDownload = () -> {
                     if (!file.exists()) {
                        SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to download layout from server, see console for more details.");
                     } else {
                        this.installLayout(p, (SimpleLayout)layout.key, file, unsafe);
                     }
                  };
                  if (((SimpleLayout)layout.key).requiresAuthorization) {
                     p.sendTitle("§c§lValidation required", "§6§lClick the link in chat to authorize", 1, 80, 1);
                     TextComponent message = new TextComponent(TextComponent.fromLegacyText(ChatColor.RED + "Validation is required to continue, click here to validate via discord."));
                     message.setClickEvent(new ClickEvent(Action.OPEN_URL, this.getAuthorizeURL((String)layout.value)));
                     SendMessage.chatToPlayer(p, message);
                     this.checkVerification(p, id, 6000L, () -> {
                        SendMessage.chatToPlayer(p, ChatColor.GREEN + "Verification success, downloading layout...");
                        this.downloadFile(id, (String)layout.value, file, onDownload);
                     });
                  } else {
                     SendMessage.chatToPlayer(p, ChatColor.GREEN + "Downloading layout...");
                     this.downloadFile(id, (String)layout.value, file, onDownload);
                  }
               } else {
                  SendMessage.chatToPlayer(p, ChatColor.RED + "Cancelled install process");
               }

            });
         });
      });
   }

   private void installLayout(Player p, SimpleLayout layout, File file, boolean unsafe) {
      SendMessage.chatToPlayer(p, ChatColor.GREEN + "Successfully downloaded layout, extracting files...");
      this.backupCurrentLayout(p, layout.files.contains("config.yml") && unsafe);
      boolean success = true;

      try {
         boolean requiresRestart = false;
         ZipInputStream zis = new ZipInputStream(new FileInputStream(file));

         for(ZipEntry zipEntry = zis.getNextEntry(); zipEntry != null; zipEntry = zis.getNextEntry()) {
            String fileName = zipEntry.getName();
            if (fileName.equals("config.yml")) {
               if (!unsafe) {
                  fileName = "temp-" + fileName;
               }

               requiresRestart = true;
            }

            File f = new File(this.plugin.getDataFolder(), fileName.replace("/", File.separator));
            FileOutputStream fos = new FileOutputStream(f);
            byte[] buffer = new byte[1024];

            int len;
            while((len = zis.read(buffer)) > 0) {
               fos.write(buffer, 0, len);
            }

            fos.close();
         }

         zis.closeEntry();
         zis.close();
         if (layout.files.contains("config.yml") && !unsafe) {
            try {
               File tempFile = new File(this.plugin.getDataFolder(), "temp-config.yml");
               Config tempConf = new Config(tempFile);
               Iterator var24 = tempConf.getKeys(false).iterator();

               label112:
               while(true) {
                  while(true) {
                     String key;
                     do {
                        if (!var24.hasNext()) {
                           ConfigManager.saveConfig();
                           Files.delete(tempFile.toPath());
                           break label112;
                        }

                        key = (String)var24.next();
                     } while(bannedOptions.contains(key) && ConfigManager.getConfig().contains(key));

                     if (listOptions.contains(key) && ConfigManager.getConfig().contains(key)) {
                        this.addValues(ConfigManager.getConfig(), tempConf, key);
                     } else {
                        ConfigManager.getConfig().set(key, tempConf.get(key));
                     }
                  }
               }
            } catch (ConfigLoadException var17) {
               SendMessage.chatToPlayer(p, ChatColor.RED + "Invalid config.yml format for downloaded layout, try to download the layout again or report this layout as broken.");
               SendMessage.warnMessage("A error occurred while reading the config.yml of downloaded shop layout at " + file.getAbsolutePath());
               var17.printStackTrace();
               success = false;
            } catch (IOException var18) {
               SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to remove temp config from plugin folder, it may be removed manually.");
               SendMessage.warnMessage("A error occurred while trying to remove temp config at " + file.getAbsolutePath());
               var18.printStackTrace();
               success = false;
            }
         }

         UpdateChecker.Version specialVersion = new UpdateChecker.Version("6.10.3");
         if (!unsafe && (new UpdateChecker.Version(this.plugin.getDescription().getVersion())).isGreater(specialVersion) && (new UpdateChecker.Version(layout.plVer)).isSmaller(specialVersion)) {
            SendMessage.logDebugMessage("Updating section files from downloaded layout...");
            if (!layout.files.contains("config.yml")) {
               Iterator var23 = layout.files.iterator();

               while(var23.hasNext()) {
                  String section = (String)var23.next();
                  if (!section.equals("config.yml")) {
                     try {
                        File f = new File(this.plugin.getDataFolder(), "sections" + File.separator + section);
                        Config c = new Config(f);
                        String slots = c.getString("slot");
                        if (slots != null) {
                           ArrayList<Integer> slot = this.plugin.calculateAmount.getSlots(slots);
                           if (slot.size() == 1) {
                              c.set("slot", (Integer)slot.get(0) + 2);
                           } else {
                              c.set("slot", slot.stream().map((i) -> {
                                 return String.valueOf(i + 2);
                              }).collect(Collectors.joining(",")));
                           }
                        }

                        c.save();
                     } catch (ConfigSaveException | ConfigLoadException var16) {
                        SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to update section config '" + section + "', download this layout again or report this layout as broken.");
                        SendMessage.warnMessage("A error occurred while reading the " + section + " config of downloaded shop layout at " + file.getAbsolutePath());
                        var16.printStackTrace();
                        success = false;
                     }
                  }
               }
            } else if (!this.plugin.getConfigManager().reload()) {
               SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to reload configs, download this layout again or report this layout as broken.");
               SendMessage.warnMessage("A error occurred while reloading the configuration files of downloaded shop layout at " + file.getAbsolutePath());
               success = false;
            } else {
               ConfigUtil.updateConfigs();
            }
         }

         if (success) {
            if (requiresRestart) {
               SendMessage.chatToPlayer(p, ChatColor.GREEN + "Successfully installed shop layout, a server restart is recommended to load all changes.");
            } else {
               SendMessage.chatToPlayer(p, ChatColor.GREEN + "Successfully installed shop layout, use command /sreload to reload all changes.");
            }
         }

         token = (new SecureRandom()).nextLong();
      } catch (IOException var19) {
         SendMessage.chatToPlayer(p, ChatColor.RED + "A unknown error occurred while extracting the downloaded file, see console for details.");
         SendMessage.warnMessage("A unknown error occurred while extracting shop layout from " + file.getAbsolutePath());
         var19.printStackTrace();
      }

   }

   private void addValues(Config original, Config temp, String key) {
      Object list = original.get(key);
      Object tempList = temp.get(key);
      Iterator var6;
      if (list instanceof List && tempList instanceof List) {
         var6 = ((List)tempList).iterator();

         while(var6.hasNext()) {
            Object val = var6.next();
            if (!((List)list).contains(val)) {
               ((List)list).add(val);
            }
         }

         original.set(key, list);
      } else if (list instanceof ConfigurationSection && tempList instanceof ConfigurationSection) {
         var6 = ((ConfigurationSection)tempList).getKeys(false).iterator();

         while(var6.hasNext()) {
            String key2 = (String)var6.next();
            ((ConfigurationSection)list).set(key2, ((ConfigurationSection)tempList).get(key2));
         }

         original.set(key, list);
      }

   }

   private void backupCurrentLayout(Player p, boolean config) {
      ArrayList<String> files = new ArrayList(Arrays.asList("config.yml"));
      File shopFolder = new File(this.plugin.getDataFolder() + File.separator + "shops");
      if (shopFolder.exists() && shopFolder.isDirectory()) {
         Arrays.stream(shopFolder.listFiles()).forEach((f) -> {
            files.add("shops/" + f.getName());
         });
      }

      File sectionsFolder = new File(this.plugin.getDataFolder() + File.separator + "sections");
      if (sectionsFolder.exists() && sectionsFolder.isDirectory()) {
         Arrays.stream(sectionsFolder.listFiles()).forEach((f) -> {
            files.add("sections/" + f.getName());
         });
      }

      try {
         Path cDir = this.createZipFile();
         SendMessage.chatToPlayer(p, ChatColor.GREEN + "Saving backup from current layout as layouts/" + cDir.getFileName());
         FileOutputStream outputStream = new FileOutputStream(cDir.toString());
         ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
         this.zipLayout(files, zipOutputStream);
         zipOutputStream.close();
         outputStream.close();
      } catch (IOException var9) {
         SendMessage.chatToPlayer(p, ChatColor.RED + "Failed to create a backup of the current shop layout, see console for details.");
         var9.printStackTrace();
      }

      if (!config) {
         files.remove("config.yml");
         files.add("temp-config.yml");
      }

      files.forEach((s) -> {
         File f = new File(this.plugin.getDataFolder(), s.replace("/", File.separator));

         try {
            Files.delete(f.toPath());
         } catch (IOException var4) {
         }

      });
   }

   private File createLayout(String layoutID) {
      String path = this.plugin.getDataFolder() + File.separator + "layouts" + File.separator + "downloads" + File.separator + layoutID;
      File folder = (new File(path)).getParentFile();
      if (!folder.exists() || folder.isFile()) {
         folder.mkdirs();
      }

      int i;
      for(i = 0; (new File(path + (i > 0 ? "(" + i + ")" : ""))).exists(); ++i) {
      }

      return Paths.get(path + (i > 0 ? "(" + i + ")" : "") + ".zip").toFile();
   }

   private String sendRequest(String url, String body) throws Exception {
      HttpURLConnection request = (HttpURLConnection)(new URL(url)).openConnection();
      request.setConnectTimeout(5000);
      request.setDoOutput(true);
      request.setRequestProperty("token", String.valueOf(token));
      request.setRequestProperty("Content-Type", "application/json");
      OutputStream os = request.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
      osw.write(body);
      osw.flush();
      osw.close();
      os.close();
      request.connect();
      return (new BufferedReader(new InputStreamReader(request.getInputStream()))).readLine();
   }

   private SimplePair<SimpleLayout, String> getLayout(String layoutID, String endpoint) throws Exception {
      HttpURLConnection request = (HttpURLConnection)(new URL(s + endpoint + "?layout=" + layoutID)).openConnection();
      request.setConnectTimeout(5000);
      request.setRequestProperty("token", String.valueOf(token));
      request.connect();
      String response = (new BufferedReader(new InputStreamReader(request.getInputStream()))).readLine();
      switch(request.getResponseCode()) {
      case 200:
         SimpleLayout layout = new SimpleLayout(response);
         return new SimplePair(layout, request.getHeaderField("token"));
      case 400:
         throw new LayoutNotFoundException();
      default:
         throw new NullPointerException("Server responded with " + request.getResponseCode() + ": " + response);
      }
   }

   private String getAuthorizeURL(String layout) {
      return s + "/authorize?token=" + token + "&layout=" + layout;
   }

   private void checkVerification(Player p, String layoutID, long timeOut, Runnable onComplete) {
      long ticks = timeOut - 100L;
      this.plugin.runKillableTaskLater(() -> {
         try {
            if (((HttpURLConnection)(new URL(s + "/check?token=" + token + "&layout=" + layoutID)).openConnection()).getResponseCode() == 202) {
               onComplete.run();
            } else if (ticks != 0L) {
               this.checkVerification(p, layoutID, ticks, onComplete);
            } else {
               SendMessage.chatToPlayer(p, ChatColor.RED + "Validation process expired");
            }
         } catch (IOException var7) {
            var7.printStackTrace();
         }

      }, 100L);
   }

   private void checkStatus(Player p, String layoutID, long timeOut, Consumer<MarketplaceIntegration.UploadResponse> onComplete) {
      long ticks = timeOut -= 100L;
      this.plugin.runKillableTaskLater(() -> {
         HttpURLConnection conn = null;

         try {
            conn = (HttpURLConnection)(new URL(s + "/getStatus?token=" + token + "&layout=" + layoutID)).openConnection();
            int status = conn.getResponseCode();
            if (status == 200) {
               onComplete.accept(new MarketplaceIntegration.UploadResponse(true));
            } else if (status == 422) {
               JsonObject obj = (JsonObject)(new Gson()).fromJson((new BufferedReader(new InputStreamReader(conn.getErrorStream()))).readLine(), JsonObject.class);
               onComplete.accept(new MarketplaceIntegration.UploadResponse(false, obj.get("reason").getAsString()));
            } else if (ticks != 0L) {
               this.checkStatus(p, layoutID, ticks, onComplete);
            } else {
               SendMessage.chatToPlayer(p, ChatColor.RED + "A timeout occurred while uploading layout " + layoutID + ", please try again soon.");
            }
         } catch (IOException var12) {
            var12.printStackTrace();
         } finally {
            conn.disconnect();
         }

      }, 100L);
   }

   private Path createZipFile() throws IOException {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy");
      LocalDateTime now = LocalDateTime.now();
      String path = this.plugin.getDataFolder() + File.separator + "layouts" + File.separator + "layout - " + dtf.format(now);

      int i;
      for(i = 0; (new File(path + (i > 0 ? "(" + i + ")" : ""))).exists(); ++i) {
      }

      return Paths.get(path + (i > 0 ? "(" + i + ")" : "") + ".zip");
   }

   private void zipLayout(ArrayList<String> files, ZipOutputStream zipOutputStream) throws IOException {
      Iterator var3 = files.iterator();

      while(var3.hasNext()) {
         String file = (String)var3.next();
         (new StringBuilder()).append(this.plugin.getDataFolder().getPath()).append(File.separator).append(file).toString();
         FileInputStream fis = new FileInputStream(new File(this.plugin.getDataFolder(), file.replace("/", File.separator)));
         zipOutputStream.putNextEntry(new ZipEntry(file));
         byte[] bytes = new byte[1024];

         int length;
         while((length = fis.read(bytes)) >= 0) {
            zipOutputStream.write(bytes, 0, length);
         }

         fis.close();
      }

   }

   private void zipFiles(ZipOutputStream zipOutputStream, String dir) throws IOException {
      File directory = new File(this.plugin.getDataFolder() + File.separator + dir);
      if (directory.exists() && directory.isDirectory()) {
         File[] var4 = directory.listFiles();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            File file = var4[var6];
            if (!file.isDirectory()) {
               String path = file.getPath();
               FileInputStream fis = new FileInputStream(path);
               zipOutputStream.putNextEntry(new ZipEntry(dir + "/" + file.toPath().getFileName().toString()));
               byte[] bytes = new byte[1024];

               int length;
               while((length = fis.read(bytes)) >= 0) {
                  zipOutputStream.write(bytes, 0, length);
               }

               fis.close();
            }
         }
      }

   }

   private int upload(Path path, String id) throws IOException {
      URL url = new URL(s + "/uploadLayout?layout=" + id);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("token", String.valueOf(token));
      connection.setRequestProperty("Content-Type", "application/zip");
      OutputStream output = connection.getOutputStream();

      try {
         FileInputStream fileInputStream = new FileInputStream(new File(path.toUri()));

         try {
            byte[] buffer = new byte[4096];

            while(true) {
               int bytesRead;
               if ((bytesRead = fileInputStream.read(buffer)) == -1) {
                  output.flush();
                  break;
               }

               output.write(buffer, 0, bytesRead);
            }
         } catch (Throwable var11) {
            try {
               fileInputStream.close();
            } catch (Throwable var10) {
               var11.addSuppressed(var10);
            }

            throw var11;
         }

         fileInputStream.close();
      } catch (Throwable var12) {
         if (output != null) {
            try {
               output.close();
            } catch (Throwable var9) {
               var12.addSuppressed(var9);
            }
         }

         throw var12;
      }

      if (output != null) {
         output.close();
      }

      int response = connection.getResponseCode();
      connection.disconnect();
      return response;
   }

   private int update(Path path, String id, String token, String msg) throws IOException {
      URL url = new URL(s + "/updateLayout?layout=" + id + "&token=" + MarketplaceIntegration.token);
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("POST");
      connection.setRequestProperty("token", token);
      connection.setRequestProperty("msg", msg);
      connection.setRequestProperty("Content-Type", "application/zip");
      OutputStream output = connection.getOutputStream();

      try {
         FileInputStream fileInputStream = new FileInputStream(new File(path.toUri()));

         try {
            byte[] buffer = new byte[4096];

            while(true) {
               int bytesRead;
               if ((bytesRead = fileInputStream.read(buffer)) == -1) {
                  output.flush();
                  break;
               }

               output.write(buffer, 0, bytesRead);
            }
         } catch (Throwable var13) {
            try {
               fileInputStream.close();
            } catch (Throwable var12) {
               var13.addSuppressed(var12);
            }

            throw var13;
         }

         fileInputStream.close();
      } catch (Throwable var14) {
         if (output != null) {
            try {
               output.close();
            } catch (Throwable var11) {
               var14.addSuppressed(var11);
            }
         }

         throw var14;
      }

      if (output != null) {
         output.close();
      }

      int response = connection.getResponseCode();
      connection.disconnect();
      return response;
   }

   private void downloadFile(String id, String s, File output, Runnable onComplete) {
      try {
         HttpURLConnection request = (HttpURLConnection)(new URL(MarketplaceIntegration.s + "/installLayout?layout=" + id + "&token=" + token)).openConnection();
         request.setConnectTimeout(5000);
         request.setRequestProperty("token", s);
         request.connect();
         BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
         FileOutputStream fis = new FileOutputStream(output);
         byte[] buffer = new byte[1024];

         int count;
         while((count = bis.read(buffer, 0, 1024)) != -1) {
            fis.write(buffer, 0, count);
         }

         fis.close();
         bis.close();
         onComplete.run();
      } catch (IOException var10) {
         SendMessage.warnMessage("Failed to download layout from server");
         var10.printStackTrace();
      }

   }

   private void disableCache(HttpURLConnection connection) {
      connection.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
      connection.setRequestProperty("Pragma", "no-cache");
      connection.setRequestProperty("Expires", "0");
   }

   static {
      data = "%%__POLYMART__%%".equals("1") ? "p/?download_token=%%__VERIFY_TOKEN__%%&user_id=%%__USER__%%&resource_id=%%__RESOURCE__%%&nonce=%%__NONCE__%%&inject_version=%%__INJECT_VER__%%&download_agent=%%__AGENT__%%&download_time=%%__TIMESTAMP__%%&key=" + token : ("%%__SONGODA__%%".equals("true") ? "s/?resource=%%__RESOURCE__%%&user_id=%%__USER__%%&version=%%__VERSION__%%&nonce=%%__NONCE__%%&file_hash=%%__FILEHASH__%%&node=%%__SONGODA_NODE__%%&download_time=%%__TIMESTAMP__%%&key=" + token : "i/?resource=%%__RESOURCE__%%&user_id=%%__USER__%%&nonce=%%__NONCE__%%&key=" + token);
   }

   private static enum UploadReason {
      CREATE,
      UPDATE;

      // $FF: synthetic method
      private static MarketplaceIntegration.UploadReason[] $values() {
         return new MarketplaceIntegration.UploadReason[]{CREATE, UPDATE};
      }
   }

   private class UploadResponse {
      final boolean success;
      final String reason;

      public UploadResponse(boolean param2) {
         this.success = success;
         this.reason = "";
      }

      public UploadResponse(boolean param2, String param3) {
         this.success = success;
         this.reason = reason;
      }
   }
}
