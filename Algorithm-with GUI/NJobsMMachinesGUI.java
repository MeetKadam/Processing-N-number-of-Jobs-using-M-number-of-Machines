import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class WelcomeGui extends JFrame {
    public WelcomeGui() {
        JFrame f = new JFrame();
        f.setTitle("JobScheduler");
        JLabel l1 = new JLabel();
        l1.setText("JobScheduler");
        l1.setFont(new Font("Book Antiqua", Font.PLAIN, 60));
        l1.setBounds(50, 50, 400, 100);
        JButton b1 = new JButton();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(500, 300);
        b1.setText("Get Started");
        b1.setBackground(new Color(0xc3ced2));
        b1.setBounds(150, 200, 200, 50);

        f.setLayout(null);
        f.setVisible(true);
        f.add(l1);
        f.add(b1);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                f.dispose();
                new NJobsMMachinesGUI();
            }
        });
    }
}

class NJobsMMachinesGUI extends Frame {
    private TextField nomTextField;
    private TextField nojTextField;
    private TextArea ptTextArea;
    private TextArea resultTextArea;
    private Button calculateButton;

    public NJobsMMachinesGUI() {
        Label nomLabel = new Label("Enter the number of machines: ");
        nomTextField = new TextField(5);
        Label nojLabel = new Label("Enter the number of jobs: ");
        nojTextField = new TextField(5);
        Label ptLabel = new Label("Enter the processing times :(J1M1,J1M2,J1M3...) ");
        ptTextArea = new TextArea(5, 20);
        calculateButton = new Button("Calculate");
        resultTextArea = new TextArea("", 50, 50, TextArea.SCROLLBARS_VERTICAL_ONLY);

        Panel panel = new Panel();
        panel.setLayout(new GridLayout(4, 2));
        panel.add(nomLabel);
        panel.add(nomTextField);
        panel.add(nojLabel);
        panel.add(nojTextField);
        panel.add(ptLabel);
        panel.add(ptTextArea);
        panel.add(calculateButton);
        panel.add(resultTextArea);
        resultTextArea.append("Result:");

        setTitle("MPR Calculator");
        setSize(600, 400);
        add(panel);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateMPR();
            }
        });
    }

    private void calculateMPR() {
        try {
            int nom = Integer.parseInt(nomTextField.getText());
            int noj = Integer.parseInt(nojTextField.getText());
            String[] ptStrings = ptTextArea.getText().split("\\s+");
            if (ptStrings.length != noj * nom) {
                resultTextArea.setText("Invalid number of processing times.");
                return;
            }

            int[][] PT = new int[noj][nom];
            for (int i = 0; i < noj; i++) {
                for (int j = 0; j < nom; j++) {
                    PT[i][j] = Integer.parseInt(ptStrings[i * nom + j]);
                }
            }

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

            int maxmaxPT = Integer.MIN_VALUE;
            for (int i = 0; i < nom - 2; i++) {
                if (maxPT[i] > maxmaxPT) {
                    maxmaxPT = maxPT[i];
                }
            }

            if (mptFM < maxmaxPT && mptLM < maxmaxPT) {
                resultTextArea.setText("Cannot continue with this method. Change the inputs.");
                return;
            }

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
                if (mdG <= mdI) {
                    OS[count] = gJI + 1;
                    allocated[gJI] = true;
                    count++;
                }
                if (mdI < mdG) {
                    OS[noj - slot - 1] = iJI + 1;
                    allocated[iJI] = true;
                }
            }

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

            int[][] inTimes = new int[nom][noj];
            for (int i = 0; i < noj; i++) {
                int jobb = OS[i] - 1;
                for (int j = 0; j < nom; j++) {
                    inTimes[j][i] = outTimes[j][i] - PT[jobb][j];
                }
            }

            int minElapsed = outTimes[nom - 1][noj - 1];
            int[] idleTimes = new int[nom];
            for (int i = 0; i < nom; i++) {
                int lastOutTime = outTimes[i][noj - 1];
                int firstInTime = inTimes[i][0];
                idleTimes[i] = (minElapsed - lastOutTime) + firstInTime;

                for (int j = 1; j < noj; j++) {
                    int inTime = inTimes[i][j];
                    int outTime = outTimes[i][j - 1];
                    int idle = inTime - outTime;

                    if (idle > 0) {
                        idleTimes[i] += idle;
                    }
                }
            }

            StringBuilder resultBuilder = new StringBuilder();
            resultBuilder.append("Final Sequence: ");
            for (int i = 0; i < noj; i++) {
                resultBuilder.append("J").append(OS[i]);
                if (i < noj - 1) {
                    resultBuilder.append(", ");
                }
            }
            resultBuilder.append("\nMinimum Elapsed Time: ").append(minElapsed).append(" units\nIdle Times for Machines:\n");
            for (int i = 0; i < nom; i++) {
                resultBuilder.append("Machine ").append(i + 1).append(": ").append(idleTimes[i]).append(" units\n");
            }
            resultTextArea.setText(resultBuilder.toString());

        } catch (NumberFormatException ex) {
            resultTextArea.setText("Invalid input. Please enter valid numbers.");
        }
    }

    public static void main(String[] args) {
        new WelcomeGui();
    }
}

//14 10 4 6 18 12 12 8 10 20 10 8 10 12 16 16 6 6 4 12