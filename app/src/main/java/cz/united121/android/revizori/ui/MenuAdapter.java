package cz.united121.android.revizori.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.united121.android.revizori.R;

/**
 * TODO add class description
 * Created by Petr Lorenc[Lorenc55Petr@seznam.cz] on {10/10/2015}
 **/
public abstract class MenuAdapter extends BaseAdapter {
	public static final String TAG = MenuAdapter.class.getName();

	List<MenuRow> mMenuRowList;
	WeakReference<Context> mActivityContext;

	MenuAdapter(Context activityContext) {
		mMenuRowList = getMenuRows();
		mActivityContext = new WeakReference<Context>(activityContext);
	}

	protected abstract List<MenuRow> getMenuRows();

	@Override
	public int getCount() {
		return mMenuRowList.size();
	}

	@Override
	public Object getItem(int position) {
		return mMenuRowList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(TAG, "getView");
		MenuRowViewHolder viewHolder;
		if (convertView != null) {
			viewHolder = (MenuRowViewHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(mActivityContext.get()).inflate(R.layout.widget_row_menu, parent, false);
			viewHolder = new MenuRowViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		viewHolder.mImage.setImageResource(mMenuRowList.get(position).pictureRes);
		viewHolder.mTitle.setText(mMenuRowList.get(position).title);
		return convertView;
	}

	public static enum MenuRow {
		MAP("Mapa", R.drawable.menu_map, R.color.primary),
		SETTINGS("Nastavení", R.drawable.menu_setting, R.color.primary),
		SCORE_BOARD("Žebříček", R.drawable.menu_about_me, R.color.primary),
		LOGOUT("Odhlásit", R.drawable.menu_logout, R.color.primary);

		private String title;
		private int pictureRes;
		private int colorRes;

		MenuRow(String title, int pictureRes, int colorRes) {
			this.title = title;
			this.pictureRes = pictureRes;
			this.colorRes = colorRes;
		}
	}

	public static class MenuRowViewHolder {
		@Bind(R.id.menu_image)
		public ImageView mImage;
		@Bind(R.id.menu_title)
		public TextView mTitle;

		public MenuRowViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}
}
