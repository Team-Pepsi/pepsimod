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

package net.daporkchop.pepsimod.accountswitcher.ias.account;

import net.daporkchop.pepsimod.accountswitcher.ias.enums.EnumBool;
import net.daporkchop.pepsimod.accountswitcher.ias.tools.JavaTools;
import net.daporkchop.pepsimod.accountswitcher.tools.alt.AccountData;

import java.util.Arrays;

/**
 * @author The_Fireplace
 */
public class ExtendedAccountData extends AccountData {
    private static final long serialVersionUID = -909128662161235160L;

    public EnumBool premium;
    public int[] lastused;
    public int useCount;

    public ExtendedAccountData(String user, String pass, String alias) {
        super(user, pass, alias);
        useCount = 0;
        lastused = JavaTools.getJavaCompat().getDate();
        premium = EnumBool.UNKNOWN;
    }

    public ExtendedAccountData(String user, String pass, String alias, int useCount, int[] lastused, EnumBool premium) {
        super(user, pass, alias);
        this.useCount = useCount;
        this.lastused = lastused;
        this.premium = premium;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ExtendedAccountData other = (ExtendedAccountData) obj;
        if (!Arrays.equals(lastused, other.lastused)) {
            return false;
        }
        if (premium != other.premium) {
            return false;
        }
        if (useCount != other.useCount) {
            return false;
        }
        return user.equals(other.user) && pass.equals(other.pass);
    }
}
