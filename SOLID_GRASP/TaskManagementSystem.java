import java.util.ArrayList;
import java.util.Date;
import java.util.List;

enum TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED
}

enum TaskPriority {
    LOW,
    MEDIUM,
    HIGH
}

enum RecurrenceFrequency {
    DAILY,
    WEEKLY,
    MONTHLY
}

class Task {
    private String title;
    private String description;
    private Date dueDate;
    private TaskStatus status;
    private TaskPriority priority;

    public Task(String title, String description, Date dueDate, TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = TaskStatus.PENDING;
    }

    public void markAsCompleted() {
        this.status = TaskStatus.COMPLETED;
    }

    public void updateStatus(TaskStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

}

class RecurringTask extends Task {
    private RecurrenceFrequency frequency;

    public RecurringTask(String title, String description, Date dueDate, TaskPriority priority, RecurrenceFrequency frequency) {
        super(title, description, dueDate, priority);
        this.frequency = frequency;
    }

    public Date getNextOccurrence() {
        return new Date();
    }
}

class HighPriorityTask extends Task {
    private boolean isImmediate;

    public HighPriorityTask(String title, String description, Date dueDate, TaskPriority priority, boolean isImmediate) {
        super(title, description, dueDate, priority);
        this.isImmediate = isImmediate;
    }

    public void notifyTeam() {
        System.out.println("Team has been notified about the high-priority task: " + this.getTitle());
    }
}

class TeamMember {
    String name;
    private String email;

    public TeamMember(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void joinProject(Project project) {
        project.addTeamMember(this);
    }

    public void leaveProject(Project project) {
        project.removeTeamMember(this);
    }

}

class Project {
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private List<Task> tasks;
    private List<TeamMember> teamMembers;

    public Project(String name, String description, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public void addTeamMember(TeamMember member) {
        teamMembers.add(member);
    }

    public void removeTeamMember(TeamMember member) {
        teamMembers.remove(member);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }
}

public class TaskManagementSystem {
    public static void main(String[] args) {
        Project project = new Project("New Website", "Development of a new e-commerce website", new Date(), new Date());

        Task task1 = new Task("Design Homepage", "Create a design for the homepage", new Date(), TaskPriority.HIGH);
        RecurringTask task2 = new RecurringTask("Weekly Team Meeting", "Hold a meeting every Monday", new Date(), TaskPriority.MEDIUM, RecurrenceFrequency.WEEKLY);
        HighPriorityTask task3 = new HighPriorityTask("Fix Payment Bug", "Resolve critical payment processing issue", new Date(), TaskPriority.HIGH, true);
        Task task4 = new Task("Develop Backend", "Create the backend API", new Date(), TaskPriority.MEDIUM);
        Task task5 = new Task("Write Documentation", "Document the project setup and usage", new Date(), TaskPriority.LOW);

        project.addTask(task1);
        project.addTask(task2);
        project.addTask(task3);
        project.addTask(task4);
        project.addTask(task5);

        TeamMember alice = new TeamMember("Alice", "alice@example.com");
        TeamMember bob = new TeamMember("Bob", "bob@example.com");
        TeamMember charlie = new TeamMember("Charlie", "charlie@example.com");

        project.addTeamMember(alice);
        project.addTeamMember(bob);
        project.addTeamMember(charlie);

        task1.markAsCompleted();
        task3.notifyTeam();
        task4.updateStatus(TaskStatus.IN_PROGRESS);

        project.removeTask(task5);

        System.out.println("Task List:");
        for (Task task : project.getTasks()) {
            System.out.println("Task: " + task.getTitle() + ", Status: " + task.getStatus());
        }

        System.out.println("\nTeam Members:");
        for (TeamMember member : project.getTeamMembers()) {
            System.out.println("Member: " + member.name);
        }
    }
}