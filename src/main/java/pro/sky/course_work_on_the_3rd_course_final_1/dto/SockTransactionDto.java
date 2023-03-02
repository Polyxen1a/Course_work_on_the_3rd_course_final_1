package pro.sky.course_work_on_the_3rd_course_final_1.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import pro.sky.course_work_on_the_3rd_course_final_1.model.Color;
import pro.sky.course_work_on_the_3rd_course_final_1.model.SocksSize;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SockTransactionDto {
    @NotNull(message = "Цвет является обязательным полем")
    private Color color;
    @NotNull(message = "Размер является обязательным полем")
    private SocksSize size;
    private Integer cottonContent;
    @Positive(message = "Количество должно быть положительным числом")
    @Setter
    private int quantity;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime date;
}
