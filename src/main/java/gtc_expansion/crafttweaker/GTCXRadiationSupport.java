package gtc_expansion.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import gtc_expansion.events.GTCXRadiationEvent;
import gtclassic.api.crafttweaker.GTCraftTweakerActions;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Locale;

@ZenClass("mods.gtclassic.Radiation")
@ZenRegister
public class GTCXRadiationSupport {

    @ZenMethod
    public static void addEntry(IItemStack stack){
        GTCraftTweakerActions.apply(new RadiationAction(CraftTweakerMC.getItemStack(stack)));
    }

    private static final class RadiationAction implements IAction {
        final ItemStack stack;

        private RadiationAction(ItemStack stack){
            this.stack = stack;
        }

        @Override
        public void apply() {
            for (ItemStack entry : GTCXRadiationEvent.radiationList){
                if (entry.isItemEqual(stack)){
                    CraftTweakerAPI.logError(CraftTweakerAPI.getScriptFileAndLine() + " > "
                            + "Entry Already exists!");
                    return;
                }
            }
            GTCXRadiationEvent.radiationList.add(stack);
        }

        @Override
        public String describe() {
            return String.format(Locale.ENGLISH, "Add Item[%s] to %s", this.stack, GTCXRadiationEvent.radiationList);
        }
    }
}
