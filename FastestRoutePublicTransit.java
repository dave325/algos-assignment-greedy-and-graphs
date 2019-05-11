/**
 * Public Transit
 * Author: David Dataram and Carolyn Yao
 * Does this compile? Y/N
 */

/**
 * This class contains solutions to the Public, Public Transit problem in the
 * shortestTimeToTravelTo method. There is an existing implementation of a
 * shortest-paths algorithm. As it is, you can run this class and get the
 * solutions from the existing shortest-path algorithm.
 */
public class FastestRoutePublicTransit {

  /**
   * The algorithm that could solve for shortest travel time from a station S to a
   * station T given various tables of information about each edge (u,v)
   *
   * @param S         the s th vertex/station in the transit map, start From
   * @param T         the t th vertex/station in the transit map, end at
   * @param startTime the start time in terms of number of minutes from 5:30am
   * @param lengths   lengths[u][v] The time it takes for a train to get between
   *                  two adjacent stations u and v
   * @param first     first[u][v] The time of the first train that stops at u on
   *                  its way to v, int in minutes from 5:30am
   * @param freq      freq[u][v] How frequently is the train that stops at u on
   *                  its way to v
   * @return shortest travel time between S and T
   */
  public void myShortestTravelTime(int S, int T, int startTime, int[][] lengths, int[][] first, int[][] freq) {
    // Your code along with comments here. Feel free to borrow code from any
    // of the existing method. You can also make new helper methods.
    int numVertices = lengths[0].length;

    // This is the array where we'll store all the final shortest times
    int[] times = new int[numVertices];

    // processed[i] will true if vertex i's shortest time is already finalized
    Boolean[] processed = new Boolean[numVertices];

    // Initialize all distances as INFINITE and processed[] as false
    for (int v = 0; v < numVertices; v++) {
      times[v] = Integer.MAX_VALUE;
      processed[v] = false;
    }

    // Distance of source vertex from itself is always 0
    times[S] = 0;
    //The total Minutes after each vertice is relax 
    int totalMin = startTime;
    // Failsafe in case the destination oes not exist 
    if(numVertices <= T){
      System.out.println("T does not exist.");
      return;
    }
    // Find shortest path to all the vertices
    for (int count = 0; count < numVertices - 1; count++) {
      // Pick the minimum distance vertex from the set of vertices not yet processed.
      // u is always equal to source in first iteration.
      // Mark u as processed.
      int u = findNextToProcess(times, processed);
      processed[u] = true;
      // Temp - Stores the calculations of all edges 
      // Min - Stores the collective minimum value for each edge
      int temp = 0, min = Integer.MAX_VALUE;
      // Update time value of all the adjacent vertices of the picked vertex.
      for (int v = 0; v < numVertices; v++) {
        // Update time[v] only if is not processed yet, there is an edge from u to v,
        // and total weight of path from source to v through u is smaller than current
        // value of time[v]
        //System.out.println(u + ": " +totalMin);
        //Returns the value for each edge in the case of first, freq, and length
        temp = findFullValue(totalMin, processed, u, v, lengths, first, freq);
        //System.out.println("Final - " + u + " - " + v + ": " + temp);
        if (!processed[v] && lengths[u][v] != 0 && times[u] != Integer.MAX_VALUE && temp != Integer.MIN_VALUE && temp < times[v]) {

          times[v] = temp;
          // If the temp value is greater than the min value, store the numbers
          if (min > temp) {
            min = temp;
          }
        }
      }
      if(min == Integer.MAX_VALUE){
        min = 0;
      }
      // Increment the totalMin by the collective min value from each vertice
      totalMin += min;
    }
    System.out.println("Station " + S + " to Station " + T);
    printShortestTimes(times, times[T]);
  }

  /**
   * 
   * @param time
   * @param processed
   * @param u
   * @param v
   * @param lengths
   * @param first
   * @param freq
   * @return int time
   */
  public int findFullValue(int time, Boolean[] processed, int u, int v, int[][] lengths, int[][] first, int[][] freq) {
    int min = 0;
    // int minIndex = -1;
    int temp = 0;
    //System.out.println("\nVertice: " + u + " to " + v + "\n");
    min = Integer.MAX_VALUE;
    //System.out.println("Time: " + time);
    if (u != v) {
      // for (int i = 0; i < lengths[u].length; i++) {
      // System.out.println("First: " + v + ": " + first[u][v]);
      //System.out.println("Lengths: " + v + ": " + lengths[u][v]);
      // Check if an edge exists and the destination has not been processed
      if (lengths[u][v] > 0 && processed[v] == false) {
        // Store the first occurance from u to v
        temp = first[u][v];
        // If the first occurance is before the current time, 
        // add onto the temp time until the next train comes,
        // based on the frequency 
        while (time > temp) {
          temp += freq[u][v];
        }
        //System.out.println("Temp(" + v + ") - " + temp);
       
        /**
         * If the min time is greater than the difference between the
         * temp and the current minute plus the length
         * Store the temp time 
         */
        if (min > (temp - time) + lengths[u][v]) {
          min = (temp - time) + lengths[u][v];
        }
        //System.out.println("Min(" + v + ") - " + min);
      }else{
        min = 0;
      }
      // System.out.println("Min value: " + min);
      /*
       * if (processed[i] == false && times[i] <= min) { min = times[i]; minIndex = i;
       * }
       */
      // }
    } else {
      min = 0;
    }
    // Increment the current time to the min time
    time += min;
     //System.out.println("Time: " + time);
    // System.out.println("Lengths - " + min);
    return time;
  }

