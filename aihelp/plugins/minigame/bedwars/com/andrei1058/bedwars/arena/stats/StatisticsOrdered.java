/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.andrei1058.bedwars.arena.stats;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.stats.GameStatistic;
import com.andrei1058.bedwars.api.arena.stats.GameStatisticProvider;
import com.andrei1058.bedwars.api.arena.stats.PlayerGameStats;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.language.Language;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StatisticsOrdered {
    private final List<Optional<PlayerGameStats>> ordered;
    private final IArena arena;
    private BoundsPolicy boundsPolicy = BoundsPolicy.EMPTY;
    private final String orderBy;

    public StatisticsOrdered(@NotNull IArena arena, String orderBy) {
        this.arena = arena;
        if (null == arena.getStatsHolder()) {
            throw new RuntimeException("Arena stats holder is null.");
        }
        if (!arena.getStatsHolder().hasStatistic(orderBy)) {
            throw new RuntimeException("Invalid order by. Provided: " + orderBy);
        }
        this.ordered = arena.getStatsHolder().getOrderedBy(orderBy);
        this.orderBy = orderBy;
    }

    public StringParser newParser() {
        return new StringParser();
    }

    public void setBoundsPolicy(BoundsPolicy boundsPolicy) {
        this.boundsPolicy = boundsPolicy;
    }

    public static enum BoundsPolicy {
        SKIP,
        EMPTY;

    }

    public class StringParser {
        private int index = 0;

        @Nullable
        public String parseString(String string, @Nullable Language lang, String emptyReplacement) {
            ITeam team;
            if (null == StatisticsOrdered.this.arena.getStatsHolder()) {
                return null;
            }
            if (this.index >= StatisticsOrdered.this.ordered.size()) {
                if (StatisticsOrdered.this.boundsPolicy == BoundsPolicy.SKIP) {
                    if (string.isBlank()) {
                        return string;
                    }
                    boolean hasPlaceholders = false;
                    for (String placeholder : new String[]{"{topPlayerName}", "{topPlayerDisplayName}", "{topTeamColor}", "{topTeamName}", "{topValue}"}) {
                        if (!string.contains(placeholder)) continue;
                        hasPlaceholders = true;
                        break;
                    }
                    if (!hasPlaceholders) {
                        for (String registered : StatisticsOrdered.this.arena.getStatsHolder().getRegistered()) {
                            if (!string.contains("{topValue-" + registered + "}")) continue;
                            hasPlaceholders = true;
                            break;
                        }
                    }
                    return hasPlaceholders ? null : string;
                }
                string = string.replace("{topPlayerName}", emptyReplacement).replace("{topPlayerDisplayName}", emptyReplacement).replace("{topTeamColor}", "").replace("{topTeamName}", "").replace("{topValue}", "{topValue-" + StatisticsOrdered.this.orderBy + "}");
                for (String registered : StatisticsOrdered.this.arena.getStatsHolder().getRegistered()) {
                    String displayValue = "null";
                    GameStatisticProvider<?> provider = StatisticsOrdered.this.arena.getStatsHolder().getProvider(registered);
                    if (null != provider) {
                        displayValue = provider.getVoidReplacement(lang);
                    }
                    string = string.replace("{topValue-" + registered + "}", displayValue);
                }
                return string;
            }
            Optional<PlayerGameStats> statsOptional = StatisticsOrdered.this.ordered.get(this.index);
            if (statsOptional.isEmpty()) {
                return string;
            }
            PlayerGameStats stats = statsOptional.get();
            boolean increment = string.contains("{topPlayerName}") || string.contains("{topPlayerDisplayName}");
            Player online = Bukkit.getPlayer((UUID)stats.getPlayer());
            ITeam iTeam = team = null == online ? StatisticsOrdered.this.arena.getExTeam(stats.getPlayer()) : StatisticsOrdered.this.arena.getTeam(online);
            if (null == team) {
                team = StatisticsOrdered.this.arena.getExTeam(stats.getPlayer());
            }
            string = string.replace("{topPlayerName}", stats.getUsername()).replace("{topPlayerDisplayName}", stats.getDisplayPlayer()).replace("{topTeamColor}", null == team ? "" : team.getColor().chat().toString()).replace("{topTeamName}", null == team ? "" : team.getDisplayName(lang)).replace("{topValue}", "{topValue-" + StatisticsOrdered.this.orderBy + "}");
            for (String registered : StatisticsOrdered.this.arena.getStatsHolder().getRegistered()) {
                String displayValue;
                Optional<GameStatistic<?>> statistic = stats.getStatistic(registered);
                if (!increment && string.contains("{topValue-" + registered + "}")) {
                    increment = true;
                }
                if (null == (displayValue = (String)statistic.map(gameStatistic -> gameStatistic.getDisplayValue(lang)).orElse(null))) {
                    GameStatisticProvider<?> provider = StatisticsOrdered.this.arena.getStatsHolder().getProvider(registered);
                    displayValue = null == provider ? "null" : provider.getVoidReplacement(lang);
                }
                string = string.replace("{topValue-" + registered + "}", displayValue);
            }
            if (increment) {
                ++this.index;
            }
            return string;
        }

        public void resetIndex() {
            this.index = 0;
        }
    }
}

