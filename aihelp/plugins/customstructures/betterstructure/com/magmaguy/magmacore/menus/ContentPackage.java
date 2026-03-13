/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package com.magmaguy.magmacore.menus;

import com.magmaguy.magmacore.menus.MenuButton;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ContentPackage
extends MenuButton {
    public ItemStack getItemstack() {
        return switch (this.getContentState().ordinal()) {
            default -> throw new IncompatibleClassChangeError();
            case 0 -> this.getInstalledItemStack();
            case 1 -> this.getPartiallyInstalledItemStack();
            case 2 -> this.getNotInstalledItemStack();
            case 3 -> this.getNotDownloadedItemStack();
            case 4 -> this.getNeedsAccessItemStack();
            case 5 -> this.getOutOfDateUpdatableItemStack();
            case 6 -> this.getOutOfDateNoAccessItemStack();
        };
    }

    protected abstract ItemStack getInstalledItemStack();

    protected abstract ItemStack getPartiallyInstalledItemStack();

    protected abstract ItemStack getNotInstalledItemStack();

    protected abstract ItemStack getNotDownloadedItemStack();

    protected abstract void doInstall(Player var1);

    protected abstract void doUninstall(Player var1);

    protected abstract void doDownload(Player var1);

    protected abstract void doShowAccessInfo(Player var1);

    protected abstract ItemStack getNeedsAccessItemStack();

    protected abstract ItemStack getOutOfDateUpdatableItemStack();

    protected abstract ItemStack getOutOfDateNoAccessItemStack();

    protected abstract ContentState getContentState();

    @Override
    public void onClick(Player player) {
        player.closeInventory();
        switch (this.getContentState().ordinal()) {
            case 0: {
                this.doUninstall(player);
                break;
            }
            case 1: {
                this.doDownload(player);
                break;
            }
            case 2: {
                this.doInstall(player);
                break;
            }
            case 3: {
                this.doDownload(player);
                break;
            }
            case 4: {
                this.doShowAccessInfo(player);
                break;
            }
            case 5: {
                this.doDownload(player);
                break;
            }
            case 6: {
                this.doShowAccessInfo(player);
            }
        }
    }

    public static enum ContentState {
        INSTALLED,
        PARTIALLY_INSTALLED,
        NOT_INSTALLED,
        NOT_DOWNLOADED,
        NEEDS_ACCESS,
        OUT_OF_DATE_UPDATABLE,
        OUT_OF_DATE_NO_ACCESS;

    }
}

