package id.segari.ortools.ortool;

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;
import com.google.protobuf.Duration;
import id.segari.ortools.dto.SegariRouteDTO;
import id.segari.ortools.dto.SegariRouteOrderDTO;
import id.segari.ortools.error.SegariRoutingErrors;
import id.segari.ortools.exception.BaseException;
import id.segari.ortools.util.GeoUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SegariRoute {
    private final SegariRouteType type;
    private final List<SegariRouteOrderDTO> orders;
    private boolean hasDistanceBetweenOrderDimension = false;
    private boolean hasDistanceWithSpDimension = false;
    private boolean hasDistanceBetweenNodeDimension = false;
    private boolean hasMaxInstanOrderCountDimension = false;
    private boolean hasMaxTurboOrderCountDimension = false;
    private boolean hasLoadFactorDimension = false;
    private boolean hasExtensionTurboInstanRatioDimension = false;
    private boolean hasSetResultMustAtMaxOrderCount = false;
    private boolean hasResultMustContainExtension = false;
    private long[][] distanceMatrix;
    private int[] start;
    private int[] finish;
    @Setter(AccessLevel.PRIVATE)
    private int vehicleNumbers;
    private long[] maxOrderDemands;
    private long[] maxOrderVehicleCapacities;
    private long[] maxTurboDemands;
    private long[] maxTurboVehicleCapacities;
    private long[] maxInstanDemands;
    private long[] maxInstanVehicleCapacities;
    private long[] loadFactorDemands;
    private long[] loadFactorVehicleCapacities;
    private long[] extensionRatioDemands;
    private long[] extensionRatioVehicleCapacities;
    @Setter(AccessLevel.PRIVATE)
    private int maxTotalDistanceInMeter;
    private int maxDistanceBetweenOrderInMeter;
    private int maxDistanceWithSpInMeter;
    private int maxDistanceBetweenNodeInMeter;
    @Setter(AccessLevel.PRIVATE)
    private int maxOrderCount;
    private int maxInstanOrderCount;
    private int maxTurboOrderCount;
    private int maxLoadFactor;
    private int extensionRatio;
    private int turboInstanRatio;
    private int extensionCount;
    @Setter(AccessLevel.PRIVATE)
    private int tspFixStartIndex;

    public static SegariRoute newVrpStartFromSpAndArbitraryFinish(SegariRouteDTO dto){
        if (dto.getOrders().size() <= 2) throw SegariRoutingErrors.emptyOrder();
        if (!SegariRouteOrderDTO.SegariRouteOrderEnum.DUMMY.equals(dto.getOrders().get(0).getType())) throw SegariRoutingErrors.indexZeroNotDummy();
        if (!SegariRouteOrderDTO.SegariRouteOrderEnum.SP.equals(dto.getOrders().get(1).getType())) throw SegariRoutingErrors.indexOneNotSp();
        SegariRoute segariRoute = new SegariRoute(SegariRouteType.VRP_SP_START_ARBITRARY_FINISH, dto.getOrders());
        injectVrpAttributes(segariRoute, dto.getMaxTotalDistanceInMeter(), dto.getMaxOrderCount(), dto.getOrders().size() - 2);
        return segariRoute;
    }

    public static SegariRoute newVrpWithArbitraryStartAndFinish(SegariRouteDTO dto){
        if (dto.getOrders().size() <= 1) throw SegariRoutingErrors.emptyOrder();
        if (!SegariRouteOrderDTO.SegariRouteOrderEnum.DUMMY.equals(dto.getOrders().get(0).getType())) throw SegariRoutingErrors.indexZeroNotDummy();
        SegariRoute segariRoute = new SegariRoute(SegariRouteType.VRP_ARBITRARY_START_AND_FINISH, dto.getOrders());
        injectVrpAttributes(segariRoute, dto.getMaxTotalDistanceInMeter(), dto.getMaxOrderCount(), dto.getOrders().size() - 1);
        return segariRoute;
    }

    public static SegariRoute newTspWithStartAndFinish(SegariRouteDTO dto, int startIndex){
        if (dto.getOrders().size() <= 1) throw SegariRoutingErrors.emptyOrder();
        if (!SegariRouteOrderDTO.SegariRouteOrderEnum.DUMMY.equals(dto.getOrders().get(0).getType())) throw SegariRoutingErrors.indexZeroNotDummy();
        SegariRoute segariRoute = new SegariRoute(SegariRouteType.TSP_FIX_START_ARBITRARY_FINISH, dto.getOrders());
        injectTspAttributes(segariRoute, dto.getMaxTotalDistanceInMeter(), dto.getMaxOrderCount(), startIndex);
        return segariRoute;
    }

    public SegariRoute alterVehicleNumbers(int vehicleNumbers){
        if (vehicleNumbers <= 0) throw SegariRoutingErrors.invalidRoutingParameter();
        this.setVehicleNumbers(vehicleNumbers);
        return this;
    }

    public SegariRoute addDistanceBetweenOrderDimension(int maxDistanceInMeter){
        if (this.hasDistanceBetweenNodeDimension) throw SegariRoutingErrors.cannotUseAddDistanceBetweenOrderDimension();
        if (maxDistanceInMeter <= 0) throw SegariRoutingErrors.invalidRoutingParameter();
        this.hasDistanceBetweenOrderDimension = true;
        this.maxDistanceBetweenOrderInMeter = maxDistanceInMeter;
        return this;
    }

    public SegariRoute addDistanceWithSpDimension(int maxDistanceInMeter){
        if (this.hasDistanceBetweenNodeDimension) throw SegariRoutingErrors.cannotUseAddDistanceWithSpDimension();
        if (maxDistanceInMeter <= 0) throw SegariRoutingErrors.invalidRoutingParameter();
        this.hasDistanceWithSpDimension = true;
        this.maxDistanceWithSpInMeter = maxDistanceInMeter;
        return this;
    }

    public SegariRoute addDistanceBetweenNodeDimension(int maxDistanceInMeter){
        if (this.hasDistanceBetweenOrderDimension || this.hasDistanceWithSpDimension) throw SegariRoutingErrors.cannotUseAddDistanceBetweenNodeDimension();
        if (maxDistanceInMeter <= 0) throw SegariRoutingErrors.invalidRoutingParameter();
        this.hasDistanceBetweenNodeDimension = true;
        this.maxDistanceBetweenNodeInMeter = maxDistanceInMeter;
        return this;
    }

    public SegariRoute addMaxInstanOrderCountDimension(int max){
        this.hasMaxInstanOrderCountDimension = true;
        this.maxInstanOrderCount = max;
        return this;
    }

    public SegariRoute addMaxTurboOrderCountDimension(int max){
        this.hasMaxTurboOrderCountDimension = true;
        this.maxTurboOrderCount = max;
        return this;
    }

    /**
     * intended for load factor which right now is not possible,
     * set it private for now
     * @param max
     * @return this
     */
    private SegariRoute addLoadFactorDimension(int max){
        this.hasLoadFactorDimension = true;
        this.maxLoadFactor = max;
        return this;
    }

    public SegariRoute addExtensionTurboInstanRatioDimension(int extensionRatio, int turboInstanRatio, int extensionCount){
        this.hasExtensionTurboInstanRatioDimension = true;
        if (extensionRatio <= 0 || turboInstanRatio <= 0 || extensionCount <= 0) throw SegariRoutingErrors.invalidRoutingParameter();
        this.extensionRatio = extensionRatio;
        this.turboInstanRatio = turboInstanRatio;
        this.extensionCount = extensionCount;
        return this;
    }

    public SegariRoute setResultMustAtMaxOrderCount(){
        this.hasSetResultMustAtMaxOrderCount = true;
        return this;
    }

    public SegariRoute setResultMustContainExtension(){
        this.hasResultMustContainExtension = true;
        return this;
    }

    public List<ArrayList<Long>> route(){
        try {
            return handleRoute();
        }
        catch (Exception e){
          throw BaseException.builder()
                  .message(e.getMessage())
                  .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                  .build();
        }
    }

    private List<ArrayList<Long>> handleRoute() {
        fillRequiredAttributes();
        RoutingIndexManager manager = getRoutingIndexManager();
        RoutingModel routing = new RoutingModel(manager);
        addDistanceDimension(routing, manager);
        addMaxOrderCountDimension(routing, manager);
        if (this.hasExtensionTurboInstanRatioDimension) addExtensionTurboInstanRatioDimension(routing, manager);
        if (this.hasMaxInstanOrderCountDimension) addMaxInstanOrderCountDimension(routing, manager);
        if (this.hasMaxTurboOrderCountDimension) addMaxTurboOrderCountDimension(routing, manager);
        if (this.hasLoadFactorDimension) addLoadFactorDimension(routing, manager);
        addPenaltyAndDropVisit(routing, manager);
        Assignment solution = findSolution(routing);
        return getResult(routing, manager, solution);
    }

    private RoutingIndexManager getRoutingIndexManager() {
        if (this.distanceMatrix.length == 0) throw SegariRoutingErrors.invalidRoutingParameter();
        if (this.vehicleNumbers <= 0) throw SegariRoutingErrors.invalidRoutingParameter();
        if (this.start.length == 0) throw SegariRoutingErrors.invalidRoutingParameter();
        if (this.finish.length == 0) throw SegariRoutingErrors.invalidRoutingParameter();
        return new RoutingIndexManager(this.distanceMatrix.length, this.vehicleNumbers, this.start, this.finish);
    }

    private void addDistanceDimension(RoutingModel routing, RoutingIndexManager manager) {
        if (this.maxTotalDistanceInMeter <= 0) throw SegariRoutingErrors.invalidRoutingParameter();
        if (this.distanceMatrix.length == 0) throw SegariRoutingErrors.invalidRoutingParameter();
        final int transitCallbackIndex =
                routing.registerTransitCallback((long fromIndex, long toIndex) -> {
                    int fromNode = manager.indexToNode(fromIndex);
                    int toNode = manager.indexToNode(toIndex);
                    return distanceMatrix[fromNode][toNode];
                });
        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);
        routing.addDimension(transitCallbackIndex, 0, this.maxTotalDistanceInMeter,
                true,
                "Distance");
    }

    private void addMaxOrderCountDimension(RoutingModel routing, RoutingIndexManager manager) {
        if (notEqualToDistanceMatrixLength(this.maxOrderDemands.length)) throw SegariRoutingErrors.invalidRoutingParameter();
        if (notEqualToVehicleNumber(this.maxOrderVehicleCapacities.length)) throw SegariRoutingErrors.invalidRoutingParameter();
        final int maxOrderCountCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            int fromNode = manager.indexToNode(fromIndex);
            return this.maxOrderDemands[fromNode];
        });
        routing.addDimensionWithVehicleCapacity(maxOrderCountCallbackIndex, 0,
                this.maxOrderVehicleCapacities,
                true,
                "MaxOrderCount");
    }

    private void addExtensionTurboInstanRatioDimension(RoutingModel routing, RoutingIndexManager manager) {
        if (notEqualToDistanceMatrixLength(this.extensionRatioDemands.length)) throw SegariRoutingErrors.invalidRoutingParameter();
        if (notEqualToVehicleNumber(this.extensionRatioVehicleCapacities.length)) throw SegariRoutingErrors.invalidRoutingParameter();
        final int extensionTurboInstanRatioCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            int fromNode = manager.indexToNode(fromIndex);
            return this.extensionRatioDemands[fromNode];
        });
        routing.addDimensionWithVehicleCapacity(extensionTurboInstanRatioCallbackIndex, 0,
                this.extensionRatioVehicleCapacities,
                true,
                "ExtensionTurboInstanRatio");
    }

    private void addMaxInstanOrderCountDimension(RoutingModel routing, RoutingIndexManager manager) {
        if (notEqualToDistanceMatrixLength(this.maxInstanDemands.length)) throw SegariRoutingErrors.invalidRoutingParameter();
        if (notEqualToVehicleNumber(this.maxInstanVehicleCapacities.length)) throw SegariRoutingErrors.invalidRoutingParameter();
        final int maxInstanOrderCountCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            int fromNode = manager.indexToNode(fromIndex);
            return this.maxInstanDemands[fromNode];
        });
        routing.addDimensionWithVehicleCapacity(maxInstanOrderCountCallbackIndex, 0,
                this.maxInstanVehicleCapacities,
                true,
                "MaxInstanOrderCount");
    }

    private void addMaxTurboOrderCountDimension(RoutingModel routing, RoutingIndexManager manager) {
        if (notEqualToDistanceMatrixLength(this.maxTurboDemands.length)) throw SegariRoutingErrors.invalidRoutingParameter();
        if (notEqualToVehicleNumber(this.maxTurboVehicleCapacities.length)) throw SegariRoutingErrors.invalidRoutingParameter();
        final int maxTurboOrderCountCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            int fromNode = manager.indexToNode(fromIndex);
            return this.maxTurboDemands[fromNode];
        });
        routing.addDimensionWithVehicleCapacity(maxTurboOrderCountCallbackIndex, 0,
                this.maxTurboVehicleCapacities,
                true,
                "MaxTurboOrderCount");
    }

    private void addLoadFactorDimension(RoutingModel routing, RoutingIndexManager manager) {
        if (notEqualToDistanceMatrixLength(this.loadFactorDemands.length)) throw SegariRoutingErrors.invalidRoutingParameter();
        if (notEqualToVehicleNumber(this.loadFactorVehicleCapacities.length)) throw SegariRoutingErrors.invalidRoutingParameter();
        final int loadFactorCallbackIndex = routing.registerUnaryTransitCallback((long fromIndex) -> {
            int fromNode = manager.indexToNode(fromIndex);
            return this.loadFactorDemands[fromNode];
        });
        routing.addDimensionWithVehicleCapacity(loadFactorCallbackIndex, 0,
                this.loadFactorVehicleCapacities,
                true,
                "LoadFactor");
    }

    private boolean notEqualToDistanceMatrixLength(int length) {
        return length != this.distanceMatrix.length;
    }

    private boolean notEqualToVehicleNumber(int length) {
        return length != this.vehicleNumbers;
    }

    private void addPenaltyAndDropVisit(RoutingModel routing, RoutingIndexManager manager) {
        long penalty = 100_000;
        for (int i = determineStartFromVrpType(); i < this.distanceMatrix.length; ++i) {
            routing.addDisjunction(new long[] {manager.nodeToIndex(i)}, penalty);
        }
    }

    private static Assignment findSolution(RoutingModel routing) {
        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .setTimeLimit(Duration.newBuilder().setSeconds(60).build())
                        .build();
        return routing.solveWithParameters(searchParameters);
    }

    private List<ArrayList<Long>> getResult(RoutingModel routing, RoutingIndexManager manager,
                                                                  Assignment solution) {
        List<ArrayList<Long>> results = new ArrayList<>();
        for (int i = 0; i < this.vehicleNumbers; i++) {
            long index = routing.start(i);

            ArrayList<Long> route = new ArrayList<>();
            boolean hasExtension = false;
            while (!routing.isEnd(index)){
                final long thisRoute = manager.indexToNode(index);
                final SegariRouteOrderDTO order = this.orders.get((int) thisRoute);
                final long orderId = order.getId();
                if (!hasExtension) hasExtension = order.getIsExtension();
                if (orderId != -1L && orderId != -2L) route.add(orderId);
                index = solution.value(routing.nextVar(index));
            }

            if (this.hasSetResultMustAtMaxOrderCount){
                if (route.size() != this.maxOrderCount) continue;
            }
            if (this.hasResultMustContainExtension){
                if (!hasExtension) continue;
            }

            putResult(route, results);
        }

        return results;
    }

    private static void putResult(ArrayList<Long> route, List<ArrayList<Long>> results) {
        if (route.size() > 1) results.add(route);
    }

    private void fillRequiredAttributes() {
        int startIndex = determineStartFromVrpType();
        int length = this.orders.size();

        this.distanceMatrix = getDistanceMatrix(length);
        this.maxOrderVehicleCapacities = initiateVehicleArray(this.vehicleNumbers, this.maxOrderCount);
        this.maxOrderDemands = initiateDemandArray(this.orders.size(), startIndex);
        this.start = getStart();
        this.finish = getFinish();

        long[] maxInstanDemands = new long[length];
        long[] maxTurboDemands = new long[length];
        long[] loadFactorDemands = new long[length];
        long[] extensionRatioDemands = new long[length];
        for (int i = startIndex; i < length; i++){
            if (this.hasMaxInstanOrderCountDimension) maxInstanDemands[i] = this.orders.get(i).getIsInstan() ? 1 : 0;
            if (this.hasMaxTurboOrderCountDimension) maxTurboDemands[i] = this.orders.get(i).getIsTurbo() ? 1 : 0;
            if (this.hasLoadFactorDimension) loadFactorDemands[i] = 0; // TODO - define load factor when possible
            if (this.hasExtensionTurboInstanRatioDimension) extensionRatioDemands[i] = this.orders.get(i).getIsExtension() ? this.extensionRatio : this.turboInstanRatio;
        }
        if (this.hasMaxInstanOrderCountDimension) {
            this.maxInstanDemands = maxInstanDemands;
            this.maxInstanVehicleCapacities = initiateVehicleArray(this.vehicleNumbers, maxInstanOrderCount);
        }
        if (this.hasMaxTurboOrderCountDimension) {
            this.maxTurboDemands = maxTurboDemands;
            this.maxTurboVehicleCapacities = initiateVehicleArray(this.vehicleNumbers, maxTurboOrderCount);
        }
        if (this.hasLoadFactorDimension) {
            this.loadFactorDemands = loadFactorDemands;
            this.loadFactorVehicleCapacities = initiateVehicleArray(this.vehicleNumbers, maxLoadFactor);
        }
        if (this.hasExtensionTurboInstanRatioDimension) {
            this.extensionRatioDemands = extensionRatioDemands;
            this.extensionRatioVehicleCapacities = initiateVehicleArray(this.vehicleNumbers, determineRatioCapacity());
        }
    }

    private int determineRatioCapacity() {
        final int ex = Math.ceilDiv(this.extensionCount, this.vehicleNumbers);
        int capacity = 0;
        for (int i = 0; i < this.maxOrderCount; i++) {
            if (i < ex){
                capacity += this.extensionRatio;
            }else {
                capacity += this.turboInstanRatio;
            }
        }
        return capacity;
    }

    private long[][] getDistanceMatrix(int length) {
        long[][] distanceMatrix = new long[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (isDummyNode(i, j)){
                    distanceMatrix[i][j] = 0;
                    continue;
                }
                long basicValue = Math.round(GeoUtils.getHaversineDistanceInMeter(
                                        this.orders.get(i).getLatitude(),
                                        this.orders.get(i).getLongitude(),
                                        this.orders.get(j).getLatitude(),
                                        this.orders.get(j).getLongitude()));
                if (this.hasDistanceBetweenNodeDimension){
                    distanceMatrix[i][j] = basicValue > this.maxDistanceBetweenNodeInMeter ? this.maxTotalDistanceInMeter + 1 : basicValue;
                    continue;
                }
                if (this.hasDistanceWithSpDimension && isSpNode(i, j)){
                    distanceMatrix[i][j] = basicValue > this.maxDistanceWithSpInMeter ? this.maxTotalDistanceInMeter + 1 : basicValue;
                    continue;
                }
                if (this.hasDistanceBetweenOrderDimension){
                    distanceMatrix[i][j] = basicValue > this.maxDistanceBetweenOrderInMeter ? this.maxTotalDistanceInMeter + 1 : basicValue;
                }
            }
        }

        return distanceMatrix;
    }

    private boolean isSpNode(int i, int j) {
        return SegariRouteOrderDTO.SegariRouteOrderEnum.SP.equals(this.orders.get(i).getType()) || SegariRouteOrderDTO.SegariRouteOrderEnum.SP.equals(this.orders.get(j).getType());
    }

    private boolean isDummyNode(int i, int j) {
        return SegariRouteOrderDTO.SegariRouteOrderEnum.DUMMY.equals(this.orders.get(i).getType()) || SegariRouteOrderDTO.SegariRouteOrderEnum.DUMMY.equals(this.orders.get(j).getType());
    }

    private int[] getFinish() {
        if (SegariRouteType.VRP_ARBITRARY_START_AND_FINISH.equals(this.type)) return arrayOfZero(this.vehicleNumbers);
        if (SegariRouteType.VRP_SP_START_ARBITRARY_FINISH.equals(this.type)) return arrayOfZero(this.vehicleNumbers);
        if (SegariRouteType.TSP_FIX_START_ARBITRARY_FINISH.equals(this.type)) return arrayOfZero(this.vehicleNumbers);
        throw SegariRoutingErrors.typeNotSupported();
    }

    private int[] getStart() {
        if (SegariRouteType.VRP_ARBITRARY_START_AND_FINISH.equals(this.type)) return arrayOfZero(this.vehicleNumbers);
        if (SegariRouteType.VRP_SP_START_ARBITRARY_FINISH.equals(this.type)) return arrayOfOne(this.vehicleNumbers);
        if (SegariRouteType.TSP_FIX_START_ARBITRARY_FINISH.equals(this.type)) return new int[]{tspFixStartIndex};
        throw SegariRoutingErrors.typeNotSupported();
    }

    private static long[] initiateVehicleArray(long vehicleNumbers, int value) {
        long[] array = new long[(int) vehicleNumbers];
        Arrays.fill(array, value);
        return array;
    }

    private static long[] initiateDemandArray(int length, int start) {
        long[] array = new long[length];
        for (int i = start; i < array.length; i++) {
            array[i] = 1;
        }
        return array;
    }

    private static int[] arrayOfZero(int length) {
        int[] array = new int[length];
        Arrays.fill(array, 0);
        return array;
    }

    private static int[] arrayOfOne(int length) {
        int[] array = new int[length];
        Arrays.fill(array, 1);
        return array;
    }

    private int determineStartFromVrpType() {
        if (SegariRouteType.VRP_ARBITRARY_START_AND_FINISH.equals(this.type)) return 1;
        if (SegariRouteType.VRP_SP_START_ARBITRARY_FINISH.equals(this.type)) return 2;
        if (SegariRouteType.TSP_FIX_START_ARBITRARY_FINISH.equals(this.type)) {
            if (SegariRouteOrderDTO.SegariRouteOrderEnum.SP.equals(orders.get(1).getType())){
                return 2;
            }else {
                return 1;
            }
        }
        throw SegariRoutingErrors.typeNotSupported();
    }

    private static void injectVrpAttributes(SegariRoute segariRoute,
                                            int maxTotalDistanceInMeter, int maxOrderCount,
                                            int orderCount) {
        if (maxTotalDistanceInMeter <= 0) throw SegariRoutingErrors.invalidRoutingParameter();
        if (maxOrderCount <= 0) throw SegariRoutingErrors.invalidRoutingParameter();
        segariRoute.setMaxTotalDistanceInMeter(maxTotalDistanceInMeter);
        segariRoute.setMaxOrderCount(maxOrderCount);
        segariRoute.setVehicleNumbers((orderCount < maxOrderCount) ? 1 : orderCount / maxOrderCount);
    }

    private static void injectTspAttributes(SegariRoute segariRoute,
                                            int maxTotalDistanceInMeter, int maxOrderCount, int startIndex) {
        if (maxTotalDistanceInMeter <= 0) throw SegariRoutingErrors.invalidRoutingParameter();
        if (maxOrderCount <= 0) throw SegariRoutingErrors.invalidRoutingParameter();
        if (startIndex < 0) throw SegariRoutingErrors.invalidRoutingParameter();
        segariRoute.setMaxTotalDistanceInMeter(maxTotalDistanceInMeter);
        segariRoute.setMaxOrderCount(maxOrderCount);
        segariRoute.setVehicleNumbers(1);
        segariRoute.setTspFixStartIndex(startIndex);
    }

    enum SegariRouteType {
        VRP_SP_START_ARBITRARY_FINISH, VRP_ARBITRARY_START_AND_FINISH, TSP_FIX_START_ARBITRARY_FINISH
    }

    static {
        Loader.loadNativeLibraries();
    }
}
