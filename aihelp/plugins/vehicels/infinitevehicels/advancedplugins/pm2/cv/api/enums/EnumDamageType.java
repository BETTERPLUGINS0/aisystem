package advancedplugins.pm2.cv.api.enums;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@VersionSensible
public enum EnumDamageType {
   IN_FIRE("in_fire"),
   LIGHTNING_BOLT("lightning_bolt"),
   ON_FIRE("on_fire"),
   LAVA("lava"),
   HOT_FLOOR("hot_floor"),
   IN_WALL("in_wall"),
   CRAMMING("cramming"),
   DROWN("drown"),
   STARVE("starve"),
   CACTUS("cactus"),
   FALL("fall"),
   FLY_INTO_WALL("fly_into_wall"),
   OUT_OF_WORLD("out_of_world"),
   GENERIC("generic"),
   MAGIC("magic"),
   WITHER("wither"),
   DRAGON_BREATH("dragon_breath"),
   DRY_OUT("dry_out"),
   SWEET_BERRY_BUSH("sweet_berry_bush"),
   FREEZE("freeze"),
   STALAGMITE("stalagmite"),
   FALLING_BLOCK("falling_block"),
   FALLING_ANVIL("falling_anvil"),
   FALLING_STALACTITE("falling_stalactite"),
   STING("sting"),
   MOB_ATTACK("mob_attack"),
   MOB_ATTACK_NO_AGGRO("mob_attack_no_aggro"),
   PLAYER_ATTACK("player_attack"),
   ARROW("arrow"),
   TRIDENT("trident"),
   MOB_PROJECTILE("mob_projectile"),
   FIREWORKS("fireworks"),
   FIREBALL("fireball"),
   UNATTRIBUTED_FIREBALL("unattributed_fireball"),
   WITHER_SKULL("wither_skull"),
   THROWN("thrown"),
   INDIRECT_MAGIC("indirect_magic"),
   THORNS("thorns"),
   EXPLOSION("explosion"),
   PLAYER_EXPLOSION("player_explosion"),
   SONIC_BOOM("sonic_boom"),
   BAD_RESPAWN_POINT("bad_respawn_point"),
   GENERIC_KILL("generic_kill"),
   WEAPONS_MECHANICS("weapons_mechanics");

   @NotNull
   private final String key;

   @Nullable
   public static EnumDamageType match(@NotNull String var0) {
      EnumDamageType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EnumDamageType var4 = var1[var3];
         if (var4.key.equalsIgnoreCase(var0)) {
            return var4;
         }
      }

      return null;
   }

   private EnumDamageType(@NotNull String param3) {
      this.key = var3;
   }

   @NotNull
   public String getKey() {
      return this.key;
   }

   // $FF: synthetic method
   private static EnumDamageType[] $values() {
      return new EnumDamageType[]{IN_FIRE, LIGHTNING_BOLT, ON_FIRE, LAVA, HOT_FLOOR, IN_WALL, CRAMMING, DROWN, STARVE, CACTUS, FALL, FLY_INTO_WALL, OUT_OF_WORLD, GENERIC, MAGIC, WITHER, DRAGON_BREATH, DRY_OUT, SWEET_BERRY_BUSH, FREEZE, STALAGMITE, FALLING_BLOCK, FALLING_ANVIL, FALLING_STALACTITE, STING, MOB_ATTACK, MOB_ATTACK_NO_AGGRO, PLAYER_ATTACK, ARROW, TRIDENT, MOB_PROJECTILE, FIREWORKS, FIREBALL, UNATTRIBUTED_FIREBALL, WITHER_SKULL, THROWN, INDIRECT_MAGIC, THORNS, EXPLOSION, PLAYER_EXPLOSION, SONIC_BOOM, BAD_RESPAWN_POINT, GENERIC_KILL, WEAPONS_MECHANICS};
   }
}
