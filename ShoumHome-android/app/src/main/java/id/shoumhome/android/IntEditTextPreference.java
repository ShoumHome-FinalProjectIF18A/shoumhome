package id.shoumhome.android;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.EditTextPreference;

public class IntEditTextPreference extends EditTextPreference {
    public IntEditTextPreference(Context context) {
        super(context);
    }

    public IntEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        return String.valueOf(getPersistedInt(defaultReturnValue == null ? 60 : Integer.parseInt(defaultReturnValue)));
    }
    @Override

    protected boolean persistString(String value) {
        try {
            return persistInt((value != null) ? Integer.parseInt(value) : 60);
        } catch (Exception e) {
            return false;
        }
    }
}
