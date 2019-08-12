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

package net.daporkchop.pepsimod.util.exception;

import lombok.NonNull;
import net.daporkchop.pepsimod.util.config.storage.ConfNode;
import net.daporkchop.pepsimod.util.config.storage.ValueType;

/**
 * Thrown when a config value is accessed with an accessor for the wrong type.
 *
 * @author DaPorkchop_
 */
public final class InvalidTypeException extends PepsimodException {
    public InvalidTypeException(String message) {
        super(message);
    }

    public InvalidTypeException(@NonNull ValueType expected) {
        this(String.format("Invalid type! Expected: %s", expected.name()));
    }

    public InvalidTypeException(String found, String expected) {
        super(String.format("Invalid type! Found: %s, expected: %s", found, expected));
    }

    public InvalidTypeException(@NonNull ValueType found, @NonNull ValueType expected) {
        this(found.name(), expected.name());
    }
}
