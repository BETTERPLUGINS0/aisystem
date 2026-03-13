/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.api.exceptions;

import com.andrei1058.bedwars.api.server.VersionSupport;

public class InvalidSoundException
extends Throwable {
    public InvalidSoundException(String s) {
        super(s + " is not a valid " + VersionSupport.getName() + " sound! Using defaults..");
    }
}

