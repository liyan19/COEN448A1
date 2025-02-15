package org.example;

import java.util.*;

public class CourseSchedule {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        if (numCourses < 1 || numCourses > 2000) {
            throw new IllegalArgumentException("numCourses must be between 1 and 2000.");
        }
        if (prerequisites.length > 0 && prerequisites[0].length != 2) {
            throw new IllegalArgumentException("Each prerequisite must have exactly 2 elements.");
        }

        // Edge case: If no prerequisites, return a simple sequence
        if (prerequisites.length == 0) {
            int[] order = new int[numCourses];
            for (int i = 0; i < numCourses; i++) {
                order[i] = i;
            }
            return order;
        }

        // Graph representation
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] inDegree = new int[numCourses];

        for (int[] prerequisite : prerequisites) {
            int course = prerequisite[0];
            int pre = prerequisite[1];

            graph.putIfAbsent(pre, new ArrayList<>());
            graph.get(pre).add(course);

            inDegree[course]++;
        }

        // Topological sorting using BFS (Kahn's Algorithm)
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        int index = 0;
        int[] order = new int[numCourses];

        while (!queue.isEmpty()) {
            int current = queue.poll();
            order[index++] = current;

            if (graph.containsKey(current)) {
                for (int neighbor : graph.get(current)) {
                    inDegree[neighbor]--;
                    if (inDegree[neighbor] == 0) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        // If we couldn't process all courses, return empty array
        return index == numCourses ? order : new int[]{};
    }
}