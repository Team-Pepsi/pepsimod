/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
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

package net.daporkchop.pepsimod.the.wurst.pkg.name;

import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.config.impl.FriendsTranslator;
import net.daporkchop.pepsimod.util.config.impl.TargettingTranslator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class EntityUtils extends PepsiConstants {
    public static final TargetSettings DEFAULT_SETTINGS = new TargetSettings();
    public static final String[] colors = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static boolean isCorrectEntity(Entity en, TargetSettings settings) {
        // non-entities
        if (en == null) {
            return false;
        }

        // dead entities
        if (en instanceof EntityLivingBase && (((EntityLivingBase) en).isDead || ((EntityLivingBase) en).getHealth() <= 0)) {
            return false;
        }

        // entities outside the range
        if (mc.player.getDistance(en) > settings.getRange()) {
            return false;
        }

        // entities outside the FOV
        if (settings.getFOV() < 360F && RotationUtils.getAngleToClientRotation(PepsiUtils.adjustVectorForBone(en.getEntityBoundingBox().getCenter(), en, settings.getTargetBone())) > settings.getFOV() / 2F) {
            return false;
        }

        // entities behind walls
        if (!settings.targetBehindWalls() && !PepsiUtils.canEntityBeSeen(en, mc.player, settings.getTargetBone())) {
            return false;
        }

        // friends
        if (!settings.targetFriends() && FriendsTranslator.INSTANCE.friends.contains(en.getUniqueID().toString())) {
            return false;
        }

        // players
        if (en instanceof EntityPlayer) {
            // normal players
            if (!settings.targetPlayers()) {
                if (!((EntityPlayer) en).isPlayerSleeping() && !en.isInvisible()) {
                    return false;
                }

                // sleeping players
            } else if (!settings.targetSleepingPlayers()) {
                if (((EntityPlayer) en).isPlayerSleeping()) {
                    return false;
                }

                // invisible players
            } else if (!settings.targetInvisiblePlayers()) {
                if (en.isInvisible()) {
                    return false;
                }
            }

            // team players
            if (settings.targetTeams() && !checkName(
                    en.getDisplayName().getFormattedText(),
                    settings.getTeamColors())) {
                return false;
            }

            // the user
            if (en == mc.player) {
                return false;
            }

            // Freecam entity
            if (en.getName()
                  .equals(mc.player.getName())) {
                return false;
            }

            // mobs
        } else if (en instanceof EntityLiving) {
            // invisible mobs
            if (en.isInvisible()) {
                if (!settings.targetInvisibleMobs()) {
                    return false;
                }

                // animals
            } else if (en instanceof EntityAgeable
                    || en instanceof EntityAmbientCreature
                    || en instanceof EntityWaterMob) {
                if (!settings.targetAnimals()) {
                    return false;
                }

                // monsters
            } else if (en instanceof EntityMob || en instanceof EntitySlime
                    || en instanceof EntityFlying) {
                if (!settings.targetMonsters()) {
                    return false;
                }

                // golems
            } else if (en instanceof EntityGolem) {
                if (!settings.targetGolems()) {
                    return false;
                }

                // other mobs
            } else {
                return false;
            }

            // team mobs
            if (settings.targetTeams() && en.hasCustomName()
                    && !checkName(en.getCustomNameTag(),
                    settings.getTeamColors())) {
                return false;
            }

            // other entities
        } else {
            return false;
        }

        return true;
    }

    private static boolean checkName(String name, boolean[] teamColors) {
        // check colors
        boolean hasKnownColor = false;
        for (int i = 0; i < 16; i++) {
            if (name.contains('\u00A7' + colors[i])) {
                hasKnownColor = true;
                if (teamColors[i]) {
                    return true;
                }
            }
        }

        // no known color => white
        return !hasKnownColor && teamColors[15];
    }

    public static ArrayList<Entity> getValidEntities(TargetSettings settings) {
        ArrayList<Entity> validEntities = new ArrayList<>();

        for (Entity entity : mc.world.loadedEntityList) {
            if (isCorrectEntity(entity, settings)) {
                validEntities.add(entity);
            }

            if (validEntities.size() >= 64) {
                break;
            }
        }

        return validEntities;
    }

    public static Entity getClosestEntity(TargetSettings settings) {
        Entity closestEntity = null;

        for (Entity entity : mc.world.loadedEntityList) {
            if (isCorrectEntity(entity, settings)
                    && (closestEntity == null || mc.player
                    .getDistance(entity) < mc.player
                    .getDistance(closestEntity))) {
                closestEntity = entity;
            }
        }

        return closestEntity;
    }

    public static Entity getBestEntityToAttack(TargetSettings settings) {
        Entity bestEntity = null;
        float bestAngle = Float.POSITIVE_INFINITY;

        for (Entity entity : mc.world.loadedEntityList) {
            if (!isCorrectEntity(entity, settings)) {
                continue;
            }

            float angle = RotationUtils.getAngleToServerRotation(PepsiUtils.adjustVectorForBone(entity.getEntityBoundingBox().getCenter(), entity, settings.getTargetBone()));

            if (angle < bestAngle) {
                bestEntity = entity;
                bestAngle = angle;
            }
        }

        return bestEntity;
    }

    public static Entity getClosestEntityOtherThan(Entity otherEntity,
                                                   TargetSettings settings) {
        Entity closestEnemy = null;

        for (Entity entity : mc.world.loadedEntityList) {
            if (isCorrectEntity(entity, settings) && entity != otherEntity
                    && (closestEnemy == null || mc.player
                    .getDistance(entity) < mc.player
                    .getDistance(closestEnemy))) {
                closestEnemy = entity;
            }
        }

        return closestEnemy;
    }

    public static Entity getClosestEntityWithName(String name,
                                                  TargetSettings settings) {
        Entity closestEntity = null;

        for (Entity entity : mc.world.loadedEntityList) {
            if (!isCorrectEntity(entity, settings)) {
                continue;
            }
            if (!entity.getName().equalsIgnoreCase(name)) {
                continue;
            }

            if (closestEntity == null || mc.player
                    .getDistanceSq(entity) < mc.player
                    .getDistanceSq(closestEntity)) {
                closestEntity = entity;
            }
        }

        return closestEntity;
    }

    public static class TargetSettings {
        public static final boolean[] team_colors = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};

        public boolean targetFriends() {
            return TargettingTranslator.INSTANCE.friends;
        }

        public boolean targetBehindWalls() {
            return TargettingTranslator.INSTANCE.through_walls;
        }

        public float getRange() {
            return TargettingTranslator.INSTANCE.reach;
        }

        public float getFOV() {
            return TargettingTranslator.INSTANCE.fov;
        }

        public boolean targetPlayers() {
            return TargettingTranslator.INSTANCE.players;
        }

        public boolean targetAnimals() {
            return TargettingTranslator.INSTANCE.animals;
        }

        public boolean targetMonsters() {
            return TargettingTranslator.INSTANCE.monsters;
        }

        public boolean targetGolems() {
            return TargettingTranslator.INSTANCE.golems;
        }

        public boolean targetSleepingPlayers() {
            return TargettingTranslator.INSTANCE.sleeping;
        }

        public boolean targetInvisiblePlayers() {
            return TargettingTranslator.INSTANCE.players && TargettingTranslator.INSTANCE.invisible;
        }

        public boolean targetInvisibleMobs() {
            return TargettingTranslator.INSTANCE.monsters && TargettingTranslator.INSTANCE.invisible;
        }

        public boolean targetTeams() {
            return TargettingTranslator.INSTANCE.teams;
        }

        public boolean[] getTeamColors() {
            return team_colors;
        }

        public TargettingTranslator.TargetBone getTargetBone() {
            return TargettingTranslator.INSTANCE.targetBone;
        }
    }
}
