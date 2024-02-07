package business;

import java.util.ArrayList;
import java.util.List;

public class PathInfo {
    private final int start;
    private final int loadingzone;
    private final int dropoffzone;
    private final float cost;
    private final List<Integer> path;

    public PathInfo(int start, int loadingzone, int dropoffzone, float cost, List<Integer> path) {
        this.loadingzone = loadingzone;
        this.dropoffzone = dropoffzone;
        this.cost = cost;
        this.path = path;
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public int getLoadingzone() {
        return loadingzone;
    }

    public int getDropoffzone() {
        return dropoffzone;
    }

    public float getCost() {
        return cost;
    }

    public List<Integer> getPath() {
        return new ArrayList<>(path);
    }

    @Override
    public String toString() {
        return "PathInfo{" +
                "start=" + start +
                ", loadingzone=" + loadingzone +
                ", dropoffzone=" + dropoffzone +
                ", cost=" + cost +
                ", path=" + path +
                '}';
    }
}
