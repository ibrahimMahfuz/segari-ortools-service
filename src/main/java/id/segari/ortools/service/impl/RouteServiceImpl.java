package id.segari.ortools.service.impl;

import id.segari.ortools.dto.RouteDTO;
import id.segari.ortools.ortool.SegariRouting;
import id.segari.ortools.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class RouteServiceImpl implements RouteService {

    @Override
    public Map<Integer, ArrayList<Long>> vrpWithSpStartAndArbitraryFinish(@Valid RouteDTO dto) {
        return SegariRouting.newVrpStartFromSpAndArbitraryFinish(dto.getRoute())
                .addDistanceBetweenNodeDimension(dto.getMaxDistanceBetweenOrder())
                .setResultMustAtMaxOrderCount()
                .route();
    }

    @Override
    public Map<Integer, ArrayList<Long>> vrpWithArbitraryStartAndArbitraryFinish(@Valid RouteDTO dto) {
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
    public Map<Integer, ArrayList<Long>> tspWithFixStartAndArbitraryFinish(@Valid RouteDTO dto, Integer index) {
        return SegariRouting.newTspWithStartAndFinish(dto.getRoute(), index)
                .addDistanceBetweenOrderDimension(dto.getMaxDistanceBetweenOrder())
                .addDistanceWithSpDimension(dto.getMaxDistanceFromSp())
                .addMaxInstanOrderCountDimension(dto.getMaxInstanOrderCount())
                .addMaxTurboOrderCountDimension(dto.getMaxTurboOrderCount())
                .route();
    }
}
