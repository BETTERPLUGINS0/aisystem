/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.libs.sidebar;

import com.andrei1058.bedwars.libs.sidebar.PlaceholderProvider;
import com.andrei1058.bedwars.libs.sidebar.PlayerTab;
import com.andrei1058.bedwars.libs.sidebar.ScoreLine;
import com.andrei1058.bedwars.libs.sidebar.Sidebar;
import com.andrei1058.bedwars.libs.sidebar.SidebarLine;
import com.andrei1058.bedwars.libs.sidebar.SidebarLineAnimated;
import com.andrei1058.bedwars.libs.sidebar.SidebarManager;
import com.andrei1058.bedwars.libs.sidebar.SidebarObjective;
import com.andrei1058.bedwars.libs.sidebar.VersionedTabGroup;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WrappedSidebar
implements Sidebar {
    private final LinkedList<ScoreLine> lines = new LinkedList();
    private final LinkedList<Player> receivers = new LinkedList();
    private final Collection<PlaceholderProvider> placeholderProviders = new ConcurrentLinkedQueue<PlaceholderProvider>();
    private final LinkedList<String> availableColors = new LinkedList();
    private final SidebarObjective sidebarObjective;
    private SidebarObjective healthObjective;
    private final LinkedList<VersionedTabGroup> tabView = new LinkedList();

    public WrappedSidebar(@NotNull SidebarLine title, @NotNull Collection<SidebarLine> lines, Collection<PlaceholderProvider> placeholderProvider) {
        for (ChatColor chatColor : ChatColor.values()) {
            this.availableColors.add(chatColor.toString());
        }
        this.placeholderProviders.addAll(placeholderProvider);
        for (SidebarLine line : lines) {
            SidebarLine.markHasPlaceholders(line, placeholderProvider);
        }
        this.sidebarObjective = SidebarManager.getInstance().getSidebarProvider().createObjective(this, "Sidebar", false, title, 1);
        this.sidebarObjective.refreshTitle();
        for (SidebarLine line : lines) {
            this.addLine(line);
        }
    }

    @Override
    public void setTitle(SidebarLine title) {
        SidebarLine.markHasPlaceholders(title, this.getPlaceholders());
        this.sidebarObjective.setTitle(title);
        this.sidebarObjective.sendUpdate();
    }

    @Override
    public void addPlaceholder(PlaceholderProvider placeholderProvider) {
        this.placeholderProviders.remove(placeholderProvider);
        this.placeholderProviders.add(placeholderProvider);
        ConcurrentLinkedQueue<PlaceholderProvider> placeholder = new ConcurrentLinkedQueue<PlaceholderProvider>();
        placeholder.add(placeholderProvider);
        for (ScoreLine line : this.lines) {
            SidebarLine.markHasPlaceholders(line.getLine(), placeholder);
        }
        if (null != this.sidebarObjective) {
            SidebarLine.markHasPlaceholders(this.sidebarObjective.getTitle(), placeholder);
        }
    }

    private int getAvailableScore() {
        if (this.lines.isEmpty()) {
            return 0;
        }
        if (this.lines.size() == 16) {
            return -1;
        }
        return this.lines.getFirst().getScoreAmount();
    }

    private static void scoreOffsetIncrease(@NotNull Collection<ScoreLine> lineCollections) {
        for (ScoreLine line : lineCollections) {
            line.setScoreAmount(line.getScoreAmount() + 1);
            line.sendUpdateToAllReceivers();
        }
    }

    private void order() {
        Collections.sort(this.lines);
    }

    @Override
    public void addLine(SidebarLine sidebarLine) {
        int score = this.getAvailableScore();
        if (score == -1) {
            return;
        }
        if (this.availableColors.isEmpty()) {
            return;
        }
        WrappedSidebar.scoreOffsetIncrease(this.lines);
        String color = this.availableColors.removeFirst();
        SidebarLine.markHasPlaceholders(sidebarLine, this.getPlaceholders());
        ScoreLine s = SidebarManager.getInstance().getSidebarProvider().createScoreLine(this, sidebarLine, score == 0 ? score : score - 1, color);
        s.refreshContent();
        s.sendCreateToAllReceivers();
        this.lines.add(s);
        this.order();
    }

    @Override
    public void setLine(SidebarLine sidebarLine, int line) {
        if (line >= 0 && line < this.lines.size()) {
            ScoreLine s = this.lines.get(line);
            SidebarLine.markHasPlaceholders(s.getLine(), this.getPlaceholders());
            s.setLine(sidebarLine);
        }
    }

    @Override
    public void add(Player player) {
        this.sidebarObjective.sendCreate(player);
        this.lines.forEach(line -> line.sendCreate(player));
        if (this.healthObjective != null) {
            this.healthObjective.sendCreate(player);
            this.tabView.forEach(tab -> tab.sendCreateToPlayer(player));
        }
        this.receivers.add(player);
    }

    @Override
    public void refreshPlaceholders() {
        for (ScoreLine line : this.lines) {
            if (line.getLine() instanceof SidebarLineAnimated || !line.refreshContent()) continue;
            line.sendUpdateToAllReceivers();
        }
    }

    @Override
    public void refreshTitle() {
        if (this.sidebarObjective.refreshTitle()) {
            this.sidebarObjective.sendUpdate();
        }
    }

    @Override
    public void refreshAnimatedLines() {
        for (ScoreLine line : this.lines) {
            if (!(line.getLine() instanceof SidebarLineAnimated) || !line.refreshContent()) continue;
            line.sendUpdateToAllReceivers();
        }
    }

    private static void scoreOffsetDecrease(@NotNull Collection<ScoreLine> lineCollections) {
        lineCollections.forEach(c -> c.setScoreAmount(c.getScoreAmount() - 1));
    }

    @Override
    public void removeLine(int line) {
        if (line >= 0 && line < this.lines.size()) {
            ScoreLine scoreLine = this.lines.get(line);
            this.lines.remove(line);
            scoreLine.sendRemoveToAllReceivers();
            this.restoreColor(scoreLine.getColor());
            WrappedSidebar.scoreOffsetDecrease(this.lines.subList(line, this.lines.size()));
        }
    }

    @Override
    public void clearLines() {
        this.lines.forEach(line -> {
            line.sendRemoveToAllReceivers();
            this.restoreColor(line.getColor());
        });
        WrappedSidebar.scoreOffsetDecrease(Collections.emptyList());
        this.lines.clear();
    }

    @Override
    public int lineCount() {
        return this.lines.size();
    }

    @Override
    public void removePlaceholder(String placeholder) {
        this.placeholderProviders.removeIf(p -> p.getPlaceholder().equalsIgnoreCase(placeholder));
    }

    @Override
    public Collection<PlaceholderProvider> getPlaceholders() {
        return this.placeholderProviders;
    }

    @Override
    public void remove(Player player) {
        this.tabView.forEach(tab -> tab.remove(player));
        this.lines.forEach(line -> line.sendRemove(player));
        this.sidebarObjective.sendRemove(player);
        if (this.healthObjective != null) {
            this.healthObjective.sendRemove(player);
        }
        this.receivers.remove(player);
        this.tabView.forEach(tab -> {
            tab.remove(player);
            if (Objects.equals(tab.getSubject(), player)) {
                tab.setSubject(null);
            }
        });
    }

    @Override
    public void setPlayerHealth(Player player, int health) {
        if (health < 0) {
            health = 0;
        }
        SidebarManager.getInstance().getSidebarProvider().sendScore(this, player.getName(), health);
    }

    public SidebarObjective getHealthObjective() {
        return this.healthObjective;
    }

    public LinkedList<Player> getReceivers() {
        return this.receivers;
    }

    @Override
    public void hidePlayersHealth() {
        if (this.healthObjective != null) {
            this.receivers.forEach(receiver -> this.healthObjective.sendRemove((Player)receiver));
            this.healthObjective = null;
        }
    }

    @Override
    public void showPlayersHealth(SidebarLine displayName, boolean list) {
        if (this.healthObjective == null) {
            this.healthObjective = SidebarManager.getInstance().getSidebarProvider().createObjective(this, list ? "health" : "health2", true, displayName, 2);
            this.healthObjective.refreshTitle();
            this.receivers.forEach(receiver -> this.healthObjective.sendCreate((Player)receiver));
        } else {
            this.healthObjective.sendUpdate();
        }
    }

    @Override
    public PlayerTab playerTabCreate(@NotNull String identifier, @Nullable Player player, SidebarLine prefix, SidebarLine suffix, PlayerTab.PushingRule pushingRule, @Nullable Collection<PlaceholderProvider> placeholders) {
        if (identifier.length() > 16) {
            throw new RuntimeException("Char limit exceeded");
        }
        VersionedTabGroup tab = SidebarManager.getInstance().getSidebarProvider().createPlayerTab(this, identifier, prefix, suffix, pushingRule, PlayerTab.NameTagVisibility.ALWAYS, placeholders);
        tab.refreshContent();
        this.getReceivers().forEach(tab::sendCreateToPlayer);
        if (null != player) {
            tab.setSubject(player);
            tab.add(player);
            tab.sendUserCreateToReceivers(player);
        }
        this.tabView.add(tab);
        return tab;
    }

    @Override
    public void removeTab(String identifier) {
        Optional<VersionedTabGroup> playerTab = this.tabView.stream().filter(tab -> tab.getIdentifier().equals(identifier)).findFirst();
        if (playerTab.isPresent()) {
            VersionedTabGroup tab2 = playerTab.get();
            this.tabView.remove(tab2);
            tab2.sendRemoveToReceivers();
        }
    }

    @Override
    public void removeTabs() {
        this.tabView.forEach(VersionedTabGroup::sendRemoveToReceivers);
        this.tabView.clear();
    }

    @Override
    public void playerTabRefreshAnimation() {
        for (VersionedTabGroup tab : this.tabView) {
            if (!tab.refreshContent()) continue;
            tab.sendUpdateToReceivers();
        }
    }

    @Override
    public void playerHealthRefreshAnimation() {
        if (null == this.healthObjective) {
            return;
        }
        this.healthObjective.sendUpdate();
    }

    void restoreColor(String color) {
        this.availableColors.add(color);
    }

    public SidebarObjective getSidebarObjective() {
        return this.sidebarObjective;
    }
}

