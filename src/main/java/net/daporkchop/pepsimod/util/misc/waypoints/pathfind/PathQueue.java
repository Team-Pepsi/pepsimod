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

package net.daporkchop.pepsimod.util.misc.waypoints.pathfind;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

public class PathQueue {
    private final PriorityQueue<PathQueue.Entry> queue =
            new PriorityQueue<>((e1, e2) -> {
                return Float.compare(e1.priority, e2.priority);
            });
    public ArrayList<PathPos> cancelledPositions = new ArrayList<>();

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean add(PathPos pos, float priority) {
        return queue.add(new Entry(pos, priority));
    }

    public PathPos[] toArray() {
        PathPos[] array = new PathPos[size()];
        Iterator itr = queue.iterator();

        for (int i = 0; i < size() && itr.hasNext(); i++)
            array[i] = ((Entry) itr.next()).pos;

        return array;
    }

    public int size() {
        return queue.size();
    }

    public PathPos poll() {
        if (cancelledPositions.size() > 0) {
            return cancelledPositions.remove(0);
        }
        return queue.poll().pos;
    }

    public PathPos get(int index) {
        if (index >= queue.size()) {
            throw new ArrayIndexOutOfBoundsException(String.valueOf(index));
        }

        Iterator<Entry> itr = queue.iterator();
        index--;
        for (int i = 0; i < index && itr.hasNext(); i++) {
        }
        return itr.next().pos;
    }

    public void removePoint(PathPos pos) {
        Iterator<Entry> itr = queue.iterator();
        while (itr.hasNext()) {
            if (pos.equals(itr.next())) {
                itr.remove();
                return;
            }
        }
    }

    private class Entry {
        private PathPos pos;
        private float priority;

        public Entry(PathPos pos, float priority) {
            this.pos = pos;
            this.priority = priority;
        }
    }
}
