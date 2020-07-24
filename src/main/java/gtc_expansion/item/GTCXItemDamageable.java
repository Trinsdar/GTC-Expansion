package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXItems;
import gtclassic.GTMod;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class GTCXItemDamageable extends Item implements IStaticTexturedItem {
    protected String name;
    protected int x;
    protected int y;

    /**
     * Constructor for making a simple item with no action.
     *
     * @param name - String name for the item
     * @param x    - int column
     * @param y    - int row
     */
    public GTCXItemDamageable(String name, int x, int y, int maxDamage) {
        this.name = name;
        this.x = x;
        this.y = y;
        setRegistryName(this.name.toLowerCase());
        this.setMaxDamage(maxDamage - 1);
        setUnlocalizedName(GTCExpansion.MODID + "." + this.name.toLowerCase());
        setCreativeTab(GTMod.creativeTabGT);
        this.setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        //tooltip.add("Durability: " + ((stack.getMaxDamage() - stack.getItemDamage()) + 1) + "/" + (stack.getMaxDamage() + 1));
        if (getRotorEfficiency() != 0){
            tooltip.add("Rotor Efficiency: " + (int)(getRotorEfficiency() * 100) + "%");
        }
    }

    public float getRotorEfficiency(){
        if (this == GTCXItems.bronzeTurbineRotor){
            return 0.6F;
        }
        if (this == GTCXItems.steelTurbineRotor){
            return 0.8F;
        }
        if (this == GTCXItems.magnaliumTurbineRotor){
            return 1.0F;
        }
        if (this == GTCXItems.tungstensteelTurbineRotor){
            return 0.9F;
        }
        if (this == GTCXItems.carbonTurbineRotor){
            return 1.25F;
        }
        if (this == GTCXItems.osmiumTurbineRotor){
            return 1.75F;
        }
        if (this == GTCXItems.osmiridiumTurbineRotor){
            return 1.5F;
        }
        return 0.0F;
    }

    @Override
    public List<Integer> getValidVariants() {
        return Arrays.asList(0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTexture(int i) {
        return Ic2Icons.getTextures(GTCExpansion.MODID + "_items")[(this.y * 16) + this.x];
    }
}
