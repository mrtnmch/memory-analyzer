package cz.mxmx.memoryanalyzer.app;

import com.beust.jcommander.internal.Lists;
import cz.mxmx.memoryanalyzer.exception.MemoryDumpAnalysisException;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class App {
    private static final List<String> NAMESPACES = Lists.newArrayList("cz.mxmx");

    public static void main(String[] args) throws IOException, MemoryDumpAnalysisException {


        OutputStream png = new FileOutputStream("test.png");
        String source = "" +
                "@startuml\n" +
                "Object <|-- ArrayList\n" +
                "\n" +
                "Object : equals()\n" +
                "ArrayList : Object[] elementData\n" +
                "ArrayList : size()\n" +
                "\n" +
                "@enduml";

        SourceStringReader reader = new SourceStringReader(source);
        String desc = reader.generateImage(png);
    }
}
