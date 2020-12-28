package ui;

import exception.InvalidLabelException;
import model.Task;
import model.ToDoList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

// References:
// 1.  get the idea of DefaultListModel, JScrollPane from ListDemo.java
//     link: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/index.html#ListDemo
// 2.  learn how to make dialogs from website
//     link: https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
// 3.  learn how to popup confirm dialog before closing JFrame from youtube video
//     "show confirm dialog before closing JFrame - JAVA" made by Mohamed MotYim
//     link: https://www.youtube.com/watch?v=GTFQi5McbzE
// 4.  learn about listCellRenderer from website
//     link: https://docs.oracle.com/javase/8/docs/api/javax/swing/ListCellRenderer.html

// Represents a GUI frame
public class Gui extends JFrame implements ListSelectionListener {

    private static final String ADD_STRING = "Add";
    private static final String DEL_STRING = "Delete";
    private static final String UNCOM_STRING = "View Uncompleted";
    private static final String COM_STRING = "View Completed";
    private static final String COMPLETE_STRING = "Complete";
    private static final String UNCOM_LIST = "UNCOMPLETED To-do list";
    private static final String COM_LIST = "COMPLETED To-do list";
    private static final String STORE_DESTINATION = "./data/ToDoList.json";
    private JList list;
    private ToDoList toDoList;
    private DefaultListModel uncompleted;
    private DefaultListModel completed;
    private JScrollPane listScrollPane;
    private String title;
    // 0 stands for displaying uncompleted list, 1 for completed list
    private int listStatus;
    private JLabel curList;
    private JButton addButton;
    private JButton deleteButton;
    private JButton viewUncompletedButton;
    private JButton viewCompletedButton;
    private JButton completeButton;
    private JsonWriter writer;
    private JsonReader reader;

