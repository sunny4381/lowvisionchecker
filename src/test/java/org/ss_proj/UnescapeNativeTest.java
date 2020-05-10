package org.ss_proj;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class UnescapeNativeTest {
    void unescapeNative(Path source) throws IOException, InterruptedException {
        File output = new File(source.toString() + ".tmp");
        try {
            ProcessBuilder builder = new ProcessBuilder("native2ascii", "-reverse", source.toString());
            builder.redirectOutput(output);

            Process p = builder.start();
            p.waitFor();
            if (p.exitValue() != 0) {
                return;
            }

            Files.copy(output.toPath(), source, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(source.toString());
        } finally {
            if (output.exists()) {
                output.delete();
            }
        }
    }

    void unescapeNativeAll(Path root) throws IOException {
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!attrs.isRegularFile()) {
                    return FileVisitResult.CONTINUE;
                }
                if (!file.toString().endsWith(".properties")) {
                    return FileVisitResult.CONTINUE;
                }

                try {
                    unescapeNative(file);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Ignore
    @Test
    public void convert() throws Exception {
        unescapeNativeAll(Paths.get(".", "src", "main", "resources"));
    }
}
