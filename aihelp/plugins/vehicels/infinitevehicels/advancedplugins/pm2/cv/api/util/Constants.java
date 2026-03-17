package advancedplugins.pm2.cv.api.util;

import advancedplugins.pm2.cv.api.InfiniteVehicles;
import java.io.File;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.NamespacedKey;

public class Constants {
   public static final String VALID_ID_PATTERN = "[a-z0-9\\-_.]+";
   public static final String VALID_NAME_PATTERN = "[a-zA-Z0-9\\s\\-_.]+";
   public static final String VALID_STATE_NAME_PATTERN = "[a-z0-9/._-]+";

   public static class Pivot {
      public static final Vector3D ARMOR_STAND_HEAD_ROTATION_PIVOT = new Vector3D(0.0D, 1.4375D, 0.0D);
      public static final Vector3D SMALL_ARMOR_STAND_HEAD_ROTATION_PIVOT;
      public static final Vector3D ARMOR_STAND_LEFT_ARM_ROTATION_PIVOT;
      public static final Vector3D ARMOR_STAND_LEFT_HAND_BOX_ROTATION_PIVOT;
      public static final Vector3D SMALL_ARMOR_STAND_LEFT_ARM_ROTATION_PIVOT;
      public static final Vector3D SMALL_ARMOR_STAND_LEFT_HAND_BOX_ROTATION_PIVOT;
      public static final Vector3D ARMOR_STAND_RIGHT_ARM_ROTATION_PIVOT;
      public static final Vector3D ARMOR_STAND_RIGHT_HAND_BOX_ROTATION_PIVOT;
      public static final Vector3D SMALL_ARMOR_STAND_RIGHT_ARM_ROTATION_PIVOT;
      public static final Vector3D SMALL_ARMOR_STAND_RIGHT_HAND_BOX_ROTATION_PIVOT;

      static {
         SMALL_ARMOR_STAND_HEAD_ROTATION_PIVOT = ARMOR_STAND_HEAD_ROTATION_PIVOT.scalarMultiply(0.5D);
         ARMOR_STAND_LEFT_ARM_ROTATION_PIVOT = new Vector3D(0.3125D, 1.375D, 0.0D);
         ARMOR_STAND_LEFT_HAND_BOX_ROTATION_PIVOT = new Vector3D(0.375D, 0.0D, 0.125D);
         SMALL_ARMOR_STAND_LEFT_ARM_ROTATION_PIVOT = ARMOR_STAND_LEFT_ARM_ROTATION_PIVOT.scalarMultiply(0.5D);
         SMALL_ARMOR_STAND_LEFT_HAND_BOX_ROTATION_PIVOT = ARMOR_STAND_LEFT_HAND_BOX_ROTATION_PIVOT.scalarMultiply(0.5D);
         ARMOR_STAND_RIGHT_ARM_ROTATION_PIVOT = new Vector3D(-0.3125D, 1.375D, 0.0D);
         ARMOR_STAND_RIGHT_HAND_BOX_ROTATION_PIVOT = new Vector3D(-0.375D, 0.0D, 0.125D);
         SMALL_ARMOR_STAND_RIGHT_ARM_ROTATION_PIVOT = ARMOR_STAND_RIGHT_ARM_ROTATION_PIVOT.scalarMultiply(0.5D);
         SMALL_ARMOR_STAND_RIGHT_HAND_BOX_ROTATION_PIVOT = ARMOR_STAND_RIGHT_HAND_BOX_ROTATION_PIVOT.scalarMultiply(0.5D);
      }
   }

