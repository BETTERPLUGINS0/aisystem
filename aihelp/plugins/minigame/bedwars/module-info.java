/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
module com.zaxxer.hikari {
    // version: 5.0.1

    requires java.sql; // version: 11.0.2
    requires java.management; // version: 11.0.2
    requires java.naming; // version: 11.0.2
    requires org.slf4j;
    requires static org.hibernate.orm.core;
    requires static simpleclient;
    requires static metrics.core;
    requires static metrics.healthchecks;
    requires static micrometer.core;

    exports com.zaxxer.hikari;
    exports com.zaxxer.hikari.hibernate;
    exports com.zaxxer.hikari.metrics;
    exports com.zaxxer.hikari.metrics.dropwizard;
    exports com.zaxxer.hikari.metrics.micrometer;
    exports com.zaxxer.hikari.metrics.prometheus;
    exports com.zaxxer.hikari.pool;
    exports com.zaxxer.hikari.util;

}

