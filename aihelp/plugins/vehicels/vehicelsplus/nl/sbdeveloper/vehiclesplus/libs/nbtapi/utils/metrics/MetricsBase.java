/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.zip.GZIPOutputStream;
import javax.net.ssl.HttpsURLConnection;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.charts.CustomChart;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;

public class MetricsBase {
    public static final String METRICS_VERSION = "3.1.0";
    private static final String REPORT_URL = "https://bStats.org/api/v2/data/%s";
    private final ScheduledExecutorService scheduler;
    private final String platform;
    private final String serverUuid;
    private final int serviceId;
    private final Consumer<JsonObjectBuilder> appendPlatformDataConsumer;
    private final Consumer<JsonObjectBuilder> appendServiceDataConsumer;
    private final Consumer<Runnable> submitTaskConsumer;
    private final Supplier<Boolean> checkServiceEnabledSupplier;
    private final BiConsumer<String, Throwable> errorLogger;
    private final Consumer<String> infoLogger;
    private final boolean logErrors;
    private final boolean logSentData;
    private final boolean logResponseStatusText;
    private final Set<CustomChart> customCharts = new HashSet<CustomChart>();
    private final boolean enabled;

    public MetricsBase(String string, String string2, int n, boolean bl, Consumer<JsonObjectBuilder> consumer, Consumer<JsonObjectBuilder> consumer2, Consumer<Runnable> consumer3, Supplier<Boolean> supplier, BiConsumer<String, Throwable> biConsumer, Consumer<String> consumer4, boolean bl2, boolean bl3, boolean bl4, boolean bl5) {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "bStats-Metrics");
            thread.setDaemon(true);
            return thread;
        });
        scheduledThreadPoolExecutor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        this.scheduler = scheduledThreadPoolExecutor;
        this.platform = string;
        this.serverUuid = string2;
        this.serviceId = n;
        this.enabled = bl;
        this.appendPlatformDataConsumer = consumer;
        this.appendServiceDataConsumer = consumer2;
        this.submitTaskConsumer = consumer3;
        this.checkServiceEnabledSupplier = supplier;
        this.errorLogger = biConsumer;
        this.infoLogger = consumer4;
        this.logErrors = bl2;
        this.logSentData = bl3;
        this.logResponseStatusText = bl4;
        if (!bl5) {
            this.checkRelocation();
        }
        if (bl) {
            this.startSubmitting();
        }
    }

    public void addCustomChart(CustomChart customChart) {
        this.customCharts.add(customChart);
    }

    public void shutdown() {
        this.scheduler.shutdown();
    }

    private void startSubmitting() {
        Runnable runnable = () -> {
            if (!this.enabled || !this.checkServiceEnabledSupplier.get().booleanValue()) {
                this.scheduler.shutdown();
                return;
            }
            if (this.submitTaskConsumer != null) {
                this.submitTaskConsumer.accept(this::submitData);
            } else {
                this.submitData();
            }
        };
        long l = (long)(60000.0 * (3.0 + Math.random() * 3.0));
        long l2 = (long)(60000.0 * (Math.random() * 30.0));
        this.scheduler.schedule(runnable, l, TimeUnit.MILLISECONDS);
        this.scheduler.scheduleAtFixedRate(runnable, l + l2, 1800000L, TimeUnit.MILLISECONDS);
    }

    private void submitData() {
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        this.appendPlatformDataConsumer.accept(jsonObjectBuilder);
        JsonObjectBuilder jsonObjectBuilder2 = new JsonObjectBuilder();
        this.appendServiceDataConsumer.accept(jsonObjectBuilder2);
        JsonObjectBuilder.JsonObject[] jsonObjectArray = (JsonObjectBuilder.JsonObject[])this.customCharts.stream().map(customChart -> customChart.getRequestJsonObject(this.errorLogger, this.logErrors)).filter(Objects::nonNull).toArray(JsonObjectBuilder.JsonObject[]::new);
        jsonObjectBuilder2.appendField("id", this.serviceId);
        jsonObjectBuilder2.appendField("customCharts", jsonObjectArray);
        jsonObjectBuilder.appendField("service", jsonObjectBuilder2.build());
        jsonObjectBuilder.appendField("serverUUID", this.serverUuid);
        jsonObjectBuilder.appendField("metricsVersion", METRICS_VERSION);
        JsonObjectBuilder.JsonObject jsonObject = jsonObjectBuilder.build();
        this.scheduler.execute(() -> {
            block2: {
                try {
                    this.sendData(jsonObject);
                } catch (Exception exception) {
                    if (!this.logErrors) break block2;
                    this.errorLogger.accept("Could not submit bStats metrics data", exception);
                }
            }
        });
    }

    private void sendData(JsonObjectBuilder.JsonObject jsonObject) {
        if (this.logSentData) {
            this.infoLogger.accept("Sent bStats metrics data: " + jsonObject.toString());
        }
        String string = String.format(REPORT_URL, this.platform);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)new URL(string).openConnection();
        byte[] byArray = MetricsBase.compress(jsonObject.toString());
        httpsURLConnection.setRequestMethod("POST");
        httpsURLConnection.addRequestProperty("Accept", "application/json");
        httpsURLConnection.addRequestProperty("Connection", "close");
        httpsURLConnection.addRequestProperty("Content-Encoding", "gzip");
        httpsURLConnection.addRequestProperty("Content-Length", String.valueOf(byArray.length));
        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        httpsURLConnection.setRequestProperty("User-Agent", "Metrics-Service/1");
        httpsURLConnection.setDoOutput(true);
        try (Object object = new DataOutputStream(httpsURLConnection.getOutputStream());){
            ((FilterOutputStream)object).write(byArray);
        }
        object = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));){
            String string2;
            while ((string2 = bufferedReader.readLine()) != null) {
                ((StringBuilder)object).append(string2);
            }
        }
        if (this.logResponseStatusText) {
            this.infoLogger.accept("Sent data to bStats and received response: " + object);
        }
    }

    private void checkRelocation() {
        if (System.getProperty("bstats.relocatecheck") == null || !System.getProperty("bstats.relocatecheck").equals("false")) {
            String string = new String(new byte[]{111, 114, 103, 46, 98, 115, 116, 97, 116, 115});
            String string2 = new String(new byte[]{121, 111, 117, 114, 46, 112, 97, 99, 107, 97, 103, 101});
            if (MetricsBase.class.getPackage().getName().startsWith(string) || MetricsBase.class.getPackage().getName().startsWith(string2)) {
                throw new IllegalStateException("bStats Metrics class has not been relocated correctly!");
            }
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

