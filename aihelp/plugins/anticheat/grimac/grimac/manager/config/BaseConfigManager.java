package ac.grim.grimac.manager.config;

import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.utils.anticheat.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import lombok.Generated;

public class BaseConfigManager {
   private final List<Pattern> ignoredClientPatterns = new ArrayList();
   private ConfigManager config = null;
   private boolean printAlertsToConsole = false;
   private String prefix = "&bGrim &8»";
   private String webhookNotEnabled;
   private String webhookTestMessage;
   private String webhookTestSucceeded;
   private String webhookTestFailed;
   private String disconnectTimeout;
   private String disconnectClosed;
   private String disconnectPacketError;
   private String disconnectBlacklistedForge;
   private boolean blockBlacklistedForgeClients;
   private boolean disablePongCancelling;

   public void load(ConfigManager config) {
      this.config = config;
      int configuredMaxTransactionTime = config.getIntElse("max-transaction-time", 60);
      if (configuredMaxTransactionTime > 180 || configuredMaxTransactionTime < 1) {
         LogUtil.warn("Detected invalid max-transaction-time! This setting is clamped between 1 and 180 to prevent issues. Attempting to disable or set this too high can result in memory usage issues.");
      }

      this.ignoredClientPatterns.clear();
      Iterator var3 = config.getStringList("client-brand.ignored-clients").iterator();

      while(var3.hasNext()) {
         String string = (String)var3.next();

         try {
            this.ignoredClientPatterns.add(Pattern.compile(string));
         } catch (PatternSyntaxException var6) {
            throw new RuntimeException("Failed to compile client pattern", var6);
         }
      }

      this.printAlertsToConsole = config.getBooleanElse("alerts.print-to-console", true);
      this.prefix = config.getStringElse("prefix", "&bGrim &8»");
      this.webhookNotEnabled = config.getStringElse("webhook-not-enabled", "Discord webhooks are not enabled!");
      this.webhookTestMessage = config.getStringElse("webhook-test-message", "test message");
      this.webhookTestSucceeded = config.getStringElse("webhook-test-succeeded", "Discord webhook test succeeded!");
      this.webhookTestFailed = config.getStringElse("webhook-test-failed", "Discord webhook test failed!");
      this.disconnectTimeout = config.getStringElse("disconnect.timeout", "<lang:disconnect.timeout>");
      this.disconnectClosed = config.getStringElse("disconnect.closed", "<lang:disconnect.timeout>");
      this.disconnectPacketError = config.getStringElse("disconnect.error", "<red>An error occurred whilst processing packets. Please contact the administrators.");
      this.blockBlacklistedForgeClients = config.getBooleanElse("client-brand.disconnect-blacklisted-forge-versions", true);
      this.disconnectBlacklistedForge = config.getStringElse("disconnect.blacklisted-forge", "<red>Your forge version is blacklisted due to inbuilt reach hacks.<newline><gold>Versions affected: 1.18.2-1.19.3<newline><newline><red>Please see https://github.com/MinecraftForge/MinecraftForge/issues/9309.");
      this.disablePongCancelling = config.getBooleanElse("disable-pong-cancelling", false);
   }

   public void start() {
   }

   public boolean isIgnoredClient(String brand) {
      Iterator var2 = this.ignoredClientPatterns.iterator();

      Pattern pattern;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         pattern = (Pattern)var2.next();
      } while(!pattern.matcher(brand).find());

      return true;
   }

   @Generated
   public ConfigManager getConfig() {
      return this.config;
   }

   @Generated
   public boolean isPrintAlertsToConsole() {
      return this.printAlertsToConsole;
   }

   @Generated
   public String getPrefix() {
      return this.prefix;
   }

   @Generated
   public String getWebhookNotEnabled() {
      return this.webhookNotEnabled;
   }

   @Generated
   public String getWebhookTestMessage() {
      return this.webhookTestMessage;
   }

   @Generated
   public String getWebhookTestSucceeded() {
      return this.webhookTestSucceeded;
   }

   @Generated
   public String getWebhookTestFailed() {
      return this.webhookTestFailed;
   }

   @Generated
   public String getDisconnectTimeout() {
      return this.disconnectTimeout;
   }

   @Generated
   public String getDisconnectClosed() {
      return this.disconnectClosed;
   }

   @Generated
   public String getDisconnectPacketError() {
      return this.disconnectPacketError;
   }

   @Generated
   public String getDisconnectBlacklistedForge() {
      return this.disconnectBlacklistedForge;
   }

   @Generated
   public boolean isBlockBlacklistedForgeClients() {
      return this.blockBlacklistedForgeClients;
   }

   @Generated
   public boolean isDisablePongCancelling() {
      return this.disablePongCancelling;
   }
}
