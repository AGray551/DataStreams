import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataStreamsFrame extends JFrame {
    JPanel frame;

    JPanel leftPnl;
    JTextField fileName;
    JTextArea ogFile;

    JPanel rightPnl;
    JTextArea newFile;

    JPanel txtPnl;
    JTextField searchString;

    JPanel botPnl;
    JButton load;
    JButton search;
    JButton quit;

    File loadedFile; // Added field to store the currently loaded file

    public DataStreamsFrame() {
        setLayout(new BorderLayout());
        frame = new JPanel();

        createLeftPnl();
        createRightPnl();
        createTxtPnl();
        createBotPnl();

        frame.setLayout(new BorderLayout());
        frame.add(txtPnl, BorderLayout.NORTH);
        frame.add(leftPnl, BorderLayout.WEST);
        frame.add(rightPnl, BorderLayout.EAST);
        frame.add(botPnl, BorderLayout.SOUTH);

        add(frame);
        setTitle("Data Streams");
        setLocationRelativeTo(null);
        setLocation(570, 250);
        setSize(new Dimension(650, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createLeftPnl() {
        leftPnl = new JPanel(new BorderLayout());

        fileName = new JTextField();
        fileName.setEditable(false);
        leftPnl.add(fileName, BorderLayout.NORTH);

        ogFile = new JTextArea(25, 20);
        ogFile.setEditable(false);
        ogFile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        ogFile.setLineWrap(true);
        ogFile.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(ogFile);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        leftPnl.add(scrollPane, BorderLayout.CENTER);
    }


    private void createRightPnl() {
        rightPnl = new JPanel(new BorderLayout());

        newFile = new JTextArea(25, 20);
        newFile.setEditable(false);
        newFile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        newFile.setLineWrap(true);
        newFile.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(newFile);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        rightPnl.add(scrollPane, BorderLayout.CENTER);
    }

    private void createTxtPnl() {
        txtPnl = new JPanel(new BorderLayout());

        txtPnl.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        searchString = new JTextField("Enter Search Term Here");
        txtPnl.add(searchString, BorderLayout.SOUTH);
    }

    private void createBotPnl() {
        botPnl = new JPanel(new FlowLayout());

        load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    loadedFile = fileChooser.getSelectedFile();
                    fileName.setText(loadedFile.getName());
                    try (Scanner scanner = new Scanner(loadedFile)) {
                        ogFile.setText("");
                        while (scanner.hasNextLine()) {
                            ogFile.append(scanner.nextLine() + "\n");
                        }
                    } catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(null, "File not found: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        botPnl.add(load);

        search = new JButton("Search");
        search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchKeyword = searchString.getText();
                if (loadedFile != null && loadedFile.exists()) {
                    try {
                        List<String> matchingLines = Files.lines(Paths.get(loadedFile.getPath()))
                                .filter(line -> line.contains(searchKeyword))
                                .collect(Collectors.toList());
                        if (!matchingLines.isEmpty()) {
                            newFile.setText("Search Results for: " + searchKeyword + "\n");
                            for (String line : matchingLines) {
                                newFile.append(line + "\n");
                            }
                        } else {
                            newFile.setText("No results found for: " + searchKeyword);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error reading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No loaded file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        botPnl.add(search);

        quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        botPnl.add(quit);

    }
}
