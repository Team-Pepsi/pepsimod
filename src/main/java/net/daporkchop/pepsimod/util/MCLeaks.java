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

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.client.CPacketEncryptionResponse;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import net.minecraft.util.CryptManager;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLLog;

import javax.annotation.Nullable;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
                    "\"" + (Minecraft.getMinecraft().getCurrentServerData().serverIP.split(":").length == 1 ? Minecraft.getMinecraft().getCurrentServerData().serverIP + ":25565" : Minecraft.getMinecraft().getCurrentServerData().serverIP) + "\" }";

            FMLLog.log.info(request);

            String result = HTTPUtils.performPostRequest(joinUrl, request, "application/json");

            FMLLog.log.info(result);

            JsonObject json = (new JsonParser()).parse(result).getAsJsonObject();
            if (!json.get("success").getAsBoolean()) {
                mgr.closeChannel(new TextComponentString("\u00A7c\u00A7lError validating \u00A79MCLeaks \u00A7ckey!"));
            }

            mgr.sendPacket(new CPacketEncryptionResponse(secretkey, publickey, pck.getVerifyToken()), new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future<? super Void> p_operationComplete_1_) throws Exception {
                    mgr.enableEncryption(secretkey);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static String hash(String str) {
        try {
            byte[] digest = digest(str, "SHA-1");
            return new BigInteger(digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            // Guava version
            throw Throwables.propagate(e);
            // Vanilla Java version
            // throw new RuntimeException(e);
        }
    }

    private static byte[] digest(String str, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        // Guava version
        byte[] strBytes = str.getBytes(Charsets.UTF_8);
        // Vanilla Java 7+ version
        // byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        return md.digest(strBytes);
    }

    public static final class RedeemResponse {

        public boolean success;

        @Nullable
        private String name;

        @Nullable
        private String session;

        public RedeemResponse() { //welp
            success = false;
        }

        public RedeemResponse(String n, String s) {
            success = true;
            this.name = n;
            this.session = s;
        }

        public String getName() {
            return name;
        }

        public String getSession() {
            return session;
        }
    }
}