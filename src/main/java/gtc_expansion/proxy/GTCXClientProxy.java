package gtc_expansion.proxy;

import gtc_expansion.GTCExpansion;
import gtc_expansion.GTCXIcons;
import gtc_expansion.GTCXJei;
import gtc_expansion.data.GTCXItems;
import gtc_expansion.entity.GTCXEntityElectricBoat;
import gtc_expansion.material.GTCXMaterial;
import gtc_expansion.render.GTCXGuiFirstLoad;
import gtclassic.api.material.GTMaterialItem;
import ic2.core.entity.render.RenderOldClassicBoat;
import ic2.core.platform.textures.Ic2Icons;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber({Side.CLIENT})
public class GTCXClientProxy extends GTCXCommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        GTCXItems.magicDye.initModel();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void init(FMLInitializationEvent e){
        super.init(e);
        RenderingRegistry.registerEntityRenderingHandler(GTCXEntityElectricBoat.class, new IRenderFactory() {
            public Render createRenderFor(RenderManager manager) {
                return new RenderOldClassicBoat(manager);
            }
        });
        GTCXJei.initJei();
    }

    @SubscribeEvent
    public void onIconLoad(Ic2Icons.SpriteReloadEvent event) {
        GTCXIcons.loadSprites();
    }

    @SubscribeEvent
    public static void onRegisterTexture(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(new ResourceLocation(GTCExpansion.MODID, "fluids/molten"));
        event.getMap().registerSprite(new ResourceLocation(GTCExpansion.MODID, "fluids/moltenflowing"));;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof GTMaterialItem && ((GTMaterialItem)event.getItemStack().getItem()).getFlag().equals(GTCXMaterial.brokenTurbineRotor)) {
            List<String> tooltips = new ArrayList<>(event.getToolTip());
            String name = tooltips.get(0);
            List<String> copy = tooltips.subList(1, event.getToolTip().size());
            event.getToolTip().clear();
            event.getToolTip().add(name);
            event.getToolTip().add(I18n.format("tooltip.gtc_expansion.broken_turbine_rotor"));
            event.getToolTip().addAll(copy);
        }
    }

    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event) {
        if(GTCExpansion.firstLoad && event.getGui() instanceof GuiMainMenu) {
            GTCExpansion.firstLoad = false;
            event.setGui(new GTCXGuiFirstLoad(event.getGui()));
        }
    }
}
