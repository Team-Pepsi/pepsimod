package net.daporkchop.pepsimod.event;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiRenderHandler {
    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }

        GuiIngame gui = PepsiMod.INSTANCE.mc.ingameGUI;

        PepsiUtils.PEPSI_NAME.drawAtPos(gui, 2, 2);

        for (int i = 0; i < ModuleManager.ENABLED_MODULES.size(); i++) {
            Module module = ModuleManager.ENABLED_MODULES.get(i);
            module.text.drawAtPos(gui, 2, 22 + i * 10);
        }
    }
}
