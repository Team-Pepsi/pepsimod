/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.gui.mcleaks;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.daporkchop.pepsimod.util.AccountManager;
import net.daporkchop.pepsimod.util.HTTPUtils;
import net.daporkchop.pepsimod.util.MCLeaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

import static net.daporkchop.pepsimod.util.PepsiConstants.pepsimod;

public class GuiScreenMCLeaks extends GuiScreen {
    public Minecraft mc;
    public GuiScreen prevScreen;
    private GuiTextField tokenField;

    public GuiScreenMCLeaks(GuiScreen screen, Minecraft minecraft) {
        this.mc = minecraft;
        this.prevScreen = screen;
    }

    public void updateScreen() {
        this.tokenField.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 18, "Log in"));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 2, this.height / 4 + 120 + 18, 98, 20, "Back"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120 + 18, 98, 20, "Original"));
        this.tokenField = new GuiTextField(1, this.fontRenderer, this.width / 2 - 100, 106, 200, 20);
        this.tokenField.setMaxStringLength(128);
        this.tokenField.setText("");
        this.buttonList.get(0).enabled = !this.tokenField.getText().isEmpty();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 1) {
                this.mc.displayGuiScreen(this.prevScreen);
            } else if (button.id == 0) {
                MCLeaks.RedeemResponse response = MCLeaks.redeemToken(this.tokenField.getText());
                if (response.success) {
                    String idJson = HTTPUtils.performGetRequest(HTTPUtils.constantURL("https://api.mojang.com/users/profiles/minecraft/" + response.getName()));
                    JsonObject json = (new JsonParser()).parse(idJson).getAsJsonObject();
                    String UUID = json.get("id").getAsString();

                    Session session = new Session(response.getName(), UUID, response.getSession(), "mojang");

                    try {
                        new AccountManager().setSession(session);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.tokenField.setText("");
                }

                pepsimod.isMcLeaksAccount = true;
            } else if (button.id == 2) {
                if (pepsimod.originalSession != null) {
                    try {
                        new AccountManager().setSession(pepsimod.originalSession);
                        pepsimod.isMcLeaksAccount = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException //TODO: obfuscate password
    {
        this.tokenField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 15) {
            this.tokenField.setFocused(!this.tokenField.isFocused());
        }

        if (keyCode == 28 || keyCode == 156) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.tokenField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.buttonList.get(0).enabled = !this.tokenField.getText().isEmpty();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "\u00A79\u00A7lMCLeaks login", this.width / 2, 17, 16777215);
        this.drawCenteredString(this.fontRenderer, "Username: " + this.mc.getSession().getUsername(), this.width / 2, 27, 10526880);
        this.drawCenteredString(this.fontRenderer, "UUID: " + this.mc.getSession().getPlayerID(), this.width / 2, 37, 10526880);
        this.tokenField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
