package com.liuxuanhe.common.tess4j;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Tess4jConfiguration {
    @Bean
    public ITesseract tesseract(){
        // 创建Tesseract对象
        ITesseract tesseract = new Tesseract();
        // 设置语言环境（词库）
        tesseract.setDatapath("E:\\MyProject\\tessdata"); // 语言包路径（不包括文件名）
        tesseract.setLanguage("chi_sim"); // 语言包名字
        return tesseract;

    }
}
