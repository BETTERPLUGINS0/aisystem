/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.betterstructures.commands.DownloadAllContentCommand;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import java.util.List;

public class UpdateContentCommand
extends AdvancedCommand {
    public UpdateContentCommand() {
        super(List.of((Object)"updatecontent", (Object)"updateall"));
        this.setPermission("betterstructures.setup");
        this.setSenderType(SenderType.ANY);
        this.setDescription("Downloads updates for outdated BetterStructures Nightbreak content.");
        this.setUsage("/bs updatecontent");
    }

    @Override
    public void execute(CommandData commandData) {
        DownloadAllContentCommand.execute(commandData.getCommandSender(), true);
    }
}

