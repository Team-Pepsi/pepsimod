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

import net.daporkchop.pepsimod.accountswitcher.ias.account.ExtendedAccountData;
import net.daporkchop.pepsimod.accountswitcher.ias.enums.EnumBool;
import net.daporkchop.pepsimod.accountswitcher.ias.tools.JavaTools;
import net.daporkchop.pepsimod.accountswitcher.iasencrypt.EncryptionTools;
import net.daporkchop.pepsimod.accountswitcher.tools.alt.AccountData;
import net.daporkchop.pepsimod.accountswitcher.tools.alt.AltDatabase;

/**
 * The GUI where the alt is added
 *
 * @author The_Fireplace
 * @author evilmidget38
 */
class GuiEditAccount extends AbstractAccountGui {
    private final ExtendedAccountData data;
    private final int selectedIndex;

    public GuiEditAccount(int index) {
        super("Edit Account");
        this.selectedIndex = index;
        AccountData data = AltDatabase.getInstance().getAlts().get(index);

        if (data instanceof ExtendedAccountData) {
            this.data = (ExtendedAccountData) data;
        } else {
            this.data = new ExtendedAccountData(data.user, data.pass, data.alias, 0, JavaTools.getJavaCompat().getDate(), EnumBool.UNKNOWN);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        setUsername(EncryptionTools.decode(data.user));
        setPassword(EncryptionTools.decode(data.pass));
    }

    @Override
    public void complete() {
        AltDatabase.getInstance().getAlts().set(selectedIndex, new ExtendedAccountData(getUsername(), getPassword(), hasUserChanged ? getUsername() : data.alias, data.useCount, data.lastused, data.premium));
    }

}
