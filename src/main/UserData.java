package main;

/**
 * the User Account information including:
 * a user name, a passCode, &
 * the signature number that the user enrolled--
 * the successfully verified signatures are possible to enrolled as updated templete.
 * This class serves as a data structure.
 * @author 
 *
 */
public class UserData {

	String userName;
	String passCode;
	int sigNum;


	public UserData(String userName, String passCode, int sigNum){
		this.userName = userName;
		this.passCode = passCode;
		this.sigNum = sigNum;		
	}

	public UserData() {
		this.userName = "Name";
		this.passCode = "passCode";
		this.sigNum = 0;		
	}

	@Override
	public String toString(){
		String s = "";
		s = userName + "--" + passCode + "--" + sigNum + "\n";
		return s;
	}


}
