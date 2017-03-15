package frank.com.tasker.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import frank.com.tasker.R;
import frank.com.tasker.global.GlobalUtil;

import frank.com.tasker.database.TaskIndiv;

public class AddEditTaskFragment extends Fragment {
    public int taskPosition = -1;
    public int priority;

    private EditText txtTitle;
    private EditText txtContent;
    private Button btnPriority;
    private Button btnAdd;

    public AddEditTaskFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_add_edit_task, container, false);

        rootView.findViewById(R.id.add_priority).setOnTouchListener(GlobalUtil.onTouchListener);
        rootView.findViewById(R.id.add_add).setOnTouchListener(GlobalUtil.onTouchListener);

        txtTitle = (EditText) rootView.findViewById(R.id.add_input_title);
        txtContent = (EditText) rootView.findViewById(R.id.add_input_content);
        btnPriority = (Button) rootView.findViewById(R.id.add_priority);
        btnAdd = (Button) rootView.findViewById(R.id.add_add);

        if (taskPosition != -1) {
            TaskIndiv taskIndiv = GlobalUtil.tasks.getTask(taskPosition);
            ((TextView) rootView.findViewById(R.id.add_title)).setText("Edit Task");
            txtTitle.setText(taskIndiv.getTitle());
            txtContent.setText(taskIndiv.getContent());
            btnAdd.setText("Save");

            this.priority = taskIndiv.getPriority();
            refreshWhenButton();
        } else
            priority = TaskIndiv.TASK_PRIORITY_NONE;
        return rootView;
    }

    public String getTitle() {
        return txtTitle.getText().toString();
    }

    public String getContent() {
        return txtContent.getText().toString();
    }

    public void refreshWhenButton() {
        btnPriority.setText(GlobalUtil.getPriorityString(priority));
    }
}
