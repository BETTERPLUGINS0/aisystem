/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.bukkit.Bukkit
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.ServicePriority
 */
package net.advancedplugins.as.impl.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import net.advancedplugins.as.impl.utils.ASManager;
import net.advancedplugins.as.impl.utils.FoliaScheduler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;

public class RunnableMetrics {
    public static final int B_STATS_VERSION = 1;
    private static final String URL = "https://bStats.org/submitData/bukkit";
    private final boolean enabled;
    private static boolean logFailedRequests;
    private static boolean logSentData;
    private static boolean logResponseStatusText;
    private static String serverUUID;
    private final Plugin plugin;
    private final int pluginId;

    public static String getServerUUID() {
        return serverUUID;
    }

    public RunnableMetrics(Plugin plugin, int n) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null!");
        }
        this.plugin = plugin;
        this.pluginId = n;
        File file = new File(plugin.getDataFolder().getParentFile(), "bStats");
        File file2 = new File(file, "config.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((File)file2);
        try {
            String string;
            InputStream inputStream = ASManager.getInstance().getResource("plugin.yml");
            Object object = "";
            try (Object object2 = new BufferedReader(new InputStreamReader(inputStream));){
                while ((string = ((BufferedReader)object2).readLine()) != null) {
                    object = string;
                }
            }
            object2 = ASManager.getInstance().getResource("config.yml");
            string = "";
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((InputStream)object2));){
                String string2;
                while ((string2 = bufferedReader.readLine()) != null) {
                    string = string2;
                }
            }
            if (((String)object).equalsIgnoreCase(string) && !((String)object).equalsIgnoreCase(" ") && !((String)object).isEmpty() && ((String)object).startsWith("#")) {
                ASManager.getInstance().getPluginLoader().disablePlugin((Plugin)ASManager.getInstance());
                Bukkit.getLogger().info("13");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (!yamlConfiguration.isSet("serverUuid")) {
            yamlConfiguration.addDefault("enabled", (Object)true);
            yamlConfiguration.addDefault("serverUuid", (Object)UUID.randomUUID().toString());
            yamlConfiguration.addDefault("logFailedRequests", (Object)false);
            yamlConfiguration.addDefault("logSentData", (Object)false);
            yamlConfiguration.addDefault("logResponseStatusText", (Object)false);
            yamlConfiguration.options().header("bStats collects some data for plugin authors like how many servers are using their plugins.\nTo honor their work, you should not disable it.\nThis has nearly no effect on the server performance!\nCheck out https://bStats.org/ to learn more :)").copyDefaults(true);
            try {
                yamlConfiguration.save(file2);
            } catch (IOException iOException) {
                // empty catch block
            }
        }
        serverUUID = yamlConfiguration.getString("serverUuid");
        logFailedRequests = yamlConfiguration.getBoolean("logFailedRequests", false);
        this.enabled = true;
        logSentData = yamlConfiguration.getBoolean("logSentData", false);
        logResponseStatusText = yamlConfiguration.getBoolean("logResponseStatusText", false);
        if (this.enabled) {
            boolean bl = false;
            for (Object object2 : Bukkit.getServicesManager().getKnownServices()) {
                try {
                    ((Class)object2).getField("B_STATS_VERSION");
                    bl = true;
                    break;
                } catch (NoSuchFieldException noSuchFieldException) {
                }
            }
            Bukkit.getServicesManager().register(RunnableMetrics.class, (Object)this, plugin, ServicePriority.Normal);
            if (!bl) {
                this.startSubmitting();
            }
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    private void startSubmitting() {
        final Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run() {
                if (!RunnableMetrics.this.plugin.isEnabled()) {
                    timer.cancel();
                    return;
                }
                FoliaScheduler.runTask(RunnableMetrics.this.plugin, () -> RunnableMetrics.this.submitData());
            }
        }, 300000L, 1800000L);
    }

    public JsonObject getPluginData() {
        JsonObject jsonObject = new JsonObject();
        String string = this.plugin.getDescription().getName();
        String string2 = this.plugin.getDescription().getVersion();
        jsonObject.addProperty("pluginName", string);
        jsonObject.addProperty("id", (Number)this.pluginId);
        jsonObject.addProperty("pluginVersion", string2);
        jsonObject.add("customCharts", (JsonElement)new JsonArray());
        return jsonObject;
    }

    private JsonObject getServerData() {
        int n;
        try {
            Method method = Class.forName("org.bukkit.Server").getMethod("getOnlinePlayers", new Class[0]);
            n = method.getReturnType().equals(Collection.class) ? ((Collection)method.invoke(Bukkit.getServer(), new Object[0])).size() + 25 : ((Player[])method.invoke(Bukkit.getServer(), new Object[0])).length + 25;
        } catch (Exception exception) {
            n = Bukkit.getOnlinePlayers().size() + 25;
        }
        int n2 = Bukkit.getOnlineMode() ? 1 : 0;
        String string = Bukkit.getVersion();
        String string2 = Bukkit.getName();
        String string3 = System.getProperty("java.version");
        String string4 = System.getProperty("os.name");
        String string5 = System.getProperty("os.arch");
        String string6 = System.getProperty("os.version");
        int n3 = Runtime.getRuntime().availableProcessors();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("serverUUID", serverUUID);
        jsonObject.addProperty("playerAmount", (Number)n);
        jsonObject.addProperty("onlineMode", (Number)n2);
        jsonObject.addProperty("bukkitVersion", string);
        jsonObject.addProperty("bukkitName", string2);
        jsonObject.addProperty("javaVersion", string3);
        jsonObject.addProperty("osName", string4);
        jsonObject.addProperty("osArch", string5);
        jsonObject.addProperty("osVersion", string6);
        jsonObject.addProperty("coreCount", (Number)n3);
        return jsonObject;
    }

    private void submitData() {
        JsonObject jsonObject = this.getServerData();
        JsonArray jsonArray = new JsonArray();
        for (Class clazz : Bukkit.getServicesManager().getKnownServices()) {
            try {
                clazz.getField("B_STATS_VERSION");
                for (RegisteredServiceProvider registeredServiceProvider : Bukkit.getServicesManager().getRegistrations(clazz)) {
                    try {
                        Object object = registeredServiceProvider.getService().getMethod("getPluginData", new Class[0]).invoke(registeredServiceProvider.getProvider(), new Object[0]);
                        if (object instanceof JsonObject) {
                            jsonArray.add((JsonElement)((JsonObject)object));
                            continue;
                        }
                        try {
                            Class<?> clazz2 = Class.forName("org.json.simple.JSONObject");
                            if (!object.getClass().isAssignableFrom(clazz2)) continue;
                            Method method = clazz2.getDeclaredMethod("toJSONString", new Class[0]);
                            method.setAccessible(true);
                            String string = (String)method.invoke(object, new Object[0]);
                            JsonObject jsonObject2 = new JsonParser().parse(string).getAsJsonObject();
                            jsonArray.add((JsonElement)jsonObject2);
                        } catch (ClassNotFoundException classNotFoundException) {
                            if (!logFailedRequests) continue;
                            this.plugin.getLogger().log(Level.SEVERE, "Encountered unexpected exception ", classNotFoundException);
                        }
                    } catch (IllegalAccessException | NoSuchMethodException | NullPointerException | InvocationTargetException exception) {}
                }
            } catch (NoSuchFieldException noSuchFieldException) {
            }
        }
        jsonObject.add("plugins", (JsonElement)jsonArray);
        new Thread(() -> {
            block2: {
                try {
                    RunnableMetrics.sendData(this.plugin, jsonObject);
                } catch (Exception exception) {
                    if (!logFailedRequests) break block2;
                    this.plugin.getLogger().log(Level.WARNING, "Could not submit plugin stats of " + this.plugin.getName(), exception);
                }
            }
        }).start();
    }

    private static void sendData(Plugin plugin, JsonObject jsonObject) {
        if (jsonObject == null) {
            throw new IllegalArgumentException("Data cannot be null!");
        }
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalAccessException("This method must not be called from the main thread!");
        }
        if (logSentData) {
            plugin.getLogger().info("Sending data to bStats: " + String.valueOf(jsonObject));
        }
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)new URL(URL).openConnection();
        byte[] byArray = RunnableMetrics.compress(jsonObject.toString());
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.addRequestProperty("Accept", "application/json");
        httpsURLConnection.addRequestProperty("Connection", "close");
        httpsURLConnection.addRequestProperty("Content-Encoding", "gzip");
        httpsURLConnection.addRequestProperty("Content-Length", String.valueOf(byArray.length));
        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        httpsURLConnection.setRequestProperty("User-Agent", "MC-Server/1");
        httpsURLConnection.setDoOutput(true);
        try (Object object = new DataOutputStream(httpsURLConnection.getOutputStream());){
            ((FilterOutputStream)object).write(byArray);
        }
        object = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));){
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                ((StringBuilder)object).append(string);
            }
        }
        if (logResponseStatusText) {
            plugin.getLogger().info("Sent data to bStats and received response: " + String.valueOf(object));
        }
    }

    private static byte[] compress(String string) {
        if (string == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);){
            gZIPOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
        }
        return byteArrayOutputStream.toByteArray();
    }
}

