package net.daporkchop.pepsimod.util;

import java.io.Serializable;

public class Friend implements Serializable {
    public final String UUID;
    public String lastKnownName;

    public Friend(String uuid)  {
        this(uuid, "");
    }

    public Friend(String uuid, String name) {
        this.UUID = uuid;
        this.lastKnownName = name;
    }
}
