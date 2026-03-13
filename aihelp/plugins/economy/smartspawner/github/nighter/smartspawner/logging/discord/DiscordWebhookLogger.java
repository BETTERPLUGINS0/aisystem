package github.nighter.smartspawner.logging.discord;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.logging.SpawnerLogEntry;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiscordWebhookLogger {
   private final SmartSpawner plugin;
   private final DiscordWebhookConfig config;
   private final ConcurrentLinkedQueue<SpawnerLogEntry> webhookQueue;
   private final AtomicBoolean isShuttingDown;
   private final AtomicLong lastWebhookTime;
   private final AtomicLong webhooksSentThisMinute;
   private Scheduler.Task webhookTask;
   private static final int MAX_REQUESTS_PER_MINUTE = 25;
   private static final long MINUTE_IN_MILLIS = 60000L;

   public DiscordWebhookLogger(SmartSpawner plugin, DiscordWebhookConfig config) {
      this.plugin = plugin;
      this.config = config;
      this.webhookQueue = new ConcurrentLinkedQueue();
      this.isShuttingDown = new AtomicBoolean(false);
      this.lastWebhookTime = new AtomicLong(System.currentTimeMillis());
      this.webhooksSentThisMinute = new AtomicLong(0L);
      if (config.isEnabled()) {
         this.startWebhookTask();
      }

   }

   public void queueWebhook(SpawnerLogEntry entry) {
      if (this.config.isEnabled() && this.config.isEventEnabled(entry.getEventType())) {
         this.webhookQueue.offer(entry);
      }
   }

   private void startWebhookTask() {
      this.webhookTask = Scheduler.runTaskTimerAsync(() -> {
         if (!this.isShuttingDown.get()) {
            this.processWebhookQueue();
         }
      }, 40L, 40L);
   }

   private void processWebhookQueue() {
      if (!this.webhookQueue.isEmpty()) {
         long currentTime = System.currentTimeMillis();
         long timeSinceLastCheck = currentTime - this.lastWebhookTime.get();
         if (timeSinceLastCheck >= 60000L) {
            this.webhooksSentThisMinute.set(0L);
            this.lastWebhookTime.set(currentTime);
         }

         while(!this.webhookQueue.isEmpty() && this.webhooksSentThisMinute.get() < 25L) {
            SpawnerLogEntry entry = (SpawnerLogEntry)this.webhookQueue.poll();
            if (entry != null) {
               this.sendWebhook(entry);
               this.webhooksSentThisMinute.incrementAndGet();
            }
         }

         if (this.webhookQueue.size() > 50) {
            this.plugin.getLogger().warning("Discord webhook queue is backing up: " + this.webhookQueue.size() + " entries pending");
         }

      }
   }

   private void sendWebhook(SpawnerLogEntry entry) {
      String webhookUrl = this.config.getWebhookUrl();
      if (webhookUrl != null && !webhookUrl.isEmpty()) {
         try {
            DiscordEmbed embed = DiscordEmbedBuilder.buildEmbed(entry, this.config, this.plugin);
            String jsonPayload = embed.toJson();
            Scheduler.runTaskAsync(() -> {
               try {
                  this.sendHttpRequest(webhookUrl, jsonPayload);
               } catch (IOException var4) {
                  this.plugin.getLogger().log(Level.WARNING, "Failed to send Discord webhook", var4);
               }

            });
         } catch (Exception var5) {
            this.plugin.getLogger().log(Level.WARNING, "Error building Discord embed", var5);
         }

      }
   }

   private void sendHttpRequest(String webhookUrl, String jsonPayload) throws IOException {
      HttpURLConnection connection = null;

      try {
         URL url = new URL(webhookUrl);
         connection = (HttpURLConnection)url.openConnection();
         connection.setRequestMethod("POST");
         connection.setRequestProperty("Content-Type", "application/json");
         connection.setRequestProperty("User-Agent", "SmartSpawner-Logger/1.0");
         connection.setDoOutput(true);
         connection.setConnectTimeout(5000);
         connection.setReadTimeout(5000);
         OutputStream os = connection.getOutputStream();

         try {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
         } catch (Throwable var13) {
            if (os != null) {
               try {
                  os.close();
               } catch (Throwable var12) {
                  var13.addSuppressed(var12);
               }
            }

            throw var13;
         }

         if (os != null) {
            os.close();
         }

         int responseCode = connection.getResponseCode();
         if (responseCode == 429) {
            this.plugin.getLogger().warning("Discord webhook rate limited. Entries will retry.");
         } else if (responseCode < 200 || responseCode >= 300) {
            this.plugin.getLogger().warning("Discord webhook returned error code: " + responseCode);
         }
      } finally {
         if (connection != null) {
            connection.disconnect();
         }

      }

   }

   public void shutdown() {
      this.isShuttingDown.set(true);
      if (this.webhookTask != null) {
         this.webhookTask.cancel();
      }

      int flushed = 0;

      while(!this.webhookQueue.isEmpty() && flushed < 10) {
         SpawnerLogEntry entry = (SpawnerLogEntry)this.webhookQueue.poll();
         if (entry != null) {
            this.sendWebhook(entry);
            ++flushed;
         }
      }

      if (!this.webhookQueue.isEmpty()) {
         Logger var10000 = this.plugin.getLogger();
         int var10001 = this.webhookQueue.size();
         var10000.warning("Discord webhook queue had " + var10001 + " pending entries at shutdown (flushed " + flushed + ")");
      }

   }
}
