/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Fish
 *  org.bukkit.entity.FishHook
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.event.Event
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.player.PlayerFishEvent
 *  org.bukkit.event.player.PlayerFishEvent$State
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.jetbrains.annotations.NotNull
 */
package net.advancedplugins.as.impl.effects.effects.effects.internal;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.lang.reflect.Method;
import java.time.Duration;
import net.advancedplugins.as.impl.effects.effects.actions.execution.ExecutionTask;
import net.advancedplugins.as.impl.effects.effects.effects.AdvancedEffect;
import net.advancedplugins.as.impl.utils.SchedulerUtils;
import net.advancedplugins.as.impl.utils.nbt.backend.ClassWrapper;
import net.advancedplugins.as.impl.utils.nbt.backend.ReflectionMethod;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fish;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AutoReelEffect
extends AdvancedEffect {
    private final Cache<FishHook, Boolean> cache = CacheBuilder.newBuilder().expireAfterWrite(Duration.ofMillis(80L)).build();

    public AutoReelEffect(JavaPlugin javaPlugin) {
        super(javaPlugin, "AUTO_REEL", "Automatically reels in the fishing rod when it gets a bite", "%e");
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, LivingEntity livingEntity, String[] stringArray) {
        this.anchorHook((PlayerFishEvent)executionTask.getBuilder().getEvent(), executionTask.getBuilder().getItem());
        return true;
    }

    @Override
    public boolean executeEffect(ExecutionTask executionTask, Location location, String[] stringArray) {
        this.anchorHook((PlayerFishEvent)executionTask.getBuilder().getEvent(), executionTask.getBuilder().getItem());
        return true;
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    private void onFish(PlayerFishEvent playerFishEvent) {
        if (playerFishEvent.getState() == PlayerFishEvent.State.REEL_IN) {
            this.cache.put(playerFishEvent.getHook(), true);
        }
    }

    private void anchorHook(PlayerFishEvent playerFishEvent, ItemStack itemStack) {
        if (playerFishEvent.getState() != PlayerFishEvent.State.BITE) {
            return;
        }
        FishHook fishHook = playerFishEvent.getHook();
        SchedulerUtils.runTask(() -> {
            if (this.cache.getIfPresent(fishHook) != null) {
                this.cache.invalidate(fishHook);
                return;
            }
            this.retrieveHook(playerFishEvent.getHook(), itemStack);
            Entity entity = playerFishEvent.getHook().getHookedEntity();
            PlayerFishEvent.State state = entity instanceof Fish ? PlayerFishEvent.State.CAUGHT_FISH : PlayerFishEvent.State.CAUGHT_ENTITY;
            PlayerFishEvent playerFishEvent2 = new PlayerFishEvent(playerFishEvent.getPlayer(), entity, playerFishEvent.getHook(), state);
            Bukkit.getPluginManager().callEvent((Event)playerFishEvent2);
        });
    }

    public void retrieveHook(@NotNull FishHook fishHook, @NotNull ItemStack itemStack) {
        try {
            Object obj = ClassWrapper.CRAFT_FISHHOOK.getClazz().cast(fishHook);
            Method method = ClassWrapper.CRAFT_FISHHOOK.getClazz().getMethod("getHandle", new Class[0]);
            Object object = method.invoke(obj, new Object[0]);
            Object object2 = ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, itemStack);
            Method method2 = object.getClass().getMethod("a", object2.getClass());
            method2.invoke(object, object2);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

