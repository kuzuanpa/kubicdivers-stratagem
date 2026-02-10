package cn.kuzuanpa.kubicdivers.stratagem.client;

import cn.kuzuanpa.kubicdivers.stratagem.KubicdiversStratagemMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = KubicdiversStratagemMod.MOD_ID, value = Dist.CLIENT)
public class StratagemHUDRenderer {
    private static final StratagemHUD hud = new StratagemHUD();

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiEvent.Post event) {
        if (Minecraft.getInstance().options.hideGui ||
                Minecraft.getInstance().screen != null) {
            return;
        }

        StratagemInputHandler handler = StratagemInputHandler.getInstance();
        if (!handler.isActive()) return;

        hud.render(event.getGuiGraphics(), event.getPartialTick());
    }
}