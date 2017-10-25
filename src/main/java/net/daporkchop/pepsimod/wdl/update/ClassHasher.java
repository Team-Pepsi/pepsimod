/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod.wdl.update;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * Hashes classes inside the jar.
 */
public class ClassHasher {
    // http://stackoverflow.com/a/9855338/3991344
    private static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Calculates the hash of the given file.
     *
     * @param relativeTo Name of class to use {@link Class#getResourceAsStream(String)}
     *                   on.
     * @param file       The name of the file (passed to
     *                   {@link Class#getResourceAsStream(String)}).
     * @return A string version of the hash.
     * @throws ClassNotFoundException When relativeTo does not exist.
     * @throws FileNotFoundException  When file cannot be found relative to relativeTo.
     * @throws Exception              When any other exception is raised.
     */
    public static String hash(String relativeTo, String file)
            throws Exception {
        Class<?> clazz = Class.forName(relativeTo);
        MessageDigest digest = MessageDigest.getInstance("MD5");

        try (InputStream stream = clazz.getResourceAsStream(file)) {
            if (stream == null) {
                throw new FileNotFoundException(file + " relative to "
                        + relativeTo);
            }
            try (DigestInputStream digestStream = new DigestInputStream(stream, digest)) {
                while (digestStream.read() != -1) ; //Read entire stream
            }
        }

        return bytesToHex(digest.digest());
    }
}
