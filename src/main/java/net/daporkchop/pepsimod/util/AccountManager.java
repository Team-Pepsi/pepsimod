package net.daporkchop.pepsimod.util;

import com.mojang.authlib.Agent;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.UserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.daporkchop.pepsimod.PepsiMod;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Created by DaPorkchop_ on 18.02.17.
 */
public class AccountManager {

    private final UserAuthentication auth;

    public AccountManager() {
        UUID uuid = UUID.randomUUID();
        AuthenticationService authService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), uuid.toString());
        auth = authService.createUserAuthentication(Agent.MINECRAFT);
        authService.createMinecraftSessionService();
    }

    /**
     * Sets the current session
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
                    //System.out.println("Found field " + f.toString() + ", injecting...");
                }
            }

            if (session == null) {
                throw new IllegalStateException("No field of type " + Session.class.getCanonicalName() + " declared.");
            }

            session.setAccessible(true);
            if (PepsiMod.INSTANCE.originalSession == null)  {
                PepsiMod.INSTANCE.originalSession = Minecraft.getMinecraft().getSession();
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
     * @param username
     * @param password
     * @return
     */
    public void setUser(String username, String password) {
        if(Minecraft.getMinecraft().getSession().getUsername() != username || Minecraft.getMinecraft().getSession().getToken().equals("0")){
            /*for (Config.AccountEntry data : PepsiMod.INSTANCE.getConfig().getAccounts()) {
                if (data.getUsername().equals(Minecraft.getMinecraft().getSession().getUsername()) && data.getUsername().equals(username)) {
                    return;
                }
            }*/
            this.auth.logOut();
            this.auth.setUsername(username);
            this.auth.setPassword(password);
            try {
                this.auth.logIn();
                Session session = new Session(this.auth.getSelectedProfile().getName(), UUIDTypeAdapter.fromUUID(auth.getSelectedProfile().getId()), this.auth.getAuthenticatedToken(), this.auth.getUserType().getName());
                setSession(session);
                /*for (int i = 0; i < PepsiMod.INSTANCE.getConfig().getAccounts().size(); i++) {
                    Config.AccountEntry data = PepsiMod.INSTANCE.getConfig().getAccounts().get(i);
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
