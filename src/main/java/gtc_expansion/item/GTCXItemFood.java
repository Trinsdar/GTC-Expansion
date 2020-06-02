package gtc_expansion.item;

import gtc_expansion.GTCExpansion;
import gtc_expansion.data.GTCXItems;
import gtclassic.GTMod;
import ic2.core.platform.textures.Ic2Icons;
import ic2.core.platform.textures.obj.IStaticTexturedItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class GTCXItemFood extends ItemFood implements IStaticTexturedItem {
    int x;
    int y;

    public GTCXItemFood(String name, int amount, float saturation, int x, int y) {
        this(name, amount, saturation, false, x, y);
    }

    public GTCXItemFood(String name, int amount, float saturation, boolean isWolfFood, int x, int y) {
        super(amount, saturation, isWolfFood);
        this.x = x;
        this.y = y;
        setRegistryName(name.toLowerCase());
        setUnlocalizedName(GTCExpansion.MODID + "." + name.toLowerCase());
        setCreativeTab(GTMod.creativeTabGT);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this == GTCXItems.oilberry){
            tooltip.add(I18n.format("tooltip.gtc_expansion.oilberry"));
        }
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        super.onFoodEaten(stack, worldIn, player);
        if (this == GTCXItems.oilberry){
            player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 600, 2, false, false));
            player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 600, 2, false, false));
            player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 600, -3, false, false));
            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 600, 0, false, false));
            player.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 600, 2, false, false));
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 600, 0, false, false));
        }
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
