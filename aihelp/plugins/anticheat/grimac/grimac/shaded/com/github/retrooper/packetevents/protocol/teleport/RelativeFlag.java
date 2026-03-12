package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.teleport;

public final class RelativeFlag {
   public static final RelativeFlag NONE = new RelativeFlag(0);
   public static final RelativeFlag X = new RelativeFlag(1);
   public static final RelativeFlag Y = new RelativeFlag(2);
   public static final RelativeFlag Z = new RelativeFlag(4);
   public static final RelativeFlag YAW = new RelativeFlag(8);
   public static final RelativeFlag PITCH = new RelativeFlag(16);
   public static final RelativeFlag DELTA_X = new RelativeFlag(32);
   public static final RelativeFlag DELTA_Y = new RelativeFlag(64);
   public static final RelativeFlag DELTA_Z = new RelativeFlag(128);
   public static final RelativeFlag ROTATE_DELTA = new RelativeFlag(256);
   private final int mask;

   public RelativeFlag(int mask) {
      this.mask = mask;
   }

   public RelativeFlag and(RelativeFlag other) {
      return new RelativeFlag(this.mask & other.mask);
   }

   public RelativeFlag or(RelativeFlag other) {
      return new RelativeFlag(this.mask | other.mask);
   }

   public boolean has(RelativeFlag flag) {
      return this.has(flag.mask);
   }

   public boolean has(int flags) {
      return (flags & this.mask) != 0;
   }

   public RelativeFlag set(RelativeFlag flag, boolean relative) {
      return this.set(flag.mask, relative);
   }

   public RelativeFlag set(int flags, boolean relative) {
      int ret = relative ? this.mask | flags : this.mask & ~flags;
      return new RelativeFlag(ret);
   }

   /** @deprecated */
   @Deprecated
   public RelativeFlag combine(RelativeFlag relativeFlag) {
      return this.or(relativeFlag);
   }

   /** @deprecated */
   @Deprecated
   public boolean isSet(byte flags) {
      return this.has(flags);
   }

   /** @deprecated */
   @Deprecated
   public byte set(byte flags, boolean relative) {
      return (byte)this.set((int)flags, relative).mask;
   }

   public byte getMask() {
      return (byte)this.mask;
   }

   public int getFullMask() {
      return this.mask;
   }
}
