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

package net.daporkchop.pepsimod.wdl.api;


/**
 * Marker interface for an extension that listens to WDL's events.
 * <br/>
 * To use this, it MUST be added to the list of WDL mods via
 * {@link WDLApi#addWDLMod(IWDLMod)}. Extensions are not loaded automatically.
 * <br/>
 * For this to actually be useful, implement one of the subinterfaces.
 * <br/>
 * It is recommended to implement {@link IWDLModDescripted} to provide
 * additional information on the extension, but that is not required.
 */
public interface IWDLMod {
    /**
     * Checks whether this extension is compatible with the given version of WDL
     * and the rest of the environment (e.g. other mods that it needs) are
     * correct. The extension will not be loaded if the environment isn't set up
     * correctly.
     * <p>
     * This is intended to handle compatibility checks before the game crashes.
     *
     * @param version The version string for WDL, as found with
     *                {@link wdl.VersionConstants#getModVersion()}. It's recommended
     *                that you check against it.
     */
    boolean isValidEnvironment(String version);

    /**
     * Gets info about why the current environment is not valid. Will only be
     * called if {@link #isValidEnvironment(String)} returned false.
     *
     * @param version The version string for WDL.
     * @return A (possibly translated) error message to display to the user
     * about why the environment is not set up correctly. May be null in
     * which case a default message is used. If there is no case where
     * the build environment would be invalid, this method should return
     * null.
     */
    String getEnvironmentErrorMessage(String version);
}
