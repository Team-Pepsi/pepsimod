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

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.io.IOException;
import java.util.List;

public class GuiWDLMultiworld extends GuiScreen {
    private final MultiworldCallback callback;
    private GuiButton multiworldEnabledBtn;
    private boolean enableMultiworld = false;

    //TODO: Some of these things can be constants, but for consistancy aren't.
    //Maybe refactor it?
    private int infoBoxWidth;
    private int infoBoxHeight;
    private int infoBoxX;
    private int infoBoxY;
    private List<String> infoBoxLines;

    public GuiWDLMultiworld(MultiworldCallback callback) {
        this.callback = callback;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        this.buttonList.clear();

        String multiworldMessage = I18n
                .format("net.daporkchop.pepsimod.wdl.gui.multiworld.descirption.requiredWhen")
                + "\n\n"
                + I18n.format("net.daporkchop.pepsimod.wdl.gui.multiworld.descirption.whatIs");

        infoBoxWidth = 320;
        infoBoxLines = Utils.wordWrap(multiworldMessage, infoBoxWidth - 20);
        infoBoxHeight = (fontRenderer.FONT_HEIGHT * (infoBoxLines.size() + 1)) + 40;

        infoBoxX = this.width / 2 - infoBoxWidth / 2;
        infoBoxY = this.height / 2 - infoBoxHeight / 2;

        this.multiworldEnabledBtn = new GuiButton(1, this.width / 2 - 100,
                infoBoxY + infoBoxHeight - 30,
                this.getMultiworldEnabledText());
        this.buttonList.add(this.multiworldEnabledBtn);

        this.buttonList.add(new GuiButton(100, this.width / 2 - 155,
                this.height - 29, 150, 20, I18n.format("gui.cancel")));

        this.buttonList.add(new GuiButton(101, this.width / 2 + 5,
                this.height - 29, 150, 20, I18n.format("gui.done")));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            this.toggleMultiworldEnabled();
        } else if (button.id == 100) {
            callback.onCancel();
        } else if (button.id == 101) {
            callback.onSelect(this.enableMultiworld);
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
            throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Fired when a key is typed. This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e).
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        Utils.drawBorder(32, 32, 0, 0, height, width);

        this.drawCenteredString(this.fontRenderer,
                I18n.format("net.daporkchop.pepsimod.wdl.gui.multiworld.title"),
                this.width / 2, 8, 0xFFFFFF);

        drawRect(infoBoxX, infoBoxY, infoBoxX + infoBoxWidth, infoBoxY
                + infoBoxHeight, 0xB0000000);

        int x = infoBoxX + 10;
        int y = infoBoxY + 10;

        for (String s : infoBoxLines) {
            this.drawString(fontRenderer, s, x, y, 0xFFFFFF);
            y += fontRenderer.FONT_HEIGHT;
        }

        //Red box around "multiworld support" button.
        drawRect(
                multiworldEnabledBtn.x - 2,
                multiworldEnabledBtn.y - 2,
                multiworldEnabledBtn.x
                        + multiworldEnabledBtn.getButtonWidth() + 2,
                multiworldEnabledBtn.y + 20 + 2, 0xFFFF0000);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Toggles whether multiworld support is enabled.
     */
    private void toggleMultiworldEnabled() {
        this.enableMultiworld = !this.enableMultiworld;

        this.multiworldEnabledBtn.displayString = getMultiworldEnabledText();
    }

    /**
     * Gets the text to display on the multiworld enabled button.
     */
    private String getMultiworldEnabledText() {
        return I18n.format("net.daporkchop.pepsimod.wdl.gui.multiworld." + enableMultiworld);
    }

    public interface MultiworldCallback {
        void onCancel();

        void onSelect(boolean enableMutliworld);
    }
}
