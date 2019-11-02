package com.grenader.mediawiki;

import org.apache.poi.hssf.usermodel.HSSFRow;

public interface CategoryStrategy {

    String getNewCategoryNameByKeywords(HSSFRow row);
}
