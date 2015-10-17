package Stemmers;

import tangible.RefObject;

public class BengaliStemmer
{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region bengali unicodes

	private static char bn_chandrabindu = (char)0x0981;
	private static char bn_anusvara = (char)0x0982;
	private static char bn_visarga = (char)0x0983;
	private static char bn_a = (char)0x0985;
	private static char bn_aa = (char)0x0986;
	private static char bn_i = (char)0x0987;
	private static char bn_ii = (char)0x0988;
	private static char bn_u = (char)0x0989;
	private static char bn_uu = (char)0x098a;
	private static char bn_ri = (char)0x098b;
	private static char bn_e = (char)0x098f;
	private static char bn_ai = (char)0x0990;
	private static char bn_o = (char)0x0993;
	private static char bn_au = (char)0x0994;
	private static char bn_k = (char)0x0995;
	private static char bn_kh = (char)0x0996;
	private static char bn_g = (char)0x0997;
	private static char bn_gh = (char)0x0998;
	private static char bn_ng = (char)0x0999;
	private static char bn_ch = (char)0x099a;
	private static char bn_chh = (char)0x099b;
	private static char bn_j = (char)0x099c;
	private static char bn_jh = (char)0x099d;
	private static char bn_n = (char)0x099e;
	private static char bn_tt = (char)0x099f;
	private static char bn_tth = (char)0x09a0;
	private static char bn_dd = (char)0x09a1;
	private static char bn_ddh = (char)0x09a2;
	private static char bn_mn = (char)0x09a3;
	private static char bn_t = (char)0x09a4;
	private static char bn_th = (char)0x09a5;
	private static char bn_d = (char)0x09a6;
	private static char bn_dh = (char)0x09a7;
	private static char bn_dn = (char)0x09a8;
	private static char bn_p = (char)0x09aa;
	private static char bn_ph = (char)0x09ab;
	private static char bn_b = (char)0x09ac;
	private static char bn_bh = (char)0x09ad;
	private static char bn_m = (char)0x09ae;
	private static char bn_y = (char)0x09df;
	private static char bn_J = (char)0x09af;
	private static char bn_r = (char)0x09b0;
	private static char bn_l = (char)0x09b2;
	private static char bn_sh = (char)0x09b6;
	private static char bn_ss = (char)0x09b7;
	private static char bn_s = (char)0x09b8;
	private static char bn_h = (char)0x09b9;

	private static char bn_AA = (char)0x09be;
	private static char bn_I = (char)0x09bf;
	private static char bn_II = (char)0x09c0;
	private static char bn_U = (char)0x09c1;
	private static char bn_UU = (char)0x09c2;
	private static char bn_RI = (char)0x09c3;
	private static char bn_E = (char)0x09c7;
	private static char bn_AI = (char)0x09c8;
	private static char bn_O = (char)0x09cb;
	private static char bn_AU = (char)0x09cc;

	private static char bn_hosh = (char)0x09cd;
	private static char bn_nukta = (char)0x09bc;
	private static char bn_virama = (char)0x0964;
	private static char bn_khandata = (char)0x09ce;
	private static char bn_rr = (char)0x09dc;
	private static char bn_rrh = (char)0x09dd;

	private static char bn_zero = (char)0x09e6;
	private static char bn_one = (char)0x09e7;
	private static char bn_two = (char)0x09e8;
	private static char bn_three = (char)0x09e9;
	private static char bn_four = (char)0x09ea;
	private static char bn_five = (char)0x09eb;
	private static char bn_six = (char)0x09ec;
	private static char bn_seven = (char)0x09ed;
	private static char bn_eight = (char)0x09ee;
	private static char bn_nine = (char)0x09ef;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	private char[] swaraBarnas = {bn_AA, bn_E, bn_I, bn_II, bn_U, bn_UU};

	private boolean IsBengaliSwaraBarna(char a)
	{
		for (int i = 0; i < swaraBarnas.length; i++)
		{
			if (a == swaraBarnas[i])
			{
				return true;
			}
		}
		return false;
	}

	/* A common suffix candidate is a sequence of vowels and bn_y. */
	private boolean IsBnCommonSuffix(char a)
	{
		if (a >= bn_AA && a <= bn_AU)
		{
			return true;
		}
		if (a >= bn_aa && a <= bn_au)
		{
			return true;
		}
		return a == bn_y ? true : false;
	}

