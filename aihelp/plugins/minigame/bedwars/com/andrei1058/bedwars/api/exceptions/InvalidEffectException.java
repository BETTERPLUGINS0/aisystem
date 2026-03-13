/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.api.exceptions;

import com.andrei1058.bedwars.api.server.VersionSupport;

public class InvalidEffectException
extends Throwable {
    public InvalidEffectException(String message) {
        super(message + " is not a valid " + VersionSupport.getName() + " effect! Using defaults..");
    }
}

