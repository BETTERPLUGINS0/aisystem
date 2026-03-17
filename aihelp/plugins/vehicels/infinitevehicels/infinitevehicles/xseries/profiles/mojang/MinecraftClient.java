package me.PM2.infinitevehicles.xseries.profiles.mojang;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import me.PM2.infinitevehicles.xseries.profiles.ProfileLogger;
import me.PM2.infinitevehicles.xseries.profiles.ProfilesCore;
import me.PM2.infinitevehicles.xseries.profiles.exceptions.MojangAPIException;
import me.PM2.infinitevehicles.xseries.profiles.exceptions.MojangAPIRetryException;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class MinecraftClient {
   private static final AtomicInteger SESSION_ID = new AtomicInteger();
   private static final Proxy PROXY;
   private static final Gson GSON;
   private static final RateLimiter TOTAL_REQUESTS;
   private static final String USER_AGENT;
   private final String method;
   private final URI baseURL;
   private final RateLimiter rateLimiter;

   public MinecraftClient.Session session(@Nullable ProfileRequestConfiguration var1) {
      MinecraftClient.Session var2 = new MinecraftClient.Session();
      if (var1 != null) {
         var1.configure(var2);
      }

      return var2;
   }

   public MinecraftClient(String var1, String var2, RateLimiter var3) {
      this.method = var1;

      try {
         this.baseURL = new URI(var2);
      } catch (URISyntaxException var5) {
         throw new IllegalArgumentException("Invalid Minecraft API URL: " + var2, var5);
      }

      this.rateLimiter = var3;
   }

   private static String totalReq() {
      return " (total: " + TOTAL_REQUESTS.getEffectiveRequestsCount() + ')';
   }

   static {
      PROXY = ProfilesCore.PROXY == null ? Proxy.NO_PROXY : ProfilesCore.PROXY;
      GSON = new Gson();
      TOTAL_REQUESTS = new RateLimiter(Integer.MAX_VALUE, Duration.ofMinutes(10L));
      USER_AGENT = "XSeries/13.5.0 (" + System.getProperty("os.name") + "; " + System.getProperty("os.version") + "; " + System.getProperty("java.vendor") + "; " + System.getProperty("java.version") + ") " + Bukkit.getName() + '/' + Bukkit.getBukkitVersion() + ' ' + Bukkit.getVersion();
   }

   public final class Session {
      private final int sessionId;
      private Duration connectTimeout;
      private Duration readTimeout;
      private Duration retryDelay;
      private int retries;
      private boolean waitInQueue;
      private Object body;
      private String append;
      private HttpURLConnection connection;
      private BiFunction<MinecraftClient.Session, Throwable, Boolean> errorHandler;

      public Session() {
         this.sessionId = MinecraftClient.SESSION_ID.getAndIncrement();
         this.connectTimeout = Duration.ofSeconds(10L);
         this.readTimeout = Duration.ofSeconds(10L);
         this.retryDelay = Duration.ofSeconds(5L);
         this.waitInQueue = true;
      }

      private void debug(String var1, Object... var2) {
         Object[] var3 = XReflection.concatenate(new Object[]{this.sessionId, this.append}, var2);
         ProfileLogger.debug("[MinecraftClient-{}][{}] " + var1, var3);
      }

      public MinecraftClient.Session exceptionally(BiFunction<MinecraftClient.Session, Throwable, Boolean> var1) {
         this.errorHandler = var1;
         return this;
      }

      public MinecraftClient.Session waitInQueue(boolean var1) {
         this.waitInQueue = var1;
         return this;
      }

      public MinecraftClient.Session retry(int var1, Duration var2) {
         this.retries = var1;
         this.retryDelay = var2;
         return this;
      }

      public MinecraftClient.Session body(Object var1) {
         this.validateMethod("POST");
         this.body = Objects.requireNonNull(var1);
         return this;
      }

      public MinecraftClient.Session append(@NotNull String var1) {
         this.append = (String)Objects.requireNonNull(var1);
         return this;
      }

      public MinecraftClient.Session timeout(Duration var1, Duration var2) {
         this.connectTimeout = var1;
         this.readTimeout = var2;
         return this;
      }

      private void validateMethod(String var1) {
         if (!MinecraftClient.this.method.equals(var1)) {
            throw new UnsupportedOperationException("Cannot " + var1 + " with a client using method " + MinecraftClient.this);
         }
      }

      @Nullable
      public JsonElement request() {
         try {
            JsonElement var1 = this.request0();
            this.debug("Received response: {}", var1);
            return var1 == null ? null : (var1.isJsonNull() ? null : var1);
         } catch (Exception var4) {
            if (this.retries > 0) {
               --this.retries;
               if (!(var4 instanceof MojangAPIRetryException) || ((MojangAPIRetryException)var4).getReason() != MojangAPIRetryException.Reason.RATELIMITED) {
                  try {
                     Thread.sleep(this.retryDelay.toMillis());
                  } catch (InterruptedException var3) {
                     throw new IllegalStateException("Mojang API retry thread was interrupted unexpectedly", var3);
                  }
               }

               return this.request();
            } else if (this.errorHandler == null) {
               throw var4;
            } else {
               Boolean var2 = (Boolean)this.errorHandler.apply(this, var4);
               if (var2 != null && !var2) {
                  throw var4;
               } else {
                  return this.request();
               }
            }
         }
      }

      @Nullable
      private JsonElement request0() {
         if (this.waitInQueue) {
            MinecraftClient.this.rateLimiter.acquireOrWait();
         } else if (!MinecraftClient.this.rateLimiter.acquire()) {
            throw new MojangAPIRetryException(MojangAPIRetryException.Reason.RATELIMITED, "Rate limit has been hit! " + MinecraftClient.this.rateLimiter + MinecraftClient.totalReq());
         }

         this.connection = (HttpURLConnection)(this.append == null ? MinecraftClient.this.baseURL : MinecraftClient.this.baseURL.resolve(this.append)).toURL().openConnection(MinecraftClient.PROXY);
         this.connection.setRequestMethod(MinecraftClient.this.method);
         this.connection.setConnectTimeout((int)this.connectTimeout.toMillis());
         this.connection.setReadTimeout((int)this.readTimeout.toMillis());
         this.connection.setDoInput(true);
         this.connection.setUseCaches(false);
         this.connection.setAllowUserInteraction(false);
         this.connection.setRequestProperty("User-Agent", MinecraftClient.USER_AGENT);
         if (this.body != null) {
            this.connection.setDoOutput(true);
            String var1 = MinecraftClient.GSON.toJson(this.body);
            this.debug("Writing body {} to {}", var1, this.connection.getURL());
            byte[] var2 = var1.getBytes(StandardCharsets.UTF_8);
            this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            this.connection.setRequestProperty("Content-Length", String.valueOf(var2.length));
            OutputStream var3 = this.connection.getOutputStream();

            try {
               var3.write(var2);
            } catch (Throwable var7) {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (var3 != null) {
               var3.close();
            }
         } else {
            this.connection.setDoOutput(false);
         }

         return this.request00();
      }

      @Nullable
      private JsonElement request00() {
         this.debug("Sending request to {}", this.connection.getURL());

         try {
            return this.connectionStreamToJson(false);
         } catch (Throwable var6) {
            Throwable var1 = var6;

            MojangAPIException var2;
            try {
               switch(this.connection.getResponseCode()) {
               case 404:
                  return null;
               case 429:
                  String var3 = MinecraftClient.this.rateLimiter.toString();
                  MinecraftClient.this.rateLimiter.instantRateLimit();
                  throw new MojangAPIRetryException(MojangAPIRetryException.Reason.RATELIMITED, "Rate limit has been hit (server confirmed): " + var3 + " -> " + var3 + MinecraftClient.totalReq());
               default:
                  if (var1 instanceof SocketException && var1.getMessage().toLowerCase(Locale.ENGLISH).contains("connection reset")) {
                     throw new MojangAPIRetryException(MojangAPIRetryException.Reason.CONNECTION_RESET, "Connection was closed", var1);
                  }

                  JsonElement var7 = this.connectionStreamToJson(true);
                  var2 = new MojangAPIException(var7 == null ? "[NO ERROR RESPONSE]" : var7.toString(), var1);
               }
            } catch (MojangAPIRetryException var4) {
               throw var4;
            } catch (Throwable var5) {
               var2 = new MojangAPIException("Failed to read both normal response and error response from '" + this.connection.getURL() + '\'');
               var2.addSuppressed(var6);
               var2.addSuppressed(var5);
            }

            throw var2;
         }
      }

      private JsonElement connectionStreamToJson(boolean var1) {
         InputStream var2 = var1 ? this.connection.getErrorStream() : this.connection.getInputStream();

         JsonReader var3;
         label89: {
            JsonElement var6;
            try {
               if (var1 && var2 == null) {
                  var3 = null;
                  break label89;
               }

               var3 = new JsonReader(new InputStreamReader(var2, StandardCharsets.UTF_8));

               try {
                  Exception var4 = null;

                  JsonElement var5;
                  try {
                     var5 = Streams.parse(var3);
                  } catch (Exception var9) {
                     var4 = var9;
                     var5 = null;
                  }

                  if (var5 == null) {
                     String var12 = CharStreams.toString(new InputStreamReader(var1 ? this.connection.getErrorStream() : this.connection.getInputStream(), Charsets.UTF_8));
                     throw new IllegalStateException((var1 ? "error response" : "normal response") + " is not a JSON object '" + this.connection.getResponseCode() + " - " + this.connection.getResponseMessage() + "': " + var12, var4);
                  }

                  var6 = var5;
               } catch (Throwable var10) {
                  try {
                     var3.close();
                  } catch (Throwable var8) {
                     var10.addSuppressed(var8);
                  }

                  throw var10;
               }

               var3.close();
            } catch (Throwable var11) {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var7) {
                     var11.addSuppressed(var7);
                  }
               }

               throw var11;
            }

            if (var2 != null) {
               var2.close();
            }

            return var6;
         }

         if (var2 != null) {
            var2.close();
         }

         return var3;
      }
   }
}
