package id.segari.ortools.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SegariRoutingOrderDTO {
    private final long id;
    private final SegariRoutingOrderEnum type;
    private final double latitude;
    private final double longitude;
    private final boolean isExtension;
    private final boolean isTurbo;
    private final boolean isInstan;

    public static SegariRoutingOrderDTO newVrpOrderType(long id,
                                                        double latitude,
                                                        double longitude,
                                                        boolean isExtension,
                                                        boolean isTurbo,
                                                        boolean isInstan){
        return new SegariRoutingOrderDTO(id, SegariRoutingOrderEnum.ORDER, latitude, longitude, isExtension, isTurbo, isInstan);
    }

    public static SegariRoutingOrderDTO newVrpSpType(double latitude, double longitude){
        return new SegariRoutingOrderDTO(-1L, SegariRoutingOrderEnum.SP, latitude, longitude, false, false, false);
    }

    public static SegariRoutingOrderDTO newVrpDummyType(){
        return new SegariRoutingOrderDTO(-2L, SegariRoutingOrderEnum.DUMMY, 0D, 0D, false, false, false);
    }

    public enum SegariRoutingOrderEnum {
        DUMMY, // id = -1
        SP, // id = -2
        ORDER
    }
}
