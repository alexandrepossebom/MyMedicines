package com.possebom.mypharmacy;

import com.possebom.mypharmacy.dao.MedicineDao;
import com.possebom.mypharmacy.model.Medicine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class MedicineAddActivity extends Activity{

	private AutoCompleteTextView autoCompleteTextViewForm;
	private EditText	editTextBarcode;
	private EditText	editTextName;
	private EditText	editTextDrug;
	private EditText	editTextConcentration;
	private EditText	editTextLaboratory;
	private EditText	editTextMonth;
	private EditText	editTextYear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicine_add);
		editTextBarcode = (EditText) findViewById(R.id.editTextBarcode);

		editTextName = (EditText) findViewById(R.id.editTextName);
		editTextDrug = (EditText) findViewById(R.id.editTextDrug);
		editTextConcentration = (EditText) findViewById(R.id.editTextConcentration);
		editTextLaboratory = (EditText) findViewById(R.id.editTextLaboratory);
		editTextMonth = (EditText) findViewById(R.id.editTextMonth);
		editTextYear = (EditText) findViewById(R.id.editTextYear);

		//AutoComplete
		autoCompleteTextViewForm = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewForm);
		String[] forms = getResources().getStringArray(R.array.forms_array);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, forms);
		autoCompleteTextViewForm.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = data.getStringExtra("SCAN_RESULT");
				editTextBarcode.setText(contents);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_add_medicine, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_barcode:
			try {
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				startActivityForResult(intent, 0);
			} catch (Exception e) {
				editTextBarcode.setText("Barcode Scanner not installed");
			}
			return true;
		case R.id.menu_save :
			MedicineDao md = new MedicineDao(getApplicationContext());
			Medicine medicine = new Medicine();
			medicine.setBrandName(editTextName.getText().toString());
			medicine.setDrug(editTextDrug.getText().toString());
			medicine.setConcentration(editTextConcentration.getText().toString());
			medicine.setForm(autoCompleteTextViewForm.getText().toString());
			medicine.setLaboratory(editTextLaboratory.getText().toString());
			try {
				medicine.setYear(Integer.parseInt(editTextYear.getText().toString()));
			} catch (Exception e) {
				medicine.setYear(0);
			}
			try{
				medicine.setMonth(Integer.parseInt(editTextMonth.getText().toString()));
			}catch (Exception e) {
				medicine.setMonth(0);
			}
			md.insert(medicine);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
