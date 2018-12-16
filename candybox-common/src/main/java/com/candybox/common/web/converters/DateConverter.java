package com.candybox.common.web.converters;

import com.candybox.common.utils.DateUtils;
import com.candybox.common.utils.DateUtils.DateTimeFormatters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.util.Date;

public class DateConverter implements Converter<String, Date> {
    private Logger LOGGER = LoggerFactory.getLogger(DateConverter.class);
    @Override
    public Date convert(String source) {
        if(StringUtils.isEmpty(source) || StringUtils.isEmpty(source.trim())){
            LOGGER.info("非法数据[{}]转为日期数据",source);
            return null;
        }
        if(source.matches("\\d{1,2}:\\d{1,2}$")){
            return DateUtils.str2Date(source , DateTimeFormatters.HH_MM);
        }else if(source.matches("^\\d{4}-\\d{1,2}$")){
            return DateUtils.str2Date(source , DateTimeFormatters.YYYY_MM);
        }else if(source.matches("^\\d{4}年\\d{1,2}月$")){
            return DateUtils.str2Date(source , DateTimeFormatters.ZH_YYYY_MM);
        }else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")){
            return DateUtils.str2Date(source , DateTimeFormatters.YYYY_MM_DD);
        }else if(source.matches("^\\d{4}年\\d{1,2}月\\d{1,2}日$")){
            return DateUtils.str2Date(source , DateTimeFormatters.ZH_YYYY_MM_DD);
        }else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")){
            return DateUtils.str2DateTime(source , DateTimeFormatters.YYYY_MM_DD_HH_MM);
        }else if(source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")){
            return DateUtils.str2DateTime(source , DateTimeFormatters.YYYY_MM_DD_HH_MM_SS);
        }else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }


}
