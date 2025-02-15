package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;



import static org.junit.jupiter.api.Assertions.*;

public class CourseScheduleTestTable2 {

    private final CourseSchedule solution = new CourseSchedule();

    @ParameterizedTest(name = "BC{index}: Graph={1}, Dependency Count={2}, Distribution={3}")
    @CsvFileSource(resources = "/Table2.csv", numLinesToSkip = 1)
    void testBasicChoiceCoverage(String testCase, String graphStructure, String dependencyCount, String distribution) {

        int numCourses = 6; // Standardized for testing
        int[][] prerequisites = generateGraph(testCase, numCourses, graphStructure, dependencyCount, distribution);



        // Expected behavior based on test case
        if (graphStructure.equals("Cyclic")) {
            assertArrayEquals(new int[]{}, solution.findOrder(numCourses, prerequisites),
                    "Cyclic graphs should return an empty array.");
        } else {
            int[] result = solution.findOrder(numCourses, prerequisites);
            assertValidSchedule(numCourses, result, prerequisites);
        }
    }


    /**
     * Generates a prerequisite graph based on the parameters.
     */
    private int[][] generateGraph(String testCase, int numCourses, String graphStructure, String dependencyCount, String distribution) {
        int[][] prerequisites;

        switch (testCase) { // Now testCase is properly passed in
            case "BC1": // Directed Acyclic, Single prerequisite, Even Distribution
                prerequisites = new int[][]{{1, 0}, {2, 1}, {3, 2}}; // Linear chain: 0 -> 1 -> 2 -> 3
                break;

            case "BC2": // Cyclic, Single prerequisite, Even Distribution
                prerequisites = new int[][]{{1, 0}, {2, 1}, {3, 2}, {0, 3}}; // Cycle: 0 -> 1 -> 2 -> 3 -> 0
                break;

            case "BC3": // Directed Acyclic, No prerequisite, Even Distribution
                prerequisites = new int[0][0]; // Empty prerequisites
                break;

            case "BC4": // Directed Acyclic, Multiple prerequisite, Even Distribution
                prerequisites = new int[][]{{2, 0}, {2, 1}, {3, 1}, {3, 2}}; // Multiple prerequisites per course
                break;

            case "BC5": // Directed Acyclic, Single prerequisite, Uneven Distribution
                prerequisites = new int[][]{{1, 0}, {4, 0}, {2, 1}, {3, 1}}; // More courses depend on course 1
                break;

            default:
                prerequisites = new int[0][0];
        }
        return prerequisites;
    }


    /**
     * Verifies that the course order is valid.
     */
    private void assertValidSchedule(int numCourses, int[] result, int[][] prerequisites) {
        assertEquals(numCourses, result.length, "Result length should match numCourses.");

        boolean[] visited = new boolean[numCourses];
        for (int course : result) {
            visited[course] = true;
        }
        for (boolean v : visited) {
            assertTrue(v, "All courses must be included.");
        }

        for (int[] prereq : prerequisites) {
            int a = prereq[0], b = prereq[1];
            int indexA = indexOf(result, a);
            int indexB = indexOf(result, b);
            assertTrue(indexB < indexA, "Prerequisite course should appear before dependent.");
        }
    }

    /**
     * Finds index of an element in an array.
     */
    private int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) return i;
        }
        return -1;
    }
}
