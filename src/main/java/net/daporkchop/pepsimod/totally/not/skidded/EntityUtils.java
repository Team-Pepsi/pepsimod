package net.daporkchop.pepsimod.totally.not.skidded;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.util.Friends;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.TargetBone;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public class EntityUtils {
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
        if (PepsiMod.INSTANCE.mc.player.getDistanceToEntity(en) > settings.getRange()) {
            return false;
        }

        // entities outside the FOV
        if (settings.getFOV() < 360F && RotationUtils.getAngleToClientRotation(PepsiUtils.adjustVectorForBone(en.getEntityBoundingBox().getCenter(), en, settings.getTargetBone())) > settings.getFOV() / 2F) {
            return false;
        }

        // entities behind walls
        if (!settings.targetBehindWalls() && !PepsiUtils.canEntityBeSeen(en, PepsiMod.INSTANCE.mc.player, settings.getTargetBone())) {
            return false;
        }

        // friends
        if (!settings.targetFriends() && Friends.isFriend(en.getUniqueID().toString())) {
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
            } else if (!settings.targetInvisiblePlayers())
                if (en.isInvisible()) {
                    return false;
                }

            // team players
            if (settings.targetTeams() && !checkName(
                    en.getDisplayName().getFormattedText(),
                    settings.getTeamColors())) {
                return false;
            }

            // the user
            if (en == PepsiMod.INSTANCE.mc.player) {
                return false;
            }

            // Freecam entity
            if (en.getName()
                    .equals(PepsiMod.INSTANCE.mc.player.getName())) {
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
        for (int i = 0; i < 16; i++)
            if (name.contains("§" + colors[i])) {
                hasKnownColor = true;
                if (teamColors[i]) {
                    return true;
                }
            }

        // no known color => white
        return !hasKnownColor && teamColors[15];
    }

    public static ArrayList<Entity> getValidEntities(TargetSettings settings) {
        ArrayList<Entity> validEntities = new ArrayList<>();

        for (Entity entity : PepsiMod.INSTANCE.mc.world.loadedEntityList) {
            if (isCorrectEntity(entity, settings))
                validEntities.add(entity);

            if (validEntities.size() >= 64)
                break;
        }

        return validEntities;
    }

    public static Entity getClosestEntity(TargetSettings settings) {
        Entity closestEntity = null;

        for (Entity entity : PepsiMod.INSTANCE.mc.world.loadedEntityList)
            if (isCorrectEntity(entity, settings)
                    && (closestEntity == null || PepsiMod.INSTANCE.mc.player
                    .getDistanceToEntity(entity) < PepsiMod.INSTANCE.mc.player
                    .getDistanceToEntity(closestEntity)))
                closestEntity = entity;

        return closestEntity;
    }

    public static Entity getBestEntityToAttack(TargetSettings settings) {
        Entity bestEntity = null;
        float bestAngle = Float.POSITIVE_INFINITY;

        for (Entity entity : PepsiMod.INSTANCE.mc.world.loadedEntityList) {
            if (!isCorrectEntity(entity, settings))
                continue;

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

        for (Entity entity : PepsiMod.INSTANCE.mc.world.loadedEntityList)
            if (isCorrectEntity(entity, settings) && entity != otherEntity
                    && (closestEnemy == null || PepsiMod.INSTANCE.mc.player
                    .getDistanceToEntity(entity) < PepsiMod.INSTANCE.mc.player
                    .getDistanceToEntity(closestEnemy)))
                closestEnemy = entity;

        return closestEnemy;
    }

    public static Entity getClosestEntityWithName(String name,
                                                  TargetSettings settings) {
        Entity closestEntity = null;

        for (Entity entity : PepsiMod.INSTANCE.mc.world.loadedEntityList) {
            if (!isCorrectEntity(entity, settings))
                continue;
            if (!entity.getName().equalsIgnoreCase(name))
                continue;

            if (closestEntity == null || PepsiMod.INSTANCE.mc.player
                    .getDistanceSqToEntity(entity) < PepsiMod.INSTANCE.mc.player
                    .getDistanceSqToEntity(closestEntity))
                closestEntity = entity;
        }

        return closestEntity;
    }

    public static class TargetSettings {
        public static final boolean[] team_colors = new boolean[]{true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};

        public boolean targetFriends() {
            return PepsiMod.INSTANCE.targetSettings.friends;
        }

        public boolean targetBehindWalls() {
            return PepsiMod.INSTANCE.targetSettings.through_walls;
        }

        public float getRange() {
            return PepsiMod.INSTANCE.targetSettings.reach;
        }

        public float getFOV() {
            return PepsiMod.INSTANCE.targetSettings.fov;
        }

        public boolean targetPlayers() {
            return PepsiMod.INSTANCE.targetSettings.players;
        }

        public boolean targetAnimals() {
            return PepsiMod.INSTANCE.targetSettings.animals;
        }

        public boolean targetMonsters() {
            return PepsiMod.INSTANCE.targetSettings.monsters;
        }

        public boolean targetGolems() {
            return PepsiMod.INSTANCE.targetSettings.golems;
        }

        public boolean targetSleepingPlayers() {
            return PepsiMod.INSTANCE.targetSettings.sleeping;
        }

        public boolean targetInvisiblePlayers() {
            return PepsiMod.INSTANCE.targetSettings.players && PepsiMod.INSTANCE.targetSettings.invisible;
        }

        public boolean targetInvisibleMobs() {
            return PepsiMod.INSTANCE.targetSettings.monsters && PepsiMod.INSTANCE.targetSettings.invisible;
        }

        public boolean targetTeams() {
            return PepsiMod.INSTANCE.targetSettings.teams;
        }

        public boolean[] getTeamColors() {
            return team_colors;
        }

        public TargetBone getTargetBone() {
            return PepsiMod.INSTANCE.targetSettings.targetBone;
        }
    }
}
