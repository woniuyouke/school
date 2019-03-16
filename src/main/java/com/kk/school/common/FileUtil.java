package com.kk.school.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@Service
public class FileUtil {


    @Value("${kk.sch.path}")
    public String path;

    //base64字符串转化成图片
    public  boolean generateImage(byte[] imgStr,String name)
    {   //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        OutputStream out = null;
        try
        {
            //Base64解码
            byte[] b = imgStr;
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            StringBuffer stringBuffer = new StringBuffer(path).append(File.separator).append(name).append(".jpeg");
            String imagePath = stringBuffer.toString();
            log.info("imagePath:"+imagePath);
            File file = new File(imagePath);
            if(!file.exists()){
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            out.write(b);
            out.flush();
            return true;
        }
        catch (Exception e)
        {
            log.error("生成图片失败",e);
            return false;
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("关闭流失败",e);
                }
            }
        }
    }


}
