package id.segari.ortools.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RouteResultDTO {
    private List<ArrayList<Long>> result;
}
