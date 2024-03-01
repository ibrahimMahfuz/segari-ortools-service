package id.segari.ortools.service;

import id.segari.ortools.dto.RouteDTO;
import id.segari.ortools.group.TspFixStartArbitraryFinish;
import id.segari.ortools.group.VrpArbitraryStartArbitraryFinish;
import id.segari.ortools.group.VrpSpStartArbitraryFinish;
import id.segari.ortools.validation.group.VGTspFixStartArbitraryFinish;
import id.segari.ortools.validation.group.VGVrpArbitraryStartArbitraryFinish;
import id.segari.ortools.validation.group.VGVrpSpStartArbitraryFinish;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Map;

@Validated
public interface RouteService {
    @Validated(VGVrpSpStartArbitraryFinish.class)
    Map<Integer, ArrayList<Long>> vrpWithSpStartAndArbitraryFinish(@Valid RouteDTO dto);
    @Validated(VGVrpArbitraryStartArbitraryFinish.class)
    Map<Integer, ArrayList<Long>> vrpWithArbitraryStartAndArbitraryFinish(@Valid RouteDTO dto);
    @Validated(VGTspFixStartArbitraryFinish.class)
    Map<Integer, ArrayList<Long>> tspWithFixStartAndArbitraryFinish(@Valid RouteDTO dto, @NotNull Integer index);
}
