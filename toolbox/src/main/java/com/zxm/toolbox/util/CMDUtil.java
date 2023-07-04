package com.zxm.toolbox.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * 执行windows的cmd命令工具类
 * 
 * @author dufei
 *
 */
public class CMDUtil {
	
	/**
	 * 执行一个cmd命令
	 * 
	 * @param cmdCommand cmd命令
	 * @return 命令执行结果字符串，如出现异常返回null
	 */
	public static String executeCMDCommand(String cmdCommand) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			Process process = Runtime.getRuntime().exec(cmdCommand);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
			return stringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 执行bat文件，
	 * 
	 * @param file          bat文件路径
	 * @param isCloseWindow 执行完毕后是否关闭cmd窗口
	 * @return bat文件输出log
	 */
	public static String executeBatFile(String file, boolean isCloseWindow) {
		String cmdCommand;
		if (isCloseWindow) {
			cmdCommand = "cmd.exe /c " + file;
		} else {
			cmdCommand = "cmd.exe /k " + file;
		}
		return executeCMDCommand(cmdCommand);
	}

	/**
	 * 执行bat文件,新开窗口
	 * 
	 * @param file          bat文件路径
	 * @param isCloseWindow 执行完毕后是否关闭cmd窗口
	 * @return bat文件输出log
	 */
	public static String executeBatFileWithNewWindow(String file, boolean isCloseWindow) {
		String cmdCommand;
		if (isCloseWindow) {
			cmdCommand = "cmd.exe /c start" + file;
		} else {
			cmdCommand = "cmd.exe /k start" + file;
		}
		return executeCMDCommand(cmdCommand);
	}

}
