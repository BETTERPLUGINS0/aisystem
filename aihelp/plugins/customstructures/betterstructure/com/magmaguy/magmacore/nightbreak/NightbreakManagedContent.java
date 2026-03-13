/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.magmacore.nightbreak;

import com.magmaguy.magmacore.nightbreak.NightbreakAccount;

public interface NightbreakManagedContent {
    public String getNightbreakSlug();

    public String getDisplayName();

    public String getDownloadLink();

    public int getLocalVersion();

    public boolean isInstalled();

    public boolean isDownloaded();

    public boolean isOutOfDate();

    public void setOutOfDate(boolean var1);

    public NightbreakAccount.AccessInfo getCachedAccessInfo();

    public void setCachedAccessInfo(NightbreakAccount.AccessInfo var1);
}

