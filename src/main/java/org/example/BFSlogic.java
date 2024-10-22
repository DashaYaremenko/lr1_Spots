package org.example;

import java.util.*;

public class BFSlogic {
    private static final int[][] GoalState = {
            {1, 2, 3},
            {8, 0, 4},
            {7, 6, 5},
    };
    private static final int[][] Directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    private static class Node {
        int[][] state;
        Node parent;

        public Node(int[][] state,Node parent) {
            this.state=state;
            this.parent=parent;
        }
    }

    public List<int[][]> findSolution(int[][] initialState) {
        Queue<Node> q=new LinkedList<>();
        Set<String> visited=new HashSet<>();
        Node startNode=new Node(initialState, null);
        q.add(startNode);
        visited.add(arrayToString(initialState));
        while (!q.isEmpty()) {
            Node currentNode=q.poll();
            if (Arrays.deepEquals(currentNode.state, GoalState)) {
                return reconstructPath(currentNode);
            }
            List<Node> neighbors=generateNeighbors(currentNode);
            for (Node neighbor:neighbors) {
                String neighborStr=arrayToString(neighbor.state);
                if (!visited.contains(neighborStr)) {// Якщо стан ще не був оброблений, додаємо його до черги
                    q.add(neighbor);
                    visited.add(neighborStr);
                }
            }
        }
        return null;
    }

    private String arrayToString(int[][] state) {
        StringBuilder builder=new StringBuilder();
        for (int[] row:state) {
            for (int val:row) {
                builder.append(val).append(",");
            }
        }
        return builder.toString();
    }

    private List<Node> generateNeighbors(Node node) {
        List<Node> neighbors=new ArrayList<>();
        int[] emptyP=findEmptyPosition(node.state);
        for (int[] direction:Directions) {
            int newRow=emptyP[0]+direction[0];
            int newCol=emptyP[1]+direction[1];
            if (isValidPosition(newRow, newCol)) {
                int[][] newState=deepCopy(node.state);
                newState[emptyP[0]][emptyP[1]]=newState[newRow][newCol];
                newState[newRow][newCol]=0;
                neighbors.add(new Node(newState,node));
            }
        }
        return neighbors;
    }

    private int[][] deepCopy(int[][] original) {
        int[][] copy=new int[original.length][];
        for (int i=0; i<original.length; i++) {
            copy[i]=original[i].clone();
        }
        return copy;
    }

    private boolean isValidPosition(int row,int col) {
        return row>=0 && row<3 && col>=0 && col<3;
    }

    private int[] findEmptyPosition(int[][] state) {
        for (int i=0; i<state.length; i++) {
            for (int j=0; j<state[i].length; j++) {
                if (state[i][j]==0) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private List<int[][]> reconstructPath(Node node) {
        List<int[][]> path = new ArrayList<>();
        while (node!=null) {
            path.add(0,node.state);
            node=node.parent;
        }
        return path;
    }
}
