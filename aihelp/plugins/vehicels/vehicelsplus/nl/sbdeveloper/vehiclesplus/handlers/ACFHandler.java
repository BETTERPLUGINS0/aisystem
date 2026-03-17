/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.configuration.InvalidConfigurationException
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.handlers;

import co.aikar.commands.MessageType;
import co.aikar.commands.PaperCommandManager;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.defaults.DefaultVehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.fuel.FuelType;
import nl.sbdeveloper.vehiclesplus.api.vehicles.rims.RimDesign;
import nl.sbdeveloper.vehiclesplus.commands.AddonCommand;
import nl.sbdeveloper.vehiclesplus.commands.FuelCommand;
import nl.sbdeveloper.vehiclesplus.commands.GarageCommand;
import nl.sbdeveloper.vehiclesplus.commands.VehicleModelCommand;
import nl.sbdeveloper.vehiclesplus.commands.VehiclesCommand;
import nl.sbdeveloper.vehiclesplus.storage.file.YamlFile;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ACFHandler {
    private static final String[] defaultLanguages = new String[]{"en", "fa"};
    private static final Pattern pattern = Pattern.compile("^lang_([a-z]{2})\\.yml$");
    private static PaperCommandManager manager;

    private ACFHandler() {
    }

    public static void init(JavaPlugin javaPlugin) {
        manager = new PaperCommandManager((Plugin)javaPlugin);
        manager.getCommandContexts().registerContext(VehicleModel.class, bukkitCommandExecutionContext -> VehiclesPlusAPI.getVehicleModel(bukkitCommandExecutionContext.popFirstArg()).get());
        manager.getCommandContexts().registerContext(DefaultVehicleModel.class, bukkitCommandExecutionContext -> DefaultVehicleModel.constructBuiltInBuilder(bukkitCommandExecutionContext.popFirstArg()));
        manager.getCommandContexts().registerContext(FuelType.class, bukkitCommandExecutionContext -> VehiclesPlusAPI.getFuelType(bukkitCommandExecutionContext.popFirstArg()).get());
        manager.getCommandContexts().registerContext(RimDesign.class, bukkitCommandExecutionContext -> VehiclesPlusAPI.getRimDesign(bukkitCommandExecutionContext.popFirstArg()).get());
        manager.getCommandCompletions().registerCompletion("garages", bukkitCommandCompletionContext -> {
            if (bukkitCommandCompletionContext.getPlayer().hasPermission("vp.admin")) {
                return VehiclesPlusAPI.getGarages().keySet();
            }
            return VehiclesPlusAPI.getGarages().values().stream().filter(garage -> garage.getMembers().contains(bukkitCommandCompletionContext.getPlayer().getUniqueId()) || garage.getOwner().getUniqueId().equals(bukkitCommandCompletionContext.getPlayer().getUniqueId())).map(Garage::getName).collect(Collectors.toSet());
        });
        manager.getCommandCompletions().registerCompletion("vehiclemodels", bukkitCommandCompletionContext -> VehiclesPlusAPI.getVehicleModels().keySet());
        manager.getCommandCompletions().registerCompletion("rimdesign", bukkitCommandCompletionContext -> VehiclesPlusAPI.getRimDesigns().keySet());
        manager.getCommandCompletions().registerCompletion("fueltypes", bukkitCommandCompletionContext -> VehiclesPlusAPI.getFuelTypes().keySet());
        manager.getCommandCompletions().registerStaticCompletion("defaultvehiclemodels", Set.of((Object)"bike", (Object)"boat", (Object)"car", (Object)"helicopter", (Object)"hovercraft", (Object)"plane", (Object)"tank"));
        manager.registerCommand(new VehiclesCommand());
        manager.registerCommand(new AddonCommand());
        manager.registerCommand(new FuelCommand());
        manager.registerCommand(new GarageCommand());
        manager.registerCommand(new VehicleModelCommand());
        manager.enableUnstableAPI("help");
        manager.setFormat(MessageType.INFO, 1, ChatColor.BLUE);
        manager.setFormat(MessageType.INFO, 2, ChatColor.GRAY);
        manager.setFormat(MessageType.INFO, 3, ChatColor.GREEN);
        manager.setFormat(MessageType.INFO, 4, ChatColor.DARK_GREEN);
        manager.setFormat(MessageType.INFO, 5, ChatColor.RED);
        manager.setFormat(MessageType.INFO, 6, ChatColor.DARK_RED);
        manager.setFormat(MessageType.INFO, 7, ChatColor.WHITE);
        manager.setFormat(MessageType.INFO, 8, ChatColor.DARK_GRAY);
        File file2 = new File(javaPlugin.getDataFolder(), "locale");
        file2.mkdirs();
        for (String string2 : defaultLanguages) {
            YamlFile yamlFile = new YamlFile(javaPlugin, "locale/lang_" + string2);
            yamlFile.loadDefaults();
        }
        Object[] objectArray = file2.listFiles((file, string) -> string.startsWith("lang_") && string.endsWith(".yml"));
        if (objectArray == null) {
            return;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Object object : objectArray) {
            Matcher matcher = pattern.matcher(((File)object).getName());
            if (!matcher.matches()) continue;
            try {
                manager.getLocales().loadYamlLanguageFile((File)object, Locale.forLanguageTag(matcher.group(1)));
            } catch (IOException | InvalidConfigurationException throwable) {
                javaPlugin.getLogger().severe("Couldn't load the language file " + ((File)object).getName() + "!");
            }
            stringBuffer.append(((File)object).getName()).append(", ");
        }
        stringBuffer.delete(stringBuffer.length() - 2, stringBuffer.length());
        javaPlugin.getLogger().info("Loaded the following language files: " + String.valueOf(stringBuffer));
        manager.getLocales().setDefaultLocale(VehiclesPlus.getStorage().getConfig().getLocale());
        javaPlugin.getLogger().info("Made '" + VehiclesPlus.getStorage().getConfig().getLocale().toLanguageTag() + "' the default language.");
    }

    public static PaperCommandManager getManager() {
        return manager;
    }
}

