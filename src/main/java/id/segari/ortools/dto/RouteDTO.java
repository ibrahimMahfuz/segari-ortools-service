package id.segari.ortools.dto;

import id.segari.ortools.validation.group.TspFixStartArbitraryFinish;
import id.segari.ortools.validation.group.VrpSpStartArbitraryFinish;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteDTO {
    @NotNull
    @Valid
    private SegariRoutingDTO route;
    @NotNull
    @Min(1)
    private Integer maxDistanceBetweenOrder;
    @NotNull(groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    @Min(1)
    private Integer maxDistanceFromSp;
    @NotNull(groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    @Min(1)
    private Integer maxTurboOrderCount;
    @NotNull(groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    @Min(1)
    private Integer maxInstanOrderCount;
    @NotNull(groups = VrpSpStartArbitraryFinish.class)
    @Min(1)
    private Integer alterVehicleNumberValue;
}
