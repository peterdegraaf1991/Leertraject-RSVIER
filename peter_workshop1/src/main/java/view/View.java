package view;

import java.util.concurrent.TimeUnit;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

public class View {
	public TextIO textIO = TextIoFactory.getTextIO();
	TextTerminal<?> terminal = textIO.getTextTerminal();

	public int RequestMenuOption() {
		int option = textIO.newIntInputReader().read(
				"Choose an option from the menu");
		return option;
	}

	public void InvalidInput() {
		terminal.println("Your input is invalid");
	}

	public void ClearTerminal() {
		terminal.printf("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
	
	public void noPermission() {
		terminal.println("You have no permission for this action");	
	}

	public void PrintMenuOptions() {	
	}

	public void PrintMenuHeader() {
	}

	public void requestContinue() {
			char option = textIO.newCharInputReader()
					.withInlinePossibleValues('y')
					.read(
					"\nDo you wish to go back to the menu?");
		}
		
	public void logoutTimer() {
		try {
			terminal.println("Logging out in 3");
			TimeUnit.SECONDS.sleep(1);
			terminal.println("Logging out in 2");
			TimeUnit.SECONDS.sleep(1);
			terminal.println("Logging out in 1");
			TimeUnit.SECONDS.sleep(1);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

