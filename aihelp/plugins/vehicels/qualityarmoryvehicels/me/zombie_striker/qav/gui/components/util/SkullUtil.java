/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.gui.components.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Base64;
import me.zombie_striker.qav.gui.components.util.VersionHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class SkullUtil {
    private static final Material SKULL = SkullUtil.getSkullMaterial();
    private static final Gson GSON = new Gson();

    private static Material getSkullMaterial() {
        if (VersionHelper.IS_ITEM_LEGACY) {
            return Material.valueOf((String)"SKULL_ITEM");
        }
        return Material.PLAYER_HEAD;
    }

    public static ItemStack skull() {
        return VersionHelper.IS_ITEM_LEGACY ? new ItemStack(SKULL, 1, 3) : new ItemStack(SKULL);
    }

    public static boolean isPlayerSkull(@NotNull ItemStack itemStack) {
        if (VersionHelper.IS_ITEM_LEGACY) {
            return itemStack.getType() == SKULL && itemStack.getDurability() == 3;
        }
        return itemStack.getType() == SKULL;
    }

    public static String getSkinUrl(String string) {
        String string2 = new String(Base64.getDecoder().decode(string));
        JsonObject jsonObject = GSON.fromJson(string2, JsonObject.class);
        JsonElement jsonElement = jsonObject.get("textures");
        if (jsonElement == null) {
            return null;
        }
        JsonElement jsonElement2 = jsonElement.getAsJsonObject().get("SKIN");
        if (jsonElement2 == null) {
            return null;
        }
        JsonElement jsonElement3 = jsonElement2.getAsJsonObject().get("url");
        return jsonElement3 == null ? null : jsonElement3.getAsString();
    }
}

