/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 */
package com.magmaguy.magmacore.instance;

import lombok.Generated;
import org.bukkit.GameMode;
import org.bukkit.Location;

public class MatchInstanceConfiguration {
    private Location lobbyLocation;
    private Location startLocation;
    private Location exitLocation;
    private Location fallbackLocation;
    private boolean worldBased;
    private boolean spectatable;
    private boolean respawnable;
    private boolean scaleEliteMobsHealthWithPlayerCount;
    private int minPlayers;
    private int maxPlayers;
    private int lives;
    private String dungeonPermission;
    private String failedToJoinOngoingMatchAsPlayerMessage;
    private String failedToJoinOngoingMatchAsPlayerInstanceIsFull;
    private String failedToJoinOngoingMatchAsPlayerNoPermission;
    private String failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage;
    private String failedToJoinMatchAsSpectatorNoPermission;
    private String matchJoinAsPlayerMessage;
    private String matchJoinAsPlayerTitle;
    private String matchJoinAsPlayerSubtitle;
    private String matchJoinAsSpectatorTitle;
    private String matchJoinAsSpectatorSubtitle;
    private String matchJoinAsSpectatorMessage;
    private String matchLeaveAsPlayerMessage;
    private String matchLeaveAsSpectatorMessage;
    private String matchFailedToStartNotEnoughPlayersMessage;
    private String matchWaitingMessageMessage;
    private String matchStartingMessage;
    private String matchStartingTitle;
    private String matchStartingSubtitle;
    private String preventTeleportInMessage;
    private String preventTeleportOutMessage;
    private GameMode matchLeaveDefaultGamemode;
    private GameMode matchGamemode;
    private boolean isProtected;
    private boolean isPvpPrevented;
    private boolean isRedstonePrevented;

    @Generated
    private static Location $default$lobbyLocation() {
        return null;
    }

    @Generated
    private static Location $default$startLocation() {
        return null;
    }

    @Generated
    private static Location $default$exitLocation() {
        return null;
    }

    @Generated
    private static Location $default$fallbackLocation() {
        return null;
    }

    @Generated
    private static boolean $default$worldBased() {
        return false;
    }

    @Generated
    private static boolean $default$spectatable() {
        return false;
    }

    @Generated
    private static boolean $default$respawnable() {
        return false;
    }

    @Generated
    private static boolean $default$scaleEliteMobsHealthWithPlayerCount() {
        return false;
    }

    @Generated
    private static int $default$minPlayers() {
        return 0;
    }

    @Generated
    private static int $default$maxPlayers() {
        return 1;
    }

    @Generated
    private static int $default$lives() {
        return 1;
    }

    @Generated
    private static String $default$failedToJoinOngoingMatchAsPlayerMessage() {
        return "Can't join this match - it has already started!";
    }

    @Generated
    private static String $default$failedToJoinOngoingMatchAsPlayerInstanceIsFull() {
        return "Can't join this match - the instance is already full!";
    }

    @Generated
    private static String $default$failedToJoinOngoingMatchAsPlayerNoPermission() {
        return "Can't join this match - you don't have the permission!";
    }

    @Generated
    private static String $default$failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage() {
        return "Can't join this match - spectators are not allowed!";
    }

    @Generated
    private static String $default$failedToJoinMatchAsSpectatorNoPermission() {
        return "Can't spectate this match - you don't have the permission!";
    }

    @Generated
    private static String $default$matchJoinAsPlayerMessage() {
        return "[Default message] Welcome to the match, $player!";
    }

    @Generated
    private static String $default$matchJoinAsSpectatorMessage() {
        return "[Default message] Welcome to the match, $player!";
    }

    @Generated
    private static String $default$matchLeaveAsPlayerMessage() {
        return "[Default message] You have left the match, $player!";
    }

    @Generated
    private static String $default$matchLeaveAsSpectatorMessage() {
        return "[Default message] You have left the match, spectator!";
    }

    @Generated
    private static String $default$matchFailedToStartNotEnoughPlayersMessage() {
        return "This match requires $count players before starting - can't start yet!";
    }

    @Generated
    private static String $default$matchWaitingMessageMessage() {
        return "[Default message] Run command /<insert label here> start to start the match! (You shouldn't be seeing this debug message!)";
    }

    @Generated
    private static String $default$matchStartingMessage() {
        return "Match starting!";
    }

    @Generated
    private static String $default$matchStartingTitle() {
        return "Match starting!";
    }

    @Generated
    private static String $default$matchStartingSubtitle() {
        return "in $count...";
    }

    @Generated
    private static String $default$preventTeleportInMessage() {
        return "You have attempted to teleport into an ongoing match - you can't do that!";
    }

