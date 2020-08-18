package resources;

import java.io.File;

public class Command {
	
	public String name;
	public File cmd;
	
	public Command(String name, File cmd) {
		this.name = name;
		this.cmd = cmd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getCmd() {
		return cmd;
	}

	public void setCmd(File cmd) {
		this.cmd = cmd;
	}
	
	public String toString() {
		return name + " @ " + cmd;
	}
	
	public void execute() {
		 
	}
	
}
