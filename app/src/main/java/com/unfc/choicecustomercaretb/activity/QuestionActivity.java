package com.unfc.choicecustomercaretb.activity;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;

import com.unfc.choicecustomercaretb.R;
import com.unfc.choicecustomercaretb.adapters.QuestionAdapter;
import com.unfc.choicecustomercaretb.api.BaseApi;
import com.unfc.choicecustomercaretb.entity.QuestionEntity;
import com.unfc.choicecustomercaretb.utility.Utilities;
import com.unfc.choicecustomercaretb.view.DisablePagingViewPager;
import com.unfc.choicecustomercaretb.view.LoadingDialog;

import java.util.List;

import butterknife.Bind;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Hai Nguyen - 8/28/15.
 */
public class QuestionActivity extends BaseActivity {

	@Bind(R.id.question_view_pager)
	DisablePagingViewPager viewPager;

	@Bind(R.id.question_next_btn)
	ImageView btnNext;

	@Bind(R.id.question_prev_btn)
	ImageView btnPrev;

	private int[] mAnswers;

	@Override
	protected int addLayoutView() {
		View decorView = getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
		decorView.setSystemUiVisibility(uiOptions);
		return R.layout.activity_question;
	}

	@Override
	protected void initComponents() {
		super.initComponents();

		Utilities.resetInfo();

		viewPager.setPagingEnabled(false);
		viewPager.setOffscreenPageLimit(4);

		btnPrev.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		getQuestions();
	}

	/**
	 * Set user question
	 */
	public void setAnswer(int pos, int answer) {

		mAnswers[pos] = answer;
	}

	/**
	 * Get questions
	 */
	public int[] getAnswers() {

		return mAnswers;
	}

	@Override
	public void onClick(View view) {
		super.onClick(view);

		switch (view.getId()) {

			case R.id.question_next_btn :

				navigatePage(true);
				break;

			case R.id.question_prev_btn :

				navigatePage(false);
				break;
		}
	}

	/**
	 * Navigate page
	 */
	private void navigatePage(boolean isNext) {

		int currentPage = viewPager.getCurrentItem();
		if (isNext) {

			currentPage++;
		} else {

			currentPage--;
		}

		viewPager.setCurrentItem(currentPage);
		if (currentPage == 0) {

			btnPrev.setVisibility(View.INVISIBLE);
			return;
		}

		int lastPage = viewPager.getAdapter().getCount() - 1;
		if (currentPage == lastPage) {

			btnNext.setVisibility(View.INVISIBLE);
			return;
		}

		btnNext.setVisibility(View.VISIBLE);
		btnPrev.setVisibility(View.VISIBLE);
	}

	/**
	 * Get questions
	 */
	private void getQuestions() {

		final Dialog dialog = LoadingDialog.show(this);
		new BaseApi(false).getInterface().getQuestions(new Callback<List<QuestionEntity>>() {
			@Override
			public void success(List<QuestionEntity> questions, Response response) {

				displayQuestions(questions);
				Utilities.dismissDialog(dialog);
			}

			@Override
			public void failure(RetrofitError retrofitError) {

				Utilities.dismissDialog(dialog);
			}
		});
	}

	/**
	 * Display question
	 */
	private void displayQuestions(List<QuestionEntity> questions) {

		if (questions == null) {

			return;
		}

		mAnswers = new int[questions.size()];
		QuestionAdapter adapter = new QuestionAdapter(getSupportFragmentManager(), questions);
		viewPager.setAdapter(adapter);
	}
}
