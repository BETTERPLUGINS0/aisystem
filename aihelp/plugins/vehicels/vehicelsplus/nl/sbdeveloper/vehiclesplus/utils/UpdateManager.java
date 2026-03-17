/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.util.function.BiConsumer;
import javax.net.ssl.HttpsURLConnection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateManager {
    private static final String SPIGOT_API = "https://api.spigotmc.org/legacy/update.php?resource=%d";
    private static final String SPIGOT_DOWNLOAD = "https://api.spiget.org/v2/resources/%s/download";
    private static final String POLYMART_API = "https://api.polymart.org/v1/getResourceInfoSimple/?resource_id=%d&key=version";
    private static final String POLYMART_DOWNLOAD = "https://api.polymart.org/v1/requestUpdateURL/?inject_version=%d&resource_id=%d&user_id=%d&nonce=%d&download_agent=%d&download_time=%d&download_token=%s";
    private final Plugin plugin;
    private final Version currentVersion;
    private final CheckType type;
    private final int resourceID;
    private int injector_version;
    private int user_id;
    private int nonce;
    private int download_agent;
    private int download_time;
    private String download_token;
    private BiConsumer<VersionResponse, Version> versionResponse;
    private BiConsumer<DownloadResponse, String> downloadResponse;

    public UpdateManager(Plugin plugin, CheckType checkType) {
        this.plugin = plugin;
        this.currentVersion = new Version(plugin.getDescription().getVersion());
        this.type = checkType;
        this.resourceID = Integer.parseInt("70523");
        if (checkType == CheckType.POLYMART_PAID) {
            this.injector_version = Integer.parseInt("%%__INJECT_VER__%%");
            this.user_id = Integer.parseInt("383518");
            this.nonce = Integer.parseInt("-359360356");
            this.download_agent = Integer.parseInt("%%__AGENT__%%");
            this.download_time = Integer.parseInt("%%__TIMESTAMP__%%");
            this.download_token = "%%__VERIFY_TOKEN__%%";
        }
    }

    public UpdateManager handleResponse(BiConsumer<VersionResponse, Version> biConsumer) {
        this.versionResponse = biConsumer;
        return this;
    }

    public UpdateManager handleDownloadResponse(BiConsumer<DownloadResponse, String> biConsumer) {
        this.downloadResponse = biConsumer;
        return this;
    }

    public void check() {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
                String string;
                HttpsURLConnection httpsURLConnection = this.type == CheckType.POLYMART_PAID ? (HttpsURLConnection)new URL(String.format(POLYMART_API, this.resourceID)).openConnection() : (HttpsURLConnection)new URL(String.format(SPIGOT_API, this.resourceID)).openConnection();
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setRequestProperty("User-Agent", "SBDChecker/2.1");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                while ((string = bufferedReader.readLine()) != null) {
                    stringBuilder.append(string);
                }
                bufferedReader.close();
                Version version = new Version(stringBuilder.toString());
                VersionResponse versionResponse = this.currentVersion.check(version);
                Bukkit.getScheduler().runTask(this.plugin, () -> this.versionResponse.accept(versionResponse, version));
            } catch (IOException | NullPointerException exception) {
                exception.printStackTrace();
                Bukkit.getScheduler().runTask(this.plugin, () -> this.versionResponse.accept(VersionResponse.UNAVAILABLE, null));
            }
        });
    }

    public void runUpdate() {
        File file = this.getPluginFile();
        if (file == null) {
            this.downloadResponse.accept(DownloadResponse.ERROR, null);
            Bukkit.getLogger().info("Pluginfile is null");
            return;
        }
        File file2 = Bukkit.getUpdateFolderFile();
        if (!file2.exists() && !file2.mkdirs()) {
            this.downloadResponse.accept(DownloadResponse.ERROR, null);
            Bukkit.getLogger().info("Updatefolder doesn't exists, and can't be made");
            return;
        }
        File file3 = new File(file2, file.getName());
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            ReadableByteChannel readableByteChannel;
            Closeable closeable;
            Object object;
            try {
                object = this.type == CheckType.POLYMART_PAID ? (HttpsURLConnection)new URL(String.format(POLYMART_DOWNLOAD, this.injector_version, this.resourceID, this.user_id, this.nonce, this.download_agent, this.download_time, this.download_token)).openConnection() : (HttpsURLConnection)new URL(String.format(SPIGOT_DOWNLOAD, this.resourceID)).openConnection();
                ((URLConnection)object).setRequestProperty("User-Agent", "Mozilla/5.0");
                closeable = ((URLConnection)object).getInputStream();
                if (((HttpURLConnection)object).getResponseCode() != 200) {
                    String string;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((InputStream)closeable));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((string = bufferedReader.readLine()) != null) {
                        stringBuilder.append(string);
                    }
                    bufferedReader.close();
                    throw new RuntimeException("Download returned status #" + ((HttpURLConnection)object).getResponseCode(), new Throwable(stringBuilder.toString()));
                }
                readableByteChannel = Channels.newChannel((InputStream)closeable);
            } catch (IOException iOException) {
                Bukkit.getScheduler().runTask(this.plugin, () -> this.downloadResponse.accept(DownloadResponse.ERROR, null));
                iOException.printStackTrace();
                return;
            }
            object = null;
            try {
                closeable = new FileOutputStream(file3);
                object = ((FileOutputStream)closeable).getChannel();
                ((FileChannel)object).transferFrom(readableByteChannel, 0L, Long.MAX_VALUE);
            } catch (IOException iOException) {
                Bukkit.getScheduler().runTask(this.plugin, () -> this.downloadResponse.accept(DownloadResponse.ERROR, null));
                iOException.printStackTrace();
                return;
            } finally {
                if (readableByteChannel != null) {
                    try {
                        readableByteChannel.close();
                    } catch (IOException iOException) {
                        System.out.println("Error while closing response body channel");
                    }
                }
                if (object != null) {
                    try {
                        ((AbstractInterruptibleChannel)object).close();
                    } catch (IOException iOException) {
                        System.out.println("Error while closing file channel for downloaded file");
                    }
                }
            }
            Bukkit.getScheduler().runTask(this.plugin, () -> this.downloadResponse.accept(DownloadResponse.DONE, file3.getPath()));
        });
    }

    private File getPluginFile() {
        if (!(this.plugin instanceof JavaPlugin)) {
            return null;
        }
        try {
            Method method = JavaPlugin.class.getDeclaredMethod("getFile", new Class[0]);
            method.setAccessible(true);
            return (File)method.invoke(this.plugin, new Object[0]);
        } catch (ReflectiveOperationException reflectiveOperationException) {
            throw new RuntimeException("Could not get plugin file", reflectiveOperationException);
        }
    }

    public static class Version {
        private final String version;

        public final String get() {
            return this.version;
        }

        private Version(String string) {
            if (string == null) {
                throw new IllegalArgumentException("Version can not be null");
            }
            if (string.endsWith("-SNAPSHOT")) {
                string = string.substring(0, string.length() - "-SNAPSHOT".length());
            }
            if (!string.matches("[0-9]+(\\.[0-9]+)*")) {
                throw new IllegalArgumentException("Invalid version format");
            }
            this.version = string;
        }

        private VersionResponse check(Version version) {
            String[] stringArray = this.get().split("\\.");
            String[] stringArray2 = version.get().split("\\.");
            int n = Math.max(stringArray.length, stringArray2.length);
            for (int i = 0; i < n; ++i) {
                int n2;
                int n3 = i < stringArray.length ? Integer.parseInt(stringArray[i]) : 0;
                int n4 = n2 = i < stringArray2.length ? Integer.parseInt(stringArray2[i]) : 0;
                if (n3 < n2) {
                    return VersionResponse.FOUND_NEW;
                }
                if (n3 <= n2) continue;
                return VersionResponse.THIS_NEWER;
            }
            return VersionResponse.LATEST;
        }
    }

    public static enum CheckType {
        SPIGOT,
        POLYMART_PAID;

    }

    public static enum DownloadResponse {
        DONE,
        ERROR,
        UNAVAILABLE;

    }

    public static enum VersionResponse {
        LATEST,
        FOUND_NEW,
        THIS_NEWER,
        UNAVAILABLE;

    }
}

