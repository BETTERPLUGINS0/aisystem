/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 */
package net.advancedplugins.as.impl.utils.nbt.backend;

import java.util.logging.Level;
import net.advancedplugins.as.impl.utils.FoliaScheduler;
import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.as.impl.utils.nbt.utils.PackageWrapper;
import org.bukkit.Bukkit;

public enum ClassWrapper {
    CRAFT_BlockData(PackageWrapper.CRAFTBUKKIT, "block.data.CraftBlockData", MinecraftVersion.MC1_13_R1, null),
    CRAFT_World(PackageWrapper.CRAFTBUKKIT, "CraftWorld", MinecraftVersion.MC1_8_R3, null),
    CRAFT_Entity(PackageWrapper.CRAFTBUKKIT, "entity.CraftEntity", MinecraftVersion.MC1_8_R3, null),
    CRAFT_Player(PackageWrapper.CRAFTBUKKIT, "entity.CraftPlayer", MinecraftVersion.MC1_8_R3, null),
    CRAFT_MagicNumbers(PackageWrapper.CRAFTBUKKIT, "util.CraftMagicNumbers", MinecraftVersion.MC1_8_R3, null),
    CRAFT_MetaBook(PackageWrapper.CRAFTBUKKIT, "inventory.CraftMetaBook", MinecraftVersion.MC1_8_R3, null),
    CRAFT_Enchantment(PackageWrapper.CRAFTBUKKIT, "enchantments.CraftEnchantment", MinecraftVersion.MC1_8_R3, null),
    NMS_Block(PackageWrapper.NMS, "world.level.block.Block", MinecraftVersion.MC1_8_R3, null),
    NMS_EntityPlayer(PackageWrapper.NMS, "server.level", "EntityPlayer", MinecraftVersion.MC1_8_R3),
    NMS_EntityHuman(PackageWrapper.NMS, "world.entity.player", "EntityHuman", MinecraftVersion.MC1_8_R3),
    NMS_DamageSource(PackageWrapper.NMS, "world.damagesource", "DamageSource", MinecraftVersion.MC1_8_R3),
    NMS_IChatBaseComponent(PackageWrapper.NMS, "network.chat", "IChatBaseComponent", MinecraftVersion.MC1_8_R3),
    NMS_EnumHand(PackageWrapper.NMS, "world", "EnumHand", MinecraftVersion.MC1_9_R1),
    NMS_Explosion(PackageWrapper.NMS, "world.level", "Explosion", MinecraftVersion.MC1_8_R3),
    NMS_PathEntity(PackageWrapper.NMS, "world.level.pathfinder", "PathEntity", MinecraftVersion.MC1_8_R3),
    NMS_ENTITY_INSENTIENT(PackageWrapper.NMS, "EntityInsentient", null, null, "net.minecraft.world.entity", "net.minecraft.world.entity.EntityInsentient"),
    CRAFT_FISHHOOK(PackageWrapper.CRAFTBUKKIT, "entity.CraftFishHook", MinecraftVersion.MC1_8_R3, null),
    CRAFT_ITEMSTACK(PackageWrapper.CRAFTBUKKIT, "inventory.CraftItemStack", MinecraftVersion.MC1_14_R1, null),
    CRAFT_METAITEM(PackageWrapper.CRAFTBUKKIT, "inventory.CraftMetaItem", MinecraftVersion.MC1_14_R1, null),
    CRAFT_ENTITY(PackageWrapper.CRAFTBUKKIT, "entity.CraftEntity", MinecraftVersion.MC1_14_R1, null),
    CRAFT_WORLD(PackageWrapper.CRAFTBUKKIT, "CraftWorld", MinecraftVersion.MC1_14_R1, null),
    CRAFT_SERVER(PackageWrapper.CRAFTBUKKIT, "CraftServer", MinecraftVersion.MC1_14_R1, null),
    CRAFT_PERSISTENTDATACONTAINER(PackageWrapper.CRAFTBUKKIT, "persistence.CraftPersistentDataContainer", MinecraftVersion.MC1_14_R1, null),
    NMS_NBTBASE(PackageWrapper.NMS, "NBTBase", null, null, "net.minecraft.nbt", "net.minecraft.nbt.Tag"),
    NMS_NBTTAGSTRING(PackageWrapper.NMS, "NBTTagString", null, null, "net.minecraft.nbt", "net.minecraft.nbt.StringTag"),
    NMS_NBTTAGINT(PackageWrapper.NMS, "NBTTagInt", null, null, "net.minecraft.nbt", "net.minecraft.nbt.IntTag"),
    NMS_NBTTAGINTARRAY(PackageWrapper.NMS, "NBTTagIntArray", null, null, "net.minecraft.nbt", "net.minecraft.nbt.IntArrayTag"),
    NMS_NBTTAGFLOAT(PackageWrapper.NMS, "NBTTagFloat", null, null, "net.minecraft.nbt", "net.minecraft.nbt.FloatTag"),
    NMS_NBTTAGDOUBLE(PackageWrapper.NMS, "NBTTagDouble", null, null, "net.minecraft.nbt", "net.minecraft.nbt.DoubleTag"),
    NMS_NBTTAGLONG(PackageWrapper.NMS, "NBTTagLong", null, null, "net.minecraft.nbt", "net.minecraft.nbt.LongTag"),
    NMS_ITEMSTACK(PackageWrapper.NMS, "ItemStack", null, null, "net.minecraft.world.item", "net.minecraft.world.item.ItemStack"),
    NMS_NBTTAGCOMPOUND(PackageWrapper.NMS, "NBTTagCompound", null, null, "net.minecraft.nbt", "net.minecraft.nbt.CompoundTag"),
    NMS_NBTTAGLIST(PackageWrapper.NMS, "NBTTagList", null, null, "net.minecraft.nbt", "net.minecraft.nbt.ListTag"),
    NMS_NBTCOMPRESSEDSTREAMTOOLS(PackageWrapper.NMS, "NBTCompressedStreamTools", null, null, "net.minecraft.nbt", "net.minecraft.nbt.NbtIo"),
    NMS_MOJANGSONPARSER(PackageWrapper.NMS, "MojangsonParser", null, null, "net.minecraft.nbt", "net.minecraft.nbt.TagParser"),
    NMS_TILEENTITY(PackageWrapper.NMS, "TileEntity", null, null, "net.minecraft.world.level.block.entity", "net.minecraft.world.level.block.entity.BlockEntity"),
    NMS_BLOCKPOSITION(PackageWrapper.NMS, "BlockPosition", MinecraftVersion.MC1_8_R3, null, "net.minecraft.core", "net.minecraft.core.BlockPos"),
    NMS_WORLDSERVER(PackageWrapper.NMS, "WorldServer", null, null, "net.minecraft.server.level", "net.minecraft.server.level.ServerLevel"),
    NMS_MINECRAFTSERVER(PackageWrapper.NMS, "MinecraftServer", null, null, "net.minecraft.server", "net.minecraft.server.MinecraftServer"),
    NMS_WORLD(PackageWrapper.NMS, "World", null, null, "net.minecraft.world.level", "net.minecraft.world.level.Level"),
    NMS_ENTITY(PackageWrapper.NMS, "Entity", null, null, "net.minecraft.world.entity", "net.minecraft.world.entity.Entity"),
    NMS_ENTITYTYPES(PackageWrapper.NMS, "EntityTypes", null, null, "net.minecraft.world.entity", "net.minecraft.world.entity.EntityType"),
    NMS_REGISTRYSIMPLE(PackageWrapper.NMS, "RegistrySimple", MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_12_R1),
    NMS_REGISTRYMATERIALS(PackageWrapper.NMS, "RegistryMaterials", null, null, "net.minecraft.core", "net.minecraft.core.MappedRegistry"),
    NMS_IREGISTRY(PackageWrapper.NMS, "IRegistry", null, null, "net.minecraft.core", "net.minecraft.core.Registry"),
    NMS_MINECRAFTKEY(PackageWrapper.NMS, "MinecraftKey", MinecraftVersion.MC1_8_R3, null, "net.minecraft.resources", "net.minecraft.resources.ResourceKey"),
    NMS_GAMEPROFILESERIALIZER(PackageWrapper.NMS, "GameProfileSerializer", null, null, "net.minecraft.nbt", "net.minecraft.nbt.NbtUtils"),
    NMS_IBLOCKDATA(PackageWrapper.NMS, "IBlockData", MinecraftVersion.MC1_8_R3, null, "net.minecraft.world.level.block.state", "net.minecraft.world.level.block.state.BlockState"),
    NMS_NBTACCOUNTER(PackageWrapper.NMS, "NBTReadLimiter", MinecraftVersion.MC1_20_R3, null, "net.minecraft.nbt", "net.minecraft.nbt.NbtAccounter"),
    NMS_CUSTOMDATA(PackageWrapper.NMS, "CustomData", MinecraftVersion.MC1_20_R4, null, "net.minecraft.world.item.component", "net.minecraft.world.item.component.CustomData"),
    NMS_DATACOMPONENTTYPE(PackageWrapper.NMS, "DataComponentType", MinecraftVersion.MC1_20_R4, null, "net.minecraft.core.component", "net.minecraft.core.component.DataComponentType"),
    NMS_DATACOMPONENTS(PackageWrapper.NMS, "DataComponents", MinecraftVersion.MC1_20_R4, null, "net.minecraft.core.component", "net.minecraft.core.component.DataComponents"),
    NMS_DATACOMPONENTHOLDER(PackageWrapper.NMS, "DataComponentHolder", MinecraftVersion.MC1_20_R4, null, "net.minecraft.core.component", "net.minecraft.core.component.DataComponentHolder"),
    NMS_PROVIDER(PackageWrapper.NMS, "HolderLookup$a", MinecraftVersion.MC1_20_R4, null, "net.minecraft.core", "net.minecraft.core.HolderLookup$Provider"),
    NMS_SERVER(PackageWrapper.NMS, "MinecraftServer", MinecraftVersion.MC1_20_R4, null, "net.minecraft.server", "net.minecraft.server.MinecraftServer"),
    NMS_DATAFIXERS(PackageWrapper.NMS, "DataConverterRegistry", MinecraftVersion.MC1_20_R4, null, "net.minecraft.util.datafix", "net.minecraft.util.datafix.DataFixers"),
    NMS_REFERENCES(PackageWrapper.NMS, "DataConverterTypes", MinecraftVersion.MC1_20_R4, null, "net.minecraft.util.datafix.fixes", "net.minecraft.util.datafix.fixes.References"),
    NMS_NBTOPS(PackageWrapper.NMS, "DynamicOpsNBT", MinecraftVersion.MC1_20_R4, null, "net.minecraft.nbt", "net.minecraft.nbt.NbtOps"),
    GAMEPROFILE(PackageWrapper.NONE, "com.mojang.authlib.GameProfile", MinecraftVersion.MC1_8_R3, null);

