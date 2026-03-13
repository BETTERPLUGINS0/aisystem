/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.magmacore.command.arguments;

import com.magmaguy.magmacore.command.arguments.ListStringCommandArgument;
import java.util.ArrayList;
import java.util.List;

public class IntegerCommandArgument
extends ListStringCommandArgument {
    public IntegerCommandArgument(List<Integer> validValues, String hint) {
        super(validValues.stream().map(String::valueOf).toList(), hint);
    }

    public IntegerCommandArgument(String hint) {
        super(new ArrayList<String>(), hint);
    }

    @Override
    public boolean matchesInput(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}

