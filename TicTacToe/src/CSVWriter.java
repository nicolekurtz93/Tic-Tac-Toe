import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVWriter {
    List<String> dataLines = new ArrayList<>();
    public String FileName;

    CSVWriter(String fileName) {
        this.FileName = fileName;
        dataLines.add("Epoch,Win Percent");
    }

    public String convertToCSV(String data) {
        return Stream.of(data)
                .collect(Collectors.joining(","));
    }

    public void CreateCSV() throws IOException {
        File csvOutputFile = new File(FileName + ".csv");
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataLines.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        }
    }
}