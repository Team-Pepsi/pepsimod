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

import net.daporkchop.pepsimod.wdl.VersionConstants;
import net.daporkchop.pepsimod.wdl.WDL;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

/**
 * Contains information about the current installation of WDL.
 */
public class GuiWDLAbout extends GuiScreen {
    private static final String FORUMS_THREAD = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2520465";
    private static final String ALL_GITHUB = "https://github.com/Pokechu22/WorldDownloader";
    /**
     * GUI to display afterwards.
     */
    private final GuiScreen parent;
    private TextList list;

    /**
     * Creates a GUI with the specified parent.
     */
    public GuiWDLAbout(GuiScreen parent) {


        this.parent = parent;
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(0, (this.width / 2) - 155, 18, 150, 20,
                I18n.format("net.daporkchop.pepsimod.wdl.gui.about.extensions")));
        buttonList.add(new GuiButton(1, (this.width / 2) + 5, 18, 150, 20,
                I18n.format("net.daporkchop.pepsimod.wdl.gui.about.debugInfo")));
        buttonList.add(new GuiButton(2, (this.width / 2) - 100,
                this.height - 29, I18n.format("gui.done")));

        String wdlVersion = VersionConstants.getModVersion();

        String mcVersion = VersionConstants.getMinecraftVersionInfo();

        list = new TextList(mc, width, height, 39, 32);
        list.addLine(I18n.format("net.daporkchop.pepsimod.wdl.gui.about.blurb"));
        list.addBlankLine();
        list.addLine(I18n.format("net.daporkchop.pepsimod.wdl.gui.about.version", wdlVersion,
                mcVersion));
        list.addBlankLine();

        String currentLanguage = WDL.minecraft.getLanguageManager()
                .getCurrentLanguage().toString();
        String translatorCredit = I18n.format("net.daporkchop.pepsimod.wdl.translatorCredit",
                currentLanguage);
        if (translatorCredit != null && !translatorCredit.isEmpty()) {
            list.addLine(translatorCredit);
            list.addBlankLine();
        }

        list.addLinkLine(I18n.format("net.daporkchop.pepsimod.wdl.gui.about.forumThread"), FORUMS_THREAD);
        list.addBlankLine();
        list.addLinkLine(I18n.format("net.daporkchop.pepsimod.wdl.gui.about.allSrc"), ALL_GITHUB);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            // Extensions
            mc.displayGuiScreen(new GuiWDLExtensions(this));
        } else if (button.id == 1) {
            // Copy debug info
            setClipboardString(WDL.getDebugInfo());
            // Change text to "copied" once clicked
            button.displayString = I18n
                    .format("net.daporkchop.pepsimod.wdl.gui.about.debugInfo.copied");
        } else if (button.id == 2) {
            // Done
            mc.displayGuiScreen(parent);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
            throws IOException {
        list.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Handles mouse input.
     */
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (list.mouseReleased(mouseX, mouseY, state)) {
            return;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (this.list == null) {
            return;
        }

        this.list.drawScreen(mouseX, mouseY, partialTicks);

        super.drawScreen(mouseX, mouseY, partialTicks);

        drawCenteredString(fontRenderer, I18n.format("net.daporkchop.pepsimod.wdl.gui.about.title"),
                width / 2, 2, 0xFFFFFF);
    }
}
