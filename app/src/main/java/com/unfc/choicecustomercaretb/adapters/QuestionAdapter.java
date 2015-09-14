/**
 * 
 */
package com.unfc.choicecustomercaretb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unfc.choicecustomercaretb.entity.QuestionEntity;
import com.unfc.choicecustomercaretb.fragment.QuestionFragment;

import java.util.List;

/**
 * @author nvhaiwork
 *
 */
public class QuestionAdapter extends FragmentStatePagerAdapter {

	private List<QuestionEntity> mQuestions;

	/**
	 * @param fm
	 *            Fragment manager
	 */
	public QuestionAdapter(FragmentManager fm, List<QuestionEntity> questions) {
		super(fm);

		this.mQuestions = questions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentStatePagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {

		return QuestionFragment.getInstance(position, mQuestions.size(), mQuestions.get(position));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {

		if (mQuestions == null) {

			return 0;
		}

		return mQuestions == null ? 0 : mQuestions.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
	 */
	@Override
	public int getItemPosition(Object object) {
		return POSITION_UNCHANGED;
	}
}
