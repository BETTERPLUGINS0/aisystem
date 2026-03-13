/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.arena;

import java.text.SimpleDateFormat;

public class ArenaManager {
    private int gid = 0;
    private String day = "";
    private String month = "";

    public String generateGameID() {
        SimpleDateFormat y = new SimpleDateFormat("yy");
        SimpleDateFormat m = new SimpleDateFormat("MM");
        SimpleDateFormat d = new SimpleDateFormat("dd");
        String m2 = m.format(System.currentTimeMillis());
        String d2 = d.format(System.currentTimeMillis());
        if (!m2.equals(this.month) && !d2.equalsIgnoreCase(this.day)) {
            this.month = m2;
            this.day = d2;
            this.gid = 0;
        }
        return "bw_temp_y" + y.format(System.currentTimeMillis()) + "m" + this.month + "d" + this.day + "g" + this.gid++;
    }
}

