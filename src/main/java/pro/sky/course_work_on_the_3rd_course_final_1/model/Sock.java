package pro.sky.course_work_on_the_3rd_course_final_1.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Builder
@EqualsAndHashCode
@ToString

public class Sock {
    private Color color;
    private SocksSize size;
    public int cottonContent;
}
