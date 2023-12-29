package minijavacompiler.virtual_machine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VMModule {

	public static void main(String[] args){
		try{
			Process vmProcess = getVMProcess(args[0]);
			BufferedReader reader = new BufferedReader(new InputStreamReader(vmProcess.getInputStream()));
			String line = reader.readLine();
			while(line != null){
				System.out.println(line);
				line = reader.readLine();
			}
		}catch(IOException exception){
			throw new RuntimeException(exception);
		}
	}

	private static Process getVMProcess(String sourceFilePath) throws IOException {
		ProcessBuilder processBuilder;
		if(!System.getProperty("os.name").equals("Linux")){
			processBuilder = getProcessBuilderForWindows(sourceFilePath);
		}else{
			processBuilder = getProcessBuilderForLinux(sourceFilePath);
		}
		processBuilder.redirectErrorStream(true);
		processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
		return processBuilder.start();
	}

	private static ProcessBuilder getProcessBuilderForWindows(String sourceFilePath){
		return new ProcessBuilder(
				"cmd.exe", "/c", "virtual_machine/CeIVM-cei2011.jar " + sourceFilePath);
	}

	private static ProcessBuilder getProcessBuilderForLinux(String sourceFilePath){
		return new ProcessBuilder(
				"java", "-jar", "src/minijavacompiler/virtual_machine/CeIVM-cei2011.jar", sourceFilePath);
	}

}
