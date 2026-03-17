/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package me.zombie_striker.qav.util;

import java.lang.reflect.Method;
import me.zombie_striker.qav.util.xseries.profiles.PlayerProfiles;
import me.zombie_striker.qav.util.xseries.profiles.builder.ProfileInstruction;
import me.zombie_striker.qav.util.xseries.profiles.builder.XSkull;
import org.bukkit.inventory.ItemStack;

public final class HeadUtil {
    public static String getTexture(ItemStack itemStack) {
        try {
            ProfileInstruction<ItemStack> profileInstruction = XSkull.of(itemStack);
            Class<?> clazz = profileInstruction.getClass();
            Method method = clazz.getMethod("getProfile", new Class[0]);
            Object object = method.invoke(profileInstruction, new Object[0]);
            if (object == null) {
                return null;
            }
            Class<PlayerProfiles> clazz2 = PlayerProfiles.class;
            Class<?> clazz3 = Class.forName("com.mojang.authlib.GameProfile");
            Method method2 = clazz2.getMethod("getTextureValue", clazz3);
            return (String)method2.invoke(null, object);
        } catch (Error | Exception throwable) {
            return null;
        }
    }
}

