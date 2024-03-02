package ru.pishemzapuskayem.backendbookservice.util;

import java.nio.file.Path;

public class PathUtil {
    public static String pathToURL(String hostUrl, String path) {
        return hostUrl + "/files/" + Path.of(path).getFileName();
    }
}
