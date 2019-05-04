package net.daporkchop.pepsimod.misc.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.daporkchop.pepsimod.PepsiModMixinLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Function;

/**
 * @author DaPorkchop_
 */
public class DataLoader {
    protected static final String RESOURCES_URL = "https://raw.githubusercontent.com/Team-Pepsi/pepsimod/master/resources/resources.json";

    protected static Function<String, InputStream> READER_FUNCTION = null;

    public static void load()   {
    }

    protected static JsonObject getResourcesRoot()  {
        if (PepsiModMixinLoader.isObfuscatedEnvironment)    {

        }
        return null; //TODO
    }

    protected static JsonObject readJson(InputStream in)    {
        return new JsonParser().parse(new InputStreamReader(in)).getAsJsonObject();
    }
}
