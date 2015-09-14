package com.unfc.choicecustomercaretb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.unfc.choicecustomercaretb.R;
import com.unfc.choicecustomercaretb.entity.RoomEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Hai Nguyen - 8/30/15.
 */
public class RoomAdapter extends BaseAdapter {

	private boolean isRoomAdapter;
	private List<RoomEntity> mRooms;
	private LayoutInflater mInflater;

	public RoomAdapter(Context ctx, List<RoomEntity> rooms, boolean isRoom) {

		this.mRooms = rooms;
		this.isRoomAdapter = isRoom;
		this.mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mRooms == null ? 0 : mRooms.size();
	}

	@Override
	public Object getItem(int i) {
		return mRooms.get(i);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return setupView(position, convertView, parent, false);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		return setupView(position, convertView, parent, true);
	}

	private View setupView(int position, View convertView, ViewGroup parent, boolean isDropDown) {

		ViewHolder holder;
		if (convertView == null) {

			convertView = mInflater.inflate(R.layout.layout_resource_spn_tem, parent, false);
			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		if (isDropDown) {
			holder.imvDown.setVisibility(View.INVISIBLE);
		} else {

			holder.imvDown.setVisibility(View.VISIBLE);
		}

		RoomEntity room = (RoomEntity) getItem(position);
		if (isRoomAdapter) {

			holder.txtText.setText(room.getName());
		} else {

			holder.txtText.setText(room.getBedNumber());
		}

		return convertView;
	}

	class ViewHolder {

		@Bind(R.id.resource_item_text)
		TextView txtText;

		@Bind(R.id.resource_item_imv)
		ImageView imvDown;

		public ViewHolder(View view) {

			ButterKnife.bind(this, view);
		}
	}
}
