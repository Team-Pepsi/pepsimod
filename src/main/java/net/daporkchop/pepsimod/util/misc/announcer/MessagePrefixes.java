/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
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

package net.daporkchop.pepsimod.util.misc.announcer;

import java.util.HashMap;
import java.util.Random;

public class MessagePrefixes {
    private static HashMap<TaskType, MessageMaker> messageMakers = new HashMap<>();

    static {
        messageMakers.put(TaskType.JOIN, new MessageMaker(new String[]{
                "Welcome, %s0",
                "Greetings, %s0",
                "Hi %s0!",
                "%s0 joined the game",
                "Hey there, %s0"
        }));
        messageMakers.put(TaskType.LEAVE, new MessageMaker(new String[]{
                "Bye, %s0!",
                "See ya later, %s0",
                "%s0 left the game"
        }));
        messageMakers.put(TaskType.BREAK, new MessageMaker(new String[]{
                "I just mined %s1 %s0!",
                "I just broke %s1 %s0!"
        }));
        messageMakers.put(TaskType.PLACE, new MessageMaker(new String[]{
                "I just placed %s1 %s0!"
        }));
        messageMakers.put(TaskType.EAT, new MessageMaker(new String[]{
                "I just ate %s1 %s0!"
        }));
        messageMakers.put(TaskType.WALK, new MessageMaker(new String[]{
                "I just walked %s0 meters!",
                "I just walked %s0 blocks!"
        }));
    }

    public static String getMessage(TaskType type, String... args) {
        return "> " + messageMakers.get(type).getMessage(args);
    }

    private static class MessageMaker {
        public String[] messages;

        public MessageMaker(String[] messages) {
            this.messages = messages;
        }

        public String getMessage(String... values) {
            String toReturn = messages[new Random().nextInt(messages.length)];

            for (int i = 0; i < values.length; i++) {
                toReturn = toReturn.replace("%s" + i, values[i]);
            }

            return toReturn;
        }
    }
}
