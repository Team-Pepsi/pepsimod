package net.daporkchop.pepsimod.util;

import java.io.Serializable;

public class NoWeatherSettings implements Serializable {
    public boolean disableRain = false;
    public boolean changeTime = false;
    public float time = 0;
    public boolean changeMoon = false;
    public int moonPhase = 0;
}