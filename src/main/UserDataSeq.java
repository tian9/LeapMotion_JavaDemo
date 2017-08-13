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
public class UserDataSeq {

	public int sequencialNum;
	String userName;
	String passCode;
	int sigNum;
	UserData userData = new UserData(userName, passCode, sigNum);
	

	public UserDataSeq(int sequencialNum){
		this.userData = getUserData(sequencialNum);
	}
	
	public UserDataSeq(String userName, String passCode, int sigNum){
		this.userData.userName = userName;
		this.userData.passCode = passCode;
		this.userData.sigNum = sigNum;
		sequencialNum = getSequancialNum(userName, passCode, sigNum);
	}
	
	public UserDataSeq(int sequencialNum, String userName, String passCode, int sigNum){
		this.userData.userName = userName;
		this.userData.passCode = passCode;
		this.userData.sigNum = sigNum;
		this.sequencialNum = sequencialNum;
	}
	
	public UserDataSeq(int sequencialNum, UserData ud){
		this.userData.userName = ud.userName;
		this.userData.passCode = ud.passCode;
		this.userData.sigNum = ud.sigNum;
		this.sequencialNum = sequencialNum;
	}
	
	
	public int getSequancialNum(String userName, String passCode, int sigNum){
		return sequencialNum;
	}
	
	public UserData getUserData(int sequancialNum){
		return this.userData;
	}
	
	public void setSequancialNum(int num){
		this.sequencialNum = num;
	}

	@Override
	public String toString(){
		
		String s = "";
		s = sequencialNum + "--" + userData.toString() ;
		return s;
	}


}
