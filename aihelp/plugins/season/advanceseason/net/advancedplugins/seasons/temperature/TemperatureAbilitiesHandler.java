/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.seasons.temperature;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import net.advancedplugins.as.impl.effects.effects.abilities.AdvancedAbility;
import net.advancedplugins.seasons.enums.TemperatureEvent;
import net.advancedplugins.seasons.temperature.PlayerTemperature;
import net.advancedplugins.seasons.temperature.TemperatureHandler;
import org.bukkit.entity.Player;

public class TemperatureAbilitiesHandler {
    private final TemperatureHandler temperatureHandler;
    private final ImmutableMap<TemperatureEvent, AdvancedAbility> temperatureEventAbilities;

    public TemperatureAbilitiesHandler(TemperatureHandler temperatureHandler) {
        this.temperatureHandler = temperatureHandler;
        this.temperatureEventAbilities = this.loadTemperatureEventAbilities();
    }

    public List<AdvancedAbility> getTemperatureEffects(Player player) {
        PlayerTemperature playerTemperature = this.temperatureHandler.getPlayerTemperatureMap().get(player.getUniqueId());
        if (playerTemperature == null) {
            return Collections.emptyList();
        }
        int n = playerTemperature.getDisplayTemperature();
        ArrayList<AdvancedAbility> arrayList = new ArrayList<AdvancedAbility>();
        if (n <= this.temperatureHandler.getBreathVisibility()) {
            arrayList.add(this.temperatureEventAbilities.get((Object)TemperatureEvent.BREATH_VISIBILITY));
        }
        TemperatureEvent temperatureEvent = this.temperatureHandler.getEventForTemperature(n);
        arrayList.add(this.temperatureEventAbilities.get((Object)temperatureEvent));
        return arrayList;
    }

    private ImmutableMap<TemperatureEvent, AdvancedAbility> loadTemperatureEventAbilities() {
        ImmutableMap.Builder<TemperatureEvent, AdvancedAbility> builder = ImmutableMap.builder();
        for (TemperatureEvent temperatureEvent : TemperatureEvent.values()) {
            builder.put(temperatureEvent, AdvancedAbility.readFromConfig(temperatureEvent.name(), Collections.singletonList("TEMPERATURE_EVENT"), this.temperatureHandler.getConfig().getConfigurationSection("temperatureEvents." + temperatureEvent.name().toLowerCase(Locale.ROOT))));
        }
        return builder.build();
    }
}

