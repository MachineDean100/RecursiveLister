import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class FileListerWorker extends SwingWorker<Void, String> {
    private JTextArea textArea;
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

