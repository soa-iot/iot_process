package cn.soa.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alibaba.fastjson.JSON;

public class JavaUtils {
	
	/**   
	 * @Title: saveFiles   
	 * @Description:  保存对象到文件 
	 * @return: void        
	 * @throws FileNotFoundException 
	 */  
	public static void saveObjectByJson(String savePath, String fileName, Object o) 
			throws FileNotFoundException {
        String json = JSON.toJSONString(o);
        File file = new File(savePath + File.separator + fileName);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
        	fileParent.mkdirs();
		}
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {           
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
	         // 完毕，关闭所有链接
	         try {
	        	 fileOutputStream.close();
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	     }
    }
	
}
