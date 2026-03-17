/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Particle
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.particles;

import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XBase;
import nl.sbdeveloper.vehiclesplus.libs.xseries.base.XRegistry;
import org.bukkit.Particle;

public enum XParticle implements XBase<XParticle, Particle>
{
    ANGRY_VILLAGER("VILLAGER_ANGRY"),
    ASH(new String[0]),
    BLOCK("BLOCK_CRACK", "BLOCK_DUST"),
    BLOCK_CRUMBLE(new String[0]),
    BLOCK_MARKER("BARRIER", "LIGHT"),
    BUBBLE("WATER_BUBBLE"),
    BUBBLE_COLUMN_UP(new String[0]),
    BUBBLE_POP(new String[0]),
    CAMPFIRE_COSY_SMOKE(new String[0]),
    CAMPFIRE_SIGNAL_SMOKE(new String[0]),
    CHERRY_LEAVES(new String[0]),
    CLOUD(new String[0]),
    COMPOSTER(new String[0]),
    CRIMSON_SPORE(new String[0]),
    CRIT(new String[0]),
    CURRENT_DOWN(new String[0]),
    DAMAGE_INDICATOR(new String[0]),
    DOLPHIN(new String[0]),
    DRAGON_BREATH(new String[0]),
    DRIPPING_DRIPSTONE_LAVA(new String[0]),
    DRIPPING_DRIPSTONE_WATER(new String[0]),
    DRIPPING_HONEY(new String[0]),
    DRIPPING_LAVA("DRIP_LAVA"),
    DRIPPING_OBSIDIAN_TEAR(new String[0]),
    DRIPPING_WATER("DRIP_WATER"),
    DUST("REDSTONE"),
    DUST_COLOR_TRANSITION(new String[0]),
    DUST_PILLAR(new String[0]),
    DUST_PLUME(new String[0]),
    EFFECT("SPELL"),
    EGG_CRACK(new String[0]),
    ELDER_GUARDIAN("MOB_APPEARANCE"),
    ELECTRIC_SPARK(new String[0]),
    ENCHANT("ENCHANTMENT_TABLE"),
    ENCHANTED_HIT("CRIT_MAGIC"),
    END_ROD(new String[0]),
    ENTITY_EFFECT("SPELL_MOB", "SPELL_MOB_AMBIENT"),
    EXPLOSION("EXPLOSION_LARGE"),
    EXPLOSION_EMITTER("EXPLOSION_HUGE"),
    FALLING_DRIPSTONE_LAVA(new String[0]),
    FALLING_DRIPSTONE_WATER(new String[0]),
    FALLING_DUST(new String[0]),
    FALLING_HONEY(new String[0]),
    FALLING_LAVA(new String[0]),
    FALLING_NECTAR(new String[0]),
    FALLING_OBSIDIAN_TEAR(new String[0]),
    FALLING_SPORE_BLOSSOM(new String[0]),
    FALLING_WATER(new String[0]),
    FIREWORK("FIREWORKS_SPARK"),
    FISHING("WATER_WAKE"),
    FLAME(new String[0]),
    FLASH(new String[0]),
    GLOW(new String[0]),
    GLOW_SQUID_INK(new String[0]),
    GUST(new String[0]),
    GUST_EMITTER_LARGE(new String[0]),
    GUST_EMITTER_SMALL(new String[0]),
    HAPPY_VILLAGER("VILLAGER_HAPPY"),
    HEART(new String[0]),
    INFESTED(new String[0]),
    INSTANT_EFFECT("SPELL_INSTANT"),
    ITEM("ITEM_CRACK"),
    ITEM_COBWEB(new String[0]),
    ITEM_SLIME("SLIME"),
    ITEM_SNOWBALL("SNOWBALL", "SNOW_SHOVEL"),
    LANDING_HONEY(new String[0]),
    LANDING_LAVA(new String[0]),
    LANDING_OBSIDIAN_TEAR(new String[0]),
    LARGE_SMOKE("SMOKE_LARGE"),
    LAVA(new String[0]),
    MYCELIUM("TOWN_AURA"),
    NAUTILUS(new String[0]),
    NOTE(new String[0]),
    OMINOUS_SPAWNING(new String[0]),
    PALE_OAK_LEAVES(new String[0]),
    POOF("EXPLOSION_NORMAL"),
    PORTAL(new String[0]),
    RAID_OMEN(new String[0]),
    RAIN("WATER_DROP"),
    REVERSE_PORTAL(new String[0]),
    SCRAPE(new String[0]),
    SCULK_CHARGE(new String[0]),
    SCULK_CHARGE_POP(new String[0]),
    SCULK_SOUL(new String[0]),
    SHRIEK(new String[0]),
    SMALL_FLAME(new String[0]),
    SMALL_GUST(new String[0]),
    SMOKE("SMOKE_NORMAL"),
    SNEEZE(new String[0]),
    SNOWFLAKE(new String[0]),
    SONIC_BOOM(new String[0]),
    SOUL(new String[0]),
    SOUL_FIRE_FLAME(new String[0]),
    SPIT(new String[0]),
    SPLASH("WATER_SPLASH"),
    SPORE_BLOSSOM_AIR(new String[0]),
    SQUID_INK(new String[0]),
    SWEEP_ATTACK(new String[0]),
    TOTEM_OF_UNDYING("TOTEM"),
    TRAIL(new String[0]),
    TRIAL_OMEN(new String[0]),
    TRIAL_SPAWNER_DETECTION(new String[0]),
    TRIAL_SPAWNER_DETECTION_OMINOUS(new String[0]),
    UNDERWATER("SUSPENDED"),
    VAULT_CONNECTION(new String[0]),
    VIBRATION(new String[0]),
    WARPED_SPORE(new String[0]),
    WAX_OFF(new String[0]),
    WAX_ON(new String[0]),
    WHITE_ASH(new String[0]),
    WHITE_SMOKE(new String[0]),
    WITCH("SPELL_WITCH");

    public static final XRegistry<XParticle, Particle> REGISTRY;
    private final Particle particle;

    private XParticle(String ... stringArray) {
        this.particle = (Particle)Data.REGISTRY.stdEnum(this, stringArray);
    }

    @Override
    public String[] getNames() {
        return new String[]{this.name()};
    }

    @Override
    public Particle get() {
        return this.particle;
    }

    public static XParticle of(Particle particle) {
        return REGISTRY.getByBukkitForm(particle);
    }

    public static Optional<XParticle> of(String string) {
        return REGISTRY.getByName(string);
    }

    static {
        REGISTRY = Data.REGISTRY;
    }

    private static final class Data {
        private static final XRegistry<XParticle, Particle> REGISTRY = new XRegistry<XParticle, Particle>(Particle.class, XParticle.class, XParticle[]::new);

        private Data() {
        }
    }
}

