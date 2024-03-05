package id.segari.ortools.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SegariRouteDTO {
    @NotEmpty
    private List<@NotNull SegariRouteOrderDTO> orders;
    @NotNull
    @Min(1)
    private Integer maxTotalDistanceInMeter;
    @NotNull
    @Min(1)
    private Integer maxOrderCount;
}
