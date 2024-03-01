package id.segari.ortools.controller;

import id.segari.ortools.dto.RouteDTO;
import id.segari.ortools.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/routes")
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/vrp/sp-start/arbitrary-finish")
    public ResponseEntity<Map<Integer, ArrayList<Long>>> vrp1(
            @RequestBody RouteDTO request
            ){
        return ResponseEntity.ok(routeService.vrpWithSpStartAndArbitraryFinish(request));
    }

    @PostMapping("/vrp/arbitrary-start/arbitrary-finish")
    public ResponseEntity<Map<Integer, ArrayList<Long>>> vrp2(
            @RequestBody RouteDTO request
    ){
        return ResponseEntity.ok(routeService.vrpWithArbitraryStartAndArbitraryFinish(request));
    }

    @PostMapping("/tsp/fix-start/{index}/arbitrary-finish")
    public ResponseEntity<Map<Integer, ArrayList<Long>>> tsp1(
            @PathVariable Integer index,
            @RequestBody RouteDTO request
    ){
        return ResponseEntity.ok(routeService.tspWithFixStartAndArbitraryFinish(request, index));
    }

}
