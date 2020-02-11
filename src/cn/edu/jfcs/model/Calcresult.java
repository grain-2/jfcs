package cn.edu.jfcs.model;

public class Calcresult {
	private int id;

	private int nian;

	private String unitid;

	private float ui;

	private float pi;

	private float ci;

	private float ryjf;

	private float zhywf;

	private TeachUnit teachunit;

	public float getCi() {
		return ci;
	}

	public void setCi(float ci) {
		this.ci = ci;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNian() {
		return nian;
	}

	public void setNian(int nian) {
		this.nian = nian;
	}

	public String getUnitid() {
		return unitid;
	}

	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}

	public float getPi() {
		return pi;
	}

	public void setPi(float pi) {
		this.pi = pi;
	}

	public float getRyjf() {
		return ryjf;
	}

	public void setRyjf(float ryjf) {
		this.ryjf = ryjf;
	}

	public float getUi() {
		return ui;
	}

	public void setUi(float ui) {
		this.ui = ui;
	}

	public TeachUnit getTeachunit() {
		return teachunit;
	}

	public void setTeachunit(TeachUnit teachunit) {
		this.teachunit = teachunit;
	}

	public float getZhywf() {
		return zhywf;
	}

	public void setZhywf(float zhywf) {
		this.zhywf = zhywf;
	}

}