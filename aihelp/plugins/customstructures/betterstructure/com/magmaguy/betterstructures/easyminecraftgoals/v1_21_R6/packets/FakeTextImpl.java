/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.server.level.WorldServer
 *  net.minecraft.world.entity.Display$TextDisplay
 *  net.minecraft.world.entity.EntityTypes
 *  net.minecraft.world.entity.decoration.EntityArmorStand
 *  net.minecraft.world.level.World
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_21_R6.CraftWorld
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TextDisplay
 *  org.bukkit.entity.TextDisplay$TextAlignment
 *  org.bukkit.util.Transformation
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R6.packets;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeText;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeTextSettings;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketEntityInterface;
import com.magmaguy.betterstructures.easyminecraftgoals.thirdparty.BedrockChecker;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R6.packets.AbstractPacketEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_21_R6.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public class FakeTextImpl
implements FakeText {
    private final FakeTextSettings settings;
    private Location location;
    private String currentText;
    private boolean visible = true;
    private final Map<UUID, PacketEntityInterface> playerEntities = new ConcurrentHashMap<UUID, PacketEntityInterface>();
    private final Set<UUID> bedrockPlayers = ConcurrentHashMap.newKeySet();
    private final List<Runnable> removeCallbacks = new ArrayList<Runnable>();

    public FakeTextImpl(Location location, FakeTextSettings settings) {
        this.location = location.clone();
        this.settings = new FakeTextSettings(settings);
        this.currentText = settings.getText();
    }

    @Override
    public void setText(String text) {
        this.currentText = text;
        for (Map.Entry<UUID, PacketEntityInterface> entry : this.playerEntities.entrySet()) {
            UUID uuid = entry.getKey();
            PacketEntityInterface entity = entry.getValue();
            if (this.bedrockPlayers.contains(uuid)) {
                EntityArmorStand armorStand = (EntityArmorStand)entity.getBukkitEntity();
                armorStand.p(true);
                armorStand.b((IChatBaseComponent)IChatBaseComponent.b((String)text));
            } else {
                TextDisplay textDisplay = (TextDisplay)entity.getBukkitEntity();
                textDisplay.setText(text);
            }
            entity.syncMetadata();
        }
    }

    @Override
    public String getText() {
        return this.currentText;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        for (PacketEntityInterface entity : this.playerEntities.values()) {
            entity.setVisible(visible);
        }
    }

    @Override
    public void displayTo(Player player) {
        PacketEntityInterface entity;
        if (player == null) {
            return;
        }
        UUID uuid = player.getUniqueId();
        if (this.playerEntities.containsKey(uuid)) {
            return;
        }
        boolean isBedrock = BedrockChecker.isBedrock(player);
        if (isBedrock) {
            entity = this.createArmorStandEntity(this.location);
            this.bedrockPlayers.add(uuid);
        } else {
            entity = this.createTextDisplayEntity(this.location);
        }
        this.playerEntities.put(uuid, entity);
        entity.displayTo(uuid);
    }

    @Override
    public void displayTo(UUID uuid) {
        Player player = Bukkit.getPlayer((UUID)uuid);
        if (player != null) {
            this.displayTo(player);
        }
    }

    @Override
    public void hideFrom(Player player) {
        if (player == null) {
            return;
        }
        this.hideFrom(player.getUniqueId());
    }

    @Override
    public void hideFrom(UUID uuid) {
        PacketEntityInterface entity = this.playerEntities.remove(uuid);
        if (entity != null) {
            entity.remove();
        }
        this.bedrockPlayers.remove(uuid);
    }

    @Override
    public void teleport(Location location) {
        this.location = location.clone();
        for (PacketEntityInterface entity : this.playerEntities.values()) {
            entity.teleport(location);
        }
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public void remove() {
        for (PacketEntityInterface entity : this.playerEntities.values()) {
            entity.remove();
        }
        this.playerEntities.clear();
        this.bedrockPlayers.clear();
        this.removeCallbacks.forEach(Runnable::run);
    }

    @Override
    public boolean hasViewers() {
        return !this.playerEntities.isEmpty();
    }

    private PacketEntityInterface createTextDisplayEntity(Location location) {
        TextDisplayPacketEntity entity = new TextDisplayPacketEntity(location);
        TextDisplay textDisplay = (TextDisplay)entity.getBukkitEntity();
        textDisplay.setText(this.currentText);
        textDisplay.setBillboard(this.settings.getBillboard());
        if (this.settings.hasBackgroundColor()) {
            if (this.settings.getBackgroundColor() != null) {
                textDisplay.setBackgroundColor(this.settings.getBackgroundColor());
            } else {
                textDisplay.setBackgroundColor(Color.fromARGB((int)this.settings.getBackgroundArgb()));
            }
        }
        if (this.settings.getTextOpacity() != -1) {
            textDisplay.setTextOpacity(this.settings.getTextOpacity());
        }
        textDisplay.setShadowed(this.settings.hasShadow());
        textDisplay.setSeeThrough(this.settings.isSeeThrough());
        textDisplay.setLineWidth(this.settings.getLineWidth());
        textDisplay.setViewRange(this.settings.getViewRange());
        switch (this.settings.getAlignment()) {
            case LEFT: {
                textDisplay.setAlignment(TextDisplay.TextAlignment.LEFT);
                break;
            }
            case RIGHT: {
                textDisplay.setAlignment(TextDisplay.TextAlignment.RIGHT);
                break;
            }
            default: {
                textDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);
            }
        }
        if (this.settings.getScale() != 1.0f) {
            Transformation transform = textDisplay.getTransformation();
            textDisplay.setTransformation(new Transformation(transform.getTranslation(), transform.getLeftRotation(), new Vector3f(this.settings.getScale(), this.settings.getScale(), this.settings.getScale()), transform.getRightRotation()));
        }
        entity.syncMetadata();
        return entity;
    }

    private PacketEntityInterface createArmorStandEntity(Location location) {
        ArmorStandTextEntity entity = new ArmorStandTextEntity(location);
        EntityArmorStand armorStand = (EntityArmorStand)entity.getNMSEntity();
        armorStand.l(true);
        armorStand.v(true);
        armorStand.p(true);
        armorStand.b((IChatBaseComponent)IChatBaseComponent.b((String)this.currentText));
        entity.syncMetadata();
        return entity;
    }

    private static class TextDisplayPacketEntity
    extends AbstractPacketEntity<Display.TextDisplay> {
        public TextDisplayPacketEntity(Location location) {
            super(location);
        }

        @Override
        protected Display.TextDisplay createEntity(Location location) {
            WorldServer level = ((CraftWorld)location.getWorld()).getHandle();
            return new Display.TextDisplay(EntityTypes.bA, (World)level);
        }
    }

    private static class ArmorStandTextEntity
    extends AbstractPacketEntity<EntityArmorStand> {
        public ArmorStandTextEntity(Location location) {
            super(location);
        }

        @Override
        protected EntityArmorStand createEntity(Location location) {
            WorldServer level = ((CraftWorld)location.getWorld()).getHandle();
            return new EntityArmorStand(EntityTypes.h, (World)level);
        }
    }
}

