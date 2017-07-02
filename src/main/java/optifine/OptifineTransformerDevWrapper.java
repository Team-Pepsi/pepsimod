package optifine;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ported from
 * https://github.com/octarine-noise/BetterFoliage/blob/abf037d8a9640594a76b9f2524885da6f440bd41/src/main/kotlin/optifine/OptifineTweakerDevWrapper.kt
 * <p>
 * PITA to understand kotlin tbh
 */
public class OptifineTransformerDevWrapper implements IClassTransformer {
    public ZipFile ofZip = null;

    {
        System.out.println("finding zip");
        try {
            URL[] urls = ((URLClassLoader) this.getClass().getClassLoader()).getURLs();
            LOOP:
            for (URL url : urls) {
                try {
                    ZipFile file = new ZipFile(new File(url.toURI().getPath()));
                    if (file.getEntry("optifine/OptiFineClassTransformer.class") != null) {
                        ofZip = file;
                        break LOOP;
                    }
                } catch (FileNotFoundException e) {
                    //so what
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(293742);
        }
        System.out.println("done!");
        System.out.println(ofZip == null ? "zip is null" : "zip is not null");
    }

    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        System.out.println("transform");
        ZipEntry entry = ofZip.getEntry(name.replace(".", "/") + ".class");
        if (entry == null) {
            return basicClass;
        } else {
            try {
                return IOUtils.toByteArray(ofZip.getInputStream(entry));
            } catch (IOException e) {
                e.printStackTrace();
                return basicClass;
            }
        }
    }
}
