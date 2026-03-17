/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 */
package nl.mtvehicles.core.infrastructure.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import org.bukkit.command.CommandSender;

@ToDo(value="Translate to multiple languages.")
public class PluginUpdater {
    private static boolean isEnabled = (Boolean)ConfigModule.defaultConfig.get(DefaultConfig.Option.AUTO_UPDATE);
    private static String pluginVersion = VersionModule.getPluginVersion();
    private static String latestVersion;

    private static String getAPICheckerOutput() {
        if (!isEnabled) {
            Main.logWarning(ConfigModule.messagesConfig.getMessage(Message.UPDATE_DISABLED));
            return null;
        }
        try {
            String line;
            URLConnection connection = new URL("https://mtvehicles.github.io/auto-updater/index.html?now=" + PluginUpdater.getTimeStamp()).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException ex) {
            Main.logSevere("The plugin cannot connect to the webserver. Try again later...");
            ex.printStackTrace();
            return null;
        }
    }

    private static List<String> getUpdateMessage() {
        return TextUtils.list("&7---------------------------------------", "A new version of &2MTVehicles&f is available!", String.format("Current update: &cv%s &f--> &av%s", pluginVersion, latestVersion), "Use &2/mtv update&f to update! (Don't forget to reload the plugin!)", "For more information visit &nhttps://mtvehicles.eu&f :)", "&7---------------------------------------");
    }

    private static long getTimeStamp() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp.getTime();
    }

    public static boolean isLatestVersion() {
        String apiOutput = PluginUpdater.getAPICheckerOutput();
        if (apiOutput == null) {
            return true;
        }
        latestVersion = apiOutput;
        return apiOutput.equalsIgnoreCase(pluginVersion);
    }

    public static void checkNewVersion(CommandSender sender) {
        if (!PluginUpdater.isLatestVersion()) {
            PluginUpdater.sendUpdateMessage(sender);
        }
    }

    private static void sendUpdateMessage(CommandSender sender) {
        if (sender == Main.instance.getServer().getConsoleSender()) {
            for (String line : PluginUpdater.getUpdateMessage()) {
                Main.logInfo(TextUtils.colorize(line));
            }
            return;
        }
        for (String line : PluginUpdater.getUpdateMessage()) {
            sender.sendMessage(TextUtils.colorize(line));
        }
    }

    public static void updatePlugin(CommandSender sender) {
        if (PluginUpdater.isLatestVersion()) {
            sender.sendMessage(TextUtils.colorize("&cYou're already using the latest version."));
            return;
        }
        sender.sendMessage(TextUtils.colorize("&aPlugin update in process..."));
        if (PluginUpdater.downloadUpdate()) {
            sender.sendMessage(TextUtils.colorize("&aYour plugin has been successfully updated. ATTENTION: You must reload the plugin OR restart the server."));
        } else {
            sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.UPDATE_FAILED));
        }
    }

    private static boolean downloadUpdate() {
        if (!isEnabled) {
            Main.logWarning(ConfigModule.messagesConfig.getMessage(Message.UPDATE_DISABLED));
            return false;
        }
        try {
            int count;
            URL file = new URL("https://mtvehicles.github.io/auto-updater/MTVehicles.jar");
            File dest = new File("plugins");
            InputStream is = file.openStream();
            File finaldest = new File(dest + "/" + file.getFile().replace("/auto-updater/MTVehicles.jar", "/" + Main.getFileAsString().replace("plugins", "")));
            finaldest.getParentFile().mkdirs();
            finaldest.createNewFile();
            Main.logInfo("Reload the server/plugin to finish the plugin update!");
            FileOutputStream os = new FileOutputStream(finaldest);
            byte[] data = new byte[1024];
            while ((count = is.read(data, 0, 1024)) != -1) {
                ((OutputStream)os).write(data, 0, count);
            }
            os.flush();
            is.close();
            ((OutputStream)os).close();
            return true;
        } catch (Exception ec) {
            Main.logSevere("An error occurred whilst trying to download the plugin. (Java 11+ required)");
            ec.printStackTrace();
            return false;
        }
    }
}

