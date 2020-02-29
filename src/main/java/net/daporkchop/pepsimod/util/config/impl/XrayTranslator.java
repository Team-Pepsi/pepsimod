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

package net.daporkchop.pepsimod.util.config.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.daporkchop.pepsimod.optimization.BlockID;
import net.daporkchop.pepsimod.util.config.IConfigTranslator;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.stream.StreamSupport;

public class XrayTranslator implements IConfigTranslator {
    public static final XrayTranslator INSTANCE = new XrayTranslator();
    public IntOpenHashSet target_blocks = new IntOpenHashSet();

    private XrayTranslator() {
    }

    public void encode(JsonObject json) {
        JsonArray array = new JsonArray();
        for (int id : this.target_blocks){
            array.add(Block.REGISTRY.getObjectById(id).getRegistryName().toString());
        }
        json.add("targetBlocks_v2", array);
    }

    public void decode(String fieldName, JsonObject json) {
        this.target_blocks.clear();
        StreamSupport.stream(this.getArray(json, "targetBlocks", new JsonArray()).spliterator(), false)
                .mapToInt(JsonElement::getAsInt)
                .forEach(this.target_blocks::add);
        StreamSupport.stream(this.getArray(json, "targetBlocks_v2", new JsonArray()).spliterator(), false)
                .map(JsonElement::getAsString)
                .map(ResourceLocation::new)
                .map(Block.REGISTRY::getObject)
                .map(BlockID.class::cast)
                .mapToInt(BlockID::getBlockId)
                .forEach(this.target_blocks::add);
        this.target_blocks.trim();
    }

    public String name() {
        return "xray";
    }

    public boolean isTargeted(Block block) {
        return this.target_blocks.contains(((BlockID) block).getBlockId());
    }

    public boolean isTargeted(BlockID block) {
        return this.target_blocks.contains(block.getBlockId());
    }
}
