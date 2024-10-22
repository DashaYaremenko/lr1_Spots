package org.example;

import java.util.List;
import java.util.Scanner;

public class SpotsGame {
    private int [][] initialState={
            {6, 4, 3},
            {5, 1, 8},
            {7, 2, 0}
    };

    private BFSlogic bfSlogic=new BFSlogic();
    private Alogic alogic=new Alogic();

    public void startGame(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Оберіть метод пошуку:");
        System.out.println("1.Пошук вшир");
        System.out.println("2.Метод з евристикою");
        int choice=scanner.nextInt();
        List<int[][]>solution=null;
        long startTime=0;
        long endTime=0;
        if (choice==1) {
            solution=bfSlogic.findSolution(initialState);
        } else if (choice==2) {
            solution=alogic.findSolution(initialState);
        }
        if (solution != null) {
            System.out.println("Знайдено рішення:");
            printSolution(solution);
        } else {
            System.out.println("Рішення не знайдено.");
        }
        if (choice == 1) {
            startTime=System.nanoTime();
            solution=bfSlogic.findSolution(initialState);
            endTime=System.nanoTime();
        } else if (choice==2) {
            startTime=System.nanoTime();
            solution=alogic.findSolution(initialState);
            endTime=System.nanoTime();
        }
        long elapsedTime = (endTime-startTime)/1000000; // Перетворення наносекунд у мілісекунди
        System.out.println("Час виконання: "+elapsedTime+" мс");

    }

    private void printSolution(List<int[][]> solution) {
        for (int[][] state:solution) {
            printState(state);
            System.out.println();
        }
    }

    private void printState(int[][] state) {
        for (int i=0; i<state.length;i++) {
            for (int j=0; j<state[i].length; j++) {
                System.out.print(state[i][j] + " ");
            }
            System.out.println();
        }
    }

}

