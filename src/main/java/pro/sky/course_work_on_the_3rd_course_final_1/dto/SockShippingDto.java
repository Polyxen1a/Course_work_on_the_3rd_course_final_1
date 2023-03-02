package pro.sky.course_work_on_the_3rd_course_final_1.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import pro.sky.course_work_on_the_3rd_course_final_1.model.Color;
import pro.sky.course_work_on_the_3rd_course_final_1.model.SocksSize;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SockShippingDto {
    @NotNull(message = "Цвет является обязательным полем")
    private Color color;
    @NotNull(message = "Размер является обязательным полем")
    private SocksSize size;
    private Integer cottonContent;
    @Positive(message = "Количество должно быть положительным числом")
    @Setter
    private int quantity;
}
