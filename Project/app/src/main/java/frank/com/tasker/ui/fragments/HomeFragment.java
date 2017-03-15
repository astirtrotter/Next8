package frank.com.tasker.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import frank.com.tasker.R;
import frank.com.tasker.global.GlobalUtil;

public class HomeFragment extends Fragment {
	
	public HomeFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        rootView.findViewById(R.id.home_add).setOnTouchListener(GlobalUtil.onTouchListener);
        return rootView;
    }
}
