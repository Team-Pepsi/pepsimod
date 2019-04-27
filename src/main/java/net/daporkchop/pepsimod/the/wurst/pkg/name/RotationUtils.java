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
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RotationUtils extends PepsiConstants {
    private static boolean fakeRotation;
    private static float serverYaw;
    private static float serverPitch;

    public static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX,
                mc.player.posY + mc.player.getEyeHeight(),
                mc.player.posZ);
    }

    public static Vec3d getClientLookVec() {
        float f = MathHelper.cos(-mc.player.rotationYaw * 0.017453292F
                - (float) Math.PI);
        float f1 = MathHelper.sin(-mc.player.rotationYaw * 0.017453292F
                - (float) Math.PI);
        float f2 =
                -MathHelper.cos(-mc.player.rotationPitch * 0.017453292F);
        float f3 =
                MathHelper.sin(-mc.player.rotationPitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    public static Vec3d getServerLookVec() {
        float f = MathHelper.cos(-serverYaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-serverYaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-serverPitch * 0.017453292F);
        float f3 = MathHelper.sin(-serverPitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    private static float[] getNeededRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();

        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new float[]{MathHelper.wrapDegrees(yaw), MathHelper.wrapDegrees(pitch)};
    }

    private static float[] getNeededRotations2(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();

        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

        return new float[]{
                mc.player.rotationYaw
                        + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw),
                mc.player.rotationPitch + MathHelper
                        .wrapDegrees(pitch - mc.player.rotationPitch)};
    }

    public static float limitAngleChange(float current, float intended,
                                         float maxChange) {
        float change = MathHelper.wrapDegrees(intended - current);

        change = MathHelper.clamp(change, -maxChange, maxChange);

        return MathHelper.wrapDegrees(current + change);
    }

    public static boolean faceVectorPacket(Vec3d vec) {
        // use fake rotation in next packet
        fakeRotation = true;

        float[] rotations = getNeededRotations(vec);

        serverYaw = rotations[0];
        serverPitch = MathHelper.normalizeAngle((int) rotations[1], 360);

        return Math.abs(serverYaw - rotations[0]) < 1F;
    }

    public static void faceVectorPacketInstant(Vec3d vec) {
        float[] rotations = getNeededRotations2(vec);

        mc.getConnection().sendPacket(new CPacketPlayer.Rotation(rotations[0],
                MathHelper.normalizeAngle((int) rotations[1], 360), mc.player.onGround));
    }

    public static boolean faceVectorClient(Vec3d vec) {
        float[] rotations = getNeededRotations(vec);

        float oldYaw = mc.player.prevRotationYaw;
        float oldPitch = mc.player.prevRotationPitch;

        mc.player.rotationYaw = rotations[0];
        mc.player.rotationPitch = MathHelper.normalizeAngle((int) rotations[1], 360);

        return Math.abs(oldYaw - rotations[0])
                + Math.abs(oldPitch - rotations[1]) < 1F;
    }


    public static boolean faceEntityClient(Entity entity) {
        // get position & rotation
        Vec3d eyesPos = getEyesPos();
        Vec3d lookVec = getServerLookVec();

        // try to face center of boundingBox
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        if (faceVectorClient(PepsiUtils.adjustVectorForBone(bb.getCenter(), entity, EntityUtils.DEFAULT_SETTINGS.getTargetBone()))) {
            return true;
        }

        // if not facing center, check if facing anything in boundingBox
        return bb.calculateIntercept(eyesPos,
                eyesPos.add(lookVec.scale(6))) != null;
    }

    public static boolean faceEntityPacket(Entity entity) {
        // get position & rotation
        Vec3d eyesPos = getEyesPos();
        Vec3d lookVec = getServerLookVec();

        // try to face center of boundingBox
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        if (faceVectorPacket(PepsiUtils.adjustVectorForBone(bb.getCenter(), entity, EntityUtils.DEFAULT_SETTINGS.getTargetBone()))) {
            return true;
        }

        // if not facing center, check if facing anything in boundingBox
        return bb.calculateIntercept(eyesPos,
                eyesPos.add(lookVec.scale(6))) != null;
    }

    public static boolean faceVectorForWalking(Vec3d vec) {
        float[] rotations = getNeededRotations(vec);

        float oldYaw = mc.player.prevRotationYaw;

        mc.player.rotationYaw = MathHelper.normalizeAngle((int) rotations[0], 360);

        return Math.abs(oldYaw - rotations[0]) < 1F;
    }

    public static float getAngleToClientRotation(Vec3d vec) {
        float[] needed = getNeededRotations(vec);

        float diffYaw =
                MathHelper.wrapDegrees(mc.player.rotationYaw) - needed[0];
        float diffPitch =
                MathHelper.wrapDegrees(mc.player.rotationPitch) - needed[1];

        float angle =
                (float) Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);

        return angle;
    }

    public static float getHorizontalAngleToClientRotation(Vec3d vec) {
        float[] needed = getNeededRotations(vec);

        float angle =
                MathHelper.wrapDegrees(mc.player.rotationYaw) - needed[0];

        return angle;
    }

    public static float getAngleToServerRotation(Vec3d vec) {
        float[] needed = getNeededRotations(vec);

        float diffYaw = serverYaw - needed[0];
        float diffPitch = serverPitch - needed[1];

        float angle =
                (float) Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);

        return angle;
    }

    public static void updateServerRotation() {
        // disable fake rotation in next packet unless manually enabled again
        if (fakeRotation) {
            fakeRotation = false;
            return;
        }

        // slowly synchronize server rotation with client
        serverYaw = limitAngleChange(serverYaw, mc.player.rotationYaw, 30);
        serverPitch = mc.player.rotationPitch;
    }

    public static float getServerYaw() {
        return serverYaw;
    }

    public static float getServerPitch() {
        return serverPitch;
    }
}
