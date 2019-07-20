/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2016-2019 DaPorkchop_
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

package net.daporkchop.pepsimod.util.render.color;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

/**
 * {@link RenderColor} implementations for each of the vanilla text colors.
 *
 * @author DaPorkchop_
 * @see TextFormatting
 */
@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public enum ChatColor implements RenderColor {
    BLACK(TextFormatting.BLACK, 0x000000),
    DARK_BLUE(TextFormatting.DARK_BLUE, 0x0000AA),
    DARK_GREEN(TextFormatting.DARK_GREEN, 0x00AA00),
    DARK_AQUA(TextFormatting.DARK_AQUA, 0x00AAAA),
    DARK_RED(TextFormatting.DARK_RED, 0xAA0000),
    DARK_PURPLE(TextFormatting.DARK_PURPLE, 0xAA00AA),
    GOLD(TextFormatting.GOLD, 0xFFAA00),
    GRAY(TextFormatting.GRAY, 0xAAAAAA),
    DARK_GRAY(TextFormatting.DARK_GRAY, 0x555555),
    BLUE(TextFormatting.BLUE, 0x5555FF),
    GREEN(TextFormatting.GREEN, 0x55FF55),
    AQUA(TextFormatting.AQUA, 0x55FFFF),
    RED(TextFormatting.RED, 0xFF5555),
    LIGHT_PURPLE(TextFormatting.LIGHT_PURPLE, 0xFF55FF),
    YELLOW(TextFormatting.YELLOW, 0xFFFF55),
    WHITE(TextFormatting.WHITE, 0xFFFFFF);

    public static final ChatColor[] VALUES = values();

    /**
     * Gets the {@link ChatColor} that corresponds to the given formatting code.
     *
     * @param formatting the formatting code
     * @return the {@link ChatColor} that corresponds to the given formatting code
     */
    public static ChatColor fromMc(@NonNull TextFormatting formatting) {
        switch (formatting) {
            case BLACK:
                return BLACK;
            case DARK_BLUE:
                return DARK_BLUE;
            case DARK_GREEN:
                return DARK_GREEN;
            case DARK_AQUA:
                return DARK_AQUA;
            case DARK_RED:
                return DARK_RED;
            case DARK_PURPLE:
                return DARK_PURPLE;
            case GOLD:
                return GOLD;
            case GRAY:
                return GRAY;
            case DARK_GRAY:
                return DARK_GRAY;
            case BLUE:
                return BLUE;
            case GREEN:
                return GREEN;
            case AQUA:
                return AQUA;
            case RED:
                return RED;
            case LIGHT_PURPLE:
                return LIGHT_PURPLE;
            case YELLOW:
                return YELLOW;
            case WHITE:
                return WHITE;
        }
        throw new IllegalArgumentException(String.format("Invalid formatting: %s", formatting));
    }

    /**
     * Gets the {@link ChatColor} that corresponds to the given formatting code.
     *
     * @param code the formatting code
     * @return the {@link ChatColor} that corresponds to the given formatting code
     */
    public static ChatColor fromMc(@NonNull String code) {
        switch (code) {
            case "0":
                return BLACK;
            case "1":
                return DARK_BLUE;
            case "2":
                return DARK_GREEN;
            case "3":
                return DARK_AQUA;
            case "4":
                return DARK_RED;
            case "5":
                return DARK_PURPLE;
            case "6":
                return GOLD;
            case "7":
                return GRAY;
            case "8":
                return DARK_GRAY;
            case "9":
                return BLUE;
            case "a":
                return GREEN;
            case "b":
                return AQUA;
            case "c":
                return RED;
            case "d":
                return LIGHT_PURPLE;
            case "e":
                return YELLOW;
            case "f":
                return WHITE;
        }
        throw new IllegalArgumentException(String.format("Invalid formatting: \"%s\"", code));
    }

    /**
     * Gets the {@link ChatColor} that corresponds to the given formatting code.
     *
     * @param code the formatting code
     * @return the {@link ChatColor} that corresponds to the given formatting code
     */
    public static ChatColor fromMc(char code) {
        switch (code) {
            case '0':
                return BLACK;
            case '1':
                return DARK_BLUE;
            case '2':
                return DARK_GREEN;
            case '3':
                return DARK_AQUA;
            case '4':
                return DARK_RED;
            case '5':
                return DARK_PURPLE;
            case '6':
                return GOLD;
            case '7':
                return GRAY;
            case '8':
                return DARK_GRAY;
            case '9':
                return BLUE;
            case 'a':
                return GREEN;
            case 'b':
                return AQUA;
            case 'c':
                return RED;
            case 'd':
                return LIGHT_PURPLE;
            case 'e':
                return YELLOW;
            case 'f':
                return WHITE;
        }
        throw new IllegalArgumentException(String.format("Invalid formatting: %c", code));
    }

    @NonNull
    protected final TextFormatting mcColor;
    protected final int            rgb;

    @Override
    public void bind() {
        GlStateManager.color(this.fR(), this.fG(), this.fB(), 1.0f);
    }

    @Override
    public int argb() {
        return this.rgb | 0xFF000000;
    }

    @Override
    public int iA() {
        return 0xFF;
    }

    @Override
    public int iR() {
        return (this.rgb >>> 16) & 0xFF;
    }

    @Override
    public int iG() {
        return (this.rgb >>> 8) & 0xFF;
    }

    @Override
    public int iB() {
        return this.rgb & 0xFF;
    }

    @Override
    public float fA() {
        return 1.0f;
    }

    @Override
    public float fR() {
        return ((this.rgb >>> 16) & 0xFF) * 255.0f;
    }

    @Override
    public float fG() {
        return ((this.rgb >>> 8) & 0xFF) * 255.0f;
    }

    @Override
    public float fB() {
        return (this.rgb & 0xFF) * 255.0f;
    }
}
