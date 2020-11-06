package forkjoin;

import java.util.Random;

public class IterativeQuickSort {

    Random rand=new Random();
    int taille = 50;
    int max = taille-1;
    int min = 0;
    int tableau[]=new int[ taille];

    IterativeQuickSort(){
        for (int i=0; i<taille; i++) {
            tableau[i] = rand.nextInt(2 * max - min + 1) + min;
        }
    }
    void swap(int arr[], int i, int j)
    {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    /* This function is same in both iterative and
       recursive*/
    int partition(int arr[], int l, int h)
    {
        int x = arr[h];
        int i = (l - 1);

        for (int j = l; j <= h - 1; j++) {
            if (arr[j] <= x) {
                i++;
                // swap arr[i] and arr[j]
                swap(arr, i, j);
            }
        }
        // swap arr[i+1] and arr[h]
        swap(arr, i + 1, h);
        return (i + 1);
    }

    // Sorts arr[l..h] using iterative forkjoincorrection.QuickSort
    void QuickSort()
    {
        // create auxiliary stack
        int stack[] = new int[max - min + 1];

        // initialize top of stack
        int top = -1;

        // push initial values in the stack
        stack[++top] = min;
        stack[++top] = max;

        // keep popping elements until stack is not empty
        while (top >= 0) {
            // pop h and l
            max = stack[top--];
            min = stack[top--];

            // set pivot element at it's proper position
            int p = partition(tableau, min, max);

            // If there are elements on left side of pivot,
            // then push left side to stack
            if (p - 1 > min) {
                stack[++top] = min;
                stack[++top] = p - 1;
            }

            // If there are elements on right side of pivot,
            // then push right side to stack
            if (p + 1 < max) {
                stack[++top] = p + 1;
                stack[++top] = max;
            }
        }
    }

    // A utility function to print contents of arr
    void printArr(int arr[], int n)
    {
        int i;
        for (i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");
    }

    public int[] getTableau() {
        return tableau;
    }

    public int getTaille() {
        return taille;
    }

    // Driver code to test above
    public static void main(String args[])
    {
        IterativeQuickSort ob = new IterativeQuickSort();
        System.out.println("IterativeQuickSort");
        System.out.print("Entree : ");
        ob.printArr(ob.getTableau(), ob.getTaille());
        System.out.println("");
        ob.QuickSort();
        System.out.print("Sortie : ");
        ob.printArr(ob.getTableau(), ob.getTaille());
    }


}
