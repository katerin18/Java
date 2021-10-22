import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
/**
 * This class stores the basic state necessary for the A* algorithm to compute a
 * path across a map.  This state includes a collection of "open waypoints" and
 * another collection of "closed waypoints."  In addition, this class provides
 * the basic operations that the A* pathfinding algorithm needs to perform its
 * processing.
 **/
public class AStarState
{
    /** This is a reference to the map that the A* algorithm is navigating. **/
    private Map2D map;

    // этот класс хранит набор открытых и закрытых вершин, и предоставляет основные операции,
    // необходимые для функционирования алгоритма поиска А*

    // добавляем 2 нестатичных поля:

    public HashMap<Location, Waypoint> closse = new HashMap<Location, Waypoint>(); // для закрытых вершин
    public HashMap<Location, Waypoint> openn = new HashMap<Location, Waypoint>(); // для открытых вершин


    // hashCode - число, которое является уникальным идентификатором содержимого объекта
    // hashMap - структура данных, организовывающая "ключ-значение"
    // hash - функция (математический алгоритм,
    // преобразовывающий произвольный массив данных в состоящую из букв и цифр строку фиксированной длины.)
    // hash нужен для быстрого сравнения двух объектов

    /**
     * Initialize a new state object for the A* pathfinding algorithm to use.
     **/
    public AStarState(Map2D map)
    {
        if (map == null)
            throw new NullPointerException("map cannot be null");

        this.map = map;
    }

    /** Returns the map that the A* pathfinder is navigating. **/
    public Map2D getMap()
    {
        return map;
    }

    /**
     * This method scans through all open waypoints, and returns the waypoint
     * with the minimum total cost.  If there are no open waypoints, this method
     * returns <code>null</code>.
     **/
    public Waypoint getMinOpenWaypoint()   // поиск минимальной стоимости вершины /// а еще это подходит под функцию из Грокаем алги
    {
        if (openn.size()==0) {
            return null;
        }
        double minn = Double.MAX_VALUE;
        Location min_node = new Location();
        for (Location point : openn.keySet()) {
            if (openn.get(point).getTotalCost() < minn) {
                min_node = point;
                minn = openn.get(point).getTotalCost();
            }
        }
        return openn.get(min_node);
    }

    /**
     * This method adds a waypoint to (or potentially updates a waypoint already
     * in) the "open waypoints" collection.  If there is not already an open
     * waypoint at the new waypoint's location then the new waypoint is simply
     * added to the collection.  However, if there is already a waypoint at the
     * new waypoint's location, the new waypoint replaces the old one <em>only
     * if</em> the new waypoint's "previous cost" value is less than the current
     * waypoint's "previous cost" value.
     **/
    public boolean addOpenWaypoint(Waypoint newWP)  // hard class
    {
        // TODO:  Implement.
        Location new_loc = newWP.getLocation();
        if (openn.containsKey(new_loc)){ // Проверяем, если точка уже была открыта
            if(openn.get(new_loc).getPreviousCost()> newWP.getPreviousCost()){  //Если до неё теперь можно быстрее, то мы её обновляем
                openn.put(new_loc, newWP); //Обновляем
                return true;
            }
            else {
                return false;
            }
        }
        else {
            openn.put(new_loc, newWP); // Добавляем её, если такой точки ещё не было
            return true;
        }
    }


    /** Возврат количества открытых вершин **/
    public int numOpenWaypoints()
    {
        return openn.size();
    }


    /**
     * Этот метод перемещает вершину из набора «открытых вершин» в набор «закрытых вершин».
     **/
    public void closeWaypoint(Location loc)
    {
        closse.put(loc, openn.get(loc)); // для waypoint пишем openn.get(loc)
            openn.remove(loc);
    }

    /**
     * Этот метод должен возвращать значение true, если указанное
     * местоположение встречается в наборе закрытых вершин, и false в противном случае.
     **/
    public boolean isLocationClosed(Location loc)
    {
         return closse.containsKey(loc); // проверка
    }
}
