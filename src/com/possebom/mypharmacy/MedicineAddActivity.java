package com.possebom.mypharmacy;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;

import com.possebom.mypharmacy.GetMedicine.GetMedicineListener;
import com.possebom.mypharmacy.SendMedicine.SetMedicineListener;
import com.possebom.mypharmacy.model.Medicine;

public class MedicineAddActivity extends Activity implements GetMedicineListener,SetMedicineListener {

	private static final String BARCODE_URL = "play://search?q=pname:com.google.zxing.client.android";

	private AutoCompleteTextView autoCompleteTextViewForm;
	private EditText	editTextBarcode;
	private EditText	editTextName;
	private EditText	editTextDrug;
	private EditText	editTextConcentration;
	private EditText	editTextLaboratory;
	private ProgressDialog progressDialog;
	private DatePicker datePickerValidity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medicine_add);


		getActionBar().setDisplayHomeAsUpEnabled(true);

		editTextBarcode = (EditText) findViewById(R.id.editTextBarcode);

		editTextName = (EditText) findViewById(R.id.editTextName);
		editTextDrug = (EditText) findViewById(R.id.editTextDrug);
		editTextConcentration = (EditText) findViewById(R.id.editTextConcentration);
		editTextLaboratory = (EditText) findViewById(R.id.editTextLaboratory);
		datePickerValidity = (DatePicker) findViewById(R.id.datePickerValidity);
		
		datePickerValidity.setMinDate(System.currentTimeMillis() - 1000);
		
		findAndHideField(datePickerValidity, "mDaySpinner");

		progressDialog = new ProgressDialog(this);

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
				progressDialog.setMessage(getString(R.string.finding));
				new GetMedicine(progressDialog,this,contents,getResources().getConfiguration().locale.getCountry()).execute();
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
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, MedicineListActivity.class));
			return true;
		case R.id.menu_barcode:
			getBarcode();
			return true;
		case R.id.menu_save :
			if(isMedicineOk()){
				progressDialog.setMessage(getString(R.string.saving));
				new SendMedicine(getApplicationContext(),this,progressDialog,fillMedicine()).execute();
			}else{
				showAlertMedicineIsIncomplete();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private Medicine fillMedicine(){
		Medicine medicine = new Medicine();
		medicine.setBrandName(editTextName.getText().toString());
		medicine.setDrug(editTextDrug.getText().toString());
		medicine.setConcentration(editTextConcentration.getText().toString());
		medicine.setForm(autoCompleteTextViewForm.getText().toString());
		medicine.setLaboratory(editTextLaboratory.getText().toString());
		medicine.setYear(datePickerValidity.getYear());
		medicine.setMonth(datePickerValidity.getMonth());
		medicine.setBarcode(editTextBarcode.getText().toString());
		medicine.setCountry(getResources().getConfiguration().locale.getCountry()); 
		return medicine;
	}

	@Override
	public void onRemoteCallComplete(Medicine medicine) {
		editTextName.setText(medicine.getBrandName());
		editTextDrug.setText(medicine.getDrug());
		editTextConcentration.setText(medicine.getConcentration());
		editTextLaboratory.setText(medicine.getLaboratory());
		autoCompleteTextViewForm.setText(medicine.getForm());
	}

	@Override
	public void onSaveCallComplete() {
		finish();
	}

	private void getBarcode(){
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			startActivityForResult(intent, 0);
		} catch (Exception e) {
			showAlertBarcode();
		}
	}

	private void showAlertBarcode(){
		new AlertDialog.Builder(this)
		.setTitle(R.string.title_alert_barcodescanner)
		.setMessage(R.string.body_alert_barcodescanner)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BARCODE_URL));
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
				}
			}
		})
		.setNegativeButton(android.R.string.cancel, null).show();
	}

	private boolean isMedicineOk(){
		Medicine medicine = fillMedicine();
		if(medicine.getBrandName().trim().length() == 0 || medicine.getMonth() == 0 || medicine.getYear() == 0)
			return false;
		return true;
	}

	private void showAlertMedicineIsIncomplete(){
		new AlertDialog.Builder(this)
		.setTitle(R.string.alert_title_medicine)
		.setMessage(R.string.alert_body_medicine)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setNeutralButton(android.R.string.ok, null).show();
	}
	
	//Code from http://stackoverflow.com/a/10796558
	private void findAndHideField(DatePicker datepicker, String name) {
	    try {
	        Field field = DatePicker.class.getDeclaredField(name);
	        field.setAccessible(true);
	        View fieldInstance = (View) field.get(datepicker);
	        fieldInstance.setVisibility(View.GONE);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}
