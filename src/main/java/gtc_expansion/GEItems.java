package gtc_expansion;

import gtc_expansion.item.GEItemContainerDamageless;
import gtc_expansion.item.GEItemMisc;
import gtc_expansion.item.tools.GEItemToolFile;
import gtc_expansion.item.tools.GEItemToolHammer;
import gtc_expansion.item.tools.GEItemToolWrench;
import gtc_expansion.material.GEMaterial;
import ic2.core.IC2;
import ic2.core.util.helpers.ToolHelper;
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
    public static final GEItemMisc fireClayBall = createItem(new GEItemMisc("fire_clay_ball", 11, 0));
    public static final GEItemMisc unfiredFireBrick = createItem(new GEItemMisc("unfired_fire_brick", 12, 0));
    public static final GEItemMisc fireBrick = createItem(new GEItemMisc("fire_brick", 13, 0));
    public static final GEItemMisc unfiredBrick = createItem(new GEItemMisc("unfired_brick", 15, 0));
    public static final GEItemMisc machineParts = createItem(new GEItemMisc("machine_parts", 2, 1));
    public static final GEItemMisc advancedCircuitParts = createItem(new GEItemMisc("advanced_circuit_parts", 4, 1));
    public static final GEItemMisc basicCircuitBoard = createItem(new GEItemMisc("basic_circuit_board", 5, 1));
    public static final GEItemMisc advancedCircuitBoard = createItem(new GEItemMisc("advanced_circuit_board", 6, 1));
    public static final GEItemMisc processorCircuitBoard = createItem(new GEItemMisc("processor_circuit_board", 7, 1));


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
