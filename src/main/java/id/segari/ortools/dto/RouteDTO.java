package id.segari.ortools.dto;

import id.segari.ortools.validation.group.TspFixStartArbitraryFinish;
import id.segari.ortools.validation.group.VrpArbitraryStartArbitraryFinish;
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
    @NotNull(groups = {VrpSpStartArbitraryFinish.class, VrpArbitraryStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    @Valid
    private SegariRouteDTO route;
    @NotNull(groups = {VrpSpStartArbitraryFinish.class, VrpArbitraryStartArbitraryFinish.class})
    @Min(value = 1, groups = {VrpSpStartArbitraryFinish.class, VrpArbitraryStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    private Integer maxDistanceBetweenOrder;
    @NotNull(groups = VrpSpStartArbitraryFinish.class)
    @Min(value = 1, groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    private Integer maxDistanceFromSp;
    @NotNull(groups = VrpSpStartArbitraryFinish.class)
    @Min(value = 1, groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    private Integer maxTurboOrderCount;
    @NotNull(groups = VrpSpStartArbitraryFinish.class)
    @Min(value = 1, groups = {VrpSpStartArbitraryFinish.class, TspFixStartArbitraryFinish.class})
    private Integer maxInstanOrderCount;
    @Min(value = 1, groups = VrpSpStartArbitraryFinish.class)
    private Integer alterVehicleNumberValue;
    private Boolean isUsingRatioDimension;
    private Integer extensionCount;

    @Override
    public String toString() {
        return "{" +
                "route:" + route +
                ", maxDistanceBetweenOrder:" + maxDistanceBetweenOrder +
                ", maxDistanceFromSp:" + maxDistanceFromSp +
                ", maxTurboOrderCount:" + maxTurboOrderCount +
                ", maxInstanOrderCount:" + maxInstanOrderCount +
                ", alterVehicleNumberValue:" + alterVehicleNumberValue +
                ", isUsingRatioDimension:" + isUsingRatioDimension +
                ", extensionCount:" + extensionCount +
                '}';
    }
}