    @Generated
    private static String $default$preventTeleportOutMessage() {
        return "You have attempted to teleport from an ongoing match - you can't do that!";
    }

    @Generated
    private static GameMode $default$matchLeaveDefaultGamemode() {
        return GameMode.SURVIVAL;
    }

    @Generated
    private static GameMode $default$matchGamemode() {
        return null;
    }

    @Generated
    private static boolean $default$isProtected() {
        return true;
    }

    @Generated
    private static boolean $default$isPvpPrevented() {
        return false;
    }

    @Generated
    private static boolean $default$isRedstonePrevented() {
        return true;
    }

    @Generated
    MatchInstanceConfiguration(Location lobbyLocation, Location startLocation, Location exitLocation, Location fallbackLocation, boolean worldBased, boolean spectatable, boolean respawnable, boolean scaleEliteMobsHealthWithPlayerCount, int minPlayers, int maxPlayers, int lives, String dungeonPermission, String failedToJoinOngoingMatchAsPlayerMessage, String failedToJoinOngoingMatchAsPlayerInstanceIsFull, String failedToJoinOngoingMatchAsPlayerNoPermission, String failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage, String failedToJoinMatchAsSpectatorNoPermission, String matchJoinAsPlayerMessage, String matchJoinAsPlayerTitle, String matchJoinAsPlayerSubtitle, String matchJoinAsSpectatorTitle, String matchJoinAsSpectatorSubtitle, String matchJoinAsSpectatorMessage, String matchLeaveAsPlayerMessage, String matchLeaveAsSpectatorMessage, String matchFailedToStartNotEnoughPlayersMessage, String matchWaitingMessageMessage, String matchStartingMessage, String matchStartingTitle, String matchStartingSubtitle, String preventTeleportInMessage, String preventTeleportOutMessage, GameMode matchLeaveDefaultGamemode, GameMode matchGamemode, boolean isProtected, boolean isPvpPrevented, boolean isRedstonePrevented) {
        this.lobbyLocation = lobbyLocation;
        this.startLocation = startLocation;
        this.exitLocation = exitLocation;
        this.fallbackLocation = fallbackLocation;
        this.worldBased = worldBased;
        this.spectatable = spectatable;
        this.respawnable = respawnable;
        this.scaleEliteMobsHealthWithPlayerCount = scaleEliteMobsHealthWithPlayerCount;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.lives = lives;
        this.dungeonPermission = dungeonPermission;
        this.failedToJoinOngoingMatchAsPlayerMessage = failedToJoinOngoingMatchAsPlayerMessage;
        this.failedToJoinOngoingMatchAsPlayerInstanceIsFull = failedToJoinOngoingMatchAsPlayerInstanceIsFull;
        this.failedToJoinOngoingMatchAsPlayerNoPermission = failedToJoinOngoingMatchAsPlayerNoPermission;
        this.failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage = failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage;
        this.failedToJoinMatchAsSpectatorNoPermission = failedToJoinMatchAsSpectatorNoPermission;
        this.matchJoinAsPlayerMessage = matchJoinAsPlayerMessage;
        this.matchJoinAsPlayerTitle = matchJoinAsPlayerTitle;
        this.matchJoinAsPlayerSubtitle = matchJoinAsPlayerSubtitle;
        this.matchJoinAsSpectatorTitle = matchJoinAsSpectatorTitle;
        this.matchJoinAsSpectatorSubtitle = matchJoinAsSpectatorSubtitle;
        this.matchJoinAsSpectatorMessage = matchJoinAsSpectatorMessage;
        this.matchLeaveAsPlayerMessage = matchLeaveAsPlayerMessage;
        this.matchLeaveAsSpectatorMessage = matchLeaveAsSpectatorMessage;
        this.matchFailedToStartNotEnoughPlayersMessage = matchFailedToStartNotEnoughPlayersMessage;
        this.matchWaitingMessageMessage = matchWaitingMessageMessage;
        this.matchStartingMessage = matchStartingMessage;
        this.matchStartingTitle = matchStartingTitle;
        this.matchStartingSubtitle = matchStartingSubtitle;
        this.preventTeleportInMessage = preventTeleportInMessage;
        this.preventTeleportOutMessage = preventTeleportOutMessage;
        this.matchLeaveDefaultGamemode = matchLeaveDefaultGamemode;
        this.matchGamemode = matchGamemode;
        this.isProtected = isProtected;
        this.isPvpPrevented = isPvpPrevented;
        this.isRedstonePrevented = isRedstonePrevented;
    }

    @Generated
    public static MatchInstanceConfigurationBuilder builder() {
        return new MatchInstanceConfigurationBuilder();
    }

    @Generated
    public Location getLobbyLocation() {
        return this.lobbyLocation;
    }

    @Generated
    public Location getStartLocation() {
        return this.startLocation;
    }

