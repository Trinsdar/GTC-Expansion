package gtc_expansion.events;

import gtc_expansion.tile.pipes.GTCXTileBasePipe;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class GTCXServerTickEvent {
    public  static final List<GTCXTileBasePipe> SERVER_TICK_PRE = new ArrayList<>(), SERVER_TICK_PR2  = new ArrayList<>();

    @SubscribeEvent
    public void onServerTickEvent(TickEvent.ServerTickEvent event){
        if (event.side.isServer()){
            if (event.phase == TickEvent.Phase.START){
                for (int i = 0; i < SERVER_TICK_PRE.size(); i++) {
                    GTCXTileBasePipe tTileEntity = SERVER_TICK_PRE.get(i);
                    if (tTileEntity.isInvalid()) {
                        SERVER_TICK_PRE.remove(i--);
                    } else {
                        try {
                            tTileEntity.onTick();
                        } catch(Throwable e) {
                            SERVER_TICK_PRE.remove(i--);
                            e.printStackTrace();
                        }
                    }
                }
                for (int i = 0; i < SERVER_TICK_PR2.size(); i++) {
                    GTCXTileBasePipe tTileEntity = SERVER_TICK_PR2.get(i);
                    if (tTileEntity.isInvalid()) {
                        SERVER_TICK_PR2.remove(i--);
                    } else {
                        try {
                            tTileEntity.onTick();
                        } catch(Throwable e) {
                            SERVER_TICK_PR2.remove(i--);
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
