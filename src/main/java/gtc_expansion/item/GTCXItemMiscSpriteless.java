package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXItems;
import gtclassic.GTMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class GTCXItemMiscSpriteless extends Item {

    String name;
    public GTCXItemMiscSpriteless(String name) {
        this.name = name;
        setRegistryName(this.name.toLowerCase());
        setUnlocalizedName(GTCExpansion.MODID + "." + this.name.toLowerCase());
        setCreativeTab(GTMod.creativeTabGT);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this == GTCXItems.magicDye){
            tooltip.add(I18n.format("tooltip.gtc_expansion.magicDye"));
        }
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
