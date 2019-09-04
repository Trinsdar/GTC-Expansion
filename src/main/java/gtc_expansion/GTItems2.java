package gtc_expansion;

import gtc_expansion.item.GTItemComponent2;
import gtclassic.material.GTMaterialGen;
import ic2.core.IC2;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class GTItems2 {
    private GTItems2() {
        throw new IllegalStateException("Utility class");
    }

    static List<Item> toRegister = new ArrayList<>();
    public static final GTItemComponent2 iridiumAlloyIngot = createItem(new GTItemComponent2("iridium_alloy_ingot", 0, 0));
    public static final GTItemComponent2 computerMonitor = createItem(new GTItemComponent2("computer_monitor", 1, 0));
    public static final GTItemComponent2 conveyorModule = createItem(new GTItemComponent2("conveyor_module",2, 0));


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
