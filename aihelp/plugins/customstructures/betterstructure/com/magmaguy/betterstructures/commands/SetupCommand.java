/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.betterstructures.menus.BetterStructuresSetupMenu;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import java.util.List;

public class SetupCommand
extends AdvancedCommand {
    public SetupCommand() {
        super(List.of((Object)"setup"));
        this.setPermission("betterstructures.setup");
        this.setSenderType(SenderType.PLAYER);
        this.setDescription("The main command for setting up BetterStructures!");
        this.setUsage("/bs setup");
    }

    @Override
    public void execute(CommandData commandData) {
        BetterStructuresSetupMenu.createMenu(commandData.getPlayerSender());
    }
}

