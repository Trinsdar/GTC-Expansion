package gtc_expansion;

import gtc_expansion.item.GTCXItemBatteryReusable;
import gtc_expansion.item.GTCXItemBatterySingleUse;
import gtc_expansion.item.GTCXItemDamageable;
import gtc_expansion.item.GTCXItemDiamondChainsaw;
import gtc_expansion.item.GTCXItemFood;
import gtc_expansion.item.GTCXItemInsulationCutter;
import gtc_expansion.item.GTCXItemIntegratedCircuit;
import gtc_expansion.item.GTCXItemMisc;
import gtc_expansion.item.GTCXItemMiscSpriteless;
import gtc_expansion.item.GTCXItemMold;
import gtc_expansion.item.GTCXItemSteelJackHammer;
import ic2.core.IC2;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class GTCXItems {
    private GTCXItems() {
        throw new IllegalStateException("Utility class");
    }

    static List<Item> toRegister = new ArrayList<>();

    public static final GTCXItemMisc iridiumAlloyIngot = createItem(new GTCXItemMisc("iridium_alloy_ingot", 0, 0));
    public static final GTCXItemMisc computerMonitor = createItem(new GTCXItemMisc("computer_monitor", 1, 0));
    public static final GTCXItemMisc conveyorModule = createItem(new GTCXItemMisc("conveyor_module",2, 0));
    public static final GTCXItemMisc pumpModule = createItem(new GTCXItemMisc("pump_module",3, 0));
    public static final GTCXItemMisc diamondGrinder = createItem(new GTCXItemMisc("diamond_grinder", 4, 0));
    public static final GTCXItemMisc wolframiumGrinder = createItem(new GTCXItemMisc("wolframium_grinder", 5, 0));
    public static final GTCXItemMisc constantanHeatingCoil = createItem(new GTCXItemMisc("constantan_heating_coil",8, 0));
    public static final GTCXItemMisc kanthalHeatingCoil = createItem(new GTCXItemMisc("kanthal_heating_coil",6, 0));
    public static final GTCXItemMisc nichromeHeatingCoil = createItem(new GTCXItemMisc("nichrome_heating_coil",7, 0));
    public static final GTCXItemMisc fireClayBall = createItem(new GTCXItemMisc("fire_clay_ball", 11, 0));
    public static final GTCXItemMisc unfiredFireBrick = createItem(new GTCXItemMisc("unfired_fire_brick", 12, 0));
    public static final GTCXItemMisc fireBrick = createItem(new GTCXItemMisc("fire_brick", 13, 0));
    public static final GTCXItemMisc unfiredBrick = createItem(new GTCXItemMisc("unfired_brick", 15, 0));
    public static final GTCXItemMisc machineParts = createItem(new GTCXItemMisc("machine_parts", 2, 1));
    public static final GTCXItemMisc advancedCircuitParts = createItem(new GTCXItemMisc("advanced_circuit_parts", 4, 1));
    public static final GTCXItemMisc basicCircuitBoard = createItem(new GTCXItemMisc("basic_circuit_board", 5, 1));
    public static final GTCXItemMisc advancedCircuitBoard = createItem(new GTCXItemMisc("advanced_circuit_board", 6, 1));
    public static final GTCXItemMisc processorCircuitBoard = createItem(new GTCXItemMisc("processor_circuit_board", 7, 1));
    public static final GTCXItemFood oilberry = createItem(new GTCXItemFood("oilberry", 4, 0.25F, 8, 1));
    public static final GTCXItemMiscSpriteless magicDye = createItem(new GTCXItemMiscSpriteless("magic_dye"));
    public static final GTCXItemMisc mold = createItem(new GTCXItemMisc("mold", 0, 2));
    public static final GTCXItemMold moldPlate = createItem(new GTCXItemMold("plate", 1, 2));
    public static final GTCXItemMold moldRod = createItem(new GTCXItemMold("rod", 2, 2));
    public static final GTCXItemMold moldCell = createItem(new GTCXItemMold("cell", 3, 2));
    public static final GTCXItemMold moldIngot = createItem(new GTCXItemMold("ingot", 4, 2));
    public static final GTCXItemMold moldWire = createItem(new GTCXItemMold("wire", 5, 2));
    public static final GTCXItemMold moldCasing = createItem(new GTCXItemMold("casing", 6, 2));
    public static final GTCXItemMold moldSmallPipe = createItem(new GTCXItemMold("small_pipe", 7, 2));
    public static final GTCXItemMold moldMediumPipe = createItem(new GTCXItemMold("medium_pipe", 8, 2));
    public static final GTCXItemMold moldLargePipe = createItem(new GTCXItemMold("large_pipe", 9, 2));
    public static final GTCXItemMold moldBlock = createItem(new GTCXItemMold("block", 10, 2));
    public static final GTCXItemMold moldGear = createItem(new GTCXItemMold("gear", 11, 2));
    public static final GTCXItemMold moldNugget = createItem(new GTCXItemMold("nugget", 12, 2));

    public static final GTCXItemInsulationCutter cutter = createItem(new GTCXItemInsulationCutter());

    public static final GTCXItemMisc batteryHull = createItem(new GTCXItemMisc("battery_hull", 0, 5));
    public static final GTCXItemMisc largeBatteryHull = createItem(new GTCXItemMisc("large_battery_hull", 1, 5));
    public static final GTCXItemBatterySingleUse acidBattery = createItem(new GTCXItemBatterySingleUse("acid", 12000, 1, 82));
    public static final GTCXItemBatterySingleUse mercuryBattery = createItem(new GTCXItemBatterySingleUse("mercury", 32000, 1, 84));
    public static final GTCXItemBatteryReusable sodiumBattery = createItem(new GTCXItemBatteryReusable("sodium", 50000, 128, 1, 86));
    public static final GTCXItemBatterySingleUse largeAcidBattery = createItem(new GTCXItemBatterySingleUse("large_acid", 36000, 2, 88));
    public static final GTCXItemBatterySingleUse largeMercuryBattery = createItem(new GTCXItemBatterySingleUse("large_mercury", 96000, 2, 90));
    public static final GTCXItemBatteryReusable largeLithiumBattery = createItem(new GTCXItemBatteryReusable("large_lithium", 300000, 256, 2, 92));
    public static final GTCXItemBatteryReusable largeSodiumBattery = createItem(new GTCXItemBatteryReusable("large_sodium", 150000, 256, 2, 94));

    public static final GTCXItemSteelJackHammer steelJackhammer = createItem(new GTCXItemSteelJackHammer());
    public static final GTCXItemDiamondChainsaw diamondChainsaw = createItem(new GTCXItemDiamondChainsaw());
    public static final GTCXItemIntegratedCircuit integratedCircuit = createItem(new GTCXItemIntegratedCircuit());
    public static final GTCXItemDamageable lavaFilter = createItem(new GTCXItemDamageable("lava_filter",13, 1, 99));

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
