/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod.wdl.gui;

import net.daporkchop.pepsimod.wdl.EntityUtils;
import net.daporkchop.pepsimod.wdl.EntityUtils.SpigotEntityType;
import net.daporkchop.pepsimod.wdl.StandardEntityManagers;
import net.daporkchop.pepsimod.wdl.WDL;
import net.daporkchop.pepsimod.wdl.WDLPluginChannels;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;

import java.io.IOException;
import java.util.Set;

/**
 * Provides fast setting for various entity options.
 */
public class GuiWDLEntityRangePresets extends GuiScreen implements GuiYesNoCallback {
    private final GuiScreen parent;

    private GuiButton vanillaButton;
    private GuiButton spigotButton;
    private GuiButton serverButton;
    private GuiButton cancelButton;

    public GuiWDLEntityRangePresets(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        int y = this.height / 4;

        this.vanillaButton = new GuiButton(0, this.width / 2 - 100, y,
                I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.vanilla"));
        y += 22;
        this.spigotButton = new GuiButton(1, this.width / 2 - 100, y,
                I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.spigot"));
        y += 22;
        this.serverButton = new GuiButton(2, this.width / 2 - 100, y,
                I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.server"));

        serverButton.enabled = WDLPluginChannels.hasServerEntityRange();

        this.buttonList.add(vanillaButton);
        this.buttonList.add(spigotButton);
        this.buttonList.add(serverButton);

        y += 28;

        this.cancelButton = new GuiButton(100, this.width / 2 - 100,
                this.height - 29, I18n.format("gui.cancel"));
        this.buttonList.add(cancelButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (!button.enabled) {
            return;
        }

        if (button.id < 3) {
            String upper;
            String lower;

            upper = I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.upperWarning");

            if (button.id == 0) {
                lower = I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.vanilla.warning");
            } else if (button.id == 1) {
                lower = I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.spigot.warning");
            } else if (button.id == 2) {
                lower = I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.server.warning");
            } else {
                //Should not happen.
                throw new Error("Button.id should never be negative.");
            }

            mc.displayGuiScreen(new GuiYesNo(this, upper, lower, button.id));
        }

        if (button.id == 100) {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Utils.drawListBackground(23, 32, 0, 0, height, width);

        this.drawCenteredString(this.fontRenderer,
                I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.title"), this.width / 2, 8,
                0xFFFFFF);

        String infoText = null;

        if (vanillaButton.isMouseOver()) {
            infoText = I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.vanilla.description");
        } else if (spigotButton.isMouseOver()) {
            infoText = I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.spigot.description");
        } else if (serverButton.isMouseOver()) {
            infoText = I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.server.description") + "\n\n";

            if (serverButton.enabled) {
                infoText += I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.server.installed");
            } else {
                infoText += I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.server.notInstalled");
            }
        } else if (cancelButton.isMouseOver()) {
            infoText = I18n.format("net.daporkchop.pepsimod.wdl.gui.rangePresets.cancel.description");
        }

        if (infoText != null) {
            Utils.drawGuiInfoBox(infoText, width, height, 48);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (result) {
            Set<String> entities = EntityUtils.getEntityTypes();

            if (id == 0) {
                for (String entity : StandardEntityManagers.VANILLA.getProvidedEntities()) {
                    WDL.worldProps.setProperty("Entity." + entity
                            + ".TrackDistance", Integer.toString(
                            StandardEntityManagers.VANILLA.getTrackDistance(entity, null)));
                }
            } else if (id == 1) {
                for (String entity : StandardEntityManagers.SPIGOT.getProvidedEntities()) {
                    SpigotEntityType type = StandardEntityManagers.SPIGOT.getSpigotType(entity);
                    // XXX Allow specifying the range for each type instead of the default
                    WDL.worldProps.setProperty("Entity." + entity
                            + ".TrackDistance", Integer.toString(
                            type.getDefaultRange()));
                }
            } else if (id == 2) {
                for (String entity : entities) {
                    WDL.worldProps.setProperty("Entity." + entity
                            + ".TrackDistance", Integer.toString(
                            WDLPluginChannels.getEntityRange(entity)));
                }
            }
        }

        mc.displayGuiScreen(parent);
    }

    @Override
    public void onGuiClosed() {
        WDL.saveProps();
    }
}
