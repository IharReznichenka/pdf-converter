package pdf.converter;

import java.nio.file.Path;
import java.nio.file.Paths;

public class App {

    public static void main(String[] args) {
        final String sourceDir = "c:\\Users\\iharr\\Downloads\\";
        final Path sourcePath = Paths.get(sourceDir, "olifer-2016.pdf");
        final Path resultPath = Paths.get(sourceDir, "olifer-2016.epub");
        PdfConverter
                .convert(sourcePath.toFile())
                .intoEpub("Компьютерные сети", resultPath.toFile());
    }
}
