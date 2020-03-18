package cn.zg.service;

public class Test {
	
	public static void main(String[] args) {
		
		String name = "[[李玲, 李涛, 李军, 李丽";
		name = name.replaceAll("^\\[|\\]$", "");
		
		System.out.println("name: "+name);
	}
}
