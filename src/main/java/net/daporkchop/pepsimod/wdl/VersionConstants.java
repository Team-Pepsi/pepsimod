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

package net.daporkchop.pepsimod.wdl;

import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsSharedConstants;

/**
 * Contains constants that vary between versions of the mod.
 */
public class VersionConstants {
    /**
     * Gets the current protocol version number.
     *
     * @return A <a href="http://wiki.vg/Protocol_version_numbers">protocol
     * version number</a>, eg <samp>316</samp>.
     */
    public static int getProtocolVersion() {
        return RealmsSharedConstants.NETWORK_PROTOCOL_VERSION;
    }

    /**
     * Gets the name of the version currently being run, which may change at
     * runtime.
     *
     * @return A version string, eg <samp>1.11</samp>.
     */
    public static String getMinecraftVersion() {
        return RealmsSharedConstants.VERSION_STRING;
    }

    /**
     * Gets the current data version, as needed for the <code>DataVersion</code>
     * tag. Note that unfortunately neither wiki gives a list of these numbers
     * at the moment.
     *
     * @return A version number, eg <samp>819</samp>.
     */
    public static int getDataVersion() {
        // As per AnvilChunkLoader
        return 1343;
    }

    /**
     * Gets the version this build is expected to run on.
     *
     * @return A version string, eg <samp>1.11</samp>.
     */
    public static String getExpectedVersion() {
        return "1.12.2";
    }

    /**
     * Gets version info similar to the info in F3.
     *
     * @return A version info string, eg <samp>Minecraft 1.9 (1.9/vanilla)</samp>
     */
    public static String getMinecraftVersionInfo() {
        String version = getMinecraftVersion();
        // Gets the launched version (appears in F3)
        String launchedVersion = Minecraft.getMinecraft().getVersion();
        String brand = ClientBrandRetriever.getClientModName();
        String versionType = Minecraft.getMinecraft().getVersionType();

        return String.format("Minecraft %s (%s/%s/%s)", version,
                launchedVersion, brand, versionType);
    }

    /**
     * Gets the current version of the mod.
     *
     * @return A version string, eg <samp>4.0.0.0</samp>
     */
    public static String getModVersion() {
        // TODO: Automatically generate this somehow
        return "4.0.1.3";
    }
}