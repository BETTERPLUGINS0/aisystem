/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 */
package nl.mtvehicles.core.infrastructure.utils;

import java.util.HashMap;
import java.util.UUID;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.LanguageSpecific;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Language;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.listeners.VehicleVoucherListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class LanguageUtils {
    public static HashMap<UUID, Boolean> languageCheck = new HashMap();

    @LanguageSpecific
    public static void openLanguageGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, (int)18, (String)InventoryTitle.CHOOSE_LANGUAGE_MENU.getStringTitle());
        inv.setItem(0, ItemUtils.getMenuItem(Material.GOLD_BLOCK, 1, "&eEnglish", "&7Press to set all messages to English."));
        inv.setItem(1, ItemUtils.getMenuItem(Material.DIAMOND_BLOCK, 1, "&9Dutch (Nederlands)", "&7Druk om alle berichten op Nederlands te zetten."));
        inv.setItem(2, ItemUtils.getMenuItem(Material.EMERALD_BLOCK, 1, "&2Spanish (Espa\u00f1ol)", "&7Presione para configurar todos los mensajes en espa\u00f1ol."));
        inv.setItem(3, ItemUtils.getMenuItem(Material.REDSTONE_BLOCK, 1, "&4Czech (\u010ce\u0161tina)", "&7Klikni pro nastaven\u00ed v\u0161ech zpr\u00e1v do \u010de\u0161tiny."));
        inv.setItem(4, ItemUtils.getMenuItem(Material.IRON_BLOCK, 1, "&fGerman (Deutsch)", "&7Dr\u00fccken Sie, um alle Nachrichten auf Deutsch einzustellen."));
        inv.setItem(5, ItemUtils.getMenuItem(Material.LAPIS_BLOCK, 1, "&9Chinese (\u4e2d\u570b\u4eba)", "&7\u6309 \u5c07\u6240\u6709\u6d88\u606f\u8a2d\u7f6e\u70ba\u4e2d\u6587\u3002"));
        inv.setItem(6, ItemUtils.getMenuItem(Material.SLIME_BLOCK, 1, "&aTurkish (T\u00fcrk)", "&7T\u00fcm mesajlar\u0131 T\u00fcrk\u00e7e olarak ayarlamak i\u00e7in bas\u0131n."));
        inv.setItem(7, ItemUtils.getMenuItem(Material.GLASS, 1, "&fJapanese (\u65e5\u672c\u8a9e)", "&7\u3092\u62bc\u3057\u3066\u3001\u3059\u3079\u3066\u306e\u30e1\u30c3\u30bb\u30fc\u30b8\u3092\u65e5\u672c\u8a9e\u306b\u8a2d\u5b9a\u3057\u307e\u3059\u3002"));
        inv.setItem(8, ItemUtils.getMenuItem(Material.STONE, 1, "&8Hebrew (\u05e2\u05b4\u05d1\u05e8\u05b4\u05d9\u05ea)", "&7.\u05dc\u05d7\u05e5 \u05db\u05d3\u05d9 \u05dc\u05d4\u05d2\u05d3\u05d9\u05e8 \u05d0\u05ea \u05db\u05dc \u05d4\u05d4\u05d5\u05d3\u05e2\u05d5\u05ea \u05dc\u05e2\u05d1\u05e8\u05d9\u05ea"));
        inv.setItem(9, ItemUtils.getMenuItem(Material.GOLD_ORE, 1, "&6Russian (\u0420\u0443\u0441\u0441\u043a\u0438\u0439)", "&7\u041d\u0430\u0436\u043c\u0438\u0442\u0435, \u0447\u0442\u043e\u0431\u044b \u043f\u0435\u0440\u0435\u0432\u0435\u0441\u0442\u0438 \u0432\u0441\u0435 \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u044f \u043d\u0430 \u0440\u0443\u0441\u0441\u043a\u0438\u0439 \u044f\u0437\u044b\u043a."));
        inv.setItem(10, ItemUtils.getMenuItem(Material.DIAMOND_ORE, 1, "&bFrench (Fran\u00e7ais)", "&7Appuyez pour d\u00e9finir tous les messages en fran\u00e7ais."));
        inv.setItem(11, ItemUtils.getMenuItem(Material.EMERALD_ORE, 1, "&aThai (\u0e20\u0e32\u0e29\u0e32\u0e44\u0e17\u0e22)", "&7\u0e01\u0e14\u0e40\u0e1e\u0e37\u0e48\u0e2d\u0e15\u0e31\u0e49\u0e07\u0e04\u0e48\u0e32\u0e02\u0e49\u0e2d\u0e04\u0e27\u0e32\u0e21\u0e17\u0e31\u0e49\u0e07\u0e2b\u0e21\u0e14\u0e40\u0e1b\u0e47\u0e19\u0e20\u0e32\u0e29\u0e32\u0e44\u0e17\u0e22."));
        inv.setItem(12, ItemUtils.getMenuItem(Material.BONE_BLOCK, 1, "&3Greek (\u0395\u03bb\u03bb\u03b7\u03bd\u03b9\u03ba\u03ae)", "&7\u03a0\u03b9\u03ad\u03c3\u03c4\u03b5 \u03b3\u03b9\u03b1 \u03bd\u03b1 \u03c1\u03c5\u03b8\u03bc\u03af\u03c3\u03b5\u03c4\u03b5 \u03cc\u03bb\u03b1 \u03c4\u03b1 \u03bc\u03b7\u03bd\u03cd\u03bc\u03b1\u03c4\u03b1 \u03c3\u03c4\u03b1 \u03b5\u03bb\u03bb\u03b7\u03bd\u03b9\u03ba\u03ac."));
        inv.setItem(17, ItemUtils.getMenuItem(Material.PAPER, 1, "&fThat's all for now!", "&7Do you want to help us by translating the plugin? &f&nClick here"));
        p.openInventory(inv);
        languageCheck.put(p.getUniqueId(), true);
    }

    public static void changeLanguageMenu(Player p, int clickedSlot) {
        if (clickedSlot == 17) {
            languageCheck.put(p.getUniqueId(), false);
            p.sendMessage("\u00a76You may find more information here: \u00a7e\u00a7nhttps://wiki.mtvehicles.eu/translating.html");
            return;
        }
        Language language = Language.values()[clickedSlot];
        LanguageUtils.changeLanguage(p, language);
    }

    public static void changeLanguage(Player p, Language language) {
        String languageCode = language.getLanguageCode();
        languageCheck.put(p.getUniqueId(), false);
        if (ConfigModule.messagesConfig.setLanguageFile(languageCode)) {
            p.sendMessage(ConfigModule.messagesConfig.getMessage(Message.LANGUAGE_HAS_CHANGED));
            ConfigModule.secretSettings.setMessagesLanguage(language);
            ConfigModule.secretSettings.save();
            VehicleVoucherListener.createVoucherInventory();
        } else {
            p.sendMessage(ChatColor.RED + "An error occurred whilst trying to set a new language.");
            Main.instance.getLogger().severe(String.format("Could not find file messages/messages_%s.yml, aborting...", languageCode));
        }
    }
}

