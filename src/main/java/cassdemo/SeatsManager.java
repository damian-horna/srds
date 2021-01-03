package cassdemo;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class SeatsManager {
    public static List<List<Integer>> findAllPossibleSeatsGroups(List<Integer> availableSeats, int groupSize) {
        List<List<Integer>> grouped = groupSeatsByRows(availableSeats);
        System.out.println(grouped);
        List<List<Integer>> allPossibilities = new ArrayList<>();
        int idsToCheck;
        for (List<Integer> row : grouped) {
            idsToCheck = row.size() - groupSize + 1;
            for (int startIdx = 0; startIdx < idsToCheck; startIdx++) {
                List<Integer> combination = row.subList(startIdx, startIdx + groupSize);
                if (isCombinationOfNeighbors(combination)) {
                    allPossibilities.add(combination);
                }
            }
        }
        return allPossibilities;
    }

    private static boolean isCombinationOfNeighbors(List<Integer> combination) {
        for (int i = 0; i < combination.size() - 1; i++) {
            if (abs(combination.get(i) - combination.get(i + 1)) > 1) {
                return false;
            }
        }
        return true;
    }

    private static List<List<Integer>> groupSeatsByRows(List<Integer> availableSeats) {
        List<List<Integer>> grouped = new ArrayList<>();
        List<Integer> subGroup = new ArrayList<>();

        int lastFullDivision = 0;
        for (Integer availableSeat : availableSeats) {
            if (availableSeat / 6 != lastFullDivision) {
                grouped.add(subGroup);
                lastFullDivision = availableSeat / 6;
                subGroup = new ArrayList<>();
            }
            subGroup.add(availableSeat);
        }

        if (!subGroup.isEmpty()) {
            grouped.add(subGroup);
        }
        return grouped;
    }

}