    @Generated
    public Location getExitLocation() {
        return this.exitLocation;
    }

    @Generated
    public Location getFallbackLocation() {
        return this.fallbackLocation;
    }

    @Generated
    public boolean isWorldBased() {
        return this.worldBased;
    }

    @Generated
    public boolean isSpectatable() {
        return this.spectatable;
    }

    @Generated
    public boolean isRespawnable() {
        return this.respawnable;
    }

    @Generated
    public boolean isScaleEliteMobsHealthWithPlayerCount() {
        return this.scaleEliteMobsHealthWithPlayerCount;
    }

    @Generated
    public int getMinPlayers() {
        return this.minPlayers;
    }

    @Generated
    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    @Generated
    public int getLives() {
        return this.lives;
    }

    @Generated
    public String getDungeonPermission() {
        return this.dungeonPermission;
    }

    @Generated
    public String getFailedToJoinOngoingMatchAsPlayerMessage() {
        return this.failedToJoinOngoingMatchAsPlayerMessage;
    }

    @Generated
    public String getFailedToJoinOngoingMatchAsPlayerInstanceIsFull() {
        return this.failedToJoinOngoingMatchAsPlayerInstanceIsFull;
    }

    @Generated
    public String getFailedToJoinOngoingMatchAsPlayerNoPermission() {
        return this.failedToJoinOngoingMatchAsPlayerNoPermission;
    }

    @Generated
    public String getFailedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage() {
        return this.failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage;
    }

    @Generated
    public String getFailedToJoinMatchAsSpectatorNoPermission() {
        return this.failedToJoinMatchAsSpectatorNoPermission;
    }

    @Generated
    public String getMatchJoinAsPlayerMessage() {
        return this.matchJoinAsPlayerMessage;
    }

    @Generated
    public String getMatchJoinAsPlayerTitle() {
        return this.matchJoinAsPlayerTitle;
    }

    @Generated
    public String getMatchJoinAsPlayerSubtitle() {
        return this.matchJoinAsPlayerSubtitle;
    }

    @Generated
    public String getMatchJoinAsSpectatorTitle() {
        return this.matchJoinAsSpectatorTitle;
    }

    @Generated
    public String getMatchJoinAsSpectatorSubtitle() {
        return this.matchJoinAsSpectatorSubtitle;
    }

    @Generated
    public String getMatchJoinAsSpectatorMessage() {
        return this.matchJoinAsSpectatorMessage;
    }

    @Generated
    public String getMatchLeaveAsPlayerMessage() {
        return this.matchLeaveAsPlayerMessage;
    }

    @Generated
    public String getMatchLeaveAsSpectatorMessage() {
        return this.matchLeaveAsSpectatorMessage;
    }

    @Generated
    public String getMatchFailedToStartNotEnoughPlayersMessage() {
        return this.matchFailedToStartNotEnoughPlayersMessage;
    }

    @Generated
    public String getMatchWaitingMessageMessage() {
        return this.matchWaitingMessageMessage;
    }

    @Generated
    public String getMatchStartingMessage() {
        return this.matchStartingMessage;
    }

    @Generated
    public String getMatchStartingTitle() {
        return this.matchStartingTitle;
    }

    @Generated
    public String getMatchStartingSubtitle() {
        return this.matchStartingSubtitle;
    }

    @Generated
    public String getPreventTeleportInMessage() {
        return this.preventTeleportInMessage;
    }

    @Generated
    public String getPreventTeleportOutMessage() {
        return this.preventTeleportOutMessage;
    }

    @Generated
    public GameMode getMatchLeaveDefaultGamemode() {
        return this.matchLeaveDefaultGamemode;
    }

    @Generated
    public GameMode getMatchGamemode() {
        return this.matchGamemode;
    }

    @Generated
    public boolean isProtected() {
        return this.isProtected;
    }

    @Generated
    public boolean isPvpPrevented() {
        return this.isPvpPrevented;
    }

    @Generated
    public boolean isRedstonePrevented() {
        return this.isRedstonePrevented;
    }

