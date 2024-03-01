package id.segari.ortools.service;

import id.segari.ortools.dto.RouteDTO;
import id.segari.ortools.validation.group.TspFixStartArbitraryFinish;
import id.segari.ortools.validation.group.VrpArbitraryStartArbitraryFinish;
import id.segari.ortools.validation.group.VrpSpStartArbitraryFinish;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Map;

@Validated
public interface RouteService {
    @Validated(VrpSpStartArbitraryFinish.class)
    Map<Integer, ArrayList<Long>> vrpWithSpStartAndArbitraryFinish(@Valid RouteDTO dto);
    @Validated(VrpArbitraryStartArbitraryFinish.class)
    Map<Integer, ArrayList<Long>> vrpWithArbitraryStartAndArbitraryFinish(@Valid RouteDTO dto);
    @Validated(TspFixStartArbitraryFinish.class)
    Map<Integer, ArrayList<Long>> tspWithFixStartAndArbitraryFinish(@Valid RouteDTO dto, @NotNull Integer index);
}
