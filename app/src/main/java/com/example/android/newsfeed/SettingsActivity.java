

package com.example.android.newsfeed;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Settings icona basınca bu intent açılıyor
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // Navigate with the app icon in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//actionbarda geri icon butonunu sağlar
        getSupportActionBar().setDisplayShowHomeEnabled(false);//actionbarda geri icon basıldığında açılış ekranına dömesini sağlar
    }

    /**
     * The NewsPreferenceFragment implements the Preference.OnPreferenceChangeListener interface
     * to set up to listen for any Preference changes made by the user.
     * And the NewsPreferenceFragment also implements the DatePickerDialog.OnDateSetListener to
     * receive a callback when the user has finished selecting a date.
     */
    public static class NewsPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener, DatePickerDialog.OnDateSetListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            // prefenceden "number of items" keyin valuesini alır
            Preference numOfItems = findPreference(getString(R.string.settings_number_of_items_key));
            System.out.println("SettingsActivity --> onCreate(Bundle savedInstanceState) --> numOfItems -->" + numOfItems);
            // değeri şuanki depolanan sharedperefencesden okur değeri ve displayde gösterir
             bindPreferenceSummaryToValue(numOfItems);
            System.out.println("SettingsActivity --> onCreate(Bundle savedInstanceState) --> bindPreferenceSummaryToValue(numOfItems) -->" + numOfItems);


             // prefenceden "order by" keyin valuesini alır
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            // değeri şuanki depolanan sharedperefencesden okur değeri ve displayde gösterir
            bindPreferenceSummaryToValue(orderBy);

            // prefenceden "order date" keyin valuesini alır
            Preference orderDate = findPreference(getString(R.string.settings_order_date_key));
            // değeri şuanki depolanan sharedperefencesden okur değeri ve displayde gösterir
            bindPreferenceSummaryToValue(orderDate);


            // prefenceden "order date" keyin valuesini alır
            Preference fromDate = findPreference(getString(R.string.settings_from_date_key));
            setOnPreferenceClick(fromDate);
            // değeri şuanki depolanan sharedperefencesden okur değeri ve displayde gösterir
            bindPreferenceSummaryToValue(fromDate);
        }

        /**settingsdeki itamlara tıklanıldığında*/
        private void setOnPreferenceClick(Preference preference) {
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    System.out.println("SettingsActivity --> setOnPreferenceClick(Preference preference)fonk");
                    String key = preference.getKey();
                    System.out.println("SettingsActivity --> setOnPreferenceClick(Preference preference) --> key --> " + key);
                    //seçilen item "from_date" ise showDatePicker() fonk çalışacak
                    if (key.equalsIgnoreCase(getString(R.string.settings_from_date_key))) {
                        showDatePicker();
                    }
                    return false;
                }
            });
        }

        /**  şuanki depolanan sharedperefencesden değeri okur  ve displayde gösterir*/
        private void bindPreferenceSummaryToValue(Preference preference) {//
            preference.setOnPreferenceChangeListener(this);

            // değeri şuanki depolanan sharedperefencesden okur değeri
            // ve displayde gösterir
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            System.out.println("SettingsActivity --> bindPreferenceSummaryToValue(Preference preference) --> preferences --> " + preferences);
            String preferenceString = preferences.getString(preference.getKey(), "");
            System.out.println("SettingsActivity --> bindPreferenceSummaryToValue(Preference preference) --> preferenceString --> " + preferenceString);

            onPreferenceChange(preference, preferenceString);

        }


        /**settingsde değişiklik yapıldığında sharedprefencese yazma işlemini gerçekleştiriyoruz*/
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            System.out.println("SettingsActivity --> nPreferenceChange(Preference preference, Object value) --> stringValue --> " + stringValue);
            // Update the summary of a ListPreference using the label
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                System.out.println("SettingsActivity --> nPreferenceChange(Preference preference, Object value) --> prefIndex --> " + prefIndex);

                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    System.out.println("SettingsActivity --> nPreferenceChange(Preference preference, Object value) --> labels --> " + labels);
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }



        /**
         * seçilen data string "2017-2-1" 'dan bu formata çeviriliyor "2017-02-01")
         */
        private String formatDate(String dateString) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
            Date dateObject = null;
            try {
                dateObject = simpleDateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.format(dateObject);
            }



        /**
         * date picker açılışında şuanki tarih gösterilir
         */
        private void showDatePicker() {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(getActivity(),
                    this, year, month, dayOfMonth).show();
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

            month = month + 1;

            String selectedDate = year + "-" + month + "-" + dayOfMonth;
            // seçilen data string "2017-2-1" 'dan bu formata çeviriliyor "2017-02-01")
            String formattedDate = formatDate(selectedDate);

            // seçilen date depolanıyor
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.settings_from_date_key), formattedDate).apply();

            // değiştirildikten sonra displayde görüntüleme işlemi yapılıyor
            Preference fromDatePreference = findPreference(getString(R.string.settings_from_date_key));
            bindPreferenceSummaryToValue(fromDatePreference);
        }



    }
}


