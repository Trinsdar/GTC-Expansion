package gtc_expansion;

import gtc_expansion.item.GEItemMisc;
import ic2.core.IC2;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class GEItems {
    private GEItems() {
        throw new IllegalStateException("Utility class");
    }

    static List<Item> toRegister = new ArrayList<>();
    public static final GEItemMisc iridiumAlloyIngot = createItem(new GEItemMisc("iridium_alloy_ingot", 0, 0));
    public static final GEItemMisc computerMonitor = createItem(new GEItemMisc("computer_monitor", 1, 0));
    public static final GEItemMisc conveyorModule = createItem(new GEItemMisc("conveyor_module",2, 0));
    public static final GEItemMisc diamondGrinder = createItem(new GEItemMisc("diamond_grinder", 4, 0));
    public static final GEItemMisc wolframiumGrinder = createItem(new GEItemMisc("wolframium_grinder", 5, 0));
    public static final GEItemMisc constantanHeatingCoil = createItem(new GEItemMisc("constantan_heating_coil",8, 0));


    public static <T extends Item> T createItem(T item) {
        toRegister.add(item);
        return item;
    }

    public static void registerItems() {
        for (Item item : toRegister) {
            IC2.getInstance().createItem(item);
        }
    }
}
