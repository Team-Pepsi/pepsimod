/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
