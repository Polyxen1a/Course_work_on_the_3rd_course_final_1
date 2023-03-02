package pro.sky.course_work_on_the_3rd_course_final_1.service;


import jakarta.annotation.PostConstruct;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.course_work_on_the_3rd_course_final_1.exception.FileProcessingException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service("sockFileService")


public abstract class SocksFileServiceImpl implements FileService {

    @Value("src/main/resources")
    private String dataFilePath;
    @Value("socks.json")
    private String dataFileName;
    @Value("operations.json")
    private String operationsFileName;

    private Path path;
    private Path operationPath;

    @PostConstruct
    private void init() {
        path = Path.of(dataFilePath, dataFileName);
        operationPath = Path.of(dataFilePath, operationsFileName);
    }

    @Override
    public void saveToFile(String json) {
        try {
            cleanDataFile(path);
            Files.writeString(path, json);
        } catch (IOException e) {
            throw new FileProcessingException("Не удалось сохранить данные");
        }
    }

    @Override
    public String readFromFile() {
        if (Files.exists(path)) {
            try {
                return Files.readString(path);
            } catch (IOException e) {
                throw new FileProcessingException("Не удалось прочитать файл");
            }
        } else {
            return "{ }";
        }
    }

    @Override
    public void cleanDataFile(Path path) {
        try {
            Files.deleteIfExists(path);
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File getDataFile() {
        return new File(dataFilePath + "/" + dataFileName);
    }

    @Override
    public InputStreamResource exportFile() throws FileNotFoundException {
        File file = getDataFile();
        return new InputStreamResource(new FileInputStream(file));
    }

    @Override
    public InputStreamResource exportOperation(String json) throws FileNotFoundException {
        try {
            cleanDataFile((operationPath));
            Files.writeString(operationPath, json);
        } catch (IOException e) {
            throw new FileProcessingException("Не удалось сохранить данные");
        }
        return new InputStreamResource(new FileInputStream(dataFilePath + "/" + operationsFileName));
    }

    @Override
    public void importFile(MultipartFile file) throws FileNotFoundException {
        cleanDataFile(path);
        FileOutputStream fos = new FileOutputStream(getDataFile());
        try {
            IOUtils.copy(file.getInputStream(), fos);
        } catch (IOException e) {
            throw new FileProcessingException("Проблема сохранения файла");
        }
    }
}


