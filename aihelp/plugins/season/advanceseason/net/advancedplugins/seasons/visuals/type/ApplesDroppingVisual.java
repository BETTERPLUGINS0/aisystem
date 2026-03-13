/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package net.advancedplugins.seasons.visuals.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.advancedplugins.seasons.enums.SeasonType;
import net.advancedplugins.seasons.visuals.type.IVisualType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ApplesDroppingVisual
implements IVisualType {
    private final List<UUID> players = new ArrayList<UUID>();
    private boolean enabled = true;
    private final Random random = new Random();

    @Override
    public void tick() {
    }

    @Override
    public void tickSync() {
        for (UUID uUID : this.players) {
            Player player = Bukkit.getPlayer((UUID)uUID);
            if (player == null || !player.isOnline()) continue;
            this.dropApple(player, player.getLocation());
        }
        this.players.clear();
    }

    @Override
    public SeasonType getType() {
        return SeasonType.SUMMER;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean bl) {
        this.enabled = bl;
    }

    @Override
    public void activate(Player player) {
        this.players.add(player.getUniqueId());
    }

    public void dropApple(Player player, Location location) {
        ArrayList<Block> arrayList = new ArrayList<Block>();
        World world = location.getWorld();
        int n = location.getBlockX() - 4;
        int n2 = location.getBlockY() - 4;
        int n3 = location.getBlockZ() - 4;
        for (int i = n; i <= n + 8; ++i) {
            for (int j = n2; j <= n2 + 8; ++j) {
                for (int k = n3; k <= n3 + 8; ++k) {
                    Block block;
                    Block block2 = world.getBlockAt(i, j, k);
                    if (!block2.getType().name().equals("OAK_LEAVES") || (block = world.getBlockAt(i, j - 1, k)).getType() != Material.AIR) continue;
                    arrayList.add(block2);
                }
            }
        }
        if (!arrayList.isEmpty()) {
            Block block = ((Block)arrayList.get(this.random.nextInt(arrayList.size()))).getRelative(BlockFace.DOWN);
            Location location2 = block.getLocation().add(0.5, 0.0, 0.5);
            block.getWorld().dropItemNaturally(location2, new ItemStack(Material.APPLE));
        }
    }
}

