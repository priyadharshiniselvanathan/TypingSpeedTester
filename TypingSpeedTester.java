import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;


public class TypingSpeedTester {

    // Sentence list
    static String[] sentences = {
        "The quick brown fox jumps over the lazy dog.",
        "Java is a powerful programming language.",
        "Typing fast and accurately takes practice.",
        "Swing is used for creating GUIs in Java.",
        "Always test your code before submitting."
    };

    // Store history
    static java.util.List<String> history = new ArrayList<>();

    public static void main(String[] args) {
        // Frame setup
        JFrame frame = new JFrame("Typing Speed Tester");
        frame.setSize(800, 500);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        // Heading
        JLabel heading = new JLabel("Typing Speed Tester");
        heading.setFont(new Font("Arial", Font.BOLD, 26));
        heading.setBounds(270, 10, 400, 40);
        heading.setForeground(new Color(0, 102, 204));
        frame.add(heading);

        // Random sentence
        JLabel sentenceLabel = new JLabel("Type this:");
        sentenceLabel.setBounds(30, 60, 100, 20);
        frame.add(sentenceLabel);

        JTextArea sentenceArea = new JTextArea();
        sentenceArea.setBounds(30, 85, 720, 40);
        sentenceArea.setLineWrap(true);
        sentenceArea.setWrapStyleWord(true);
        sentenceArea.setEditable(false);
        sentenceArea.setBackground(new Color(235, 235, 235));
        frame.add(sentenceArea);

        // Typing area
        JLabel typeHere = new JLabel("Start typing below:");
        typeHere.setBounds(30, 140, 200, 20);
        frame.add(typeHere);

        JTextArea typingArea = new JTextArea();
        typingArea.setBounds(30, 165, 720, 60);
        typingArea.setLineWrap(true);
        typingArea.setWrapStyleWord(true);
        frame.add(typingArea);

        // Result label
        JLabel resultLabel = new JLabel("Your result will appear here.");
        resultLabel.setBounds(30, 280, 600, 30);
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        frame.add(resultLabel);

        // Score history label
        JLabel historyLabel = new JLabel("<html><u>Score History</u></html>");
        historyLabel.setBounds(30, 310, 200, 20);
        historyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(historyLabel);

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setBackground(new Color(245, 245, 245));
        historyArea.setFont(new Font("Courier New", Font.PLAIN, 12));

        // Add scroll bar
        JScrollPane scrollPane = new JScrollPane(historyArea);
        scrollPane.setBounds(30, 335, 720, 100);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane);

        // Buttons
        JButton doneButton = new JButton("Done");
        doneButton.setBounds(320, 240, 100, 30);
        doneButton.setBackground(new Color(0, 153, 76));
        doneButton.setForeground(Color.WHITE);
        frame.add(doneButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(440, 240, 100, 30);
        resetButton.setBackground(new Color(204, 0, 0));
        resetButton.setForeground(Color.WHITE);
        frame.add(resetButton);

        JButton exportButton = new JButton("Export");
        exportButton.setBounds(560, 240, 100, 30);
        exportButton.setBackground(new Color(0, 102, 204));
        exportButton.setForeground(Color.WHITE);
        frame.add(exportButton);


        // Time tracking
        final long[] startTime = new long[1];
        final long[] endTime = new long[1];

        // Random sentence selection
        Random rand = new Random();
        String currentSentence = sentences[rand.nextInt(sentences.length)];
        sentenceArea.setText(currentSentence);

        // Start timer when typing begins
        typingArea.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (startTime[0] == 0) {
                    startTime[0] = System.currentTimeMillis();
                }
            }
        });
        //Export button logic
        exportButton.addActionListener(e -> {
        try {
        FileWriter writer = new FileWriter("typing_results.txt");
        writer.write(historyArea.getText());
        writer.close();
        JOptionPane.showMessageDialog(frame, "Results exported to typing_results.txt!");
        } catch (IOException ex) {
        JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage());
        }
        });


        // Done button logic
        doneButton.addActionListener(e -> {
            endTime[0] = System.currentTimeMillis();
            String typedText = typingArea.getText();
            long timeTaken = (endTime[0] - startTime[0]) / 1000; // in seconds
            int wordCount = typedText.split("\\s+").length;

            double wpm = (wordCount / (timeTaken / 60.0));

            int correctChars = 0;
            for (int i = 0; i < Math.min(currentSentence.length(), typedText.length()); i++) {
                if (currentSentence.charAt(i) == typedText.charAt(i)) {
                    correctChars++;
                }
            }
            double accuracy = ((double) correctChars / currentSentence.length()) * 100;

            String result = String.format("Time: %ds | Speed: %.2f WPM | Accuracy: %.2f%%", timeTaken, wpm, accuracy);
            resultLabel.setText(result);

            // Add to history
            history.add(result);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < history.size(); i++) {
                sb.append((i + 1)).append(". ").append(history.get(i)).append("\n");
            }
            historyArea.setText(sb.toString());
        });

        // Reset button logic
        resetButton.addActionListener(e -> {
            typingArea.setText("");
            resultLabel.setText("Your result will appear here.");
            startTime[0] = 0;
            endTime[0] = 0;

            // Get new random sentence
            String newSentence = sentences[rand.nextInt(sentences.length)];
            sentenceArea.setText(newSentence);
        });

        frame.setVisible(true);
    }
}
