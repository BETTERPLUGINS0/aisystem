/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.betterstructures.commands;

import com.magmaguy.betterstructures.config.modulegenerators.ModuleGeneratorsConfig;
import com.magmaguy.betterstructures.config.modulegenerators.ModuleGeneratorsConfigFields;
import com.magmaguy.betterstructures.modules.WFCGenerator;
import com.magmaguy.magmacore.command.AdvancedCommand;
import com.magmaguy.magmacore.command.CommandData;
import com.magmaguy.magmacore.command.SenderType;
import com.magmaguy.magmacore.command.arguments.ListStringCommandArgument;
import com.magmaguy.magmacore.util.Logger;
import java.util.List;

public class GenerateModulesCommand
extends AdvancedCommand {
    public GenerateModulesCommand() {
        super(List.of((Object)"generateModules"));
        this.setUsage("/bs generateModules <ModuleGeneratorsConfigFile.yml>");
        this.addArgument("moduleGeneratorsConfigFile", new ListStringCommandArgument(ModuleGeneratorsConfig.getModuleGenerators().keySet().stream().toList(), "<module.yml>"));
        this.setPermission("betterstructures.generatemodules");
        this.setDescription("Generates modular builds in a dedicated world, based on the generator's configuration file.");
        this.setSenderType(SenderType.PLAYER);
    }

    @Override
    public void execute(CommandData commandData) {
        ModuleGeneratorsConfigFields moduleGeneratorsConfigFields = ModuleGeneratorsConfig.getModuleGenerators().get(commandData.getStringArgument("moduleGeneratorsConfigFile"));
        if (moduleGeneratorsConfigFields == null) {
            Logger.sendMessage(commandData.getCommandSender(), "File " + commandData.getStringArgument("moduleGeneratorsConfigFile") + " not found! The world won't generate.");
            return;
        }
        WFCGenerator.generateFromConfig(moduleGeneratorsConfigFields, commandData.getPlayerSender());
    }
}

