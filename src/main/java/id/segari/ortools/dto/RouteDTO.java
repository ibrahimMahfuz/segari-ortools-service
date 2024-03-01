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
    @NotNull(groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    @Valid
    private SegariRoutingDTO route;
    @NotNull(groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    @Min(value = 1, groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    private Integer maxDistanceBetweenOrder;
    @NotNull(groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    @Min(value = 1, groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    private Integer maxDistanceFromSp;
    @NotNull(groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    @Min(value = 1, groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    private Integer maxTurboOrderCount;
    @NotNull(groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    @Min(value = 1, groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    private Integer maxInstanOrderCount;
    @NotNull(groups = VrpSpStartArbitraryFinish.class)
    @Min(value = 1, groups = VrpSpStartArbitraryFinish.class)
    private Integer alterVehicleNumberValue;
}
