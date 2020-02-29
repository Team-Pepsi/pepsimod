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
            if (pepsimod.originalSession == null) {
                pepsimod.originalSession = Minecraft.getMinecraft().getSession();
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
            /*for (Config.AccountEntry data : Pepsimod.pepsimodInstance.getConfig().getAccounts()) {
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
                /*for (int i = 0; i < Pepsimod.pepsimodInstance.getConfig().getAccounts().size(); i++) {
                    Config.AccountEntry data = Pepsimod.pepsimodInstance.getConfig().getAccounts().get(i);
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
