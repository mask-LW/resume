package com.resume.service.Impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.resume.common.ServerResponse;
import com.resume.service.IFileService;


@Service("iFileService")
public class FileService implements IFileService{
	private Logger logger = LoggerFactory.getLogger(FileService.class);
	
	
	
	public String upload(MultipartFile file , String path) {
		//文件名
		String fileName = file.getOriginalFilename();
		//扩展名即.以后 的内容，包括点.=>+1则去掉.
		String fileExtensionName = fileName.substring(fileName.lastIndexOf("."));
		String uploadFileName = UUID.randomUUID().toString()+fileExtensionName;
		
		logger.info("开始上传文件，上传文件的文件名为:{},上传路径:{},新文件名:{}",fileName,path,uploadFileName);
		
		File fileDir = new File(path);
		if(!fileDir.exists()) {
			fileDir.setWritable(true);
			//创建多个目录 
			fileDir.mkdirs();
		}
		File targetFile = new File(path,uploadFileName);
		try {
			file.transferTo(targetFile);
			//文件上传成功
			
			//todo 将uploadFileName上传到ftp服务器上
			//FTPUtil.uploadFile(Lists.newArrayList(targetFile));
			
			//上传以后将upload下面的文件删除
			//targetFile.delete();
			
			
			//targetFile.delete();
			
		} catch (IllegalStateException | IOException e) {
			
			logger.error("文件上传异常",e);
		}
		return targetFile.getName();
	}
	
	
	
	
	public  ServerResponse<String[]> readFile(String path) {
		String[] resume = new String[100];
		Integer count = 0 ;
		try {
			FileReader read = new FileReader(path);
			BufferedReader br = new BufferedReader(read); 
			
			String line ;
			
			try {
				while((line = br.readLine()) != null) {
					if(line != null) {
						if(count<99) {
							count++;
							resume[count] = line.trim();
							logger.info(resume[count]);
						}else {
							resume = null;
							count = 0 ;
							return ServerResponse.createByErrorMessage("简历份数大于100");
						}
					}
				}
				logger.info("count"+count);
				return ServerResponse.createBySuccess(resume);
			} catch (IOException e) {
				logger.error("文件读取错误", e);
				return ServerResponse.createByErrorMessage("文件读取错误");
			}
			
		} catch (FileNotFoundException e) {
			
			logger.error("文件无法读取", e);
			return ServerResponse.createByErrorMessage("文件读取失败");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
