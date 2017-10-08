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

package net.daporkchop.pepsimod.accountswitcher.iasencrypt;

import net.daporkchop.pepsimod.accountswitcher.ias.account.ExtendedAccountData;
import net.daporkchop.pepsimod.accountswitcher.tools.Config;
import net.daporkchop.pepsimod.accountswitcher.tools.alt.AccountData;
import net.daporkchop.pepsimod.accountswitcher.tools.alt.AltDatabase;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;

/**
 * @author The_Fireplace
 */
public final class Standards {
    public static final String cfgn = ".iasx";
    public static final String pwdn = ".iasp";
    public static File IASFOLDER = Minecraft.getMinecraft().mcDataDir;

    public static String getPassword() {
        File passwordFile = new File(IASFOLDER, pwdn);
        if (passwordFile.exists()) {
            String pass;
            try {
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(passwordFile));
                pass = (String) stream.readObject();
                stream.close();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            return pass;
        } else {
            String newPass = EncryptionTools.generatePassword();
            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(passwordFile));
                out.writeObject(newPass);
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                Path file = passwordFile.toPath();
                DosFileAttributes attr = Files.readAttributes(file, DosFileAttributes.class);
                DosFileAttributeView view = Files.getFileAttributeView(file, DosFileAttributeView.class);
                if (!attr.isHidden())
                    view.setHidden(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return newPass;
        }
    }

    public static void updateFolder() {
        String dir;
        String OS = (System.getProperty("os.name")).toUpperCase();
        if (OS.contains("WIN")) {
            dir = System.getenv("AppData");
        } else {
            dir = System.getProperty("user.home");
            if (OS.contains("MAC"))
                dir += "/Library/Application Support";
        }

        IASFOLDER = new File(dir);
    }

    public static void importAccounts() {
        processData(getConfigV3());
        processData(getConfigV2());
        processData(getConfigV1(), false);
    }

    private static boolean hasData(AccountData data) {
        for (AccountData edata : AltDatabase.getInstance().getAlts()) {
            if (edata.equalsBasic(data)) {
                return true;
            }
        }
        return false;
    }

    private static void processData(Config olddata) {
        processData(olddata, true);
    }

    private static void processData(Config olddata, boolean decrypt) {
        if (olddata != null) {
            for (AccountData data : ((AltDatabase) olddata.getKey("altaccounts")).getAlts()) {
                AccountData data2 = convertData(data, decrypt);
                if (!hasData(data2))
                    AltDatabase.getInstance().getAlts().add(data2);
            }
        }
    }

    private static ExtendedAccountData convertData(AccountData oldData, boolean decrypt) {
        if (decrypt) {
            if (oldData instanceof ExtendedAccountData)
                return new ExtendedAccountData(EncryptionTools.decodeOld(oldData.user), EncryptionTools.decodeOld(oldData.pass), oldData.alias, ((ExtendedAccountData) oldData).useCount, ((ExtendedAccountData) oldData).lastused, ((ExtendedAccountData) oldData).premium);
            else
                return new ExtendedAccountData(EncryptionTools.decodeOld(oldData.user), EncryptionTools.decodeOld(oldData.pass), oldData.alias);
        } else {
            if (oldData instanceof ExtendedAccountData)
                return new ExtendedAccountData(oldData.user, oldData.pass, oldData.alias, ((ExtendedAccountData) oldData).useCount, ((ExtendedAccountData) oldData).lastused, ((ExtendedAccountData) oldData).premium);
            else
                return new ExtendedAccountData(oldData.user, oldData.pass, oldData.alias);
        }
    }

    private static Config getConfigV3() {
        File f = new File(IASFOLDER, ".ias");
        Config cfg = null;
        if (f.exists()) {
            try {
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                cfg = (Config) stream.readObject();
                stream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            f.delete();
        }
        return cfg;
    }

    private static Config getConfigV2() {
        File f = new File(Minecraft.getMinecraft().mcDataDir, ".ias");
        Config cfg = null;
        if (f.exists()) {
            try {
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                cfg = (Config) stream.readObject();
                stream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            f.delete();
        }
        return cfg;
    }

    private static Config getConfigV1() {
        File f = new File(Minecraft.getMinecraft().mcDataDir, "user.cfg");
        Config cfg = null;
        if (f.exists()) {
            try {
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                cfg = (Config) stream.readObject();
                stream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            f.delete();
        }
        return cfg;
    }
}
