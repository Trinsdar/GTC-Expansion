package gtc_expansion.events;

import gtc_expansion.material.GTCXMaterialGen;
import gtc_expansion.util.GTCXIc2cECompat;
import gtclassic.api.helpers.GTUtility;
import gtclassic.api.helpers.GTValues;
import gtclassic.api.material.GTMaterial;
import gtclassic.api.material.GTMaterialGen;
import ic2.core.IC2;
import ic2.core.entity.IC2Potion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

import static ic2.core.item.armor.standart.ItemHazmatArmor.isFullHazmatSuit;

public class GTCXRadiationEvent {
    public static List<ItemStack> radiationList = new ArrayList<>();

    public static void init(){
        addRadiation(GTMaterialGen.getDust(GTMaterial.Plutonium, 1));
        addRadiation(GTMaterialGen.getDust(GTMaterial.Uranium, 1));
        addRadiation(GTCXMaterialGen.getSmallDust(GTMaterial.Plutonium, 1));
        addRadiation(GTCXMaterialGen.getSmallDust(GTMaterial.Uranium, 1));
        addRadiation(GTMaterialGen.getIngot(GTMaterial.Plutonium, 1));
        addRadiation(GTMaterialGen.getMaterialBlock(GTMaterial.Plutonium, 1));
        addRadiation(GTCXMaterialGen.getNugget(GTMaterial.Plutonium, 1));
        addRadiation(GTCXMaterialGen.getPlate(GTMaterial.Plutonium,1));
    }

    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event){
        EntityPlayer player = event.player;

        if (IC2.platform.isSimulating() && event.phase == TickEvent.Phase.END){
            if (!player.isCreative()) {
                if (!isFullHazmatSuit(player) && !GTUtility.hasFullQuantumSuit(player)) {
                    if (hasRadiationItem(player)) {
                        player.addPotionEffect(new PotionEffect(IC2Potion.radiation, 1800, 0, false, false));
                    }
                }
            }
        }
    }

    private boolean hasRadiationItem(EntityPlayer player) {
        for (ItemStack item : radiationList) {
            if (player.inventory.hasItemStack(item))
                return true;
        }
        return false;
    }

    public static void addRadiation(ItemStack stack){
        if (Loader.isModLoaded(GTValues.MOD_ID_IC2_EXTRAS)){
            GTCXIc2cECompat.addToRadiationWhitelist(stack);
        } else {
            radiationList.add(stack);
        }
    }
}
