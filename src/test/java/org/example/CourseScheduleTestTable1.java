package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

public class CourseScheduleTestTable1 {

    private CourseSchedule solution = new CourseSchedule();

    @ParameterizedTest(name = "EC{index}: numCourses={0}, prereqItemLength={1}, courseRange={2}, uniqueness={3}")
    @CsvFileSource(resources = "/Table1.csv", numLinesToSkip = 1)
    void testEachChoiceCoverage(String testCase, String numCoursesStr, String prereqItemLengthStr, String courseRange, String uniqueness) {
        int numCourses = parseNumCourses(numCoursesStr);
        int prereqItemLength = parsePrereqItemLength(prereqItemLengthStr);
        int[][] prerequisites = generatePrerequisites(numCourses, prereqItemLength, courseRange, uniqueness);

        // Handle invalid cases
        if (numCourses < 1 || numCourses > 2000 || prereqItemLength != 2) {
            assertThrows(IllegalArgumentException.class, () -> solution.findOrder(numCourses, prerequisites));
            return;
        }

        // Call the function
        int[] result = solution.findOrder(numCourses, prerequisites);

        // Ensure valid results
        assertValidSchedule(numCourses, result, prerequisites);
    }

    /**
     * Generates prerequisites based on course range and uniqueness conditions.
     */
    private int[][] generatePrerequisites(int numCourses, int prereqItemLength, String courseRange, String uniqueness) {
        int prereqLength = (prereqItemLength == 2) ? 2 : 3; // Force wrong length for invalid case
        int[][] prerequisites = new int[prereqLength][prereqItemLength];
        if (numCourses == 0) {
            System.out.println("number of Course must be greater than 0");
            return prerequisites;
        }
        for (int i = 0; i < prereqLength; i++) {
            if (courseRange.contains("<0")) {
                prerequisites[i] = new int[]{-1, -1}; // Invalid values
            } else if (courseRange.contains("> numCourses")) {
                prerequisites[i] = new int[]{numCourses + 1, numCourses + 2}; // Out of bounds
            } else {

                prerequisites[i] = new int[]{i % numCourses, (i + 1) % numCourses}; // Valid range
            }

            if (uniqueness.equals("Not Distinct")) {
                prerequisites[i] = new int[]{0, 0}; // Non-unique pair
            }
        }
        return prerequisites;
    }

    /**
     * Verifies the output schedule is valid.
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

        // Check prerequisites order
        for (int[] prereq : prerequisites) {
            if (prereq.length != 2) continue; // Ignore invalid ones
            int a = prereq[0], b = prereq[1];
            int indexA = indexOf(result, a);
            int indexB = indexOf(result, b);
            assertTrue(indexB < indexA, "Prerequisite course should appear before dependent.");
        }
    }

    /**
     * Finds index of element in array.
     */
    private int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) return i;
        }
        return -1;
    }

    /**
     * Parses the numCourses field, handling conditions like "<1" or ">2000".
     */
    private int parseNumCourses(String condition) {
        if (condition.contains("<")) {
            return 0; // Example: numCourses<1 -> 0
        } else if (condition.contains(">")) {
            return 2001; // Example: numCourses>2000 -> 2001
        } else {
            return Integer.parseInt(condition); // Example: "5" -> 5
        }
    }

    /**
     * Parses the prerequisites[i].length field, handling conditions like "!=2" or "==2".
     */
    private int parsePrereqItemLength(String condition) {
        if (condition.contains("!=")) {
            return 3; // Invalid case (should trigger error)
        } else if (condition.contains("==")) {
            return 2; // Valid case
        } else {
            return Integer.parseInt(condition);
        }
    }
}
