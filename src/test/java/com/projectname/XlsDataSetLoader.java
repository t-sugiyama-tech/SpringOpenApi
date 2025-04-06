package com.projectname;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.springframework.core.io.Resource;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;

public class XlsDataSetLoader extends AbstractDataSetLoader {

	@Override
	protected final IDataSet createDataSet(final Resource resource) throws Exception {
		
		try (InputStream inputStream = resource.getInputStream()) {
			IDataSet iDataSet = new XlsDataSet(inputStream);
			return replacementDataset(iDataSet);		
		}
	}
	
	private ReplacementDataSet replacementDataset(final IDataSet iDataSet) {
		
		// 置換後のデータセット
		ReplacementDataSet replacementDataSet =new ReplacementDataSet(iDataSet);
		
		DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
	    LocalDateTime ldt = LocalDateTime.now();
		replacementDataSet.addReplacementObject("[NOW_DATE_TIME]", ldt.format(datetime)); // 置換対象と置換後の値を設定
		
		return replacementDataSet;
	}
	
}
