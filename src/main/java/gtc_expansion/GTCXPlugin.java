package gtc_expansion;

import gtc_expansion.item.overrides.GTCXItemDrill;
import ic2.api.classic.addon.IC2Plugin;
import ic2.api.classic.addon.PluginBase;
import ic2.api.classic.addon.misc.IOverrideObject;
import ic2.core.platform.lang.components.base.LocaleComp;
import ic2.core.platform.lang.storage.Ic2ItemLang;
import ic2.core.util.misc.ModulLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@IC2Plugin(id = "gtcx_plugin", name = "GTCX Plugin", version = GTCExpansion.MODVERSION)
public class GTCXPlugin extends PluginBase {
    @Override
    public boolean canLoad(Side side) {
        return true;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event, Map<String, IOverrideObject> map) {
        map.put(getID(Ic2ItemLang.drillItem), new ModulLoader.ItemOverride(new GTCXItemDrill()));
        //map.put(getID(Ic2ItemLang.boatItem), new ModulLoader.ItemOverride(new GTCXItemElectricBoat()));
    }

    private static String getID(LocaleComp comp) {
        return comp.getUnlocalized().replace("item.", "");
    }
}
