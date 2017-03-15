package frank.com.tasker.database;

public class TaskIndiv {
    public static final int TASK_PRIORITY_NONE = 0;
    public static final int TASK_PRIORITY_WHENEVER = 1;
    public static final int TASK_PRIORITY_NOWISH = 2;
    public static final int TASK_PRIORITY_SOON = 3;
    public static final int TASK_PRIORITY_JUST= 4;

    public static final int PRIORITY_COLOR_WHENEVER = 0xfad1a9;
    public static final int PRIORITY_COLOR_NOWISH = 0x75cbe6;
    public static final int PRIORITY_COLOR_SOON = 0xfcb9b8;
    public static final int PRIORITY_COLOR_JUST = 0xb3df4c;

    public static final int TASK_STATE_PENDING = 1;
    public static final int TASK_STATE_DONE = 2;

    long id;
    String title;
    String content;
    int priority;
    int state;
    long rank;

    public long getRank() {
        return rank;
    }

    public TaskIndiv setRank(long rank) {
        this.rank = rank;
        return this;
    }

    public long getId() {
        return id;
    }

    public TaskIndiv setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public TaskIndiv setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public TaskIndiv setContent(String content) {
        this.content = content;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public TaskIndiv setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public int getState() {
        return state;
    }

    public TaskIndiv setState(int state) {
        this.state = state;
        return this;
    }
}
