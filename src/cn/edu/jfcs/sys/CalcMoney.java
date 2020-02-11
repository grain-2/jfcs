/*
 *编写者：陈冈
 *高校经费测算系统--测算经费
 *编写时间：2006-11-27
 */
package cn.edu.jfcs.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.program.Program;
import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import cn.edu.jfcs.model.PubData;
import cn.edu.jfcs.model.YearTeachUnit;

public class CalcMoney {
	private String outfile = null;

	public CalcMoney(int curYear) {
		calc(curYear);
	}

	// 经费测算
	private void calc(final int curYear) {
		ProgressMonitorDialog pmd = new ProgressMonitorDialog(null);
		IRunnableWithProgress rwp = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				monitor.beginTask("正在计算经费。。。", IProgressMonitor.UNKNOWN);
				Session session = HibernateSessionFactory
						.getSession("hibernate_derby.cfg.xml");
				monitor.subTask("计算经费明细。。。");
				// 计算∑Si、∑S1i、∑S2i
				Query query = session
						.getNamedQuery("cn.edu.jfcs.sys.CalcMoney.calc.getUPCi");
				query.setInteger(0, curYear);
				ScrollableResults result = query.setCacheMode(CacheMode.IGNORE)
						.scroll(ScrollMode.SCROLL_SENSITIVE);
				float[] total = new float[3];
				total[0] = 0;
				total[1] = 0;
				total[2] = 0;
				int i = 0;
				while (result.next()) {
					total[0] += result.getBigDecimal(1).floatValue();
					total[1] += result.getBigDecimal(2).floatValue();
					total[2] += result.getBigDecimal(3).floatValue();
					i++;
				}
				// 获取各个教学单位的Si、S1i、S2i
				String[][] upci = new String[i][4];
				int j = 0;
				result.beforeFirst();
				while (result.next()) {
					upci[j][0] = result.getString(0);
					upci[j][1] = result.getBigDecimal(1).toString();
					upci[j][2] = result.getBigDecimal(2).toString();
					upci[j][3] = result.getBigDecimal(3).toString();
					j++;
				}
				// 获取公共参数
				query = session
						.getNamedQuery("cn.edu.jfcs.sys.CalcMoney.calc.getPubData");
				query.setInteger(0, curYear);
				result = query.setCacheMode(CacheMode.IGNORE).scroll(
						ScrollMode.FORWARD_ONLY);
				PubData pubData = null;
				if (result.next()) {
					pubData = (PubData) result.get(0);
				}
				// 获取各个教学单位的数据
				query = session
						.getNamedQuery("cn.edu.jfcs.sys.CalcMoney.calc.getYearTeachUnit");
				query.setInteger(0, curYear);
				result = query.setCacheMode(CacheMode.IGNORE).scroll(
						ScrollMode.FORWARD_ONLY);
				List<YearTeachUnit> list = new ArrayList<YearTeachUnit>();
				while (result.next()) {
					YearTeachUnit yearTeachUnit = (YearTeachUnit) result.get(0);
					list.add(yearTeachUnit);
				}
				// 标准学生数Si、综合学生数S1i、公共课标准人·学时数S2i、学生经费Ui、专业培养费预算额Pi、公共课经费Ci
				// 专业培养费实际下拨P1i、缴费率、总下拨经费预算额MT、总下拨经费实际下拨MT1
				float jxgl = 0, jxyw = 0, jxyj = 0, szpy = 0, jcj = 0, xsknbz = 0, xshdjf = 0, xsjxj = 0;
				String[][] calcnum = new String[i][19];
				DecimalFormat format = new DecimalFormat(",##0.00");
				for (int k = 0; k < upci.length; k++) {
					// 下标与Excel报表的列对应
					calcnum[k][1] = upci[k][1]; // Si
					calcnum[k][2] = upci[k][2]; // S1i
					calcnum[k][3] = upci[k][3]; // S2i
					// 计算Ui、Pi、Ci。数据库存放的Uper、PPer、CPer不是百分数，故需要除以100,下同
					for (int s = 0; s < list.size(); s++) {
						if (list.get(s).getTeachunit().getUnitid().trim()
								.equals(upci[k][0])) {
							float ui, pi, ci;
							if (list.get(s).getHaszyk().equals("1")) {
								ui = (pubData.getUper().floatValue()
										* pubData.getMt().floatValue() / 100)
										* Float.parseFloat(upci[k][1])
										/ total[0];
								pi = (pubData.getPper().floatValue()
										* pubData.getMt().floatValue() / 100)
										* Float.parseFloat(upci[k][2])
										/ total[1];
							} else {
								ui = 0;
								pi = 0;
							}
							calcnum[k][5] = format.format(ui);
							calcnum[k][6] = format.format(pi);
							if (list.get(s).getHasggk().equals("1"))
								ci = (pubData.getCper().floatValue()
										* pubData.getMt().floatValue() / 100)
										* Float.parseFloat(upci[k][3])
										/ total[2];
							else
								ci = 0;
							calcnum[k][8] = format.format(ci);
							calcnum[k][0] = list.get(s).getTeachunit()
									.getUnitname();
							int ssb = list.get(s).getSsb(); // 生师比
							float jfys = list.get(s).getJfys().floatValue();// 应收金额
							float jfss = list.get(s).getJfss().floatValue();// 实收金额
							float sjf = pubData.getSjf().floatValue() / 100;
							// 定额综合学生人数
							float Mi = list.get(s).getTa1()
									* pubData.getTb1().floatValue();
							Mi += list.get(s).getTa2()
									* pubData.getTb2().floatValue();
							Mi += list.get(s).getTa3()
									* pubData.getTb3().floatValue();
							Mi += list.get(s).getTa4()
									* pubData.getTb4().floatValue();
							Mi += list.get(s).getTa5()
									* pubData.getTb5().floatValue();
							Mi += list.get(s).getTa6()
									* pubData.getTb6().floatValue();
							Mi += list.get(s).getTa7()
									* pubData.getTb7().floatValue();
							Mi += list.get(s).getTa8()
									* pubData.getTb8().floatValue();
							Mi *= ssb;
							float p1i = pi * Mi / Float.parseFloat(upci[k][2]);
							calcnum[k][7] = format.format(p1i); // P1i
							calcnum[k][4] = format.format((100 * jfss / jfys)); // 教学单位缴费率
							float mt1i = (ui + p1i)
									* (jfys / jfss)
									/ sjf
									+ ci
									* (pubData.getRte().floatValue() / pubData
											.getMte().floatValue()) / sjf;
							calcnum[k][10] = format.format(mt1i);
							// 教学管理费、教学业务费、教学研究费、师资培养费、教师奖酬金、学生困难补助
							// 学生活动经费、学生奖学金
							jxgl = list.get(s).getJxglper().floatValue()
									* (ui + pi + ci) / 100;
							jxyw = list.get(s).getJxywper().floatValue()
									* (ui + pi + ci) / 100;
							jxyj = list.get(s).getJxyjper().floatValue()
									* (ui + pi + ci) / 100;
							szpy = list.get(s).getSzpyper().floatValue()
									* (ui + pi + ci) / 100;
							jcj = pubData.getJcjper().floatValue()
									* (ui + pi + ci) / 100;
							xsknbz = pubData.getXsknbzper().floatValue()
									* (ui + pi + ci) / 100;
							xshdjf = pubData.getXshdjfper().floatValue()
									* (ui + pi + ci) / 100;
							xsjxj = pubData.getXsjxjper().floatValue()
									* (ui + pi + ci) / 100;
							calcnum[k][9] = format.format(ui + pi + ci); // MT
							calcnum[k][11] = format.format(jcj);
							calcnum[k][12] = format.format(xsknbz);
							calcnum[k][13] = format.format(xshdjf);
							calcnum[k][14] = format.format(xsjxj);
							calcnum[k][15] = format.format(jxgl);
							calcnum[k][16] = format.format(jxyw);
							calcnum[k][17] = format.format(jxyj);
							calcnum[k][18] = format.format(szpy);
							monitor.subTask("保存计算结果。。。");
							saveCalcResult(session, curYear, list.get(s)
									.getTeachunit().getUnitid().trim(), ui, pi,
									ci, jcj + xsknbz + xshdjf + xsjxj, jxyw
											+ jxgl + jxyj + szpy);
							break;
						}
					}
				}
				HibernateSessionFactory.closeSession();
				monitor.subTask("写入Excel报表。。。");
				outfile = writeExcel(calcnum, curYear, curYear + "年教学单位切块经费测算表");
				monitor.done();
			}
		};
		try {
			pmd.run(true, false, rwp);
		} catch (Exception e) {
		}
		// 打开Excel文件
		if (outfile != null)
			Program.launch(outfile);
	}

	private void saveCalcResult(Session session, int curYear,
			final String unitid, float ui, float pi, float ci, float ryjf,
			float zhywf) {
		Query query = session
				.getNamedQuery("cn.edu.jfcs.sys.CalcMoney.saveCalcResult.updateCalcresult");
		query.setFloat(0, ui);
		query.setFloat(1, pi);
		query.setFloat(2, ci);
		query.setFloat(3, ryjf);
		query.setFloat(4, zhywf);
		query.setInteger(5, curYear);
		query.setString(6, unitid);
		Transaction tx = session.beginTransaction();
		query.executeUpdate();
		tx.commit();
	}

	// 数据写入Excel文件
	private String writeExcel(String[][] calcResult, int curYear, String caption) {
		String outpath = null;
		try {
			String path = FileLocator.toFileURL(
					Platform.getBundle(IAppConstants.APPLICATION_ID).getEntry("")).getPath()
					.toString()
					+ "reports/";
			String xlsFile = path + curYear + ".xls";
			File model = new File(path + "reportModel.xls");
			File out = new File(xlsFile);
			outpath = path.substring(1) + curYear + ".xls";
			// 拷贝model文件
			FileInputStream fis = new FileInputStream(model);
			FileOutputStream fos = new FileOutputStream(out);
			byte[] bytes = new byte[1024];
			int c;
			while ((c = fis.read(bytes)) != -1)
				fos.write(bytes, 0, c);
			fis.close();
			fos.close();
			model = null;
			out = null;
			// Excel写入处理
			FileInputStream fileInput = new FileInputStream(xlsFile);
			HSSFWorkbook workbook = new HSSFWorkbook(fileInput);
			HSSFSheet sheet = workbook.getSheetAt(0);
			// 设置单元格标题的字体、字号、加粗
			HSSFFont font = workbook.createFont();
			font.setFontName("隶书");
			font.setFontHeightInPoints((short) 22);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			// 设置标题文字
			HSSFRow row = sheet.getRow(0);
			HSSFCell cell = row.getCell(0);
			cell.setCellStyle(cellStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(caption);
			HSSFCellStyle dataCellStyle = workbook.createCellStyle();
			dataCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			dataCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			dataCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
			dataCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			dataCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			// 将测算结果写入Excel
			for (int i = 0; i < calcResult.length; i++) {
				for (int j = 0; j < calcResult[i].length; j++) {
					sheet.createRow(i + 4);
					HSSFRow row2 = sheet.getRow(i + 4);
					HSSFCell cell2 = row2.createCell((short) j);
					cell2.setCellStyle(dataCellStyle);
//					cell2.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell2.setCellValue(calcResult[i][j]);
				}
			}
			// 写入文件
			FileOutputStream fileOutput = new FileOutputStream(xlsFile);
			workbook.write(fileOutput);
			fileOutput.flush();
			fileOutput.close();
			fileInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outpath;
	}
}
