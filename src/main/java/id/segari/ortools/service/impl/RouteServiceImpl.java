package id.segari.ortools.service.impl;

import id.segari.ortools.dto.RouteDTO;
import id.segari.ortools.dto.RouteResultDTO;
import id.segari.ortools.error.SegariRoutingErrors;
import id.segari.ortools.ortool.SegariRoute;
import id.segari.ortools.service.RouteService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Primary
public class RouteServiceImpl implements RouteService {

    @Override
    public RouteResultDTO vrpWithSpStartAndArbitraryFinish(RouteDTO dto) {
        SegariRoute segariRoute = SegariRoute.newVrpStartFromSpAndArbitraryFinish(dto.getRoute())
                .addDistanceBetweenOrderDimension(dto.getMaxDistanceBetweenOrder())
                .addDistanceWithSpDimension(dto.getMaxDistanceFromSp())
                .addMaxInstanOrderCountDimension(dto.getMaxInstanOrderCount())
                .addMaxTurboOrderCountDimension(dto.getMaxTurboOrderCount());
        if (Boolean.TRUE.equals(dto.getIsUsingRatioDimension())) {
            if (Objects.isNull(dto.getExtensionCount())) throw SegariRoutingErrors.invalidRoutingParameter("getExtensionCount in vrpWithSpStartAndArbitraryFinish");
            segariRoute.addExtensionTurboInstanRatioDimension(1, 100, dto.getExtensionCount());
            segariRoute.setResultMustContainExtension();
            segariRoute.setResultMinimum(4);
            segariRoute.alterVehicleNumbers(dto.getExtensionCount());
        }
        return RouteResultDTO.builder()
                .result(segariRoute.route())
                .build();
    }

    @Override
    public RouteResultDTO vrpWithArbitraryStartAndArbitraryFinish(RouteDTO dto) {
        return RouteResultDTO.builder()
                .result(SegariRoute.newVrpWithArbitraryStartAndFinish(dto.getRoute())
                        .addDistanceBetweenNodeDimension(dto.getMaxDistanceBetweenOrder())
                        .setResultMinimum(dto.getRoute().getMaxOrderCount())
                        .route())
                .build();
    }

    @Override
    public RouteResultDTO tspWithFixStartAndArbitraryFinish(RouteDTO dto, Integer index) {
        SegariRoute segariRoute = SegariRoute.newTspWithStartAndFinish(dto.getRoute(), index);

        if (Objects.nonNull(dto.getMaxDistanceBetweenOrder())) segariRoute.addDistanceBetweenOrderDimension(dto.getMaxDistanceBetweenOrder());
        if (Objects.nonNull(dto.getMaxDistanceFromSp())) segariRoute.addDistanceWithSpDimension(dto.getMaxDistanceFromSp());
        if (Objects.nonNull(dto.getMaxInstanOrderCount())) segariRoute.addMaxInstanOrderCountDimension(dto.getMaxInstanOrderCount());
        if (Objects.nonNull(dto.getMaxTurboOrderCount())) segariRoute.addMaxTurboOrderCountDimension(dto.getMaxTurboOrderCount());

        return RouteResultDTO.builder()
                .result(segariRoute.route())
                .build();
    }
}
