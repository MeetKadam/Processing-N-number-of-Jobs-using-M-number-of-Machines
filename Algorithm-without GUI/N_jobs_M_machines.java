import java.util.*;

public class N_jobs_M_machines {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter the number of machines: ");
            int nom = sc.nextInt();
            System.out.print("Enter the number of jobs: ");
            int noj = sc.nextInt();
            int[][] PT = new int[noj][nom];
            for (int i = 0; i < noj; i++) {
                for (int j = 0; j < nom; j++) {
                    System.out.print("Enter processing time for Job " + (i + 1) + " on Machine " + (j + 1) + ": ");
                    PT[i][j] = sc.nextInt();
                }
            }
             // min PT
            int mptFM = Integer.MAX_VALUE;
            int mptLM = Integer.MAX_VALUE;
            for (int i = 0; i < noj; i++) {
                int ptFM = PT[i][0];
                int ptLM = PT[i][nom - 1];
                if (ptFM < mptFM) {
                    mptFM = ptFM;
                }
                 if (ptLM < mptLM) {
                    mptLM = ptLM;
                }
            }
             //  max PT
            int[] maxPT = new int[nom - 2];
             for (int j = 1; j < nom - 1; j++) { 
                int tmaxPT = Integer.MIN_VALUE;
                for (int i = 0; i < noj; i++) {
                    int tpt = PT[i][j];
                    if (tpt > tmaxPT) {
                        tmaxPT = tpt;
                    }
                }
                maxPT[j - 1] = tmaxPT;
            }
             // max of max PT
            int maxmaxPT = Integer.MIN_VALUE;
            for (int i = 0 ; i < nom-2 ; i++ ) {
                if ( maxPT[i] > maxmaxPT) {
                    maxmaxPT = maxPT[i] ;
                }
            }
            System.out.println("\nMinimum processing time on the first machine: " + mptFM);
            System.out.println("Minimum processing time on the last machine: " + mptLM);
            System.out.println("\nMaximum procesing time for each machine (except first and last):");
            for (int j = 1; j < nom - 1; j++) { 
                System.out.println("Machine " + (j + 1) + ": " + maxPT[j - 1]);
            }
             System.out.println("Maximum of the maximum processing times: " + maxmaxPT);

            // condition
            if (mptFM < maxmaxPT && mptLM < maxmaxPT) {
                System.out.println("\nCannot continue with this method. Chnage the inputs.");
                return;
            }
            
            //machine G and I claculations
            int[] machineG = new int[noj];
            int[] machineI = new int[noj];
            for (int i = 0; i < noj; i++) {
                for (int j = 0; j < nom - 1; j++) {
                    machineG[i] = machineG[i] + PT[i][j];
                }
                for (int j = 1; j < nom; j++) {
                    machineI[i] = machineI[i] + PT[i][j];
                }
            }
             System.out.println("\nData stored in Machine G:");
            for (int i = 0; i < noj; i++) {
                System.out.println("Job " + (i + 1) + ": " + machineG[i]);
            }
             System.out.println("\nData stored in Machine I:");
            for (int i = 0; i < noj; i++) {
                System.out.println("Job " + (i + 1) + ": " + machineI[i]);
            }
            
            // optimal sequence
            boolean[] allocated = new boolean[noj];
            int[] OS = new int[noj];
            int count = 0;
             for (int slot = 0; slot < noj; slot++) {
                int mdI = Integer.MAX_VALUE;
                int mdG = Integer.MAX_VALUE;
                int iJI = -1;
                int gJI = -1;

                 for (int i = 0; i < noj; i++) {
                    if (!allocated[i] && machineG[i] < mdG) {
                        mdG = machineG[i];
                        gJI = i;
                    }
                }
                for (int i = 0; i < noj; i++) {
                    if (!allocated[i] && machineI[i] < mdI) {
                        mdI = machineI[i];
                        iJI = i;
                    }
                }
                if(mdG <= mdI) {
                    OS[count] = gJI + 1;
                    allocated[gJI] = true ;
                    count ++ ;
                }
                if(mdI < mdG) {
                    OS[noj-slot-1] = iJI + 1;
                    allocated[iJI] = true;
                } 
            }
            System.out.println("\n\nOptimal Sequence:");
            for (int i = 0; i < noj; i++) {
                System.out.print("  " +  OS[i]);
            }


