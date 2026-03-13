/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.hikari.pool;

import com.andrei1058.bedwars.libs.hikari.pool.ProxyConnection;
import com.andrei1058.bedwars.libs.hikari.pool.ProxyPreparedStatement;
import java.sql.CallableStatement;

public abstract class ProxyCallableStatement
extends ProxyPreparedStatement
implements CallableStatement {
    protected ProxyCallableStatement(ProxyConnection connection, CallableStatement statement) {
        super(connection, statement);
    }
}

