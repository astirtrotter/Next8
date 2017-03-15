package frank.com.tasker.ui.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import frank.com.tasker.R;
import frank.com.tasker.global.GlobalUtil;

import frank.com.tasker.database.TaskIndiv;

public class TaskFragment extends Fragment {
    public int taskPosition;

    Button btnEdit;
    Button btnDone;
    ImageView imgPriority;
    View infoPane;

	public TaskFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);
        btnEdit = (Button) rootView.findViewById(R.id.task_edit);
        btnEdit.setOnTouchListener(GlobalUtil.onTouchListener);
        btnDone = (Button) rootView.findViewById(R.id.task_done);
        btnDone.setOnTouchListener(GlobalUtil.onTouchListener);
        rootView.findViewById(R.id.task_back).setOnTouchListener(GlobalUtil.onTouchListener);
        imgPriority = (ImageView) rootView.findViewById(R.id.task_priority);
        infoPane = rootView.findViewById(R.id.task_info);

        if (taskPosition != -1) {
            TaskIndiv taskIndiv = GlobalUtil.tasks.getTask(taskPosition);

            ((TextView) rootView.findViewById(R.id.task_title)).setText(taskIndiv.getTitle());
            ((TextView) rootView.findViewById(R.id.task_content)).setText(taskIndiv.getContent());
            int bgColor = Color.CYAN;
            switch (taskIndiv.getPriority()) {
                case TaskIndiv.TASK_PRIORITY_WHENEVER:
                    imgPriority.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_whenever));
                    bgColor = 0x99000000 + TaskIndiv.PRIORITY_COLOR_WHENEVER;
                    break;
                case TaskIndiv.TASK_PRIORITY_SOON:
                    imgPriority.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_soon));
                    bgColor = 0x99000000 + TaskIndiv.PRIORITY_COLOR_SOON;
                    break;
                case TaskIndiv.TASK_PRIORITY_NOWISH:
                    imgPriority.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_nowish));
                    bgColor = 0x99000000 + TaskIndiv.PRIORITY_COLOR_NOWISH;
                    break;
                case TaskIndiv.TASK_PRIORITY_JUST:
                    imgPriority.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_just));
                    bgColor = 0x99000000 + TaskIndiv.PRIORITY_COLOR_JUST;
                    break;
            }
            infoPane.setBackgroundColor(bgColor);
        }
        return rootView;
    }

    public void onDoneClicked() {
        btnEdit.setEnabled(false);
        btnEdit.setAlpha(0.3f);
        btnDone.setEnabled(false);
        btnDone.setAlpha(0.3f);
        imgPriority.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_done));

        GlobalUtil.tasks.checkTaskAsDone(taskPosition);
    }
}
