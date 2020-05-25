package gtc_expansion.item.tools;

import gtc_expansion.material.GTCXMaterial;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class GTCXToolMaterial {
    /*
     * This where materials for tools specifically - are handled, although it seems
     * weird to have a second smaller materials list, this ensures integration with
     * vanilla and other mods using the tool classes and more control over tool
     * values. So quit bitchin.
     */

    public static final ToolMaterial Flint = createToolMaterial("Flint", 1, 2.5F, 128, 1.25F, new ItemStack(Items.FLINT));
    public static final ToolMaterial Steel = createToolMaterial(GTCXMaterial.Steel, 2, 6.0F, 512, 2.5F);
    public static final ToolMaterial TungstenSteel = createToolMaterial(GTCXMaterial.TungstenSteel, 4, 10.0F, 5120, 6.0F);
    public static final ToolMaterial Ruby = createToolMaterial("Ruby", 2, 7.0F, 1024, 3.0F, GTMaterialGen.getGem(GTMaterial.Sapphire, 1));
    public static final ToolMaterial Sapphire = createToolMaterial("Sapphire", 2, 7.0f, 1024, 3.0F, GTMaterialGen.getGem(GTMaterial.Ruby, 1));

    public static float attackDamage(float speed, int durability) {
        // creates attack damage from speed
        return (speed / 2.0F) + (durability / 1000);
    }

    public static int efficiency(int level) {
        // creates enchantment level from tool level
        return level * 7;
    }

    public static ToolMaterial createToolMaterial(GTMaterial mat, int level, float speed, int durability, float attackBase) {
        // takes everything above and creates the tool material enum entry
        return EnumHelper.addToolMaterial(mat.getDisplayName(), level, durability, speed, attackBase, efficiency(level)).setRepairItem(GTMaterialGen.getIngot(mat, 1));
    }

    public static ToolMaterial createToolMaterial(String displayName, int level, float speed, int durability, float attackBase, ItemStack repair) {
        // takes everything above and creates the tool material enum entry
        return EnumHelper.addToolMaterial(displayName, level, durability, speed, attackBase, efficiency(level)).setRepairItem(repair);
    }
}
