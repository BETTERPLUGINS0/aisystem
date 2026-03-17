/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MetricsConfig {
    private final File file;
    private final boolean defaultEnabled;
    private String serverUUID;
    private boolean enabled;
    private boolean logErrors;
    private boolean logSentData;
    private boolean logResponseStatusText;
    private boolean didExistBefore = true;

    public MetricsConfig(File file, boolean bl) {
        this.file = file;
        this.defaultEnabled = bl;
        this.setupConfig();
    }

    public String getServerUUID() {
        return this.serverUUID;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isLogErrorsEnabled() {
        return this.logErrors;
    }

    public boolean isLogSentDataEnabled() {
        return this.logSentData;
    }

    public boolean isLogResponseStatusTextEnabled() {
        return this.logResponseStatusText;
    }

    public boolean didExistBefore() {
        return this.didExistBefore;
    }

    private void setupConfig() {
        if (!this.file.exists()) {
            this.didExistBefore = false;
            this.writeConfig();
        }
        this.readConfig();
        if (this.serverUUID == null) {
            this.writeConfig();
            this.readConfig();
        }
    }

    private void writeConfig() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("# bStats (https://bStats.org) collects some basic information for plugin authors, like");
        arrayList.add("# how many people use their plugin and their total player count. It's recommended to keep");
        arrayList.add("# bStats enabled, but if you're not comfortable with this, you can turn this setting off.");
        arrayList.add("# There is no performance penalty associated with having metrics enabled, and data sent to");
        arrayList.add("# bStats is fully anonymous.");
        arrayList.add("enabled=" + this.defaultEnabled);
        arrayList.add("server-uuid=" + UUID.randomUUID().toString());
        arrayList.add("log-errors=false");
        arrayList.add("log-sent-data=false");
        arrayList.add("log-response-status-text=false");
        this.writeFile(this.file, arrayList);
    }

    private void readConfig() {
        List<String> list = this.readFile(this.file);
        if (list == null) {
            throw new AssertionError((Object)"Content of newly created file is null");
        }
        this.enabled = this.getConfigValue("enabled", list).map("true"::equals).orElse(true);
        this.serverUUID = this.getConfigValue("server-uuid", list).orElse(null);
        this.logErrors = this.getConfigValue("log-errors", list).map("true"::equals).orElse(false);
        this.logSentData = this.getConfigValue("log-sent-data", list).map("true"::equals).orElse(false);
        this.logResponseStatusText = this.getConfigValue("log-response-status-text", list).map("true"::equals).orElse(false);
    }

    private Optional<String> getConfigValue(String string, List<String> list) {
        return list.stream().filter(string2 -> string2.startsWith(string + "=")).map(string2 -> string2.replaceFirst(Pattern.quote(string + "="), "")).findFirst();
    }

    private List<String> readFile(File file) {
        if (!file.exists()) {
            return null;
        }
        try (FileReader fileReader = new FileReader(file);){
            List<String> list;
            try (BufferedReader bufferedReader = new BufferedReader(fileReader);){
                list = bufferedReader.lines().collect(Collectors.toList());
            }
            return list;
        }
    }

    private void writeFile(File file, List<String> list) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        try (FileWriter fileWriter = new FileWriter(file);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);){
            for (String string : list) {
                bufferedWriter.write(string);
                bufferedWriter.newLine();
            }
        }
    }
}

