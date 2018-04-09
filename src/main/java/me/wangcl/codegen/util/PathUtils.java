package me.wangcl.codegen.util;

import java.io.File;
import java.io.IOException;

public class PathUtils {
	/**
	 * 如果文件夹不存在，创建文件夹。
	 *
	 * @param path 文件夹路径
	 */
	public static void makeDirectory(String path) {
		if (!path.endsWith("/")) {
			path += "/";
		}

		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		} else if (!file.isDirectory()) {
			file.mkdir();
		}
	}

	/**
	 * 删除指定文件夹，文件夹必须为空。
	 *
	 * @param path 文件夹路径
	 */
	public static void deleteDirectory(String path) {
		File file = new File(path);
		deleteDirectory(file);
	}

	public static void deleteDirectory(File file) {
		if (file.exists() && file.isDirectory()) {
			do {
				file.delete();
				file = file.getParentFile();
			}
			while (file.getParentFile() != null);
		}
	}

	/**
	 * 如果文件不存在，创建文件。
	 *
	 * @param path 文件路径
	 */
	public static File makeFile(String path) {
		File file = new File(path);
		try {
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
}
