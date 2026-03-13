/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore.nightbreak;

import com.magmaguy.magmacore.util.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import lombok.Generated;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class NightbreakAccount {
    private static final String BASE_URL = "https://nightbreak.io";
    private static final String CONFIG_FOLDER_NAME = "MagmaCore";
    private static final String CONFIG_FILE_NAME = "nightbreak.yml";
    private static NightbreakAccount instance;
    private String token;
    private static final int CONNECT_TIMEOUT_MS = 10000;
    private static final int READ_TIMEOUT_MS = 10000;

    private NightbreakAccount(String token) {
        this.token = token;
        instance = this;
    }

    public static NightbreakAccount initialize(JavaPlugin plugin) {
        File configFile = NightbreakAccount.getConfigFile(plugin);
        if (!configFile.exists()) {
            Logger.info("No Nightbreak token found. Use /nightbreaklogin <token> to register your token.");
            return null;
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration((File)configFile);
        String token = config.getString("token");
        if (token == null || token.isEmpty() || token.equals("YOUR_TOKEN_HERE")) {
            Logger.info("No Nightbreak token configured. Use /nightbreaklogin <token> to register your token.");
            return null;
        }
        instance = new NightbreakAccount(token);
        Logger.info("Nightbreak account loaded successfully!");
        return instance;
    }

    public static NightbreakAccount registerToken(JavaPlugin plugin, String token) {
        File configFile = NightbreakAccount.getConfigFile(plugin);
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration((File)configFile);
        config.options().setHeader(List.of((Object[])new String[]{"MagmaCore Shared Configuration", "", "This folder is a common config folder for plugins created by MagmaGuy, including:", "- EliteMobs", "- BetterStructures", "- FreeMinecraftModels", "- BetterFood", "- ResourcePackManager", "- ExtractionCraft", "- EternalTD", "- MegaBlock Survivors", "- And others!", "", "This folder can be deleted without causing too many problems,", "but you may need to reconfigure shared settings like your Nightbreak token.", "", "Get your token at: https://nightbreak.io/account"}));
        config.set("token", (Object)token);
        try {
            config.save(configFile);
        } catch (IOException e) {
            Logger.warn("Failed to save Nightbreak token: " + e.getMessage());
            return null;
        }
        instance = new NightbreakAccount(token);
        return instance;
    }

    private static File getConfigFile(JavaPlugin plugin) {
        File pluginsFolder = plugin.getDataFolder().getParentFile();
        File magmaCoreFolder = new File(pluginsFolder, CONFIG_FOLDER_NAME);
        return new File(magmaCoreFolder, CONFIG_FILE_NAME);
    }

    public static boolean hasToken() {
        return instance != null && NightbreakAccount.instance.token != null && !NightbreakAccount.instance.token.isEmpty();
    }

    public VersionInfo getVersion(String slug) {
        try {
            String url = "https://nightbreak.io/server/dlc/" + slug + "/version";
            String response = this.httpGet(url, false);
            if (response == null) {
                return null;
            }
            return this.parseVersionInfo(response);
        } catch (Exception e) {
            Logger.warn("Error getting version for DLC '" + slug + "': " + e.getMessage());
            return null;
        }
    }

    public Map<String, VersionInfo> getAllVersions() {
        try {
            String url = "https://nightbreak.io/api/dlc/versions";
            String response = this.httpGet(url, false);
            if (response == null) {
                return new HashMap<String, VersionInfo>();
            }
            return this.parseAllVersionsResponse(response);
        } catch (Exception e) {
            Logger.warn("Error getting all DLC versions: " + e.getMessage());
            return new HashMap<String, VersionInfo>();
        }
    }

    public AccessInfo checkAccess(String slug) {
        if (!NightbreakAccount.hasToken()) {
            Logger.warn("Cannot check access: No Nightbreak token registered. Use /nightbreaklogin <token> first.");
            return null;
        }
        try {
            String url = "https://nightbreak.io/server/dlc/" + slug + "/access";
            String response = this.httpGet(url, true);
            if (response == null) {
                return null;
            }
            return this.parseAccessInfo(response);
        } catch (Exception e) {
            Logger.warn("Error checking access for DLC '" + slug + "': " + e.getMessage());
            return null;
        }
    }

    public boolean download(String slug, File destinationFile) {
        return this.download(slug, destinationFile, null);
    }

    public boolean download(String slug, File destinationFile, String version) {
        if (!NightbreakAccount.hasToken()) {
            Logger.warn("Cannot download: No Nightbreak token registered. Use /nightbreaklogin <token> first.");
            return false;
        }
        try {
            String url = "https://nightbreak.io/server/dlc/" + slug + "/download";
            if (version != null) {
                url = url + "?version=" + version;
            }
            return this.httpDownload(url, destinationFile);
        } catch (Exception e) {
            Logger.warn("Error downloading DLC '" + slug + "': " + e.getMessage());
            return false;
        }
    }

    public boolean download(String slug, File destinationFile, String version, DownloadProgressCallback progressCallback) {
        if (!NightbreakAccount.hasToken()) {
            Logger.warn("Cannot download: No Nightbreak token registered. Use /nightbreaklogin <token> first.");
            return false;
        }
        try {
            String url = "https://nightbreak.io/server/dlc/" + slug + "/download";
            if (version != null) {
                url = url + "?version=" + version;
            }
            return this.httpDownloadWithProgress(url, destinationFile, progressCallback);
        } catch (Exception e) {
            Logger.warn("Error downloading DLC '" + slug + "': " + e.getMessage());
            return false;
        }
    }

    private String httpGet(String urlString, boolean withAuth) {
        try {
            int responseCode;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            if (withAuth && this.token != null) {
                connection.setRequestProperty("Authorization", "Bearer " + this.token);
            }
            if ((responseCode = connection.getResponseCode()) == 200) {
                Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();
                return response.toString();
            }
            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                Scanner scanner = new Scanner(errorStream, StandardCharsets.UTF_8);
                StringBuilder errorResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    errorResponse.append(scanner.nextLine());
                }
                scanner.close();
                Logger.warn("API error (" + responseCode + ") for " + urlString + ": " + String.valueOf(errorResponse));
            } else {
                Logger.warn("API error (" + responseCode + ") for " + urlString + " (no error body)");
            }
            return null;
        } catch (IOException e) {
            Logger.warn("HTTP request failed for " + urlString + ": " + e.getMessage());
            return null;
        }
    }

    private boolean httpDownload(String urlString, File destinationFile) {
        try {
            int responseCode;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            if (this.token != null) {
                connection.setRequestProperty("Authorization", "Bearer " + this.token);
            }
            if ((responseCode = connection.getResponseCode()) == 200) {
                if (!destinationFile.getParentFile().exists()) {
                    destinationFile.getParentFile().mkdirs();
                }
                try (InputStream in = connection.getInputStream();
                     FileOutputStream out = new FileOutputStream(destinationFile);){
                    int bytesRead;
                    byte[] buffer = new byte[8192];
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    if (destinationFile.exists()) {
                        destinationFile.delete();
                    }
                    throw e;
                }
                return true;
            }
            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                Scanner scanner = new Scanner(errorStream, StandardCharsets.UTF_8);
                StringBuilder errorResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    errorResponse.append(scanner.nextLine());
                }
                scanner.close();
                Logger.warn("Download failed (" + responseCode + ") for " + urlString + ": " + String.valueOf(errorResponse));
            } else {
                Logger.warn("Download failed (" + responseCode + ") for " + urlString + " (no error body)");
            }
            return false;
        } catch (IOException e) {
            if (destinationFile.exists()) {
                destinationFile.delete();
            }
            Logger.warn("Download failed for " + urlString + ": " + e.getMessage());
            return false;
        }
    }

    private boolean httpDownloadWithProgress(String urlString, File destinationFile, DownloadProgressCallback callback) {
        try {
            int responseCode;
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            if (this.token != null) {
                connection.setRequestProperty("Authorization", "Bearer " + this.token);
            }
            if ((responseCode = connection.getResponseCode()) == 200) {
                if (!destinationFile.getParentFile().exists()) {
                    destinationFile.getParentFile().mkdirs();
                }
                long totalBytes = connection.getContentLengthLong();
                long bytesDownloaded = 0L;
                long lastProgressUpdate = 0L;
                try (InputStream in = connection.getInputStream();
                     FileOutputStream out = new FileOutputStream(destinationFile);){
                    int bytesRead;
                    byte[] buffer = new byte[8192];
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                        if (callback == null || (bytesDownloaded += (long)bytesRead) - lastProgressUpdate < 102400L && bytesRead != -1) continue;
                        callback.onProgress(bytesDownloaded, totalBytes);
                        lastProgressUpdate = bytesDownloaded;
                    }
                    if (callback != null) {
                        callback.onProgress(bytesDownloaded, totalBytes);
                    }
                } catch (IOException e) {
                    if (destinationFile.exists()) {
                        destinationFile.delete();
                    }
                    throw e;
                }
                return true;
            }
            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                Scanner scanner = new Scanner(errorStream, StandardCharsets.UTF_8);
                StringBuilder errorResponse = new StringBuilder();
                while (scanner.hasNext()) {
                    errorResponse.append(scanner.nextLine());
                }
                scanner.close();
                Logger.warn("Download failed (" + responseCode + ") for " + urlString + ": " + String.valueOf(errorResponse));
            } else {
                Logger.warn("Download failed (" + responseCode + ") for " + urlString + " (no error body)");
            }
            return false;
        } catch (IOException e) {
            if (destinationFile.exists()) {
                destinationFile.delete();
            }
            Logger.warn("Download failed for " + urlString + ": " + e.getMessage());
            return false;
        }
    }

    private VersionInfo parseVersionInfo(String json) {
        VersionInfo info = new VersionInfo();
        info.slug = this.extractJsonString(json, "slug");
        info.version = this.extractJsonString(json, "version");
        info.versionInt = this.extractJsonInt(json, "versionInt");
        info.fileSize = this.extractJsonLong(json, "fileSize");
        info.fileName = this.extractJsonString(json, "fileName");
        return info;
    }

    private AccessInfo parseAccessInfo(String json) {
        AccessInfo info = new AccessInfo();
        info.slug = this.extractJsonString(json, "slug");
        info.hasAccess = this.extractJsonBoolean(json, "hasAccess");
        info.accessSource = this.extractJsonString(json, "accessSource");
        info.reason = this.extractJsonString(json, "reason");
        info.version = this.extractJsonString(json, "version");
        info.fileSize = this.extractJsonLong(json, "fileSize");
        info.requiredTier = this.extractJsonString(json, "requiredTier");
        info.patreonLink = this.extractJsonString(json, "patreonLink");
        info.itchLink = this.extractJsonString(json, "itchLink");
        info.error = this.extractJsonString(json, "error");
        info.message = this.extractJsonString(json, "message");
        return info;
    }

    private String extractJsonString(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            return null;
        }
        int endIndex = json.indexOf("\"", startIndex += searchKey.length());
        if (endIndex == -1) {
            return null;
        }
        return json.substring(startIndex, endIndex);
    }

    private int extractJsonInt(String json, String key) {
        int endIndex;
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            return -1;
        }
        for (endIndex = startIndex += searchKey.length(); endIndex < json.length() && (Character.isDigit(json.charAt(endIndex)) || json.charAt(endIndex) == '-'); ++endIndex) {
        }
        try {
            return Integer.parseInt(json.substring(startIndex, endIndex));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private long extractJsonLong(String json, String key) {
        int endIndex;
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            return -1L;
        }
        for (endIndex = startIndex += searchKey.length(); endIndex < json.length() && (Character.isDigit(json.charAt(endIndex)) || json.charAt(endIndex) == '-'); ++endIndex) {
        }
        try {
            return Long.parseLong(json.substring(startIndex, endIndex));
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    private boolean extractJsonBoolean(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex == -1) {
            return false;
        }
        return json.substring(startIndex += searchKey.length()).startsWith("true");
    }

    private Map<String, VersionInfo> parseAllVersionsResponse(String json) {
        int objectEnd;
        int objectStart;
        HashMap<String, VersionInfo> versions = new HashMap<String, VersionInfo>();
        int versionsStart = json.indexOf("\"versions\":");
        if (versionsStart == -1) {
            return versions;
        }
        int arrayStart = json.indexOf("[", versionsStart);
        if (arrayStart == -1) {
            return versions;
        }
        int arrayEnd = this.findMatchingBracket(json, arrayStart, '[', ']');
        if (arrayEnd == -1) {
            return versions;
        }
        String arrayContent = json.substring(arrayStart, arrayEnd + 1);
        int pos = 0;
        while (pos < arrayContent.length() && (objectStart = arrayContent.indexOf("{", pos)) != -1 && (objectEnd = this.findMatchingBracket(arrayContent, objectStart, '{', '}')) != -1) {
            String objectJson = arrayContent.substring(objectStart, objectEnd + 1);
            VersionInfo info = this.parseVersionInfo(objectJson);
            if (info.slug != null) {
                versions.put(info.slug, info);
            }
            pos = objectEnd + 1;
        }
        return versions;
    }

    private int findMatchingBracket(String json, int openPos, char openChar, char closeChar) {
        int depth = 0;
        boolean inString = false;
        for (int i = openPos; i < json.length(); ++i) {
            char c = json.charAt(i);
            if (c == '\"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
                continue;
            }
            if (inString) continue;
            if (c == openChar) {
                ++depth;
                continue;
            }
            if (c != closeChar || --depth != 0) continue;
            return i;
        }
        return -1;
    }

    @Generated
    public static NightbreakAccount getInstance() {
        return instance;
    }

    @Generated
    public String getToken() {
        return this.token;
    }

    public static class VersionInfo {
        public String slug;
        public String version;
        public int versionInt;
        public long fileSize;
        public String fileName;

        public String toString() {
            return "VersionInfo{slug='" + this.slug + "', version='" + this.version + "', versionInt=" + this.versionInt + ", fileSize=" + this.fileSize + ", fileName='" + this.fileName + "'}";
        }
    }

    public static class AccessInfo {
        public String slug;
        public boolean hasAccess;
        public String accessSource;
        public String reason;
        public String version;
        public long fileSize;
        public String requiredTier;
        public String patreonLink;
        public String itchLink;
        public String error;
        public String message;

        public boolean isError() {
            return this.error != null && !this.error.isEmpty();
        }

        public String toString() {
            if (this.isError()) {
                return "AccessInfo{error='" + this.error + "', message='" + this.message + "'}";
            }
            return "AccessInfo{slug='" + this.slug + "', hasAccess=" + this.hasAccess + ", accessSource='" + this.accessSource + "', reason='" + this.reason + "', version='" + this.version + "', fileSize=" + this.fileSize + ", patreonLink='" + this.patreonLink + "', itchLink='" + this.itchLink + "'}";
        }
    }

    public static interface DownloadProgressCallback {
        public void onProgress(long var1, long var3);
    }
}

