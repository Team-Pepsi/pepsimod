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

package net.daporkchop.pepsimod.util.event;

import net.daporkchop.pepsimod.util.event.annotation.Cancellable;
import net.daporkchop.pepsimod.util.event.impl.Event;

/**
 * The status with which a {@link Event} annotated with {@link Cancellable} may complete.
 *
 * @author DaPorkchop_
 */
public enum EventStatus {
    /**
     * Indicates that the event has been handled successfully, and execution should proceed onto the next handler as usual.
     * <p>
     * A handler exiting with {@code null} is the same as if it were to exit with {@link #OK}.
     */
    OK,
    /**
     * Indicates that the event should be cancelled. Execution will proceed onto the next handler as usual, however the exit code will be {@link #CANCEL}.
     */
    CANCEL,
    /**
     * Similar to {@link #CANCEL}, however this will abort the event handling process and later handlers will not be notified.
     */
    ABORT;
}
