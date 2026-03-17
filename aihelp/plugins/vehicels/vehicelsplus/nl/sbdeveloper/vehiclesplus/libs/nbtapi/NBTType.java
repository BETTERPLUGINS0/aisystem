/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

public enum NBTType {
    NBTTagEnd(0, ""),
    NBTTagByte(1, "BYTE"),
    NBTTagShort(2, "SHORT"),
    NBTTagInt(3, "INT"),
    NBTTagLong(4, "LONG"),
    NBTTagFloat(5, "FLOAT"),
    NBTTagDouble(6, "DOUBLE"),
    NBTTagByteArray(7, "BYTE[]"),
    NBTTagString(8, "STRING"),
    NBTTagList(9, "LIST"),
    NBTTagCompound(10, "COMPOUND"),
    NBTTagIntArray(11, "INT[]"),
    NBTTagLongArray(12, "LONG[]");

    private final int id;
    private final String name;

    private NBTType(int n2, String string2) {
        this.id = n2;
        this.name = string2;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public static NBTType valueOf(int n) {
        for (NBTType nBTType : NBTType.values()) {
            if (nBTType.getId() != n) continue;
            return nBTType;
        }
        return NBTTagEnd;
    }

    public static NBTType fromName(String string) {
        for (NBTType nBTType : NBTType.values()) {
            if (!nBTType.getName().equals(string)) continue;
            return nBTType;
        }
        return NBTTagEnd;
    }
}

