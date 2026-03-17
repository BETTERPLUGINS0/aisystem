/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  me.zombie_striker.qg.QAMain
 *  me.zombie_striker.qg.handlers.MultiVersionLookup
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.SkullMeta
 */
package me.zombie_striker.qav.customitemmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.zombie_striker.qav.util.HeadUtil;
import me.zombie_striker.qg.QAMain;
import me.zombie_striker.qg.handlers.MultiVersionLookup;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class MaterialStorage {
    private static final MaterialStorage EMPTY = new MaterialStorage(null, 0, 0);
    private static final List<MaterialStorage> store = new ArrayList<MaterialStorage>();
    private final int d;
    private final Material m;
    private final int variant;
    private final String specialValues;
    private final String specialValues2;

    private MaterialStorage(Material material, int n, int n2) {
        this(material, n, n2, null);
    }

    private MaterialStorage(Material material, int n, int n2, String string) {
        this(material, n, n2, string, null);
    }

    private MaterialStorage(Material material, int n, int n2, String string, String string2) {
        this.m = material;
        this.d = n;
        this.variant = n2;
        this.specialValues = string;
        this.specialValues2 = string2;
    }

    public static MaterialStorage getMS(Material material, int n, int n2) {
        return MaterialStorage.getMS(material, n, n2, null);
    }

    public static MaterialStorage getMS(Material material, int n, int n2, String string) {
        return MaterialStorage.getMS(material, n, n2, string, null);
    }

    public static MaterialStorage getMS(Material material, int n, int n2, String string, String string2) {
        for (MaterialStorage materialStorage : store) {
            if (!MaterialStorage.matchesMaterials(materialStorage, material, n) || !MaterialStorage.matchVariants(materialStorage, n2) || !MaterialStorage.matchHeads(materialStorage, string, string2)) continue;
            return materialStorage;
        }
        MaterialStorage materialStorage = new MaterialStorage(material, n, n2, string, string2);
        store.add(materialStorage);
        return materialStorage;
    }

    private static boolean matchesMaterials(MaterialStorage materialStorage, Material material, int n) {
        return materialStorage.m == material && (materialStorage.d == n || materialStorage.d == -1);
    }

    public static boolean matchVariants(MaterialStorage materialStorage, int n) {
        return !materialStorage.hasVariant() && n == 0 || materialStorage.variant == n;
    }

    public static boolean matchHeads(MaterialStorage materialStorage, String string, String string2) {
        boolean bl = !materialStorage.hasSpecialValue() || materialStorage.hasSpecialValue2() || string != null && (string.equals("-1") || materialStorage.getSpecialValue().equals(string));
        boolean bl2 = !materialStorage.hasSpecialValue2() || string2 != null && (string2.equals("-1") || materialStorage.getSpecialValue2().equals(string2));
        return bl && bl2;
    }

    public static MaterialStorage getMS(ItemStack itemStack) {
        return MaterialStorage.getMS(itemStack, MaterialStorage.getVariant(itemStack));
    }

    public static MaterialStorage getMS(ItemStack itemStack, int n) {
        if (itemStack == null) {
            return EMPTY;
        }
        String string = itemStack.getType() == MultiVersionLookup.getSkull() ? ((SkullMeta)itemStack.getItemMeta()).getOwner() : null;
        String string2 = null;
        if (string != null) {
            string2 = HeadUtil.getTexture(itemStack);
        }
        try {
            return MaterialStorage.getMS(itemStack.getType(), itemStack.getItemMeta().hasCustomModelData() ? itemStack.getItemMeta().getCustomModelData() : 0, n, itemStack.getType() == MultiVersionLookup.getSkull() ? ((SkullMeta)itemStack.getItemMeta()).getOwner() : null, string2);
        } catch (Error | Exception throwable) {
            return MaterialStorage.getMS(itemStack.getType(), itemStack.getDurability(), n, itemStack.getType() == MultiVersionLookup.getSkull() ? ((SkullMeta)itemStack.getItemMeta()).getOwner() : null, string2);
        }
    }

    public static int getVariant(ItemStack itemStack) {
        if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            for (String string : Objects.requireNonNull(itemStack.getItemMeta().getLore())) {
                if (string.startsWith(QAMain.S_ITEM_VARIANTS_NEW)) {
                    try {
                        return Integer.parseInt(string.split(QAMain.S_ITEM_VARIANTS_NEW)[1].trim());
                    } catch (Error | Exception throwable) {
                        throwable.printStackTrace();
                        return 0;
                    }
                }
                if (!string.startsWith(QAMain.S_ITEM_VARIANTS_LEGACY)) continue;
                try {
                    return Integer.parseInt(string.split(QAMain.S_ITEM_VARIANTS_LEGACY)[1].trim());
                } catch (Error | Exception throwable) {
                    throwable.printStackTrace();
                    return 0;
                }
            }
        }
        return 0;
    }

    public int getData() {
        return this.d;
    }

    public boolean hasSpecialValue() {
        return this.specialValues != null;
    }

    public String getSpecialValue() {
        return this.specialValues;
    }

    public boolean hasSpecialValue2() {
        return this.specialValues2 != null;
    }

    public String getSpecialValue2() {
        return this.specialValues2;
    }

    public Material getMat() {
        return this.m;
    }

    public boolean hasVariant() {
        return this.variant > 0;
    }
}

