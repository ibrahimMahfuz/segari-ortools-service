package id.segari.ortools.service;

import id.segari.ortools.dto.RouteDTO;
import id.segari.ortools.group.TspFixStartArbitraryFinish;
import id.segari.ortools.group.VrpArbitraryStartArbitraryFinish;
import id.segari.ortools.group.VrpSpStartArbitraryFinish;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Map;

public interface RouteService {
    @Validated(VrpSpStartArbitraryFinish.class)
    Map<Integer, ArrayList<Long>> vrpWithSpStartAndArbitraryFinish(@Valid RouteDTO dto);
    @Validated(VrpArbitraryStartArbitraryFinish.class)
    Map<Integer, ArrayList<Long>> vrpWithArbitraryStartAndArbitraryFinish(@Valid RouteDTO dto);
    @Validated(TspFixStartArbitraryFinish.class)
    Map<Integer, ArrayList<Long>> tspWithFixStartAndArbitraryFinish(@Valid RouteDTO dto, Integer index);
}
