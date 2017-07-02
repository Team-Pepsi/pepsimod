package net.daporkchop.pepsimod.event;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiRenderHandler {
    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }

        GuiIngame gui = PepsiMod.INSTANCE.mc.ingameGUI;

        ScaledResolution scaled = new ScaledResolution(PepsiMod.INSTANCE.mc);
        int width = scaled.getScaledWidth();

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
                ((RainbowText) module.text).drawWithEndAtPos(gui, width - 2, 2 + j * 10, ++j * 4);
            } else {
                module.text.drawWithEndAtPos(gui, width - 2, 2 + i++ * 10);
            }
        }
    }
}
