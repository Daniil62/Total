package ru.job4j.total;

import android.os.Environment;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class FileMaster {
    private String currentPath = Environment.getRootDirectory().getPath();
    String getCurrentPath() {
        return  this.currentPath;
    }
    List<File> getFiles(String path) {
        List<File> result = new ArrayList<>();
        if (path != null) {
            File directory = new File(path);
            File[] files = directory.listFiles();
            if (files != null) {
                result = Arrays.asList(files);
            }
        }
        return result;
    }
}