    private Class<?> clazz;
    private String mojangName = "";

    private ClassWrapper(PackageWrapper packageWrapper, String string2, MinecraftVersion minecraftVersion, MinecraftVersion minecraftVersion2) {
        this(packageWrapper, string2, minecraftVersion, minecraftVersion2, null, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private ClassWrapper(PackageWrapper packageWrapper, String string2, MinecraftVersion minecraftVersion, MinecraftVersion minecraftVersion2, String string3, String string4) {
        this.mojangName = string4;
        if (minecraftVersion != null && MinecraftVersion.getVersion().getVersionId() < minecraftVersion.getVersionId()) {
            return;
        }
        if (minecraftVersion2 != null && MinecraftVersion.getVersion().getVersionId() > minecraftVersion2.getVersionId()) {
            return;
        }
        try {
            if (packageWrapper == PackageWrapper.NONE) {
                this.clazz = Class.forName(string2);
            } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
                if (MinecraftVersion.isPaper()) {
                    this.clazz = string3 != null ? Class.forName(string3 + "." + string2) : Class.forName((packageWrapper.equals((Object)PackageWrapper.NMS) ? packageWrapper.getUri().split(".server")[0] + "." : packageWrapper.getUri() + ".") + string2);
                } else {
                    String string5 = MinecraftVersion.getVersion().getPackageName();
                    this.clazz = string3 != null ? Class.forName(string3 + "." + string2) : Class.forName((packageWrapper.equals((Object)PackageWrapper.NMS) ? packageWrapper.getUri().split(".server")[0] + "." : packageWrapper.getUri() + "." + string5 + ".") + string2);
                }
            } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_18_R1) && string4 != null) {
                try {
                    this.clazz = Class.forName(string4);
                } catch (ClassNotFoundException classNotFoundException) {
                    this.clazz = Class.forName(string3 + "." + string2);
                } finally {
                    if (this.clazz == null) {
                        Bukkit.getLogger().warning("Failed to load class " + string2);
                    }
                }
            } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_17_R1) && string3 != null) {
                this.clazz = Class.forName(string3 + "." + string2);
            } else if (packageWrapper == PackageWrapper.CRAFTBUKKIT) {
                this.clazz = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + "." + string2);
            } else {
                String string6 = MinecraftVersion.getVersion().getPackageName();
                String string7 = packageWrapper.equals((Object)PackageWrapper.NMS) ? packageWrapper.getUri().split(".server")[0] : packageWrapper.getUri() + "." + string6;
                this.clazz = Class.forName(string7 + "." + string2);
            }
            if (this.clazz == null) {
                Bukkit.getLogger().warning("Failed to load class " + string2);
            }
        } catch (Throwable throwable) {
            if (FoliaScheduler.isFolia()) {
                Bukkit.getLogger().log(Level.WARNING, "[AdvancedPlugins] Skipping class '" + string2 + "' due to Folia");
            }
            Bukkit.getLogger().log(Level.WARNING, "[AdvancedPlugins] Error while trying to resolve the class '" + string2 + "'!", throwable);
        }
    }

    private ClassWrapper(PackageWrapper packageWrapper, String string2, String string3, MinecraftVersion minecraftVersion, boolean bl) {
        this(packageWrapper, string2 + "." + string3, minecraftVersion, null, null, null);
    }

    private ClassWrapper(PackageWrapper packageWrapper, String string2, String string3, MinecraftVersion minecraftVersion) {
        this(packageWrapper, string2, string3, minecraftVersion, false);
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public String getMojangName() {
        return this.mojangName;
    }
}

