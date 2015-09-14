package com.unfc.choicecustomercaretb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.unfc.choicecustomercaretb.R;
import com.unfc.choicecustomercaretb.entity.MessageEntity;
import com.unfc.choicecustomercaretb.utility.RequestType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Hai Nguyen - 8/29/15.
 */
public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.ViewHolder> {

	private List<MessageEntity> mMessages;
	private SimpleDateFormat mServerFormat, mDateFormat;

	public QueueAdapter(List<MessageEntity> messages) {

		this.mMessages = messages;
		mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
		mServerFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_request_queue_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

		MessageEntity message = mMessages.get(position);
		if (message.getResponded() == null || message.getResponded().equals("")) {

			holder.imvResponse.setVisibility(View.INVISIBLE);
		} else {

			holder.imvResponse.setVisibility(View.VISIBLE);
		}

		try {

			Date sentTime = mServerFormat.parse(message.getSent());
			holder.txtTime.setText(mDateFormat.format(sentTime));
		} catch (Exception ex) {

			holder.txtTime.setVisibility(View.GONE);
		}

		String strRequest = RequestType.values()[message.getMessageTypeId()].toString().toLowerCase();
		holder.txtRequest.setText(strRequest.toLowerCase() + " request");
	}

	@Override
	public int getItemCount() {

		return mMessages == null ? 0 : mMessages.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		@Bind(R.id.queue_item_imv)
		ImageView imvResponse;

		@Bind(R.id.queue_item_text)
		TextView txtRequest;

		@Bind(R.id.queue_item_time)
		TextView txtTime;

		public ViewHolder(View itemView) {
			super(itemView);

			ButterKnife.bind(this, itemView);
		}
	}
}
