package ac.grim.grimac.shaded.snakeyaml.emitter;

import java.io.IOException;

interface EmitterState {
   void expect() throws IOException;
}
