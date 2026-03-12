package me.SuperRonanCraft.BetterRTP.references.file;

public class Files {
   private final FileLanguage langFile = new FileLanguage();
   private final FileOther basics = new FileOther();

   public FileLanguage getLang() {
      return this.langFile;
   }

   public FileOther.FILETYPE getType(FileOther.FILETYPE type) {
      return (FileOther.FILETYPE)this.basics.types.get(this.basics.types.indexOf(type));
   }

   public void loadAll() {
      this.basics.load();
      this.langFile.load();
   }
}
