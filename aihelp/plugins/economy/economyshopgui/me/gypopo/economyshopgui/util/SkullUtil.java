package me.gypopo.economyshopgui.util;

import com.google.common.collect.LinkedHashMultimap;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.SkullCache;
import me.gypopo.economyshopgui.methodes.SendMessage;
import net.minecraft.world.item.component.ResolvableProfile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SkullUtil {
   private static final String VALUE_PROPERTY = "{\"textures\":{\"SKIN\":";
   private static EconomyShopGUI plugin;
   private static SkullCache SKULL_CACHE;
   private static Field VALUE;
   private static Field MODERN_PROFILE = null;
   private static Method PROFILE_SETTER = null;
   private static Field PROFILE;
   private static Field VAL;
   private static Field SIG;
   private static Field propertyMap;
   private static Method createResolved;
   private static boolean rateLimit = false;
   private static final Function<GameProfile, PropertyMap> propertiesMethod = getPropertiesMethod();
   private static final Function<GameProfile, String> nameMethod = getNameMethod();
   private static Class<?> profileClass;

   public static PropertyMap getProperties(GameProfile profile) {
      return (PropertyMap)propertiesMethod.apply(profile);
   }

   public static String getName(GameProfile profile) {
      return (String)nameMethod.apply(profile);
   }

   public SkullUtil(EconomyShopGUI plugin) {
      SKULL_CACHE = new SkullCache(plugin);
      SkullUtil.plugin = plugin;
   }

   public void saveCache() {
      SKULL_CACHE.write();
   }

   private static String getPost(String url) throws Exception {
      URL link = new URL(url);
      BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));
      StringBuilder builder = new StringBuilder();
      Iterator var4 = ((List)in.lines().collect(Collectors.toList())).iterator();

      while(var4.hasNext()) {
         String s = (String)var4.next();
         builder.append(s);
      }

      in.close();
      return builder.toString().replaceAll(" ", "");
   }

   private static void cache(Consumer<String> onComplete, ItemStack item, String name) {
      if (!name.equals("CS-CoreLib")) {
         try {
            String result = getPost("https://api.minecraftservices.com/minecraft/profile/lookup/name/" + name);
            String uuid = (String)((JSONObject)(new JSONParser()).parse(result)).get("id");
            String s = getPost("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            JSONObject property = (JSONObject)((JSONArray)((JSONObject)(new JSONParser()).parse(s)).get("properties")).get(0);
            onComplete.accept(getTextureURL(JsonUtil.getString(property, "value")));
         } catch (FileNotFoundException var7) {
            onComplete.accept(" ");
         } catch (SocketException var8) {
            SendMessage.logDebugMessage("Socket exception occurred while trying to retrieve skull texture for player '" + name + "', trying again in 5 seconds...");
            plugin.runTaskLaterAsync(() -> {
               cache(onComplete, item, name);
            }, 100L);
         } catch (UnknownHostException var9) {
         } catch (IOException var10) {
            if (var10.getMessage().contains("429")) {
               if (!rateLimit) {
                  SendMessage.logDebugMessage("Mojang started to rate limit our connections while trying to retrieve a skull texture, trying again in 60 seconds...");
                  rateLimit = true;
               }

               plugin.runTaskLaterAsync(() -> {
                  cache(onComplete, item, name);
                  rateLimit = false;
               }, 1200L);
            } else {
               SendMessage.warnMessage("Failed to retrieve skull texture for player '" + name + "'");
               var10.printStackTrace();
            }
         } catch (Exception var11) {
            SendMessage.warnMessage("Failed to retrieve skull texture for player '" + name + "'");
            var11.printStackTrace();
         }

      }
   }

   public static String getPlayerSkullTexture(Object data) {
      try {
         String v = new String(Base64.getDecoder().decode((String)VALUE.get(data)));
         JsonObject json = ((JsonObject)(new Gson()).fromJson(v, JsonObject.class)).get("textures").getAsJsonObject();
         json.get("SKIN").getAsJsonObject().remove("metadata");
         return Base64.getEncoder().encodeToString(("{\"textures\":" + json + "}").getBytes());
      } catch (Exception var3) {
         SendMessage.errorMessage("Failed to get skull texture from item");
         var3.printStackTrace();
         return null;
      }
   }

   public static String getSkullTexture(SkullMeta meta) {
      try {
         return getSkullTexture(getProfile(meta));
      } catch (Exception var2) {
         SendMessage.errorMessage("Failed to get skull texture from item");
         var2.printStackTrace();
         return null;
      }
   }

   private static GameProfile getProfile(SkullMeta meta) throws Exception {
      Object profile = PROFILE.get(meta);
      return (GameProfile)(profileClass == GameProfile.class ? profile : MODERN_PROFILE.get(profile));
   }

   private static String getSkullTexture(GameProfile profile) throws Exception {
      if (profile != null && !getProperties(profile).get("textures").isEmpty()) {
         Iterator var1 = getProperties(profile).get("textures").iterator();

         while(var1.hasNext()) {
            Property property = (Property)var1.next();
            String value = getValue(property);
            if (!value.isEmpty()) {
               return value;
            }
         }
      }

      return null;
   }

   public static String getTextureURL(String raw) {
      String json = new String(Base64.getDecoder().decode(raw));

      try {
         JSONObject data = (JSONObject)(new JSONParser()).parse(json);
         String url = ((JSONObject)data.get("textures")).get("SKIN").toString().replace("\\", "").replace("https://", "http://");
         return encodeTexture(url);
      } catch (ParseException var4) {
         return "";
      } catch (Exception var5) {
         SendMessage.errorMessage("Failed to get skull texture from item: " + json);
         var5.printStackTrace();
         return null;
      }
   }

   private static String encodeTexture(String url) {
      return Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":" + url + "}}").getBytes());
   }

   public static boolean isSimilarSkull(ItemStack item1, ItemStack item2) {
      return getSkullTexture((SkullMeta)item1.getItemMeta()).equals(getSkullTexture((SkullMeta)item2.getItemMeta()));
   }

   public static void setSkullTexture(ItemStack stack, SkullMeta meta, String name, boolean async) {
      String texture = SKULL_CACHE.getSkull(name);
      if (texture == null) {
         Consumer<String> onComplete = (s) -> {
            SKULL_CACHE.cacheSkull(name, s);
            plugin.runTaskLater(() -> {
               SkullMeta sm = (SkullMeta)stack.getItemMeta();
               setSkullTexture(sm, name, s);
               stack.setItemMeta(sm);
            }, 1L);
         };
         if (async) {
            plugin.runTaskLaterAsync(() -> {
               cache(onComplete, stack, name);
            }, 1L);
         } else {
            cache(onComplete, stack, name);
         }
      } else if (meta == null) {
         SkullMeta sm = (SkullMeta)stack.getItemMeta();
         setSkullTexture(sm, name, texture);
         stack.setItemMeta(sm);
      } else {
         setSkullTexture(meta, name, texture);
      }

   }

   public static void setSkullTexture(Consumer<GameProfile> onComplete, ItemStack stack, SkullMeta meta, String name) {
      String texture = SKULL_CACHE.getSkull(name);
      if (texture == null) {
         plugin.runTaskLaterAsync(() -> {
            cache((s) -> {
               SKULL_CACHE.cacheSkull(name, s);
               onComplete.accept(getProfile(name, s));
            }, stack, name);
         }, 1L);
      } else if (meta == null) {
         SkullMeta sm = (SkullMeta)stack.getItemMeta();
         setSkullTexture(sm, name, texture);
         stack.setItemMeta(sm);
      } else {
         setSkullTexture(meta, name, texture);
      }

   }

   private static void setSkullTexture(SkullMeta meta, String name, String texture) {
      applySkullProfile(getProfile(name, texture), meta);
   }

   private static GameProfile getProfile(String name, String texture) {
      GameProfile profile = createNewProfile(UUID.nameUUIDFromBytes(texture.getBytes(StandardCharsets.UTF_8)), name);
      getProperties(profile).put("textures", new Property("textures", texture));
      return profile;
   }

   public static void applySkullProfile(Object profile, SkullMeta sm) {
      try {
         if (PROFILE_SETTER != null) {
            PROFILE_SETTER.invoke(sm, profileClass == GameProfile.class ? profile : getModernProfile((GameProfile)profile));
         } else {
            PROFILE.set(sm, profile);
         }
      } catch (IllegalAccessException | IllegalArgumentException | ExceptionInInitializerError | InvocationTargetException var3) {
         SendMessage.logDebugMessage("Failed to create skull texture");
         var3.printStackTrace();
      }

   }

   public static void updateTexture(Player p) {
      String texture = null;

      try {
         Method m = p.getClass().getDeclaredMethod(plugin.version > 117 ? "getPlayerProfile" : "getProfile");
         m.setAccessible(true);
         if (plugin.version > 117) {
            String url = ((PlayerProfile)m.invoke(p)).getTextures().getSkin().toString();
            if (url == null) {
               return;
            }

            texture = encodeTexture("{\"url\":\"" + url + "\"}");
         } else {
            texture = getSkullTexture((GameProfile)m.invoke(p));
            if (texture == null) {
               return;
            }

            texture = getTextureURL(texture);
         }
      } catch (Exception var5) {
      }

      if (texture != null) {
         SkullCache.PlayerTexture cached = SKULL_CACHE.getTexture(p.getName());
         if (cached == null) {
            SKULL_CACHE.cacheSkull(p.getName(), texture);
         } else {
            try {
               if (!texture.equalsIgnoreCase(cached.getTexture())) {
                  SendMessage.logDebugMessage("Detected skin change of " + p.getName() + ", updating cached skin...");
                  SKULL_CACHE.cacheSkull(p.getName(), texture);
               }
            } catch (ArrayIndexOutOfBoundsException var4) {
            }

         }
      }
   }

   private static String getValue(Property prop) {
      try {
         return (String)VAL.get(prop);
      } catch (IllegalAccessException var2) {
         SendMessage.warnMessage("A error occurred while reading skull");
         var2.printStackTrace();
         return "";
      }
   }

   private static GameProfile createNewProfile(UUID uuid, String name) {
      if (ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_21_R6)) {
         try {
            PropertyMap properties = new PropertyMap(LinkedHashMultimap.create());
            propertyMap.set(properties, LinkedHashMultimap.create());
            return new GameProfile(uuid, name, properties);
         } catch (IllegalAccessException var3) {
            SendMessage.errorMessage("Failed to create modern player profile properties map");
            throw new RuntimeException(var3);
         }
      } else {
         return new GameProfile(uuid, name);
      }
   }

   private static ResolvableProfile getModernProfile(GameProfile profile) {
      try {
         return ServerInfo.getVersion().newerOrEqualAs(ServerInfo.Version.v1_21_R6) ? (ResolvableProfile)createResolved.invoke((Object)null, profile) : (ResolvableProfile)ResolvableProfile.class.getDeclaredConstructor(GameProfile.class).newInstance(profile);
      } catch (Exception var2) {
         SendMessage.errorMessage("Failed to create modern player profile instance");
         throw new RuntimeException(var2);
      }
   }

   private static Function<GameProfile, PropertyMap> getPropertiesMethod() {
      try {
         Method method = GameProfile.class.getMethod("getProperties");
         return (profile) -> {
            try {
               return (PropertyMap)method.invoke(profile);
            } catch (Exception var3) {
               throw new RuntimeException(var3);
            }
         };
      } catch (NoSuchMethodException var1) {
         return GameProfile::properties;
      }
   }

   private static Function<GameProfile, String> getNameMethod() {
      try {
         Method method = GameProfile.class.getMethod("getName");
         return (profile) -> {
            try {
               return (String)method.invoke(profile);
            } catch (Exception var3) {
               throw new RuntimeException(var3);
            }
         };
      } catch (NoSuchMethodException var1) {
         return GameProfile::name;
      }
   }

   static {
      SkullMeta meta = (SkullMeta)(new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial())).getItemMeta();

      try {
         if (meta.getClass().getDeclaredField("profile").getType() == GameProfile.class) {
            throw new NoSuchFieldException();
         }

         profileClass = Class.forName("net.minecraft.world.item.component.ResolvableProfile");
         Field[] var1 = profileClass.getDeclaredFields();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Field field = var1[var3];
            if (field.getType().equals(GameProfile.class)) {
               MODERN_PROFILE = field;
               field.setAccessible(true);
               break;
            }
         }
      } catch (SecurityException | ClassNotFoundException | LinkageError | NoSuchFieldException var13) {
         profileClass = GameProfile.class;
      }

      try {
         PROFILE_SETTER = meta.getClass().getDeclaredMethod("setProfile", profileClass);
         PROFILE_SETTER.setAccessible(true);
      } catch (SecurityException | NoSuchMethodException var12) {
      }

      try {
         createResolved = Class.forName("net.minecraft.world.item.component.ResolvableProfile").getDeclaredMethod(ServerInfo.usesMojangMappings() ? "createResolved" : "a", GameProfile.class);
         createResolved.setAccessible(true);
      } catch (NoSuchMethodException var10) {
         try {
            createResolved = Class.forName("net.minecraft.world.item.component.ResolvableProfile").getDeclaredMethod("a", GameProfile.class);
            createResolved.setAccessible(true);
         } catch (NoSuchMethodException | ClassNotFoundException var9) {
         }
      } catch (ClassNotFoundException var11) {
      }

      try {
         PROFILE = meta.getClass().getDeclaredField("profile");
         PROFILE.setAccessible(true);
      } catch (SecurityException | NoSuchFieldException var8) {
         var8.printStackTrace();
      }

      Property property = new Property("", "");

      try {
         VAL = property.getClass().getDeclaredField("value");
         VAL.setAccessible(true);
         SIG = property.getClass().getDeclaredField("signature");
         SIG.setAccessible(true);
      } catch (SecurityException | NoSuchFieldException var7) {
         var7.printStackTrace();
      }

      try {
         Class<?> map = Class.forName("com.mojang.authlib.properties.PropertyMap");
         propertyMap = map.getDeclaredField("properties");
         propertyMap.setAccessible(true);
         Class<?> c = Class.forName("org.geysermc.floodgate.skin.SkinDataImpl");
         VALUE = c.getDeclaredField("value");
         VALUE.setAccessible(true);
      } catch (SecurityException | NoSuchFieldException var5) {
         var5.printStackTrace();
      } catch (ClassNotFoundException var6) {
      }

   }
}
