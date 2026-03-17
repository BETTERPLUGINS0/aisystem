/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Verify
implements Listener {
    private static final String polymartEndpointURL = "https://api.polymart.org/v1/verifyPurchase/?inject_version=%d&resource_id=%d&user_id=%d&nonce=%d&download_agent=%d&download_time=%d&download_token=%s";
    private final JavaPlugin plugin;
    private final int resourceID;
    private final int user_id;
    private final String nonce;
    private final int download_time;
    private final String download_token;
    private int injector_version = 0;
    private int download_agent = 0;
    private String invalidReason = null;
    private static Boolean valid = null;

    public Verify(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
        this.resourceID = Integer.parseInt("70523");
        this.user_id = Integer.parseInt("383518");
        this.nonce = "508520782";
        this.download_time = Integer.parseInt("%%__TIMESTAMP__%%");
        this.download_token = "%%__VERIFY_TOKEN__%%";
        this.injector_version = Integer.parseInt("%%__INJECT_VER__%%");
        this.download_agent = Integer.parseInt("%%__AGENT__%%");
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)javaPlugin);
        this.validate();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        if (this.invalidReason == null) {
            return;
        }
        Player player = playerJoinEvent.getPlayer();
        if (player.isOp() || player.hasPermission("vp.admin")) {
            Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> player.sendMessage(String.valueOf(ChatColor.GOLD) + "[" + String.valueOf(ChatColor.RED) + this.plugin.getName() + String.valueOf(ChatColor.GOLD) + "] " + String.valueOf(ChatColor.RED) + "The license is incorrect! Reason: " + String.valueOf(ChatColor.GOLD) + this.invalidReason), 100L);
        }
    }

    private void validate() {
        try {
            String string;
            URL uRL = new URL(String.format(polymartEndpointURL, this.injector_version, this.resourceID, this.user_id, Integer.parseInt(this.nonce), this.download_agent, this.download_time, this.download_token));
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "SBDVerify/2.0");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            int n = httpURLConnection.getResponseCode();
            if (n != 200) {
                this.disable("Could not send the validating request.");
                return;
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            while ((string = bufferedReader.readLine()) != null) {
                stringBuilder.append(string);
            }
            bufferedReader.close();
            JsonObject jsonObject = new JsonParser().parse(stringBuilder.toString()).getAsJsonObject();
            if (jsonObject == null) {
                this.disable("Could not send the validating request.");
                return;
            }
            if (!jsonObject.has("response")) {
                this.disable("An invalid response was returned by the validating request.");
                return;
            }
            JsonObject jsonObject2 = jsonObject.get("response").getAsJsonObject();
            if (!jsonObject2.has("success")) {
                this.disable("An invalid response was returned by the validating request.");
                return;
            }
            if (!jsonObject2.get("success").getAsBoolean()) {
                this.disable("The purchase of this plugin could not be verified.");
            }
        } catch (IOException iOException) {
            this.disable("Could not send the validating request.");
        }
    }

    private void disable(String string) {
        this.invalidReason = string;
        Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
            valid = false;
            this.plugin.getLogger().severe("Stopping plugin because licensing system check failed.");
            this.plugin.getLogger().severe("Reason: " + string);
            this.plugin.getLogger().severe("Contact the developer if you believe something is wrong on their side.");
            Bukkit.getPluginManager().disablePlugin((Plugin)this.plugin);
        });
    }

    public Boolean isValidated() {
        return this.invalidReason == null;
    }

    public static Boolean isValid() {
        return valid;
    }
}

