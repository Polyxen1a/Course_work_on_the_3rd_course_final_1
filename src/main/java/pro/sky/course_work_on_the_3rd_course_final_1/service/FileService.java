package pro.sky.course_work_on_the_3rd_course_final_1.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public interface FileService {

    void saveToFile(String json);

    String readFromFile();

    void cleanDataFile(Path path);

    void getDataFile();

    InputStreamResource exportFile() throws FileNotFoundException;

    InputStreamResource exportOperation(String json) throws FileNotFoundException;

    void importFile(MultipartFile file) throws FileNotFoundException;

    String importOperations(MultipartFile file) throws FileNotFoundException;

    InputStreamResource exportOperations(String json) throws FileNotFoundException;

    Path getPath();

    Path getOperationPath();
}
