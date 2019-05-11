/**
 * Physics Experiment
 * Author: Davi Dataram and Carolyn Yao
 * Does this compile or finish running within 5 seconds? Y/N
 */

/**
 * This class implements a greedy scheduler to assign students to steps in a
 * physics experiment. There are three test cases in the main method. Please
 * read through the whole file before attempting to code the solution.
 *
 * You will only be graded on code you add to the scheduleExperiments method. Do
 * not mess with the existing formatting and identation. You don't need to use
 * the helper methods, but if they come in handy setting up a custom test case,
 * feel free to use them.
 */
public class PhysicsExperiment {

  /**
   * The actual greedy scheduler you will be implementing!
   * 
   * @param numStudents The number of students who can participate, m
   * @param numSteps    The number of steps in the experiment, n
   * @param signUpTable An easy lookup tool, signUpTable[x][Y] = student X signed
   *                    up or did not sign up for step Y. Example:
   *                    signUpTable[1][3] = 1 if Student 1 signed up for Step 3
   *                    signUpTable[1][3] = 0 if Student 1 didn't sign up for Step
   *                    3
   * @return scheduleTable: a table similar to the signUpTable where
   *         scheduleTable[X][Y] = 1 means student X is assigned to step Y in an
   *         optimal schedule
   */
  public int[][] scheduleExperiments(int numStudents, int numSteps, int[][] signUpTable) {
    // Your scheduleTable is initialized as all 0's so far. Your code will put 1's
    // in the table in the right places based on the return description
    int[][] scheduleTable = new int[numStudents + 1][numSteps + 1];
    // Holds the value of every student through each iteration
    int consecutiveSteps[] = new int[numStudents + 1];
    for (int i = 0; i < consecutiveSteps.length; i++) {
      consecutiveSteps[i] = 0;
    }
    // Array to hold list of students and whether they were already selected
    boolean[] studentWasUsed = new boolean[numStudents + 1];
    // Value for students
    int students = 0;
    // Index that will hold which student can take the step
    int validStudent = -1;
    // Your code goes here
    // Array to loop through each step
    for (int i = 1; i <= numSteps; i++) {
      /*
       * int validStudent = findStudentStep(consecutiveSteps, i);
       * System.out.println(validStudent); if (consecutiveSteps[validStudent] > 0) {
       * scheduleTable[validStudent][i] = 1; consecutiveSteps[validStudent]--;
       * continue; } else {
       */

      // Check if there is a valid student set
      // Check if the current index has nother iteration
      if (validStudent > 0 && consecutiveSteps[validStudent] > 0) {

        // System.out.println("Step: " + (i));
        // System.out.println();
        // System.out.println((consecutiveSteps[validStudent]));
        // Set the scheduleTable to 1
        scheduleTable[validStudent][i] = 1;
        // Reduce the consecutive count by 1
        consecutiveSteps[validStudent]--;
        // System.out.println(i);
        // Run once the no more consective students are avilable
        // Reset all of the consecutive array
        if (consecutiveSteps[validStudent] == 0) {
          for (int o = 0; o < consecutiveSteps.length; o++) {
            consecutiveSteps[o] = 0;
          }
        }
        // Ignore rest of for loop
        continue;
      }
      // Loop through list of students
      for (students = 1; students <= numStudents; students++) {
        // Check if student is elligible to take current step
        if (studentHasStep(signUpTable, students, i)) {
          // If the student can take the step increase counter
          consecutiveSteps[students]++;
          // Loop through the remaining steps
          innerloop: for (int temp = i; temp <= numSteps; temp++) {
            // Check if student has the next step of the iteratino and if they hold the
            // current step
            if (studentHasNextStep(signUpTable, students, temp) && studentHasStep(signUpTable, students, i)) {
              // Increas counter for that student
              consecutiveSteps[students]++;
              continue;
            } else {
              // If they do not have the next step in loop or current step, break the loop
              break innerloop;
            }
          } // End of temp numSteps for loop
        } // End of studentHasStep() && studentDidNotGo() if statement
      } // End of students for loop
      // Retrieve the optimal index from loop
      validStudent = findStudentStep(consecutiveSteps, studentWasUsed);
      // Store the student variable in array to know they went already
      studentWasUsed[validStudent] = true;
      // Update the table
      scheduleTable[validStudent][i] = 1;
      // Reduce counter by 1
      consecutiveSteps[validStudent]--;
    }
    // } // End of numSteps for loop

    return scheduleTable;
  }

  /**
   * 
   * @param signUpTable Current table that holds whether students can do the step
   * @param students    Current Student
   * @param step        Current step
   * @param totalSteps
   * @return boolean
   */
  private boolean studentHasStep(int[][] signUpTable, int students, int step) {
    if (signUpTable[students][step] == 1) {
      return true;
    }
    return false;
  }

  /**
   * 
   * @param arr         Array of current students
   * @param alreadyWent Array of students containing boolean variable
   * @return index of student
   */
  private int findStudentStep(int[] arr, boolean[] alreadyWent) {
    // Variable to hold current value
    int temp = -1;
    // Variable to hold index
    int result = -1;
    // Variable that checks if there is a user who already went
    boolean set = false;
    // Loop through consecutive array
    for (int i = 0; i < arr.length; i++) {
      // Store value of arr at the index
      int num = arr[i];
      // Check if current user already went and add the info to the array
      if (alreadyWent[i] && num > temp) {
        set = true;
        result = i;
        temp = num;
      }
      if (!set && num > temp) {
        result = i;
        temp = num;
      }
      set = false;
    }

    return result;
  }

