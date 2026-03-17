/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Registry
 *  org.bukkit.Sound
 *  org.bukkit.SoundCategory
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries;

import com.google.common.base.Enums;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XModule;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public final class XSound
extends XModule<XSound, Sound> {
    public static final XRegistry<XSound, Sound> REGISTRY = new XRegistry<XSound, Sound>(Sound.class, XSound.class, () -> Registry.SOUNDS, XSound::new, XSound[]::new);
    public static final XSound AMBIENT_UNDERWATER_LOOP = XSound.std("ambient.underwater.loop", "AMBIENT_UNDERWATER_EXIT");
    public static final XSound AMBIENT_UNDERWATER_LOOP_ADDITIONS = XSound.std("ambient.underwater.loop.additions", "AMBIENT_UNDERWATER_EXIT");
    public static final XSound AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE = XSound.std("ambient.underwater.loop.additions.rare", "AMBIENT_UNDERWATER_EXIT");
    public static final XSound AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE = XSound.std("ambient.underwater.loop.additions.ultra_rare", "AMBIENT_UNDERWATER_EXIT");
    public static final XSound BLOCK_ANVIL_BREAK = XSound.std("block.anvil.break", "ANVIL_BREAK");
    public static final XSound BLOCK_ANVIL_HIT = XSound.std("block.anvil.hit", "BLOCK_ANVIL_FALL");
    public static final XSound BLOCK_ANVIL_LAND = XSound.std("block.anvil.land", "ANVIL_LAND");
    public static final XSound BLOCK_ANVIL_PLACE = XSound.std("block.anvil.place", "BLOCK_ANVIL_FALL");
    public static final XSound BLOCK_ANVIL_STEP = XSound.std("block.anvil.step", "BLOCK_ANVIL_FALL");
    public static final XSound BLOCK_ANVIL_USE = XSound.std("block.anvil.use", "ANVIL_USE");
    public static final XSound BLOCK_BEACON_DEACTIVATE = XSound.std("block.beacon.deactivate", "BLOCK_BEACON_AMBIENT");
    public static final XSound BLOCK_BEACON_POWER_SELECT = XSound.std("block.beacon.power_select", "BLOCK_BEACON_AMBIENT");
    public static final XSound BLOCK_CHEST_CLOSE = XSound.std("block.chest.close", "CHEST_CLOSE", "ENTITY_CHEST_CLOSE");
    public static final XSound BLOCK_CHEST_OPEN = XSound.std("block.chest.open", "CHEST_OPEN", "ENTITY_CHEST_OPEN");
    public static final XSound BLOCK_FIRE_AMBIENT = XSound.std("block.fire.ambient", "FIRE");
    public static final XSound BLOCK_FIRE_EXTINGUISH = XSound.std("block.fire.extinguish", "FIZZ");
    public static final XSound BLOCK_GLASS_BREAK = XSound.std("block.glass.break", "GLASS");
    public static final XSound BLOCK_GRASS_BREAK = XSound.std("block.grass.break", "DIG_GRASS");
    public static final XSound BLOCK_GRASS_STEP = XSound.std("block.grass.step", "STEP_GRASS");
    public static final XSound BLOCK_GRAVEL_BREAK = XSound.std("block.gravel.break", "DIG_GRAVEL");
    public static final XSound BLOCK_GRAVEL_STEP = XSound.std("block.gravel.step", "STEP_GRAVEL");
    public static final XSound BLOCK_LADDER_STEP = XSound.std("block.ladder.step", "STEP_LADDER");
    public static final XSound BLOCK_LAVA_AMBIENT = XSound.std("block.lava.ambient", "LAVA");
    public static final XSound BLOCK_LAVA_POP = XSound.std("block.lava.pop", "LAVA_POP");
    public static final XSound BLOCK_LILY_PAD_PLACE = XSound.std("block.lily_pad.place", "BLOCK_WATERLILY_PLACE");
    public static final XSound BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF = XSound.std("block.metal_pressure_plate.click_off", "BLOCK_METAL_PRESSUREPLATE_CLICK_OFF");
    public static final XSound BLOCK_METAL_PRESSURE_PLATE_CLICK_ON = XSound.std("block.metal_pressure_plate.click_on", "BLOCK_METAL_PRESSUREPLATE_CLICK_ON");
    public static final XSound BLOCK_NOTE_BLOCK_BASEDRUM = XSound.std("block.note_block.basedrum", "NOTE_BASS_DRUM", "BLOCK_NOTE_BASEDRUM");
    public static final XSound BLOCK_NOTE_BLOCK_BASS = XSound.std("block.note_block.bass", "NOTE_BASS", "BLOCK_NOTE_BASS");
    public static final XSound BLOCK_NOTE_BLOCK_BELL = XSound.std("block.note_block.bell", "BLOCK_NOTE_BELL");
    public static final XSound BLOCK_NOTE_BLOCK_CHIME = XSound.std("block.note_block.chime", "BLOCK_NOTE_CHIME");
    public static final XSound BLOCK_NOTE_BLOCK_FLUTE = XSound.std("block.note_block.flute", "BLOCK_NOTE_FLUTE");
    public static final XSound BLOCK_NOTE_BLOCK_GUITAR = XSound.std("block.note_block.guitar", "NOTE_BASS_GUITAR", "BLOCK_NOTE_GUITAR");
    public static final XSound BLOCK_NOTE_BLOCK_HARP = XSound.std("block.note_block.harp", "NOTE_PIANO", "BLOCK_NOTE_HARP");
    public static final XSound BLOCK_NOTE_BLOCK_HAT = XSound.std("block.note_block.hat", "NOTE_STICKS", "BLOCK_NOTE_HAT");
    public static final XSound BLOCK_NOTE_BLOCK_PLING = XSound.std("block.note_block.pling", "NOTE_PLING", "BLOCK_NOTE_PLING");
    public static final XSound BLOCK_NOTE_BLOCK_SNARE = XSound.std("block.note_block.snare", "NOTE_SNARE_DRUM", "BLOCK_NOTE_SNARE");
    public static final XSound BLOCK_NOTE_BLOCK_XYLOPHONE = XSound.std("block.note_block.xylophone", "BLOCK_NOTE_XYLOPHONE");
    public static final XSound BLOCK_PISTON_CONTRACT = XSound.std("block.piston.contract", "PISTON_RETRACT");
    public static final XSound BLOCK_PISTON_EXTEND = XSound.std("block.piston.extend", "PISTON_EXTEND");
    public static final XSound BLOCK_PORTAL_AMBIENT = XSound.std("block.portal.ambient", "PORTAL");
    public static final XSound BLOCK_PORTAL_TRAVEL = XSound.std("block.portal.travel", "PORTAL_TRAVEL");
    public static final XSound BLOCK_PORTAL_TRIGGER = XSound.std("block.portal.trigger", "PORTAL_TRIGGER");
    public static final XSound BLOCK_SAND_BREAK = XSound.std("block.sand.break", "DIG_SAND");
    public static final XSound BLOCK_SAND_STEP = XSound.std("block.sand.step", "STEP_SAND");
    public static final XSound BLOCK_SLIME_BLOCK_BREAK = XSound.std("block.slime_block.break", "BLOCK_SLIME_BREAK");
    public static final XSound BLOCK_SLIME_BLOCK_FALL = XSound.std("block.slime_block.fall", "BLOCK_SLIME_FALL");
    public static final XSound BLOCK_SLIME_BLOCK_HIT = XSound.std("block.slime_block.hit", "BLOCK_SLIME_HIT");
    public static final XSound BLOCK_SLIME_BLOCK_PLACE = XSound.std("block.slime_block.place", "BLOCK_SLIME_PLACE");
    public static final XSound BLOCK_SLIME_BLOCK_STEP = XSound.std("block.slime_block.step", "BLOCK_SLIME_STEP");
    public static final XSound BLOCK_SNOW_BREAK = XSound.std("block.snow.break", "DIG_SNOW");
    public static final XSound BLOCK_SNOW_STEP = XSound.std("block.snow.step", "STEP_SNOW");
    public static final XSound BLOCK_STONE_BREAK = XSound.std("block.stone.break", "DIG_STONE");
    public static final XSound BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF = XSound.std("block.stone_pressure_plate.click_off", "BLOCK_STONE_PRESSUREPLATE_CLICK_OFF");
    public static final XSound BLOCK_STONE_PRESSURE_PLATE_CLICK_ON = XSound.std("block.stone_pressure_plate.click_on", "BLOCK_STONE_PRESSUREPLATE_CLICK_ON");
    public static final XSound BLOCK_STONE_STEP = XSound.std("block.stone.step", "STEP_STONE");
    public static final XSound BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES = XSound.std("block.sweet_berry_bush.pick_berries", "ITEM_SWEET_BERRIES_PICK_FROM_BUSH");
    public static final XSound BLOCK_WATER_AMBIENT = XSound.std("block.water.ambient", "WATER");
    public static final XSound BLOCK_WET_GRASS_PLACE = XSound.std("block.wet_grass.place", "BLOCK_WET_GRASS_HIT");
    public static final XSound BLOCK_WET_GRASS_STEP = XSound.std("block.wet_grass.step", "BLOCK_WET_GRASS_HIT");
    public static final XSound BLOCK_WOODEN_BUTTON_CLICK_OFF = XSound.std("block.wooden_button.click_off", "WOOD_CLICK", "BLOCK_WOOD_BUTTON_CLICK_OFF");
    public static final XSound BLOCK_WOODEN_BUTTON_CLICK_ON = XSound.std("block.wooden_button.click_on", "WOOD_CLICK", "BLOCK_WOOD_BUTTON_CLICK_ON");
    public static final XSound BLOCK_WOODEN_DOOR_CLOSE = XSound.std("block.wooden_door.close", "DOOR_CLOSE");
    public static final XSound BLOCK_WOODEN_DOOR_OPEN = XSound.std("block.wooden_door.open", "DOOR_OPEN");
    public static final XSound BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF = XSound.std("block.wooden_pressure_plate.click_off", "BLOCK_WOOD_PRESSUREPLATE_CLICK_OFF");
    public static final XSound BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON = XSound.std("block.wooden_pressure_plate.click_on", "BLOCK_WOOD_PRESSUREPLATE_CLICK_ON");
    public static final XSound BLOCK_WOOD_BREAK = XSound.std("block.wood.break", "DIG_WOOD");
    public static final XSound BLOCK_WOOD_STEP = XSound.std("block.wood.step", "STEP_WOOD");
    public static final XSound BLOCK_WOOL_BREAK = XSound.std("block.wool.break", "DIG_WOOL", "BLOCK_CLOTH_BREAK");
    public static final XSound BLOCK_WOOL_HIT = XSound.std("block.wool.hit", "BLOCK_WOOL_FALL", "BLOCK_CLOTH_FALL", "BLOCK_CLOTH_HIT");
    public static final XSound BLOCK_WOOL_PLACE = XSound.std("block.wool.place", "BLOCK_WOOL_FALL", "BLOCK_CLOTH_PLACE");
    public static final XSound BLOCK_WOOL_STEP = XSound.std("block.wool.step", "STEP_WOOL", "BLOCK_CLOTH_STEP");
    public static final XSound ENTITY_ARMOR_STAND_BREAK = XSound.std("entity.armor_stand.break", "ENTITY_ARMORSTAND_BREAK");
    public static final XSound ENTITY_ARMOR_STAND_FALL = XSound.std("entity.armor_stand.fall", "ENTITY_ARMORSTAND_FALL");
    public static final XSound ENTITY_ARMOR_STAND_HIT = XSound.std("entity.armor_stand.hit", "ENTITY_ARMORSTAND_HIT");
    public static final XSound ENTITY_ARMOR_STAND_PLACE = XSound.std("entity.armor_stand.place", "ENTITY_ARMORSTAND_PLACE");
    public static final XSound ENTITY_ARROW_HIT = XSound.std("entity.arrow.hit", "ARROW_HIT");
    public static final XSound ENTITY_ARROW_HIT_PLAYER = XSound.std("entity.arrow.hit_player", "SUCCESSFUL_HIT");
    public static final XSound ENTITY_ARROW_SHOOT = XSound.std("entity.arrow.shoot", "SHOOT_ARROW");
    public static final XSound ENTITY_BAT_AMBIENT = XSound.std("entity.bat.ambient", "BAT_IDLE");
    public static final XSound ENTITY_BAT_DEATH = XSound.std("entity.bat.death", "BAT_DEATH");
    public static final XSound ENTITY_BAT_HURT = XSound.std("entity.bat.hurt", "BAT_HURT");
    public static final XSound ENTITY_BAT_LOOP = XSound.std("entity.bat.loop", "BAT_LOOP");
    public static final XSound ENTITY_BAT_TAKEOFF = XSound.std("entity.bat.takeoff", "BAT_TAKEOFF");
    public static final XSound ENTITY_BLAZE_AMBIENT = XSound.std("entity.blaze.ambient", "BLAZE_BREATH");
    public static final XSound ENTITY_BLAZE_DEATH = XSound.std("entity.blaze.death", "BLAZE_DEATH");
    public static final XSound ENTITY_BLAZE_HURT = XSound.std("entity.blaze.hurt", "BLAZE_HIT");
    public static final XSound ENTITY_CAT_AMBIENT = XSound.std("entity.cat.ambient", "CAT_MEOW");
    public static final XSound ENTITY_CAT_EAT = XSound.std("entity.cat.eat");
    public static final XSound ENTITY_CAT_HISS = XSound.std("entity.cat.hiss", "CAT_HISS");
    public static final XSound ENTITY_CAT_HURT = XSound.std("entity.cat.hurt", "CAT_HIT");
    public static final XSound ENTITY_CAT_PURR = XSound.std("entity.cat.purr", "CAT_PURR");
    public static final XSound ENTITY_CAT_PURREOW = XSound.std("entity.cat.purreow", "CAT_PURREOW");
    public static final XSound ENTITY_CHICKEN_AMBIENT = XSound.std("entity.chicken.ambient", "CHICKEN_IDLE");
    public static final XSound ENTITY_CHICKEN_EGG = XSound.std("entity.chicken.egg", "CHICKEN_EGG_POP");
    public static final XSound ENTITY_CHICKEN_HURT = XSound.std("entity.chicken.hurt", "CHICKEN_HURT");
    public static final XSound ENTITY_CHICKEN_STEP = XSound.std("entity.chicken.step", "CHICKEN_WALK");
    public static final XSound ENTITY_COW_AMBIENT = XSound.std("entity.cow.ambient", "COW_IDLE");
    public static final XSound ENTITY_COW_HURT = XSound.std("entity.cow.hurt", "COW_HURT");
    public static final XSound ENTITY_COW_STEP = XSound.std("entity.cow.step", "COW_WALK");
    public static final XSound ENTITY_CREEPER_DEATH = XSound.std("entity.creeper.death", "CREEPER_DEATH");
    public static final XSound ENTITY_CREEPER_PRIMED = XSound.std("entity.creeper.primed", "CREEPER_HISS");
    public static final XSound ENTITY_DONKEY_AMBIENT = XSound.std("entity.donkey.ambient", "DONKEY_IDLE");
    public static final XSound ENTITY_DONKEY_ANGRY = XSound.std("entity.donkey.angry", "DONKEY_ANGRY");
    public static final XSound ENTITY_DONKEY_DEATH = XSound.std("entity.donkey.death", "DONKEY_DEATH");
    public static final XSound ENTITY_DONKEY_HURT = XSound.std("entity.donkey.hurt", "DONKEY_HIT");
    public static final XSound ENTITY_DRAGON_FIREBALL_EXPLODE = XSound.std("entity.dragon_fireball.explode", "ENTITY_ENDERDRAGON_FIREBALL_EXPLODE");
    public static final XSound ENTITY_ENDERMAN_AMBIENT = XSound.std("entity.enderman.ambient", "ENDERMAN_IDLE", "ENTITY_ENDERMEN_AMBIENT");
    public static final XSound ENTITY_ENDERMAN_DEATH = XSound.std("entity.enderman.death", "ENDERMAN_DEATH", "ENTITY_ENDERMEN_DEATH");
    public static final XSound ENTITY_ENDERMAN_HURT = XSound.std("entity.enderman.hurt", "ENDERMAN_HIT", "ENTITY_ENDERMEN_HURT");
    public static final XSound ENTITY_ENDERMAN_SCREAM = XSound.std("entity.enderman.scream", "ENDERMAN_SCREAM", "ENTITY_ENDERMEN_SCREAM");
    public static final XSound ENTITY_ENDERMAN_STARE = XSound.std("entity.enderman.stare", "ENDERMAN_STARE", "ENTITY_ENDERMEN_STARE");
    public static final XSound ENTITY_ENDERMAN_TELEPORT = XSound.std("entity.enderman.teleport", "ENDERMAN_TELEPORT", "ENTITY_ENDERMEN_TELEPORT");
    public static final XSound ENTITY_ENDER_DRAGON_AMBIENT = XSound.std("entity.ender_dragon.ambient", "ENDERDRAGON_WINGS", "ENTITY_ENDERDRAGON_AMBIENT");
    public static final XSound ENTITY_ENDER_DRAGON_DEATH = XSound.std("entity.ender_dragon.death", "ENDERDRAGON_DEATH", "ENTITY_ENDERDRAGON_DEATH");
    public static final XSound ENTITY_ENDER_DRAGON_FLAP = XSound.std("entity.ender_dragon.flap", "ENDERDRAGON_WINGS", "ENTITY_ENDERDRAGON_FLAP");
    public static final XSound ENTITY_ENDER_DRAGON_GROWL = XSound.std("entity.ender_dragon.growl", "ENDERDRAGON_GROWL", "ENTITY_ENDERDRAGON_GROWL");
    public static final XSound ENTITY_ENDER_DRAGON_HURT = XSound.std("entity.ender_dragon.hurt", "ENDERDRAGON_HIT", "ENTITY_ENDERDRAGON_HURT");
    public static final XSound ENTITY_ENDER_DRAGON_SHOOT = XSound.std("entity.ender_dragon.shoot", "ENTITY_ENDERDRAGON_SHOOT");
    public static final XSound ENTITY_ENDER_EYE_LAUNCH = XSound.std("entity.ender_eye.launch", "ENTITY_ENDER_EYE_DEATH", "ENTITY_ENDEREYE_DEATH", "ENTITY_ENDEREYE_LAUNCH");
    public static final XSound ENTITY_ENDER_PEARL_THROW = XSound.std("entity.ender_pearl.throw", "ENTITY_ENDERPEARL_THROW");
    public static final XSound ENTITY_EVOKER_AMBIENT = XSound.std("entity.evoker.ambient", "ENTITY_EVOCATION_ILLAGER_AMBIENT");
    public static final XSound ENTITY_EVOKER_CAST_SPELL = XSound.std("entity.evoker.cast_spell", "ENTITY_EVOCATION_ILLAGER_CAST_SPELL");
    public static final XSound ENTITY_EVOKER_DEATH = XSound.std("entity.evoker.death", "ENTITY_EVOCATION_ILLAGER_DEATH");
    public static final XSound ENTITY_EVOKER_FANGS_ATTACK = XSound.std("entity.evoker_fangs.attack", "ENTITY_EVOCATION_FANGS_ATTACK");
    public static final XSound ENTITY_EVOKER_HURT = XSound.std("entity.evoker.hurt", "ENTITY_EVOCATION_ILLAGER_HURT");
    public static final XSound ENTITY_EVOKER_PREPARE_ATTACK = XSound.std("entity.evoker.prepare_attack", "ENTITY_EVOCATION_ILLAGER_PREPARE_ATTACK");
    public static final XSound ENTITY_EVOKER_PREPARE_SUMMON = XSound.std("entity.evoker.prepare_summon", "ENTITY_EVOCATION_ILLAGER_PREPARE_SUMMON");
    public static final XSound ENTITY_EVOKER_PREPARE_WOLOLO = XSound.std("entity.evoker.prepare_wololo", "ENTITY_EVOCATION_ILLAGER_PREPARE_WOLOLO");
    public static final XSound ENTITY_FIREWORK_ROCKET_BLAST = XSound.std("entity.firework_rocket.blast", "FIREWORK_BLAST", "ENTITY_FIREWORK_BLAST");
    public static final XSound ENTITY_FIREWORK_ROCKET_BLAST_FAR = XSound.std("entity.firework_rocket.blast_far", "FIREWORK_BLAST2", "ENTITY_FIREWORK_BLAST_FAR");
    public static final XSound ENTITY_FIREWORK_ROCKET_LARGE_BLAST = XSound.std("entity.firework_rocket.large_blast", "FIREWORK_LARGE_BLAST", "ENTITY_FIREWORK_LARGE_BLAST");
    public static final XSound ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR = XSound.std("entity.firework_rocket.large_blast_far", "FIREWORK_LARGE_BLAST2", "ENTITY_FIREWORK_LARGE_BLAST_FAR");
    public static final XSound ENTITY_FIREWORK_ROCKET_LAUNCH = XSound.std("entity.firework_rocket.launch", "FIREWORK_LAUNCH", "ENTITY_FIREWORK_LAUNCH");
    public static final XSound ENTITY_FIREWORK_ROCKET_TWINKLE = XSound.std("entity.firework_rocket.twinkle", "FIREWORK_TWINKLE", "ENTITY_FIREWORK_TWINKLE");
    public static final XSound ENTITY_FIREWORK_ROCKET_TWINKLE_FAR = XSound.std("entity.firework_rocket.twinkle_far", "FIREWORK_TWINKLE2", "ENTITY_FIREWORK_TWINKLE_FAR");
    public static final XSound ENTITY_FISHING_BOBBER_SPLASH = XSound.std("entity.fishing_bobber.splash", "SPLASH2", "ENTITY_BOBBER_SPLASH");
    public static final XSound ENTITY_FISHING_BOBBER_THROW = XSound.std("entity.fishing_bobber.throw", "ENTITY_BOBBER_THROW");
    public static final XSound ENTITY_GENERIC_BIG_FALL = XSound.std("entity.generic.big_fall", "FALL_BIG");
    public static final XSound ENTITY_GENERIC_DRINK = XSound.std("entity.generic.drink", "DRINK");
    public static final XSound ENTITY_GENERIC_EAT = XSound.std("entity.generic.eat", "EAT");
    public static final XSound ENTITY_GENERIC_EXPLODE = XSound.std("entity.generic.explode", "EXPLODE");
    public static final XSound ENTITY_GENERIC_SMALL_FALL = XSound.std("entity.generic.small_fall", "FALL_SMALL");
    public static final XSound ENTITY_GENERIC_SPLASH = XSound.std("entity.generic.splash", "SPLASH");
    public static final XSound ENTITY_GENERIC_SWIM = XSound.std("entity.generic.swim", "SWIM");
    public static final XSound ENTITY_GHAST_AMBIENT = XSound.std("entity.ghast.ambient", "GHAST_MOAN");
    public static final XSound ENTITY_GHAST_DEATH = XSound.std("entity.ghast.death", "GHAST_DEATH");
    public static final XSound ENTITY_GHAST_HURT = XSound.std("entity.ghast.hurt", "GHAST_SCREAM2");
    public static final XSound ENTITY_GHAST_SCREAM = XSound.std("entity.ghast.scream", "GHAST_SCREAM");
    public static final XSound ENTITY_GHAST_SHOOT = XSound.std("entity.ghast.shoot", "GHAST_FIREBALL");
    public static final XSound ENTITY_GHAST_WARN = XSound.std("entity.ghast.warn", "GHAST_CHARGE");
    public static final XSound ENTITY_HORSE_AMBIENT = XSound.std("entity.horse.ambient", "HORSE_IDLE");
    public static final XSound ENTITY_HORSE_ANGRY = XSound.std("entity.horse.angry", "HORSE_ANGRY");
    public static final XSound ENTITY_HORSE_ARMOR = XSound.std("entity.horse.armor", "HORSE_ARMOR");
    public static final XSound ENTITY_HORSE_BREATHE = XSound.std("entity.horse.breathe", "HORSE_BREATHE");
    public static final XSound ENTITY_HORSE_DEATH = XSound.std("entity.horse.death", "HORSE_DEATH");
    public static final XSound ENTITY_HORSE_EAT = XSound.std("entity.horse.eat");
    public static final XSound ENTITY_HORSE_GALLOP = XSound.std("entity.horse.gallop", "HORSE_GALLOP");
    public static final XSound ENTITY_HORSE_HURT = XSound.std("entity.horse.hurt", "HORSE_HIT");
    public static final XSound ENTITY_HORSE_JUMP = XSound.std("entity.horse.jump", "HORSE_JUMP");
    public static final XSound ENTITY_HORSE_LAND = XSound.std("entity.horse.land", "HORSE_LAND");
    public static final XSound ENTITY_HORSE_SADDLE = XSound.std("entity.horse.saddle", "HORSE_SADDLE");
    public static final XSound ENTITY_HORSE_STEP = XSound.std("entity.horse.step", "HORSE_SOFT");
    public static final XSound ENTITY_HORSE_STEP_WOOD = XSound.std("entity.horse.step_wood", "HORSE_WOOD");
    public static final XSound ENTITY_HOSTILE_BIG_FALL = XSound.std("entity.hostile.big_fall", "FALL_BIG");
    public static final XSound ENTITY_HOSTILE_SMALL_FALL = XSound.std("entity.hostile.small_fall", "FALL_SMALL");
    public static final XSound ENTITY_HOSTILE_SPLASH = XSound.std("entity.hostile.splash", "SPLASH");
    public static final XSound ENTITY_HOSTILE_SWIM = XSound.std("entity.hostile.swim", "SWIM");
    public static final XSound ENTITY_ILLUSIONER_AMBIENT = XSound.std("entity.illusioner.ambient", "ENTITY_ILLUSION_ILLAGER_AMBIENT");
    public static final XSound ENTITY_ILLUSIONER_CAST_SPELL = XSound.std("entity.illusioner.cast_spell", "ENTITY_ILLUSION_ILLAGER_CAST_SPELL");
    public static final XSound ENTITY_ILLUSIONER_DEATH = XSound.std("entity.illusioner.death", "ENTITY_ILLUSIONER_CAST_DEATH", "ENTITY_ILLUSION_ILLAGER_DEATH");
    public static final XSound ENTITY_ILLUSIONER_HURT = XSound.std("entity.illusioner.hurt", "ENTITY_ILLUSION_ILLAGER_HURT");
    public static final XSound ENTITY_ILLUSIONER_MIRROR_MOVE = XSound.std("entity.illusioner.mirror_move", "ENTITY_ILLUSION_ILLAGER_MIRROR_MOVE");
    public static final XSound ENTITY_ILLUSIONER_PREPARE_BLINDNESS = XSound.std("entity.illusioner.prepare_blindness", "ENTITY_ILLUSION_ILLAGER_PREPARE_BLINDNESS");
    public static final XSound ENTITY_ILLUSIONER_PREPARE_MIRROR = XSound.std("entity.illusioner.prepare_mirror", "ENTITY_ILLUSION_ILLAGER_PREPARE_MIRROR");
    public static final XSound ENTITY_IRON_GOLEM_ATTACK = XSound.std("entity.iron_golem.attack", "IRONGOLEM_THROW", "ENTITY_IRONGOLEM_ATTACK");
    public static final XSound ENTITY_IRON_GOLEM_DEATH = XSound.std("entity.iron_golem.death", "IRONGOLEM_DEATH", "ENTITY_IRONGOLEM_DEATH");
    public static final XSound ENTITY_IRON_GOLEM_HURT = XSound.std("entity.iron_golem.hurt", "IRONGOLEM_HIT", "ENTITY_IRONGOLEM_HURT");
    public static final XSound ENTITY_IRON_GOLEM_STEP = XSound.std("entity.iron_golem.step", "IRONGOLEM_WALK", "ENTITY_IRONGOLEM_STEP");
    public static final XSound ENTITY_ITEM_BREAK = XSound.std("entity.item.break", "ITEM_BREAK");
    public static final XSound ENTITY_ITEM_FRAME_ADD_ITEM = XSound.std("entity.item_frame.add_item", "ENTITY_ITEMFRAME_ADD_ITEM");
    public static final XSound ENTITY_ITEM_FRAME_BREAK = XSound.std("entity.item_frame.break", "ENTITY_ITEMFRAME_BREAK");
    public static final XSound ENTITY_ITEM_FRAME_PLACE = XSound.std("entity.item_frame.place", "ENTITY_ITEMFRAME_PLACE");
    public static final XSound ENTITY_ITEM_FRAME_REMOVE_ITEM = XSound.std("entity.item_frame.remove_item", "ENTITY_ITEMFRAME_REMOVE_ITEM");
    public static final XSound ENTITY_ITEM_FRAME_ROTATE_ITEM = XSound.std("entity.item_frame.rotate_item", "ENTITY_ITEMFRAME_ROTATE_ITEM");
    public static final XSound ENTITY_ITEM_PICKUP = XSound.std("entity.item.pickup", "ITEM_PICKUP");
    public static final XSound ENTITY_LEASH_KNOT_BREAK = XSound.std("entity.leash_knot.break", "ENTITY_LEASHKNOT_BREAK");
    public static final XSound ENTITY_LEASH_KNOT_PLACE = XSound.std("entity.leash_knot.place", "ENTITY_LEASHKNOT_PLACE");
    public static final XSound ENTITY_LIGHTNING_BOLT_IMPACT = XSound.std("entity.lightning_bolt.impact", "ENTITY_LIGHTNING_IMPACT", "AMBIENCE_THUNDER");
    public static final XSound ENTITY_LIGHTNING_BOLT_THUNDER = XSound.std("entity.lightning_bolt.thunder", "ENTITY_LIGHTNING_THUNDER", "AMBIENCE_THUNDER");
    public static final XSound ENTITY_LINGERING_POTION_THROW = XSound.std("entity.lingering_potion.throw", "ENTITY_LINGERINGPOTION_THROW");
    public static final XSound ENTITY_MAGMA_CUBE_DEATH = XSound.std("entity.magma_cube.death", "ENTITY_MAGMACUBE_DEATH");
    public static final XSound ENTITY_MAGMA_CUBE_DEATH_SMALL = XSound.std("entity.magma_cube.death_small", "ENTITY_SMALL_MAGMACUBE_DEATH");
    public static final XSound ENTITY_MAGMA_CUBE_HURT = XSound.std("entity.magma_cube.hurt", "ENTITY_MAGMACUBE_HURT");
    public static final XSound ENTITY_MAGMA_CUBE_HURT_SMALL = XSound.std("entity.magma_cube.hurt_small", "ENTITY_SMALL_MAGMACUBE_HURT");
    public static final XSound ENTITY_MAGMA_CUBE_JUMP = XSound.std("entity.magma_cube.jump", "MAGMACUBE_JUMP", "ENTITY_MAGMACUBE_JUMP");
    public static final XSound ENTITY_MAGMA_CUBE_SQUISH = XSound.std("entity.magma_cube.squish", "MAGMACUBE_WALK", "ENTITY_MAGMACUBE_SQUISH");
    public static final XSound ENTITY_MAGMA_CUBE_SQUISH_SMALL = XSound.std("entity.magma_cube.squish_small", "MAGMACUBE_WALK2", "ENTITY_SMALL_MAGMACUBE_SQUISH");
    public static final XSound ENTITY_MINECART_INSIDE = XSound.std("entity.minecart.inside", "MINECART_INSIDE");
    public static final XSound ENTITY_MINECART_RIDING = XSound.std("entity.minecart.riding", "MINECART_BASE");
    public static final XSound ENTITY_MULE_CHEST = XSound.std("entity.mule.chest", "ENTITY_MULE_AMBIENT");
    public static final XSound ENTITY_MULE_DEATH = XSound.std("entity.mule.death", "ENTITY_MULE_AMBIENT");
    public static final XSound ENTITY_MULE_HURT = XSound.std("entity.mule.hurt", "ENTITY_MULE_AMBIENT");
    public static final XSound ENTITY_PIG_AMBIENT = XSound.std("entity.pig.ambient", "PIG_IDLE");
    public static final XSound ENTITY_PIG_DEATH = XSound.std("entity.pig.death", "PIG_DEATH");
    public static final XSound ENTITY_PIG_SADDLE = XSound.std("entity.pig.saddle", "ENTITY_PIG_HURT");
    public static final XSound ENTITY_PIG_STEP = XSound.std("entity.pig.step", "PIG_WALK");
    public static final XSound ENTITY_PLAYER_ATTACK_STRONG = XSound.std("entity.player.attack.strong", "SUCCESSFUL_HIT");
    public static final XSound ENTITY_PLAYER_BIG_FALL = XSound.std("entity.player.big_fall", "FALL_BIG");
    public static final XSound ENTITY_PLAYER_BURP = XSound.std("entity.player.burp", "BURP");
    public static final XSound ENTITY_PLAYER_HURT = XSound.std("entity.player.hurt", "HURT_FLESH");
    public static final XSound ENTITY_PLAYER_LEVELUP = XSound.std("entity.player.levelup", "LEVEL_UP");
    public static final XSound ENTITY_PLAYER_SMALL_FALL = XSound.std("entity.player.small_fall", "FALL_SMALL");
    public static final XSound ENTITY_PLAYER_SPLASH = XSound.std("entity.player.splash", "SLASH");
    public static final XSound ENTITY_PLAYER_SPLASH_HIGH_SPEED = XSound.std("entity.player.splash.high_speed", "SPLASH");
    public static final XSound ENTITY_PLAYER_SWIM = XSound.std("entity.player.swim", "SWIM");
    public static final XSound ENTITY_POLAR_BEAR_AMBIENT_BABY = XSound.std("entity.polar_bear.ambient_baby", "ENTITY_POLAR_BEAR_BABY_AMBIENT");
    public static final XSound ENTITY_SALMON_HURT = XSound.std("entity.salmon.hurt", "ENTITY_SALMON_FLOP");
    public static final XSound ENTITY_SHEEP_AMBIENT = XSound.std("entity.sheep.ambient", "SHEEP_IDLE");
    public static final XSound ENTITY_SHEEP_SHEAR = XSound.std("entity.sheep.shear", "SHEEP_SHEAR");
    public static final XSound ENTITY_SHEEP_STEP = XSound.std("entity.sheep.step", "SHEEP_WALK");
    public static final XSound ENTITY_SILVERFISH_AMBIENT = XSound.std("entity.silverfish.ambient", "SILVERFISH_IDLE");
    public static final XSound ENTITY_SILVERFISH_DEATH = XSound.std("entity.silverfish.death", "SILVERFISH_KILL");
    public static final XSound ENTITY_SILVERFISH_HURT = XSound.std("entity.silverfish.hurt", "SILVERFISH_HIT");
    public static final XSound ENTITY_SILVERFISH_STEP = XSound.std("entity.silverfish.step", "SILVERFISH_WALK");
    public static final XSound ENTITY_SKELETON_AMBIENT = XSound.std("entity.skeleton.ambient", "SKELETON_IDLE");
    public static final XSound ENTITY_SKELETON_DEATH = XSound.std("entity.skeleton.death", "SKELETON_DEATH");
    public static final XSound ENTITY_SKELETON_HORSE_AMBIENT = XSound.std("entity.skeleton_horse.ambient", "HORSE_SKELETON_IDLE");
    public static final XSound ENTITY_SKELETON_HORSE_DEATH = XSound.std("entity.skeleton_horse.death", "HORSE_SKELETON_DEATH");
    public static final XSound ENTITY_SKELETON_HORSE_HURT = XSound.std("entity.skeleton_horse.hurt", "HORSE_SKELETON_HIT");
    public static final XSound ENTITY_SKELETON_HURT = XSound.std("entity.skeleton.hurt", "SKELETON_HURT");
    public static final XSound ENTITY_SKELETON_STEP = XSound.std("entity.skeleton.step", "SKELETON_WALK");
    public static final XSound ENTITY_SLIME_ATTACK = XSound.std("entity.slime.attack", "SLIME_ATTACK");
    public static final XSound ENTITY_SLIME_HURT_SMALL = XSound.std("entity.slime.hurt_small", "ENTITY_SMALL_SLIME_HURT");
    public static final XSound ENTITY_SLIME_JUMP = XSound.std("entity.slime.jump", "SLIME_WALK", "ENTITY_SMALL_SLIME_DEATH");
    public static final XSound ENTITY_SLIME_JUMP_SMALL = XSound.std("entity.slime.jump_small", "SLIME_WALK2", "ENTITY_SMALL_SLIME_SQUISH", "ENTITY_SMALL_SLIME_JUMP");
    public static final XSound ENTITY_SLIME_SQUISH = XSound.std("entity.slime.squish", "SLIME_WALK2");
    public static final XSound ENTITY_SLIME_SQUISH_SMALL = XSound.std("entity.slime.squish_small", "ENTITY_SMALL_SLIME_SQUISH");
    public static final XSound ENTITY_SNOW_GOLEM_AMBIENT = XSound.std("entity.snow_golem.ambient", "ENTITY_SNOWMAN_AMBIENT");
    public static final XSound ENTITY_SNOW_GOLEM_DEATH = XSound.std("entity.snow_golem.death", "ENTITY_SNOWMAN_DEATH");
    public static final XSound ENTITY_SNOW_GOLEM_HURT = XSound.std("entity.snow_golem.hurt", "ENTITY_SNOWMAN_HURT");
    public static final XSound ENTITY_SNOW_GOLEM_SHEAR = XSound.std("entity.snow_golem.shear");
    public static final XSound ENTITY_SNOW_GOLEM_SHOOT = XSound.std("entity.snow_golem.shoot", "ENTITY_SNOWMAN_SHOOT");
    public static final XSound ENTITY_SPIDER_AMBIENT = XSound.std("entity.spider.ambient", "SPIDER_IDLE");
    public static final XSound ENTITY_SPIDER_DEATH = XSound.std("entity.spider.death", "SPIDER_DEATH");
    public static final XSound ENTITY_SPIDER_STEP = XSound.std("entity.spider.step", "SPIDER_WALK");
    public static final XSound ENTITY_TNT_PRIMED = XSound.std("entity.tnt.primed", "FUSE");
    public static final XSound ENTITY_TROPICAL_FISH_FLOP = XSound.std("entity.tropical_fish.flop", "ENTITY_TROPICAL_FISH_DEATH");
    public static final XSound ENTITY_VILLAGER_AMBIENT = XSound.std("entity.villager.ambient", "VILLAGER_IDLE");
    public static final XSound ENTITY_VILLAGER_DEATH = XSound.std("entity.villager.death", "VILLAGER_DEATH");
    public static final XSound ENTITY_VILLAGER_HURT = XSound.std("entity.villager.hurt", "VILLAGER_HIT");
    public static final XSound ENTITY_VILLAGER_NO = XSound.std("entity.villager.no", "VILLAGER_NO");
    public static final XSound ENTITY_VILLAGER_TRADE = XSound.std("entity.villager.trade", "VILLAGER_HAGGLE", "ENTITY_VILLAGER_TRADING");
    public static final XSound ENTITY_VILLAGER_YES = XSound.std("entity.villager.yes", "VILLAGER_YES");
    public static final XSound ENTITY_VINDICATOR_AMBIENT = XSound.std("entity.vindicator.ambient", "ENTITY_VINDICATION_ILLAGER_AMBIENT");
    public static final XSound ENTITY_VINDICATOR_DEATH = XSound.std("entity.vindicator.death", "ENTITY_VINDICATION_ILLAGER_DEATH");
    public static final XSound ENTITY_VINDICATOR_HURT = XSound.std("entity.vindicator.hurt", "ENTITY_VINDICATION_ILLAGER_HURT");
    public static final XSound ENTITY_WITHER_AMBIENT = XSound.std("entity.wither.ambient", "WITHER_IDLE");
    public static final XSound ENTITY_WITHER_DEATH = XSound.std("entity.wither.death", "WITHER_DEATH");
    public static final XSound ENTITY_WITHER_HURT = XSound.std("entity.wither.hurt", "WITHER_HURT");
    public static final XSound ENTITY_WITHER_SHOOT = XSound.std("entity.wither.shoot", "WITHER_SHOOT");
    public static final XSound ENTITY_WITHER_SPAWN = XSound.std("entity.wither.spawn", "WITHER_SPAWN");
    public static final XSound ENTITY_WOLF_AMBIENT = XSound.std("entity.wolf.ambient", "WOLF_BARK");
    public static final XSound ENTITY_WOLF_DEATH = XSound.std("entity.wolf.death", "WOLF_DEATH");
    public static final XSound ENTITY_WOLF_GROWL = XSound.std("entity.wolf.growl", "WOLF_GROWL");
    public static final XSound ENTITY_WOLF_HOWL = XSound.std("entity.wolf.howl", "WOLF_HOWL");
    public static final XSound ENTITY_WOLF_HURT = XSound.std("entity.wolf.hurt", "WOLF_HURT");
    public static final XSound ENTITY_WOLF_PANT = XSound.std("entity.wolf.pant", "WOLF_PANT");
    public static final XSound ENTITY_WOLF_SHAKE = XSound.std("entity.wolf.shake", "WOLF_SHAKE");
    public static final XSound ENTITY_WOLF_STEP = XSound.std("entity.wolf.step", "WOLF_WALK");
    public static final XSound ENTITY_WOLF_WHINE = XSound.std("entity.wolf.whine", "WOLF_WHINE");
    public static final XSound ENTITY_ZOMBIE_AMBIENT = XSound.std("entity.zombie.ambient", "ZOMBIE_IDLE");
    public static final XSound ENTITY_ZOMBIE_ATTACK_IRON_DOOR = XSound.std("entity.zombie.attack_iron_door", "ZOMBIE_METAL");
    public static final XSound ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR = XSound.std("entity.zombie.attack_wooden_door", "ZOMBIE_WOOD", "ENTITY_ZOMBIE_ATTACK_DOOR_WOOD");
    public static final XSound ENTITY_ZOMBIE_BREAK_WOODEN_DOOR = XSound.std("entity.zombie.break_wooden_door", "ZOMBIE_WOODBREAK", "ENTITY_ZOMBIE_BREAK_DOOR_WOOD");
    public static final XSound ENTITY_ZOMBIE_DEATH = XSound.std("entity.zombie.death", "ZOMBIE_DEATH");
    public static final XSound ENTITY_ZOMBIE_HORSE_AMBIENT = XSound.std("entity.zombie_horse.ambient", "HORSE_ZOMBIE_IDLE");
    public static final XSound ENTITY_ZOMBIE_HORSE_DEATH = XSound.std("entity.zombie_horse.death", "HORSE_ZOMBIE_DEATH");
    public static final XSound ENTITY_ZOMBIE_HORSE_HURT = XSound.std("entity.zombie_horse.hurt", "HORSE_ZOMBIE_HIT");
    public static final XSound ENTITY_ZOMBIE_HURT = XSound.std("entity.zombie.hurt", "ZOMBIE_HURT");
    public static final XSound ENTITY_ZOMBIE_INFECT = XSound.std("entity.zombie.infect", "ZOMBIE_INFECT");
    public static final XSound ENTITY_ZOMBIE_STEP = XSound.std("entity.zombie.step", "ZOMBIE_WALK");
    public static final XSound ENTITY_ZOMBIE_VILLAGER_CONVERTED = XSound.std("entity.zombie_villager.converted", "ZOMBIE_UNFECT");
    public static final XSound ENTITY_ZOMBIE_VILLAGER_CURE = XSound.std("entity.zombie_villager.cure", "ZOMBIE_REMEDY");
    public static final XSound ENTITY_ZOMBIFIED_PIGLIN_AMBIENT = XSound.std("entity.zombified_piglin.ambient", "ZOMBIE_PIG_IDLE", "ENTITY_ZOMBIE_PIG_AMBIENT", "ENTITY_ZOMBIE_PIGMAN_AMBIENT");
    public static final XSound ENTITY_ZOMBIFIED_PIGLIN_ANGRY = XSound.std("entity.zombified_piglin.angry", "ZOMBIE_PIG_ANGRY", "ENTITY_ZOMBIE_PIG_ANGRY", "ENTITY_ZOMBIE_PIGMAN_ANGRY");
    public static final XSound ENTITY_ZOMBIFIED_PIGLIN_DEATH = XSound.std("entity.zombified_piglin.death", "ZOMBIE_PIG_DEATH", "ENTITY_ZOMBIE_PIG_DEATH", "ENTITY_ZOMBIE_PIGMAN_DEATH");
    public static final XSound ENTITY_ZOMBIFIED_PIGLIN_HURT = XSound.std("entity.zombified_piglin.hurt", "ZOMBIE_PIG_HURT", "ENTITY_ZOMBIE_PIG_HURT", "ENTITY_ZOMBIE_PIGMAN_HURT");
    public static final XSound ITEM_FLINTANDSTEEL_USE = XSound.std("item.flintandsteel.use", "FIRE_IGNITE");
    public static final XSound ITEM_TRIDENT_RIPTIDE_2 = XSound.std("item.trident.riptide_2", "ITEM_TRIDENT_RIPTIDE_1");
    public static final XSound ITEM_TRIDENT_RIPTIDE_3 = XSound.std("item.trident.riptide_3", "ITEM_TRIDENT_RIPTIDE_1");
    public static final XSound MUSIC_DISC_11 = XSound.std("music_disc.11", "RECORD_11");
    public static final XSound MUSIC_DISC_13 = XSound.std("music_disc.13", "RECORD_13");
    public static final XSound MUSIC_DISC_BLOCKS = XSound.std("music_disc.blocks", "RECORD_BLOCKS");
    public static final XSound MUSIC_DISC_CAT = XSound.std("music_disc.cat", "RECORD_CAT");
    public static final XSound MUSIC_DISC_CHIRP = XSound.std("music_disc.chirp", "RECORD_CHIRP");
    public static final XSound MUSIC_DISC_FAR = XSound.std("music_disc.far", "RECORD_FAR");
    public static final XSound MUSIC_DISC_MALL = XSound.std("music_disc.mall", "RECORD_MALL");
    public static final XSound MUSIC_DISC_MELLOHI = XSound.std("music_disc.mellohi", "RECORD_MELLOHI");
    public static final XSound MUSIC_DISC_STAL = XSound.std("music_disc.stal", "RECORD_STAL");
    public static final XSound MUSIC_DISC_STRAD = XSound.std("music_disc.strad", "RECORD_STRAD");
    public static final XSound MUSIC_DISC_WAIT = XSound.std("music_disc.wait", "RECORD_WAIT");
    public static final XSound MUSIC_DISC_WARD = XSound.std("music_disc.ward", "RECORD_WARD");
    public static final XSound MUSIC_NETHER_BASALT_DELTAS = XSound.std("music.nether.basalt_deltas", "MUSIC_NETHER");
    public static final XSound UI_BUTTON_CLICK = XSound.std("ui.button.click", "CLICK");
    public static final XSound WEATHER_RAIN = XSound.std("weather.rain", "AMBIENCE_RAIN");
    public static final XSound AMBIENT_CAVE = XSound.std("ambient.cave", "AMBIENCE_CAVE");
    public static final XSound ENTITY_EXPERIENCE_ORB_PICKUP = XSound.std("entity.experience_orb.pickup", "ORB_PICKUP");
    public static final XSound AMBIENT_BASALT_DELTAS_ADDITIONS = XSound.std("ambient.basalt_deltas.additions");
    public static final XSound AMBIENT_BASALT_DELTAS_LOOP = XSound.std("ambient.basalt_deltas.loop");
    public static final XSound AMBIENT_BASALT_DELTAS_MOOD = XSound.std("ambient.basalt_deltas.mood");
    public static final XSound AMBIENT_CRIMSON_FOREST_ADDITIONS = XSound.std("ambient.crimson_forest.additions");
    public static final XSound AMBIENT_CRIMSON_FOREST_LOOP = XSound.std("ambient.crimson_forest.loop");
    public static final XSound AMBIENT_CRIMSON_FOREST_MOOD = XSound.std("ambient.crimson_forest.mood");
    public static final XSound AMBIENT_NETHER_WASTES_ADDITIONS = XSound.std("ambient.nether_wastes.additions");
    public static final XSound AMBIENT_NETHER_WASTES_LOOP = XSound.std("ambient.nether_wastes.loop");
    public static final XSound AMBIENT_NETHER_WASTES_MOOD = XSound.std("ambient.nether_wastes.mood");
    public static final XSound AMBIENT_SOUL_SAND_VALLEY_ADDITIONS = XSound.std("ambient.soul_sand_valley.additions");
    public static final XSound AMBIENT_SOUL_SAND_VALLEY_LOOP = XSound.std("ambient.soul_sand_valley.loop");
    public static final XSound AMBIENT_SOUL_SAND_VALLEY_MOOD = XSound.std("ambient.soul_sand_valley.mood");
    public static final XSound AMBIENT_UNDERWATER_ENTER = XSound.std("ambient.underwater.enter");
    public static final XSound AMBIENT_UNDERWATER_EXIT = XSound.std("ambient.underwater.exit");
    public static final XSound AMBIENT_WARPED_FOREST_ADDITIONS = XSound.std("ambient.warped_forest.additions");
    public static final XSound AMBIENT_WARPED_FOREST_LOOP = XSound.std("ambient.warped_forest.loop");
    public static final XSound AMBIENT_WARPED_FOREST_MOOD = XSound.std("ambient.warped_forest.mood");
    public static final XSound BLOCK_AMETHYST_BLOCK_BREAK = XSound.std("block.amethyst_block.break");
    public static final XSound BLOCK_AMETHYST_BLOCK_CHIME = XSound.std("block.amethyst_block.chime");
    public static final XSound BLOCK_AMETHYST_BLOCK_FALL = XSound.std("block.amethyst_block.fall");
    public static final XSound BLOCK_AMETHYST_BLOCK_HIT = XSound.std("block.amethyst_block.hit");
    public static final XSound BLOCK_AMETHYST_BLOCK_PLACE = XSound.std("block.amethyst_block.place");
    public static final XSound BLOCK_AMETHYST_BLOCK_RESONATE = XSound.std("block.amethyst_block.resonate");
    public static final XSound BLOCK_AMETHYST_BLOCK_STEP = XSound.std("block.amethyst_block.step");
    public static final XSound BLOCK_AMETHYST_CLUSTER_BREAK = XSound.std("block.amethyst_cluster.break");
    public static final XSound BLOCK_AMETHYST_CLUSTER_FALL = XSound.std("block.amethyst_cluster.fall");
    public static final XSound BLOCK_AMETHYST_CLUSTER_HIT = XSound.std("block.amethyst_cluster.hit");
    public static final XSound BLOCK_AMETHYST_CLUSTER_PLACE = XSound.std("block.amethyst_cluster.place");
    public static final XSound BLOCK_AMETHYST_CLUSTER_STEP = XSound.std("block.amethyst_cluster.step");
    public static final XSound BLOCK_ANCIENT_DEBRIS_BREAK = XSound.std("block.ancient_debris.break");
    public static final XSound BLOCK_ANCIENT_DEBRIS_FALL = XSound.std("block.ancient_debris.fall");
    public static final XSound BLOCK_ANCIENT_DEBRIS_HIT = XSound.std("block.ancient_debris.hit");
    public static final XSound BLOCK_ANCIENT_DEBRIS_PLACE = XSound.std("block.ancient_debris.place");
    public static final XSound BLOCK_ANCIENT_DEBRIS_STEP = XSound.std("block.ancient_debris.step");
    public static final XSound BLOCK_ANVIL_DESTROY = XSound.std("block.anvil.destroy");
    public static final XSound BLOCK_ANVIL_FALL = XSound.std("block.anvil.fall");
    public static final XSound BLOCK_AZALEA_BREAK = XSound.std("block.azalea.break");
    public static final XSound BLOCK_AZALEA_FALL = XSound.std("block.azalea.fall");
    public static final XSound BLOCK_AZALEA_HIT = XSound.std("block.azalea.hit");
    public static final XSound BLOCK_AZALEA_LEAVES_BREAK = XSound.std("block.azalea_leaves.break");
    public static final XSound BLOCK_AZALEA_LEAVES_FALL = XSound.std("block.azalea_leaves.fall");
    public static final XSound BLOCK_AZALEA_LEAVES_HIT = XSound.std("block.azalea_leaves.hit");
    public static final XSound BLOCK_AZALEA_LEAVES_PLACE = XSound.std("block.azalea_leaves.place");
    public static final XSound BLOCK_AZALEA_LEAVES_STEP = XSound.std("block.azalea_leaves.step");
    public static final XSound BLOCK_AZALEA_PLACE = XSound.std("block.azalea.place");
    public static final XSound BLOCK_AZALEA_STEP = XSound.std("block.azalea.step");
    public static final XSound BLOCK_BAMBOO_BREAK = XSound.std("block.bamboo.break");
    public static final XSound BLOCK_BAMBOO_FALL = XSound.std("block.bamboo.fall");
    public static final XSound BLOCK_BAMBOO_HIT = XSound.std("block.bamboo.hit");
    public static final XSound BLOCK_BAMBOO_PLACE = XSound.std("block.bamboo.place");
    public static final XSound BLOCK_BAMBOO_SAPLING_BREAK = XSound.std("block.bamboo_sapling.break");
    public static final XSound BLOCK_BAMBOO_SAPLING_HIT = XSound.std("block.bamboo_sapling.hit");
    public static final XSound BLOCK_BAMBOO_SAPLING_PLACE = XSound.std("block.bamboo_sapling.place");
    public static final XSound BLOCK_BAMBOO_STEP = XSound.std("block.bamboo.step");
    public static final XSound BLOCK_BAMBOO_WOOD_BREAK = XSound.std("block.bamboo_wood.break");
    public static final XSound BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF = XSound.std("block.bamboo_wood_button.click_off");
    public static final XSound BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON = XSound.std("block.bamboo_wood_button.click_on");
    public static final XSound BLOCK_BAMBOO_WOOD_DOOR_CLOSE = XSound.std("block.bamboo_wood_door.close");
    public static final XSound BLOCK_BAMBOO_WOOD_DOOR_OPEN = XSound.std("block.bamboo_wood_door.open");
    public static final XSound BLOCK_BAMBOO_WOOD_FALL = XSound.std("block.bamboo_wood.fall");
    public static final XSound BLOCK_BAMBOO_WOOD_FENCE_GATE_CLOSE = XSound.std("block.bamboo_wood_fence_gate.close");
    public static final XSound BLOCK_BAMBOO_WOOD_FENCE_GATE_OPEN = XSound.std("block.bamboo_wood_fence_gate.open");
    public static final XSound BLOCK_BAMBOO_WOOD_HANGING_SIGN_BREAK = XSound.std("block.bamboo_wood_hanging_sign.break");
    public static final XSound BLOCK_BAMBOO_WOOD_HANGING_SIGN_FALL = XSound.std("block.bamboo_wood_hanging_sign.fall");
    public static final XSound BLOCK_BAMBOO_WOOD_HANGING_SIGN_HIT = XSound.std("block.bamboo_wood_hanging_sign.hit");
    public static final XSound BLOCK_BAMBOO_WOOD_HANGING_SIGN_PLACE = XSound.std("block.bamboo_wood_hanging_sign.place");
    public static final XSound BLOCK_BAMBOO_WOOD_HANGING_SIGN_STEP = XSound.std("block.bamboo_wood_hanging_sign.step");
    public static final XSound BLOCK_BAMBOO_WOOD_HIT = XSound.std("block.bamboo_wood.hit");
    public static final XSound BLOCK_BAMBOO_WOOD_PLACE = XSound.std("block.bamboo_wood.place");
    public static final XSound BLOCK_BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF = XSound.std("block.bamboo_wood_pressure_plate.click_off");
    public static final XSound BLOCK_BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON = XSound.std("block.bamboo_wood_pressure_plate.click_on");
    public static final XSound BLOCK_BAMBOO_WOOD_STEP = XSound.std("block.bamboo_wood.step");
    public static final XSound BLOCK_BAMBOO_WOOD_TRAPDOOR_CLOSE = XSound.std("block.bamboo_wood_trapdoor.close");
    public static final XSound BLOCK_BAMBOO_WOOD_TRAPDOOR_OPEN = XSound.std("block.bamboo_wood_trapdoor.open");
    public static final XSound BLOCK_BARREL_CLOSE = XSound.std("block.barrel.close");
    public static final XSound BLOCK_BARREL_OPEN = XSound.std("block.barrel.open");
    public static final XSound BLOCK_BASALT_BREAK = XSound.std("block.basalt.break");
    public static final XSound BLOCK_BASALT_FALL = XSound.std("block.basalt.fall");
    public static final XSound BLOCK_BASALT_HIT = XSound.std("block.basalt.hit");
    public static final XSound BLOCK_BASALT_PLACE = XSound.std("block.basalt.place");
    public static final XSound BLOCK_BASALT_STEP = XSound.std("block.basalt.step");
    public static final XSound BLOCK_BEACON_ACTIVATE = XSound.std("block.beacon.activate");
    public static final XSound BLOCK_BEACON_AMBIENT = XSound.std("block.beacon.ambient");
    public static final XSound BLOCK_BEEHIVE_DRIP = XSound.std("block.beehive.drip");
    public static final XSound BLOCK_BEEHIVE_ENTER = XSound.std("block.beehive.enter");
    public static final XSound BLOCK_BEEHIVE_EXIT = XSound.std("block.beehive.exit");
    public static final XSound BLOCK_BEEHIVE_SHEAR = XSound.std("block.beehive.shear");
    public static final XSound BLOCK_BEEHIVE_WORK = XSound.std("block.beehive.work");
    public static final XSound BLOCK_BELL_RESONATE = XSound.std("block.bell.resonate");
    public static final XSound BLOCK_BELL_USE = XSound.std("block.bell.use");
    public static final XSound BLOCK_BIG_DRIPLEAF_BREAK = XSound.std("block.big_dripleaf.break");
    public static final XSound BLOCK_BIG_DRIPLEAF_FALL = XSound.std("block.big_dripleaf.fall");
    public static final XSound BLOCK_BIG_DRIPLEAF_HIT = XSound.std("block.big_dripleaf.hit");
    public static final XSound BLOCK_BIG_DRIPLEAF_PLACE = XSound.std("block.big_dripleaf.place");
    public static final XSound BLOCK_BIG_DRIPLEAF_STEP = XSound.std("block.big_dripleaf.step");
    public static final XSound BLOCK_BIG_DRIPLEAF_TILT_DOWN = XSound.std("block.big_dripleaf.tilt_down");
    public static final XSound BLOCK_BIG_DRIPLEAF_TILT_UP = XSound.std("block.big_dripleaf.tilt_up");
    public static final XSound BLOCK_BLASTFURNACE_FIRE_CRACKLE = XSound.std("block.blastfurnace.fire_crackle");
    public static final XSound BLOCK_BONE_BLOCK_BREAK = XSound.std("block.bone_block.break");
    public static final XSound BLOCK_BONE_BLOCK_FALL = XSound.std("block.bone_block.fall");
    public static final XSound BLOCK_BONE_BLOCK_HIT = XSound.std("block.bone_block.hit");
    public static final XSound BLOCK_BONE_BLOCK_PLACE = XSound.std("block.bone_block.place");
    public static final XSound BLOCK_BONE_BLOCK_STEP = XSound.std("block.bone_block.step");
    public static final XSound BLOCK_BREWING_STAND_BREW = XSound.std("block.brewing_stand.brew");
    public static final XSound BLOCK_BUBBLE_COLUMN_BUBBLE_POP = XSound.std("block.bubble_column.bubble_pop");
    public static final XSound BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT = XSound.std("block.bubble_column.upwards_ambient");
    public static final XSound BLOCK_BUBBLE_COLUMN_UPWARDS_INSIDE = XSound.std("block.bubble_column.upwards_inside");
    public static final XSound BLOCK_BUBBLE_COLUMN_WHIRLPOOL_AMBIENT = XSound.std("block.bubble_column.whirlpool_ambient");
    public static final XSound BLOCK_BUBBLE_COLUMN_WHIRLPOOL_INSIDE = XSound.std("block.bubble_column.whirlpool_inside");
    public static final XSound BLOCK_CAKE_ADD_CANDLE = XSound.std("block.cake.add_candle");
    public static final XSound BLOCK_CALCITE_BREAK = XSound.std("block.calcite.break");
    public static final XSound BLOCK_CALCITE_FALL = XSound.std("block.calcite.fall");
    public static final XSound BLOCK_CALCITE_HIT = XSound.std("block.calcite.hit");
    public static final XSound BLOCK_CALCITE_PLACE = XSound.std("block.calcite.place");
    public static final XSound BLOCK_CALCITE_STEP = XSound.std("block.calcite.step");
    public static final XSound BLOCK_CAMPFIRE_CRACKLE = XSound.std("block.campfire.crackle");
    public static final XSound BLOCK_CANDLE_AMBIENT = XSound.std("block.candle.ambient");
    public static final XSound BLOCK_CANDLE_BREAK = XSound.std("block.candle.break");
    public static final XSound BLOCK_CANDLE_EXTINGUISH = XSound.std("block.candle.extinguish");
    public static final XSound BLOCK_CANDLE_FALL = XSound.std("block.candle.fall");
    public static final XSound BLOCK_CANDLE_HIT = XSound.std("block.candle.hit");
    public static final XSound BLOCK_CANDLE_PLACE = XSound.std("block.candle.place");
    public static final XSound BLOCK_CANDLE_STEP = XSound.std("block.candle.step");
    public static final XSound BLOCK_CAVE_VINES_BREAK = XSound.std("block.cave_vines.break");
    public static final XSound BLOCK_CAVE_VINES_FALL = XSound.std("block.cave_vines.fall");
    public static final XSound BLOCK_CAVE_VINES_HIT = XSound.std("block.cave_vines.hit");
    public static final XSound BLOCK_CAVE_VINES_PICK_BERRIES = XSound.std("block.cave_vines.pick_berries");
    public static final XSound BLOCK_CAVE_VINES_PLACE = XSound.std("block.cave_vines.place");
    public static final XSound BLOCK_CAVE_VINES_STEP = XSound.std("block.cave_vines.step");
    public static final XSound BLOCK_CHAIN_BREAK = XSound.std("block.chain.break");
    public static final XSound BLOCK_CHAIN_FALL = XSound.std("block.chain.fall");
    public static final XSound BLOCK_CHAIN_HIT = XSound.std("block.chain.hit");
    public static final XSound BLOCK_CHAIN_PLACE = XSound.std("block.chain.place");
    public static final XSound BLOCK_CHAIN_STEP = XSound.std("block.chain.step");
    public static final XSound BLOCK_CHERRY_LEAVES_BREAK = XSound.std("block.cherry_leaves.break");
    public static final XSound BLOCK_CHERRY_LEAVES_FALL = XSound.std("block.cherry_leaves.fall");
    public static final XSound BLOCK_CHERRY_LEAVES_HIT = XSound.std("block.cherry_leaves.hit");
    public static final XSound BLOCK_CHERRY_LEAVES_PLACE = XSound.std("block.cherry_leaves.place");
    public static final XSound BLOCK_CHERRY_LEAVES_STEP = XSound.std("block.cherry_leaves.step");
    public static final XSound BLOCK_CHERRY_SAPLING_BREAK = XSound.std("block.cherry_sapling.break");
    public static final XSound BLOCK_CHERRY_SAPLING_FALL = XSound.std("block.cherry_sapling.fall");
    public static final XSound BLOCK_CHERRY_SAPLING_HIT = XSound.std("block.cherry_sapling.hit");
    public static final XSound BLOCK_CHERRY_SAPLING_PLACE = XSound.std("block.cherry_sapling.place");
    public static final XSound BLOCK_CHERRY_SAPLING_STEP = XSound.std("block.cherry_sapling.step");
    public static final XSound BLOCK_CHERRY_WOOD_BREAK = XSound.std("block.cherry_wood.break");
    public static final XSound BLOCK_CHERRY_WOOD_BUTTON_CLICK_OFF = XSound.std("block.cherry_wood_button.click_off");
    public static final XSound BLOCK_CHERRY_WOOD_BUTTON_CLICK_ON = XSound.std("block.cherry_wood_button.click_on");
    public static final XSound BLOCK_CHERRY_WOOD_DOOR_CLOSE = XSound.std("block.cherry_wood_door.close");
    public static final XSound BLOCK_CHERRY_WOOD_DOOR_OPEN = XSound.std("block.cherry_wood_door.open");
    public static final XSound BLOCK_CHERRY_WOOD_FALL = XSound.std("block.cherry_wood.fall");
    public static final XSound BLOCK_CHERRY_WOOD_FENCE_GATE_CLOSE = XSound.std("block.cherry_wood_fence_gate.close");
    public static final XSound BLOCK_CHERRY_WOOD_FENCE_GATE_OPEN = XSound.std("block.cherry_wood_fence_gate.open");
    public static final XSound BLOCK_CHERRY_WOOD_HANGING_SIGN_BREAK = XSound.std("block.cherry_wood_hanging_sign.break");
    public static final XSound BLOCK_CHERRY_WOOD_HANGING_SIGN_FALL = XSound.std("block.cherry_wood_hanging_sign.fall");
    public static final XSound BLOCK_CHERRY_WOOD_HANGING_SIGN_HIT = XSound.std("block.cherry_wood_hanging_sign.hit");
    public static final XSound BLOCK_CHERRY_WOOD_HANGING_SIGN_PLACE = XSound.std("block.cherry_wood_hanging_sign.place");
    public static final XSound BLOCK_CHERRY_WOOD_HANGING_SIGN_STEP = XSound.std("block.cherry_wood_hanging_sign.step");
    public static final XSound BLOCK_CHERRY_WOOD_HIT = XSound.std("block.cherry_wood.hit");
    public static final XSound BLOCK_CHERRY_WOOD_PLACE = XSound.std("block.cherry_wood.place");
    public static final XSound BLOCK_CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF = XSound.std("block.cherry_wood_pressure_plate.click_off");
    public static final XSound BLOCK_CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON = XSound.std("block.cherry_wood_pressure_plate.click_on");
    public static final XSound BLOCK_CHERRY_WOOD_STEP = XSound.std("block.cherry_wood.step");
    public static final XSound BLOCK_CHERRY_WOOD_TRAPDOOR_CLOSE = XSound.std("block.cherry_wood_trapdoor.close");
    public static final XSound BLOCK_CHERRY_WOOD_TRAPDOOR_OPEN = XSound.std("block.cherry_wood_trapdoor.open");
    public static final XSound BLOCK_CHEST_LOCKED = XSound.std("block.chest.locked");
    public static final XSound BLOCK_CHISELED_BOOKSHELF_BREAK = XSound.std("block.chiseled_bookshelf.break");
    public static final XSound BLOCK_CHISELED_BOOKSHELF_FALL = XSound.std("block.chiseled_bookshelf.fall");
    public static final XSound BLOCK_CHISELED_BOOKSHELF_HIT = XSound.std("block.chiseled_bookshelf.hit");
    public static final XSound BLOCK_CHISELED_BOOKSHELF_INSERT = XSound.std("block.chiseled_bookshelf.insert");
    public static final XSound BLOCK_CHISELED_BOOKSHELF_INSERT_ENCHANTED = XSound.std("block.chiseled_bookshelf.insert.enchanted");
    public static final XSound BLOCK_CHISELED_BOOKSHELF_PICKUP = XSound.std("block.chiseled_bookshelf.pickup");
    public static final XSound BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED = XSound.std("block.chiseled_bookshelf.pickup.enchanted");
    public static final XSound BLOCK_CHISELED_BOOKSHELF_PLACE = XSound.std("block.chiseled_bookshelf.place");
    public static final XSound BLOCK_CHISELED_BOOKSHELF_STEP = XSound.std("block.chiseled_bookshelf.step");
    public static final XSound BLOCK_CHORUS_FLOWER_DEATH = XSound.std("block.chorus_flower.death");
    public static final XSound BLOCK_CHORUS_FLOWER_GROW = XSound.std("block.chorus_flower.grow");
    public static final XSound BLOCK_COBWEB_BREAK = XSound.std("block.cobweb.break");
    public static final XSound BLOCK_COBWEB_FALL = XSound.std("block.cobweb.fall");
    public static final XSound BLOCK_COBWEB_HIT = XSound.std("block.cobweb.hit");
    public static final XSound BLOCK_COBWEB_PLACE = XSound.std("block.cobweb.place");
    public static final XSound BLOCK_COBWEB_STEP = XSound.std("block.cobweb.step");
    public static final XSound BLOCK_COMPARATOR_CLICK = XSound.std("block.comparator.click");
    public static final XSound BLOCK_COMPOSTER_EMPTY = XSound.std("block.composter.empty");
    public static final XSound BLOCK_COMPOSTER_FILL = XSound.std("block.composter.fill");
    public static final XSound BLOCK_COMPOSTER_FILL_SUCCESS = XSound.std("block.composter.fill_success");
    public static final XSound BLOCK_COMPOSTER_READY = XSound.std("block.composter.ready");
    public static final XSound BLOCK_CONDUIT_ACTIVATE = XSound.std("block.conduit.activate");
    public static final XSound BLOCK_CONDUIT_AMBIENT = XSound.std("block.conduit.ambient");
    public static final XSound BLOCK_CONDUIT_AMBIENT_SHORT = XSound.std("block.conduit.ambient.short");
    public static final XSound BLOCK_CONDUIT_ATTACK_TARGET = XSound.std("block.conduit.attack.target");
    public static final XSound BLOCK_CONDUIT_DEACTIVATE = XSound.std("block.conduit.deactivate");
    public static final XSound BLOCK_COPPER_BREAK = XSound.std("block.copper.break");
    public static final XSound BLOCK_COPPER_BULB_BREAK = XSound.std("block.copper_bulb.break");
    public static final XSound BLOCK_COPPER_BULB_FALL = XSound.std("block.copper_bulb.fall");
    public static final XSound BLOCK_COPPER_BULB_HIT = XSound.std("block.copper_bulb.hit");
    public static final XSound BLOCK_COPPER_BULB_PLACE = XSound.std("block.copper_bulb.place");
    public static final XSound BLOCK_COPPER_BULB_STEP = XSound.std("block.copper_bulb.step");
    public static final XSound BLOCK_COPPER_BULB_TURN_OFF = XSound.std("block.copper_bulb.turn_off");
    public static final XSound BLOCK_COPPER_BULB_TURN_ON = XSound.std("block.copper_bulb.turn_on");
    public static final XSound BLOCK_COPPER_DOOR_CLOSE = XSound.std("block.copper_door.close");
    public static final XSound BLOCK_COPPER_DOOR_OPEN = XSound.std("block.copper_door.open");
    public static final XSound BLOCK_COPPER_FALL = XSound.std("block.copper.fall");
    public static final XSound BLOCK_COPPER_GRATE_BREAK = XSound.std("block.copper_grate.break");
    public static final XSound BLOCK_COPPER_GRATE_FALL = XSound.std("block.copper_grate.fall");
    public static final XSound BLOCK_COPPER_GRATE_HIT = XSound.std("block.copper_grate.hit");
    public static final XSound BLOCK_COPPER_GRATE_PLACE = XSound.std("block.copper_grate.place");
    public static final XSound BLOCK_COPPER_GRATE_STEP = XSound.std("block.copper_grate.step");
    public static final XSound BLOCK_COPPER_HIT = XSound.std("block.copper.hit");
    public static final XSound BLOCK_COPPER_PLACE = XSound.std("block.copper.place");
    public static final XSound BLOCK_COPPER_STEP = XSound.std("block.copper.step");
    public static final XSound BLOCK_COPPER_TRAPDOOR_CLOSE = XSound.std("block.copper_trapdoor.close");
    public static final XSound BLOCK_COPPER_TRAPDOOR_OPEN = XSound.std("block.copper_trapdoor.open");
    public static final XSound BLOCK_CORAL_BLOCK_BREAK = XSound.std("block.coral_block.break");
    public static final XSound BLOCK_CORAL_BLOCK_FALL = XSound.std("block.coral_block.fall");
    public static final XSound BLOCK_CORAL_BLOCK_HIT = XSound.std("block.coral_block.hit");
    public static final XSound BLOCK_CORAL_BLOCK_PLACE = XSound.std("block.coral_block.place");
    public static final XSound BLOCK_CORAL_BLOCK_STEP = XSound.std("block.coral_block.step");
    public static final XSound BLOCK_CRAFTER_CRAFT = XSound.std("block.crafter.craft");
    public static final XSound BLOCK_CRAFTER_FAIL = XSound.std("block.crafter.fail");
    public static final XSound BLOCK_CREAKING_HEART_BREAK = XSound.std("block.creaking_heart.break");
    public static final XSound BLOCK_CREAKING_HEART_FALL = XSound.std("block.creaking_heart.fall");
    public static final XSound BLOCK_CREAKING_HEART_HIT = XSound.std("block.creaking_heart.hit");
    public static final XSound BLOCK_CREAKING_HEART_HURT = XSound.std("block.creaking_heart.hurt");
    public static final XSound BLOCK_CREAKING_HEART_IDLE = XSound.std("block.creaking_heart.idle");
    public static final XSound BLOCK_CREAKING_HEART_PLACE = XSound.std("block.creaking_heart.place");
    public static final XSound BLOCK_CREAKING_HEART_SPAWN = XSound.std("block.creaking_heart.spawn");
    public static final XSound BLOCK_CREAKING_HEART_STEP = XSound.std("block.creaking_heart.step");
    public static final XSound BLOCK_CROP_BREAK = XSound.std("block.crop.break");
    public static final XSound BLOCK_DECORATED_POT_BREAK = XSound.std("block.decorated_pot.break");
    public static final XSound BLOCK_DECORATED_POT_FALL = XSound.std("block.decorated_pot.fall");
    public static final XSound BLOCK_DECORATED_POT_HIT = XSound.std("block.decorated_pot.hit");
    public static final XSound BLOCK_DECORATED_POT_INSERT = XSound.std("block.decorated_pot.insert");
    public static final XSound BLOCK_DECORATED_POT_INSERT_FAIL = XSound.std("block.decorated_pot.insert_fail");
    public static final XSound BLOCK_DECORATED_POT_PLACE = XSound.std("block.decorated_pot.place");
    public static final XSound BLOCK_DECORATED_POT_SHATTER = XSound.std("block.decorated_pot.shatter");
    public static final XSound BLOCK_DECORATED_POT_STEP = XSound.std("block.decorated_pot.step");
    public static final XSound BLOCK_DEEPSLATE_BREAK = XSound.std("block.deepslate.break");
    public static final XSound BLOCK_DEEPSLATE_BRICKS_BREAK = XSound.std("block.deepslate_bricks.break");
    public static final XSound BLOCK_DEEPSLATE_BRICKS_FALL = XSound.std("block.deepslate_bricks.fall");
    public static final XSound BLOCK_DEEPSLATE_BRICKS_HIT = XSound.std("block.deepslate_bricks.hit");
    public static final XSound BLOCK_DEEPSLATE_BRICKS_PLACE = XSound.std("block.deepslate_bricks.place");
    public static final XSound BLOCK_DEEPSLATE_BRICKS_STEP = XSound.std("block.deepslate_bricks.step");
    public static final XSound BLOCK_DEEPSLATE_FALL = XSound.std("block.deepslate.fall");
    public static final XSound BLOCK_DEEPSLATE_HIT = XSound.std("block.deepslate.hit");
    public static final XSound BLOCK_DEEPSLATE_PLACE = XSound.std("block.deepslate.place");
    public static final XSound BLOCK_DEEPSLATE_STEP = XSound.std("block.deepslate.step");
    public static final XSound BLOCK_DEEPSLATE_TILES_BREAK = XSound.std("block.deepslate_tiles.break");
    public static final XSound BLOCK_DEEPSLATE_TILES_FALL = XSound.std("block.deepslate_tiles.fall");
    public static final XSound BLOCK_DEEPSLATE_TILES_HIT = XSound.std("block.deepslate_tiles.hit");
    public static final XSound BLOCK_DEEPSLATE_TILES_PLACE = XSound.std("block.deepslate_tiles.place");
    public static final XSound BLOCK_DEEPSLATE_TILES_STEP = XSound.std("block.deepslate_tiles.step");
    public static final XSound BLOCK_DISPENSER_DISPENSE = XSound.std("block.dispenser.dispense");
    public static final XSound BLOCK_DISPENSER_FAIL = XSound.std("block.dispenser.fail");
    public static final XSound BLOCK_DISPENSER_LAUNCH = XSound.std("block.dispenser.launch");
    public static final XSound BLOCK_DRIPSTONE_BLOCK_BREAK = XSound.std("block.dripstone_block.break");
    public static final XSound BLOCK_DRIPSTONE_BLOCK_FALL = XSound.std("block.dripstone_block.fall");
    public static final XSound BLOCK_DRIPSTONE_BLOCK_HIT = XSound.std("block.dripstone_block.hit");
    public static final XSound BLOCK_DRIPSTONE_BLOCK_PLACE = XSound.std("block.dripstone_block.place");
    public static final XSound BLOCK_DRIPSTONE_BLOCK_STEP = XSound.std("block.dripstone_block.step");
    public static final XSound BLOCK_ENCHANTMENT_TABLE_USE = XSound.std("block.enchantment_table.use");
    public static final XSound BLOCK_ENDER_CHEST_CLOSE = XSound.std("block.ender_chest.close", "BLOCK_ENDERCHEST_CLOSE");
    public static final XSound BLOCK_ENDER_CHEST_OPEN = XSound.std("block.ender_chest.open", "BLOCK_ENDERCHEST_OPEN");
    public static final XSound BLOCK_END_GATEWAY_SPAWN = XSound.std("block.end_gateway.spawn");
    public static final XSound BLOCK_END_PORTAL_FRAME_FILL = XSound.std("block.end_portal_frame.fill");
    public static final XSound BLOCK_END_PORTAL_SPAWN = XSound.std("block.end_portal.spawn");
    public static final XSound BLOCK_FENCE_GATE_CLOSE = XSound.std("block.fence_gate.close");
    public static final XSound BLOCK_FENCE_GATE_OPEN = XSound.std("block.fence_gate.open");
    public static final XSound BLOCK_FLOWERING_AZALEA_BREAK = XSound.std("block.flowering_azalea.break");
    public static final XSound BLOCK_FLOWERING_AZALEA_FALL = XSound.std("block.flowering_azalea.fall");
    public static final XSound BLOCK_FLOWERING_AZALEA_HIT = XSound.std("block.flowering_azalea.hit");
    public static final XSound BLOCK_FLOWERING_AZALEA_PLACE = XSound.std("block.flowering_azalea.place");
    public static final XSound BLOCK_FLOWERING_AZALEA_STEP = XSound.std("block.flowering_azalea.step");
    public static final XSound BLOCK_FROGLIGHT_BREAK = XSound.std("block.froglight.break");
    public static final XSound BLOCK_FROGLIGHT_FALL = XSound.std("block.froglight.fall");
    public static final XSound BLOCK_FROGLIGHT_HIT = XSound.std("block.froglight.hit");
    public static final XSound BLOCK_FROGLIGHT_PLACE = XSound.std("block.froglight.place");
    public static final XSound BLOCK_FROGLIGHT_STEP = XSound.std("block.froglight.step");
    public static final XSound BLOCK_FROGSPAWN_BREAK = XSound.std("block.frogspawn.break");
    public static final XSound BLOCK_FROGSPAWN_FALL = XSound.std("block.frogspawn.fall");
    public static final XSound BLOCK_FROGSPAWN_HATCH = XSound.std("block.frogspawn.hatch");
    public static final XSound BLOCK_FROGSPAWN_HIT = XSound.std("block.frogspawn.hit");
    public static final XSound BLOCK_FROGSPAWN_PLACE = XSound.std("block.frogspawn.place");
    public static final XSound BLOCK_FROGSPAWN_STEP = XSound.std("block.frogspawn.step");
    public static final XSound BLOCK_FUNGUS_BREAK = XSound.std("block.fungus.break");
    public static final XSound BLOCK_FUNGUS_FALL = XSound.std("block.fungus.fall");
    public static final XSound BLOCK_FUNGUS_HIT = XSound.std("block.fungus.hit");
    public static final XSound BLOCK_FUNGUS_PLACE = XSound.std("block.fungus.place");
    public static final XSound BLOCK_FUNGUS_STEP = XSound.std("block.fungus.step");
    public static final XSound BLOCK_FURNACE_FIRE_CRACKLE = XSound.std("block.furnace.fire_crackle");
    public static final XSound BLOCK_GILDED_BLACKSTONE_BREAK = XSound.std("block.gilded_blackstone.break");
    public static final XSound BLOCK_GILDED_BLACKSTONE_FALL = XSound.std("block.gilded_blackstone.fall");
    public static final XSound BLOCK_GILDED_BLACKSTONE_HIT = XSound.std("block.gilded_blackstone.hit");
    public static final XSound BLOCK_GILDED_BLACKSTONE_PLACE = XSound.std("block.gilded_blackstone.place");
    public static final XSound BLOCK_GILDED_BLACKSTONE_STEP = XSound.std("block.gilded_blackstone.step");
    public static final XSound BLOCK_GLASS_FALL = XSound.std("block.glass.fall");
    public static final XSound BLOCK_GLASS_HIT = XSound.std("block.glass.hit");
    public static final XSound BLOCK_GLASS_PLACE = XSound.std("block.glass.place");
    public static final XSound BLOCK_GLASS_STEP = XSound.std("block.glass.step");
    public static final XSound BLOCK_GRASS_FALL = XSound.std("block.grass.fall");
    public static final XSound BLOCK_GRASS_HIT = XSound.std("block.grass.hit");
    public static final XSound BLOCK_GRASS_PLACE = XSound.std("block.grass.place");
    public static final XSound BLOCK_GRAVEL_FALL = XSound.std("block.gravel.fall");
    public static final XSound BLOCK_GRAVEL_HIT = XSound.std("block.gravel.hit");
    public static final XSound BLOCK_GRAVEL_PLACE = XSound.std("block.gravel.place");
    public static final XSound BLOCK_GRINDSTONE_USE = XSound.std("block.grindstone.use");
    public static final XSound BLOCK_GROWING_PLANT_CROP = XSound.std("block.growing_plant.crop");
    public static final XSound BLOCK_HANGING_ROOTS_BREAK = XSound.std("block.hanging_roots.break");
    public static final XSound BLOCK_HANGING_ROOTS_FALL = XSound.std("block.hanging_roots.fall");
    public static final XSound BLOCK_HANGING_ROOTS_HIT = XSound.std("block.hanging_roots.hit");
    public static final XSound BLOCK_HANGING_ROOTS_PLACE = XSound.std("block.hanging_roots.place");
    public static final XSound BLOCK_HANGING_ROOTS_STEP = XSound.std("block.hanging_roots.step");
    public static final XSound BLOCK_HANGING_SIGN_BREAK = XSound.std("block.hanging_sign.break");
    public static final XSound BLOCK_HANGING_SIGN_FALL = XSound.std("block.hanging_sign.fall");
    public static final XSound BLOCK_HANGING_SIGN_HIT = XSound.std("block.hanging_sign.hit");
    public static final XSound BLOCK_HANGING_SIGN_PLACE = XSound.std("block.hanging_sign.place");
    public static final XSound BLOCK_HANGING_SIGN_STEP = XSound.std("block.hanging_sign.step");
    public static final XSound BLOCK_HANGING_SIGN_WAXED_INTERACT_FAIL = XSound.std("block.hanging_sign.waxed_interact_fail");
    public static final XSound BLOCK_HEAVY_CORE_BREAK = XSound.std("block.heavy_core.break");
    public static final XSound BLOCK_HEAVY_CORE_FALL = XSound.std("block.heavy_core.fall");
    public static final XSound BLOCK_HEAVY_CORE_HIT = XSound.std("block.heavy_core.hit");
    public static final XSound BLOCK_HEAVY_CORE_PLACE = XSound.std("block.heavy_core.place");
    public static final XSound BLOCK_HEAVY_CORE_STEP = XSound.std("block.heavy_core.step");
    public static final XSound BLOCK_HONEY_BLOCK_BREAK = XSound.std("block.honey_block.break");
    public static final XSound BLOCK_HONEY_BLOCK_FALL = XSound.std("block.honey_block.fall");
    public static final XSound BLOCK_HONEY_BLOCK_HIT = XSound.std("block.honey_block.hit");
    public static final XSound BLOCK_HONEY_BLOCK_PLACE = XSound.std("block.honey_block.place");
    public static final XSound BLOCK_HONEY_BLOCK_SLIDE = XSound.std("block.honey_block.slide");
    public static final XSound BLOCK_HONEY_BLOCK_STEP = XSound.std("block.honey_block.step");
    public static final XSound BLOCK_IRON_DOOR_CLOSE = XSound.std("block.iron_door.close");
    public static final XSound BLOCK_IRON_DOOR_OPEN = XSound.std("block.iron_door.open");
    public static final XSound BLOCK_IRON_TRAPDOOR_CLOSE = XSound.std("block.iron_trapdoor.close");
    public static final XSound BLOCK_IRON_TRAPDOOR_OPEN = XSound.std("block.iron_trapdoor.open");
    public static final XSound BLOCK_LADDER_BREAK = XSound.std("block.ladder.break");
    public static final XSound BLOCK_LADDER_FALL = XSound.std("block.ladder.fall");
    public static final XSound BLOCK_LADDER_HIT = XSound.std("block.ladder.hit");
    public static final XSound BLOCK_LADDER_PLACE = XSound.std("block.ladder.place");
    public static final XSound BLOCK_LANTERN_BREAK = XSound.std("block.lantern.break");
    public static final XSound BLOCK_LANTERN_FALL = XSound.std("block.lantern.fall");
    public static final XSound BLOCK_LANTERN_HIT = XSound.std("block.lantern.hit");
    public static final XSound BLOCK_LANTERN_PLACE = XSound.std("block.lantern.place");
    public static final XSound BLOCK_LANTERN_STEP = XSound.std("block.lantern.step");
    public static final XSound BLOCK_LARGE_AMETHYST_BUD_BREAK = XSound.std("block.large_amethyst_bud.break");
    public static final XSound BLOCK_LARGE_AMETHYST_BUD_PLACE = XSound.std("block.large_amethyst_bud.place");
    public static final XSound BLOCK_LAVA_EXTINGUISH = XSound.std("block.lava.extinguish");
    public static final XSound BLOCK_LEVER_CLICK = XSound.std("block.lever.click");
    public static final XSound BLOCK_LODESTONE_BREAK = XSound.std("block.lodestone.break");
    public static final XSound BLOCK_LODESTONE_FALL = XSound.std("block.lodestone.fall");
    public static final XSound BLOCK_LODESTONE_HIT = XSound.std("block.lodestone.hit");
    public static final XSound BLOCK_LODESTONE_PLACE = XSound.std("block.lodestone.place");
    public static final XSound BLOCK_LODESTONE_STEP = XSound.std("block.lodestone.step");
    public static final XSound BLOCK_MANGROVE_ROOTS_BREAK = XSound.std("block.mangrove_roots.break");
    public static final XSound BLOCK_MANGROVE_ROOTS_FALL = XSound.std("block.mangrove_roots.fall");
    public static final XSound BLOCK_MANGROVE_ROOTS_HIT = XSound.std("block.mangrove_roots.hit");
    public static final XSound BLOCK_MANGROVE_ROOTS_PLACE = XSound.std("block.mangrove_roots.place");
    public static final XSound BLOCK_MANGROVE_ROOTS_STEP = XSound.std("block.mangrove_roots.step");
    public static final XSound BLOCK_MEDIUM_AMETHYST_BUD_BREAK = XSound.std("block.medium_amethyst_bud.break");
    public static final XSound BLOCK_MEDIUM_AMETHYST_BUD_PLACE = XSound.std("block.medium_amethyst_bud.place");
    public static final XSound BLOCK_METAL_BREAK = XSound.std("block.metal.break");
    public static final XSound BLOCK_METAL_FALL = XSound.std("block.metal.fall");
    public static final XSound BLOCK_METAL_HIT = XSound.std("block.metal.hit");
    public static final XSound BLOCK_METAL_PLACE = XSound.std("block.metal.place");
    public static final XSound BLOCK_METAL_STEP = XSound.std("block.metal.step");
    public static final XSound BLOCK_MOSS_BREAK = XSound.std("block.moss.break");
    public static final XSound BLOCK_MOSS_CARPET_BREAK = XSound.std("block.moss_carpet.break");
    public static final XSound BLOCK_MOSS_CARPET_FALL = XSound.std("block.moss_carpet.fall");
    public static final XSound BLOCK_MOSS_CARPET_HIT = XSound.std("block.moss_carpet.hit");
    public static final XSound BLOCK_MOSS_CARPET_PLACE = XSound.std("block.moss_carpet.place");
    public static final XSound BLOCK_MOSS_CARPET_STEP = XSound.std("block.moss_carpet.step");
    public static final XSound BLOCK_MOSS_FALL = XSound.std("block.moss.fall");
    public static final XSound BLOCK_MOSS_HIT = XSound.std("block.moss.hit");
    public static final XSound BLOCK_MOSS_PLACE = XSound.std("block.moss.place");
    public static final XSound BLOCK_MOSS_STEP = XSound.std("block.moss.step");
    public static final XSound BLOCK_MUDDY_MANGROVE_ROOTS_BREAK = XSound.std("block.muddy_mangrove_roots.break");
    public static final XSound BLOCK_MUDDY_MANGROVE_ROOTS_FALL = XSound.std("block.muddy_mangrove_roots.fall");
    public static final XSound BLOCK_MUDDY_MANGROVE_ROOTS_HIT = XSound.std("block.muddy_mangrove_roots.hit");
    public static final XSound BLOCK_MUDDY_MANGROVE_ROOTS_PLACE = XSound.std("block.muddy_mangrove_roots.place");
    public static final XSound BLOCK_MUDDY_MANGROVE_ROOTS_STEP = XSound.std("block.muddy_mangrove_roots.step");
    public static final XSound BLOCK_MUD_BREAK = XSound.std("block.mud.break");
    public static final XSound BLOCK_MUD_BRICKS_BREAK = XSound.std("block.mud_bricks.break");
    public static final XSound BLOCK_MUD_BRICKS_FALL = XSound.std("block.mud_bricks.fall");
    public static final XSound BLOCK_MUD_BRICKS_HIT = XSound.std("block.mud_bricks.hit");
    public static final XSound BLOCK_MUD_BRICKS_PLACE = XSound.std("block.mud_bricks.place");
    public static final XSound BLOCK_MUD_BRICKS_STEP = XSound.std("block.mud_bricks.step");
    public static final XSound BLOCK_MUD_FALL = XSound.std("block.mud.fall");
    public static final XSound BLOCK_MUD_HIT = XSound.std("block.mud.hit");
    public static final XSound BLOCK_MUD_PLACE = XSound.std("block.mud.place");
    public static final XSound BLOCK_MUD_STEP = XSound.std("block.mud.step");
    public static final XSound BLOCK_NETHERITE_BLOCK_BREAK = XSound.std("block.netherite_block.break");
    public static final XSound BLOCK_NETHERITE_BLOCK_FALL = XSound.std("block.netherite_block.fall");
    public static final XSound BLOCK_NETHERITE_BLOCK_HIT = XSound.std("block.netherite_block.hit");
    public static final XSound BLOCK_NETHERITE_BLOCK_PLACE = XSound.std("block.netherite_block.place");
    public static final XSound BLOCK_NETHERITE_BLOCK_STEP = XSound.std("block.netherite_block.step");
    public static final XSound BLOCK_NETHERRACK_BREAK = XSound.std("block.netherrack.break");
    public static final XSound BLOCK_NETHERRACK_FALL = XSound.std("block.netherrack.fall");
    public static final XSound BLOCK_NETHERRACK_HIT = XSound.std("block.netherrack.hit");
    public static final XSound BLOCK_NETHERRACK_PLACE = XSound.std("block.netherrack.place");
    public static final XSound BLOCK_NETHERRACK_STEP = XSound.std("block.netherrack.step");
    public static final XSound BLOCK_NETHER_BRICKS_BREAK = XSound.std("block.nether_bricks.break");
    public static final XSound BLOCK_NETHER_BRICKS_FALL = XSound.std("block.nether_bricks.fall");
    public static final XSound BLOCK_NETHER_BRICKS_HIT = XSound.std("block.nether_bricks.hit");
    public static final XSound BLOCK_NETHER_BRICKS_PLACE = XSound.std("block.nether_bricks.place");
    public static final XSound BLOCK_NETHER_BRICKS_STEP = XSound.std("block.nether_bricks.step");
    public static final XSound BLOCK_NETHER_GOLD_ORE_BREAK = XSound.std("block.nether_gold_ore.break");
    public static final XSound BLOCK_NETHER_GOLD_ORE_FALL = XSound.std("block.nether_gold_ore.fall");
    public static final XSound BLOCK_NETHER_GOLD_ORE_HIT = XSound.std("block.nether_gold_ore.hit");
    public static final XSound BLOCK_NETHER_GOLD_ORE_PLACE = XSound.std("block.nether_gold_ore.place");
    public static final XSound BLOCK_NETHER_GOLD_ORE_STEP = XSound.std("block.nether_gold_ore.step");
    public static final XSound BLOCK_NETHER_ORE_BREAK = XSound.std("block.nether_ore.break");
    public static final XSound BLOCK_NETHER_ORE_FALL = XSound.std("block.nether_ore.fall");
    public static final XSound BLOCK_NETHER_ORE_HIT = XSound.std("block.nether_ore.hit");
    public static final XSound BLOCK_NETHER_ORE_PLACE = XSound.std("block.nether_ore.place");
    public static final XSound BLOCK_NETHER_ORE_STEP = XSound.std("block.nether_ore.step");
    public static final XSound BLOCK_NETHER_SPROUTS_BREAK = XSound.std("block.nether_sprouts.break");
    public static final XSound BLOCK_NETHER_SPROUTS_FALL = XSound.std("block.nether_sprouts.fall");
    public static final XSound BLOCK_NETHER_SPROUTS_HIT = XSound.std("block.nether_sprouts.hit");
    public static final XSound BLOCK_NETHER_SPROUTS_PLACE = XSound.std("block.nether_sprouts.place");
    public static final XSound BLOCK_NETHER_SPROUTS_STEP = XSound.std("block.nether_sprouts.step");
    public static final XSound BLOCK_NETHER_WART_BREAK = XSound.std("block.nether_wart.break");
    public static final XSound BLOCK_NETHER_WOOD_BREAK = XSound.std("block.nether_wood.break");
    public static final XSound BLOCK_NETHER_WOOD_BUTTON_CLICK_OFF = XSound.std("block.nether_wood_button.click_off");
    public static final XSound BLOCK_NETHER_WOOD_BUTTON_CLICK_ON = XSound.std("block.nether_wood_button.click_on");
    public static final XSound BLOCK_NETHER_WOOD_DOOR_CLOSE = XSound.std("block.nether_wood_door.close");
    public static final XSound BLOCK_NETHER_WOOD_DOOR_OPEN = XSound.std("block.nether_wood_door.open");
    public static final XSound BLOCK_NETHER_WOOD_FALL = XSound.std("block.nether_wood.fall");
    public static final XSound BLOCK_NETHER_WOOD_FENCE_GATE_CLOSE = XSound.std("block.nether_wood_fence_gate.close");
    public static final XSound BLOCK_NETHER_WOOD_FENCE_GATE_OPEN = XSound.std("block.nether_wood_fence_gate.open");
    public static final XSound BLOCK_NETHER_WOOD_HANGING_SIGN_BREAK = XSound.std("block.nether_wood_hanging_sign.break");
    public static final XSound BLOCK_NETHER_WOOD_HANGING_SIGN_FALL = XSound.std("block.nether_wood_hanging_sign.fall");
    public static final XSound BLOCK_NETHER_WOOD_HANGING_SIGN_HIT = XSound.std("block.nether_wood_hanging_sign.hit");
    public static final XSound BLOCK_NETHER_WOOD_HANGING_SIGN_PLACE = XSound.std("block.nether_wood_hanging_sign.place");
    public static final XSound BLOCK_NETHER_WOOD_HANGING_SIGN_STEP = XSound.std("block.nether_wood_hanging_sign.step");
    public static final XSound BLOCK_NETHER_WOOD_HIT = XSound.std("block.nether_wood.hit");
    public static final XSound BLOCK_NETHER_WOOD_PLACE = XSound.std("block.nether_wood.place");
    public static final XSound BLOCK_NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF = XSound.std("block.nether_wood_pressure_plate.click_off");
    public static final XSound BLOCK_NETHER_WOOD_PRESSURE_PLATE_CLICK_ON = XSound.std("block.nether_wood_pressure_plate.click_on");
    public static final XSound BLOCK_NETHER_WOOD_STEP = XSound.std("block.nether_wood.step");
    public static final XSound BLOCK_NETHER_WOOD_TRAPDOOR_CLOSE = XSound.std("block.nether_wood_trapdoor.close");
    public static final XSound BLOCK_NETHER_WOOD_TRAPDOOR_OPEN = XSound.std("block.nether_wood_trapdoor.open");
    public static final XSound BLOCK_NOTE_BLOCK_BANJO = XSound.std("block.note_block.banjo");
    public static final XSound BLOCK_NOTE_BLOCK_BIT = XSound.std("block.note_block.bit");
    public static final XSound BLOCK_NOTE_BLOCK_COW_BELL = XSound.std("block.note_block.cow_bell");
    public static final XSound BLOCK_NOTE_BLOCK_DIDGERIDOO = XSound.std("block.note_block.didgeridoo");
    public static final XSound BLOCK_NOTE_BLOCK_IMITATE_CREEPER = XSound.std("block.note_block.imitate.creeper");
    public static final XSound BLOCK_NOTE_BLOCK_IMITATE_ENDER_DRAGON = XSound.std("block.note_block.imitate.ender_dragon");
    public static final XSound BLOCK_NOTE_BLOCK_IMITATE_PIGLIN = XSound.std("block.note_block.imitate.piglin");
    public static final XSound BLOCK_NOTE_BLOCK_IMITATE_SKELETON = XSound.std("block.note_block.imitate.skeleton");
    public static final XSound BLOCK_NOTE_BLOCK_IMITATE_WITHER_SKELETON = XSound.std("block.note_block.imitate.wither_skeleton");
    public static final XSound BLOCK_NOTE_BLOCK_IMITATE_ZOMBIE = XSound.std("block.note_block.imitate.zombie");
    public static final XSound BLOCK_NOTE_BLOCK_IRON_XYLOPHONE = XSound.std("block.note_block.iron_xylophone");
    public static final XSound BLOCK_NYLIUM_BREAK = XSound.std("block.nylium.break");
    public static final XSound BLOCK_NYLIUM_FALL = XSound.std("block.nylium.fall");
    public static final XSound BLOCK_NYLIUM_HIT = XSound.std("block.nylium.hit");
    public static final XSound BLOCK_NYLIUM_PLACE = XSound.std("block.nylium.place");
    public static final XSound BLOCK_NYLIUM_STEP = XSound.std("block.nylium.step");
    public static final XSound BLOCK_PACKED_MUD_BREAK = XSound.std("block.packed_mud.break");
    public static final XSound BLOCK_PACKED_MUD_FALL = XSound.std("block.packed_mud.fall");
    public static final XSound BLOCK_PACKED_MUD_HIT = XSound.std("block.packed_mud.hit");
    public static final XSound BLOCK_PACKED_MUD_PLACE = XSound.std("block.packed_mud.place");
    public static final XSound BLOCK_PACKED_MUD_STEP = XSound.std("block.packed_mud.step");
    public static final XSound BLOCK_PALE_HANGING_MOSS_IDLE = XSound.std("block.pale_hanging_moss.idle");
    public static final XSound BLOCK_PINK_PETALS_BREAK = XSound.std("block.pink_petals.break");
    public static final XSound BLOCK_PINK_PETALS_FALL = XSound.std("block.pink_petals.fall");
    public static final XSound BLOCK_PINK_PETALS_HIT = XSound.std("block.pink_petals.hit");
    public static final XSound BLOCK_PINK_PETALS_PLACE = XSound.std("block.pink_petals.place");
    public static final XSound BLOCK_PINK_PETALS_STEP = XSound.std("block.pink_petals.step");
    public static final XSound BLOCK_POINTED_DRIPSTONE_BREAK = XSound.std("block.pointed_dripstone.break");
    public static final XSound BLOCK_POINTED_DRIPSTONE_DRIP_LAVA = XSound.std("block.pointed_dripstone.drip_lava");
    public static final XSound BLOCK_POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON = XSound.std("block.pointed_dripstone.drip_lava_into_cauldron");
    public static final XSound BLOCK_POINTED_DRIPSTONE_DRIP_WATER = XSound.std("block.pointed_dripstone.drip_water");
    public static final XSound BLOCK_POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON = XSound.std("block.pointed_dripstone.drip_water_into_cauldron");
    public static final XSound BLOCK_POINTED_DRIPSTONE_FALL = XSound.std("block.pointed_dripstone.fall");
    public static final XSound BLOCK_POINTED_DRIPSTONE_HIT = XSound.std("block.pointed_dripstone.hit");
    public static final XSound BLOCK_POINTED_DRIPSTONE_LAND = XSound.std("block.pointed_dripstone.land");
    public static final XSound BLOCK_POINTED_DRIPSTONE_PLACE = XSound.std("block.pointed_dripstone.place");
    public static final XSound BLOCK_POINTED_DRIPSTONE_STEP = XSound.std("block.pointed_dripstone.step");
    public static final XSound BLOCK_POLISHED_DEEPSLATE_BREAK = XSound.std("block.polished_deepslate.break");
    public static final XSound BLOCK_POLISHED_DEEPSLATE_FALL = XSound.std("block.polished_deepslate.fall");
    public static final XSound BLOCK_POLISHED_DEEPSLATE_HIT = XSound.std("block.polished_deepslate.hit");
    public static final XSound BLOCK_POLISHED_DEEPSLATE_PLACE = XSound.std("block.polished_deepslate.place");
    public static final XSound BLOCK_POLISHED_DEEPSLATE_STEP = XSound.std("block.polished_deepslate.step");
    public static final XSound BLOCK_POLISHED_TUFF_BREAK = XSound.std("block.polished_tuff.break");
    public static final XSound BLOCK_POLISHED_TUFF_FALL = XSound.std("block.polished_tuff.fall");
    public static final XSound BLOCK_POLISHED_TUFF_HIT = XSound.std("block.polished_tuff.hit");
    public static final XSound BLOCK_POLISHED_TUFF_PLACE = XSound.std("block.polished_tuff.place");
    public static final XSound BLOCK_POLISHED_TUFF_STEP = XSound.std("block.polished_tuff.step");
    public static final XSound BLOCK_POWDER_SNOW_BREAK = XSound.std("block.powder_snow.break");
    public static final XSound BLOCK_POWDER_SNOW_FALL = XSound.std("block.powder_snow.fall");
    public static final XSound BLOCK_POWDER_SNOW_HIT = XSound.std("block.powder_snow.hit");
    public static final XSound BLOCK_POWDER_SNOW_PLACE = XSound.std("block.powder_snow.place");
    public static final XSound BLOCK_POWDER_SNOW_STEP = XSound.std("block.powder_snow.step");
    public static final XSound BLOCK_PUMPKIN_CARVE = XSound.std("block.pumpkin.carve");
    public static final XSound BLOCK_REDSTONE_TORCH_BURNOUT = XSound.std("block.redstone_torch.burnout");
    public static final XSound BLOCK_RESPAWN_ANCHOR_AMBIENT = XSound.std("block.respawn_anchor.ambient");
    public static final XSound BLOCK_RESPAWN_ANCHOR_CHARGE = XSound.std("block.respawn_anchor.charge");
    public static final XSound BLOCK_RESPAWN_ANCHOR_DEPLETE = XSound.std("block.respawn_anchor.deplete");
    public static final XSound BLOCK_RESPAWN_ANCHOR_SET_SPAWN = XSound.std("block.respawn_anchor.set_spawn");
    public static final XSound BLOCK_ROOTED_DIRT_BREAK = XSound.std("block.rooted_dirt.break");
    public static final XSound BLOCK_ROOTED_DIRT_FALL = XSound.std("block.rooted_dirt.fall");
    public static final XSound BLOCK_ROOTED_DIRT_HIT = XSound.std("block.rooted_dirt.hit");
    public static final XSound BLOCK_ROOTED_DIRT_PLACE = XSound.std("block.rooted_dirt.place");
    public static final XSound BLOCK_ROOTED_DIRT_STEP = XSound.std("block.rooted_dirt.step");
    public static final XSound BLOCK_ROOTS_BREAK = XSound.std("block.roots.break");
    public static final XSound BLOCK_ROOTS_FALL = XSound.std("block.roots.fall");
    public static final XSound BLOCK_ROOTS_HIT = XSound.std("block.roots.hit");
    public static final XSound BLOCK_ROOTS_PLACE = XSound.std("block.roots.place");
    public static final XSound BLOCK_ROOTS_STEP = XSound.std("block.roots.step");
    public static final XSound BLOCK_SAND_FALL = XSound.std("block.sand.fall");
    public static final XSound BLOCK_SAND_HIT = XSound.std("block.sand.hit");
    public static final XSound BLOCK_SAND_PLACE = XSound.std("block.sand.place");
    public static final XSound BLOCK_SCAFFOLDING_BREAK = XSound.std("block.scaffolding.break");
    public static final XSound BLOCK_SCAFFOLDING_FALL = XSound.std("block.scaffolding.fall");
    public static final XSound BLOCK_SCAFFOLDING_HIT = XSound.std("block.scaffolding.hit");
    public static final XSound BLOCK_SCAFFOLDING_PLACE = XSound.std("block.scaffolding.place");
    public static final XSound BLOCK_SCAFFOLDING_STEP = XSound.std("block.scaffolding.step");
    public static final XSound BLOCK_SCULK_BREAK = XSound.std("block.sculk.break");
    public static final XSound BLOCK_SCULK_CATALYST_BLOOM = XSound.std("block.sculk_catalyst.bloom");
    public static final XSound BLOCK_SCULK_CATALYST_BREAK = XSound.std("block.sculk_catalyst.break");
    public static final XSound BLOCK_SCULK_CATALYST_FALL = XSound.std("block.sculk_catalyst.fall");
    public static final XSound BLOCK_SCULK_CATALYST_HIT = XSound.std("block.sculk_catalyst.hit");
    public static final XSound BLOCK_SCULK_CATALYST_PLACE = XSound.std("block.sculk_catalyst.place");
    public static final XSound BLOCK_SCULK_CATALYST_STEP = XSound.std("block.sculk_catalyst.step");
    public static final XSound BLOCK_SCULK_CHARGE = XSound.std("block.sculk.charge");
    public static final XSound BLOCK_SCULK_FALL = XSound.std("block.sculk.fall");
    public static final XSound BLOCK_SCULK_HIT = XSound.std("block.sculk.hit");
    public static final XSound BLOCK_SCULK_PLACE = XSound.std("block.sculk.place");
    public static final XSound BLOCK_SCULK_SENSOR_BREAK = XSound.std("block.sculk_sensor.break");
    public static final XSound BLOCK_SCULK_SENSOR_CLICKING = XSound.std("block.sculk_sensor.clicking");
    public static final XSound BLOCK_SCULK_SENSOR_CLICKING_STOP = XSound.std("block.sculk_sensor.clicking_stop");
    public static final XSound BLOCK_SCULK_SENSOR_FALL = XSound.std("block.sculk_sensor.fall");
    public static final XSound BLOCK_SCULK_SENSOR_HIT = XSound.std("block.sculk_sensor.hit");
    public static final XSound BLOCK_SCULK_SENSOR_PLACE = XSound.std("block.sculk_sensor.place");
    public static final XSound BLOCK_SCULK_SENSOR_STEP = XSound.std("block.sculk_sensor.step");
    public static final XSound BLOCK_SCULK_SHRIEKER_BREAK = XSound.std("block.sculk_shrieker.break");
    public static final XSound BLOCK_SCULK_SHRIEKER_FALL = XSound.std("block.sculk_shrieker.fall");
    public static final XSound BLOCK_SCULK_SHRIEKER_HIT = XSound.std("block.sculk_shrieker.hit");
    public static final XSound BLOCK_SCULK_SHRIEKER_PLACE = XSound.std("block.sculk_shrieker.place");
    public static final XSound BLOCK_SCULK_SHRIEKER_SHRIEK = XSound.std("block.sculk_shrieker.shriek");
    public static final XSound BLOCK_SCULK_SHRIEKER_STEP = XSound.std("block.sculk_shrieker.step");
    public static final XSound BLOCK_SCULK_SPREAD = XSound.std("block.sculk.spread");
    public static final XSound BLOCK_SCULK_STEP = XSound.std("block.sculk.step");
    public static final XSound BLOCK_SCULK_VEIN_BREAK = XSound.std("block.sculk_vein.break");
    public static final XSound BLOCK_SCULK_VEIN_FALL = XSound.std("block.sculk_vein.fall");
    public static final XSound BLOCK_SCULK_VEIN_HIT = XSound.std("block.sculk_vein.hit");
    public static final XSound BLOCK_SCULK_VEIN_PLACE = XSound.std("block.sculk_vein.place");
    public static final XSound BLOCK_SCULK_VEIN_STEP = XSound.std("block.sculk_vein.step");
    public static final XSound BLOCK_SHROOMLIGHT_BREAK = XSound.std("block.shroomlight.break");
    public static final XSound BLOCK_SHROOMLIGHT_FALL = XSound.std("block.shroomlight.fall");
    public static final XSound BLOCK_SHROOMLIGHT_HIT = XSound.std("block.shroomlight.hit");
    public static final XSound BLOCK_SHROOMLIGHT_PLACE = XSound.std("block.shroomlight.place");
    public static final XSound BLOCK_SHROOMLIGHT_STEP = XSound.std("block.shroomlight.step");
    public static final XSound BLOCK_SHULKER_BOX_CLOSE = XSound.std("block.shulker_box.close");
    public static final XSound BLOCK_SHULKER_BOX_OPEN = XSound.std("block.shulker_box.open");
    public static final XSound BLOCK_SIGN_WAXED_INTERACT_FAIL = XSound.std("block.sign.waxed_interact_fail");
    public static final XSound BLOCK_SMALL_AMETHYST_BUD_BREAK = XSound.std("block.small_amethyst_bud.break");
    public static final XSound BLOCK_SMALL_AMETHYST_BUD_PLACE = XSound.std("block.small_amethyst_bud.place");
    public static final XSound BLOCK_SMALL_DRIPLEAF_BREAK = XSound.std("block.small_dripleaf.break");
    public static final XSound BLOCK_SMALL_DRIPLEAF_FALL = XSound.std("block.small_dripleaf.fall");
    public static final XSound BLOCK_SMALL_DRIPLEAF_HIT = XSound.std("block.small_dripleaf.hit");
    public static final XSound BLOCK_SMALL_DRIPLEAF_PLACE = XSound.std("block.small_dripleaf.place");
    public static final XSound BLOCK_SMALL_DRIPLEAF_STEP = XSound.std("block.small_dripleaf.step");
    public static final XSound BLOCK_SMITHING_TABLE_USE = XSound.std("block.smithing_table.use");
    public static final XSound BLOCK_SMOKER_SMOKE = XSound.std("block.smoker.smoke");
    public static final XSound BLOCK_SNIFFER_EGG_CRACK = XSound.std("block.sniffer_egg.crack");
    public static final XSound BLOCK_SNIFFER_EGG_HATCH = XSound.std("block.sniffer_egg.hatch");
    public static final XSound BLOCK_SNIFFER_EGG_PLOP = XSound.std("block.sniffer_egg.plop");
    public static final XSound BLOCK_SNOW_FALL = XSound.std("block.snow.fall");
    public static final XSound BLOCK_SNOW_HIT = XSound.std("block.snow.hit");
    public static final XSound BLOCK_SNOW_PLACE = XSound.std("block.snow.place");
    public static final XSound BLOCK_SOUL_SAND_BREAK = XSound.std("block.soul_sand.break");
    public static final XSound BLOCK_SOUL_SAND_FALL = XSound.std("block.soul_sand.fall");
    public static final XSound BLOCK_SOUL_SAND_HIT = XSound.std("block.soul_sand.hit");
    public static final XSound BLOCK_SOUL_SAND_PLACE = XSound.std("block.soul_sand.place");
    public static final XSound BLOCK_SOUL_SAND_STEP = XSound.std("block.soul_sand.step");
    public static final XSound BLOCK_SOUL_SOIL_BREAK = XSound.std("block.soul_soil.break");
    public static final XSound BLOCK_SOUL_SOIL_FALL = XSound.std("block.soul_soil.fall");
    public static final XSound BLOCK_SOUL_SOIL_HIT = XSound.std("block.soul_soil.hit");
    public static final XSound BLOCK_SOUL_SOIL_PLACE = XSound.std("block.soul_soil.place");
    public static final XSound BLOCK_SOUL_SOIL_STEP = XSound.std("block.soul_soil.step");
    public static final XSound BLOCK_SPAWNER_BREAK = XSound.std("block.spawner.break");
    public static final XSound BLOCK_SPAWNER_FALL = XSound.std("block.spawner.fall");
    public static final XSound BLOCK_SPAWNER_HIT = XSound.std("block.spawner.hit");
    public static final XSound BLOCK_SPAWNER_PLACE = XSound.std("block.spawner.place");
    public static final XSound BLOCK_SPAWNER_STEP = XSound.std("block.spawner.step");
    public static final XSound BLOCK_SPONGE_ABSORB = XSound.std("block.sponge.absorb");
    public static final XSound BLOCK_SPONGE_BREAK = XSound.std("block.sponge.break");
    public static final XSound BLOCK_SPONGE_FALL = XSound.std("block.sponge.fall");
    public static final XSound BLOCK_SPONGE_HIT = XSound.std("block.sponge.hit");
    public static final XSound BLOCK_SPONGE_PLACE = XSound.std("block.sponge.place");
    public static final XSound BLOCK_SPONGE_STEP = XSound.std("block.sponge.step");
    public static final XSound BLOCK_SPORE_BLOSSOM_BREAK = XSound.std("block.spore_blossom.break");
    public static final XSound BLOCK_SPORE_BLOSSOM_FALL = XSound.std("block.spore_blossom.fall");
    public static final XSound BLOCK_SPORE_BLOSSOM_HIT = XSound.std("block.spore_blossom.hit");
    public static final XSound BLOCK_SPORE_BLOSSOM_PLACE = XSound.std("block.spore_blossom.place");
    public static final XSound BLOCK_SPORE_BLOSSOM_STEP = XSound.std("block.spore_blossom.step");
    public static final XSound BLOCK_STEM_BREAK = XSound.std("block.stem.break");
    public static final XSound BLOCK_STEM_FALL = XSound.std("block.stem.fall");
    public static final XSound BLOCK_STEM_HIT = XSound.std("block.stem.hit");
    public static final XSound BLOCK_STEM_PLACE = XSound.std("block.stem.place");
    public static final XSound BLOCK_STEM_STEP = XSound.std("block.stem.step");
    public static final XSound BLOCK_STONE_BUTTON_CLICK_OFF = XSound.std("block.stone_button.click_off");
    public static final XSound BLOCK_STONE_BUTTON_CLICK_ON = XSound.std("block.stone_button.click_on");
    public static final XSound BLOCK_STONE_FALL = XSound.std("block.stone.fall");
    public static final XSound BLOCK_STONE_HIT = XSound.std("block.stone.hit");
    public static final XSound BLOCK_STONE_PLACE = XSound.std("block.stone.place");
    public static final XSound BLOCK_SUSPICIOUS_GRAVEL_BREAK = XSound.std("block.suspicious_gravel.break");
    public static final XSound BLOCK_SUSPICIOUS_GRAVEL_FALL = XSound.std("block.suspicious_gravel.fall");
    public static final XSound BLOCK_SUSPICIOUS_GRAVEL_HIT = XSound.std("block.suspicious_gravel.hit");
    public static final XSound BLOCK_SUSPICIOUS_GRAVEL_PLACE = XSound.std("block.suspicious_gravel.place");
    public static final XSound BLOCK_SUSPICIOUS_GRAVEL_STEP = XSound.std("block.suspicious_gravel.step");
    public static final XSound BLOCK_SUSPICIOUS_SAND_BREAK = XSound.std("block.suspicious_sand.break");
    public static final XSound BLOCK_SUSPICIOUS_SAND_FALL = XSound.std("block.suspicious_sand.fall");
    public static final XSound BLOCK_SUSPICIOUS_SAND_HIT = XSound.std("block.suspicious_sand.hit");
    public static final XSound BLOCK_SUSPICIOUS_SAND_PLACE = XSound.std("block.suspicious_sand.place");
    public static final XSound BLOCK_SUSPICIOUS_SAND_STEP = XSound.std("block.suspicious_sand.step");
    public static final XSound BLOCK_SWEET_BERRY_BUSH_BREAK = XSound.std("block.sweet_berry_bush.break");
    public static final XSound BLOCK_SWEET_BERRY_BUSH_PLACE = XSound.std("block.sweet_berry_bush.place");
    public static final XSound BLOCK_TRIAL_SPAWNER_ABOUT_TO_SPAWN_ITEM = XSound.std("block.trial_spawner.about_to_spawn_item");
    public static final XSound BLOCK_TRIAL_SPAWNER_AMBIENT = XSound.std("block.trial_spawner.ambient");
    public static final XSound BLOCK_TRIAL_SPAWNER_AMBIENT_OMINOUS = XSound.std("block.trial_spawner.ambient_ominous");
    public static final XSound BLOCK_TRIAL_SPAWNER_BREAK = XSound.std("block.trial_spawner.break");
    public static final XSound BLOCK_TRIAL_SPAWNER_CLOSE_SHUTTER = XSound.std("block.trial_spawner.close_shutter");
    public static final XSound BLOCK_TRIAL_SPAWNER_DETECT_PLAYER = XSound.std("block.trial_spawner.detect_player");
    public static final XSound BLOCK_TRIAL_SPAWNER_EJECT_ITEM = XSound.std("block.trial_spawner.eject_item");
    public static final XSound BLOCK_TRIAL_SPAWNER_FALL = XSound.std("block.trial_spawner.fall");
    public static final XSound BLOCK_TRIAL_SPAWNER_HIT = XSound.std("block.trial_spawner.hit");
    public static final XSound BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE = XSound.std("block.trial_spawner.ominous_activate");
    public static final XSound BLOCK_TRIAL_SPAWNER_OPEN_SHUTTER = XSound.std("block.trial_spawner.open_shutter");
    public static final XSound BLOCK_TRIAL_SPAWNER_PLACE = XSound.std("block.trial_spawner.place");
    public static final XSound BLOCK_TRIAL_SPAWNER_SPAWN_ITEM = XSound.std("block.trial_spawner.spawn_item");
    public static final XSound BLOCK_TRIAL_SPAWNER_SPAWN_ITEM_BEGIN = XSound.std("block.trial_spawner.spawn_item_begin");
    public static final XSound BLOCK_TRIAL_SPAWNER_SPAWN_MOB = XSound.std("block.trial_spawner.spawn_mob");
    public static final XSound BLOCK_TRIAL_SPAWNER_STEP = XSound.std("block.trial_spawner.step");
    public static final XSound BLOCK_TRIPWIRE_ATTACH = XSound.std("block.tripwire.attach");
    public static final XSound BLOCK_TRIPWIRE_CLICK_OFF = XSound.std("block.tripwire.click_off");
    public static final XSound BLOCK_TRIPWIRE_CLICK_ON = XSound.std("block.tripwire.click_on");
    public static final XSound BLOCK_TRIPWIRE_DETACH = XSound.std("block.tripwire.detach");
    public static final XSound BLOCK_TUFF_BREAK = XSound.std("block.tuff.break");
    public static final XSound BLOCK_TUFF_BRICKS_BREAK = XSound.std("block.tuff_bricks.break");
    public static final XSound BLOCK_TUFF_BRICKS_FALL = XSound.std("block.tuff_bricks.fall");
    public static final XSound BLOCK_TUFF_BRICKS_HIT = XSound.std("block.tuff_bricks.hit");
    public static final XSound BLOCK_TUFF_BRICKS_PLACE = XSound.std("block.tuff_bricks.place");
    public static final XSound BLOCK_TUFF_BRICKS_STEP = XSound.std("block.tuff_bricks.step");
    public static final XSound BLOCK_TUFF_FALL = XSound.std("block.tuff.fall");
    public static final XSound BLOCK_TUFF_HIT = XSound.std("block.tuff.hit");
    public static final XSound BLOCK_TUFF_PLACE = XSound.std("block.tuff.place");
    public static final XSound BLOCK_TUFF_STEP = XSound.std("block.tuff.step");
    public static final XSound BLOCK_VAULT_ACTIVATE = XSound.std("block.vault.activate");
    public static final XSound BLOCK_VAULT_AMBIENT = XSound.std("block.vault.ambient");
    public static final XSound BLOCK_VAULT_BREAK = XSound.std("block.vault.break");
    public static final XSound BLOCK_VAULT_CLOSE_SHUTTER = XSound.std("block.vault.close_shutter");
    public static final XSound BLOCK_VAULT_DEACTIVATE = XSound.std("block.vault.deactivate");
    public static final XSound BLOCK_VAULT_EJECT_ITEM = XSound.std("block.vault.eject_item");
    public static final XSound BLOCK_VAULT_FALL = XSound.std("block.vault.fall");
    public static final XSound BLOCK_VAULT_HIT = XSound.std("block.vault.hit");
    public static final XSound BLOCK_VAULT_INSERT_ITEM = XSound.std("block.vault.insert_item");
    public static final XSound BLOCK_VAULT_INSERT_ITEM_FAIL = XSound.std("block.vault.insert_item_fail");
    public static final XSound BLOCK_VAULT_OPEN_SHUTTER = XSound.std("block.vault.open_shutter");
    public static final XSound BLOCK_VAULT_PLACE = XSound.std("block.vault.place");
    public static final XSound BLOCK_VAULT_REJECT_REWARDED_PLAYER = XSound.std("block.vault.reject_rewarded_player");
    public static final XSound BLOCK_VAULT_STEP = XSound.std("block.vault.step");
    public static final XSound BLOCK_VINE_BREAK = XSound.std("block.vine.break");
    public static final XSound BLOCK_VINE_FALL = XSound.std("block.vine.fall");
    public static final XSound BLOCK_VINE_HIT = XSound.std("block.vine.hit");
    public static final XSound BLOCK_VINE_PLACE = XSound.std("block.vine.place");
    public static final XSound BLOCK_VINE_STEP = XSound.std("block.vine.step");
    public static final XSound BLOCK_WART_BLOCK_BREAK = XSound.std("block.wart_block.break");
    public static final XSound BLOCK_WART_BLOCK_FALL = XSound.std("block.wart_block.fall");
    public static final XSound BLOCK_WART_BLOCK_HIT = XSound.std("block.wart_block.hit");
    public static final XSound BLOCK_WART_BLOCK_PLACE = XSound.std("block.wart_block.place");
    public static final XSound BLOCK_WART_BLOCK_STEP = XSound.std("block.wart_block.step");
    public static final XSound BLOCK_WEEPING_VINES_BREAK = XSound.std("block.weeping_vines.break");
    public static final XSound BLOCK_WEEPING_VINES_FALL = XSound.std("block.weeping_vines.fall");
    public static final XSound BLOCK_WEEPING_VINES_HIT = XSound.std("block.weeping_vines.hit");
    public static final XSound BLOCK_WEEPING_VINES_PLACE = XSound.std("block.weeping_vines.place");
    public static final XSound BLOCK_WEEPING_VINES_STEP = XSound.std("block.weeping_vines.step");
    public static final XSound BLOCK_WET_GRASS_BREAK = XSound.std("block.wet_grass.break");
    public static final XSound BLOCK_WET_GRASS_FALL = XSound.std("block.wet_grass.fall");
    public static final XSound BLOCK_WET_GRASS_HIT = XSound.std("block.wet_grass.hit");
    public static final XSound BLOCK_WET_SPONGE_BREAK = XSound.std("block.wet_sponge.break");
    public static final XSound BLOCK_WET_SPONGE_DRIES = XSound.std("block.wet_sponge.dries");
    public static final XSound BLOCK_WET_SPONGE_FALL = XSound.std("block.wet_sponge.fall");
    public static final XSound BLOCK_WET_SPONGE_HIT = XSound.std("block.wet_sponge.hit");
    public static final XSound BLOCK_WET_SPONGE_PLACE = XSound.std("block.wet_sponge.place");
    public static final XSound BLOCK_WET_SPONGE_STEP = XSound.std("block.wet_sponge.step");
    public static final XSound BLOCK_WOODEN_TRAPDOOR_CLOSE = XSound.std("block.wooden_trapdoor.close");
    public static final XSound BLOCK_WOODEN_TRAPDOOR_OPEN = XSound.std("block.wooden_trapdoor.open");
    public static final XSound BLOCK_WOOD_FALL = XSound.std("block.wood.fall");
    public static final XSound BLOCK_WOOD_HIT = XSound.std("block.wood.hit");
    public static final XSound BLOCK_WOOD_PLACE = XSound.std("block.wood.place");
    public static final XSound BLOCK_WOOL_FALL = XSound.std("block.wool.fall");
    public static final XSound ENCHANT_THORNS_HIT = XSound.std("enchant.thorns.hit");
    public static final XSound ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM = XSound.std("entity.allay.ambient_without_item");
    public static final XSound ENTITY_ALLAY_AMBIENT_WITH_ITEM = XSound.std("entity.allay.ambient_with_item");
    public static final XSound ENTITY_ALLAY_DEATH = XSound.std("entity.allay.death");
    public static final XSound ENTITY_ALLAY_HURT = XSound.std("entity.allay.hurt");
    public static final XSound ENTITY_ALLAY_ITEM_GIVEN = XSound.std("entity.allay.item_given");
    public static final XSound ENTITY_ALLAY_ITEM_TAKEN = XSound.std("entity.allay.item_taken");
    public static final XSound ENTITY_ALLAY_ITEM_THROWN = XSound.std("entity.allay.item_thrown");
    public static final XSound ENTITY_ARMADILLO_AMBIENT = XSound.std("entity.armadillo.ambient");
    public static final XSound ENTITY_ARMADILLO_BRUSH = XSound.std("entity.armadillo.brush");
    public static final XSound ENTITY_ARMADILLO_DEATH = XSound.std("entity.armadillo.death");
    public static final XSound ENTITY_ARMADILLO_EAT = XSound.std("entity.armadillo.eat");
    public static final XSound ENTITY_ARMADILLO_HURT = XSound.std("entity.armadillo.hurt");
    public static final XSound ENTITY_ARMADILLO_HURT_REDUCED = XSound.std("entity.armadillo.hurt_reduced");
    public static final XSound ENTITY_ARMADILLO_LAND = XSound.std("entity.armadillo.land");
    public static final XSound ENTITY_ARMADILLO_PEEK = XSound.std("entity.armadillo.peek");
    public static final XSound ENTITY_ARMADILLO_ROLL = XSound.std("entity.armadillo.roll");
    public static final XSound ENTITY_ARMADILLO_SCUTE_DROP = XSound.std("entity.armadillo.scute_drop");
    public static final XSound ENTITY_ARMADILLO_STEP = XSound.std("entity.armadillo.step");
    public static final XSound ENTITY_ARMADILLO_UNROLL_FINISH = XSound.std("entity.armadillo.unroll_finish");
    public static final XSound ENTITY_ARMADILLO_UNROLL_START = XSound.std("entity.armadillo.unroll_start");
    public static final XSound ENTITY_AXOLOTL_ATTACK = XSound.std("entity.axolotl.attack");
    public static final XSound ENTITY_AXOLOTL_DEATH = XSound.std("entity.axolotl.death");
    public static final XSound ENTITY_AXOLOTL_HURT = XSound.std("entity.axolotl.hurt");
    public static final XSound ENTITY_AXOLOTL_IDLE_AIR = XSound.std("entity.axolotl.idle_air");
    public static final XSound ENTITY_AXOLOTL_IDLE_WATER = XSound.std("entity.axolotl.idle_water");
    public static final XSound ENTITY_AXOLOTL_SPLASH = XSound.std("entity.axolotl.splash");
    public static final XSound ENTITY_AXOLOTL_SWIM = XSound.std("entity.axolotl.swim");
    public static final XSound ENTITY_BEE_DEATH = XSound.std("entity.bee.death");
    public static final XSound ENTITY_BEE_HURT = XSound.std("entity.bee.hurt");
    public static final XSound ENTITY_BEE_LOOP = XSound.std("entity.bee.loop");
    public static final XSound ENTITY_BEE_LOOP_AGGRESSIVE = XSound.std("entity.bee.loop_aggressive");
    public static final XSound ENTITY_BEE_POLLINATE = XSound.std("entity.bee.pollinate");
    public static final XSound ENTITY_BEE_STING = XSound.std("entity.bee.sting");
    public static final XSound ENTITY_BLAZE_BURN = XSound.std("entity.blaze.burn");
    public static final XSound ENTITY_BLAZE_SHOOT = XSound.std("entity.blaze.shoot");
    public static final XSound ENTITY_BOAT_PADDLE_LAND = XSound.std("entity.boat.paddle_land");
    public static final XSound ENTITY_BOAT_PADDLE_WATER = XSound.std("entity.boat.paddle_water");
    public static final XSound ENTITY_BOGGED_AMBIENT = XSound.std("entity.bogged.ambient");
    public static final XSound ENTITY_BOGGED_DEATH = XSound.std("entity.bogged.death");
    public static final XSound ENTITY_BOGGED_HURT = XSound.std("entity.bogged.hurt");
    public static final XSound ENTITY_BOGGED_SHEAR = XSound.std("entity.bogged.shear");
    public static final XSound ENTITY_BOGGED_STEP = XSound.std("entity.bogged.step");
    public static final XSound ENTITY_BREEZE_CHARGE = XSound.std("entity.breeze.charge");
    public static final XSound ENTITY_BREEZE_DEATH = XSound.std("entity.breeze.death");
    public static final XSound ENTITY_BREEZE_DEFLECT = XSound.std("entity.breeze.deflect");
    public static final XSound ENTITY_BREEZE_HURT = XSound.std("entity.breeze.hurt");
    public static final XSound ENTITY_BREEZE_IDLE_AIR = XSound.std("entity.breeze.idle_air");
    public static final XSound ENTITY_BREEZE_IDLE_GROUND = XSound.std("entity.breeze.idle_ground");
    public static final XSound ENTITY_BREEZE_INHALE = XSound.std("entity.breeze.inhale");
    public static final XSound ENTITY_BREEZE_JUMP = XSound.std("entity.breeze.jump");
    public static final XSound ENTITY_BREEZE_LAND = XSound.std("entity.breeze.land");
    public static final XSound ENTITY_BREEZE_SHOOT = XSound.std("entity.breeze.shoot");
    public static final XSound ENTITY_BREEZE_SLIDE = XSound.std("entity.breeze.slide");
    public static final XSound ENTITY_BREEZE_WHIRL = XSound.std("entity.breeze.whirl");
    public static final XSound ENTITY_BREEZE_WIND_BURST = XSound.std("entity.breeze.wind_burst");
    public static final XSound ENTITY_CAMEL_AMBIENT = XSound.std("entity.camel.ambient");
    public static final XSound ENTITY_CAMEL_DASH = XSound.std("entity.camel.dash");
    public static final XSound ENTITY_CAMEL_DASH_READY = XSound.std("entity.camel.dash_ready");
    public static final XSound ENTITY_CAMEL_DEATH = XSound.std("entity.camel.death");
    public static final XSound ENTITY_CAMEL_EAT = XSound.std("entity.camel.eat");
    public static final XSound ENTITY_CAMEL_HURT = XSound.std("entity.camel.hurt");
    public static final XSound ENTITY_CAMEL_SADDLE = XSound.std("entity.camel.saddle");
    public static final XSound ENTITY_CAMEL_SIT = XSound.std("entity.camel.sit");
    public static final XSound ENTITY_CAMEL_STAND = XSound.std("entity.camel.stand");
    public static final XSound ENTITY_CAMEL_STEP = XSound.std("entity.camel.step");
    public static final XSound ENTITY_CAMEL_STEP_SAND = XSound.std("entity.camel.step_sand");
    public static final XSound ENTITY_CAT_BEG_FOR_FOOD = XSound.std("entity.cat.beg_for_food");
    public static final XSound ENTITY_CAT_DEATH = XSound.std("entity.cat.death");
    public static final XSound ENTITY_CAT_STRAY_AMBIENT = XSound.std("entity.cat.stray_ambient");
    public static final XSound ENTITY_CHICKEN_DEATH = XSound.std("entity.chicken.death");
    public static final XSound ENTITY_COD_AMBIENT = XSound.std("entity.cod.ambient");
    public static final XSound ENTITY_COD_DEATH = XSound.std("entity.cod.death");
    public static final XSound ENTITY_COD_FLOP = XSound.std("entity.cod.flop");
    public static final XSound ENTITY_COD_HURT = XSound.std("entity.cod.hurt");
    public static final XSound ENTITY_COW_DEATH = XSound.std("entity.cow.death");
    public static final XSound ENTITY_COW_MILK = XSound.std("entity.cow.milk");
    public static final XSound ENTITY_CREAKING_ACTIVATE = XSound.std("entity.creaking.activate");
    public static final XSound ENTITY_CREAKING_AMBIENT = XSound.std("entity.creaking.ambient");
    public static final XSound ENTITY_CREAKING_ATTACK = XSound.std("entity.creaking.attack");
    public static final XSound ENTITY_CREAKING_DEACTIVATE = XSound.std("entity.creaking.deactivate");
    public static final XSound ENTITY_CREAKING_DEATH = XSound.std("entity.creaking.death");
    public static final XSound ENTITY_CREAKING_FREEZE = XSound.std("entity.creaking.freeze");
    public static final XSound ENTITY_CREAKING_SPAWN = XSound.std("entity.creaking.spawn");
    public static final XSound ENTITY_CREAKING_STEP = XSound.std("entity.creaking.step");
    public static final XSound ENTITY_CREAKING_SWAY = XSound.std("entity.creaking.sway");
    public static final XSound ENTITY_CREAKING_UNFREEZE = XSound.std("entity.creaking.unfreeze");
    public static final XSound ENTITY_CREEPER_HURT = XSound.std("entity.creeper.hurt");
    public static final XSound ENTITY_DOLPHIN_AMBIENT = XSound.std("entity.dolphin.ambient");
    public static final XSound ENTITY_DOLPHIN_AMBIENT_WATER = XSound.std("entity.dolphin.ambient_water");
    public static final XSound ENTITY_DOLPHIN_ATTACK = XSound.std("entity.dolphin.attack");
    public static final XSound ENTITY_DOLPHIN_DEATH = XSound.std("entity.dolphin.death");
    public static final XSound ENTITY_DOLPHIN_EAT = XSound.std("entity.dolphin.eat");
    public static final XSound ENTITY_DOLPHIN_HURT = XSound.std("entity.dolphin.hurt");
    public static final XSound ENTITY_DOLPHIN_JUMP = XSound.std("entity.dolphin.jump");
    public static final XSound ENTITY_DOLPHIN_PLAY = XSound.std("entity.dolphin.play");
    public static final XSound ENTITY_DOLPHIN_SPLASH = XSound.std("entity.dolphin.splash");
    public static final XSound ENTITY_DOLPHIN_SWIM = XSound.std("entity.dolphin.swim");
    public static final XSound ENTITY_DONKEY_CHEST = XSound.std("entity.donkey.chest");
    public static final XSound ENTITY_DONKEY_EAT = XSound.std("entity.donkey.eat");
    public static final XSound ENTITY_DONKEY_JUMP = XSound.std("entity.donkey.jump");
    public static final XSound ENTITY_DROWNED_AMBIENT = XSound.std("entity.drowned.ambient");
    public static final XSound ENTITY_DROWNED_AMBIENT_WATER = XSound.std("entity.drowned.ambient_water");
    public static final XSound ENTITY_DROWNED_DEATH = XSound.std("entity.drowned.death");
    public static final XSound ENTITY_DROWNED_DEATH_WATER = XSound.std("entity.drowned.death_water");
    public static final XSound ENTITY_DROWNED_HURT = XSound.std("entity.drowned.hurt");
    public static final XSound ENTITY_DROWNED_HURT_WATER = XSound.std("entity.drowned.hurt_water");
    public static final XSound ENTITY_DROWNED_SHOOT = XSound.std("entity.drowned.shoot");
    public static final XSound ENTITY_DROWNED_STEP = XSound.std("entity.drowned.step");
    public static final XSound ENTITY_DROWNED_SWIM = XSound.std("entity.drowned.swim");
    public static final XSound ENTITY_EGG_THROW = XSound.std("entity.egg.throw");
    public static final XSound ENTITY_ELDER_GUARDIAN_AMBIENT = XSound.std("entity.elder_guardian.ambient");
    public static final XSound ENTITY_ELDER_GUARDIAN_AMBIENT_LAND = XSound.std("entity.elder_guardian.ambient_land");
    public static final XSound ENTITY_ELDER_GUARDIAN_CURSE = XSound.std("entity.elder_guardian.curse");
    public static final XSound ENTITY_ELDER_GUARDIAN_DEATH = XSound.std("entity.elder_guardian.death");
    public static final XSound ENTITY_ELDER_GUARDIAN_DEATH_LAND = XSound.std("entity.elder_guardian.death_land");
    public static final XSound ENTITY_ELDER_GUARDIAN_FLOP = XSound.std("entity.elder_guardian.flop");
    public static final XSound ENTITY_ELDER_GUARDIAN_HURT = XSound.std("entity.elder_guardian.hurt");
    public static final XSound ENTITY_ELDER_GUARDIAN_HURT_LAND = XSound.std("entity.elder_guardian.hurt_land");
    public static final XSound ENTITY_ENDERMITE_AMBIENT = XSound.std("entity.endermite.ambient");
    public static final XSound ENTITY_ENDERMITE_DEATH = XSound.std("entity.endermite.death");
    public static final XSound ENTITY_ENDERMITE_HURT = XSound.std("entity.endermite.hurt");
    public static final XSound ENTITY_ENDERMITE_STEP = XSound.std("entity.endermite.step");
    public static final XSound ENTITY_ENDER_EYE_DEATH = XSound.std("entity.ender_eye.death");
    public static final XSound ENTITY_EVOKER_CELEBRATE = XSound.std("entity.evoker.celebrate");
    public static final XSound ENTITY_EXPERIENCE_BOTTLE_THROW = XSound.std("entity.experience_bottle.throw");
    public static final XSound ENTITY_FIREWORK_ROCKET_SHOOT = XSound.std("entity.firework_rocket.shoot", "ENTITY_FIREWORK_SHOOT");
    public static final XSound ENTITY_FISHING_BOBBER_RETRIEVE = XSound.std("entity.fishing_bobber.retrieve", "ENTITY_BOBBER_RETRIEVE");
    public static final XSound ENTITY_FISH_SWIM = XSound.std("entity.fish.swim");
    public static final XSound ENTITY_FOX_AGGRO = XSound.std("entity.fox.aggro");
    public static final XSound ENTITY_FOX_AMBIENT = XSound.std("entity.fox.ambient");
    public static final XSound ENTITY_FOX_BITE = XSound.std("entity.fox.bite");
    public static final XSound ENTITY_FOX_DEATH = XSound.std("entity.fox.death");
    public static final XSound ENTITY_FOX_EAT = XSound.std("entity.fox.eat");
    public static final XSound ENTITY_FOX_HURT = XSound.std("entity.fox.hurt");
    public static final XSound ENTITY_FOX_SCREECH = XSound.std("entity.fox.screech");
    public static final XSound ENTITY_FOX_SLEEP = XSound.std("entity.fox.sleep");
    public static final XSound ENTITY_FOX_SNIFF = XSound.std("entity.fox.sniff");
    public static final XSound ENTITY_FOX_SPIT = XSound.std("entity.fox.spit");
    public static final XSound ENTITY_FOX_TELEPORT = XSound.std("entity.fox.teleport");
    public static final XSound ENTITY_FROG_AMBIENT = XSound.std("entity.frog.ambient");
    public static final XSound ENTITY_FROG_DEATH = XSound.std("entity.frog.death");
    public static final XSound ENTITY_FROG_EAT = XSound.std("entity.frog.eat");
    public static final XSound ENTITY_FROG_HURT = XSound.std("entity.frog.hurt");
    public static final XSound ENTITY_FROG_LAY_SPAWN = XSound.std("entity.frog.lay_spawn");
    public static final XSound ENTITY_FROG_LONG_JUMP = XSound.std("entity.frog.long_jump");
    public static final XSound ENTITY_FROG_STEP = XSound.std("entity.frog.step");
    public static final XSound ENTITY_FROG_TONGUE = XSound.std("entity.frog.tongue");
    public static final XSound ENTITY_GENERIC_BURN = XSound.std("entity.generic.burn");
    public static final XSound ENTITY_GENERIC_DEATH = XSound.std("entity.generic.death");
    public static final XSound ENTITY_GENERIC_EXTINGUISH_FIRE = XSound.std("entity.generic.extinguish_fire");
    public static final XSound ENTITY_GENERIC_HURT = XSound.std("entity.generic.hurt");
    public static final XSound ENTITY_GLOW_ITEM_FRAME_ADD_ITEM = XSound.std("entity.glow_item_frame.add_item");
    public static final XSound ENTITY_GLOW_ITEM_FRAME_BREAK = XSound.std("entity.glow_item_frame.break");
    public static final XSound ENTITY_GLOW_ITEM_FRAME_PLACE = XSound.std("entity.glow_item_frame.place");
    public static final XSound ENTITY_GLOW_ITEM_FRAME_REMOVE_ITEM = XSound.std("entity.glow_item_frame.remove_item");
    public static final XSound ENTITY_GLOW_ITEM_FRAME_ROTATE_ITEM = XSound.std("entity.glow_item_frame.rotate_item");
    public static final XSound ENTITY_GLOW_SQUID_AMBIENT = XSound.std("entity.glow_squid.ambient");
    public static final XSound ENTITY_GLOW_SQUID_DEATH = XSound.std("entity.glow_squid.death");
    public static final XSound ENTITY_GLOW_SQUID_HURT = XSound.std("entity.glow_squid.hurt");
    public static final XSound ENTITY_GLOW_SQUID_SQUIRT = XSound.std("entity.glow_squid.squirt");
    public static final XSound ENTITY_GOAT_AMBIENT = XSound.std("entity.goat.ambient");
    public static final XSound ENTITY_GOAT_DEATH = XSound.std("entity.goat.death");
    public static final XSound ENTITY_GOAT_EAT = XSound.std("entity.goat.eat");
    public static final XSound ENTITY_GOAT_HORN_BREAK = XSound.std("entity.goat.horn_break");
    public static final XSound ENTITY_GOAT_HURT = XSound.std("entity.goat.hurt");
    public static final XSound ENTITY_GOAT_LONG_JUMP = XSound.std("entity.goat.long_jump");
    public static final XSound ENTITY_GOAT_MILK = XSound.std("entity.goat.milk");
    public static final XSound ENTITY_GOAT_PREPARE_RAM = XSound.std("entity.goat.prepare_ram");
    public static final XSound ENTITY_GOAT_RAM_IMPACT = XSound.std("entity.goat.ram_impact");
    public static final XSound ENTITY_GOAT_SCREAMING_AMBIENT = XSound.std("entity.goat.screaming.ambient");
    public static final XSound ENTITY_GOAT_SCREAMING_DEATH = XSound.std("entity.goat.screaming.death");
    public static final XSound ENTITY_GOAT_SCREAMING_EAT = XSound.std("entity.goat.screaming.eat");
    public static final XSound ENTITY_GOAT_SCREAMING_HURT = XSound.std("entity.goat.screaming.hurt");
    public static final XSound ENTITY_GOAT_SCREAMING_LONG_JUMP = XSound.std("entity.goat.screaming.long_jump");
    public static final XSound ENTITY_GOAT_SCREAMING_MILK = XSound.std("entity.goat.screaming.milk");
    public static final XSound ENTITY_GOAT_SCREAMING_PREPARE_RAM = XSound.std("entity.goat.screaming.prepare_ram");
    public static final XSound ENTITY_GOAT_SCREAMING_RAM_IMPACT = XSound.std("entity.goat.screaming.ram_impact");
    public static final XSound ENTITY_GOAT_STEP = XSound.std("entity.goat.step");
    public static final XSound ENTITY_GUARDIAN_AMBIENT = XSound.std("entity.guardian.ambient");
    public static final XSound ENTITY_GUARDIAN_AMBIENT_LAND = XSound.std("entity.guardian.ambient_land");
    public static final XSound ENTITY_GUARDIAN_ATTACK = XSound.std("entity.guardian.attack");
    public static final XSound ENTITY_GUARDIAN_DEATH = XSound.std("entity.guardian.death");
    public static final XSound ENTITY_GUARDIAN_DEATH_LAND = XSound.std("entity.guardian.death_land");
    public static final XSound ENTITY_GUARDIAN_FLOP = XSound.std("entity.guardian.flop");
    public static final XSound ENTITY_GUARDIAN_HURT = XSound.std("entity.guardian.hurt");
    public static final XSound ENTITY_GUARDIAN_HURT_LAND = XSound.std("entity.guardian.hurt_land");
    public static final XSound ENTITY_HOGLIN_AMBIENT = XSound.std("entity.hoglin.ambient");
    public static final XSound ENTITY_HOGLIN_ANGRY = XSound.std("entity.hoglin.angry");
    public static final XSound ENTITY_HOGLIN_ATTACK = XSound.std("entity.hoglin.attack");
    public static final XSound ENTITY_HOGLIN_CONVERTED_TO_ZOMBIFIED = XSound.std("entity.hoglin.converted_to_zombified");
    public static final XSound ENTITY_HOGLIN_DEATH = XSound.std("entity.hoglin.death");
    public static final XSound ENTITY_HOGLIN_HURT = XSound.std("entity.hoglin.hurt");
    public static final XSound ENTITY_HOGLIN_RETREAT = XSound.std("entity.hoglin.retreat");
    public static final XSound ENTITY_HOGLIN_STEP = XSound.std("entity.hoglin.step");
    public static final XSound ENTITY_HOSTILE_DEATH = XSound.std("entity.hostile.death");
    public static final XSound ENTITY_HOSTILE_HURT = XSound.std("entity.hostile.hurt");
    public static final XSound ENTITY_HUSK_AMBIENT = XSound.std("entity.husk.ambient");
    public static final XSound ENTITY_HUSK_CONVERTED_TO_ZOMBIE = XSound.std("entity.husk.converted_to_zombie");
    public static final XSound ENTITY_HUSK_DEATH = XSound.std("entity.husk.death");
    public static final XSound ENTITY_HUSK_HURT = XSound.std("entity.husk.hurt");
    public static final XSound ENTITY_HUSK_STEP = XSound.std("entity.husk.step");
    public static final XSound ENTITY_IRON_GOLEM_DAMAGE = XSound.std("entity.iron_golem.damage");
    public static final XSound ENTITY_IRON_GOLEM_REPAIR = XSound.std("entity.iron_golem.repair");
    public static final XSound ENTITY_LLAMA_AMBIENT = XSound.std("entity.llama.ambient");
    public static final XSound ENTITY_LLAMA_ANGRY = XSound.std("entity.llama.angry");
    public static final XSound ENTITY_LLAMA_CHEST = XSound.std("entity.llama.chest");
    public static final XSound ENTITY_LLAMA_DEATH = XSound.std("entity.llama.death");
    public static final XSound ENTITY_LLAMA_EAT = XSound.std("entity.llama.eat");
    public static final XSound ENTITY_LLAMA_HURT = XSound.std("entity.llama.hurt");
    public static final XSound ENTITY_LLAMA_SPIT = XSound.std("entity.llama.spit");
    public static final XSound ENTITY_LLAMA_STEP = XSound.std("entity.llama.step");
    public static final XSound ENTITY_LLAMA_SWAG = XSound.std("entity.llama.swag");
    public static final XSound ENTITY_MINECART_INSIDE_UNDERWATER = XSound.std("entity.minecart.inside.underwater");
    public static final XSound ENTITY_MOOSHROOM_CONVERT = XSound.std("entity.mooshroom.convert");
    public static final XSound ENTITY_MOOSHROOM_EAT = XSound.std("entity.mooshroom.eat");
    public static final XSound ENTITY_MOOSHROOM_MILK = XSound.std("entity.mooshroom.milk");
    public static final XSound ENTITY_MOOSHROOM_SHEAR = XSound.std("entity.mooshroom.shear");
    public static final XSound ENTITY_MOOSHROOM_SUSPICIOUS_MILK = XSound.std("entity.mooshroom.suspicious_milk");
    public static final XSound ENTITY_MULE_AMBIENT = XSound.std("entity.mule.ambient");
    public static final XSound ENTITY_MULE_ANGRY = XSound.std("entity.mule.angry");
    public static final XSound ENTITY_MULE_EAT = XSound.std("entity.mule.eat");
    public static final XSound ENTITY_MULE_JUMP = XSound.std("entity.mule.jump");
    public static final XSound ENTITY_OCELOT_AMBIENT = XSound.std("entity.ocelot.ambient");
    public static final XSound ENTITY_OCELOT_DEATH = XSound.std("entity.ocelot.death");
    public static final XSound ENTITY_OCELOT_HURT = XSound.std("entity.ocelot.hurt");
    public static final XSound ENTITY_PAINTING_BREAK = XSound.std("entity.painting.break");
    public static final XSound ENTITY_PAINTING_PLACE = XSound.std("entity.painting.place");
    public static final XSound ENTITY_PANDA_AGGRESSIVE_AMBIENT = XSound.std("entity.panda.aggressive_ambient");
    public static final XSound ENTITY_PANDA_AMBIENT = XSound.std("entity.panda.ambient");
    public static final XSound ENTITY_PANDA_BITE = XSound.std("entity.panda.bite");
    public static final XSound ENTITY_PANDA_CANT_BREED = XSound.std("entity.panda.cant_breed");
    public static final XSound ENTITY_PANDA_DEATH = XSound.std("entity.panda.death");
    public static final XSound ENTITY_PANDA_EAT = XSound.std("entity.panda.eat");
    public static final XSound ENTITY_PANDA_HURT = XSound.std("entity.panda.hurt");
    public static final XSound ENTITY_PANDA_PRE_SNEEZE = XSound.std("entity.panda.pre_sneeze");
    public static final XSound ENTITY_PANDA_SNEEZE = XSound.std("entity.panda.sneeze");
    public static final XSound ENTITY_PANDA_STEP = XSound.std("entity.panda.step");
    public static final XSound ENTITY_PANDA_WORRIED_AMBIENT = XSound.std("entity.panda.worried_ambient");
    public static final XSound ENTITY_PARROT_AMBIENT = XSound.std("entity.parrot.ambient");
    public static final XSound ENTITY_PARROT_DEATH = XSound.std("entity.parrot.death");
    public static final XSound ENTITY_PARROT_EAT = XSound.std("entity.parrot.eat");
    public static final XSound ENTITY_PARROT_FLY = XSound.std("entity.parrot.fly");
    public static final XSound ENTITY_PARROT_HURT = XSound.std("entity.parrot.hurt");
    public static final XSound ENTITY_PARROT_IMITATE_BLAZE = XSound.std("entity.parrot.imitate.blaze");
    public static final XSound ENTITY_PARROT_IMITATE_BOGGED = XSound.std("entity.parrot.imitate.bogged");
    public static final XSound ENTITY_PARROT_IMITATE_BREEZE = XSound.std("entity.parrot.imitate.breeze");
    public static final XSound ENTITY_PARROT_IMITATE_CREAKING = XSound.std("entity.parrot.imitate.creaking");
    public static final XSound ENTITY_PARROT_IMITATE_CREEPER = XSound.std("entity.parrot.imitate.creeper");
    public static final XSound ENTITY_PARROT_IMITATE_DROWNED = XSound.std("entity.parrot.imitate.drowned");
    public static final XSound ENTITY_PARROT_IMITATE_ELDER_GUARDIAN = XSound.std("entity.parrot.imitate.elder_guardian");
    public static final XSound ENTITY_PARROT_IMITATE_ENDERMITE = XSound.std("entity.parrot.imitate.endermite");
    public static final XSound ENTITY_PARROT_IMITATE_ENDER_DRAGON = XSound.std("entity.parrot.imitate.ender_dragon", "ENTITY_PARROT_IMITATE_ENDERDRAGON");
    public static final XSound ENTITY_PARROT_IMITATE_EVOKER = XSound.std("entity.parrot.imitate.evoker", "ENTITY_PARROT_IMITATE_EVOCATION_ILLAGER");
    public static final XSound ENTITY_PARROT_IMITATE_GHAST = XSound.std("entity.parrot.imitate.ghast");
    public static final XSound ENTITY_PARROT_IMITATE_GUARDIAN = XSound.std("entity.parrot.imitate.guardian");
    public static final XSound ENTITY_PARROT_IMITATE_HOGLIN = XSound.std("entity.parrot.imitate.hoglin");
    public static final XSound ENTITY_PARROT_IMITATE_HUSK = XSound.std("entity.parrot.imitate.husk");
    public static final XSound ENTITY_PARROT_IMITATE_ILLUSIONER = XSound.std("entity.parrot.imitate.illusioner", "ENTITY_PARROT_IMITATE_ILLUSION_ILLAGER");
    public static final XSound ENTITY_PARROT_IMITATE_MAGMA_CUBE = XSound.std("entity.parrot.imitate.magma_cube", "ENTITY_PARROT_IMITATE_MAGMACUBE");
    public static final XSound ENTITY_PARROT_IMITATE_PHANTOM = XSound.std("entity.parrot.imitate.phantom");
    public static final XSound ENTITY_PARROT_IMITATE_PIGLIN = XSound.std("entity.parrot.imitate.piglin", "ENTITY_PARROT_IMITATE_ZOMBIE_PIGMAN");
    public static final XSound ENTITY_PARROT_IMITATE_PIGLIN_BRUTE = XSound.std("entity.parrot.imitate.piglin_brute");
    public static final XSound ENTITY_PARROT_IMITATE_PILLAGER = XSound.std("entity.parrot.imitate.pillager");
    public static final XSound ENTITY_PARROT_IMITATE_RAVAGER = XSound.std("entity.parrot.imitate.ravager");
    public static final XSound ENTITY_PARROT_IMITATE_SHULKER = XSound.std("entity.parrot.imitate.shulker");
    public static final XSound ENTITY_PARROT_IMITATE_SILVERFISH = XSound.std("entity.parrot.imitate.silverfish");
    public static final XSound ENTITY_PARROT_IMITATE_SKELETON = XSound.std("entity.parrot.imitate.skeleton");
    public static final XSound ENTITY_PARROT_IMITATE_SLIME = XSound.std("entity.parrot.imitate.slime");
    public static final XSound ENTITY_PARROT_IMITATE_SPIDER = XSound.std("entity.parrot.imitate.spider");
    public static final XSound ENTITY_PARROT_IMITATE_STRAY = XSound.std("entity.parrot.imitate.stray");
    public static final XSound ENTITY_PARROT_IMITATE_VEX = XSound.std("entity.parrot.imitate.vex");
    public static final XSound ENTITY_PARROT_IMITATE_VINDICATOR = XSound.std("entity.parrot.imitate.vindicator", "ENTITY_PARROT_IMITATE_VINDICATION_ILLAGER");
    public static final XSound ENTITY_PARROT_IMITATE_WARDEN = XSound.std("entity.parrot.imitate.warden");
    public static final XSound ENTITY_PARROT_IMITATE_WITCH = XSound.std("entity.parrot.imitate.witch");
    public static final XSound ENTITY_PARROT_IMITATE_WITHER = XSound.std("entity.parrot.imitate.wither");
    public static final XSound ENTITY_PARROT_IMITATE_WITHER_SKELETON = XSound.std("entity.parrot.imitate.wither_skeleton");
    public static final XSound ENTITY_PARROT_IMITATE_ZOGLIN = XSound.std("entity.parrot.imitate.zoglin");
    public static final XSound ENTITY_PARROT_IMITATE_ZOMBIE = XSound.std("entity.parrot.imitate.zombie");
    public static final XSound ENTITY_PARROT_IMITATE_ZOMBIE_VILLAGER = XSound.std("entity.parrot.imitate.zombie_villager");
    public static final XSound ENTITY_PARROT_STEP = XSound.std("entity.parrot.step");
    public static final XSound ENTITY_PHANTOM_AMBIENT = XSound.std("entity.phantom.ambient");
    public static final XSound ENTITY_PHANTOM_BITE = XSound.std("entity.phantom.bite");
    public static final XSound ENTITY_PHANTOM_DEATH = XSound.std("entity.phantom.death");
    public static final XSound ENTITY_PHANTOM_FLAP = XSound.std("entity.phantom.flap");
    public static final XSound ENTITY_PHANTOM_HURT = XSound.std("entity.phantom.hurt");
    public static final XSound ENTITY_PHANTOM_SWOOP = XSound.std("entity.phantom.swoop");
    public static final XSound ENTITY_PIGLIN_ADMIRING_ITEM = XSound.std("entity.piglin.admiring_item");
    public static final XSound ENTITY_PIGLIN_AMBIENT = XSound.std("entity.piglin.ambient");
    public static final XSound ENTITY_PIGLIN_ANGRY = XSound.std("entity.piglin.angry");
    public static final XSound ENTITY_PIGLIN_BRUTE_AMBIENT = XSound.std("entity.piglin_brute.ambient");
    public static final XSound ENTITY_PIGLIN_BRUTE_ANGRY = XSound.std("entity.piglin_brute.angry");
    public static final XSound ENTITY_PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED = XSound.std("entity.piglin_brute.converted_to_zombified");
    public static final XSound ENTITY_PIGLIN_BRUTE_DEATH = XSound.std("entity.piglin_brute.death");
    public static final XSound ENTITY_PIGLIN_BRUTE_HURT = XSound.std("entity.piglin_brute.hurt");
    public static final XSound ENTITY_PIGLIN_BRUTE_STEP = XSound.std("entity.piglin_brute.step");
    public static final XSound ENTITY_PIGLIN_CELEBRATE = XSound.std("entity.piglin.celebrate");
    public static final XSound ENTITY_PIGLIN_CONVERTED_TO_ZOMBIFIED = XSound.std("entity.piglin.converted_to_zombified");
    public static final XSound ENTITY_PIGLIN_DEATH = XSound.std("entity.piglin.death");
    public static final XSound ENTITY_PIGLIN_HURT = XSound.std("entity.piglin.hurt");
    public static final XSound ENTITY_PIGLIN_JEALOUS = XSound.std("entity.piglin.jealous");
    public static final XSound ENTITY_PIGLIN_RETREAT = XSound.std("entity.piglin.retreat");
    public static final XSound ENTITY_PIGLIN_STEP = XSound.std("entity.piglin.step");
    public static final XSound ENTITY_PIG_HURT = XSound.std("entity.pig.hurt");
    public static final XSound ENTITY_PILLAGER_AMBIENT = XSound.std("entity.pillager.ambient");
    public static final XSound ENTITY_PILLAGER_CELEBRATE = XSound.std("entity.pillager.celebrate");
    public static final XSound ENTITY_PILLAGER_DEATH = XSound.std("entity.pillager.death");
    public static final XSound ENTITY_PILLAGER_HURT = XSound.std("entity.pillager.hurt");
    public static final XSound ENTITY_PLAYER_ATTACK_CRIT = XSound.std("entity.player.attack.crit");
    public static final XSound ENTITY_PLAYER_ATTACK_KNOCKBACK = XSound.std("entity.player.attack.knockback");
    public static final XSound ENTITY_PLAYER_ATTACK_NODAMAGE = XSound.std("entity.player.attack.nodamage");
    public static final XSound ENTITY_PLAYER_ATTACK_SWEEP = XSound.std("entity.player.attack.sweep");
    public static final XSound ENTITY_PLAYER_ATTACK_WEAK = XSound.std("entity.player.attack.weak");
    public static final XSound ENTITY_PLAYER_BREATH = XSound.std("entity.player.breath");
    public static final XSound ENTITY_PLAYER_DEATH = XSound.std("entity.player.death");
    public static final XSound ENTITY_PLAYER_HURT_DROWN = XSound.std("entity.player.hurt_drown");
    public static final XSound ENTITY_PLAYER_HURT_FREEZE = XSound.std("entity.player.hurt_freeze");
    public static final XSound ENTITY_PLAYER_HURT_ON_FIRE = XSound.std("entity.player.hurt_on_fire");
    public static final XSound ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH = XSound.std("entity.player.hurt_sweet_berry_bush");
    public static final XSound ENTITY_PLAYER_TELEPORT = XSound.std("entity.player.teleport");
    public static final XSound ENTITY_POLAR_BEAR_AMBIENT = XSound.std("entity.polar_bear.ambient");
    public static final XSound ENTITY_POLAR_BEAR_DEATH = XSound.std("entity.polar_bear.death");
    public static final XSound ENTITY_POLAR_BEAR_HURT = XSound.std("entity.polar_bear.hurt");
    public static final XSound ENTITY_POLAR_BEAR_STEP = XSound.std("entity.polar_bear.step");
    public static final XSound ENTITY_POLAR_BEAR_WARNING = XSound.std("entity.polar_bear.warning");
    public static final XSound ENTITY_PUFFER_FISH_AMBIENT = XSound.std("entity.puffer_fish.ambient");
    public static final XSound ENTITY_PUFFER_FISH_BLOW_OUT = XSound.std("entity.puffer_fish.blow_out");
    public static final XSound ENTITY_PUFFER_FISH_BLOW_UP = XSound.std("entity.puffer_fish.blow_up");
    public static final XSound ENTITY_PUFFER_FISH_DEATH = XSound.std("entity.puffer_fish.death");
    public static final XSound ENTITY_PUFFER_FISH_FLOP = XSound.std("entity.puffer_fish.flop");
    public static final XSound ENTITY_PUFFER_FISH_HURT = XSound.std("entity.puffer_fish.hurt");
    public static final XSound ENTITY_PUFFER_FISH_STING = XSound.std("entity.puffer_fish.sting");
    public static final XSound ENTITY_RABBIT_AMBIENT = XSound.std("entity.rabbit.ambient");
    public static final XSound ENTITY_RABBIT_ATTACK = XSound.std("entity.rabbit.attack");
    public static final XSound ENTITY_RABBIT_DEATH = XSound.std("entity.rabbit.death");
    public static final XSound ENTITY_RABBIT_HURT = XSound.std("entity.rabbit.hurt");
    public static final XSound ENTITY_RABBIT_JUMP = XSound.std("entity.rabbit.jump");
    public static final XSound ENTITY_RAVAGER_AMBIENT = XSound.std("entity.ravager.ambient");
    public static final XSound ENTITY_RAVAGER_ATTACK = XSound.std("entity.ravager.attack");
    public static final XSound ENTITY_RAVAGER_CELEBRATE = XSound.std("entity.ravager.celebrate");
    public static final XSound ENTITY_RAVAGER_DEATH = XSound.std("entity.ravager.death");
    public static final XSound ENTITY_RAVAGER_HURT = XSound.std("entity.ravager.hurt");
    public static final XSound ENTITY_RAVAGER_ROAR = XSound.std("entity.ravager.roar");
    public static final XSound ENTITY_RAVAGER_STEP = XSound.std("entity.ravager.step");
    public static final XSound ENTITY_RAVAGER_STUNNED = XSound.std("entity.ravager.stunned");
    public static final XSound ENTITY_SALMON_AMBIENT = XSound.std("entity.salmon.ambient");
    public static final XSound ENTITY_SALMON_DEATH = XSound.std("entity.salmon.death");
    public static final XSound ENTITY_SALMON_FLOP = XSound.std("entity.salmon.flop");
    public static final XSound ENTITY_SHEEP_DEATH = XSound.std("entity.sheep.death");
    public static final XSound ENTITY_SHEEP_HURT = XSound.std("entity.sheep.hurt");
    public static final XSound ENTITY_SHULKER_AMBIENT = XSound.std("entity.shulker.ambient");
    public static final XSound ENTITY_SHULKER_BULLET_HIT = XSound.std("entity.shulker_bullet.hit");
    public static final XSound ENTITY_SHULKER_BULLET_HURT = XSound.std("entity.shulker_bullet.hurt");
    public static final XSound ENTITY_SHULKER_CLOSE = XSound.std("entity.shulker.close");
    public static final XSound ENTITY_SHULKER_DEATH = XSound.std("entity.shulker.death");
    public static final XSound ENTITY_SHULKER_HURT = XSound.std("entity.shulker.hurt");
    public static final XSound ENTITY_SHULKER_HURT_CLOSED = XSound.std("entity.shulker.hurt_closed");
    public static final XSound ENTITY_SHULKER_OPEN = XSound.std("entity.shulker.open");
    public static final XSound ENTITY_SHULKER_SHOOT = XSound.std("entity.shulker.shoot");
    public static final XSound ENTITY_SHULKER_TELEPORT = XSound.std("entity.shulker.teleport");
    public static final XSound ENTITY_SKELETON_CONVERTED_TO_STRAY = XSound.std("entity.skeleton.converted_to_stray");
    public static final XSound ENTITY_SKELETON_HORSE_AMBIENT_WATER = XSound.std("entity.skeleton_horse.ambient_water");
    public static final XSound ENTITY_SKELETON_HORSE_GALLOP_WATER = XSound.std("entity.skeleton_horse.gallop_water");
    public static final XSound ENTITY_SKELETON_HORSE_JUMP_WATER = XSound.std("entity.skeleton_horse.jump_water");
    public static final XSound ENTITY_SKELETON_HORSE_STEP_WATER = XSound.std("entity.skeleton_horse.step_water");
    public static final XSound ENTITY_SKELETON_HORSE_SWIM = XSound.std("entity.skeleton_horse.swim");
    public static final XSound ENTITY_SKELETON_SHOOT = XSound.std("entity.skeleton.shoot");
    public static final XSound ENTITY_SLIME_DEATH = XSound.std("entity.slime.death");
    public static final XSound ENTITY_SLIME_DEATH_SMALL = XSound.std("entity.slime.death_small");
    public static final XSound ENTITY_SLIME_HURT = XSound.std("entity.slime.hurt");
    public static final XSound ENTITY_SNIFFER_DEATH = XSound.std("entity.sniffer.death");
    public static final XSound ENTITY_SNIFFER_DIGGING = XSound.std("entity.sniffer.digging");
    public static final XSound ENTITY_SNIFFER_DIGGING_STOP = XSound.std("entity.sniffer.digging_stop");
    public static final XSound ENTITY_SNIFFER_DROP_SEED = XSound.std("entity.sniffer.drop_seed");
    public static final XSound ENTITY_SNIFFER_EAT = XSound.std("entity.sniffer.eat");
    public static final XSound ENTITY_SNIFFER_HAPPY = XSound.std("entity.sniffer.happy");
    public static final XSound ENTITY_SNIFFER_HURT = XSound.std("entity.sniffer.hurt");
    public static final XSound ENTITY_SNIFFER_IDLE = XSound.std("entity.sniffer.idle");
    public static final XSound ENTITY_SNIFFER_SCENTING = XSound.std("entity.sniffer.scenting");
    public static final XSound ENTITY_SNIFFER_SEARCHING = XSound.std("entity.sniffer.searching");
    public static final XSound ENTITY_SNIFFER_SNIFFING = XSound.std("entity.sniffer.sniffing");
    public static final XSound ENTITY_SNIFFER_STEP = XSound.std("entity.sniffer.step");
    public static final XSound ENTITY_SNOWBALL_THROW = XSound.std("entity.snowball.throw");
    public static final XSound ENTITY_SPIDER_HURT = XSound.std("entity.spider.hurt");
    public static final XSound ENTITY_SPLASH_POTION_BREAK = XSound.std("entity.splash_potion.break");
    public static final XSound ENTITY_SPLASH_POTION_THROW = XSound.std("entity.splash_potion.throw");
    public static final XSound ENTITY_SQUID_AMBIENT = XSound.std("entity.squid.ambient");
    public static final XSound ENTITY_SQUID_DEATH = XSound.std("entity.squid.death");
    public static final XSound ENTITY_SQUID_HURT = XSound.std("entity.squid.hurt");
    public static final XSound ENTITY_SQUID_SQUIRT = XSound.std("entity.squid.squirt");
    public static final XSound ENTITY_STRAY_AMBIENT = XSound.std("entity.stray.ambient");
    public static final XSound ENTITY_STRAY_DEATH = XSound.std("entity.stray.death");
    public static final XSound ENTITY_STRAY_HURT = XSound.std("entity.stray.hurt");
    public static final XSound ENTITY_STRAY_STEP = XSound.std("entity.stray.step");
    public static final XSound ENTITY_STRIDER_AMBIENT = XSound.std("entity.strider.ambient");
    public static final XSound ENTITY_STRIDER_DEATH = XSound.std("entity.strider.death");
    public static final XSound ENTITY_STRIDER_EAT = XSound.std("entity.strider.eat");
    public static final XSound ENTITY_STRIDER_HAPPY = XSound.std("entity.strider.happy");
    public static final XSound ENTITY_STRIDER_HURT = XSound.std("entity.strider.hurt");
    public static final XSound ENTITY_STRIDER_RETREAT = XSound.std("entity.strider.retreat");
    public static final XSound ENTITY_STRIDER_SADDLE = XSound.std("entity.strider.saddle");
    public static final XSound ENTITY_STRIDER_STEP = XSound.std("entity.strider.step");
    public static final XSound ENTITY_STRIDER_STEP_LAVA = XSound.std("entity.strider.step_lava");
    public static final XSound ENTITY_TADPOLE_DEATH = XSound.std("entity.tadpole.death");
    public static final XSound ENTITY_TADPOLE_FLOP = XSound.std("entity.tadpole.flop");
    public static final XSound ENTITY_TADPOLE_GROW_UP = XSound.std("entity.tadpole.grow_up");
    public static final XSound ENTITY_TADPOLE_HURT = XSound.std("entity.tadpole.hurt");
    public static final XSound ENTITY_TROPICAL_FISH_AMBIENT = XSound.std("entity.tropical_fish.ambient");
    public static final XSound ENTITY_TROPICAL_FISH_DEATH = XSound.std("entity.tropical_fish.death");
    public static final XSound ENTITY_TROPICAL_FISH_HURT = XSound.std("entity.tropical_fish.hurt");
    public static final XSound ENTITY_TURTLE_AMBIENT_LAND = XSound.std("entity.turtle.ambient_land");
    public static final XSound ENTITY_TURTLE_DEATH = XSound.std("entity.turtle.death");
    public static final XSound ENTITY_TURTLE_DEATH_BABY = XSound.std("entity.turtle.death_baby");
    public static final XSound ENTITY_TURTLE_EGG_BREAK = XSound.std("entity.turtle.egg_break");
    public static final XSound ENTITY_TURTLE_EGG_CRACK = XSound.std("entity.turtle.egg_crack");
    public static final XSound ENTITY_TURTLE_EGG_HATCH = XSound.std("entity.turtle.egg_hatch");
    public static final XSound ENTITY_TURTLE_HURT = XSound.std("entity.turtle.hurt");
    public static final XSound ENTITY_TURTLE_HURT_BABY = XSound.std("entity.turtle.hurt_baby");
    public static final XSound ENTITY_TURTLE_LAY_EGG = XSound.std("entity.turtle.lay_egg");
    public static final XSound ENTITY_TURTLE_SHAMBLE = XSound.std("entity.turtle.shamble");
    public static final XSound ENTITY_TURTLE_SHAMBLE_BABY = XSound.std("entity.turtle.shamble_baby");
    public static final XSound ENTITY_TURTLE_SWIM = XSound.std("entity.turtle.swim");
    public static final XSound ENTITY_VEX_AMBIENT = XSound.std("entity.vex.ambient");
    public static final XSound ENTITY_VEX_CHARGE = XSound.std("entity.vex.charge");
    public static final XSound ENTITY_VEX_DEATH = XSound.std("entity.vex.death");
    public static final XSound ENTITY_VEX_HURT = XSound.std("entity.vex.hurt");
    public static final XSound ENTITY_VILLAGER_CELEBRATE = XSound.std("entity.villager.celebrate");
    public static final XSound ENTITY_VILLAGER_WORK_ARMORER = XSound.std("entity.villager.work_armorer");
    public static final XSound ENTITY_VILLAGER_WORK_BUTCHER = XSound.std("entity.villager.work_butcher");
    public static final XSound ENTITY_VILLAGER_WORK_CARTOGRAPHER = XSound.std("entity.villager.work_cartographer");
    public static final XSound ENTITY_VILLAGER_WORK_CLERIC = XSound.std("entity.villager.work_cleric");
    public static final XSound ENTITY_VILLAGER_WORK_FARMER = XSound.std("entity.villager.work_farmer");
    public static final XSound ENTITY_VILLAGER_WORK_FISHERMAN = XSound.std("entity.villager.work_fisherman");
    public static final XSound ENTITY_VILLAGER_WORK_FLETCHER = XSound.std("entity.villager.work_fletcher");
    public static final XSound ENTITY_VILLAGER_WORK_LEATHERWORKER = XSound.std("entity.villager.work_leatherworker");
    public static final XSound ENTITY_VILLAGER_WORK_LIBRARIAN = XSound.std("entity.villager.work_librarian");
    public static final XSound ENTITY_VILLAGER_WORK_MASON = XSound.std("entity.villager.work_mason");
    public static final XSound ENTITY_VILLAGER_WORK_SHEPHERD = XSound.std("entity.villager.work_shepherd");
    public static final XSound ENTITY_VILLAGER_WORK_TOOLSMITH = XSound.std("entity.villager.work_toolsmith");
    public static final XSound ENTITY_VILLAGER_WORK_WEAPONSMITH = XSound.std("entity.villager.work_weaponsmith");
    public static final XSound ENTITY_VINDICATOR_CELEBRATE = XSound.std("entity.vindicator.celebrate");
    public static final XSound ENTITY_WANDERING_TRADER_AMBIENT = XSound.std("entity.wandering_trader.ambient");
    public static final XSound ENTITY_WANDERING_TRADER_DEATH = XSound.std("entity.wandering_trader.death");
    public static final XSound ENTITY_WANDERING_TRADER_DISAPPEARED = XSound.std("entity.wandering_trader.disappeared");
    public static final XSound ENTITY_WANDERING_TRADER_DRINK_MILK = XSound.std("entity.wandering_trader.drink_milk");
    public static final XSound ENTITY_WANDERING_TRADER_DRINK_POTION = XSound.std("entity.wandering_trader.drink_potion");
    public static final XSound ENTITY_WANDERING_TRADER_HURT = XSound.std("entity.wandering_trader.hurt");
    public static final XSound ENTITY_WANDERING_TRADER_NO = XSound.std("entity.wandering_trader.no");
    public static final XSound ENTITY_WANDERING_TRADER_REAPPEARED = XSound.std("entity.wandering_trader.reappeared");
    public static final XSound ENTITY_WANDERING_TRADER_TRADE = XSound.std("entity.wandering_trader.trade");
    public static final XSound ENTITY_WANDERING_TRADER_YES = XSound.std("entity.wandering_trader.yes");
    public static final XSound ENTITY_WARDEN_AGITATED = XSound.std("entity.warden.agitated");
    public static final XSound ENTITY_WARDEN_AMBIENT = XSound.std("entity.warden.ambient");
    public static final XSound ENTITY_WARDEN_ANGRY = XSound.std("entity.warden.angry");
    public static final XSound ENTITY_WARDEN_ATTACK_IMPACT = XSound.std("entity.warden.attack_impact");
    public static final XSound ENTITY_WARDEN_DEATH = XSound.std("entity.warden.death");
    public static final XSound ENTITY_WARDEN_DIG = XSound.std("entity.warden.dig");
    public static final XSound ENTITY_WARDEN_EMERGE = XSound.std("entity.warden.emerge");
    public static final XSound ENTITY_WARDEN_HEARTBEAT = XSound.std("entity.warden.heartbeat");
    public static final XSound ENTITY_WARDEN_HURT = XSound.std("entity.warden.hurt");
    public static final XSound ENTITY_WARDEN_LISTENING = XSound.std("entity.warden.listening");
    public static final XSound ENTITY_WARDEN_LISTENING_ANGRY = XSound.std("entity.warden.listening_angry");
    public static final XSound ENTITY_WARDEN_NEARBY_CLOSE = XSound.std("entity.warden.nearby_close");
    public static final XSound ENTITY_WARDEN_NEARBY_CLOSER = XSound.std("entity.warden.nearby_closer");
    public static final XSound ENTITY_WARDEN_NEARBY_CLOSEST = XSound.std("entity.warden.nearby_closest");
    public static final XSound ENTITY_WARDEN_ROAR = XSound.std("entity.warden.roar");
    public static final XSound ENTITY_WARDEN_SNIFF = XSound.std("entity.warden.sniff");
    public static final XSound ENTITY_WARDEN_SONIC_BOOM = XSound.std("entity.warden.sonic_boom");
    public static final XSound ENTITY_WARDEN_SONIC_CHARGE = XSound.std("entity.warden.sonic_charge");
    public static final XSound ENTITY_WARDEN_STEP = XSound.std("entity.warden.step");
    public static final XSound ENTITY_WARDEN_TENDRIL_CLICKS = XSound.std("entity.warden.tendril_clicks");
    public static final XSound ENTITY_WIND_CHARGE_THROW = XSound.std("entity.wind_charge.throw");
    public static final XSound ENTITY_WITCH_AMBIENT = XSound.std("entity.witch.ambient");
    public static final XSound ENTITY_WITCH_CELEBRATE = XSound.std("entity.witch.celebrate");
    public static final XSound ENTITY_WITCH_DEATH = XSound.std("entity.witch.death");
    public static final XSound ENTITY_WITCH_DRINK = XSound.std("entity.witch.drink");
    public static final XSound ENTITY_WITCH_HURT = XSound.std("entity.witch.hurt");
    public static final XSound ENTITY_WITCH_THROW = XSound.std("entity.witch.throw");
    public static final XSound ENTITY_WITHER_BREAK_BLOCK = XSound.std("entity.wither.break_block");
    public static final XSound ENTITY_WITHER_SKELETON_AMBIENT = XSound.std("entity.wither_skeleton.ambient");
    public static final XSound ENTITY_WITHER_SKELETON_DEATH = XSound.std("entity.wither_skeleton.death");
    public static final XSound ENTITY_WITHER_SKELETON_HURT = XSound.std("entity.wither_skeleton.hurt");
    public static final XSound ENTITY_WITHER_SKELETON_STEP = XSound.std("entity.wither_skeleton.step");
    public static final XSound ENTITY_ZOGLIN_AMBIENT = XSound.std("entity.zoglin.ambient");
    public static final XSound ENTITY_ZOGLIN_ANGRY = XSound.std("entity.zoglin.angry");
    public static final XSound ENTITY_ZOGLIN_ATTACK = XSound.std("entity.zoglin.attack");
    public static final XSound ENTITY_ZOGLIN_DEATH = XSound.std("entity.zoglin.death");
    public static final XSound ENTITY_ZOGLIN_HURT = XSound.std("entity.zoglin.hurt");
    public static final XSound ENTITY_ZOGLIN_STEP = XSound.std("entity.zoglin.step");
    public static final XSound ENTITY_ZOMBIE_CONVERTED_TO_DROWNED = XSound.std("entity.zombie.converted_to_drowned");
    public static final XSound ENTITY_ZOMBIE_DESTROY_EGG = XSound.std("entity.zombie.destroy_egg");
    public static final XSound ENTITY_ZOMBIE_VILLAGER_AMBIENT = XSound.std("entity.zombie_villager.ambient");
    public static final XSound ENTITY_ZOMBIE_VILLAGER_DEATH = XSound.std("entity.zombie_villager.death");
    public static final XSound ENTITY_ZOMBIE_VILLAGER_HURT = XSound.std("entity.zombie_villager.hurt");
    public static final XSound ENTITY_ZOMBIE_VILLAGER_STEP = XSound.std("entity.zombie_villager.step");
    public static final XSound EVENT_MOB_EFFECT_BAD_OMEN = XSound.std("event.mob_effect.bad_omen");
    public static final XSound EVENT_MOB_EFFECT_RAID_OMEN = XSound.std("event.mob_effect.raid_omen");
    public static final XSound EVENT_MOB_EFFECT_TRIAL_OMEN = XSound.std("event.mob_effect.trial_omen");
    public static final XSound EVENT_RAID_HORN = XSound.std("event.raid.horn");
    public static final XSound INTENTIONALLY_EMPTY = XSound.std("intentionally_empty");
    public static final XSound ITEM_ARMOR_EQUIP_CHAIN = XSound.std("item.armor.equip_chain");
    public static final XSound ITEM_ARMOR_EQUIP_DIAMOND = XSound.std("item.armor.equip_diamond");
    public static final XSound ITEM_ARMOR_EQUIP_ELYTRA = XSound.std("item.armor.equip_elytra");
    public static final XSound ITEM_ARMOR_EQUIP_GENERIC = XSound.std("item.armor.equip_generic");
    public static final XSound ITEM_ARMOR_EQUIP_GOLD = XSound.std("item.armor.equip_gold");
    public static final XSound ITEM_ARMOR_EQUIP_IRON = XSound.std("item.armor.equip_iron");
    public static final XSound ITEM_ARMOR_EQUIP_LEATHER = XSound.std("item.armor.equip_leather");
    public static final XSound ITEM_ARMOR_EQUIP_NETHERITE = XSound.std("item.armor.equip_netherite");
    public static final XSound ITEM_ARMOR_EQUIP_TURTLE = XSound.std("item.armor.equip_turtle");
    public static final XSound ITEM_ARMOR_EQUIP_WOLF = XSound.std("item.armor.equip_wolf");
    public static final XSound ITEM_ARMOR_UNEQUIP_WOLF = XSound.std("item.armor.unequip_wolf");
    public static final XSound ITEM_AXE_SCRAPE = XSound.std("item.axe.scrape");
    public static final XSound ITEM_AXE_STRIP = XSound.std("item.axe.strip");
    public static final XSound ITEM_AXE_WAX_OFF = XSound.std("item.axe.wax_off");
    public static final XSound ITEM_BONE_MEAL_USE = XSound.std("item.bone_meal.use");
    public static final XSound ITEM_BOOK_PAGE_TURN = XSound.std("item.book.page_turn");
    public static final XSound ITEM_BOOK_PUT = XSound.std("item.book.put");
    public static final XSound ITEM_BOTTLE_EMPTY = XSound.std("item.bottle.empty");
    public static final XSound ITEM_BOTTLE_FILL = XSound.std("item.bottle.fill");
    public static final XSound ITEM_BOTTLE_FILL_DRAGONBREATH = XSound.std("item.bottle.fill_dragonbreath");
    public static final XSound ITEM_BRUSH_BRUSHING_GENERIC = XSound.std("item.brush.brushing.generic");
    public static final XSound ITEM_BRUSH_BRUSHING_GRAVEL = XSound.std("item.brush.brushing.gravel");
    public static final XSound ITEM_BRUSH_BRUSHING_GRAVEL_COMPLETE = XSound.std("item.brush.brushing.gravel.complete");
    public static final XSound ITEM_BRUSH_BRUSHING_SAND = XSound.std("item.brush.brushing.sand");
    public static final XSound ITEM_BRUSH_BRUSHING_SAND_COMPLETE = XSound.std("item.brush.brushing.sand.complete");
    public static final XSound ITEM_BUCKET_EMPTY = XSound.std("item.bucket.empty");
    public static final XSound ITEM_BUCKET_EMPTY_AXOLOTL = XSound.std("item.bucket.empty_axolotl");
    public static final XSound ITEM_BUCKET_EMPTY_FISH = XSound.std("item.bucket.empty_fish");
    public static final XSound ITEM_BUCKET_EMPTY_LAVA = XSound.std("item.bucket.empty_lava");
    public static final XSound ITEM_BUCKET_EMPTY_POWDER_SNOW = XSound.std("item.bucket.empty_powder_snow");
    public static final XSound ITEM_BUCKET_EMPTY_TADPOLE = XSound.std("item.bucket.empty_tadpole");
    public static final XSound ITEM_BUCKET_FILL = XSound.std("item.bucket.fill");
    public static final XSound ITEM_BUCKET_FILL_AXOLOTL = XSound.std("item.bucket.fill_axolotl");
    public static final XSound ITEM_BUCKET_FILL_FISH = XSound.std("item.bucket.fill_fish");
    public static final XSound ITEM_BUCKET_FILL_LAVA = XSound.std("item.bucket.fill_lava");
    public static final XSound ITEM_BUCKET_FILL_POWDER_SNOW = XSound.std("item.bucket.fill_powder_snow");
    public static final XSound ITEM_BUCKET_FILL_TADPOLE = XSound.std("item.bucket.fill_tadpole");
    public static final XSound ITEM_BUNDLE_DROP_CONTENTS = XSound.std("item.bundle.drop_contents");
    public static final XSound ITEM_BUNDLE_INSERT = XSound.std("item.bundle.insert");
    public static final XSound ITEM_BUNDLE_INSERT_FAIL = XSound.std("item.bundle.insert_fail");
    public static final XSound ITEM_BUNDLE_REMOVE_ONE = XSound.std("item.bundle.remove_one");
    public static final XSound ITEM_CHORUS_FRUIT_TELEPORT = XSound.std("item.chorus_fruit.teleport");
    public static final XSound ITEM_CROP_PLANT = XSound.std("item.crop.plant");
    public static final XSound ITEM_CROSSBOW_HIT = XSound.std("item.crossbow.hit");
    public static final XSound ITEM_CROSSBOW_LOADING_END = XSound.std("item.crossbow.loading_end");
    public static final XSound ITEM_CROSSBOW_LOADING_MIDDLE = XSound.std("item.crossbow.loading_middle");
    public static final XSound ITEM_CROSSBOW_LOADING_START = XSound.std("item.crossbow.loading_start");
    public static final XSound ITEM_CROSSBOW_QUICK_CHARGE_1 = XSound.std("item.crossbow.quick_charge_1");
    public static final XSound ITEM_CROSSBOW_QUICK_CHARGE_2 = XSound.std("item.crossbow.quick_charge_2");
    public static final XSound ITEM_CROSSBOW_QUICK_CHARGE_3 = XSound.std("item.crossbow.quick_charge_3");
    public static final XSound ITEM_CROSSBOW_SHOOT = XSound.std("item.crossbow.shoot");
    public static final XSound ITEM_DYE_USE = XSound.std("item.dye.use");
    public static final XSound ITEM_ELYTRA_FLYING = XSound.std("item.elytra.flying");
    public static final XSound ITEM_FIRECHARGE_USE = XSound.std("item.firecharge.use");
    public static final XSound ITEM_GLOW_INK_SAC_USE = XSound.std("item.glow_ink_sac.use");
    public static final XSound ITEM_GOAT_HORN_SOUND_0 = XSound.std("item.goat_horn.sound.0");
    public static final XSound ITEM_GOAT_HORN_SOUND_1 = XSound.std("item.goat_horn.sound.1");
    public static final XSound ITEM_GOAT_HORN_SOUND_2 = XSound.std("item.goat_horn.sound.2");
    public static final XSound ITEM_GOAT_HORN_SOUND_3 = XSound.std("item.goat_horn.sound.3");
    public static final XSound ITEM_GOAT_HORN_SOUND_4 = XSound.std("item.goat_horn.sound.4");
    public static final XSound ITEM_GOAT_HORN_SOUND_5 = XSound.std("item.goat_horn.sound.5");
    public static final XSound ITEM_GOAT_HORN_SOUND_6 = XSound.std("item.goat_horn.sound.6");
    public static final XSound ITEM_GOAT_HORN_SOUND_7 = XSound.std("item.goat_horn.sound.7");
    public static final XSound ITEM_HOE_TILL = XSound.std("item.hoe.till");
    public static final XSound ITEM_HONEYCOMB_WAX_ON = XSound.std("item.honeycomb.wax_on");
    public static final XSound ITEM_HONEY_BOTTLE_DRINK = XSound.std("item.honey_bottle.drink");
    public static final XSound ITEM_INK_SAC_USE = XSound.std("item.ink_sac.use");
    public static final XSound ITEM_LODESTONE_COMPASS_LOCK = XSound.std("item.lodestone_compass.lock");
    public static final XSound ITEM_MACE_SMASH_AIR = XSound.std("item.mace.smash_air");
    public static final XSound ITEM_MACE_SMASH_GROUND = XSound.std("item.mace.smash_ground");
    public static final XSound ITEM_MACE_SMASH_GROUND_HEAVY = XSound.std("item.mace.smash_ground_heavy");
    public static final XSound ITEM_NETHER_WART_PLANT = XSound.std("item.nether_wart.plant");
    public static final XSound ITEM_OMINOUS_BOTTLE_DISPOSE = XSound.std("item.ominous_bottle.dispose");
    public static final XSound ITEM_SHIELD_BLOCK = XSound.std("item.shield.block");
    public static final XSound ITEM_SHIELD_BREAK = XSound.std("item.shield.break");
    public static final XSound ITEM_SHOVEL_FLATTEN = XSound.std("item.shovel.flatten");
    public static final XSound ITEM_SPYGLASS_STOP_USING = XSound.std("item.spyglass.stop_using");
    public static final XSound ITEM_SPYGLASS_USE = XSound.std("item.spyglass.use");
    public static final XSound ITEM_TOTEM_USE = XSound.std("item.totem.use");
    public static final XSound ITEM_TRIDENT_HIT = XSound.std("item.trident.hit");
    public static final XSound ITEM_TRIDENT_HIT_GROUND = XSound.std("item.trident.hit_ground");
    public static final XSound ITEM_TRIDENT_RETURN = XSound.std("item.trident.return");
    public static final XSound ITEM_TRIDENT_RIPTIDE_1 = XSound.std("item.trident.riptide_1");
    public static final XSound ITEM_TRIDENT_THROW = XSound.std("item.trident.throw");
    public static final XSound ITEM_TRIDENT_THUNDER = XSound.std("item.trident.thunder");
    public static final XSound ITEM_WOLF_ARMOR_BREAK = XSound.std("item.wolf_armor.break");
    public static final XSound ITEM_WOLF_ARMOR_CRACK = XSound.std("item.wolf_armor.crack");
    public static final XSound ITEM_WOLF_ARMOR_DAMAGE = XSound.std("item.wolf_armor.damage");
    public static final XSound ITEM_WOLF_ARMOR_REPAIR = XSound.std("item.wolf_armor.repair");
    public static final XSound MUSIC_CREATIVE = XSound.std("music.creative");
    public static final XSound MUSIC_CREDITS = XSound.std("music.credits");
    public static final XSound MUSIC_DISC_5 = XSound.std("music_disc.5");
    public static final XSound MUSIC_DISC_CREATOR = XSound.std("music_disc.creator");
    public static final XSound MUSIC_DISC_CREATOR_MUSIC_BOX = XSound.std("music_disc.creator_music_box");
    public static final XSound MUSIC_DISC_OTHERSIDE = XSound.std("music_disc.otherside");
    public static final XSound MUSIC_DISC_PIGSTEP = XSound.std("music_disc.pigstep");
    public static final XSound MUSIC_DISC_PRECIPICE = XSound.std("music_disc.precipice");
    public static final XSound MUSIC_DISC_RELIC = XSound.std("music_disc.relic");
    public static final XSound MUSIC_DRAGON = XSound.std("music.dragon");
    public static final XSound MUSIC_END = XSound.std("music.end");
    public static final XSound MUSIC_GAME = XSound.std("music.game");
    public static final XSound MUSIC_MENU = XSound.std("music.menu");
    public static final XSound MUSIC_NETHER_CRIMSON_FOREST = XSound.std("music.nether.crimson_forest");
    public static final XSound MUSIC_NETHER_NETHER_WASTES = XSound.std("music.nether.nether_wastes");
    public static final XSound MUSIC_NETHER_SOUL_SAND_VALLEY = XSound.std("music.nether.soul_sand_valley");
    public static final XSound MUSIC_NETHER_WARPED_FOREST = XSound.std("music.nether.warped_forest");
    public static final XSound MUSIC_OVERWORLD_BADLANDS = XSound.std("music.overworld.badlands");
    public static final XSound MUSIC_OVERWORLD_BAMBOO_JUNGLE = XSound.std("music.overworld.bamboo_jungle");
    public static final XSound MUSIC_OVERWORLD_CHERRY_GROVE = XSound.std("music.overworld.cherry_grove");
    public static final XSound MUSIC_OVERWORLD_DEEP_DARK = XSound.std("music.overworld.deep_dark");
    public static final XSound MUSIC_OVERWORLD_DESERT = XSound.std("music.overworld.desert");
    public static final XSound MUSIC_OVERWORLD_DRIPSTONE_CAVES = XSound.std("music.overworld.dripstone_caves");
    public static final XSound MUSIC_OVERWORLD_FLOWER_FOREST = XSound.std("music.overworld.flower_forest");
    public static final XSound MUSIC_OVERWORLD_FOREST = XSound.std("music.overworld.forest");
    public static final XSound MUSIC_OVERWORLD_FROZEN_PEAKS = XSound.std("music.overworld.frozen_peaks");
    public static final XSound MUSIC_OVERWORLD_GROVE = XSound.std("music.overworld.grove");
    public static final XSound MUSIC_OVERWORLD_JAGGED_PEAKS = XSound.std("music.overworld.jagged_peaks");
    public static final XSound MUSIC_OVERWORLD_JUNGLE = XSound.std("music.overworld.jungle");
    public static final XSound MUSIC_OVERWORLD_LUSH_CAVES = XSound.std("music.overworld.lush_caves");
    public static final XSound MUSIC_OVERWORLD_MEADOW = XSound.std("music.overworld.meadow");
    public static final XSound MUSIC_OVERWORLD_OLD_GROWTH_TAIGA = XSound.std("music.overworld.old_growth_taiga");
    public static final XSound MUSIC_OVERWORLD_SNOWY_SLOPES = XSound.std("music.overworld.snowy_slopes");
    public static final XSound MUSIC_OVERWORLD_SPARSE_JUNGLE = XSound.std("music.overworld.sparse_jungle");
    public static final XSound MUSIC_OVERWORLD_STONY_PEAKS = XSound.std("music.overworld.stony_peaks");
    public static final XSound MUSIC_OVERWORLD_SWAMP = XSound.std("music.overworld.swamp");
    public static final XSound MUSIC_UNDER_WATER = XSound.std("music.under_water");
    public static final XSound PARTICLE_SOUL_ESCAPE = XSound.std("particle.soul_escape");
    public static final XSound UI_CARTOGRAPHY_TABLE_TAKE_RESULT = XSound.std("ui.cartography_table.take_result");
    public static final XSound UI_HUD_BUBBLE_POP = XSound.std("ui.hud.bubble_pop");
    public static final XSound UI_LOOM_SELECT_PATTERN = XSound.std("ui.loom.select_pattern");
    public static final XSound UI_LOOM_TAKE_RESULT = XSound.std("ui.loom.take_result");
    public static final XSound UI_STONECUTTER_SELECT_RECIPE = XSound.std("ui.stonecutter.select_recipe");
    public static final XSound UI_STONECUTTER_TAKE_RESULT = XSound.std("ui.stonecutter.take_result");
    public static final XSound UI_TOAST_CHALLENGE_COMPLETE = XSound.std("ui.toast.challenge_complete");
    public static final XSound UI_TOAST_IN = XSound.std("ui.toast.in");
    public static final XSound UI_TOAST_OUT = XSound.std("ui.toast.out");
    public static final XSound WEATHER_RAIN_ABOVE = XSound.std("weather.rain.above");
    public static final XSound BLOCK_EYEBLOSSOM_CLOSE = XSound.std("block.eyeblossom.close");
    public static final XSound BLOCK_RESIN_BRICKS_FALL = XSound.std("block.resin_bricks.fall");
    public static final XSound BLOCK_RESIN_BRICKS_STEP = XSound.std("block.resin_bricks.step");
    public static final XSound BLOCK_RESIN_PLACE = XSound.std("block.resin.place");
    public static final XSound ENTITY_CREAKING_TWITCH = XSound.std("entity.creaking.twitch");
    public static final XSound BLOCK_EYEBLOSSOM_IDLE = XSound.std("block.eyeblossom.idle");
    public static final XSound BLOCK_RESIN_BREAK = XSound.std("block.resin.break");
    public static final XSound BLOCK_RESIN_BRICKS_PLACE = XSound.std("block.resin_bricks.place");
    public static final XSound BLOCK_RESIN_BRICKS_BREAK = XSound.std("block.resin_bricks.break");
    public static final XSound BLOCK_EYEBLOSSOM_CLOSE_LONG = XSound.std("block.eyeblossom.close_long");
    public static final XSound BLOCK_RESIN_FALL = XSound.std("block.resin.fall");
    public static final XSound BLOCK_RESIN_STEP = XSound.std("block.resin.step");
    public static final XSound BLOCK_EYEBLOSSOM_OPEN = XSound.std("block.eyeblossom.open");
    public static final XSound BLOCK_RESIN_BRICKS_HIT = XSound.std("block.resin_bricks.hit");
    public static final XSound BLOCK_EYEBLOSSOM_OPEN_LONG = XSound.std("block.eyeblossom.open_long");
    public static final XSound ENTITY_WIND_CHARGE_WIND_BURST = XSound.std("entity.wind_charge.wind_burst", "ENTITY_GENERIC_WIND_BURST");
    @Deprecated
    public static final XSound MUSIC_OVERWORLD_JUNGLE_AND_FOREST = XSound.std("MUSIC_OVERWORLD_JUNGLE_AND_FOREST");
    @Deprecated
    public static final XSound BLOCK_TRIAL_SPAWNER_AMBIENT_CHARGED = XSound.std("BLOCK_TRIAL_SPAWNER_AMBIENT_CHARGED");
    @Deprecated
    public static final XSound BLOCK_TRIAL_SPAWNER_CHARGE_ACTIVATE = XSound.std("BLOCK_TRIAL_SPAWNER_CHARGE_ACTIVATE");
    @Deprecated
    public static final XSound ENTITY_GOAT_SCREAMING_HORN_BREAK = XSound.std("ENTITY_GOAT_SCREAMING_HORN_BREAK");
    @Deprecated
    public static final XSound ITEM_BRUSH_BRUSH_SAND_COMPLETED = XSound.std("ITEM_BRUSH_BRUSH_SAND_COMPLETED");
    @Deprecated
    public static final XSound ITEM_GOAT_HORN_PLAY = XSound.std("ITEM_GOAT_HORN_PLAY");
    @Deprecated
    public static final XSound ITEM_BRUSH_BRUSHING = XSound.std("ITEM_BRUSH_BRUSHING");
    @Deprecated
    public static final XSound ENTITY_PARROT_IMITATE_WOLF = XSound.std("ENTITY_PARROT_IMITATE_WOLF");
    @Deprecated
    public static final XSound ENTITY_PARROT_IMITATE_POLAR_BEAR = XSound.std("ENTITY_PARROT_IMITATE_POLAR_BEAR");
    @Deprecated
    public static final XSound ENTITY_PARROT_IMITATE_ENDERMAN = XSound.std("ENTITY_PARROT_IMITATE_ENDERMAN");
    public static final @Unmodifiable Set<XSound> MUSIC = Collections.unmodifiableSet(REGISTRY.nameMapping().values().stream().filter(xSound -> xSound.name().toUpperCase(Locale.ENGLISH).startsWith("MUSIC")).collect(Collectors.toSet()));
    public static final float DEFAULT_VOLUME = 1.0f;
    public static final float DEFAULT_PITCH = 1.0f;
    public static final Pattern NAMESPACED_SOUND_PATTERN = Pattern.compile("(?<namespace>[a-z0-9._-]+):(?<key>[a-z0-9/._-]+)");
    public static final Pattern RECORD_PATTERN = Pattern.compile("\\s*(?<atLocation>~)?\\s*(?:(?<category>[\\w$_]+)@)?(?<sound>[\\w$_]+|" + NAMESPACED_SOUND_PATTERN.pattern() + ")\\s*(?:,\\s*(?<volume>[+-]?(?:\\d*\\.)?\\d+)\\s*(?:,\\s*(?<pitch>[+-]?(?:\\d*\\.)?\\d+))?)?\\s*");

    private XSound(Sound sound, String[] stringArray) {
        super(sound, stringArray);
    }

    private static XSound std(String ... stringArray) {
        return REGISTRY.std((XSound)stringArray);
    }

    @Deprecated
    @NotNull
    public static Optional<XSound> matchXSound(@NotNull String string) {
        return REGISTRY.getByName(string);
    }

    @Deprecated
    @NotNull
    public static XSound matchXSound(@NotNull Sound sound) {
        return REGISTRY.getByBukkitForm(sound);
    }

    public static XSound of(@NotNull Sound sound) {
        return REGISTRY.getByBukkitForm(sound);
    }

    public static Optional<XSound> of(@NotNull String string) {
        return REGISTRY.getByName(string);
    }

    @Deprecated
    public static XSound[] values() {
        return (XSound[])REGISTRY.values();
    }

    @NotNull
    public static @Unmodifiable Collection<XSound> getValues() {
        return REGISTRY.getValues();
    }

    private static List<String> split(@NotNull String string, char c) {
        ArrayList<String> arrayList = new ArrayList<String>(4);
        boolean bl = false;
        boolean bl2 = false;
        int n = string.length();
        int n2 = 0;
        for (int i = 0; i < n; ++i) {
            if (string.charAt(i) == c) {
                if (bl) {
                    arrayList.add(string.substring(n2, i));
                    bl = false;
                    bl2 = true;
                }
                n2 = i + 1;
                continue;
            }
            bl2 = false;
            bl = true;
        }
        if (bl || bl2) {
            arrayList.add(string.substring(n2, n));
        }
        return arrayList;
    }

    @Nullable
    public static Record play(@Nullable String string, Consumer<SoundPlayer> consumer) {
        Record record;
        try {
            record = XSound.parse(string);
        } catch (Throwable throwable) {
            return null;
        }
        if (record == null) {
            return null;
        }
        SoundPlayer soundPlayer = record.soundPlayer();
        consumer.accept(soundPlayer);
        soundPlayer.play();
        return record;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Nullable
    public static Record parse(@Nullable String string) {
        String string2;
        Optional<XSound> optional;
        if (Strings.isNullOrEmpty(string) || string.equalsIgnoreCase("none")) {
            return null;
        }
        List<String> list = XSound.split(string.replace(" ", ""), ',');
        Record record = new Record();
        String string3 = list.get(0);
        if (string3.charAt(0) == '~') {
            string3 = string3.substring(1);
            record.publicSound(true);
        } else {
            record.publicSound(false);
        }
        if (string3.isEmpty()) {
            throw new IllegalArgumentException("No sound name specified: " + string);
        }
        int n = string3.indexOf(64);
        if (n != -1) {
            optional = string3.substring(0, n);
            string2 = string3.substring(n + 1);
            Category category = Enums.getIfPresent(Category.class, ((String)((Object)optional)).toUpperCase(Locale.ENGLISH)).orNull();
            if (category == null) {
                throw new IllegalArgumentException("Unknown sound category '" + (String)((Object)optional) + "' in: " + string);
            }
            record.inCategory(category);
        } else {
            string2 = string3;
        }
        if (string2.isEmpty()) {
            throw new IllegalArgumentException("No sound name specified: " + string3);
        }
        optional = XSound.of(string2);
        if (!optional.isPresent()) {
            if (string2.indexOf(58) == -1) throw new IllegalArgumentException("Unknown sound: " + string3 + " -> '" + string2 + '\'');
            if (!NAMESPACED_SOUND_PATTERN.matcher(string2 = string2.toLowerCase(Locale.ENGLISH)).matches()) {
                throw new IllegalArgumentException("Unknown sound '" + string2 + "', invalid namespace characters: " + string3);
            }
            record.withSound(string2);
        } else {
            record.withSound((XSound)optional.get());
        }
        try {
            if (list.size() > 1) {
                record.withVolume(Float.parseFloat(list.get(1)));
            }
        } catch (NumberFormatException numberFormatException) {
            throw new NumberFormatException("Invalid number '" + list.get(1) + "' for sound volume '" + string + '\'');
        }
        try {
            if (list.size() > 2) {
                record.withPitch(Float.parseFloat(list.get(2)));
            }
        } catch (NumberFormatException numberFormatException) {
            throw new NumberFormatException("Invalid number '" + list.get(2) + "' for sound pitch '" + string + '\'');
        }
        try {
            if (list.size() <= 3) return record;
            record.withSeed(Long.parseLong(list.get(3)));
            return record;
        } catch (NumberFormatException numberFormatException) {
            throw new NumberFormatException("Invalid number '" + list.get(3) + "' for sound seed '" + string + '\'');
        }
    }

    public static void stopMusic(@NotNull Player player) {
        Objects.requireNonNull(player, "Cannot stop playing musics from null player");
        for (XSound xSound : MUSIC) {
            Sound sound = (Sound)xSound.get();
            if (sound == null) continue;
            player.stopSound(sound);
        }
    }

    @Deprecated
    @Nullable
    public Sound parseSound() {
        return (Sound)this.get();
    }

    public void stopSound(@NotNull Player player) {
        Objects.requireNonNull(player, "Cannot stop playing sound from null player");
        Sound sound = (Sound)this.get();
        if (sound != null) {
            player.stopSound(sound);
        }
    }

    public void play(@NotNull Entity entity) {
        Objects.requireNonNull(entity, "Cannot play sound for null entity");
        SoundPlayer soundPlayer = this.record().soundPlayer();
        if (entity instanceof Player) {
            soundPlayer.forPlayers((Player)entity);
        } else if (entity instanceof LivingEntity) {
            soundPlayer.atLocation(((LivingEntity)entity).getEyeLocation());
        } else {
            soundPlayer.atLocation(entity.getLocation());
        }
        soundPlayer.play();
    }

    public void play(@NotNull Location location) {
        Objects.requireNonNull(location, "Cannot play sound at null location");
        this.record().soundPlayer().atLocation(location).play();
    }

    public void play(@NotNull Entity entity, float f, float f2) {
        if (!(entity instanceof Player)) {
            Location location = entity instanceof LivingEntity ? ((LivingEntity)entity).getEyeLocation() : entity.getLocation();
            this.play(location, f, f2);
            return;
        }
        this.record().withVolume(f).withPitch(f2).soundPlayer().forPlayers((Player)entity).play();
    }

    public void play(@NotNull Location location, float f, float f2) {
        this.record().withVolume(f).withPitch(f2).soundPlayer().atLocation(location).play();
    }

    public Record record() {
        return new Record().withSound(this);
    }

    public static final class Record
    implements Cloneable {
        private static final Random RANDOM = new Random();
        private Object sound;
        @NotNull
        private Category category = Category.MASTER;
        @Nullable
        private Long seed;
        private float volume = 1.0f;
        private float pitch = 1.0f;
        private boolean publicSound;

        @Nullable
        public Long getSeed() {
            return this.seed;
        }

        public Object std() {
            return this.sound;
        }

        @NotNull
        public Category getCategory() {
            return this.category;
        }

        public float getVolume() {
            return this.volume;
        }

        public float getPitch() {
            return this.pitch;
        }

        public Record inCategory(Category category) {
            this.category = Objects.requireNonNull(category, "Sound category cannot be null");
            return this;
        }

        public SoundPlayer soundPlayer() {
            return new SoundPlayer(this);
        }

        public Record withSound(@NotNull XSound xSound) {
            Objects.requireNonNull(xSound, "Cannot play a null sound");
            this.sound = xSound;
            return this;
        }

        public Record withSound(@NotNull String string) {
            Objects.requireNonNull(string, "Cannot play a null sound");
            string = string.toLowerCase(Locale.ENGLISH);
            if (string.indexOf(58) < 0) {
                throw new IllegalArgumentException("Raw sound name doesn't contain both namespace and key: " + string);
            }
            this.sound = string;
            return this;
        }

        public long generateSeed() {
            return this.seed == null ? RANDOM.nextLong() : this.seed.longValue();
        }

        public Record withVolume(float f) {
            this.volume = f;
            return this;
        }

        public Record publicSound(boolean bl) {
            this.publicSound = bl;
            return this;
        }

        public Record withPitch(float f) {
            this.pitch = f;
            return this;
        }

        public Record withSeed(Long l) {
            this.seed = l;
            return this;
        }

        public String rebuild() {
            String string = "";
            if (this.publicSound) {
                string = string + "~";
            }
            if (this.category != Category.MASTER) {
                string = string + this.category.name();
            }
            string = string + this.sound + ", " + this.volume + ", " + this.pitch;
            if (this.seed != null) {
                string = string + ", " + this.seed;
            }
            return string;
        }

        public String toString() {
            return "Record{sound=" + this.sound + ", category=" + (Object)((Object)this.category) + ", seed=" + this.seed + ", volume=" + this.volume + ", pitch=" + this.pitch + ", publicSound=" + this.publicSound + '}';
        }

        public Record clone() {
            Record record = new Record();
            record.sound = this.sound;
            record.volume = this.volume;
            record.pitch = this.pitch;
            record.publicSound = this.publicSound;
            record.seed = this.seed;
            return record;
        }
    }

    public static final class SoundPlayer {
        private static final byte SUPPORTED_METHOD_LEVEL;
        public Record record;
        public Set<UUID> players = new HashSet<UUID>(10);
        public Set<UUID> heard = new HashSet<UUID>();
        @Nullable
        public Location location;

        public SoundPlayer(Record record) {
            this.withRecord(record);
        }

        public SoundPlayer withRecord(Record record) {
            this.record = Objects.requireNonNull(record, "Cannot play a null record");
            return this;
        }

        public SoundPlayer forPlayers(@Nullable Player ... playerArray) {
            this.players.clear();
            if (playerArray != null && playerArray.length > 0) {
                this.players.addAll(Arrays.stream(playerArray).map(Entity::getUniqueId).collect(Collectors.toSet()));
            }
            return this;
        }

        public SoundPlayer atLocation(@Nullable Location location) {
            this.location = location;
            return this;
        }

        public SoundPlayer forPlayers(@Nullable Collection<Player> collection) {
            this.players.clear();
            this.players.addAll(collection.stream().map(Entity::getUniqueId).collect(Collectors.toList()));
            return this;
        }

        public Collection<Player> getHearingPlayers() {
            if (this.record.publicSound || this.players.isEmpty()) {
                Location location;
                if (this.location == null) {
                    if (this.players.size() != 1) {
                        throw new IllegalStateException("Cannot play public sound when no location is specified: " + this);
                    }
                    Player player = Bukkit.getPlayer((UUID)this.players.iterator().next());
                    if (player == null) {
                        return new ArrayList<Player>();
                    }
                    location = player.getEyeLocation();
                } else {
                    location = this.location;
                }
                return SoundPlayer.getHearingPlayers(location, this.record.volume);
            }
            return SoundPlayer.toOnlinePlayers(this.players, Collectors.toList());
        }

        @NotNull
        public static Collection<Player> getHearingPlayers(Location location, double d) {
            d = d > 1.0 ? 16.0 * d : 16.0;
            double d2 = d * d;
            List list = location.getWorld().getPlayers();
            ArrayList<Player> arrayList = new ArrayList<Player>(list.size());
            double d3 = location.getX();
            double d4 = location.getY();
            double d5 = location.getZ();
            for (Player player : list) {
                double d6;
                double d7;
                Location location2 = player.getLocation();
                double d8 = d3 - location2.getX();
                double d9 = d8 * d8 + (d7 = d4 - location2.getY()) * d7 + (d6 = d5 - location2.getZ()) * d6;
                if (!(d9 < d2)) continue;
                arrayList.add(player);
            }
            return arrayList;
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public void play() {
            Location location;
            if (this.location == null) {
                if (this.players.size() != 1) throw new IllegalStateException("Cannot play sound when there is no location available");
                UUID uUID = this.players.iterator().next();
                Player player = Bukkit.getPlayer((UUID)uUID);
                if (player == null) {
                    return;
                }
                location = player.getEyeLocation();
            } else {
                location = this.location;
            }
            this.play(location);
        }

        public void play(@NotNull Location location) {
            Collection<Player> collection = this.getHearingPlayers();
            this.heard = collection.stream().map(Entity::getUniqueId).collect(Collectors.toSet());
            if (collection.isEmpty()) {
                return;
            }
            this.play(collection, location);
        }

        private static <A, R> R toOnlinePlayers(Collection<UUID> collection, Collector<Player, A, R> collector) {
            return collection.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(collector);
        }

        public void play(Collection<Player> collection, @NotNull Location location) {
            Objects.requireNonNull(location, "Cannot play sound at null location");
            Sound sound = this.record.sound instanceof XSound ? (Sound)((XSound)this.record.sound).get() : null;
            String string = this.record.sound instanceof String ? (String)this.record.sound : null;
            block5: for (Player player : collection) {
                switch (SUPPORTED_METHOD_LEVEL) {
                    case 3: {
                        if (sound != null) {
                            player.playSound(location, sound, (SoundCategory)this.record.category.getBukkitObject(), this.record.volume, this.record.pitch, this.record.generateSeed());
                            continue block5;
                        }
                        player.playSound(location, string, (SoundCategory)this.record.category.getBukkitObject(), this.record.volume, this.record.pitch, this.record.generateSeed());
                        continue block5;
                    }
                    case 2: {
                        if (sound != null) {
                            player.playSound(location, sound, (SoundCategory)this.record.category.getBukkitObject(), this.record.volume, this.record.pitch);
                            continue block5;
                        }
                        player.playSound(location, string, (SoundCategory)this.record.category.getBukkitObject(), this.record.volume, this.record.pitch);
                        continue block5;
                    }
                    case 1: {
                        if (sound != null) {
                            player.playSound(location, sound, this.record.volume, this.record.pitch);
                            continue block5;
                        }
                        player.playSound(location, string, this.record.volume, this.record.pitch);
                        continue block5;
                    }
                }
                throw new IllegalStateException("Unknown format: " + SUPPORTED_METHOD_LEVEL);
            }
        }

        public void stopSound() {
            if (this.heard == null || this.heard.isEmpty()) {
                return;
            }
            List<Player> list = SoundPlayer.toOnlinePlayers(this.heard, Collectors.toList());
            list.forEach(player -> {
                if (this.record.sound instanceof XSound) {
                    player.stopSound((Sound)((XSound)this.record.sound).get());
                } else {
                    player.stopSound((String)this.record.sound);
                }
            });
        }

        static {
            int n;
            try {
                Player.class.getDeclaredMethod("playSound", Location.class, String.class, SoundCategory.class, Float.TYPE, Float.TYPE, Long.TYPE);
                n = 3;
            } catch (Throwable throwable) {
                try {
                    Player.class.getDeclaredMethod("playSound", Location.class, String.class, SoundCategory.class, Float.TYPE, Float.TYPE);
                    n = 2;
                } catch (Throwable throwable2) {
                    try {
                        Player.class.getDeclaredMethod("playSound", Location.class, Sound.class, Float.TYPE, Float.TYPE);
                        n = 1;
                    } catch (Throwable throwable3) {
                        throw new RuntimeException("None of sound methods are supported", throwable3);
                    }
                }
            }
            SUPPORTED_METHOD_LEVEL = (byte)n;
        }
    }

    public static enum Category {
        MASTER,
        MUSIC,
        RECORDS,
        WEATHER,
        BLOCKS,
        HOSTILE,
        NEUTRAL,
        PLAYERS,
        AMBIENT,
        VOICE;

        private final Object bukkitObject;

        public boolean isSupported() {
            return this.bukkitObject != null;
        }

        private static <T> T cast(Object object) {
            return (T)object;
        }

        private Category() {
            Object var3_3 = null;
            try {
                var3_3 = Enums.getIfPresent((Class)Category.cast(Class.forName("org.bukkit.SoundCategory")), this.name()).orNull();
            } catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
            this.bukkitObject = var3_3;
        }

        public Object getBukkitObject() {
            return this.bukkitObject;
        }
    }
}

