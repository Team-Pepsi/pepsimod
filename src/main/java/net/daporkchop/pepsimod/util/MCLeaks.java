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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.util.CryptManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLLog;

import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.PublicKey;

public class MCLeaks {

    public static final URL redeemUrl = HTTPUtils.constantURL("http://auth.mcleaks.net/v1/redeem");
    public static final URL joinUrl = HTTPUtils.constantURL("http://auth.mcleaks.net/v1/joinserver");

    public static RedeemResponse redeemToken(String token) {
        try {
            String response = HTTPUtils.performPostRequest(redeemUrl,
                    "{ \"token\": \"" + token + "\" }",
                    "application/json");

            JsonObject json = (new JsonParser()).parse(response).getAsJsonObject();
            JsonObject result = json.getAsJsonObject("result");

            return new RedeemResponse(result.get("mcname").getAsString(), result.get("session").getAsString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Invalid or expired token!", "MCLeaks Error", JOptionPane.OK_OPTION);
        }
        return new RedeemResponse();
    }

    public static void joinServerStuff(SPacketEncryptionRequest pck, NetworkManager mgr) {
        try {
            final SecretKey secretkey = CryptManager.createNewSharedKey();
            String s = pck.getServerId();
            PublicKey publickey = pck.getPublicKey();
            String serverhash = (new BigInteger(CryptManager.getServerIdHash(s, publickey, secretkey))).toString(16);

            String request = "{ \"session\": \"" + Minecraft.getMinecraft().getSession().getToken() + "\", " +
                    "\"mcname\": \"" + Minecraft.getMinecraft().getSession().getUsername() + "\", " +
                    "\"serverhash\": \"" + serverhash + "\", \"server\": " +
                    '"' + (Minecraft.getMinecraft().getCurrentServerData().serverIP.split(":").length == 1 ? Minecraft.getMinecraft().getCurrentServerData().serverIP + ":25565" : Minecraft.getMinecraft().getCurrentServerData().serverIP) + "\" }";

            FMLLog.log.info(request);

            String result = HTTPUtils.performPostRequest(joinUrl, request, "application/json");

            FMLLog.log.info(result);

            JsonObject json = (new JsonParser()).parse(result).getAsJsonObject();
            if (!json.get("success").getAsBoolean()) {
                mgr.closeChannel(new TextComponentString("\u00A7c\u00A7lError validating \u00A79MCLeaks \u00A7ckey!"));
            }

            mgr.sendPacket(new CPacketEncryptionResponse(secretkey, publickey, pck.getVerifyToken()), p_operationComplete_1_ -> mgr.enableEncryption(secretkey));

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static class RedeemResponse {

        public boolean success;

        @Nullable
        private String name;

        @Nullable
        private String session;

        public RedeemResponse() { //welp
            this.success = false;
        }

        public RedeemResponse(String n, String s) {
            this.success = true;
            this.name = n;
            this.session = s;
        }

        public String getName() {
            return this.name;
        }

        public String getSession() {
            return this.session;
        }
    }
}