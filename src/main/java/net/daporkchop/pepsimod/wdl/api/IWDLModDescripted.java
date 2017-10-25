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
 * WDL mod that has additional description.
 */
public interface IWDLModDescripted extends IWDLMod {
    /**
     * Gets the display name for the mod.  Should be translated if the mod
     * supports translation.
     *
     * @return The display name for the mod.
     */
    String getDisplayName();

    /**
     * Get the author of the mod.
     * If there is more than 1, return the main author here, and use
     * {@link #getAuthors()} to specify the rest.
     * <p>
     * If there is no main author (IE, 2 people doing the same amount
     * of work), return null and specify using {@link #getAuthors()}.
     *
     * @return The main author of the mod.
     */
    String getMainAuthor();

    /**
     * The rest of the authors of the mod.  If the main author
     * is included in this list, they will not be included here
     * (they will still be displayed in the main author slot).
     *
     * @return The remaining authors, or null.
     */
    String[] getAuthors();

    /**
     * A URL for more information about the extension, EG a github link
     * or a minecraftforums link.
     *
     * @return A URL, or null if there is no URL.
     */
    String getURL();

    /**
     * A detailed description of the extension.  Color codes (§) and
     * <code>\n</code> are allowed.
     *
     * @return A description, or null.
     */
    String getDescription();
}