  /**
   * Finds the vertex with the minimum time from the source that has not been
   * processed yet.
   * 
   * @param times     The shortest times from the source
   * @param processed boolean array tells you which vertices have been fully
   *                  processed
   * @return the index of the vertex that is next vertex to process
   */
  public int findNextToProcess(int[] times, Boolean[] processed) {
    int min = Integer.MAX_VALUE;
    int minIndex = -1;

    for (int i = 0; i < times.length; i++) {
      if (processed[i] == false && times[i] <= min) {
        min = times[i];
        minIndex = i;
      }
    }
    return minIndex;
  }

  public void printShortestTimes(int times[], int totalTime) {
    System.out.println("Vertex Distances (time) from Source");
    System.out.println("Total Time: " + totalTime);
    for (int i = 0; i < times.length; i++)
      System.out.println(i + ": " + times[i] + " minutes");

    if (totalTime > 0) {
      System.out.println("Total Time: " + totalTime);
    }
  }

  /**
   * Given an adjacency matrix of a graph, implements
   * 
   * @param graph  The connected, directed graph in an adjacency matrix where if
   *               graph[i][j] != 0 there is an edge with the weight graph[i][j]
   * @param source The starting vertex
   */
  public void shortestTime(int graph[][], int source) {
    int numVertices = graph[0].length;

    // This is the array where we'll store all the final shortest times
    int[] times = new int[numVertices];

    // processed[i] will true if vertex i's shortest time is already finalized
    Boolean[] processed = new Boolean[numVertices];

    // Initialize all distances as INFINITE and processed[] as false
    for (int v = 0; v < numVertices; v++) {
      times[v] = Integer.MAX_VALUE;
      processed[v] = false;
    }

    // Distance of source vertex from itself is always 0
    times[source] = 0;

    // Find shortest path to all the vertices
    for (int count = 0; count < numVertices - 1; count++) {
      // Pick the minimum distance vertex from the set of vertices not yet processed.
      // u is always equal to source in first iteration.
      // Mark u as processed.
      int u = findNextToProcess(times, processed);
      processed[u] = true;

      // Update time value of all the adjacent vertices of the picked vertex.
      for (int v = 0; v < numVertices; v++) {
        // Update time[v] only if is not processed yet, there is an edge from u to v,
        // and total weight of path from source to v through u is smaller than current
        // value of time[v]
        if (!processed[v] && graph[u][v] != 0 && times[u] != Integer.MAX_VALUE && times[u] + graph[u][v] < times[v]) {
          times[v] = times[u] + graph[u][v];
        }
      }
    }

    printShortestTimes(times, 0);
  }

  public static void main(String[] args) {
    /* length(e) */
    int lengthTimeGraph[][] = new int[][] { { 0, 4, 0, 0, 0, 0, 0, 8, 0 }, { 4, 0, 8, 0, 0, 0, 0, 11, 0 },
        { 0, 8, 0, 7, 0, 4, 0, 0, 2 }, { 0, 0, 7, 0, 9, 14, 0, 0, 0 }, { 0, 0, 0, 9, 0, 10, 0, 0, 0 },
        { 0, 0, 4, 14, 10, 0, 2, 0, 0 }, { 0, 0, 0, 0, 0, 2, 0, 1, 6 }, { 8, 11, 0, 0, 0, 0, 1, 0, 7 },
        { 0, 0, 2, 0, 0, 0, 6, 7, 0 } };

    int newLength[][] = new int[][] { { 0, 5, 1 }, { 0, 0, 5 }, { 0, 0, 1 } };

    int firstTimeGraph[][] = new int[][] { { 0, 3, 10 }, { 0, 0, 5 }, { 0, 0, 10 } };
    int freqTimeGraph[][] = new int[][] { { 0, 2, 10 }, { 0, 0, 3 }, { 0, 0, 10 } };
    FastestRoutePublicTransit t = new FastestRoutePublicTransit();
    // t.shortestTime(lengthTimeGraph, 0);
    t.myShortestTravelTime(0, 2, 0, newLength, firstTimeGraph, freqTimeGraph);

    // You can create a test case for your implemented method for extra credit below
  }
}