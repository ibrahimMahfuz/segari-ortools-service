package id.segari.ortools.controller;

import id.segari.ortools.dto.ResponseDTO;
import id.segari.ortools.dto.RouteDTO;
import id.segari.ortools.dto.RouteResultDTO;
import id.segari.ortools.service.RouteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/routes")
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/vrp/sp-start/arbitrary-finish")
    public ResponseDTO<RouteResultDTO> vrp1(
            @RequestBody RouteDTO request
    ){
        log.info("dto for /vrp/sp-start/arbitrary-finish: {}", request);
        return ResponseDTO.<RouteResultDTO>builder()
                .data(routeService.vrpWithSpStartAndArbitraryFinish(request))
                .build();
    }

    @PostMapping("/vrp/arbitrary-start/arbitrary-finish")
    public ResponseDTO<RouteResultDTO> vrp2(
            @RequestBody RouteDTO request
    ){
        log.info("dto for /vrp/arbitrary-start/arbitrary-finish: {}", request);
        return ResponseDTO.<RouteResultDTO>builder()
                .data(routeService.vrpWithArbitraryStartAndArbitraryFinish(request))
                .build();
    }

    @PostMapping("/tsp/fix-start/{index}/arbitrary-finish")
    public ResponseDTO<RouteResultDTO> tsp1(
            @PathVariable Integer index,
            @RequestBody RouteDTO request
    ){
        log.info("dto for /tsp/fix-start/{}/arbitrary-finish: {}", index, request);
        return ResponseDTO.<RouteResultDTO>builder()
                .data(routeService.tspWithFixStartAndArbitraryFinish(request, index))
                .build();
    }

}
