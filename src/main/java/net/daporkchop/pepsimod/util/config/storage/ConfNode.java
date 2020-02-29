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

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.daporkchop.pepsimod.util.config.Configuration;
import net.daporkchop.pepsimod.util.exception.InvalidTypeException;

import java.util.StringJoiner;

/**
 * Base for the different configuration node types.
 *
 * @author DaPorkchop_
 */
public interface ConfNode {
    /**
     * @return this node's value's type
     */
    ValueType type();

    /**
     * @return this node's name
     */
    String name();

    /**
     * @return this node's qualified name
     */
    String qualifiedName();

    /**
     * @return this node's path
     */
    String[] path();

    /**
     * @return this node's value
     */
    Configuration objValue();

    /**
     * @return this node's value
     */
    int intValue();

    /**
     * @return this node's value
     */
    long longValue();

    /**
     * @return this node's value
     */
    float floatValue();

    /**
     * @return this node's value
     */
    double doubleValue();

    /**
     * @return this node's value
     */
    boolean booleanValue();

    /**
     * A base implementation of {@link ConfNode}.
     *
     * @author DaPorkchop_
     */
    @Getter
    abstract class AbstractConfNode implements ConfNode {
        protected final String[] path;
        protected       String   qualifiedName;

        public AbstractConfNode(@NonNull String[] path) {
            if (path.length <= 0) {
                throw new IllegalArgumentException("Path length must be at least 1!");
            } else {
                this.path = path;
            }
        }

        @Override
        public String name() {
            return this.path[this.path.length - 1];
        }

        @Override
        public String qualifiedName() {
            String qualifiedName = this.qualifiedName;
            if (qualifiedName == null) {
                StringJoiner joiner = new StringJoiner(".");
                for (String s : this.path) {
                    joiner.add(s);
                }
                qualifiedName = this.qualifiedName = joiner.toString();
            }
            return qualifiedName;
        }

        @Override
        public Configuration objValue() {
            throw new InvalidTypeException(ValueType.OBJ, this.type());
        }

        @Override
        public int intValue() {
            throw new InvalidTypeException(ValueType.INT, this.type());
        }

        @Override
        public long longValue() {
            throw new InvalidTypeException(ValueType.LONG, this.type());
        }

        @Override
        public float floatValue() {
            throw new InvalidTypeException(ValueType.FLOAT, this.type());
        }

        @Override
        public double doubleValue() {
            throw new InvalidTypeException(ValueType.DOUBLE, this.type());
        }

        @Override
        public boolean booleanValue() {
            throw new InvalidTypeException(ValueType.BOOLEAN, this.type());
        }
    }

    /**
     * Implementation of {@link ConfNode} for {@link ValueType#OBJ}.
     *
     * @author DaPorkchop_
     */
    final class Obj extends AbstractConfNode {
        protected final Configuration value;

        public Obj(@NonNull String[] path, @NonNull Configuration value) {
            super(path);

            this.value = value;
        }

        @Override
        public ValueType type() {
            return ValueType.OBJ;
        }

        @Override
        public Configuration objValue() {
            return this.value;
        }
    }

    /**
     * Implementation of {@link ConfNode} for {@link ValueType#INT}.
     *
     * @author DaPorkchop_
     */
    final class Int extends AbstractConfNode {
        protected final int value;

        public Int(@NonNull String[] path, int value) {
            super(path);

            this.value = value;
        }

        @Override
        public ValueType type() {
            return ValueType.INT;
        }

        @Override
        public int intValue() {
            return this.value;
        }
    }

    /**
     * Implementation of {@link ConfNode} for {@link ValueType#LONG}.
     *
     * @author DaPorkchop_
     */
    final class Long extends AbstractConfNode {
        protected final long value;

        public Long(@NonNull String[] path, long value) {
            super(path);

            this.value = value;
        }

        @Override
        public ValueType type() {
            return ValueType.LONG;
        }

        @Override
        public long longValue() {
            return this.value;
        }
    }

    /**
     * Implementation of {@link ConfNode} for {@link ValueType#FLOAT}.
     *
     * @author DaPorkchop_
     */
    final class Float extends AbstractConfNode {
        protected final float value;

        public Float(@NonNull String[] path, float value) {
            super(path);

            this.value = value;
        }

        @Override
        public ValueType type() {
            return ValueType.FLOAT;
        }

        @Override
        public float floatValue() {
            return this.value;
        }
    }

    /**
     * Implementation of {@link ConfNode} for {@link ValueType#DOUBLE}.
     *
     * @author DaPorkchop_
     */
    final class Double extends AbstractConfNode {
        protected final double value;

        public Double(@NonNull String[] path, double value) {
            super(path);

            this.value = value;
        }

        @Override
        public ValueType type() {
            return ValueType.DOUBLE;
        }

        @Override
        public double doubleValue() {
            return this.value;
        }
    }

    /**
     * Implementation of {@link ConfNode} for {@link ValueType#BOOLEAN}.
     *
     * @author DaPorkchop_
     */
    final class Boolean extends AbstractConfNode {
        protected final boolean value;

        public Boolean(@NonNull String[] path, boolean value) {
            super(path);

            this.value = value;
        }

        @Override
        public ValueType type() {
            return ValueType.BOOLEAN;
        }

        @Override
        public boolean booleanValue() {
            return this.value;
        }
    }
}
