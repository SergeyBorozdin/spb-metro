import core.Line;
import core.Station;
import junit.framework.TestCase;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteCalculatorTest extends TestCase
{
    StationIndex stationIndex;
    RouteCalculator routeCalculator;

    @Override
    protected void setUp()
    {
        stationIndex = new StationIndex();

        Line line1 = new Line(1, "first");
        Line line2 = new Line(2, "second");
        Line line3 = new Line(3, "three");

        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);

        Station station1 = new Station("A1", line1);
        Station station2 = new Station("A2", line1);
        Station station3 = new Station("A3", line1);
        Station station4 = new Station("B1", line2);
        Station station5 = new Station("B2", line2);
        Station station6 = new Station("B3", line2);
        Station station7 = new Station("C1", line3);
        Station station8 = new Station("C2", line3);
        Station station9 = new Station("C3", line3);

        line1.addStation(station1);
        line1.addStation(station2);
        line1.addStation(station3);
        line2.addStation(station4);
        line2.addStation(station5);
        line2.addStation(station6);
        line3.addStation(station7);
        line3.addStation(station8);
        line3.addStation(station9);

        stationIndex.addStation(station1);
        stationIndex.addStation(station2);
        stationIndex.addStation(station3);
        stationIndex.addStation(station4);
        stationIndex.addStation(station5);
        stationIndex.addStation(station6);
        stationIndex.addStation(station7);
        stationIndex.addStation(station8);
        stationIndex.addStation(station9);

        List<Station> connection1to2 = new ArrayList<>();
        connection1to2.add(station2);
        connection1to2.add(station4);
        stationIndex.addConnection(connection1to2);

        List<Station> connection2to3 = new ArrayList<>();
        connection2to3.add(station6);
        connection2to3.add(station8);
        stationIndex.addConnection(connection2to3);

        routeCalculator = new RouteCalculator(stationIndex);
    }

    @Test
    public void testCalculateDuration() // 1
    {
        List<Station> route = Arrays.asList(
                stationIndex.getStation("A1", 1),
                stationIndex.getStation("A2", 1),
                stationIndex.getStation("A3", 1),
                stationIndex.getStation("B1", 2),
                stationIndex.getStation("B2", 2),
                stationIndex.getStation("B3", 2),
                stationIndex.getStation("C1", 3),
                stationIndex.getStation("C2", 3),
                stationIndex.getStation("C3", 3));

        double actual = RouteCalculator.calculateDuration(route);
        double expected = 22.0;
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRouteOnTheLine() //2
    {
        Station from = stationIndex.getStation("B1", 2);
        Station to = stationIndex.getStation("B3", 2);

        System.out.println();
        List<Station> actual = routeCalculator.getShortestRoute(from, to);
        List<Station> expected = Arrays.asList(
                stationIndex.getStation("B1", 2),
                stationIndex.getStation("B2", 2),
                stationIndex.getStation("B3", 2));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRouteWithOneConnection() //3
    {
        Station from = stationIndex.getStation("A1", 1);
        Station to = stationIndex.getStation("B2", 2);

        List<Station> actual = routeCalculator.getShortestRoute(from, to);
        List<Station> expected = Arrays.asList(
                stationIndex.getStation("A1", 1),
                stationIndex.getStation("A2", 1),
                stationIndex.getStation("B1", 2),
                stationIndex.getStation("B2", 2));
        assertEquals(expected,actual);
    }


    @Test
    public  void testGetRouteWithTwoConnections() //6
    {
        Station from = stationIndex.getStation("A3", 1);
        Station to = stationIndex.getStation("C1", 3);

        List<Station> actual = routeCalculator.getShortestRoute(from, to);
        List<Station> expected = Arrays.asList(
                stationIndex.getStation("A3", 1),
                stationIndex.getStation("A2", 1),
                stationIndex.getStation("B1", 2),
                stationIndex.getStation("B2", 2),
                stationIndex.getStation("B3", 2),
                stationIndex.getStation("C2", 3),
                stationIndex.getStation("C1", 3));
        assertEquals(expected,actual);

    }
    //===================================================================
}
