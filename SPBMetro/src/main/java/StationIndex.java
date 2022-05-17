import core.Line;
import core.Station;

import java.util.*;
import java.util.stream.Collectors;

public class StationIndex {
    private final Map<Integer, Line> number2line; // map  из ключа числа и Line имя+номер
    private final TreeSet<Station> stations; // уникальный список станций апорядоченый номеру линии и по имени
    private final Map<Station, TreeSet<Station>> connections; // пересадки в мар,
    // где ключ станции а значение уникальный  упорядоченый список станций

    public StationIndex() {
        number2line = new HashMap<>();
        stations = new TreeSet<>();
        connections = new TreeMap<>();
    }

    public void addStation(Station station) {
        stations.add(station);
    }

    public void addLine(Line line) {
        number2line.put(line.getNumber(), line);
    }

    public void addConnection(List<Station> stations) { //
        for (Station station : stations) { // перебирает список станций
            if (!connections.containsKey(station)) { // если станции нет в пересадках запсываем ее в список
                connections.put(station, new TreeSet<>());
            }
            TreeSet<Station> connectedStations = connections.get(station);
            connectedStations.addAll(stations.stream()
                    .filter(s -> !s.equals(station)).collect(Collectors.toList()));
            // собирает в лист станции по совпадению
        }
    }

    public Line getLine(int number) {
        return number2line.get(number);
    }

    public Station getStation(String name) {
        for (Station station : stations) {
            if (station.getName().equalsIgnoreCase(name)) {
                return station;
            }
        }
        return null;
    }

    public Station getStation(String name, int lineNumber) {
        Station query = new Station(name, getLine(lineNumber));
        Station station = stations.ceiling(query);
        return station.equals(query) ? station : null;
    }

    public Set<Station> getConnectedStations(Station station) {
        return connections.containsKey(station) ?
                connections.get(station) : new TreeSet<>();
    }
}