	private boolean IsBengaliByanjanBarna(char a)
	{
		return a >= bn_k && a <= bn_y;
	}

	// Strip off suffixes "gulo", "guli", "gulote" "gulite"
	private void StripPluralSuffixes(tangible.RefObject<String> word)
	{
		int len = word.argValue.length();
		if (word.argValue.length() <= 6)
		{
			return;
		}

		if (word.argValue.charAt(len - 1) == bn_E && word.argValue.charAt(len - 2) == bn_t)
		{
			word.argValue = word.argValue.substring(0, len - 2);
			len = word.argValue.length();
		}

		if (len <= 6)
		{
			return;
		}

		if (word.argValue.charAt(len - 4) == bn_g && word.argValue.charAt(len - 3) == bn_U && word.argValue.charAt(len - 2) == bn_l && (word.argValue.charAt(len - 1) == bn_O || word.argValue.charAt(len - 1) == bn_I))
		{
			word.argValue = word.argValue.substring(0, len - 4);
		}
	}

	// Strip off suffixes like "rai", "tuku", "shil" "ta" etc.
	private boolean StripCommonSuffixes(tangible.RefObject<String> word, boolean i_removed)
	{
		int newlen = word.argValue.length();
		int len = word.argValue.length();
		do
		{
			if (len <= 4)
			{
				break;
			}

			// Remove 'tta' or 'ta' (only if it is not preceeded with a m or g)
			if (word.argValue.charAt(len - 1) == bn_AA && (word.argValue.charAt(len - 2) == bn_tt || (word.argValue.charAt(len - 2) == bn_t && word.argValue.charAt(len - 3) != bn_m && word.argValue.charAt(len - 3) != bn_g)))
			{
				word.argValue = word.argValue.substring(0, len - 2);
				len = word.argValue.length();
			}

			if (len <= 4)
			{
				break;
			}

			// Remove 'ti' or 'tti'
			if (word.argValue.charAt(len - 1) == bn_I && word.argValue.charAt(len - 2) == bn_tt)
			{
				word.argValue = word.argValue.substring(0, len - 2);
				len = word.argValue.length();
			}

			if (len <= 4)
			{
				break;
			}

			// Remove "ra"  ("rai" has alreday been stemmed to "ra").
			if (word.argValue.charAt(len - 1) == bn_r)
			{
				word.argValue = word.argValue.substring(0, len - 1);
				len = word.argValue.length();

				if (len <= 4)
				{
					break;
				}

				// Remove "-er"
				if (word.argValue.charAt(len - 1) == bn_E)
				{
					int pos = word.argValue.charAt(len - 2) == bn_d ? 2 : 1; // Remove '-der'
					word.argValue = word.argValue.substring(0, len - pos);
					len = word.argValue.length();
				}
			}

			if (len <= 5)
			{
				break;
			}

			// Remove ttai tai ttar or tar
			if ((word.argValue.charAt(len - 1) == bn_y || word.argValue.charAt(len - 1) == bn_r) && word.argValue.charAt(len - 2) == bn_AA && (word.argValue.charAt(len - 3) == bn_tt || word.argValue.charAt(len - 3) == bn_t))
			{
				word.argValue = word.argValue.substring(0, len - 3);
				len = word.argValue.length();
			}
			else if ((word.argValue.charAt(len - 1) == bn_r) && word.argValue.charAt(len - 2) == bn_I && word.argValue.charAt(len - 3) == bn_tt)
			{
				word.argValue = word.argValue.substring(0, len - 3);
				len = word.argValue.length();
			}

			if (len <= 5)
			{
				break;
			}

			if (word.argValue.charAt(len - 1) == bn_E && word.argValue.charAt(len - 2) == bn_k)
			{
				word.argValue = word.argValue.substring(0, len - 2);
				len = word.argValue.length();
			}

			if (len <= 5)
			{
				break;
			}

			// Remove 'shil'
			if (word.argValue.charAt(len - 1) == bn_l && word.argValue.charAt(len - 2) == bn_II && word.argValue.charAt(len - 3) == bn_sh)
			{
				word.argValue = word.argValue.substring(0, len - 3);
				len = word.argValue.length();
			}

			if (len <= 6)
			{
				break;
			}

			// Remove 'tuku'
			if (word.argValue.charAt(len - 1) == bn_U && word.argValue.charAt(len - 2) == bn_k && word.argValue.charAt(len - 3) == bn_U && word.argValue.charAt(len - 4) == bn_tt)
			{
				word.argValue = word.argValue.substring(0, len - 4);
				len = word.argValue.length();
			}

			// Remove 'debi'
			if (len <= 6)
			{
				break;
			}

			if (word.argValue.charAt(len - 1) == bn_II && word.argValue.charAt(len - 2) == bn_b && word.argValue.charAt(len - 3) == bn_E && word.argValue.charAt(len - 4) == bn_d)
			{
				word.argValue = word.argValue.substring(0, len - 4);
				len = word.argValue.length();
			}

			// Remove 'babu'
			if (len <= 6)
			{
				break;
			}

			if (word.argValue.charAt(len - 1) == bn_U && word.argValue.charAt(len - 2) == bn_b && word.argValue.charAt(len - 3) == bn_AA && word.argValue.charAt(len - 4) == bn_b)
			{
				word.argValue = word.argValue.substring(0, len - 4);
				len = word.argValue.length();
			}

			// Remove 'bhai'
			if (len <= 6 || !i_removed)
			{
				break;
			}

			if (word.argValue.charAt(len - 1) == bn_AA && word.argValue.charAt(len - 2) == bn_bh)
			{
				word.argValue = word.argValue.substring(0, len - 2);
				len = word.argValue.length();
			}

			// Remove 'bhabe'
			if (len <= 6)
			{
				break;
			}

			if (word.argValue.charAt(len - 1) == bn_b && word.argValue.charAt(len - 2) == bn_E && word.argValue.charAt(len - 3) == bn_AA && word.argValue.charAt(len - 4) == bn_bh)
			{
				word.argValue = word.argValue.substring(0, len - 4);
				len = word.argValue.length();
			}


		} while (false);

		return newlen != len;
	}

