package gtc_expansion.util;

import net.minecraft.item.ItemStack;
import trinsdar.ic2c_extras.tileentity.TileEntityRoller;

public class GEIc2cECompat {
    public static void addRollerRecipe(String input, int count, ItemStack output){
        TileEntityRoller.addRecipe(input, count, output);
    }
}
