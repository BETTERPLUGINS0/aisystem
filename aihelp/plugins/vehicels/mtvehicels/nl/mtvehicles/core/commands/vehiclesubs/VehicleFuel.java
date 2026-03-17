/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package nl.mtvehicles.core.commands.vehiclesubs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.mtvehicles.core.events.inventory.JerryCanMenuOpen;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemFactory;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VehicleFuel
extends MTVSubCommand {
    public VehicleFuel() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!this.checkPermission("mtvehicles.benzine")) {
            return true;
        }
        Inventory inv = Bukkit.createInventory(null, (int)9, (String)InventoryTitle.JERRYCAN_MENU.getStringTitle());
        List jerrycans = (List)ConfigModule.defaultConfig.get(DefaultConfig.Option.JERRYCANS);
        assert (jerrycans != null);
        Iterator iterator = jerrycans.iterator();
        while (iterator.hasNext()) {
            int jerrycan = (Integer)iterator.next();
            inv.addItem(new ItemStack[]{VehicleFuel.jerrycanItem(jerrycan, jerrycan)});
        }
        JerryCanMenuOpen api = new JerryCanMenuOpen(this.player);
        api.call();
        if (api.isCancelled()) {
            return true;
        }
        this.player.openInventory(inv);
        return true;
    }

    public static ItemStack jerrycanItem(int maxFuel, int currentFuel) {
        ItemStack is = new ItemFactory(Material.getMaterial((String)"DIAMOND_HOE")).setAmount(1).setDurability(58).setNBT("mtvehicles.benzineval", "" + currentFuel).setNBT("mtvehicles.benzinesize", "" + maxFuel).toItemStack();
        ItemMeta im = is.getItemMeta();
        ArrayList<String> itemlore = new ArrayList<String>();
        itemlore.add(TextUtils.colorize("&8"));
        itemlore.add(TextUtils.colorize("&7" + ConfigModule.messagesConfig.getMessage(Message.JERRYCAN) + " &e" + currentFuel + "&7/&e" + maxFuel + "&7l"));
        assert (im != null);
        im.setLore(itemlore);
        im.setUnbreakable(true);
        im.setDisplayName(TextUtils.colorize("&6" + ConfigModule.messagesConfig.getMessage(Message.JERRYCAN) + " " + maxFuel + "L"));
        is.setItemMeta(im);
        return is;
    }
}

