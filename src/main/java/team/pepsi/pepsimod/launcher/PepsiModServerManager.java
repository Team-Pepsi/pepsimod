package team.pepsi.pepsimod.launcher;

import net.minecraftforge.fml.common.FMLLog;
import team.pepsi.pepsimod.common.ClientAuthInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class PepsiModServerManager {
    public static HashMap<String, byte[]> downloadPepsiMod() {
        try {
            FMLLog.log.info("Opening connection...");
            Socket socket = new Socket("127.0.0.1", 48273); //TODO: use server address
            FMLLog.log.info("Connection opened!");
            ClientAuthInfo info = new ClientAuthInfo();
            info.username = "";
            info.password = "";
            info.authKey = "";
            info.HWID = ""; //TODO: put in useful values here
            FMLLog.log.info("Creating streams...");
            ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
            FMLLog.log.info("Writing object...");
            os.writeObject(info);
            try {
                FMLLog.log.info("Reading object...");
                HashMap<String, byte[]> pepsimod = (HashMap<String, byte[]>) is.readObject();
                is.close();
                os.close();
                socket.close();
                FMLLog.log.info("Done! " + pepsimod.size());
                return pepsimod;
            } catch (ClassNotFoundException | ClassCastException e) {
                e.printStackTrace();
                is.close();
                os.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().exit(-1);
        return null;
    }
}
