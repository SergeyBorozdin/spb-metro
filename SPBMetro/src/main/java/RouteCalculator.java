import core.Station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class RouteCalculator {
    private final StationIndex stationIndex;

    private static final double INTER_STATION_DURATION = 2.5;
    private static final double INTER_CONNECTION_DURATION = 3.5;

    public RouteCalculator(StationIndex stationIndex) {
        this.stationIndex = stationIndex;
    }

    public List<Station> getShortestRoute(Station from, Station to) {
        List<Station> route = getRouteOnTheLine(from, to);
        if (route != null) {
            return route;
        }

        route = getRouteWithOneConnection(from, to);
        if (!route.isEmpty()) {
            return route;
        }

        route = getRouteWithTwoConnections(from, to);
        return route;
    }

    public static double calculateDuration(List<Station> route) { // 1 возвращает время перемещнеия в минутах
        double duration = 0; //  продолжительность?
        Station previousStation = null;  // предыдущая станция?
        for (int i = 0; i < route.size(); i++) {
            Station station = route.get(i);
            if (i > 0) {
                duration += previousStation.getLine().equals(station.getLine()) ?
                        INTER_STATION_DURATION : INTER_CONNECTION_DURATION;
                // прибавляет время в зависиомсти от ветки(одна ветка 2.5, разные 3.5)
            }
            previousStation = station;
        }
        int [] array = {2, 5};
        System.out.println(array[3]);
        return duration;
    }

    private List<Station> getRouteOnTheLine(Station from, Station to) { // 2 возращает маршрут одной линии
        if (!from.getLine().equals(to.getLine())) { // если разные линии присваивает нул
            return null;
        }
        List<Station> route = new ArrayList<>(); // создаем маршрут, сайз 0
        List<Station> stations = from.getLine().getStations(); // создаем спискок из станций определенной линии
        int direction = 0; // направление?
        for (Station station : stations) { // запускаем перебор станций по циклу
            if (direction == 0) { // если направление 0 ищем станцию отправления
                if (station.equals(from)) {
                    direction = 1; // направлению единицу
                } else if (station.equals(to)) { // еще если станция равна назначению направление -1
                    direction = -1;
                }
            }

            if (direction != 0) { // если нарпавление не рано 0 добавляем все станции в маршрут
                route.add(station);
            }

            if ((direction == 1 && station.equals(to)) || (direction == -1 && station.equals(from))) {
                // если направление 1 и Станция  назначения равна отпралению
                // или  напраление равно -1 и станции назначения останавливаем цикл
                break;
            }
        }
        if (direction == -1) { // если напрааление -1 делаем реверс  списка
            Collections.reverse(route);
        }
        return route;
    }

    private List<Station> getRouteWithOneConnection(Station from, Station to) { //3
        if (from.getLine().equals(to.getLine())) {  // если линия одна присваивает нул
            return null;
        }
        List<Station> route = new ArrayList<>(); // инициализрует = 0 из эмпти

        List<Station> fromLineStations = from.getLine().getStations(); // берет все станции с линии
        List<Station> toLineStations = to.getLine().getStations();
        for (Station srcStation : fromLineStations) { // перебирает все станции линии отправления
            for (Station dstStation : toLineStations) {  // перебирает все станции линии назначения
                if (isConnected(srcStation, dstStation)) { // если пересадка срабоатывает
                    ArrayList<Station> way = new ArrayList<>(); // содает путь
                    way.addAll(getRouteOnTheLine(from, srcStation)); // добавляет маршрут по линии откуда до найденой
                    way.addAll(getRouteOnTheLine(dstStation, to)); // добавляет маршрут по линии от найденой до назначения
                    if (route.isEmpty() || route.size() > way.size()) {
                        route.clear(); // очищаем маршрут
                        route.addAll(way); // добавляем путь в маршрут
                    }
                }
            }
        }
        return route;
    }

    private boolean isConnected(Station station1, Station station2) { // 4  берет две станции
        Set<Station> connected = stationIndex.getConnectedStations(station1); // создает уникальный список и помещает в него
        return connected.contains(station2); // возвращает true если станция есть в спске пересадок
    }

    private List<Station> getRouteViaConnectedLine(Station from, Station to) { //5
        Set<Station> fromConnected = stationIndex.getConnectedStations(from); // берет все пересадки с линии отправления
        Set<Station> toConnected = stationIndex.getConnectedStations(to); // берет все пересадки с линии прибытия

        for (Station srcStation : fromConnected) { // перебирает все пересадки
            for (Station dstStation : toConnected) {
                if (srcStation.getLine().equals(dstStation.getLine())) { // сравнивает линии
                    return getRouteOnTheLine(srcStation, dstStation); // при совпадении возвращает маршрут
                }
            }
        }
        return null;
    }

    private List<Station> getRouteWithTwoConnections(Station from, Station to) { //6
        if (from.getLine().equals(to.getLine())) { //одна линия - возвращает нулл
            return null;
        }

        ArrayList<Station> route = new ArrayList<>(); //  иницация роут

        List<Station> fromLineStations = from.getLine().getStations(); //  список станций линии
        List<Station> toLineStations = to.getLine().getStations(); // список станций линии

        for (Station srcStation : fromLineStations) { //  перебор станций на линии
            for (Station dstStation : toLineStations) { //
                List<Station> connectedLineRoute =
                        getRouteViaConnectedLine(srcStation, dstStation); // возвращает маршрут если есть пересечение линий
                if (connectedLineRoute == null) { //
                    continue;
                }
                List<Station> way = new ArrayList<>(); //
                way.addAll(getRouteOnTheLine(from, srcStation)); //
                way.addAll(connectedLineRoute);
                way.addAll(getRouteOnTheLine(dstStation, to));
                if (route.isEmpty() || route.size() > way.size()) { //
                    route.clear();
                    route.addAll(way);
                }
            }
        }
        return route;
    }
}