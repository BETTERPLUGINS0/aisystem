package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class WithRemainderSlotDisplay extends SlotDisplay<WithRemainderSlotDisplay> {
   private SlotDisplay<?> input;
   private SlotDisplay<?> remainder;

   public WithRemainderSlotDisplay(SlotDisplay<?> input, SlotDisplay<?> remainder) {
      super(SlotDisplayTypes.WITH_REMAINDER);
      this.input = input;
      this.remainder = remainder;
   }

   public static WithRemainderSlotDisplay read(PacketWrapper<?> wrapper) {
      SlotDisplay<?> input = SlotDisplay.read(wrapper);
      SlotDisplay<?> remainder = SlotDisplay.read(wrapper);
      return new WithRemainderSlotDisplay(input, remainder);
   }

   public static void write(PacketWrapper<?> wrapper, WithRemainderSlotDisplay display) {
      SlotDisplay.write(wrapper, display.input);
      SlotDisplay.write(wrapper, display.remainder);
   }

   public SlotDisplay<?> getInput() {
      return this.input;
   }

   public void setInput(SlotDisplay<?> input) {
      this.input = input;
   }

   public SlotDisplay<?> getRemainder() {
      return this.remainder;
   }

   public void setRemainder(SlotDisplay<?> remainder) {
      this.remainder = remainder;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof WithRemainderSlotDisplay)) {
         return false;
      } else {
         WithRemainderSlotDisplay that = (WithRemainderSlotDisplay)obj;
         return !this.input.equals(that.input) ? false : this.remainder.equals(that.remainder);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.input, this.remainder});
   }

   public String toString() {
      return "WithRemainderSlotDisplay{input=" + this.input + ", remainder=" + this.remainder + '}';
   }
}