    @Generated
    public static class MatchInstanceConfigurationBuilder {
        @Generated
        private boolean lobbyLocation$set;
        @Generated
        private Location lobbyLocation$value;
        @Generated
        private boolean startLocation$set;
        @Generated
        private Location startLocation$value;
        @Generated
        private boolean exitLocation$set;
        @Generated
        private Location exitLocation$value;
        @Generated
        private boolean fallbackLocation$set;
        @Generated
        private Location fallbackLocation$value;
        @Generated
        private boolean worldBased$set;
        @Generated
        private boolean worldBased$value;
        @Generated
        private boolean spectatable$set;
        @Generated
        private boolean spectatable$value;
        @Generated
        private boolean respawnable$set;
        @Generated
        private boolean respawnable$value;
        @Generated
        private boolean scaleEliteMobsHealthWithPlayerCount$set;
        @Generated
        private boolean scaleEliteMobsHealthWithPlayerCount$value;
        @Generated
        private boolean minPlayers$set;
        @Generated
        private int minPlayers$value;
        @Generated
        private boolean maxPlayers$set;
        @Generated
        private int maxPlayers$value;
        @Generated
        private boolean lives$set;
        @Generated
        private int lives$value;
        @Generated
        private String dungeonPermission;
        @Generated
        private boolean failedToJoinOngoingMatchAsPlayerMessage$set;
        @Generated
        private String failedToJoinOngoingMatchAsPlayerMessage$value;
        @Generated
        private boolean failedToJoinOngoingMatchAsPlayerInstanceIsFull$set;
        @Generated
        private String failedToJoinOngoingMatchAsPlayerInstanceIsFull$value;
        @Generated
        private boolean failedToJoinOngoingMatchAsPlayerNoPermission$set;
        @Generated
        private String failedToJoinOngoingMatchAsPlayerNoPermission$value;
        @Generated
        private boolean failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage$set;
        @Generated
        private String failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage$value;
        @Generated
        private boolean failedToJoinMatchAsSpectatorNoPermission$set;
        @Generated
        private String failedToJoinMatchAsSpectatorNoPermission$value;
        @Generated
        private boolean matchJoinAsPlayerMessage$set;
        @Generated
        private String matchJoinAsPlayerMessage$value;
        @Generated
        private String matchJoinAsPlayerTitle;
        @Generated
        private String matchJoinAsPlayerSubtitle;
        @Generated
        private String matchJoinAsSpectatorTitle;
        @Generated
        private String matchJoinAsSpectatorSubtitle;
        @Generated
        private boolean matchJoinAsSpectatorMessage$set;
        @Generated
        private String matchJoinAsSpectatorMessage$value;
        @Generated
        private boolean matchLeaveAsPlayerMessage$set;
        @Generated
        private String matchLeaveAsPlayerMessage$value;
        @Generated
        private boolean matchLeaveAsSpectatorMessage$set;
        @Generated
        private String matchLeaveAsSpectatorMessage$value;
        @Generated
        private boolean matchFailedToStartNotEnoughPlayersMessage$set;
        @Generated
        private String matchFailedToStartNotEnoughPlayersMessage$value;
        @Generated
        private boolean matchWaitingMessageMessage$set;
        @Generated
        private String matchWaitingMessageMessage$value;
        @Generated
        private boolean matchStartingMessage$set;
        @Generated
        private String matchStartingMessage$value;
        @Generated
        private boolean matchStartingTitle$set;
        @Generated
        private String matchStartingTitle$value;
        @Generated
        private boolean matchStartingSubtitle$set;
        @Generated
        private String matchStartingSubtitle$value;
        @Generated
        private boolean preventTeleportInMessage$set;
        @Generated
        private String preventTeleportInMessage$value;
        @Generated
        private boolean preventTeleportOutMessage$set;
        @Generated
        private String preventTeleportOutMessage$value;
        @Generated
        private boolean matchLeaveDefaultGamemode$set;
        @Generated
        private GameMode matchLeaveDefaultGamemode$value;
        @Generated
        private boolean matchGamemode$set;
        @Generated
        private GameMode matchGamemode$value;
        @Generated
        private boolean isProtected$set;
        @Generated
        private boolean isProtected$value;
        @Generated
        private boolean isPvpPrevented$set;
        @Generated
        private boolean isPvpPrevented$value;
        @Generated
        private boolean isRedstonePrevented$set;
        @Generated
        private boolean isRedstonePrevented$value;

        @Generated
        MatchInstanceConfigurationBuilder() {
        }

