/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
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

package net.daporkchop.pepsimod;

import lombok.Getter;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.PepsiUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class of pepsimod's Forge mod.
 *
 * @author DaPorkchop_
 */
@Mod(
        modid = PepsiConstants.MOD_ID,
        useMetadata = true
)
public final class Pepsimod implements PepsiConstants {
    @Getter(onMethod_ = {@Mod.InstanceFactory})
    private static final Pepsimod INSTANCE = new Pepsimod();

    private Pepsimod() {
        if (INSTANCE != null) {
            throw new IllegalStateException("pepsimod instance already created!");
        }
    }

    @Mod.EventHandler
    public void construction(FMLConstructionEvent event) {
        PepsiConstants.setMC(Minecraft.getMinecraft());

        ModContainer container = FMLCommonHandler.instance().findContainerFor(this);
        PepsiConstants.setVersion(container.getVersion());
        if ("${version}".equals(VERSION)) {
            PepsiConstants.setVersion("dev");
            PepsiConstants.setVersionFull("dev-" + MinecraftForge.MC_VERSION);
        } else {
            Matcher matcher = Pattern.compile("([.0-9]+)-([.0-9]+)").matcher(VERSION);
            if (!matcher.find()) {
                throw new IllegalStateException(String.format("Unparseable version: \"%s\"", VERSION));
            }
            PepsiConstants.setVersion(matcher.group(1));
            String mcVersion = matcher.group(2);
            PepsiConstants.setVersionFull(String.format("%s-%s", VERSION, mcVersion));
            if (!MinecraftForge.MC_VERSION.equals(mcVersion)) {
                throw new IllegalStateException(String.format("pepsimod expected Minecraft %s, but found %s!", MinecraftForge.MC_VERSION, mcVersion));
            }
        }

        log.info("Loading pepsimod %s...\n", VERSION_FULL);

        /*this.data = new DataLoader(
                "https://raw.githubusercontent.com/Team-Pepsi/pepsimod/master/resources/resources.json",
                new File(mc.gameDir, "pepsimod/resources/")
        );*/
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //MinecraftForge.EVENT_BUS.register(new KeyRegistry());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}
