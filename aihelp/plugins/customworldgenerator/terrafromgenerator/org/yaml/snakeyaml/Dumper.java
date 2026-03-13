package org.yaml.snakeyaml;

import org.yaml.snakeyaml.representer.Representer;

/** @deprecated */
public final class Dumper {
   protected final Representer representer;
   protected final DumperOptions options;

   public Dumper(Representer representer, DumperOptions options) {
      this.representer = representer;
      this.options = options;
   }

   public Dumper(DumperOptions options) {
      this(new Representer(), options);
   }

   public Dumper(Representer representer) {
      this(representer, new DumperOptions());
   }

   public Dumper() {
      this(new Representer(), new DumperOptions());
   }
}
