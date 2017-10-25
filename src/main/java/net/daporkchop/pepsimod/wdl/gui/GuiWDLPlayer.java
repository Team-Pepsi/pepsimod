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
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class GuiWDLPlayer extends GuiScreen {
    private String title;
    private GuiScreen parent;
    private GuiButton healthBtn;
    private GuiButton hungerBtn;
    private GuiButton playerPosBtn;
    private GuiButton pickPosBtn;
    private boolean showPosFields = false;
    private GuiNumericTextField posX;
    private GuiNumericTextField posY;
    private GuiNumericTextField posZ;
    private int posTextY;

    public GuiWDLPlayer(GuiScreen var1) {
        this.parent = var1;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.title = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.title",
                WDL.baseFolderName.replace('@', ':'));
        int y = this.height / 4 - 15;
        this.healthBtn = new GuiButton(1, this.width / 2 - 100, y, getHealthText());
        this.buttonList.add(this.healthBtn);
        y += 22;
        this.hungerBtn = new GuiButton(2, this.width / 2 - 100, y, getHungerText());
        this.buttonList.add(this.hungerBtn);
        y += 22;
        this.playerPosBtn = new GuiButton(3, this.width / 2 - 100, y,
                getPlayerPosText());
        this.buttonList.add(this.playerPosBtn);
        y += 22;
        this.posTextY = y + 4;
        this.posX = new GuiNumericTextField(40, this.fontRenderer,
                this.width / 2 - 87, y, 50, 16);
        this.posY = new GuiNumericTextField(41, this.fontRenderer,
                this.width / 2 - 19, y, 50, 16);
        this.posZ = new GuiNumericTextField(42, this.fontRenderer,
                this.width / 2 + 48, y, 50, 16);
        this.posX.setText(WDL.worldProps.getProperty("PlayerX"));
        this.posY.setText(WDL.worldProps.getProperty("PlayerY"));
        this.posZ.setText(WDL.worldProps.getProperty("PlayerZ"));
        this.posX.setMaxStringLength(7);
        this.posY.setMaxStringLength(7);
        this.posZ.setMaxStringLength(7);
        y += 18;
        this.pickPosBtn = new GuiButton(4, this.width / 2 - 0, y, 100, 20,
                I18n.format("net.daporkchop.pepsimod.wdl.gui.player.setPositionToCurrentPosition"));
        this.buttonList.add(this.pickPosBtn);

        upadatePlayerPosVisibility();

        this.buttonList.add(new GuiButton(100, this.width / 2 - 100,
                this.height - 29, I18n.format("gui.done")));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton var1) {
        if (var1.enabled) {
            if (var1.id == 1) {
                this.cycleHealth();
            } else if (var1.id == 2) {
                this.cycleHunger();
            } else if (var1.id == 3) {
                this.cyclePlayerPos();
            } else if (var1.id == 4) {
                this.setPlayerPosToPlayerPosition();
            } else if (var1.id == 100) {
                this.mc.displayGuiScreen(this.parent);
            }
        }
    }

    @Override
    public void onGuiClosed() {
        if (this.showPosFields) {
            WDL.worldProps.setProperty("PlayerX", posX.getText());
            WDL.worldProps.setProperty("PlayerY", posY.getText());
            WDL.worldProps.setProperty("PlayerZ", posZ.getText());
        }

        WDL.saveProps();
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
            throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.showPosFields) {
            this.posX.mouseClicked(mouseX, mouseY, mouseButton);
            this.posY.mouseClicked(mouseX, mouseY, mouseButton);
            this.posZ.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.posX.textboxKeyTyped(typedChar, keyCode);
        this.posY.textboxKeyTyped(typedChar, keyCode);
        this.posZ.textboxKeyTyped(typedChar, keyCode);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        this.posX.updateCursorCounter();
        this.posY.updateCursorCounter();
        this.posZ.updateCursorCounter();
        super.updateScreen();
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Utils.drawListBackground(23, 32, 0, 0, height, width);

        this.drawCenteredString(this.fontRenderer, this.title,
                this.width / 2, 8, 0xFFFFFF);

        String tooltip = null;

        if (this.showPosFields) {
            this.drawString(this.fontRenderer, "X:", this.width / 2 - 99,
                    this.posTextY, 0xFFFFFF);
            this.drawString(this.fontRenderer, "Y:", this.width / 2 - 31,
                    this.posTextY, 0xFFFFFF);
            this.drawString(this.fontRenderer, "Z:", this.width / 2 + 37,
                    this.posTextY, 0xFFFFFF);
            this.posX.drawTextBox();
            this.posY.drawTextBox();
            this.posZ.drawTextBox();

            if (Utils.isMouseOverTextBox(mouseX, mouseY, posX)) {
                tooltip = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.positionTextBox.description", "X");
            } else if (Utils.isMouseOverTextBox(mouseX, mouseY, posY)) {
                tooltip = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.positionTextBox.description", "Y");
            } else if (Utils.isMouseOverTextBox(mouseX, mouseY, posZ)) {
                tooltip = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.positionTextBox.description", "Z");
            }

            if (pickPosBtn.isMouseOver()) {
                tooltip = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.setPositionToCurrentPosition.description");
            }
        }

        if (healthBtn.isMouseOver()) {
            tooltip = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.health.description");
        }
        if (hungerBtn.isMouseOver()) {
            tooltip = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.hunger.description");
        }
        if (playerPosBtn.isMouseOver()) {
            tooltip = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.position.description");
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        if (tooltip != null) {
            Utils.drawGuiInfoBox(tooltip, width, height, 48);
        }
    }

    private void cycleHealth() {
        String prop = WDL.baseProps.getProperty("PlayerHealth");

        if (prop.equals("keep")) {
            WDL.baseProps.setProperty("PlayerHealth", "20");
        } else if (prop.equals("20")) {
            WDL.baseProps.setProperty("PlayerHealth", "keep");
        }

        this.healthBtn.displayString = getHealthText();
    }

    private void cycleHunger() {
        String prop = WDL.baseProps.getProperty("PlayerFood");

        if (prop.equals("keep")) {
            WDL.baseProps.setProperty("PlayerFood", "20");
        } else if (prop.equals("20")) {
            WDL.baseProps.setProperty("PlayerFood", "keep");
        }

        this.hungerBtn.displayString = getHungerText();
    }

    private void cyclePlayerPos() {
        String prop = WDL.worldProps.getProperty("PlayerPos");

        if (prop.equals("keep")) {
            WDL.worldProps.setProperty("PlayerPos", "xyz");
        } else if (prop.equals("xyz")) {
            WDL.worldProps.setProperty("PlayerPos", "keep");
        }

        playerPosBtn.displayString = getPlayerPosText();
        upadatePlayerPosVisibility();
    }

    private String getHealthText() {
        String result = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.health."
                + WDL.baseProps.getProperty("PlayerHealth"));

        if (result.startsWith("net.daporkchop.pepsimod.wdl.gui.player.health.")) {
            // Unrecognized hunger -- not translated
            // Only done with time because time can have a value that
            // isn't on the normal list but still can be parsed
            result = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.health.custom",
                    WDL.baseProps.getProperty("PlayerHealth"));
        }

        return result;
    }

    private String getHungerText() {
        String result = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.hunger."
                + WDL.baseProps.getProperty("PlayerFood"));

        if (result.startsWith("net.daporkchop.pepsimod.wdl.gui.player.hunger.")) {
            // Unrecognized hunger -- not translated
            // Only done with time because time can have a value that
            // isn't on the normal list but still can be parsed
            result = I18n.format("net.daporkchop.pepsimod.wdl.gui.player.hunger.custom",
                    WDL.baseProps.getProperty("PlayerFood"));
        }

        return result;
    }

    private void upadatePlayerPosVisibility() {
        showPosFields = WDL.worldProps.getProperty("PlayerPos").equals("xyz");
        pickPosBtn.visible = showPosFields;
    }

    private String getPlayerPosText() {
        return I18n.format("net.daporkchop.pepsimod.wdl.gui.player.position."
                + WDL.worldProps.getProperty("PlayerPos"));
    }

    private void setPlayerPosToPlayerPosition() {
        this.posX.setValue((int) WDL.thePlayer.posX);
        this.posY.setValue((int) WDL.thePlayer.posY);
        this.posZ.setValue((int) WDL.thePlayer.posZ);
    }
}
