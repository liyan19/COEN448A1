package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

public class CourseScheduleTestTable1 {
//Used AI to generate help generate these parametrized tests
    private CourseSchedule solution = new CourseSchedule();

    @ParameterizedTest(name = "EC{index}: numCourses={0}, prereqItemLength={1}, courseRange={2}, uniqueness={3}")
    @CsvFileSource(resources = "/Table1.csv", numLinesToSkip = 1)
    void testEachChoiceCoverage(String testCase, String numCoursesStr, String prereqItemLengthStr, String courseRange, String uniqueness) {
        int numCourses = parseNumCourses(numCoursesStr);
        int prereqItemLength = parsePrereqItemLength(prereqItemLengthStr);
        int[][] prerequisites = generatePrerequisites(numCourses, prereqItemLength, courseRange, uniqueness);
        System.out.println(numCourses);
        for(int[] prereq : prerequisites) {
            System.out.println(prereq[0] + " " + prereq[1]);
        }

        if (numCourses < 1 || numCourses > 2000 || prereqItemLength != 2) {
            assertThrows(IllegalArgumentException.class, () -> solution.findOrder(numCourses, prerequisites));
            return;
        }


        int[] result = solution.findOrder(numCourses, prerequisites);


        assertValidSchedule(numCourses, result, prerequisites);
    }

    /**
     * Generates prerequisites based on course range and uniqueness conditions.
     */
    private int[][] generatePrerequisites(int numCourses, int prereqItemLength, String courseRange, String uniqueness) {
        int prereqLength = (prereqItemLength == 2) ? 2 : 3;
        int[][] prerequisites = new int[prereqLength][prereqItemLength];
        if (numCourses == 0) {

            return prerequisites;
        }
        for (int i = 0; i < prereqLength; i++) {
            if (courseRange.contains("<0")) {
                prerequisites[i] = new int[]{-1, -1};
            } else if (courseRange.contains("> numCourses")) {
                prerequisites[i] = new int[]{numCourses + 1, numCourses + 2};
            } else {

                prerequisites[i] = new int[]{i % numCourses, (i + 1) % numCourses};
            }

            if (uniqueness.equals("Not Distinct")) {
                prerequisites[i] = new int[]{0, 0};
            }
        }
        return prerequisites;
    }

    /**
     * Verifies the output schedule is valid.
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

        // Check prerequisites order
        for (int[] prereq : prerequisites) {
            if (prereq.length != 2) continue;
            int a = prereq[0], b = prereq[1];
            int indexA = indexOf(result, a);
            int indexB = indexOf(result, b);
            assertTrue(indexB < indexA);
        }
    }


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
        if (condition.contains("<=")){
            return 3;
        }
        if (condition.contains("<")) {
            return 0;
        } else  {
            return 2001;
        }
    }

    /**
     * Parses the prerequisites[i].length field, handling conditions like "!=2" or "==2".
     */
    private int parsePrereqItemLength(String condition) {
        if (condition.contains("!=")) {
            return 3;
        } else if (condition.contains("==")) {
            return 2;
        } else {
            return Integer.parseInt(condition);
        }
    }
}
