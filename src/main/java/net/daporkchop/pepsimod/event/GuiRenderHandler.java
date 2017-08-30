package net.daporkchop.pepsimod.event;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.misc.TickRate;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class GuiRenderHandler {
    public static GuiRenderHandler INSTANCE;

    {
        INSTANCE = this;
    }

    public ScaledResolution scaled = new ScaledResolution(PepsiMod.INSTANCE.mc);

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }

        GuiIngame gui = PepsiMod.INSTANCE.mc.ingameGUI;

        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();

        if ( PepsiUtils.PEPSI_NAME instanceof RainbowText) {
            ((RainbowText) PepsiUtils.PEPSI_NAME).drawAtPos(gui, 2, 2, 0);
        } else {
            PepsiUtils.PEPSI_NAME.drawAtPos(gui, 2, 2);
        }

        for (int i = 0, j = 0; i < ModuleManager.ENABLED_MODULES.size(); i++) {
            Module module = ModuleManager.ENABLED_MODULES.get(i);
            if (module.hide)    {
                continue;
            }
            if (module.text instanceof RainbowText) {
                ((RainbowText) module.text).drawAtPos(gui, width - 2 - module.text.width(), 2 + j * 10, ++j * 8);
            } else {
                module.text.drawAtPos(gui, width - 2 - module.text.width(), 2 + i++ * 10);
            }
        }

        String tpsText = PepsiUtils.COLOR_ESCAPE + "7TPS: " + PepsiUtils.COLOR_ESCAPE + "r" + TickRate.TPS;
        gui.drawString(PepsiMod.INSTANCE.mc.fontRenderer, tpsText, width - (PepsiMod.INSTANCE.mc.fontRenderer.getStringWidth("TPS: " + TickRate.TPS) + 2), height - 10, Color.white.getRGB());
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event)   {
        /*GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();

        GlStateManager.glLineWidth(1.f);

        Vec3d renderPos = PepsiUtils.getInterpolatedPos(PepsiMod.INSTANCE.mc.player, event.getPartialTicks());
        GeometryTessellator.instance.setTranslation(-renderPos.x, -renderPos.y, -renderPos.z);
*/
        for (Module module : ModuleManager.ENABLED_MODULES) {
            module.onRender(event.getPartialTicks());
        }

        /*GlStateManager.glLineWidth(1.f);

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();*/
    }
}
