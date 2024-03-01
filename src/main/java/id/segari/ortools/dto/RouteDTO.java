package id.segari.ortools.dto;

import id.segari.ortools.validation.group.VGTspFixStartArbitraryFinish;
import id.segari.ortools.validation.group.VGVrpSpStartArbitraryFinish;
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
    @NotNull(groups = {VGVrpSpStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class})
    @Valid
    private SegariRoutingDTO route;
    @NotNull(groups = {VGVrpSpStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class})
    @Min(value = 1, groups = {VGVrpSpStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class})
    private Integer maxDistanceBetweenOrder;
    @NotNull(groups = {VGVrpSpStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class})
    @Min(value = 1, groups = {VGVrpSpStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class})
    private Integer maxDistanceFromSp;
    @NotNull(groups = {VGVrpSpStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class})
    @Min(value = 1, groups = {VGVrpSpStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class})
    private Integer maxTurboOrderCount;
    @NotNull(groups = {VGVrpSpStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class})
    @Min(value = 1, groups = {VGVrpSpStartArbitraryFinish.class, VGTspFixStartArbitraryFinish.class})
    private Integer maxInstanOrderCount;
    @NotNull(groups = VGVrpSpStartArbitraryFinish.class)
    @Min(value = 1, groups = VGVrpSpStartArbitraryFinish.class)
    private Integer alterVehicleNumberValue;
}
