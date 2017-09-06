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

package net.daporkchop.pepsimod.totally.not.skidded;

public final class GeometryMasks {
    public static final class Quad {
        public static final int DOWN = 0x01;
        public static final int UP = 0x02;
        public static final int NORTH = 0x04;
        public static final int SOUTH = 0x08;
        public static final int WEST = 0x10;
        public static final int EAST = 0x20;
        public static final int ALL = DOWN | UP | NORTH | SOUTH | WEST | EAST;
    }

    public static final class Line {
        public static final int DOWN_WEST = 0x11;
        public static final int UP_WEST = 0x12;
        public static final int DOWN_EAST = 0x21;
        public static final int UP_EAST = 0x22;
        public static final int DOWN_NORTH = 0x05;
        public static final int UP_NORTH = 0x06;
        public static final int DOWN_SOUTH = 0x09;
        public static final int UP_SOUTH = 0x0A;
        public static final int NORTH_WEST = 0x14;
        public static final int NORTH_EAST = 0x24;
        public static final int SOUTH_WEST = 0x18;
        public static final int SOUTH_EAST = 0x28;
        public static final int ALL = DOWN_WEST | UP_WEST | DOWN_EAST | UP_EAST | DOWN_NORTH | UP_NORTH | DOWN_SOUTH | UP_SOUTH | NORTH_WEST | NORTH_EAST | SOUTH_WEST | SOUTH_EAST;
    }
}
