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
		//�ļ���
		String fileName = file.getOriginalFilename();
		//��չ����.�Ժ� �����ݣ�������.=>+1��ȥ��.
		String fileExtensionName = fileName.substring(fileName.lastIndexOf("."));
		String uploadFileName = UUID.randomUUID().toString()+fileExtensionName;
		
		logger.info("��ʼ�ϴ��ļ����ϴ��ļ����ļ���Ϊ:{},�ϴ�·��:{},���ļ���:{}",fileName,path,uploadFileName);
		
		File fileDir = new File(path);
		if(!fileDir.exists()) {
			fileDir.setWritable(true);
			//�������Ŀ¼ 
			fileDir.mkdirs();
		}
		File targetFile = new File(path,uploadFileName);
		try {
			file.transferTo(targetFile);
			//�ļ��ϴ��ɹ�
			
			//todo ��uploadFileName�ϴ���ftp��������
			//FTPUtil.uploadFile(Lists.newArrayList(targetFile));
			
			//�ϴ��Ժ�upload������ļ�ɾ��
			//targetFile.delete();
			
			
			//targetFile.delete();
			
		} catch (IllegalStateException | IOException e) {
			
			logger.error("�ļ��ϴ��쳣",e);
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
							return ServerResponse.createByErrorMessage("������������100");
						}
					}
				}
				logger.info("count"+count);
				return ServerResponse.createBySuccess(resume);
			} catch (IOException e) {
				logger.error("�ļ���ȡ����", e);
				return ServerResponse.createByErrorMessage("�ļ���ȡ����");
			}
			
		} catch (FileNotFoundException e) {
			
			logger.error("�ļ��޷���ȡ", e);
			return ServerResponse.createByErrorMessage("�ļ���ȡʧ��");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
