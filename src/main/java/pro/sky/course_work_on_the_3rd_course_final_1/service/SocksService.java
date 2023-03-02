package pro.sky.course_work_on_the_3rd_course_final_1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.course_work_on_the_3rd_course_final_1.dto.SockShippingDto;
import pro.sky.course_work_on_the_3rd_course_final_1.dto.SocksMapper;
import pro.sky.course_work_on_the_3rd_course_final_1.exception.EmptyStockException;
import pro.sky.course_work_on_the_3rd_course_final_1.model.Color;
import pro.sky.course_work_on_the_3rd_course_final_1.model.Sock;
import pro.sky.course_work_on_the_3rd_course_final_1.model.SocksSize;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class SocksService {

    private static Map<Sock, Integer> socksMap = new HashMap<>();

    private final SocksMapper socksMapper;
    private final FileService fileService;

    public void addSocks(SockShippingDto socksShippingDto) {
        Sock sock = socksMapper.toSocks(socksShippingDto);
        if (socksMap.containsKey(sock)) {
            socksMap.put(sock, socksMap.get(sock) + socksShippingDto.getQuantity());
        } else {
            socksMap.put(sock, socksShippingDto.getQuantity());
        }
    }

    public void sellSocks(SockShippingDto socksShippingDto) {
        decreaseSockQuantity(socksShippingDto);
    }

    public void removeDefectiveSocks(SockShippingDto socksShippingDto) {
        decreaseSockQuantity(socksShippingDto);
    }

    public void decreaseSockQuantity(SockShippingDto socksShippingDto) {
        Sock sock = socksMapper.toSocks(socksShippingDto);
        int sockQuantity = socksMap.getOrDefault(sock, 0);
        if (sockQuantity >= socksShippingDto.getQuantity()) {
            socksMap.put(sock, sockQuantity - socksShippingDto.getQuantity());
        } else {
            throw new EmptyStockException("На складе нет носков");
        }
    }

    public Integer getSockQuantity(Color color, SocksSize size, Integer cottonMin, Integer cottonMax) {
        return socksMap.entrySet().stream()
                .filter(color != null ? s -> color.equals(s.getKey().getColor()) : s -> true)
                .filter(size != null ? s -> size.equals(s.getKey().getSize()) : s -> true)
                .filter(cottonMin != null ? s -> cottonMin <= s.getKey().getCottonContent() : s -> true)
                .filter(cottonMax != null ? s -> cottonMax >= s.getKey().getCottonContent() : s -> true)
                .mapToInt(s -> s.getValue()).sum();
    }

    public void importFile(MultipartFile multipartFile) throws FileProcessingException, FileNotFoundException {
        fileService.importFile(multipartFile);
        try {
            String json = fileService.readFromFile();
            List<SockShippingDto> socksList = new ObjectMapper().readValue(json, new TypeReference<ArrayList<SockShippingDto>>() {
            });
            socksMap = socksMapper.fromListOfSocksDto(socksList);
        } catch (JsonProcessingException e) {
            throw new FileProcessingException("Файл не удалось прочитать");
        }
    }

    public InputStreamResource exportFile() throws FileProcessingException, FileNotFoundException {
        try {
            String json = new ObjectMapper().writeValueAsString(socksMapper.fromMapOfSocks(socksMap));
            fileService.saveToFile(json);
            return fileService.exportFile();
        } catch (JsonProcessingException e) {
            throw new FileProcessingException("Файл не удалось сохранить");
        }
    }
}