/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.sidebar.thread;

import com.andrei1058.bedwars.sidebar.SidebarService;

public class RefreshLifeTask
implements Runnable {
    @Override
    public void run() {
        SidebarService.getInstance().refreshHealth();
    }
}

