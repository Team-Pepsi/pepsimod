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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Sanity checks, run at compile and run time.
 */
enum SanityCheck {
    TRIPWIRE("net.daporkchop.pepsimod.wdl.sanity.tripwire") {
        /**
         * Tripwire sometimes has the wrong state due to
         * https://github.com/MinecraftForge/MinecraftForge/issues/3924
         */
        @Override
        public void run() throws Exception {
            int wireID = Block.getIdFromBlock(Blocks.TRIPWIRE);
            for (int meta = 0; meta <= 15; meta++) {
                int id = wireID << 4 | meta;
                // Note: Deprecated but supported under forge, and this is
                // what the game actually uses, so we should too for checking
                IBlockState state = Block.BLOCK_STATE_IDS.getByValue(id);
                Block block = (state != null ? state.getBlock() : null);
                LOGGER.trace("id {} ({}) => {} ({})", id, meta, state, block);

                // meta 15 is unused for some reason, ignore it
                if (meta == 15) {
                    continue;
                }
                if (state == null) {
                    throw new Exception("Unexpected null state for meta " + meta + " (" + id + ")");
                }
                if (block != Blocks.TRIPWIRE) {
                    throw new Exception("Unexpected block for meta " + meta + " (" + id + "): " + state);
                }
            }
        }
    },
    VERSION("net.daporkchop.pepsimod.wdl.sanity.version") {
        @Override
        public void run() throws Exception {
            String expected = VersionConstants.getExpectedVersion();
            String actual = VersionConstants.getMinecraftVersion();
            if (expected == null) {
                throw new Exception("Unexpected null expected version!");
            }
            if (actual == null) {
                throw new Exception("Unexpected null running version!");
            }
            if (!expected.equals(actual)) {
                throw new Exception("Unexpected version mismatch - expected to be running on `" + expected + "' but was running on `" + actual + "'!");
            }
        }
    },
    TRANSLATION("net.daporkchop.pepsimod.wdl.sanity.translation") {
        @Override
        public void run() throws Exception {
            if (!I18n.hasKey(this.errorMessage)) {
                // Verbose, because obviously the normal string will not be translated.
                throw new Exception("Translation strings are not present!  All messages will be the untranslated keys (e.g. `net.daporkchop.pepsimod.wdl.sanity.translation').  Please redownload the mod.  If this problem persists, file a bug report.");
            }
        }
    };
    private static final Logger LOGGER = LogManager.getLogger();
    /**
     * Translation key for the general message
     */
    public final String errorMessage;

    SanityCheck(String message) {
        this.errorMessage = message;
    }

    /**
     * Performs this sanity check.
     * Methods are encouraged to log trace information.
     *
     * @throws Exception on failure
     */
    public abstract void run() throws Exception;
}
