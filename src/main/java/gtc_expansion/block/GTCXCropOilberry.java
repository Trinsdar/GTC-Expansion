package gtc_expansion.block;

import gtc_expansion.data.GTCXItems;
import gtc_expansion.GTCExpansion;
import gtclassic.api.material.GTMaterialGen;
import ic2.api.crops.CropProperties;
import ic2.api.crops.ICropTile;
import ic2.core.block.crop.crops.CropCardBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GTCXCropOilberry extends CropCardBase {
    public GTCXCropOilberry() {
        super(new CropProperties(9, 6, 1, 2, 1, 12));
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int state) {
        return this.getSprite(GTCExpansion.MODID + "_crops")[2 + state];
    }

    @Override
    public String getDiscoveredBy() {
        return "Spacetoad";
    }

    public String getId() {
        return "Oilberry";
    }

    public int getMaxSize() {
        return 4;
    }

    public ItemStack getGain(ICropTile crop) {
        return GTMaterialGen.get(GTCXItems.oilberry).copy();
    }

    @Override
    public double dropGainChance() {
        return super.dropGainChance() / 2.0D;
    }

    @Override
    public String[] getAttributes() {
        return new String[]{"Fire", "Dark", "Reed", "Rotten", "Oil"};
    }

    @Override
    public int getGrowthDuration(ICropTile cropTile) {
        return cropTile.getCurrentSize() == 3 ? 2200 : 1000;
    }

    @Override
    public int getOptimalHarvestSize(ICropTile cropTile) {
        return 4;
    }

    @Override
    public boolean canBeHarvested(ICropTile cropTile) {
        return cropTile.getCurrentSize() == 4;
    }

    @Override
    public int getSizeAfterHarvest(ICropTile cropTile) {
        return 2;
    }
}
