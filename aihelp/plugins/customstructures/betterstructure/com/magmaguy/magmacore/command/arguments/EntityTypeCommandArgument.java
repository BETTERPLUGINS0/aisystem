/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.EntityType
 */
package com.magmaguy.magmacore.command.arguments;

import com.magmaguy.magmacore.command.arguments.ListStringCommandArgument;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

public class EntityTypeCommandArgument
extends ListStringCommandArgument {
    public EntityTypeCommandArgument(List<String> validValues, String hint) {
        super(validValues, hint);
    }

    public EntityTypeCommandArgument() {
        super(new ArrayList<String>(), "");
    }

    @Override
    public List<String> getSuggestions(CommandSender sender, String partialInput) {
        if (this.validValues.isEmpty()) {
            ArrayList<String> validEntities = new ArrayList<String>();
            for (EntityType value : EntityType.values()) {
                if (value.equals((Object)EntityType.UNKNOWN)) continue;
                validEntities.add(value.getKey().getKey());
            }
            return validEntities;
        }
        return super.getSuggestions(sender, partialInput);
    }

    @Override
    public boolean matchesInput(String input) {
        try {
            EntityType.valueOf((String)input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

