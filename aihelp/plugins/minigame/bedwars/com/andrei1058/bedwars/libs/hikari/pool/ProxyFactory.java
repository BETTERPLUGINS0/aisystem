/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.andrei1058.bedwars.libs.hikari.pool;

import com.andrei1058.bedwars.libs.hikari.pool.HikariProxyCallableStatement;
import com.andrei1058.bedwars.libs.hikari.pool.HikariProxyConnection;
import com.andrei1058.bedwars.libs.hikari.pool.HikariProxyDatabaseMetaData;
import com.andrei1058.bedwars.libs.hikari.pool.HikariProxyPreparedStatement;
import com.andrei1058.bedwars.libs.hikari.pool.HikariProxyResultSet;
import com.andrei1058.bedwars.libs.hikari.pool.HikariProxyStatement;
import com.andrei1058.bedwars.libs.hikari.pool.PoolEntry;
import com.andrei1058.bedwars.libs.hikari.pool.ProxyConnection;
import com.andrei1058.bedwars.libs.hikari.pool.ProxyLeakTask;
import com.andrei1058.bedwars.libs.hikari.pool.ProxyStatement;
import com.andrei1058.bedwars.libs.hikari.util.FastList;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public final class ProxyFactory {
    private ProxyFactory() {
    }

    static ProxyConnection getProxyConnection(PoolEntry poolEntry, Connection connection, FastList<Statement> fastList, ProxyLeakTask proxyLeakTask, boolean bl, boolean bl2) {
        return new HikariProxyConnection(poolEntry, connection, (FastList)fastList, proxyLeakTask, bl, bl2);
    }

    static Statement getProxyStatement(ProxyConnection proxyConnection, Statement statement) {
        return new HikariProxyStatement(proxyConnection, statement);
    }

    static CallableStatement getProxyCallableStatement(ProxyConnection proxyConnection, CallableStatement callableStatement) {
        return new HikariProxyCallableStatement(proxyConnection, callableStatement);
    }

    static PreparedStatement getProxyPreparedStatement(ProxyConnection proxyConnection, PreparedStatement preparedStatement) {
        return new HikariProxyPreparedStatement(proxyConnection, preparedStatement);
    }

    static ResultSet getProxyResultSet(ProxyConnection proxyConnection, ProxyStatement proxyStatement, ResultSet resultSet) {
        return new HikariProxyResultSet(proxyConnection, proxyStatement, resultSet);
    }

    static DatabaseMetaData getProxyDatabaseMetaData(ProxyConnection proxyConnection, DatabaseMetaData databaseMetaData) {
        return new HikariProxyDatabaseMetaData(proxyConnection, databaseMetaData);
    }
}

