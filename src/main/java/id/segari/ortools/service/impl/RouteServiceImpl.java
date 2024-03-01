package id.segari.ortools.service.impl;

import id.segari.ortools.dto.RouteDTO;
import id.segari.ortools.group.TspFixStartArbitraryFinish;
import id.segari.ortools.group.VrpArbitraryStartArbitraryFinish;
import id.segari.ortools.group.VrpSpStartArbitraryFinish;
import id.segari.ortools.ortool.SegariRouting;
import id.segari.ortools.service.RouteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Map;

@Service
@Primary
public class RouteServiceImpl implements RouteService {

    @Override
    public Map<Integer, ArrayList<Long>> vrpWithSpStartAndArbitraryFinish(RouteDTO dto) {
        return SegariRouting.newVrpStartFromSpAndArbitraryFinish(dto.getRoute())
                .addDistanceBetweenNodeDimension(dto.getMaxDistanceBetweenOrder())
                .setResultMustAtMaxOrderCount()
                .route();
    }

    @Override
    public Map<Integer, ArrayList<Long>> vrpWithArbitraryStartAndArbitraryFinish(RouteDTO dto) {
        return SegariRouting.newVrpWithArbitraryStartAndFinish(dto.getRoute())
                .addDistanceBetweenOrderDimension(dto.getMaxDistanceBetweenOrder())
                .addDistanceWithSpDimension(dto.getMaxDistanceFromSp())
                .addExtensionTurboInstanRatioDimension(1, 10)
                .addMaxInstanOrderCountDimension(dto.getMaxInstanOrderCount())
                .addMaxTurboOrderCountDimension(dto.getMaxTurboOrderCount())
                .alterVehicleNumbers(dto.getAlterVehicleNumberValue())
                .route();
    }

    @Override
    public Map<Integer, ArrayList<Long>> tspWithFixStartAndArbitraryFinish(RouteDTO dto, Integer index) {
        return SegariRouting.newTspWithStartAndFinish(dto.getRoute(), index)
                .addDistanceBetweenOrderDimension(dto.getMaxDistanceBetweenOrder())
                .addDistanceWithSpDimension(dto.getMaxDistanceFromSp())
                .addMaxInstanOrderCountDimension(dto.getMaxInstanOrderCount())
                .addMaxTurboOrderCountDimension(dto.getMaxTurboOrderCount())
                .route();
    }
}
