/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonSyntaxException
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package com.andrei1058.bedwars.lobbysocket;

import com.andrei1058.bedwars.BedWars;
import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.arena.Arena;
import com.andrei1058.bedwars.arena.Misc;
import com.andrei1058.bedwars.lobbysocket.LoadedUser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ArenaSocket {
    public static List<String> lobbies = new ArrayList<String>();
    private static final ConcurrentHashMap<String, RemoteLobby> sockets = new ConcurrentHashMap();

    public static void sendMessage(String message) {
        if (message == null) {
            return;
        }
        if (message.isEmpty()) {
            return;
        }
        for (String lobby : lobbies) {
            String[] l = lobby.split(":");
            if (l.length != 2 || !Misc.isNumber(l[1])) continue;
            if (sockets.containsKey(lobby)) {
                sockets.get(lobby).sendMessage(message);
                continue;
            }
            try {
                Socket socket = new Socket(l[0], Integer.parseInt(l[1]));
                RemoteLobby rl = new RemoteLobby(socket, lobby);
                if (rl.out == null) continue;
                sockets.put(lobby, rl);
                rl.sendMessage(message);
            } catch (IOException iOException) {}
        }
    }

    public static String formatUpdateMessage(IArena a) {
        if (a == null) {
            return "";
        }
        if (a.getWorldName() == null) {
            return "";
        }
        JsonObject js = new JsonObject();
        js.addProperty("type", "UPDATE");
        js.addProperty("server_name", BedWars.config.getString("bungee-settings.server-id"));
        js.addProperty("arena_name", a.getArenaName());
        js.addProperty("arena_identifier", a.getWorldName());
        js.addProperty("arena_status", a.getStatus().toString().toUpperCase());
        js.addProperty("arena_current_players", (Number)a.getPlayers().size());
        js.addProperty("arena_max_players", (Number)a.getMaxPlayers());
        js.addProperty("arena_max_in_team", (Number)a.getMaxInTeam());
        js.addProperty("arena_group", a.getGroup().toUpperCase());
        js.addProperty("spectate", Boolean.valueOf(a.isAllowSpectate()));
        return js.toString();
    }

    public static void disable() {
        for (RemoteLobby rl : new ArrayList<RemoteLobby>(sockets.values())) {
            rl.disable();
        }
    }

    private static class RemoteLobby {
        private Socket socket;
        private PrintWriter out;
        private Scanner in;
        private String lobby;
        private boolean compute = true;

        private RemoteLobby(Socket socket, String lobby) {
            this.socket = socket;
            this.lobby = lobby;
            try {
                this.out = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ignored) {
                this.out = null;
                return;
            }
            try {
                this.in = new Scanner(socket.getInputStream());
            } catch (IOException ignored) {
                return;
            }
            BedWars.debug("RemoteLobby created: " + lobby + " " + socket.toString());
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)BedWars.plugin, () -> {
                while (this.compute) {
                    if (this.in.hasNext()) {
                        JsonObject json;
                        String msg = this.in.next();
                        BedWars.debug(msg);
                        if (msg.isEmpty()) continue;
                        try {
                            json = new JsonParser().parse(msg).getAsJsonObject();
                        } catch (JsonSyntaxException e) {
                            BedWars.plugin.getLogger().log(Level.WARNING, "Received bad data from: " + socket.getInetAddress().toString());
                            continue;
                        }
                        if (json == null || !json.has("type")) continue;
                        switch (json.get("type").getAsString().toUpperCase()) {
                            case "PLD": {
                                new LoadedUser(json.get("uuid").getAsString(), json.get("arena_identifier").getAsString(), json.get("lang_iso").getAsString(), json.get("target").getAsString());
                                break;
                            }
                            case "Q": {
                                IArena a;
                                Player p = Bukkit.getPlayer((String)json.get("name").getAsString());
                                if (p == null || !p.isOnline() || (a = Arena.getArenaByPlayer(p)) == null) break;
                                JsonObject jo = new JsonObject();
                                jo.addProperty("type", "Q");
                                jo.addProperty("name", p.getName());
                                jo.addProperty("requester", json.get("requester").getAsString());
                                jo.addProperty("server_name", BedWars.config.getString("bungee-settings.server-id"));
                                jo.addProperty("arena_id", a.getWorldName());
                                this.out.println(jo.toString());
                            }
                        }
                        continue;
                    }
                    this.disable();
                }
            });
        }

        private boolean sendMessage(String message) {
            if (this.socket == null) {
                this.disable();
                return false;
            }
            if (!this.socket.isConnected()) {
                this.disable();
                return false;
            }
            if (this.out == null) {
                this.disable();
                return false;
            }
            if (this.in == null) {
                this.disable();
                return false;
            }
            if (this.out.checkError()) {
                this.disable();
                return false;
            }
            this.out.println(message);
            return true;
        }

        private void disable() {
            this.compute = false;
            BedWars.debug("Disabling socket: " + this.socket.toString());
            sockets.remove(this.lobby);
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.in = null;
            this.out = null;
        }
    }
}

