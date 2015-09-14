package com.unfc.choicecustomercaretb.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unfc.choicecustomercaretb.R;
import com.unfc.choicecustomercaretb.activity.HomeActivity;
import com.unfc.choicecustomercaretb.activity.QuestionActivity;
import com.unfc.choicecustomercaretb.entity.QuestionEntity;
import com.unfc.choicecustomercaretb.utility.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Hai Nguyen - 8/28/15.
 */
public class QuestionFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

	private int mPos, mTotal;
	private QuestionEntity mQuestions;

	@Bind(R.id.question_title)
	TextView txtTitle;

	@Bind(R.id.question_text)
	TextView txtText;

	@Bind(R.id.question_count_text)
	TextView txtCount;

	@Bind(R.id.fragment_question_group_layout)
	RadioGroup questionGroup;

	@Bind(R.id.question_continue_btn)
	TextView btnContinue;

	public static QuestionFragment getInstance(int pos, int total, QuestionEntity question) {

		QuestionFragment fragment = new QuestionFragment();
		fragment.setPos(pos, total, question);
		return fragment;
	}

	public void setPos(int pos, int total, QuestionEntity questions) {
		this.mPos = pos;
		this.mTotal = total;
		this.mQuestions = questions;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_question, container, false);
		ButterKnife.bind(this, view);

		if (mPos == mTotal - 1) {

			btnContinue.setOnClickListener(this);
			btnContinue.setVisibility(View.VISIBLE);
		} else {

			btnContinue.setVisibility(View.GONE);
		}

		displayQuestion();
		return view;
	}

	/**
	 * Display question
	 */
	private void displayQuestion() {

		questionGroup.removeAllViews();
		if (mQuestions == null) {

			return;
		}

		String strCount = String.format(getString(R.string.question_count), mPos + 1, mTotal);
		txtCount.setText(strCount);
		txtText.setText(mQuestions.getName().replace("\\n", "\n"));
		txtTitle.setText(mQuestions.getTitle().replace("\\n", "\n"));
		QuestionEntity question;
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/SansSerifFLF.otf");
		for (int i = 0; i < mQuestions.getAnswers().size(); i++) {

			question = mQuestions.getAnswers().get(i);
			RadioButton button = (RadioButton) LayoutInflater.from(getActivity()).inflate(R.layout.layout_question_item,
					null, false);
			button.setId(question.getId());
			button.setText(question.getValue());
			button.setTypeface(font);
			questionGroup.addView(button);

			if (i == 0) {

				button.setChecked(true);
				((QuestionActivity) getActivity()).setAnswer(mPos, question.getId());
			}
		}

		questionGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int i) {

		((QuestionActivity) getActivity()).setAnswer(mPos, i);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

			case R.id.question_continue_btn :

				int[] questions = ((QuestionActivity) getActivity()).getAnswers();
				String strQuestions = new Gson().toJson(questions);

				Intent intent = new Intent(getActivity(), HomeActivity.class);
				intent.putExtra(Constants.INTENT_PATIENT_QUESTION, strQuestions);
				startActivity(intent);
				getActivity().finish();
				break;
		}
	}
}
