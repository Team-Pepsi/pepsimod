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

package net.daporkchop.pepsimod.accountswitcher.ias.gui;

import net.daporkchop.pepsimod.accountswitcher.iasencrypt.EncryptionTools;
import net.daporkchop.pepsimod.accountswitcher.tools.alt.AccountData;
import net.daporkchop.pepsimod.accountswitcher.tools.alt.AltDatabase;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * @author evilmidget38
 * @author The_Fireplace
 */
public abstract class AbstractAccountGui extends GuiScreen {
    private final String actionString;
    protected boolean hasUserChanged = false;
    private GuiTextField username;
    private GuiTextField password;
    private GuiButton complete;

    public AbstractAccountGui(String actionString) {
        this.actionString = actionString;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(complete = new GuiButton(2, this.width / 2 - 152, this.height - 28, 150, 20, this.actionString));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 2, this.height - 28, 150, 20, "Cancel"));
        username = new GuiTextField(0, this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
        username.setFocused(true);
        username.setMaxStringLength(64);
        password = new GuiPasswordField(1, this.fontRenderer, this.width / 2 - 100, 90, 200, 20);
        password.setMaxStringLength(64);
        complete.enabled = false;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        this.drawCenteredString(fontRenderer, this.actionString, this.width / 2, 7, -1);
        this.drawCenteredString(fontRenderer, "Username:", this.width / 2 - 130, 66, -1);
        this.drawCenteredString(fontRenderer, "Password:", this.width / 2 - 130, 96, -1);
        username.drawTextBox();
        password.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }

    @Override
    protected void keyTyped(char character, int keyIndex) {
        if (keyIndex == Keyboard.KEY_ESCAPE) {
            escape();
        } else if (keyIndex == Keyboard.KEY_RETURN) {
            if (username.isFocused()) {
                username.setFocused(false);
                password.setFocused(true);
            } else if (password.isFocused() && complete.enabled) {
                complete();
                escape();
            }
        } else if (keyIndex == Keyboard.KEY_TAB) {
            username.setFocused(!username.isFocused());
            password.setFocused(!password.isFocused());
        } else {
            // GuiTextField checks if it's focused before doing anything
            username.textboxKeyTyped(character, keyIndex);
            password.textboxKeyTyped(character, keyIndex);
            if (username.isFocused())
                hasUserChanged = true;
        }
    }

    @Override
    public void updateScreen() {
        username.updateCursorCounter();
        password.updateCursorCounter();
        complete.enabled = canComplete();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 2) {
                complete();
                escape();
            } else if (button.id == 3) {
                escape();
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Return to the Account Selector
     */
    private void escape() {
        mc.displayGuiScreen(new GuiAccountSelector());
    }

    public String getUsername() {
        return username.getText();
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public String getPassword() {
        return password.getText();
    }

    public void setPassword(String password) {
        this.password.setText(password);
    }

    protected boolean accountNotInList() {
        for (AccountData data : AltDatabase.getInstance().getAlts())
            if (EncryptionTools.decode(data.user).equals(getUsername()))
                return false;
        return true;
    }

    public boolean canComplete() {
        return getUsername().length() > 0 && accountNotInList();
    }

    public abstract void complete();
}
