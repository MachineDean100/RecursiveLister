import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.SwingWorker;

public class RecursiveListerApp extends JFrame implements ActionListener {
    private JButton startButton;
    private JButton quitButton;
    private JLabel titleLabel;
    private JTextArea textArea;
    private JScrollPane scrollPane;

    public RecursiveListerApp() {
        setTitle("Recursive File Lister");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        initComponents();

        setVisible(true);
    }

    private void initComponents() {
        titleLabel = new JLabel("Recursive File Lister", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel buttonsPanel = new JPanel();
        startButton = new JButton("Start");
        quitButton = new JButton("Quit");

        startButton.addActionListener(this);
        quitButton.addActionListener(this);

        buttonsPanel.add(startButton);
        buttonsPanel.add(quitButton);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);

        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            selectDirectoryAndListFiles();
        } else if (e.getSource() == quitButton) {
            System.exit(0);
        }
    }

    private void selectDirectoryAndListFiles() {
        JFileChooser directoryChooser = new JFileChooser();
        directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        directoryChooser.setDialogTitle("Select a Directory to List Files");

        int result = directoryChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDir = directoryChooser.getSelectedFile();
            textArea.setText("");

            FileListerWorker worker = new FileListerWorker(selectedDir);
            worker.execute();
        }
    }

    private class FileListerWorker extends SwingWorker<Void, String> {
        private File dir;

        public FileListerWorker(File dir) {
            this.dir = dir;
        }

        @Override
        protected Void doInBackground() throws Exception {
            listFilesRecursiveHelper(dir, "");
            return null;
        }

        private void listFilesRecursiveHelper(File dir, String indent) {
            if (dir.isDirectory()) {
                publish(indent + "[Directory] " + dir.getName() + "\n");
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        listFilesRecursiveHelper(file, indent + "   ");
                    }
                }
            } else {
                publish(indent + dir.getName() + "\n");
            }
        }

        @Override
        protected void process(java.util.List<String> chunks) {
            for (String chunk : chunks) {
                textArea.append(chunk);
            }
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new RecursiveListerApp());
    }
}
