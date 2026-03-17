package advancedplugins.pm2.cv.models.api.utils;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.utils.data.GSONUtils;
import advancedplugins.pm2.cv.models.api.utils.future.Future;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.profile.PlayerTextures.SkinModel;
import org.jetbrains.annotations.NotNull;

public class MojangAPI {
   private static final HttpClient CLIENT = HttpClient.newHttpClient();
   private static final Map<String, UUID> UUID_CACHE = new ConcurrentHashMap();
   private static final Map<UUID, String> DATA_CACHE = new ConcurrentHashMap();

   private MojangAPI() {
      throw new IllegalStateException();
   }

   public static PlayerProfile fromBase64(@NotNull String var0) {
      PlayerProfile var1 = Bukkit.createProfile(UUID.randomUUID());
      String var2 = new String(Base64.getDecoder().decode(var0));
      JsonObject var3 = (JsonObject)ModelAPI.getAPI().getGson().fromJson(var2, JsonObject.class);
      GSONUtils.ifPresent(var3, "textures", (var1x) -> {
         GSONUtils.ifPresent(var1x, "SKIN", (var1xx) -> {
            String var2 = (String)GSONUtils.get(var1xx, "url", JsonElement::getAsString);
            if (var2 == null) {
               throw new NullPointerException("Skin URL cannot be null.");
            } else {
               JsonObject var3 = (JsonObject)GSONUtils.get(var1xx, "metadata", JsonElement::getAsJsonObject);
               boolean var4 = (Boolean)GSONUtils.get(var3, "model", (var0) -> {
                  return var0.getAsString().equals("slim");
               }, false);

               try {
                  PlayerTextures var5 = var1.getTextures();
                  var5.setSkin(new URL(var2), var4 ? SkinModel.SLIM : SkinModel.CLASSIC);
                  var1.setTextures(var5);
               } catch (MalformedURLException var6) {
                  throw new RuntimeException(var6);
               }
            }
         });
      });
      return var1;
   }

   public static PlayerProfile fromUUID(UUID var0) {
      String var1 = (String)DATA_CACHE.computeIfAbsent(var0, (var1x) -> {
         try {
            HttpRequest var2 = HttpRequest.newBuilder().uri(new URI("https://sessionserver.mojang.com/session/minecraft/profile/" + String.valueOf(var0))).timeout(Duration.of(30L, ChronoUnit.SECONDS)).GET().build();
            HttpClient var3 = HttpClient.newHttpClient();
            String var4 = (String)var3.sendAsync(var2, BodyHandlers.ofString()).thenApply(HttpResponse::body).get(30L, TimeUnit.SECONDS);
            JsonObject var5 = (JsonObject)ModelAPI.getAPI().getGson().fromJson(var4, JsonObject.class);
            return (String)GSONUtils.get(var5, (String)"properties", (Function)((var0x) -> {
               JsonArray var1 = var0x.getAsJsonArray();
               return var1.isEmpty() ? null : (String)GSONUtils.get(var1.get(0), "value", JsonElement::getAsString);
            }));
         } catch (Throwable var6) {
            throw new RuntimeException(var6);
         }
      });
      return var1 == null ? null : fromBase64(var1);
   }

   public static UUID getUUIDFromUsername(String var0) {
      return (UUID)UUID_CACHE.computeIfAbsent(var0, (var1) -> {
         try {
            long var2 = System.currentTimeMillis() / 1000L;
            HttpRequest var4 = HttpRequest.newBuilder().uri(new URI("https://api.mojang.com/users/profiles/minecraft/" + var0 + "?at=" + var2)).timeout(Duration.of(30L, ChronoUnit.SECONDS)).GET().build();
            String var5 = (String)CLIENT.sendAsync(var4, BodyHandlers.ofString()).thenApply(HttpResponse::body).get(30L, TimeUnit.SECONDS);
            JsonObject var6 = (JsonObject)ModelAPI.getAPI().getGson().fromJson(var5, JsonObject.class);
            return (UUID)GSONUtils.get(var6, (String)"id", (Function)((var0x) -> {
               return MathUtils.parseUUID(var0x.getAsString());
            }));
         } catch (Throwable var7) {
            throw new RuntimeException(var7);
         }
      });
   }

   public static Future<PlayerProfile> fromUUIDPromise(UUID var0) {
      return Future.supplyingAsync(() -> {
         return fromUUID(var0);
      });
   }

   public static Future<UUID> getUUIDFromUsernamePromise(String var0) {
      return Future.supplyingAsync(() -> {
         return getUUIDFromUsername(var0);
      });
   }
}
