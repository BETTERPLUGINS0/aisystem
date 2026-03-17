/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package co.aikar.commands;

import co.aikar.commands.InvalidCommandArgument;
import java.util.ArrayList;
import java.util.List;

public class ShowCommandHelp
extends InvalidCommandArgument {
    List<String> searchArgs = null;
    boolean search = false;

    public ShowCommandHelp() {
    }

    public ShowCommandHelp(boolean bl) {
        this.search = bl;
    }

    public ShowCommandHelp(List<String> list) {
        this(true);
        this.searchArgs = new ArrayList<String>(list);
    }
}

