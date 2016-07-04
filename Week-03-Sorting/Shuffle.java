// Author : Shin

import java.lang.Integer;
import java.lang.System;
import java.lang.UnsupportedOperationException;
import java.util.Random;
import java.util.Scanner;

class Shuffle
{
    private static boolean less(Integer x, Integer y)
    {
        return x < y;
    }

    private static boolean more(Integer x, Integer y)
    {
        return x > y;
    }

    private static void swap(Integer a[], int i, int j)
    {
        Integer temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    static void sort(Integer arr[])
    {
        int h = 1, N = arr.length;
        while( h < N/3 )
            h = 3 * h + 1;    //create seq 1,4,13,40,...
        while( h > 0 )
        {
            for( int i = h; i < N; ++i )
            {
                for( int j = i; j >= h && less(arr[j], arr[j - h]); j -= h )
                    swap(arr, j, j - h);
            }
            h = h/3;
        }
    }

    static void shuffle(Integer arr[])
    {
        int h = 1, N = arr.length;
        while( h < N/3 )
            h = 3 * h + 1;    //create seq 1,4,13,40,...

        Integer ref[] = new Integer[N];
        Random rand = new Random();
        for (int i = 0; i < N; ++i)
            ref[i] = rand.nextInt((1000 - 10) + 1) + 10;

        while( h > 0 )
        {
            for( int i = h; i < N; ++i )
            {
                for( int j = i; j >= h && less(ref[j], ref[j - h]); j -= h )
                {
                    swap(arr, j, j - h);
                    swap(ref, j, j - h);
                }
            }
            h = h/3;
        }
    }
}

class RunApp
{

    public static void main(String args[])
    {
        //Take Input
        Scanner readIn = new Scanner(System.in);
        Integer n = readIn.nextInt();
        Integer min = readIn.nextInt();
        Integer max = readIn.nextInt();
        readIn.close();
        if (min >= max)
            throw new UnsupportedOperationException();

        //Intitalize Array with sorted Values
        Integer array[] = new Integer[n];
        Random rand = new Random();
        for (int i = 0; i < array.length; ++i)
            array[i] = rand.nextInt((max - min) + 1) + min;
        Shuffle.sort(array);

        //Display Sorted Array
        for (Integer x : array)
            System.out.print(x + " ");
        System.out.print("\n");

        //Shuffle array
        Shuffle.shuffle(array);

        //Display Shuffled Array
        for (Integer x : array)
            System.out.print(x + " ");
        System.out.print("\n");
    }
}