package com.possebom.mymedicines;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.possebom.mymedicines.dao.MedicineDao;
import com.possebom.mymedicines.model.Medicine;


public class SendMedicine extends AsyncTask<Void, Void, Void> {
	private static final String	TAG	= "MEDICINE";
	private Medicine medicine;
	private Context context;
	private SetMedicineListener listener;

	public SendMedicine(Context context, SetMedicineListener listener, Medicine medicine) {
		this.medicine = medicine;
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		listener.onSaveCallComplete();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		MedicineDao.getInstance(context).insert(medicine);
		if(medicine.getBarcode().trim().length() > 0){
			sendData(medicine);
		}
		return null;
	}

	private void sendData(Medicine medicine){
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://possebom.com/android/mymedicines/addMedicine.php");
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("brandName", medicine.getBrandName()));
			nameValuePairs.add(new BasicNameValuePair("drug", medicine.getDrug()));
			nameValuePairs.add(new BasicNameValuePair("laboratory", medicine.getLaboratory()));
			nameValuePairs.add(new BasicNameValuePair("concentration", medicine.getConcentration()));
			nameValuePairs.add(new BasicNameValuePair("form", medicine.getForm()));
			nameValuePairs.add(new BasicNameValuePair("country", medicine.getCountry()));
			nameValuePairs.add(new BasicNameValuePair("barcode", medicine.getBarcode()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			Log.d(TAG, "Sending medicine : "+ response.getStatusLine());
		} catch (ClientProtocolException e) {
			Log.d(TAG, "Error Sending medicine : "+ e.getLocalizedMessage());
		} catch (IOException e) {
			Log.d(TAG, "Error Sending medicine : "+ e.getLocalizedMessage());
		}
	}

	public interface SetMedicineListener {
        public void onSaveCallComplete();
    }
}
