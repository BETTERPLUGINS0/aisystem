/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.m56738.smoothcoasters.api.implementation;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import me.m56738.smoothcoasters.api.Feature;
import me.m56738.smoothcoasters.api.NetworkInterface;
import me.m56738.smoothcoasters.api.implementation.Implementation;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ImplV5
implements Implementation {
    protected static final String CHANNEL_ROTATION = "smoothcoasters:rot";
    protected static final String CHANNEL_ENTITY_ROTATION = "smoothcoasters:erot";
    protected static final String CHANNEL_ENTITY_PROPERTIES = "smoothcoasters:eprop";
    protected static final String CHANNEL_ROTATION_LIMIT = "smoothcoasters:limit";
    private final EnumSet<Feature> features = EnumSet.of(Feature.ROTATION, Feature.ENTITY_ROTATION, Feature.ENTITY_PROPERTIES, Feature.ROTATION_LIMIT);

    public ImplV5(Plugin plugin) {
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL_ROTATION);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL_ENTITY_ROTATION);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL_ENTITY_PROPERTIES);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, CHANNEL_ROTATION_LIMIT);
    }

    @Override
    public EnumSet<Feature> getFeatures() {
        return this.features;
    }

    @Override
    public byte getVersion() {
        return 5;
    }

    @Override
    public void sendRotation(NetworkInterface networkInterface, Player player, float f, float f2, float f3, float f4, byte by) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(17);
        byteBuffer.putFloat(f);
        byteBuffer.putFloat(f2);
        byteBuffer.putFloat(f3);
        byteBuffer.putFloat(f4);
        byteBuffer.put(by);
        byteBuffer.rewind();
        byte[] byArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(byArray);
        networkInterface.sendMessage(player, CHANNEL_ROTATION, byArray);
    }

    @Override
    public void sendEntityRotation(NetworkInterface networkInterface, Player player, int n, float f, float f2, float f3, float f4, byte by) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(21);
        byteBuffer.putInt(n);
        byteBuffer.putFloat(f);
        byteBuffer.putFloat(f2);
        byteBuffer.putFloat(f3);
        byteBuffer.putFloat(f4);
        byteBuffer.put(by);
        byteBuffer.rewind();
        byte[] byArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(byArray);
        networkInterface.sendMessage(player, CHANNEL_ENTITY_ROTATION, byArray);
    }

    @Override
    public void sendEntityProperties(NetworkInterface networkInterface, Player player, int n, byte by) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        byteBuffer.putInt(n);
        byteBuffer.put(by);
        byteBuffer.rewind();
        byte[] byArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(byArray);
        networkInterface.sendMessage(player, CHANNEL_ENTITY_PROPERTIES, byArray);
    }

    @Override
    public void sendRotationLimit(NetworkInterface networkInterface, Player player, float f, float f2, float f3, float f4) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.putFloat(f);
        byteBuffer.putFloat(f2);
        byteBuffer.putFloat(f3);
        byteBuffer.putFloat(f4);
        byteBuffer.rewind();
        byte[] byArray = new byte[byteBuffer.remaining()];
        byteBuffer.get(byArray);
        networkInterface.sendMessage(player, CHANNEL_ROTATION_LIMIT, byArray);
    }
}

