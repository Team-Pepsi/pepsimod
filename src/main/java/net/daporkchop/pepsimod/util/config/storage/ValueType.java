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

package net.daporkchop.pepsimod.util.config.storage;

import net.daporkchop.pepsimod.util.exception.InvalidTypeException;

/**
 * The different types permitted to be used as configuration values.
 *
 * @author DaPorkchop_
 */
public enum ValueType {
    OBJ,
    INT,
    LONG {
        @Override
        public boolean checkCompatible(ValueType other) {
            return other == this || other == INT;
        }
    },
    FLOAT,
    DOUBLE {
        @Override
        public boolean checkCompatible(ValueType other) {
            return other == this || other == FLOAT;
        }
    },
    BOOLEAN;

    /**
     * Checks whether a type is compatible with this type.
     * <p>
     * Compatibility means that the value in the configuration is of this type, but the user is trying to read it as the other type.
     *
     * @param other the other type
     * @return whether or not the types are compatible
     */
    public boolean checkCompatible(ValueType other) {
        return other == this;
    }

    /**
     * Asserts that a type is compatible with this type.
     * <p>
     * Compatibility means that the value in the configuration is of this type, but the user is trying to read it as the other type.
     *
     * @param other the other type
     * @throws InvalidTypeException if the types are not compatible
     */
    public void assertCompatible(ValueType other) throws InvalidTypeException {
        if (!this.checkCompatible(other)) {
            throw new InvalidTypeException(other.name(), this.name());
        }
    }
}
