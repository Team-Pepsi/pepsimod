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
import net.daporkchop.pepsimod.wdl.WDLPluginChannels;
import net.daporkchop.pepsimod.wdl.update.WDLUpdateChecker;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GuiWDL extends GuiScreen {
    /**
     * Tooltip to display on the given frame.
     */
    private String displayedTooltip = null;
    private String title = "";
    private GuiScreen parent;
    private GuiTextField worldname;
    private GuiWDLButtonList list;

    public GuiWDL(GuiScreen parent) {
        this.parent = parent;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        if (WDL.isMultiworld && WDL.worldName.isEmpty()) {
            this.mc.displayGuiScreen(new GuiWDLMultiworldSelect(I18n
                    .format("net.daporkchop.pepsimod.wdl.gui.multiworldSelect.title.changeOptions"),
                    new GuiWDLMultiworldSelect.WorldSelectionCallback() {
                        @Override
                        public void onWorldSelected(String selectedWorld) {
                            WDL.worldName = selectedWorld;
                            WDL.isMultiworld = true;
                            WDL.propsFound = true;

                            WDL.worldProps = WDL.loadWorldProps(selectedWorld);
                            mc.displayGuiScreen(GuiWDL.this);
                        }

                        @Override
                        public void onCancel() {
                            mc.displayGuiScreen(null);
                        }
                    }));
            return;
        }

        if (!WDL.propsFound) {
            mc.displayGuiScreen(new GuiWDLMultiworld(new GuiWDLMultiworld.MultiworldCallback() {
                @Override
                public void onSelect(boolean enableMutliworld) {
                    WDL.isMultiworld = enableMutliworld;

                    if (WDL.isMultiworld) {
                        // Ask the user which world is loaded
                        // TODO: Copy-pasted code from above -- suboptimal.
                        mc.displayGuiScreen(new GuiWDLMultiworldSelect(I18n
                                .format("net.daporkchop.pepsimod.wdl.gui.multiworldSelect.title.changeOptions"),
                                new GuiWDLMultiworldSelect.WorldSelectionCallback() {
                                    @Override
                                    public void onWorldSelected(String selectedWorld) {
                                        WDL.worldName = selectedWorld;
                                        WDL.isMultiworld = true;
                                        WDL.propsFound = true;

                                        WDL.worldProps = WDL.loadWorldProps(selectedWorld);
                                        mc.displayGuiScreen(GuiWDL.this);
                                    }

                                    @Override
                                    public void onCancel() {
                                        mc.displayGuiScreen(null);
                                    }
                                }));
                    } else {
                        WDL.baseProps.setProperty("LinkedWorlds", "");
                        WDL.saveProps();
                        WDL.propsFound = true;

                        mc.displayGuiScreen(GuiWDL.this);
                    }
                }

                @Override
                public void onCancel() {
                    mc.displayGuiScreen(null);
                }
            }));
            return;
        }

        this.buttonList.clear();
        this.title = I18n.format("net.daporkchop.pepsimod.wdl.gui.net.daporkchop.pepsimod.wdl.title",
                WDL.baseFolderName.replace('@', ':'));

        if (WDL.baseProps.getProperty("ServerName").isEmpty()) {
            WDL.baseProps.setProperty("ServerName", WDL.getServerName());
        }

        this.worldname = new GuiTextField(42, this.fontRenderer,
                this.width / 2 - 155, 19, 150, 18);
        this.worldname.setText(WDL.baseProps.getProperty("ServerName"));

        this.buttonList.add(new GuiButton(100, this.width / 2 - 100,
                this.height - 29, I18n.format("gui.done")));

        this.list = new GuiWDLButtonList();
    }

    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton button) {
        if (!button.enabled) {
            return;
        }

        if (button.id == 100) { // Done
            this.mc.displayGuiScreen(this.parent);
        }
    }

    @Override
    public void onGuiClosed() {
        if (this.worldname != null) {
            WDL.baseProps.setProperty("ServerName", this.worldname.getText());

            WDL.saveProps();
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
        this.worldname.mouseClicked(mouseX, mouseY, mouseButton);
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

    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.worldname.textboxKeyTyped(typedChar, keyCode);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        this.worldname.updateCursorCounter();
        super.updateScreen();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        displayedTooltip = null;

        this.list.drawScreen(mouseX, mouseY, partialTicks);

        this.drawCenteredString(this.fontRenderer, this.title,
                this.width / 2, 8, 0xFFFFFF);
        String name = I18n.format("net.daporkchop.pepsimod.wdl.gui.net.daporkchop.pepsimod.wdl.worldname");
        this.drawString(this.fontRenderer, name, this.worldname.x
                - this.fontRenderer.getStringWidth(name + " "), 26, 0xFFFFFF);
        this.worldname.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);

        Utils.drawGuiInfoBox(displayedTooltip, width, height, 48);
    }

    private class GuiWDLButtonList extends GuiListExtended {
        private final List<GuiListEntry> entries;

        {
            entries = new ArrayList<>();

            entries.add(new ButtonEntry("worldOverrides", GuiWDLWorld::new, true));
            entries.add(new ButtonEntry("generatorOverrides", GuiWDLGenerator::new, true));
            entries.add(new ButtonEntry("playerOverrides", GuiWDLPlayer::new, true));
            entries.add(new ButtonEntry("entityOptions", GuiWDLEntities::new, true));
            entries.add(new ButtonEntry("gameruleOptions", GuiWDLGameRules::new, true));
            entries.add(new ButtonEntry("backupOptions", GuiWDLBackup::new, true));
            entries.add(new ButtonEntry("messageOptions", GuiWDLMessages::new, false));
            entries.add(new ButtonEntry("permissionsInfo", GuiWDLPermissions::new, false));
            entries.add(new ButtonEntry("about", GuiWDLAbout::new, false));
            if (WDLUpdateChecker.hasNewVersion()) {
                // Put at start
                entries.add(0, new ButtonEntry("updates.hasNew", GuiWDLUpdates::new, false));
            } else {
                entries.add(new ButtonEntry("updates", GuiWDLUpdates::new, false));
            }
        }

        public GuiWDLButtonList() {
            super(GuiWDL.this.mc, GuiWDL.this.width, GuiWDL.this.height, 39,
                    GuiWDL.this.height - 32, 20);
        }

        @Override
        public IGuiListEntry getListEntry(int index) {
            return entries.get(index);
        }

        @Override
        protected int getSize() {
            return entries.size();
        }

        private class ButtonEntry extends GuiListEntry {
            private final GuiButton button;
            private final Function<GuiScreen, GuiScreen> openFunc;

            private final String tooltip;

            /**
             * Constructor.
             *
             * @param key        The I18n key, which will have the base for this GUI
             *                   prepended.
             * @param openFunc   Supplier that constructs a GuiScreen to open based off
             *                   of this screen (the one to open when that screen is
             *                   closed)
             * @param needsPerms Whether the player needs download permission to use
             *                   this button.
             */
            public ButtonEntry(String key, Function<GuiScreen, GuiScreen> openFunc, boolean needsPerms) {
                this.button = new GuiButton(0, 0, 0, I18n.format("net.daporkchop.pepsimod.wdl.gui.net.daporkchop.pepsimod.wdl."
                        + key + ".name"));
                this.openFunc = openFunc;
                if (needsPerms) {
                    button.enabled = WDLPluginChannels.canDownloadAtAll();
                }

                this.tooltip = I18n.format("net.daporkchop.pepsimod.wdl.gui.net.daporkchop.pepsimod.wdl." + key + ".description");
            }

            @Override
            public void drawEntry(int slotIndex, int x, int y, int listWidth,
                                  int slotHeight, int mouseX, int mouseY, boolean isSelected) {
                button.x = GuiWDL.this.width / 2 - 100;
                button.y = y;

                LocalUtils.drawButton(button, mc, mouseX, mouseY);

                if (button.isMouseOver()) {
                    displayedTooltip = tooltip;
                }
            }

            @Override
            public boolean mousePressed(int slotIndex, int x, int y,
                                        int mouseEvent, int relativeX, int relativeY) {
                if (button.mousePressed(mc, x, y)) {
                    mc.displayGuiScreen(openFunc.apply(GuiWDL.this));

                    button.playPressSound(mc.getSoundHandler());

                    return true;
                }

                return false;
            }

            @Override
            public void mouseReleased(int slotIndex, int x, int y,
                                      int mouseEvent, int relativeX, int relativeY) {

            }
        }
    }
}
