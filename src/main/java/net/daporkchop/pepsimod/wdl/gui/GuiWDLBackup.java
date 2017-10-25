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

import net.daporkchop.pepsimod.wdl.WDL;
import net.daporkchop.pepsimod.wdl.WorldBackup.WorldBackupType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

/**
 * GUI allowing control over the way the world is backed up.
 */
public class GuiWDLBackup extends GuiScreen {
    private GuiScreen parent;

    private String description;

    private WorldBackupType backupType;

    public GuiWDLBackup(GuiScreen parent) {
        this.parent = parent;

        this.description = I18n.format("net.daporkchop.pepsimod.wdl.gui.backup.description1") + "\n\n"
                + I18n.format("net.daporkchop.pepsimod.wdl.gui.backup.description2") + "\n\n"
                + I18n.format("net.daporkchop.pepsimod.wdl.gui.backup.description3");
    }

    @Override
    public void initGui() {
        backupType = WorldBackupType.match(
                WDL.baseProps.getProperty("Backup", "ZIP"));

        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 32,
                getBackupButtonText()));

        this.buttonList.add(new GuiButton(100, this.width / 2 - 100,
                height - 29, I18n.format("gui.done")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (!button.enabled) {
            return;
        }

        if (button.id == 0) { //Backup mode
            switch (backupType) {
                case NONE:
                    backupType = WorldBackupType.FOLDER;
                    break;
                case FOLDER:
                    backupType = WorldBackupType.ZIP;
                    break;
                case ZIP:
                    backupType = WorldBackupType.NONE;
                    break;
            }

            button.displayString = getBackupButtonText();
        } else if (button.id == 100) { //Done
            this.mc.displayGuiScreen(this.parent);
        }
    }

    private String getBackupButtonText() {
        return I18n.format("net.daporkchop.pepsimod.wdl.gui.backup.backupMode",
                backupType.getDescription());
    }

    @Override
    public void onGuiClosed() {
        WDL.baseProps.setProperty("Backup", backupType.name());

        WDL.saveProps();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Utils.drawListBackground(23, 32, 0, 0, height, width);

        this.drawCenteredString(this.fontRenderer,
                I18n.format("net.daporkchop.pepsimod.wdl.gui.backup.title"), this.width / 2, 8,
                0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);

        Utils.drawGuiInfoBox(description, width - 50, 3 * this.height / 5, width,
                height, 48);
    }
}
