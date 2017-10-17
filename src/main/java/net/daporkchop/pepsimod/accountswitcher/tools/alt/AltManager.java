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

package net.daporkchop.pepsimod.accountswitcher.tools.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;
import net.daporkchop.pepsimod.accountswitcher.MR;
import net.daporkchop.pepsimod.accountswitcher.ias.account.AlreadyLoggedInException;
import net.daporkchop.pepsimod.accountswitcher.iasencrypt.EncryptionTools;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.util.UUID;

/**
 * @author MRebhan
 * @author The_Fireplace
 */
public class AltManager {
    private static AltManager manager = null;
    private final UserAuthentication auth;

    private AltManager() {
        UUID uuid = UUID.randomUUID();
        AuthenticationService authService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), uuid.toString());
        auth = authService.createUserAuthentication(Agent.MINECRAFT);
        authService.createMinecraftSessionService();
    }

    public static AltManager getInstance() {
        if (manager == null) {
            manager = new AltManager();
        }

        return manager;
    }

    public Throwable setUser(String username, String password) {
        Throwable throwable = null;
        if (!Minecraft.getMinecraft().getSession().getUsername().equals(EncryptionTools.decode(username)) || Minecraft.getMinecraft().getSession().getToken().equals("0")) {
            if (!Minecraft.getMinecraft().getSession().getToken().equals("0")) {
                for (AccountData data : AltDatabase.getInstance().getAlts()) {
                    if (data.alias.equals(Minecraft.getMinecraft().getSession().getUsername()) && data.user.equals(username)) {
                        throwable = new AlreadyLoggedInException();
                        return throwable;
                    }
                }
            }
            this.auth.logOut();
            this.auth.setUsername(EncryptionTools.decode(username));
            this.auth.setPassword(EncryptionTools.decode(password));
            try {
                this.auth.logIn();
                Session session = new Session(this.auth.getSelectedProfile().getName(), UUIDTypeAdapter.fromUUID(auth.getSelectedProfile().getId()), this.auth.getAuthenticatedToken(), this.auth.getUserType().getName());
                MR.setSession(session);
                for (int i = 0; i < AltDatabase.getInstance().getAlts().size(); i++) {
                    AccountData data = AltDatabase.getInstance().getAlts().get(i);
                    if (data.user.equals(username) && data.pass.equals(password)) {
                        data.alias = session.getUsername();
                    }
                }
            } catch (Exception e) {
                throwable = e;
            }
        }
        return throwable;
    }

    public void setUserOffline(String username) {
        this.auth.logOut();
        Session session = new Session(username, username, "0", "legacy");
        try {
            MR.setSession(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
