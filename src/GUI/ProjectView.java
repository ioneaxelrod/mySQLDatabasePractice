package GUI;

import DatabaseInteraction.ProjectDatabaseInteraction;
import DatabaseInteraction.TaskDatabaseInteraction;
import Model.Project;
import Model.Task;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ProjectView extends JFrame {

    private static Project currentProject;
    final private JPanel projectTitlePanel;
    final private JPanel projectPanel;
    final private JPanel bottomPanel;
    final private JFrame monthFrame;
    final private JFrame dayFrame;

    ProjectView(final Project project, final JFrame monthFrame, final JFrame dayFrame) {
        super(project.getTitle());
        this.currentProject = project;
        this.monthFrame = monthFrame;
        this.dayFrame = dayFrame;

        setSize(500, 500);
        setResizable(false);
        dispose();
        setLayout(new BorderLayout());
        setVisible(true);
        System.out.println("Project frame created");


        projectTitlePanel = new JPanel();
        add(projectTitlePanel, BorderLayout.NORTH);

        final JLabel deadlineLabel = new JLabel(currentProject.getTitle());
        projectTitlePanel.add(deadlineLabel);

        projectPanel = new JPanel();
        add(projectPanel, BorderLayout.CENTER);

        final ArrayList<String> parsedProjectInfo = currentProject.parseProjectForJList();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String data : parsedProjectInfo) {
            listModel.addElement(data);
        }

        final JList projectList = new JList(listModel);
        projectList.setPreferredSize(new Dimension(400,400));

        projectPanel.add(projectList);

        bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        final JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteProject(currentProject));
        bottomPanel.add(deleteButton);

    }

    //TODO: move this somehwere else

    public static String formatLocalDate(final LocalDate date) {
        return date.getMonthValue() + "-" + date.getDayOfMonth() + "-" + date.getYear();
    }

    private void deleteProject(final Project project) {
        for (Task task : project.getSteps()) {
            TaskDatabaseInteraction.deleteAllProjectTasksFromDatabase(project.getId());
        }
        ProjectDatabaseInteraction.deleteProjectFromDatabase(project);
        MonthView.updateFrame(monthFrame);
        dayFrame.dispose();
        dispose();
    }

}


