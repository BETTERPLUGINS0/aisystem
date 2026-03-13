/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.IChatBaseComponent
 *  net.minecraft.world.entity.Display$TextDisplay
 *  net.minecraft.world.entity.EntityTypes
 *  net.minecraft.world.entity.decoration.EntityArmorStand
 *  net.minecraft.world.level.World
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_20_R3.CraftWorld
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.TextDisplay
 *  org.bukkit.entity.TextDisplay$TextAlignment
 *  org.bukkit.util.Transformation
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R3.packets;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeText;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.FakeTextSettings;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketEntityInterface;
import com.magmaguy.betterstructures.easyminecraftgoals.thirdparty.BedrockChecker;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R3.packets.AbstractPacketEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;

public class FakeTextImpl
implements FakeText {
    private final FakeTextSettings settings;
    private Location location;
    private String currentText;
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
                EntityArmorStand as = (EntityArmorStand)entity.getBukkitEntity();
                as.n(true);
                as.b((IChatBaseComponent)IChatBaseComponent.b((String)text));
            } else {
                TextDisplay td = (TextDisplay)entity.getBukkitEntity();
                td.setText(text);
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
        for (PacketEntityInterface e : this.playerEntities.values()) {
            e.setVisible(visible);
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
        PacketEntityInterface packetEntityInterface = entity = isBedrock ? this.createArmorStandEntity(this.location) : this.createTextDisplayEntity(this.location);
        if (isBedrock) {
            this.bedrockPlayers.add(uuid);
        }
        this.playerEntities.put(uuid, entity);
        entity.displayTo(uuid);
    }

    @Override
    public void displayTo(UUID uuid) {
        Player p = Bukkit.getPlayer((UUID)uuid);
        if (p != null) {
            this.displayTo(p);
        }
    }

    @Override
    public void hideFrom(Player player) {
        if (player != null) {
            this.hideFrom(player.getUniqueId());
        }
    }

    @Override
    public void hideFrom(UUID uuid) {
        PacketEntityInterface e = this.playerEntities.remove(uuid);
        if (e != null) {
            e.remove();
        }
        this.bedrockPlayers.remove(uuid);
    }

    @Override
    public void teleport(Location loc) {
        this.location = loc.clone();
        for (PacketEntityInterface e : this.playerEntities.values()) {
            e.teleport(loc);
        }
    }

    @Override
    public Location getLocation() {
        return this.location.clone();
    }

    @Override
    public void remove() {
        for (PacketEntityInterface e : this.playerEntities.values()) {
            e.remove();
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
        TextDisplay td = (TextDisplay)entity.getBukkitEntity();
        td.setText(this.currentText);
        td.setBillboard(this.settings.getBillboard());
        if (this.settings.hasBackgroundColor()) {
            td.setBackgroundColor(this.settings.getBackgroundColor() != null ? this.settings.getBackgroundColor() : Color.fromARGB((int)this.settings.getBackgroundArgb()));
        }
        if (this.settings.getTextOpacity() != -1) {
            td.setTextOpacity(this.settings.getTextOpacity());
        }
        td.setShadowed(this.settings.hasShadow());
        td.setSeeThrough(this.settings.isSeeThrough());
        td.setLineWidth(this.settings.getLineWidth());
        td.setViewRange(this.settings.getViewRange());
        switch (this.settings.getAlignment()) {
            case LEFT: {
                td.setAlignment(TextDisplay.TextAlignment.LEFT);
                break;
            }
            case RIGHT: {
                td.setAlignment(TextDisplay.TextAlignment.RIGHT);
                break;
            }
            default: {
                td.setAlignment(TextDisplay.TextAlignment.CENTER);
            }
        }
        if (this.settings.getScale() != 1.0f) {
            Transformation t = td.getTransformation();
            td.setTransformation(new Transformation(t.getTranslation(), t.getLeftRotation(), new Vector3f(this.settings.getScale(), this.settings.getScale(), this.settings.getScale()), t.getRightRotation()));
        }
        entity.syncMetadata();
        return entity;
    }

    private PacketEntityInterface createArmorStandEntity(Location location) {
        ArmorStandTextEntity entity = new ArmorStandTextEntity(location);
        EntityArmorStand as = (EntityArmorStand)entity.getNMSEntity();
        as.j(true);
        as.u(true);
        as.n(true);
        as.b((IChatBaseComponent)IChatBaseComponent.b((String)this.currentText));
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
            return new Display.TextDisplay(EntityTypes.aY, (World)((CraftWorld)location.getWorld()).getHandle());
        }
    }

    private static class ArmorStandTextEntity
    extends AbstractPacketEntity<EntityArmorStand> {
        public ArmorStandTextEntity(Location location) {
            super(location);
        }

        @Override
        protected EntityArmorStand createEntity(Location location) {
            return new EntityArmorStand(EntityTypes.d, (World)((CraftWorld)location.getWorld()).getHandle());
        }
    }
}

