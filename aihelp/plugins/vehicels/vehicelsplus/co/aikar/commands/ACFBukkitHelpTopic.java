/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.help.GenericCommandHelpTopic
 */
package co.aikar.commands;

import co.aikar.commands.ACFUtil;
import co.aikar.commands.BukkitCommandIssuer;
import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.BukkitRootCommand;
import co.aikar.commands.CommandIssuer;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.help.GenericCommandHelpTopic;

public class ACFBukkitHelpTopic
extends GenericCommandHelpTopic {
    public ACFBukkitHelpTopic(BukkitCommandManager bukkitCommandManager, BukkitRootCommand bukkitRootCommand) {
        super((Command)bukkitRootCommand);
        final ArrayList<String> arrayList = new ArrayList<String>();
        BukkitCommandIssuer bukkitCommandIssuer = new BukkitCommandIssuer(bukkitCommandManager, (CommandSender)Bukkit.getConsoleSender()){

            @Override
            public void sendMessageInternal(String string) {
                arrayList.add(string);
            }
        };
        bukkitCommandManager.generateCommandHelp((CommandIssuer)bukkitCommandIssuer, bukkitRootCommand).showHelp(bukkitCommandIssuer);
        this.fullText = ACFUtil.join(arrayList, "\n");
    }
}

