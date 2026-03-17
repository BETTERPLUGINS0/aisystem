/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DynamicOps
 *  org.bukkit.Bukkit
 *  org.bukkit.block.BlockState
 *  org.bukkit.entity.Entity
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompoundList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTContainer;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTDoubleList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTFloatList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTIntArrayList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTIntegerList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTListCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTLongList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTStringList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTType;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTUUIDList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.DataFixerUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.GsonWrapper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.ReflectionUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.CodecHelper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ObjectCreator;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NBTReflectionUtil {
    private static Field field_unhandledTags = null;
    private static Field field_handle = null;
    private static Object type_custom_data = null;
    private static Object registry_access = null;
    public static Codec<Object> itemstack_codec = null;
    public static DynamicOps<Object> nbtOps = null;
    public static DynamicOps<Object> nbtRegistryOps = null;
    public static Object problemReporter = null;
    private static final NBTContainer dummyNBT;

    private NBTReflectionUtil() {
    }

    public static Object getNMSEntity(Entity entity) {
        try {
            return ReflectionMethod.CRAFT_ENTITY_GET_HANDLE.run(ClassWrapper.CRAFT_ENTITY.getClazz().cast(entity), new Object[0]);
        } catch (Exception exception) {
            throw new NbtApiException("Exception while getting the NMS Entity from a Bukkit Entity!", exception);
        }
    }

    public static Object readNBT(InputStream inputStream) {
        try {
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R3)) {
                return ReflectionMethod.NBTFILE_READV2.run(null, inputStream, ReflectionMethod.NBTACCOUNTER_CREATE_UNLIMITED.run(null, new Object[0]));
            }
            return ReflectionMethod.NBTFILE_READ.run(null, inputStream);
        } catch (Exception exception) {
            try {
                inputStream.close();
            } catch (IOException iOException) {
                // empty catch block
            }
            throw new NbtApiException("Exception while reading a NBT File!", exception);
        }
    }

    public static Object writeNBT(Object object, OutputStream outputStream) {
        try {
            return ReflectionMethod.NBTFILE_WRITE.run(null, object, outputStream);
        } catch (Exception exception) {
            throw new NbtApiException("Exception while writing NBT!", exception);
        }
    }

    public static Object getCraftItemHandle(ItemStack itemStack) {
        try {
            return field_handle.get(itemStack);
        } catch (IllegalAccessException | IllegalArgumentException exception) {
            throw new NbtApiException("Error getting handle from " + itemStack.getClass(), exception);
        }
    }

    public static void writeApiNBT(NBTCompound nBTCompound, OutputStream outputStream) {
        try {
            Object object = nBTCompound.getResolvedObject();
            if (object == null) {
                object = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
            }
            ReflectionMethod.NBTFILE_WRITE.run(null, object, outputStream);
        } catch (Exception exception) {
            throw new NbtApiException("Exception while writing NBT!", exception);
        }
    }

    public static Object getItemRootNBTTagCompound(Object object) {
        try {
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
                Object object2 = ReflectionMethod.NMSDATACOMPONENTHOLDER_GET.run(object, type_custom_data);
                if (object2 == null) {
                    return null;
                }
                return ReflectionMethod.NMSCUSTOMDATA_GETCOPY.run(object2, new Object[0]);
            }
            Object object3 = ReflectionMethod.NMSITEM_GETTAG.run(object, new Object[0]);
            return object3;
        } catch (Exception exception) {
            throw new NbtApiException("Exception while getting an Itemstack's NBTCompound!", exception);
        }
    }

    public static void setItemStackCompound(Object object, Object object2) {
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            if (object2 == null) {
                ReflectionMethod.NMSITEM_SET.run(object, type_custom_data, null);
            } else {
                ReflectionMethod.NMSITEM_SET.run(object, type_custom_data, ObjectCreator.NMS_CUSTOMDATA.getInstance(object2));
            }
        } else {
            ReflectionMethod.ITEMSTACK_SET_TAG.run(object, object2);
        }
    }

    public static Object convertNBTCompoundtoNMSItem(NBTCompound nBTCompound) {
        Object object = null;
        try {
            object = NBTReflectionUtil.getToCompount(nBTCompound.getCompound(), nBTCompound);
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
                if (nBTCompound.hasTag("DataVersion", NBTType.NBTTagInt)) {
                    int n;
                    int n2 = nBTCompound.getInteger("DataVersion");
                    if (n2 < (n = DataFixerUtil.getCurrentVersion())) {
                        object = DataFixerUtil.fixUpRawItemData(object, n2, n);
                    }
                } else if (nBTCompound.hasTag("tag") || nBTCompound.hasTag("Count")) {
                    object = DataFixerUtil.fixUpRawItemData(object, 3700, DataFixerUtil.getCurrentVersion());
                }
                if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
                    return CodecHelper.convertNbtToItemStack(object);
                }
                if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4)) {
                    Optional optional = (Optional)ReflectionMethod.NMSITEM_LOAD_MODERN.run(null, registry_access, object);
                    return optional.orElse(null);
                }
                return ReflectionMethod.NMSITEM_LOAD.run(null, registry_access, object);
            }
            if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_11_R1.getVersionId()) {
                return ObjectCreator.NMS_COMPOUNDFROMITEM.getInstance(object);
            }
            return ReflectionMethod.NMSITEM_CREATESTACK.run(null, object);
        } catch (Exception exception) {
            throw new NbtApiException("Exception while converting NBTCompound to NMS ItemStack! " + object, exception);
        }
    }

    public static NBTContainer convertNMSItemtoNBTCompound(Object object) {
        try {
            NBTContainer nBTContainer;
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
                nBTContainer = new NBTContainer(CodecHelper.convertItemStackToNbt(object));
            } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
                nBTContainer = new NBTContainer(ReflectionMethod.NMSITEM_SAVE_MODERN.run(object, registry_access));
            } else {
                Object object2 = ReflectionMethod.NMSITEM_SAVE.run(object, ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]));
                nBTContainer = new NBTContainer(object2);
            }
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_12_R1)) {
                nBTContainer.setInteger("DataVersion", DataFixerUtil.getCurrentVersion());
            }
            return nBTContainer;
        } catch (Exception exception) {
            throw new NbtApiException("Exception while converting NMS ItemStack to NBTCompound!", exception);
        }
    }

    @Deprecated
    public static Map<String, Object> getUnhandledNBTTags(ItemMeta itemMeta) {
        try {
            return (Map)field_unhandledTags.get(itemMeta);
        } catch (Exception exception) {
            throw new NbtApiException("Exception while getting unhandled tags from ItemMeta!", exception);
        }
    }

    public static Object getEntityNBTTagCompound(Object object) {
        try {
            Object object2;
            Object obj = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
                Object object3 = ReflectionMethod.NMS_GET_TAG_VALUE_OUTPUT.run(null, problemReporter, registry_access);
                ReflectionMethod.NMS_ENTITY_GET_NBT_1216.run(object, object3);
                object2 = ReflectionMethod.NMS_TAG_VALUE_OUTPUT_TO_TAG_COMPOUND.run(object3, new Object[0]);
            } else {
                object2 = ReflectionMethod.NMS_ENTITY_GET_NBT.run(object, obj);
            }
            if (object2 == null) {
                object2 = obj;
            }
            return object2;
        } catch (Exception exception) {
            throw new NbtApiException("Exception while getting NBTCompound from NMS Entity!", exception);
        }
    }

    public static Object setEntityNBTTag(Object object, Object object2) {
        try {
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
                Object object3 = ReflectionMethod.NMS_GET_TAG_VALUE_INPUT.run(null, problemReporter, registry_access, object);
                ReflectionMethod.NMS_ENTITY_SET_NBT_1216.run(object2, object3);
            } else {
                ReflectionMethod.NMS_ENTITY_SET_NBT.run(object2, object);
            }
            return object2;
        } catch (Exception exception) {
            throw new NbtApiException("Exception while setting the NBTCompound of an Entity", exception);
        }
    }

    public static Object getTileEntityNBTTagCompound(BlockState blockState) {
        try {
            Object object;
            Object obj = ClassWrapper.CRAFT_WORLD.getClazz().cast(blockState.getWorld());
            Object object2 = ReflectionMethod.CRAFT_WORLD_GET_HANDLE.run(obj, new Object[0]);
            Object object3 = null;
            if (MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
                object3 = ReflectionMethod.NMS_WORLD_GET_TILEENTITY_1_7_10.run(object2, blockState.getX(), blockState.getY(), blockState.getZ());
            } else {
                object = ObjectCreator.NMS_BLOCKPOSITION.getInstance(blockState.getX(), blockState.getY(), blockState.getZ());
                object3 = ReflectionMethod.NMS_WORLD_GET_TILEENTITY.run(object2, object);
            }
            if (object3 == null) {
                throw new NbtApiException("The passed BlockState(" + blockState.getType() + ") doesn't point to a BlockEntity. Only BlockEntities like Chest/Signs/Furnance/etc have NBT.");
            }
            object = null;
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
                Object object4 = ReflectionMethod.NMS_GET_TAG_VALUE_OUTPUT.run(null, problemReporter, registry_access);
                ReflectionMethod.TILEENTITY_GET_NBT_1216.run(object3, object4);
                object = ReflectionMethod.NMS_TAG_VALUE_OUTPUT_TO_TAG_COMPOUND.run(object4, new Object[0]);
            } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
                object = ReflectionMethod.TILEENTITY_GET_NBT_1205.run(object3, registry_access);
            } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_18_R1)) {
                object = ReflectionMethod.TILEENTITY_GET_NBT_1181.run(object3, new Object[0]);
            } else {
                object = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
                ReflectionMethod.TILEENTITY_GET_NBT.run(object3, object);
            }
            if (object == null) {
                throw new NbtApiException("Unable to get NBTCompound from TileEntity! " + blockState + " " + object3);
            }
            return object;
        } catch (Exception exception) {
            throw new NbtApiException("Exception while getting NBTCompound from TileEntity!", exception);
        }
    }

    public static void setTileEntityNBTTagCompound(BlockState blockState, Object object) {
        try {
            Object object2;
            Object obj = ClassWrapper.CRAFT_WORLD.getClazz().cast(blockState.getWorld());
            Object object3 = ReflectionMethod.CRAFT_WORLD_GET_HANDLE.run(obj, new Object[0]);
            Object object4 = null;
            if (MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
                object4 = ReflectionMethod.NMS_WORLD_GET_TILEENTITY_1_7_10.run(object3, blockState.getX(), blockState.getY(), blockState.getZ());
            } else {
                object2 = ObjectCreator.NMS_BLOCKPOSITION.getInstance(blockState.getX(), blockState.getY(), blockState.getZ());
                object4 = ReflectionMethod.NMS_WORLD_GET_TILEENTITY.run(object3, object2);
            }
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
                object2 = ReflectionMethod.NMS_GET_TAG_VALUE_INPUT.run(null, problemReporter, registry_access, object);
                ReflectionMethod.TILEENTITY_SET_NBT_1216.run(object4, object2);
            } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
                ReflectionMethod.TILEENTITY_SET_NBT_1205.run(object4, object, registry_access);
            } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_17_R1)) {
                ReflectionMethod.TILEENTITY_SET_NBT.run(object4, object);
            } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_16_R1)) {
                object2 = ReflectionMethod.TILEENTITY_GET_BLOCKDATA.run(object4, new Object[0]);
                ReflectionMethod.TILEENTITY_SET_NBT_LEGACY1161.run(object4, object2, object);
            } else {
                ReflectionMethod.TILEENTITY_SET_NBT_LEGACY1151.run(object4, object);
            }
        } catch (Exception exception) {
            throw new NbtApiException("Exception while setting NBTData for a TileEntity!", exception);
        }
    }

    public static Object getSubNBTTagCompound(Object object, String string) {
        try {
            if (((Boolean)ReflectionMethod.COMPOUND_HAS_KEY.run(object, string)).booleanValue()) {
                Object object2 = ReflectionMethod.COMPOUND_GET_COMPOUND.run(object, string);
                if (object2 instanceof Optional) {
                    return ((Optional)object2).orElse(null);
                }
                return object2;
            }
            throw new NbtApiException("Tried getting invalid compound '" + string + "' from '" + object + "'!");
        } catch (Exception exception) {
            throw new NbtApiException("Exception while getting NBT subcompounds!", exception);
        }
    }

    public static void addNBTTagCompound(NBTCompound nBTCompound, String string) {
        if (string == null) {
            NBTReflectionUtil.remove(nBTCompound, string);
            return;
        }
        Object object = nBTCompound.getCompound();
        if (object == null) {
            object = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
        }
        if (!NBTReflectionUtil.validCompound(nBTCompound)) {
            return;
        }
        Object object2 = NBTReflectionUtil.getToCompount(object, nBTCompound);
        try {
            ReflectionMethod.COMPOUND_SET.run(object2, string, ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance());
            nBTCompound.setCompound(object);
        } catch (Exception exception) {
            throw new NbtApiException("Exception while adding a Compound!", exception);
        }
    }

    public static boolean validCompound(NBTCompound nBTCompound) {
        Object object = nBTCompound.getCompound();
        if (object instanceof Optional) {
            object = ((Optional)object).orElse(null);
        }
        if (object == null) {
            object = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
        }
        Object object2 = NBTReflectionUtil.getToCompount(object, nBTCompound);
        nBTCompound.setResolvedObject(object2);
        return object2 != null;
    }

    public static Object getToCompount(Object object, NBTCompound nBTCompound) {
        ArrayDeque<String> arrayDeque = new ArrayDeque<String>();
        while (nBTCompound.getParent() != null) {
            arrayDeque.add(nBTCompound.getName());
            nBTCompound = nBTCompound.getParent();
        }
        if (object instanceof Optional) {
            object = ((Optional)object).orElse(null);
        }
        while (!arrayDeque.isEmpty()) {
            String string = (String)arrayDeque.pollLast();
            if ((object = NBTReflectionUtil.getSubNBTTagCompound(object, string)) instanceof Optional) {
                object = ((Optional)object).orElse(null);
            }
            if (object != null) continue;
            throw new NbtApiException("Unable to find tag '" + string + "' in " + object);
        }
        return object;
    }

    public static void mergeOtherNBTCompound(NBTCompound nBTCompound, NBTCompound nBTCompound2) {
        Object object = nBTCompound2.getResolvedObject();
        if (object == null) {
            return;
        }
        Object object2 = nBTCompound.getCompound();
        if (object2 == null) {
            object2 = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
        }
        if (!NBTReflectionUtil.validCompound(nBTCompound)) {
            throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
        }
        Object object3 = NBTReflectionUtil.getToCompount(object2, nBTCompound);
        try {
            ReflectionMethod.COMPOUND_MERGE.run(object3, object);
            nBTCompound.setCompound(object2);
        } catch (Exception exception) {
            throw new NbtApiException("Exception while merging two NBTCompounds!", exception);
        }
    }

    public static void set(NBTCompound nBTCompound, String string, Object object) {
        if (object == null) {
            NBTReflectionUtil.remove(nBTCompound, string);
            return;
        }
        Object object2 = nBTCompound.getCompound();
        if (object2 == null) {
            object2 = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
        }
        if (!NBTReflectionUtil.validCompound(nBTCompound)) {
            throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
        }
        Object object3 = NBTReflectionUtil.getToCompount(object2, nBTCompound);
        try {
            ReflectionMethod.COMPOUND_SET.run(object3, string, object);
            nBTCompound.setCompound(object2);
        } catch (Exception exception) {
            throw new NbtApiException("Exception while setting key '" + string + "' to '" + object + "'!", exception);
        }
    }

    public static <T> NBTList<T> getList(NBTCompound nBTCompound, String string, NBTType nBTType, Class<T> clazz) {
        Object object = nBTCompound.getResolvedObject();
        if (object == null) {
            object = dummyNBT.getCompound();
        }
        try {
            Object object2 = null;
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4)) {
                object2 = ReflectionMethod.COMPOUND_GET_LIST.run(object, string);
                if (object2 instanceof Optional) {
                    object2 = ((Optional)object2).orElse(null);
                }
                if (object2 == null) {
                    object2 = ClassWrapper.NMS_NBTTAGLIST.getClazz().newInstance();
                }
            } else {
                object2 = ReflectionMethod.COMPOUND_GET_LIST_LEGACY.run(object, string, nBTType.getId());
            }
            if (clazz == String.class) {
                return new NBTStringList(nBTCompound, string, nBTType, object2);
            }
            if (clazz == NBTListCompound.class) {
                return new NBTCompoundList(nBTCompound, string, nBTType, object2);
            }
            if (clazz == Integer.class) {
                return new NBTIntegerList(nBTCompound, string, nBTType, object2);
            }
            if (clazz == Float.class) {
                return new NBTFloatList(nBTCompound, string, nBTType, object2);
            }
            if (clazz == Double.class) {
                return new NBTDoubleList(nBTCompound, string, nBTType, object2);
            }
            if (clazz == Long.class) {
                return new NBTLongList(nBTCompound, string, nBTType, object2);
            }
            if (clazz == int[].class) {
                return new NBTIntArrayList(nBTCompound, string, nBTType, object2);
            }
            if (clazz == UUID.class) {
                return new NBTUUIDList(nBTCompound, string, nBTType, object2);
            }
            return null;
        } catch (Exception exception) {
            throw new NbtApiException("Exception while getting a list with the type '" + (Object)((Object)nBTType) + "'!", exception);
        }
    }

    public static NBTType getListType(NBTCompound nBTCompound, String string) {
        Object object = nBTCompound.getResolvedObject();
        if (object == null) {
            object = dummyNBT.getCompound();
        }
        try {
            Field field;
            Object object2 = ReflectionMethod.COMPOUND_GET.run(object, string);
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4)) {
                if (object2 instanceof Optional) {
                    object2 = ((Optional)object2).orElse(null);
                }
                if (object2 == null) {
                    return NBTType.NBTTagEnd;
                }
                if (new NBTStringList(nBTCompound, string, NBTType.NBTTagString, object2).isEmpty()) {
                    return NBTType.NBTTagEnd;
                }
                Object object3 = ReflectionMethod.LIST_GET.run(object2, 0);
                if (object3 instanceof Optional) {
                    object3 = ((Optional)object3).orElse(null);
                }
                if (object3 == null) {
                    return NBTType.NBTTagEnd;
                }
                return NBTType.fromName((String)ReflectionMethod.TAGTYPE_GET_NAME.run(ReflectionMethod.TAGTYPE_OWN_TYPE.run(object3, new Object[0]), new Object[0]));
            }
            String string2 = "type";
            if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_17_R1)) {
                string2 = "w";
            }
            try {
                field = object2.getClass().getDeclaredField(string2);
            } catch (NoSuchFieldException noSuchFieldException) {
                field = object2.getClass().getDeclaredField("type");
            }
            field.setAccessible(true);
            return NBTType.valueOf(field.getByte(object2));
        } catch (Exception exception) {
            throw new NbtApiException("Exception while getting the list type!", exception);
        }
    }

    public static Object getEntry(NBTCompound nBTCompound, String string) {
        Object object = nBTCompound.getResolvedObject();
        try {
            return ReflectionMethod.COMPOUND_GET.run(object, string);
        } catch (Exception exception) {
            throw new NbtApiException("Exception while getting an Entry!", exception);
        }
    }

    public static void setObject(NBTCompound nBTCompound, String string, Object object) {
        if (!MinecraftVersion.hasGsonSupport()) {
            return;
        }
        try {
            String string2 = GsonWrapper.getString(object);
            NBTReflectionUtil.setData(nBTCompound, ReflectionMethod.COMPOUND_SET_STRING, string, string2);
        } catch (Exception exception) {
            throw new NbtApiException("Exception while setting the Object '" + object + "'!", exception);
        }
    }

    public static <T> T getObject(NBTCompound nBTCompound, String string, Class<T> clazz) {
        if (!MinecraftVersion.hasGsonSupport()) {
            return null;
        }
        String string2 = (String)NBTReflectionUtil.getData(nBTCompound, ReflectionMethod.COMPOUND_GET_STRING, string);
        if (string2 == null) {
            return null;
        }
        return GsonWrapper.deserializeJson(string2, clazz);
    }

    public static void remove(NBTCompound nBTCompound, String string) {
        Object object = nBTCompound.getCompound();
        if (object == null) {
            return;
        }
        if (!NBTReflectionUtil.validCompound(nBTCompound)) {
            return;
        }
        Object object2 = NBTReflectionUtil.getToCompount(object, nBTCompound);
        ReflectionMethod.COMPOUND_REMOVE_KEY.run(object2, string);
        nBTCompound.setCompound(object);
    }

    public static Set<String> getKeys(NBTCompound nBTCompound) {
        Object object = nBTCompound.getResolvedObject();
        if (object == null) {
            return Collections.emptySet();
        }
        return (Set)ReflectionMethod.COMPOUND_GET_KEYS.run(object, new Object[0]);
    }

    public static void setData(NBTCompound nBTCompound, ReflectionMethod reflectionMethod, String string, Object object) {
        if (object == null) {
            NBTReflectionUtil.remove(nBTCompound, string);
            return;
        }
        Object object2 = nBTCompound.getCompound();
        if (object2 == null) {
            object2 = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
        }
        if (!NBTReflectionUtil.validCompound(nBTCompound)) {
            throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
        }
        Object object3 = NBTReflectionUtil.getToCompount(object2, nBTCompound);
        reflectionMethod.run(object3, string, object);
        nBTCompound.setCompound(object2);
    }

    public static Object getData(NBTCompound nBTCompound, ReflectionMethod reflectionMethod, String string) {
        Object object;
        Object object2 = nBTCompound.getResolvedObject();
        if (object2 == null) {
            object2 = dummyNBT.getCompound();
        }
        if (object2 instanceof Optional) {
            object2 = ((Optional)object2).orElseGet(() -> dummyNBT.getCompound());
        }
        if ((object = reflectionMethod.run(object2, string)) instanceof Optional) {
            return ((Optional)object).orElseGet(() -> NBTReflectionUtil.getDefaultValue(reflectionMethod));
        }
        return object;
    }

    private static Object getDefaultValue(ReflectionMethod reflectionMethod) {
        if (reflectionMethod == ReflectionMethod.COMPOUND_GET_STRING) {
            return "";
        }
        if (reflectionMethod == ReflectionMethod.COMPOUND_GET_BYTE) {
            return (byte)0;
        }
        if (reflectionMethod == ReflectionMethod.COMPOUND_GET_SHORT) {
            return (short)0;
        }
        if (reflectionMethod == ReflectionMethod.COMPOUND_GET_BOOLEAN) {
            return false;
        }
        if (reflectionMethod == ReflectionMethod.COMPOUND_GET_INT) {
            return 0;
        }
        if (reflectionMethod == ReflectionMethod.COMPOUND_GET_LONG) {
            return 0L;
        }
        if (reflectionMethod == ReflectionMethod.COMPOUND_GET_FLOAT) {
            return Float.valueOf(0.0f);
        }
        if (reflectionMethod == ReflectionMethod.COMPOUND_GET_DOUBLE) {
            return 0.0;
        }
        if (reflectionMethod == ReflectionMethod.COMPOUND_GET_BYTEARRAY) {
            return new byte[0];
        }
        if (reflectionMethod == ReflectionMethod.COMPOUND_GET_INTARRAY) {
            return new int[0];
        }
        if (reflectionMethod == ReflectionMethod.COMPOUND_GET_LONGARRAY) {
            return new long[0];
        }
        return null;
    }

    static {
        try {
            field_unhandledTags = ClassWrapper.CRAFT_METAITEM.getClazz().getDeclaredField("unhandledTags");
            field_unhandledTags.setAccessible(true);
        } catch (NoSuchFieldException noSuchFieldException) {
            // empty catch block
        }
        try {
            field_handle = ClassWrapper.CRAFT_ITEMSTACK.getClazz().getDeclaredField("handle");
            field_handle.setAccessible(true);
        } catch (NoSuchFieldException noSuchFieldException) {
            // empty catch block
        }
        if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
            Object object;
            try {
                object = ReflectionUtil.getMappedField(ClassWrapper.NMS_DATACOMPONENTS.getClazz(), "net.minecraft.core.component.DataComponents#CUSTOM_DATA");
                type_custom_data = ((Field)object).get(null);
            } catch (Exception exception) {
                MinecraftVersion.getLogger().log(Level.WARNING, "Unable to find DataComponents#CUSTOM_DATA, NBTApi will not be able to read/write custom data on 1.20+", exception);
            }
            try {
                object = ReflectionMethod.NMSSERVER_GETSERVER.run(Bukkit.getServer(), new Object[0]);
                registry_access = ReflectionMethod.NMSSERVER_GETREGISTRYACCESS.run(object, new Object[0]);
                itemstack_codec = (Codec)ReflectionUtil.getMappedField(ClassWrapper.NMS_ITEMSTACK.getClazz(), "net.minecraft.world.item.ItemStack#CODEC").get(null);
                nbtOps = (DynamicOps)ReflectionUtil.getMappedField(ClassWrapper.NMS_NBTOPS.getClazz(), "net.minecraft.nbt.NbtOps#INSTANCE").get(null);
                if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
                    nbtRegistryOps = (DynamicOps)ReflectionMethod.GET_SERIALIZATION_CONTEXT.run(registry_access, nbtOps);
                    problemReporter = ReflectionUtil.getMappedField(ClassWrapper.NMS_PROBLEM_REPORTER.getClazz(), "net.minecraft.util.ProblemReporter#DISCARDING").get(null);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        dummyNBT = new NBTContainer();
    }
}

