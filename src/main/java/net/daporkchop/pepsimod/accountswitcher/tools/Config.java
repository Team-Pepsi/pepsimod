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

package net.daporkchop.pepsimod.accountswitcher.tools;

import net.daporkchop.pepsimod.accountswitcher.iasencrypt.Standards;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;

/**
 * @author mrebhan
 * @author The_Fireplace
 */
public class Config implements Serializable {
    public static final long serialVersionUID = 0xDEADBEEF;
    private static final String configFileName = Standards.cfgn;
    private static Config instance = null;
    private ArrayList<Pair<String, Object>> field_218893_c;

    private Config() {
        this.field_218893_c = new ArrayList<Pair<String, Object>>();
        instance = this;
    }

    public static Config getInstance() {
        return instance;
    }

    public static void save() {
        saveToFile();
    }

    public static void load() {
        loadFromOld();
        readFromFile();
    }

    private static void readFromFile() {
        File f = new File(Standards.IASFOLDER, configFileName);
        if (f.exists()) {
            try {
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                instance = (Config) stream.readObject();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
                instance = new Config();
                f.delete();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                instance = new Config();
                f.delete();
            }
        }
        if (instance == null)
            instance = new Config();
    }

    private static void saveToFile() {
        try {
            Path file = new File(Standards.IASFOLDER, configFileName).toPath();
            DosFileAttributes attr = Files.readAttributes(file, DosFileAttributes.class);
            DosFileAttributeView view = Files.getFileAttributeView(file, DosFileAttributeView.class);
            if (attr.isHidden())
                view.setHidden(false);
        } catch (NoSuchFileException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(Standards.IASFOLDER, configFileName)));
            out.writeObject(instance);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Path file = new File(Standards.IASFOLDER, configFileName).toPath();
            DosFileAttributes attr = Files.readAttributes(file, DosFileAttributes.class);
            DosFileAttributeView view = Files.getFileAttributeView(file, DosFileAttributeView.class);
            if (!attr.isHidden())
                view.setHidden(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadFromOld() {
        File f = new File(Minecraft.getMinecraft().mcDataDir, "user.cfg");
        if (f.exists()) {
            try {
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(f));
                instance = (Config) stream.readObject();
                stream.close();
                f.delete();
                System.out.println("Loaded data from old file");
            } catch (IOException e) {
                e.printStackTrace();
                f.delete();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                f.delete();
            }
        }
    }

    public void setKey(Pair<String, Object> key) {
        if (this.getKey(key.getValue1()) != null)
            this.removeKey(key.getValue1());
        field_218893_c.add(key);
        save();
    }

    public void setKey(String key, Object value) {
        this.setKey(new Pair<String, Object>(key, value));
    }

    public Object getKey(String key) {
        if (field_218893_c == null) {
            System.out.println("Error: Config failed to load during PreInitialization. Loading now.");
            load();
        }
        for (Pair<String, Object> aField_218893_c : field_218893_c) {
            if (aField_218893_c.getValue1().equals(key))
                return aField_218893_c.getValue2();
        }

        return null;
    }

    private void removeKey(String key) {
        for (int i = 0; i < field_218893_c.size(); i++) {
            if (field_218893_c.get(i).getValue1().equals(key))
                field_218893_c.remove(i);
        }
    }
}
