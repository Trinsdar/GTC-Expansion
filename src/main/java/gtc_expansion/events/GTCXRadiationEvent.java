package gtc_expansion.events;

import gtclassic.api.helpers.GTHelperPlayer;
import ic2.core.IC2;
import ic2.core.entity.IC2Potion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

import static ic2.core.item.armor.standart.ItemHazmatArmor.isFullHazmatSuit;

public class GTCXRadiationEvent {
    public static List<ItemStack> radiationList = new ArrayList<>();

    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event){
        EntityPlayer player = event.player;

        if (IC2.platform.isSimulating() && event.phase == TickEvent.Phase.END){
            if (!player.isCreative()) {
                if (!isFullHazmatSuit(player) && !GTHelperPlayer.hasFullQuantumSuit(player)) {
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
}
