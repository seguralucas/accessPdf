package test;

public class Test {

	int n(Object o){
		return 1;
	}
	int n(String a){
		return 2;
	}
	public static void main(String[] args) {
		Test t = new Test();
		System.out.println(t.n("a"));
		System.out.println(t.n(new Object()));
	}

}
