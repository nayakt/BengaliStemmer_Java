package Stemmers;

import java.util.*;

public class TestBengaliStemmer
{
	public static void main(String[] args)
	{
		BengaliStemmer stemmer = new BengaliStemmer();
		String word = "বলিভিয়ানদের";
		//put a breakpoint here and check the root word. Console may not display unicode characters properly.
		String root = stemmer.Stem(word);
		System.out.println(root);
		System.out.println("Press any key to exit.........");
		new Scanner(System.in).nextLine();
	}
}