        @Generated
        public MatchInstanceConfigurationBuilder lobbyLocation(Location lobbyLocation) {
            this.lobbyLocation$value = lobbyLocation;
            this.lobbyLocation$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder startLocation(Location startLocation) {
            this.startLocation$value = startLocation;
            this.startLocation$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder exitLocation(Location exitLocation) {
            this.exitLocation$value = exitLocation;
            this.exitLocation$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder fallbackLocation(Location fallbackLocation) {
            this.fallbackLocation$value = fallbackLocation;
            this.fallbackLocation$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder worldBased(boolean worldBased) {
            this.worldBased$value = worldBased;
            this.worldBased$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder spectatable(boolean spectatable) {
            this.spectatable$value = spectatable;
            this.spectatable$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder respawnable(boolean respawnable) {
            this.respawnable$value = respawnable;
            this.respawnable$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder scaleEliteMobsHealthWithPlayerCount(boolean scaleEliteMobsHealthWithPlayerCount) {
            this.scaleEliteMobsHealthWithPlayerCount$value = scaleEliteMobsHealthWithPlayerCount;
            this.scaleEliteMobsHealthWithPlayerCount$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder minPlayers(int minPlayers) {
            this.minPlayers$value = minPlayers;
            this.minPlayers$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder maxPlayers(int maxPlayers) {
            this.maxPlayers$value = maxPlayers;
            this.maxPlayers$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder lives(int lives) {
            this.lives$value = lives;
            this.lives$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder dungeonPermission(String dungeonPermission) {
            this.dungeonPermission = dungeonPermission;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder failedToJoinOngoingMatchAsPlayerMessage(String failedToJoinOngoingMatchAsPlayerMessage) {
            this.failedToJoinOngoingMatchAsPlayerMessage$value = failedToJoinOngoingMatchAsPlayerMessage;
            this.failedToJoinOngoingMatchAsPlayerMessage$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder failedToJoinOngoingMatchAsPlayerInstanceIsFull(String failedToJoinOngoingMatchAsPlayerInstanceIsFull) {
            this.failedToJoinOngoingMatchAsPlayerInstanceIsFull$value = failedToJoinOngoingMatchAsPlayerInstanceIsFull;
            this.failedToJoinOngoingMatchAsPlayerInstanceIsFull$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder failedToJoinOngoingMatchAsPlayerNoPermission(String failedToJoinOngoingMatchAsPlayerNoPermission) {
            this.failedToJoinOngoingMatchAsPlayerNoPermission$value = failedToJoinOngoingMatchAsPlayerNoPermission;
            this.failedToJoinOngoingMatchAsPlayerNoPermission$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage(String failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage) {
            this.failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage$value = failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage;
            this.failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder failedToJoinMatchAsSpectatorNoPermission(String failedToJoinMatchAsSpectatorNoPermission) {
            this.failedToJoinMatchAsSpectatorNoPermission$value = failedToJoinMatchAsSpectatorNoPermission;
            this.failedToJoinMatchAsSpectatorNoPermission$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchJoinAsPlayerMessage(String matchJoinAsPlayerMessage) {
            this.matchJoinAsPlayerMessage$value = matchJoinAsPlayerMessage;
            this.matchJoinAsPlayerMessage$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchJoinAsPlayerTitle(String matchJoinAsPlayerTitle) {
            this.matchJoinAsPlayerTitle = matchJoinAsPlayerTitle;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchJoinAsPlayerSubtitle(String matchJoinAsPlayerSubtitle) {
            this.matchJoinAsPlayerSubtitle = matchJoinAsPlayerSubtitle;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchJoinAsSpectatorTitle(String matchJoinAsSpectatorTitle) {
            this.matchJoinAsSpectatorTitle = matchJoinAsSpectatorTitle;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchJoinAsSpectatorSubtitle(String matchJoinAsSpectatorSubtitle) {
            this.matchJoinAsSpectatorSubtitle = matchJoinAsSpectatorSubtitle;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchJoinAsSpectatorMessage(String matchJoinAsSpectatorMessage) {
            this.matchJoinAsSpectatorMessage$value = matchJoinAsSpectatorMessage;
            this.matchJoinAsSpectatorMessage$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchLeaveAsPlayerMessage(String matchLeaveAsPlayerMessage) {
            this.matchLeaveAsPlayerMessage$value = matchLeaveAsPlayerMessage;
            this.matchLeaveAsPlayerMessage$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchLeaveAsSpectatorMessage(String matchLeaveAsSpectatorMessage) {
            this.matchLeaveAsSpectatorMessage$value = matchLeaveAsSpectatorMessage;
            this.matchLeaveAsSpectatorMessage$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchFailedToStartNotEnoughPlayersMessage(String matchFailedToStartNotEnoughPlayersMessage) {
            this.matchFailedToStartNotEnoughPlayersMessage$value = matchFailedToStartNotEnoughPlayersMessage;
            this.matchFailedToStartNotEnoughPlayersMessage$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchWaitingMessageMessage(String matchWaitingMessageMessage) {
            this.matchWaitingMessageMessage$value = matchWaitingMessageMessage;
            this.matchWaitingMessageMessage$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchStartingMessage(String matchStartingMessage) {
            this.matchStartingMessage$value = matchStartingMessage;
            this.matchStartingMessage$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchStartingTitle(String matchStartingTitle) {
            this.matchStartingTitle$value = matchStartingTitle;
            this.matchStartingTitle$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchStartingSubtitle(String matchStartingSubtitle) {
            this.matchStartingSubtitle$value = matchStartingSubtitle;
            this.matchStartingSubtitle$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder preventTeleportInMessage(String preventTeleportInMessage) {
            this.preventTeleportInMessage$value = preventTeleportInMessage;
            this.preventTeleportInMessage$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder preventTeleportOutMessage(String preventTeleportOutMessage) {
            this.preventTeleportOutMessage$value = preventTeleportOutMessage;
            this.preventTeleportOutMessage$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchLeaveDefaultGamemode(GameMode matchLeaveDefaultGamemode) {
            this.matchLeaveDefaultGamemode$value = matchLeaveDefaultGamemode;
            this.matchLeaveDefaultGamemode$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder matchGamemode(GameMode matchGamemode) {
            this.matchGamemode$value = matchGamemode;
            this.matchGamemode$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder isProtected(boolean isProtected) {
            this.isProtected$value = isProtected;
            this.isProtected$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder isPvpPrevented(boolean isPvpPrevented) {
            this.isPvpPrevented$value = isPvpPrevented;
            this.isPvpPrevented$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfigurationBuilder isRedstonePrevented(boolean isRedstonePrevented) {
            this.isRedstonePrevented$value = isRedstonePrevented;
            this.isRedstonePrevented$set = true;
            return this;
        }

        @Generated
        public MatchInstanceConfiguration build() {
            Location lobbyLocation$value = this.lobbyLocation$value;
            if (!this.lobbyLocation$set) {
                lobbyLocation$value = MatchInstanceConfiguration.$default$lobbyLocation();
            }
            Location startLocation$value = this.startLocation$value;
            if (!this.startLocation$set) {
                startLocation$value = MatchInstanceConfiguration.$default$startLocation();
            }
            Location exitLocation$value = this.exitLocation$value;
            if (!this.exitLocation$set) {
                exitLocation$value = MatchInstanceConfiguration.$default$exitLocation();
            }
            Location fallbackLocation$value = this.fallbackLocation$value;
            if (!this.fallbackLocation$set) {
                fallbackLocation$value = MatchInstanceConfiguration.$default$fallbackLocation();
            }
            boolean worldBased$value = this.worldBased$value;
            if (!this.worldBased$set) {
                worldBased$value = MatchInstanceConfiguration.$default$worldBased();
            }
            boolean spectatable$value = this.spectatable$value;
            if (!this.spectatable$set) {
                spectatable$value = MatchInstanceConfiguration.$default$spectatable();
            }
            boolean respawnable$value = this.respawnable$value;
            if (!this.respawnable$set) {
                respawnable$value = MatchInstanceConfiguration.$default$respawnable();
            }
            boolean scaleEliteMobsHealthWithPlayerCount$value = this.scaleEliteMobsHealthWithPlayerCount$value;
            if (!this.scaleEliteMobsHealthWithPlayerCount$set) {
                scaleEliteMobsHealthWithPlayerCount$value = MatchInstanceConfiguration.$default$scaleEliteMobsHealthWithPlayerCount();
            }
            int minPlayers$value = this.minPlayers$value;
            if (!this.minPlayers$set) {
                minPlayers$value = MatchInstanceConfiguration.$default$minPlayers();
            }
            int maxPlayers$value = this.maxPlayers$value;
            if (!this.maxPlayers$set) {
                maxPlayers$value = MatchInstanceConfiguration.$default$maxPlayers();
            }
            int lives$value = this.lives$value;
            if (!this.lives$set) {
                lives$value = MatchInstanceConfiguration.$default$lives();
            }
            String failedToJoinOngoingMatchAsPlayerMessage$value = this.failedToJoinOngoingMatchAsPlayerMessage$value;
            if (!this.failedToJoinOngoingMatchAsPlayerMessage$set) {
                failedToJoinOngoingMatchAsPlayerMessage$value = MatchInstanceConfiguration.$default$failedToJoinOngoingMatchAsPlayerMessage();
            }
            String failedToJoinOngoingMatchAsPlayerInstanceIsFull$value = this.failedToJoinOngoingMatchAsPlayerInstanceIsFull$value;
            if (!this.failedToJoinOngoingMatchAsPlayerInstanceIsFull$set) {
                failedToJoinOngoingMatchAsPlayerInstanceIsFull$value = MatchInstanceConfiguration.$default$failedToJoinOngoingMatchAsPlayerInstanceIsFull();
            }
            String failedToJoinOngoingMatchAsPlayerNoPermission$value = this.failedToJoinOngoingMatchAsPlayerNoPermission$value;
            if (!this.failedToJoinOngoingMatchAsPlayerNoPermission$set) {
                failedToJoinOngoingMatchAsPlayerNoPermission$value = MatchInstanceConfiguration.$default$failedToJoinOngoingMatchAsPlayerNoPermission();
            }
            String failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage$value = this.failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage$value;
            if (!this.failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage$set) {
                failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage$value = MatchInstanceConfiguration.$default$failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage();
            }
            String failedToJoinMatchAsSpectatorNoPermission$value = this.failedToJoinMatchAsSpectatorNoPermission$value;
            if (!this.failedToJoinMatchAsSpectatorNoPermission$set) {
                failedToJoinMatchAsSpectatorNoPermission$value = MatchInstanceConfiguration.$default$failedToJoinMatchAsSpectatorNoPermission();
            }
            String matchJoinAsPlayerMessage$value = this.matchJoinAsPlayerMessage$value;
            if (!this.matchJoinAsPlayerMessage$set) {
                matchJoinAsPlayerMessage$value = MatchInstanceConfiguration.$default$matchJoinAsPlayerMessage();
            }
            String matchJoinAsSpectatorMessage$value = this.matchJoinAsSpectatorMessage$value;
            if (!this.matchJoinAsSpectatorMessage$set) {
                matchJoinAsSpectatorMessage$value = MatchInstanceConfiguration.$default$matchJoinAsSpectatorMessage();
            }
            String matchLeaveAsPlayerMessage$value = this.matchLeaveAsPlayerMessage$value;
            if (!this.matchLeaveAsPlayerMessage$set) {
                matchLeaveAsPlayerMessage$value = MatchInstanceConfiguration.$default$matchLeaveAsPlayerMessage();
            }
            String matchLeaveAsSpectatorMessage$value = this.matchLeaveAsSpectatorMessage$value;
            if (!this.matchLeaveAsSpectatorMessage$set) {
                matchLeaveAsSpectatorMessage$value = MatchInstanceConfiguration.$default$matchLeaveAsSpectatorMessage();
            }
            String matchFailedToStartNotEnoughPlayersMessage$value = this.matchFailedToStartNotEnoughPlayersMessage$value;
            if (!this.matchFailedToStartNotEnoughPlayersMessage$set) {
                matchFailedToStartNotEnoughPlayersMessage$value = MatchInstanceConfiguration.$default$matchFailedToStartNotEnoughPlayersMessage();
            }
            String matchWaitingMessageMessage$value = this.matchWaitingMessageMessage$value;
            if (!this.matchWaitingMessageMessage$set) {
                matchWaitingMessageMessage$value = MatchInstanceConfiguration.$default$matchWaitingMessageMessage();
            }
            String matchStartingMessage$value = this.matchStartingMessage$value;
            if (!this.matchStartingMessage$set) {
                matchStartingMessage$value = MatchInstanceConfiguration.$default$matchStartingMessage();
            }
            String matchStartingTitle$value = this.matchStartingTitle$value;
            if (!this.matchStartingTitle$set) {
                matchStartingTitle$value = MatchInstanceConfiguration.$default$matchStartingTitle();
            }
            String matchStartingSubtitle$value = this.matchStartingSubtitle$value;
            if (!this.matchStartingSubtitle$set) {
                matchStartingSubtitle$value = MatchInstanceConfiguration.$default$matchStartingSubtitle();
            }
            String preventTeleportInMessage$value = this.preventTeleportInMessage$value;
            if (!this.preventTeleportInMessage$set) {
                preventTeleportInMessage$value = MatchInstanceConfiguration.$default$preventTeleportInMessage();
            }
            String preventTeleportOutMessage$value = this.preventTeleportOutMessage$value;
            if (!this.preventTeleportOutMessage$set) {
                preventTeleportOutMessage$value = MatchInstanceConfiguration.$default$preventTeleportOutMessage();
            }
            GameMode matchLeaveDefaultGamemode$value = this.matchLeaveDefaultGamemode$value;
            if (!this.matchLeaveDefaultGamemode$set) {
                matchLeaveDefaultGamemode$value = MatchInstanceConfiguration.$default$matchLeaveDefaultGamemode();
            }
            GameMode matchGamemode$value = this.matchGamemode$value;
            if (!this.matchGamemode$set) {
                matchGamemode$value = MatchInstanceConfiguration.$default$matchGamemode();
            }
            boolean isProtected$value = this.isProtected$value;
            if (!this.isProtected$set) {
                isProtected$value = MatchInstanceConfiguration.$default$isProtected();
            }
            boolean isPvpPrevented$value = this.isPvpPrevented$value;
            if (!this.isPvpPrevented$set) {
                isPvpPrevented$value = MatchInstanceConfiguration.$default$isPvpPrevented();
            }
            boolean isRedstonePrevented$value = this.isRedstonePrevented$value;
            if (!this.isRedstonePrevented$set) {
                isRedstonePrevented$value = MatchInstanceConfiguration.$default$isRedstonePrevented();
            }
            return new MatchInstanceConfiguration(lobbyLocation$value, startLocation$value, exitLocation$value, fallbackLocation$value, worldBased$value, spectatable$value, respawnable$value, scaleEliteMobsHealthWithPlayerCount$value, minPlayers$value, maxPlayers$value, lives$value, this.dungeonPermission, failedToJoinOngoingMatchAsPlayerMessage$value, failedToJoinOngoingMatchAsPlayerInstanceIsFull$value, failedToJoinOngoingMatchAsPlayerNoPermission$value, failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage$value, failedToJoinMatchAsSpectatorNoPermission$value, matchJoinAsPlayerMessage$value, this.matchJoinAsPlayerTitle, this.matchJoinAsPlayerSubtitle, this.matchJoinAsSpectatorTitle, this.matchJoinAsSpectatorSubtitle, matchJoinAsSpectatorMessage$value, matchLeaveAsPlayerMessage$value, matchLeaveAsSpectatorMessage$value, matchFailedToStartNotEnoughPlayersMessage$value, matchWaitingMessageMessage$value, matchStartingMessage$value, matchStartingTitle$value, matchStartingSubtitle$value, preventTeleportInMessage$value, preventTeleportOutMessage$value, matchLeaveDefaultGamemode$value, matchGamemode$value, isProtected$value, isPvpPrevented$value, isRedstonePrevented$value);
        }

        @Generated
        public String toString() {
            return "MatchInstanceConfiguration.MatchInstanceConfigurationBuilder(lobbyLocation$value=" + String.valueOf(this.lobbyLocation$value) + ", startLocation$value=" + String.valueOf(this.startLocation$value) + ", exitLocation$value=" + String.valueOf(this.exitLocation$value) + ", fallbackLocation$value=" + String.valueOf(this.fallbackLocation$value) + ", worldBased$value=" + this.worldBased$value + ", spectatable$value=" + this.spectatable$value + ", respawnable$value=" + this.respawnable$value + ", scaleEliteMobsHealthWithPlayerCount$value=" + this.scaleEliteMobsHealthWithPlayerCount$value + ", minPlayers$value=" + this.minPlayers$value + ", maxPlayers$value=" + this.maxPlayers$value + ", lives$value=" + this.lives$value + ", dungeonPermission=" + this.dungeonPermission + ", failedToJoinOngoingMatchAsPlayerMessage$value=" + this.failedToJoinOngoingMatchAsPlayerMessage$value + ", failedToJoinOngoingMatchAsPlayerInstanceIsFull$value=" + this.failedToJoinOngoingMatchAsPlayerInstanceIsFull$value + ", failedToJoinOngoingMatchAsPlayerNoPermission$value=" + this.failedToJoinOngoingMatchAsPlayerNoPermission$value + ", failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage$value=" + this.failedToJoinMatchAsSpectatorNoSpectatorsAllowedMessage$value + ", failedToJoinMatchAsSpectatorNoPermission$value=" + this.failedToJoinMatchAsSpectatorNoPermission$value + ", matchJoinAsPlayerMessage$value=" + this.matchJoinAsPlayerMessage$value + ", matchJoinAsPlayerTitle=" + this.matchJoinAsPlayerTitle + ", matchJoinAsPlayerSubtitle=" + this.matchJoinAsPlayerSubtitle + ", matchJoinAsSpectatorTitle=" + this.matchJoinAsSpectatorTitle + ", matchJoinAsSpectatorSubtitle=" + this.matchJoinAsSpectatorSubtitle + ", matchJoinAsSpectatorMessage$value=" + this.matchJoinAsSpectatorMessage$value + ", matchLeaveAsPlayerMessage$value=" + this.matchLeaveAsPlayerMessage$value + ", matchLeaveAsSpectatorMessage$value=" + this.matchLeaveAsSpectatorMessage$value + ", matchFailedToStartNotEnoughPlayersMessage$value=" + this.matchFailedToStartNotEnoughPlayersMessage$value + ", matchWaitingMessageMessage$value=" + this.matchWaitingMessageMessage$value + ", matchStartingMessage$value=" + this.matchStartingMessage$value + ", matchStartingTitle$value=" + this.matchStartingTitle$value + ", matchStartingSubtitle$value=" + this.matchStartingSubtitle$value + ", preventTeleportInMessage$value=" + this.preventTeleportInMessage$value + ", preventTeleportOutMessage$value=" + this.preventTeleportOutMessage$value + ", matchLeaveDefaultGamemode$value=" + String.valueOf(this.matchLeaveDefaultGamemode$value) + ", matchGamemode$value=" + String.valueOf(this.matchGamemode$value) + ", isProtected$value=" + this.isProtected$value + ", isPvpPrevented$value=" + this.isPvpPrevented$value + ", isRedstonePrevented$value=" + this.isRedstonePrevented$value + ")";
        }
    }
}

