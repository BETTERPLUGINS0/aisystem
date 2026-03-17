/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.bukkit.configuration.file.YamlConfiguration
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTItem;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;
import org.bukkit.configuration.file.YamlConfiguration;

public class VersionChecker {
    private static final String USER_AGENT = "nbt-api Version check";
    private static final String REQUEST_URL = "https://api.spiget.org/v2/resources/7939/versions?size=100";
    public static boolean hideOk = false;

    protected static void checkForUpdates() {
        URL uRL = new URL(REQUEST_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
        httpURLConnection.addRequestProperty("User-Agent", USER_AGENT);
        InputStream inputStream = httpURLConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        JsonElement jsonElement = new JsonParser().parse((Reader)inputStreamReader);
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = (JsonArray)jsonElement;
            JsonObject jsonObject = (JsonObject)jsonArray.get(jsonArray.size() - 1);
            int n = VersionChecker.getVersionDifference(jsonObject.get("name").getAsString());
            if (n == -1) {
                MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] The NBT-API in '" + VersionChecker.getPlugin() + "' seems to be outdated!");
                MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Current Version: '2.15.5' Newest Version: " + jsonObject.get("name").getAsString() + "'");
                MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Please update the NBTAPI or the plugin that contains the api(nag the mod author when the newest release has an old version, not the NBTAPI dev)!");
            } else if (n == 0) {
                if (!hideOk) {
                    MinecraftVersion.getLogger().log(Level.INFO, "[NBTAPI] The NBT-API seems to be up-to-date!");
                }
            } else if (n == 1) {
                MinecraftVersion.getLogger().log(Level.INFO, "[NBTAPI] The NBT-API in '" + VersionChecker.getPlugin() + "' seems to be a future Version, not yet released on Spigot/CurseForge! This is not an error!");
                MinecraftVersion.getLogger().log(Level.INFO, "[NBTAPI] Current Version: '2.15.5' Newest Version: " + jsonObject.get("name").getAsString() + "'");
            }
        } else {
            MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error when looking for Updates! Got non Json Array: '" + jsonElement.toString() + "'");
        }
    }

    private static int getVersionDifference(String string) {
        int n;
        String string2 = "2.15.5";
        if (string2.equals(string)) {
            return 0;
        }
        String string3 = "\\.";
        if (string2.split(string3).length != 3 || string.split(string3).length != 3) {
            return -1;
        }
        int n2 = Integer.parseInt(string2.split(string3)[0]);
        int n3 = Integer.parseInt(string2.split(string3)[1]);
        String string4 = string2.split(string3)[2];
        int n4 = Integer.parseInt(string.split(string3)[0]);
        int n5 = Integer.parseInt(string.split(string3)[1]);
        String string5 = string.split(string3)[2];
        if (n2 < n4) {
            return -1;
        }
        if (n2 > n4) {
            return 1;
        }
        if (n3 < n5) {
            return -1;
        }
        if (n3 > n5) {
            return 1;
        }
        int n6 = Integer.parseInt(string4.split("-")[0]);
        if (n6 < (n = Integer.parseInt(string5.split("-")[0]))) {
            return -1;
        }
        if (n6 > n) {
            return 1;
        }
        if (!string5.contains("-") && string4.contains("-")) {
            return -1;
        }
        if (string5.contains("-") && string4.contains("-")) {
            return 0;
        }
        return 1;
    }

    protected static String getPlugin() {
        block19: {
            InputStream inputStream;
            ClassLoader classLoader;
            block18: {
                classLoader = VersionChecker.class.getClassLoader();
                inputStream = classLoader.getResourceAsStream("paper-plugin.yml");
                if (inputStream != null) {
                    String string;
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    try {
                        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((Reader)inputStreamReader);
                        string = yamlConfiguration.getString("name");
                    } catch (Throwable throwable) {
                        try {
                            try {
                                inputStreamReader.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                            throw throwable;
                        } catch (IOException iOException) {
                            break block18;
                        } catch (IllegalArgumentException illegalArgumentException) {
                            MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error reading paper-plugin.yml: " + illegalArgumentException.getMessage());
                        }
                    }
                    inputStreamReader.close();
                    return string;
                }
            }
            if ((inputStream = classLoader.getResourceAsStream("plugin.yml")) != null) {
                String string;
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                try {
                    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((Reader)inputStreamReader);
                    string = yamlConfiguration.getString("name");
                } catch (Throwable throwable) {
                    try {
                        try {
                            inputStreamReader.close();
                        } catch (Throwable throwable3) {
                            throwable.addSuppressed(throwable3);
                        }
                        throw throwable;
                    } catch (IOException iOException) {
                        break block19;
                    } catch (IllegalArgumentException illegalArgumentException) {
                        MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error reading plugin.yml: " + illegalArgumentException.getMessage());
                    }
                }
                inputStreamReader.close();
                return string;
            }
        }
        return NBTItem.class.getPackage().getName();
    }

    protected static String getPluginforBStats() {
        block19: {
            InputStream inputStream;
            ClassLoader classLoader;
            block18: {
                classLoader = VersionChecker.class.getClassLoader();
                inputStream = classLoader.getResourceAsStream("paper-plugin.yml");
                if (inputStream != null) {
                    String string;
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    try {
                        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((Reader)inputStreamReader);
                        string = yamlConfiguration.getString("name");
                    } catch (Throwable throwable) {
                        try {
                            try {
                                inputStreamReader.close();
                            } catch (Throwable throwable2) {
                                throwable.addSuppressed(throwable2);
                            }
                            throw throwable;
                        } catch (IOException iOException) {
                            break block18;
                        } catch (IllegalArgumentException illegalArgumentException) {
                            MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error reading paper-plugin.yml: " + illegalArgumentException.getMessage());
                        }
                    }
                    inputStreamReader.close();
                    return string;
                }
            }
            if ((inputStream = classLoader.getResourceAsStream("plugin.yml")) != null) {
                String string;
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                try {
                    YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration((Reader)inputStreamReader);
                    string = yamlConfiguration.getString("name");
                } catch (Throwable throwable) {
                    try {
                        try {
                            inputStreamReader.close();
                        } catch (Throwable throwable3) {
                            throwable.addSuppressed(throwable3);
                        }
                        throw throwable;
                    } catch (IOException iOException) {
                        break block19;
                    } catch (IllegalArgumentException illegalArgumentException) {
                        MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error reading plugin.yml: " + illegalArgumentException.getMessage());
                    }
                }
                inputStreamReader.close();
                return string;
            }
        }
        return "UnknownPlugin";
    }

    protected static String getPluginType() {
        ClassLoader classLoader = VersionChecker.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("paper-plugin.yml");
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException iOException) {
                // empty catch block
            }
            return "PaperPlugin";
        }
        inputStream = classLoader.getResourceAsStream("plugin.yml");
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException iOException) {
                // empty catch block
            }
            return "SpigotPlugin";
        }
        return "UnknownPlugin";
    }
}

