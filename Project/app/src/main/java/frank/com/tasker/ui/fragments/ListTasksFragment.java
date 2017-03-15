package frank.com.tasker.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import frank.com.tasker.ui.MainActivity;
import frank.com.tasker.R;
import frank.com.tasker.global.GlobalUtil;

import frank.com.tasker.database.TaskIndiv;
import frank.com.tasker.ui.dropdownlistview.DynamicListView;

public class ListTasksFragment extends Fragment {
	
	public ListTasksFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_list_tasks, container, false);

        DynamicListView listView = (DynamicListView) rootView.findViewById(R.id.list_list);
        listView.setAdapter(new TasksListAdapter());

//        DragNDropCursorAdapter cursorAdapter = new DragNDropCursorAdapter(getActivity(),
//                                    R.layout.tasks_list_row,
//                                    null,
//                                    new String[]{"text"},
//                                    new int[]{R.id.list_title},
//                                    R.id.list_priority);
//        listView.setDragNDropAdapter(cursorAdapter);

        listView.setOnItemClickListener(itemClickListener);
         
        return rootView;
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ((MainActivity) getActivity()).onListItemClicked(position);
        }
    };

    class TasksListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return GlobalUtil.tasks.getPendingCount();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return GlobalUtil.tasks.getTask(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            TaskIndiv taskIndiv = (TaskIndiv) getItem(position);
            if (taskIndiv == null)
                return -1;

            return taskIndiv.getId();
        }

        private LayoutInflater inflater;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (inflater == null)
                inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.tasks_list_row, null);

            TaskIndiv task = (TaskIndiv) getItem(position);

            TextView txtTitle = (TextView) convertView.findViewById(R.id.list_title);
            TextView txtContent = (TextView) convertView.findViewById(R.id.list_content);
            ImageView imgPriority = (ImageView) convertView.findViewById(R.id.list_priority);

            txtTitle.setText(task.getTitle());
            txtContent.setText(task.getContent());

            int bgColor = Color.CYAN;
            switch (task.getPriority()) {
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
            convertView.setBackgroundColor(bgColor);

            return convertView;
        }
    }


}