   public static class Dimensions {
      public static final double FANCY_PROJECTION_SCALE = 0.0625D;
      public static final double FANCY_NAME_PROJECTION_SCALE = 0.0270586D;
      public static final double ARMOR_STAND_HEIGHT = 1.875D;
      public static final double SMALL_ARMOR_STAND_HEIGHT = 0.9375D;
      public static final double ARMOR_STAND_HEAD_BOX_SIZE = 0.625D;
      public static final double SMALL_ARMOR_STAND_HEAD_BOX_SIZE = 0.40625D;
      public static final double ARMOR_STAND_HAND_BOX_SIZE = 0.390625D;
      public static final double SMALL_ARMOR_STAND_HAND_BOX_SIZE = 0.25390625D;
      public static final double ARMOR_STAND_NAME_BOX_HEIGHT = 0.285D;
   }

   public static class HitBox {
      public static final double MIN_HIT_BOX_SIZE = 0.5D;
   }

   public static class NamespacedKeys {
      public static final NamespacedKey ITEM_ID = new NamespacedKey(InfiniteVehicles.getPlugin(), "infinite-item-id");
      public static final NamespacedKey UNIQUE_ID = new NamespacedKey(InfiniteVehicles.getPlugin(), "infinite-unique-id");
      public static final NamespacedKey VEHICLE_ID = new NamespacedKey(InfiniteVehicles.getPlugin(), "infinite-vehicle-id");
      public static final NamespacedKey LEGAL_ITEM_AMOUNT = new NamespacedKey(InfiniteVehicles.getPlugin(), "infinite-legal-item-amount");
   }

