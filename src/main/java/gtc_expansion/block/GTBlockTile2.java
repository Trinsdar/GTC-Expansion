package gtc_expansion.block;

import gtclassic.block.GTBlockTile;

public class GTBlockTile2 extends GTBlockTile {
    public GTBlockTile2(String name) {
        super(name);
        this.setRegistryName(name);
        this.setUnlocalizedName("gtc_expansion." + name);
    }

    public GTBlockTile2(String name, int additionalInfo) {
        super(name, additionalInfo);
        this.setRegistryName(name.toLowerCase());
        this.setUnlocalizedName("gtc_expansion." + name.toLowerCase());
    }
}
