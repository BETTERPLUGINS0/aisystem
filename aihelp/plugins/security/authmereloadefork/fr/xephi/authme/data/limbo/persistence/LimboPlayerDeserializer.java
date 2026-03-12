package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.data.limbo.UserGroup;
import fr.xephi.authme.libs.com.google.common.reflect.TypeToken;
import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.JsonArray;
import fr.xephi.authme.libs.com.google.gson.JsonDeserializationContext;
import fr.xephi.authme.libs.com.google.gson.JsonDeserializer;
import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.com.google.gson.JsonObject;
import fr.xephi.authme.service.BukkitService;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.bukkit.Location;
import org.bukkit.World;

class LimboPlayerDeserializer implements JsonDeserializer<LimboPlayer> {
   private static final String GROUP_LEGACY = "group";
   private static final String CONTEXT_MAP = "contextMap";
   private static final String GROUP_NAME = "groupName";
   private BukkitService bukkitService;

   LimboPlayerDeserializer(BukkitService bukkitService) {
      this.bukkitService = bukkitService;
   }

   public LimboPlayer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      if (jsonObject == null) {
         return null;
      } else {
         Location loc = this.deserializeLocation(jsonObject);
         boolean operator = getBoolean(jsonObject, "operator");
         Collection<UserGroup> groups = getLimboGroups(jsonObject);
         boolean canFly = getBoolean(jsonObject, "can-fly");
         float walkSpeed = getFloat(jsonObject, "walk-speed", 0.2F);
         float flySpeed = getFloat(jsonObject, "fly-speed", 0.1F);
         return new LimboPlayer(loc, operator, groups, canFly, walkSpeed, flySpeed);
      }
   }

   private Location deserializeLocation(JsonObject jsonObject) {
      JsonObject e;
      if ((e = jsonObject.getAsJsonObject("location")) != null) {
         JsonObject locationObject = e.getAsJsonObject();
         World world = this.bukkitService.getWorld(getString(locationObject, "world"));
         if (world != null) {
            double x = getDouble(locationObject, "x");
            double y = getDouble(locationObject, "y");
            double z = getDouble(locationObject, "z");
            float yaw = getFloat(locationObject, "yaw");
            float pitch = getFloat(locationObject, "pitch");
            return new Location(world, x, y, z, yaw, pitch);
         }
      }

      return null;
   }

   private static String getString(JsonObject jsonObject, String memberName) {
      JsonElement element = jsonObject.get(memberName);
      return element != null ? element.getAsString() : "";
   }

   private static List<UserGroup> getLimboGroups(JsonObject jsonObject) {
      JsonElement element = jsonObject.get("groups");
      if (element == null) {
         String legacyGroup = (String)Optional.ofNullable(jsonObject.get("group")).map(JsonElement::getAsString).orElse((Object)null);
         return legacyGroup == null ? Collections.emptyList() : Collections.singletonList(new UserGroup(legacyGroup, (Map)null));
      } else {
         List<UserGroup> result = new ArrayList();
         JsonArray jsonArray = element.getAsJsonArray();
         Iterator var4 = jsonArray.iterator();

         while(var4.hasNext()) {
            JsonElement arrayElement = (JsonElement)var4.next();
            if (!arrayElement.isJsonObject()) {
               result.add(new UserGroup(arrayElement.getAsString(), (Map)null));
            } else {
               JsonObject jsonGroup = arrayElement.getAsJsonObject();
               Map<String, String> contextMap = null;
               if (jsonGroup.has("contextMap")) {
                  JsonElement contextMapJson = jsonGroup.get("contextMap");
                  Type type = (new TypeToken<Map<String, String>>() {
                  }).getType();
                  contextMap = (Map)(new Gson()).fromJson(contextMapJson.getAsString(), type);
               }

               String groupName = jsonGroup.get("groupName").getAsString();
               result.add(new UserGroup(groupName, contextMap));
            }
         }

         return result;
      }
   }

   private static boolean getBoolean(JsonObject jsonObject, String memberName) {
      JsonElement element = jsonObject.get(memberName);
      return element != null && element.getAsBoolean();
   }

   private static float getFloat(JsonObject jsonObject, String memberName) {
      return (Float)getNumberFromElement(jsonObject.get(memberName), JsonElement::getAsFloat, 0.0F);
   }

   private static float getFloat(JsonObject jsonObject, String memberName, float defaultValue) {
      return (Float)getNumberFromElement(jsonObject.get(memberName), JsonElement::getAsFloat, defaultValue);
   }

   private static double getDouble(JsonObject jsonObject, String memberName) {
      return (Double)getNumberFromElement(jsonObject.get(memberName), JsonElement::getAsDouble, 0.0D);
   }

   private static <N extends Number> N getNumberFromElement(JsonElement jsonElement, Function<JsonElement, N> numberFunction, N defaultValue) {
      if (jsonElement != null) {
         try {
            return (Number)numberFunction.apply(jsonElement);
         } catch (NumberFormatException var4) {
         }
      }

      return defaultValue;
   }
}
