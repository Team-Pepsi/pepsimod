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

package net.daporkchop.pepsimod.accountswitcher.ias;

import net.daporkchop.pepsimod.accountswitcher.MR;
import net.daporkchop.pepsimod.accountswitcher.ias.config.ConfigValues;
import net.daporkchop.pepsimod.accountswitcher.ias.events.ClientEvents;
import net.daporkchop.pepsimod.accountswitcher.ias.tools.SkinTools;
import net.daporkchop.pepsimod.accountswitcher.iasencrypt.Standards;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author The_Fireplace
 */
public class IAS {
    public static Configuration config;
    private static Property CASESENSITIVE_PROPERTY;
    private static Property ENABLERELOG_PROPERTY;

    public static void syncConfig() {
        ConfigValues.CASESENSITIVE = CASESENSITIVE_PROPERTY.getBoolean();
        ConfigValues.ENABLERELOG = ENABLERELOG_PROPERTY.getBoolean();
        if (config.hasChanged())
            config.save();
    }

    public static void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        CASESENSITIVE_PROPERTY = config.get(Configuration.CATEGORY_GENERAL, ConfigValues.CASESENSITIVE_NAME, ConfigValues.CASESENSITIVE_DEFAULT, "Should account searches be case sensitive?");
        ENABLERELOG_PROPERTY = config.get(Configuration.CATEGORY_GENERAL, ConfigValues.ENABLERELOG_NAME, ConfigValues.ENABLERELOG_DEFAULT, "Enables logging in to the account you are already logged in to.");
        syncConfig();
        if (!event.getModMetadata().version.equals("${version}"))//Dev environment needs to use a local list, to avoid issues
            Standards.updateFolder();
        else
            System.out.println("Dev environment detected!");
    }

    public static void init(FMLInitializationEvent event) {
        MR.init();
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        Standards.importAccounts();
    }

    public static void postInit(FMLPostInitializationEvent event) {
        SkinTools.cacheSkins();
    }
}