   public static class Key {
      public static final String VALUE = "value";
      public static final String EXTRA_DATA = "extra-data";
      public static final String CALC_TYPE = "calc-type";
      public static final String INVERTED = "inverted";
      public static final String ID = "id";
      public static final String IDENTIFIER = "identifier";
      public static final String NAME = "name";
      public static final String DISPLAY_NAME = "display-name";
      public static final String TYPE = "type";
      public static final String MODEL = "model";
      public static final String DATA = "data";
      public static final String DESCRIPTION = "description";
      public static final String ITEM = "item";
      public static final String SPAWN_RELATIVE = "spawn-relative-to-vehicle";
      public static final String PICKUP_ITEM = "pickup-item";
      public static final String ADMIN_PICKUP_ITEM = "admin-pickup-item";
      public static final String RESULT = "result";
      public static final String SHAPED = "shaped";
      public static final String SHAPELESS = "shapeless";
      public static final String EXIT_SHORTCUT = "exit-shortcut";
      public static final String OPERATOR_EXIT_SHORTCUT = nodes("exit-shortcut", "operator");
      public static final String PASSENGER_EXIT_SHORTCUT = nodes("exit-shortcut", "passenger");
      public static final String X = "x";
      public static final String Y = "y";
      public static final String Z = "z";
      public static final String WIDTH = "width";
      public static final String HEIGHT = "height";
      public static final String DEPTH = "depth";
      public static final String HIT_BOX = "hitbox";
      public static final String PARTS = "parts";
      public static final String MODEL_ID = "model-id";
      public static final String MODEL_OFFSET = "model-offset";
      public static final String MODEL_INTERPOLATION = "model-interpolation";
      public static final String BONES = "bones";
      public static final String RIG = "rig";
      public static final String SEATS = "seats";
      public static final String PROJECTILE_SHOOTER = "projectile-shooters";
      public static final String PARTICLE = "particle";
      public static final String PARTICLES = "particles";
      public static final String SOUNDS = "sounds";
      public static final String SOUND = "sound";
      public static final String MAIN = "main";
      public static final String TEXTURES = "textures";
      public static final String MATERIAL = "material";
      public static final String BLOCK_DATA = "block-data";
      public static final String CUSTOM_MODEL_DATA = "custom-model-data";
      public static final String CUSTOM_HEAD_TEXTURE = "custom-head-texture";
      public static final String HEAD_TEXTURE = "head-texture";
      public static final String SMALL = "small";
      public static final String ROTATION = "rotation";
      public static final String OFFSET = "offset";
      public static final String SCALE = "scale";
      public static final String PIVOT = "pivot";
      public static final String PARENT = "parent";
      public static final String CHILDREN = "children";
      public static final String ANIMATIONS = "animations";
      public static final String KEYFRAMES = "keyframes";
      public static final String DURATION = "duration";
      public static final String INTERPOLATION_MODE = "interpolation-mode";
      public static final String LOOP_MODE = "loop-mode";
      public static final String TRANSLATIONS = "translations";
      public static final String ROTATIONS = "rotations";
      public static final String SCALES = "scales";
      public static final String CATEGORY = "category";
      public static final String DELAY = "delay";
      public static final String VOLUME = "volume";
      public static final String PITCH = "pitch";
      public static final String YAW = "yaw";
      public static final String GLOBAL = "global";
      public static final String COUNT = "count";
      public static final String DISPERSION = "dispersion";
      public static final String PHYSICS = "physics";
      public static final String CONTROLLERS = "controllers";
      public static final String PROPERTIES = "properties";
      public static final String HEALTH = "health";
      public static final String DAMAGE = "damage";
      public static final String LEADERBOARD = "leaderboard";
      public static final String EXPLOSION = "explosion";
      public static final String DESTROY = "destroy";
      public static final String FLOATS = "floats";
      public static final String GRAVITY = "gravity";
      public static final String MAXIMUM = "maximum";
      public static final String ACCELERATION = "acceleration";
      public static final String FRICTION = "friction";
      public static final String CLIMBING = "climbing";
      public static final String AIR = "air";
      public static final String ON_UNKNOWN = "on-unknown";
      public static final String ON_SOLID = "on-solid";
      public static final String ON_DUSTY = "on-dusty";
      public static final String ON_SNOWY = "on-snowy";
      public static final String ON_SLIPPERY = "on-slippery";
      public static final String ON_WATER = "on-water";
      public static final String ON_LAVA = "on-lava";
      public static final String THROUGH_WATER = "through-water";
      public static final String THROUGH_LAVA = "through-lava";
      public static final String STATES_TO_APPLY = "states-to-apply";
      public static final String DISABLE = "disable";
      public static final String MAX_HEALTH = "max-health";
      public static final String GRAVITY_MAXIMUM = nodes("gravity", "maximum");
      public static final String GRAVITY_ACCELERATION = nodes("gravity", "acceleration");
      public static final String FRICTION_AIR = nodes("friction", "air");
      public static final String FRICTION_ON_UNKNOWN = nodes("friction", "on-unknown");
      public static final String FRICTION_ON_SOLID = nodes("friction", "on-solid");
      public static final String FRICTION_ON_DUSTY = nodes("friction", "on-dusty");
      public static final String FRICTION_ON_SNOWY = nodes("friction", "on-snowy");
      public static final String FRICTION_ON_SLIPPERY = nodes("friction", "on-slippery");
      public static final String FRICTION_ON_WATER = nodes("friction", "on-water");
      public static final String FRICTION_ON_LAVA = nodes("friction", "on-lava");
      public static final String FRICTION_THROUGH_WATER = nodes("friction", "through-water");
      public static final String FRICTION_THROUGH_LAVA = nodes("friction", "through-lava");
      public static final String BLOCK_CLIMB_CAPACITY = nodes("climbing", "block-climb-capacity");
      public static final String VEHICLE_ID = "vehicle-id";
      public static final String FUEL = "fuel";
      public static final String FUEL_AMOUNT = "fuel-amount";
      public static final String ACTION = "action";
      public static final String CAPACITY = "capacity";
      public static final String MIN_CONSUMPTION = "min-consumption";
      public static final String MAX_CONSUMPTION = "max-consumption";
      public static final String ITEM_STORAGE_SLOTS = "item-storage-slots";
      public static final String MODIFIERS = "modifiers";
      public static final String EFFECTS = "effects";
      public static final String DAMAGE_EFFECT = nodes("effects", "damage");
      public static final String DESTROY_EFFECT = nodes("effects", "destroy");
      public static final String UPGRADE = "upgrade";
      public static final String SLOT = "slot";
      public static final String SEAT_INDEX = "seat-index";
      public static final String TITLE = "title";
      public static final String ROWS = "rows";
      public static final String REPAIR_ITEM = "repair-item";
      public static final String FUEL_DISPLAY_ITEM = "fuel-display-item";
      public static final String SEATS_GUI_ITEM = "seats-gui-item";
      public static final String SEAT_ITEM = "seat-item";
      public static final String OPERATOR_SEAT_ITEM = "operator-seat-item";
      public static final String INVENTORY_ITEM = "inventory-item";
      public static final String EXPLOSION_STRENGTH = "explosion-strength";
      public static final String PARTICLE_BEAM = "particle-beam";
      public static final String PARTICLE_GRAVITY = "particle-gravity";
      public static final String BLOCK = "block";
      public static final String ENTITY = "entity";
      public static final String WORLD = "world";
      public static final String IMPACT_TYPE = "impact-type";
      public static final String Y_FORCE = "y-force";
      public static final String PIERCE_AMOUNT = "pierce-amount";
      public static final String PARTICLE_SPEED = "particle-speed";
      public static final String PARTICLE_AMOUNT = "particle-amount";
      public static final String PARTICLE_DISTANCE = "particle-distance-blocks";
      public static final String VELOCITY_MULTI = "velocity-multiplier";
      public static final String SHOOT_BIND = "shoot-bind";
      public static final String SHOOT_SECONDARY_BIND = "shoot-secondary-bind";
      public static final String SHOOT_COOLDOWN = "shoot-cooldown";
      public static final String PLACEMENT_CONFIGURATION = "placement";
      public static final String WORLDS = "worlds";
      public static final String WHITELIST = "whitelist";
      public static final String BLACKLIST = "blacklist";
      public static final String ADD_ON_EXIT = "add-on-exit";
      public static final String ADD_ON_PLACE = "add-on-place";
      public static final String PLACE_LIMIT = "place-limit";
      public static final String PASS_THROUGH_DAMAGE = "pass-through-damage";
      public static final String REPAIR_CONFIGURATION = "repair";

