/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.api.Via
 *  com.viaversion.viaversion.api.protocol.version.ProtocolVersion
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 */
package me.zombie_striker.qav.hooks;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class ViaVersionHook {
    public static String getVersion(Player player) {
        if (!Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
            return XReflection.MINOR_NUMBER + "." + XReflection.PATCH_NUMBER;
        }
        try {
            int n = Via.getAPI().getPlayerVersion(player.getUniqueId());
            return ProtocolVersion.getProtocols().stream().filter(protocolVersion -> protocolVersion.getVersion() == n).findFirst().map(protocolVersion -> {
                String[] stringArray = protocolVersion.getName().split("\\.");
                return stringArray[1] + "." + stringArray[2];
            }).orElse(XReflection.MINOR_NUMBER + "." + XReflection.PATCH_NUMBER);
        } catch (Error | Exception throwable) {
            return XReflection.MINOR_NUMBER + "." + XReflection.PATCH_NUMBER;
        }
    }
}

