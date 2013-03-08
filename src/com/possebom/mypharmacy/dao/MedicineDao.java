package com.possebom.mypharmacy.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.possebom.mypharmacy.model.Medicine;

public class MedicineDao {
	private SQLiteDatabase db;
	private SQLiteHelper dbHelper; 

	private static final String TABLE = "medicine";
	private static final String SCRIPT_DB_DELETE = "DROP TABLE IF EXISTS medicine";
	private static final String SCRIPT_DB_CREATE =
			"create table medicine (_id integer primary "+
					"key autoincrement, brandName text not null, "+
					"drug text," +
					"laboratory text," +
					"concentration text," +
					"form text," +
					"month integer,"+
					"year integer);";

	public MedicineDao(Context ctx) {
		dbHelper = new SQLiteHelper(ctx,TABLE, 3,SCRIPT_DB_CREATE, SCRIPT_DB_DELETE);
	}

	public void insert(Medicine m){
		ContentValues cv = new ContentValues();
		cv.put("brandName", m.getBrandName());
		cv.put("drug", m.getDrug());
		cv.put("laboratory", m.getLaboratory());
		cv.put("concentration", m.getConcentration());
		cv.put("month", m.getMonth());
		cv.put("year", m.getYear());
		cv.put("form", m.getForm());
		db = dbHelper.getWritableDatabase();
		try{
			db.insertOrThrow(TABLE, null, cv);
		}catch (Exception e) {
			Log.d("MEDICINE","Erro inserindo mensagem : "+ e.getLocalizedMessage());
		}
		db.close();
	}

	public Medicine fillMedicine(Cursor c) {
		Medicine medicine = new Medicine();
		medicine.setId(c.getInt(c.getColumnIndex("_id")));
		medicine.setBrandName(c.getString(c.getColumnIndex("brandName")));
		medicine.setDrug(c.getString(c.getColumnIndex("drug")));
		medicine.setLaboratory(c.getString(c.getColumnIndex("laboratory")));
		medicine.setConcentration(c.getString(c.getColumnIndex("concentration")));
		medicine.setForm(c.getString(c.getColumnIndex("form")));
		medicine.setMonth(c.getInt(c.getColumnIndex("month")));
		medicine.setYear(c.getInt(c.getColumnIndex("year")));
		return medicine;
	}

	public Medicine getMessageById(int id){
		String[] columns = new String[]{"_id", "brandName", "drug", "laboratory", "concentration", "form", "month", "year"};
		String[] args = new String[]{String.valueOf(id)};
		db = dbHelper.getWritableDatabase();
		Cursor c = db.query(TABLE, columns, "_id = ?", args, null, null, null);
		Medicine medicine = null;
		if(c.moveToFirst())
			medicine = fillMedicine(c);
		c.close();
		db.close();
		return medicine;
	}

	public Medicine getMedicineById(int id){
		String[] columns = new String[]{"_id", "brandName", "drug", "laboratory", "concentration", "form", "month", "year"};
		String[] args = new String[]{String.valueOf(id)};
		db = dbHelper.getWritableDatabase();
		Cursor c = db.query(TABLE, columns, "_id = ?", args, null, null, null);
		Medicine medicine = null;
		if(c.moveToFirst())
			medicine = fillMedicine(c);
		c.close();
		db.close();
		return medicine;
	}

	public List<Medicine> getAll(){
		List<Medicine> list = new ArrayList<Medicine>();
		String[] columns = new String[]{"_id", "brandName", "drug", "laboratory", "concentration", "form", "month", "year"};
		db = dbHelper.getWritableDatabase();
		Cursor c = db.query(TABLE, columns,null, null, null, null,"brandName desc");
		c.moveToFirst();
		while(!c.isAfterLast()){
			Medicine message = fillMedicine(c);
			list.add(message);
			c.moveToNext();
		}
		c.close();
		db.close();
		return list;
	}

}
