package pro.sky.course_work_on_the_3rd_course_final_1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.course_work_on_the_3rd_course_final_1.dto.SockShippingDto;
import pro.sky.course_work_on_the_3rd_course_final_1.exception.FileProcessingException;
import pro.sky.course_work_on_the_3rd_course_final_1.model.Color;
import pro.sky.course_work_on_the_3rd_course_final_1.model.SocksSize;
import pro.sky.course_work_on_the_3rd_course_final_1.service.FileService;
import pro.sky.course_work_on_the_3rd_course_final_1.service.SocksService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Склад носков", description = "CRUD-операции для работы с поставками носков")

public class SocksController {

    private final SocksService sockService;
    private final FileService sockFileService;

    @PostMapping
    @Operation(summary = "Добавление носков на склад")
    public void addSocks(@Valid @RequestBody SockShippingDto sockShippingDto) {
        sockService.addSocks(sockShippingDto);
    }

    @PutMapping
    @Operation(summary = "Отгрузка со склада")
    public void sellSocks(@Valid @RequestBody SockShippingDto sockShippingDto) {
        sockService.sellSocks(sockShippingDto);
    }

    @GetMapping
    @Operation(summary = "Получение носков по параментрам")
    public Integer getSocksCount(@RequestParam(required = false, name = "color") Color color,
                                 @RequestParam(required = false, name = "size") SocksSize size,
                                 @RequestParam(required = false, name = "cottonMin") Integer cottonMin,
                                 @RequestParam(required = false, name = "cottonMax") Integer cottonMax) {
        return sockService.getSockQuantity(color, size, cottonMin, cottonMax);
    }

    @DeleteMapping
    @Operation(summary = "Списание брака")
    public void removeDefectiveSocks(@Valid @RequestBody SockShippingDto sockShippingDto) {
        sockService.removeDefectiveSocks(sockShippingDto);
    }

    @GetMapping("/export")
    @Operation(description = "Экспорт файла")
    public ResponseEntity downloadFile() throws IOException, FileProcessingException {
        InputStreamResource inputStreamResource = sockService.exportFile();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength((Files.size(sockFileService.getPath())))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"socks.json\"")
                .body(inputStreamResource);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "Импорт файла")
    public ResponseEntity<Void> uploadDataFile(MultipartFile file) throws FileNotFoundException, FileProcessingException {
        sockService.importFile(file);
        return ResponseEntity.ok().build();
    }
}
