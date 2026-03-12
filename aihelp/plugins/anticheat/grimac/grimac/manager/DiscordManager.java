package ac.grim.grimac.manager;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.GrimUser;
import ac.grim.grimac.api.config.ConfigManager;
import ac.grim.grimac.manager.init.ReloadableInitable;
import ac.grim.grimac.manager.init.start.StartableInitable;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.Unmodifiable;
import ac.grim.grimac.utils.anticheat.LogUtil;
import ac.grim.grimac.utils.anticheat.MessageUtil;
import ac.grim.grimac.utils.data.Pair;
import ac.grim.grimac.utils.data.webhook.discord.CompiledDiscordTemplate;
import ac.grim.grimac.utils.data.webhook.discord.Embed;
import ac.grim.grimac.utils.data.webhook.discord.EmbedField;
import ac.grim.grimac.utils.data.webhook.discord.EmbedFooter;
import ac.grim.grimac.utils.data.webhook.discord.WebhookMessage;
import java.awt.Color;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class DiscordManager implements StartableInitable, ReloadableInitable {
   private static final Predicate<String> WEBHOOK_REGEX = Pattern.compile("^https://discord\\.com/api(?:/v\\d+)?/webhooks/\\d+/[\\w-]+(\\?thread_id=\\d+)?$").asMatchPredicate();
   private static final Predicate<String> HTTPS_URL_REGEX = Pattern.compile("^https://[^/\\s]+/\\S+$").asMatchPredicate();
   private static final Duration timeout = Duration.ofSeconds(15L);
   private static final HttpClient client;
   private static final ConcurrentLinkedDeque<Pair<HttpRequest, CompletableFuture<Boolean>>> requests;
   private static final AtomicBoolean taskStarted;
   private static final AtomicBoolean sending;
   private static long rateLimitedUntil;
   private URI url;
   private int embedColor;
   private CompiledDiscordTemplate compiledContent;
   private char backtickReplacement = 715;
   private String embedTitle = "";
   private boolean includeTimestamp;
   private boolean includeVerbose;
   @Nullable
   private String embedImageUrl;
   @Nullable
   private String embedThumbnailUrl;
   @Nullable
   private String embedFooterUrl;
   private String embedFooterText = "";
   private static final Pattern URL_PATTERN;

   private static String validatedConfigURL(String configPath, String defaultURL) {
      String url = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("embed-image-url", defaultURL);
      if (url != null && !url.isBlank()) {
         if (URL_PATTERN.matcher(url).matches()) {
            return url;
         } else {
            LogUtil.warn("Invalid embed url for config path " + configPath + ": " + configPath);
            return defaultURL;
         }
      } else {
         return null;
      }
   }

   public void start() {
      this.reload();
   }

   public void reload() {
      try {
         ConfigManager config = GrimAPI.INSTANCE.getConfigManager().getConfig();
         if (!config.getBooleanElse("enabled", false)) {
            this.url = null;
            return;
         }

         String webhook = config.getStringElse("webhook", "");
         boolean strictValidation = !config.getBooleanElse("disable-webhook-validation", false);
         if (webhook.isEmpty()) {
            this.url = null;
         } else if (strictValidation) {
            if (!WEBHOOK_REGEX.test(webhook)) {
               LogUtil.error("Discord webhook URL does not match expected format (https://discord.com/api/webhooks/<id>/<token>): " + webhook);
               LogUtil.error("If you are using a proxy or custom endpoint, set 'disable-webhook-validation: true' in the Discord config.");
               this.url = null;
            } else {
               this.url = new URI(webhook);
            }
         } else if (!HTTPS_URL_REGEX.test(webhook)) {
            LogUtil.error("Discord webhook URL is not a valid HTTPS URL: " + webhook);
            this.url = null;
         } else {
            String var10000 = webhook.substring(0, Math.min(webhook.length(), 40));
            LogUtil.info("Webhook validation disabled — using custom endpoint: " + var10000 + "...");
            this.url = new URI(webhook);
         }

         this.embedImageUrl = validatedConfigURL("embed-image-url", (String)null);
         this.embedThumbnailUrl = validatedConfigURL("embed-thumbnail-url", "https://crafthead.net/helm/%uuid%");
         this.embedFooterUrl = validatedConfigURL("embed-footer-url", "https://grim.ac/images/grim.png");
         this.embedFooterText = config.getStringElse("embed-footer-text", "v%grim_version%");
         this.embedTitle = config.getStringElse("embed-title", "**Grim Alert**");

         try {
            this.embedColor = Color.decode(config.getStringElse("embed-color", "#00FFFF")).getRGB();
         } catch (NumberFormatException var7) {
            LogUtil.warn("Discord embed color is invalid");
         }

         StringBuilder sb = new StringBuilder();
         Iterator var5 = config.getStringListElse("violation-content", this.getDefaultContents()).iterator();

         while(var5.hasNext()) {
            String string = (String)var5.next();
            sb.append(string).append("\n");
         }

         this.includeTimestamp = config.getBooleanElse("include-timestamp", true);
         this.includeVerbose = config.getBooleanElse("include-verbose", true);
         String btReplace = config.getStringElse("backtick-replacement-char", "ˋ");
         this.backtickReplacement = btReplace.isEmpty() ? 715 : btReplace.charAt(0);
         this.compiledContent = CompiledDiscordTemplate.compile(sb.toString());
      } catch (Exception var8) {
         LogUtil.error("Failed to load Discord webhook configuration", var8);
      }

   }

   @Contract(
      value = " -> new",
      pure = true
   )
   @NotNull
   @Unmodifiable
   private List<String> getDefaultContents() {
      return List.of("**Player**: `%player%`", "**Check**: %check%", "**Violations**: %violations%", "**Client Version**: %version%", "**Brand**: `%brand%`", "**Ping**: %ping%", "**TPS**: %tps%");
   }

   public void sendAlert(@NotNull GrimPlayer player, String verbose, String checkName, int violations) {
      if (!this.isDisabled()) {
         Map<String, String> statics = new HashMap(GrimAPI.INSTANCE.getExternalAPI().getStaticReplacements());
         statics.put("%check%", checkName);
         statics.put("%violations%", Integer.toString(violations));
         Map<String, Function<GrimUser, String>> dynamics = GrimAPI.INSTANCE.getExternalAPI().getVariableReplacements();
         String content = this.compiledContent.render(player, statics, dynamics, this.backtickReplacement);
         Embed embed = (new Embed(content)).color(this.embedColor).title(this.embedTitle).imageURL(MessageUtil.replacePlaceholders(player, this.embedImageUrl, false)).thumbnailURL(MessageUtil.replacePlaceholders(player, this.embedThumbnailUrl, false)).footer(new EmbedFooter(MessageUtil.replacePlaceholders(player, this.embedFooterText, true), MessageUtil.replacePlaceholders(player, this.embedFooterUrl, false)));
         if (this.includeTimestamp) {
            embed.timestamp(Instant.now());
         }

         if (!verbose.isEmpty() && this.includeVerbose) {
            embed.addFields(new EmbedField("Verbose", CompiledDiscordTemplate.escapeMarkdown(verbose), true));
         }

         this.sendWebhookMessage((new WebhookMessage()).addEmbeds(embed));
      }
   }

   public CompletableFuture<Boolean> sendWebhookMessage(WebhookMessage message) {
      if (this.isDisabled()) {
         return CompletableFuture.completedFuture(false);
      } else {
         HttpRequest request = HttpRequest.newBuilder().uri(this.url).header("Content-Type", "application/json").POST(BodyPublishers.ofString(message.toJson().toString())).timeout(timeout).build();
         CompletableFuture<Boolean> future = new CompletableFuture();
         requests.add(new Pair(request, future));
         if (!taskStarted.getAndSet(true)) {
            GrimAPI.INSTANCE.getScheduler().getAsyncScheduler().runAtFixedRate(GrimAPI.INSTANCE.getGrimPlugin(), DiscordManager::tick, 0L, 1L);
         }

         return future;
      }
   }

   public boolean isDisabled() {
      return this.url == null;
   }

   private static void tick() {
      Pair<HttpRequest, CompletableFuture<Boolean>> pair = (Pair)requests.peek();
      if (pair != null && rateLimitedUntil < System.currentTimeMillis() && !sending.getAndSet(true)) {
         HttpRequest request = (HttpRequest)pair.first();
         client.sendAsync(request, BodyHandlers.ofString()).whenComplete((response, throwable) -> {
            if (throwable != null) {
               sending.set(false);
               LogUtil.error("Exception caught while sending a Discord webhook alert", throwable);
            } else if (response != null && response.statusCode() == 429) {
               sending.set(false);
               rateLimitedUntil = Math.max(response.headers().firstValueAsLong("X-RateLimit-Reset").getAsLong() * 1000L, rateLimitedUntil);
            } else {
               requests.remove(pair);
               sending.set(false);
               if (response != null && response.statusCode() >= 400) {
                  int var10000 = response.statusCode();
                  LogUtil.error("Encountered status code " + var10000 + " with body " + (String)response.body() + " and headers " + String.valueOf(response.headers().map()) + " while sending a Discord webhook alert.");
                  ((CompletableFuture)pair.second()).complete(false);
               } else {
                  ((CompletableFuture)pair.second()).complete(true);
               }

            }
         });
      }

   }

   static {
      client = HttpClient.newBuilder().connectTimeout(timeout).build();
      requests = new ConcurrentLinkedDeque();
      taskStarted = new AtomicBoolean();
      sending = new AtomicBoolean();
      URL_PATTERN = Pattern.compile("^https?://(?:www\\.)?[-a-z0-9@:%._+~#=]{1,256}\\.[a-z0-9()]{1,6}\\b[-a-z0-9()@:%_+.~#?&/=]*$", 2);
   }
}
