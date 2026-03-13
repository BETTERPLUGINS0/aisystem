package org.avarion.yaml.exceptions;

public class FinalAttribute extends YamlException {
   public FinalAttribute(String key) {
      super("'" + key + "' is final. Please adjust this!");
   }
}
