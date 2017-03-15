package frank.com.tasker.global;

import android.content.Context;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;

import frank.com.tasker.database.TaskIndiv;
import frank.com.tasker.database.Tasks;

public class GlobalUtil {
    public static final int VIEW_HOME = 0;
    public static final int VIEW_LIST_TASKS = 1;
    public static final int VIEW_ADD_TASK = 2;
    public static final int VIEW_SETTING = 3;

    public static final int VIEW_TASK_INFO = 4;
    public static final int VIEW_EDIT_TASK = 5;

    public static final String PARSE_DATABASE_CLASS = "tasks";

    public static View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                v.setAlpha(0.6f);
            else if (event.getAction() == MotionEvent.ACTION_UP)
                v.setAlpha(1f);
            return false;
        }
    };


    public static Tasks tasks;

    public static String getPriorityString(int priority) {
        String str = null;
        switch (priority) {
            case TaskIndiv.TASK_PRIORITY_WHENEVER:
                str = "Whenever...";
                break;
            case TaskIndiv.TASK_PRIORITY_JUST:
                str = "Go on with it!";
                break;
            case TaskIndiv.TASK_PRIORITY_SOON:
                str = "Soon";
                break;
            case TaskIndiv.TASK_PRIORITY_NOWISH:
                str = "Now-ish?";
                break;
            default:
                str = "When";
        }

        return str;
    }

    public static String getUserID(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return android_id;
    }
}