      public static String nodes(String... var0) {
         StringBuilder var1 = new StringBuilder();

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1.append(var0[var2]);
            if (var2 < var0.length - 1) {
               var1.append(".");
            }
         }

         return var1.toString();
      }

      public static class LeaderboardMenu {
         public static final String TITLE = "title";
         public static final String ROWS = "rows";
         public static final String EXIT_ITEM = "exit-item";
         public static final String PLAYER_HEAD_ITEM = "player-head-item";
         public static final String SLOTS = "slots";
         public static final String PREVIOUS_ITEM = "previous-item";
         public static final String NEXT_ITEM = "next-item";
      }
   }

   public static class Files {
      public static final File ITEMS_FOLDER = new File(InfiniteVehicles.getPlugin().getDataFolder(), "item");
      public static final File VEHICLES_FOLDER = new File(InfiniteVehicles.getPlugin().getDataFolder(), "vehicle");
      public static final File VEHICLE_MODELS_FOLDER = new File(InfiniteVehicles.getPlugin().getDataFolder(), "model");
      public static final File VEHICLE_CONTROLLERS_FOLDER = new File(InfiniteVehicles.getPlugin().getDataFolder(), "controllers");
      public static final File UPGRADES_FOLDER = new File(InfiniteVehicles.getPlugin().getDataFolder(), "upgrade");
      public static final File RECIPES_CONFIGURATION_FILE = new File(InfiniteVehicles.getPlugin().getDataFolder(), "RecipesConfiguration.yml");

      public static void mkdirs() {
         VEHICLES_FOLDER.mkdirs();
         VEHICLE_MODELS_FOLDER.mkdirs();
         VEHICLE_CONTROLLERS_FOLDER.mkdirs();
      }
   }
}
