package com.example.library.util;

import com.example.library.model.Book;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

// Simple CSV exporter using File I/O
public class BookCsvExporter {

    public void exportToCsv(List<Book> books, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("id,title,author,genre,availableCopies,totalCopies\n");
            for (Book book : books) {
                writer.write(String.format("%s,%s,%s,%s,%d,%d%n",
                        escape(book.getId()),
                        escape(book.getTitle()),
                        escape(book.getAuthor()),
                        book.getGenre(),
                        book.getAvailableCopies(),
                        book.getTotalCopies()));
            }
        }
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        String escaped = value.replace("\"", "\"\"");
        if (escaped.contains(",") || escaped.contains("\"")) {
            return "\"" + escaped + "\"";
        }
        return escaped;
    }
}