/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
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
