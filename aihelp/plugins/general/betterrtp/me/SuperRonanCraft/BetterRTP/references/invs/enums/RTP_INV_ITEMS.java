package me.SuperRonanCraft.BetterRTP.references.invs.enums;

public enum RTP_INV_ITEMS {
   NORMAL("paper", 1),
   BACK("book", 1, "Back", 0);

   public String item;
   public String name;
   public int amt;
   public int slot = -1;

   private RTP_INV_ITEMS(String item, int amt) {
      this.item = item;
      this.amt = amt;
   }

   private RTP_INV_ITEMS(String item, int amt, String name, int slot) {
      this.item = item;
      this.amt = amt;
      this.name = name;
      this.slot = slot;
   }

   // $FF: synthetic method
   private static RTP_INV_ITEMS[] $values() {
      return new RTP_INV_ITEMS[]{NORMAL, BACK};
   }
}