	private boolean SuffixEndingByanjonBarna(char ch)
	{
		return (ch == bn_d || ch == bn_k || ch == bn_tt || ch == bn_t || ch == bn_m);
	}

	private String StemWord(String word, boolean isAggressive)
	{
		int len = word.length();
		int wordlen = len;
		boolean i_removed = false;
		int p;

		String buff = "";
		for (int i = 0; i < len; i++)
		{
			buff += word.charAt(i);
		}

		if (!isAggressive)
		{
			if (len <= 3)
			{
				return buff;
			}
		}

		// Remove trailing okhyor "i" and "o"
		if (buff.charAt(len - 1) == bn_i || buff.charAt(len - 1) == bn_o)
		{
			buff = buff.substring(0, len - 1);
			len = len - 1;
			i_removed = true;
		}

		tangible.RefObject<String> tempRef_buff = new tangible.RefObject<String>(buff);
		boolean tempVar = StripCommonSuffixes(tempRef_buff, i_removed);
			buff = tempRef_buff.argValue;
		while (tempVar)
		{
			i_removed = false;
			tangible.RefObject<String> tempRef_buff2 = new tangible.RefObject<String>(buff);
			tempVar = StripCommonSuffixes(tempRef_buff2, i_removed);
			buff = tempRef_buff2.argValue;
		}

		tangible.RefObject<String> tempRef_buff3 = new tangible.RefObject<String>(buff);
		StripPluralSuffixes(tempRef_buff3);
		buff = tempRef_buff3.argValue;

		if (isAggressive)
		{
			p = buff.length() - 1;
			while (IsBnCommonSuffix(buff.charAt(p)))
			{
				p--;
			}
			if (p - wordlen + 1 >= 2)
			{
				buff = buff.substring(0, p + 1);
			}
		}

		if (buff.charAt(buff.length() - 1) == bn_E)
		{
			buff = buff.substring(0, buff.length() - 1);
		}

		return buff;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IStemmer Members

	public final String Stem(String word)
	{
		return StemWord(word, isAggressive);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	private boolean isAggressive = false;

	public final boolean getIsAggressive()
	{
		return isAggressive;
	}
	public final void setIsAggressive(boolean value)
	{
		isAggressive = value;
	}
}