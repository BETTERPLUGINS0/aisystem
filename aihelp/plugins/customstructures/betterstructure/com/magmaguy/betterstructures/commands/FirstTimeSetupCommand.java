/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.betterstructures.menus.BetterStructuresFirstTimeSetupMenu;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import java.util.List;

public class FirstTimeSetupCommand
extends AdvancedCommand {
    public FirstTimeSetupCommand() {
        super(List.of((Object)"initialize"));
        this.setUsage("/bs initialize");
        this.setPermission("betterstructures.initialize");
        this.setDescription("Does the first time setup of the plugin.");
        this.setSenderType(SenderType.PLAYER);
    }

    @Override
    public void execute(CommandData commandData) {
        BetterStructuresFirstTimeSetupMenu.createMenu(commandData.getPlayerSender());
    }
}

