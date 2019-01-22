package controller;

import model_class.Account;
import model_class.Customer;
import dao.AccountDao;
import dao.AccountDaoImpl;
import utility.Hashing.CannotPerformOperationException;
import view.AccountView;

public class AccountController extends Controller {
	AccountView accountView = new AccountView();
	private AccountDao accountDaoImpl = new AccountDaoImpl();
	CustomerController customerController = new CustomerController();

	@Override
	public void runController() {
		int keuze = 1;
		Controller.newView = true;
		do {
			if (Controller.newView == true) {
				accountView.ClearTerminal();
				accountView.PrintMenuHeader();
				accountView.PrintMenuOptions();
				Controller.newView = false;
			}
			keuze = accountView.RequestMenuOption();

			switch (keuze) {
			case 1:
				CreateAccount();
				requestNewMenu();
				break;
			case 2:
				ChangeEmail();
				requestNewMenu();
				break;
			case 3:
				ChangePassword();
				requestNewMenu();
				break;
			case 4:
				DeleteAccount();
				requestNewMenu();
			case 9:
				keuze = 0;
				Controller.newView = true;
				break;
			default:
				accountView.InvalidInput();
				break;
			}
		} while (keuze != 0);
	}

	private void DeleteAccount() {
		if (workerOrAdminPermission() == false) {
			if (accountView.confirmDeleteAccount() == true)
				accountDaoImpl.deleteAccount(LoginController.loggedInAccount
						.getId());
			System.exit(0);
		}
		if (workerOrAdminPermission() == true) {
			Customer customer = customerController.ChoosePersonFromList();
			Account account = accountDaoImpl.readAccountByCustomerId(customer
					.getId());
			if (account.equals(LoginController.loggedInAccount)) {
				if (accountView.confirmDeleteAccount() == true) {
					accountDaoImpl.deleteAccount(account.getId());
					System.exit(0);
				}
			} 
			else {
				accountDaoImpl.deleteAccount(account.getId());
				accountView.accountDeleted();
			}
			return;
		}
	}

	public void CreateAccount() {
		if (adminPermission() == false) {
			return;
		}
		Customer customer = customerController.ChoosePersonFromList();
		if (customer == null) {
			return;
		}
		if (accountDaoImpl.readAccountByCustomerId(customer.getId()).getId() != 0) {
			accountView.PersonAlreadyHasAccount();
			return;
		}
		Account account = new Account();
		account.setCustomer(customer);
		account.setEmail(accountView.RequestInputUsername());
		String password = accountView.RequestInputPassword();
		account.setPassword(password);
		String hash = null;
		try {
			hash = utility.Hashing.createHash(password);
		} catch (CannotPerformOperationException e) {
			e.printStackTrace();
		}
		account.setHash(hash);
		int accountTypeId = accountView.requestAccountType();
		account.setAccountTypeId(accountTypeId);
		accountDaoImpl.createAccount(account);
		accountView.accountCreated();
		Controller.newView = true;
	}

	public void ChangePassword() {
		Customer customer = customerController.ChoosePersonFromList();
		if (customer == null) {
			return;
		}
		Account account = accountDaoImpl.readAccountByCustomerId(customer
				.getId());
		if (account.getId() == 0) {
			accountView.NoAccountFound();
			return;
		}
		account.setPassword(accountView.RequestInputPassword());
		accountDaoImpl.updateAccount(account);
		accountView.PasswordChanged();
	}

	public void ChangeEmail() {
		Customer customer = customerController.ChoosePersonFromList();
		if (customer == null) {
			return;
		}
		Account account = accountDaoImpl.readAccountByCustomerId(customer
				.getId());
		if (account.getId() == 0) {
			accountView.NoAccountFound();
			return;
		}
		account.setEmail(accountView.RequestInputUsername());
		accountDaoImpl.updateAccount(account);
		accountView.emailChanged();
	}

}
