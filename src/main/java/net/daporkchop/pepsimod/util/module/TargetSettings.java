package net.daporkchop.pepsimod.util.module;

import java.io.Serializable;

public class TargetSettings implements Serializable {
    public boolean players = false;
    public boolean animals = false;
    public boolean monsters = false;
    public boolean golems = false;
    public boolean sleeping = false;
    public boolean invisible = false;
    public boolean teams = false;
    public boolean friends = false;
    public boolean through_walls = false;
    public boolean use_cooldown = false;
    public boolean silent = false;
    public boolean rotate = false;
    public TargetBone targetBone = TargetBone.FEET;
    public float fov = 360f;
    public float reach = 4.25f;
    public int delay = 20;
}
