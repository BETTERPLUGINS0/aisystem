/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.stream.JsonReader
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.profile.PlayerProfile
 *  org.bukkit.profile.PlayerTextures
 */
package com.magmaguy.magmacore.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.magmaguy.magmacore.util.ChatColorConverter;
import com.magmaguy.magmacore.util.VersionChecker;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

public class ItemStackGenerator {
    private static final HashMap<String, PlayerProfile> cachedPlayerProfiles = new HashMap();

    private ItemStackGenerator() {
    }

    public static ItemStack generateSkullItemStack(String username, String name, List<String> lore) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        UUID playerUUID = ItemStackGenerator.getUUIDFromUsername(username);
        SkullMeta skullMeta = (SkullMeta)itemStack.getItemMeta();
        if (cachedPlayerProfiles.containsKey(username)) {
            if (cachedPlayerProfiles.get(username).getName() != null) {
                skullMeta.setOwnerProfile(cachedPlayerProfiles.get(username));
            }
        } else if (playerUUID != null) {
            String sessionServerURL = "https://sessionserver.mojang.com/session/minecraft/profile/" + String.valueOf(playerUUID);
            try {
                URL url = new URL(sessionServerURL);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");
                connection.connect();
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                Scanner scanner = new Scanner(reader);
                StringBuilder jsonResponse = new StringBuilder();
                while (scanner.hasNextLine()) {
                    jsonResponse.append(scanner.nextLine());
                }
                JsonObject jsonObject = JsonParser.parseString((String)jsonResponse.toString()).getAsJsonObject();
                JsonArray properties = jsonObject.getAsJsonArray("properties");
                for (JsonElement property : properties) {
                    JsonObject propertyObject = property.getAsJsonObject();
                    if (!propertyObject.get("name").getAsString().equals("textures")) continue;
                    String encodedValue = propertyObject.get("value").getAsString();
                    URL skinUrl = ItemStackGenerator.getUrlFromBase64(encodedValue);
                    PlayerProfile playerProfile = Bukkit.createPlayerProfile((UUID)playerUUID);
                    PlayerTextures textures = playerProfile.getTextures();
                    textures.setSkin(skinUrl);
                    if (playerProfile.getName() != null) {
                        skullMeta.setOwnerProfile(playerProfile);
                    }
                    cachedPlayerProfiles.put(username, playerProfile);
                    break;
                }
                scanner.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        skullMeta.setDisplayName(ChatColorConverter.convert(name));
        skullMeta.setLore(ChatColorConverter.convert(lore));
        itemStack.setItemMeta((ItemMeta)skullMeta);
        return itemStack;
    }

    public static URL getUrlFromBase64(String base64) throws MalformedURLException {
        String decoded = new String(Base64.getDecoder().decode(base64));
        JsonObject jsonObject = JsonParser.parseString((String)decoded).getAsJsonObject();
        JsonObject textures = jsonObject.getAsJsonObject("textures");
        JsonObject skin = textures.getAsJsonObject("SKIN");
        String urlString = skin.get("url").getAsString();
        return new URL(urlString);
    }

    private static UUID getUUIDFromUsername(String username) throws IllegalStateException {
        UUID uUID;
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setReadTimeout(5000);
        conn.setInstanceFollowRedirects(true);
        conn.addRequestProperty("User-Agent", "Mozilla/4.0");
        conn.setDoOutput(false);
        JsonReader reader = new JsonReader((Reader)new InputStreamReader(conn.getInputStream()));
        try {
            reader.setLenient(true);
            JsonObject json = (JsonObject)new Gson().fromJson(reader, JsonObject.class);
            reader.close();
            uUID = UUID.fromString(json.get("id").getAsString().replaceFirst("(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"));
        } catch (Throwable throwable) {
            try {
                try {
                    try {
                        reader.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                    throw throwable;
                } catch (IOException e) {
                    throw new IllegalStateException("ERROR_CHECKING");
                }
            } catch (Exception e) {
                return null;
            }
        }
        reader.close();
        return uUID;
    }

    public static ItemStack generateItemStack(ItemStack itemStack, String name, List<String> lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColorConverter.convert(name));
        itemMeta.setLore(ChatColorConverter.convert(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack generateItemStack(Material material, String name, List<String> lore, int customModelID) {
        ItemStack itemStack = ItemStackGenerator.generateItemStack(material, ChatColorConverter.convert(name));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(ChatColorConverter.convert(lore));
        if (customModelID > 0) {
            itemMeta.setCustomModelData(Integer.valueOf(customModelID));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack generateItemStack(Material material, String name, List<String> lore, String namespacedKey) {
        ItemStack itemStack = ItemStackGenerator.generateItemStack(material, ChatColorConverter.convert(name));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(ChatColorConverter.convert(lore));
        if (!VersionChecker.serverVersionOlderThan(21, 4) && namespacedKey != null) {
            try {
                itemMeta.setItemModel(NamespacedKey.fromString((String)namespacedKey));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Failed to set item model for " + namespacedKey);
            }
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack generateItemStack(Material material, String name, List<String> lore) {
        ItemStack itemStack = ItemStackGenerator.generateItemStack(material, ChatColorConverter.convert(name));
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(ChatColorConverter.convert(lore));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack generateItemStack(Material material, String name) {
        ItemStack itemStack = ItemStackGenerator.generateItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColorConverter.convert(name));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack generateItemStack(Material material) {
        if (material == null) {
            material = Material.AIR;
        }
        ItemStack itemStack = new ItemStack(material);
        if (material.equals((Object)Material.AIR)) {
            return itemStack;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack generateFlaglessItemStack(Material material, String name, List<String> loreList) {
        ItemStack itemStack = ItemStackGenerator.generateItemStack(material, name, loreList);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        itemMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ADDITIONAL_TOOLTIP});
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}

