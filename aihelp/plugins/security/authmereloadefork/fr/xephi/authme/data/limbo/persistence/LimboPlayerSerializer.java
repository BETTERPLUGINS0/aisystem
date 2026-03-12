package fr.xephi.authme.data.limbo.persistence;

import fr.xephi.authme.data.limbo.LimboPlayer;
import fr.xephi.authme.libs.com.google.gson.Gson;
import fr.xephi.authme.libs.com.google.gson.JsonArray;
import fr.xephi.authme.libs.com.google.gson.JsonElement;
import fr.xephi.authme.libs.com.google.gson.JsonObject;
import fr.xephi.authme.libs.com.google.gson.JsonSerializationContext;
import fr.xephi.authme.libs.com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.bukkit.Location;

class LimboPlayerSerializer implements JsonSerializer<LimboPlayer> {
   static final String LOCATION = "location";
   static final String LOC_WORLD = "world";
   static final String LOC_X = "x";
   static final String LOC_Y = "y";
   static final String LOC_Z = "z";
   static final String LOC_YAW = "yaw";
   static final String LOC_PITCH = "pitch";
   static final String GROUPS = "groups";
   static final String IS_OP = "operator";
   static final String CAN_FLY = "can-fly";
   static final String WALK_SPEED = "walk-speed";
   static final String FLY_SPEED = "fly-speed";
   private static final Gson GSON = new Gson();

   public JsonElement serialize(LimboPlayer limboPlayer, Type type, JsonSerializationContext context) {
      Location loc = limboPlayer.getLocation();
      JsonObject locationObject = new JsonObject();
      locationObject.addProperty("world", loc.getWorld().getName());
      locationObject.addProperty("x", (Number)loc.getX());
      locationObject.addProperty("y", (Number)loc.getY());
      locationObject.addProperty("z", (Number)loc.getZ());
      locationObject.addProperty("yaw", (Number)loc.getYaw());
      locationObject.addProperty("pitch", (Number)loc.getPitch());
      JsonObject obj = new JsonObject();
      obj.add("location", locationObject);
      List<JsonObject> groups = (List)limboPlayer.getGroups().stream().map((g) -> {
         JsonObject jsonGroup = new JsonObject();
         jsonGroup.addProperty("groupName", g.getGroupName());
         if (g.getContextMap() != null) {
            jsonGroup.addProperty("contextMap", GSON.toJson((Object)g.getContextMap()));
         }

         return jsonGroup;
      }).collect(Collectors.toList());
      JsonArray jsonGroups = new JsonArray();
      Objects.requireNonNull(jsonGroups);
      groups.forEach(jsonGroups::add);
      obj.add("groups", jsonGroups);
      obj.addProperty("operator", limboPlayer.isOperator());
      obj.addProperty("can-fly", limboPlayer.isCanFly());
      obj.addProperty("walk-speed", (Number)limboPlayer.getWalkSpeed());
      obj.addProperty("fly-speed", (Number)limboPlayer.getFlySpeed());
      return obj;
   }
}
