/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.magmacore.command.arguments;

import com.magmaguy.magmacore.command.arguments.ListStringCommandArgument;
import java.util.ArrayList;
import java.util.List;

public class DoubleCommandArgument
extends ListStringCommandArgument {
    public DoubleCommandArgument(List<Double> validValues, String hint) {
        super(validValues.stream().map(String::valueOf).toList(), hint);
    }

    public DoubleCommandArgument(String hint) {
        super(new ArrayList<String>(), hint);
    }

    @Override
    public boolean matchesInput(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}

