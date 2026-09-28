package br.com.adotapet.api.domain.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DataUtil {

    private DataUtil() {
    }

    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DTS = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static LocalDate newDate(String data) {
        return LocalDate.parse(data, DT);
    }

    public static LocalDateTime newDateTime(String data) {
        return LocalDateTime.parse(data, DTS);
    }
}
