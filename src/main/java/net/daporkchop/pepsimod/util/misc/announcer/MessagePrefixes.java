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

package net.daporkchop.pepsimod.util.misc.announcer;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MessagePrefixes {
    private static Map<TaskType, MessageMaker> messageMakers = new EnumMap<>(TaskType.class);

    static {
        messageMakers.put(TaskType.JOIN, new MessageMaker(new String[]{
                "Welcome, %1$s",
                "Greetings, %1$s",
                "Hi %1$s!",
                "%1$s joined the game",
                "Hey there, %1$s"
        }));
        messageMakers.put(TaskType.LEAVE, new MessageMaker(new String[]{
                "Bye, %1$s!",
                "See ya later, %1$s",
                "%1$s left the game"
        }));
        messageMakers.put(TaskType.BREAK, new MessageMaker(new String[]{
                "I just mined %2$d %1$s!",
                "I just broke %2$d %1$s!"
        }));
        messageMakers.put(TaskType.PLACE, new MessageMaker(new String[]{
                "I just placed %2$d %1$s!"
        }));
        messageMakers.put(TaskType.EAT, new MessageMaker(new String[]{
                "I just ate %2$d %1$s!"
        }));
        messageMakers.put(TaskType.WALK, new MessageMaker(new String[]{
                "I just walked %1$.2f meters!",
                "I just walked %1$.2f blocks!"
        }));
    }

    public static String getMessage(TaskType type, Object... args) {
        return "> " + messageMakers.get(type).getMessage(args);
    }

    private static class MessageMaker {
        public String[] messages;

        public MessageMaker(String[] messages) {
            this.messages = messages;
        }

        public String getMessage(Object... args) {
            return String.format(this.messages[ThreadLocalRandom.current().nextInt(this.messages.length)], args);
        }
    }
}
