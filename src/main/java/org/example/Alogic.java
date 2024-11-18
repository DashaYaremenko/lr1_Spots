package org.example;

import java.util.*;

public class Alogic {
    private static final int [][] GoalState={
            {1,2,3},
            {8,0,4},
            {7,6,5},
    };
    private static final int [][] Directions={
            {-1,0},{1,0},{0,-1},{0,1}
    };
    private static class Node{
        int[][] state; // Поточний стан головоломки
        int g; // Кількість кроків від початкового стану.
        int h; // Евристична оцінка для цього стану.
        Node parent; //Посилання на батьківський вузол, який відтворює шлях до початкового стану.

        public Node(int[][] state, int g, int h, Node parent) {
            this.state=state;
            this.g=g;
            this.h=h;
            this.parent=parent;
        }
        public int getF(){
            return g+h;
        } //Метод, який використовується для порівняння вузлів у черзі.
    }

    private static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return Integer.compare(n1.getF(), n2.getF());
        }
    } // Це для роботи пріоритетної черги, яка обирає вузли з найменшим значенням f.

    public List<int[][]> findSolution(int[][] initialState) {
        PriorityQueue<Node> openList=new PriorityQueue<>(new NodeComparator());
        Set<String> closedSet=new HashSet<>();
        Node startNode=new Node(initialState, 0, calculateHeuristic(initialState), null);
        openList.add(startNode);
        closedSet.add(arrayToString(initialState));
        while (!openList.isEmpty()) {
            Node currentNode=openList.poll();
            if (Arrays.deepEquals(currentNode.state,GoalState)) { // Якщо поточний стан відповідає цільовому, повертаємо шлях до цього стану
                return reconstructPath(currentNode);
            }
            // Генерує всі можливі сусідні стани
            List<Node> neighbors=generateNeighbors(currentNode);
            for (Node neighbor : neighbors) {
                String neighborStr=arrayToString(neighbor.state);
                if (!closedSet.contains(neighborStr)) { // Додаємо в чергу, якщо стан ще не оброблений
                    openList.add(neighbor);
                    closedSet.add(neighborStr);
                }
            }
        }
        return null;
    }

    private List<Node> generateNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[] emptyP=findEmptyPosition(node.state);
        for (int[] direction : Directions) {
            int newRow=emptyP[0] + direction[0];
            int newCol=emptyP[1] + direction[1];
            if (isValidPosition(newRow, newCol)) {
                int[][] newState = deepCopy(node.state);
                // Міняються місцями порожню клітинку і сусідню фішку
                newState[emptyP[0]][emptyP[1]]=newState[newRow][newCol];
                newState[newRow][newCol]=0;
                int g = node.g + 1;  // збільшуємо кількість кроків
                int h = calculateHeuristic(newState);  // обчислюємо евристику
                neighbors.add(new Node(newState, g, h, node));
            }
        }
        return neighbors;
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

    // Перевірка, чи знаходиться позиція в межах поля
    private boolean isValidPosition(int row, int col) {
        return row>=0 && row<3 && col>=0 && col<3;
    }

    // Глибоке копіювання масиву
    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[original.length][];
        for (int i=0; i<original.length; i++) {
            copy[i]=original[i].clone();
        }
        return copy;
    }

    private List<int[][]> reconstructPath(Node node) {
        List<int[][]> path = new ArrayList<>();
        while (node!=null) {
            path.add(0, node.state);
            node=node.parent;
        }
        return path;
    }

    private String arrayToString(int[][] state) {
        StringBuilder sb = new StringBuilder();
        for (int[] row:state) {
            for (int val:row) {
                sb.append(val).append(",");
            }
        }
        return sb.toString();
    }

    private int calculateHeuristic(int[][] state) {
        int h=0;
        for (int i=0; i<state.length; i++) {
            for (int j=0; j<state[i].length; j++) {
                int value=state[i][j];
                if (value!=0) {
                    int targetRow=(value-1)/3;
                    int targetCol=(value-1)%3;
                    h+=Math.abs(i-targetRow)+Math.abs(j-targetCol);  // Манхеттенська відстань
                }
            }
        }
        return h;
    }
}
