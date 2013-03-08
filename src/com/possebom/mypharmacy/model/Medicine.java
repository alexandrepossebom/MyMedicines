package com.possebom.mypharmacy.model;

public class Medicine {
	private int id;
	private String brandName;
	private String drug;
	private String laboratory;
	private String concentration;
	private String form;
	private int month;
	private int year;
	
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getDrug() {
		return drug;
	}
	public void setDrug(String drug) {
		this.drug = drug;
	}
	public String getLaboratory() {
		return laboratory;
	}
	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}
	public String getConcentration() {
		return concentration;
	}
	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	
	@Override
	public String toString() {
		return getBrandName();
	}

}
