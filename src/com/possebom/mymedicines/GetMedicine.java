package com.possebom.mymedicines;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.possebom.mymedicines.model.Medicine;

public class GetMedicine extends AsyncTask<Void, Void, Void> {
	private static final String	TAG	= "MEDICINE";
	private final String barcode;
	private JSONObject json;
	private final String country;
	private final GetMedicineListener listener;

	public GetMedicine(final GetMedicineListener listener, final String barcode, final String country) {
		super();
		this.barcode = barcode;
		this.country = country;
		this.listener = listener;
	}

	@Override
	protected void onPostExecute(final Void result) {
		super.onPostExecute(result);
		final Medicine medicine = new Medicine();
		if(json != null){
			try {
				medicine.setBrandName(json.getString("brandName"));
				medicine.setDrug(json.getString("drug"));
				medicine.setConcentration(json.getString("concentration"));
				medicine.setForm(json.getString("form"));
				medicine.setLaboratory(json.getString("laboratory"));
				medicine.setBarcode(barcode);
				medicine.setCountry(country);
			} catch (Exception e) {
				Log.e(TAG, "Error on json : " + e.toString());
			}
		}
		
		listener.onRemoteCallComplete(medicine);
	}

	@Override
	protected Void doInBackground(final Void... arg0) {
		final HttpClient httpclient = new DefaultHttpClient();
		final ResponseHandler<String> handler = new BasicResponseHandler();
		final HttpGet request = new HttpGet("http://possebom.com/android/mymedicines/getMedicine.php?country="+country+"&barcode="+barcode);
        String result = null;
		try {
			result = new String(httpclient.execute(request, handler).getBytes("ISO-8859-1"),"UTF-8");
			final JSONArray jsonArray = new JSONArray(result);
			json = jsonArray.getJSONObject(0);
			httpclient.getConnectionManager().shutdown();
		} catch (Exception e) {
            Log.e(TAG, "Request URL : " + request.getURI().toString());
			Log.e(TAG, "Error converting result " + e.toString() + " JSON : " + result);
		}
		return null;
	}

	public interface GetMedicineListener {
		public void onRemoteCallComplete(Medicine medicine);
	}

}
