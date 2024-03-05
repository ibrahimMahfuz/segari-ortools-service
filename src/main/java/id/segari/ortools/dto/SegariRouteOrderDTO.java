package id.segari.ortools.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SegariRouteOrderDTO {
    @NotNull
    private final Long id;
    @NotNull
    private final SegariRouteOrderEnum type;
    private final Double latitude;
    private final Double longitude;
    @NotNull
    private final Boolean isExtension;
    @NotNull
    private final Boolean isTurbo;
    @NotNull
    private final Boolean isInstan;


    public enum SegariRouteOrderEnum {
        DUMMY, // id = -1
        SP, // id = -2
        ORDER
    }
}
