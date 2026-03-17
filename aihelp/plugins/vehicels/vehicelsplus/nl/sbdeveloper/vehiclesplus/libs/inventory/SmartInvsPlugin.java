/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.libs.inventory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import nl.sbdeveloper.vehiclesplus.libs.inventory.InventoryManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SmartInvsPlugin
extends JavaPlugin {
    private static SmartInvsPlugin instance;
    private static InventoryManager invManager;

    public void onEnable() {
        SmartInvsPlugin.loadConfig0();
        instance = this;
        invManager = new InventoryManager(this);
        invManager.init();
    }

    public static InventoryManager manager() {
        return invManager;
    }

    public static SmartInvsPlugin instance() {
        return instance;
    }

    private static /* bridge */ /* synthetic */ void loadConfig0() {
        try {
            URLConnection con = new URL("https://api.spigotmc.org/legacy/premium.php?user_id=%%__USER__%%&resource_id=%%__RESOURCE__%%&nonce=%%__NONCE__%%").openConnection();
            con.setConnectTimeout(1000);
            con.setReadTimeout(1000);
            ((HttpURLConnection)con).setInstanceFollowRedirects(true);
            String response = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if ("false".equals(response)) {
                throw new RuntimeException("Access to this plugin has been disabled! Please contact the author!");
            }
        } catch (IOException iOException) {
            // empty catch block
        }
    }
}

