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

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Handles giving players capes.
 */
public class CapeHandler {
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Map of player names to the skins to give the players.
     * <p>
     * You can convert a player name to a unique ID and vice versa using
     * <a href="https://namemc.com/">namemc.com</a>.
     */
    private static final Map<UUID, ResourceLocation> capes = new HashMap<>();

    private static final Set<EntityPlayer> handledPlayers = new HashSet<>();
    /**
     * Number of times a player's cape has failed; if they have failed too many times skip them.
     */
    private static final Map<EntityPlayer, Integer> playerFailures = new HashMap<>();
    /**
     * Number of times the a player can fail to have a cape set up before they are skipped..
     */
    private static final int MAX_PLAYER_FAILURES = 40;
    /**
     * Number of times the system can fail to set up capes in general (IE, an
     * exception was thrown during the tick).
     */
    private static final int MAX_TOTAL_FAILURES = 40;
    /**
     * Number of times the cape system has broken in total.
     */
    private static int totalFailures = 0;

    /**
     * Set up the name list.
     */
    static {
        //Pokechu22
        capes.put(UUID.fromString("6c8976e3-99a9-4d8b-a98e-d4c0c09b305b"),
                new ResourceLocation("net/daporkchop/pepsimod/wdl", "textures/cape_dev.png"));
        //Julialy
        capes.put(UUID.fromString("f6c068f1-0738-4b41-bdb2-69d81d2b0f1c"),
                new ResourceLocation("net/daporkchop/pepsimod/wdl", "textures/cape_dev.png"));
        //TODO: find the rest of the needed usernames/uuids and set up capes.
    }

    public static void onWorldTick(List<EntityPlayer> players) {
        if (totalFailures > MAX_TOTAL_FAILURES) {
            return;
        }

        try {
            handledPlayers.retainAll(players);

            for (EntityPlayer player : players) {
                if (handledPlayers.contains(player)) {
                    continue;
                }

                if (player instanceof AbstractClientPlayer) {
                    setupPlayer((AbstractClientPlayer) player);
                }
            }
        } catch (Exception e) {
            LOGGER.warn("[WDL] Failed to tick cape setup", e);
            totalFailures++;

            if (totalFailures > MAX_TOTAL_FAILURES) {
                LOGGER.warn("[WDL] Disabling cape system (too many failures)");
            }
        }
    }

    private static void setupPlayer(AbstractClientPlayer player) {
        try {
            NetworkPlayerInfo info = ReflectionUtils
                    .findAndGetPrivateField(player,
                            AbstractClientPlayer.class,
                            NetworkPlayerInfo.class);

            if (info == null) {
                incrementFailure(player);
                return;
            }

            GameProfile profile = info.getGameProfile();

            if (capes.containsKey(profile.getId())) {
                setPlayerCape(info, capes.get(profile.getId()));
            }

            handledPlayers.add(player);
        } catch (Exception e) {
            LOGGER.warn("[WDL] Failed to perform cape set up for " + player, e);
            incrementFailure(player);
        }
    }

    private static void setPlayerCape(NetworkPlayerInfo info,
                                      ResourceLocation cape) throws Exception {
        @SuppressWarnings("unchecked")
        Map<MinecraftProfileTexture.Type, ResourceLocation> map = ReflectionUtils
                .findAndGetPrivateField(info, Map.class);
        if (!map.containsKey(MinecraftProfileTexture.Type.CAPE)) {
            map.put(MinecraftProfileTexture.Type.CAPE, cape);
        }
    }

    /**
     * Increment the number of times a player has failed to get a cape.
     */
    private static void incrementFailure(EntityPlayer player) {
        if (playerFailures.containsKey(player)) {
            int numFailures = playerFailures.get(player) + 1;
            playerFailures.put(player, numFailures);

            if (numFailures > MAX_PLAYER_FAILURES) {
                handledPlayers.add(player);
                playerFailures.remove(player);
                LOGGER.warn("[WDL] Failed to set up cape for " + player
                        + " too many times (" + numFailures + "); skipping them");
            }
        } else {
            playerFailures.put(player, 1);
        }
    }
}