  private boolean studentDidGo() {
    return true;
  }

  private boolean studentHasNextStep(int[][] signUpTable, int students, int step) {
    try {
      if (signUpTable[students][step + 1] == 1) {
        return true;
      }
      return false;
    } catch (ArrayIndexOutOfBoundsException ae) {
      return false;
    }
  }

  /**
   * Makes the convenient lookup table based on the steps each student says they
   * can do
   * 
   * @param numSteps       the number of steps in the experiment
   * @param studentSignUps student sign ups ex: {{1, 2, 4}, {3, 5}, {6, 7}}
   * @return a lookup table so if we want to know if student x can do step y,
   *         lookupTable[x][y] = 1 if student x can do step y lookupTable[x][y] =
   *         0 if student x cannot do step y
   */
  public int[][] makeSignUpLookup(int numSteps, int[][] studentSignUps) {
    int numStudents = studentSignUps.length;
    int[][] lookupTable = new int[numStudents + 1][numSteps + 1];
    for (int student = 1; student <= numStudents; student++) {
      int[] signedUpSteps = studentSignUps[student - 1];
      for (int i = 0; i < signedUpSteps.length; i++) {
        lookupTable[student][signedUpSteps[i]] = 1;
      }
    }
    return lookupTable;
  }

  /**
   * Prints the optimal schedule by listing which steps each student will do
   * Example output is Student 1: 1, 3, 4
   * 
   * @param schedule The table of 0's and 1's of the optimal schedule, where
   *                 schedule[x][y] means whether in the optimal schedule student
   *                 x is doing step y
   */
  public void printResults(int[][] schedule) {
    for (int student = 1; student < schedule.length; student++) {
      int[] curStudentSchedule = schedule[student];
      System.out.print("Student " + student + ": ");
      for (int step = 1; step < curStudentSchedule.length; step++) {
        if (curStudentSchedule[step] == 1) {
          System.out.print(step + " ");
        }
      }
      System.out.println("");
    }
  }

  /**
   * This validates the input data about the experiment step sign-ups.
   * 
   * @param numStudents the number of students
   * @param numSteps    the number of steps
   * @param signUps     the data given about which steps each student can do
   * @return true or false whether the input sign-ups match the given number of
   *         students and steps, and whether all the steps are guaranteed at least
   *         one student.
   */
  public boolean inputsValid(int numStudents, int numSteps, int signUps[][]) {
    int studentSignUps = signUps.length;

    // Check if there are any students or signups
    if (numStudents < 1 || studentSignUps < 1) {
      System.out.println("You either did not put in any student or any signups");
      return false;
    }

    // Check if the number of students and sign-up rows matches
    if (numStudents != studentSignUps) {
      System.out.println("You input " + numStudents + " students but your signup suggests " + signUps.length);
      return false;
    }

    // Check that all steps are guaranteed in the signups
    int[] stepsGuaranteed = new int[numSteps + 1];
    for (int i = 0; i < studentSignUps; i++) {
      for (int j = 0; j < signUps[i].length; j++) {
        stepsGuaranteed[signUps[i][j]] = 1;
      }
    }
    for (int step = 1; step <= numSteps; step++) {
      if (stepsGuaranteed[step] != 1) {
        System.out.println("Your signup is incomplete because not all steps are guaranteed.");
        return false;
      }
    }

    return true;
  }

  /**
   * This sets up the scheduling test case and calls the scheduling method.
   * 
   * @param numStudents the number of students
   * @param numSteps    the number of steps
   * @param signUps     which steps each student can do, in order of students and
   *                    steps
   */
  public void makeExperimentAndSchedule(int experimentNum, int numStudents, int numSteps, int[][] signUps) {
    System.out.println("----Experiment " + experimentNum + "----");
    if (!inputsValid(numStudents, numSteps, signUps)) {
      System.out.println("Experiment signup info is invalid");
      return;
    }
    int[][] signUpsLookup = makeSignUpLookup(numSteps, signUps);
    int[][] schedule = scheduleExperiments(numStudents, numSteps, signUpsLookup);
    printResults(schedule);
    System.out.println("");
  }

  /**
   * You can make additional test cases using the same format. In fact the helper
   * functions I've provided will even check your test case is set up correctly.
   * Do not touch any of of the existing lines. Just make sure to comment out or
   * delete any of your own test cases when you submit. The three experiment test
   * cases existing in this main method should be the only output when running
   * this file.
   */
  public static void main(String args[]) {
    PhysicsExperiment pe = new PhysicsExperiment();

    // Experiment 1: Example 1 from README, 3 students, 6 steps:
    int[][] signUpsExperiment1 = { { 1, 2, 3, 5 }, { 2, 3, 4 }, { 1, 4, 5, 6 } };
    pe.makeExperimentAndSchedule(1, 3, 6, signUpsExperiment1);

    // Experiment 2: Example 2 from README, 4 students, 8 steps
    int[][] signUpsExperiment2 = { { 5, 7, 8 }, { 2, 3, 4, 5, 6 }, { 1, 5, 7, 8 }, { 1, 3, 4, 8 } };
    pe.makeExperimentAndSchedule(2, 4, 8, signUpsExperiment2);

    // Experiment 3: Another test case, 5 students, 11 steps
    int[][] signUpsExperiment3 = { { 7, 10, 11 }, { 8, 9, 10 }, { 2, 3, 4, 5, 7 }, { 1, 5, 6, 7, 8 }, { 1, 3, 4, 8 } };
    pe.makeExperimentAndSchedule(3, 5, 11, signUpsExperiment3);
  }
}
