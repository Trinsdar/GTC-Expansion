package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.events.GTCXRadiationEvent;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.gtclassic.Radiation")
@ZenRegister
public class GTCXRadiationSupport {

    @ZenMethod
    public static void addEntry(IItemStack stack){
        for (ItemStack entry : GTCXRadiationEvent.radiationList){
            if (entry.isItemEqual(CraftTweakerMC.getItemStack(stack))){
                CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                        + "Entry Already exists!");
                return;
            }
        }
        GTCXRadiationEvent.radiationList.add(CraftTweakerMC.getItemStack(stack));
    }
}
