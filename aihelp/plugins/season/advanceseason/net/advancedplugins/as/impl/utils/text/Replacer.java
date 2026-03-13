/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.PlaceholderAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.OfflinePlayer
 */
package net.advancedplugins.as.impl.utils.text;

import com.google.common.collect.Maps;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Replacer {
    private final Map<String, Object> variables = Maps.newHashMap();
    private final Map<String, Supplier<Object>> retrievableVariables = Maps.newHashMap();
    private OfflinePlayer player;
    private boolean usePlaceholderApi;

    public static String to(String string, UnaryOperator<Replacer> unaryOperator) {
        return ((Replacer)unaryOperator.apply(new Replacer())).applyTo(string);
    }

    public static Replacer of(Map<String, String> map) {
        Replacer replacer = new Replacer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            replacer.set(entry.getKey(), entry.getValue());
        }
        return replacer;
    }

    public Replacer set(String string, Object object) {
        if (object instanceof Iterable) {
            object = String.join((CharSequence)"<new>", (Iterable)object);
        }
        if (object instanceof BigDecimal) {
            object = ((BigDecimal)object).stripTrailingZeros().toPlainString();
        }
        this.variables.put("%" + string + "%", object);
        return this;
    }

    public Replacer set(String string, Supplier<Object> supplier) {
        this.retrievableVariables.put("%" + string + "%", supplier);
        return this;
    }

    public Replacer tryAddPapi(OfflinePlayer offlinePlayer) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            this.player = offlinePlayer;
            this.usePlaceholderApi = true;
        }
        return this;
    }

    public HashMap<String, String> getPlaceholders() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : this.variables.entrySet()) {
            hashMap.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        for (Map.Entry<String, Object> entry : this.retrievableVariables.entrySet()) {
            hashMap.put(entry.getKey(), Objects.toString(((Supplier)entry.getValue()).get()));
        }
        return hashMap;
    }

    public String applyTo(String string) {
        String string2 = string;
        for (Map.Entry<String, Object> entry : this.variables.entrySet()) {
            string2 = string2.replace(entry.getKey(), Objects.toString(entry.getValue()));
        }
        for (Map.Entry<String, Object> entry : this.retrievableVariables.entrySet()) {
            if (!string2.contains(entry.getKey())) continue;
            string2 = string2.replace(entry.getKey(), Objects.toString(((Supplier)entry.getValue()).get()));
        }
        if (this.usePlaceholderApi) {
            return PlaceholderAPI.setPlaceholders((OfflinePlayer)this.player, (String)string2);
        }
        return string2;
    }

    public OfflinePlayer getPlayer() {
        return this.player;
    }

    public boolean isUsePlaceholderApi() {
        return this.usePlaceholderApi;
    }
}

