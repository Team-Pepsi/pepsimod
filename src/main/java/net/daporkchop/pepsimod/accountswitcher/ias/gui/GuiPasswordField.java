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

import joptsimple.internal.Strings;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiPasswordField extends GuiTextField {
    public GuiPasswordField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width, int par6Height) {
        super(componentId, fontrendererObj, x, y, par5Width, par6Height);
    }

    @Override
    public void drawTextBox() {
        String password = getText();
        replaceText(Strings.repeat('*', getText().length()));
        super.drawTextBox();
        replaceText(password);
    }

    @Override
    public boolean textboxKeyTyped(char typedChar, int keyCode) {
        // Ignore ctrl+c and ctrl+x to prevent copying the contents of the field
        return !GuiScreen.isKeyComboCtrlC(keyCode) && !GuiScreen.isKeyComboCtrlX(keyCode) && super.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        // Minecraft has variable-width fonts, so replace the text with asterisks so that the correct cursor position is calculated
        String password = getText();
        replaceText(Strings.repeat('*', getText().length()));
        super.mouseClicked(mouseX, mouseY, mouseButton);
        replaceText(password);
        return true;
    }

    /**
     * Sets the text of the field while maintaining the cursor positions
     *
     * @param newText
     */
    private void replaceText(String newText) {
        int cursorPosition = getCursorPosition();
        int selectionEnd = getSelectionEnd();
        setText(newText);
        setCursorPosition(cursorPosition);
        setSelectionPos(selectionEnd);
    }
}
