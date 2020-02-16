package pdf.converter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pdf.converter.epub.EpubCreator;
import pdf.converter.img.ImageFileExtension;
import pdf.converter.img.ImgCreator;
import pdf.converter.zip.ZipCreator;

public class PdfConverter {
    private static final Logger log = LoggerFactory.getLogger(PdfConverter.class);
    private static final int RESOLUTION = 300;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;
    private File pdf;

    private PdfConverter(File pdf) {
        this.pdf = pdf;
    }

    public static PdfConverter convert(File pdf) {
        return new PdfConverter(pdf);
    }

    public void intoImage(File output, int resolution, int width, int height, ImageFileExtension format) {
        ImgCreator creator = new ImgCreator();
        creator.process(pdf, output, resolution, width, height, format);
    }

    public void intoEpub(String title, File output) {
        log.info("starting epub conversion into {}", output);
        Path imgsdir = new File(String.format("/tmp/%s", UUID.randomUUID().toString())).toPath();
        try {
            Files.createDirectories(imgsdir);

            ImgCreator creator = new ImgCreator();
            creator.process(pdf, imgsdir.toFile(), RESOLUTION, WIDTH, HEIGHT, ImageFileExtension.PNG);
            EpubCreator epubCreator = new EpubCreator();
            epubCreator.create(title, imgsdir.toFile(), output);
        } catch (IOException e) {
            log.warn("failed to convert into epub", e);
        }

        try {
            FileUtils.deleteDirectory(imgsdir.toFile());
        } catch (IOException ex) {
            log.warn("failed to cleanup images directory {}", imgsdir, ex);
        }

        log.info("successfully converted to epub {}", output);
    }

    public void intoEpub(File imgsdir, String title, File output) {
        EpubCreator epubCreator = new EpubCreator();
        try {
            epubCreator.create(title, imgsdir, output);
        } catch (IOException e) {
            log.warn("", e);
        }
    }

    public void intoZip(String title, File output) {
        try {
            File imgsdir = File.createTempFile(UUID.randomUUID().toString(), "");
            imgsdir.mkdirs();
            ImgCreator creator = new ImgCreator();
            creator.process(pdf, imgsdir, RESOLUTION, WIDTH, HEIGHT, ImageFileExtension.PNG);
            ZipCreator zipCreator = new ZipCreator();
            zipCreator.create(imgsdir, output);
            FileUtils.deleteDirectory(imgsdir);
        } catch (IOException e) {
            log.warn("", e);
        }

    }

    public void intoZip(File imgsdir, String title, File output) {
        ZipCreator zipCreator = new ZipCreator();
        try {
            zipCreator.create(imgsdir, output);
        } catch (IOException e) {
            log.warn("", e);
        }
    }
}
