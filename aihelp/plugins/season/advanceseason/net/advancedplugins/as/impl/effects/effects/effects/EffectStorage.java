/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package net.advancedplugins.as.impl.effects.effects.effects;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.List;
import net.advancedplugins.as.impl.effects.effects.EffectsHandler;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.effects.effects.effects.external.advancedseasons.SetTemperatureEffect;
import net.advancedplugins.as.impl.effects.effects.effects.external.advancedskills.GiveManaEffect;
import net.advancedplugins.as.impl.effects.effects.effects.external.advancedskills.GivePointsEffect;
import net.advancedplugins.as.impl.effects.effects.effects.external.ae.AddEnchantEffect;
import net.advancedplugins.as.impl.effects.effects.effects.external.ae.AddSoulsEffect;
import net.advancedplugins.as.impl.effects.effects.effects.external.ae.RemoveEnchantEffect;
import net.advancedplugins.as.impl.effects.effects.effects.external.ae.RemoveSoulsEffect;
import net.advancedplugins.as.impl.effects.effects.effects.external.mcmmo.AddExpMcmmoEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ActionbarEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.AddDurabilityArmorEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.AddDurabilityCurrentItemEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.AddDurabilityItemEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.AddFoodEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.AddHealthEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.AddMoneyEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.AddWalkSpeedEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.AirEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ApplyPotionEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.AutoReelEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.BleedEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.BloodEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.BoostEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.BreakBlockEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.BreakTreeEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.BroadcastEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.BroadcastPermissionEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.BurnEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.CactusEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.CancelEventEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.CancelUseEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ConsoleCommandEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.CureEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.CurePermanentEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.DecreaseDamageEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.DeleteItemEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.DisableActivationEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.DisableKnockbackEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.DisarmEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.DoHarmEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.DoubleDamageEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.DropHeadEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.DropHeldItemEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.DropItemEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ExpEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ExplodeEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ExtinguishEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.FireballEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.FireworkEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.FlyEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.FlySpeedEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.FreezeEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.GiveItemEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.GuardEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.HalfDamageEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.IgnoreArmorDamageEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.IgnoreArmorProtectionEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.IncreaseDamageEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.InvertVariableEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.InvincibleEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.KeepOnDeathEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.KillEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.LavaWalkerEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.LightningEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.MessageEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.MoreDropsEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.NegateDamageEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.OpenCraftingTableEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.OpenEnderChestEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ParticleEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ParticleLineEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.PermissionEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.PlantSeedsEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.PlayEntityEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.PlaySoundEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.PlaySoundOutloudEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.PlayerCommandEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.PotionOverrideEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ProjectileEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.PullAwayEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.PullCloserEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.PumpkinEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.RemoveArmorEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.RemoveHealthDamageEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.RemoveHealthDamageTotemEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.RemoveHealthEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.RemoveHealthTotemEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.RemoveMoneyEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.RemoveRandomArmorEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.RepairEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ResetComboEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ReviveEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ScreenFreezeEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.SetAirEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.SetBlockEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.SetMaxCatchTimeEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.SetMinCatchTimeEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.SetVariableEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.ShuffleHotbarEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.SmeltEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.SnowblindEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.SpawnArrowsEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.SpawnBlocksEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.SpawnEntityEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.StealExpEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.StealGuardEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.StealHealthEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.StealMoneyEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.StopKnockbackEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.SubtitleEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.TakeAwayEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.TeleportBehindEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.TeleportEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.TitleEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.TntEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.TotemEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.TpDropsEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.WalkSpeedEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.WaterWalkerEffect;
import net.advancedplugins.as.impl.effects.effects.effects.internal.WebWalkerEffect;
import net.advancedplugins.as.impl.utils.hooks.HookPlugin;
import net.advancedplugins.as.impl.utils.hooks.HooksHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class EffectStorage {
    private ImmutableMap<String, AdvancedEffect> effectMap = ImmutableMap.builder().build();

    public EffectStorage(JavaPlugin javaPlugin) {
        this.register(javaPlugin, new ActionbarEffect(javaPlugin));
        this.register(javaPlugin, new AddMoneyEffect(javaPlugin));
        this.register(javaPlugin, new RemoveMoneyEffect(javaPlugin));
        this.register(javaPlugin, new AddDurabilityArmorEffect(javaPlugin));
        this.register(javaPlugin, new AddDurabilityCurrentItemEffect(javaPlugin));
        this.register(javaPlugin, new AddDurabilityItemEffect(javaPlugin));
        this.register(javaPlugin, new AddFoodEffect(javaPlugin));
        this.register(javaPlugin, new AddHealthEffect(javaPlugin));
        this.register(javaPlugin, new AddWalkSpeedEffect(javaPlugin));
        this.register(javaPlugin, new AirEffect(javaPlugin));
        this.register(javaPlugin, new AutoReelEffect(javaPlugin));
        this.register(javaPlugin, new BleedEffect(javaPlugin));
        this.register(javaPlugin, new BloodEffect(javaPlugin));
        this.register(javaPlugin, new BoostEffect(javaPlugin));
        this.register(javaPlugin, new BreakBlockEffect(javaPlugin));
        this.register(javaPlugin, new BreakTreeEffect(javaPlugin));
        this.register(javaPlugin, new BroadcastEffect(javaPlugin));
        this.register(javaPlugin, new BroadcastPermissionEffect(javaPlugin));
        this.register(javaPlugin, new BurnEffect(javaPlugin));
        this.register(javaPlugin, new CactusEffect(javaPlugin));
        this.register(javaPlugin, new CancelUseEffect(javaPlugin));
        this.register(javaPlugin, new CancelEventEffect(javaPlugin));
        this.register(javaPlugin, new ConsoleCommandEffect(javaPlugin));
        this.register(javaPlugin, new CureEffect(javaPlugin));
        this.register(javaPlugin, new CurePermanentEffect(javaPlugin));
        this.register(javaPlugin, new DecreaseDamageEffect(javaPlugin));
        this.register(javaPlugin, new DeleteItemEffect(javaPlugin));
        this.register(javaPlugin, new DisableActivationEffect(javaPlugin));
        this.register(javaPlugin, new DisableKnockbackEffect(javaPlugin));
        this.register(javaPlugin, new DisarmEffect(javaPlugin));
        this.register(javaPlugin, new DoHarmEffect(javaPlugin));
        this.register(javaPlugin, new DoubleDamageEffect(javaPlugin));
        this.register(javaPlugin, new DropHeadEffect(javaPlugin));
        this.register(javaPlugin, new DropItemEffect(javaPlugin));
        this.register(javaPlugin, new DropHeldItemEffect(javaPlugin));
        this.register(javaPlugin, new OpenCraftingTableEffect(javaPlugin));
        this.register(javaPlugin, new OpenEnderChestEffect(javaPlugin));
        this.register(javaPlugin, new PlayEntityEffect(javaPlugin));
        this.register(javaPlugin, new ExpEffect(javaPlugin));
        this.register(javaPlugin, new ExplodeEffect(javaPlugin));
        this.register(javaPlugin, new ExtinguishEffect(javaPlugin));
        this.register(javaPlugin, new FireworkEffect(javaPlugin));
        this.register(javaPlugin, new FireballEffect(javaPlugin));
        this.register(javaPlugin, new ProjectileEffect(javaPlugin));
        this.register(javaPlugin, new FlyEffect(javaPlugin));
        this.register(javaPlugin, new FlySpeedEffect(javaPlugin));
        this.register(javaPlugin, new FreezeEffect(javaPlugin));
        this.register(javaPlugin, new GiveItemEffect(javaPlugin));
        this.register(javaPlugin, new HalfDamageEffect(javaPlugin));
        this.register(javaPlugin, new IgnoreArmorDamageEffect(javaPlugin));
        this.register(javaPlugin, new IgnoreArmorProtectionEffect(javaPlugin));
        this.register(javaPlugin, new IncreaseDamageEffect(javaPlugin));
        this.register(javaPlugin, new InvincibleEffect(javaPlugin));
        this.register(javaPlugin, new InvertVariableEffect(javaPlugin));
        this.register(javaPlugin, new KillEffect(javaPlugin));
        this.register(javaPlugin, new LightningEffect(javaPlugin));
        this.register(javaPlugin, new MessageEffect(javaPlugin));
        this.register(javaPlugin, new MoreDropsEffect(javaPlugin));
        this.register(javaPlugin, new NegateDamageEffect(javaPlugin));
        this.register(javaPlugin, new ParticleEffect(javaPlugin));
        this.register(javaPlugin, new PermissionEffect(javaPlugin));
        this.register(javaPlugin, new PlantSeedsEffect(javaPlugin));
        this.register(javaPlugin, new PlayerCommandEffect(javaPlugin));
        this.register(javaPlugin, new PlaySoundEffect(javaPlugin));
        this.register(javaPlugin, new PlaySoundOutloudEffect(javaPlugin));
        this.register(javaPlugin, new ApplyPotionEffect(javaPlugin));
        this.register(javaPlugin, new PotionOverrideEffect(javaPlugin));
        this.register(javaPlugin, new PullAwayEffect(javaPlugin));
        this.register(javaPlugin, new PullCloserEffect(javaPlugin));
        this.register(javaPlugin, new PumpkinEffect(javaPlugin));
        this.register(javaPlugin, new RemoveArmorEffect(javaPlugin));
        this.register(javaPlugin, new RemoveHealthDamageEffect(javaPlugin));
        this.register(javaPlugin, new RemoveHealthDamageTotemEffect(javaPlugin));
        this.register(javaPlugin, new RemoveHealthEffect(javaPlugin));
        this.register(javaPlugin, new RemoveHealthTotemEffect(javaPlugin));
        this.register(javaPlugin, new RemoveRandomArmorEffect(javaPlugin));
        this.register(javaPlugin, new RepairEffect(javaPlugin));
        this.register(javaPlugin, new ReviveEffect(javaPlugin));
        this.register(javaPlugin, new ScreenFreezeEffect(javaPlugin));
        this.register(javaPlugin, new SetAirEffect(javaPlugin));
        this.register(javaPlugin, new SetBlockEffect(javaPlugin));
        this.register(javaPlugin, new SetMaxCatchTimeEffect(javaPlugin));
        this.register(javaPlugin, new SetMinCatchTimeEffect(javaPlugin));
        this.register(javaPlugin, new ShuffleHotbarEffect(javaPlugin));
        this.register(javaPlugin, new SmeltEffect(javaPlugin));
        this.register(javaPlugin, new SnowblindEffect(javaPlugin));
        this.register(javaPlugin, new SpawnArrowsEffect(javaPlugin));
        this.register(javaPlugin, new SpawnBlocksEffect(javaPlugin));
        this.register(javaPlugin, new SpawnEntityEffect(javaPlugin));
        this.register(javaPlugin, new StealExpEffect(javaPlugin));
        this.register(javaPlugin, new StealHealthEffect(javaPlugin));
        this.register(javaPlugin, new StealMoneyEffect(javaPlugin));
        this.register(javaPlugin, new StopKnockbackEffect(javaPlugin));
        this.register(javaPlugin, new SubtitleEffect(javaPlugin));
        this.register(javaPlugin, new TakeAwayEffect(javaPlugin));
        this.register(javaPlugin, new TeleportBehindEffect(javaPlugin));
        this.register(javaPlugin, new TeleportEffect(javaPlugin));
        this.register(javaPlugin, new TitleEffect(javaPlugin));
        this.register(javaPlugin, new TntEffect(javaPlugin));
        this.register(javaPlugin, new TotemEffect(javaPlugin));
        this.register(javaPlugin, new TpDropsEffect(javaPlugin));
        this.register(javaPlugin, new SetVariableEffect(javaPlugin));
        this.register(javaPlugin, new GuardEffect(javaPlugin));
        this.register(javaPlugin, new StealGuardEffect(javaPlugin));
        this.register(javaPlugin, new WebWalkerEffect(javaPlugin));
        this.register(javaPlugin, new WalkSpeedEffect(javaPlugin));
        this.register(javaPlugin, new WaterWalkerEffect(javaPlugin));
        this.register(javaPlugin, new LavaWalkerEffect(javaPlugin));
        this.register(javaPlugin, new KeepOnDeathEffect(javaPlugin));
        this.register(javaPlugin, new ResetComboEffect(javaPlugin));
        this.register(javaPlugin, new ParticleLineEffect(javaPlugin));
        if (Bukkit.getPluginManager().isPluginEnabled("AdvancedEnchantments")) {
            this.register(javaPlugin, new AddEnchantEffect(javaPlugin));
            this.register(javaPlugin, new RemoveEnchantEffect(javaPlugin));
            this.register(javaPlugin, new AddSoulsEffect(javaPlugin));
            this.register(javaPlugin, new RemoveSoulsEffect(javaPlugin));
        }
        if (HooksHandler.isEnabled(HookPlugin.MCMMO)) {
            this.register(javaPlugin, new AddExpMcmmoEffect(javaPlugin));
        }
        if (HooksHandler.isEnabled(HookPlugin.ADVANCEDSKILLS)) {
            this.register(javaPlugin, new GivePointsEffect(javaPlugin));
            this.register(javaPlugin, new GiveManaEffect(javaPlugin));
        }
        if (Bukkit.getPluginManager().isPluginEnabled("AdvancedSeasons")) {
            this.register(javaPlugin, new SetTemperatureEffect(javaPlugin));
        }
    }

    public boolean register(JavaPlugin javaPlugin, AdvancedEffect advancedEffect) {
        if (this.effectMap.containsKey(advancedEffect.getName())) {
            EffectsHandler.getInstance().getLogger().warning("An effect named '" + advancedEffect.getName() + "' already exists!");
            return false;
        }
        if (!javaPlugin.equals((Object)EffectsHandler.getInstance())) {
            EffectsHandler.getInstance().getLogger().info(javaPlugin.getName() + " register a new effect: " + advancedEffect.getName());
        }
        Bukkit.getPluginManager().registerEvents((Listener)advancedEffect, (Plugin)javaPlugin);
        this.effectMap = ImmutableMap.builder().putAll(this.effectMap).put(advancedEffect.getName(), advancedEffect).build();
        return true;
    }

    public void unload() {
        WaterWalkerEffect.clearQueue();
        LavaWalkerEffect.clearQueue();
    }

    public AdvancedEffect getEffect(String string) {
        return this.effectMap.get(string);
    }

    public ImmutableCollection<AdvancedEffect> getEffects() {
        return this.effectMap.values();
    }

    public List<String> getEffectsAsStringList() {
        return ((ImmutableSet)this.effectMap.keySet()).asList();
    }
}

