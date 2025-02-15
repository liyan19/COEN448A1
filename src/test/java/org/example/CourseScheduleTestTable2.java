package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;



import static org.junit.jupiter.api.Assertions.*;

public class CourseScheduleTestTable2 {
    //Used AI to generate help generate these parametrized tests
    private final CourseSchedule solution = new CourseSchedule();

    @ParameterizedTest(name = "BC{index}: Graph={1}, Dependency Count={2}, Distribution={3}")
    @CsvFileSource(resources = "/Table2.csv", numLinesToSkip = 1)
    void testBasicChoiceCoverage(String testCase, String graphStructure, String dependencyCount, String distribution) {

        int numCourses = 6;
        int[][] prerequisites = generateGraph(testCase, numCourses, graphStructure, dependencyCount, distribution);
        for(int[] prereq : prerequisites) {
            System.out.println(prereq[0] + " " + prereq[1]);
        }

        // Expected behavior based on test case
        if (graphStructure.equals("Cyclic")) {
            assertArrayEquals(new int[]{}, solution.findOrder(numCourses, prerequisites));
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

        switch (testCase) {
            case "BC1": // Directed Acyclic, Single prerequisite, Even Distribution
                prerequisites = new int[][]{{1, 0}, {2, 1}, {3, 2}};
                break;

            case "BC2": // Cyclic, Single prerequisite, Even Distribution
                prerequisites = new int[][]{{1, 0}, {2, 1}, {3, 2}, {0, 3}};
                break;

            case "BC3": // Directed Acyclic, No prerequisite, Even Distribution
                prerequisites = new int[0][0];
                break;

            case "BC4": // Directed Acyclic, Multiple prerequisite, Even Distribution
                prerequisites = new int[][]{{2, 0}, {2, 1}, {3, 1}, {3, 2}};
                break;

            case "BC5": // Directed Acyclic, Single prerequisite, Uneven Distribution
                prerequisites = new int[][]{{1, 0}, {4, 0}, {2, 1}, {3, 1}};
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
        assertEquals(numCourses, result.length);

        boolean[] visited = new boolean[numCourses];
        for (int course : result) {
            visited[course] = true;
        }
        for (boolean v : visited) {
            assertTrue(v);
        }

        for (int[] prereq : prerequisites) {
            int a = prereq[0], b = prereq[1];
            int indexA = indexOf(result, a);
            int indexB = indexOf(result, b);
            assertTrue(indexB < indexA);
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
