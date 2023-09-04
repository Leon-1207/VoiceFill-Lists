package com.mw.voicefilllists;

import android.content.Context;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.lang.reflect.Method;

public abstract class SpinnerHelper {
    public void setupAdapter(Context context, SpinnerAdapter baseAdapter, Spinner spinner) {
        SpinnerAdapter adapter1 = new NothingSelectedSpinnerAdapter(baseAdapter, R.layout.please_select_an_option_item, context);
        SpinnerAdapter adapter2 = new DecoratedSpinnerAdapterWithAddOption(context, adapter1) {
            @Override
            public void onClickedCreateNewEntryOption() {
                SpinnerHelper.this.onClickedCreateNewEntryOption();
            }
        };
        spinner.setAdapter(adapter2);
    }

    protected abstract void onClickedCreateNewEntryOption();

    /**
     * Hides a spinner's drop down.
     */
    public void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
