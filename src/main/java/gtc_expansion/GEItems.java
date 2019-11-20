package gtc_expansion;

import gtc_expansion.item.GEItemBatteryReusable;
import gtc_expansion.item.GEItemBatterySingleUse;
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
    public static final GEItemMisc kanthalHeatingCoil = createItem(new GEItemMisc("kanthal_heating_coil",6, 0));
    public static final GEItemMisc nichromeHeatingCoil = createItem(new GEItemMisc("nichrome_heating_coil",7, 0));
    public static final GEItemMisc fireClayBall = createItem(new GEItemMisc("fire_clay_ball", 11, 0));
    public static final GEItemMisc unfiredFireBrick = createItem(new GEItemMisc("unfired_fire_brick", 12, 0));
    public static final GEItemMisc fireBrick = createItem(new GEItemMisc("fire_brick", 13, 0));
    public static final GEItemMisc unfiredBrick = createItem(new GEItemMisc("unfired_brick", 15, 0));
    public static final GEItemMisc machineParts = createItem(new GEItemMisc("machine_parts", 2, 1));
    public static final GEItemMisc advancedCircuitParts = createItem(new GEItemMisc("advanced_circuit_parts", 4, 1));
    public static final GEItemMisc basicCircuitBoard = createItem(new GEItemMisc("basic_circuit_board", 5, 1));
    public static final GEItemMisc advancedCircuitBoard = createItem(new GEItemMisc("advanced_circuit_board", 6, 1));
    public static final GEItemMisc processorCircuitBoard = createItem(new GEItemMisc("processor_circuit_board", 7, 1));
    public static final GEItemMisc oilberry = createItem(new GEItemMisc("oilberry", 8, 1));

    public static final GEItemMisc batteryHull = createItem(new GEItemMisc("battery_hull", 0, 5));
    public static final GEItemMisc largeBatteryHull = createItem(new GEItemMisc("large_battery_hull", 1, 5));
    public static final GEItemBatterySingleUse acidBattery = createItem(new GEItemBatterySingleUse("acid", 12000, 1, 82));
    public static final GEItemBatterySingleUse mercuryBattery = createItem(new GEItemBatterySingleUse("mercury", 32000, 1, 84));
    public static final GEItemBatteryReusable sodiumBattery = createItem(new GEItemBatteryReusable("sodium", 50000, 128, 1, 86));
    public static final GEItemBatterySingleUse largeAcidBattery = createItem(new GEItemBatterySingleUse("large_acid", 36000, 2, 88));
    public static final GEItemBatterySingleUse largeMercuryBattery = createItem(new GEItemBatterySingleUse("large_mercury", 96000, 2, 90));
    public static final GEItemBatteryReusable largeLithiumBattery = createItem(new GEItemBatteryReusable("large_lithium", 300000, 256, 2, 92));
    public static final GEItemBatteryReusable largeSodiumBattery = createItem(new GEItemBatteryReusable("large_sodium", 150000, 256, 2, 94));

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
