/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.chat.BaseComponent
 *  org.bukkit.command.CommandSender
 *  org.bukkit.plugin.java.JavaPlugin
 */
package com.magmaguy.magmacore.command;

import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import com.magmaguy.magmacore.util.Logger;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class LogifyCommand
extends AdvancedCommand {
    private final JavaPlugin plugin;

    public LogifyCommand(JavaPlugin plugin) {
        super(new ArrayList<String>());
        this.setUsage("/logify");
        this.setSenderType(SenderType.ANY);
        this.setPermission("logify.*");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandData commandData) {
        File[] logFiles;
        if (!commandData.getCommandSender().hasPermission("logify.*")) {
            return;
        }
        CommandSender sender = commandData.getCommandSender();
        File logsFolder = new File(this.plugin.getDataFolder().getParentFile().getParentFile(), "Logs");
        if (!logsFolder.exists()) {
            logsFolder = new File(this.plugin.getDataFolder().getParentFile().getParentFile(), "logs");
        }
        if (!logsFolder.exists() || !logsFolder.isDirectory()) {
            sender.sendMessage("\u00a7cCould not find the logs folder on your server!");
            return;
        }
        File logFile = new File(logsFolder, "latest.log");
        if (!logFile.exists() && (logFiles = logsFolder.listFiles(file -> file.isFile() && file.getName().endsWith(".log"))) != null && logFiles.length > 0) {
            File mostRecentLog = null;
            long mostRecentTime = 0L;
            for (File file2 : logFiles) {
                if (file2.lastModified() <= mostRecentTime) continue;
                mostRecentTime = file2.lastModified();
                mostRecentLog = file2;
            }
            logFile = mostRecentLog;
        }
        if (logFile == null || !logFile.exists()) {
            sender.sendMessage("\u00a7cNo log file found!");
            return;
        }
        try {
            byte[] fileBytes = Files.readAllBytes(logFile.toPath());
            String content = new String(fileBytes, StandardCharsets.UTF_8);
            content = this.anonymizeIPs(content);
            String encodedContent = URLEncoder.encode((String)content, (Charset)StandardCharsets.UTF_8);
            String response = this.uploadLog(encodedContent);
            if (response != null && response.contains("\"success\":true")) {
                String logUrl = this.extractLogUrl(response);
                commandData.getCommandSender().spigot().sendMessage(new BaseComponent[]{Logger.simpleMessage("&aLog uploaded successfully! View it here: "), Logger.hoverLinkMessage("&9" + logUrl, "Click to go to link!", logUrl), Logger.simpleMessage(" &a. "), Logger.hoverCopyMessage("&6Click here to copy it!", "Click to copy link to clipboard!", logUrl)});
            } else {
                Logger.sendMessage(commandData.getCommandSender(), "&cFailed to upload log!");
            }
        } catch (IOException e) {
            sender.sendMessage("\u00a7cAn error occurred while processing the log file.");
            Logger.warn("Error reading log file: " + e.getMessage());
        }
    }

    private String anonymizeIPs(String content) {
        Pattern ipPattern = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}(?::\\d+)?\\b");
        Matcher matcher = ipPattern.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String ipPart = matcher.group();
            String anonymized = ipPart.replaceAll("\\d", "*");
            matcher.appendReplacement(sb, Matcher.quoteReplacement(anonymized));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String uploadLog(String encodedContent) {
        try {
            URL url = new URL("https://api.mclo.gs/1/log");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            String postData = "content=" + encodedContent;
            try (OutputStream os = connection.getOutputStream();){
                os.write(postData.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }
            Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            return response.toString();
        } catch (IOException e) {
            Logger.warn("Error uploading log: " + e.getMessage());
            return null;
        }
    }

    private String extractLogUrl(String jsonResponse) {
        int urlIndex = jsonResponse.indexOf("\"url\":\"") + 7;
        int endIndex = jsonResponse.indexOf("\"", urlIndex);
        return jsonResponse.substring(urlIndex, endIndex).replace("\\/", "/");
    }
}

