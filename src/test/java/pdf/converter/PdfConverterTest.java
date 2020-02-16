package pdf.converter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfConverterTest {

    private static final Logger log = LoggerFactory.getLogger(PdfConverterTest.class);

    @Test
    public void convert() throws Exception {
        Path tmpDir = copyToTmp();
        final Path source = Paths.get(tmpDir.toString(), "mobydick.pdf");
        final Path result = Paths.get(tmpDir.toString(), "mobydick.epub");
        PdfConverter.convert(source.toFile()).intoEpub("Moby Dick", result.toFile());
        Assert.assertTrue(result.toFile().exists());
    }

    private Path copyToTmp() throws IOException {
        final Path tmpDir = Paths.get("D:/tmp/", UUID.randomUUID().toString());
        log.info("tmp dir: {}", tmpDir.toAbsolutePath());
        Files.createDirectories(tmpDir);

        final Path destination = Paths.get(tmpDir.toString(), "mobydick.pdf");

        ClassLoader classLoader = getClass().getClassLoader();
        final URL resource = classLoader.getResource("mobydick.pdf");
        Assert.assertNotNull(resource);
        Path file = new File(resource.getFile()).toPath();
        Files.copy(file, destination, StandardCopyOption.REPLACE_EXISTING);
        return tmpDir;
    }
}
