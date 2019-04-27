/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.lang.reflect.Field;
import java.util.UUID;

public class AccountManager extends PepsiConstants {

    private final UserAuthentication auth;

    public AccountManager() {
        UUID uuid = UUID.randomUUID();
        AuthenticationService authService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), uuid.toString());
        this.auth = authService.createUserAuthentication(Agent.MINECRAFT);
        authService.createMinecraftSessionService();
    }

    /**
     * Sets the current session
     *
     * @param s
     * @throws Exception
     */
    public void setSession(Session s) throws Exception {
        Class<? extends Minecraft> mc = Minecraft.getMinecraft().getClass();
        try {
            Field session = null;

            for (Field f : mc.getDeclaredFields()) {
                if (f.getType().isInstance(s)) {
                    session = f;
                    //FMLLog.log.info("Found field " + f.toString() + ", injecting...");
                }
            }

            if (session == null) {
                throw new IllegalStateException("No field of type " + Session.class.getCanonicalName() + " declared.");
            }

            session.setAccessible(true);
            if (pepsiMod.originalSession == null) {
                pepsiMod.originalSession = Minecraft.getMinecraft().getSession();
            }
            session.set(Minecraft.getMinecraft(), s);
            session.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Sets the current user as a player with the given credentials
     *
     * @param username
     * @param password
     * @return
     */
    public void setUser(String username, String password) {
        if (Minecraft.getMinecraft().getSession().getUsername() != username || "0".equals(Minecraft.getMinecraft().getSession().getToken())) {
            /*for (Config.AccountEntry data : PepsiMod.pepsimodInstance.getConfig().getAccounts()) {
                if (data.getUsername().equals(Minecraft.getMinecraft().getSession().getUsername()) && data.getUsername().equals(username)) {
                    return;
                }
            }*/
            this.auth.logOut();
            this.auth.setUsername(username);
            this.auth.setPassword(password);
            try {
                this.auth.logIn();
                Session session = new Session(this.auth.getSelectedProfile().getName(), UUIDTypeAdapter.fromUUID(this.auth.getSelectedProfile().getId()), this.auth.getAuthenticatedToken(), this.auth.getUserType().getName());
                this.setSession(session);
                /*for (int i = 0; i < PepsiMod.pepsimodInstance.getConfig().getAccounts().size(); i++) {
                    Config.AccountEntry data = PepsiMod.pepsimodInstance.getConfig().getAccounts().get(i);
                    if (data.getUsername().equals(username) && data.getPassword().equals(password)) {
                        data.setName(session.getUsername());
                    }
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //user is already logged in, do nothing
        }
    }
}
