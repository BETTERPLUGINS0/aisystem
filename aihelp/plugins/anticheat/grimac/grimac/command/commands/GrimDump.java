package ac.grim.grimac.command.commands;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.command.BuildableCommand;
import ac.grim.grimac.platform.api.PlatformPlugin;
import ac.grim.grimac.platform.api.manager.cloud.CloudCommandAdapter;
import ac.grim.grimac.platform.api.sender.Sender;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.incendo.cloud.CommandManager;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.description.Description;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.common.PropertiesUtil;
import ac.grim.grimac.utils.reflection.ReflectionUtils;
import ac.grim.grimac.utils.viaversion.ViaVersionUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.Properties;
import java.util.Map.Entry;

public class GrimDump implements BuildableCommand {
   private static final boolean PAPER = ReflectionUtils.hasClass("com.destroystokyo.paper.PaperConfig") || ReflectionUtils.hasClass("io.papermc.paper.configuration.Configuration");
   private final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
   private String link = null;

   public void register(CommandManager<Sender> commandManager, CloudCommandAdapter adapter) {
      commandManager.command(commandManager.commandBuilder("grim", new String[]{"grimac"}).literal("dump", Description.of("Generate a debug dump")).permission("grim.dump").handler(this::handleDump));
   }

   private void handleDump(@NotNull CommandContext<Sender> context) {
      Sender sender = (Sender)context.sender();
      if (this.link != null) {
         sender.sendMessage(MessageUtil.miniMessage(GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("upload-log", "%prefix% &fUploaded debug to: %url%").replace("%url%", this.link)));
      } else {
         GrimLog.sendLogAsync(sender, this.generateDump(), (string) -> {
            this.link = string;
         }, "text/yaml");
      }
   }

   public static JsonObject getDumpInfo() {
      JsonObject base = new JsonObject();
      base.addProperty("type", "dump");
      base.addProperty("timestamp", System.currentTimeMillis());
      JsonObject versions = new JsonObject();
      base.add("versions", versions);
      versions.addProperty("grim", GrimAPI.INSTANCE.getExternalAPI().getGrimVersion());
      versions.addProperty("packetevents", PacketEvents.getAPI().getVersion().toString());
      versions.addProperty("server", PacketEvents.getAPI().getServerManager().getVersion().getReleaseName());
      versions.addProperty("implementation", GrimAPI.INSTANCE.getPlatformServer().getPlatformImplementationString());
      JsonObject states = new JsonObject();
      base.add("states", states);
      if (GrimAPI.INSTANCE.isInitialized()) {
         states.addProperty("platform", GrimAPI.INSTANCE.getPlatform().toString());
      }

      if (ViaVersionUtil.isAvailable) {
         states.addProperty("has_viaversion", true);
      }

      if (PAPER) {
         states.addProperty("has_paper", true);
      }

      JsonObject settings = new JsonObject();
      if (GrimAPI.INSTANCE.getAlertManager().hasConsoleVerboseEnabled()) {
         settings.addProperty("console_verbose", true);
      }

      if (!GrimAPI.INSTANCE.getAlertManager().hasConsoleAlertsEnabled()) {
         settings.addProperty("console_alerts", false);
      }

      if (settings.size() > 0) {
         states.add("settings", settings);
      }

      JsonObject system = new JsonObject();
      base.add("system", system);
      system.addProperty("os_name", System.getProperty("os.name"));
      system.addProperty("java_version", System.getProperty("java.version"));
      system.addProperty("user_language", System.getProperty("user.language"));
      base.add("build", getBuildInfo());
      JsonArray plugins = new JsonArray();
      base.add("plugins", plugins);
      PlatformPlugin[] var6 = GrimAPI.INSTANCE.getPluginManager().getPlugins();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         PlatformPlugin plugin = var6[var8];
         JsonObject pluginJson = new JsonObject();
         pluginJson.addProperty("enabled", plugin.isEnabled());
         pluginJson.addProperty("name", plugin.getName());
         pluginJson.addProperty("version", plugin.getVersion());
         plugins.add(pluginJson);
      }

      return base;
   }

   private static JsonObject getBuildInfo() {
      JsonObject object = new JsonObject();

      try {
         Properties properties = PropertiesUtil.readProperties(GrimAPI.INSTANCE.getClass(), "grimac.properties");
         Iterator var2 = properties.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<Object, Object> entry = (Entry)var2.next();
            object.addProperty(entry.getKey().toString(), entry.getValue().toString());
         }
      } catch (Exception var4) {
      }

      return object;
   }

   private String generateDump() {
      JsonObject base = getDumpInfo();
      return this.gson.toJson(base);
   }
}
