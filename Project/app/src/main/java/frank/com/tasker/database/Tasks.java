package frank.com.tasker.database;

import android.app.Activity;
import android.app.ProgressDialog;

import bolts.Task;
import frank.com.tasker.global.GlobalUtil;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Tasks {
    Activity activity;

    ArrayList<TaskIndiv> aryTasksPending = new ArrayList<TaskIndiv>();
    ArrayList<TaskIndiv> aryTasksDone = new ArrayList<TaskIndiv>();

    public Tasks(Activity activity) {
        this.activity = activity;
    }

    public TaskIndiv getTask(int position) {
        if (aryTasksPending.size() <= position || position < 0)
            return null;

        return aryTasksPending.get(position);
    }

    public void addTask(TaskIndiv taskIndiv) {
        tmp = taskIndiv;

        final ProgressDialog progress = ProgressDialog.show(activity, "Saving", "Please wait for a while.", true);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                aryTasksPending.add(tmp);

                ParseObject parseObject = new ParseObject(GlobalUtil.PARSE_DATABASE_CLASS);
                parseObject.put("userID", GlobalUtil.getUserID(activity));
                parseObject.put("taskID", tmp.getId());
                parseObject.put("title", tmp.getTitle());
                parseObject.put("content", tmp.getContent());
                parseObject.put("priority", tmp.getPriority());
                parseObject.put("state", tmp.getState());
                parseObject.put("rank", tmp.getRank());

                try {
                    parseObject.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });
            }
        }).start();
    }

    public void checkTaskAsDone(int position) {
        final TaskIndiv taskIndiv = aryTasksPending.remove(position);
        taskIndiv.setState(TaskIndiv.TASK_STATE_DONE);
        taskIndiv.setRank(System.currentTimeMillis());
        aryTasksDone.add(taskIndiv);

        updateTask(taskIndiv);
    }

    TaskIndiv tmp;
    public void updateTask(TaskIndiv taskIndiv) {
        updateTask(taskIndiv, false);
    }

    public void updateTask(TaskIndiv taskIndiv, final boolean isRunBackground) {
        tmp = taskIndiv;

        final ProgressDialog progress = ProgressDialog.show(activity, "Saving", "Please wait for a while.", true);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalUtil.PARSE_DATABASE_CLASS);
                query.whereEqualTo("userID", GlobalUtil.getUserID(activity));
                query.whereEqualTo("taskID", tmp.getId());
                try {
                    List<ParseObject> taskList = query.find();
                    if (taskList.size() != 1)
                        return;

                    ParseObject parseObject = taskList.get(0);
                    parseObject.put("title", tmp.getTitle());
                    parseObject.put("content", tmp.getContent());
                    parseObject.put("priority", tmp.getPriority());
                    parseObject.put("state", tmp.getState());
                    parseObject.put("rank", tmp.getRank());
                    if (isRunBackground)
                        parseObject.saveInBackground();
                    else
                        parseObject.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });
            }
        }).start();
    }

    public void loadTasks() {
        final ProgressDialog progress = ProgressDialog.show(activity, "Loading", "Please wait for a while.", true);
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                aryTasksDone.clear();
                aryTasksPending.clear();

                ParseQuery<ParseObject> query = ParseQuery.getQuery(GlobalUtil.PARSE_DATABASE_CLASS);
                query.whereEqualTo("userID", GlobalUtil.getUserID(activity));
                query.orderByAscending("rank");
                try {
                    List<ParseObject> taskList = query.find();
                    for (int i = 0; i < taskList.size(); i++) {
                        ParseObject parseObject = taskList.get(i);
                        TaskIndiv taskIndiv = new TaskIndiv();
                        taskIndiv.setId(parseObject.getLong("taskID"));
                        taskIndiv.setTitle(parseObject.getString("title"));
                        taskIndiv.setContent(parseObject.getString("content"));
                        taskIndiv.setPriority(parseObject.getInt("priority"));
                        taskIndiv.setState(parseObject.getInt("state"));
                        taskIndiv.setRank(parseObject.getLong("rank"));
                        if (taskIndiv.getState() == TaskIndiv.TASK_STATE_PENDING) {
                            aryTasksPending.add(taskIndiv);
                        } else {
                            aryTasksDone.add(taskIndiv);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                    }
                });
            }
        }).start();

    }

    public void swapTasks(int indexOne, int indexTwo) {
        TaskIndiv taskOne = getTask(indexOne);
        TaskIndiv taskTwo = getTask(indexTwo);

        int offset = (taskTwo.getRank() > taskOne.getRank()) ? 1 : -1;
        double offsetRank = 10000;
        TaskIndiv taskNext = getTask(indexTwo + offset);
        if (taskNext != null)
            offsetRank = (double) Math.abs((taskNext.getRank() + taskTwo.getRank()) / 2 - taskOne.getRank());

        taskOne.setRank(taskTwo.getRank() + (long)((offset > 0)?offsetRank:-offsetRank));

        aryTasksPending.set(indexOne, taskTwo);
        aryTasksPending.set(indexTwo, taskOne);

        updateTask(taskOne);
    }

    public int getPendingCount() {
        return aryTasksPending.size();
    }

    public int getDoneCount() {
        return aryTasksDone.size();
    }
}
