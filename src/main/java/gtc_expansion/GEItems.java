package gtc_expansion;

import gtc_expansion.item.GEItemMisc;
import gtc_expansion.item.tools.GEItemToolAxe;
import gtc_expansion.item.tools.GEItemToolFile;
import gtc_expansion.item.tools.GEItemToolHammer;
import gtc_expansion.item.tools.GEItemToolPickaxe;
import gtc_expansion.item.tools.GEItemToolSword;
import gtc_expansion.material.GEMaterial;
import gtclassic.material.GTMaterial;
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
    public static final GEItemToolHammer bronzeHammer = createItem(new GEItemToolHammer(GEMaterial.Bronze, ToolHelper.bronzeToolMaterial));
    public static final GEItemToolFile bronzeFile = createItem(new GEItemToolFile(GEMaterial.Bronze, ToolHelper.bronzeToolMaterial));

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
