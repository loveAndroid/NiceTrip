package com.nicerip.freetrip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Kongxs
 */
public class DrawableDel {

	private static final String prefix = "res\\";
	
	private static final String RES_DRAWABLE = "drawable";
	private static final String RES_ANIM = "anim";
	
	// F:\kong\FreeTrip\del.txt
	public static void main(String[] args) {
		
		if (args != null) {
			
			String path = args[0];
			if(args.length > 1){
				for (int i=1;i<args.length;i++){
					
					String type = args[i];
					
					String target = prefix;
					
					if(RES_DRAWABLE.equals(type)){
						target += RES_DRAWABLE;
						exec(path, target);
					} else if (RES_ANIM.equals(type)){
						target += RES_ANIM;
						exec(path, target);
					} else {
						print("invalid type to del . ..");
					}
				}
			} else {
				exec(path, prefix + RES_DRAWABLE);
			}
			
		} else {
			print("must input a file path!");
		}
	}

	private static void exec(String path,String type) {
		if (path != null) {
			File f = new File(path);
			if (f.exists()) {

				List<File> deleteTargetImages = listTargetImageDirs(f,type);

				if (deleteTargetImages != null && deleteTargetImages.size() > 0) {
					print("total " + deleteTargetImages.size()
							+ " find ,file will to be del");
					for (File file : deleteTargetImages) {
						if (file.exists()) {
							boolean delete = file.delete();
							if(delete){
								print(file.getAbsolutePath()
										+ " is del...");
							} else {
								print(file.getAbsolutePath()
										+ " del failed...");
							}
						}
					}
					print("file is del done!");
				} else {
					print("have no file to del!");
				}

			} else {
				print("invalid file dir ,try again");
			}
		} else {
			print("please input a file dir,contains the dir what is you want to del!");
		}
	}

	static List<File> listTargetImageDirs(File f, String type) {

		print("**********************************************");
		print("start to load file , wait ...");
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		List<File> listDirs = new ArrayList<>();

		BufferedReader br = null;
		try {
			FileInputStream fis = new FileInputStream(f);

			InputStreamReader reader = new InputStreamReader(fis);

			br = new BufferedReader(reader);

			String msg;
			while (null != (msg = br.readLine())) {

				if (msg != null && msg.contains(type)) {

					String[] split = msg.trim().split("\\\\");

					if (split != null && split.length > 0) {

						File file = new File(msg.trim());
						if (file != null && file.exists()) {
							listDirs.add(file);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			print("word : load file finish ...");
		}
		return listDirs;
	}
	
	static void print(String msg){
		System.out.println(msg);
	}

}