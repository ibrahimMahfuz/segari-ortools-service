package id.segari.ortools.service.impl;

import id.segari.ortools.dto.RouteDTO;
import id.segari.ortools.dto.RouteResultDTO;
import id.segari.ortools.ortool.SegariRouting;
import id.segari.ortools.service.RouteService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@Service
@Primary
public class RouteServiceImpl implements RouteService {

    @Override
    public RouteResultDTO vrpWithSpStartAndArbitraryFinish(RouteDTO dto) {
        SegariRouting segariRouting = SegariRouting.newVrpStartFromSpAndArbitraryFinish(dto.getRoute())
                .addDistanceBetweenOrderDimension(dto.getMaxDistanceBetweenOrder())
                .addDistanceWithSpDimension(dto.getMaxDistanceFromSp())
                .addMaxInstanOrderCountDimension(dto.getMaxInstanOrderCount())
                .addMaxTurboOrderCountDimension(dto.getMaxTurboOrderCount());
        if (Boolean.TRUE.equals(dto.getIsUsingRatioDimension())) segariRouting.addExtensionTurboInstanRatioDimension(1, 10);
        if (Objects.nonNull(dto.getAlterVehicleNumberValue())) segariRouting.alterVehicleNumbers(dto.getAlterVehicleNumberValue());
        return RouteResultDTO.builder()
                .result(segariRouting.route())
                .build();
    }

    @Override
    public RouteResultDTO vrpWithArbitraryStartAndArbitraryFinish(RouteDTO dto) {
        return RouteResultDTO.builder()
                .result(SegariRouting.newVrpWithArbitraryStartAndFinish(dto.getRoute())
                        .addDistanceBetweenNodeDimension(dto.getMaxDistanceBetweenOrder())
                        .setResultMustAtMaxOrderCount()
                        .route())
                .build();
    }

    @Override
    public RouteResultDTO tspWithFixStartAndArbitraryFinish(RouteDTO dto, Integer index) {
        SegariRouting segariRouting = SegariRouting.newTspWithStartAndFinish(dto.getRoute(), index);

        if (Objects.nonNull(dto.getMaxDistanceBetweenOrder())) segariRouting.addDistanceBetweenOrderDimension(dto.getMaxDistanceBetweenOrder());
        if (Objects.nonNull(dto.getMaxDistanceFromSp())) segariRouting.addDistanceWithSpDimension(dto.getMaxDistanceFromSp());
        if (Objects.nonNull(dto.getMaxInstanOrderCount())) segariRouting.addMaxInstanOrderCountDimension(dto.getMaxInstanOrderCount());
        if (Objects.nonNull(dto.getMaxTurboOrderCount())) segariRouting.addMaxTurboOrderCountDimension(dto.getMaxTurboOrderCount());

        return RouteResultDTO.builder()
                .result(segariRouting.route())
                .build();
    }
}