    // EFFECTS: initialize all the components
    public Gui() {
        super("My to-do list");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));

        listStatus = 0;
        curList = new JLabel(UNCOM_LIST);
        curList.setFont(new Font("Courier New", Font.BOLD, 20));


        initializeListModel();
        initializeAddButton();
        initializeDeleteButton();
        initializeViewButtons();
        initializeCompleteButton();
        initializeJson();
        loadBeforeStart();
        saveBeforeClose();

        createPanel();
        pack();
        setVisible(true);
    }

    // EFFECTS: run the application
    public static void main(String[] args) {
        new Gui();
    }

    // MODIFIES: this
    // EFFECTS: popup window before exit to let users save all the data to file
    private void saveBeforeClose() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(list,
                        "Do you want to save before exit?",
                        "Save", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        writer.open();
                        writer.write(toDoList);
                        writer.close();
                    } catch (FileNotFoundException fileNotFoundException) {
                        JOptionPane.showMessageDialog(null,
                                "Unable to save file to " + STORE_DESTINATION);
                    }

                } else {
                    e.getWindow().dispose();
                }

            }
        });
    }

    // MODIFIES: this
    // EFFECTS: popup window once run the application to let users load data from file
    private void loadBeforeStart() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog(list,
                        "Do you want to load data from file?",
                        "Load", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        toDoList = reader.read();
                        for (Task t : toDoList.getUncompleted()) {
                            uncompleted.addElement(t);
                        }
                        for (Task t : toDoList.getIsCompleted()) {
                            completed.addElement(t);
                        }

                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(null,
                                "Unable to load file from " + STORE_DESTINATION);
                    }
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: create the list and put it in a scroll pane
    private void initializeListModel() {
        toDoList = new ToDoList();
        title = toDoList.getName();
        this.setTitle(title);
        uncompleted = new DefaultListModel();
        completed = new DefaultListModel();

        list = new JList(uncompleted);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(-1);
        list.setCellRenderer(new MyListCellRenderer());
        listScrollPane = new JScrollPane(list);
    }


    // MODIFIES: this
    // EFFECTS: create the add button and add its listener
    private void initializeAddButton() {
        addButton = new JButton(ADD_STRING);
        addButton.addActionListener(new AddListener());
    }

    // MODIFIES: this
    // EFFECTS: create the delete button and add its listener
    private void initializeDeleteButton() {
        deleteButton = new JButton(DEL_STRING);
        deleteButton.addActionListener(new DeleteListener());
        deleteButton.setEnabled(false);
    }

    // MODIFIES: this
    // EFFECTS: create the view buttons and add their listeners
    private void initializeViewButtons() {
        viewUncompletedButton = new JButton(UNCOM_STRING);
        viewCompletedButton = new JButton(COM_STRING);
        viewUncompletedButton.addActionListener(new UncompletedListener());
        viewCompletedButton.addActionListener(new CompletedListener());
    }

    // MODIFIES: this
    // EFFECTS: create the complete buttons and add its listener
    private void initializeCompleteButton() {
        completeButton = new JButton(COMPLETE_STRING);
        completeButton.addActionListener(new CompleteListener());
        completeButton.setEnabled(false);
    }

    // MODIFIES: this
    // EFFECTS: instantiate the JSON writer and JSON reader
    private void initializeJson() {
        writer = new JsonWriter(STORE_DESTINATION);
        reader = new JsonReader(STORE_DESTINATION);
    }


    @Override
    // MODIFIES: this
    // EFFECTS: disable delete and complete buttons when no entry is selected;
    //          disable complete button when completed tasks are displayed
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (list.getSelectedIndex() == -1) {
                deleteButton.setEnabled(false);
                completeButton.setEnabled(false);
            } else {
                if (listStatus == 1) {
                    completeButton.setEnabled(false);
                }
                deleteButton.setEnabled(true);
                completeButton.setEnabled(true);
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: create the layout of all components
    private void createPanel() {
        JPanel addDelPanel = new JPanel();
        int height = listScrollPane.getHeight();
        addDelPanel.setLayout(new BoxLayout(addDelPanel, BoxLayout.Y_AXIS));
        addDelPanel.add(addButton);
        addDelPanel.add(deleteButton);

        addDelPanel.add(completeButton);
        addDelPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        addDelPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.X_AXIS));
        viewPanel.add(viewUncompletedButton);
        viewPanel.add(viewCompletedButton);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
        topPanel.add(curList);
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(topPanel, BorderLayout.PAGE_START);
        add(listScrollPane, BorderLayout.CENTER);
        add(addDelPanel, BorderLayout.AFTER_LINE_ENDS);
        add(viewPanel, BorderLayout.SOUTH);

    }

    // Represent a listener for the add button
    class AddListener implements ActionListener {
        DefaultListModel currentList = (DefaultListModel) list.getModel();

        @Override
        // MODIFIES: uncompleted, completed, toDoList,list
        // EFFECTS: add new tasks to list model
        public void actionPerformed(ActionEvent e) {
            Task task;

            String label = JOptionPane.showInputDialog(addButton,
                    "Please type in the label of your task below\n"
                            + "Empty and redundant labels are not accepted", null);
            if (label != null) {
                try {
                    task = new Task(label);
                    addTaskToCurList(task, label);
                } catch (InvalidLabelException invalidLabelException) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(addButton,
                            "Empty task label!", "Warning", JOptionPane.ERROR_MESSAGE);
                }


                list.setSelectedIndex(currentList.size() - 1);
                list.ensureIndexIsVisible(currentList.size() - 1);
            }


        }

        // EFFECTS: pop up a warning dialog if task is contained in to-do list;
        //          if not, add the new task to to-do list
        private void addTaskToCurList(Task task, String label) {
            if (containsInList(task)) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(addButton,
                        "Redundant task label!", "Warning", JOptionPane.ERROR_MESSAGE);
                return;
            } else {
                if (listStatus == 0) {
                    uncompleted.addElement(task);
                    toDoList.addUncompleted(label);
                } else {
                    completed.addElement(task);
                    toDoList.addCompleted(label);
                }
            }

        }

        // EFFECTS: return true of task with same label is contained in current list model; false otherwise
        private boolean containsInList(Task task) {
            return uncompleted.contains(task) || completed.contains(task);
        }
    }

    // Represent a listener for the delete button
    class DeleteListener implements ActionListener {
        DefaultListModel currentList = (DefaultListModel) list.getModel();

        @Override
        // MODIFIES: uncompleted, completed,list
        // EFFECTS: delete the selected entry
        public void actionPerformed(ActionEvent e) {
            int i = list.getSelectedIndex();

            removeSelected(i);

            if (currentList.size() == 0) {
                deleteButton.setEnabled(false);
            } else {
                list.setSelectedIndex(currentList.size() - 1);
                list.ensureIndexIsVisible(currentList.size() - 1);
            }

        }

        // MODIFIES: currentList
        // EFFECTS: remove the selected task
        private void removeSelected(int i) {
            if (listStatus == 0) {
                uncompleted.remove(i);
                toDoList.deleteUncompleted(i + 1);
            } else {
                completed.remove(i);
                toDoList.deleteCompleted(i + 1);
            }
        }
    }

    // Represent a listener for view uncompleted button
    class UncompletedListener implements ActionListener {

        @Override
        // MODIFIES: listStatus,list,curlist
        // EFFECTS: display uncompleted tasks
        public void actionPerformed(ActionEvent e) {
            listStatus = 0;
            list.setModel(uncompleted);
            list.setSelectedIndex(0);
            curList.setText(UNCOM_LIST);
        }
    }

    // Represent a listener for view completed button
    class CompletedListener implements ActionListener {

        @Override
        // MODIFIES: listStatus,list,curlist
        // EFFECTS: display completed tasks
        public void actionPerformed(ActionEvent e) {
            listStatus = 1;
            list.setModel(completed);
            list.setSelectedIndex(0);
            curList.setText(COM_LIST);
        }
    }

    // Represent a listener for complete button
    class CompleteListener implements ActionListener {

        @Override
        // MODIFIES: uncompleted,completed,curlist
        // EFFECTS: remove the selected uncompleted task to completed list
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();

            if (index == -1) {
                Toolkit.getDefaultToolkit().beep();
            } else {
                if (listStatus == 0) {
                    Task t = (Task) uncompleted.getElementAt(index);
                    uncompleted.remove(index);
                    completed.addElement(t);
                    t.setIsCompleted();
                    toDoList.deleteUncompleted(index + 1);
                    toDoList.addCompleted(t.getLabel());

                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
                if (uncompleted.size() == 0) {
                    completeButton.setEnabled(false);
                }
            }
        }
    }

    // Represent a list cell renderer
    class MyListCellRenderer extends DefaultListCellRenderer {

        // EFFECTS: set this list cell renderer to be opaque
        public MyListCellRenderer() {
            setOpaque(true);
        }

        // EFFECTS: return the label of the task in list
        public Component getListCellRendererComponent(JList<?> list,
                                                      Object value,
                                                      int index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setText(((Task) value).getLabel());
            return label;
        }
    }
}


