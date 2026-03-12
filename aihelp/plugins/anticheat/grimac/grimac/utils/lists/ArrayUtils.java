package ac.grim.grimac.utils.lists;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import java.util.Collection;
import java.util.Iterator;

public class ArrayUtils {
   public static StateType[] combine(Collection<StateType> tagStates, StateType... manualStates) {
      StateType[] result = new StateType[tagStates.size() + manualStates.length];
      int i = 0;

      StateType state;
      for(Iterator var4 = tagStates.iterator(); var4.hasNext(); result[i++] = state) {
         state = (StateType)var4.next();
      }

      System.arraycopy(manualStates, 0, result, tagStates.size(), manualStates.length);
      return result;
   }
}
