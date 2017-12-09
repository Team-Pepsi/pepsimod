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

package net.daporkchop.pepsimod.util.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.daporkchop.pepsimod.util.config.impl.*;
import net.minecraftforge.fml.common.FMLLog;

import java.util.Hashtable;
import java.util.Map;

public class Config {
    private static Hashtable<String, IConfigTranslator> translators = new Hashtable<>();

    static {
        registerConfigTranslator(AnnouncerTranslator.INSTANCE);
        registerConfigTranslator(AutoEatTranslator.INSTANCE);
        registerConfigTranslator(BedBomberTranslator.INSTANCE);
        registerConfigTranslator(ClickGUITranslator.INSTANCE);
        registerConfigTranslator(CpuLimitTranslator.INSTANCE);
        registerConfigTranslator(CriticalsTranslator.INSTANCE);
        registerConfigTranslator(CrystalAuraTranslator.INSTANCE);
        registerConfigTranslator(ElytraFlyTranslator.INSTANCE);
        registerConfigTranslator(EntitySpeedTranslator.INSTANCE);
        registerConfigTranslator(ESPTranslator.INSTANCE);
        registerConfigTranslator(FlightTranslator.INSTANCE);
        registerConfigTranslator(FreecamTranslator.INSTANCE);
        registerConfigTranslator(FriendsTranslator.INSTANCE);
        registerConfigTranslator(GeneralTranslator.INSTANCE);
        registerConfigTranslator(HUDTranslator.INSTANCE);
        registerConfigTranslator(NotificationsTranslator.INSTANCE);
        registerConfigTranslator(NoWeatherTranslator.INSTANCE);
        registerConfigTranslator(SpeedmineTranslator.INSTANCE);
        registerConfigTranslator(StepTranslator.INSTANCE);
        registerConfigTranslator(TargettingTranslator.INSTANCE);
        registerConfigTranslator(TimerTranslator.INSTANCE);
        registerConfigTranslator(TracersTranslator.INSTANCE);
        registerConfigTranslator(VelocityTranslator.INSTANCE);
        registerConfigTranslator(WaypointsTranslator.INSTANCE);
        registerConfigTranslator(XrayTranslator.INSTANCE);
    }

    public static void registerConfigTranslator(IConfigTranslator element) {
        translators.put(element.name(), element);
    }

    public static void loadConfig(String configJson) {
        System.out.println("Loading config!");
        System.out.println(configJson);

        JsonObject object = null;
        try {
            object = new JsonParser().parse(configJson).getAsJsonObject();
        } catch (IllegalStateException e) {
            //Thrown when the config is an empty string
            FMLLog.info("Using default config because the file is empty or unreadable");
            return;
        }
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            translators.getOrDefault(entry.getKey(), NullConfigTranslator.INSTANCE).decode(entry.getKey(), entry.getValue().getAsJsonObject());
        }
    }

    public static String saveConfig() {
        JsonObject object = new JsonObject();

        for (IConfigTranslator translator : translators.values()) {
            JsonObject elementObj = new JsonObject();
            translator.encode(elementObj);
            object.add(translator.name(), elementObj);
        }

        return new Gson().toJson(object);
    }
}
