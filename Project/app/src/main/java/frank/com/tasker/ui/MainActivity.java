package frank.com.tasker.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import frank.com.tasker.R;
import frank.com.tasker.ui.fragments.AddEditTaskFragment;
import frank.com.tasker.ui.fragments.HomeFragment;
import frank.com.tasker.ui.fragments.ListTasksFragment;
import frank.com.tasker.ui.fragments.SettingFragment;
import frank.com.tasker.ui.fragments.TaskFragment;
import frank.com.tasker.global.GlobalUtil;

import java.util.ArrayList;

import frank.com.tasker.ui.slidemenu.NavDrawerListAdapter;
import frank.com.tasker.database.TaskIndiv;
import frank.com.tasker.database.Tasks;
import frank.com.tasker.ui.slidemenu.NavDrawerItem;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Add Task
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// List Tasks
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Setting
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
//		getActionBar().setDisplayHomeAsUpEnabled(true);
//		Drawable drawerIcon = getResources().getDrawable(R.drawable.ic_drawer);
//		drawerIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
//		getActionBar().setIcon(drawerIcon);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(GlobalUtil.VIEW_HOME);

			GlobalUtil.tasks = new Tasks(this);
			GlobalUtil.tasks.loadTasks();
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */

	Fragment fragment = null;
	int navItemIndex = -1;
	int taskPosition = -1;
	private void displayView(int position) {
		if (this.navItemIndex == position) {
			mDrawerLayout.closeDrawer(mDrawerList);
			return;
		}

		// update the main content by replacing fragments
		switch (position) {
			case GlobalUtil.VIEW_HOME:
				fragment = new HomeFragment();
				break;
			case GlobalUtil.VIEW_LIST_TASKS:
				fragment = new ListTasksFragment();
				break;
			case GlobalUtil.VIEW_ADD_TASK:
				fragment = new AddEditTaskFragment();
				break;
			case GlobalUtil.VIEW_SETTING:
				fragment = new SettingFragment();
				break;
			case GlobalUtil.VIEW_TASK_INFO:
				fragment = new TaskFragment();
				((TaskFragment) fragment).taskPosition = taskPosition;
				break;
			case GlobalUtil.VIEW_EDIT_TASK:
				fragment = new AddEditTaskFragment();
				((AddEditTaskFragment) fragment).taskPosition = taskPosition;
				break;

			default:
				break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();

			transaction.setCustomAnimations(R.anim.anim_in, R.anim.anim_out);
			transaction.replace(R.id.frame_container, fragment).commit();

			if (position <= GlobalUtil.VIEW_SETTING) {
				// update selected item and title, then close the drawer
				mDrawerList.setItemChecked(position, true);
				mDrawerList.setSelection(position);
				setTitle(navMenuTitles[position]);
				mDrawerLayout.closeDrawer(mDrawerList);

				this.navItemIndex = position;
			} else {
				this.navItemIndex = -1;
			}
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
//		mTitle = title;
//		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void onHomeAddTask(View view) {
		displayView(GlobalUtil.VIEW_ADD_TASK);
	}

	public void onAddWhen(View view) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.dlg_priority_choose, (ViewGroup) findViewById(R.id.priority_choose));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				((AddEditTaskFragment) fragment).refreshWhenButton();
			}
		});
		final AlertDialog dialog=builder.create();
		dialog.show();

		layout.findViewById(R.id.priority_whenever).setOnTouchListener(GlobalUtil.onTouchListener);
		layout.findViewById(R.id.priority_soon).setOnTouchListener(GlobalUtil.onTouchListener);
		layout.findViewById(R.id.priority_nowish).setOnTouchListener(GlobalUtil.onTouchListener);
		layout.findViewById(R.id.priority_just).setOnTouchListener(GlobalUtil.onTouchListener);

		layout.findViewById(R.id.priority_whenever).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((AddEditTaskFragment) fragment).priority = TaskIndiv.TASK_PRIORITY_WHENEVER;
				dialog.dismiss();
			}
		});
		layout.findViewById(R.id.priority_soon).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((AddEditTaskFragment) fragment).priority = TaskIndiv.TASK_PRIORITY_SOON;
				dialog.dismiss();
			}
		});
		layout.findViewById(R.id.priority_nowish).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((AddEditTaskFragment) fragment).priority = TaskIndiv.TASK_PRIORITY_NOWISH;
				dialog.dismiss();
			}
		});
		layout.findViewById(R.id.priority_just).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((AddEditTaskFragment) fragment).priority = TaskIndiv.TASK_PRIORITY_JUST;
				dialog.dismiss();
			}
		});
	}

	public void onAddAdd(View view) {
		AddEditTaskFragment addTaskFragment = (AddEditTaskFragment) fragment;

		if (addTaskFragment.taskPosition == -1) {// Add
			if (addTaskFragment.getTitle().isEmpty() ||
					addTaskFragment.getContent().isEmpty() ||
					addTaskFragment.priority == TaskIndiv.TASK_PRIORITY_NONE) {
				Toast.makeText(this, "Please check your input", Toast.LENGTH_SHORT).show();
				return;
			}

			TaskIndiv taskIndiv = new TaskIndiv();
			taskIndiv.setId(System.currentTimeMillis());
			taskIndiv.setRank(System.currentTimeMillis());
			taskIndiv.setTitle(addTaskFragment.getTitle());
			taskIndiv.setContent(addTaskFragment.getContent());
			taskIndiv.setPriority(addTaskFragment.priority);
			taskIndiv.setState(TaskIndiv.TASK_STATE_PENDING);

			GlobalUtil.tasks.addTask(taskIndiv);

			displayView(GlobalUtil.VIEW_LIST_TASKS);
		} else {
			TaskIndiv taskIndiv = GlobalUtil.tasks.getTask(taskPosition);
			taskIndiv.setTitle(addTaskFragment.getTitle());
			taskIndiv.setContent(addTaskFragment.getContent());
			taskIndiv.setPriority(addTaskFragment.priority);
			GlobalUtil.tasks.updateTask(taskIndiv);

			displayView(GlobalUtil.VIEW_TASK_INFO);
		}
	}

	public void onListItemClicked(int position) {
		taskPosition = position;
		displayView(GlobalUtil.VIEW_TASK_INFO);
	}

	public void onTaskEdit(View view) {
		displayView(GlobalUtil.VIEW_EDIT_TASK);
	}

	public void onTaskBack(View view) {
		displayView(GlobalUtil.VIEW_LIST_TASKS);
	}

	public void onTaskDone(View view) {
		((TaskFragment) fragment).onDoneClicked();
	}
}
