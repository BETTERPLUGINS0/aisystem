/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.inventory.content;

public class SlotPos {
    private final int row;
    private final int column;

    public SlotPos(int n, int n2) {
        this.row = n;
        this.column = n2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        SlotPos slotPos = (SlotPos)object;
        return this.row == slotPos.row && this.column == slotPos.column;
    }

    public int hashCode() {
        int n = this.row;
        n = 31 * n + this.column;
        return n;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public static SlotPos of(int n, int n2) {
        return new SlotPos(n, n2);
    }
}

