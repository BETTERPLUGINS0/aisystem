/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.database;

import com.andrei1058.bedwars.shop.quickbuy.QuickBuyElement;
import com.andrei1058.bedwars.stats.PlayerStats;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface Database {
    public void init();

    public boolean hasStats(UUID var1);

    public void saveStats(PlayerStats var1);

    public PlayerStats fetchStats(UUID var1);

    @Deprecated
    public void setQuickBuySlot(UUID var1, String var2, int var3);

    public String getQuickBuySlots(UUID var1, int var2);

    public HashMap<Integer, String> getQuickBuySlots(UUID var1, int[] var2);

    public boolean hasQuickBuy(UUID var1);

    public int getColumn(UUID var1, String var2);

    public Object[] getLevelData(UUID var1);

    public void setLevelData(UUID var1, int var2, int var3, String var4, int var5);

    public void setLanguage(UUID var1, String var2);

    public String getLanguage(UUID var1);

    public void pushQuickBuyChanges(HashMap<Integer, String> var1, UUID var2, List<QuickBuyElement> var3);
}