            // outTimes
          int[][] outTimes = new int[nom][noj];
    for (int i = 0; i < noj; i++) {
        int job = OS[i] - 1; 
        for (int j = 0; j < nom; j++) {
            if (i == 0) {
                if (j == 0) {
                    outTimes[j][i] = PT[job][j];
                } else {
                    outTimes[j][i] = outTimes[j - 1][i] + PT[job][j];
                }
            } else {
                if (j == 0) {
                    outTimes[j][i] = outTimes[j][i - 1] + PT[job][j];
                } else {
                    outTimes[j][i] = Math.max(outTimes[j - 1][i], outTimes[j][i - 1]) + PT[job][j];
                }
            }
        }
    }
     System.out.println();
     System.out.println();
    System.out.println("Values in outTimes");
    for (int i = 0; i < nom; i++) {
        for (int j = 0; j < noj; j++) {
            System.out.print(outTimes[i][j] + " ");
        }
        System.out.println(); 
    }
     //inTimes
  int[][] inTimes = new int[nom][noj];

for (int i = 0; i < noj; i++) {
     int jobb = OS[i] - 1; 
    for (int j = 0; j < nom; j++) {
        inTimes[j][i] = outTimes[j][i] - PT[jobb][j];
    }
}
System.out.println("\nValues in inTimes array:");
for (int i = 0; i < nom; i++) {
    for (int j = 0; j < noj; j++) {
        System.out.print(inTimes[i][j] + " ");
    }
    System.out.println(); 
}

    // minimum elapsed time
int minElapsed = outTimes[nom - 1][noj - 1];
System.out.println("\nMinimum Elapsed Time: " + minElapsed);

// idle time 
int[] idleTimes = new int[nom];
for (int i = 0; i < nom; i++) {
    int lastOutTime = outTimes[i][noj - 1];
    int firstInTime = inTimes[i][0];
    idleTimes[i] = (minElapsed - lastOutTime) + firstInTime ;
    
    for (int j = 1; j < noj; j++) {
        int inTime = inTimes[i][j];
        int outTime = outTimes[i][j - 1];
        int idle = inTime - outTime;
        
        if (idle > 0) {
            idleTimes[i] = idleTimes[i] + idle;
        }
    }
}
System.out.println("\nIdle Time for Each Machine:");
for (int i = 0; i < nom; i++) {
    System.out.println("Machine " + (i + 1) + ": " + idleTimes[i]);
}

        }
    }
}



/*Enter the number of machines: 5
Enter the number of jobs: 4
Enter processing time for Job 1 on Machine 1: 14 
Enter processing time for Job 1 on Machine 2: 10
Enter processing time for Job 1 on Machine 3: 4
Enter processing time for Job 1 on Machine 4: 6
Enter processing time for Job 1 on Machine 5: 18
Enter processing time for Job 2 on Machine 1: 12
Enter processing time for Job 2 on Machine 2: 12
Enter processing time for Job 2 on Machine 3: 8
Enter processing time for Job 2 on Machine 4: 10
Enter processing time for Job 2 on Machine 5: 20
Enter processing time for Job 3 on Machine 1: 10
Enter processing time for Job 3 on Machine 2: 8
Enter processing time for Job 3 on Machine 3: 10
Enter processing time for Job 3 on Machine 4: 12
Enter processing time for Job 3 on Machine 5: 16
Enter processing time for Job 4 on Machine 1: 16
Enter processing time for Job 4 on Machine 2: 6
Enter processing time for Job 4 on Machine 3: 6
Enter processing time for Job 4 on Machine 4: 4
Enter processing time for Job 4 on Machine 5: 12

Minimum processing time on the first machine: 10
Minimum processing time on the last machine: 12

Maximum procesing time for each machine (except first and last):
Machine 2: 12
Machine 3: 10
Machine 4: 12
Maximum of the maximum processing times: 12

Data stored in Machine G:
Job 1: 34
Job 2: 42
Job 3: 40
Job 4: 32

Data stored in Machine I:
Job 1: 38
Job 2: 50
Job 3: 46
Job 4: 28


Optimal Sequence:
  1  3  2  4

Values in outTimes
14 24 36 52
24 32 48 58
28 42 56 64
34 54 66 70
52 70 90 102

Values in inTimes array:
0 14 24 36 
14 24 36 52
24 32 48 58
28 42 56 66
34 54 70 90

Minimum Elapsed Time: 102

Idle Time for Each Machine:
Machine 1: 50
Machine 2: 66
Machine 3: 74
Machine 4: 70
Machine 5: 36 */