package com.possebom.mypharmacy;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.possebom.mypharmacy.model.Medicine;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class GetMedicine extends AsyncTask<Void, Void, Void> {
	private static final String	TAG	= "MEDICINE";
	private String barcode;
	private ProgressDialog progressDialog;
	private JSONObject json;
	private String country;
	private GetMedicineListener listener;

	public GetMedicine(ProgressDialog progressDialog,GetMedicineListener listener, String barcode, String country) {
		this.barcode = barcode;
		this.country = country;
		this.listener = listener;
		this.progressDialog = progressDialog;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog.show();
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Medicine medicine = new Medicine();
		
		try {
			medicine.setBrandName(json.getString("brandName"));
			medicine.setDrug(json.getString("drug"));
			medicine.setConcentration(json.getString("concentration"));
			medicine.setForm(json.getString("form"));
			medicine.setLaboratory(json.getString("laboratory"));
		} catch (JSONException e) {
			Log.e(TAG, "Error on json : " + e.toString());
		}

		listener.onRemoteCallComplete(medicine);
		
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}

	@Override
	protected Void doInBackground(Void... arg0) {
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL("http://possebom.com/android/mypharmacy/getMedicine.php?country="+country+"&barcode="+barcode);
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			BufferedInputStream bufferedInput = new BufferedInputStream(in);
			ByteArrayBuffer byteArray = new ByteArrayBuffer(50);
			int current = 0;
			while((current = bufferedInput.read()) != -1){
				byteArray.append((byte)current);
			}				
			String result = new String(byteArray.toByteArray(),"UTF-8");
			JSONArray jsonArray = new JSONArray(result);

			json = jsonArray.getJSONObject(0);

			urlConnection.disconnect();
		} catch (Exception e) {
			json = null;
			Log.e(TAG, "Error converting result " + e.toString());
		}
		return null;
	}
	
	public interface GetMedicineListener {
        public void onRemoteCallComplete(Medicine medicine);
    }

}
